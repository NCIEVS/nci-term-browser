package gov.nih.nci.evs.browser.utils;


import gov.nih.nci.system.client.ApplicationServiceProvider;
import java.io.*;
import java.net.URI;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Map;
import org.apache.logging.log4j.*;
import org.json.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.commonTypes.*;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSApplicationService;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
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
import org.LexGrid.LexBIG.History.*;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.naming.*;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;
import org.LexGrid.util.PrintUtility;
import org.LexGrid.valueSets.DefinitionEntry;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;
import org.LexGrid.valueSets.EntityReference;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.LexGrid.valueSets.PropertyMatchValue;
import org.LexGrid.valueSets.PropertyReference;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.*;


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


public class ConceptEntity {
    static String serviceUrl = "https://lexevsapi6.nci.nih.gov/lexevsapi64";
    String entityDescription = null;
    LexBIGService lbSvc = null;

    public ConceptEntity() {
        //super();
    }


    public ConceptEntity(LexBIGService lbSvc) {
        //super();
        this.lbSvc = lbSvc;
    }



    static void displayAndLogError(String s, Exception e) {
		System.out.println(s);
		e.printStackTrace();
	}

    /**
     * Process the provided code.
     *
     * @param code
     * @throws LBException
     */
    public void run(String scheme, String version, String code) throws LBException {
	    //LexBIGService lbSvc = createLexBIGService();
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		if (version != null) csvt.setVersion(version);
		printProps(code, scheme, csvt);
		printFrom(code, scheme, csvt);
		printTo(code, scheme, csvt);
    }


    public void displayMessage(String s) {
	   System.out.println(s);
	}


    /**
     * Display properties for the given code.
     *
     * @param code
     * @param lbSvc
     * @param scheme
     * @param csvt
     * @return
     * @throws LBException
     */
    protected boolean printProps(String code, String scheme, CodingSchemeVersionOrTag csvt)
            throws LBException {
        // Perform the query ...
        System.out.println("======================================================================");
        System.out.println("Coding scheme: " + scheme);
        System.out.println("Coding scheme version: " + csvt.getVersion());
        System.out.println("code: " + code);
        System.out.println("======================================================================");

        ConceptReferenceList crefs = ConvenienceMethods.createConceptReferenceList(new String[] { code }, scheme);

        CodedNodeSet cns = lbSvc.getCodingSchemeConcepts(scheme, csvt);


if (cns == null) {
System.out.println("CNS == NULL???");
return false;
}


        cns = cns.restrictToStatus(ActiveOption.ALL, null);
        cns = cns.restrictToCodes(crefs);
        ResolvedConceptReferenceList matches = cns.resolveToList(null, null, null, 1);

        // Analyze the result ...
        if (matches.getResolvedConceptReferenceCount() > 0) {
            ResolvedConceptReference ref = (ResolvedConceptReference) matches.enumerateResolvedConceptReference()
                    .nextElement();

            Entity node = ref.getEntity();

            Presentation[] prsentations = node.getPresentation();
            if (prsentations == null) return false;
            for (int i = 0; i < prsentations.length; i++) {
                 Presentation presentation = prsentations[i];
                 if (presentation == null) return false;
                 displayMessage(new StringBuffer().append("\tPresentation name: ").append(presentation.getPropertyName())
                         .append(" text: ").append(presentation.getValue().getContent()).toString());

                 PropertyQualifier[] qualifiers = presentation.getPropertyQualifier();
                 if (qualifiers == null) return false;
                 for (int k=0; k<qualifiers.length; k++) {
                      PropertyQualifier qualifier = qualifiers[k];
						 if (qualifier == null) return false;
						 displayMessage(new StringBuffer().append("\t\tQualifier name: ").append(qualifier.getPropertyQualifierName())
								 .append(" text: ").append(qualifier.getValue().getContent()).toString());
				 }

                 Source[] sources = presentation.getSource();
                 if (sources == null) return false;
                 for (int k=0; k<sources.length; k++) {
                      Source source = sources[k];
						 displayMessage(new StringBuffer().append("\t\tSource: ").append(source.getContent()).toString());
				 }

            }
            System.out.println("\n");

            Definition[] definitions = node.getDefinition();
            if (definitions == null) return false;
            for (int i = 0; i < definitions.length; i++) {
                Definition definition = definitions[i];
                if (definition == null) return false;
                displayMessage(new StringBuffer().append("\tDefinition name: ").append(definition.getPropertyName())
                        .append(" text: ").append(definition.getValue().getContent()).toString());
            }
            System.out.println("\n");

            Comment[] comments = node.getComment();
            if (comments == null) return false;
            for (int i = 0; i < comments.length; i++) {
                Comment comment = comments[i];
                if (comment == null) return false;
                displayMessage(new StringBuffer().append("\tComment name: ").append(comment.getPropertyName())
                        .append(" text: ").append(comment.getValue().getContent()).toString());
            }
            System.out.println("\n");

            Property[] props = node.getProperty();
            if (props == null) return false;
            for (int i = 0; i < props.length; i++) {
                Property prop = props[i];
                if (prop == null) return false;
                displayMessage(new StringBuffer().append("\tProperty name: ").append(prop.getPropertyName())
                        .append(" text: ").append(prop.getValue().getContent()).toString());
            }
            System.out.println("\n");


            System.out.println("\n=========================================================================");

            props = node.getAllProperties();
            for (int i = 0; i < props.length; i++) {
                Property prop = props[i];
                if (prop == null) return false;
                displayMessage(new StringBuffer().append("\tProperty name: ").append(prop.getPropertyName())
                        .append(" text: ").append(prop.getValue().getContent()).toString());
            }
            System.out.println("\n");

        } else {
            displayMessage("No match found!");
            return false;
        }

        return true;
    }

