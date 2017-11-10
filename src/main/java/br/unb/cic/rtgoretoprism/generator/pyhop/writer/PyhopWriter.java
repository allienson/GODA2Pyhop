package br.unb.cic.rtgoretoprism.generator.pyhop.writer;

import java.util.*;
import it.itc.sra.taom4e.model.core.informalcore.Plan;
import it.itc.sra.taom4e.model.core.informalcore.Actor;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.FileObject;

import br.unb.cic.RTRegexBaseVisitor;
import br.unb.cic.RTRegexLexer;
import br.unb.cic.RTRegexParser;
import br.unb.cic.RTRegexParser.GAltContext;
import br.unb.cic.RTRegexParser.GCardContext;
import br.unb.cic.RTRegexParser.GIdContext;
import br.unb.cic.RTRegexParser.GOptContext;
import br.unb.cic.RTRegexParser.GSkipContext;
import br.unb.cic.RTRegexParser.GTimeContext;
import br.unb.cic.RTRegexParser.GTryContext;
import br.unb.cic.RTRegexParser.ParensContext;
import br.unb.cic.RTRegexParser.PrintExprContext;

import br.unb.cic.rtgoretoprism.console.ATCConsole;
import br.unb.cic.rtgoretoprism.generator.CodeGenerationException;
import br.unb.cic.rtgoretoprism.generator.goda.parser.CtxParser;
import br.unb.cic.rtgoretoprism.generator.kl.AgentDefinition;
import br.unb.cic.rtgoretoprism.generator.pyhop.gui.PyhopGUI;
import br.unb.cic.rtgoretoprism.model.ctx.ContextCondition;
import br.unb.cic.rtgoretoprism.model.ctx.CtxSymbols;
import br.unb.cic.rtgoretoprism.model.kl.Const;
import br.unb.cic.rtgoretoprism.model.kl.GoalContainer;
import br.unb.cic.rtgoretoprism.model.kl.PlanContainer;
import br.unb.cic.rtgoretoprism.model.kl.RTContainer;
import br.unb.cic.rtgoretoprism.util.FileUtility;
import br.unb.cic.rtgoretoprism.util.PathLocation;

public class PyhopWriter{
	private AgentDefinition ad;
	private Actor a;
	private PrintWriter file;
	private LinkedList<String> actions = new LinkedList<String>();
	private LinkedList<String> variables = new LinkedList<String>();
	public String filePath = "/home/sabiah/Desktop/dananau-pyhop/goda/goda_problem.py";
	
	public PyhopWriter(AgentDefinition ad, Actor a){
		this.ad = ad;
		this.a = a;
	}

	public void start() throws FileNotFoundException{
		file = createFile(a.getName());
		
		file.print("state.objects = { \\\n");

		initialState(ad.rootlist.getFirst());
		goalState(ad.rootlist.getFirst(), 0);
		
		PyhopGUI dialog =  new PyhopGUI(filePath, variables, actions);
		dialog.renderGUI();
        
		//variables = dialog.getVariables();
        //actions = dialog.getActions();
        
        if(variables.size()!=0){
			for(int i=0; i<variables.size(); i++){
				System.out.println(variables.get(i));
				if(i != variables.size()){
					String var = variables.get(i).concat(", \\\n");
					file.print(var);
				}
			}
		}
        file.print("'" + getName_noRT(ad.rootlist.getFirst().getName()) + "':False}\n\n");
		
        file.print("pyhop(state, [ \\\n");
		if(actions.size()!=0){
			for(int i=0; i<actions.size(); i++){
				System.out.println(actions.get(i));
				if(i != actions.size()){
					String action = actions.get(i).concat(", \\\n");
					file.print(action);
				}
			}
		}
		file.print("], verbose=1)");
		
		file.close();
		
		
	}

	private PrintWriter createFile(String actor) throws FileNotFoundException {
		
		PrintWriter fileObj = new PrintWriter(filePath);
		fileObj.println("from __future__ import print_function");
		fileObj.println("from pyhop import *\n");
		fileObj.println("import goda_operators");
		fileObj.println("import goda_methods\n");
		fileObj.println("print('')");
		fileObj.println("print_operators()\n");
		fileObj.println("print('')");
		fileObj.println("print_methods()\n");
		fileObj.println("state = State('" + actor + "')");
		
		return fileObj;		
	}
	
