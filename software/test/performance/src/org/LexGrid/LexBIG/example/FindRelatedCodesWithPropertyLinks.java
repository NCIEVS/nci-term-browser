/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-term-browser/LICENSE.txt for details.
 */

/*
 * Copyright: (c) 2004-2007 Mayo Foundation for Medical Education and
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.eclipse.org/legal/epl-v10.html
 *
 */
package org.LexGrid.LexBIG.example;

import gov.nih.nci.system.applicationservice.EVSApplicationService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.util.RemoteServerUtil2;
import org.LexGrid.LexBIG.util.Util;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.concepts.PropertyLink;

/**
 * Example showing how to find all concepts codes related to another code
 * with distance 1, plus the Property Link relations.
 */
public class FindRelatedCodesWithPropertyLinks {

	public FindRelatedCodesWithPropertyLinks() {
		super();
	}

	/**
	 * Entry point for processing.
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println(
				"Example: FindRelatedCodes \"C25762\" ");
			return;
		};

		try {
			String code = args[0];
			new FindRelatedCodesWithPropertyLinks().run(code);
		} catch (Exception e) {
			Util.displayAndLogError("REQUEST FAILED !!!", e);
		}
	}

	public void run(String code)throws LBException{
		CodingSchemeSummary css = Util.promptForCodeSystem();
		if (css != null) {
			//LexBIGService lbSvc = LexBIGServiceImpl.defaultInstance();
			EVSApplicationService lbSvc = RemoteServerUtil2.createLexBIGService();
			String scheme = css.getCodingSchemeURN();
			CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
			csvt.setVersion(css.getRepresentsVersion());

			printFrom(code, lbSvc, scheme, csvt);
			printTo(code, lbSvc, scheme, csvt);
			printPropertyLinks(code, lbSvc, scheme, csvt);
		}
	}

	/**
	 * Display relations from the given code to other concepts.
	 * @param code
	 * @param relation
	 * @param lbSvc
	 * @param scheme
	 * @param csvt
	 * @throws LBException
	 */
	protected void printFrom(String code, EVSApplicationService lbSvc, String scheme, CodingSchemeVersionOrTag csvt)
		throws LBException
	{
		Util.displayMessage("Pointed at by ...");

		// Perform the query ...
/*
		ResolvedConceptReferenceList matches =
			lbSvc.getNodeGraph(scheme, csvt, null)
				.resolveAsList(
					ConvenienceMethods.createConceptReference(code, scheme),
					false, true, 1, 1, new LocalNameList(), null, null, 1024);
*/
        CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
		ResolvedConceptReferenceList matches =
			cng.resolveAsList(
					ConvenienceMethods.createConceptReference(code, scheme),
					false, true, 1, 1, new LocalNameList(), null, null, 1024);

		// Analyze the result ...
		if (matches.getResolvedConceptReferenceCount() > 0) {
			ResolvedConceptReference ref =
				(ResolvedConceptReference) matches.enumerateResolvedConceptReference().nextElement();

			// Print the associations
			AssociationList targetof = ref.getTargetOf();
			Association[] associations = targetof.getAssociation();
			for (int i = 0; i < associations.length; i++) {
				Association assoc = associations[i];
				AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
				for (int j = 0; j < acl.length; j++) {
					AssociatedConcept ac = acl[j];
					EntityDescription ed = ac.getEntityDescription();
					Util.displayMessage(
						"\t\t" + ac.getConceptCode() + "/"
							+ (ed == null?
									"**No Description**":ed.getContent()));
				}
			}
		}
	}
	/**
	 * Display Property Link relations.
	 * @param code
	 * @param relation
	 * @param lbSvc
	 * @param scheme
	 * @param csvt
	 * @throws LBException
	 */
	protected void printPropertyLinks(String code, EVSApplicationService lbSvc, String scheme, CodingSchemeVersionOrTag csvt) throws LBException
	{
		Util.displayMessage("Property Links ...");

		// Perform the query ...
/*
		ResolvedConceptReferenceList matches =
			lbSvc.getNodeGraph(scheme, csvt, null)
				.resolveAsList(
					ConvenienceMethods.createConceptReference(code, scheme),
					true, true, 1, 1, new LocalNameList(), null, null, 1024);
*/
        CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
		ResolvedConceptReferenceList matches =
			cng.resolveAsList(
					ConvenienceMethods.createConceptReference(code, scheme),
					true, true, 1, 1, new LocalNameList(), null, null, 1024);


		// Analyze the result ...
		if (matches.getResolvedConceptReferenceCount() > 0) {
			ResolvedConceptReference ref =
				(ResolvedConceptReference) matches.enumerateResolvedConceptReference().nextElement();

			//check to see if it has property links -- if so display the relationships
			PropertyLink[] propertyLinks = ref.getReferencedEntry().getPropertyLink();

			//analyze the Property Links
	    	for(int i=0;i < propertyLinks.length;i++){
	    		PropertyLink propertyLink = propertyLinks[i];

	    		//find the source of the Property Link
	    		String sourcePropertyId = propertyLink.getSourceProperty();

	    		//find the target of the Property Link
	    		String targetPropertyId = propertyLink.getTargetProperty();

	    		String sourceText = "";
	    		String targetText = "";

	    		//link the Property Link source and target to the correspond presentation text
	    		//Example: Find Presentation with Property ID = T-1 -> get its value and present it.
	    		for(int j=0;j<ref.getReferencedEntry().getPresentation().length;j++){
	    			String propertyId = ref.getReferencedEntry().getPresentation(j).getPropertyId();
	    			if(propertyId.equals(sourcePropertyId)){
	    				sourceText = ref.getReferencedEntry().getPresentation(j).getText().getContent();
	    			}
	    			if(propertyId.equals(targetPropertyId)){
	    				targetText = ref.getReferencedEntry().getPresentation(j).getText().getContent();
	    			}

	    		}
				Util.displayMessage(
						"\t\t" + "Association: " +
						propertyLinks[i].getLink() + " " +
						"\n" + "\t\t\t" +
						"Source: " + propertyLinks[i].getSourceProperty() + " "  + sourceText +
						"\n" + "\t\t\t" +
						"Target: "+ propertyLinks[i].getTargetProperty() + " " + targetText);
			}
		}
	}

