package gov.nih.nci.evs.service;

import java.io.*;
import java.util.*;
import java.net.*;
import java.text.*;


/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction
 * with the National Cancer Institute, and so to the extent government
 * employees are co-authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the disclaimer of Article 3,
 *      below. Redistributions in binary form must reproduce the above
 *      copyright notice, this list of conditions and the following
 *      disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution,
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIT and the National
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIT" must
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not
 *      authorize the recipient to use any trademarks owned by either NCI
 *      or NGIT
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT,
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author EVS Team
 * @version 1.0
 *
 *          Modification history Initial implementation kim.ong@ngc.com
 *
 */


public class RDFGenerator {
    public HashMap code2dataHashMap = null;
    public String datafile = null;
    public String owlfile = null;
    public HashSet subset_codes = null;
    public HashSet code_subsetcode_set = null;

	public RDFGenerator() {
	}

	public static Vector readFile(String filename)
	{
		Vector v = new Vector();
		try {
			BufferedReader in = new BufferedReader(
			   new InputStreamReader(
						  new FileInputStream(filename), "UTF8"));
			String str;
			while ((str = in.readLine()) != null) {
				v.add(str);
			}
            in.close();
		} catch (Exception ex) {
            ex.printStackTrace();
		}
		return v;
	}

