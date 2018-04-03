package gov.nih.nci.evs.browser.utils;


import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.security.SecurityToken;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import java.io.*;
import java.text.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lexevs.tree.dao.iterator.ChildTreeNodeIterator;
import org.lexevs.tree.json.JsonConverter;
import org.lexevs.tree.json.JsonConverterFactory;
import org.lexevs.tree.model.LexEvsTree;
import org.lexevs.tree.model.LexEvsTreeNode.ExpandableStatus;
import org.lexevs.tree.model.LexEvsTreeNode;
import org.lexevs.tree.service.TreeService;
import org.lexevs.tree.service.TreeServiceFactory;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeTagList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.RenderingDetail;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.naming.*;


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


public class MetathesaurusUtils { //extends ServiceTestCase {
	LexBIGService lbSvc = null;
	ConceptDetails conceptDetails = null;

	public static final String SOURCE = "source";
	public static final String SOURCE_CODE = "source_code";
	public static final String SOURCE_CONCEPT_NAME = "source_concept_name";
	public static final String TARGET = "target";
	public static final String TARGET_CODE = "target_code";
	public static final String TARGET_CONCEPT_NAME = "target_concept_name";
	public static final String DEFINITION = "definition";

	public MetathesaurusUtils(LexBIGService lbSvc) {
		this.lbSvc = lbSvc;
		conceptDetails = new ConceptDetails(lbSvc);
	}

    // Restrictions: property name and value pair, list of sources of presentation properties
	public ResolvedConceptReferencesIterator searchByPresentationProperty(String codingSchemeURN, String codingSchemeVersion,
	                                            String propertyName, String propertyValue, String[] sourceList, String algorithm) {
		ResolvedConceptReferencesIterator iterator = null;
		try {
	        CodedNodeSet cns = getEntitiesWithProperty(codingSchemeURN, codingSchemeVersion,
	                                                   propertyName, propertyValue, sourceList, algorithm);
            SortOptionList sortOptions = null;
            LocalNameList filterOptions = null;
            LocalNameList propertyNames = null;
            CodedNodeSet.PropertyType[] propertyTypes = null;
            boolean resolveObjects = false;
			iterator = cns.resolve(sortOptions, filterOptions, propertyNames, propertyTypes, resolveObjects);

	    } catch (Exception ex) {
			ex.printStackTrace();
		}
		return iterator;
	}


	public CodedNodeSet getEntitiesWithProperty(String codingSchemeURN, String codingSchemeVersion,
	                                            String propertyName, String propertyValue, String[] sourceList, String algorithm) {
		return getEntitiesWithProperty(codingSchemeURN, codingSchemeVersion, "concept",
	                                            propertyName, propertyValue, sourceList, algorithm);

	}