	private void initialState(RTContainer root) {
		String variable;
		for(int i=0; i < container_size(root); i++){
			initialState(container_element(root,i));
			RTContainer next = container_element(root,i);
			if(next.getRtRegex()==null && container_size(next)==0){
				variable = getName_noRT(container_element(root,i).getName());
				//file.print("'" + variable + "':True");
				variables.add(variable + "=True");
			} else {
				variable = getName_noRT(container_element(root,i).getName());
				//file.print("'" + variable + "':False");
				variables.add(variable + "=False");
			}
		}
	}
	
	public String getName_forRT(String a){
		String b = a.substring(0, a.indexOf(":"));
		return b;
	}
	
	public String getName_noRT(String a){
		String b = a.substring(0, a.indexOf(":"));
		if(a.indexOf('[') != -1 && a.indexOf(']') != -1)
			b = a.substring(0, a.indexOf("[")-1);
		else
			b = a.substring(0, a.length());
		return b;
	}

	public boolean ctx_on(RTContainer n){ //return true if n contains context condition
		if(n.getFulfillmentConditions().size()>0)
			return true;
		return false;
	}

	public void get_ctx(RTContainer n, int ctx){ //print context condition
		int index = n.getFulfillmentConditions().size()-1;
		System.out.print("<< Exclusive_Gateway_ctx : ");
		for(int i=0;i<n.getFulfillmentConditions().size()-1-ctx;i++)
			System.out.print("(" + n.getFulfillmentConditions().get(i).substring(n.getFulfillmentConditions().get(i).indexOf("assertion")+10, n.getFulfillmentConditions().get(i).length()) + "?);");
		System.out.print("(" + n.getFulfillmentConditions().get(index-ctx).substring(n.getFulfillmentConditions().get(index-ctx).indexOf("assertion")+10, n.getFulfillmentConditions().get(index-ctx).length()) + "?)");
		System.out.println(">>\n-----------------------No");
		System.out.println("[Final Event]");
		System.out.println("-----------------------Yes");
	}

	public int container_size(RTContainer a){ //returns size of container
		if(a.getDecompGoals().size()>0 && a.getDecompPlans().size()==0)
			return a.getDecompGoals().size();
		else
			return a.getDecompPlans().size();
	}
	
	public RTContainer container_element(RTContainer a, boolean first){ //if first == true > first element, else last
		if(a.getDecompGoals().size()>0 && a.getDecompPlans().size()==0){
			if(first)
				return a.getDecompGoals().getFirst();
			else
				return a.getDecompGoals().getLast(); 
		}else{
			if(first)
				return a.getDecompPlans().getFirst();
			else
				return a.getDecompPlans().getLast();
		}
	}

	public RTContainer container_element(RTContainer a, int element){  //if second element is a integer, take the element 
		if(a.getDecompGoals().size()>0 && a.getDecompPlans().size()==0)
			return a.getDecompGoals().get(element); 
		else 
			return a.getDecompPlans().get(element);
	}
	
	public String take_last(RTContainer n, String name, int ctx){ //takes the last node inside the sub-tree, for [RETURN]
		String rule = n.getRtRegex();
		String back = name;
		if(container_size(n)>0){
			back = getName_noRT(container_element(n,true).getName());
			back = take_last(container_element(n,true), back, ctx);
		}

		if(rule!=null){
			if((rule.indexOf('#') != -1 && n.getDecomposition()==Const.AND) || rule.indexOf('%') != -1)
				back = "PARALLEL_GATEWAY (From RT: " + getName_noRT(n.getName()) + ") ";
			if(rule.indexOf('#') != -1 && n.getDecomposition()==Const.OR)
				back = "INCLUSIVE_GATEWAY (From RT: " + getName_noRT(n.getName()) + ") ";
			if(rule.contains("opt") || rule.indexOf('|') != -1)
				back = "EXCLUSIVE_GATEWAY (From RT: " + getName_noRT(n.getName()) + ") ";
			if(rule.contains("try")){
				for(int i=0;i<container_size(n);i++){
					String args1 = n.getRtRegex().substring(n.getRtRegex().indexOf("(") + 1, n.getRtRegex().indexOf(")"));
					if(args1.equals(getName_forRT(container_element(n,i).getName())))
						back = getName_noRT(container_element(n,i).getName());
				}
			}
			if(container_size(n)>0)
					back = take_last(container_element(n,true), back, ctx);
		}

		if(ctx_on(n)&&(n.getFulfillmentConditions().size()>ctx)){
			ctx+=1000000; // It's to avoid future changes
			back = "EXCLUSIVE_GATEWAY_CTX (From: " + getName_noRT(n.getName()) + ") ";
		}
		return back;
	}

