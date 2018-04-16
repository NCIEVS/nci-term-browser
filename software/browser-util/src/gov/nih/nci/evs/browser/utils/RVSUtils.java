package gov.nih.nci.evs.browser.utils;

import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.security.SecurityToken;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
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
import org.apache.log4j.*;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;
import org.junit.Test;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSApplicationService;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
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

	public RVSUtils(String serviceUrl, LexBIGService lbSvc, LexEVSValueSetDefinitionServices vsd_service) {
		this.serviceUrl = serviceUrl;
		this.lbSvc = lbSvc;
		this.vsd_service = vsd_service;
		this.csdu = new CodingSchemeDataUtils(lbSvc);
		System.out.println("createLexEVSResolvedValueSetService ...");
		rvs_service = createLexEVSResolvedValueSetService(NCI_THESAURUS);
		System.out.println("AssertedRVSUtils ...");
		avsu = new AssertedValueSetUtils(serviceUrl, lbSvc);
		System.out.println("AssertedVSearchUtils ...");
		avssu = new AssertedVSearchUtils(lbSvc);
		System.out.println("ValueSetMetadataUtils ...");
		vsmdu = new ValueSetMetadataUtils(vsd_service);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public String getProductionVersion(String codingScheme) {
		return csdu.getVocabularyVersionByTag(codingScheme, "PRODUCITON");
	}

	public LexEVSResolvedValueSetServiceImpl createLexEVSResolvedValueSetService(String codingScheme) {
		CodingScheme cs = this.csdu.resolveCodingScheme(codingScheme);
		String version = this.csdu.getVocabularyVersionByTag(codingScheme, Constants.PRODUCTION);
		return createLexEVSResolvedValueSetService(codingScheme, version, cs.getCodingSchemeURI());
	}

    public LexEVSResolvedValueSetServiceImpl createLexEVSResolvedValueSetService(String codingScheme, String version, String codingSchemeURI) {
		AssertedValueSetParameters params =
		new AssertedValueSetParameters.Builder(version).
		assertedDefaultHierarchyVSRelation("Concept_In_Subset").
		codingSchemeName(codingScheme).
		codingSchemeURI(codingSchemeURI)
		.build();
		LexEVSResolvedValueSetServiceImpl service = (LexEVSResolvedValueSetServiceImpl) getLexEVSAppService().getLexEVSResolvedVSService(params);
		service.initParams(params);
		return service;
	}

	private LexEVSApplicationService getLexEVSAppService() {
		LexEVSApplicationService lexevsAppService = null;
		try{
			lexevsAppService = (LexEVSApplicationService)ApplicationServiceProvider.getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");
			//goodToken = new SecurityToken();
			//goodToken.setAccessToken(ConfigurationController.MEDDRA_TOKEN);
		}
		catch (Exception e)
		{
			//System.err.println("Problem initiating Test config");
			e.printStackTrace();
			//System.exit(-1);
		}
		return lexevsAppService;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// list resolved value sets
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<CodingScheme> listAllResolvedValueSets() throws Exception {
		return avsu.listAllResolvedValueSets();
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

    //searchOption
    public static final int BY_CODE = 1;
    public static final int BY_NAME = 2;
    public static final int BY_PROPERTY = 3;

    //algorithm
    public static final String EXACT_MATCH = "exactMatch";
    public static final String CONTAINS = "contains";
    public static final String LUCENE = "lucene";

    static final String[] ALGORITHMS = {EXACT_MATCH, CONTAINS, LUCENE};

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
		//dumpSearchInputParmeterValues(schemes, versions, matchText, searchOption, algorithm);
		try {
			return avssu.search(schemes, versions, matchText, searchOption, algorithm);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
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

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) {
		LexBIGService lbSvc = null;//RemoteServerUtil.createLexBIGService();
		String serviceUrl = null;//RemoteServerUtil.getServiceUrl();
		LexEVSValueSetDefinitionServices vsd_service = null;//RemoteServerUtil.getLexEVSValueSetDefinitionServices();
		long ms = System.currentTimeMillis();
		RVSUtils vsu = new RVSUtils(serviceUrl, lbSvc, vsd_service);
		System.out.println("RVSUtils initialization run time (ms): " + (System.currentTimeMillis() - ms));

/*

		AssertedRVSUtils test = new AssertedRVSUtils(serviceUrl, lbSvc);
		String codingScheme = "NCI_Thesaurus";
		CodingScheme cs = new CodingSchemeDataUtils(lbSvc).resolveCodingScheme(codingScheme);
		String version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(codingScheme, Constants.PRODUCTION);

		System.out.println("version: " + version);
*/
/*
		HashMap rvsURI2NameHashMap = test.getRVSURI2NameHashMap();

		System.out.println(rvsURI2NameHashMap.keySet().size());
		Iterator it = rvsURI2NameHashMap.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) rvsURI2NameHashMap.get(key);
			System.out.println(key + " --> " + value);
		}
*/
/*
		try {

			List<CodingScheme> schemes = test.listAllResolvedValueSetsWithMiniScheme();
			System.out.println("Number of listAllResolvedValueSetsWithMiniScheme: " + schemes.size());
			for (int i=0; i<schemes.size(); i++) {
				int j = i+1;
				cs = (CodingScheme) schemes.get(i);
				System.out.println("(" + j + ") " + cs.getCodingSchemeURI() + " (" + cs.getCodingSchemeName() + ")");
			}

			schemes = test.getRegularResolvedValueSets();
			System.out.println("Number of RegularResolvedValueSets: " + schemes.size());
			for (int i=0; i<schemes.size(); i++) {
				int j = i+1;
				cs = (CodingScheme) schemes.get(i);
				System.out.println("(" + j + ") " + cs.getCodingSchemeURI() + " (" + cs.getCodingSchemeName() + ")");
			}

			schemes = test.listAllResolvedValueSets();
			System.out.println("Number of listAllResolvedValueSets: " + schemes.size());
			for (int i=0; i<schemes.size(); i++) {
				int j = i+1;
				cs = (CodingScheme) schemes.get(i);
				System.out.println("(" + j + ") " + cs.getCodingSchemeURI() + " (" + cs.getCodingSchemeName() + ")");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
*/
		try {
			ms = System.currentTimeMillis();
			String rvs_uri = "http://evs.nci.nih.gov/valueset/FDA/C54453";
			String metadata_str = vsu.getValueSetDefinitionMetadata(rvs_uri);
			System.out.println(metadata_str);
			System.out.println("RVSUtils getValueSetDefinitionMetadata run time (ms): " + (System.currentTimeMillis() - ms));

			ms = System.currentTimeMillis();
			ValueSetDefinition vsd = vsu.findValueSetDefinitionByURI(rvs_uri);
			System.out.println(vsd.getValueSetDefinitionName());
			System.out.println("RVSUtils findValueSetDefinitionByURI run time (ms): " + (System.currentTimeMillis() - ms));

/*
			ResolvedConceptReferencesIterator iterator = vsu.getValueSetIteratorForURI(rvs_uri);
			int batchSize = 10;
			boolean showIndex = true;
			IteratorHelper.dumpIterator(iterator, batchSize, showIndex);
*/
            String code = "C48326";
            String matchText = "red";
            //Red (Code C48326)
			vsu.test_search(rvs_uri, code, matchText);
/*

			List<CodingScheme> schemes = test.getRegularResolvedValueSets();
			System.out.println("Number of RegularResolvedValueSets: " + schemes.size());
			for (int i=0; i<schemes.size(); i++) {
				int j = i+1;
				cs = (CodingScheme) schemes.get(i);
				System.out.println("(" + j + ") " + cs.getCodingSchemeURI() + " (" + cs.getCodingSchemeName() + ")");
			}

			schemes = test.listAllResolvedValueSetsWithMiniScheme();
			System.out.println("Number of RegularResolvedValueSets: " + schemes.size());
			for (int i=0; i<schemes.size(); i++) {
				int j = i+1;
				cs = (CodingScheme) schemes.get(i);
				System.out.println("(" + j + ") " + cs.getCodingSchemeURI() + " (" + cs.getCodingSchemeName() + ")");
			}

			schemes = test.listAllResolvedValueSets();
			System.out.println("Number of RegularResolvedValueSets: " + schemes.size());
			for (int i=0; i<schemes.size(); i++) {
				int j = i+1;
				cs = (CodingScheme) schemes.get(i);
				System.out.println("(" + j + ") " + cs.getCodingSchemeURI() + " (" + cs.getCodingSchemeName() + ")");
			}

/*
			ResolvedConceptReferenceList rcrl = test.getValueSetEntities(rvs_uri);
			IteratorHelper.dumpResolvedConceptReferenceList(rcrl);

			List<CodingScheme> schemes = test.getRegularResolvedValueSets();
			System.out.println("Number of RegularResolvedValueSets: " + schemes.size());
			for (int i=0; i<schemes.size(); i++) {
				int j = i+1;
				cs = (CodingScheme) schemes.get(i);
				System.out.println("(" + j + ") " + cs.getCodingSchemeURI() + " (" + cs.getCodingSchemeName() + ")");
			}

	        //List<AbsoluteCodingSchemeVersionReference> acsvrl = test.getValueSetURIAndVersionForTextContains("red");
	        //IteratorHelper.dumpAbsoluteCodingSchemeVersionReferenceList(acsvrl);
	        //Red (Code C48326)
	        List<AbsoluteCodingSchemeVersionReference> acsvrl = test.getValueSetURIAndVersionForCode("C48326");
	        IteratorHelper.dumpAbsoluteCodingSchemeVersionReferenceList(acsvrl);

			schemes = test.listAllResolvedValueSetsWithMiniScheme();
			System.out.println("Number of RegularResolvedValueSets: " + schemes.size());
			for (int i=0; i<schemes.size(); i++) {
				int j = i+1;
				cs = (CodingScheme) schemes.get(i);
				System.out.println("(" + j + ") " + cs.getCodingSchemeURI() + " (" + cs.getCodingSchemeName() + ")");
			}

			schemes = test.listAllResolvedValueSets();
			System.out.println("Number of RegularResolvedValueSets: " + schemes.size());
			for (int i=0; i<schemes.size(); i++) {
				int j = i+1;
				cs = (CodingScheme) schemes.get(i);
				System.out.println("(" + j + ") " + cs.getCodingSchemeURI() + " (" + cs.getCodingSchemeName() + ")");
			}

			schemes = test.listAllResolvedValueSetsWithNoAssertedScheme();
			System.out.println("Number of RegularResolvedValueSets: " + schemes.size());
			for (int i=0; i<schemes.size(); i++) {
				int j = i+1;
				cs = (CodingScheme) schemes.get(i);
				System.out.println("(" + j + ") " + cs.getCodingSchemeURI() + " (" + cs.getCodingSchemeName() + ")");
			}

            Properties properties = test.getCodingSchemeMetadataForResolvedValueSetURI(rvs_uri);
            System.out.println("\ndumpProperties");
            test.dumpProperties(properties);
            System.out.println("\ndumpCodingSchemeMetadata");
            test.dumpCodingSchemeMetadata(properties);
*/
			System.out.println("Total run time (ms): " + (System.currentTimeMillis() - ms));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
/*
		AssertedRVSUtils test = new AssertedRVSUtils(lbSvc);

		String codingScheme = "NCI_Thesaurus";
		CodingScheme cs = new CodingSchemeDataUtils(lbSvc).resolveCodingScheme(codingScheme);
		String version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(codingScheme, "PRODUCTION");
		if (cs == null) {
			System.out.println("Unable to resolve " + codingScheme);
		} else {
			System.out.println("cs name " + cs.getCodingSchemeName());
			System.out.println("cs uri " + cs.getCodingSchemeURI());
			System.out.println("cs formal name " + cs.getFormalName());
			System.out.println("version " + version);
		}

        String serviceUrl = "https://lexevsapi65-dev.nci.nih.gov/lexevsapi65";
		LexEVSDistributed distributed = null;
		try {
			distributed = (LexEVSDistributed)
				ApplicationServiceProvider.getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");

			LexEVSResolvedValueSetService service
			   = test.createLexEVSResolvedValueSetService(distributed, codingScheme, version, cs.getCodingSchemeURI());

			List<CodingScheme> cs_list = service.listAllResolvedValueSets();
			System.out.println("cs_list.size()= " + cs_list.size());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String scheme = "http://evs.nci.nih.gov/valueset/FDA/C54453";
		String version = test.getProductionVersion(scheme);
		version = "18.01eVS";

			LexEVSDistributed distributed =
				(LexEVSDistributed)
				ApplicationServiceProvider.getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");
*/
	}

}


