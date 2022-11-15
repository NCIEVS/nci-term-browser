package gov.nih.nci.evs.browser.utils;

import java.util.*;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.codec.language.*;
import org.apache.logging.log4j.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.types.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension.MatchAlgorithm;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension;
import org.LexGrid.LexBIG.Extensions.Generic.SourceAssertedValueSetSearchExtension;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.naming.*;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;


public class AssertedVSearchUtils {
    public static final int BY_CODE = 1;
    public static final int BY_NAME = 2;
    public static final int BY_PROPERTY = 3;

    public static final String EXACT_MATCH = "exactMatch";
    public static final String CONTAINS = "contains";
    public static final String LUCENE = "lucene";

	private static Logger _logger = LogManager.getLogger(AssertedVSearchUtils.class);
	private AssertedValueSetParameters params = null;
	private SourceAssertedValueSetSearchExtension service = null;
	private LexBIGService lbSvc = null;
	private CodingSchemeDataUtils csdu = null;

    public AssertedVSearchUtils(LexBIGService lbSvc) {
		this.lbSvc = lbSvc;
		this.csdu = new CodingSchemeDataUtils(lbSvc);
		try {
			//System.out.println("Instantiating SourceAssertedValueSetSearchExtension ...");
			service = (SourceAssertedValueSetSearchExtension) lbSvc.getGenericExtension("AssertedValueSetSearchExtension");
			//System.out.println("Instantiated SourceAssertedValueSetSearchExtension.");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static String getResolvedConceptReferenceExpression(ResolvedConceptReference ref) {
		StringBuffer buf = new StringBuffer();
		buf.append(ref.getEntityDescription().getContent() + " (" + ref.getConceptCode() + ")").append("\n");
		buf.append("\turi: " + ref.getCodingSchemeURI()).append("\n");
		buf.append("\tcoding scheme: " + ref.getCodingSchemeName()).append("\n");
		buf.append("\tversion: " + ref.getCodingSchemeVersion()).append("\n");
		buf.append("\tnamespace: " + ref.getCodeNamespace());
		return buf.toString();
	}

    protected static void displayRef(ResolvedConceptReference ref) {
        System.out.println(getResolvedConceptReferenceExpression(ref));
    }

    protected static void displayRef(int index, ResolvedConceptReference ref) {
        System.out.println("(" + index + ") " + getResolvedConceptReferenceExpression(ref));
    }

    public static void dumpIterator(ResolvedConceptReferencesIterator itr, int batchSize) {
		dumpIterator(itr, batchSize, true);
	}

    public static void dumpIterator(ResolvedConceptReferencesIterator itr, int batchSize, boolean showIndex) {
		if (batchSize != -1) {
			try {
				int knt = 0;
				while (itr.hasNext()) {
					ResolvedConceptReference[] refs =
						itr.next(batchSize).getResolvedConceptReference();
					for (ResolvedConceptReference ref : refs) {
						knt++;
						if (showIndex) {
							displayRef(knt, ref);
						} else {
							displayRef(ref);
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			try {
				int knt = 0;
				while (itr.hasNext()) {
					knt++;
					ResolvedConceptReference ref = (ResolvedConceptReference) itr.next();
					if (showIndex) {
						displayRef(knt, ref);
					} else {
						displayRef(ref);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
    }

    public static void dumpIterator(ResolvedConceptReferencesIterator itr) {
		dumpIterator(itr, -1);
	}


	public SearchExtension.MatchAlgorithm convertToMatchAlgorithm(int searchOption, String algorithm) {
		if (algorithm == null) return null;
	    if (searchOption != BY_CODE && searchOption != BY_NAME && searchOption != BY_PROPERTY) return null;
	    if (searchOption == BY_NAME) {
			if (algorithm.compareTo("exactMatch") == 0) {
				return SearchExtension.MatchAlgorithm.PRESENTATION_EXACT;
			} else if (algorithm.compareTo("contains") == 0) {
				return SearchExtension.MatchAlgorithm.PRESENTATION_CONTAINS;
			} else if (algorithm.compareTo("lucene") == 0) {
				return SearchExtension.MatchAlgorithm.LUCENE;
			} else { // Note: there is no startsWith equivalence in the search extension algorithm.
				return SearchExtension.MatchAlgorithm.LUCENE;
			}
	    } else if (searchOption == BY_PROPERTY) {
			if (algorithm.compareTo("exactMatch") == 0) {
				return SearchExtension.MatchAlgorithm.PROPERTY_EXACT;
			} else if (algorithm.compareTo("contains") == 0) {
				return SearchExtension.MatchAlgorithm.PROPERTY_CONTAINS;
			} else if (algorithm.compareTo("lucene") == 0) {
				return SearchExtension.MatchAlgorithm.LUCENE;
			} else { // Note: there is no startsWith equivalence in the search extension algorithm.
				return SearchExtension.MatchAlgorithm.LUCENE;
			}

		} else if (algorithm.compareTo("exactMatch") == 0 && searchOption == BY_CODE) {
			return SearchExtension.MatchAlgorithm.CODE_EXACT;
		}
		return null;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ResolvedConceptReferencesIterator search(
        String scheme, String version, String matchText, String target, String algorithm) throws LBException {
		if (scheme == null) return null;
		Vector<String> schemes = new Vector();
		Vector<String> versions = new Vector();
		schemes.add(scheme);
		versions.add(version);

		int searchOption = BY_NAME;
		if (target != null) {
			target = target.toLowerCase();
			if (target.startsWith("code")) {
				searchOption = BY_CODE;
			}
		}
		return search(schemes, versions, matchText, searchOption, algorithm);
    }

    public ResolvedConceptReferencesIterator search(
        String scheme, String version, String matchText, int searchOption, String algorithm) throws LBException {
		if (scheme == null) return null;
		Vector<String> schemes = new Vector();
		Vector<String> versions = new Vector();
		schemes.add(scheme);
		versions.add(version);
		return search(schemes, versions, matchText, searchOption, algorithm);
    }



     public ResolvedConceptReferencesIterator search(
        Vector<String> schemes, Vector<String> versions, String matchText, int searchOption, String algorithm) throws LBException {
	    if (schemes == null || versions == null) return null;
	    if (schemes.size() != versions.size()) return null;
	    if (schemes.size() == 0) return null;
	    if (matchText == null) return null;
	    if (searchOption != BY_CODE && algorithm == null) {
			return null;
		}
        Set<CodingSchemeReference> csRefs = new HashSet<CodingSchemeReference>();
        for (int i=0; i<schemes.size(); i++) {
			String scheme = (String) schemes.elementAt(i);
			String version = (String) versions.elementAt(i);
			CodingSchemeReference csRef = new CodingSchemeReference();
			csRef.setCodingScheme(scheme);
			csRef.setVersionOrTag(Constructors.createCodingSchemeVersionOrTagFromVersion(version));
			csRefs.add(csRef);
		}

		SearchExtension.MatchAlgorithm matchAlgorithm = convertToMatchAlgorithm(searchOption, algorithm);
		ResolvedConceptReferencesIterator iterator = null;
		if (matchAlgorithm == null) {
			return null;
		}
		try {
			iterator = service.search(
				matchText, csRefs, null, matchAlgorithm, false);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return iterator;
	}

     public ResolvedConceptReferencesIterator search(
        Vector<String> schemes, Vector<String> versions, String matchText, SearchExtension.MatchAlgorithm matchAlgorithm, boolean searchRVSs) throws LBException {
        return search(schemes, versions, matchText, matchAlgorithm, searchRVSs, false);
     }

     public ResolvedConceptReferencesIterator search(
        Vector<String> schemes, Vector<String> versions, String matchText, SearchExtension.MatchAlgorithm matchAlgorithm, boolean searchRVSs,
        boolean includeAnonymous) throws LBException {
		if (matchAlgorithm == null) {
			return null;
		}
	    if (schemes == null || versions == null) return null;
	    if (schemes.size() != versions.size()) return null;
	    if (schemes.size() == 0) return null;
	    if (matchText == null) return null;
        Set<CodingSchemeReference> csRefs = new HashSet<CodingSchemeReference>();
        for (int i=0; i<schemes.size(); i++) {
			String scheme = (String) schemes.elementAt(i);
			String version = (String) versions.elementAt(i);
			CodingSchemeReference csRef = new CodingSchemeReference();
			csRef.setCodingScheme(scheme);
			csRef.setVersionOrTag(Constructors.createCodingSchemeVersionOrTagFromVersion(version));
			csRefs.add(csRef);
		}
		ResolvedConceptReferencesIterator iterator = null;

		try {
			if (searchRVSs) {
				iterator = service.search(matchText, null, csRefs, matchAlgorithm, includeAnonymous);
			} else {
				iterator = service.search(matchText, csRefs, null, matchAlgorithm, includeAnonymous);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return iterator;
	}

    public ResolvedConceptReferencesIterator search(
        Vector<String> schemes, Vector<String> versions, String matchText, SearchExtension.MatchAlgorithm matchAlgorithm) throws LBException {
		return search(schemes, versions, matchText, matchAlgorithm, true);
	}

	public String getProductionVersion(String codingScheme) {
		return csdu.getVocabularyVersionByTag(codingScheme, "PRODUCITON");
	}


    public ResolvedConceptReferencesIterator search(
        String selected_uris, String matchText, SearchExtension.MatchAlgorithm matchAlgorithm, boolean searchRVSs) throws LBException {
		try {
			Vector u = StringUtils.parseData(selected_uris, ',');
			Vector schemes = new Vector();
			Vector versions = new Vector();
			for (int i=0; i<u.size(); i++) {
				String scheme = (String) u.elementAt(i);
				schemes.add(scheme);
				versions.add(null);
			}
			return search(schemes, versions, matchText, matchAlgorithm, searchRVSs, false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
    }

    public ResolvedConceptReferencesIterator search(
        String selected_uris, String matchText, int searchOption, SearchExtension.MatchAlgorithm matchAlgorithm) throws LBException {
		try {
			Vector u = StringUtils.parseData(selected_uris, ',');
			Vector schemes = new Vector();
			Vector versions = new Vector();
			for (int i=0; i<u.size(); i++) {
				String scheme = (String) u.elementAt(i);
				schemes.add(scheme);
				versions.add(null);
			}
			return search(schemes, versions, matchText, matchAlgorithm);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}


    public void test_search(String inputfile, String code, String name) {
		 Vector<String> schemes = Utils.readFile(inputfile);
		 Vector<String> versions = new Vector();
         for (int i=0; i<schemes.size(); i++) {
			 versions.add(null);
		 }
		 String matchText = code;
		 int searchOption = 1;
		 String algorithm = "exactMatch";
		 System.out.println(matchText);
		 System.out.println(algorithm);
		 try {
			 ResolvedConceptReferencesIterator iterator = search(schemes, versions, matchText, searchOption, algorithm);
			 try {
				 if (iterator == null) {
					 System.out.println("Iterator is null???");
					 return;
				 }
				 int numRemaining = iterator.numberRemaining();
				 System.out.println("\tNumber of matches: " + numRemaining);
			 } catch (Exception ex) {
				 ex.printStackTrace();
			 }
		 } catch (Exception ex) {
			 ex.printStackTrace();
		 }

		 matchText = name;
		 searchOption = 2;
		 String[] algorithms = new String[] {"exactMatch", "startsWith", "contains"};
		 try {
			 for (int k=0; k<algorithms.length; k++) {
				 algorithm = algorithms[k];
				 System.out.println(matchText);
				 System.out.println(algorithm);
				 ResolvedConceptReferencesIterator iterator = search(schemes, versions, matchText, searchOption, algorithm);
				 try {
					 if (iterator == null) {
						 System.out.println("Iterator is null???");
						 return;
					 }
					 int numRemaining = iterator.numberRemaining();
					 System.out.println("\tNumber of matches: " + numRemaining);
				 } catch (Exception ex) {
					 ex.printStackTrace();
				 }
			 }
		 } catch (Exception ex) {
			 ex.printStackTrace();
		 }
	 }
}