    /**
     * Display relations to the given code from other concepts.
     *
     * @param code
     * @param lbSvc
     * @param scheme
     * @param csvt
     * @throws LBException
     */
    @SuppressWarnings("unchecked")
    public void printFrom(String code, String scheme, CodingSchemeVersionOrTag csvt)
            throws LBException {
        displayMessage("Pointed at by ...");

        // Perform the query ...
        ResolvedConceptReferenceList matches = lbSvc.getNodeGraph(scheme, csvt, null).resolveAsList(
                ConvenienceMethods.createConceptReference(code, scheme), false, true, 1, 1, new LocalNameList(), null,
                null, 1024);

        // Analyze the result ...
        if (matches.getResolvedConceptReferenceCount() > 0) {
            Enumeration<? extends ResolvedConceptReference> refEnum = matches.enumerateResolvedConceptReference();

            while (refEnum.hasMoreElements()) {
                ResolvedConceptReference ref = refEnum.nextElement();
                AssociationList targetof = ref.getTargetOf();

                if (targetof != null) {
					Association[] associations = targetof.getAssociation();

					for (int i = 0; i < associations.length; i++) {
						Association assoc = associations[i];
						displayMessage("\t" + assoc.getAssociationName());

						AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
						if (acl == null) return;
						for (int j = 0; j < acl.length; j++) {
							AssociatedConcept ac = acl[j];

							String rela = replaceAssociationNameByRela(ac, assoc.getAssociationName());
							EntityDescription ed = ac.getEntityDescription();
							String concept_code = ac.getConceptCode();
							if (concept_code == null) return;
							if (!concept_code.startsWith("@")) {
								displayMessage("\t\t" + ac.getConceptCode() + "/"
									+ (ed == null ? "**No Description**" : ed.getContent()) + " --> (" + rela + ") --> " + code);
						    }
						}
					}
			    }
            }
        }

    }


    public String code2Name(String scheme, String version, String code) {
		ConceptDetails cd = new ConceptDetails(lbSvc);
		String ns = cd.getNamespaceByCode(scheme, version, code);
        Entity entity = new ConceptDetails(lbSvc).getConceptByCode(scheme, version, code, ns, true);
        String name = entity.getEntityDescription().getContent();
        return name;
	}


