package gov.nih.nci.evs.service;

import java.awt.*;
import java.awt.event.* ;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.* ;
import java.util.*;
import javax.swing.* ;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;
import javax.swing.tree.*;
import org.apache.log4j.*;

public class CDISCPublisher extends JPanel
                             implements ActionListener {
    static private final String newline = "\n";
    static String title = "EVS Service - CDISC Publication";
    JTextArea log;
    JFileChooser fc;

	DefaultHighlighter highlighter;
	DefaultHighlighter unhighlighter;
	DefaultHighlighter.DefaultHighlightPainter highlight_painter;
	DefaultHighlighter.DefaultHighlightPainter unhighlight_painter;

	SimpleAttributeSet sas;
	Color backgroundColor;

    JFrame frame;

    JDialog dialog;
	PrintWriter out;
	JButton okButton;
	JButton resetButton;
	JButton openButton;
	JButton closeButton;
	JButton saveButton;

	JTextField textfield;// = new JTextField(20);
	JTextField dataFile;// = new JTextField(20);
	JTextField outputFile;// = new JTextField(20);

	JTextArea textarea = null;

    public CDISCPublisher() {
        super(new BorderLayout());
        fc = new JFileChooser();

        highlighter = new DefaultHighlighter();
        highlight_painter =
           new DefaultHighlighter.DefaultHighlightPainter(
                 new Color(198,198,250));
        sas = new SimpleAttributeSet();
        // Create Frame
		frame = new JFrame(title);
		frame.setSize(500,100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		dialog = new JDialog(frame);
		dialog.setSize(750,300);

        // GridBagLayout
        GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0,5,0,0);

////////////////////////////////////////////////////////////////////////////////////////
        JPanel panel = new JPanel(gridbag);

		c.gridx=0;
		c.gridy=0;
		JLabel label0 = new JLabel("Tab-delimited Data File (.txt):");
		gridbag.setConstraints(label0, c);
		panel.add(label0);

		c.gridx=1;
		c.gridy=0;
		dataFile = new JTextField(55);
		dataFile.setText("");
		gridbag.setConstraints(dataFile, c);
		panel.add(dataFile);

		c.gridx=2;
		c.gridy=0;
		openButton = new JButton("Select");
		openButton.addActionListener(this);
		gridbag.setConstraints(openButton, c);
		panel.add(openButton);

		c.gridx=0;
		c.gridy=1;
		JLabel label1 = new JLabel("Output File (.owl):");
		gridbag.setConstraints(label1, c);
		panel.add(label1);

		c.gridx=1;
		c.gridy=1;
		outputFile = new JTextField(55);
		outputFile.setText("");
		gridbag.setConstraints(outputFile, c);
		panel.add(outputFile);

		c.gridx=2;
		c.gridy=1;
		saveButton = new JButton("Select");
		saveButton.addActionListener(this);
		gridbag.setConstraints(saveButton, c);
		panel.add(saveButton);
		dialog.getContentPane().add(panel,BorderLayout.NORTH);

////////////////////////////////////////////////////////////////////////////////////////
        // Text Area
		textarea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(log);
		scrollPane.setPreferredSize(new Dimension(800, 600));
		scrollPane.setViewportView(textarea);
		dialog.getContentPane().add(scrollPane,BorderLayout.CENTER);
////////////////////////////////////////////////////////////////////////////////////////
        // Button Panel
		JPanel buttonPanel = new JPanel();
		okButton = new JButton("Generate");
		okButton.addActionListener(this);
		buttonPanel.add(okButton);

		closeButton = new JButton("Exit");
		closeButton.addActionListener(this);
		buttonPanel.add(closeButton);

		okButton.addActionListener(this);
		dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

////////////////////////////////////////////////////////////////////////////////////////
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setLocation(100,20);
		dialog.setModal(true);
		dialog.setTitle("EVS Support -- Mapping Exporter");
		dialog.pack();
		dialog.show();
		frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		frame.setVisible(true);
    }

    private void outputLine(String s)
	{
        if (textarea != null)
		{
			textarea.append(s + "\n");
		}
	}

    public void highlightText(String target)
	{
        String theText = textarea.getText();
		if (theText.equals("")) return;
		int n = theText.indexOf(target);
		if (n == -1)
		{
		   System.out.println(target + " not found.");
		   return;
		}

        try{
		   highlighter.addHighlight(n, n+target.length(), highlight_painter);
        }
        catch(Exception e) {}
	}


    public void actionPerformed(ActionEvent event) {
        Object action = event.getSource();

        if (action == closeButton) {
			frame.setVisible(false);
            System.exit(0);
		} else if (action == okButton) {
			String datafile = dataFile.getText();
			String outputfile = outputFile.getText();
     		if (datafile.equals(""))
     		{
				JOptionPane.showMessageDialog(null,"Please specify a data file.", "Warning",JOptionPane.WARNING_MESSAGE);
				return;
			}
			else if (outputfile.equals(""))
			{
				JOptionPane.showMessageDialog(null,"Please specify an output file.","Warning",JOptionPane.WARNING_MESSAGE);
				return;
			}

            textarea.setText("");
            okButton.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            // generate the file
            datafile = (String) dataFile.getText();
            outputfile = (String) outputFile.getText();
            new RDFGenerator().generate(datafile, outputfile);
            okButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            textarea.setText(outputfile + " generated.");

        } else if (action == openButton) {
			System.out.println("Open button pressed");
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Open File As...");
			int returnVal = chooser.showOpenDialog(dialog);
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = chooser.getSelectedFile();
				String datafile = file.getAbsolutePath();
				dataFile.setText(datafile);
				int n = datafile.lastIndexOf(".");
				String outputfile = datafile.substring(0, n) + ".owl";
				outputFile.setText(outputfile);
			}

        } else if (action == saveButton) {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Save File As...");
			int returnVal = chooser.showSaveDialog(dialog);
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = chooser.getSelectedFile();
				if (file.exists()) {
				    int action0 = JOptionPane.showConfirmDialog(this,
				                 "Replace existing file?");
					if (action0 != JOptionPane.YES_OPTION) return;
				}
				//saveToFile(file);
				outputFile.setText(file.getAbsolutePath());
			}
	    } else if (action == resetButton) {
			dataFile.setText("");
			textarea.setText("");
		}
    }
    private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComponent newContentPane = new CDISCPublisher();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

    }
}
