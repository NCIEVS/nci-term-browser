package gov.nih.nci.evs.browser.utils;

import gov.nih.nci.evs.browser.properties.*;
import gov.nih.nci.evs.browser.bean.*;

import java.util.*;
import org.apache.commons.codec.language.*;
import org.apache.log4j.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.DataModel.Core.types.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.naming.*;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension.Direction;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.graph.*;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Utility.Constructors;

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


 public class SearchUtilsExt {
    private static Logger _logger = Logger.getLogger(SearchUtilsExt.class);
    private static final boolean CASE_SENSITIVE = true;
    private LexBIGService lbSvc = null;
    NodeGraphResolutionExtensionImpl ngr = null;
    String graphdb_uri = null;

    public SearchUtilsExt(LexBIGService lbSvc) {
		this.lbSvc = lbSvc;
	}

    //String uri = "https://graphresolve-dev.nci.nih.gov";
    public SearchUtilsExt(LexBIGService lbSvc, String graphdb_uri) {
		this.lbSvc = lbSvc;
		this.graphdb_uri = graphdb_uri;
        try {
			ngr = (NodeGraphResolutionExtensionImpl) LexBIGServiceImpl
					.defaultInstance()
					.getGenericExtension("NodeGraphResolution");
			ngr.init(graphdb_uri);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

/*
    public List<ResolvedConceptReference> search(String scheme, String version, String matchText, String matchAlgorithm) {
		CodingSchemeDataUtils csdu = new CodingSchemeDataUtils(lbSvc);
		if (version == null) {
			version = csdu.scheme, "PRODUCTION");
		}
		System.out.println("scheme: " + scheme);
		System.out.println("version: " + version);

		//String version = "201908";
		String sourceAbbr = "ALL";
		String code = "BLOOD DYSCRASIA";
		//String code = "blood";

		int maxToReturn = -1;
		boolean searchInactive = false;

		try {
			Vector schemes = new Vector();
			Vector versions = new Vector();
			schemes.add(scheme);
			versions.add(version);
			ResolvedConceptReferencesIterator iterator = new SimpleSearchUtils(lbSvc).search(schemes, versions, matchText, SimpleSearchUtils.BY_NAME, matchAlgorithm);
			if (iterator == null) {
				System.out.println("iterator == null???");
				System.exit(0);
			}
			try {
				List<ResolvedConceptReference> list = new ArrayList<ResolvedConceptReference>();
				int numRemaining = iterator.numberRemaining();
				int maxReturn = 200;
				int knt = 0;
				System.out.println("Number of matches: " + numRemaining);
				while (iterator.hasNext()) {
					knt++;
					ResolvedConceptReference rcr = (ResolvedConceptReference) iterator.next();
					//System.out.println("(" + knt + ") " + rcr.getEntityDescription().getContent() + " (" + rcr.getCode() + ")" + " " + rcr.getCodingSchemeURI() + " "
					//    + rcr.getCodingSchemeName() + " " + rcr.getCodingSchemeVersion());
					list.add(rcr);
					if (knt == maxReturn) break;
				}
				return list;

			} catch (Exception ex) {
				ex.printStackTrace();
			}


		} catch (Exception ex) {
			System.out.println("Unable to connect to the server???");
			ex.printStackTrace();
		}

		return null;
	}
*/

    private CodedNodeSet union(Vector<CodedNodeSet> cns_vec) {
        if (cns_vec == null)
            return null;
        if (cns_vec.size() == 0)
            return null;
        CodedNodeSet cns = cns_vec.elementAt(0);

        if (cns_vec.size() == 1)
            return cns;
        for (int i = 1; i < cns_vec.size(); i++) {
            CodedNodeSet next_cns = cns_vec.elementAt(i);
            try {
                cns = cns.union(next_cns);
            } catch (Exception ex) {
                _logger.error("WARNING: cns.union throws exception.");
            }
        }
        return cns;
    }

    public CodedNodeSet getNodeSet(String scheme, CodingSchemeVersionOrTag versionOrTag)
        throws Exception {
		CodedNodeSet cns = null;
		try {
			cns = lbSvc.getCodingSchemeConcepts(scheme, versionOrTag);
			CodedNodeSet.AnonymousOption restrictToAnonymous = CodedNodeSet.AnonymousOption.NON_ANONYMOUS_ONLY;
			//6.0 mod (KLO, 101810)
			cns = cns.restrictToAnonymous(restrictToAnonymous);
	    } catch (Exception ex) {
			ex.printStackTrace();
		}
		return cns;
	}

    public LocalNameList vector2LocalNameList(Vector<String> v) {
        if (v == null)
            return null;
        LocalNameList list = new LocalNameList();
        for (int i = 0; i < v.size(); i++) {
            String vEntry = (String) v.elementAt(i);
            list.addEntry(vEntry);
        }
        return list;
    }

    public CodedNodeSet restrictToSource(CodedNodeSet cns, String source) {
        if (cns == null)
            return cns;
        if (source == null || source.compareTo("*") == 0
            || source.compareTo("") == 0 || source.compareTo("ALL") == 0)
            return cns;

        LocalNameList contextList = null;
        LocalNameList sourceLnL = null;
        NameAndValueList qualifierList = null;

        Vector<String> w2 = new Vector<String>();
        w2.add(source);
        sourceLnL = vector2LocalNameList(w2);
        LocalNameList propertyLnL = null;
        CodedNodeSet.PropertyType[] types =
            new CodedNodeSet.PropertyType[] { CodedNodeSet.PropertyType.PRESENTATION };
        try {
            cns =
                cns.restrictToProperties(propertyLnL, types, sourceLnL,
                    contextList, qualifierList);
        } catch (Exception ex) {
            _logger.error("restrictToSource throws exceptions.");
            return null;
        }
        return cns;
    }

    public CodedNodeSet matchByDesignations(Vector schemes, Vector versions, String matchText, String matchAlgorithm, String source) {
		Vector<CodedNodeSet> cns_vec = new Vector<CodedNodeSet>();
		for (int i = 0; i < schemes.size(); i++) {
			String scheme = (String) schemes.elementAt(i);
			CodingSchemeVersionOrTag versionOrTag =
				new CodingSchemeVersionOrTag();
			String version = (String) versions.elementAt(i);
			if (version != null) {
				versionOrTag.setVersion(version);
			}
            try {
				CodedNodeSet cns = getNodeSet(scheme, versionOrTag);
				if (cns != null) {
					cns =
						cns.restrictToMatchingDesignations(matchText,
							null, matchAlgorithm, null);
					if (source != null) {
						cns = restrictToSource(cns, source);
					}
				}

				if (cns != null) {
					cns_vec.add(cns);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return union(cns_vec);
	}

    public static void displayRef(int k, ResolvedConceptReference ref) {
        System.out.println("(" + k + ") " + ref.getCodingSchemeName() + " "
            + ref.getConceptCode() + ":"
            + ref.getEntityDescription().getContent());
    }

    public static void dumpData(List list) {
        if (list == null) {
            System.out.println("WARNING: dumpData list = null???");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            ResolvedConceptReference rcr =
                (ResolvedConceptReference) list.get(i);
            int j = i + 1;
            displayRef(j, rcr);
        }
    }

    public static void dumpData(List list, int max) {
        if (list == null) {
            System.out.println("WARNING: dumpData list = null???");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            ResolvedConceptReference rcr =
                (ResolvedConceptReference) list.get(i);
            int j = i + 1;
            displayRef(j, rcr);
            if (j == max) break;
        }
    }

    public Direction getDirection(boolean getInbound) {
		if (getInbound) {
			return Direction.SOURCE_OF;
		}
		return Direction.TARGET_OF;
	}

    public NameAndValueList createNameAndValueList(String[] names,
        String[] values) {
		if (names == null) return null;
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

	public List<ResolvedConceptReference> getAssociatedConcepts(String scheme, String version, String matchText, String matchAlgorithm, String source, boolean getInbound, int depth, String assocName) {
        Vector schemes = new Vector();
        schemes.add(scheme);
        Vector versions = new Vector();
        versions.add(version);
	    return getAssociatedConcepts(schemes, versions, matchText, matchAlgorithm, source, getInbound, depth, assocName);
	}

	public List<ResolvedConceptReference> getAssociatedConcepts(Vector schemes, Vector versions, String matchText, String matchAlgorithm, String source, boolean getInbound, int depth, String assocName) {
		Direction direction = getDirection(getInbound);
		NameAndValueList nameAndValueList = null;
		if (assocName != null) {
			//nameAndValueList = createNameAndValueList(new String[] { assocName }, null);
			nameAndValueList = Constructors.createNameAndValueList("association", assocName);
	    }
		CodedNodeSet cns = matchByDesignations(schemes, versions, matchText, matchAlgorithm, source);
		return getAssociatedConcepts(cns, direction, depth, nameAndValueList);
	}

    public List toList(ResolvedConceptReferenceList rcrl) {
		List list = new ArrayList();
		for (int i=0; i<rcrl.getResolvedConceptReferenceCount(); i++) {
			ResolvedConceptReference rcr = rcrl.getResolvedConceptReference(i);
			list.add(rcr);
		}
        return list;
	}

    public List toList(ResolvedConceptReferencesIterator iterator) {
		List list = new ArrayList();
		try {
			while (iterator.hasNext()) {
				ResolvedConceptReference rcr = (ResolvedConceptReference) iterator.next();
				list.add(rcr);
			}
		} catch (LBResourceUnavailableException ex) {
			ex.printStackTrace();
			return null;
		} catch (LBInvocationException ex) {
			ex.printStackTrace();
			return null;
		}
		return list;
	}

    public List toList(ResolvedConceptReferencesIterator iterator, int max) {
		List list = new ArrayList();
		int lcv = 0;
		try {
			while (iterator.hasNext()) {
				ResolvedConceptReference rcr = (ResolvedConceptReference) iterator.next();
				list.add(rcr);
				lcv++;
				if (lcv == max) break;
			}
		} catch (LBResourceUnavailableException ex) {
			ex.printStackTrace();
			return null;
		} catch (LBInvocationException ex) {
			ex.printStackTrace();
			return null;
		}
        return list;
	}

	public List<ResolvedConceptReference> getAssociatedConcepts(CodedNodeSet cns, Direction direction, int depth, NameAndValueList nameAndValueList) {
        return ngr.getAssociatedConcepts(
                cns,
                direction, //Direction.SOURCE_OF,
                depth,
                nameAndValueList);
	}

/*
    public IteratorBean createRelationshipSearchIteratorBean(Vector schemes, Vector versions,
                        String matchText, String source, String matchAlgorithm) {

    //public IteratorBean createRelationshipSearchIteratorBean(Vector schemes, Vector versions,
    //                    String matchText, String source, String matchAlgorithm, boolean designationOnly,
    //                    boolean ranking, int maxToReturn) {
        boolean getInbound = true;
        int depth = 1;
        String assocName = null;
	    List<ResolvedConceptReference> list = getAssociatedConcepts(schemes, versions, matchText, matchAlgorithm, source, getInbound, depth, assocName);
	    return new IteratorBean(list);
    }
*/

    /*
            String key =
	            iteratorBeanManager.createIteratorKey(schemes, versions, matchText,
	                searchTarget, matchAlgorithm, maxToReturn);
    */

/*
    public static void main(String[] args) {
		String[] serviceUrls = new String[] {
			        ConfigurationController.prod_serviceUrl,
					ConfigurationController.stage_serviceUrl,
					ConfigurationController.dataqa_serviceUrl,
					ConfigurationController.qa_serviceUrl,
					ConfigurationController.dev_serviceUrl};

		try {
			String serviceUrl = serviceUrls[0];
			String scheme = "NCI Metathesaurus";
			String version = null;
			String matchText = "blood";
			String matchAlgorithm = "startsWith";
			String assocName = null;
			//
			boolean getInbound = true;
			int depth = 1;
			String source = null;
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService(serviceUrl);

			SearchUtilsExt util = new SearchUtilsExt(lbSvc);
			List<ResolvedConceptReference> list = null;

		    list = util.getAssociatedConcepts(scheme, version, matchText, matchAlgorithm, source, getInbound, depth, assocName);
            util.dumpData(list, 100);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
*/
}

