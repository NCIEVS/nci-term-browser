package gov.nih.nci.evs.browser.utils;


import gov.nih.nci.evs.browser.bean.*;
import gov.nih.nci.evs.browser.bean.MappingData;
import gov.nih.nci.evs.browser.common.Constants;
import gov.nih.nci.evs.browser.properties.*;
import java.io.*;
import java.net.URI;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Map;
import javax.faces.model.*;
import org.apache.commons.lang.*;
import org.apache.log4j.*;
import org.lexevs.dao.database.service.valuesets.LexEVSTreeItem;
import org.lexevs.dao.database.service.valuesets.ValueSetHierarchyService;
import org.lexevs.dao.database.service.valuesets.ValueSetHierarchyServiceImpl;
//import org.lexevs.property.PropertyExtension;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.commonTypes.*;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSApplicationService;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.DataModel.Core.types.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Direction;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping.SearchContext;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOption;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOptionName;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.QualifierSortOption;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Extensions.Generic.SupplementExtension;
import org.LexGrid.LexBIG.History.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.naming.*;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.sourceasserted.impl.SourceAssertedValueSetHierarchyServicesImpl;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.*;
import static gov.nih.nci.evs.browser.common.Constants.*;


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


public class ValueSetTreeUtils {

	private SourceAssertedValueSetHierarchyServicesImpl service;
    private static String scheme = "NCI_Thesaurus";
    private static String version = null;
    private static String association = "Concept_In_Subset";
    private static String sourceDesignation = "Contributing_Source";
	private static String publishName = "Publish_Value_Set";
	private static String root_code = "C54453";

    private static Logger _logger = Logger.getLogger(ValueSetTreeUtils.class);

    private HashMap sourceValueSetTree = null;
    private HashMap terminologyValueSetTree = null;
    private StringBuffer sourceValueSetTreeStringBuffer = null;
    private StringBuffer terminologyValueSetTreeStringBuffer = null;
    private HashMap _sourceValueSetTreeKey2TreeItemMap = null;
    private HashMap _terminologyValueSetDescriptionHashMap = null;
    private HashMap sourceValueSetCheckboxid2NodeIdMap = null;
    private HashMap terminologyValueSetCheckboxid2NodeIdMap = null;
    private static Set _vocabularyNameSet = null;

    LexBIGService lbSvc = null;
    String serviceUrl = null;
    boolean mode = true;

