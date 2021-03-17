package tekno_gui;


import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


import tekno.HighLevelParsing;
import tekno.KnowledgeGraph;

public class GraphTextFrame extends JFrame {
	
	public class CustomOutputStream extends OutputStream {
	    private JTextArea textArea;

	    public CustomOutputStream(JTextArea textArea) {
	        this.textArea = textArea;
	    }

	    @Override
	    public void write(int b) throws IOException {
	        // redirects data to the text area
	        textArea.append(String.valueOf((char)b));
	        // scrolls the text area to the end of data
	        textArea.setCaretPosition(textArea.getDocument().getLength());
	        // keeps the textArea up to date
	        textArea.update(textArea.getGraphics());
	    }
	}
	private int windowH = 650;
	private int windowW = 600;
	
	private int label_x_pos = 50;
	private int text_x_pos = 300;
	
	private int y_start_pos = 0;
	private int y_offset = 50;


	private int label_len = 250;
	private int text_len = 250;
	
	
	private int label_font_size = 15;
	private int input_font_size = 15;
	
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
	private JTextArea log;
	private JScrollPane scroll;
	private JLabel prolog_f;
	private JRadioButton prolog_rb;
	private JLabel l_prolog_file;
	private JTextField t_prolog_file;
	private JLabel l_graph;
	private JRadioButton graph_rb;
	private JLabel l_wiki;
	private JRadioButton wiki_rb;