	public void goalState(RTContainer n, int ctx_aux){
		String rule = n.getRtRegex();
		int ctx=ctx_aux;
		if(ctx_on(n)&&(n.getFulfillmentConditions().size()>ctx)){
			get_ctx(n,ctx);
			ctx+=n.getFulfillmentConditions().size()-ctx;
		}

		if(rule==null)
			rule="none";

		if(rule.indexOf(';') != -1 && n.getDecomposition()==Const.AND){
			sAND(n,ctx);
		}
		
		if(rule.indexOf('#') != -1 && n.getDecomposition()==Const.AND){
			pAND(n,ctx);
		}

		if(rule.indexOf(';') != -1 && n.getDecomposition()==Const.OR){
			sOR(n,ctx);
		}
 
		if(rule.indexOf('#') != -1 && n.getDecomposition()==Const.OR){
			pOR(n,ctx);
		}

		if(rule.indexOf('+') != -1){
			k_times(n,ctx);
		}

		if(rule.indexOf('%') != -1){
			k_times_par(n,ctx);
		}

		if(rule.indexOf('@') != -1){
			k_tries(n,ctx);
		}

		if(rule.contains("opt")){
			opt(n,ctx);
		}

		if(rule.contains("try")){		
			try_op(n,ctx);
		}

		if(rule.indexOf('|') != -1){
			xor(n,ctx);
		}

		if(rule.contains("none") && container_size(n)>0){
			means_end(n,ctx);
		}
	}

	public void sAND(RTContainer n, int ctx){		
		int i;
		String action = "('and_seq', '" + getName_noRT(n.getName()) + "'";
		
		for(i=0;i<container_size(n);i++){
			action = action.concat(", '");
			action = action.concat(getName_noRT(container_element(n,i).getName()));
			action = action.concat("'");
		}
		action = action.concat(")");
		
		for(i=0;i<container_size(n);i++){
			goalState(container_element(n,i), ctx);
		}
		actions.addLast(action);
	}

	public void pAND(RTContainer n, int ctx){
		int i;
		String action = "('and_par', '" + getName_noRT(n.getName()) + "'";
		
		for(i=0;i<container_size(n);i++){
			action = action.concat(", '");
			action = action.concat(getName_noRT(container_element(n,i).getName()));
			action = action.concat("'");
		}
		action = action.concat(")");
		
		for(i=0;i<container_size(n);i++){
			goalState(container_element(n,i), ctx);
		}
		actions.addLast(action);
	}

	public void sOR(RTContainer n, int ctx){
		int i;
		String action = "('or_seq', '" + getName_noRT(n.getName()) + "'";
		
		for(i=0;i<container_size(n);i++){
			action = action.concat(", '");
			action = action.concat(getName_noRT(container_element(n,i).getName()));
			action = action.concat("'");
		}
		action = action.concat(")");
		
		for(i=0;i<container_size(n);i++){
			goalState(container_element(n,i), ctx);
		}
		actions.addLast(action);
	}

	public void pOR(RTContainer n, int ctx){
		int i;
		String action = "('or_par', '" + getName_noRT(n.getName()) + "'";
		
		for(i=0;i<container_size(n);i++){
			action = action.concat(", '");
			action = action.concat(getName_noRT(container_element(n,i).getName()));
			action = action.concat("'");
		}
		action = action.concat(")");
		
		for(i=0;i<container_size(n);i++){
			goalState(container_element(n,i), ctx);
		}
		actions.addLast(action);
	}

