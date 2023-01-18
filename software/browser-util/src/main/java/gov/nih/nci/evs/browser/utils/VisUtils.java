package gov.nih.nci.evs.browser.utils;


import gov.nih.nci.evs.browser.bean.*;
import gov.nih.nci.evs.browser.common.Constants;
import java.io.*;
import java.util.*;
import java.util.Enumeration;
import java.util.Map.Entry;
import org.apache.commons.lang.*;
import org.apache.logging.log4j.*;
import org.LexGrid.codingSchemes.*;
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


public class VisUtils {
	private static Logger _logger = LogManager.getLogger(VisUtils.class);
    private LexBIGService lbSvc = null;
    private LexBIGServiceConvenienceMethods lbscm = null;

    public static final int NODES_ONLY = 1;
    public static final int EDGES_ONLY = 2;
    public static final int NODES_AND_EDGES = 3;

    public static final String ROOT = "<ROOT>";
    public static final String PART_OF = "part_of";
/*
    public static final String[] Constants.ALL_RELATIONSHIP_TYPES = {"type_superconcept",
                                                           "type_subconcept",
                                                           "type_role",
                                                           "type_inverse_role",
                                                           "type_association",
                                                           "type_inverse_association"};

*/
    public static HashMap RELATIONSHIP_LABEL_MAP;

    static {
		RELATIONSHIP_LABEL_MAP = new HashMap();
		RELATIONSHIP_LABEL_MAP.put("ALL", "ALL");
		RELATIONSHIP_LABEL_MAP.put("type_superconcept", "Superconcept");
		RELATIONSHIP_LABEL_MAP.put("type_subconcept", "Subconcept");
		RELATIONSHIP_LABEL_MAP.put("type_role", "Role");
		RELATIONSHIP_LABEL_MAP.put("type_inverse_role", "Inverse Role");
		RELATIONSHIP_LABEL_MAP.put("type_association", "Association");
		RELATIONSHIP_LABEL_MAP.put("type_inverse_association", "Inverse Association");
		RELATIONSHIP_LABEL_MAP.put("type_part_of", "Part Of");
		RELATIONSHIP_LABEL_MAP.put("type_part_of_path", "Part Of (Path to Roots)");
	};

	public static String getRelatinshipLabel(String option_label) {
	    if (!RELATIONSHIP_LABEL_MAP.containsKey(option_label)) return option_label;
	    return (String) RELATIONSHIP_LABEL_MAP.get(option_label);
	}

	public VisUtils() {

	}

