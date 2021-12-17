package gov.nih.nci.evs.service;

import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping.*;

import gov.nih.nci.evs.browser.bean.*;
import gov.nih.nci.evs.browser.common.*;
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
import org.apache.logging.log4j.*;
import org.LexGrid.LexBIG.caCore.interfaces.*;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;

import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.custom.relations.*;

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

	static String serviceUrl = "null";
	static LexBIGService lbSvc = null;

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

	static String[] remoteServiceUrls = {
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
        LexBIGService lbSvc = createLexBIGService(serviceUrl);
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
            selectedServicerUrl = (String) serviceUrlList.getSelectedItem();
			Object mapping = mappingList.getSelectedItem();
			String mappingCodingSchemeName = mapping.toString();
			String metadata = (String) codingSchemeHashMap.get(mappingCodingSchemeName);
			Vector u = StringUtils.parseData(metadata, '|');
			String mapping_version = (String) u.elementAt(1);
			String outputfile = (String) outputFile.getText();
			if (selectedServicerUrl == null || selectedServicerUrl.compareTo("null") == 0) {
				System.out.println("Local mode...");
				export_mapping(mappingCodingSchemeName, mapping_version, outputfile);
			} else {
				System.out.println("Remote mode...");
				export_mapping(selectedServicerUrl, mappingCodingSchemeName, mapping_version, outputfile);
			}

            if (!output_file_generated) {
			    JOptionPane.showMessageDialog(null, outputfile + " generated.", "Information",JOptionPane.INFORMATION_MESSAGE);
			    output_file_generated = true;
			}

            okButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            return;

        } else if (action == saveButton) {
			System.out.println("Save button pressed");
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
				outputFile.setText(file.getAbsolutePath());
			}
			return;

        } else if (action == serviceUrlList) {
			JComboBox cb = (JComboBox) action;
	;       Object selectedServiceUrlObj = cb.getSelectedItem();
			String selectedServiceUrl = selectedServiceUrlObj.toString();
			serviceUrl = selectedServiceUrl;
			updateMappings((String) serviceUrl);

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
		return;
    }

    private static void createAndShowGUI(String serviceUrl) {
		System.out.println("Initializing ...-- please wait.");
		serviceUrl = serviceUrl;
		lbSvc = createLexBIGService(serviceUrl);
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

    public static Vector getMappingCodingSchemes(String serviceUrl) {
		LexBIGService lbSvc = createLexBIGService(serviceUrl);
		CodingSchemeDataUtils csdu = new CodingSchemeDataUtils(lbSvc);
		Vector v = csdu.getMappingCodingSchemes();
		return v;
	}

	public static String escapeCommaCharacters(String s) {
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

	/*
    public static void export_mapping(String serviceUrl, String mapping_schema, String mapping_version, String outputfile) {
		if (serviceUrl == null || serviceUrl.compareTo("null") == 0) {
			export_mapping(mapping_schema, mapping_version, outputfile);
		} else {
			LexBIGService lbSvc = createLexBIGService(serviceUrl);
			export_mapping(lbSvc, mapping_schema, mapping_version, outputfile);
		}
	}
	*/

    public static void export_mapping(String serviceUrl, String mapping_schema, String mapping_version, String outputfile) {
		if (serviceUrl == null || serviceUrl.compareTo("null") == 0) {
			export_mapping(mapping_schema, mapping_version, outputfile);
			return;
		}
		long ms = System.currentTimeMillis();
		LexBIGService lbSvc = createLexBIGService(serviceUrl);
        ResolvedConceptReferencesIterator _iterator = new MappingUtils(lbSvc).getMappingDataIterator(mapping_schema, mapping_version);
        System.out.println("Total getMappingDataIterator run time (ms): " + (System.currentTimeMillis() - ms));
        ms = System.currentTimeMillis();
		int numRemaining = 0;
		if (_iterator != null) {
			try {
				numRemaining = _iterator.numberRemaining();
                System.out.println("numRemaining: " + numRemaining);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
        System.out.println("Exporting mapping data to " + outputfile + ". Please wait...");
		String sourceCode = null;
		String sourceName = null;
		String sourceCodingScheme = null;
		String sourceCodingSchemeVesion = null;
		String sourceCodeNamespace = null;
		String rel = null;
		int score = 0;
		String targetCode = null;
		String targetName = null;
		String targetCodingScheme = null;
		String targetCodingSchemeVesion = null;
		String targetCodeNamespace = null;
		String description = null;
		String associationName = null;

		PrintWriter pw = null;
		//String outputfile = (String) outputFile.getText();
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
			pw.println(sb.toString());

			while (_iterator.hasNext()) {
				ResolvedConceptReference ref = _iterator.next();
				description = null;
				if(ref.getEntityDescription() == null) {
					description = "NOT AVAILABLE";
				} else {
					description = ref.getEntityDescription().getContent();
				}
				sourceCode = ref.getCode();
				sourceName = description;
				sourceCodingScheme = ref.getCodingSchemeName();
				sourceCodingSchemeVesion = ref.getCodingSchemeVersion();
				sourceCodeNamespace = ref.getCodeNamespace();
				rel = null;
				score = 0;

				AssociationList assocs = ref.getSourceOf();
				if(assocs != null){
					for(Association assoc : assocs.getAssociation()){
						associationName = assoc.getAssociationName();
						int lcv = 0;
						for(AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept()){
							lcv++;
							if(ac.getEntityDescription() == null) {
								description = "NOT AVAILABLE";
							} else {
								description = ac.getEntityDescription().getContent();
							}
							targetCode = ac.getCode();
							targetName = description;
							targetCodingScheme = ac.getCodingSchemeName();
							targetCodingSchemeVesion = ac.getCodingSchemeVersion();
							targetCodeNamespace = ac.getCodeNamespace();

							if (ac.getAssociationQualifiers() != null && ac.getAssociationQualifiers().getNameAndValue() != null) {
								for (NameAndValue qual : ac.getAssociationQualifiers().getNameAndValue()) {
									String qualifier_name = qual.getName();
									String qualifier_value = qual.getContent();
									if (qualifier_name.compareTo("rel") == 0) {
										rel = qualifier_value;
									} else if (qualifier_name.compareTo("score") == 0) {
										score = Integer.parseInt(qualifier_value);
									}
								}
							}

							MappingData mappingData = new MappingData(
								sourceCode,
								sourceName,
								sourceCodingScheme,
								sourceCodingSchemeVesion,
								sourceCodeNamespace,
								associationName,
								rel,
								score,
								targetCode,
								targetName,
								targetCodingScheme,
								targetCodingSchemeVesion,
								targetCodeNamespace);

							sb = new StringBuffer();
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
							pw.println(sb.toString());
						}
					}
				}

				assocs = ref.getTargetOf();
				if(assocs != null){
					for(Association assoc : assocs.getAssociation()){
						associationName = assoc.getAssociationName();

						int lcv = 0;
						for(AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept()){
							lcv++;
							if(ac.getEntityDescription() == null) {
								description = "NOT AVAILABLE";
							} else {
								description = ac.getEntityDescription().getContent();
							}
							targetCode = ac.getCode();
							targetName = description;
							targetCodingScheme = ac.getCodingSchemeName();
							targetCodingSchemeVesion = ac.getCodingSchemeVersion();
							targetCodeNamespace = ac.getCodeNamespace();

							if (ac.getAssociationQualifiers() != null && ac.getAssociationQualifiers().getNameAndValue() != null) {
								for (NameAndValue qual : ac.getAssociationQualifiers().getNameAndValue()) {
									String qualifier_name = qual.getName();
									String qualifier_value = qual.getContent();
									if (qualifier_name.compareTo("rel") == 0) {
										rel = qualifier_value;
									} else if (qualifier_name.compareTo("score") == 0) {
										score = Integer.parseInt(qualifier_value);
									}
								}
							}

							MappingData mappingData = new MappingData(
								sourceCode,
								sourceName,
								sourceCodingScheme,
								sourceCodingSchemeVesion,
								sourceCodeNamespace,
								associationName,
								rel,
								score,
								targetCode,
								targetName,
								targetCodingScheme,
								targetCodingSchemeVesion,
								targetCodeNamespace);


							sb = new StringBuffer();
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
							pw.println(sb.toString());
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("Total export run time (ms): " + (System.currentTimeMillis() - ms));
	}


    public static java.util.List<TerminologyMapBean> resolveBulkMapping(final String mappingName, String mappingVersion) {
		java.util.List<TerminologyMapBean> tmb_list = new MappingExtensionImpl().resolveBulkMapping(mappingName, mappingVersion);
		return tmb_list;
	}


    public static String getMappingMetadata(String serviceUrl, String mappingCodingScheme, String mappingCodingSchemeVersion) {
		LexBIGService lbSvc = createLexBIGService(serviceUrl);
		CodingSchemeDataUtils csdu = new CodingSchemeDataUtils(lbSvc);
		return csdu.getMappingMetadata(mappingCodingScheme, mappingCodingSchemeVersion);
	}

    public static String getMappingMetadata(String mappingCodingScheme, String mappingCodingSchemeVersion) {
		LexBIGService lbSvc = createLexBIGService(null);
		CodingSchemeDataUtils csdu = new CodingSchemeDataUtils(lbSvc);
		return csdu.getMappingMetadata(mappingCodingScheme, mappingCodingSchemeVersion);
	}

    public static void export_mapping(String mapping_schema, String mapping_version, String outputfile) {
		/*
		if (serviceUrl != null && remoteServiceUrl.contains(serviceUrl)) {
			export_mapping(serviceUrl, mapping_schema, mapping_version, outputfile);
			return;
		}
		*/
		long ms = System.currentTimeMillis();
		String metadata = getMappingMetadata(mapping_schema, mapping_version);
		Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(metadata);
		String sourceCodingScheme = (String) u.elementAt(0);
		String sourceCodingSchemeVersion = (String) u.elementAt(1);
		String targetCodingScheme = (String) u.elementAt(2);
		String targetCodingSchemeVersion = (String) u.elementAt(3);
        String associationName = (String) u.elementAt(4);
        java.util.List<TerminologyMapBean> tmb_list = resolveBulkMapping(mapping_schema, mapping_version);
        System.out.println("Total resolveBulkMapping run time (ms): " + (System.currentTimeMillis() - ms));
        ms = System.currentTimeMillis();
		int	numRemaining = tmb_list.size();
        System.out.println("numRemaining: " + numRemaining);
        System.out.println("Exporting mapping data to " + outputfile + ". Please wait...");
		String sourceCode = null;
		String sourceName = null;
		String sourceCodeNamespace = null;
		String rel = null;
		int score = 0;
		String targetCode = null;
		String targetName = null;
		String targetCodeNamespace = null;
		String description = null;

		PrintWriter pw = null;
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
			pw.println(sb.toString());

			for (int i=0; i<tmb_list.size(); i++) {
				TerminologyMapBean tmb = (TerminologyMapBean) tmb_list.get(i);
				sourceCode = tmb.getSourceCode();
				sourceName = tmb.getSourceName();
				sourceCodeNamespace = tmb.getSource();
				rel = tmb.getRel();
				score = Integer.parseInt(tmb.getMapRank());
				targetCode = tmb.getTargetCode();
				targetName = tmb.getTargetName();
				targetCodeNamespace = tmb.getTarget();
				sb = new StringBuffer();
				sb.append("\"" + sourceCode + "\",");
				sb.append("\"" + escapeCommaCharacters(sourceName) + "\",");
				sb.append("\"" + sourceCodingScheme + "\",");
				sb.append("\"" + sourceCodingSchemeVersion + "\",");
				sb.append("\"" + sourceCodeNamespace + "\",");
				sb.append("\"" + associationName + "\",");
				sb.append("\"" + rel + "\",");
				sb.append("\"" + score + "\",");
				sb.append("\"" + targetCode + "\",");
				sb.append("\"" + escapeCommaCharacters(targetName) + "\",");
				sb.append("\"" + targetCodingScheme + "\",");
				sb.append("\"" + targetCodingSchemeVersion + "\",");
				sb.append("\"" + targetCodeNamespace + "\"");
				pw.println(sb.toString());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("Total export run time (ms): " + (System.currentTimeMillis() - ms));
	}

    public static LexBIGService createLexBIGService(String serviceUrl) {
        try {
            if (serviceUrl == null || serviceUrl.compareTo("") == 0 || serviceUrl.compareToIgnoreCase("null") == 0) {
                LexBIGService lbSvc = LexBIGServiceImpl.defaultInstance();
                return lbSvc;
            }
            LexEVSApplicationService lexevsService =
                (LexEVSApplicationService) ApplicationServiceProvider
                    .getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");
            return (LexBIGService) lexevsService;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
		String serviceUrl = args[0];
		if (args.length == 1) {
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					createAndShowGUI(serviceUrl);
				}
			});
		} else {
			String mapping_schema = args[1];
			String mapping_version = args[2];
			String outputfile = args[3];
			MappingExporter.export_mapping(serviceUrl, mapping_schema, mapping_version, outputfile);
		}
    }

}