    public void printFrom(PrintWriter pw, String scheme, String version, String code)
            throws LBException {
        String name = code2Name(scheme, version, code);

		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		if (version == null) {
			csvt.setVersion(version);
		}
        // Perform the query ...
        ResolvedConceptReferenceList matches = lbSvc.getNodeGraph(scheme, csvt, null).resolveAsList(
                ConvenienceMethods.createConceptReference(code, scheme), false, true, 1, 1, new LocalNameList(), null,
                null, -1);

        // Analyze the result ...
        if (matches != null && matches.getResolvedConceptReferenceCount() > 0) {
            Enumeration<? extends ResolvedConceptReference> refEnum = matches.enumerateResolvedConceptReference();

            while (refEnum.hasMoreElements()) {
                ResolvedConceptReference ref = refEnum.nextElement();
                AssociationList targetof = ref.getTargetOf();

                if (targetof != null) {
					Association[] associations = targetof.getAssociation();

					for (int i = 0; i < associations.length; i++) {
						Association assoc = associations[i];
						//displayMessage("\t" + assoc.getAssociationName());

						AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
						if (acl == null) return;
						for (int j = 0; j < acl.length; j++) {
							AssociatedConcept ac = acl[j];
							String rela = replaceAssociationNameByRela(ac, assoc.getAssociationName());
							EntityDescription ed = ac.getEntityDescription();
							String concept_code = ac.getConceptCode();
							if (concept_code == null) return;
							if (!concept_code.startsWith("@")) {
								pw.println(ac.getConceptCode() + "|"
									+ (ed == null ? "**No Description**" : ed.getContent()) + "|" + rela
									+ "|" + code + "|" + name);
						    }
						}
					}
			    }
            }
        }
    }


    /**
     * Display relations from the given code to other concepts.
     *
     * @param code
     * @param lbSvc
     * @param scheme
     * @param csvt
     * @throws LBException
     */
    @SuppressWarnings("unchecked")
    protected void printTo(String code, String scheme, CodingSchemeVersionOrTag csvt)
            throws LBException {
        displayMessage("Points to ...");

        // Perform the query ...
        ResolvedConceptReferenceList matches = lbSvc.getNodeGraph(scheme, csvt, null).resolveAsList(
                ConvenienceMethods.createConceptReference(code, scheme), true, false, 1, 1, new LocalNameList(), null,
                null, 1024);

        // Analyze the result ...
        if (matches.getResolvedConceptReferenceCount() > 0) {
            Enumeration<? extends ResolvedConceptReference> refEnum = matches.enumerateResolvedConceptReference();

            while (refEnum.hasMoreElements()) {
                ResolvedConceptReference ref = refEnum.nextElement();
                AssociationList sourceof = ref.getSourceOf();
                Association[] associations = sourceof.getAssociation();

                for (int i = 0; i < associations.length; i++) {
                    Association assoc = associations[i];
                    displayMessage("\t" + assoc.getAssociationName());

                    AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
                    if (acl == null) return;
                    for (int j = 0; j < acl.length; j++) {
                        AssociatedConcept ac = acl[j];

                        String rela = replaceAssociationNameByRela(ac, assoc.getAssociationName());

                        EntityDescription ed = ac.getEntityDescription();
                        displayMessage("\t\t" + code + " --> (" + rela + ") --> " + ac.getConceptCode() + "/"
                                + (ed == null ? "**No Description**" : ed.getContent()));
                    }
                }
            }
        }
    }




    public String replaceAssociationNameByRela(AssociatedConcept ac, String associationName) {
		if (ac.getAssociationQualifiers() == null) return associationName;
		if (ac.getAssociationQualifiers().getNameAndValue() == null) return associationName;

		for(NameAndValue qual : ac.getAssociationQualifiers().getNameAndValue()){
			String qualifier_name = qual.getName();
			String qualifier_value = qual.getContent();
			if (qualifier_name.compareToIgnoreCase("rela") == 0) {
				return qualifier_value; // replace associationName by Rela value
			}
		}
		return associationName;
	}



