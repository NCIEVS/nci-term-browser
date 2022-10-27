package gov.nih.nci.evs.browser.utils;


import gov.nih.nci.evs.browser.common.*;
import java.io.*;
import java.text.*;
import java.util.*;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;


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


public class ExpressionFormatter {
//    static String Constants.INDENT_HALF = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
//    static String indent = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

    String TYPE_CATEGORY = "1";
    String TYPE_PARENT = "2";
    String TYPE_ROLE = "3";
    String TYPE_ROLE_GROUP = "4";

    LexBIGService lbSvc = null;

    //LexBIGService lbSvc = null;
    static HashSet valueDomainSet = null;
    UIUtils uiUtils = null;

    static {
		valueDomainSet = new HashSet();
		String[] lines = Constants.NCIT_ROLE_DATA;
		for (int i=0; i<lines.length; i++) {
			String line = (String) lines[i];
            Vector u = StringUtils.parseData(line);
            String range = (String) u.elementAt(3);
            valueDomainSet.add(range);
		}
		valueDomainSet.add("Parent");
	}

    public ExpressionFormatter(LexBIGService lbSvc) {
        uiUtils = new UIUtils(lbSvc);
	}

//<a href="ncitbrowser/ConceptReport.jsp?dictionary=NCI_Thesaurus&version=17.01e&code=C39679&ns=NCI_Thesaurus">Hallmark Cell</a>

    public String createHyperlink(String line) {
		if (line.indexOf("\t") == -1) return line;
		if (!line.endsWith(")")) return line;
		int n = line.lastIndexOf("(");
		if (n == -1) return line;
		String code = line.substring(n+1, line.length()-1);
		String name = line.substring(0, n-1);
		name = name.trim();
		n = name.indexOf("\t");
		String role = "";
		if (n != -1) {
		    role = name.substring(0, n);
		    role = role.trim();
		}
		name = name.substring(n+1, name.length());
        name = name.trim();
        /*
        StringBuffer buf = new StringBuffer();
        buf.append("<a href=\"ncitbrowser/ConceptReport.jsp?dictionary=NCI_Thesaurus");
        buf.append("&code=").append(code).append("&ns=NCI_Thesaurus\">").append(name).append("</a>");
		String hyperlink = buf.toString();
		*/
        String hyperlink = uiUtils.getHyperlink(name, code);

		if (role.compareTo("") == 0) {
			return Constants.INDENT + hyperlink;
		}
		return Constants.INDENT + role + Constants.INDENT + hyperlink;
	}

    public Vector formatLine(String line) {
		Vector w = new Vector();
		if (line.indexOf("Role group") != -1) {
			line = line.trim();
			w.add(TYPE_ROLE_GROUP);
			//w.add(line);
			w.add("Role Group(s):");
			return w;
		} else if (line.compareTo("\tor") == 0) {
			line = line.trim();
			w.add(TYPE_ROLE_GROUP);
			w.add(line);
			return w;
		}
		if (line.indexOf("\t") == -1) {
			w.add(TYPE_CATEGORY);
			w.add(line);
			return w;
		}
		if (!line.endsWith(")")) {
			w.add(TYPE_CATEGORY);
			w.add(line);
			return w;
		}
		int n = line.lastIndexOf("(");
		if (n == -1) {
			w.add(TYPE_CATEGORY);
			w.add(line);
			return w;
		}
		String code = line.substring(n+1, line.length()-1);
		String name = line.substring(0, n-1);
		name = name.trim();
		n = name.indexOf("\t");
		String role = "";
		if (n != -1) {
		    role = name.substring(0, n);
		    role = role.trim();
		}
		name = name.substring(n+1, name.length());
        name = name.trim();

		String hyperlink = uiUtils.getHyperlink(name, code);

		if (role.compareTo("") == 0) {
			w.add(TYPE_PARENT);
			w.add(hyperlink);
			return w;
		}
		w.add(TYPE_ROLE);
		w.add(role);
		w.add(hyperlink);
		return w;
	}