	public VisUtils(LexBIGService lbSvc) {
        this.lbSvc = lbSvc;
        try {
            lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


    public String getLabel(String name, String code) {
		name = encode(name);
		StringBuffer buf = new StringBuffer();
		buf.append(name + " (" + code + ")");
		return buf.toString();
	}

    public String getLabel(String line) {
        Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(line);
        String name = (String) u.elementAt(0);
        name = encode(name);
        String code = (String) u.elementAt(1);
        return getLabel(name, code);
	}

    public String getFieldValue(String line, int index) {
        Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(line);
        return (String) u.elementAt(index);
	}

	public String encode(String t) {
		if (t == null) return null;
		t = t.replaceAll("'", "\'");
		return t;
	}

    public String generateDiGraph(String scheme, String version, String namespace, String code) {
		boolean useNamespace = false;
		if (namespace != null) useNamespace = true;
		Entity concept = new ConceptDetails(lbSvc).getConceptByCode(scheme, version, code, namespace, useNamespace);
		String name = "<NO DESCRIPTION>";
		if (concept.getEntityDescription() != null) {
			name = concept.getEntityDescription().getContent();
		}
		name = encode(name);

		if (gov.nih.nci.evs.browser.utils.StringUtils.isNullOrBlank(namespace)) {
			namespace = concept.getEntityCodeNamespace();
		}

		StringBuffer buf = new StringBuffer();
        buf.append("\ndigraph {").append("\n");
        buf.append("node [shape=oval fontsize=16]").append("\n");
        buf.append("edge [length=100, color=gray, fontcolor=black]").append("\n");

        String focused_node_label = "\"" + getLabel(name, code) + "\"" ;

        RelationshipUtils relUtils = new RelationshipUtils(lbSvc);
        HashMap relMap = relUtils.getRelationshipHashMap(scheme, version, code, namespace, useNamespace);

		String key = "type_superconcept";
		ArrayList list = (ArrayList) relMap.get(key);
		if (list != null) {
			for (int i=0; i<list.size(); i++) {
				String t = (String) list.get(i);
				String rel_node_label = "\"" + getLabel(t) + "\"" ; //getLabel(t);
				String rel_label = "is_a";
				buf.append(focused_node_label + " -> " + rel_node_label).append("\n");
				buf.append("[label=" + rel_label + "];").append("\n");
			}
		}

		key = "type_subconcept";
		list = (ArrayList) relMap.get(key);
		if (list != null) {
			for (int i=0; i<list.size(); i++) {
				String t = (String) list.get(i);
				String rel_node_label = "\"" + getLabel(t) + "\"" ; //getLabel(t);
				String rel_label = "inverse_is_a";
				buf.append(focused_node_label + " -> " + rel_node_label).append("\n");
				buf.append("[label=" + rel_label + "];").append("\n");
			}
		}

		key = "type_role";
		list = (ArrayList) relMap.get(key);
		if (list != null) {
			for (int i=0; i<list.size(); i++) {
				String t = (String) list.get(i);
				String rel_node_label =  "\"" + getLabel(getFieldValue(t, 1), getFieldValue(t, 2)) + "\"";
				String rel_label = getFieldValue(t, 0);
				buf.append(focused_node_label + " -> " + rel_node_label).append("\n");
				buf.append("[label=" + rel_label + "];").append("\n");
			}
		}

		key = "type_inverse_role";
		list = (ArrayList) relMap.get(key);
		if (list != null) {
			for (int i=0; i<list.size(); i++) {
				String t = (String) list.get(i);
				String rel_node_label =  "\"" + getLabel(getFieldValue(t, 1), getFieldValue(t, 2)) + "\"";
				String rel_label = getFieldValue(t, 0);
				buf.append(rel_node_label + " -> " + focused_node_label).append("\n");
				buf.append("[label=" + rel_label + "];").append("\n");
			}
		}

		key = "type_association";
		list = (ArrayList) relMap.get(key);
		if (list != null) {
			for (int i=0; i<list.size(); i++) {
				String t = (String) list.get(i);
				String rel_node_label =  "\"" + getLabel(getFieldValue(t, 1), getFieldValue(t, 2)) + "\"";
				String rel_label = getFieldValue(t, 0);
				buf.append(focused_node_label + " -> " + rel_node_label).append("\n");
				buf.append("[label=" + rel_label + "];").append("\n");
			}
		}

		key = "type_inverse_association";
		list = (ArrayList) relMap.get(key);
		if (list != null) {
			for (int i=0; i<list.size(); i++) {
				String t = (String) list.get(i);
				String rel_node_label =  "\"" + getLabel(getFieldValue(t, 1), getFieldValue(t, 2)) + "\"";
				String rel_label = getFieldValue(t, 0);
				buf.append(rel_node_label + " -> " + focused_node_label).append("\n");
				buf.append("[label=" + rel_label + "];").append("\n");
			}
		}

        buf.append(focused_node_label + " [").append("\n");
        buf.append("fontcolor=white,").append("\n");
        buf.append("color=red,").append("\n");
        buf.append("]").append("\n");
        buf.append("}").append("\n");
        return buf.toString();
	}


    public String generateGraphScript(String scheme, String version, String namespace, String code) {
		return generateGraphScript(scheme, version, namespace, code, null);
	}


    public String generateGraphScript(String scheme, String version, String namespace, String code, String[] types) {
		return generateGraphScript(scheme, version, namespace, code, types, NODES_AND_EDGES, null);
	}



    public String generateGraphScript(String scheme, String version, String namespace, String code, String[] types, int option, HashMap hmap) {
        if (types == null) {
			types = Constants.ALL_RELATIONSHIP_TYPES;
		}
        Vector graphData = generateGraphData(scheme, version, namespace, code, types, option, hmap);
        return GraphUtils.generateGraphScript(graphData, option);
	}


    public Vector generateGraphScriptVector(String scheme, String version, String namespace, String code, String[] types, int option, HashMap hmap) {
        if (types == null) {
			types = Constants.ALL_RELATIONSHIP_TYPES;
		}
        Vector graphData = generateGraphData(scheme, version, namespace, code, types, option, hmap);
        return GraphUtils.generateGraphScriptVector(graphData, option);
	}

	public Vector treeItem2GraphData(TreeItem root) {
	    Vector graphData = treeItem2GraphData(root, new Vector());
	    return graphData;
    }

	public Vector treeItem2GraphData(TreeItem ti, Vector v) {
		String focused_node_label = getLabel(ti._text, ti._code);
		for (String association : ti._assocToChildMap.keySet()) {
			List<TreeItem> children = ti._assocToChildMap.get(association);
			for (TreeItem childItem : children) {
				String code = childItem._code;
				String text = childItem._text;
				String rel_node_label = getLabel(text, code);
				v.add(focused_node_label + "|" + rel_node_label + "|" + association + "|7");
				v = treeItem2GraphData(childItem, v);
			}
		}
	    return v;
    }


    public Vector generatePartonomyGraphData(String scheme, String version, String namespace, String code, String type, int option, HashMap hmap) {
		Vector graphData = new Vector();
		boolean useNamespace = true;
		if (gov.nih.nci.evs.browser.utils.StringUtils.isNullOrBlank(namespace)) {
			useNamespace = false;
		}

		Entity concept = new ConceptDetails(lbSvc).getConceptByCode(scheme, version, code, namespace, useNamespace);
		String name = "<NO DESCRIPTION>";
		if (concept.getEntityDescription() != null) {
			name = concept.getEntityDescription().getContent();
		}
		name = encode(name);
		if (gov.nih.nci.evs.browser.utils.StringUtils.isNullOrBlank(namespace)) {
			namespace = concept.getEntityCodeNamespace();
		}
		if (!gov.nih.nci.evs.browser.utils.StringUtils.isNullOrBlank(namespace)) {
			useNamespace = true;
		}
        String focused_node_label = getLabel(name, code);

        HashMap relMap = null;
        if (hmap == null) {
			RelationshipUtils relUtils = new RelationshipUtils(lbSvc);
			relMap = relUtils.getRelationshipHashMap(scheme, version, code, namespace, useNamespace);
	    } else {
			relMap = hmap;
		}

        HashSet nodes = new HashSet();
        nodes.add(focused_node_label);

        PartonomyUtils partUtils = new PartonomyUtils(lbSvc);

        if (type.compareTo("type_part_of") == 0) {
			ArrayList list = null;

			List part_of_list = partUtils.getPartOfData(relMap);
			if (part_of_list == null) return null;
			for (int i=0; i<part_of_list.size(); i++) {
				String t = (String) part_of_list.get(i);
				String rel_node_label = getLabel(getFieldValue(t, 1), getFieldValue(t, 2));
				String rel_label = getFieldValue(t, 0);
				graphData.add(focused_node_label + "|" + rel_node_label + "|" + rel_label + "|7");
			}

			List has_part_list = partUtils.getHasPartData(relMap);
			if (has_part_list == null) return null;
			for (int i=0; i<has_part_list.size(); i++) {
				String t = (String) has_part_list.get(i);
				String rel_node_label = getLabel(getFieldValue(t, 1), getFieldValue(t, 2));
				String rel_label = getFieldValue(t, 0);
				graphData.add(rel_node_label + "|" + focused_node_label + "|" + rel_label + "|7");
			}
		} else if (type.compareTo("type_part_of_path") == 0) {
            HashMap map = partUtils.getPathsToRoots(scheme, version, code, namespace, PART_OF);
            if (map == null) return null;
            TreeItem root = (TreeItem) map.get(ROOT);
            graphData = treeItem2GraphData(root);
		}
		return graphData;
	}


    public Vector generateGraphData(String scheme, String version, String namespace, String code, String[] types, int option, HashMap hmap) {
		if (types != null && types.length == 1) {
			String type = types[0];

			if (type.compareTo("type_part_of") == 0 || type.compareTo("type_part_of_path") == 0) {
				return generatePartonomyGraphData(scheme, version, namespace, code, type, option, hmap);
			}
		}

		Vector graphData = new Vector();
		List typeList = null;
		if (types != null) {
			typeList = Arrays.asList(types);
		} else {
			typeList = new ArrayList();
			typeList.add("type_superconcept");
			typeList.add("type_subconcept");
    	}

		boolean useNamespace = true;
		if (gov.nih.nci.evs.browser.utils.StringUtils.isNullOrBlank(namespace)) {
			useNamespace = false;
		}

		Entity concept = new ConceptDetails(lbSvc).getConceptByCode(scheme, version, code, namespace, useNamespace);
		if (concept == null && useNamespace) {
			concept = new ConceptDetails(lbSvc).getConceptByCode(scheme, version, code, namespace, false);
			if (concept == null) {
				System.out.println("Unable to find concept with code: " + code);
			}
		}

		String name = "<NO DESCRIPTION>";
		if (concept != null && concept.getEntityDescription() != null) {
			name = concept.getEntityDescription().getContent();
		}
		name = encode(name);
		if (gov.nih.nci.evs.browser.utils.StringUtils.isNullOrBlank(namespace)) {
			namespace = concept.getEntityCodeNamespace();
		}
		if (!gov.nih.nci.evs.browser.utils.StringUtils.isNullOrBlank(namespace)) {
			useNamespace = true;
		}
        String focused_node_label = getLabel(name, code);

        HashMap relMap = null;
        if (hmap == null) {
			RelationshipUtils relUtils = new RelationshipUtils(lbSvc);
			relMap = relUtils.getRelationshipHashMap(scheme, version, code, namespace, useNamespace);
	    } else {
			relMap = hmap;
		}

        HashSet nodes = new HashSet();
        nodes.add(focused_node_label);

        ArrayList list = null;

		String key = null;

		key = "type_superconcept";
		if (typeList.contains(key)) {
			list = (ArrayList) relMap.get(key);
			if (list != null) {
				for (int i=0; i<list.size(); i++) {
					String t = (String) list.get(i);
					String rel_node_label = getLabel(t);
					if (!nodes.contains(rel_node_label)) {
						nodes.add(rel_node_label);
					}
				}
			}
	    }

		key = "type_subconcept";
		if (typeList.contains(key)) {
			list = (ArrayList) relMap.get(key);
			if (list != null) {
				for (int i=0; i<list.size(); i++) {
					String t = (String) list.get(i);
					String rel_node_label = getLabel(t);
					if (!nodes.contains(rel_node_label)) {
						nodes.add(rel_node_label);
					}
				}
			}
	    }

		key = "type_role";
		if (typeList.contains(key)) {
			list = (ArrayList) relMap.get(key);
			if (list != null) {
				for (int i=0; i<list.size(); i++) {
					String t = (String) list.get(i);
					String rel_node_label = getLabel(getFieldValue(t, 1), getFieldValue(t, 2));
					if (!nodes.contains(rel_node_label)) {
						nodes.add(rel_node_label);
					}
				}
			}
	    }

		key = "type_inverse_role";
		if (typeList.contains(key)) {
			list = (ArrayList) relMap.get(key);
			if (list != null) {
				for (int i=0; i<list.size(); i++) {
					String t = (String) list.get(i);
					String rel_node_label = getLabel(getFieldValue(t, 1), getFieldValue(t, 2));
					if (!nodes.contains(rel_node_label)) {
						nodes.add(rel_node_label);
					}
				}
			}
	    }

		key = "type_association";
		if (typeList.contains(key)) {
			list = (ArrayList) relMap.get(key);
			if (list != null) {
				for (int i=0; i<list.size(); i++) {
					String t = (String) list.get(i);
					String rel_node_label = getLabel(getFieldValue(t, 1), getFieldValue(t, 2));
					if (!nodes.contains(rel_node_label)) {
						nodes.add(rel_node_label);
					}
				}
			}
	    }

		key = "type_inverse_association";
		if (typeList.contains(key)) {
			list = (ArrayList) relMap.get(key);
			if (list != null) {
				for (int i=0; i<list.size(); i++) {
					String t = (String) list.get(i);
					String rel_node_label = getLabel(getFieldValue(t, 1), getFieldValue(t, 2));
					if (!nodes.contains(rel_node_label)) {
						nodes.add(rel_node_label);
					}
				}
			}
	    }

		Vector node_label_vec = new Vector();
		Iterator it = nodes.iterator();
		if (it == null) return null;
		while (it.hasNext()) {
			String node_label = (String) it.next();
			node_label_vec.add(node_label);
		}

		key = "type_superconcept";
		if (typeList.contains(key)) {
			list = (ArrayList) relMap.get(key);
			if (list != null) {
				for (int i=0; i<list.size(); i++) {
					String t = (String) list.get(i);
					String rel_node_label = getLabel(t);
					String rel_label = "is_a";
					if (focused_node_label.compareTo(rel_node_label) != 0) {
						graphData.add(focused_node_label + "|" + rel_node_label + "|" + rel_label + "|1");
				    }
				}
			}
	    }

		key = "type_subconcept";
		if (typeList.contains(key)) {
			list = (ArrayList) relMap.get(key);
			if (list != null) {
				for (int i=0; i<list.size(); i++) {
					String t = (String) list.get(i);
					String rel_node_label = getLabel(t);
					String rel_label = "is_a";
					if (focused_node_label.compareTo(rel_node_label) != 0) {
						graphData.add(rel_node_label + "|" + focused_node_label + "|" + rel_label + "|2");
				    }
				}
			}
	    }

		key = "type_role";
		if (typeList.contains(key)) {
			list = (ArrayList) relMap.get(key);
			if (list != null) {
				for (int i=0; i<list.size(); i++) {
					String t = (String) list.get(i);
					String rel_node_label = getLabel(getFieldValue(t, 1), getFieldValue(t, 2));
					String rel_label = getFieldValue(t, 0);
					graphData.add(focused_node_label + "|" + rel_node_label + "|" + rel_label + "|3");
				}
			}
	    }

		key = "type_inverse_role";
		if (typeList.contains(key)) {
			list = (ArrayList) relMap.get(key);
			if (list != null) {
				for (int i=0; i<list.size(); i++) {
					String t = (String) list.get(i);
					String rel_node_label = getLabel(getFieldValue(t, 1), getFieldValue(t, 2));
					String rel_label = getFieldValue(t, 0);
					graphData.add(rel_node_label + "|" + focused_node_label + "|" +rel_label + "|4");
				}
			}
		}

		key = "type_association";
		if (typeList.contains(key)) {
			list = (ArrayList) relMap.get(key);
			if (list != null) {
				for (int i=0; i<list.size(); i++) {
					String t = (String) list.get(i);
					String rel_node_label = getLabel(getFieldValue(t, 1), getFieldValue(t, 2));
					String rel_label = getFieldValue(t, 0);
					graphData.add(focused_node_label + "|" + rel_node_label + "|" + rel_label + "|5");
				}
			}
	    }

		key = "type_inverse_association";
		if (typeList.contains(key)) {
			list = (ArrayList) relMap.get(key);
			if (list != null) {
				for (int i=0; i<list.size(); i++) {
					String t = (String) list.get(i);
					String rel_node_label = getLabel(getFieldValue(t, 1), getFieldValue(t, 2));
					String rel_label = getFieldValue(t, 0);
					graphData.add(rel_node_label + "|" + focused_node_label + "|" +rel_label + "|6");
				}
			}
		}
        return graphData;
	}

    public String generateGraphScript(String scheme, String version, String namespace, String code, int option) {
        Vector graphData = generateGraphData(scheme, version, namespace, code, Constants.ALL_RELATIONSHIP_TYPES, option, null);
        return GraphUtils.generateGraphScript(graphData, option);
	}

    public String findCodeInGraph(String nodes_and_edges, String id) {
		String target = "{id: " + id + ", label:";
		int n = nodes_and_edges.indexOf(target);
		if (n == -1) return null;
		String t = nodes_and_edges.substring(n+target.length(), nodes_and_edges.length());
		target = ")'}";
		n = t.indexOf(target);
		t = t.substring(0, n);
		n = t.lastIndexOf("(");
		t = t.substring(n+1, t.length());
		return t;
	}
/*
    public static void main(String [] args) {
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		VisUtils visUtils = new VisUtils(lbSvc);

		String codingSchemeURN = "NCI_Thesaurus";
		String codingSchemeVersion = "15.06e";
		String code = "C9118";//"Sarcoma (Code C9118)";
		String namespace = null;
		boolean useNamespace = false;

		System.out.println("scheme: " + codingSchemeURN);
		System.out.println("version: " + codingSchemeVersion);
		System.out.println("code: " + code);
		System.out.println("namespace: " + namespace);
		System.out.println("useNamespace: " + useNamespace);
		System.out.println("\n");
		String graph = visUtils.generateGraphScript(codingSchemeURN, codingSchemeVersion, namespace, code, NODES_AND_EDGES);
		System.out.println(graph);
	}
*/
}


