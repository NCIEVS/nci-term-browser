package gov.nih.nci.evs.browser.utils;

import gov.nih.nci.evs.security.SecurityToken;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
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


public class AssertedValueSetUtils {
    private LexBIGService lbSvc = null;
    private LexEVSResolvedValueSetServiceImpl service = null;

	private CodingSchemeDataUtils csdu = null;
	String serviceUrl = null;

    public AssertedValueSetUtils(String serviceUrl, LexBIGService lbSvc) {
		this.serviceUrl = serviceUrl;
		this.lbSvc = lbSvc;
		this.csdu = new CodingSchemeDataUtils(lbSvc);
		service = new LexEVSResolvedValueSetServiceImpl(lbSvc);
		service.setLexBIGService(lbSvc);
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
		//LexBIGService lbSvc = LexEVSServiceHolder.instance().getLexEVSAppService();
		CodingScheme cs = new CodingSchemeDataUtils(lbSvc).resolveCodingScheme(codingScheme);
		String version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(codingScheme, "PRODUCTION");
		return createLexEVSResolvedValueSetService(codingScheme, version, cs.getCodingSchemeURI());
	}


    public LexEVSResolvedValueSetServiceImpl createLexEVSResolvedValueSetService(String codingScheme, String version, String codingSchemeURI) {
		//LexBIGService lbSvc = LexEVSServiceHolder.instance().getLexEVSAppService();
		AssertedValueSetParameters params =
		new AssertedValueSetParameters.Builder(version).
		assertedDefaultHierarchyVSRelation("Concept_In_Subset").
		codingSchemeName(codingScheme).
		codingSchemeURI(codingSchemeURI)
		.build();
		//service = (LexEVSResolvedValueSetServiceImpl) LexEVSServiceHolder.instance().getLexEVSAppService().getLexEVSResolvedVSService(params);
		service = (LexEVSResolvedValueSetServiceImpl) getLexEVSAppService().getLexEVSResolvedVSService(params);
		service.initParams(params);
		return service;
	}

	public LexEVSResolvedValueSetServiceImpl createLexEVSResolvedValueSetService(LexBIGService lbSvc, String codingScheme) {
		CodingScheme cs = new CodingSchemeDataUtils(lbSvc).resolveCodingScheme(codingScheme);
		String version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(codingScheme, "PRODUCTION");
		return createLexEVSResolvedValueSetService(codingScheme, version, cs.getCodingSchemeURI());
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
			System.err.println("Problem initiating Test config");
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
		//service = (LexEVSResolvedValueSetServiceImpl) LexEVSServiceHolder.instance().getLexEVSAppService().getLexEVSResolvedVSService(params);
		service = (LexEVSResolvedValueSetServiceImpl) getLexEVSAppService().getLexEVSResolvedVSService(params);
		service.initParams(params);
		return service;
	}

	public ResolvedConceptReferencesIterator getValueSetIteratorForURI(LexEVSResolvedValueSetServiceImpl service,
	    	String rvs_uri) {
		//URI uri = new URI(vs_uri);
		ResolvedConceptReferencesIterator iterator = service.getValueSetIteratorForURI(rvs_uri);
		return iterator;
	}

	public static void main(String[] args) {
		LexBIGService lbSvc = null;//RemoteServerUtil.createLexBIGService();
		String serviceUrl = null;//RemoteServerUtil.getServiceUrl();
		AssertedValueSetUtils test = new AssertedValueSetUtils(serviceUrl, lbSvc);
		String codingScheme = "NCI_Thesaurus";
		CodingScheme cs = new CodingSchemeDataUtils(lbSvc).resolveCodingScheme(codingScheme);
		String version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(codingScheme, "PRODUCTION");

		LexEVSResolvedValueSetServiceImpl service = test.createLexEVSResolvedValueSetService(lbSvc, codingScheme);
		try {
			long ms = System.currentTimeMillis();
			String rvs_uri = "http://evs.nci.nih.gov/valueset/FDA/C54453";
			ResolvedConceptReferencesIterator iterator = service.getValueSetIteratorForURI(rvs_uri);
			try {
				int numRemaining = iterator.numberRemaining();
				System.out.println("numRemaining: " + numRemaining);
				IteratorHelper.dumpIterator(iterator);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			System.out.println("Total run time (ms): " + (System.currentTimeMillis() - ms));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
