package br.unb.cic.rtgoretoprism.generator.pyhop.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

import br.unb.cic.rtgoretoprism.generator.kl.AgentDefinition;
import it.itc.sra.taom4e.model.core.informalcore.Actor;

public class PyhopGUI extends JFrame{

	JFrame frame = new JFrame("GODA-Pyhop");

    JPanel panelBody = new JPanel();
  
    GroupLayout layout = new GroupLayout(panelBody);

    JPanel panelTop    = new JPanel();
    JPanel panelVar     = new JPanel();
    JPanel panelVar2     = new JPanel();
    JPanel panelSel     = new JPanel();
    JPanel panelOptions = new JPanel();
    JPanel panelActs = new JPanel();
    JPanel panelFooter = new JPanel();

    final JDialog dialog = new JDialog(frame);
    
    JLabel varLabel   = new JLabel("Selecione var:");
	JLabel actionsLabel   = new JLabel("Selecione acttion:");
	
    StyledButton selButton = new StyledButton("Select All");
    StyledButton cancelButton = new StyledButton("Cancel");
    StyledButton chooserButton = new StyledButton("Choose");
    StyledButton accButton = new StyledButton("Finish");
    
    JCheckBox runCheck = new JCheckBox("Open generated files");    
    
    JTextField textField = new JTextField();
    
    private String filePath = "/home/sabiah/Desktop/dananau-pyhop/goda";
    private String verbose = "verbose=1";
	private PrintWriter file;
	private LinkedList<String> actions = new LinkedList<String>();
	private LinkedList<String> variables = new LinkedList<String>();
	private LinkedList<JCheckBox> checked = new LinkedList<JCheckBox>();
	
	private AgentDefinition ad;
	private Actor a;
	

	public PyhopGUI(AgentDefinition ad, Actor a, LinkedList<String> variables, LinkedList<String> actions) throws FileNotFoundException{
		this.a = a;
		this.ad = ad;
		this.variables = variables;
		this.actions = actions;
		
		renderGUI();
		return;
	}	
	
	class StyledButton extends JButton{

        public StyledButton(String text){
            setText(text);
        	setFocusable(false);
            setOpaque(false);
        }
	}
	
	public void renderGUI() {
		
		panelTop.setPreferredSize(new Dimension (575, 60));
		panelTop.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Select Pyhop directory:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelTop.setLayout(null);
		panelTop.setAlignmentY(JComponent.LEFT_ALIGNMENT);
		
		textField.setBounds(15, 20, 450, 25); 
		//textField.setText(filePath);
		chooserButton.setBounds(474, 20, 89, 25);
		
		chooserButton.setPreferredSize(new Dimension (40, 40));
		chooserButton.setFocusable(false);
		chooserButton.setOpaque(false);
		
		panelTop.add(textField);
		panelTop.add(chooserButton);
		
		panelVar.setPreferredSize(new Dimension (575, 190));
	    panelVar.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Select variables:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
	    panelVar.setLayout(new BoxLayout(panelVar, BoxLayout.Y_AXIS));
	    
	    panelVar2.setLayout(new GridLayout(5, 1));
	    panelVar2.setPreferredSize(new Dimension (575, 150));
	    
	    for (String var : variables) {
	    	if(var.contains("True")){
	    		String item = var.substring(var.indexOf("'") + 1, var.indexOf("':"));
	    		item = item.replaceAll("_", " ");
	    		JCheckBox cb = new JCheckBox(item);
	    		cb.setToolTipText(item);
	    		checked.add(cb);
	    	}
	    }
	    
	    for(JCheckBox cb : checked){
    		panelVar2.add(cb);
	    }

	    panelSel.setLayout(null);
	    panelSel.setPreferredSize(new Dimension (575, 60));
	    selButton.setBounds(437, 3, 120, 25);
	    
	    panelSel.add(selButton);
	    
	    panelVar.add(panelVar2);
	    panelVar.add(panelSel);
	    
	    panelOptions.setPreferredSize(new Dimension (250, 55));
	    panelOptions.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
	    panelOptions.setLayout(new GridLayout(1, 2));
	    
	    panelOptions.add(runCheck);
	    
	    cancelButton.setBounds(105, 25, 85, 25);
		accButton.setBounds(205, 25, 85, 25);
		accButton.setEnabled(false);
		
		panelActs.setPreferredSize(new Dimension (275, 55));
		panelActs.setLayout(null);
		panelActs.add(cancelButton);
		panelActs.add(accButton);
	    
		panelFooter.setPreferredSize(new Dimension (575, 55));
		panelFooter.setLayout(new BoxLayout(panelFooter, BoxLayout.X_AXIS));
			    
		panelFooter.add(panelOptions);
		panelFooter.add(panelActs);
		
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(panelTop)   
				.addComponent(panelVar)
	            .addComponent(panelFooter)
		);
		
