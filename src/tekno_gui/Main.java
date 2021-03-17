package tekno_gui;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Main extends JFrame {

	private JFrame f = new JFrame("TeKno");
	private JButton b1 = new JButton("Graph");
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new Main();

	}

	/**
	 * Create the frame.
	 */
	public Main() {
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		f.getContentPane().add(b1);
		
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				f.dispose();
				new GraphTextFrame();
			}
		});
		f.setSize(500,500);
        f.add(new MenuPane());
        f.pack();
        f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
	
    public class MenuPane extends JPanel {

        public MenuPane() {
            setBorder(new EmptyBorder(50, 50, 50, 50));
            setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.NORTH;

            add(new JLabel("<html><h1><strong><i>TeKno</i></strong></h1><hr></html>"), gbc);
            add(new JLabel("<html></br></html>"), gbc);
            JLabel sub_text = new JLabel("<html>Select one of the following:"
            		+ "<ul>"
            		+ "<li>Graph from Text: generate a knowledge graph from a text file</li>"
            		+ "<li>Graph from XML: import a knowledge graph from a XML file</li>"
            		+ "</ul>"
            		+ "</br>"
            		+ "</html>");
            sub_text.setSize(50, 10);
            add(sub_text, gbc);

            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JPanel buttons = new JPanel(new GridBagLayout());

            JButton b1 = new JButton("Graph from Text");
    		b1.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent ae) {
    				f.dispose();
    				new GraphTextFrame();
    			}
    		});
            JButton b2 = new JButton("Graph from XML");
    		b2.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent ae) {
    				f.dispose();
    				new GraphXMLFrame();
    			}
    		});
            
            buttons.add(b1, gbc);
            buttons.add(b2, gbc);

            gbc.weighty = 1;
            add(buttons, gbc);
        }

    }
	
	
	

}
