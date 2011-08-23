/*
 * Rebus generator
 * By Carl Estabrook
 * 8/23/2011
 */
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;

public class rebusGUI extends JFrame implements ActionListener 
{
	private final int WIDTH = 1000;
	private final int HEIGHT = 400;
	private JTextPane textPane;
	private JComboBox combo1;
	private String choices[] = {
			"",
			"Solve this work here.",
			"Is your solution optimal?",
			"Outlying data points violated our weakest heuristics.",
			"The quiet oenophile adored rhododendrons but loathed zoological exoticness.", 
			};

	public rebusGUI()
	{
		 super();
		 setSize(WIDTH, HEIGHT);
		 setTitle("Rebus");
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 Container pane = getContentPane();
		 pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		 
		 combo1 = new JComboBox();
		 combo1.setAlignmentX(Component.CENTER_ALIGNMENT);
		 for (int i=0;i<choices.length;i++) 
				combo1.addItem (choices[i]);
		 combo1.setFont(new Font("Serif", Font.BOLD, 16));
		 combo1.setEditable(true);
				
		pane.add(combo1);
		 
		 JButton button1 = new JButton("create");
		 button1.addActionListener(this);
		 button1.setAlignmentX(Component.CENTER_ALIGNMENT);
		 pane.add(button1);
		 
		 textPane = new JTextPane();
		 textPane.setEditable(true);
		 textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
				textPane.setFont(new Font("Serif", Font.BOLD, 30));
								pane.add(textPane);				 	
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		textPane.setText("");
		String sentence = (String)combo1.getSelectedItem();
		 new rebusLogic(this, sentence);	
	}
	
	public void printChar(String s)
	{
		textPane.replaceSelection(s);		
	}
	
	public void printImage(String im)
	{
		try 
		{			   
		    StyledDocument doc = (StyledDocument)textPane.getDocument();
		    javax.swing.ImageIcon image = new javax.swing.ImageIcon("Images/"+im);
		    image.setImage(resizeImage(image.getImage()));
		    Style style = doc.addStyle("StyleName", null);
		    StyleConstants.setIcon(style, image);
		    doc.insertString(doc.getLength(), "ignored text", style);
		} 
		catch (BadLocationException e)
		{
		}	   
	}
		
	 private java.awt.Image resizeImage(java.awt.Image image)
	 {
	        int width = (int)(image.getWidth(null) / 2);
	        int height = (int) (image.getHeight(null) /2);

	        return image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);  
	 }
}
