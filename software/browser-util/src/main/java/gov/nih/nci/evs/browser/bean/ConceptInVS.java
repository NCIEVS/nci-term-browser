package gov.nih.nci.evs.browser.bean;


import java.io.*;
import java.net.*;
import java.util.*;


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


public class ConceptInVS
{
// Variable declaration
	private String ncitCode; //*
	private String sourcePreferredTerm;  //*
	private String ncitPreferredTerm; //*
	private ArrayList ncitSynonyms;
	private ArrayList sourceSynonyms; //*
	private ArrayList ncitDefinitions; //*
	private ArrayList sourceDefinitions;

// Default constructor
	public ConceptInVS() {
	}

// Constructor
	public ConceptInVS(
		String ncitCode,
		String sourcePreferredTerm,
		String ncitPreferredTerm,
		ArrayList sourceSynonyms,
		ArrayList ncitDefinitions) {

		this.ncitCode = ncitCode;
		this.ncitPreferredTerm = ncitPreferredTerm;
		this.sourcePreferredTerm = sourcePreferredTerm;
		this.ncitSynonyms = null;
		this.sourceSynonyms = sourceSynonyms;
		this.ncitDefinitions = ncitDefinitions;
		this.sourceDefinitions = null;
	}

	public ConceptInVS(
		String ncitCode,
		String ncitPreferredTerm,
		String sourcePreferredTerm,
		ArrayList ncitSynonyms,
		ArrayList sourceSynonyms,
		ArrayList ncitDefinitions,
		ArrayList sourceDefinitions) {

		this.ncitCode = ncitCode;
		this.ncitPreferredTerm = ncitPreferredTerm;
		this.sourcePreferredTerm = sourcePreferredTerm;
		this.ncitSynonyms = ncitSynonyms;
		this.sourceSynonyms = sourceSynonyms;
		this.ncitDefinitions = ncitDefinitions;
		this.sourceDefinitions = sourceDefinitions;
	}

// Set methods
	public void setNcitCode(String ncitCode) {
		this.ncitCode = ncitCode;
	}

	public void setNcitPreferredTerm(String ncitPreferredTerm) {
		this.ncitPreferredTerm = ncitPreferredTerm;
	}

	public void setSourcePreferredTerm(String sourcePreferredTerm) {
		this.sourcePreferredTerm = sourcePreferredTerm;
	}

	public void setNcitSynonyms(ArrayList ncitSynonyms) {
		this.ncitSynonyms = ncitSynonyms;
	}

	public void setSourceSynonyms(ArrayList sourceSynonyms) {
		this.sourceSynonyms = sourceSynonyms;
	}

	public void setNcitDefinitions(ArrayList ncitDefinitions) {
		this.ncitDefinitions = ncitDefinitions;
	}

	public void setSourceDefinitions(ArrayList sourceDefinitions) {
		this.sourceDefinitions = sourceDefinitions;
	}


// Get methods
	public String getNcitCode() {
		return this.ncitCode;
	}

	public String getNcitPreferredTerm() {
		return this.ncitPreferredTerm;
	}

	public String getSourcePreferredTerm() {
		return this.sourcePreferredTerm;
	}

	public ArrayList getNcitSynonyms() {
		return this.ncitSynonyms;
	}

	public ArrayList getSourceSynonyms() {
		return this.sourceSynonyms;
	}

	public ArrayList getNcitDefinitions() {
		return this.ncitDefinitions;
	}

	public ArrayList getSourceDefinitions() {
		return this.sourceDefinitions;
	}

}
