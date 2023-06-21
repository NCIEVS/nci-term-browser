package gov.nih.nci.evs.browser.utils;


import gov.nih.nci.evs.browser.common.*;
//import gov.nih.nci.evs.security.SecurityToken;
//import gov.nih.nci.system.client.ApplicationServiceProvider;
import java.io.*;
import java.text.*;
import java.util.*;
import org.junit.Test;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;


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


public class RoleDataGenerator {

	static String ASSOCIATION = "association";
	static String NOT_AVAILABLE = "not available";
	LexBIGService lbSvc = null;
	private LexBIGServiceConvenienceMethods lbscm = null;
	public RoleDataGenerator(LexBIGService lbSvc) {
		this.lbSvc = lbSvc;
		try {
			this.lbscm = (LexBIGServiceConvenienceMethods) lbSvc.getGenericExtension("LexBIGServiceConvenienceMethods");
	    } catch (Exception ex) {
			ex.printStackTrace();
		}
	}

    public boolean isBlank(String str) {
        if ((str == null) || str.matches("^\\s*$")) {
            return true;
        } else {
            return false;
        }
    }

    protected  String getDirectionalLabel(
        LexBIGServiceConvenienceMethods lbscm, String scheme,
        CodingSchemeVersionOrTag csvt, Association assoc, boolean navigatedFwd)
            throws LBException {

        String assocLabel =
            navigatedFwd ? lbscm.getAssociationForwardName(assoc
                .getAssociationName(), scheme, csvt) : lbscm
                .getAssociationReverseName(assoc.getAssociationName(), scheme,
                    csvt);
        if (isBlank(assocLabel))
            assocLabel =
                (navigatedFwd ? "" : "[Inverse]") + assoc.getAssociationName();
        return assocLabel;
    }

    public Vector getRoleData(String scheme, String version) {
		Vector w = new Vector();
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        if (version != null) {
            versionOrTag.setVersion(version);
		}
		//java.lang.String relationContainerName = null;
        NameAndValueList association = null;
        //NameAndValueList associationQualifiers = null;
        Boolean restrictToAnonymous = Boolean.FALSE;

        String[] associationsToNavigate = new String[2];
        associationsToNavigate[0] = "Role_Has_Domain";
        associationsToNavigate[1] = "Role_Has_Range";
        boolean associationsNavigatedFwd = true;
        LocalNameList _noopList = new LocalNameList();

        try {
			CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, versionOrTag, null);
			cng = cng.restrictToAnonymous(restrictToAnonymous);
			cng = cng.restrictToAssociations(Constructors
					.createNameAndValueList(associationsToNavigate), null);
			ConceptReference focus = null;
			ResolvedConceptReferenceList branch = null;
            try {
                branch =
                    cng.resolveAsList(focus,
                        associationsNavigatedFwd,
                        !associationsNavigatedFwd, -1, 1, _noopList, null,
                        null, null, -1, false);

            } catch (Exception e) {
				e.printStackTrace();
                return null;
            }
            for (Iterator<? extends ResolvedConceptReference> nodes =
                branch.iterateResolvedConceptReference(); nodes.hasNext();) {
                ResolvedConceptReference node = nodes.next();
                AssociationList childAssociationList = null;
                if (associationsNavigatedFwd) {
                    childAssociationList = node.getSourceOf();
                } else {
                    childAssociationList = node.getTargetOf();
                }

                StringBuffer buf = new StringBuffer();
                buf.append(node.getEntityDescription().getContent()).append("|");

                if (childAssociationList != null) {
                    for (Iterator<? extends Association> pathsToChildren =
                        childAssociationList.iterateAssociation(); pathsToChildren
                        .hasNext();) {
                        Association child = pathsToChildren.next();

                        String childNavText =
                            getDirectionalLabel(lbscm, scheme, versionOrTag, child,
                                associationsNavigatedFwd);
                        buf.append(childNavText).append("|");
                        AssociatedConceptList branchItemList =
                            child.getAssociatedConcepts();

                        List child_list = new ArrayList();
                        for (Iterator<? extends AssociatedConcept> branchNodes =
                            branchItemList.iterateAssociatedConcept(); branchNodes
                            .hasNext();) {
                            AssociatedConcept branchItemNode =
                                branchNodes.next();
                            child_list.add(branchItemNode);
                        }

                        new SortUtils().quickSort(child_list);

                        for (int i = 0; i < child_list.size(); i++) {
                            AssociatedConcept branchItemNode =
                                (AssociatedConcept) child_list.get(i);
                            //String branchItemCode = branchItemNode.getConceptCode();
                            String branchItemText = branchItemNode.getEntityDescription().getContent();
                            buf.append(branchItemText).append("|");
                        }
                    }
					String role_data = buf.toString();
					role_data = role_data.substring(0, role_data.length()-1);
                    w.add(role_data);
                }
            }

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return w;
	}


