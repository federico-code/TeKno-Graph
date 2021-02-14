package tekno_gui;


import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JTextField;

import tekno.HighLevelParsing;
import tekno.KnowledgeGraph;

public class GraphTextFrame extends JFrame {

	private JFrame f = new JFrame("Graph from Text");
	private JLabel uri;
	private JTextField t_uri;
	private JLabel usr;
	private JTextField t_usr;
	private JLabel pwd;
	private JTextField t_pwd;
	private JLabel file;
	private JTextField t_file;
	private JButton btn;

	/**
	 * Create the frame.
	 */
	public GraphTextFrame() {
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(300,300);
		f.setBounds(300, 90, 600, 600); 
		f.setLayout(null);
		f.setVisible(true);
		
		uri = new JLabel("Neo4J URI connection"); 
		uri.setFont(new Font("Arial", Font.PLAIN, 20)); 
		uri.setSize(200, 20); 
		uri.setLocation(100, 100); 
        f.add(uri); 
  
        t_uri = new JTextField(); 
        t_uri.setFont(new Font("Arial", Font.PLAIN, 15)); 
        t_uri.setSize(200, 20); 
        t_uri.setLocation(300, 100); 
        f.add(t_uri); 
        
        usr = new JLabel("neo4J user"); 
        usr.setFont(new Font("Arial", Font.PLAIN, 20)); 
        usr.setSize(100, 20); 
        usr.setLocation(100, 150); 
        f.add(usr); 
  
        t_usr = new JTextField(); 
        t_usr.setFont(new Font("Arial", Font.PLAIN, 15)); 
        t_usr.setSize(200, 20); 
        t_usr.setLocation(300, 150); 
        f.add(t_usr); 
  
        
        pwd = new JLabel("neo4J password"); 
        pwd.setFont(new Font("Arial", Font.PLAIN, 20)); 
        pwd.setSize(200, 20); 
        pwd.setLocation(100, 200); 
        f.add(pwd); 
  
        t_pwd = new JTextField(); 
        t_pwd.setFont(new Font("Arial", Font.PLAIN, 15)); 
        t_pwd.setSize(200, 20); 
        t_pwd.setLocation(300, 200); 
        f.add(t_pwd); 
        
        file = new JLabel("Text file location"); 
        file.setFont(new Font("Arial", Font.PLAIN, 20)); 
        file.setSize(200, 20); 
        file.setLocation(100, 250); 
        f.add(file); 
  
        t_file = new JTextField(); 
        t_file.setFont(new Font("Arial", Font.PLAIN, 15)); 
        t_file.setSize(200, 20); 
        t_file.setLocation(300, 250); 
        f.add(t_file); 
        
        btn = new JButton("Submit"); 
        btn.setFont(new Font("Arial", Font.PLAIN, 15)); 
        btn.setSize(100, 20); 
        btn.setLocation(250, 320); 
        btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

		    	KnowledgeGraph kg = new KnowledgeGraph(t_uri.getText(),t_usr.getText(),t_pwd.getText());
		    	HighLevelParsing hlp = new HighLevelParsing();
		    	if(kg.resetGraph()) {
		    		hlp.readFile(t_file.getText());
		        	hlp.executeTeKnoPipeline();
		        	hlp.generateGraphDB(kg);
		    	}
		    	
		    	//kg.extractFacts("bg", "prolog_files");
		    	try {
					kg.close();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
        	
        }); 
        f.add(btn); 
	}

}
