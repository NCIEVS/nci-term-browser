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


public class TerminologyBean
{

// Variable declaration
	private String displayLabel;
	private String displayName;
	private String fullName;
	private String displayVersion;
	private String codingSchemeName;
	private String codingSchemeVersion;

// Default constructor
	public TerminologyBean() {
	}

// Constructor
	public TerminologyBean(
		String displayLabel,
		String displayName,
		String codingSchemeName,
		String codingSchemeVersion) {

		this.displayLabel = displayLabel;
		this.displayName = displayName;
		this.fullName = displayName;
		this.displayVersion = codingSchemeVersion;
		this.codingSchemeName = codingSchemeName;
		this.codingSchemeVersion = codingSchemeVersion;
	}


	public TerminologyBean(
		String displayLabel,
		String displayName,
		String fullName,
		String displayVersion,
		String codingSchemeName,
		String codingSchemeVersion) {

		this.displayLabel = displayLabel;
		this.displayName = displayName;
		this.fullName = fullName;
		this.displayVersion = displayVersion;
		this.codingSchemeName = codingSchemeName;
		this.codingSchemeVersion = codingSchemeVersion;
	}

// Set methods
	public void setDisplayLabel(String displayLabel) {
		this.displayLabel = displayLabel;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setDisplayVersion(String displayVersion) {
		this.displayVersion = displayVersion;
	}

	public void setCodingSchemeName(String codingSchemeName) {
		this.codingSchemeName = codingSchemeName;
	}

	public void setCodingSchemeVersion(String codingSchemeVersion) {
		this.codingSchemeVersion = codingSchemeVersion;
	}


// Get methods
	public String getDisplayLabel() {
		return this.displayLabel;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public String getFullName() {
		return this.fullName;
	}

	public String getDisplayVersion() {
		return this.displayVersion;
	}

	public String getCodingSchemeName() {
		return this.codingSchemeName;
	}

	public String getCodingSchemeVersion() {
		return this.codingSchemeVersion;
	}


	public String toString() {
         StringBuffer buf = new StringBuffer();
         buf.append(displayLabel);
         buf.append("\n\tdisplayName: " + displayName);
         buf.append("\n\tfullName: " + fullName);
         buf.append("\n\tdisplayVersion: " + displayVersion);
         buf.append("\n\tcodingSchemeName: " + codingSchemeName);
         buf.append("\n\tcodingSchemeVersion: " + codingSchemeVersion);
         return buf.toString();
	}

}
