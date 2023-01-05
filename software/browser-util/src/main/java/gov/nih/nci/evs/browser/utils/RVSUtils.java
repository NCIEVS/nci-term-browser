package gov.nih.nci.evs.browser.utils;

import gov.nih.nci.evs.browser.common.*;
//import gov.nih.nci.evs.security.SecurityToken;
//import gov.nih.nci.system.applicationservice.ApplicationException;
//import gov.nih.nci.system.applicationservice.ApplicationService;
//import gov.nih.nci.system.client.ApplicationServiceProvider;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.language.*;
import org.apache.logging.log4j.*;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;
import org.junit.Test;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.*;
//import org.LexGrid.LexBIG.caCore.interfaces.LexEVSApplicationService;
//import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
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
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.sourceasserted.impl.AssertedValueSetResolvedConceptReferenceIterator;
import org.LexGrid.valueSets.ValueSetDefinition;

public class RVSUtils {
    private LexBIGService lbSvc = null;
    private String serviceUrl = null;
    private LexEVSResolvedValueSetServiceImpl rvs_service = null;
    private LexEVSValueSetDefinitionServices vsd_service = null;
	private CodingSchemeDataUtils csdu = null;
    private AssertedVSearchUtils avssu = null;
    private AssertedValueSetUtils avsu = null;
    private ValueSetMetadataUtils vsmdu = null;
    private static String NCI_THESAURUS = "NCI_Thesaurus";


    //searchOption
    public static final int BY_CODE = 1;
    public static final int BY_NAME = 2;
    public static final int BY_PROPERTY = 3;

    //algorithm
    public static final String EXACT_MATCH = "exactMatch";
    public static final String CONTAINS = "contains";
    public static final String LUCENE = "lucene";

    static final String[] ALGORITHMS = {EXACT_MATCH, CONTAINS, LUCENE};

    public static SearchExtension.MatchAlgorithm[] SUPPORTED_ALGORITHMS = new SearchExtension.MatchAlgorithm[] {
		SearchExtension.MatchAlgorithm.CODE_EXACT,
		SearchExtension.MatchAlgorithm.PRESENTATION_EXACT,
		SearchExtension.MatchAlgorithm.PRESENTATION_CONTAINS,
		SearchExtension.MatchAlgorithm.PROPERTY_EXACT,
		SearchExtension.MatchAlgorithm.PROPERTY_CONTAINS,
		SearchExtension.MatchAlgorithm.LUCENE};

//	public RVSUtils(String serviceUrl, LexBIGService lbSvc, LexEVSValueSetDefinitionServices vsd_service) {
//		this.serviceUrl = serviceUrl;
//		this.lbSvc = lbSvc;
//		this.vsd_service = vsd_service;
//		this.csdu = new CodingSchemeDataUtils(lbSvc);
//		System.out.println("createLexEVSResolvedValueSetService ...");
//		rvs_service = createLexEVSResolvedValueSetService(NCI_THESAURUS);
//		System.out.println("AssertedValueSetUtils ...");
//		avsu = new AssertedValueSetUtils(serviceUrl, lbSvc);
//		System.out.println("AssertedVSearchUtils ...");
//		avssu = new AssertedVSearchUtils(lbSvc);
//		System.out.println("ValueSetMetadataUtils ...");
//		vsmdu = new ValueSetMetadataUtils(vsd_service);
//	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public String getProductionVersion(String codingScheme) {
		return csdu.getVocabularyVersionByTag(codingScheme, "PRODUCITON");
	}

//	public LexEVSResolvedValueSetServiceImpl createLexEVSResolvedValueSetService(String codingScheme) {
//		CodingScheme cs = this.csdu.resolveCodingScheme(codingScheme);
//		String version = this.csdu.getVocabularyVersionByTag(codingScheme, Constants.PRODUCTION);
//		return createLexEVSResolvedValueSetService(codingScheme, version, cs.getCodingSchemeURI());
//	}

//    public LexEVSResolvedValueSetServiceImpl createLexEVSResolvedValueSetService(String codingScheme, String version, String codingSchemeURI) {
//		AssertedValueSetParameters params =
//		new AssertedValueSetParameters.Builder(version).
//		assertedDefaultHierarchyVSRelation("Concept_In_Subset").
//		codingSchemeName(codingScheme).
//		codingSchemeURI(codingSchemeURI)
//		.build();
//		LexEVSResolvedValueSetServiceImpl service = (LexEVSResolvedValueSetServiceImpl) getLexEVSAppService().getLexEVSResolvedVSService(params);
//		service.initParams(params);
//		return service;
//	}

