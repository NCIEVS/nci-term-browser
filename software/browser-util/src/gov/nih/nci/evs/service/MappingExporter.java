package gov.nih.nci.evs.service;

import gov.nih.nci.evs.browser.bean.*;
import gov.nih.nci.evs.browser.properties.*;
import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.security.*;
import gov.nih.nci.system.client.*;
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
import org.LexGrid.LexBIG.caCore.interfaces.*;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.Impl.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;

public class MappingExporter extends JPanel
                             implements ActionListener {

    static private final String newline = "\n";
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

	JTextField textfield;
	JTextField outputFile;
	JTextArea textarea;

	String mappingCodingSchemeName = "";
	JComboBox mappingList = null;
	JComboBox serviceUrlList = null;

	String mapping = null;
	String serviceUrl = "null";
	LexBIGService lbSvc = null;
	String SERVICE_URL_PROD = "https://lexevsapi65.nci.nih.gov";
	String DEFAULT_SERVICE_URL = "null";
	boolean output_file_generated = false;

	static String[] serviceUrls = {
		                     "null",
							 "https://lexevsapi65-dev.nci.nih.gov",
							 "https://lexevsapi65-qa.nci.nih.gov",
							 "https://lexevsapi65-data-qa.nci.nih.gov",
							 "https://lexevsapi65-stage.nci.nih.gov",
							 "https://lexevsapi65.nci.nih.gov"
							 };

	String[] mappings = null;
	Vector mapping_data = null;
	HashMap codingSchemeHashMap = null;
	Vector codingSchemeNames = null;
	static String title = "EVS Support  -- Mapping Exporter";

	String selectedServicerUrl = SERVICE_URL_PROD;

    public MappingExporter(String serviceUrl) {
        super(new BorderLayout());
        this.selectedServicerUrl = serviceUrl;
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
		JLabel label0 = new JLabel("Service Url:");
		gridbag.setConstraints(label0, c);

		c.gridx=1;
		c.gridy=0;
		serviceUrlList = new JComboBox(serviceUrls);
        Dimension preferredSize = serviceUrlList.getPreferredSize();
        preferredSize.width = 600;
        serviceUrlList.setPreferredSize(preferredSize);
		gridbag.setConstraints(serviceUrlList, c);
		panel.add(label0);
		serviceUrlList.addActionListener(this);
		serviceUrlList.setSelectedItem(selectedServicerUrl);
		panel.add(serviceUrlList);

		c.gridx=0;
		c.gridy=1;
		JLabel label1 = new JLabel("Mapping Coding Scheme:");
		gridbag.setConstraints(label1, c);
		c.gridx=1;
		c.gridy=1;
        try {
        	updateMappings(selectedServicerUrl);
		} catch (Exception ex) {

		}
		if (mappings == null) {
			System.out.println("mappings == null ");
			mappings = new String[0];
		}

		mappingList = new JComboBox(mappings);
        mappingList.setPreferredSize(preferredSize);
		gridbag.setConstraints(mappingList, c);
		panel.add(label1);
		mappingList.addActionListener(this);
		panel.add(mappingList);

		c.gridx=0;
		c.gridy=2;
		JLabel label2 = new JLabel("Outputfile (.csv):");
		gridbag.setConstraints(label2, c);
		c.gridx=1;
		c.gridy=2;
		outputFile = new JTextField(55);

		outputFile.setText("");

;       Object mapping = mappingList.getSelectedItem();
		String mappingCodingSchemeName = null;
		if (mapping != null) {
			mappingCodingSchemeName = mapping.toString();

			String metadata = (String) codingSchemeHashMap.get(mappingCodingSchemeName);
			Vector u = StringUtils.parseData(metadata, '|');
			String mapping_version = (String) u.elementAt(1);

			String filename = mappingCodingSchemeName + "_" + mapping_version;
			filename = filename.replaceAll(" ", "_");
			filename = filename + ".csv";
			outputFile.setText(filename);

	    }

		gridbag.setConstraints(outputFile, c);
		panel.add(label2);
		panel.add(outputFile);

		c.gridx=2;
		c.gridy=2;
		saveButton = new JButton("Select");
		saveButton.addActionListener(this);
		gridbag.setConstraints(saveButton, c);
		panel.add(saveButton);
		dialog.getContentPane().add(panel,BorderLayout.NORTH);

////////////////////////////////////////////////////////////////////////////////////////
        // Text Area
		textarea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textarea);
		scrollPane.setPreferredSize(new Dimension(800, 600));
		scrollPane.setViewportView(textarea);
		dialog.getContentPane().add(scrollPane,BorderLayout.CENTER);

////////////////////////////////////////////////////////////////////////////////////////
        // Button Panel
		JPanel buttonPanel = new JPanel();
		okButton = new JButton("Export to CSV");
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

	public void updateMappings(String serviceUrl) {
		output_file_generated = false;
		try {
			mapping_data = getMappingCodingSchemes(serviceUrl);
			codingSchemeHashMap = new HashMap();
			codingSchemeNames = new Vector();
			for (int i=0; i<mapping_data.size(); i++) {
				String line = (String) mapping_data.elementAt(i);
				Vector u = StringUtils.parseData(line, '|');
				String codingSchemeName = (String) u.elementAt(0);
				codingSchemeNames.add(codingSchemeName);
				codingSchemeHashMap.put(codingSchemeName, line);
			}
			codingSchemeNames = new SortUtils().quickSort(codingSchemeNames);
			mappings = new String[codingSchemeNames.size()];
			for (int i=0; i<codingSchemeNames.size(); i++) {
				String codingSchemeName = (String) codingSchemeNames.elementAt(i);
				mappings[i] = codingSchemeName;
			}
		} catch (Exception ex) {
			System.out.println("ERROR: Unable to retrieve mapping data from the server.");
		}
	}


    private void outputLine(String s)
	{
        if (textarea != null)
		{
			textarea.append(s + "\n");
		}
	}

   void loadFile(String filename)
   {
	    filename.trim();
		try
		{
			String s;
			BufferedReader inFile = new BufferedReader(new FileReader(filename));
			textarea.setText("");
			while ((s = inFile.readLine()) != null)
			{
				outputLine(s);
			}
			inFile.close();
			highlightText(outputFile.getText());

		}
		catch(Exception e)
		{
			System.err.println(e);
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
            okButton.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			Object mapping = mappingList.getSelectedItem();
			String mappingCodingSchemeName = mapping.toString();
			String metadata = (String) codingSchemeHashMap.get(mappingCodingSchemeName);
			Vector u = StringUtils.parseData(metadata, '|');
			String mapping_version = (String) u.elementAt(1);
			export_mapping_search_results(lbSvc, mappingCodingSchemeName, mapping_version);
			String outputfile = (String) outputFile.getText();
            if (!output_file_generated) {
			    JOptionPane.showMessageDialog(null, outputfile + " generated.", "Information",JOptionPane.INFORMATION_MESSAGE);
			    output_file_generated = true;
			}
            okButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        } else if (action == saveButton) {
			System.out.println("Save button pressed");
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Save File As...");
			int returnVal = chooser.showSaveDialog(dialog);
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = chooser.getSelectedFile();
				if (file.exists()) {
				    // If file already exists, ask before replacing it.
				    int action0 = JOptionPane.showConfirmDialog(this,
				                 "Replace existing file?");
					if (action0 != JOptionPane.YES_OPTION) return;
				}
				outputFile.setText(file.getAbsolutePath());
			}

        } else if (action == serviceUrlList) {
			JComboBox cb = (JComboBox) action;
	;       Object selectedServiceUrlObj = cb.getSelectedItem();
			String selectedServiceUrl = selectedServiceUrlObj.toString();
			//if (selectedServiceUrl.compareTo(serviceUrl) != 0) {
				serviceUrl = selectedServiceUrl;
				updateMappings((String) serviceUrl);
				//mappingList = new JComboBox(mappings);
				//dialog.show();
			//}
        } else if (action == mappingList) {
			JComboBox cb = (JComboBox) action;
	;       Object mapping = cb.getSelectedItem();
			String mappingCodingSchemeName = mapping.toString();

			String metadata = (String) codingSchemeHashMap.get(mappingCodingSchemeName);
			Vector u = StringUtils.parseData(metadata, '|');
			String mapping_version = (String) u.elementAt(1);

			String filename = mappingCodingSchemeName + "_" + mapping_version;
			filename = filename.replaceAll(" ", "_");
			filename = filename + ".csv";
			outputFile.setText(filename);

	    } else if (action == resetButton) {
			outputFile.setText("");
			textarea.setText("");
		}
    }

    private static void createAndShowGUI(String serviceUrl) {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new MappingExporter(serviceUrl);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public Vector getMappingCodingSchemes(String serviceUrl) {
		lbSvc = RemoteServerUtil.createLexBIGService(serviceUrl);
		CodingSchemeDataUtils csdu = new CodingSchemeDataUtils(lbSvc);
		Vector v = csdu.getMappingCodingSchemes();
		return v;
	}

	public String escapeCommaCharacters(String s) {
		if (s == null) return null;
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			if (c != ',') {
				buf.append(c);
			} else {
				buf.append(c).append(c);
			}
		}
		return buf.toString();
	}

    public void export_mapping_search_results(LexBIGService lbSvc, String mapping_schema, String mapping_version) {
        ResolvedConceptReferencesIterator iterator = new MappingUtils(lbSvc).getMappingDataIterator(mapping_schema, mapping_version);
		int numRemaining = 0;
		if (iterator != null) {
			try {
				numRemaining = iterator.numberRemaining();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		long ms = System.currentTimeMillis();
		PrintWriter pw = null;
		String outputfile = (String) outputFile.getText();
		try {
			pw = new PrintWriter(outputfile, "UTF-8");
	        StringBuffer sb = new StringBuffer();
			sb.append("Source Code,");
			sb.append("Source Name,");
			sb.append("Source Coding Scheme,");
			sb.append("Source Coding Scheme Version,");
			sb.append("Source Coding Scheme Namespace,");

			sb.append("Association Name,");
			sb.append("REL,");
			sb.append("Map Rank,");

			sb.append("Target Code,");
			sb.append("Target Name,");
			sb.append("Target Coding Scheme,");
			sb.append("Target Coding Scheme Version,");
			sb.append("Target Coding Scheme Namespace");
			//sb.append("\r\n");
			pw.println(sb.toString());

			MappingIteratorBean bean = new MappingIteratorBean(iterator);
            java.util.List<MappingData> list = bean.getData(0, numRemaining-1);
            if (list == null) return;
            for (int k=0; k<list.size(); k++) {
				sb = new StringBuffer();
				MappingData mappingData = (MappingData) list.get(k);
				sb.append("\"" + mappingData.getSourceCode() + "\",");
				sb.append("\"" + escapeCommaCharacters(mappingData.getSourceName()) + "\",");
				sb.append("\"" + mappingData.getSourceCodingScheme() + "\",");
				sb.append("\"" + mappingData.getSourceCodingSchemeVersion() + "\",");
				sb.append("\"" + mappingData.getSourceCodeNamespace() + "\",");

				sb.append("\"" + mappingData.getAssociationName() + "\",");
				sb.append("\"" + mappingData.getRel() + "\",");
				sb.append("\"" + mappingData.getScore() + "\",");

				sb.append("\"" + mappingData.getTargetCode() + "\",");
				sb.append("\"" + escapeCommaCharacters(mappingData.getTargetName()) + "\",");
				sb.append("\"" + mappingData.getTargetCodingScheme() + "\",");
				sb.append("\"" + mappingData.getTargetCodingSchemeVersion() + "\",");
				sb.append("\"" + mappingData.getTargetCodeNamespace() + "\"");
				//sb.append("\r\n");
				pw.println(sb.toString());
			}
		    System.out.println("Total run time (ms): " + (System.currentTimeMillis() - ms));

		} catch (Exception ex)	{
			System.out.println("ERROR: Export to CVS action failed.");
			ex.printStackTrace();
		} finally {
			try {
				pw.close();
				System.out.println("Output file " + outputfile + " generated.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return;
	}

    public static void main(String[] args) {
		String serviceUrl = args[0];
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(serviceUrl);
            }
        });

    }
}
