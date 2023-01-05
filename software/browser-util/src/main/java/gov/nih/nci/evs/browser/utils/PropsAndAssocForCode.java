package gov.nih.nci.evs.browser.utils;


//import gov.nih.nci.system.applicationservice.ApplicationException;
//import gov.nih.nci.system.client.ApplicationServiceProvider;
import java.io.*;
import java.util.Enumeration;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
//import org.LexGrid.LexBIG.caCore.interfaces.LexEVSApplicationService;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;


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


public class PropsAndAssocForCode {

	private String serviceUrl = null;
	LexBIGService lbSvc = null;

	public PropsAndAssocForCode() {
		lbSvc = getLexBIGService();
	}

	public PropsAndAssocForCode(LexBIGService lbSvc) {
		this.lbSvc = lbSvc;
	}

    public LexBIGService getLexBIGService() {
		LexBIGServiceImpl lbSvc = null;
		try {
			lbSvc = LexBIGServiceImpl.defaultInstance();

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return lbSvc;
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
    public void run(PrintWriter pw, String scheme, String version, String code) throws LBException {
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		if (version != null) csvt.setVersion(version);
		printProps(pw, code, lbSvc, scheme, csvt);
		printFrom(pw, code, lbSvc, scheme, csvt);
		printTo(pw, code, lbSvc, scheme, csvt);
    }



    public void run(PrintWriter pw, String scheme, String version, String code, String entityType) throws LBException {
		CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
		if (version != null) csvt.setVersion(version);
		printProps(pw, code, lbSvc, scheme, csvt, entityType);
		printFrom(pw, code, lbSvc, scheme, csvt);
		printTo(pw, code, lbSvc, scheme, csvt);
    }




    void displayMessage(String s) {
		displayMessage(null, s);
	}

    void displayMessage(PrintWriter pw, String s) {
	    if (pw == null) {
			System.out.println(s);
		} else {
			pw.println(s);
		}
	}

    public CodedNodeSet getCodedNodeSet(String scheme, CodingSchemeVersionOrTag csvt, String entityType) {
		LocalNameList lnl = new LocalNameList();
		lnl.addEntry(entityType);
		CodedNodeSet cns = null;
		try {
			cns = lbSvc.getNodeSet(scheme, csvt, lnl);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cns;
    }

    public CodedNodeSet getCodedNodeSet(String scheme, CodingSchemeVersionOrTag csvt) {
		return getCodedNodeSet(scheme, csvt, "concept");
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

    public boolean printProps(PrintWriter pw, String code, LexBIGService lbSvc, String scheme, CodingSchemeVersionOrTag csvt)
            throws LBException {
		return printProps(pw, code, lbSvc, scheme, csvt, "concept");
	}


    public boolean printProps(PrintWriter pw, String code, LexBIGService lbSvc, String scheme, CodingSchemeVersionOrTag csvt, String entityType)
            throws LBException {
        ConceptReferenceList crefs = ConvenienceMethods.createConceptReferenceList(new String[] { code }, scheme);

        //CodedNodeSet cns = lbSvc.getCodingSchemeConcepts(scheme, csvt);

        CodedNodeSet cns = getCodedNodeSet(scheme, csvt, entityType);
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

                 String representationalForm = presentation.getRepresentationalForm();
                 StringBuffer buf = new StringBuffer();
                 buf.append("\tPresentation name: " + presentation.getPropertyName());
                 buf.append(" text: " + presentation.getValue().getContent());
                 buf.append(" form: " + representationalForm);
                 displayMessage(pw, buf.toString());


                 PropertyQualifier[] qualifiers = presentation.getPropertyQualifier();
                 if (qualifiers == null) return false;
                 for (int k=0; k<qualifiers.length; k++) {
                      PropertyQualifier qualifier = qualifiers[k];
                      buf = new StringBuffer();
                      buf.append("\t\tQualifier name: " + qualifier.getPropertyQualifierName());
                      buf.append(" text: " + qualifier.getValue().getContent());
                      displayMessage(pw, buf.toString());
				 }

                 Source[] sources = presentation.getSource();
                 if (sources == null) return false;
                 for (int k=0; k<sources.length; k++) {
                      Source source = sources[k];
						 displayMessage(pw, new StringBuffer().append("\t\tSource: ").append(source.getContent()).toString());
				 }

            }
            System.out.println("\n");

            Definition[] definitions = node.getDefinition();
            if (definitions == null) return false;
            for (int i = 0; i < definitions.length; i++) {
                Definition definition = definitions[i];
                if (definition == null) return false;
                displayMessage(pw, new StringBuffer().append("\tDefinition name: ").append(definition.getPropertyName())
                        .append(" text: ").append(definition.getValue().getContent()).toString());

                 Source[] sources = definition.getSource();
                 if (sources == null) return false;
                 for (int j=0; j<sources.length; j++) {
					 Source src = sources[j];
					 displayMessage(pw, new StringBuffer().append("\t\tSource: ").append(src.getContent()).toString());
				 }

				 PropertyQualifier[] qualifiers = definition.getPropertyQualifier();
				 if (qualifiers != null && qualifiers.length > 0) {
					 for (int j = 0; j < qualifiers.length; j++) {
						 PropertyQualifier q = qualifiers[j];
						 String qualifier_name = q.getPropertyQualifierName();
						 String qualifier_value = q.getValue().getContent();
                         displayMessage(pw, new StringBuffer().append("\t\tQualifier - ").append(qualifier_name + ": ").append(qualifier_value).toString());
					 }
				 }
            }

            System.out.println("\n");

            Comment[] comments = node.getComment();
            if (comments == null) return false;
            for (int i = 0; i < comments.length; i++) {
                Comment comment = comments[i];
                if (comment == null) return false;
                displayMessage(pw, new StringBuffer().append("\tComment name: ").append(comment.getPropertyName())
                        .append(" text: ").append(comment.getValue().getContent()).toString());

 				 PropertyQualifier[] qualifiers = comment.getPropertyQualifier();
				 if (qualifiers != null && qualifiers.length > 0) {
					 for (int j = 0; j < qualifiers.length; j++) {
						 PropertyQualifier q = qualifiers[j];
						 String qualifier_name = q.getPropertyQualifierName();
						 String qualifier_value = q.getValue().getContent();
                         displayMessage(pw, new StringBuffer().append("\t\tQualifier - ").append(qualifier_name + ": ").append(qualifier_value).toString());
					 }
				 }


            }
            System.out.println("\n");

            Property[] props = node.getProperty();
            if (props == null) return false;
            for (int i = 0; i < props.length; i++) {
                Property prop = props[i];
                if (prop == null) return false;
                displayMessage(pw, new StringBuffer().append("\tProperty name: ").append(prop.getPropertyName())
                        .append(" text: ").append(prop.getValue().getContent()).toString());

  				 PropertyQualifier[] qualifiers = prop.getPropertyQualifier();
				 if (qualifiers != null && qualifiers.length > 0) {
					 for (int j = 0; j < qualifiers.length; j++) {
						 PropertyQualifier q = qualifiers[j];
						 String qualifier_name = q.getPropertyQualifierName();
						 String qualifier_value = q.getValue().getContent();
                         displayMessage(pw, new StringBuffer().append("\t\tQualifier - ").append(qualifier_name + ": ").append(qualifier_value).toString());
					 }
				 }


            }
            System.out.println("\n");

            props = node.getAllProperties();
            for (int i = 0; i < props.length; i++) {
                Property prop = props[i];
                if (prop == null) return false;
                displayMessage(pw, new StringBuffer().append("\tProperty name: ").append(prop.getPropertyName())
                        .append(" text: ").append(prop.getValue().getContent()).toString());

   				 PropertyQualifier[] qualifiers = prop.getPropertyQualifier();
				 if (qualifiers != null && qualifiers.length > 0) {
					 for (int j = 0; j < qualifiers.length; j++) {
						 PropertyQualifier q = qualifiers[j];
						 String qualifier_name = q.getPropertyQualifierName();
						 String qualifier_value = q.getValue().getContent();
                         displayMessage(pw, new StringBuffer().append("\t\tQualifier - ").append(qualifier_name + ": ").append(qualifier_value).toString());
					 }
				 }



            }
            System.out.println("\n");

        } else {
            displayMessage(pw, "No match found!");
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
    protected void printFrom(PrintWriter pw, String code, LexBIGService lbSvc, String scheme, CodingSchemeVersionOrTag csvt)
            throws LBException {
        displayMessage(pw, "\n\tPointed at by ...");

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
					if (associations != null && associations.length > 0) {
						for (int i = 0; i < associations.length; i++) {
							Association assoc = associations[i];
							//displayMessage(pw, "\t" + assoc.getAssociationName());

							AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
							if (acl != null) {
								for (int j = 0; j < acl.length; j++) {
									AssociatedConcept ac = acl[j];
									String rela = replaceAssociationNameByRela(ac, assoc.getAssociationName());
									EntityDescription ed = ac.getEntityDescription();
									displayMessage(pw, "\t\t" + ac.getConceptCode() + "/"
											+ (ed == null ? "**No Description**" : ed.getContent()) + " --> (" + rela + ") --> " + code);
									if (ac.getAssociationQualifiers() != null && ac.getAssociationQualifiers().getNameAndValue() != null) {
												for(NameAndValue nv: ac.getAssociationQualifiers().getNameAndValue()){
										displayMessage(pw, "\t\t\tAssoc Qualifier - " + nv.getName() + ": " + nv.getContent());
										displayMessage(pw, "\n");
												}

									}
								}
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
    protected void printTo(PrintWriter pw, String code, LexBIGService lbSvc, String scheme, CodingSchemeVersionOrTag csvt)
            throws LBException {
        displayMessage(pw, "\n\tPoints to ...");

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

                if (sourceof != null) {
					Association[] associations = sourceof.getAssociation();

					if (associations != null && associations.length > 0) {
						for (int i = 0; i < associations.length; i++) {
							Association assoc = associations[i];
							//displayMessage(pw, "\t" + assoc.getAssociationName());

							AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
							if (acl == null) return;
							for (int j = 0; j < acl.length; j++) {
								AssociatedConcept ac = acl[j];

								String rela = replaceAssociationNameByRela(ac, assoc.getAssociationName());

								EntityDescription ed = ac.getEntityDescription();
								displayMessage(pw, "\t\t" + code + " --> (" + rela + ") --> " + ac.getConceptCode() + "/"
										+ (ed == null ? "**No Description**" : ed.getContent()));
								if (ac.getAssociationQualifiers() != null && ac.getAssociationQualifiers().getNameAndValue() != null) {
											for(NameAndValue nv: ac.getAssociationQualifiers().getNameAndValue()){
									displayMessage(pw, "\t\t\tAssoc Qualifier - " + nv.getName() + ": " + nv.getContent());
									displayMessage(pw, "\n");
											}

								}
							}
						}
					}
			    }
            }
        }
    }



    private String replaceAssociationNameByRela(AssociatedConcept ac, String associationName) {
		return associationName;
	}

    public static void main(String[] args) {
		String scheme = "owl2lexevs2.owl";
		String version = "0.1.2";
		String code = "in_organism";
		String entityType = "association";
		code = "http://purl.obolibrary.org/obo/CL_0000000";

		scheme = "PDQ";
		version = null;//"2013_0131";
        entityType = "concept";
		code = "CDR0000459776";


        //PrintWriter pw = null;
        try {
            new PropsAndAssocForCode().run(null, scheme, version, code, entityType);
        } catch (Exception e) {
            displayAndLogError("REQUEST FAILED !!!", e);
        }
    }
}

