package tekno_gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JTextField;

public class GraphXMLFrame extends JFrame {

	private JFrame f = new JFrame("Graph from XML");
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Create the frame.
	 */
	public GraphXMLFrame() {
	
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(300,300);
		f.setVisible(true);
		
	}

}