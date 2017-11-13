package br.unb.cic.rtgoretoprism.generator.pyhop.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class PyhopGUI extends JFrame{

	JFrame frame = new JFrame("Pyhop Settings");

    JPanel panelBody = new JPanel();
  
    GroupLayout layout = new GroupLayout(panelBody);

    JPanel panelTop    = new JPanel();
    JPanel panelVar     = new JPanel();
    JPanel panelVar2     = new JPanel();
    JPanel panelSel     = new JPanel();
    JPanel panelVerb = new JPanel();
    JPanel panelActs = new JPanel();
    JPanel panelFooter = new JPanel();

    final JDialog dialog = new JDialog(frame);
    
    JLabel varLabel   = new JLabel("Selecione var:");
	JLabel actionsLabel   = new JLabel("Selecione acttion:");
	
    StyledButton selButton = new StyledButton("Select All");
    StyledButton cancelButton = new StyledButton("Cancel");
    StyledButton chooserButton = new StyledButton("Choose");
    StyledButton accButton = new StyledButton("Finish");
    
    JRadioButton verb1 = new JRadioButton("verbose=1");
    JRadioButton verb2 = new JRadioButton("verbose=2");
    
    
    JTextField textField = new JTextField();
    
    String filePath;
    String verbose;
	private LinkedList<String> actions = new LinkedList<String>();
	private LinkedList<String> variables = new LinkedList<String>();
	private LinkedList<JCheckBox> checked = new LinkedList<JCheckBox>();
	

	public PyhopGUI(String file, LinkedList<String> vars, LinkedList<String> acts){
		this.filePath = file;
		this.actions = acts;
		this.variables = vars;

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
	    		String item = var.substring(0, var.indexOf("="));
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
	    
	    panelVerb.setPreferredSize(new Dimension (250, 55));
	    panelVerb.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Select verbose:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
	    panelVerb.setLayout(new GridLayout(1, 2));
	    
	    ButtonGroup radios = new ButtonGroup();
	    
	    verb1.setFocusable(false);
	    verb1.setSelected(true);
	    verb2.setFocusable(false);
	    	    
	    radios.add(verb1);
	    radios.add(verb2);
	    panelVerb.add(verb1);
	    panelVerb.add(verb2);
	    
	    cancelButton.setBounds(105, 25, 85, 25);
		accButton.setBounds(205, 25, 85, 25);
		
		panelActs.setPreferredSize(new Dimension (275, 55));
		panelActs.setLayout(null);
		panelActs.add(cancelButton);
		panelActs.add(accButton);
	    
		panelFooter.setPreferredSize(new Dimension (575, 55));
		panelFooter.setLayout(new BoxLayout(panelFooter, BoxLayout.X_AXIS));
			    
		panelFooter.add(panelVerb);
		panelFooter.add(panelActs);
		
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(panelTop)   
				.addComponent(panelVar)
	            .addComponent(panelFooter)
		);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension (615, 370));
        //frame.setLocationRelativeTo(null);
        frame.setContentPane(panelBody);
        frame.pack();
        frame.setVisible(true);
        
        setActions();
	}
	
	private void setActions(){
		
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
				
				for(int i=0; i<checked.size();i++){
					
					JCheckBox cb = checked.get(i);	
					String cbText = checked.get(i).getText();
					int varIndex = variables.indexOf(cbText);
					
					if(cb.isSelected()){
						String var = "'" + cbText + "':True";
						variables.set(varIndex, var);
					} else {
						String var = "'" + cbText + "':False";
						variables.set(varIndex, var);
					}
				}
				if(verb1.isSelected()){
					verbose = "verbose=1";
				} else {
					verbose = "verbose=2";
				}
				fileWriter();
			}

			
		});	
		
		cancelButton.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e){
				frame.dispose();
			}
		});
	}

	public LinkedList<String> getVariables() {
		return variables;
	}
	
	public LinkedList<String> getActions() {
		return actions;
	}
	
	private void fileWriter() {
		
		
		
		frame.dispose();
		
	}
}