    public ValueSetTreeUtils(LexBIGService lbSvc) {
		this.lbSvc = lbSvc;
		try {
        	initialize();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }

    public ValueSetTreeUtils(LexBIGService lbSvc, String serviceUrl) {
		this.lbSvc = lbSvc;
		this.serviceUrl = serviceUrl;
		if (serviceUrl == null) {
			this.mode = true;  // local
		} else {
			this.mode = false; // remote
		}
		System.out.println("ValueSetTreeUtils serviceUrl:" + serviceUrl);
		try {
        	initialize();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }

    public void setMode(boolean mode) {
		this.mode = mode;
	}
/*
	public void initialize() throws Exception{
		long ms = System.currentTimeMillis();
		service = ((LexEVSApplicationService)lbSvc).getLexEVSSourceAssertedValueSetHierarchyServices();
		service.setLexBIGService(lbSvc);
		String prod_version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(ValueSetHierarchyService.SCHEME, "PRODUCTION");
		service.preprocessSourceHierarchyData(ValueSetHierarchyService.SCHEME, prod_version, ValueSetHierarchyService.HIERARCHY, ValueSetHierarchyService.SOURCE,ValueSetHierarchyService.PUBLISH_DESIGNATION, ValueSetHierarchyService.ROOT_CODE);
	    constructSourceValueSetTree();
	    constructTerminologyValueSetTree();
	}
*/


	public void initialize() throws Exception{
        service = createSourceAssertedValueSetHierarchyServices();
		String prod_version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(ValueSetHierarchyService.SCHEME, "PRODUCTION");
		service.preprocessSourceHierarchyData(ValueSetHierarchyService.SCHEME, prod_version, ValueSetHierarchyService.HIERARCHY, ValueSetHierarchyService.SOURCE,ValueSetHierarchyService.PUBLISH_DESIGNATION, ValueSetHierarchyService.ROOT_CODE);
	    constructSourceValueSetTree();
	    constructTerminologyValueSetTree();
	}

/*
    public SourceAssertedValueSetHierarchyServicesImpl createSourceAssertedValueSetHierarchyServices() {
		if (this.mode) {
			return (SourceAssertedValueSetHierarchyServicesImpl) SourceAssertedValueSetHierarchyServicesImpl.defaultInstance();
		} else {
			return((LexEVSApplicationService)lbSvc).getLexEVSSourceAssertedValueSetHierarchyServices();
		}
	}
*/
    public SourceAssertedValueSetHierarchyServicesImpl createSourceAssertedValueSetHierarchyServices() {
		SourceAssertedValueSetHierarchyServicesImpl service = null;
		if (this.mode) {
			service = (SourceAssertedValueSetHierarchyServicesImpl) SourceAssertedValueSetHierarchyServicesImpl.defaultInstance();
		} else {
			service = ((LexEVSApplicationService)lbSvc).getLexEVSSourceAssertedValueSetHierarchyServices();
			service.setLexBIGService(lbSvc);
		}
		return service;
	}

    public void constructSourceValueSetTree() {
        long ms = System.currentTimeMillis();
        try {
			Map<String, LexEVSTreeItem> source_items  = service.getFullServiceValueSetTree();
			LexEVSTreeItem source_item = source_items.get(ValueSetHierarchyServiceImpl.ROOT);
			TreeItem ti = LexEVSTreeItem2TreeItem.toTreeItem(source_item);
			ti = LexEVSTreeItem2TreeItem.placeNCItAsFirstNode(ti);

			sourceValueSetTree = new HashMap();
			sourceValueSetTree.put("<Root>", ti);

			TreeItem root = (TreeItem) sourceValueSetTree.get("<Root>");
			sourceValueSetTreeStringBuffer = new StringBuffer();
			SimpleTreeUtils stu = new SimpleTreeUtils(_vocabularyNameSet);

            sourceValueSetTreeStringBuffer = stu.getValueSetTreeStringBuffer(sourceValueSetTree);
			createSourceValueSetTreeKey2TreeItemMap();
			sourceValueSetCheckboxid2NodeIdMap = stu.getCheckboxid2NodeIdMap();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

    private void createSourceValueSetTreeKey2TreeItemMap() {
        if (_sourceValueSetTreeKey2TreeItemMap != null) {
            return;
        }
        HashMap hmap = sourceValueSetTree;
        TreeItem ti = (TreeItem) hmap.get("<Root>");
        _sourceValueSetTreeKey2TreeItemMap = new HashMap();
        createSourceValueSetTreeKey2TreeItemMap(ti);
    }

    private void createSourceValueSetTreeKey2TreeItemMap(TreeItem ti) {
        String key = ti._code + "$" + ti._text;
        _sourceValueSetTreeKey2TreeItemMap.put(key, ti);

		for (String association : ti._assocToChildMap.keySet()) {
			 List<TreeItem> children = ti._assocToChildMap.get(association);
			 for (TreeItem childItem : children) {
				 createSourceValueSetTreeKey2TreeItemMap(childItem);
			 }
		}
    }
//////////////////////////////////
    public LexEVSTreeItem sortLexEVSTreeItem(LexEVSTreeItem item) {
		for (String association : item._assocToChildMap.keySet()) {
			List<LexEVSTreeItem> children = item._assocToChildMap.get(association);
			new SortUtils().quickSort(children);
			for (int i=0; i<children.size(); i++) {
				LexEVSTreeItem childItem = (LexEVSTreeItem) children.get(i);
				sortLexEVSTreeItem(childItem);
			}
		}
		return item;
	}

    public LexEVSTreeItem mergeLexEVSTreeBranches(LexEVSTreeItem item1, LexEVSTreeItem item2) {
		for (String association : item2._assocToChildMap.keySet()) {
			List<LexEVSTreeItem> children = item2._assocToChildMap.get(association);
			HashSet hset = new HashSet();
			String duplicate_root_text = "";
			for (int i=0; i<children.size(); i++) {
				LexEVSTreeItem childItem = (LexEVSTreeItem) children.get(i);
				item1.addChild(association, childItem);
			}
		}
		return item1;
	}


    public String find_duplicate_root_text(LexEVSTreeItem lexevs_ti) {
		if (lexevs_ti == null) return null;
		String duplicate_root_text = null;
		for (String association : lexevs_ti._assocToChildMap.keySet()) {
			List<LexEVSTreeItem> children = lexevs_ti._assocToChildMap.get(association);
			HashSet hset = new HashSet();
			for (int i=0; i<children.size(); i++) {
				LexEVSTreeItem childItem = (LexEVSTreeItem) children.get(i);
				System.out.println(childItem.get_text());
				if (hset.contains(childItem.get_text())) {
					duplicate_root_text = childItem.get_text();
					break;
				} else {
					hset.add(childItem.get_text());
				}
			}
		}
		return duplicate_root_text;
	}


    public LexEVSTreeItem mergeLexEVSTreeBranches(LexEVSTreeItem lexevs_ti) {
		if (lexevs_ti == null) return null;
		String duplicate_root_text = find_duplicate_root_text(lexevs_ti);
		while (duplicate_root_text != null) {
			System.out.println("duplicate_root_text: " + duplicate_root_text);
			Vector root_nodes = new Vector();
			for (String association : lexevs_ti._assocToChildMap.keySet()) {
				root_nodes = new Vector();
				int index = -1;
				List<LexEVSTreeItem> children = lexevs_ti._assocToChildMap.get(association);
				if (children == null) return null;
				for (int i=0; i<children.size(); i++) {
					LexEVSTreeItem childItem = (LexEVSTreeItem) children.get(i);
					if (childItem == null) return null;
					if (childItem.get_text().compareTo(duplicate_root_text) == 0) {
						root_nodes.add(childItem);
						if (root_nodes.size() == 1) {
							index = i;
						}
						if (root_nodes.size() > 1) break;
					}
				}
				LexEVSTreeItem first_node = (LexEVSTreeItem) root_nodes.elementAt(0);
				LexEVSTreeItem second_node = (LexEVSTreeItem) root_nodes.elementAt(1);
				first_node = mergeLexEVSTreeBranches(first_node, second_node);
				children.set(index, first_node);
				children.remove(second_node);
			}
			duplicate_root_text = find_duplicate_root_text(lexevs_ti);
		}
		lexevs_ti = sortLexEVSTreeItem(lexevs_ti);
		return lexevs_ti;
	}


	public Map<String, LexEVSTreeItem> modifySourceDefinedTree(Map<String, LexEVSTreeItem> items) {
		LexEVSTreeItem lexevs_ti = items.get(ValueSetHierarchyServiceImpl.ROOT);
		lexevs_ti = mergeLexEVSTreeBranches(lexevs_ti);
		HashMap<String, LexEVSTreeItem> map = new HashMap<String, LexEVSTreeItem>();
		map.put(ValueSetHierarchyServiceImpl.ROOT, lexevs_ti);
		return map;
	}

    public void constructTerminologyValueSetTree() {
        long ms = System.currentTimeMillis();
        try {
			Map<String, LexEVSTreeItem> terminology_items = service.getSourceDefinedTree();
			LexEVSTreeItem terminology_item = terminology_items.get(ValueSetHierarchyServiceImpl.ROOT);
			TreeItem ti = LexEVSTreeItem2TreeItem.toTreeItem(terminology_item);
			ti = LexEVSTreeItem2TreeItem.placeNCItAsFirstNode(ti);
			terminologyValueSetTree = new HashMap();
			terminologyValueSetTree.put("<Root>", ti);

			TreeItem root = (TreeItem) terminologyValueSetTree.get("<Root>");

			terminologyValueSetTreeStringBuffer = new StringBuffer();
			SimpleTreeUtils stu_2 = new SimpleTreeUtils(_vocabularyNameSet);
			terminologyValueSetTreeStringBuffer = stu_2.getValueSetTreeStringBuffer(terminologyValueSetTree);
            setTerminologyValueSetDescriptionHashMap();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


    public HashMap getSourceValueSetTree() {
        return sourceValueSetTree;
	}

    public HashMap getTerminologyValueSetTree() {
		return terminologyValueSetTree;
	}

    public StringBuffer getSourceValueSetTreeStringBuffer() {
    	return sourceValueSetTreeStringBuffer;
	}

    public StringBuffer getTerminologyValueSetTreeStringBuffer() {
        return terminologyValueSetTreeStringBuffer;
	}

    public HashMap getSourceValueSetTreeKey2TreeItemMap() {
    	return _sourceValueSetTreeKey2TreeItemMap;
	}

    public HashMap getTerminologyValueSetDescriptionHashMap() {
        return _terminologyValueSetDescriptionHashMap;
	}

	public HashMap getSourceValueSetCheckboxid2NodeIdMap() {
		return sourceValueSetCheckboxid2NodeIdMap;
	}


    private void setTerminologyValueSetDescriptionHashMap() {
		String prod_version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(Constants.TERMINOLOGY_VALUE_SET_NAME, Constants.PRODUCTION);
		_terminologyValueSetDescriptionHashMap = getPropertyValues(Constants.TERMINOLOGY_VALUE_SET_NAME, prod_version, "GENERIC", "Description");
	}

    public static String getPropertyQualfierValues(
        org.LexGrid.commonTypes.Property p) {

        StringBuffer buf = new StringBuffer();
        String s = "";

        PropertyQualifier[] qualifiers = p.getPropertyQualifier();
        if (qualifiers != null && qualifiers.length > 0) {
            for (int j = 0; j < qualifiers.length; j++) {
                PropertyQualifier q = qualifiers[j];
                String qualifier_name = q.getPropertyQualifierName();
                //KLO, 110910
                if (qualifier_name.compareTo("label") != 0) {
					String qualifier_value = q.getValue().getContent();

					//s = s + qualifier_name + ": " + qualifier_value;
					buf.append(qualifier_name + ": " + qualifier_value);
					if (j < qualifiers.length - 1)
						//s = s + "; ";
						buf.append("; ");
				}
            }
        }
        s = buf.toString();
        return s;
    }

    public CodedNodeSet getNodeSet(LexBIGService lbSvc, String scheme, CodingSchemeVersionOrTag versionOrTag)
        throws Exception {
		CodedNodeSet cns = null;
		try {
			cns = lbSvc.getCodingSchemeConcepts(scheme, versionOrTag);
			CodedNodeSet.AnonymousOption restrictToAnonymous = CodedNodeSet.AnonymousOption.NON_ANONYMOUS_ONLY;
			cns = cns.restrictToAnonymous(restrictToAnonymous);
	    } catch (Exception ex) {
			ex.printStackTrace();
		}

		return cns;
	}

    public Vector getPropertyValues(Entity concept,
        String property_type, String property_name) {

		if (concept	== null || property_type == null || property_name == null) return null;
        Vector v = new Vector();
        org.LexGrid.commonTypes.Property[] properties = null;

        boolean addQualifiers = false;
        if (property_type.compareToIgnoreCase("GENERIC") == 0) {
            properties = concept.getProperty();
            addQualifiers = true;
        } else if (property_type.compareToIgnoreCase("PRESENTATION") == 0) {
            properties = concept.getPresentation();
        } else if (property_type.compareToIgnoreCase("COMMENT") == 0) {
            properties = concept.getComment();
            addQualifiers = true;
        } else if (property_type.compareToIgnoreCase("DEFINITION") == 0) {
            properties = concept.getDefinition();
        }

        if (properties == null || properties.length == 0)
            return v;
        for (int i = 0; i < properties.length; i++) {
            Property p = (Property) properties[i];
            if (p == null) return null;
            if (property_name.compareTo(p.getPropertyName()) == 0) {
                String t = p.getValue().getContent();

                // #27034
                if (addQualifiers) {
                    String qualifiers = getPropertyQualfierValues(p);
                    if (qualifiers == null) return null;
                    if (qualifiers.compareTo("") != 0) {
                        t = t + " (" + getPropertyQualfierValues(p) + ")";
                    }
                }

                Source[] sources = p.getSource();
                if (sources != null && sources.length > 0) {
                    Source src = sources[0];
                    t = t + "|" + src.getContent();
                }

                v.add(t);
            }
        }
        return v;
    }


    private HashMap getPropertyValues(String scheme, String version, String propertyType, String propertyName) {
		HashMap hmap = new HashMap();
		//LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		if (version != null) versionOrTag.setVersion(version);
		try {
			CodedNodeSet cns = getNodeSet(lbSvc, scheme, versionOrTag);
			SortOptionList sortOptions = null;
			LocalNameList filterOptions = null;
			LocalNameList propertyNames = Constructors.createLocalNameList(propertyName);
			CodedNodeSet.PropertyType[] propertyTypes = null;
			boolean resolveObjects = true;

			ResolvedConceptReferencesIterator iterator = cns.resolve(sortOptions, filterOptions, propertyNames,
				propertyTypes, resolveObjects);
			while (iterator != null && iterator.hasNext()) {
				ResolvedConceptReference rcr = iterator.next();
				Entity concept = rcr.getEntity();
    			Vector v = getPropertyValues(concept, propertyType, propertyName);
    			if (v != null) {
					if (v.size() > 0) {
						String key = concept.getEntityCode();
						String value = (String) v.elementAt(0);
						//System.out.println(key + " -> " + value);
						hmap.put(key, value);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return hmap;
	}
}

