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
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
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


public class ConceptDetails {
	private String SOURCE_CODE = "source-code";
	private String SOURCE_DATE = "source-date";
	private String SUBSOURCE_NAME = "subsource-name";
	private String SOURCE = "source";
	private String TERM_TYPE = "term-type";

	private int _maxReturn = -1;

	private static Logger _logger = LogManager.getLogger(ConceptDetails.class);
    private LexBIGService lbSvc = null;
    private LexBIGServiceConvenienceMethods lbscm = null;

    public ConceptDetails() {

	}

	public ConceptDetails(LexBIGService lbSvc) {
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

    public ConceptReferenceList createConceptReferenceList(
        String[] codes, String codingSchemeName, String ns) {
        if (codes == null) {
            return null;
        }
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < codes.length; i++) {
            ConceptReference cr = new ConceptReference();
            cr.setCodingSchemeName(codingSchemeName);
            cr.setConceptCode(codes[i]);
            if (ns != null) {
				cr.setCodeNamespace(ns);
			}
            list.addConceptReference(cr);
        }
        return list;
    }

    public List<String> getDistinctNamespacesOfCode(
            String codingScheme,
            String version,
            String code) {

        try {
            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(version);
            List<String> list = lbscm.getDistinctNamespacesOfCode(codingScheme, csvt, code);
            return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        return null;
	}

    public ConceptReferenceList createConceptReferenceList(
        String[] codes, String codingSchemeName) {
        if (codes == null) {
            return null;
        }
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < codes.length; i++) {
            ConceptReference cr = new ConceptReference();
            cr.setCodingSchemeName(codingSchemeName);
            cr.setConceptCode(codes[i]);
            list.addConceptReference(cr);
        }
        return list;
    }

