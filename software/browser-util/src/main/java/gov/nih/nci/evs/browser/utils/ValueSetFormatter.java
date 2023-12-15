package gov.nih.nci.evs.browser.utils;

import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.XStream;
import gov.nih.nci.evs.browser.bean.*;
import gov.nih.nci.evs.browser.bean.MappingData;
import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.common.Constants;
import gov.nih.nci.evs.browser.properties.*;
import java.io.*;
import java.net.URI;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Map;
//import javax.faces.model.*;
import org.apache.commons.codec.language.*;
import org.apache.commons.lang.*;
import org.apache.logging.log4j.*;
//import org.lexevs.property.PropertyExtension;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.commonTypes.*;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.concepts.*;
//import org.LexGrid.LexBIG.caCore.interfaces.LexEVSApplicationService;
//import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.DataModel.Core.types.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Direction;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping.SearchContext;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOption;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOptionName;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.QualifierSortOption;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Extensions.Generic.SupplementExtension;
//import org.LexGrid.LexBIG.gui.sortOptions.SortOptions;
import org.LexGrid.LexBIG.History.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.naming.*;
import org.LexGrid.naming.Mappings.*;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.*;
import static gov.nih.nci.evs.browser.common.Constants.*;


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


public class ValueSetFormatter {

// Variable declaration
	public static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	LexBIGService lbSvc = null;
	String serviceUrl = null;
	LexEVSValueSetDefinitionServices vsd_service = null;
	CodingSchemeDataUtils csdu = null;
	ConceptDetails cd = null;
	public static int MAX_RETURN = 250;

	private String uri;
	private String name;
	private String defaultCodingScheme;
	private String version;
	private String supportedSource;

	private static int maxToReturn = 250;
	private static String TYPE_PRESENTATION = "PRESENTATION";
	private static String TYPE_DEFINITION = "DEFINITION";
	private static String TYPE_COMMENT = "COMMENT";
	private static String TYPE_PROPERTY = "PROPERTY";

	private static String NCIT_CONCEPT_CODE = "NCIt Concept Code";
	private static String SOURCE_PREFERRED_TERM = "Source Name";
	private static String SOURCE_DISPLAY_NAME = "Source Display Name";
	private static String SOURCE_PREFERRED_TERM_SOURCE_CODE = "Source Preferred Term Source Code";
	private static String SOURCE_SYNONYMS = "Source Synonyms";
	private static String SOURCE_SYNONYM_SOURCE_CODE= "Source Synonym Source Code";
	private static String SOURCE_DEFINITION = "Source Definition";
	private static String SOURCE_SUBSET_CODE = "Source Subset Code";
	private static String SOURCE_SUBSET_NAME = "Source Subset Name";

	private static String NCIT_PREFERRED_NAME = "NCIt Preferred Name";
	private static String NCIT_PREFERRED_TERM = "NCIt Preferred Term";
	private static String NCIT_DISPLAY_NAME = "NCIt Display Name";
	private static String NCIT_SYNONYMS = "NCIt Synonyms";
	private static String NCIT_DEFINITION = "NCIt Definition";

	private static String NCIT_SUBSET_CODE = "NCIt Subset Code";
	private static String NCIT_SUBSET_NAME = "NCIt Subset Name";

	private static String MALIGNANCY_STATUS = "Malignancy Status";
	private static String NCI_METATHESAURUS_CUI = "NCI Metathesaurus CUI";

	private static String UMLS_CUI = "UMLS CUI";
	private static String CDISC = "CDISC";
	private static String CTRP = "CTRP";
	private static String NCI_SOURCE = "NCI";
	private static String TYPE_AB = "AB";

	private static String[] TYPES = {NCIT_CONCEPT_CODE,
	                                 SOURCE_PREFERRED_TERM,
	                                 SOURCE_DISPLAY_NAME,
	                                 SOURCE_PREFERRED_TERM_SOURCE_CODE,
	                                 SOURCE_SYNONYMS,
	                                 SOURCE_SYNONYM_SOURCE_CODE,
	                                 SOURCE_DEFINITION,
	                                 SOURCE_SUBSET_CODE,
	                                 SOURCE_SUBSET_NAME,
	                                 NCIT_PREFERRED_NAME,
	                                 NCIT_PREFERRED_TERM,
	                                 NCIT_DISPLAY_NAME,
	                                 NCIT_SYNONYMS,
	                                 NCIT_DEFINITION,
	                                 NCIT_SUBSET_CODE,
	                                 NCIT_SUBSET_NAME,
	                                 MALIGNANCY_STATUS,
	                                 NCI_METATHESAURUS_CUI,
	                                 UMLS_CUI
	                                 };
    static HashMap vsHeading2VarHashMap = null;

	private String NCITCODE = "ncitCode";
	private String SOURCEPREFERREDTERM = "sourcePreferredTerm";
	private String NCITPREFERREDTERM = "ncitPreferredTerm";
	private String NCITPREFERREDNAME = "ncitPreferredName";

	private String NCITSUBSETCODE = "ncitSubsetCode";
	private String NCITSUBSETNAME = "ncitSubsetName";

	private String SOURCEDISPLAYNAME = "sourceDisplayName";
	private String NCITDISPLAYNAME = "ncitDisplayName";

	private String NCITSYNONYMS = "ncitSynonyms";
	private String SOURCESYNONYMS = "sourceSynonyms";
	private String NCITDEFINITIONS = "ncitDefinitions";
	private String SOURCEDEFINITIONS = "sourceDefinitions";

	private	ValueSetMetadataUtils vsmdu = null;//new ValueSetMetadataUtils(vsd_service);
	UIUtils uiUtils = null;

    static {
		vsHeading2VarHashMap = new HashMap();
		vsHeading2VarHashMap.put("NCIt Concept Code", "ncitCode");
		vsHeading2VarHashMap.put("Source Name", "sourcePreferredTerm");
		vsHeading2VarHashMap.put("Source Display Name", "sourceDisplayName");
		vsHeading2VarHashMap.put("NCIt Preferred Term", "ncitPreferredTerm");
		vsHeading2VarHashMap.put("NCIt Preferred Name", "ncitPreferredName");
		vsHeading2VarHashMap.put("NCIt Subset Code", "ncitSubsetCode");
		vsHeading2VarHashMap.put("NCIt Subset Name", "ncitSubsetName");
		vsHeading2VarHashMap.put("NCIt Display Name", "ncitDisplayName");
		vsHeading2VarHashMap.put("NCIt Synonyms", "ncitSynonyms");
		vsHeading2VarHashMap.put("Source Synonyms", "sourceSynonyms");
		vsHeading2VarHashMap.put("NCIt Definition", "ncitDefinitions");
		vsHeading2VarHashMap.put("Source Definition", "sourceDefinitions");
	}

// Constructor

    public ValueSetFormatter(String serviceUrl, LexBIGService lbSvc, LexEVSValueSetDefinitionServices vsd_service) {
		this.serviceUrl = serviceUrl;
		this.lbSvc = lbSvc;
		this.vsd_service = vsd_service;
        this.csdu = new CodingSchemeDataUtils(lbSvc);
	    this.vsmdu = new ValueSetMetadataUtils(vsd_service);
        this.cd = new ConceptDetails(lbSvc);
        this.uiUtils = new UIUtils(lbSvc);
	}

