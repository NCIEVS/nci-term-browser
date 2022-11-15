package gov.nih.nci.evs.browser.utils;


import gov.nih.nci.evs.browser.common.*;
//import gov.nih.nci.evs.security.SecurityToken;
//import gov.nih.nci.system.client.ApplicationServiceProvider;
import java.io.*;
import java.text.*;
import java.util.*;
//import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lexevs.tree.dao.iterator.ChildTreeNodeIterator;
import org.lexevs.tree.json.JsonConverter;
import org.lexevs.tree.json.JsonConverterFactory;
import org.lexevs.tree.model.LexEvsTree;
import org.lexevs.tree.model.LexEvsTreeNode.ExpandableStatus;
import org.lexevs.tree.model.LexEvsTreeNode;
import org.lexevs.tree.service.TreeService;
import org.lexevs.tree.service.TreeServiceFactory;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeTagList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.RenderingDetail;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.naming.*;


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


public class ResolvedValueSetMappingUtils {
	LexBIGService lbSvc = null;
	ConceptDetails conceptDetails = null;
	MetathesaurusUtils metathesaurusUtils = null;
	EntityExporter entityExporter = null;
	CodingSchemeDataUtils codingSchemeDataUtils = null;
	Vector valueSetCodes = null;


	public ResolvedValueSetMappingUtils() {

	}


	public ResolvedValueSetMappingUtils(LexBIGService lbSvc) {
		this.lbSvc = lbSvc;
		conceptDetails = new ConceptDetails(lbSvc);
		metathesaurusUtils = new MetathesaurusUtils(lbSvc);
		entityExporter = new EntityExporter(lbSvc);
		codingSchemeDataUtils = new CodingSchemeDataUtils(lbSvc);
	}

//"C3173","Accelerated Phase Chronic Myelogenous Leukemia, BCR-ABL1 Positive","Accelerated Phase CML","A phase of chronic myelogenous leukemia characterized by one or more of the following: 1) Myeloblasts accounting for 10-19% of the peripheral blood white cells or of the nucleated cells in the bone marrow, 2) peripheral blood basophils at least 20%, 3) persistent thrombocytopenia that is unrelated to therapy, 4) persistent thrombocytosis despite adequate therapy, 5) increasing white blood cell count and increasing spleen size unresponsive to therapy, and/or evidence of clonal evolution. (WHO, 2001)","Malignant"

    public String getCode(String csvLine) {
		int n = csvLine.indexOf("\",");
		String code = csvLine.substring(1, n);
		return code;
	}

    public String getCodeAndName(String csvLine) {
		int n = csvLine.indexOf("\",");
		String code = csvLine.substring(1, n);
		csvLine = csvLine.substring(n+2, csvLine.length());
		n = csvLine.indexOf("\",");
		String name = csvLine.substring(1, n);
		return code + "|" + name;
	}



    public void run(PrintWriter pw, Vector codes) {
		if (pw == null) return;
		if (codes == null) return;
		String scheme = "NCI_Thesaurus";
		String version = codingSchemeDataUtils.getVocabularyVersionByTag(scheme, "PRODUCTION");
		String ncim_scheme = "NCI Metathesaurus";
		String ncim_version = codingSchemeDataUtils.getVocabularyVersionByTag(ncim_scheme, "PRODUCTION");
		String ltag = null;
		long ms = System.currentTimeMillis();
		//for (int i=0; i<10; i++) {
		for (int i=0; i<codes.size(); i++) {
			String line = (String) codes.elementAt(i);
			Vector u = StringUtils.parseData(line);
			String code = (String) u.elementAt(0);
			String name = (String) u.elementAt(1);
			int j = i+1;
			pw.println("(" + j + ") " + name + " (" + code + ")");
            Vector cuis = metathesaurusUtils.getMatchedMetathesaurusCUIs(scheme, version, ltag, code);
            if (cuis == null) return;
            //for (int k=0; k<cuis.size(); k++) {
		    if (cuis != null && cuis.size() > 0) {
				for (int k=0; k<cuis.size(); k++) {
					String cui = (String) cuis.elementAt(k);
					Entity ncim_entity = conceptDetails.getConceptByCode(ncim_scheme, ncim_version, cui);
					String description = ncim_entity.getEntityDescription().getContent();
					pw.println("CUI: " + cui + " Name: " + description);
					try {
						entityExporter.printProperties(pw, ncim_entity);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
		    }
		    pw.println("\n");
	    }
	    System.out.println("Total run time (ms): " + (System.currentTimeMillis() - ms));
    }

    public Vector getMatchedMetathesaurusCUIs(Entity c) {
        if (c != null) {
            Vector v = conceptDetails.getConceptPropertyValues(c, "NCI_META_CUI");
			if (v == null) return null;
			if (v == null || v.size() == 0) {
				return conceptDetails.getConceptPropertyValues(c, "UMLS_CUI");
			}
        }
        return null;
    }


/////////////////////////////////////////////////////////////////////////////////////////
    public Vector loadValueSetData(String csvFile) {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		Vector v = new Vector();
		valueSetCodes = new Vector();
		try {
			br = new BufferedReader(new FileReader(csvFile));
			line = br.readLine();
			int k = 0;
			while ((line = br.readLine()) != null) {
                String code = getCode(line);
				String codeName = getCodeAndName(line);
				valueSetCodes.add(code);
				v.add(codeName);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return v;
	}

	public static void dumpVector(String label, Vector v) {
		System.out.println(label);
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			System.out.println(t);
		}
	}


    public void run(String value_set_ascii_file, String outputfile) {
		if (outputfile == null) return;
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(outputfile, "UTF-8");
			if (pw != null) {
				Vector valueSetCodes = loadValueSetData(value_set_ascii_file);
				run(pw, valueSetCodes);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (pw != null) pw.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
