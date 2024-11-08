package gov.nih.nci.evs.browser.utils;

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
//import javax.faces.model.*;
import org.apache.commons.lang.*;
import org.apache.logging.log4j.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.commonTypes.*;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.*;
//import org.LexGrid.LexBIG.caCore.interfaces.LexEVSApplicationService;
//import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.DataModel.Core.types.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.LexBIG.Exceptions.*;
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


public class RelationshipUtils {
	private static Logger _logger = LogManager.getLogger(RelationshipUtils.class);
    private LexBIGService lbSvc = null;
    private LexBIGServiceConvenienceMethods lbscm = null;
    private TreeUtils treeUtils = null;
    private ConceptDetails cd = null;

    public static int SUPERCONCEPT_OPTION = 0;
    public static int SUBCONCEPT_OPTION = 1;
    public static int ROLE_OPTION = 2;
    public static int INVERSE_ROLE_OPTION = 3;
    public static int ASSOCIATION_OPTION = 4;
    public static int INVERSE_ASSOCIATION_OPTION = 5;


    public List createOptionList(boolean superconcept,
                                 boolean subconcept,
                                 boolean role,
                                 boolean inverse_role,
                                 boolean association,
                                 boolean inverse_association) {
		ArrayList list = new ArrayList();
		list.add(new Boolean(superconcept));
		list.add(new Boolean(subconcept));
		list.add(new Boolean(role));
		list.add(new Boolean(inverse_role));
		list.add(new Boolean(association));
		list.add(new Boolean(inverse_association));
		return list;
    }

    public List getDefaultOptionList() {
		ArrayList list = new ArrayList();
		for (int i=0; i<6; i++) {
			list.add(new Boolean(true));
		}
		return list;
	}

	public boolean checkOption(List options, int index) {
		Boolean bool = (Boolean) options.get(index);
		if (bool.equals(Boolean.TRUE)) return true;
		return false;
	}


	public RelationshipUtils(LexBIGService lbSvc) {
        this.lbSvc = lbSvc;
        try {
			this.cd = new ConceptDetails(lbSvc);
            this.lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.treeUtils = new TreeUtils(lbSvc);
	}

    public NameAndValueList getMappingAssociationNames(String scheme, String version) {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);

		NameAndValueList navList = new NameAndValueList();
		try {
			CodingScheme cs = lbSvc.resolveCodingScheme(scheme, csvt);
			Relations[] relations = cs.getRelations();
			if (relations == null) return null;
			for (int i = 0; i < relations.length; i++) {
				Relations relation = relations[i];
                Boolean isMapping = relation.isIsMapping();
                if (isMapping != null && isMapping.equals(Boolean.TRUE)) {
					AssociationPredicate[] associationPredicates = relation.getAssociationPredicate();
					if (associationPredicates == null) return null;
					for (int j=0; j<associationPredicates.length; j++) {
						AssociationPredicate associationPredicate = associationPredicates[j];
						String name = associationPredicate.getAssociationName();
						NameAndValue vNameAndValue = new NameAndValue();
						vNameAndValue.setName(name);
						navList.addNameAndValue(vNameAndValue);
					}
					return navList;
				} else {
					return null;
				}
			}
		} catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }



    public List getSupportedRoleNames(String scheme, String version) {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);