	public CodedNodeSet getEntitiesWithProperty(String codingSchemeURN, String codingSchemeVersion, String type,
	                                            String propertyName, String propertyValue, String[] sourceList, String algorithm) {
		CodedNodeSet cns = null;
		try {
			cns = getEntities(codingSchemeURN, codingSchemeVersion, type);
			LocalNameList propertyNames = new LocalNameList();
			propertyNames.addEntry(propertyName);
			cns = cns.restrictToMatchingProperties(propertyNames, null, propertyValue, algorithm, null);

            if (sourceList != null && sourceList.length > 0) {
				for (int i=0; i<sourceList.length; i++) {
					String source = sourceList[i];
					LocalNameList sources = new LocalNameList();
					sources.addEntry(source);
					LocalNameList contextList = null;
					NameAndValueList qualifierList = null;
					CodedNodeSet.PropertyType[] propertyTypes = new CodedNodeSet.PropertyType[1];
					propertyTypes[0] = CodedNodeSet.PropertyType.PRESENTATION;
					cns = cns.restrictToProperties(null, propertyTypes, sources, contextList, qualifierList);
			    }
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cns;
	}



	public CodedNodeSet getEntitiesWithProperty(String codingSchemeURN, String codingSchemeVersion, String type,
	                                            String propertyName, String propertyValue, String algorithm) {
		CodedNodeSet cns = null;
		try {
			cns = getEntities(codingSchemeURN, codingSchemeVersion, type);
			LocalNameList propertyNames = new LocalNameList();
			propertyNames.addEntry(propertyName);
			cns = cns.restrictToMatchingProperties(propertyNames, null, propertyValue, algorithm, null);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cns;
	}


	public CodedNodeSet getEntitiesWithProperty(String codingSchemeURN, String codingSchemeVersion, String type, String propertyName) {
		CodedNodeSet cns = null;
		try {
			cns = getEntities(codingSchemeURN, codingSchemeVersion, type);
			LocalNameList propertyList = new LocalNameList();
			propertyList.addEntry(propertyName);
			cns = cns.restrictToProperties(propertyList, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cns;
	}

	public CodedNodeSet getEntities(String codingSchemeURN, String codingSchemeVersion, String type) {
		LocalNameList lnl = new LocalNameList();
		lnl.addEntry(type);
		CodedNodeSet cns = null;
		try {
			cns = lbSvc.getNodeSet(codingSchemeURN, Constructors.createCodingSchemeVersionOrTagFromVersion(codingSchemeVersion), lnl);
		} catch (Exception ex) {
            ex.printStackTrace();
		}
		return cns;
	}

	public CodedNodeSet getEntities(String codingSchemeURN, String codingSchemeVersion) {
		LocalNameList lnl = new LocalNameList();
		lnl.addEntry("concept");
		CodedNodeSet cns = null;
		try {
			cns = lbSvc.getNodeSet(codingSchemeURN, Constructors.createCodingSchemeVersionOrTagFromVersion(codingSchemeVersion), lnl);
		} catch (Exception ex) {
            ex.printStackTrace();
		}
		return cns;
	}


    public Vector getMatchedMetathesaurusCUIs(String scheme, String version,
        String ltag, String code) {
        Entity c = conceptDetails.getConceptByCode(scheme, version, code);
        Vector v = null;
        if (c != null) {
            v = conceptDetails.getConceptPropertyValues(c, "NCI_META_CUI");
            if (v == null || v.size() == 0) {
				v = conceptDetails.getConceptPropertyValues(c, "UMLS_CUI");
			}
		}
        return v;
    }


    public Vector getMatchedMetathesaurusCUIs(Entity c) {
        if (c != null) {
            Vector v = conceptDetails.getConceptPropertyValues(c, "NCI_META_CUI");
            if (v == null) return null;
            if (v == null || v.size() == 0) {
				return conceptDetails.getConceptPropertyValues(c, "UMLS_CUI");
			}
        }
        return null;
    }


    public JSONObject nciDefinition2JSONObject(String src_abbrev, String src_code, String nci_code, String nci_concept_name, String def) {
		JSONObject obj = null;
		try {
			obj=new JSONObject();
			obj.put(SOURCE, src_abbrev);
			obj.put(TARGET, "NCI");
			obj.put(SOURCE_CODE, src_code);
			obj.put(TARGET_CODE, nci_code);
			obj.put(TARGET_CONCEPT_NAME, nci_concept_name);
			obj.put(DEFINITION, def);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return obj;
	}

	public String getNCIDefinitionInJSON(String src_abbrev, String src_code) {
	    String nci_code = null;
	    String nci_def = null;
	    String target_concept_name = "No match";

	    Vector nci_code_vec = new Vector();
	    JSONArray array = new JSONArray();

		try {
			//LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            Vector cui_vec = getMatchedMetathesaurusCUIs(src_abbrev, null, null, src_code);
            String cui = null;
            for (int i=0; i<cui_vec.size(); i++) {
				cui = (String) cui_vec.elementAt(i);
			}
			if (cui == null) return null;

			ConceptDetails conceptDetails = new ConceptDetails(lbSvc);
			Entity ncim_entity = conceptDetails.getConceptByCode(Constants.NCI_METATHESAURUS, null, cui, null, false);
			String syn_1 = null;
			String syn_2 = null;

			String code_1 = null;
			String code_2 = null;

			if (ncim_entity != null) {
				org.LexGrid.commonTypes.Property[] properties = ncim_entity.getPresentation();
				if (properties == null) return null;
				for (int i = 0; i < properties.length; i++) {
					Property p = (Property) properties[i];
					String t = p.getValue().getContent();
					Source[] sources = p.getSource();
					if (sources != null && sources.length > 0) {

						Source src = sources[0];
						String src_abbr = src.getContent();
						if (src_abbr == null) return null;
						if (src_abbr.compareTo(src_abbrev) == 0) {
							syn_1 = p.getValue().getContent();
						} else if (src_abbr.compareTo("NCI") == 0) {
							syn_2 = p.getValue().getContent();
						}

						PropertyQualifier[] qualifiers = p.getPropertyQualifier();
						if (qualifiers != null && qualifiers.length > 0) {
							for (int j = 0; j < qualifiers.length; j++) {
								PropertyQualifier q = qualifiers[j];
								String qualifier_name = q.getPropertyQualifierName();
								String qualifier_value = q.getValue().getContent();
								if (qualifier_name.compareTo("source-code") == 0) {
									if (src_abbr.compareTo(src_abbrev) == 0) {
										 code_1 = qualifier_value;
									} else if (src_abbr.compareTo("NCI") == 0) {
										 code_2 = qualifier_value;
										 if (!nci_code_vec.contains(code_2)) {
											 nci_code_vec.add(code_2);
										 }
									}
								}
							}
						}
					}
				}
			}
			if (nci_code_vec.size() == 0) return null;
			for (int lcv=0; lcv<nci_code_vec.size(); lcv++) {
				nci_code = (String) nci_code_vec.elementAt(lcv);
				Entity ncit_entity = conceptDetails.getConceptByCode(Constants.NCIT_CS_NAME, null, nci_code, null, false);
				if (ncit_entity != null) {
					target_concept_name = ncit_entity.getEntityDescription().getContent();
					org.LexGrid.concepts.Definition[] properties = ncit_entity.getDefinition();
					if (properties == null) return null;
					for (int i = 0; i < properties.length; i++) {
						Definition p = (Definition) properties[i];
						String t = p.getValue().getContent();
						Source[] sources = p.getSource();
						if (sources != null && sources.length > 0) {
							Source src = sources[0];
							String src_abbr = src.getContent();
							if (src_abbr == null) return null;
							if (src_abbr.compareTo("NCI") == 0) {
								nci_def = p.getValue().getContent();
								JSONObject obj = nciDefinition2JSONObject(src_abbrev, src_code, nci_code, target_concept_name, nci_def);
								array.add(obj);
							}
						}
					}
				}
			}
			if (array.size() == 0) return null;
			StringWriter out = new StringWriter();
			try {
				array.writeJSONString(out);
				String jsonText = out.toString();
				return jsonText;
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}