	public ResolvedConceptReferenceList getEntities(String codingSchemeURN, String codingSchemeVersion, String type) {
        ResolvedConceptReferenceList rcrlist = null;
		LocalNameList lnl = new LocalNameList();
		lnl.addEntry(type);
		try {
			CodedNodeSet cns = lbSvc.getNodeSet(codingSchemeURN, Constructors.createCodingSchemeVersionOrTagFromVersion(codingSchemeVersion), lnl);
			rcrlist = cns.resolveToList(null, null, null, -1);
		} catch (Exception ex) {
            ex.printStackTrace();
		}
		return rcrlist;
	}

	public String[] generateRoleData(String scheme, String version) {
		Vector v = getRoleData(scheme, version);
		HashMap map = new HashMap();
		Vector names = new Vector();
		//Vector w = new Vector();
		for (int i=0; i<v.size(); i++) {
			String line = (String) v.elementAt(i);
			int n = line.indexOf("|");
			String name = line.substring(0, n);
			names.add(name);
			String data = line.substring(n+1, line.length());
			//int j = i+1;
			map.put(name, data);
		}
		HashMap id2name = getRoleId2NameMap(scheme, version);
		HashMap name2id = getInverseHashMap(id2name);
		String[] roledata = new String[v.size()];
		Vector lines = new Vector();
		Iterator it = name2id.keySet().iterator();
		if (it == null) return null;
		while (it.hasNext()) {
			String name = (String) it.next();
			if (names.contains(name)) {
				String id = (String) name2id.get(name);
				String data = (String) map.get(name);
				String line = id + "|" + name + "|" + data;
				lines.add(line);
		    }
		}
		lines = new SortUtils().quickSort(lines);
		for (int i=0; i<lines.size(); i++) {
			String line = (String) lines.elementAt(i);
			roledata[i] = line;
		}
        return roledata;
	}

	public HashMap getRoleId2NameMap(String scheme, String version) {
        HashMap hmap = new HashMap();
		ResolvedConceptReferenceList rcrlist = getEntities(scheme, version, ASSOCIATION);
		if (rcrlist == null) return null;
		for (int i=0; i<rcrlist.getResolvedConceptReferenceCount(); i++) {
			ResolvedConceptReference rcr = (ResolvedConceptReference) rcrlist.getResolvedConceptReference(i);
			if (rcr.getEntityDescription() != null) {
				hmap.put(rcr.getCode(), rcr.getEntityDescription().getContent());
			} else {
				hmap.put(rcr.getCode(), NOT_AVAILABLE);
			}
		}
		return hmap;
	}

	public HashMap getInverseHashMap(HashMap hmap) {
		if (hmap == null) return null;
		HashMap inverseMap = new HashMap();
		Iterator it = hmap.keySet().iterator();
		if (it == null) return null;
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) hmap.get(key);
			inverseMap.put(value, key);
		}
		return inverseMap;
	}

/*
    public static void main(String[] args) {
		String scheme = "NCI_Thesaurus";
        String outputfile = scheme + "_roledata.txt";
        PrintWriter pw = null;
		try {
			pw = new PrintWriter(outputfile, "UTF-8");
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            RoleDataGenerator test = new RoleDataGenerator(lbSvc);
		    String version = null;//new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(scheme, Constants.PRODUCTION);
            version = "OWL2Asserted 16.12x";
            System.out.println(scheme + " (" + version + ")");
            String[] data = test.generateRoleData(scheme, version);
			if (data == null) return;
			for (int i=0; i<data.length; i++) {
				String line = data[i];
				pw.println(line);
			}
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
    }
*/
}

