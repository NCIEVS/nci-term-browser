package gov.nih.nci.evs.browser.utils;

import java.util.HashSet;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension.MatchAlgorithm;
import org.LexGrid.LexBIG.Extensions.Generic.SourceAssertedValueSetSearchExtension;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
//import org.LexGrid.LexBIG.testUtil.LexEVSServiceHolder;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;


import java.util.*;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;


import org.apache.log4j.*;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension;

import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.codingSchemes.*;
import org.apache.log4j.*;

import org.LexGrid.LexBIG.DataModel.Core.types.*;
import org.LexGrid.naming.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;

import org.apache.commons.codec.language.*;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension;

import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;

import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;




public class AssertedVSearchUtils {
    public static final int BY_CODE = 1;
    public static final int BY_NAME = 2;
    public static final int BY_PROPERTY = 3;

    public static final String EXACT_MATCH = "exactMatch";
    public static final String CONTAINS = "contains";
    public static final String LUCENE = "lucene";

    public static final String NAMES = "names";
    public static final String CODES = "codes";
    public static final String PROPERTIES = "properties";

    private static Logger _logger = Logger.getLogger(AssertedVSearchUtils.class);
	private AssertedValueSetParameters params = null;
	private SourceAssertedValueSetSearchExtension service = null;
	private LexBIGService lbSvc = null;
	private CodingSchemeDataUtils csdu = null;

    public AssertedVSearchUtils(LexBIGService lbSvc) {
		this.lbSvc = lbSvc;
		this.csdu = new CodingSchemeDataUtils(lbSvc);
		try {
			System.out.println("Instantiating SourceAssertedValueSetSearchExtension ...");
			service = (SourceAssertedValueSetSearchExtension) lbSvc.getGenericExtension("AssertedValueSetSearchExtension");
			System.out.println("Instantiated SourceAssertedValueSetSearchExtension.");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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

    public ResolvedConceptReferencesIterator search(
        Vector<String> schemes, Vector<String> versions, String matchText, String algorithm, String target) throws LBException {
        if (algorithm == null|| target == null) return null;

        if (algorithm.compareToIgnoreCase(EXACT_MATCH) == 0 && target.compareToIgnoreCase(CODES) == 0) {
			return search(schemes, versions, matchText, BY_CODE, "exactMatch");
        } else if (algorithm.compareToIgnoreCase(LUCENE) == 0 && target.compareToIgnoreCase(CODES) == 0) {
			return search(schemes, versions, matchText, BY_CODE, "exactMatch");


        } else if (algorithm.compareToIgnoreCase(EXACT_MATCH) == 0 && target.compareToIgnoreCase(NAMES) == 0) {
			return search(schemes, versions, matchText, BY_NAME, "exactMatch");


        } else if (algorithm.compareToIgnoreCase(LUCENE) == 0 && target.compareToIgnoreCase(NAMES) == 0) {
			return search(schemes, versions, matchText, BY_NAME, "lucene");
        } else if (algorithm.compareToIgnoreCase(CONTAINS) == 0 && target.compareToIgnoreCase(NAMES) == 0) {
			return search(schemes, versions, matchText, BY_NAME, "contains");
		}
		return null;
	}


    public ResolvedConceptReferencesIterator search(
        String scheme, String version, String matchText, String target, String algorithm) throws LBException {
		if (scheme == null) return null;
		Vector<String> schemes = new Vector();
		Vector<String> versions = new Vector();
		schemes.add(scheme);
		versions.add(version);

		int searchOption = BY_NAME;
		String search_target = target;
		if (search_target != null) {
			search_target = search_target.toLowerCase();
			if (search_target.startsWith("code")) {
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
		System.out.println("Calling service.search");
		try {
			iterator = service.search(
				matchText, null, csRefs, matchAlgorithm, true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return iterator;
	}

	public String getProductionVersion(String codingScheme) {
		return csdu.getVocabularyVersionByTag(codingScheme, "PRODUCITON");
	}

//SPL Color Terminology|http://evs.nci.nih.gov/valueset/FDA/C54453|ftp://ftp1.nci.nih.gov/pub/cacore/EVS/FDA/SPL/FDA-SPL_NCIt_Subsets.xls|1:2:C54453

	public static void main(String[] args) {
		LexBIGService lbSvc = null;//RemoteServerUtil.createLexBIGService();
		AssertedVSearchUtils test = new AssertedVSearchUtils(lbSvc);

		String scheme = "http://evs.nci.nih.gov/valueset/FDA/C54453";
		String version = test.getProductionVersion(scheme);
		version = "18.01eVS";
		String matchText = "red";
		int target = AssertedVSearchUtils.BY_NAME;
		String algorithm = AssertedVSearchUtils.EXACT_MATCH;//AssertedVSearchUtils.CONTAINS;
		algorithm = AssertedVSearchUtils.CONTAINS;

		System.out.println("scheme: " + scheme);
		System.out.println("version: " + version);
		System.out.println("matchText: " + matchText);
		System.out.println("target: " + target);
		System.out.println("algorithm: " + algorithm);

        ResolvedConceptReferencesIterator iterator = null;
        try {
			iterator = test.search(scheme, version, matchText, target, algorithm);
			try {
				if (iterator == null) {
					System.out.println("search returns a null iterator.");
				} else {
					IteratorHelper.dumpIterator(iterator, 100, true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}