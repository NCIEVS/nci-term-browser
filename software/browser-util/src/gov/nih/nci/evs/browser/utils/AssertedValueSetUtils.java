package gov.nih.nci.evs.browser.utils;

import gov.nih.nci.evs.security.SecurityToken;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.HashSet;
import java.util.HashMap;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;
import org.junit.Test;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSApplicationService;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension.MatchAlgorithm;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.sourceasserted.impl.AssertedValueSetResolvedConceptReferenceIterator;

import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import javax.servlet.http.HttpServletRequest;
import org.LexGrid.LexBIG.caCore.applicationservice.impl.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;

/*
	static SourceAssertedValueSetService svc;
	static SourceAssertedValueSetSearchIndexService service;

	@BeforeClass
	public static void createIndex() throws Exception {
		service = LexEvsServiceLocator.getInstance().getIndexServiceManager().getAssertedValueSetIndexService();
		service.createIndex(Constructors.createAbsoluteCodingSchemeVersionReference(
				"http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5"));

		AssertedValueSetParameters params = new AssertedValueSetParameters.Builder("0.1.5").
				assertedDefaultHierarchyVSRelation("Concept_In_Subset").
				codingSchemeName("owl2lexevs").
				codingSchemeURI("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl")
				.build();
		svc = SourceAssertedValueSetServiceImpl.getDefaultValueSetServiceForVersion(params);
	}

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
*/
public class AssertedValueSetUtils {
    private LexBIGService lbSvc = null;
    private LexEVSResolvedValueSetServiceImpl service = null;

	private CodingSchemeDataUtils csdu = null;
	String serviceUrl = null;

    public AssertedValueSetUtils(String serviceUrl, LexBIGService lbSvc) {
		if (serviceUrl != null && serviceUrl.compareToIgnoreCase("null") == 0) {
			serviceUrl = null;
		}
		this.serviceUrl = serviceUrl;
		this.lbSvc = lbSvc;
		this.csdu = new CodingSchemeDataUtils(lbSvc);
		service = createLexEVSResolvedValueSetService("NCI_Thesaurus");
	}

	public String find_checked_value_sets(HttpServletRequest request, HashMap rvsuri2nameHashMap) {
		Iterator it = rvsuri2nameHashMap.keySet().iterator();
		int lcv = 0;
		int knt = 0;
		StringBuffer buf = new StringBuffer();
		while (it.hasNext()) {
			lcv++;
			String rvs_uri = (String) it.next();
			String[] results = request.getParameterValues(rvs_uri);
			if (results != null && results.length > 0) {
				for (int i = 0; i < results.length; i++) {
					String result = results[i];
					if (result != null && result.compareTo("") != 0) {
						knt++;
						if (knt > 1) {
							buf.append(",");
						}
						buf.append(rvs_uri);
					}
				}
			}
		}
		return buf.toString();
	}



	public AssertedValueSetParameters createAssertedValueSetParameters(String codingScheme, String version, String codingSchemeURI) {
		AssertedValueSetParameters params =
			new AssertedValueSetParameters.Builder(version).
			assertedDefaultHierarchyVSRelation("Concept_In_Subset").
			codingSchemeName(codingScheme).
			codingSchemeURI(codingSchemeURI)
			.build();
		return params;
	}

	public LexEVSResolvedValueSetServiceImpl createLexEVSResolvedValueSetService(String codingScheme) {
		CodingScheme cs = new CodingSchemeDataUtils(lbSvc).resolveCodingScheme(codingScheme);
		String version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(codingScheme, "PRODUCTION");
		return createLexEVSResolvedValueSetService(codingScheme, version, cs.getCodingSchemeURI());
	}

    public LexEVSResolvedValueSetServiceImpl createLexEVSResolvedValueSetService(String codingScheme, String version, String codingSchemeURI) {
		AssertedValueSetParameters params =
		new AssertedValueSetParameters.Builder(version).
		assertedDefaultHierarchyVSRelation("Concept_In_Subset").
		codingSchemeName(codingScheme).
		codingSchemeURI(codingSchemeURI)
		.build();

		if (serviceUrl == null) {
			service = new LexEVSResolvedValueSetServiceImpl();
		} else {
			service = (LexEVSResolvedValueSetServiceImpl) getLexEVSAppService().getLexEVSResolvedVSService(params);
		}
		service.initParams(params);
		return service;
	}