    public ConceptReferenceList createConceptReferenceList(Vector code_vec,
        String codingSchemeName) {
        if (code_vec == null) {
            return null;
        }
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < code_vec.size(); i++) {
            String code = (String) code_vec.elementAt(i);
            ConceptReference cr = new ConceptReference();
            cr.setCodingSchemeName(codingSchemeName);
            cr.setConceptCode(code);
            list.addConceptReference(cr);
        }
        return list;
    }

    public CodedNodeSet getNodeSet(String scheme, String version) throws Exception {
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		if (version != null) versionOrTag.setVersion(version);
		return getNodeSet(scheme, versionOrTag);
	}

    public CodedNodeSet getNodeSet(String scheme, CodingSchemeVersionOrTag versionOrTag) throws Exception {
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

    public CodedNodeSet getNodeSet(String scheme, String version, String[] types) throws Exception {
		if (types == null) {
			return getNodeSet(scheme, version);
		}
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		if (version != null) {
			versionOrTag.setVersion(version);
		}
		LocalNameList entityTypes = new LocalNameList();
		for (int i=0; i<types.length; i++) {
			String entityType = types[i];
		    entityTypes.addEntry(entityType);
	    }
		return lbSvc.getNodeSet(scheme, versionOrTag, entityTypes);
	}

    public Entity getConceptByCode(String codingSchemeName, String vers, String code, String ns, boolean use_ns) {
        try {
			if (code == null) {
				return null;
			}
			if (code.indexOf("@") != -1) return null; // anonymous class
            CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
            if (vers != null) versionOrTag.setVersion(vers);

            ConceptReferenceList crefs = null;
            if (use_ns) {
                 crefs = createConceptReferenceList(new String[] { code }, codingSchemeName, ns);
			} else {
				 crefs = createConceptReferenceList(new String[] { code }, codingSchemeName);
			}

            CodedNodeSet cns = null;
            try {
				try {
					cns = getNodeSet(codingSchemeName, versionOrTag);

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}

                if (cns == null) {
					return null;
				}

                cns = cns.restrictToCodes(crefs);
 				ResolvedConceptReferenceList matches = null;
				try {
					matches = cns.resolveToList(null, null, null, 1);
				} catch (Exception e) {
					e.printStackTrace();
				}

                if (matches == null) {
                    //System.out.println("Concept not found.");
                    return null;
                }
                int count = matches.getResolvedConceptReferenceCount();
                // Analyze the result ...
                if (count == 0)
                    return null;
                if (count > 0) {
                    try {
                        ResolvedConceptReference ref = (ResolvedConceptReference) matches
                                .enumerateResolvedConceptReference()
                                .nextElement();
                        Entity entry = ref.getReferencedEntry();
                        return entry;
                    } catch (Exception ex1) {
                        ex1.printStackTrace();
                        return null;
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    public Entity getConceptByCode(String codingSchemeName, String vers, String code) {

        try {
			if (code == null) {
				return null;
			}
			if (code.indexOf("@") != -1) return null; // anonymous class
            CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
            if (vers != null) versionOrTag.setVersion(vers);

            ConceptReferenceList crefs = createConceptReferenceList(
                    new String[] { code }, codingSchemeName);

            CodedNodeSet cns = null;
            try {
				try {
					cns = getNodeSet(codingSchemeName, versionOrTag);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}

                if (cns == null) {
					return null;
				}

                cns = cns.restrictToCodes(crefs);
 				ResolvedConceptReferenceList matches = null;
				try {
					matches = cns.resolveToList(null, null, null, 1);
				} catch (Exception e) {
					e.printStackTrace();
				}

                if (matches == null) {
                   return null;
                }
                int count = matches.getResolvedConceptReferenceCount();
                // Analyze the result ...
                if (count == 0)
                    return null;
                if (count > 0) {
                    try {
                        ResolvedConceptReference ref = (ResolvedConceptReference) matches
                                .enumerateResolvedConceptReference()
                                .nextElement();
                        Entity entry = ref.getReferencedEntry();
                        return entry;
                    } catch (Exception ex1) {
                        ex1.printStackTrace();
                        return null;
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public String encodeTerm(String s) {
		/*
		if (s == null) return null;
		if (gov.nih.nci.evs.browser.utils.StringUtils.isAlphanumeric(s)) return s;

        StringBuilder buf = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
			if (Character.isLetterOrDigit(c)) {
                buf.append(c);
            } else {
                buf.append("&#").append((int) c).append(";");
            }
        }
        return buf.toString();
        */
        return gov.nih.nci.evs.browser.utils.StringUtils.encodeTerm(s);
    }

    public Vector getPresentationProperties(String codingSchemeName, String vers, String code) {
		Entity concept = getConceptByCode(codingSchemeName, vers, code);
		if (concept == null) return null;
		return getPresentationProperties(concept);
	}


    public Vector getPresentationProperties(Entity concept) {
        Vector v = new Vector();
        org.LexGrid.commonTypes.Property[] properties =
            concept.getPresentation();
        // name$value$isPreferred

        if (properties == null || properties.length == 0)
            return v;
        for (int i = 0; i < properties.length; i++) {
            Presentation p = (Presentation) properties[i];
            String name = p.getPropertyName();
            String value = p.getValue().getContent();
            String isPreferred = "false";
            if (p.getIsPreferred() != null) {
                isPreferred = p.getIsPreferred().toString();
			}
            String t = name + "$" + value + "$" + isPreferred;
            v.add(t);
        }
        return new SortUtils().quickSort(v);
    }


    public Entity getConceptWithProperty(String scheme, String version,
        String code, String propertyName) {
        try {
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            if (version != null) versionOrTag.setVersion(version);

            ConceptReferenceList crefs =
                createConceptReferenceList(new String[] { code }, scheme);
            CodedNodeSet cns = null;

            try {
                cns = lbSvc.getCodingSchemeConcepts(scheme, versionOrTag);
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }

            //cns = cns.restrictToCodes(crefs);

            try {
				cns = cns.restrictToCodes(crefs);

                LocalNameList propertyNames = new LocalNameList();
                if (propertyName != null) propertyNames.addEntry(propertyName);
                CodedNodeSet.PropertyType[] propertyTypes = null;

                //long ms = System.currentTimeMillis(), delay = 0;
                SortOptionList sortOptions = null;
                LocalNameList filterOptions = null;
                boolean resolveObjects = true; // needs to be set to true
                int maxToReturn = 1000;

                ResolvedConceptReferenceList rcrl =
                    cns.resolveToList(sortOptions, filterOptions,
                        propertyNames, propertyTypes, resolveObjects,
                        maxToReturn);

                //HashMap hmap = new HashMap();
                if (rcrl == null) {
                    _logger.warn("Concep not found.");
                    return null;
                }

                if (rcrl.getResolvedConceptReferenceCount() > 0) {
                    // ResolvedConceptReference[] list =
                    // rcrl.getResolvedConceptReference();
                    for (int i = 0; i < rcrl.getResolvedConceptReferenceCount(); i++) {
                        ResolvedConceptReference rcr =
                            rcrl.getResolvedConceptReference(i);
                        Entity c = rcr.getReferencedEntry();
                        return c;
                    }
                }

                return null;

            } catch (Exception e) {
                _logger.error("Method: SearchUtil.getConceptWithProperty");
                _logger.error("* ERROR: getConceptWithProperty throws exceptions.");
                _logger.error("* " + e.getClass().getSimpleName() + ": "
                    + e.getMessage());
                //e.printStackTrace();
            }
        } catch (Exception ex) {
                _logger.error("Method: SearchUtil.getConceptWithProperty");
                _logger.error("* ERROR: getConceptWithProperty throws exceptions.");
                _logger.error("* " + ex.getClass().getSimpleName() + ": "
                    + ex.getMessage());

        }
        return null;
    }


    public Vector getConceptPropertyValues(Entity c, String propertyName) {
        if (c == null) {
            return null;
		}
        Vector v = new Vector();
        Property[] properties = c.getAllProperties();

        for (int j = 0; j < properties.length; j++) {
            Property prop = properties[j];
            if (prop == null) return null;
            if (prop.getPropertyName().compareTo(propertyName) == 0) {
                v.add(prop.getValue().getContent());
            }
        }
        return v;
    }

    public String getConceptStatus(String scheme, String version,
        String ltag, String code) {
        Entity c =
            getConceptWithProperty(scheme, version, code, "Concept_Status");
        String con_status = null;
        if (c != null) {
            Vector status_vec = getConceptPropertyValues(c, "Concept_Status");
            if (status_vec == null) return null;
            if (status_vec == null || status_vec.size() == 0) {
                con_status = c.getStatus();
            } else {
                con_status = gov.nih.nci.evs.browser.utils.StringUtils.convertToCommaSeparatedValue(status_vec);
            }
            return con_status;
        }
        return null;
    }

    public Vector<String> getSupportedPropertyNames(String codingScheme,
        String version) {
        try {
            CodingScheme cs = new CodingSchemeDataUtils(lbSvc).resolveCodingScheme(codingScheme, version);
            return getSupportedPropertyNames(cs);
        } catch (Exception ex) {
        }
        return null;
    }

    public Vector getPropertyValues(Entity concept, String property_type, String property_name) {
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
                if (addQualifiers) {
                    String qualifiers = getPropertyQualfierValues(p);
                    if (qualifiers == null) return null;
                    if (qualifiers.compareTo("") != 0) {
                        //t = t + " (" + getPropertyQualfierValues(p) + ")";
                        t = t + " (" + qualifiers + ")";
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

    public String getPropertyQualfierValues(org.LexGrid.commonTypes.Property p) {
        StringBuffer buf = new StringBuffer();
        String s = "";
        PropertyQualifier[] qualifiers = p.getPropertyQualifier();
        if (qualifiers != null && qualifiers.length > 0) {
            for (int j = 0; j < qualifiers.length; j++) {
                PropertyQualifier q = qualifiers[j];
                String qualifier_name = q.getPropertyQualifierName();
                if (qualifier_name == null) return null;
                if (qualifier_name.compareTo("label") != 0) {
					String qualifier_value = q.getValue().getContent();
					buf.append(qualifier_name + ": " + qualifier_value);
					if (j < qualifiers.length - 1) {
						buf.append("; ");
					}
				}
            }
        }
        s = buf.toString();
        return s;
    }

    public Vector getNCImCodes(String scheme, String version, String code) {
        Vector w = new Vector();
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null) {
			csvt.setVersion(version);
		}
		try {
			ConceptReferenceList crefs = ConvenienceMethods.createConceptReferenceList(new String[] { code }, scheme);
			CodedNodeSet cns = lbSvc.getCodingSchemeConcepts(scheme, csvt);

			if (cns == null) {
				return null;
			}
			cns = cns.restrictToStatus(ActiveOption.ALL, null);
			cns = cns.restrictToCodes(crefs);
			ResolvedConceptReferenceList matches = cns.resolveToList(null, null, null, 1);
			if (matches == null) return null;
			if (matches.getResolvedConceptReferenceCount() > 0) {
				ResolvedConceptReference ref = (ResolvedConceptReference) matches.enumerateResolvedConceptReference()
						.nextElement();
				Entity node = ref.getEntity();
				return getNCImCodes(node);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return w;
    }

    public Vector getNCImCodes(Entity node) {
		if (node == null) return null;
        Vector w = new Vector();
		Property[] props = node.getAllProperties();
		if (props == null) return null;
		for (int i = 0; i < props.length; i++) {
			 Property prop = props[i];
			 PropertyQualifier[] qualifiers = prop.getPropertyQualifier();
			 if (qualifiers == null) return null;
			 for (int k=0; k<qualifiers.length; k++) {
				  PropertyQualifier qualifier = qualifiers[k];
			 }
			 Source[] sources = prop.getSource();
			 if (sources == null) return null;
			 for (int k=0; k<sources.length; k++) {
				  Source source = sources[k];
			 }
			 if (Arrays.asList(Constants.NCIM_CODE_PROPERTYIES).contains(prop.getPropertyName())) {
				 if (!w.contains(prop.getValue().getContent())) {
					w.add(prop.getValue().getContent());
				 }
			 }
		}
		return w;
    }

    public Vector getPropertyNamesByType(Entity concept,
        String property_type) {
        Vector v = new Vector();
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
        if (properties == null || properties.length == 0)
            return v;
        for (int i = 0; i < properties.length; i++) {
            Property p = (Property) properties[i];
            // v.add(p.getText().getContent());
            v.add(p.getPropertyName());
        }
        return v;
    }

    public Vector getConceptStatusByConceptCodes(String scheme,
        String version, String ltag, Vector code_vec) {
        boolean conceptStatusSupported = false;

        Vector w = new Vector();
        long ms = System.currentTimeMillis();
        for (int i = 0; i < code_vec.size(); i++) {
            if (conceptStatusSupported) {
                String code = (String) code_vec.elementAt(i);
                Entity c =
                    getConceptWithProperty(scheme, version, code,
                        "Concept_Status");
                String con_status = null;
                if (c != null) {
                    Vector status_vec =
                        getConceptPropertyValues(c, "Concept_Status");
                    if (status_vec == null || status_vec.size() == 0) {
                        con_status = c.getStatus();
                    } else {
                        con_status =
                            gov.nih.nci.evs.browser.utils.StringUtils.convertToCommaSeparatedValue(status_vec);
                    }
                    w.add(con_status);
                } else {
                    w.add(null);
                }
            } else {
                w.add(null);
            }
        }
        _logger.debug("getConceptStatusByConceptCodes Run time (ms): "
            + (System.currentTimeMillis() - ms) + " number of concepts: "
            + code_vec.size());
        return w;
    }

/*
    public Vector<String> getSupportedPropertyNames(CodingScheme cs) {
        Vector w = getSupportedProperties(cs);
        if (w == null)
            return null;

        Vector<String> v = new Vector<String>();
        for (int i = 0; i < w.size(); i++) {
            SupportedProperty sp = (SupportedProperty) w.elementAt(i);
            v.add(sp.getLocalId());
        }
        return v;
    }
*/

	public boolean isAnnotationPropertyPCode(String t) {
		if (t == null) return false;
		if (t.length() <= 1) return false;
		if (!t.startsWith("P")) return false;
		for (int i=1; i<t.length(); i++) {
			char c = t.charAt(i);
			if (!Character.isDigit(c)) return false;
		}
		return true;
	}

    public Vector<String> getSupportedPropertyNames(CodingScheme cs) {
        Vector w = getSupportedProperties(cs);
		if (w == null) return null;
        Vector<String> v = new Vector<String>();
		for (int i=0; i<w.size(); i++)
		{
		     SupportedProperty sp = (SupportedProperty) w.elementAt(i);
		     if (sp.getUri() != null && isAnnotationPropertyPCode(sp.getLocalId())) {
				 if (!sp.getUri().endsWith(sp.getLocalId())) {
					 v.add(sp.getLocalId());
				 }
			 } else {
				 v.add(sp.getLocalId());
			 }
		}
        return new SortUtils().quickSort(v);
	}


    public Vector<String> getSupportedPropertyData(CodingScheme cs) {
        Vector w = getSupportedProperties(cs);
		if (w == null) return null;
        Vector<String> v = new Vector<String>();
		for (int i=0; i<w.size(); i++)
		{
		     SupportedProperty sp = (SupportedProperty) w.elementAt(i);
		     v.add(sp.getUri() + "|" + sp.getLocalId() + "|" + sp.getContent() + "|" + sp.getPropertyType());
		}
        return new SortUtils().quickSort(v);
	}

    public HashMap getPropertyName2TypeHashMap(CodingScheme cs) {
        Vector w = getSupportedProperties(cs);
		if (w == null) return null;
        HashMap hmap = new HashMap();
		for (int i=0; i<w.size(); i++)
		{
		     SupportedProperty sp = (SupportedProperty) w.elementAt(i);
		     if (sp.getUri() != null) {
				 if (!sp.getUri().endsWith(sp.getLocalId())) {
					 hmap.put(sp.getLocalId(), sp.getPropertyType().toString());
				 }
			 }
		}
        return hmap;
	}


    public CodingScheme getCodingScheme(String codingScheme,
        CodingSchemeVersionOrTag versionOrTag) throws LBException {

        CodingScheme cs = null;
        try {
            //LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            cs = lbSvc.resolveCodingScheme(codingScheme, versionOrTag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cs;
    }

    public Vector<SupportedProperty> getSupportedProperties(
        CodingScheme cs) {
        if (cs == null)
            return null;
        Vector<SupportedProperty> v = new Vector<SupportedProperty>();
        SupportedProperty[] properties =
            cs.getMappings().getSupportedProperty();
        for (int i = 0; i < properties.length; i++) {
            SupportedProperty sp = (SupportedProperty) properties[i];
            v.add(sp);
        }
        return v;
    }

    public String getVocabularyVersionByTag(String codingSchemeName,
        String ltag) {

		if (codingSchemeName == null) {
			codingSchemeName = "NCI Thesaurus";
		}

        String version = null;
        int knt = 0;
        try {
            CodingSchemeRenderingList lcsrl = lbSvc.getSupportedCodingSchemes();
            CodingSchemeRendering[] csra = lcsrl.getCodingSchemeRendering();
            if (csra == null) return null;
            for (int i = 0; i < csra.length; i++) {
                CodingSchemeRendering csr = csra[i];
                CodingSchemeSummary css = csr.getCodingSchemeSummary();
                if (css == null) return null;
                if (css.getFormalName().compareTo(codingSchemeName) == 0
                    || css.getLocalName().compareTo(codingSchemeName) == 0
                    || css.getCodingSchemeURI().compareTo(codingSchemeName) == 0) {
					version = css.getRepresentsVersion();
                    knt++;

                    if (ltag == null)
                        return version;
                    RenderingDetail rd = csr.getRenderingDetail();
                    CodingSchemeTagList cstl = rd.getVersionTags();
                    java.lang.String[] tags = cstl.getTag();
                    if (tags == null)
                        return version;
					if (tags.length > 0) {
                        for (int j = 0; j < tags.length; j++) {
                            String version_tag = (String) tags[j];

                            if (version_tag != null && version_tag.compareToIgnoreCase(ltag) == 0) {
                                return version;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       if (ltag != null && ltag.compareToIgnoreCase(Constants.PRODUCTION) == 0
            & knt == 1) {
            return version;
        }
        return null;
    }

    public String getNamespaceByCode(String codingSchemeName, String vers, String code) {
        try {
			if (code == null) {
				return null;
			}
			if (code.indexOf("@") != -1) return null; // anonymous class
            CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
            if (vers != null) versionOrTag.setVersion(vers);

            ConceptReferenceList crefs = createConceptReferenceList(
                    new String[] { code }, codingSchemeName);

            CodedNodeSet cns = null;
            try {
				try {
					cns = getNodeSet(codingSchemeName, versionOrTag);

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}

                if (cns == null) {
					return null;
				}

                cns = cns.restrictToCodes(crefs);
 				ResolvedConceptReferenceList matches = null;
				try {
					matches = cns.resolveToList(null, null, null, 1);
				} catch (Exception e) {
					e.printStackTrace();
				}

                if (matches == null) {
					System.out.println("DataUtils getNamespaceByCode --  no match.");

                    return null;
                }
                int count = matches.getResolvedConceptReferenceCount();
                // Analyze the result ...
                if (count == 0)
                    return null;
                if (count > 0) {
                    try {
                        ResolvedConceptReference ref = (ResolvedConceptReference) matches
                                .enumerateResolvedConceptReference()
                                .nextElement();

                        return ref.getCodeNamespace();
                    } catch (Exception ex1) {
                        ex1.printStackTrace();
                        return null;
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static NameAndValueList createNameAndValueList(String[] names, String[] values) {
        NameAndValueList nvList = new NameAndValueList();
        for (int i = 0; i < names.length; i++) {
            NameAndValue nv = new NameAndValue();
            nv.setName(names[i]);
            if (values != null) {
                nv.setContent(values[i]);
            }
            nvList.addNameAndValue(nv);
        }
        return nvList;
    }

    public static NameAndValueList createNameAndValueList(Vector names, Vector values) {
        if (names == null)
            return null;
        NameAndValueList nvList = new NameAndValueList();
        for (int i = 0; i < names.size(); i++) {
            String name = (String) names.elementAt(i);
            String value = (String) values.elementAt(i);
            NameAndValue nv = new NameAndValue();
            nv.setName(name);
            if (value != null) {
                nv.setContent(value);
            }
            nvList.addNameAndValue(nv);
        }
        return nvList;
    }


    public static LocalNameList vector2LocalNameList(Vector<String> v) {
        if (v == null)
            return null;
        LocalNameList list = new LocalNameList();
        for (int i = 0; i < v.size(); i++) {
            String vEntry = (String) v.elementAt(i);
            list.addEntry(vEntry);
        }
        return list;
    }

    public String getPreferredName(Entity c) {
        Presentation[] presentations = c.getPresentation();
        if (presentations == null) return null;
        for (int i = 0; i < presentations.length; i++) {
            Presentation p = presentations[i];
            if (p == null) return null;
            if (p.getPropertyName().compareTo("Preferred_Name") == 0) {
                return p.getValue().getContent();
            }
        }
        return null;
    }

    public HashMap getPropertyValues(String scheme, String version, String propertyType, String propertyName) {
		HashMap hmap = new HashMap();
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		if (version != null) versionOrTag.setVersion(version);
		try {
			CodedNodeSet cns = getNodeSet(scheme, versionOrTag);
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
						hmap.put(key, value);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return hmap;
	}

    public Vector getAssociationSourceCodes(String scheme, String version,
        String code, String assocName) {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);
        ResolvedConceptReferenceList matches = null;
        Vector v = new Vector();
        try {
            CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
            Boolean restrictToAnonymous = Boolean.FALSE;
            cng = cng.restrictToAnonymous(restrictToAnonymous);
            NameAndValueList nameAndValueList =
                createNameAndValueList(new String[] { assocName }, null);

            NameAndValueList nameAndValueList_qualifier = null;
            cng =
                cng.restrictToAssociations(nameAndValueList,
                    nameAndValueList_qualifier);

            matches =
                cng.resolveAsList(ConvenienceMethods.createConceptReference(
                    code, scheme), false, true, 1, 1, new LocalNameList(),
                    null, null, _maxReturn);

            if (matches.getResolvedConceptReferenceCount() > 0) {
                java.util.Enumeration<? extends ResolvedConceptReference> refEnum = matches.enumerateResolvedConceptReference();
                if (refEnum == null) return null;
                while (refEnum.hasMoreElements()) {
                    ResolvedConceptReference ref = (ResolvedConceptReference) refEnum.nextElement();

                    AssociationList targetof = ref.getTargetOf();
                    Association[] associations = targetof.getAssociation();

                    for (int i = 0; i < associations.length; i++) {
                        Association assoc = associations[i];
                        AssociatedConcept[] acl =
                            assoc.getAssociatedConcepts()
                                .getAssociatedConcept();
                        for (int j = 0; j < acl.length; j++) {
                            AssociatedConcept ac = acl[j];
                            v.add(ac.getReferencedEntry().getEntityCode());
                        }
                    }
                }
                new SortUtils().quickSort(v);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return v;
    }

    public Vector getSynonyms(String scheme, String version, String tag, String code) {
        Entity concept = getConceptByCode(scheme, version, code);
        return getSynonyms(scheme, concept);
    }

    public Vector getSynonyms(String scheme, Entity concept) {
        if (concept == null)
            return null;
        Vector v = new Vector();
        Presentation[] properties = concept.getPresentation();
        int n = 0;
        boolean inclusion = true;
        for (int i = 0; i < properties.length; i++) {
            Presentation p = properties[i];
            // for NCI Thesaurus or Pre-NCI Thesaurus, show FULL_SYNs only
            if (scheme != null && (scheme.indexOf("NCI_Thesaurus") != -1 || scheme.indexOf("NCI Thesaurus") != -1)) {
                inclusion = false;
                if (p.getPropertyName().compareTo("FULL_SYN") == 0) {
                    inclusion = true;
                }
            }
            if (inclusion) {
                String term_name = p.getValue().getContent();
                String term_type = "null";
                String term_source = "null";
                String term_source_code = "null";
                String term_subsource = "null";

                PropertyQualifier[] qualifiers = p.getPropertyQualifier();
                if (qualifiers != null) {
                    for (int j = 0; j < qualifiers.length; j++) {
                        PropertyQualifier q = qualifiers[j];
                        String qualifier_name = q.getPropertyQualifierName();
                        String qualifier_value = q.getValue().getContent();
                        if (qualifier_name.compareTo("source-code") == 0) {
                            term_source_code = qualifier_value;
                        }
                        if (qualifier_name.compareTo("subsource-name") == 0) {
                            term_subsource = qualifier_value;
                        }
                    }
                }
                term_type = p.getRepresentationalForm();
                Source[] sources = p.getSource();
                if (sources != null && sources.length > 0) {
                    Source src = sources[0];
                    term_source = src.getContent();
                }
                v.add(term_name + "|" + term_type + "|" + term_source + "|"
                    + term_source_code + "|" +  term_subsource);
            }
        }
        new SortUtils().quickSort(v);
        return v;
    }


    public Vector getAllSynonyms(String scheme, Entity concept) {
        if (concept == null)
            return null;
        Vector v = new Vector();
        Presentation[] properties = concept.getPresentation();
        int n = 0;
        //boolean inclusion = true;
        for (int i = 0; i < properties.length; i++) {
            Presentation p = properties[i];
			String term_name = p.getValue().getContent();
			String term_type = "null";
			String term_source = "null";
			String term_source_code = "null";
			String term_subsource = "null";

			PropertyQualifier[] qualifiers = p.getPropertyQualifier();
			if (qualifiers != null) {
				for (int j = 0; j < qualifiers.length; j++) {
					PropertyQualifier q = qualifiers[j];
					String qualifier_name = q.getPropertyQualifierName();
					String qualifier_value = q.getValue().getContent();
					if (qualifier_name.compareTo("source-code") == 0) {
						term_source_code = qualifier_value;
					}
					if (qualifier_name.compareTo("subsource-name") == 0) {
						term_subsource = qualifier_value;
					}
				}
			}
			term_type = p.getRepresentationalForm();
			Source[] sources = p.getSource();
			if (sources != null && sources.length > 0) {
				Source src = sources[0];
				term_source = src.getContent();
			}
			v.add(term_name + "|" + term_type + "|" + term_source + "|"
				+ term_source_code + "|" +  term_subsource);
        }
        new SortUtils().quickSort(v);
        return v;
    }

    public HashMap getPropertyValuesForCodes(String scheme, String version,
        Vector codes, String propertyName) {
        try {
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            versionOrTag.setVersion(version);

            ConceptReferenceList crefs =
                createConceptReferenceList(codes, scheme);

            CodedNodeSet cns = null;

            try {
                cns = lbSvc.getCodingSchemeConcepts(scheme, versionOrTag);
                if (cns == null) return null;
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
            try {
				cns = cns.restrictToCodes(crefs);
                LocalNameList propertyNames = new LocalNameList();
                propertyNames.addEntry(propertyName);
                CodedNodeSet.PropertyType[] propertyTypes = null;

                //long ms = System.currentTimeMillis(), delay = 0;
                SortOptionList sortOptions = null;
                LocalNameList filterOptions = null;
                boolean resolveObjects = true; // needs to be set to true
                int maxToReturn = -1;

                ResolvedConceptReferenceList rcrl =
                    cns.resolveToList(sortOptions, filterOptions,
                        propertyNames, propertyTypes, resolveObjects,
                        maxToReturn);

                // _logger.debug("resolveToList done");
                HashMap hmap = new HashMap();

                if (rcrl == null) {
                    _logger.debug("Concept not found.");
                    return null;
                }

                if (rcrl.getResolvedConceptReferenceCount() > 0) {
                    // ResolvedConceptReference[] list =
                    // rcrl.getResolvedConceptReference();
                    for (int i = 0; i < rcrl.getResolvedConceptReferenceCount(); i++) {
                        ResolvedConceptReference rcr =
                            rcrl.getResolvedConceptReference(i);
                        // _logger.debug("(*) " + rcr.getCode());
                        Entity c = rcr.getReferencedEntry();
                        if (c == null) {
                            _logger.debug("Concept is null.");
                        } else {
                            _logger
                                .debug(c.getEntityDescription().getContent());
                            Property[] properties = c.getProperty();
                            //String values = "";
                            StringBuffer buf = new StringBuffer();
                            for (int j = 0; j < properties.length; j++) {
                                Property prop = properties[j];
                                //values = values + prop.getValue().getContent();
                                buf.append(prop.getValue().getContent());

                                if (j < properties.length - 1) {
                                    //values = values + "; ";
                                    buf.append("; ");
                                }
                            }
                            String values = buf.toString();
                            hmap.put(rcr.getCode(), values);
                        }
                    }
                }
                return hmap;

            } catch (Exception e) {
                _logger.error("Method: SearchUtil.searchByProperties");
                _logger.error("* ERROR: cns.resolve throws exceptions.");
                _logger.error("* " + e.getClass().getSimpleName() + ": "
                    + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ResolvedConceptReferencesIterator codedNodeGraph2CodedNodeSetIterator(
        CodedNodeGraph cng, ConceptReference graphFocus,
        boolean resolveForward, boolean resolveBackward,
        int resolveAssociationDepth, int maxToReturn) {
        CodedNodeSet cns = null;
        try {
            cns =
                cng.toNodeList(graphFocus, resolveForward, resolveBackward,
                    resolveAssociationDepth, maxToReturn);

            if (cns == null) {
                _logger.warn("cng.toNodeList returns null???");
                return null;
            }

            SortOptionList sortCriteria = null;
            LocalNameList propertyNames = null;
            CodedNodeSet.PropertyType[] propertyTypes = null;
            ResolvedConceptReferencesIterator iterator = null;
            try {
                iterator =
                    cns.resolve(sortCriteria, propertyNames, propertyTypes);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (iterator == null) {
                _logger.warn("cns.resolve returns null???");
            }
            return iterator;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public HashMap getPropertyName2ValueHashMap(Entity curr_concept) {
		if (curr_concept == null) return null;
		Vector propertytypes = new Vector();
		propertytypes.add("PRESENTATION");
		propertytypes.add("DEFINITION");
		propertytypes.add("GENERIC");
		propertytypes.add("COMMENT");
		HashSet hset = new HashSet();
		HashMap hmap = new HashMap();
		Vector propertyvalues = null;
		for (int i=0; i<propertytypes.size(); i++) {
		    String propertytype = (String) propertytypes.elementAt(i);
		    Vector propertynames = getPropertyNamesByType(
			    curr_concept, propertytype);

		    for (int j=0; j<propertynames.size(); j++) {
			    String propertyname = (String) propertynames.elementAt(j);
			    if (!hset.contains(propertyname)) {
			        hset.add(propertyname);
			        propertyvalues = getPropertyValues(curr_concept, propertytype, propertyname);
			        if (propertyvalues != null) {
				        hmap.put(propertyname, propertyvalues);
					}
			    }
		    }
		}
		return hmap;
	}

    public HashMap getPropertyQualifierHashMap(Entity node) {
		HashMap hmap = new HashMap();
		Presentation[] presentations = node.getPresentation();
		if (presentations == null) return null;
		for (int i = 0; i < presentations.length; i++) {
			 Presentation presentation = presentations[i];
			 String key = presentation.getPropertyName() + "$" + presentation.getValue().getContent();
			 PropertyQualifier[] qualifiers = presentation.getPropertyQualifier();
			 if (qualifiers == null) return null;
			 for (int k=0; k<qualifiers.length; k++) {
				  PropertyQualifier qualifier = qualifiers[k];
				  String value = qualifier.getPropertyQualifierName() + "=" + qualifier.getValue().getContent();
				  Vector v = new Vector();
				  if (hmap.containsKey(key)) {
					   v = (Vector) hmap.get(key);
				  }
				  v.add(value);
				  hmap.put(key, v);
			 }
     	}

		Definition[] definitions = node.getDefinition();
		if (definitions == null) return null;
		for (int i = 0; i < definitions.length; i++) {
			 Definition definition = definitions[i];
			 String key = definition.getPropertyName() + "$" + definition.getValue().getContent();
			 PropertyQualifier[] qualifiers = definition.getPropertyQualifier();
			 if (qualifiers == null) return null;
			 for (int k=0; k<qualifiers.length; k++) {
				  PropertyQualifier qualifier = qualifiers[k];
				  String value = qualifier.getPropertyQualifierName() + "=" + qualifier.getValue().getContent();
				  Vector v = new Vector();
				  if (hmap.containsKey(key)) {
					   v = (Vector) hmap.get(key);
				  }
				  v.add(value);
				  hmap.put(key, v);
			 }
		}

		Comment[] comments = node.getComment();
		if (comments == null) return null;
		for (int i = 0; i < comments.length; i++) {
			 Comment comment = comments[i];
			 String key = comment.getPropertyName() + "$" + comment.getValue().getContent();
			 PropertyQualifier[] qualifiers = comment.getPropertyQualifier();
			 if (qualifiers == null) return null;
			 for (int k=0; k<qualifiers.length; k++) {
				  PropertyQualifier qualifier = qualifiers[k];
				  String value = qualifier.getPropertyQualifierName() + "=" + qualifier.getValue().getContent();
				  Vector v = new Vector();
				  if (hmap.containsKey(key)) {
					   v = (Vector) hmap.get(key);
				  }
				  v.add(value);
				  hmap.put(key, v);
			 }
		}

		Property[] properties = node.getProperty();
		if (properties == null) return null;
		for (int i = 0; i < properties.length; i++) {
			 Property property = properties[i];
			 String key = property.getPropertyName() + "$" + property.getValue().getContent();
			 PropertyQualifier[] qualifiers = property.getPropertyQualifier();
			 if (qualifiers == null) return null;
			 for (int k=0; k<qualifiers.length; k++) {
				  PropertyQualifier qualifier = qualifiers[k];
				  String value = qualifier.getPropertyQualifierName() + "=" + qualifier.getValue().getContent();
				  Vector v = new Vector();
				  if (hmap.containsKey(key)) {
					   v = (Vector) hmap.get(key);
				  }
				  v.add(value);
				  hmap.put(key, v);
			 }
		}
        return hmap;
    }

    public Vector getRelationshipSource(String scheme, String version, String code) {
		return getRelationshipTarget(scheme, version, code, true);
	}

    public Vector getRelationshipSource(String scheme, String version, String code, boolean namedClassOnly) {
		Vector v = new Vector();
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		if (version != null) {
			csvt.setVersion(version);
		}
        // Perform the query ...
        try {
			ResolvedConceptReferenceList matches = lbSvc.getNodeGraph(scheme, csvt, null).resolveAsList(
					ConvenienceMethods.createConceptReference(code, scheme), false, true, 1, 1, new LocalNameList(), null,
					null, -1);

			// Analyze the result ...
			if (matches.getResolvedConceptReferenceCount() > 0) {
				Enumeration<? extends ResolvedConceptReference> refEnum = matches.enumerateResolvedConceptReference();

				while (refEnum.hasMoreElements()) {
					ResolvedConceptReference ref = refEnum.nextElement();
					AssociationList targetof = ref.getTargetOf();
					if (targetof != null) {
						Association[] associations = targetof.getAssociation();
						if (associations != null && associations.length > 0) {
							for (int i = 0; i < associations.length; i++) {
								Association assoc = associations[i];
								AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
								if (acl != null) {
									for (int j = 0; j < acl.length; j++) {
										AssociatedConcept ac = acl[j];
										if (ac != null) {
											if (namedClassOnly) {
												if (!ac.getConceptCode().startsWith("@")) {
													String rela = assoc.getAssociationName();
													EntityDescription ed = ac.getEntityDescription();
													v.add(ed.getContent() + "$" + ac.getConceptCode() + "$" + rela);
												}
											} else {
												String rela = assoc.getAssociationName();
												EntityDescription ed = ac.getEntityDescription();
												v.add(ed.getContent() + "$" + ac.getConceptCode() + "$" + rela);
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
			ex.printStackTrace();
		}
        return v;
    }

    public Vector getRelationshipTarget(String scheme, String version, String code) {
		return getRelationshipTarget(scheme, version, code, true);
	}


    public Vector getRelationshipTarget(String scheme, String version, String code, boolean namedClassOnly) {
		Vector v = new Vector();
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		if (version != null) {
			csvt.setVersion(version);
		}
        // Perform the query ...
        try {
			ResolvedConceptReferenceList matches = lbSvc.getNodeGraph(scheme, csvt, null).resolveAsList(
					ConvenienceMethods.createConceptReference(code, scheme), true, false, 1, 1, new LocalNameList(), null,
					null, -1);

			// Analyze the result ...
			if (matches.getResolvedConceptReferenceCount() > 0) {
				Enumeration<? extends ResolvedConceptReference> refEnum = matches.enumerateResolvedConceptReference();

				while (refEnum.hasMoreElements()) {
					ResolvedConceptReference ref = refEnum.nextElement();
					AssociationList sourceof = ref.getSourceOf();

					if (sourceof != null) {
						Association[] associations = sourceof.getAssociation();

						if (associations != null && associations.length > 0) {
							for (int i = 0; i < associations.length; i++) {
								Association assoc = associations[i];
								//displayMessage(pw, "\t" + assoc.getAssociationName());

								AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
								if (acl == null) return null;
								for (int j = 0; j < acl.length; j++) {
									AssociatedConcept ac = acl[j];
									if (namedClassOnly) {
										if (!ac.getConceptCode().startsWith("@")) {
											String rela = assoc.getAssociationName();
											EntityDescription ed = ac.getEntityDescription();
											v.add(ed.getContent() + "$" + ac.getConceptCode() + "$" + rela);
										}
								    } else {
										String rela = assoc.getAssociationName();
										EntityDescription ed = ac.getEntityDescription();
										v.add(ed.getContent() + "$" + ac.getConceptCode() + "$" + rela);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return v;
    }

    public Vector getPropertyTable(Entity node) {
		Vector w = new Vector();
		StringBuffer buf = new StringBuffer();

		Presentation[] presentations = node.getPresentation();
		if (presentations == null) return null;
		for (int i = 0; i < presentations.length; i++) {
			 buf = new StringBuffer();
			 Presentation presentation = presentations[i];
			 buf.append(presentation.getPropertyName() + "|" + presentation.getValue().getContent());
			 StringBuffer qualifier_buf = new StringBuffer();
			 PropertyQualifier[] qualifiers = presentation.getPropertyQualifier();
			 if (qualifiers == null) return null;
			 for (int k=0; k<qualifiers.length; k++) {
				  PropertyQualifier qualifier = qualifiers[k];
				  String value = qualifier.getPropertyQualifierName() + "=" + qualifier.getValue().getContent();
				  qualifier_buf.append(value).append("$");
			 }
			 String qualifier_str = qualifier_buf.toString();
			 if (qualifier_str == null) return null;
			 if (qualifier_str.endsWith("$")) {
				 qualifier_str = qualifier_str.substring(0, qualifier_str.length()-1);
			 }
			 if (qualifier_buf.length() > 0) {
				 buf.append("|").append(qualifier_str);
			 }
			 w.add(buf.toString());
     	}

		Definition[] definitions = node.getDefinition();
		if (definitions == null) return null;
		for (int i = 0; i < definitions.length; i++) {
			 buf = new StringBuffer();
			 Definition definition = definitions[i];
			 buf.append(definition.getPropertyName() + "|" + definition.getValue().getContent());
			 StringBuffer qualifier_buf = new StringBuffer();
			 PropertyQualifier[] qualifiers = definition.getPropertyQualifier();
			 if (qualifiers == null) return null;
			 for (int k=0; k<qualifiers.length; k++) {
				  PropertyQualifier qualifier = qualifiers[k];
				  String value = qualifier.getPropertyQualifierName() + "=" + qualifier.getValue().getContent();
				  qualifier_buf.append(value).append("$");
			 }
			 String qualifier_str = qualifier_buf.toString();
			 if (qualifier_str == null) return null;
			 if (qualifier_str.endsWith("$")) {
				 qualifier_str = qualifier_str.substring(0, qualifier_str.length()-1);
			 }
			 if (qualifier_buf.length() > 0) {
				 buf.append("|").append(qualifier_str);
			 }
			 w.add(buf.toString());
		}

		Comment[] comments = node.getComment();
		if (comments == null) return null;
		for (int i = 0; i < comments.length; i++) {
			 buf = new StringBuffer();
			 Comment comment = comments[i];
			 buf.append(comment.getPropertyName() + "|" + comment.getValue().getContent());
			 StringBuffer qualifier_buf = new StringBuffer();
			 PropertyQualifier[] qualifiers = comment.getPropertyQualifier();
			 if (qualifiers == null) return null;
			 for (int k=0; k<qualifiers.length; k++) {
				  PropertyQualifier qualifier = qualifiers[k];
				  String value = qualifier.getPropertyQualifierName() + "=" + qualifier.getValue().getContent();
				  qualifier_buf.append(value).append("$");
			 }
			 String qualifier_str = qualifier_buf.toString();
			 if (qualifier_str == null) return null;
			 if (qualifier_str.endsWith("$")) {
				 qualifier_str = qualifier_str.substring(0, qualifier_str.length()-1);
			 }
			 if (qualifier_buf.length() > 0) {
				 buf.append("|").append(qualifier_str);
			 }
			 w.add(buf.toString());
		}

		Property[] properties = node.getProperty();
		if (properties == null) return null;
		for (int i = 0; i < properties.length; i++) {
			 buf = new StringBuffer();
			 Property property = properties[i];
			 buf.append(property.getPropertyName() + "|" + property.getValue().getContent());
			 StringBuffer qualifier_buf = new StringBuffer();
			 PropertyQualifier[] qualifiers = property.getPropertyQualifier();
			 if (qualifiers == null) return null;
			 for (int k=0; k<qualifiers.length; k++) {
				  PropertyQualifier qualifier = qualifiers[k];
				  String value = qualifier.getPropertyQualifierName() + "=" + qualifier.getValue().getContent();
				  qualifier_buf.append(value).append("$");
			 }
			 String qualifier_str = qualifier_buf.toString();
			 if (qualifier_str == null) return null;
			 if (qualifier_str.endsWith("$")) {
				 qualifier_str = qualifier_str.substring(0, qualifier_str.length()-1);
			 }
			 if (qualifier_buf.length() > 0) {
				 buf.append("|").append(qualifier_str);
			 }
			 w.add(buf.toString());
		}

		w = new SortUtils().quickSort(w);
        return w;
    }

	public String sortPropertyQualifiers(String qualifiers) {
		if (qualifiers == null) return null;
		Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(qualifiers, "$");
		u = new SortUtils().quickSort(u);
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<u.size(); i++) {
			String t = (String) u.elementAt(i);
			buf.append(t).append("$");
		}
		String s = buf.toString();
		if (s == null) return null;
		if (s.endsWith("$")) {
			s = s.substring(0, s.length()-1);
		}
		return s;
	}


    public HashMap getPropertyNameValue2QualifierHashMap(Vector property_table) {
		if (property_table == null) return null;
		HashMap hmap = new HashMap();
		String qualifiers = null;
		for (int i=0; i<property_table.size(); i++) {
			String t = (String) property_table.elementAt(i);
			Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(t);
			String proprety_name = (String) u.elementAt(0);
			String proprety_value = (String) u.elementAt(1);
			qualifiers = null;
			if (u.size() > 2) {
				qualifiers = (String) u.elementAt(2);
				qualifiers = sortPropertyQualifiers(qualifiers);
			}
			Vector w = new Vector();
			if (qualifiers != null) {
				String nv = proprety_name + "|" + proprety_value;
				if (hmap.containsKey(nv)) {
					w = (Vector) hmap.get(nv);
				}
				if (!w.contains(qualifiers)) {
					w.add(qualifiers);
				}
				hmap.put(nv, w);
			}
		}
        return hmap;
	}

    public Vector searchPropertyWithQualifierNameAndValue(HashMap propertyQualifierHashMap,
        String qualifierName, String qualifierValue) {
		if (propertyQualifierHashMap == null) return null;
		String target = qualifierName + "=" + qualifierValue;
		Vector w = new Vector();
		Iterator it = propertyQualifierHashMap.keySet().iterator();
		if (it == null) return null;
		while (it.hasNext()) {
			String nv = (String) it.next();
			Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(nv, "$");
			if (u != null) {
				String propertyName = (String) u.elementAt(0);
				//String propertyValue = (String) u.elementAt(1);
				Vector v = (Vector) propertyQualifierHashMap.get(nv);
				if (v != null) {
					if (v.contains(target) && !w.contains(propertyName)) {
						w.add(propertyName);
					}
				}
		    }
		}
		w = new SortUtils().quickSort(w);
		return w;
	}


    public Vector getPropertyNames(HashMap propertyQualifierHashMap) {
		Vector w = new Vector();
		Iterator it = propertyQualifierHashMap.keySet().iterator();
		if (it == null) return null;
		while (it.hasNext()) {
			String nv = (String) it.next();
			Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(nv, "$");
			if (u != null) {
				String propertyName = (String) u.elementAt(0);
				if (!w.contains(propertyName)) {
					w.add(propertyName);
				}
		    }
		}
		w = new SortUtils().quickSort(w);
		return w;
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String property2String(Property p) {
		StringBuffer buf = new StringBuffer();
		buf.append(p.getValue().getContent());
		PropertyQualifier[] qualifiers = p.getPropertyQualifier();
		StringBuffer qual_buf = new StringBuffer();
		if (qualifiers != null && qualifiers.length>0) {
			for (int j=0; j<qualifiers.length; j++) {
				PropertyQualifier q = qualifiers[j];
				String qualifier_name = q.getPropertyQualifierName();
				String qualifier_value = q.getValue().getContent();
				qual_buf.append(qualifier_name).append("=").append(qualifier_value);
				qual_buf.append("$");
			}
		}

		Source[] sources = p.getSource();
		if (sources != null && sources.length > 0) {
			Source src = sources[0];
			String term_source = src.getContent();
			qual_buf.append("source").append("=").append(term_source);
			qual_buf.append("$");
		}

		if (p instanceof org.LexGrid.concepts.Presentation) {
			Presentation presentation = (Presentation) p;
			String term_type = presentation.getRepresentationalForm();
			if (term_type != null) {
				qual_buf.append("term-type").append("=").append(term_type);
			}
		}

		buf.append("|").append(qual_buf);
		String line = buf.toString();
		if (line == null) return null;
		if (line.endsWith("$")) {
			line = line.substring(0, line.length()-1);
		}
		if (line.endsWith("|")) {
			line = line.substring(0, line.length()-1);
		}
		return line;
    }


    public void dumpPropertyHashMap(HashMap propertyHashMap) {
		if (propertyHashMap == null) return;
		Iterator it = propertyHashMap.keySet().iterator();
		if (it == null) return;
		while (it.hasNext()) {
			String code = (String) it.next();
			System.out.println("\n" + code);
			HashMap hmap = (HashMap) propertyHashMap.get(code);
			Iterator it2 = hmap.keySet().iterator();
			if (it2 == null) return;
			while (it2.hasNext()) {
				String key = (String) it2.next();
				Vector values = (Vector) hmap.get(key);
				if (values == null) return;
				for (int k=0; k<values.size(); k++) {
					String value = (String) values.elementAt(k);
					System.out.println("\t" + key + ":" + value);
				}
			}
		}
	}

	public HashMap constructPropertyHashMap(Entity e) {
		HashMap hmap = new HashMap();
		//key:   prop_name
		//value: Vector(prop_value|qual-name=qual-value$qual-name=qual-value$...)
		Presentation[] presentations = e.getPresentation();
        if (presentations == null) return null;
        for (int i = 0; i < presentations.length; i++) {
            Presentation p = presentations[i];
            String prop_name = p.getPropertyName();
            Vector v = new Vector();
            if (hmap.containsKey(prop_name)) {
				v = (Vector) hmap.get(prop_name);
			}
			String value = property2String(p);
			v.add(value);
			hmap.put(prop_name, v);
		}


		Property[] properties = e.getProperty();
        if (properties == null) return null;
        for (int i = 0; i < properties.length; i++) {
            Property p = properties[i];
            String prop_name = p.getPropertyName();
            Vector v = new Vector();
            if (hmap.containsKey(prop_name)) {
				v = (Vector) hmap.get(prop_name);
			}
			String value = property2String(p);
			v.add(value);
			hmap.put(prop_name, v);
		}

		Definition[] definitions = e.getDefinition();
        if (definitions == null) return null;
        for (int i = 0; i < definitions.length; i++) {
            Definition p = definitions[i];
            String prop_name = p.getPropertyName();
            Vector v = new Vector();
            if (hmap.containsKey(prop_name)) {
				v = (Vector) hmap.get(prop_name);
			}
			String value = property2String(p);
			v.add(value);
			hmap.put(prop_name, v);
		}

		Comment[] comments = e.getComment();
        if (comments == null) return null;
        for (int i = 0; i < comments.length; i++) {
            Comment p = comments[i];
            String prop_name = p.getPropertyName();
            Vector v = new Vector();
            if (hmap.containsKey(prop_name)) {
				v = (Vector) hmap.get(prop_name);
			}
			String value = property2String(p);
			v.add(value);
			hmap.put(prop_name, v);
		}

		return hmap;
	}

    public HashMap getPropertyValuesForCodes(String scheme, String version,
        Vector codes, Vector property_names) {
        try {
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            versionOrTag.setVersion(version);

            ConceptReferenceList crefs =
                createConceptReferenceList(codes, scheme);

            CodedNodeSet cns = null;

            try {
                cns = lbSvc.getCodingSchemeConcepts(scheme, versionOrTag);
                if (cns == null) return null;
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }

            try {
				cns = cns.restrictToCodes(crefs);
                LocalNameList propertyNames = new LocalNameList();
                for (int i=0; i<property_names.size(); i++) {
					String property_name = (String) property_names.elementAt(i);
                	propertyNames.addEntry(property_name);
				}
                CodedNodeSet.PropertyType[] propertyTypes = new CodedNodeSet.PropertyType[4];
				propertyTypes[0] = CodedNodeSet.PropertyType.PRESENTATION;
				propertyTypes[1] = CodedNodeSet.PropertyType.DEFINITION;
				propertyTypes[2] = CodedNodeSet.PropertyType.GENERIC;
				propertyTypes[3] = CodedNodeSet.PropertyType.COMMENT;

                SortOptionList sortOptions = null;
                LocalNameList filterOptions = null;
                boolean resolveObjects = true;
                int maxToReturn = -1;

                ResolvedConceptReferencesIterator iterator =
                    cns.resolve(sortOptions, filterOptions, propertyNames, propertyTypes, resolveObjects);
                if (iterator == null) return null;
                HashMap code2PropHashMap = new HashMap();
                while (iterator.hasNext()) {
                    ResolvedConceptReference rcr = (ResolvedConceptReference) iterator.next();
                    Entity c = rcr.getReferencedEntry();
					HashMap hmap = constructPropertyHashMap(c);
					code2PropHashMap.put(c.getEntityCode(), hmap);
				}
                return code2PropHashMap;
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public HashMap string2HashMap(String qualifier) {
		HashMap qual_hmap = new HashMap();
		Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(qualifier, '$');
		if (u == null) return null;
		for (int i=0; i<u.size(); i++) {
			String t = (String) u.elementAt(i);
			Vector u2 = gov.nih.nci.evs.browser.utils.StringUtils.parseData(t, '=');
			String qual_name = (String) u2.elementAt(0);
			String qual_value = (String) u2.elementAt(1);
			qual_hmap.put(qual_name, qual_value);
		}
		return qual_hmap;
	}


    public Vector getMatchedPropertyValue(HashMap hmap, String property_name, String source, String term_type,
                                          String qualifier_name, String qualifier_value) {

		if (hmap == null) return null;
		Vector w = new Vector();
		Iterator it = hmap.keySet().iterator();

		while (it.hasNext()) {
			String prop_name = (String) it.next();
			Vector prop_values = (Vector) hmap.get(prop_name);
			if (prop_name.compareTo(property_name) == 0) {

				for (int k=0; k<prop_values.size(); k++) {
					boolean matched = true;
					String value = (String) prop_values.elementAt(k);
					Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(value, '|');
					String prop_value = (String) u.elementAt(0);
					if (u.size() > 1) {
						String qualifier_str = (String) u.elementAt(1);
						HashMap qual_hmap = string2HashMap(qualifier_str);
						if (source != null) {
							if (qual_hmap.containsKey(SOURCE)) {
								String s = (String) qual_hmap.get(SOURCE);
								if (s == null) return null;
								if (s.compareTo(source) != 0) {
									matched = false;
								}
							}
						}
						if (term_type != null) {
							if (qual_hmap.containsKey(TERM_TYPE)) {
								String s = (String) qual_hmap.get(TERM_TYPE);
								if (s == null) return null;
								if (s.compareTo(term_type) != 0) {
									matched = false;
								}
							}
						}

						if (qualifier_name != null) {
							if (qual_hmap.containsKey(qualifier_name)) {
								String s = (String) qual_hmap.get(qualifier_name);
								if (s == null) return null;
								if (s.compareTo(qualifier_value) != 0) {
									matched = false;
								}
							}
						}
					}
					if (matched) {
						w.add(prop_value);
					}
				}
			}
		}
		return w;
	}


}