    public static Vector parseData(String line, char delimiter) {
		if(line == null) return null;
		Vector w = new Vector();
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<line.length(); i++) {
			char c = line.charAt(i);
			if (c == delimiter) {
				w.add(buf.toString());
				buf = new StringBuffer();
			} else {
				buf.append(c);
			}
		}
		w.add(buf.toString());
		return w;
	}

	public static String encode(String term) {
		if (term == null) return null;
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<term.length(); i++) {
			char c = term.charAt(i);
			if (c == '<') {
				buf.append("&lt;");
			} else if (c == '>') {
				buf.append("&gt;");
			} else {
				buf.append(c);
			}
		}
		return buf.toString();
	}

	public HashMap getCode2dataHashMap(String datafile) {
		HashMap code2dataHashMap = new HashMap();
		Vector v = readFile(datafile);
		for (int i=1; i<v.size(); i++) {
			String line = (String) v.elementAt(i);
			Vector u = parseData(line, '\t');
			String code = (String) u.elementAt(0);
			Vector w = new Vector();
			if (code2dataHashMap.containsKey(code)) {
				w = (Vector) code2dataHashMap.get(code);
			}
			w.add(line);
			code2dataHashMap.put(code, w);
		}
		return code2dataHashMap;
	}

	public static String getToday() {
		return getToday("MM-dd-yyyy");
	}

	public static String getToday(String format) {
		java.util.Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public void writeHeader(PrintWriter out) {
		out.println("<?xml version=\"1.0\"?>");
		out.println("<rdf:RDF");
		out.println("    xmlns:mms=\"http://rdf.cdisc.org/mms#\"");
		out.println("    xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"");
		out.println("    xmlns=\"http://rdf.cdisc.org/cdash-terminology#\"");
		out.println("    xmlns:owl=\"http://www.w3.org/2002/07/owl#\"");
		out.println("    xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"");
		out.println("    xmlns:cts=\"http://rdf.cdisc.org/ct/schema#\"");
		out.println("    xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"");
		out.println("  xml:base=\"http://rdf.cdisc.org/cdash-terminology\">");
		out.println("  <owl:Ontology rdf:about=\"\">");
		out.println("    <owl:imports rdf:resource=\"http://rdf.cdisc.org/mms\"/>");
		out.println("    <owl:imports rdf:resource=\"http://rdf.cdisc.org/ct/schema\"/>");
		out.println("    <owl:versionInfo rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\"");
		out.println("    >Created with RDFGenerator</owl:versionInfo>");
		out.println("    <owl:imports rdf:resource=\"http://purl.org/dc/elements/1.1/\"/>");
		out.println("    <owl:imports rdf:resource=\"http://purl.org/dc/terms/\"/>");
		out.println("    <owl:imports rdf:resource=\"http://www.w3.org/2004/02/skos/core\"/>");
		out.println("  </owl:Ontology>");
	}

	public void writeFooter(PrintWriter out) {
		out.println("");
		out.println("</rdf:RDF>");
		out.println("");
		out.println("<!-- Created with RDFGenerator on " + getToday() + " -->");
		out.println("");
	}

	public void writePermissibleValue(PrintWriter out, String code) {
		Vector w = (Vector) code2dataHashMap.get(code);
		for (int i=0; i<w.size(); i++) {
			String line = (String) w.elementAt(i);
			Vector u = parseData(line, '\t');
			String subsetCode = (String) u.elementAt(1);

			String code_dot_subsetcode = code + "." + subsetCode;
			if (!code_subsetcode_set.contains(code_dot_subsetcode)) {
                code_subsetcode_set.add(code_dot_subsetcode);
				String cdiscDefinition = (String) u.elementAt(6);
				String nciPreferredTerm = (String) u.elementAt(7);
				String cdiscSynonyms = (String) u.elementAt(5);
				String cdiscSubmissionValue = (String) u.elementAt(4);
				String codelistName = (String) u.elementAt(3);
				out.println("  <mms:PermissibleValue rdf:ID=\"" + subsetCode + "." + code + "\">");
				if (!subset_codes.contains(subsetCode)) {
					writeInValueDomain(out, subsetCode);
					subset_codes.add(subsetCode);
				} else {
					out.println("    <mms:inValueDomain rdf:resource=\"#" + subsetCode + "\"/>");
				}

				out.println("    <cts:nciPreferredTerm rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\"");
				out.println("    >" + encode(nciPreferredTerm) + "</cts:nciPreferredTerm>");
				out.println("    <cts:nciCode rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\"");
				out.println("    >" + code + "</cts:nciCode>");
				out.println("    <cts:cdiscDefinition rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\"");
				out.println("    >" + encode(cdiscDefinition) + "</cts:cdiscDefinition>");
				if (cdiscSynonyms.length() > 0) {
					out.println("    <cts:cdiscSynonyms rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\"");
					out.println("    >" + encode(cdiscSynonyms) + "</cts:cdiscSynonyms>");
				}
				out.println("    <cts:cdiscSubmissionValue rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\"");
				out.println("    >" + encode(cdiscSubmissionValue) + "</cts:cdiscSubmissionValue>");
				out.println("  </mms:PermissibleValue>");
			}
		}
	}

	public void writeInValueDomain(PrintWriter out, String code) {
		Vector w = (Vector) code2dataHashMap.get(code);
		for (int i=0; i<w.size(); i++) {
			String line = (String) w.elementAt(i);
			Vector u = parseData(line, '\t');
			String cdiscDefinition = (String) u.elementAt(6);
			String nciPreferredTerm = (String) u.elementAt(7);
			String cdiscSynonyms = (String) u.elementAt(5);
			String cdiscSubmissionValue = (String) u.elementAt(4);
			String codelistName = (String) u.elementAt(3);
			String isExtensibleCodelist = (String) u.elementAt(2);
			if (isExtensibleCodelist.compareTo("Yes") == 0) {
				isExtensibleCodelist = "true";
			} else {
				isExtensibleCodelist = "false";
			}
			out.println("    <mms:inValueDomain>");
			out.println("      <mms:EnumeratedValueDomain rdf:ID=\"" + code + "\">");
			out.println("        <cts:cdiscDefinition rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\"");
			out.println("        >" + encode(cdiscDefinition) + "</cts:cdiscDefinition>");
			out.println("        <cts:nciPreferredTerm rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\"");
			out.println("        >" + encode(nciPreferredTerm) + "</cts:nciPreferredTerm>");
			out.println("        <cts:nciCode rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\"");
			out.println("        >" + code + "</cts:nciCode>");
			out.println("        <cts:cdiscSynonyms rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\"");
			out.println("        >" + encode(cdiscSynonyms) + "</cts:cdiscSynonyms>");
			out.println("        <cts:cdiscSubmissionValue rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\"");
			out.println("        >" + encode(cdiscSubmissionValue) + "</cts:cdiscSubmissionValue>");
			out.println("        <cts:codelistName rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\"");
			out.println("        >" + encode(codelistName) + "</cts:codelistName>");
			out.println("        <cts:isExtensibleCodelist rdf:datatype=\"http://www.w3.org/2001/XMLSchema#boolean\"");
			out.println("        >" + isExtensibleCodelist + "</cts:isExtensibleCodelist>");
			out.println("      </mms:EnumeratedValueDomain>");
			out.println("    </mms:inValueDomain>");
		}
	}

	public void generate(String datafile, String outputfile) {
		subset_codes = new HashSet();
		code_subsetcode_set = new HashSet();
        code2dataHashMap = getCode2dataHashMap(datafile);
        long ms = System.currentTimeMillis();
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(outputfile, "UTF-8");
	        writeHeader(pw);
			Vector codes = new Vector();
            Iterator it = code2dataHashMap.keySet().iterator();
	        while (it.hasNext()) {
				String key = (String) it.next();
				Vector w = (Vector) code2dataHashMap.get(key);
				for (int i=0; i<w.size(); i++) {
					String line = (String) w.elementAt(i);
					Vector u = parseData(line, '\t');
					String yesOrNo = (String) u.elementAt(2);
					if (yesOrNo != null && yesOrNo.length() == 0) {
						codes.add(key);
					}
				}
			}
			//codes = new SortUtils().quickSort(codes);
		    for (int i=0; i<codes.size(); i++) {
				String key = (String) codes.elementAt(i);
				writePermissibleValue(pw, key);
			}
            writeFooter(pw);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				pw.close();
				System.out.println("Output file " + outputfile + " generated.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("Total run time (ms): " + (System.currentTimeMillis() - ms));
	}

	public static void main(String[] args) {
		String datafile = args[0];
		String owlfile = args[1];
		RDFGenerator rdfGenerator = new RDFGenerator();
		rdfGenerator.generate(datafile, owlfile);
	}
}