//	private LexEVSApplicationService getLexEVSAppService() {
//		LexEVSApplicationService lexevsAppService = null;
//		try{
//			lexevsAppService = (LexEVSApplicationService)ApplicationServiceProvider.getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");
			//goodToken = new SecurityToken();
			//goodToken.setAccessToken(ConfigurationController.MEDDRA_TOKEN);
//		}
//		catch (Exception e)
//		{
//			//System.err.println("Problem initiating Test config");
//			e.printStackTrace();
//			//System.exit(-1);
//		}
//		return lexevsAppService;


//	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// list resolved value sets
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<CodingScheme> listAllResolvedValueSets() throws Exception {
		return avsu.listAllResolvedValueSets();
	}


    public HashMap createVSDURI2NameHashMap() {
		HashMap hmap = new HashMap();
		try {
			List list = listAllResolvedValueSets();
			for (int i=0; i<list.size(); i++) {
				CodingScheme cs = (CodingScheme) list.get(i);
				hmap.put(cs.getCodingSchemeURI(), cs.getCodingSchemeName());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return hmap;
	}

	public Vector getAllResolvedValueSetURIs() {
		Vector w = new Vector();
		try {
			List list = listAllResolvedValueSets();
			for (int i=0; i<list.size(); i++) {
				CodingScheme cs = (CodingScheme) list.get(i);
				w.add(cs.getCodingSchemeURI());
			}
			w = new SortUtils().quickSort(w);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return w;
	}

    public ValueSetDefinition findValueSetDefinitionByURI(String uri) {
	    if (uri == null) return null;
		String valueSetDefinitionRevisionId = null;
		try {
			ValueSetDefinition vsd = vsd_service.getValueSetDefinition(new URI(uri), valueSetDefinitionRevisionId);
			return vsd;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public String getValueSetDefinitionMetadata(ValueSetDefinition vsd) {
		if (vsd == null) return null;
		String vsd_uri = vsd.getValueSetDefinitionURI();
		return vsmdu.getValueSetDefinitionMetadata(vsd_uri);
	}

	public String getValueSetDefinitionMetadata(String vsd_uri) {
		return vsmdu.getValueSetDefinitionMetadata(vsd_uri);
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// resolve value set
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public ResolvedConceptReferenceList getValueSetEntities(String rvs_uri) {
		try {
			return avsu.getValueSetEntities(rvs_uri);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public ResolvedConceptReferencesIterator getValueSetIteratorForURI(String rvs_uri) {
		try {
			return avsu.getValueSetIteratorForURI(rvs_uri);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// search value sets
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static int getSearchOption(String searchOptionText) {
		String t = searchOptionText.toLowerCase();
		if (t.indexOf("code") != -1) {
			return BY_CODE;
		} else if (t.indexOf("name") != -1) {
			return BY_NAME;
		} else if (t.indexOf("propert") != -1) {
			return BY_PROPERTY;
		}
		return -1;
	}

    private static String getSearchOptionText(int searchOption) {
		if (searchOption == BY_CODE) {
			return "codes";
		} else if (searchOption == BY_NAME) {
			return "names";
		} else if (searchOption == BY_PROPERTY) {
			return "properties";
		}
		return null;
	}

    public void dumpSearchInputParmeterValues(Vector<String> schemes, Vector<String> versions, String matchText, int searchOption, String algorithm) {
		for (int i=0; i<schemes.size(); i++) {
			String scheme = (String) schemes.elementAt(i);
			String version = (String) versions.elementAt(i);
			System.out.println(scheme + " (" + version + ")");
		}
    }

    public ResolvedConceptReferencesIterator search(Vector<String> schemes, Vector<String> versions, String matchText, String searchTarget, String algorithm) {
		int searchOption = getSearchOption(searchTarget);
		try {
			return search(schemes, versions, matchText, searchOption, algorithm);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

    public ResolvedConceptReferencesIterator search(
        Vector<String> schemes, Vector<String> versions, String matchText, int searchOption, String algorithm) throws LBException {
		try {
			return avssu.search(schemes, versions, matchText, searchOption, algorithm);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

    public ResolvedConceptReferencesIterator search(
        Vector<String> schemes, Vector<String> versions, String matchText, SearchExtension.MatchAlgorithm matchAlgorithm) throws LBException {
		try {
			return avssu.search(schemes, versions, matchText, matchAlgorithm);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
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
			return avssu.search(schemes, versions, matchText, matchAlgorithm);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
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
			return avssu.search(schemes, versions, matchText, matchAlgorithm, searchRVSs, false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
    }


    public ResolvedConceptReferencesIterator search(
        Vector<String> schemes, Vector<String> versions, String matchText, SearchExtension.MatchAlgorithm matchAlgorithm, boolean searchRVSs) throws LBException {
        return avssu.search(schemes, versions, matchText, matchAlgorithm, searchRVSs, false);
    }

	public void test_search(String rvs_uri, String code, String matchText) {
		int batchSize = 200;
		boolean showIndex = true;
		Vector<String> schemes = new Vector();
		Vector<String> versions = new Vector();
		schemes.add(rvs_uri);
		String version = getProductionVersion(rvs_uri);
		versions.add(version);
        String text = code;
        int maxReturn = 1000;
		int searchOption = 1;
		for (int j=0; j<ALGORITHMS.length; j++) {
			String algorithm = ALGORITHMS[j];
			System.out.println("\nSearch option: " + searchOption + " algorithm: " + algorithm);
			try {
				ResolvedConceptReferencesIterator iterator = search(
					schemes, versions, text, searchOption, algorithm);
				IteratorHelper.dumpIterator(iterator, batchSize, showIndex, maxReturn);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		text = matchText;
		for (int i=2; i<=3; i++) {
			searchOption = i;
			for (int j=0; j<ALGORITHMS.length; j++) {
				String algorithm = ALGORITHMS[j];
				System.out.println("\nSearch option: " + searchOption + " algorithm: " + algorithm);
				try {
					ResolvedConceptReferencesIterator iterator = search(
						schemes, versions, text, searchOption, algorithm);

					IteratorHelper.dumpIterator(iterator, batchSize, showIndex, maxReturn);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

    public static void dumpIterator(ResolvedConceptReferencesIterator itr, int batchSize) {
		if (itr == null) return;
		if (batchSize != -1) {
			try {
				while (itr.hasNext()) {
					ResolvedConceptReference[] refs =
						itr.next(batchSize).getResolvedConceptReference();
					for (ResolvedConceptReference ref : refs) {
						displayRef(ref);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

    public static void dumpIterator(ResolvedConceptReferencesIterator itr) {
		dumpIterator(itr, 100);
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Vector findRVSwithoutSupportingSource(Vector v) {
		String base_uri = "http://evs.nci.nih.gov/valueset";
		Vector w = new Vector();
		for (int i=0; i<v.size(); i++) {
			String line = (String) v.elementAt(i);
			Vector u = StringUtils.parseData(line, '|');
			String uri = (String) u.elementAt(0);
			String name = (String) u.elementAt(1);
			int n = uri.lastIndexOf("/");
			String t = uri.substring(0, n);
			if (t.compareTo(base_uri) == 0) {
				System.out.println(uri + " (" +  name + ")");
			}
		}
		return w;
	}

	public static String constructSearchAllString(Vector v) {
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<v.size(); i++) {
			String line = (String) v.elementAt(i);
			Vector u = StringUtils.parseData(line, '|');
			String uri = (String) u.elementAt(0);
			buf.append(uri);
			if (i < v.size()-1) {
				buf.append(",");
			}
		}
		return buf.toString();
	}

	public void generateRVSURIFile(String outputfile) {
		HashMap rvsURI2NameHashMap = avsu.getRVSURI2NameHashMap();
		Vector w = new Vector();
		System.out.println(rvsURI2NameHashMap.keySet().size());
		Iterator it = rvsURI2NameHashMap.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) rvsURI2NameHashMap.get(key);
			w.add(key + "|" + value);
		}
		Utils.saveToFile(outputfile, w);
	}

	public void testIterator(String matchText) {
		try {
			long ms = System.currentTimeMillis();
            Vector w = Utils.readFile("rvs_uri.txt");
			String selected_uris = constructSearchAllString(w);

			SearchExtension.MatchAlgorithm matchAlgorithm = MatchAlgorithm.PRESENTATION_CONTAINS;
			boolean searchRVSs = true;
			/*
    public ResolvedConceptReferencesIterator search(
        String selected_uris, String matchText, SearchExtension.MatchAlgorithm matchAlgorithm, boolean searchRVSs) throws LBException {
			*/

            ResolvedConceptReferencesIterator iterator = avssu.search(selected_uris, matchText, matchAlgorithm, searchRVSs);
            if (iterator != null) {
                IteratorHelper.dumpIterator(iterator, 100, true);
		    }
			searchRVSs = false;
            iterator = avssu.search(selected_uris, matchText, matchAlgorithm, searchRVSs);
            if (iterator != null) {
                IteratorHelper.dumpIterator(iterator, 100, true);
		    }
			System.out.println("RVSUtils search run time (ms): " + (System.currentTimeMillis() - ms));
	    } catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