	private LexEVSApplicationService getLexEVSAppService() {
		LexEVSApplicationService lexevsAppService = null;
		//testing
		if (serviceUrl == null) {
			try {
				return (LexEVSApplicationService) LexBIGServiceImpl.defaultInstance();// new LexEVSApplicationServiceImpl(null, null);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return lexevsAppService;
		}

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

    public LexEVSResolvedValueSetServiceImpl createLexEVSResolvedValueSetService(LexBIGService lbSvc, String codingScheme, String version, String codingSchemeURI) {
		AssertedValueSetParameters params =
		new AssertedValueSetParameters.Builder(version).
		assertedDefaultHierarchyVSRelation("Concept_In_Subset").
		codingSchemeName(codingScheme).
		codingSchemeURI(codingSchemeURI)
		.build();

		// KLO, 04162018
		if (serviceUrl == null) {
		    service = new LexEVSResolvedValueSetServiceImpl();// LexEVSServiceHolder.instance().getLexEVSAppService().getLexEVSResolvedVSService(params);
	    } else {
		    service = (LexEVSResolvedValueSetServiceImpl) getLexEVSAppService().getLexEVSResolvedVSService(params);
	    }
		service.initParams(params);
		return service;
	}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<CodingScheme> listAllResolvedValueSets() throws Exception {
		long start = System.currentTimeMillis();
		List<CodingScheme> list = service.listAllResolvedValueSets();
		long end = System.currentTimeMillis();
		System.out.println("Retrieving " + list.size() + " full scheme value sets: " + (end - start) + " mseconds");
		return list;
	}

	public List<CodingScheme> listAllResolvedValueSetsWithNoAssertedScheme() throws Exception {
		long start = System.currentTimeMillis();
		LexEVSResolvedValueSetService nullVsService = getLexEVSAppService().getLexEVSResolvedVSService(null);
		List<CodingScheme> list = nullVsService.listAllResolvedValueSets();
		long end = System.currentTimeMillis();
		System.out.println("Retrieving " + list.size() + " full scheme value sets: " + (end - start) + " mseconds");
		return list;
	}

	public List<CodingScheme> listAllResolvedValueSetsWithMiniScheme() throws Exception {
		long start = System.currentTimeMillis();
		List<CodingScheme> list = service.getMinimalResolvedValueSetSchemes();
		long end = System.currentTimeMillis();
		System.out.println("Retrieving " + list.size() + " mini scheme value sets: " + (end - start) + " mseconds");
		return list;
	}

	public List<CodingScheme> listAllResolvedValueSetsWithMiniSchemeAndNoAssertedScheme() throws Exception {
		long start = System.currentTimeMillis();
		LexEVSResolvedValueSetService nullVsService = getLexEVSAppService().getLexEVSResolvedVSService(null);
		List<CodingScheme> schemes = nullVsService.getMinimalResolvedValueSetSchemes();
		long end = System.currentTimeMillis();
		System.out.println("Retrieving mini scheme value sets: " + (end - start) + " mseconds");
		return schemes;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public CodingScheme getResolvedValueSetForValueSetURI(String rvs_uri) throws Exception {
		URI uri = new URI(rvs_uri);
		CodingScheme ref = service.getResolvedValueSetForValueSetURI(uri);
		return ref;
	}

	public ResolvedConceptReferenceList getValueSetEntitiesWithNoAssertedScheme(String rvs_uri) throws Exception {
		LexEVSResolvedValueSetService nullVsService = getLexEVSAppService().getLexEVSResolvedVSService(null);
		URI uri = new URI(rvs_uri);
		ResolvedConceptReferenceList refs = nullVsService.getValueSetEntitiesForURI(uri.toString());
		return refs;
	}

/*
	@Test
	public void testResolveDuplicateValueSetsWithTestSource() throws Exception {
		URI uri = new URI("http://evs.nci.nih.gov/valueset/TEST/C48323");
		CodingScheme ref = service.getResolvedValueSetForValueSetURI(uri);
		assertNotNull(ref);
		ResolvedConceptReferenceList refs = service.getValueSetEntitiesForURI(uri.toString());
		assertNotNull(refs);
		assertTrue(refs.getResolvedConceptReferenceCount() > 0);
	}

	@Test
	public void testResolveDuplicateValueSetsWithFDASource() throws Exception {
		URI uri = new URI("http://evs.nci.nih.gov/valueset/FDA/C48323");
		CodingScheme ref = service.getResolvedValueSetForValueSetURI(uri);
		assertNotNull(ref);
		ResolvedConceptReferenceList refs = service.getValueSetEntitiesForURI(uri.toString());
		assertNotNull(refs);
		assertTrue(refs.getResolvedConceptReferenceCount() > 0);
	}
*/

	public ResolvedConceptReferencesIterator getValueSetEntitiesWithNoAssertedSchemeFromIterator(String rvs_uri) throws Exception {
		LexEVSResolvedValueSetServiceImpl nullVsService = new LexEVSResolvedValueSetServiceImpl();
		URI uri = new URI(rvs_uri);
		ResolvedConceptReferencesIterator refs = nullVsService.getValueSetIteratorForURI(uri.toString());
		return refs;
	}


	public List<CodingScheme> getResolvedValueSetsforConceptReferenceWithNoAssertedScheme(ConceptReference ref) {
		if (ref == null) return null;
		LexEVSResolvedValueSetService nullVsService = getLexEVSAppService().getLexEVSResolvedVSService(null);
		//Resolved value set coding scheme
		/*
		ConceptReference ref = new ConceptReference();
		ref.setCode("005");
		ref.setCodeNamespace("Automobiles");
		ref.setCodingSchemeName("Automobiles");
		*/
		List<CodingScheme> schemes = nullVsService.getResolvedValueSetsForConceptReference(ref);
		return schemes;
	}
/*
	@Test(expected = RuntimeException.class)
    @Category(RemoveFromDistributedTests.class)
	public void testGetValueSEtForResolvedValueSetURIWithNoAssertedScheme() throws URISyntaxException {
		LexEVSResolvedValueSetService nullVsService = LexEVSServiceHolder.instance().getLexEVSAppService().getLexEVSResolvedVSService(null);
		URI uri = new URI("SRITEST:AUTO:AllDomesticButGM");
		CodingScheme scheme = nullVsService.getResolvedValueSetForValueSetURI(uri);
		for (Property prop : scheme.getProperties().getPropertyAsReference()) {
			if (prop.getPropertyName().equals(LexEVSValueSetDefinitionServices.RESOLVED_AGAINST_CODING_SCHEME_VERSION)) {
				assertTrue(getPropertyQualifierValue(LexEVSValueSetDefinitionServices.CS_NAME, prop).equals(
						"Automobiles"));
				assertTrue(getPropertyQualifierValue(LexEVSValueSetDefinitionServices.VERSION, prop).equals("1.0"));
			}
		}

		// Expected to have a runtime exception when attempting to resolve as coding scheme
		URI asVSuri = new URI("http://evs.nci.nih.gov/valueset/FDA/C48323");
		nullVsService.getResolvedValueSetForValueSetURI(asVSuri);
	}

	@Test
	public void testCorrectTruncationForFormalNameJIRA_594() throws URISyntaxException {
		URI uri = new URI("SRITEST:AUTO:AllDomesticButGMWithlt250charName");
		CodingScheme scheme = service.getResolvedValueSetForValueSetURI(uri);
		for (Property prop : scheme.getProperties().getPropertyAsReference()) {
			if (prop.getPropertyName().equals("formalName")) {
				assertTrue(scheme.getFormalName().length() > 50);

			}
		}
	}

	@Test
	public void testVerifyLoadOfChildNodeOnly() throws URISyntaxException {
		URI uri = new URI("XTEST:One.Node.ValueSet");
		ResolvedConceptReferenceList list = service.getValueSetEntitiesForURI(uri.toString());
		assertTrue(list.getResolvedConceptReferenceCount() == 1);
		assertTrue(list.getResolvedConceptReference(0).getConceptCode().equals("C0011(5564)"));
	}
*/

	public List<AbsoluteCodingSchemeVersionReference> getValueSetURIAndVersionForCodeWithNoAssertedSource(String entityCode) throws LBException{
		LexEVSResolvedValueSetService nullVsService = getLexEVSAppService().getLexEVSResolvedVSService(null);
		List<AbsoluteCodingSchemeVersionReference> refs = nullVsService.getResolvedValueSetsforEntityCode(entityCode);
		return refs;
	}

	public List<CodingScheme> getResolvedValueSetsforConceptReference(ConceptReference ref) {
		List<CodingScheme> schemes = service.getResolvedValueSetsForConceptReference(ref);
		return schemes;
	}

	public ResolvedConceptReferencesIterator getValueSetIteratorForURI(String rvs_uri) {
		ResolvedConceptReferencesIterator iterator = service.getValueSetIteratorForURI(rvs_uri);
		return iterator;
	}

	public List<AbsoluteCodingSchemeVersionReference> getValueSetURIAndVersionForCode(String code) throws LBException{
		List<AbsoluteCodingSchemeVersionReference> asVSrefs = service.getResolvedValueSetsforEntityCode(code);
		return asVSrefs;
    }

	public Properties getCodingSchemeMetadataForResolvedValueSetURI(String rvs_uri) throws URISyntaxException {
		//URI asVSuri = new URI("http://evs.nci.nih.gov/valueset/FDA/C48323");
		URI asVSuri = new URI(rvs_uri);
		CodingScheme asVSscheme = service.getResolvedValueSetForValueSetURI(asVSuri);
		return asVSscheme.getProperties();
	}

	public void dumpProperties(Properties properties) {
		for (int i=0; i<properties.getPropertyCount(); i++) {
			Property property = properties.getProperty(i);
			System.out.println(property.getPropertyName() + ": " + property.getValue().getContent());
		}
	}

	private String getPropertyQualifierValue(String qualifierName, Property prop) {
		for (PropertyQualifier pq : prop.getPropertyQualifier()) {
			if (pq.getPropertyQualifierName().equals(qualifierName)) {
				return pq.getValue().getContent();
			}
		}
		return "";
	}

	public void dumpCodingSchemeMetadata(Properties properties) {
		for (Property prop : properties.getPropertyAsReference()) {
			if (prop.getPropertyName().equals(LexEVSValueSetDefinitionServices.RESOLVED_AGAINST_CODING_SCHEME_VERSION)) {
				System.out.println(getPropertyQualifierValue(LexEVSValueSetDefinitionServices.CS_NAME, prop));
				System.out.println(getPropertyQualifierValue(LexEVSValueSetDefinitionServices.VERSION, prop));
			}
		}
	}

	public List<CodingScheme> getRegularResolvedValueSets() {
		return lbSvc.getRegularResolvedVSCodingSchemes();
	}

	public ResolvedConceptReferenceList getValueSetEntities(String rvs_uri) throws Exception {
		//URI uri = new URI("http://evs.nci.nih.gov/valueset/TEST/C48323");
		URI uri = new URI(rvs_uri);
		ResolvedConceptReferenceList refs = service.getValueSetEntitiesForURI(uri.toString());
		return refs;
	}

	public List<AbsoluteCodingSchemeVersionReference> getValueSetURIAndVersionForTextContains(String matchText) throws LBException{
		long start = System.currentTimeMillis();
		List<AbsoluteCodingSchemeVersionReference> refs =
				service.getResolvedValueSetsforTextSearch(matchText,
						MatchAlgorithm.PRESENTATION_CONTAINS);
		long end = System.currentTimeMillis();
		System.out.println("Contains search: " + (end - start) + " mseconds");
		return refs;
	}

	public List<AbsoluteCodingSchemeVersionReference> getValueSetURIAndVersionForTextLucene() throws LBException{
		long start = System.currentTimeMillis();
		List<AbsoluteCodingSchemeVersionReference> refs =
				service.getResolvedValueSetsforTextSearch("Domestic",
						MatchAlgorithm.LUCENE);
		long end = System.currentTimeMillis();
		System.out.println("Lucene Search: " + (end - start) + " mseconds");
		return refs;
	}

	public List<AbsoluteCodingSchemeVersionReference> getValueSetURIAndVersionForTextExact(String matchText) throws LBException{
		long start = System.currentTimeMillis();
		List<AbsoluteCodingSchemeVersionReference> refs =
				service.getResolvedValueSetsforTextSearch(matchText,
						MatchAlgorithm.PRESENTATION_EXACT);
		long end = System.currentTimeMillis();
		System.out.println("Exact Match: " + (end - start) + " mseconds");
		return refs;
	}

    public HashMap getRVSURI2NameHashMap() {
        HashMap hmap = new HashMap();
		try {
			List<CodingScheme> schemes = listAllResolvedValueSets();
			if (schemes != null) {
				for (int i = 0; i < schemes.size(); i++) {
					CodingScheme cs = schemes.get(i);
					int j = i+1;
					String key = cs.getCodingSchemeURI();
					String name = cs.getCodingSchemeName();
					hmap.put(key, name);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return hmap;
	}

	public static void main(String[] args) {
		LexBIGService lbSvc = null;//RemoteServerUtil.createLexBIGService();
		String serviceUrl = null;//RemoteServerUtil.getServiceUrl();
		System.out.println(serviceUrl);
		AssertedValueSetUtils test = new AssertedValueSetUtils(serviceUrl, lbSvc);

		/*
		HashMap hmap = test.getRVSURI2NameHashMap();

		Iterator it = hmap.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) hmap.get(key);
			System.out.println(key + " --> " + value);
		}
		*/
		try {
			long ms = System.currentTimeMillis();
			String rvs_uri = "http://evs.nci.nih.gov/valueset/FDA/C54453";

			ResolvedConceptReferencesIterator iterator = test.getValueSetIteratorForURI(rvs_uri);
			try {
				int numRemaining = iterator.numberRemaining();
				System.out.println("numRemaining: " + numRemaining);
				IteratorHelper.dumpIterator(iterator);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			int batchSize = 200;
			boolean showIndex = true;
			IteratorHelper.dumpIterator(iterator, batchSize, showIndex);
			System.out.println("Total run time (ms): " + (System.currentTimeMillis() - ms));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