        frame.setPreferredSize(new Dimension (615, 370));
        frame.setContentPane(panelBody);
        frame.pack();
        frame.setVisible(true);
        
        setActions();
	}
	
	private void setActions(){
		
		chooserButton.addActionListener(new ActionListener(){ 
            public void actionPerformed(ActionEvent e){ 
            	fileChooser();
            	accButton.setEnabled(true);
            }
        });
		
		
		selButton.addActionListener(new ActionListener(){ 
            public void actionPerformed(ActionEvent e){ 
            	if(selButton.getText().equals("Select All")){
            		for(JCheckBox cb : checked){
                		cb.setSelected(true);
            		}
            		selButton.setText("Unselect All");
            	} else if(selButton.getText().equals("Unselect All")){
            		for(JCheckBox cb : checked){
            			cb.setSelected(false);
            		}
            		selButton.setText("Select All");
            	}
            }
        });
				
		accButton.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e){ 
				
				setVariables();
				
				try {
					writeFile();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				frame.dispose();
			}
		});	
		
		cancelButton.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e){
				frame.dispose();
			}
		});
	}

	protected void setVariables() {
		
		
		for(int i=0; i<checked.size();i++){
			
			String cbText = checked.get(i).getText();
			String var = "";
			String aux = "";
			
			aux = "'" + cbText + "':True";
			aux = aux.replaceAll(" ", "_");
			
			int varIndex = variables.indexOf(aux);	
			
			if(checked.get(i).isSelected()){
				var = "'" + cbText + "':True";;
			} else {
				var = "'" + cbText + "':False";
			}
			
			variables.set(varIndex, var);
			
		}
		if(runCheck.isSelected()){
			try {
				
				Process process = new ProcessBuilder("/usr/bin/gedit", "/home/sabiah/Desktop/dananau-pyhop/goda/goda-pyhop.py").start();
				//File f = new File(filePath + "/goda-pyhop.py");
				//java.awt.Desktop.getDesktop().open(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}

	public void writeFile() throws FileNotFoundException{
		
		file = createFile();
		
		writeObjects();
		writeActions();
		writeVerbose();

		file.close();	
	}

	private void writeVerbose() {
 		file.print("], verbose=1)");
 	}

	private void writeActions() {
		file.print("pyhop(state, [\\\n");
		if(actions.size()!=0){
			for(int i=0; i<actions.size(); i++){
				System.out.println(actions.get(i));
				if(i != actions.size()){
					String action = actions.get(i).concat(",\\\n");
					file.print(action);
				}
			}
		}
		
	}

	private void writeObjects() {
		file.print("state.objects = {\\\n");
		if(variables.size()!=0){
			for(int i=0; i<variables.size(); i++){
				System.out.println(variables.get(i));
				if(i != variables.size()){
					String var = variables.get(i).concat(",\\\n");
					var = var.replaceAll(" ","_");
					file.print(var);
				}
			}
		}
		String root = getName_noRT(ad.rootlist.getFirst().getName());
        file.print("'" + root + "':False}\n\n");
		
	}

	private PrintWriter createFile() throws FileNotFoundException {
		
		PrintWriter fileObj = new PrintWriter(filePath + "/goda-pyhop.py");
		fileObj.println("from __future__ import print_function");
		fileObj.println("from pyhop import *\n");
		fileObj.println("import goda_operators");
		fileObj.println("import goda_methods\n");
		fileObj.println("print('')");
		fileObj.println("print_operators()\n");
		fileObj.println("print('')");
		fileObj.println("print_methods()\n");
		fileObj.println("state = State('" + this.a.getName() + "')");
		
		return fileObj;		
	}
	
	private String getName_noRT(String a){
		String b = a.substring(0, a.indexOf(":"));
		if(a.indexOf('[') != -1 && a.indexOf(']') != -1)
			b = a.substring(0, a.indexOf("[")-1);
		else
			b = a.substring(0, a.length());
		return b;
	}
	
	private void fileChooser(){
	
		JFileChooser pyhopChooser = new JFileChooser("..");  
	    pyhopChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

	    pyhopChooser.showOpenDialog(this);  
	    filePath = pyhopChooser.getSelectedFile().getPath();
	    textField.setText(filePath);
		
	}
}


