package gov.nih.nci.evs.browser.utils;

//aria-labelledby=

import gov.nih.nci.evs.browser.bean.*;
import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.common.Constants;
import java.io.*;
import java.util.*;
import java.util.Enumeration;
import java.util.Map.Entry;
import org.apache.commons.lang.*;
import org.apache.logging.log4j.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.*;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
//import org.LexGrid.LexBIG.caCore.interfaces.LexEVSApplicationService;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeTagList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.descriptors.RenderingDetailDescriptor;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.RenderingDetail;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.naming.SupportedProperty;


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


public class UIUtils {
	private static Logger _logger = LogManager.getLogger(UIUtils.class);
    private LexBIGService lbSvc = null;
    private LexBIGServiceConvenienceMethods lbscm = null;
    private String indent = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    private List OWL_ROLE_QUALIFIER_LIST = null;
    private CodingSchemeDataUtils csdu = null;
    private ConceptDetails cd = null;

    private String NCIT_PRODUCTION_VERSION = null;

    private String codingScheme = null;
    private String version = null;

	public void set_owl_role_quantifiers(String owl_role_quantifiers) {
        OWL_ROLE_QUALIFIER_LIST = new ArrayList();
		if (owl_role_quantifiers == null) {
			return;
		}
		Vector v = gov.nih.nci.evs.browser.utils.StringUtils.parseData(owl_role_quantifiers);
		if (v == null) return;
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			OWL_ROLE_QUALIFIER_LIST.add(t);
		}
	}

	public void setCodingSchemeNameAndVersion(String codingScheme, String version) {
		this.codingScheme = codingScheme;
		this.version = version;
	}

	public UIUtils(LexBIGService lbSvc) {
		this.lbSvc = lbSvc;
		String codingScheme = Constants.NCIT_CS_NAME;
		String prod_version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(codingScheme, Constants.PRODUCTION);
		setCodingSchemeNameAndVersion(codingScheme, prod_version);
        try {
			cd = new ConceptDetails(lbSvc);
			csdu = new CodingSchemeDataUtils(lbSvc);
			NCIT_PRODUCTION_VERSION = csdu.getVocabularyVersionByTag(Constants.NCIT_CS_NAME, Constants.PRODUCTION);
            lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public UIUtils(LexBIGService lbSvc, String codingScheme, String version) {
        this.lbSvc = lbSvc;
        setCodingSchemeNameAndVersion(codingScheme, version);
        try {
			cd = new ConceptDetails(lbSvc);
			csdu = new CodingSchemeDataUtils(lbSvc);
			NCIT_PRODUCTION_VERSION = csdu.getVocabularyVersionByTag(Constants.NCIT_CS_NAME, Constants.PRODUCTION);
            lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

    public Vector getPropertiesByName(org.LexGrid.commonTypes.Property[] properties , String propertyName) {
		Vector v = new Vector();
        for (int i = 0; i < properties.length; i++) {
            Property p = (Property) properties[i];
            if (p == null) return null;
            if (p.getPropertyName().compareTo(propertyName) == 0) {
            	v.add(p);
			}
        }
        return v;
    }

    public void generatePropertyTable(Entity concept) {
		String prop_PRESENTATION = generatePropertyTable(concept, "PRESENTATION");
		//System.out.println(prop_PRESENTATION);

		String prop_DEFINITION = generatePropertyTable(concept, "DEFINITION");
		//System.out.println(prop_DEFINITION);

		String prop_COMMENT = generatePropertyTable(concept, "COMMENT");
		//System.out.println(prop_COMMENT);

		String prop_GENERIC = generatePropertyTable(concept, "GENERIC");
		//System.out.println(prop_GENERIC);
	}


    public String generatePropertyTable(Entity concept, String property_type) {
		if (concept == null) return null;
		if (property_type == null) return null;

		org.LexGrid.commonTypes.Property[] properties = null;

        if (property_type.compareToIgnoreCase("GENERIC") == 0) {
            properties = concept.getProperty();
        } else if (property_type.compareToIgnoreCase("PRESENTATION") == 0) {
            properties = concept.getPresentation();
        } else if (property_type.compareToIgnoreCase("COMMENT") == 0) {
            properties = concept.getComment();
        } else if (property_type.compareToIgnoreCase("DEFINITION") == 0) {
            properties = concept.getDefinition();
        }

		String description = property_type;
		String firstColumnHeading = "Name";
		String secondColumnHeading = "Value";
		int firstPercentColumnWidth = 20;
		int secondPercentColumnWidth = 80;
		int qualifierColumn = 2;

		Vector keyVec = new Vector();
		HashMap qualifierHashMap = new HashMap();
		if (properties == null) return null;
		int lcv = 0;

        for (int i = 0; i < properties.length; i++) {
            Property p = (Property) properties[i];
            if (p != null) {
				String name = p.getPropertyName();
				if (p.getValue() != null) {
					String value = p.getValue().getContent();
					//String n_v = name + "|" + value;
					String n_v = name + "$" + value + "$" + lcv;
					lcv++;
					Vector qualifier_vec = new Vector();
					PropertyQualifier[] qualifiers = p.getPropertyQualifier();
					if (qualifiers != null) {
						for (int j = 0; j < qualifiers.length; j++) {
							PropertyQualifier q = qualifiers[j];
							String qualifier_name = q.getPropertyQualifierName();
							String qualifier_value = q.getValue().getContent();
							String t = qualifier_name + "|" + qualifier_value;

							qualifier_vec.add(t);
						}
						keyVec.add(n_v);
						qualifier_vec = new SortUtils().quickSort(qualifier_vec);
						qualifierHashMap.put(n_v, qualifier_vec);
					}
				}
			}
		}

		keyVec = new SortUtils().quickSort(keyVec);

	    HTMLTableSpec spec = new HTMLTableSpec(
			 description,
			 firstColumnHeading,
			 secondColumnHeading,
			 firstPercentColumnWidth,
			 secondPercentColumnWidth,
			 qualifierColumn,
			 keyVec,
			 qualifierHashMap);

		return generateHTMLTable(spec);
	}

	public HTMLTableSpec relationshipList2HTMLTableSpec(
		String description,
		String firstColumnHeading,
		String secondColumnHeading,
		int firstPercentColumnWidth,
		int secondPercentColumnWidth,
		int qualifierColumn,
		ArrayList list) {

		Vector keyVec = new Vector();
		HashMap qualifierHashMap = new HashMap();

		if (list != null && list.size() > 0) {
		    for (int i=0; i<list.size(); i++) {
				String line = (String) list.get(i);
				Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(line);
				String name = (String) u.elementAt(0);
				String value = (String) u.elementAt(1);
				String code = null;
				String codingScheme = null;
				String namespace = null;
				String qualifiers = null;
				if (u.size() > 4) {
					code = (String) u.elementAt(2);
					codingScheme = (String) u.elementAt(3);
					namespace = (String) u.elementAt(4);
				}
				if (u.size() > 5) {
					qualifiers = (String) u.elementAt(5);
				}
				if (qualifiers != null) {
					String key = name + "|" + value + "|" + code + "|" + namespace;
					keyVec.add(key);
					Vector w = gov.nih.nci.evs.browser.utils.StringUtils.parseData(qualifiers, "$");
					Vector w2 = new Vector();
					for (int k=0; k<w.size(); k++) {
						String t = (String) w.elementAt(k);
						t = t.replaceAll("=", "|");
						w2.add(t);
					}
					qualifierHashMap.put(key, w2);
				} else {
					String key = name + "|" + value + "|" + code + "|" + namespace;
					keyVec.add(key);
					qualifierHashMap.put(key, new Vector());
				}
			}
	    }

	    return new HTMLTableSpec(
		    description,
		    firstColumnHeading,
		    secondColumnHeading,
		    firstPercentColumnWidth,
		    secondPercentColumnWidth,
		    qualifierColumn,
		    keyVec,
		    qualifierHashMap);

	}

    public String generateHTMLTable(HTMLTableSpec spec) {
		return generateHTMLTable(spec, null, null);
	}

	public boolean hasQualifiers(Vector qualifiers) {
		if (qualifiers == null || qualifiers.size() == 0) return false;
		for (int j = 0; j < qualifiers.size(); j++) {
			String q = (String) qualifiers.elementAt(j);
			if (q != null && q.length() > 0) return true;
		}
		return false;
	}

    public String generateHTMLTable(HTMLTableSpec spec, String codingScheme, String version) {
		if (spec == null) return null;
		return generateHTMLTable(spec, codingScheme, version, null);
	}

    public String generateHTMLTable(HTMLTableSpec spec, String codingScheme, String version, String rel_type) {
		if (spec == null) return null;
		StringBuffer buf = new StringBuffer();
		HashMap qualifierHashMap = spec.getQualifierHashMap();
		if (qualifierHashMap == null) return null;
		Vector nv_vec = spec.getKeyVec();
		if (nv_vec == null) {
			nv_vec = new Vector();
			Iterator entries = qualifierHashMap.entrySet().iterator();
			if (entries == null) return null;
			while (entries.hasNext()) {
				Entry thisEntry = (Entry) entries.next();
				String nv = (String) thisEntry.getKey();
				nv_vec.add(nv);
			}
			nv_vec = new SortUtils().quickSort(nv_vec);
		}
		String description = spec.getDescription();
		if (description != null) {
			buf.append(description).append("\n");
		}
		buf.append("<table class=\"datatable_960\" border=\"0\" width=\"100%\">").append("\n");

	    String firstColumnHeading = spec.getFirstColumnHeading();
	    String secondColumnHeading = spec.getSecondColumnHeading();
        if (firstColumnHeading != null && secondColumnHeading != null) {
			buf.append("<tr>").append("\n");
			buf.append("   <th class=\"dataCellText\" scope=\"col\" align=\"left\">" + Constants.INDENT_HALF + firstColumnHeading  + "</th>").append("\n");
			buf.append("   <th class=\"dataCellText\" scope=\"col\" align=\"left\">" + secondColumnHeading + "</th>").append("\n");
			buf.append("</tr>").append("\n");
	    }
        int firstPercentColumnWidth = spec.getFirstPercentColumnWidth();
        int secondPercentColumnWidth = spec.getSecondPercentColumnWidth();

        if (firstPercentColumnWidth <= 0 || firstPercentColumnWidth <= 0) {
			buf.append("   <col width=\"50%\">").append("\n");
			buf.append("   <col width=\"50%\">").append("\n");
		} else {
			String w1 = Integer.toString(firstPercentColumnWidth);
			String w2 = Integer.toString(secondPercentColumnWidth);
			buf.append("   <col width=\"" + w1 + "%\">").append("\n");
			buf.append("   <col width=\"" + w2 + "%\">").append("\n");
	    }

	    int qualifierColumn = spec.getQualifierColumn();
		int n = 0;
        for (int i = 0; i < nv_vec.size(); i++) {
            String n_v = (String) nv_vec.elementAt(i);
            //Vector w = gov.nih.nci.evs.browser.utils.StringUtils.parseData(n_v, '$');
            //KLO, 11032022
            Vector w = gov.nih.nci.evs.browser.utils.StringUtils.parseData(n_v, '|');

            String name = "";
            String value = "";

            if (w.size() > 0) {
            	name = (String) w.elementAt(0);
			}

			if (w.size() > 1) {
            	value = (String) w.elementAt(1);
			}
            String code = null;
            String namespace = null;
/*
            if (w.size() > 2) {
				code = (String) w.elementAt(2);
			}
			if (w.size() > 3) {
				namespace = (String) w.elementAt(3);
			}
*/
			if (w.size() > 3) {
				code = (String) w.elementAt(2);
				namespace = (String) w.elementAt(3);
			}

             Vector qualifiers = (Vector) qualifierHashMap.get(n_v);

			 boolean is_maps_to = false;

			 for (int i2=0; i2<qualifiers.size(); i2++) {
				 String t = (String) qualifiers.elementAt(i2);
				 if (t.indexOf("Target_Terminology") != -1) {
					 is_maps_to = true;
					 break;
				 }
			 }

			 if (is_maps_to) {
				 qualifiers = sort_maps_to_qualifiers(qualifiers);
			 } else {
				 qualifiers = new SortUtils().quickSort(qualifiers);
			 }

			if ((n++) % 2 == 0) {
				  buf.append("	<tr class=\"dataRowDark\">").append("\n");
			} else {
				  buf.append("	<tr class=\"dataRowLight\">").append("\n");
			}
			if (qualifierColumn == 0) {



                  if (rel_type == null || !rel_type.startsWith("type_inverse")) {
					  buf.append("<td class=\"dataCellText\" valign=\"top\">").append("\n");
					  buf.append(Constants.INDENT_HALF + name).append("\n");
					  buf.append("</td>").append("\n");
					  if (code != null) {
						  value = getHyperlink(codingScheme, version, value, code, namespace);
					  }
					  buf.append("<td class=\"dataCellText\" scope=\"row\" valign=\"top\">" + value + "</td>").append("\n");
				  } else {
					  if (code != null) {
						  value = getHyperlink(codingScheme, version, value, code, namespace);
					  }
					  buf.append("<td class=\"dataCellText\" valign=\"top\">").append("\n");
					  buf.append(Constants.INDENT_HALF + value).append("\n");
					  buf.append("</td>").append("\n");
					  buf.append("<td class=\"dataCellText\" scope=\"row\" valign=\"top\">" + name + "</td>").append("\n");
				  }

			} else if (qualifierColumn == 1) {
                if (hasQualifiers(qualifiers)) {
					if (code != null) {
						value = getHyperlink(codingScheme, version, value, code, namespace);
					}
					buf.append("	  <td class=\"dataCellText\" scope=\"row\" valign=\"top\">").append("\n");
					buf.append("		  <table>").append("\n");
					buf.append("			 <tr>");
					buf.append("<td class=\"dataCellText\">").append("\n");
					buf.append(Constants.INDENT_HALF + value).append("\n");
					buf.append("			 </td></tr>").append("\n");
					for (int j = 0; j < qualifiers.size(); j++) {
						String q = (String) qualifiers.elementAt(j);
						Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(q);

						String qualifier_name = "";
						String qualifier_value = "";

						if (u.size() > 0) {
							qualifier_name = (String) u.elementAt(0);
						}
						if (u.size() > 1) {
							qualifier_value = (String) u.elementAt(1);
						}

						if (displayQualifier(qualifier_name)) {
							String t = qualifier_name + ":" + qualifier_value;

							if (t == null) return null;
							if (t.length() > 1) {
								buf.append("			 <tr>").append("\n");
								buf.append("			 <td class=\"dataCellText\" >" + Constants.INDENT + t + "</td>").append("\n");
								buf.append("			 </tr>").append("\n");
							}
						}
					}

					buf.append("		  </table>").append("\n");
					buf.append("	  </td>").append("\n");
					buf.append("	  <td class=\"dataCellText\" scope=\"row\" valign=\"top\">" + name + "</td>").append("\n");
			    } else {
					if (code != null) {
						value = getHyperlink(codingScheme, version, value, code, namespace);
					}
					buf.append("	  <td class=\"dataCellText\" scope=\"row\" valign=\"top\">" + value + "</td>").append("\n");
					buf.append("	  <td class=\"dataCellText\" scope=\"row\" valign=\"top\">" + name + "</td>").append("\n");
				}
			} else if (qualifierColumn == 2) {
                if (hasQualifiers(qualifiers)) {
					if (code != null) {
						value = getHyperlink(codingScheme, version, value, code, namespace);
					}
					buf.append("	  <td class=\"dataCellText\" scope=\"row\" valign=\"top\">" + Constants.INDENT + name + "</td>").append("\n");

					buf.append("	  <td class=\"dataCellText\" scope=\"row\" valign=\"top\">").append("\n");
					buf.append("		  <table>").append("\n");
					buf.append("			 <tr>");
					buf.append("<td class=\"dataCellText\">").append("\n");
					//buf.append("				 " + value).append("\n");
					buf.append(Constants.INDENT_HALF + value).append("\n");
					buf.append("			 </td></tr>").append("\n");
					for (int j = 0; j < qualifiers.size(); j++) {
						String q = (String) qualifiers.elementAt(j);
						Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(q);

						String qualifier_name = "";
						String qualifier_value = "";

						if (u.size() > 0) {
							qualifier_name = (String) u.elementAt(0);
						}
						if (u.size() > 1) {
							qualifier_value = (String) u.elementAt(1);
						}


						if (displayQualifier(qualifier_name)) {
							String t = qualifier_name + ":" + qualifier_value;
							if (t == null) return null;
							if (t.length() > 1) {
								if (t.endsWith(":")) {
									t = t.substring(0, t.length()-1);
								}
								buf.append("			 <tr>").append("\n");
								buf.append("			 <td class=\"dataCellText\" >" + Constants.INDENT + t + "</td>").append("\n");
								buf.append("			 </tr>").append("\n");
							}
						}
					}
					buf.append("		  </table>").append("\n");
					buf.append("	  </td>").append("\n");

			    } else {
					if (code != null) {
						value = getHyperlink(codingScheme, version, value, code, namespace);
					}
					buf.append("	  <td class=\"dataCellText\" scope=\"row\" valign=\"top\">" + Constants.INDENT + name + "</td>").append("\n");
					buf.append("	  <td class=\"dataCellText\" scope=\"row\" valign=\"top\">" + value + "</td>").append("\n");
				}
			}
			buf.append("	</tr>").append("\n");
		}
		buf.append("</table>").append("\n");
        return buf.toString();
	}

    public String getHyperlink(String name, String code) {
        return getHyperlink(this.version, name, code);
    }

    public String getHyperlink(String version, String name, String code) {
		String ns = null;
		try {
			ns = cd.getNamespaceByCode(codingScheme, version, code);
		} catch (Exception ex) {
			ex.printStackTrace();
			ns = "ncit";
		}
        String hyperlink = getHyperlink(codingScheme, version, name, code, ns);
        return hyperlink;
    }

    public String getHyperlink(String codingScheme, String version, String name, String code, String ns) {
		if (Arrays.asList(Constants.NON_CONCEPT_TO_CONCEPT_ASSOCIATION).contains(name)) return name;

		StringBuffer buf = new StringBuffer();
		if (version != null) {
			if (gov.nih.nci.evs.browser.utils.StringUtils.isNullOrBlank(ns)) {
				buf.append("<a href=\"/ncitbrowser/ConceptReport.jsp?dictionary=" + codingScheme + "&version=" + version + "&code=" + code + "\">");
			} else {
				buf.append("<a href=\"/ncitbrowser/ConceptReport.jsp?dictionary=" + codingScheme + "&version=" + version + "&code=" + code + "&ns=" + ns + "\">");
			}
	    } else {
			if (gov.nih.nci.evs.browser.utils.StringUtils.isNullOrBlank(ns)) {
				buf.append("<a href=\"/ncitbrowser/ConceptReport.jsp?dictionary=" + codingScheme + "&code=" + code + "\">");
			} else {
				buf.append("<a href=\"/ncitbrowser/ConceptReport.jsp?dictionary=" + codingScheme + "&code=" + code + "&ns=" + ns + "\">");
			}
	    }
		buf.append(name).append("</a>").append("\n");
		return buf.toString();
    }

//https://nciterms.nci.nih.gov
    public String getHyperlink(String host, String codingScheme, String version, String name, String code, String ns) {
		if (Arrays.asList(Constants.NON_CONCEPT_TO_CONCEPT_ASSOCIATION).contains(name)) return name;

		StringBuffer buf = new StringBuffer();
		if (version != null) {
			if (gov.nih.nci.evs.browser.utils.StringUtils.isNullOrBlank(ns)) {
				buf.append("<a href=\"" + host + "/ncitbrowser/ConceptReport.jsp?dictionary=" + codingScheme + "&version=" + version + "&code=" + code + "\">");
			} else {
				buf.append("<a href=\"" + host + "/ncitbrowser/ConceptReport.jsp?dictionary=" + codingScheme + "&version=" + version + "&code=" + code + "&ns=" + ns + "\">");
			}
	    } else {
			if (gov.nih.nci.evs.browser.utils.StringUtils.isNullOrBlank(ns)) {
				buf.append("<a href=\"" + host + "/ncitbrowser/ConceptReport.jsp?dictionary=" + codingScheme + "&code=" + code + "\">");
			} else {
				buf.append("<a href=\"" + host + "/ncitbrowser/ConceptReport.jsp?dictionary=" + codingScheme + "&code=" + code + "&ns=" + ns + "\">");
			}
	    }
		buf.append(name).append("</a>").append("\n");
		return buf.toString();
    }


	public String getRelationshipTableLabel(String defaultLabel, String type, boolean isEmpty) {
		String NONE = "<i>(none)</i>";
		StringBuffer buf = new StringBuffer();

		if (type.compareTo(Constants.TYPE_SUPERCONCEPT) == 0) {
			buf.append("<b>Parent Concepts:</b>");
			if (isEmpty) {
				buf.append(" ").append(NONE).append("\n");
			}
		} else if (type.compareTo(Constants.TYPE_SUBCONCEPT) == 0) {
			buf.append("<b>Child Concepts:</b>");
			if (isEmpty) {
				buf.append(" ").append(NONE).append("\n");
			}
		} else if (type.compareTo(Constants.TYPE_ROLE) == 0) {
			buf.append("<b>Role Relationships</b>&nbsp;pointing from the current concept to other concepts:");
			if (isEmpty) {
				buf.append(" ").append(NONE).append("\n");
			} else {
				buf.append("<br/>").append("\n");
//    public static final String ROLE_DESCRIPTION_LABEL = "(True for the current concept and its descendants, may be inherited from parent(s).)";
				//buf.append("<i>(True for the current concept.)</i>").append("\n");
				buf.append("<i>" + Constants.ROLE_DESCRIPTION_LABEL + "</i>").append("\n");
			}

		} else if (type.compareTo(Constants.TYPE_ASSOCIATION) == 0) {
			buf.append("<b>Associations</b>&nbsp;pointing from the current concept to other concepts:");
			if (isEmpty) {
				buf.append(" ").append(NONE).append("\n");
			} else {
				buf.append("<br/>").append("\n");
				buf.append("<i>(True for the current concept.)</i>").append("\n");
			}
		} else if (type.compareTo(Constants.TYPE_INVERSE_ROLE) == 0) {
			buf.append("<b>Incoming Role Relationships</b>&nbsp;pointing from other concepts to the current concept:");
			if (isEmpty) {
				buf.append(" ").append(NONE).append("\n");
			}
		} else if (type.compareTo(Constants.TYPE_INVERSE_ASSOCIATION) == 0) {
			buf.append("<b>Incoming Associations</b>&nbsp;pointing from other concepts to the current concept:");
			if (isEmpty) {
				buf.append(" ").append(NONE).append("\n");
			}
		} else if (type.compareTo(Constants.TYPE_LOGICAL_DEFINITION) == 0) {
			buf.append("<b>Logical Definition</b>,&nbsp;showing the parent concepts and direct role assertions that define this concept:");
			if (isEmpty) {
				buf.append(" ").append(NONE).append("\n");
			} else {
				buf.append("<br/>").append("\n");
			}
		}
		String label = buf.toString();
		if (label == null) return null;
		if (label.length() == 0) {
			label = defaultLabel;
		}
	    return label;
    }




    public String generatePropertyTable(Entity concept, Vector property_names, String description) {
		int qualifierColumn = 1;
        return generatePropertyTable(concept, property_names, description, qualifierColumn);
	}

//KLO
    public String generatePropertyTable(Entity concept, Vector property_names, String description, int qualifierColumn) {
		if (concept == null) return null;
		if (property_names == null) return null;

		org.LexGrid.commonTypes.Property[] properties = null;
		properties = concept.getAllProperties();
		if (properties == null) return null;

		String firstColumnHeading = "Name";
		String secondColumnHeading = "Value";
		secondColumnHeading = "Value (qualifiers indented underneath)";

		int firstPercentColumnWidth = 20;
		int secondPercentColumnWidth = 80;

		Vector keyVec = new Vector();
		HashMap qualifierHashMap = new HashMap();
		int lcv = 0;

        for (int i = 0; i < properties.length; i++) {
            Property p = (Property) properties[i];
			String name = p.getPropertyName();
			if (property_names.contains(name)) {
				String value = p.getValue().getContent();
				//String n_v = name + "$" + value;
				String n_v = name + "$" + value + "$" + lcv;

				System.out.println("n_v: " + n_v);


				lcv++;
				Vector qualifier_vec = new Vector();
				PropertyQualifier[] qualifiers = p.getPropertyQualifier();
				if (qualifiers == null) return null;
				for (int j = 0; j < qualifiers.length; j++) {
					PropertyQualifier q = qualifiers[j];
					String qualifier_name = q.getPropertyQualifierName();
					String qualifier_value = q.getValue().getContent();
					String t = qualifier_name + "|" + qualifier_value;

					System.out.println("t: " + t);

					qualifier_vec.add(t);
				}

				//Utils.dumpVector("qualifier_vec", qualifier_vec);
				keyVec.add(n_v);
				qualifier_vec = new SortUtils().quickSort(qualifier_vec);
				qualifierHashMap.put(n_v, qualifier_vec);
			}
		}
		keyVec = new SortUtils().quickSort(keyVec);

		//Utils.dumpVector("keyVec", keyVec);

	    HTMLTableSpec spec = new HTMLTableSpec(
			 description,
			 firstColumnHeading,
			 secondColumnHeading,
			 firstPercentColumnWidth,
			 secondPercentColumnWidth,
			 qualifierColumn,
			 keyVec,
			 qualifierHashMap);

		//return generateHTMLTable(spec);
		return generatePropertyTable(spec, null, null);
	}

    public Vector sort_maps_to_qualifiers(Vector qualifiers) {
		if (qualifiers == null) return null;
		Vector w = new Vector();
		HashMap hmap = new HashMap();
		for (int j = 0; j < qualifiers.size(); j++) {
			String q = (String) qualifiers.elementAt(j);
			Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(q);
			String qualifier_name = "";
			String qualifier_value = "";
			if (u.size() > 0) {
				qualifier_name = (String) u.elementAt(0);
			}
			if (u.size() > 1) {
				qualifier_value = (String) u.elementAt(1);
				qualifier_value = qualifier_value.trim();
				if (qualifier_value.endsWith(":")) {
					qualifier_value = qualifier_value.substring(0, qualifier_value.length()-1);
				}
			}
			hmap.put(qualifier_name, qualifier_value);
		}
		Vector keys = getMapsToQualifierNames();
		for (int i=0; i<keys.size(); i++) {
			String key = (String) keys.elementAt(i);
			String value = (String) hmap.get(key);
			if (value != null) {
				value = value.trim();
				if (value.endsWith(":")) {
					value = value.substring(0, value.length()-1);
				}
				w.add(key + ":" + value);
		    }
		}
		return w;
	}

    public String generatePropertyTable(HTMLTableSpec spec, String codingScheme, String version) {
		if (spec == null) return null;
		StringBuffer buf = new StringBuffer();
		HashMap qualifierHashMap = spec.getQualifierHashMap();
		if (qualifierHashMap == null) return null;
		Vector nv_vec = spec.getKeyVec();
		if (nv_vec == null) {
			nv_vec = new Vector();
			Iterator entries = qualifierHashMap.entrySet().iterator();
			if (entries == null) return null;
			while (entries.hasNext()) {
				Entry thisEntry = (Entry) entries.next();
				String nv = (String) thisEntry.getKey();
				nv_vec.add(nv);
			}
			nv_vec = new SortUtils().quickSort(nv_vec);
		}
		String description = spec.getDescription();
		if (description != null) {
			buf.append(description).append("\n");
		}
		buf.append("<table class=\"datatable_960\" border=\"0\" width=\"100%\">").append("\n");

	    String firstColumnHeading = spec.getFirstColumnHeading();
	    String secondColumnHeading = spec.getSecondColumnHeading();
        if (firstColumnHeading != null && secondColumnHeading != null) {
			buf.append("<tr>").append("\n");
			buf.append("   <th class=\"dataCellText\" scope=\"col\" align=\"left\">" + Constants.INDENT_HALF + firstColumnHeading  + "</th>").append("\n");
			buf.append("   <th class=\"dataCellText\" scope=\"col\" align=\"left\">" + secondColumnHeading + "</th>").append("\n");
			buf.append("</tr>").append("\n");
	    }
        int firstPercentColumnWidth = spec.getFirstPercentColumnWidth();
        int secondPercentColumnWidth = spec.getSecondPercentColumnWidth();

        if (firstPercentColumnWidth <= 0 || firstPercentColumnWidth <= 0) {
			buf.append("   <col width=\"50%\">").append("\n");
			buf.append("   <col width=\"50%\">").append("\n");
		} else {
			String w1 = Integer.toString(firstPercentColumnWidth);
			String w2 = Integer.toString(secondPercentColumnWidth);
			buf.append("   <col width=\"" + w1 + "%\">").append("\n");
			buf.append("   <col width=\"" + w2 + "%\">").append("\n");
	    }

	    int qualifierColumn = spec.getQualifierColumn();
		int n = 0;
        for (int i = 0; i < nv_vec.size(); i++) {
            String n_v = (String) nv_vec.elementAt(i);

            //System.out.println("UIUtils n_v: " + n_v);

            Vector w = gov.nih.nci.evs.browser.utils.StringUtils.parseData(n_v, '$');

            //Utils.dumpVector(n_v, w);

            String name = "";
            String value = "";
            String lcv_str = "";
            String code = null;
            String namespace = null;

            name = (String) w.elementAt(0);
            lcv_str = (String) w.elementAt(w.size()-1);
            StringBuffer b = new StringBuffer();
            for (int k=1; k<w.size()-1; k++) {
				b.append((String) w.elementAt(k)).append("$");
			}
			value = b.toString();
			value = value.substring(0, value.length()-1);

/*
            if (w.size() > 0) {
            	name = (String) w.elementAt(0);

            	System.out.println("name: " + name);
			}

			if (w.size() > 1) {
            	value = (String) w.elementAt(1);
			}
            String code = null;
            String namespace = null;

			if (w.size() > 3) {
				code = (String) w.elementAt(2);
				namespace = (String) w.elementAt(3);
			}
*/

            Vector qualifiers = (Vector) qualifierHashMap.get(n_v);

            /////////////////////////////////////////////////////////////////////////
            //KLO 03222018
            if (name.compareTo(MAPS_TO) == 0) {
				qualifiers = sort_maps_to_qualifiers(qualifiers);
			} else {
            	qualifiers = new SortUtils().quickSort(qualifiers);
			}

			n++;

			//if ((n++) % 2 == 0) {
			if (n % 2 == 0) {
				  buf.append("	<tr class=\"dataRowDark\">").append("\n");
			} else {
				  buf.append("	<tr class=\"dataRowLight\">").append("\n");
			}

            if (qualifierColumn == 1) {
                if (hasQualifiers(qualifiers)) {
					buf.append("	  <td class=\"dataCellText\" scope=\"row\">").append("\n");

					buf.append("		  <table>").append("\n");
					buf.append("			 <tr>");
					buf.append("<td class=\"dataCellText\" valign=\"top\">").append("\n");
					buf.append(Constants.INDENT_HALF + name).append("\n");
					buf.append("			 </td></tr>").append("\n");

					for (int j = 0; j < qualifiers.size(); j++) {
						String q = (String) qualifiers.elementAt(j);
						Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(q);

						String qualifier_name = "";
						String qualifier_value = "";

						if (u.size() > 0) {
							qualifier_name = (String) u.elementAt(0);
						}
						if (u.size() > 1) {
							qualifier_value = (String) u.elementAt(1);
						}

						if (displayQualifier(qualifier_name)) {
							String t = qualifier_name + ":&nbsp;&nbsp;&nbsp;" + qualifier_value;
							if (t.length() > 1) {
								buf.append("			 <tr>").append("\n");
								buf.append("			 <td class=\"dataCellText\" >" + indent + t + "</td>").append("\n");
								buf.append("			 </tr>").append("\n");
							}
					    }
					}

					buf.append("		  </table>").append("\n");
					buf.append("	  </td>").append("\n");
					if (code != null) {
						value = getHyperlink(codingScheme, version, value, code, namespace);
					}
					buf.append("	  <td class=\"dataCellText\" scope=\"row\">" + value + "</td>").append("\n");
			    } else {
					buf.append("	  <td class=\"dataCellText\" scope=\"row\">" + name + "</td>").append("\n");
					if (code != null) {
						value = getHyperlink(codingScheme, version, value, code, namespace);
					}
					buf.append("	  <td class=\"dataCellText\" scope=\"row\">" + value + "</td>").append("\n");
				}
			}

            if (qualifierColumn == 2) {

                if (hasQualifiers(qualifiers)) {
					buf.append("	  <td class=\"dataCellText\" scope=\"row\" valign=\"top\">" + name + "</td>").append("\n");
					if (code != null) {
						value = getHyperlink(codingScheme, version, value, code, namespace);
					}

					buf.append("	  <td class=\"dataCellText\" scope=\"row\">").append("\n");
					buf.append(value).append("\n");
					buf.append("	 </td></tr>").append("\n");

					//buf.append("<tr>").append("\n");


			if (n % 2 == 0) {
				  buf.append("	<tr class=\"dataRowDark\">").append("\n");
			} else {
				  buf.append("	<tr class=\"dataRowLight\">").append("\n");
			}


					buf.append("<td class=\"dataCellText\" scope=\"row\" valign=\"top\"></td>").append("\n");
					buf.append("<td class=\"dataCellText\" scope=\"row\">").append("\n");
							buf.append("		  <table>").append("\n");
							for (int j = 0; j < qualifiers.size(); j++) {
								String q = (String) qualifiers.elementAt(j);
								Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(q);

								String qualifier_name = "";
								String qualifier_value = "";

								if (u.size() > 0) {
									qualifier_name = (String) u.elementAt(0);
								}
								if (u.size() > 1) {
									qualifier_value = (String) u.elementAt(1);
									qualifier_value = qualifier_value.trim();
									if (qualifier_value.endsWith(":")) {
										qualifier_value = qualifier_value.substring(0, qualifier_value.length()-1);
									}
								}

								String t = qualifier_name + ":" + qualifier_value;
								if (t.endsWith(":")) {
									t = t.substring(0, t.length()-1);
								}

								int m = t.indexOf(":");
								if (m != -1) {
									t = t.substring(0, m) + ":&nbsp;" + t.substring(m+1, t.length());
								}

								if (t.length() > 1) {
									buf.append("			 <tr>").append("\n");
									buf.append("			 <td class=\"dataCellText\" >" + indent + t + "</td>").append("\n");
									buf.append("			 </tr>").append("\n");
								}
							}
							buf.append("		  </table>").append("\n");
					buf.append("	  </td>").append("\n");
			    } else {
					buf.append("	  <td class=\"dataCellText\" scope=\"row\">" + name + "</td>").append("\n");
					if (code != null) {
						value = getHyperlink(codingScheme, version, value, code, namespace);
					}
					buf.append("	  <td class=\"dataCellText\" scope=\"row\">" + value + "</td>").append("\n");
				}
			}
			buf.append("	</tr>").append("\n");
		}
		buf.append("</table>").append("\n");
        return buf.toString();
	}

    public boolean displayQualifier(String qualifierName) {
        if (qualifierName == null) return false;
        if (qualifierName.length() == 0) return false;
		//if (Constants.OWL_ROLE_QUALIFIER_LIST.contains(qualifierName)) {
		if (OWL_ROLE_QUALIFIER_LIST != null && OWL_ROLE_QUALIFIER_LIST.contains(qualifierName)) {
			return false;
		}
		return true;
	}

	public static Boolean isEven(Integer i) {
		return (i % 2) == 0;
	}

	public String generateCheckBoxes(Vector labels) {
        StringBuffer buf = new StringBuffer();
        buf.append("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\n");

        Boolean is_even = isEven(new Integer(labels.size()));
        int num_rows = labels.size() / 2;
        if (!is_even) {
			num_rows++;
			labels.add("");
		}

		String label = null;

		for (int i=0; i<num_rows; i++) {
			int row_num = i;
			String label_1 = (String) labels.elementAt(2*row_num);
			String label_2 = (String) labels.elementAt(2*row_num + 1);
			buf.append("<tr>\n");
			buf.append("\t<td class=\"dataCellText\">\n");

			String displayed_label = label_1;
			int n = displayed_label.indexOf("(*)");
			if (n != -1) {
				label = displayed_label.substring(0, n);
				label = label.trim();
				displayed_label = label + " (<font size=\"2\" color=\"red\">*</font>)";
			}

			if (label_1.length() > 0) {

				buf.append("\t<input type=\"checkbox\" aria-labelledby=\"" + label + "\" id=\"" + label + "\"  name=\"" + label + "\" >" + displayed_label + "</input>\n");
				buf.append("\t</td>");
				buf.append("\t<td class=\"dataCellText\">\n");
		    }
			displayed_label = label_2;
			n = displayed_label.indexOf("(*)");
			if (n != -1) {
				label = displayed_label.substring(0, n);
				label = label.trim();
				displayed_label = label + " (<font size=\"2\" color=\"red\">*</font>)";
			}

            if (label_2.length() > 0) {
				buf.append("\t<input type=\"checkbox\" aria-labelledby=\"" + label + "\" id=\"" + label + "\"  name=\"" + label + "\" >" + displayed_label + "</input>\n");
				buf.append("\t</td>\n");
				buf.append("</tr>\n");
		    }
		}
		buf.append("</table>");
		return buf.toString();
	}

	public static Vector removeDuplicateValues(Vector v) {
		if (v == null) return null;
		HashSet hset = new HashSet();
		Vector w = new Vector();
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			if (t == null) return null;
			if (!hset.contains(t)) {
				hset.add(t);
				w.add(t);
			}
		}
		return w;
	}

	public static String createTable(Vector w) {
		return createTable(w, true);
	}


	public static String createTable(Vector w, boolean removeDuplicate) {
		if (w == null) return null;
		if (removeDuplicate) {
			w = removeDuplicateValues(w);
		}
        StringBuffer buf = new StringBuffer();
		buf.append("<table>");
		for (int i=0; i<w.size(); i++) {
			//int j = i+1;
			String value = (String) w.elementAt(i);
			boolean isEven = UIUtils.isEven(i);
			if (isEven) {
				buf.append("<tr class=\"dataRowDark\">");
			} else {
				buf.append("<tr class=\"dataRowLight\">");
			}
			buf.append("<td valign=\"top\">");
			buf.append(value);
			buf.append("</td>");
			buf.append("</tr>");
		}
		buf.append("</table>").append("\n");
		return buf.toString();
	}

	public static String delimitedString2Table(String line, char c) {
		String delim = null;
		delim = "" + c;
		Vector w = gov.nih.nci.evs.browser.utils.StringUtils.parseData(line, delim);
		return createTable(w);
	}


	public String getOtherMappingString(String page_url, Vector target_terminologies, String ncit_version) {
		CodingSchemeDataUtils csdu = new CodingSchemeDataUtils(lbSvc);

		StringBuffer buf = new StringBuffer();
		buf.append("<hr></hr><p></p>").append("\n");
        buf.append("<table class=\"termstable_960\" border=\"0\">").append("\n");
        buf.append("<tr><td class=\"textbody\">Other mappings available for download:</td><td></td></tr>").append("\n");

		//Vector v = FTPDownload.extractMappingsFromURL(page_url);
		//Vector v = FTPDownload.getOtherMappingData();
		Vector v = FTPCrawler.getOtherMappingData();

		//Utils.dumpVector("OtherMappingData", v);

		if (v == null) return null;
		for (int i=0; i<v.size(); i++) {
			String line = (String) v.elementAt(i);
			Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(line, '|');
			String cs_name = (String) u.elementAt(0);
			CodingScheme cs = csdu.resolveCodingScheme(cs_name);
			if (cs == null) {
				String name = (String) u.elementAt(1);
				String display_name = (String) u.elementAt(2);
				String url = (String) u.elementAt(3);
				buf.append("<tr>").append("\n");
				buf.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append("\n");
				buf.append("<a href=\"/ncitbrowser/ajax?action=exportMapping&uri=" + url + "\">").append("\n");
				buf.append(	name + ": " + display_name ).append("\n");
				buf.append("</a>").append("\n");

				if (url.endsWith(".xls")) {
					  buf.append("&nbsp;&nbsp;");
					  buf.append("<a title=\"Download Plugin Microsoft Excel Viewer\" href=\"https://products.office.com/en-US/excel?legRedir=true&CorrelationId=1229dc2e-5ff3-4e3b-adc8-2b6f59e21be4\" target=\"_blank\"><img").append("\n");
					  buf.append("     src=\"/ncitbrowser/images/link_xls.gif\" width=\"16\"").append("\n");
					  buf.append("     height=\"16\" border=\"0\"").append("\n");
					  buf.append("alt=\"Download Plugin Microsoft Excel Viewer\" /></a>").append("\n");
				}

				buf.append("</td>").append("\n");
				buf.append("<td>&nbsp;</td>").append("\n");
				buf.append("</tr>").append("\n");
			}
		}

		if (target_terminologies != null) {
			System.out.println("target_terminologies.size(): " + target_terminologies.size());
		}

        if (target_terminologies != null && target_terminologies.size() > 0) {
			for (int i=0; i<target_terminologies.size(); i++) {
				String terminology = (String) target_terminologies.elementAt(i);
				buf.append("<tr>").append("\n");
				buf.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append("\n");
				buf.append("<a href=\"/ncitbrowser/ajax?action=export_maps_to_mapping&target=" + terminology + "\">").append("\n");
				buf.append(	MapsToReportProcessor.getMapsToMappingyDisplayName(terminology, ncit_version)).append("\n");
				buf.append("</a>").append("\n");
				buf.append("</td>").append("\n");
				buf.append("<td>&nbsp;</td>").append("\n");
				buf.append("</tr>").append("\n");
			}
	    }

		buf.append("</table>");
        return buf.toString();
	}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String removeBackSlashChars(String line) {
		if (line == null) return null;
		StringBuffer buf = new StringBuffer();;
		for (int i=0; i<line.length(); i++) {
			char c = line.charAt(i);
			if (c != '\\') {
				buf.append(c);
			}
		}
		return buf.toString();
	}


	public static String otherProperty2HTMLString(String propertyName, String propertyValue, HashMap qualifierMap, Vector keys) {
		//KLO
	    return otherProperty2HTMLString(propertyName, propertyValue, qualifierMap, keys, false);
    }

	public static String otherProperty2HTMLString(String propertyName, String propertyValue, HashMap qualifierMap, Vector keys, boolean inner_qualifier_table_only) {
		if (propertyName == null || propertyValue == null || qualifierMap == null) return null;
		StringBuffer buf = new StringBuffer();
		if (!inner_qualifier_table_only) {
			buf.append("<tr class=\"dataRowLight\">").append("\n");
			buf.append("<td class=\"dataCellText\" scope=\"row\" valign=\"top\">" + propertyName + "</td>").append("\n");
			buf.append("<td class=\"dataCellText\" scope=\"row\">" + propertyValue + "</td>").append("\n");
			buf.append("</tr>").append("\n");
			buf.append("<tr class=\"dataRowLight\">").append("\n");
			buf.append("<td class=\"dataCellText\" scope=\"row\" valign=\"top\"></td>").append("\n");
			buf.append("<td class=\"dataCellText\" scope=\"row\">").append("\n");
		}
        buf.append("<table>").append("\n");
        if (keys == null) {
			keys = new Vector();
			Iterator it = qualifierMap.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				keys.add(key);
			}
		}

		for (int i=0; i<keys.size(); i++) {
			String key = (String) keys.elementAt(i);
			String value = (String) qualifierMap.get(key);
			if (value != null) {
				buf.append("<tr>").append("\n");
				//String target_terminology = (String) qualifierMap.get(TARGET_TERMINOLOGY);
				buf.append("<td class=\"dataCellText\">" + NBSP + "" + key + ":&nbsp;" + value + "</td>").append("\n");
				buf.append("</tr>").append("\n");
			}
		}
        buf.append("</table>").append("\n");
        if (!inner_qualifier_table_only) {
			buf.append("</td>").append("\n");
			buf.append("</tr>").append("\n");
		}
        return buf.toString();
	}

    private static final String MAPS_TO = "Maps_To";
	private static final String TARGET_TERMINOLOGY = "Target_Terminology";
	private static final String TARGET_TERMINOLOGY_VERSION = "Target_Terminology_Version";
	private static final String RELATIONSHIP_TO_TARGET = "Relationship_to_Target";
	private static final String TARGET_TERM_TYPE = "Target_Term_Type";
	private static final String TARGET_CODE = "Target_Code";
	private static final String RDFS_COMMENT = "comment";
	private static final String NBSP = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

	public static Vector getMapsToQualifierNames() {
		Vector v = new Vector();
		v.add(TARGET_TERMINOLOGY);
		v.add(TARGET_TERMINOLOGY_VERSION);
		v.add(RELATIONSHIP_TO_TARGET);
		v.add(TARGET_TERM_TYPE);
		v.add(TARGET_CODE);
		v.add(RDFS_COMMENT);
		return v;
	}

	public static String otherProperty2HTMLString(String propertyName, String propertyValue, HashMap qualifierMap) {
		if (propertyName == null || propertyValue == null || qualifierMap == null) return null;
		Vector keys = null;
		if (propertyName.compareTo(MAPS_TO) == 0) {
			keys = getMapsToQualifierNames();
		}
		return otherProperty2HTMLString(propertyName, propertyValue, qualifierMap, keys);
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
    public static void main(String [] args) {
        boolean testLocal = true;
        LexBIGService lbSvc = null;
        if (testLocal) {
			lbSvc = LexBIGServiceImpl.defaultInstance();
		} else {
			lbSvc = RemoteServerUtil.createLexBIGService();
		}

		UIUtils uiUtils = new UIUtils(lbSvc);
		ConceptDetails conceptDetails = new ConceptDetails(lbSvc);
		String codingSchemeURN = "NCI_Thesaurus";
		String codingSchemeVersion = "15.10d";
		String code = "C16612";
		String namespace = "NCI_Thesaurus";
		boolean useNamespace = true;

        if (testLocal) {
			codingSchemeURN = "http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl";
			//= "owl2lexevs.owl";
			codingSchemeVersion = "0.1.2";
			code = "HappyPatientWalkingAround";
			namespace = null;
			useNamespace = false;
	    }

		Entity concept = conceptDetails.getConceptByCode(codingSchemeURN, codingSchemeVersion, code, namespace, useNamespace);
		String property_type = "PRESENTATION";
		String t = uiUtils.generatePropertyTable(concept, property_type);
		System.out.println(t);

        RelationshipUtils relUtils = new RelationshipUtils(lbSvc);
        HashMap relMap = relUtils.getRelationshipHashMap(codingSchemeURN, codingSchemeVersion, code, namespace, useNamespace);
        Iterator it = relMap.keySet().iterator();
        if (it == null) return;
        while (it.hasNext()) {
			String key = (String) it.next();
			ArrayList list = (ArrayList) relMap.get(key);
			System.out.println("\n" + key);
			if (list != null) {
				for (int i=0; i<list.size(); i++) {
					t = (String) list.get(i);
					System.out.println("\t" + t);
				}
			}
		}

		String description = "Association";
		String firstColumnHeading = "Name";
		String secondColumnHeading = "Value";
		int firstPercentColumnWidth = 20;
		int secondPercentColumnWidth = 80;
		int qualifierColumn = 2;
		//qualifierColumn = 0;
		ArrayList list = (ArrayList) relMap.get("type_association");

	    HTMLTableSpec spec = uiUtils.relationshipList2HTMLTableSpec(
		    description,
		    firstColumnHeading,
		    secondColumnHeading,
		    firstPercentColumnWidth,
		    secondPercentColumnWidth,
		    qualifierColumn,
		    list);

		//String html_str = uiUtils.generateHTMLTable(spec, "NCI_Thesaurus", "12.05d");
		String html_str = uiUtils.generateHTMLTable(spec, codingSchemeURN, codingSchemeVersion);
		System.out.println(html_str);

	}
*/
}