	public ValueSetFormatter(
		String uri,
		String name,
		String defaultCodingScheme,
		String version,
		String supportedSource) {

		this.uri = uri;
		this.name = name;
		this.defaultCodingScheme = defaultCodingScheme;
		this.version = version;
		this.supportedSource = supportedSource;
	}

// Set methods
	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDefaultCodingScheme(String defaultCodingScheme) {
		this.defaultCodingScheme = defaultCodingScheme;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setSupportedSource(String supportedSource) {
		this.supportedSource = supportedSource;
	}


// Get methods
	public String getUri() {
		return this.uri;
	}

	public String getName() {
		return this.name;
	}

	public String getDefaultCodingScheme() {
		return this.defaultCodingScheme;
	}

	public String getVersion() {
		return this.version;
	}

	public String getSupportedSource() {
		return this.supportedSource;
	}


    public static Vector getDefaultFields(boolean withSource) {
		Vector fields = new Vector();
		fields.add(NCIT_CONCEPT_CODE);
		if (withSource) {
			fields.add(SOURCE_PREFERRED_TERM);
		}
		fields.add(NCIT_PREFERRED_TERM);
		fields.add(NCIT_SYNONYMS);
		if (withSource) {
			fields.add(SOURCE_DEFINITION);
	    }
		fields.add(NCIT_DEFINITION);
		return fields;
	}

    public static Vector getCTRPFields() {
		Vector fields = new Vector();
		fields.add(NCIT_SUBSET_CODE);
		fields.add(NCIT_SUBSET_NAME);
		fields.add(NCIT_CONCEPT_CODE);
		fields.add(NCIT_PREFERRED_NAME);
		fields.add(NCIT_DISPLAY_NAME);
		fields.add(SOURCE_DISPLAY_NAME);
		//fields.add(NCIT_SYNONYMS);
		//fields.add(NCIT_DEFINITION);
		return fields;
	}

    public static Vector getDefaultFields() {
		return getDefaultFields(true);
	}

    public Vector resolveIterator(ResolvedConceptReferencesIterator iterator, int maxToReturn) {
        Vector v = new Vector();
        if (iterator == null) {
            return v;
        }
        try {
            int iteration = 0;

			while (iterator != null && iterator.hasNext()) {
				ResolvedConceptReference[] refs = iterator.next(500).getResolvedConceptReference();
				for (ResolvedConceptReference ref : refs) {
				    String t = ref.getEntityDescription().getContent() + "|"+ ref.getCode() + "|"+ ref.getCodingSchemeName()
					+ "|" + ref.getCodeNamespace();
                    v.add(t);
				}
			}
			/*
            while (iterator.hasNext()) {
                iteration++;
                iterator = iterator.scroll(maxToReturn);
                ResolvedConceptReferenceList rcrl = iterator.getNext();
                ResolvedConceptReference[] rcra =
                    rcrl.getResolvedConceptReference();
                for (int i = 0; i < rcra.length; i++) {
                    ResolvedConceptReference rcr = rcra[i];
				    if (rcr == null) return null;
				    String t = rcr.getEntityDescription().getContent() + "|"+ rcr.getCode() + "|"+ rcr.getCodingSchemeName()
					+ "|" + rcr.getCodeNamespace();
                    v.add(t);
                }
            }
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    private LocalNameList createLocalNameList(Vector propertyNames) {
		if (propertyNames == null) return null;
		LocalNameList propertyList = new LocalNameList();
		for (int i=0; i<propertyNames.size(); i++) {
			String propertyName = (String) propertyNames.elementAt(i);
			propertyList.addEntry(propertyName);
		}
		return propertyList;
	}

    public CodedNodeSet restrictToProperties(CodedNodeSet cns, Vector propertyNames) {
		LocalNameList propertyList = createLocalNameList(propertyNames);
		try {
			cns = cns.restrictToProperties(propertyList, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cns;
	}

    public LocalNameList getPropertyNameLocalNameList(Vector types) {
		 Vector property_names = new Vector();
		 if (types == null || types.size() == 0) return null;
		 for (int i=0; i<types.size(); i++) {
			 String type = (String) types.elementAt(i);
			 if (type == null) return null;
			 if (type.endsWith("Name") || type.endsWith("Term") || type.endsWith("Code")) {
				 if (!property_names.contains("FULL_SYN")) {
					 property_names.add("FULL_SYN");
				 }
				 if (!property_names.contains("Display_Name")) {
					 property_names.add("Display_Name");
				 }
				 if (!property_names.contains("Preferred_Name")) {
					 property_names.add("Preferred_Name");
				 }
			 } else if (type.endsWith("Definition")) {
				 if (!property_names.contains("DEFINITION")) {
					 property_names.add("DEFINITION");
				 }
				 if (!property_names.contains("ALT_DEFINITION")) {
					 property_names.add("ALT_DEFINITION");
				 }
			 } else {
				 if (type.compareTo(MALIGNANCY_STATUS) == 0) {
					 if (!property_names.contains("Malignancy_Status")) {
						 property_names.add("Malignancy_Status");
					 }
				 }
				 if (type.compareTo(MALIGNANCY_STATUS) == 0) {
					 if (!property_names.contains("Malignancy_Status")) {
						 property_names.add("Malignancy_Status");
					 }
				 } else if (type.compareTo(NCI_METATHESAURUS_CUI) == 0) {
					 if (!property_names.contains("NCI_META_CUI")) {
						 property_names.add("NCI_META_CUI");
					 }
				 } else if (type.compareTo(UMLS_CUI) == 0) {
					 if (!property_names.contains("UMLS_CUI")) {
						 property_names.add("UMLS_CUI");
					 }
				 }
			 }
		 }
		 return createLocalNameList(property_names);
	 }

     public Vector resolve(String scheme, String version, String source, Vector fields, Vector codes, int maxToReturn) {
        String defaultCodingScheme = scheme;
		if (codes == null) return null;
		CodingSchemeDataUtils csdu = new CodingSchemeDataUtils(lbSvc);
		if (csdu == null) return null;
		String metadata = vsmdu.getValueSetDefinitionMetadata(scheme);

		if (metadata != null) {
			Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(metadata);
			defaultCodingScheme = (String) u.elementAt(6);
			if (defaultCodingScheme.compareTo("ncit") == 0) {
				defaultCodingScheme = "NCI_Thesaurus";
			}
		}

		Vector w = new Vector();
		try {
			CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
			if (version == null) {
				version = csdu.getVocabularyVersionByTag(defaultCodingScheme, Constants.PRODUCTION);
			}
			if (version != null) {
				csvt.setVersion(version);
			}
			String[] a = new String[codes.size()];
			for (int i=0; i<codes.size(); i++) {
				String t = (String) codes.elementAt(i);
				a[i] = t;
			}
			ConceptReferenceList crefs = ConvenienceMethods.createConceptReferenceList(a, defaultCodingScheme);
			CodedNodeSet cns = lbSvc.getCodingSchemeConcepts(defaultCodingScheme, csvt);

			if (cns == null) {
				return null;
			}

			//org.LexGrid.commonTypes.Property[] properties = null;
			cns = cns.restrictToStatus(ActiveOption.ALL, null);
			if (cns == null) {
				return null;
			}

			cns = cns.restrictToCodes(crefs);
			if (cns == null) {
				return null;
			}

            SortOptionList sortCriteria = null;
            CodedNodeSet.PropertyType[] propertyTypes = null;
            LocalNameList propertyNames = getPropertyNameLocalNameList(fields);

            ResolvedConceptReferencesIterator iterator = null;
            try {
                iterator = cns.resolve(sortCriteria, propertyNames, propertyTypes);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (iterator == null) return null;

			while (iterator != null && iterator.hasNext()) {
				ResolvedConceptReference[] refs = iterator.next(500).getResolvedConceptReference();
				for (ResolvedConceptReference ref : refs) {
					Entity node = ref.getEntity();
					String line = csdu.getPropertyValues(node);
					//C111116|ncit|name$FULL_SYN$Diffusion Weighted MRI$form=SY$source=NCI|name$FULL_SYN$DWI$form=SY$source=CDISC|name$FULL_SYN$Diffusion Weighted Imaging$form=SY$source=CDISC|name$FULL_SYN$Diffusion-Weighted Magnetic Resonance Imaging$form=SY$source=NCI|name$FULL_SYN$Diffusion Weighted Imaging$form=SY$subsource-name=caDSR$source=NCI|name$FULL_SYN$Diffusion Weighted Imaging$form=DN$source=CTRP|name$FULL_SYN$DWI MRI$form=SY$subsource-name=caDSR$source=NCI|name$FULL_SYN$DWI-MRI$form=SY$subsource-name=caDSR$source=NCI|name$FULL_SYN$DWI$form=SY$subsource-name=caDSR$source=NCI|name$FULL_SYN$Diffusion-Weighted MRI$form=SY$source=NCI|name$FULL_SYN$DIFFUSION WEIGHTED MRI$form=PT$source=CDISC|name$FULL_SYN$Diffusion Weighted Imaging$form=PT$source=NCI|name$FULL_SYN$DW-MRI$form=SY$source=CDISC|name$FULL_SYN$Diffusion-Weighted MR Imaging$form=SY$source=NCI|name$FULL_SYN$MR Diffusion-Weighted Imaging$form=SY$source=NCI
					w.add(line);
				}
			}
            /*
            while (iterator.hasNext()) {
                iterator = iterator.scroll(maxToReturn);
                ResolvedConceptReferenceList rcrl = iterator.getNext();
                if (rcrl != null) {
					ResolvedConceptReference[] rcra =
						rcrl.getResolvedConceptReference();
					if (rcra != null) {
						for (int lcv = 0; lcv< rcra.length; lcv++) {
							ResolvedConceptReference ref = rcra[lcv];
							Entity node = ref.getEntity();
							String line = csdu.getPropertyValues(node);
							//C111116|ncit|name$FULL_SYN$Diffusion Weighted MRI$form=SY$source=NCI|name$FULL_SYN$DWI$form=SY$source=CDISC|name$FULL_SYN$Diffusion Weighted Imaging$form=SY$source=CDISC|name$FULL_SYN$Diffusion-Weighted Magnetic Resonance Imaging$form=SY$source=NCI|name$FULL_SYN$Diffusion Weighted Imaging$form=SY$subsource-name=caDSR$source=NCI|name$FULL_SYN$Diffusion Weighted Imaging$form=DN$source=CTRP|name$FULL_SYN$DWI MRI$form=SY$subsource-name=caDSR$source=NCI|name$FULL_SYN$DWI-MRI$form=SY$subsource-name=caDSR$source=NCI|name$FULL_SYN$DWI$form=SY$subsource-name=caDSR$source=NCI|name$FULL_SYN$Diffusion-Weighted MRI$form=SY$source=NCI|name$FULL_SYN$DIFFUSION WEIGHTED MRI$form=PT$source=CDISC|name$FULL_SYN$Diffusion Weighted Imaging$form=PT$source=NCI|name$FULL_SYN$DW-MRI$form=SY$source=CDISC|name$FULL_SYN$Diffusion-Weighted MR Imaging$form=SY$source=NCI|name$FULL_SYN$MR Diffusion-Weighted Imaging$form=SY$source=NCI
							w.add(line);
						}
					}
				}
			}
			*/
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        return w;
    }


//C111116|ncit|name$FULL_SYN$Diffusion Weighted MRI$form=SY$source=NCI|name$FULL_SYN$DWI$form=SY$source=CDISC|name$FULL_SYN$Diffusion Weighted Imaging$form=SY$source=CDISC|name$FULL_SYN$Diffusion-Weighted Magnetic Resonance Imaging$form=SY$source=NCI|name$FULL_SYN$Diffusion Weighted Imaging$form=SY$subsource-name=caDSR$source=NCI|name$FULL_SYN$Diffusion Weighted Imaging$form=DN$source=CTRP|name$FULL_SYN$DWI MRI$form=SY$subsource-name=caDSR$source=NCI|name$FULL_SYN$DWI-MRI$form=SY$subsource-name=caDSR$source=NCI|name$FULL_SYN$DWI$form=SY$subsource-name=caDSR$source=NCI|name$FULL_SYN$Diffusion-Weighted MRI$form=SY$source=NCI|name$FULL_SYN$DIFFUSION WEIGHTED MRI$form=PT$source=CDISC|name$FULL_SYN$Diffusion Weighted Imaging$form=PT$source=NCI|name$FULL_SYN$DW-MRI$form=SY$source=CDISC|name$FULL_SYN$Diffusion-Weighted MR Imaging$form=SY$source=NCI|name$FULL_SYN$MR Diffusion-Weighted Imaging$form=SY$source=NCI

    public HashMap lineSegment2HashMap(String t) {
		HashMap hmap = new HashMap();
		Vector w = gov.nih.nci.evs.browser.utils.StringUtils.parseData(t, "$");
		String prop_type = (String) w.elementAt(0);
		hmap.put("prop_type", prop_type);
		String prop_name = (String) w.elementAt(1);
		hmap.put("prop_name", prop_name);
		String prop_value = (String) w.elementAt(2);
		hmap.put("prop_value", prop_value);
		if (w.size() > 3) {
			for (int i=3; i<w.size(); i++) {
				String s = (String) w.elementAt(i);
				Vector nv = gov.nih.nci.evs.browser.utils.StringUtils.parseData(s, "=");
				String nm = (String) nv.elementAt(0);
				String val = (String) nv.elementAt(1);
				hmap.put(nm, val);
			}
		}
		return hmap;
	}

    public String parseProperty(String line, String type, String source) {
		return parseProperty(line, type, source, null);
	}


    public String parseProperty(String line, String type, String source, String fullSynTermName) {
		if (line == null) return null;

//C111116|ncit|name$FULL_SYN$Diffusion Weighted MRI$form=SY$source=NCI|name$FULL_SYN$DWI$form=SY$source=CDISC|name$FULL_SYN$Diffusion Weighted Imaging$form=SY$source=CDISC|name$FULL_SYN$Diffusion-Weighted Magnetic Resonance Imaging$form=SY$source=NCI|name$FULL_SYN$Diffusion Weighted Imaging$form=SY$subsource-name=caDSR$source=NCI|name$FULL_SYN$Diffusion Weighted Imaging$form=DN$source=CTRP|name$FULL_SYN$DWI MRI$form=SY$subsource-name=caDSR$source=NCI|name$FULL_SYN$DWI-MRI$form=SY$subsource-name=caDSR$source=NCI|name$FULL_SYN$DWI$form=SY$subsource-name=caDSR$source=NCI|name$FULL_SYN$Diffusion-Weighted MRI$form=SY$source=NCI|name$FULL_SYN$DIFFUSION WEIGHTED MRI$form=PT$source=CDISC|name$FULL_SYN$Diffusion Weighted Imaging$form=PT$source=NCI|name$FULL_SYN$DW-MRI$form=SY$source=CDISC|name$FULL_SYN$Diffusion-Weighted MR Imaging$form=SY$source=NCI|name$FULL_SYN$MR Diffusion-Weighted Imaging$form=SY$source=NCI


		Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(line);
		if (type.compareTo(NCIT_CONCEPT_CODE) == 0) {
			String code = (String) u.elementAt(0);
			return code;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		} else if (type.compareTo(SOURCE_DISPLAY_NAME) == 0) {
			String term_name_found = null;
            for (int i=0; i<u.size(); i++) {
				String t = (String) u.elementAt(i);
				if (t == null) return null;
				if (t.startsWith("name")) {
					HashMap hmap = lineSegment2HashMap(t);
					String form = (String) hmap.get("form");
					String src = (String) hmap.get("source");
					if (form != null && form.compareTo("DN") == 0 && src != null && source != null && src.compareTo(source) == 0) {
						String term_name = (String) hmap.get("prop_value");
						return term_name;
					}
				}
			}

		} else if (type.compareTo(SOURCE_PREFERRED_TERM) == 0) {
			String term_name_found = null;
            for (int i=0; i<u.size(); i++) {
				String t = (String) u.elementAt(i);
				if (t == null) return null;
				if (t.startsWith("name")) {
					HashMap hmap = lineSegment2HashMap(t);
					String form = (String) hmap.get("form");
					String src = (String) hmap.get("source");
					String source_code = (String) hmap.get("source-code");
					if (fullSynTermName == null) {
						if (form != null && form.compareTo("PT") == 0 && src != null && source != null && src.compareTo(source) == 0) {
							String term_name = (String) hmap.get("prop_value");
							return term_name;
						}
					} else {
						//Prostate Specific Antigen|PT|CDISC|SDTM-LBTEST|null
						if (form != null && form.compareTo("PT") == 0 && src != null && source != null && src.compareTo(source) == 0
						    && source_code != null && source_code.compareTo(fullSynTermName) == 0) {
							String term_name = (String) hmap.get("prop_value");
							return term_name;
						}
					}
				}
			}
			if (term_name_found == null) {
				for (int i=0; i<u.size(); i++) {
					String t = (String) u.elementAt(i);
					if (t == null) return null;
					if (t.startsWith("name")) {
						HashMap hmap = lineSegment2HashMap(t);
						String form = (String) hmap.get("form");
						String src = (String) hmap.get("source");
						if (form != null && form.compareTo("PT") == 0 && src != null && source != null && src.compareTo(source) == 0) {
							String term_name = (String) hmap.get("prop_value");
							return term_name;
						}
					}
				}
			}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		} else if (type.compareTo(SOURCE_PREFERRED_TERM_SOURCE_CODE) == 0) {
			StringBuffer buf = new StringBuffer();
            for (int i=0; i<u.size(); i++) {
				String t = (String) u.elementAt(i);
				if (t == null) return null;
				if (t.startsWith("name")) {
					HashMap hmap = lineSegment2HashMap(t);
					String form = (String) hmap.get("form");
					String src = (String) hmap.get("source");
					if (form != null && form.compareTo("PT") == 0 && src != null && source != null && src.compareTo(source) == 0) {
						String source_code = (String) hmap.get("source_code");
						buf.append(source_code).append("$");
					}
				}
			}
			String s = buf.toString();
			if (s == null) return null;
			if (s.length() > 0) {
				s = s.substring(0, s.length()-1);
			}
			return s;

		} else if (type.compareTo(SOURCE_SYNONYMS) == 0) {
			StringBuffer buf = new StringBuffer();
            for (int i=0; i<u.size(); i++) {
				String t = (String) u.elementAt(i);
				if (t == null) return null;
				if (t.startsWith("name")) {
					HashMap hmap = lineSegment2HashMap(t);
					String form = (String) hmap.get("form");
					String src = (String) hmap.get("source");
					if (form != null && form.compareTo("PT") != 0 && src != null && source != null && src.compareTo(source) == 0) {
						String term_name = (String) hmap.get("prop_value");
						buf.append(term_name).append("$");
					}
				}
			}
			String s = buf.toString();
			if (s == null) return null;
			if (s.length() > 0) {
				s = s.substring(0, s.length()-1);
			}
			return s;
		} else if (type.compareTo(SOURCE_SYNONYM_SOURCE_CODE) == 0) {
			StringBuffer buf = new StringBuffer();
            for (int i=0; i<u.size(); i++) {
				String t = (String) u.elementAt(i);
				if (t == null) return null;
				if (t.startsWith("name")) {
					HashMap hmap = lineSegment2HashMap(t);
					String form = (String) hmap.get("form");
					String src = (String) hmap.get("source");
					if (form != null && form.compareTo("PT") != 0 && src != null && source != null && src.compareTo(source) == 0) {
						String source_code = (String) hmap.get("source-code");
						buf.append(source_code).append("$");
					}
				}
			}
			String s = buf.toString();
			if (s == null) return null;
			if (s.length() > 0) {
				s = s.substring(0, s.length()-1);
			}
			return s;
		} else if (type.compareTo(SOURCE_DEFINITION) == 0) {
			StringBuffer buf = new StringBuffer();
            for (int i=0; i<u.size(); i++) {
				String t = (String) u.elementAt(i);
				if (t == null) return null;
				if (t.startsWith("definition")) {
					HashMap hmap = lineSegment2HashMap(t);
					String src = (String) hmap.get("source");
					if (src != null && source != null && src.compareTo(source) == 0) {
						String def = (String) hmap.get("prop_value");
						buf.append(def).append("$");
					}
				}
			}
			String s = buf.toString();
			if (s == null) return null;
			if (s.length() > 0) {
				s = s.substring(0, s.length()-1);
			}
			return s;

		} else if (type.compareTo(NCIT_PREFERRED_TERM) == 0) {
            for (int i=0; i<u.size(); i++) {
				String t = (String) u.elementAt(i);
				if (t == null) return null;
				if (t.startsWith("name")) {
					HashMap hmap = lineSegment2HashMap(t);
					String form = (String) hmap.get("form");
					String src = (String) hmap.get("source");
					if (form != null && form.compareTo("PT") == 0 && src != null && src.compareTo("NCI") == 0) {
						String term_name = (String) hmap.get("prop_value");
						return term_name;
					}
				}
			}
		} else if (type.compareTo(NCIT_PREFERRED_NAME) == 0) {
            for (int i=0; i<u.size(); i++) {
				String t = (String) u.elementAt(i);
				if (t == null) return null;
				if (t.startsWith("name")) {
					HashMap hmap = lineSegment2HashMap(t);
					String prop_name = (String) hmap.get("prop_name");
					if (prop_name.compareTo("Preferred_Name") == 0) {
						String term_name = (String) hmap.get("prop_value");
						return term_name;
					}
				}
			}

		} else if (type.compareTo(NCIT_DISPLAY_NAME) == 0) {
            for (int i=0; i<u.size(); i++) {
				String t = (String) u.elementAt(i);
				if (t == null) return null;
				if (t.startsWith("name")) {
					HashMap hmap = lineSegment2HashMap(t);
					String prop_name = (String) hmap.get("prop_name");
					if (prop_name.compareTo("Display_Name") == 0) {
						String term_name = (String) hmap.get("prop_value");
						return term_name;
					}
				}
			}

		} else if (type.compareTo(NCIT_SYNONYMS) == 0) {
			Vector syn_vec = new Vector();
            for (int i=0; i<u.size(); i++) {
				String t = (String) u.elementAt(i);
				if (t == null) return null;
				if (t.startsWith("name")) {
					HashMap hmap = lineSegment2HashMap(t);
					String form = (String) hmap.get("form");
					String src = (String) hmap.get("source");
					if (form != null && form.compareTo("PT") != 0 && src != null && src.compareTo("NCI") == 0) {
						String term_name = (String) hmap.get("prop_value");
						syn_vec.add(term_name);
					}
				}
			}
            Vector values  = new gov.nih.nci.evs.browser.utils.SortUtils().caseInsensitiveSort(syn_vec);
			StringBuffer buf = new StringBuffer();
            for (int i=0; i<values.size(); i++) {
				String value = (String) values.elementAt(i);
				buf.append(value).append("$");
			}
			String s = buf.toString();
			if (s == null) return null;
			if (s.length() > 0) {
				s = s.substring(0, s.length()-1);
			}
			return s;
		} else if (type.compareTo(NCIT_DEFINITION) == 0) {
			StringBuffer buf = new StringBuffer();
            for (int i=0; i<u.size(); i++) {
				String t = (String) u.elementAt(i);
				if (t == null) return null;
				if (t.startsWith("definition")) {
					HashMap hmap = lineSegment2HashMap(t);
					String src = (String) hmap.get("source");
					if (src != null && src.compareTo("NCI") == 0) {
						String def = (String) hmap.get("prop_value");
						buf.append(def).append("$");
					}
				}
			}
			String s = buf.toString();
			if (s == null) return null;
			if (s.length() > 0) {
				s = s.substring(0, s.length()-1);
			}
			return s;

		} else if (type.compareTo(MALIGNANCY_STATUS) == 0) { //Neoplastic_Status
            for (int i=0; i<u.size(); i++) {
				String t = (String) u.elementAt(i);
				if (t == null) return null;
				if (t.startsWith("property")) {
					Vector w = gov.nih.nci.evs.browser.utils.StringUtils.parseData(t, "$");
					String prop_name = (String) w.elementAt(1);
					if (prop_name == null) return null;
					if (prop_name.compareTo("Neoplastic_Status") == 0) {
						String prop_value = (String) w.elementAt(2);
						return prop_value;
					}
				}
			}



		} else if (type.compareTo(NCI_METATHESAURUS_CUI) == 0 || type.compareTo("NCI_META_CUI") == 0) {
            for (int i=0; i<u.size(); i++) {
				String t = (String) u.elementAt(i);
				if (t == null) return null;
				if (t.startsWith("property")) {
					Vector w = gov.nih.nci.evs.browser.utils.StringUtils.parseData(t, "$");
					String prop_name = (String) w.elementAt(1);
					if (prop_name == null) return null;
					if (prop_name.compareTo("NCI_META_CUI") == 0) {
						String prop_value = (String) w.elementAt(2);
						return prop_value;
					}
				}
			}

		} else if (type.compareTo(UMLS_CUI) == 0 || type.compareTo("UMLS_CUI") == 0) {
            for (int i=0; i<u.size(); i++) {
				String t = (String) u.elementAt(i);
				if (t == null) return null;
				if (t.startsWith("property")) {
					Vector w = gov.nih.nci.evs.browser.utils.StringUtils.parseData(t, "$");
					String prop_name = (String) w.elementAt(1);
					if (prop_name == null) return null;
					if (prop_name.compareTo("UMLS_CUI") == 0) {
						String prop_value = (String) w.elementAt(2);
					    return prop_value;
					}
				}
			}
		} else {
            for (int i=0; i<u.size(); i++) {
				String t = (String) u.elementAt(i);
				if (t == null) return null;
				if (t.startsWith("property")) {
					Vector w = gov.nih.nci.evs.browser.utils.StringUtils.parseData(t, "$");
					String prop_name = (String) w.elementAt(1);
					if (prop_name == null) return null;
					if (prop_name.compareTo(type) == 0) {
						String prop_value = (String) w.elementAt(2);
						return prop_value;
					}
				}
			}
		}
		return null;
	}

    public String line2Table(String line, String source) {
		HashMap hmap = line2ValueHashMap(line, source);
		return valueHashMap2Table(hmap);
	}

    public HashMap line2ValueHashMap(String line, String source) {
		HashMap hmap = new HashMap();
		for (int k=0; k<TYPES.length; k++) {
			String type = TYPES[k];
			String value = parseProperty(line, type, source);
			hmap.put(new Integer(k), value);
		}
		return hmap;
	}

	public String valueHashMap2Table(HashMap hmap) {
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<TYPES.length; i++) {
			Integer int_obj = new Integer(i);
			String type = TYPES[i];
			String values = (String) hmap.get(int_obj);
			if (values != null  && values.length()>0) {
				buf.append("<table>");
				buf.append("<tr class=\"textbody\">");
				buf.append("    <td>" + type + "</td>");
				Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(values);
				buf.append("<td>");
				buf.append("<table>");
				for (int k=0; k<u.size(); k++) {
					String value = (String) u.elementAt(k);
					buf.append("<tr class=\"textbody\">");
					buf.append("<td valign=\"top\">" + value + "</td>");
					buf.append("</tr>");
			    }
				buf.append("</table>");
				buf.append("</td>");
				buf.append("</tr>");
				buf.append("</table>");
		    }
		}
		return buf.toString();
	}

    public String formatLine(Vector fields, HashMap fieldValueHmap, boolean isEven) {
		return formatLine(fields, fieldValueHmap, isEven, null);
	}


    public String formatLine(Vector fields, HashMap fieldValueHmap, boolean isEven, String fullSynTermName) {
		String key = null;
		String value = "";
        StringBuffer buf = new StringBuffer();
        if (isEven) {
			buf.append("<tr class=\"dataRowDark\">");
		} else {
			buf.append("<tr class=\"dataRowLight\">");
		}

        for (int i=0; i<fields.size(); i++) {
			buf.append("<td class=\"dataCellText\" scope=\"row\">");
			key = (String) fields.elementAt(i);
			value = (String) fieldValueHmap.get(key);
			if (value == null || value.compareTo("null") == 0) {
				value = "";
			}
			Vector w = gov.nih.nci.evs.browser.utils.StringUtils.parseData(value, "$");
			if (w == null) return null;
			if (w.size() == 1) {
				String hyperlink = value;
				if (key.compareTo(NCIT_CONCEPT_CODE) == 0 || key.compareTo(NCIT_SUBSET_CODE) == 0) {
					String code = value;
					hyperlink = getHyperlink(code);
				}
				buf.append(hyperlink);
		    } else {
				String table = UIUtils.createTable(w);
				buf.append(table);
			}
			buf.append("</td>");
		}
		buf.append("</tr>");
		return buf.toString();
	}

    public String formatLine(Vector fields, HashMap fieldValueHmap) {
		String key = null;
		String value = "";
        StringBuffer buf = new StringBuffer();
        for (int i=0; i<fields.size(); i++) {
			key = (String) fields.elementAt(i);
			value = (String) fieldValueHmap.get(key);
			if (value == null || value.compareTo("null") == 0) {
				value = "";
			}
			Vector w = gov.nih.nci.evs.browser.utils.StringUtils.parseData(value, "$");
			if (w == null) return null;
			if (w.size() == 1) {
				buf.append(value).append("|");
		    } else {
				StringBuffer value_buf = new StringBuffer();
				for (int n=0; n<w.size(); n++) {
					String s = (String) w.elementAt(n);
					value_buf.append(s);
					if (n <w.size()-1) {
						value_buf.append("$");
					}
				}
				String values = value_buf.toString();
                buf.append(values).append("|");
			}
		}
		String retstr = buf.toString();
		if (retstr == null) return null;
		if (retstr.endsWith("|")) {
			retstr = retstr.substring(0, retstr.length()-1);
		}
		return retstr;
	}

    public String formatLine(String line, String source) {
	    int n = line.indexOf("|");
	    String code = line.substring(0, n);
	    StringBuffer buf = new StringBuffer();
		buf.append("<table>");
		buf.append("<tr class=\"textbody\">");
		buf.append("<td>");
		buf.append(code);
		buf.append("</td>");
		buf.append("<td>");
		String table = line2Table(line, source);
		buf.append(table);
		buf.append("</td>");
		buf.append("</tr>");
		buf.append("</table>");
		return buf.toString();
	}

	public String getHyperlink(String code) {
		//return "<a href=\"https://nciterms.nci.nih.gov/ncitbrowser/ConceptReport.jsp?dictionary=NCI_Thesaurus&code=" + code + "&ns=NCI_Thesaurus\">" + code + "</a>";
		return uiUtils.getHyperlink(code, code);
    }

	public String generate(String vsd_uri, String version, String source, Vector fields, Vector codes) {
		return generate(vsd_uri, version, source, fields, codes, MAX_RETURN);
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public String generate(String vsd_uri, String version, String source, Vector fields, Vector codes, int maxReturn) {
        String fullSynTermName = null;
		if (source != null && source.compareTo(CDISC) == 0) {
			String vs_code = getValueSetCode(vsd_uri);
            if (vs_code != null) {
			   fullSynTermName = getFullSynTermName(Constants.NCI_THESAURUS, version, vs_code, NCI_SOURCE, TYPE_AB);
		    }
		}

		String metadata = vsmdu.getValueSetDefinitionMetadata(vsd_uri);
		Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(metadata);
		String defaultCodingScheme = (String) u.elementAt(6);
		if (defaultCodingScheme.compareTo("ncit") == 0) {
			defaultCodingScheme = "NCI_Thesaurus";
		}

		if (codes == null) {
		    codes = csdu.getCodesInValueSet(serviceUrl, vsd_uri);
		}
        StringBuffer buf = new StringBuffer();
		Vector w = resolve(defaultCodingScheme, version, source, fields, codes, maxReturn);

        HashMap fieldValueHmap = new HashMap();
		Entity e = null;
		if (fields.contains(NCIT_SUBSET_CODE)) {
			e = getValueSetHeaderConcept(vsd_uri);
			if (e != null) fieldValueHmap.put(NCIT_SUBSET_CODE, e.getEntityCode());
		}

        //[NCITERM-759] Term Browser: Rel 2.10: Values page table is formatted incorrectly
		buf.append("<table class=\"datatable_960\">").append("\n");
		for (int k=0; k<fields.size(); k++) {
			String field = (String) fields.elementAt(k);
			buf.append("<th class=\"textbody\" align=\"left\">").append("\n");
			if (source != null) {
				if (field.startsWith("Source ")) {
					field= field.replace("Source", source);
				}
			}
			buf.append(field).append("\n");
			buf.append("</th>").append("\n");
		}

		if (fields.contains(NCIT_SUBSET_NAME)) {
			if (e != null) fieldValueHmap.put(NCIT_SUBSET_NAME, e.getEntityDescription().getContent());
		}

		for (int i=0; i<w.size(); i++) {
			String line = (String) w.elementAt(i);
			for (int k=0; k<fields.size(); k++) {
				String type = (String) fields.elementAt(k);
				if (type.compareTo(NCIT_SUBSET_CODE) != 0 && type.compareTo(NCIT_SUBSET_NAME) != 0) {
					String value = parseProperty(line, type, source, fullSynTermName);
					fieldValueHmap.put(type, value);
				}
			}
			boolean isEven = UIUtils.isEven(i);
			String formatted_line = formatLine(fields, fieldValueHmap, isEven, fullSynTermName);
			buf.append(formatted_line).append("\n");
		}
		buf.append("</table>");
		return buf.toString();
	}

	public String generate(String vsd_uri, String version, String source, Vector fields, int maxReturn) {
		Vector codes = null;
		return generate(vsd_uri, version, source, fields, codes, maxReturn);
	}

	public String generate(String vsd_uri, String version, String source, Vector fields) {
		Vector codes = null;
		return generate(vsd_uri, version, source, fields, codes, MAX_RETURN);
	}

	public String generate(String vsd_uri, String version, Vector fields) {
        String source = getValueSetSupportedSource(vsd_uri);
        Vector codes = null;
		return generate(vsd_uri, version, source, fields, codes, MAX_RETURN);
	}

	public String generate(String vsd_uri, String version, Vector fields, int maxReturn) {
        String source = getValueSetSupportedSource(vsd_uri);
        Vector codes = null;
		return generate(vsd_uri, version, source, fields, codes, maxReturn);
	}

////////////////////////////////////////////////////////////////////////////////////////////////////
	public Vector export(String vsd_uri, String version, Vector fields) {
		Vector codes = null;
		String source = getValueSetSupportedSource(vsd_uri);
		return export(vsd_uri, version, source, fields, codes);
	}

	public Vector export(String vsd_uri, String version, String source, Vector fields) {
		Vector codes = null;
		return export(vsd_uri, version, source, fields, codes);
	}

	public Vector export(String vsd_uri, String version, String source, Vector fields, Vector codes) {
		if (codes == null) {
		    //codes = csdu.getCodesInCodingScheme(vsd_uri, null);
		    codes = csdu.getCodesInValueSet(serviceUrl, vsd_uri);
		}
        String fullSynTermName = null;
		if (source != null && source.compareTo(CDISC) == 0) {
			String vs_code = getValueSetCode(vsd_uri);
            if (vs_code != null) {
			   fullSynTermName = getFullSynTermName(Constants.NCI_THESAURUS, version, vs_code, NCI_SOURCE, TYPE_AB);
		    }
		}

		HashMap fieldValueHmap = new HashMap();
        Vector retvec = new Vector();
		Vector w = resolve(vsd_uri, version, source, fields, codes, codes.size());
		StringBuffer buf = new StringBuffer();
		for (int k=0; k<fields.size(); k++) {
			String field = (String) fields.elementAt(k);
			buf.append(field);
			if (k <fields.size()-1) {
				buf.append("|");
			}
		}
		retvec.add(buf.toString());
		for (int i=0; i<w.size(); i++) {
			String line = (String) w.elementAt(i);
			for (int k=0; k<fields.size(); k++) {
				String type = (String) fields.elementAt(k);
				String value = parseProperty(line, type, source, fullSynTermName);
				fieldValueHmap.put(type, value);
			}
			String formatted_line = formatLine(fields, fieldValueHmap);
            retvec.add(formatted_line);
		}
		return retvec;
	}

    public ValueSetDefinition findValueSetDefinitionByURI(String uri) {
		if (uri == null) return null;
		String valueSetDefinitionRevisionId = null;
		try {
			//LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
			ValueSetDefinition vsd = vsd_service.getValueSetDefinition(new URI(uri), valueSetDefinitionRevisionId);
			return vsd;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

    public String getValueSetSupportedSource(String uri) {
		ValueSetDefinition vsd = findValueSetDefinitionByURI(uri);
		Mappings mappings = vsd.getMappings();
		SupportedSource[] supporetedSources = mappings.getSupportedSource();
		if (supporetedSources == null) return null;
		for (int i=0; i<supporetedSources.length; i++) {
			SupportedSource supportedSource = supporetedSources[i];
			return supportedSource.getContent();
		}
        return null;
	}

//=================================================================================================================
    public String getVarName(String colName) {
		if (!vsHeading2VarHashMap.containsKey(colName)) {
			return null;
		}
		return (String) vsHeading2VarHashMap.get(colName);
	}

	public Vector getVars(String line) {
		Vector w = new Vector();
		Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(line);
		if (u == null) return null;
		for (int i=0; i<u.size(); i++) {
			String heading = (String) u.elementAt(i);
			String varName = getVarName(heading);
			w.add(varName);
		}
		return w;
	}

	public ArrayList delimited2List(String delimitedStr) {
		if (delimitedStr == null) return null;
		ArrayList a = new ArrayList();
		Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(delimitedStr, '$');
		u = removeDuplicateValues(u);
		u = new SortUtils().quickSort(u);
		for (int i=0; i<u.size(); i++) {
			String t = (String) u.elementAt(i);
			a.add(t);
		}
		return a;
	}

	public Vector removeDuplicateValues(Vector v) {
		Vector w = new Vector();
		HashSet hset = new HashSet();
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			if (t == null) return null;
			if (!hset.contains(t)) {
				hset.add(t);
				w.add(t);
			}
		}
		return w;
	}

//to be modified
    public ValueSet instantiateValueSet(String vsd_uri, String version, Vector fields) {
		Vector vs_data = export(vsd_uri, version, fields);
		ValueSet vs = new ValueSet();
		ArrayList concepts =new ArrayList();
		String heading_line = (String) vs_data.elementAt(0);
		Vector var_vec = getVars(heading_line);
		for (int i=1; i<vs_data.size(); i++) {
			String line = (String) vs_data.elementAt(i);
			Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(line, '|');
			//KLO, 12132023
			gov.nih.nci.evs.browser.bean.ConceptInVS c = new gov.nih.nci.evs.browser.bean.ConceptInVS();
			//if (c == null) return null;
			for (int j=0; j<var_vec.size(); j++) {
				String var = (String) var_vec.elementAt(j);
				String value = (String) u.elementAt(j);
                if (var.compareTo(NCITCODE) == 0) {
					c.setNcitCode(value);
                } else if (var.compareTo(SOURCEPREFERREDTERM) == 0) {
					c.setSourcePreferredTerm(value);
                } else if (var.compareTo(NCITPREFERREDTERM) == 0) {
					c.setNcitPreferredTerm(value);
				} else {
					ArrayList a = delimited2List(value);
					if (var.compareTo(NCITSYNONYMS) == 0) {
						c.setNcitSynonyms(a);
					} else if (var.compareTo(SOURCESYNONYMS) == 0) {
						c.setSourceSynonyms(a);
					} else if (var.compareTo(NCITDEFINITIONS) == 0) {
						c.setNcitDefinitions(a);
					} else if (var.compareTo(SOURCEDEFINITIONS) == 0) {
						c.setSourceDefinitions(a);
					}
				}
			}
			concepts.add(c);
		}
		vs.setConcepts(concepts);
        return vs;
    }


	public String object2XMLStream(ValueSet vs) {
		XStream xstream_xml = new XStream(new DomDriver());
		String xml = XML_DECLARATION + "\n" + xstream_xml.toXML(vs);
		if (!xml.endsWith(">")) {
			xml = xml + ">";
		}
		return xml;
	}


    public String get_rvs_tbl(String vsd_uri) {
		return get_rvs_tbl(vsd_uri, null);
	}

    public String get_rvs_tbl(String vsd_uri, Vector codes) {
		String rvs_tbl = null;
		String supported_source = vsmdu.getValueSetSupportedSource(vsd_uri);
		CodingSchemeDataUtils csdu = new CodingSchemeDataUtils(lbSvc);
		String metadata = vsmdu.getValueSetDefinitionMetadata(vsd_uri);
		Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(metadata);

		String defaultCodingScheme = (String) u.elementAt(6);
		boolean non_ncit_source = true;
		if (supported_source == null || supported_source.compareTo("null") == 0 || supported_source.compareTo("NCI") == 0) {
			non_ncit_source = false;
		}

		String supportedsources = (String) u.elementAt(4);
		if (codes == null) {
			codes = new Vector();
			ResolvedConceptReferencesIterator rcri = null;
			boolean resolveObjects = false;
			try {
    			rcri = csdu.resolveValueSet(serviceUrl, defaultCodingScheme, vsd_uri, null, resolveObjects);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (rcri != null) {
				try {
					while (rcri != null && rcri.hasNext()) {
						ResolvedConceptReference[] refs = rcri.next(500).getResolvedConceptReference();
						for (ResolvedConceptReference ref : refs) {
							codes.add(ref.getCode());
						}
					}
					/*
					while (rcri.hasNext()) {
						rcri = rcri.scroll(maxToReturn);
						ResolvedConceptReferenceList rcrl = rcri.getNext();
						ResolvedConceptReference[] rcra =
							rcrl.getResolvedConceptReference();
						for (int i = 0; i < rcra.length; i++) {
							ResolvedConceptReference rcr = rcra[i];
							codes.add(rcr.getCode());
						}
					}
					*/

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		Vector fields = getDefaultFields(non_ncit_source);
		if (supported_source != null && supported_source.compareTo(CTRP) == 0) {
			fields = getCTRPFields();
		}
		rvs_tbl = generate(vsd_uri, null, supported_source, fields, codes, codes.size());
		return rvs_tbl;
	}

	public String getValueSetCode(String vsd_uri) {
		int n = vsd_uri.lastIndexOf("/");
		if (n == -1) return null;
		return vsd_uri.substring(n+1, vsd_uri.length());
	}

	public String getFullSynTermName(String scheme, String version, String code, String source, String type) {
        Vector v = cd.getSynonyms(scheme, version, null, code);
        String name = null;
        for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			if (t == null) return null;
			if (t.indexOf("|AB|NCI|") != -1) {
				name = t.substring(0, t.indexOf("|AB|NCI|"));
				break;
			}
		}
		return name;
	}

	public String getSourcePT(String scheme, String version, String code, String source, String target) {
        Vector v = cd.getSynonyms(scheme, version, null, code);
        gov.nih.nci.evs.browser.utils.StringUtils.dumpVector("synonyms", v);
        //|PT|CDISC|SDTM-LBTESTCD|
        for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			int n = t.indexOf("|PT|" + source + "|" + target);
			if (n != -1) {
				return t.substring(0, n);
			}
		}

        for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			int n = t.indexOf("|PT|" + source);
			if (n != -1) {
				return t.substring(0, n);
			}
		}
		return null;
	}


	public String getValueSetDefinitionMetadata(String vsd_uri) {
		if (vsd_uri == null) return null;
		return vsmdu.getValueSetDefinitionMetadata(vsd_uri);
	}

    public void generateHTML(String vsd_uri) {
		generateHTML(vsd_uri, null);
	}

    public void generateHTML(String vsd_uri, String outputfile) {
		if (outputfile == null) {
			int n = vsd_uri.lastIndexOf("/");
			outputfile = "RVS_" + vsd_uri.substring(n+1, vsd_uri.length()) + ".html";
			System.out.println(outputfile);
		}
	    String rvs_tbl = get_rvs_tbl(vsd_uri);
	    Vector u = new Vector();
	    u.add("<html>");
	    u.add("<head>");
	    u.add("</head>");
	    u.add("<body>");
	    u.add(rvs_tbl);
	    u.add("</body>");
	    u.add("</html>");
	    Utils.saveToFile(outputfile, u);
	}

    public Entity getValueSetHeaderConcept(String vsd_uri) {
		Entity e = null;
		String metadata = vsmdu.getValueSetDefinitionMetadata(vsd_uri);
		Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(metadata);
		String defaultCodingScheme = (String) u.elementAt(6);
		if (defaultCodingScheme.compareTo("ncit") == 0) {
			defaultCodingScheme = "NCI_Thesaurus";
		}
		int n = vsd_uri.lastIndexOf("/");
		if (n != -1) {
			String code = vsd_uri.substring(n+1, vsd_uri.length());
			e = cd.getConceptByCode(defaultCodingScheme, null, code, null, false);
		}
		return e;
	}

/*
	public static void main(String[] args) {
		String serviceUrl = RemoteServerUtil.getServiceURL();
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();

		ValueSetFormatter formatter = new ValueSetFormatter(serviceUrl, lbSvc, vsd_service);
		String vsd_uri = "http://evs.nci.nih.gov/valueset/CDISC/C67154";
		vsd_uri = "http://evs.nci.nih.gov/valueset/CDISC/C66731";
		vsd_uri = "http://evs.nci.nih.gov/valueset/C138189";

	    vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
	    ValueSetMetadataUtils vsmdu = new ValueSetMetadataUtils(vsd_service);
		String metadata = vsmdu.getValueSetDefinitionMetadata(vsd_uri);
		Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(metadata);
		String name = (String) u.elementAt(0);
		String valueset_uri = (String) u.elementAt(1);
		String description = (String) u.elementAt(2);
		String concept_domain = (String) u.elementAt(3);
		String sources = (String) u.elementAt(4);
		String supportedsources = (String) u.elementAt(5);
		String supportedsource = null;
		if (supportedsources != null) {
			Vector u2 = gov.nih.nci.evs.browser.utils.StringUtils.parseData(supportedsources, ";");
			supportedsource = (String) u2.elementAt(0);
		}


		String defaultCodingScheme = (String) u.elementAt(6);
		String version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(defaultCodingScheme, Constants.PRODUCTION);
		System.out.println("\tname " + name);
		System.out.println("\tvalueset_uri " + valueset_uri);
		System.out.println("\tdescription " + description);
		System.out.println("\tconcept_domain " + concept_domain);
		System.out.println("\tsources " + sources);
		System.out.println("\tsupportedsources " + supportedsources);
		System.out.println("\tsupportedsource " + supportedsource);
		System.out.println("\tdefaultCodingScheme " + defaultCodingScheme);

		if (!DataUtils.isNCIT(defaultCodingScheme)) {
			exportToXMLAction();
			return;
		}
		if (defaultCodingScheme.compareTo("ncit") == 0) {
			defaultCodingScheme = "NCI_Thesaurus";
		}

		System.out.println("\tdefaultCodingScheme " + defaultCodingScheme);
		boolean withSource = true;
		if (supportedsource == null || supportedsource.compareTo("null") == 0 || supportedsource.compareTo("NCI") == 0) {
			withSource = false;
		}
		System.out.println("\twithSource " + withSource);
		Vector fields = formatter.getDefaultFields(withSource);
		for (int i=0; i<fields.size(); i++) {
		String t = (String) fields.elementAt(i);
		System.out.println(t);
		}

		ValueSet vs = formatter.instantiateValueSet(vsd_uri, version, fields);
		String xml_str = formatter.object2XMLStream(vs);

		System.out.println(vsd_uri);

		String scheme = "NCI_Thesaurus";
		String version = null;
		String nci_source = "NCI";
		String type = "AB";
		String code = "C17634";
		code = "C81982";

		String vs_code = formatter.getValueSetCode(vsd_uri);
		System.out.println(vs_code);
		String fullSynTermName = formatter.getFullSynTermName(scheme, version, vs_code, nci_source, type);
		System.out.println("fullSynTermName: " + fullSynTermName);
		String source = "CDISC";
	    String source_pt = formatter.getSourcePT(scheme, version, code, source, fullSynTermName);
	    System.out.println("source_pt: " + source_pt);

	    String metadata = formatter.getValueSetDefinitionMetadata(vsd_uri);
	    System.out.println(metadata);
		Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(metadata);
		String name = (String) u.elementAt(0);
		String valueset_uri = (String) u.elementAt(1);
		String description = (String) u.elementAt(2);
		String concept_domain = (String) u.elementAt(3);
		String sources = (String) u.elementAt(4);
		String supportedsources = (String) u.elementAt(5);
		String supportedsource = null;
		String defaultCodingScheme = (String) u.elementAt(6);
		System.out.println(name);
		System.out.println(valueset_uri);
		System.out.println(description);
		System.out.println(concept_domain);
		System.out.println(sources);
		System.out.println(supportedsources);
		System.out.println(defaultCodingScheme);

	    String rvs_tbl = formatter.get_rvs_tbl(vsd_uri);
	    Vector u = new Vector();
	    u.add("<html>");
	    u.add("<head>");
	    u.add("</head>");
	    u.add("<body>");
	    u.add(rvs_tbl);
	    u.add("</body>");
	    u.add("</html>");
	    Utils.saveToFile("VS_C66731.html", u);
	}
*/
}