	public void k_times(RTContainer n, int ctx){ //n+k
		int i;
		String args = n.getRtRegex().substring(n.getRtRegex().indexOf("+") + 1, n.getRtRegex().length());
		int k = Integer.parseInt(args);
		String action = "";
		RTContainer next = container_element(n,0);
		for(i=0;i<container_size(n);i++){
			action = action.concat("('k_times', '" + getName_noRT(n.getName()) + "', '" + getName_noRT(next.getName()) + "', '" + k + "')");	
		}
		
		for(i=0;i<container_size(n);i++){
			goalState(container_element(n,i), ctx);
		}
		
		actions.addLast(action);
	}

	public void k_times_par(RTContainer n, int ctx){ //n#k
		int i;
		String args = n.getRtRegex().substring(n.getRtRegex().indexOf("%") + 1, n.getRtRegex().length());
		int k = Integer.parseInt(args);
		String action = "";
		RTContainer next = container_element(n,0);
		for(i=0;i<container_size(n);i++){
			action = action.concat("('k_times_par', '" + getName_noRT(n.getName()) + "', '" + getName_noRT(next.getName()) + "', '" + k + "')");	
		}
		
		for(i=0;i<container_size(n);i++){
			goalState(container_element(n,i), ctx);
		}
		
		actions.addLast(action);
	}

	public void k_tries(RTContainer n, int ctx){ //n@k
		int i;
		String args = n.getRtRegex().substring(n.getRtRegex().indexOf("@") + 1, n.getRtRegex().length());
		int k = Integer.parseInt(args);
		String action = "";
		RTContainer next = container_element(n,0);
		for(i=0;i<container_size(n);i++){
			action = action.concat("('k_tries', '" + getName_noRT(n.getName()) + "', '" + getName_noRT(next.getName()) + "', '" + k + "')");	
		}
		
		for(i=0;i<container_size(n);i++){
			goalState(container_element(n,i), ctx);
		}

		actions.addLast(action);
	}

	public void opt(RTContainer n, int ctx){
		int i;
		String action = "";
		RTContainer next = container_element(n,0);
		for(i=0;i<container_size(n);i++){
			action = action.concat("('opt', '" + getName_noRT(n.getName()) + "', '" + getName_noRT(next.getName()) + "')");	
		}
		
		for(i=0;i<container_size(n);i++){
			goalState(container_element(n,i), ctx);
		}
		
		actions.addLast(action);
	}

	// TODO
	public void try_op(RTContainer n, int ctx){ //try():
		
		int i;
		boolean flag = false;
		String action = "('try_op', '" + getName_noRT(n.getName()) + "'";
		String arg1 = getName_noRT(container_element(n,0).getName());
		String arg2 = n.getRtRegex().substring(n.getRtRegex().indexOf("?") + 1, n.getRtRegex().indexOf(":"));
		String arg3 = n.getRtRegex().substring(n.getRtRegex().indexOf(":") + 1, n.getRtRegex().length());
		
		if(!arg2.equals("skip")){
			arg2 = getName_noRT(container_element(n,1).getName());
		}
		
		if(!arg3.equals("skip")){
			arg3 = getName_noRT(container_element(n,2).getName());
		}

		action = action.concat(", '");
		action = action.concat(arg1);
		action = action.concat("', '");
		action = action.concat(arg2);
		action = action.concat("', '");
		action = action.concat(arg3);
		action = action.concat("')");
		
		for(i=0;i<container_size(n);i++){
			goalState(container_element(n,i), ctx);
		}
		actions.addLast(action);
	}

	public void xor(RTContainer n, int ctx){
		int i;
		String action = "('xor', '" + getName_noRT(n.getName()) + "'";
		
		for(i=0;i<container_size(n);i++){
			action = action.concat(", '");
			action = action.concat(getName_noRT(container_element(n,i).getName()));
			action = action.concat("'");
		}
		action = action.concat(")");
		
		for(i=0;i<container_size(n);i++){
			goalState(container_element(n,i), ctx);
		}
		actions.addLast(action);
	}

	public void means_end(RTContainer n, int ctx){
		int i;
		String action = "";
		RTContainer next = container_element(n, 0);
		for(i=0;i<container_size(n);i++){
			action = action.concat("('means_end', '" + getName_noRT(n.getName()) + "', '" + getName_noRT(next.getName()) + "')");	
		}
		
		for(i=0;i<container_size(n);i++){
			goalState(container_element(n,i), ctx);
		}
		
		actions.addLast(action);
	}
}