        List list = new ArrayList();
        try {
            CodingScheme cs = lbSvc.resolveCodingScheme(scheme, csvt);
            Relations[] relations = cs.getRelations();
            if (relations == null) return null;
            for (int i = 0; i < relations.length; i++) {
                Relations relation = relations[i];

                //_logger.debug("** getSupportedRoleNames containerName: "
                //    + relation.getContainerName());

                if (relation.getContainerName().compareToIgnoreCase("roles") == 0
                    || relation.getContainerName().compareToIgnoreCase("relations") == 0) {
                    //org.LexGrid.relations.Association[] asso_array =
                    org.LexGrid.relations.AssociationPredicate[] asso_array =
                        relation.getAssociationPredicate();
                    for (int j = 0; j < asso_array.length; j++) {
                        org.LexGrid.relations.AssociationPredicate association =
                            (org.LexGrid.relations.AssociationPredicate) asso_array[j];
                        // list.add(association.getAssociationName());
                        // KLO, 092209
                        //list.add(association.getForwardName());
                        list.add(association.getAssociationName());
                    }
                }
            }
        } catch (Exception ex) {

        }
        return list;
    }


    public LexBIGServiceConvenienceMethods createLexBIGServiceConvenienceMethods(
        LexBIGService lbSvc) {
        LexBIGServiceConvenienceMethods lbscm = null;
        try {
            lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lbscm;
    }

    public HashMap getRelationshipHashMap(String scheme, String version, String code) {
		String namespace = new ConceptDetails(lbSvc).getNamespaceByCode(scheme, version, code);
		return getRelationshipHashMap(scheme, version, code, namespace, false);
	}


    public HashMap getRelationshipHashMap(String scheme, String version, String code, String ns, boolean useNamespace) {
		return getRelationshipHashMap(scheme, version, code, ns, useNamespace, getDefaultOptionList());
	}


	public List sortList(List list) {
		if (list == null) return list;
        Vector v = new Vector();
		for (int i=0; i<list.size(); i++) {
			String t = (String) list.get(i);
			v.add(t);
		}
		v = new SortUtils().quickSort(v);
		list = new ArrayList();
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			list.add(t);
		}
        return list;
	}

	public ArrayList sortList(ArrayList list) {
		if (list == null) return list;
        Vector v = new Vector();
		for (int i=0; i<list.size(); i++) {
			String t = (String) list.get(i);
			v.add(t);
		}
		v = new SortUtils().quickSort(v);
		ArrayList new_list = new ArrayList();
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			new_list.add(t);
		}
        return new_list;
	}

    public HashMap getRelationshipHashMap(String scheme, String version, String code, String ns, boolean useNamespace, List options) {
        return getRelationshipHashMap(scheme, version, code, ns, useNamespace, options, true);
	}

    public HashMap getRelationshipHashMap(String scheme, String version, String code, String ns, boolean useNamespace, List options,
        boolean restrictToConcept) {
		if (options == null) {
			options = getDefaultOptionList();
		}
        boolean isMapping = isMapping(scheme, version);
        NameAndValueList navl = null;
        if (isMapping) navl = getMappingAssociationNames(scheme, version);
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);

        ConceptReference cr = ConvenienceMethods.createConceptReference(code, scheme);
        String entityCodeNamespace = null;
		Entity concept = null;
		if (ns != null) {
			if (ns.compareTo("null") == 0) {
				ns = cd.getNamespaceByCode(scheme, version, code);
			}
			concept = new ConceptDetails(lbSvc).getConceptByCode(scheme, version, code, ns, useNamespace);
		} else {
			concept = new ConceptDetails(lbSvc).getConceptByCode(scheme, version, code);
		}
		if (concept == null) {
			if (!isMapping) {
				return null;
		    }
		} else {
			entityCodeNamespace = concept.getEntityCodeNamespace();
		}

        if (entityCodeNamespace != null) {
			cr.setCodingSchemeName(entityCodeNamespace);
		}
        // Perform the query ...
        ResolvedConceptReferenceList matches = null;
        List list = getSupportedRoleNames(scheme, version);

        ArrayList roleList = new ArrayList();
        ArrayList associationList = new ArrayList();

        ArrayList inverse_roleList = new ArrayList();
        ArrayList inverse_associationList = new ArrayList();

        ArrayList superconceptList = new ArrayList();
        ArrayList subconceptList = new ArrayList();

        HashMap map = new HashMap();

        String[] associationsToNavigate =
            treeUtils.getAssociationsToNavigate(scheme, version);
        Vector w = new Vector();
        if (associationsToNavigate != null) {
            for (int k = 0; k < associationsToNavigate.length; k++) {
                w.add(associationsToNavigate[k]);
            }
        }

         // superconcepts:
        if (checkOption(options, SUPERCONCEPT_OPTION)) {
			if (!isMapping) {
					HashMap hmap_super = treeUtils.getSuperconcepts(scheme, version, code, ns);
					if (hmap_super != null) {
						TreeItem ti = (TreeItem) hmap_super.get(code);
						if (ti != null) {
							for (String association : ti._assocToChildMap.keySet()) {
								List<TreeItem> children =
									ti._assocToChildMap.get(association);
								for (TreeItem childItem : children) {
									superconceptList.add(childItem._text + "|" + childItem._code + "|" + childItem._ns);
								}
							}
						}
					}
					//Collections.sort(superconceptList);
					new SortUtils().quickSort(superconceptList);
			}
	    }
        map.put(Constants.TYPE_SUPERCONCEPT, superconceptList);

        /*
         * HashMap hmap_sub = TreeUtils.getSubconcepts(scheme, version, code);
         * if (hmap_sub != null) { TreeItem ti = (TreeItem) hmap_sub.get(code);
         * if (ti != null) { for (String association :
         * ti.assocToChildMap.keySet()) { List<TreeItem> children =
         * ti.assocToChildMap.get(association); for (TreeItem childItem :
         * children) { subconceptList.add(childItem.text + "|" +
         * childItem.code); } } } }
         */

        // subconcepts:
        if (checkOption(options, SUBCONCEPT_OPTION)) {
			if (!isMapping) {
					subconceptList = treeUtils.getSubconceptNamesAndCodes(scheme, version, code, ns);
					//new SortUtils().quickSort(subconceptList);
					subconceptList = sortList(subconceptList);
			}
	    }
        map.put(Constants.TYPE_SUBCONCEPT, subconceptList);

        // associations:
        CodedNodeGraph cng = null;
        if (checkOption(options, ROLE_OPTION) || checkOption(options, ASSOCIATION_OPTION) ) {

			try {
				cng = lbSvc.getNodeGraph(scheme, csvt, null);
				if (restrictToConcept) {
					cng = restrictToEntityType(cng, "concept");
			    }
				if (cng == null) return null;

				if (isMapping) {
					 if (navl != null) {
						 cng = cng.restrictToAssociations(navl, null);
					 }
				}

				matches = null;
				try {
					matches = cng.resolveAsList(cr, true, false, 0, 1, null, null, null, null, -1, false);

				} catch (Exception e) {
					//_logger
					 //   .error("ERROR: DataUtils getRelationshipHashMap cng.resolveAsList throws exceptions."
					  //      + code);
				}

				if (matches != null
					&& matches.getResolvedConceptReferenceCount() > 0) {
					Enumeration<? extends ResolvedConceptReference> refEnum =
						matches.enumerateResolvedConceptReference();

					while (refEnum.hasMoreElements()) {
						ResolvedConceptReference ref = (ResolvedConceptReference) refEnum.nextElement();
						AssociationList sourceof = ref.getSourceOf();
						if (sourceof != null) {
							Association[] associations = sourceof.getAssociation();
							if (associations != null) {
								for (int i = 0; i < associations.length; i++) {
									Association assoc = associations[i];
									String associationName = null;

									try {
										associationName =
											lbscm
												.getAssociationNameFromAssociationCode(
													scheme, csvt, assoc
														.getAssociationName());
									} catch (Exception ex) {
										associationName = assoc.getAssociationName();
									}

									//associationName = assoc.getDirectionalName();

									boolean isRole = false;
									if (list.contains(associationName)) {
										isRole = true;
									}

									AssociatedConcept[] acl =
										assoc.getAssociatedConcepts()
											.getAssociatedConcept();

									for (int j = 0; j < acl.length; j++) {
										AssociatedConcept ac = acl[j];

										//String ac_csn = ac.getCodingSchemeName();

										// [#26283] Remove self-referential
										// relationships.
										boolean include = true;
										if (ac.getConceptCode().compareTo(code) == 0)
											include = false;

										if (include) {

											EntityDescription ed =
												ac.getEntityDescription();

											String name = "No Description";
											if (ed != null)
												name = ed.getContent();
											String pt = name;

											if (associationName
												.compareToIgnoreCase("equivalentClass") != 0
												&& ac.getConceptCode().indexOf("@") == -1) {
												if (!w.contains(associationName)) {
													// String s = associationName +
													// "|" + pt + "|" +
													// ac.getConceptCode();
													String relaValue =
														replaceAssociationNameByRela(
															ac, associationName);

													String s =
														relaValue + "|" + pt + "|"
															+ ac.getConceptCode() + "|"
															+ ac.getCodingSchemeName() + "|"
															+ ac.getCodeNamespace();

													StringBuffer sb = new StringBuffer();
													//if (isMapping) {
														if (ac.getAssociationQualifiers() != null) {
															//String qualifiers = "";
															StringBuffer buf = new StringBuffer();
															for (NameAndValue qual : ac
																	.getAssociationQualifiers()
																	.getNameAndValue()) {
																String qualifier_name = qual.getName();
																String qualifier_value = qual.getContent();
																if (gov.nih.nci.evs.browser.utils.StringUtils.isNullOrBlank(qualifier_name) &&
																	gov.nih.nci.evs.browser.utils.StringUtils.isNullOrBlank(qualifier_value)) {
																} else {
																	buf.append((qualifier_name + "=" + qualifier_value) + "$");
																}

															}
															String qualifiers = buf.toString();
															if (qualifiers == null) return null;
															if (qualifiers.endsWith("$")) {
																qualifiers = qualifiers.substring(0, qualifiers.length()-1);
															}

															//s = s + "|" + qualifiers;
															sb.append("|" + qualifiers);
														}
														//s = s + "|" + ac.getCodeNamespace();
														//sb.append("|" + ac.getCodeNamespace());
													//}
													s = s + sb.toString();


													if (isRole) {
														// if
														// (associationName.compareToIgnoreCase("hasSubtype")
														// != 0) {
														// //_logger.debug("Adding role: "
														// +
														// s);
														roleList.add(s);
														// }
													} else {
														// //_logger.debug("Adding association: "
														// + s);
														associationList.add(s);

													}

												}
											}
										}
									}
								}
							}
						}
					}
				}
			} catch (Exception ex) {

			}
		}
        if (checkOption(options, INVERSE_ROLE_OPTION) || checkOption(options, INVERSE_ASSOCIATION_OPTION)) {
            try {
				cng = lbSvc.getNodeGraph(scheme, csvt, null);
				if (restrictToConcept) {
					cng = restrictToEntityType(cng, "concept");
				}
				if (cng == null) return null;

				if (isMapping) {
					 if (navl != null) {
						 cng = cng.restrictToAssociations(navl, null);
					 }
				}

				matches = null;
				try {
					/*
					 * CodedNodeSet.PropertyType[] propertyTypes = new
					 * CodedNodeSet.PropertyType[1]; propertyTypes[0] =
					 * PropertyType.PRESENTATION; int resolveCodedEntryDepth = 0;
					 */
					matches =
						cng.resolveAsList(cr,
								// false, true, 0, 1, new LocalNameList(), null,
								// null, 10000);
								// false, true, 0, 1, null, new LocalNameList(),
								// null, null, -1, false);
								false, true, 0, 1, null, null, null, null, -1,
								false);
					/*
					 * matches = cng.resolveAsList(ConvenienceMethods
					 * .createConceptReference(code, scheme), //true, true, 1, 1,
					 * noopList_, null, null, null, -1, false); //true, true, 1, 1,
					 * noopList_, propertyTypes, null, null, -1, false); true, true,
					 * 0, 1, null, propertyTypes, null, null, -1, false);
					 */

				} catch (Exception e) {
					//_logger
						//error("ERROR: DataUtils getRelationshipHashMap cng.resolveAsList throws exceptions."
						 //   + code);
				}

				if (matches != null
					&& matches.getResolvedConceptReferenceCount() > 0) {
					Enumeration<? extends ResolvedConceptReference> refEnum =
						matches.enumerateResolvedConceptReference();

					while (refEnum.hasMoreElements()) {
						ResolvedConceptReference ref = refEnum.nextElement();

						// inverse roles and associations
						AssociationList targetof = ref.getTargetOf();
						if (targetof != null) {
							Association[] inv_associations =
								targetof.getAssociation();
							if (inv_associations != null) {
								for (int i = 0; i < inv_associations.length; i++) {
									Association assoc = inv_associations[i];
									// String associationName =
									// assoc.getAssociationName();

									String associationName = null;
									try {
										associationName = lbscm
											.getAssociationNameFromAssociationCode(
												scheme, csvt, assoc
													.getAssociationName());
									} catch (Exception ex) {
										associationName = assoc.getAssociationName();
									}

									//associationName = assoc.getDirectionalName();

									boolean isRole = false;
									if (list.contains(associationName)) {
										isRole = true;
									}
									AssociatedConcept[] acl =
										assoc.getAssociatedConcepts()
											.getAssociatedConcept();
									for (int j = 0; j < acl.length; j++) {
										AssociatedConcept ac = acl[j];

										// [#26283] Remove self-referential
										// relationships.
										boolean include = true;
										if (ac.getConceptCode().compareTo(code) == 0)
											include = false;

										if (include) {

											EntityDescription ed =
												ac.getEntityDescription();

											String name = "No Description";
											if (ed != null)
												name = ed.getContent();

											String pt = name;

											// [#24749] inverse association names
											// are empty for domain and range
											if (associationName.compareTo("domain") == 0
												|| associationName
													.compareTo("range") == 0) {

												try {
													pt =
														lbscm
															.getAssociationNameFromAssociationCode(
																scheme, csvt, ac
																	.getConceptCode());
												} catch (Exception ex) {
													pt = ac.getConceptCode();
												}
											}

											// if
											// (associationName.compareToIgnoreCase("equivalentClass")
											// != 0) {
											if (associationName
												.compareToIgnoreCase("equivalentClass") != 0
												&& ac.getConceptCode().indexOf("@") == -1) {

												if (!w.contains(associationName)) {
													// String s = associationName +
													// "|" + pt + "|" +
													// ac.getConceptCode();
													String relaValue =
														replaceAssociationNameByRela(
															ac, associationName);

													String s =
														relaValue + "|" + pt + "|"
															 + ac.getConceptCode() + "|"
															 + ac.getCodingSchemeName() + "|"
															 + ac.getCodeNamespace();

													StringBuffer sb = new StringBuffer();


													//if (isMapping) {
														if (ac.getAssociationQualifiers() != null) {
															//String qualifiers = "";
															StringBuffer buf = new StringBuffer();
															for (NameAndValue qual : ac
																	.getAssociationQualifiers()
																	.getNameAndValue()) {
																String qualifier_name = qual.getName();
																String qualifier_value = qual.getContent();
																buf.append((qualifier_name + "=" + qualifier_value) + "$");
															}
															String qualifiers = buf.toString();
															if (qualifiers == null) return null;
															if (qualifiers.endsWith("$")) {
																qualifiers = qualifiers.substring(0, qualifiers.length()-1);
															}
															//s = s + "|" + qualifiers;
															sb.append("|" + qualifiers);
														}
														//s = s + "|" + ac.getCodeNamespace();
														//sb.append("|" + ac.getCodeNamespace());
														s = s + sb.toString();
													//}

													if (isRole) {
														inverse_roleList.add(s);
													} else {
														inverse_associationList
															.add(s);
													}
												}

											}
										}
									}
								}
							}
						}
					}
				}
		    } catch (Exception ex) {

			}
		}

		if (!checkOption(options, ROLE_OPTION)) {
			roleList = new ArrayList();
		}
		if (!checkOption(options, INVERSE_ROLE_OPTION)) {
			inverse_roleList = new ArrayList();
		}
		if (!checkOption(options, ASSOCIATION_OPTION)) {
			associationList = new ArrayList();
		}
		if (!checkOption(options, INVERSE_ASSOCIATION_OPTION)) {
			inverse_associationList = new ArrayList();
		}

		if (roleList.size() > 0) {
			new SortUtils().quickSort(roleList);
		}

		if (associationList.size() > 0) {
			new SortUtils().quickSort(associationList);
		}

		map.put(Constants.TYPE_ROLE, roleList);
		map.put(Constants.TYPE_ASSOCIATION, associationList);

		if (inverse_roleList.size() > 0) {
			new SortUtils().quickSort(inverse_roleList);
		}

		if (inverse_associationList.size() > 0) {
			new SortUtils().quickSort(inverse_associationList);
		}

		map.put(Constants.TYPE_INVERSE_ROLE, inverse_roleList);
		map.put(Constants.TYPE_INVERSE_ASSOCIATION, inverse_associationList);
        return map;
    }

    private String replaceAssociationNameByRela(AssociatedConcept ac,
        String associationName) {

        if (ac.getAssociationQualifiers() == null)
            return associationName;
        if (ac.getAssociationQualifiers().getNameAndValue() == null)
            return associationName;

        for (NameAndValue qual : ac.getAssociationQualifiers()
            .getNameAndValue()) {
            String qualifier_name = qual.getName();
            String qualifier_value = qual.getContent();
            if (qualifier_name.compareToIgnoreCase("rela") == 0) {
                return qualifier_value; // replace associationName by Rela value
            }
        }
        return associationName;
    }

    public boolean isMapping(String scheme, String version) {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);

		try {
			MappingExtension mappingExtension = (MappingExtension)
				lbSvc.getGenericExtension("MappingExtension");

            boolean isMappingCS = mappingExtension.isMappingCodingScheme(scheme, csvt);
            //Boolean bool_obj = Boolean.valueOf(isMappingCS);//   new Boolean(isMappingCS);
			return isMappingCS;

		} catch (Exception ex) {
            return false;
        }
    }


    private CodedNodeGraph restrictToEntityType(CodedNodeGraph cng, String type) {
    	LocalNameList lnl = new LocalNameList();
		lnl.addEntry(type);
		try {
			cng = cng.restrictToEntityTypes(lnl);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return cng;
	}


	public List getRelationshipData(HashMap relMap, String key, List relationship_list) {
		if (relMap == null) return null;
		List list = (ArrayList) relMap.get(key);
		if (list == null) return null;
		List a = new ArrayList();
		for (int i=0; i<list.size(); i++) {
			String t = (String) list.get(i);
			String rel_label = gov.nih.nci.evs.browser.utils.StringUtils.getFieldValue(t, 0);
			String name = gov.nih.nci.evs.browser.utils.StringUtils.getFieldValue(t, 1);
			String code = gov.nih.nci.evs.browser.utils.StringUtils.getFieldValue(t, 2);
			if (relationship_list.contains(rel_label)) {
				a.add(rel_label + "|" + name + "|" + code);
			}
		}
		return a;
	}

    public ArrayList getRelationshipData(String scheme, String version, String namespace, String code,
                                         String associationName, boolean direction) {

        ArrayList list = new ArrayList();
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null) {
			csvt.setVersion(version);
		}

        // Perform the query ...
        String relationContainerName = null;
        ResolvedConceptReferenceList matches = null;
        ConceptReference cref = ConvenienceMethods.createConceptReference(code, scheme);
        if (namespace == null) {
			namespace = new ConceptDetails(lbSvc).getNamespaceByCode(scheme, version, code);
		}
        cref.setCodeNamespace(namespace);
        try {
			matches = lbSvc.getNodeGraph(scheme, csvt, relationContainerName).resolveAsList(
					cref, direction, !direction, 1, 1, new LocalNameList(), null,
					null, -1);
		} catch (Exception ex) {
			return null;
		}
        boolean associationExists = false;
        if (matches.getResolvedConceptReferenceCount() > 0) {
            Enumeration<? extends ResolvedConceptReference> refEnum = matches.enumerateResolvedConceptReference();

            while (refEnum.hasMoreElements()) {
                ResolvedConceptReference ref = refEnum.nextElement();
                AssociationList sourceof = ref.getSourceOf();
                if (!direction) {
					sourceof = ref.getTargetOf();
				}
                if (sourceof != null) {
					Association[] associations = sourceof.getAssociation();
					if (associations == null) return null;
					for (int i = 0; i < associations.length; i++) {
						Association assoc = associations[i];
						if (assoc == null) return null;
						if (assoc.getAssociationName().compareTo(associationName) == 0) {
							associationExists = true;
							AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
							if (acl == null) return null;
							for (int j = 0; j < acl.length; j++) {
								AssociatedConcept ac = acl[j];

								EntityDescription ed =
									ac.getEntityDescription();

								String name = "No Description";
								if (ed != null) {
									name = ed.getContent();
								}
								String pt = name;
								String rela = replaceAssociationNameByRela(ac, assoc.getAssociationName());
								if (direction) {
									String s =
										rela + "|" + pt + "|"
											+ ac.getConceptCode() + "|"
											+ ac.getCodingSchemeName() + "|"
											+ ac.getCodeNamespace();

									StringBuffer sb = new StringBuffer();
									if (ac.getAssociationQualifiers() != null) {
										StringBuffer buf = new StringBuffer();
										for (NameAndValue qual : ac
												.getAssociationQualifiers()
												.getNameAndValue()) {
											String qualifier_name = qual.getName();
											String qualifier_value = qual.getContent();
											if (gov.nih.nci.evs.browser.utils.StringUtils.isNullOrBlank(qualifier_name) &&
												gov.nih.nci.evs.browser.utils.StringUtils.isNullOrBlank(qualifier_value)) {
											} else {
												buf.append((qualifier_name + "=" + qualifier_value) + "$");
											}

										}
										String qualifiers = buf.toString();
										if (qualifiers == null) return null;
										if (qualifiers.endsWith("$")) {
											qualifiers = qualifiers.substring(0, qualifiers.length()-1);
										}
										sb.append("|" + qualifiers);
									}
									s = s + sb.toString();
									list.add(s);

								} else {
									String s =
										rela + "|" + pt + "|"
											+ ac.getConceptCode() + "|"
											+ ac.getCodingSchemeName() + "|"
											+ ac.getCodeNamespace();
									StringBuffer sb = new StringBuffer();
									if (ac.getAssociationQualifiers() != null) {
										StringBuffer buf = new StringBuffer();
										for (NameAndValue qual : ac
												.getAssociationQualifiers()
												.getNameAndValue()) {
											String qualifier_name = qual.getName();
											String qualifier_value = qual.getContent();
											buf.append((qualifier_name + "=" + qualifier_value) + "$");
										}
										String qualifiers = buf.toString();
										if (qualifiers == null) return null;
										if (qualifiers.endsWith("$")) {
											qualifiers = qualifiers.substring(0, qualifiers.length()-1);
										}
										sb.append("|" + qualifiers);
									}
									s = s + sb.toString();
									list.add(s);
								}
							}
							break;
						}
					}
			    }
            }
        }
        if (!associationExists) return null;
        return list;
    }
}