	/**
	 * Create the frame.
	 */
	public GraphTextFrame() {
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBounds(windowH, 90, windowW, windowH); 
		f.setLayout(null);
		f.setVisible(true);
		
		uri = new JLabel("Neo4J URI connection"); 
		uri.setFont(new Font("Arial", Font.PLAIN, label_font_size)); 
		uri.setSize(label_len, 20); 
		uri.setLocation(label_x_pos, y_start_pos + (y_offset)); 
        f.add(uri); 
  
        t_uri = new JTextField(); 
        t_uri.setFont(new Font("Arial", Font.PLAIN, input_font_size)); 
        t_uri.setSize(text_len, 20); 
        t_uri.setText("bolt://localhost:7687");
        t_uri.setLocation(text_x_pos, y_start_pos + (y_offset)); 
        f.add(t_uri); 
        
        usr = new JLabel("neo4J user"); 
        usr.setFont(new Font("Arial", Font.PLAIN, label_font_size)); 
        usr.setSize(label_len, 20); 
        usr.setLocation(label_x_pos, y_start_pos + (y_offset*2)); 
        f.add(usr); 
  
        t_usr = new JTextField(); 
        t_usr.setFont(new Font("Arial", Font.PLAIN, input_font_size)); 
        t_usr.setSize(text_len, 20); 
        t_usr.setText("neo4j");

        t_usr.setLocation(text_x_pos, y_start_pos + (y_offset*2)); 
        f.add(t_usr); 
  
        
        pwd = new JLabel("neo4J password"); 
        pwd.setFont(new Font("Arial", Font.PLAIN, label_font_size)); 
        pwd.setSize(label_len, 20); 
        pwd.setLocation(label_x_pos, y_start_pos + (y_offset*3)); 
        f.add(pwd); 
  
        t_pwd = new JPasswordField(); 
        t_pwd.setFont(new Font("Arial", Font.PLAIN, input_font_size)); 
        t_pwd.setSize(text_len, 20); 
        t_pwd.setLocation(text_x_pos, y_start_pos + (y_offset*3)); 
        f.add(t_pwd); 
        
        file = new JLabel("Text file location"); 
        file.setFont(new Font("Arial", Font.PLAIN, label_font_size)); 
        file.setSize(label_len, 20); 
        file.setLocation(label_x_pos, y_start_pos + (y_offset*4)); 
        f.add(file); 
  
        t_file = new JTextField(); 
        t_file.setFont(new Font("Arial", Font.PLAIN, input_font_size)); 
        t_file.setText("./source_files/");
        t_file.setSize(text_len, 20); 
        t_file.setLocation(text_x_pos, y_start_pos + (y_offset*4)); 
        f.add(t_file); 
        
        btn = new JButton("Submit"); 
        btn.setFont(new Font("Arial", Font.PLAIN, input_font_size)); 
        btn.setSize(100, 20); 
        btn.setLocation(250, y_start_pos + (y_offset*9)); 
        btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						PrintStream printStream = new PrintStream(new CustomOutputStream(log));
						System.setOut(printStream);
				    	KnowledgeGraph kg = new KnowledgeGraph(t_uri.getText(),t_usr.getText(),t_pwd.getText());
				    	HighLevelParsing hlp = new HighLevelParsing();
				    	if(graph_rb.isSelected())
					    	kg.resetGraph();
					    	
			    	
			    		hlp.readFile(t_file.getText());
			        	hlp.executeTeKnoPipeline(wiki_rb.isSelected());
			        	hlp.generateGraphDB(kg);
					    	
				    	
					    	
				    	if(prolog_rb.isSelected())
				    		kg.extractFacts(t_prolog_file.getText(), "prolog_files");
				    	
				    	try {
							kg.close();
							System.out.println("done.");
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}.start();

			}
        	
        }); 
        f.add(btn);
        
        prolog_f = new JLabel("<html>Generate a prolog file? </br>(saved into ./prolog_files)</html>"); 
        prolog_f.setFont(new Font("Arial", Font.PLAIN, label_font_size)); 
        prolog_f.setSize(label_len, 35); 
        prolog_f.setLocation(label_x_pos, y_start_pos + (y_offset *5)); 
        f.add(prolog_f); 
  
        prolog_rb = new JRadioButton("select to choose 'yes'"); 
        prolog_rb.setFont(new Font("Arial", Font.PLAIN, input_font_size)); 
        prolog_rb.setSelected(true); 
        prolog_rb.setSize(text_len, 20); 
        prolog_rb.setLocation(text_x_pos, y_start_pos + (y_offset *5)); 
        f.add(prolog_rb); 
       
        
        l_prolog_file = new JLabel("<html>prolog file name </br>(no extension)</html>"); 
        l_prolog_file.setFont(new Font("Arial", Font.PLAIN, label_font_size)); 
        l_prolog_file.setSize(label_len, 20); 
        l_prolog_file.setLocation(label_x_pos, y_start_pos + (y_offset*6)); 
        f.add(l_prolog_file); 
  
        t_prolog_file = new JTextField(); 
        t_prolog_file.setFont(new Font("Arial", Font.PLAIN, input_font_size)); 
        t_prolog_file.setSize(text_len, 20); 
        t_prolog_file.setLocation(text_x_pos, y_start_pos + (y_offset*6)); 
        f.add(t_prolog_file); 
        
        l_graph = new JLabel("Reset the graph?"); 
        l_graph.setFont(new Font("Arial", Font.PLAIN, label_font_size)); 
        l_graph.setSize(label_len, 20); 
        l_graph.setLocation(label_x_pos, y_start_pos + (y_offset *7)); 
        f.add(l_graph); 
  
        graph_rb = new JRadioButton("select to choose 'yes'"); 
        graph_rb.setFont(new Font("Arial", Font.PLAIN, input_font_size)); 
        graph_rb.setSelected(true); 
        graph_rb.setSize(text_len, 20); 
        graph_rb.setLocation(text_x_pos, y_start_pos + (y_offset *7)); 
        f.add(graph_rb); 
       
        l_wiki = new JLabel("Use Wikipedia search?"); 
        l_wiki.setFont(new Font("Arial", Font.PLAIN, label_font_size)); 
        l_wiki.setSize(label_len, 20); 
        l_wiki.setLocation(label_x_pos, y_start_pos + (y_offset *8)); 
        f.add(l_wiki); 
  
        wiki_rb = new JRadioButton("select to choose 'yes'"); 
        wiki_rb.setFont(new Font("Arial", Font.PLAIN, input_font_size)); 
        wiki_rb.setSelected(true); 
        wiki_rb.setSize(text_len, 20); 
        wiki_rb.setLocation(text_x_pos, y_start_pos + (y_offset *8)); 
        f.add(wiki_rb); 
       
        
        log = new JTextArea(); 
        log.setFont(new Font("Arial", Font.PLAIN, input_font_size)); 
        log.setEditable(false);
        
        scroll = new JScrollPane(log);
        scroll.setSize(600, 100);
        scroll.setLocation(0, y_start_pos + (y_offset*10));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        f.add(scroll); 
	}

}
