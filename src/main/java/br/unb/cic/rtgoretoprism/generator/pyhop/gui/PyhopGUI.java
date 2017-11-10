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
    JPanel panelVerb = new JPanel();
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
		
		panelTop.setPreferredSize(new Dimension (475, 60));
		panelTop.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Select Pyhop directory:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelTop.setLayout(null);
		panelTop.setAlignmentY(JComponent.LEFT_ALIGNMENT);
		
		textField.setBounds(15, 20, 350, 25); 
		chooserButton.setBounds(374, 20, 89, 25);
		
		chooserButton.setPreferredSize(new Dimension (40, 40));
		chooserButton.setFocusable(false);
		chooserButton.setOpaque(false);
		
		panelTop.add(textField);
		panelTop.add(chooserButton);
		
		panelVar.setPreferredSize(new Dimension (475, 190));
	    panelVar.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Select variables:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
	    panelVar.setLayout(new GridLayout(6, 3));
	    panelVar.setAlignmentY(JComponent.LEFT_ALIGNMENT);
	    
	    for (String var : variables) {
	    	if(var.contains("True")){
	    		String item = var.substring(0, var.indexOf("="));
	    		JCheckBox cb = new JCheckBox(var);
	    		checked.add(cb);
	    	}
	    }
	    for(JCheckBox cb : checked){
	    	panelVar.add(cb);
	    }
	    
	    selButton.setBounds(375, 116, 87, 23);
	    panelVar.add(selButton);
	    
	    panelVerb.setPreferredSize(new Dimension (295, 55));
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
	    
	    panelFooter.setAlignmentY(JComponent.RIGHT_ALIGNMENT);
		panelFooter.setPreferredSize(new Dimension (180, 40));
		//panelFooter.setLayout(null);
		//panelFooter.setBounds(302, 294, 183, 40);
		
		cancelButton.setBounds(12, 11, 75, 23);
		accButton.setBounds(99, 11, 75, 23);
	    
		panelFooter.add(cancelButton);
		panelFooter.add(accButton);
		
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(panelTop)   
				.addComponent(panelVar)
				.addComponent(panelVerb)
	            .addComponent(panelFooter)
		);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension (515, 400));
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
				frame.dispose();
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
}