   	public String getRelationshipTableLabel(String type, boolean isEmpty) {
		String NONE = "<i>(none)</i>";
		StringBuffer buf = new StringBuffer();
		if (type.compareTo(Constants.TYPE_ROLE) == 0) {
			buf.append("<b>Role Relationships</b>,&nbsp;asserted or inherited, pointing from the current concept to other concepts:");
			if (isEmpty) {
				buf.append(" ").append(NONE).append("\n");
			} else {
				buf.append("<br/>").append("\n");
				//buf.append("<i>(True for the current concept.)</i>").append("\n");
				buf.append("<i>" + Constants.ROLE_DESCRIPTION_LABEL + "</i>").append("\n");
			}

		} else if (type.compareTo(Constants.TYPE_LOGICAL_DEFINITION) == 0) {
			buf.append("<b>Logical Definition</b>,&nbsp;showing the parent concepts and direct role assertions that define this concept:");
			if (isEmpty) {
				buf.append(" ").append(NONE).append("\n");
			} else {
				buf.append("<br/>").append("\n");
				//buf.append("<i>(True for the current concept.)</i>").append("\n");
			}
		} else if (type.compareTo(Constants.TYPE_INVERSE_ROLE) == 0) {
			buf.append("<b>Incoming Role Relationships</b>,&nbsp;asserted or inherited, pointing from other concepts to the current concept:");
			if (isEmpty) {
				buf.append(" ").append(NONE).append("\n");
			}
		}
		return buf.toString();
	}

    private String createTable(Vector columnHeadings, Vector columnWidths) {
		StringBuffer buf = new StringBuffer();
		buf.append("<table class=\"datatable_960\" border=\"0\" width=\"100%\">");
		buf.append("<tr>");
		for (int i=0; i<columnHeadings.size(); i++) {
			String s = (String) columnHeadings.elementAt(i);
 			if (i == 0) {
				buf.append("   <th class=\"dataCellText\" scope=\"col\" align=\"left\">" + Constants.INDENT_HALF + s + "</th>");
			} else {
				buf.append("   <th class=\"dataCellText\" scope=\"col\" align=\"left\">" + s + "</th>");
			}
		}
		buf.append("</tr>");
		for (int i=0; i<columnWidths.size(); i++) {
			Integer int_obj = (Integer) columnWidths.elementAt(i);
			buf.append("   <col width=\"" + int_obj.intValue() + "%\">");
		}
		return buf.toString();
	}

	public String reformat(String expression) {
		if (expression == null) return null;
        StringBuffer buf = new StringBuffer();
		String label = getRelationshipTableLabel(Constants.TYPE_LOGICAL_DEFINITION, false);
        buf.append(label);
        Vector columnHeadings = new Vector();
        columnHeadings.add("Relationship");
		columnHeadings.add("Value (qualifiers indented underneath)");
        Vector columnWidths = new Vector();

        columnWidths.add(new Integer(40));
        columnWidths.add(new Integer(60));
        String table = createTable(columnHeadings, columnWidths);
        buf.append(table);
		Vector v = gov.nih.nci.evs.browser.utils.StringUtils.parseData(expression, "\n");
		boolean role_group_start = false;
		for (int i=0; i<v.size(); i++) {
			String line = (String) v.elementAt(i);
				String category = line;
				category = category.trim();

				if (valueDomainSet.contains(category) || category.compareTo("Parent") == 0) {
					buf.append("<tr class=\"dataRowDark\">");
					buf.append("<td class=\"dataCellText\">\n");
					buf.append(Constants.INDENT_HALF + category);
					buf.append("</td><td>" + Constants.INDENT + "</td></tr>");

				} else {
					buf.append("<tr class=\"dataRowLight\">");
				}

				Vector w = formatLine(line);
				String type = (String) w.elementAt(0);
				if (type == null) return null;
				if (type.compareTo(TYPE_ROLE_GROUP) == 0) {
					String s = (String) w.elementAt(1);
					//if (start) {

						buf.append("<td class=\"dataCellText\" scope=\"row\" valign=\"top\">");
						buf.append(Constants.INDENT).append(s);
						buf.append("</td><td>" + Constants.INDENT + "</td></tr>");

					role_group_start = true;

				} else if (type.compareTo(TYPE_CATEGORY) == 0) {
					String s = (String) w.elementAt(1);
					if (s == null) return null;
					if (s.compareTo("or") != 0) {
						role_group_start = false;
					}
				} else if (type.compareTo(TYPE_PARENT) == 0) {
					String s = (String) w.elementAt(1);

						buf.append("<td class=\"dataCellText\" scope=\"row\" valign=\"top\">");
						buf.append(Constants.INDENT).append(s);
						buf.append("</td><td>" + Constants.INDENT + "</td></tr>");

				} else if (type.compareTo(TYPE_ROLE) == 0) {
					String s = (String) w.elementAt(1);
					String t = (String) w.elementAt(2);

					if (role_group_start) {
						//buf.append(Constants.INDENT);
					}
					buf.append("<td class=\"dataCellText\" scope=\"row\" valign=\"top\">");
					buf.append(Constants.INDENT).append(s);
					buf.append("</td><td>" + t + "</td></tr>");
				}
				buf.append("\n");
			//}
		}
		buf.append("</table>\n");
		return buf.toString();
	}
}