    public Vector getProps(String scheme, String version, String code, String propName) throws LBException {
        Vector w = new Vector();
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null) {
			csvt.setVersion(version);
		}
        ConceptReferenceList crefs = ConvenienceMethods.createConceptReferenceList(new String[] { code }, scheme);
        CodedNodeSet cns = lbSvc.getCodingSchemeConcepts(scheme, csvt);
        cns = cns.restrictToStatus(ActiveOption.ALL, null);
        cns = cns.restrictToCodes(crefs);
        ResolvedConceptReferenceList matches = cns.resolveToList(null, null, null, 1);
        // Analyze the result ...
        if (matches.getResolvedConceptReferenceCount() > 0) {
            ResolvedConceptReference ref = (ResolvedConceptReference) matches.enumerateResolvedConceptReference()
                    .nextElement();
            Entity node = ref.getEntity();
            Property[] props = node.getAllProperties();
            if (props == null) return null;
            for (int i = 0; i < props.length; i++) {
                Property prop = props[i];
                if (prop == null) return null;
                if (prop.getPropertyName().compareTo(propName) == 0) {
					w.add(prop.getValue().getContent().toString());
				}
            }
        }
        return w;
    }


    public Vector getOutboundRelationships(String scheme, String version, String code, String name)
            throws LBException {
        Vector w = new Vector();
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null) {
			csvt.setVersion(version);
		}

        // Perform the query ...
        ResolvedConceptReferenceList matches = lbSvc.getNodeGraph(scheme, csvt, null).resolveAsList(
                ConvenienceMethods.createConceptReference(code, scheme), true, false, 1, 1, new LocalNameList(), null,
                null, 1024);

        // Analyze the result ...
        if (matches.getResolvedConceptReferenceCount() > 0) {
            Enumeration<? extends ResolvedConceptReference> refEnum = matches.enumerateResolvedConceptReference();

            while (refEnum.hasMoreElements()) {
                ResolvedConceptReference ref = refEnum.nextElement();
                AssociationList sourceof = ref.getSourceOf();
                Association[] associations = sourceof.getAssociation();

                for (int i = 0; i < associations.length; i++) {
                    Association assoc = associations[i];
                    //displayMessage("\t" + assoc.getAssociationName());
                    AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
                    if (acl == null) return null;
                    for (int j = 0; j < acl.length; j++) {
                        AssociatedConcept ac = acl[j];
                        String rela = replaceAssociationNameByRela(ac, assoc.getAssociationName());

                        String asso_concept_name = ac.getEntityDescription().getContent();
                        String line = code + "|" + name + "|" + rela + "|outbound|"
                                      + asso_concept_name + "|" + ac.getConceptCode();
                        w.add(line);
                    }
                }
            }
        }
        return w;
    }

    public Vector getInboundRelationships(String scheme, String version, String code, String name)
            throws LBException {
        Vector w = new Vector();
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null) {
			csvt.setVersion(version);
		}

        // Perform the query ...
        ResolvedConceptReferenceList matches = lbSvc.getNodeGraph(scheme, csvt, null).resolveAsList(
                ConvenienceMethods.createConceptReference(code, scheme), false, true, 1, 1, new LocalNameList(), null,
                null, -1);

        // Analyze the result ...
        if (matches.getResolvedConceptReferenceCount() > 0) {
            Enumeration<? extends ResolvedConceptReference> refEnum = matches.enumerateResolvedConceptReference();

            while (refEnum.hasMoreElements()) {
                ResolvedConceptReference ref = refEnum.nextElement();
                AssociationList targetof = ref.getTargetOf();

                if (targetof != null) {
					Association[] associations = targetof.getAssociation();
					if (associations == null) return null;
					for (int i = 0; i < associations.length; i++) {
						Association assoc = associations[i];
						//displayMessage("\t" + assoc.getAssociationName());
						AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
						if (acl == null) return null;
						for (int j = 0; j < acl.length; j++) {
							AssociatedConcept ac = acl[j];
							String rela = replaceAssociationNameByRela(ac, assoc.getAssociationName());
							String asso_concept_name = ac.getEntityDescription().getContent();
							String line = code + "|" + name + "|" + rela + "|inbound|"
										  + asso_concept_name + "|" + ac.getConceptCode();
							w.add(line);
						}
					}
			    }
            }
        }
        return w;
    }
}