	/**
	 * Display relations to the given code from other concepts.
	 * @param code
	 * @param relation
	 * @param lbSvc
	 * @param scheme
	 * @param csvt
	 * @throws LBException
	 */
	protected void printTo(String code, EVSApplicationService lbSvc, String scheme, CodingSchemeVersionOrTag csvt)
		throws LBException
	{
		Util.displayMessage("Points to ...");

		// Perform the query ...
/*
		ResolvedConceptReferenceList matches =
			lbSvc.getNodeGraph(scheme, csvt, null)
				.resolveAsList(
					ConvenienceMethods.createConceptReference(code, scheme),
					true, false, 1, 1, new LocalNameList(), null, null, 1024);
*/

        CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
		ResolvedConceptReferenceList matches =
			cng.resolveAsList(
					ConvenienceMethods.createConceptReference(code, scheme),
					true, false, 1, 1, new LocalNameList(), null, null, 1024);


		// Analyze the result ...
		if (matches.getResolvedConceptReferenceCount() > 0) {
			ResolvedConceptReference ref =
				(ResolvedConceptReference) matches.enumerateResolvedConceptReference().nextElement();

			// Print the associations
			AssociationList sourceof = ref.getSourceOf();
			Association[] associations = sourceof.getAssociation();
			for (int i = 0; i < associations.length; i++) {
				Association assoc = associations[i];
				AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
				for (int j = 0; j < acl.length; j++) {
					AssociatedConcept ac = acl[j];
					EntityDescription ed = ac.getEntityDescription();
					Util.displayMessage(
						"\t\t" + ac.getConceptCode() + "/"
							+ (ed == null?
									"**No Description**":ed.getContent()));
				}
			}

		}
	}
	}