package gov.nih.nci.evs.browser.bean;

import gov.nih.nci.evs.browser.utils.*;

import java.util.*;
import java.net.URI;
import java.io.*;


import javax.faces.context.*;
import javax.faces.event.*;
import javax.faces.model.*;
import javax.servlet.http.*;

import gov.nih.nci.evs.browser.utils.RemoteServerUtil;
import gov.nih.nci.evs.browser.properties.*;
import gov.nih.nci.evs.browser.common.*;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.naming.*;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.*;
import org.apache.logging.log4j.*;
import javax.faces.event.ValueChangeEvent;

//import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import javax.servlet.ServletOutputStream;
import org.LexGrid.concepts.*;
import org.lexgrid.valuesets.dto.ResolvedValueSetCodedNodeSet;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.concepts.Definition;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Presentation;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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

public class ValueSetBean {
	private static Logger _logger = LogManager.getLogger(ValueSetBean.class);
    private static List _rela_list = null;
    private static List _association_name_list = null;
    private static List _property_name_list = null;
    private static List _property_type_list = null;
    private static List _source_list = null;

    private static Vector _value_set_uri_vec = null;
    private static Vector _coding_scheme_vec = null;
    private static Vector _concept_domain_vec = null;

	private String selectedConceptDomain = null;
	private List conceptDomainList = null;
	private Vector<String> conceptDomainListData = null;
	//public static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    public ValueSetBean() {

	}

	public ValueSetSearchUtils createValueSetSearchUtils() {
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        ValueSetSearchUtils valueSetSearchUtils = new ValueSetSearchUtils(lbSvc);
        String serviceUrl = RemoteServerUtil.getServiceUrl();
        valueSetSearchUtils.setServiceUrl(serviceUrl);
        return valueSetSearchUtils;
	}

	public String getSelectedConceptDomain() {
		return this.selectedConceptDomain;
	}

	public void setSelectedConceptDomain(String selectedConceptDomain) {
		this.selectedConceptDomain = selectedConceptDomain;
	}

	public List getConceptDomainList() {
		if (conceptDomainList != null) return conceptDomainList;
		conceptDomainListData = DataUtils.getConceptDomainNames();
		conceptDomainList = new ArrayList();
		for (int i=0; i<conceptDomainListData.size(); i++) {
			String t = (String) conceptDomainListData.elementAt(i);
			conceptDomainList.add(new SelectItem(t));
		}

		if (conceptDomainList.size() > 0) {
			setSelectedConceptDomain(((SelectItem) conceptDomainList.get(0)).getLabel());
		}
		return conceptDomainList;
	}




	private String selectedOntology = null;
	private static List ontologyList = null;
	private Vector<String> ontologyListData = null;


	public String getSelectedOntology() {
		return this.selectedOntology;
	}

	public void setSelectedOntology(String selectedOntology) {
		this.selectedOntology = selectedOntology;
	}


	public List getOntologyList() {
		if (ontologyList != null) return ontologyList;
		Vector v = new Vector();
		//HashSet hset = new HashSet();
		ontologyList = new ArrayList();

/*
		Vector vsd_vec = DataUtils.getValueSetDefinitionMetadata();
		HashMap csURN2ValueSetMetadataHashMap = DataUtils.getCodingSchemeURN2ValueSetMetadataHashMap(vsd_vec);
		Iterator it = csURN2ValueSetMetadataHashMap.keySet().iterator();
        if (it == null) return null;
        while (it.hasNext()) {
           String cs = (String) it.next();
           if (!hset.contains(cs)) {
		   	   v.add(cs);
			   hset.add(cs);
		   }
	    }
*/

        HashSet hset = DataUtils.getValueSetHierarchy().get_valueSetParticipationHashSet();
        //HashSet hset = new DataUtils().get_valueSetParticipationHashSet();

        if (hset == null) return null;
        Iterator it = hset.iterator();
        if (it == null) return null;
        while (it.hasNext()) {
			String cs = (String) it.next();
			String formalName = DataUtils.getFormalName(cs);
			String codingSchemeName = DataUtils.uri2CodingSchemeName(formalName);
			v.add(codingSchemeName);
		}

		v = new SortUtils().quickSort(v);

		String display_name = "ALL";
		ontologyList.add(new SelectItem(display_name));

		for (int j = 0; j < v.size(); j++) {
            display_name = (String) v.elementAt(j);
			ontologyList.add(new SelectItem(display_name));
		}

		if (ontologyList.size() > 0) {
			setSelectedOntology(((SelectItem) ontologyList.get(0)).getLabel());
		}
		return ontologyList;

	}


	private String selectedValueSetURI = null;
	private List valueSetURIList = null;
	private Vector<String> valueSetURIListData = null;


	public String getSelectedValueSetURI() {
		return this.selectedValueSetURI;
	}

	public void setSelectedValueSetURI(String selectedValueSetURI) {
		this.selectedValueSetURI = selectedValueSetURI;
	}

	public List getValueSetURIList() {
		if (valueSetURIList != null) return valueSetURIList;
		valueSetURIListData = DataUtils.getValueSetURIs();

		valueSetURIList = new ArrayList();
		for (int i=0; i<valueSetURIListData.size(); i++) {
			String t = (String) valueSetURIListData.elementAt(i);
			valueSetURIList.add(new SelectItem(t));
		}

		if (valueSetURIList.size() > 0) {
			setSelectedValueSetURI(((SelectItem) valueSetURIList.get(0)).getLabel());
		}
		return valueSetURIList;
	}


	public void selectValueSetSearchOptionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("selectValueSetSearchOption", newValue);
	}

    public void valueSetURIChangedEvent(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();
        setSelectedValueSetURI(newValue);
	}

    public void ontologyChangedEvent(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();
        setSelectedOntology(newValue);
	}

    public void conceptDomainChangedEvent(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();
        setSelectedConceptDomain(newValue);
	}


    public String valueSetSearchRedirect(HttpServletRequest request, HttpServletResponse response, String vsduri) {
		try {
			request.getRequestDispatcher("/ncitbrowser/ajax?action=create_src_vs_tree&vsd_uri=" + vsduri).
							  forward(request,response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


    public String selectCSVersionAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        String selectedvalueset = null;
        String uri = null;
        String multiplematches = HTTPUtils.cleanXSS((String) request.getParameter("multiplematches"));
        if (multiplematches != null) {
			selectedvalueset = HTTPUtils.cleanXSS((String) request.getParameter("valueset"));
			uri = selectedvalueset;
		} else {
			uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
			if (uri.indexOf("|") != -1) {
				Vector u = DataUtils.parseData(uri);
				uri = (String) u.elementAt(1);
			}
		}

		request.getSession().setAttribute("vsd_uri", uri);

        return "select_value_set";
	}


    public String downloadValueSetAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();

		String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
		try {
			request.getRequestDispatcher("/ncitbrowser/pages/download_value_set.jsf?vsd_uri=" + vsd_uri).
							  forward(request,response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
    }

    //[NCITERM-723] Resolved value set contains anonymous classes.
    public String resolveValueSetAction() {
		ValueSetDefUtils vsdu = null;
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
		vsdu = new ValueSetDefUtils(lbSvc, vsd_service);

        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        String selectedvalueset = null;
        String multiplematches = HTTPUtils.cleanXSS((String) request.getParameter("multiplematches"));
        if (multiplematches != null) {
			selectedvalueset = HTTPUtils.cleanXSS((String) request.getParameter("valueset"));
		} else {
			selectedvalueset = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
			if (selectedvalueset != null && selectedvalueset.indexOf("|") != -1) {
				Vector u = StringUtils.parseData(selectedvalueset);
				selectedvalueset = (String) u.elementAt(1);
			}
	    }
        String vsd_uri = selectedvalueset;
		request.getSession().setAttribute("selectedvalueset", selectedvalueset);
		String key = vsd_uri;

        request.getSession().setAttribute("vsd_uri", vsd_uri);
        String[] coding_scheme_ref = null;

        Vector w = vsdu.getCodingSchemeReferencesInValueSetDefinition(vsd_uri, "PRODUCTION");
        if (w != null) {
			coding_scheme_ref = new String[w.size()];
			for (int i=0; i<w.size(); i++) {
				String s = (String) w.elementAt(i);
				coding_scheme_ref[i] = s;
			}
		}

        if (coding_scheme_ref == null || coding_scheme_ref.length == 0) {
			String msg = "No PRODUCTION version of coding scheme is available.";
			request.getSession().setAttribute("message", msg);
			return "resolve_value_set";
		}

		AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
        StringBuffer buf = new StringBuffer();

        for (int i=0; i<coding_scheme_ref.length; i++) {
			String t = coding_scheme_ref[i];

			String delim = "|";
			if (t.indexOf("|") == -1) {
				delim = "$";
			}
			Vector u = StringUtils.parseData(t, delim);
			String uri = (String) u.elementAt(0);
			String version = (String) u.elementAt(1);
			if (version == null) {
				version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(uri, Constants.PRODUCTION);
			}
			buf.append("|" + uri + "$" + version);
            csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference(uri, version));
		}
		key = key + buf.toString();
        request.getSession().setAttribute("coding_scheme_ref", coding_scheme_ref);
		try {
            CodingSchemeDataUtils codingSchemeDataUtils = new CodingSchemeDataUtils(lbSvc);

			//ResolvedConceptReferencesIterator itr = codingSchemeDataUtils.resolveCodingScheme(vsd_uri, null, false);
/*
    public ResolvedConceptReferencesIterator resolveValueSet(
		String serviceUrl, String defaultCodingScheme, String vsd_uri, String version, boolean resolveObjects) {
*/

        ValueSetMetadataUtils vsmdu = new ValueSetMetadataUtils(vsd_service);
		String metadata = vsmdu.getValueSetDefinitionMetadata(vsd_uri);
		Vector u = StringUtils.parseData(metadata);
		String defaultCodingScheme = (String) u.elementAt(6);
		if (defaultCodingScheme.compareTo("ncit") == 0) {
			defaultCodingScheme = "NCI_Thesaurus";
		}

    ResolvedConceptReferencesIterator itr = codingSchemeDataUtils.resolveValueSet(RemoteServerUtil.getServiceUrl(),
            defaultCodingScheme, vsd_uri, null, false);

			try {
				int numberRemaining = itr.numberRemaining();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			IteratorBeanManager iteratorBeanManager = (IteratorBeanManager) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get("iteratorBeanManager");

			if (iteratorBeanManager == null) {
				iteratorBeanManager = new IteratorBeanManager();
				request.getSession().setAttribute("iteratorBeanManager", iteratorBeanManager);
			}

			request.getSession().setAttribute("ResolvedConceptReferencesIterator", itr);

			IteratorBean iteratorBean = iteratorBeanManager.getIteratorBean(key);
			if (iteratorBean == null) {
				iteratorBean = new IteratorBean(itr);
				iteratorBean.initialize();
				iteratorBean.setKey(key);
				iteratorBeanManager.addIteratorBean(iteratorBean);
			}
			request.getSession().setAttribute("coding_scheme_ref", coding_scheme_ref);
			request.getSession().setAttribute("ResolvedConceptReferencesIterator", itr);
			request.getSession().setAttribute("resolved_vs_key", key);
/*
			try {
				int numberRemaining = itr.numberRemaining();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
*/
			return "resolved_value_set";

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String msg = "Unable to resolve the value set " + vsd_uri;
		request.getSession().setAttribute("message", msg);
        return "resolved_value_set";
	}

    // radio button implementation
    public String continueResolveValueSetAction() {
		ValueSetDefUtils vsdu = null;
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
		vsdu = new ValueSetDefUtils(lbSvc, vsd_service);

        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        String version_selection = HTTPUtils.cleanXSS((String) request.getParameter("version_selection"));
        String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
        if (vsd_uri == null) {
			vsd_uri = (String) request.getSession().getAttribute("vsd_uri");
		}

        request.getSession().setAttribute("vsd_uri", vsd_uri);
		Vector coding_scheme_ref_vec = vsdu.getCodingSchemeReferencesInValueSetDefinition(vsd_uri);
		if (coding_scheme_ref_vec == null) {
					String msg = "WARNING: No coding scheme version reference is available.";
					request.getSession().setAttribute("message", msg);
					return "resolve_value_set";
		}

		Vector cs_name_vec = vsdu.getCodingSchemeURNsInValueSetDefinition(vsd_uri);
		AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
		Vector ref_vec = new Vector();

		String key = vsd_uri;
		StringBuffer buf = new StringBuffer();

        for (int i=0; i<cs_name_vec.size(); i++) {
			String cs_name = (String) cs_name_vec.elementAt(i);
			String version = HTTPUtils.cleanXSS((String) request.getParameter(cs_name));
			if (version != null) {
				csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference(cs_name, version));
				ref_vec.add(cs_name + "$" + version);
				//key = key + "|" + cs_name + "$" + version;
				buf.append("|" + cs_name + "$" + version);
		    }
		}
		key = key + buf.toString();
		ResolvedValueSetDefinition rvsd = null;
		String valueSetDefinitionRevisionId = null;
		int lcv = 0;
		try {
			ResolvedValueSetCodedNodeSet rvs_cns = null;
			String csVersionTag = null;
			rvs_cns = vsd_service.getCodedNodeSetForValueSetDefinition(new URI(vsd_uri),
																		valueSetDefinitionRevisionId,
																		csvList,
																		csVersionTag);
			CodedNodeSet cns = rvs_cns.getCodedNodeSet();
			SortOptionList sortOptions = null;
			LocalNameList filterOptions = null;
			LocalNameList propertyNames = null;//new LocalNameList();
			CodedNodeSet.PropertyType[] propertyTypes = null;
			boolean resolveObjects = false;
			ResolvedConceptReferencesIterator itr = null;
			itr = cns.resolve(sortOptions, filterOptions, propertyNames, propertyTypes, resolveObjects);

			IteratorBeanManager iteratorBeanManager = (IteratorBeanManager) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get("iteratorBeanManager");

			if (iteratorBeanManager == null) {
				iteratorBeanManager = new IteratorBeanManager();
				request.getSession().setAttribute("iteratorBeanManager", iteratorBeanManager);
			}

			request.getSession().setAttribute("ResolvedConceptReferencesIterator", itr);

			IteratorBean iteratorBean = iteratorBeanManager.getIteratorBean(key);
			if (iteratorBean == null) {
				iteratorBean = new IteratorBean(itr);
				iteratorBean.initialize();
				iteratorBean.setKey(key);
				iteratorBeanManager.addIteratorBean(iteratorBean);
			}

			String[] coding_scheme_ref = new String[ref_vec.size()];
			for (int j=0; j<ref_vec.size(); j++) {
				coding_scheme_ref[j] = (String) ref_vec.elementAt(j);
			}
            request.getSession().setAttribute("coding_scheme_ref", coding_scheme_ref);
            request.getSession().setAttribute("resolved_vs_key", key);
            request.getSession().setAttribute("version_selection", "true");
			return "resolved_value_set";
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String msg = "Unable to resolve the value set " + vsd_uri;
		request.getSession().setAttribute("message", msg);
        return "resolved_value_set";
	}


    public String exportValueSetAction() {
		/*
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        */

        return "exported_value_set";

	}


    public String valueSetDefinition2XMLString(String uri) {
        LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
        String s = null;
        String valueSetDefinitionRevisionId = null;
        try {
			URI valueSetDefinitionURI = new URI(uri);
			StringBuffer buf = vsd_service.exportValueSetDefinition(valueSetDefinitionURI, valueSetDefinitionRevisionId);
            s = buf.toString();
        } catch (Exception ex) {
           ex.printStackTrace();
        }
		return s;
	}



    public void exportVSDToXMLAction() {

        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();


        String selectedvalueset = null;
        String multiplematches = HTTPUtils.cleanXSS((String) request.getParameter("multiplematches"));
        if (multiplematches != null) {
			selectedvalueset = HTTPUtils.cleanXSS((String) request.getParameter("valueset"));
		} else {
			selectedvalueset = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
			if (selectedvalueset != null && selectedvalueset.indexOf("|") != -1) {
				Vector u = DataUtils.parseData(selectedvalueset);
				selectedvalueset = (String) u.elementAt(1);
			}
	    }
        String uri = selectedvalueset;
		request.getSession().setAttribute("selectedvalueset", uri);

        String xml_str = valueSetDefinition2XMLString(uri);

		try {
			HttpServletResponse response = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();
			response.setContentType("text/xml");

			String vsd_name = DataUtils.valueSetDefinitionURI2Name(uri);
			vsd_name = vsd_name.replaceAll(" ", "_");
			vsd_name = vsd_name + ".xml";

		    response.setHeader("Content-Disposition", "attachment; filename="
					+ vsd_name);

			response.setContentLength(xml_str.length());

			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(xml_str.getBytes("UTF8"), 0, xml_str.length());
			ouputStream.flush();
			ouputStream.close();

		} catch(IOException e) {
			e.printStackTrace();
		}

		FacesContext.getCurrentInstance().responseComplete();
	}


    public void exportToXMLAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

		String valueSetDefinitionRevisionId = null;
		AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
        String from_download = HTTPUtils.cleanXSS((String) request.getParameter("from_download"));
        String uri = null;
        if (from_download != null && from_download.compareTo("true") == 0) {
			uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
			if (uri == null) {
				uri = (String) request.getSession().getAttribute("vsd_uri");
			}

			Vector cs_vec = DataUtils.getCodingSchemeURNsInValueSetDefinition(uri);
			if (cs_vec == null) return;
			for (int k=0; k<cs_vec.size(); k++) {
				String cs_urn = (String) cs_vec.elementAt(k);
				String version = DataUtils.getVocabularyVersionByTag(cs_urn, "PRODUCTION");
                csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference(cs_urn, version));
			}
			//Vector u = DataUtils.getResovedValueSetVersions(uri);
			//String version = (String) u.elementAt(0);
			//csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference(uri, version));
		} else {
			String[] coding_scheme_ref = (String[]) request.getSession().getAttribute("coding_scheme_ref");
			uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
			if (uri == null) {
				uri = (String) request.getSession().getAttribute("vsd_uri");
			}

			if (coding_scheme_ref == null || coding_scheme_ref.length == 0) {
				String msg = "No coding scheme reference is selected.";
				request.getSession().setAttribute("message", msg);
				return;// "resolve_value_set";
			}

			for (int i=0; i<coding_scheme_ref.length; i++) {
				String t = coding_scheme_ref[i];
				String delim = "$";
				if (t.indexOf("$") == -1) delim = "|";
				Vector u = DataUtils.parseData(t, delim);
				String url = (String) u.elementAt(0);
				String version = (String) u.elementAt(1);
				csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference(url, version));
			}
	    }

		String csVersionTag = null;
		boolean failOnAllErrors = false;

        try {
        	LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
			InputStream reader =  vsd_service.exportValueSetResolution(new URI(uri), valueSetDefinitionRevisionId,
			   csvList, csVersionTag, failOnAllErrors);

			if (reader != null) {
				StringBuffer sb = new StringBuffer();
				try {
					for(int c = reader.read(); c != -1; c = reader.read()) {
						sb.append((char)c);
					}

					HttpServletResponse response = (HttpServletResponse) FacesContext
							.getCurrentInstance().getExternalContext().getResponse();
					response.setContentType("text/xml");

					String vsd_name = DataUtils.valueSetDefinitionURI2Name(uri);
					vsd_name = vsd_name.replaceAll(" ", "_");
					vsd_name = "resolved_" + vsd_name + ".xml";

					response.setHeader("Content-Disposition", "attachment; filename="
							+ vsd_name);


					response.setContentLength(sb.length());
					ServletOutputStream ouputStream = response.getOutputStream();
					ouputStream.write(sb.toString().getBytes("UTF8"), 0, sb.length());
					ouputStream.flush();
					ouputStream.close();

				} catch(IOException e) {
					throw e;
				} finally {
					try {
						reader.close();
					} catch(Exception e) {
						// ignored
					}
			    }
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

        FacesContext.getCurrentInstance().responseComplete();
	}


    public String getNCIDefinition(ResolvedConceptReference ref) {
		if (ref == null) return null;
		Entity concept = ref.getReferencedEntry();
		if (concept == null) return null;
		Definition[] definitions = concept.getDefinition();
		Vector v = new Vector();
		if (definitions == null) return null;
		for (int i=0; i<definitions.length; i++) {
			Definition definition = definitions[i];
			Source[] sources = definition.getSource();
			if (sources == null) return null;
			for (int j=0; j<sources.length; j++)
			{
				Source src = sources[j];
				String src_name = src.getContent();
				if (src_name == null) return null;
				if (src_name.compareTo("NCI") == 0) {
					v.add(definition.getValue().getContent());
				}
			}

			PropertyQualifier[] qualifiers = definition.getPropertyQualifier();
			if (qualifiers == null) return null;
			for (int j=0; j<qualifiers.length; j++)
			{
				String qualifier_value = qualifiers[j].getValue().getContent();
				if (qualifier_value == null) return null;
				if (qualifier_value.compareTo("NCI") == 0) {
					v.add(definition.getValue().getContent());
				}
			}
		}
		if (v.size() == 0) return null;
		if (v.size() == 1) return (String) v.elementAt(0);

		String def_str = "";
		for (int i=0; i<v.size(); i++) {
			String def = (String) v.elementAt(i);
			if (i == 0) {
				def_str = def;
			} else {
				def_str = def_str + "|" + def;
			}
		}
        return def_str;
	}


    public void exportToCSVAction() {

		System.out.println("ValueSetBean exportToCSVAction");

        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

		String vsd_uri = (String) request.getSession().getAttribute("vsd_uri");
		if (!DataUtils.isNull(vsd_uri)) {

			ValueSetConfig vsc = ValueSetDefinitionConfig.getValueSetConfig(vsd_uri);
			String filename = vsc.getReportURI();

			if (filename != null) {
				filename = filename.trim();
			}
			System.out.println("ValueSetBean Released file: " + filename);

			if (!DataUtils.isNull(filename) && filename.length() > 0) {
				//ResolvedValueSetIteratorHolder rvsi = constructResolvedValueSetIteratorHolder(vsd_uri);
				//request.getSession().setAttribute("rvsi", rvsi);
				ResolvedValueSetIteratorHolder rvsi = (ResolvedValueSetIteratorHolder) request.getSession().getAttribute("rvsi");
				Vector rvs_content_vec = rvsi.extractRawDataFromTableContent();
				StringBuffer sb = new StringBuffer();
				for (int k=0; k<rvs_content_vec.size(); k++) {
					String line = (String) rvs_content_vec.elementAt(k);
					sb.append(line);
					if (k < rvs_content_vec.size()-1) {
						sb.append("\r\n");
					}
				}
				String content = sb.toString();

				//vsd_uri = DataUtils.valueSetDefinitionURI2Name(vsd_uri);
				vsd_uri = vsd_uri.replaceAll(" ", "_");
				vsd_uri = "resolved_" + vsd_uri + ".csv";

				HttpServletResponse response = (HttpServletResponse) FacesContext
						.getCurrentInstance().getExternalContext().getResponse();
				response.setContentType("text/csv");
				response.setHeader("Content-Disposition", "attachment; filename="
						+ vsd_uri);

				response.setContentLength(content.length());

				try {
					ServletOutputStream ouputStream = response.getOutputStream();
					ouputStream.write(content.getBytes("UTF8"), 0, content.length());
					ouputStream.flush();
					ouputStream.close();
				} catch (Exception ex) {
					ex.printStackTrace();
					//sb.append("WARNING: Export to CVS action failed.");
				}
				FacesContext.getCurrentInstance().responseComplete();
				return;
			}
		}


		String version_selection = (String) HTTPUtils.cleanXSS((String) request.getParameter("version_selection"));
		String valueSetDefinitionRevisionId = null;
		AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();

        String from_download = HTTPUtils.cleanXSS((String) request.getParameter("from_download"));
        //String vsd_uri = null;
        StringBuffer sb = new StringBuffer();


        if (from_download != null && from_download.compareTo("true") == 0) {

			System.out.println("from_download: TRUE");

			/*
			vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
			String heading = "NCIt Concept Code	Source Name	NCIt Preferred Term	NCIt Synonyms	Source Definition	NCIt Definition";
			Vector fields = StringUtils.parseData(heading, '\t');
			String serviceUrl = RemoteServerUtil.getServiceUrl();
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
			CodingSchemeDataUtils codingSchemeDataUtils = new CodingSchemeDataUtils(lbSvc);
			String version = codingSchemeDataUtils.getVocabularyVersionByTag(Constants.NCI_THESAURUS, Constants.PRODUCTION);
			ValueSetFormatter formatter = new ValueSetFormatter(serviceUrl, lbSvc, vsd_service);
			Vector vs_data = formatter.export(vsd_uri, version, fields);
			Vector v = gov.nih.nci.evs.browser.utils.StringUtils.convertDelimited2CSV(vs_data, '|');

			for (int i=0; i<v.size(); i++) {
				String line = (String) v.elementAt(i);
				sb.append(line);
				if (i<v.size()-1) {
					sb.append("\r\n");
				}
			}
			*/

			vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
			if (vsd_uri == null) {
				vsd_uri = (String) request.getSession().getAttribute("vsd_uri");
			}
			ResolvedValueSetIteratorHolder rvsi = (ResolvedValueSetIteratorHolder) request.getSession().getAttribute("rvsi");
			if (rvsi != null) {
				Vector w = rvsi.extractRawDataFromTableContent();
				if (w != null) {
					try {
						w = rvsi.tableContent2CSV(w);
						for (int k=0; k<w.size(); k++) {
							String t = (String) w.elementAt(k);
							sb.append(t);
							sb.append("\n");
						}
					} catch (Exception ex) {
                        ex.printStackTrace();
					}
				}
			}

		} else {

			System.out.println("from_download: FALSE");

			String scheme_version = null;
			String[] coding_scheme_ref = (String[]) request.getSession().getAttribute("coding_scheme_ref");
			vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
			if (vsd_uri == null) {
				vsd_uri = (String) request.getSession().getAttribute("vsd_uri");
			}

			if (coding_scheme_ref == null || coding_scheme_ref.length == 0) {
				String msg = "No coding scheme reference is selected.";
				request.getSession().setAttribute("message", msg);
				return;// "resolve_value_set";
			}

			for (int i=0; i<coding_scheme_ref.length; i++) {
				String t = coding_scheme_ref[i];
				String delim = "$";
				if (t.indexOf("$") == -1) delim = "|";
				Vector u = DataUtils.parseData(t, delim);
				String url = (String) u.elementAt(0);
				String version = (String) u.elementAt(1);
				scheme_version = version;
				csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference(url, version));
			}
			String csVersionTag = null;
			boolean failOnAllErrors = false;
            if (version_selection == null) {
				try {
					LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
					CodingSchemeDataUtils codingSchemeDataUtils = new CodingSchemeDataUtils(lbSvc);
					CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();

					LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
					ValueSetMetadataUtils vsmdu = new ValueSetMetadataUtils(vsd_service);
					String metadata = vsmdu.getValueSetDefinitionMetadata(vsd_uri);
					Vector u = StringUtils.parseData(metadata);
					String defaultCodingScheme = (String) u.elementAt(6);
					if (defaultCodingScheme.compareTo("ncit") == 0) {
						defaultCodingScheme = "NCI_Thesaurus";
					}

					CodingSchemeDataUtils csdu = new CodingSchemeDataUtils(lbSvc);
					String version = csdu.getVocabularyVersionByTag(defaultCodingScheme, Constants.PRODUCTION);
					versionOrTag.setVersion(version);

/////////////////////////////////////////////////////////////////////////////////////////////////////////
                    String serviceUrl = RemoteServerUtil.getServiceUrl();
					AssertedValueSetUtils avsu = new AssertedValueSetUtils(serviceUrl, lbSvc);
					ResolvedConceptReferencesIterator iterator2 = null;
					iterator2 = avsu.getValueSetIteratorForURI(vsd_uri);
					CodedNodeSet cns = codingSchemeDataUtils.getNodeSet(defaultCodingScheme, versionOrTag);
					ConceptReferenceList codeList = csdu.iterator2List(iterator2);
					cns = cns.restrictToCodes(codeList);
					SortOptionList sortOptions = null;
					LocalNameList filterOptions = null;
					LocalNameList propertyNames = null;//new LocalNameList();
					CodedNodeSet.PropertyType[] propertyTypes = new CodedNodeSet.PropertyType[1];
					propertyTypes[0] = CodedNodeSet.PropertyType.DEFINITION;
					boolean resolveObjects = true;
					ResolvedConceptReferencesIterator itr = null;
					itr = cns.resolve(sortOptions, filterOptions, propertyNames, propertyTypes, resolveObjects);

					sb.append("Code,");
					sb.append("Name,");
					sb.append("Terminology,");
					sb.append("Version,");
					sb.append("Namespace,");
					sb.append("Definition");
					sb.append("\r\n");

					while (itr != null && itr.hasNext()) {
						ResolvedConceptReference[] refs = itr.next(100).getResolvedConceptReference();
						for (ResolvedConceptReference ref : refs) {
							String entityDescription = "<NOT ASSIGNED>";
							if (ref.getEntityDescription() != null) {
								entityDescription = ref.getEntityDescription().getContent();
							}

							sb.append("\"" + ref.getConceptCode() + "\",");
							sb.append("\"" + entityDescription + "\",");
							sb.append("\"" + ref.getCodingSchemeName() + "\",");
							sb.append("\"" + ref.getCodingSchemeVersion() + "\",");
							sb.append("\"" + ref.getCodeNamespace() + "\",");

							String definition = getNCIDefinition(ref);
							if (definition == null) definition = "";
							sb.append("\"" + definition + "\"");
							sb.append("\r\n");
						}
					}

				} catch (Exception ex)	{
					sb.append("WARNING: Export to CVS action failed.");
					ex.printStackTrace();
				}
            } else {
				try {
					LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();

					ResolvedValueSetCodedNodeSet rvs_cns = null;
					rvs_cns = vsd_service.getCodedNodeSetForValueSetDefinition(new URI(vsd_uri),
																				valueSetDefinitionRevisionId,
																				csvList,
																				csVersionTag);
					CodedNodeSet cns = rvs_cns.getCodedNodeSet();
					SortOptionList sortOptions = null;
					LocalNameList filterOptions = null;
					LocalNameList propertyNames = null;//new LocalNameList();
					CodedNodeSet.PropertyType[] propertyTypes = new CodedNodeSet.PropertyType[1];
					propertyTypes[0] = CodedNodeSet.PropertyType.DEFINITION;
					boolean resolveObjects = true;
					ResolvedConceptReferencesIterator itr = null;
					itr = cns.resolve(sortOptions, filterOptions, propertyNames, propertyTypes, resolveObjects);

					sb.append("Code,");
					sb.append("Name,");
					sb.append("Terminology,");
					sb.append("Version,");
					sb.append("Namespace,");
					sb.append("Definition");
					sb.append("\r\n");

					while (itr != null && itr.hasNext()) {
						ResolvedConceptReference[] refs = itr.next(100).getResolvedConceptReference();
						for (ResolvedConceptReference ref : refs) {
							String entityDescription = "<NOT ASSIGNED>";
							if (ref.getEntityDescription() != null) {
								entityDescription = ref.getEntityDescription().getContent();
							}

							sb.append("\"" + ref.getConceptCode() + "\",");
							sb.append("\"" + entityDescription + "\",");
							sb.append("\"" + ref.getCodingSchemeName() + "\",");
							sb.append("\"" + ref.getCodingSchemeVersion() + "\",");
							sb.append("\"" + ref.getCodeNamespace() + "\",");

							String definition = getNCIDefinition(ref);
							if (definition == null) definition = "";
							sb.append("\"" + definition + "\"");
							sb.append("\r\n");
						}
					}

				} catch (Exception ex)	{
					sb.append("WARNING: Export to CVS action failed.");
					ex.printStackTrace();
				}
		    }
		}

		vsd_uri = DataUtils.valueSetDefinitionURI2Name(vsd_uri);
		vsd_uri = vsd_uri.replaceAll(" ", "_");
		vsd_uri = "resolved_" + vsd_uri + ".csv";

		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ vsd_uri);

		response.setContentLength(sb.length());

		try {
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(sb.toString().getBytes("UTF8"), 0, sb.length());
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			sb.append("WARNING: Export to CVS action failed.");
		}
		FacesContext.getCurrentInstance().responseComplete();
	}


	public String searchAction() {
    	return "search_results";
	}


    public String resolvedValueSetSearchAction() {
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();

        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
        if (vsd_uri == null) {
			vsd_uri = (String) request.getSession().getAttribute("vsd_uri");
		}
        request.getSession().setAttribute("nav_type", "valuesets");
        request.getSession().setAttribute("vsd_uri", vsd_uri);
        String matchText = HTTPUtils.cleanXSS((String) request.getParameter("matchText"));
        request.getSession().setAttribute("matchText_RVS", matchText);

        if (matchText != null)
            matchText = matchText.trim();

        // [#19965] Error message is not displayed when Search Criteria is not
        // proivded
        if (matchText == null || matchText.length() == 0) {
            String message = "Please enter a search string.";
            request.getSession().setAttribute("message", message);
            // request.getSession().removeAttribute("matchText");
            request.removeAttribute("matchText");
            return "message";
        }
        request.getSession().setAttribute("matchText", matchText);

        String matchAlgorithm = HTTPUtils.cleanXSS((String) request.getParameter("algorithm"));
        String searchTarget = HTTPUtils.cleanXSS((String) request.getParameter("searchTarget"));

        request.getSession().setAttribute("searchTarget", searchTarget);
        request.getSession().setAttribute("algorithm", matchAlgorithm);
        request.getSession().setAttribute("valueset_search_algorithm", matchAlgorithm);

        IteratorBeanManager iteratorBeanManager =
            (IteratorBeanManager) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap()
                .get("iteratorBeanManager");

        if (iteratorBeanManager == null) {
            iteratorBeanManager = new IteratorBeanManager();
            FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap()
                .put("iteratorBeanManager", iteratorBeanManager);
        }

        IteratorBean iteratorBean = null;
        ResolvedConceptReferencesIterator iterator = null;

        Vector schemes = new Vector();
        schemes.add(vsd_uri);

        int maxToReturn = -1;
        ValueSetSearchUtils vssu = createValueSetSearchUtils();
		AbsoluteCodingSchemeVersionReferenceList csVersinList
		   = vssu.getDefaultAbsoluteCodingSchemeVersionReferenceList(vsd_uri);

        String key =
            iteratorBeanManager.createIteratorKey(schemes, matchText,
                searchTarget, matchAlgorithm, maxToReturn);
        if (searchTarget.compareTo("codes") == 0) {
            if (iteratorBeanManager.containsIteratorBean(key)) {
                iteratorBean = iteratorBeanManager.getIteratorBean(key);
                iterator = iteratorBean.getIterator();
            } else {

                iterator =
					vssu.searchByCode(
				        vsd_uri, csVersinList, matchText, maxToReturn);

                    if (iterator != null) {
                        iteratorBean = new IteratorBean(iterator);
                        iteratorBean.setKey(key);
                        iteratorBeanManager.addIteratorBean(iteratorBean);
                    }
            }

        } else if (searchTarget.compareTo("names") == 0) {
            if (iteratorBeanManager.containsIteratorBean(key)) {
                iteratorBean = iteratorBeanManager.getIteratorBean(key);
                iterator = iteratorBean.getIterator();
            } else {
                iterator =
					vssu.searchByName(
				        vsd_uri, csVersinList, matchText, matchAlgorithm, maxToReturn);

                if (iterator != null) {
                    //iterator = wrapper.getIterator();
                    //if (iterator != null) {
                        iteratorBean = new IteratorBean(iterator);
                        iteratorBean.setKey(key);
                        iteratorBeanManager.addIteratorBean(iteratorBean);
                    //}
                }
            }

        } else if (searchTarget.compareTo("properties") == 0) {
            if (iteratorBeanManager.containsIteratorBean(key)) {
                iteratorBean = iteratorBeanManager.getIteratorBean(key);
                iterator = iteratorBean.getIterator();
            } else {
				boolean excludeDesignation = true;
                iterator =
					createValueSetSearchUtils().searchByProperties(
				        vsd_uri, matchText, excludeDesignation, matchAlgorithm, maxToReturn);

                if (iterator != null) {
                    //iterator = wrapper.getIterator();
                    //if (iterator != null) {
                        iteratorBean = new IteratorBean(iterator);
                        iteratorBean.setKey(key);
                        iteratorBeanManager.addIteratorBean(iteratorBean);
                    //}
                }
            }

        }

		request.getSession().setAttribute("key", key);
        if (iterator != null) {
            request.getSession().setAttribute("key", key);
            int numberRemaining = 0;
            try {
                numberRemaining = iterator.numberRemaining();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            int size = iteratorBean.getSize();
            if (size > 0) {
                //request.getSession().setAttribute("search_results", v);
                String match_size = Integer.toString(size);
                request.getSession().setAttribute("match_size", match_size);
                request.getSession().setAttribute("page_string", "1");
                request.getSession().setAttribute("vsd_uri", vsd_uri);
                return "search_results";
			}
        }

        String message = "No match found.";
// [NCITERM-613] Remove the minimum 3-character search string length restriction on all name searches.
//       int minimumSearchStringLength =
//           NCItBrowserProperties.getMinimumSearchStringLength();

		if (matchAlgorithm.compareTo(Constants.EXACT_SEARCH_ALGORITHM) == 0) {
			String t = searchTarget.toLowerCase(Locale.ENGLISH);
			if (t == null) return null;
			if (t.indexOf("code") != -1) {
				message = Constants.ERROR_NO_MATCH_FOUND_CODE_IS_CASESENSITIVE;
			} else {
				message = Constants.ERROR_NO_MATCH_FOUND_TRY_OTHER_ALGORITHMS;
			}
		}


//        else if (matchAlgorithm.compareTo(Constants.STARTWITH_SEARCH_ALGORITHM) == 0
//            && matchText.length() < minimumSearchStringLength) {
//            message = Constants.ERROR_ENCOUNTERED_TRY_NARROW_QUERY;
//        }


        request.getSession().setAttribute("message", message);
        return "message";
    }


    public String valueSetSearchAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        request.getSession().removeAttribute("error_msg");
        boolean retval = HTTPUtils.validateRequestParameters(request);
        if (!retval) {
			return "invalid_parameter";
		}
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();

		java.lang.String valueSetDefinitionRevisionId = null;
		String msg = null;

        String selectValueSetSearchOption = HTTPUtils.cleanXSS((String) request.getParameter("searchTarget"));
        if (DataUtils.isNull(selectValueSetSearchOption)) {
			selectValueSetSearchOption = "Name";
		}
		request.getSession().setAttribute("selectValueSetSearchOption", selectValueSetSearchOption);

        String algorithm = HTTPUtils.cleanXSS((String) request.getParameter("algorithm"));
        if (DataUtils.isNull(algorithm)) {
			algorithm = "exactMatch";
		}
        request.getSession().setAttribute("valueset_search_algorithm", algorithm);
		String checked_vocabularies = HTTPUtils.cleanXSS((String) request.getParameter("checked_vocabularies"));
		String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
		if (checked_vocabularies != null && checked_vocabularies.compareTo("") == 0) {
			msg = "No value set definition is selected.";
			request.getSession().setAttribute("message", msg);
			return "message";
		}

		//Vector selected_vocabularies = DataUtils.parseData(checked_vocabularies, ",");
        String VSD_view = HTTPUtils.cleanXSS((String) request.getParameter("view"));
        request.getSession().setAttribute("view", VSD_view);
        String matchText = HTTPUtils.cleanXSS((String) request.getParameter("matchText"));
        if (matchText != null) {
			matchText = matchText.trim();
			request.getSession().setAttribute("matchText", matchText);
		}

        int searchOption = SimpleSearchUtils.BY_CODE;
        if (selectValueSetSearchOption.compareTo("Name") == 0 || selectValueSetSearchOption.compareTo("names") == 0) {
			searchOption = SimpleSearchUtils.BY_NAME;
		}
		request.getSession().setAttribute("checked_vocabularies", checked_vocabularies);

		if (checked_vocabularies == null) {
			checked_vocabularies = vsd_uri;
		}
        ResolvedConceptReferencesIterator iterator
            = createValueSetSearchUtils().searchResolvedValueSetCodingSchemes(checked_vocabularies,
            matchText, searchOption, algorithm);

        if (iterator == null) {
			msg = "No match found.";
			if (searchOption == SimpleSearchUtils.BY_CODE) {
   			    msg = Constants.ERROR_NO_MATCH_FOUND_CODE_IS_CASESENSITIVE;
			}
			request.getSession().setAttribute("message", msg);
			return "message";
		} else {
			try {
				int numRemaining = iterator.numberRemaining();
				if (numRemaining == 0) {
					msg = "No match found.";
					if (searchOption == SimpleSearchUtils.BY_CODE) {
						msg = Constants.ERROR_NO_MATCH_FOUND_CODE_IS_CASESENSITIVE;
					}
					request.getSession().setAttribute("message", msg);
					return "message";
				}
			} catch (Exception ex) {
				msg = "No match found.";
				if (searchOption == SimpleSearchUtils.BY_CODE) {
					msg = Constants.ERROR_NO_MATCH_FOUND_CODE_IS_CASESENSITIVE;
				}
				request.getSession().setAttribute("message", msg);
				return "message";
			}

			IteratorBean iteratorBean = new IteratorBean(iterator);

            String key = IteratorBeanManager.createIteratorKey(checked_vocabularies, matchText,
                selectValueSetSearchOption, algorithm);
            iteratorBean.setKey(key);
			request.getSession().setAttribute("value_set_entity_search_results", iteratorBean);
			return "value_set";
		}
    }

/*
    public void exportToExcelAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

		try {
			HttpServletResponse response = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();

			String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));

    		response.setContentType("application/vnd.ms-excel");
			//response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

			String vsd_name = DataUtils.valueSetDefinitionURI2Name(vsd_uri);
			vsd_name = vsd_name.replaceAll(" ", "_");
			vsd_name = vsd_name + ".xls";

		    response.setHeader("Content-Disposition", "attachment; filename="
					+ vsd_name);

            StringBuffer sb = new StringBuffer();

			ResolvedValueSetIteratorHolder rvsi = (ResolvedValueSetIteratorHolder) request.getSession().getAttribute("rvsi");
			List list = null;
			if (rvsi != null) {
				list = rvsi.getResolvedValueSetList();
			    sb.append(rvsi.getOpenTableTag("rvs_table"));
				String first_line = (String) list.get(0);
			    first_line = first_line.replaceAll("td", "th");
			    sb.append(first_line);

				for (int k=1; k<list.size(); k++) {
					String line = (String) list.get(k);
					line = ResolvedValueSetIteratorHolder.removeHyperlinks(line);
					sb.append(line);
				}
				sb.append(rvsi.getCloseTableTag());
			}

			String outputstr = sb.toString();
			response.setContentLength(outputstr.length());

			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(outputstr.getBytes("UTF8"), 0, outputstr.length());
			ouputStream.flush();
			ouputStream.close();

		} catch(IOException e) {
			e.printStackTrace();
		}

		FacesContext.getCurrentInstance().responseComplete();
	}
*/
    public void exportToExcelAction(HttpServletRequest request, HttpServletResponse response) {
		try {
			String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
    		response.setContentType("application/vnd.ms-excel");
			String vsd_name = DataUtils.valueSetDefinitionURI2Name(vsd_uri);
			vsd_name = vsd_name.replaceAll(" ", "_");
			String sheet_name = vsd_name;
			vsd_name = vsd_name + ".xls";
		    response.setHeader("Content-Disposition", "attachment; filename="
					+ vsd_name);
			ResolvedValueSetIteratorHolder rvsi = (ResolvedValueSetIteratorHolder) request.getSession().getAttribute("rvsi");
			HSSFWorkbook hssfWorkbook = rvsi.createHSSFWorkbook(sheet_name);
			ServletOutputStream ouputStream = response.getOutputStream();
            hssfWorkbook.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();

		} catch(IOException e) {
			e.printStackTrace();
		}
		FacesContext.getCurrentInstance().responseComplete();
	}


/*
	public void exportValuesToCSVAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

		String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
		if (vsd_uri == null) {
		    vsd_uri = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("vsd_uri"));
		}

		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		String serviceUrl = RemoteServerUtil.getServiceUrl();
		AssertedValueSetUtils avsu = new AssertedValueSetUtils(serviceUrl, lbSvc);
		ResolvedConceptReferencesIterator iterator2 = null;
		iterator2 = avsu.getValueSetIteratorForURI(vsd_uri);

		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		CodingSchemeDataUtils csdu = new CodingSchemeDataUtils(lbSvc);
		String version = csdu.getVocabularyVersionByTag(Constants.NCI_THESAURUS, Constants.PRODUCTION);
		versionOrTag.setVersion(version);
		ResolvedConceptReferencesIterator itr = null;
		try {
			CodedNodeSet cns = csdu.getNodeSet(Constants.NCI_THESAURUS, versionOrTag);
			ConceptReferenceList codeList = csdu.iterator2List(iterator2);
			cns = cns.restrictToCodes(codeList);
			SortOptionList sortOptions = null;
			LocalNameList filterOptions = null;
			LocalNameList propertyNames = null;//new LocalNameList();
			CodedNodeSet.PropertyType[] propertyTypes = new CodedNodeSet.PropertyType[2];
			propertyTypes[0] = CodedNodeSet.PropertyType.PRESENTATION;
			propertyTypes[1] = CodedNodeSet.PropertyType.DEFINITION;
			boolean resolveObjects = true;

			itr = cns.resolve(sortOptions, filterOptions, propertyNames, propertyTypes, resolveObjects);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
//String heading = "NCIt Concept Code	Source Name	NCIt Preferred Term	NCIt Synonyms	Source Definition	NCIt Definition";
		StringBuffer sb = new StringBuffer();
		sb.append("NCIt Concept Code,");
		sb.append("NCIt Preferred Term,");
		sb.append("NCIt Synonyms,");
		sb.append("NCIt Definition");
		sb.append("\r\n");
        int knt = 0;
        try {
			while (itr != null && itr.hasNext()) {
				ResolvedConceptReference[] refs = itr.next(100).getResolvedConceptReference();
				for (ResolvedConceptReference ref : refs) {
					knt++;
					String entityDescription = "<NOT ASSIGNED>";
					if (ref.getEntityDescription() != null) {
						entityDescription = ref.getEntityDescription().getContent();
					}

					sb.append("\"" + ref.getConceptCode() + "\",");
					sb.append("\"" + entityDescription + "\",");
					String synonyms = getNCISynonyms(ref);
					if (synonyms == null) synonyms = "";
					sb.append("\"" + synonyms + "\",");
					String definition = getNCIDefinition(ref);
					if (definition == null) definition = "";
					sb.append("\"" + definition + "\"");
					sb.append("\r\n");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("Number of records: " + knt);
		String vsd_name = DataUtils.valueSetDefinitionURI2Name(vsd_uri);
		vsd_name = vsd_name.replaceAll(" ", "_");
		vsd_name = "resolved_" + vsd_name + ".csv";
		//System.out.println("ValueSetBean exportValuesToCSV: " + vsd_name + " (num_records: " + num_records + ")");

		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ vsd_name);

		response.setContentLength(sb.length());
		try {
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(sb.toString().getBytes("UTF8"), 0, sb.length());
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		FacesContext.getCurrentInstance().responseComplete();
	}
*/

    public static String getSupportedSource(String vsd_uri) {
		LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
	    ValueSetMetadataUtils vsmdu = new ValueSetMetadataUtils(vsd_service);
		String metadata = vsmdu.getValueSetDefinitionMetadata(vsd_uri);
		Vector u = StringUtils.parseData(metadata);
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
		return supportedsource;
	}

	public static String convertVSDUri2HeaderConcdptCode(String vsd_uri) {
		int n = vsd_uri.lastIndexOf("/");
		return vsd_uri.substring(n+1, vsd_uri.length());
	}


	public void exportValuesToCSVAction3() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
		String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
		if (vsd_uri == null) {
		    vsd_uri = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("vsd_uri"));
		}
        //System.out.println("ValueSetBean exportValuesToCSVAction exportValuesToCSVAction vsd_uri: " + vsd_uri);

		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		String serviceUrl = RemoteServerUtil.getServiceUrl();
        String defaultCodingScheme = Constants.NCI_THESAURUS;
        String version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(defaultCodingScheme, Constants.PRODUCTION);
        System.out.println("version: " + version);
        String namespace = "ncit";
        String code = convertVSDUri2HeaderConcdptCode(vsd_uri);
        System.out.println("header concept code: " + code);
        String associationName = "Concept_In_Subset";
        boolean direction = false;
        Vector v = null;
        RelationshipUtils relUtils = new RelationshipUtils(lbSvc);
        ArrayList A8_list = null;
        if (relUtils == null) {
			System.out.println("RelationshipUtils == null???");
		} else {
            A8_list = relUtils.getRelationshipData(defaultCodingScheme, version, namespace, code, associationName, direction);
		}
        if (A8_list == null) {
			System.out.println("A8_list == null???");
		} else {
            v = list2Vector(A8_list);
			if (v == null) {
				System.out.println("list2Vector returns null???");
			}
		}
        ConceptReferenceList codeList = new ConceptReferenceList();
        for (int i=0; i<v.size(); i++) {
			String line = (String) v.elementAt(i);
			Vector u = StringUtils.parseData(line, '|');
			ConceptReference ref = new ConceptReference();
			ref.setConceptCode((String) u.elementAt(2));
			codeList.addConceptReference(ref);
		}
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion(version);
        CodingSchemeDataUtils codingSchemeDataUtils = new CodingSchemeDataUtils(lbSvc);
        ResolvedConceptReferencesIterator itr = null;
        try {
			CodedNodeSet cns = codingSchemeDataUtils.getNodeSet(defaultCodingScheme, versionOrTag);
			cns = cns.restrictToCodes(codeList);
			SortOptionList sortOptions = null;
			LocalNameList filterOptions = null;
			LocalNameList propertyNames = null;
			CodedNodeSet.PropertyType[] propertyTypes = new CodedNodeSet.PropertyType[2];
			propertyTypes[0] = CodedNodeSet.PropertyType.PRESENTATION;
			propertyTypes[1] = CodedNodeSet.PropertyType.DEFINITION;
			boolean resolveObjects = true;
			itr = cns.resolve(sortOptions, filterOptions, propertyNames, propertyTypes, resolveObjects);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("NCIt Concept Code,");
		sb.append("NCIt Preferred Term,");
		sb.append("NCIt Synonyms,");
		sb.append("NCIt Definition");
		sb.append("\r\n");
        int knt = 0;
        try {
			while (itr != null && itr.hasNext()) {
				ResolvedConceptReference[] refs = itr.next(100).getResolvedConceptReference();
				for (ResolvedConceptReference ref : refs) {
					knt++;
					String entityDescription = "<NOT ASSIGNED>";
					if (ref.getEntityDescription() != null) {
						entityDescription = ref.getEntityDescription().getContent();
					}
					sb.append("\"" + ref.getConceptCode() + "\",");
					sb.append("\"" + entityDescription + "\",");
					String synonyms = getNCISynonyms(ref);
					if (synonyms == null) synonyms = "";
					sb.append("\"" + synonyms + "\",");
					String definition = getNCIDefinition(ref);
					if (definition == null) definition = "";
					sb.append("\"" + definition + "\"");
					sb.append("\r\n");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		int num_records = v.size()-1;

	    LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
	    ValueSetMetadataUtils vsmdu = new ValueSetMetadataUtils(vsd_service);
		String metadata = vsmdu.getValueSetDefinitionMetadata(vsd_uri);
		Vector u = StringUtils.parseData(metadata);
		String vsd_name = (String) u.elementAt(0);

		vsd_name = vsd_name.replaceAll(" ", "_");
		vsd_name = "resolved_" + vsd_name + ".csv";
		System.out.println("ValueSetBean exportValuesToCSV: " + vsd_name + " (num_records: " + num_records + ")");

		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ vsd_name);

		response.setContentLength(sb.length());
		try {
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(sb.toString().getBytes("UTF8"), 0, sb.length());
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		FacesContext.getCurrentInstance().responseComplete();
	}

	public static Vector list2Vector(List list) {
		if (list == null) return null;
		Vector w = new Vector();
		for (int i=0; i<list.size(); i++) {
			w.add((String) list.get(i));
		}
		return w;
	}

	public void exportValuesToCSVAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
		String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
		if (vsd_uri == null) {
		    vsd_uri = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("vsd_uri"));
		}
        //System.out.println("ValueSetBean exportValuesToCSVAction exportValuesToCSVAction vsd_uri: " + vsd_uri);

        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		String serviceUrl = RemoteServerUtil.getServiceUrl();
        String defaultCodingScheme = "NCI_Thesaurus";
        String version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(defaultCodingScheme, "PRODUCTION");
        String namespace = "ncit";
        String code = convertVSDUri2HeaderConcdptCode(vsd_uri);
        String associationName = "Concept_In_Subset";
        boolean direction = false;

        ArrayList A8_list = new RelationshipUtils(lbSvc).getRelationshipData(defaultCodingScheme, version, namespace, code, associationName, direction);
        Vector v = list2Vector(A8_list);
        System.out.println("value set size: " + v.size());

        ConceptReferenceList codeList = new ConceptReferenceList();
        for (int i=0; i<v.size(); i++) {
			String line = (String) v.elementAt(i);
			Vector u = StringUtils.parseData(line, '|');
			ConceptReference ref = new ConceptReference();
			ref.setConceptCode((String) u.elementAt(2));
			codeList.addConceptReference(ref);
		}

		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion(version);
        CodingSchemeDataUtils codingSchemeDataUtils = new CodingSchemeDataUtils(lbSvc);
        ResolvedConceptReferencesIterator itr = null;

        try {
			CodedNodeSet cns = codingSchemeDataUtils.getNodeSet(defaultCodingScheme, versionOrTag);
			cns = cns.restrictToCodes(codeList);
			SortOptionList sortOptions = null;
			LocalNameList filterOptions = null;
			LocalNameList propertyNames = null;
			CodedNodeSet.PropertyType[] propertyTypes = new CodedNodeSet.PropertyType[2];
			propertyTypes[0] = CodedNodeSet.PropertyType.PRESENTATION;
			propertyTypes[1] = CodedNodeSet.PropertyType.DEFINITION;
			boolean resolveObjects = true;
			itr = cns.resolve(sortOptions, filterOptions, propertyNames, propertyTypes, resolveObjects);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		StringBuffer sb = new StringBuffer();
		sb.append("NCIt Concept Code,");
		sb.append("NCIt Preferred Term,");
		sb.append("NCIt Synonyms,");
		sb.append("NCIt Definition");
		sb.append("\r\n");
        int knt = 0;
        try {
			while (itr != null && itr.hasNext()) {
				ResolvedConceptReference[] refs = itr.next(100).getResolvedConceptReference();
				for (ResolvedConceptReference ref : refs) {
					knt++;
					String entityDescription = "<NOT ASSIGNED>";
					if (ref.getEntityDescription() != null) {
						entityDescription = ref.getEntityDescription().getContent();
					}
					sb.append("\"" + ref.getConceptCode() + "\",");
					sb.append("\"" + entityDescription + "\",");
					String synonyms = getNCISynonyms(ref);
					if (synonyms == null) synonyms = "";
					sb.append("\"" + synonyms + "\",");
					String definition = getNCIDefinition(ref);
					if (definition == null) definition = "";
					sb.append("\"" + definition + "\"");
					sb.append("\r\n");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		int num_records = v.size()-1;
	    LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
	    ValueSetMetadataUtils vsmdu = new ValueSetMetadataUtils(vsd_service);
		String metadata = vsmdu.getValueSetDefinitionMetadata(vsd_uri);
		Vector u = StringUtils.parseData(metadata);
		String vsd_name = (String) u.elementAt(0);
		vsd_name = vsd_name.replaceAll(" ", "_");
		vsd_name = "resolved_" + vsd_name + ".csv";
		System.out.println("ValueSetBean exportValuesToCSV: " + vsd_name + " (num_records: " + num_records + ")");

		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ vsd_name);

		response.setContentLength(sb.length());
		try {
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(sb.toString().getBytes("UTF8"), 0, sb.length());
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		FacesContext.getCurrentInstance().responseComplete();
	}

	public void exportValuesToCSVAction2() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

		String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
		if (vsd_uri == null) {
		    vsd_uri = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("vsd_uri"));
		}

		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		String serviceUrl = RemoteServerUtil.getServiceUrl();
		String heading = "NCIt Concept Code	Source Name	NCIt Preferred Term	NCIt Synonyms	Source Definition	NCIt Definition";
		Vector fields = StringUtils.parseData(heading, '\t');

		LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
		CodingSchemeDataUtils codingSchemeDataUtils = new CodingSchemeDataUtils(lbSvc);
		if (codingSchemeDataUtils == null) {
			System.out.println("Unable to instantiate CodingSchemeDataUtils???");
		} else {
			System.out.println("instantiated CodingSchemeDataUtils");
		}
		String version = codingSchemeDataUtils.getVocabularyVersionByTag(Constants.NCI_THESAURUS, Constants.PRODUCTION);

		//////////////////////////////////////////////////////////////////////////////////////////////////
		ValueSetFormatter formatter = new ValueSetFormatter(serviceUrl, lbSvc, vsd_service);
		Vector vs_data = formatter.export(vsd_uri, version, fields);
		Vector v = gov.nih.nci.evs.browser.utils.StringUtils.convertDelimited2CSV(vs_data, '|');
		//////////////////////////////////////////////////////////////////////////////////////////////////

		System.out.println("v: " + v.size());
        StringBuffer sb = new StringBuffer();
        for (int k=0; k<fields.size(); k++) {
			String field = (String) fields.elementAt(k);
			sb.append(field);
			if (k < fields.size()-1) {
				sb.append(",");
			}
		}
		sb.append("\r\n");
		for (int k=1; k<v.size(); k++) {
			String t = (String) v.elementAt(k);
			sb.append(t);
			sb.append("\n");
		}
		int num_records = v.size()-1;
		String vsd_name = DataUtils.valueSetDefinitionURI2Name(vsd_uri);

		vsd_name = vsd_name.replaceAll(" ", "_");
		vsd_name = "resolved_" + vsd_name + ".csv";
		System.out.println("ValueSetBean exportValuesToCSV: " + vsd_name + " (num_records: " + num_records + ")");

		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ vsd_name);

		response.setContentLength(sb.length());
		try {
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(sb.toString().getBytes("UTF8"), 0, sb.length());
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		FacesContext.getCurrentInstance().responseComplete();
	}

/*
	public void exportValuesToCSVAction0() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();


		String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
		if (vsd_uri == null) {
		    vsd_uri = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("vsd_uri"));
		}

	    LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
	    ValueSetMetadataUtils vsmdu = new ValueSetMetadataUtils(vsd_service);
		String metadata = vsmdu.getValueSetDefinitionMetadata(vsd_uri);
		Vector u = StringUtils.parseData(metadata);
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

		if (!DataUtils.isNCIT(defaultCodingScheme)) {
			exportToCSVAction();
			return;
		}
		boolean reformat = true;
		boolean use_new_format = true;

		if (supportedsources != null) {
		    Vector w = gov.nih.nci.evs.browser.utils.StringUtils.parseData(supportedsources, ";");
		    supportedsource = (String) w.elementAt(0);
		}
		//if (supportedsource == null || supportedsource.compareTo("null") == 0) {
		if (supportedsource == null || supportedsource.compareTo("null") == 0 || supportedsource.compareTo("NCI") == 0) {
		    reformat = false;
		}

		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		String serviceUrl = RemoteServerUtil.getServiceUrl();
		//LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
		ValueSetFormatter formatter = new ValueSetFormatter(serviceUrl, lbSvc, vsd_service);
        Vector fields = formatter.getDefaultFields(reformat);

		CodingSchemeDataUtils csdu = new CodingSchemeDataUtils(lbSvc);
		String version = csdu.getVocabularyVersionByTag(defaultCodingScheme, Constants.PRODUCTION);
		AssertedValueSetUtils avsu = new AssertedValueSetUtils(serviceUrl, lbSvc);
        ResolvedConceptReferencesIterator iterator2 = avsu.getValueSetIteratorForURI(vsd_uri);
		//Vector lines = csdu.resolve(vsd_uri, version);
		int maxToReturn = 250;
		Vector lines = csdu.resolveIterator(iterator2, maxToReturn);
		Vector codes = new Vector();
		for (int i=0; i<lines.size(); i++) {
			String line = (String) lines.elementAt(i);
			u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(line, '|');
			String code = (String) u.elementAt(1);
			codes.add(code);
		}
		Vector rvs_tbl = formatter.export(vsd_uri, version, supportedsource, fields, codes);
	    Vector v = gov.nih.nci.evs.browser.utils.StringUtils.convertDelimited2CSV(rvs_tbl, '|');
        StringBuffer sb = new StringBuffer();
        for (int k=0; k<fields.size(); k++) {
			String field = (String) fields.elementAt(k);
			sb.append(field);
			if (k < fields.size()-1) {
				sb.append(",");
			}
		}
		sb.append("\r\n");
		for (int k=0; k<v.size(); k++) {
			String t = (String) v.elementAt(k);
			sb.append(t);
			sb.append("\n");
		}
		String vsd_name = DataUtils.valueSetDefinitionURI2Name(vsd_uri);
		vsd_name = vsd_name.replaceAll(" ", "_");
		vsd_name = "resolved_" + vsd_name + ".csv";

		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ vsd_name);

		response.setContentLength(sb.length());
		try {
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(sb.toString().getBytes("UTF8"), 0, sb.length());
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			//sb.append("WARNING: Export to CVS action failed.");
		}
		FacesContext.getCurrentInstance().responseComplete();
	}
	*/

	public void exportValuesToXMLAction() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        String vsd_uri = (String) request.getSession().getAttribute("vsd_uri");
	    LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
	    ValueSetMetadataUtils vsmdu = new ValueSetMetadataUtils(vsd_service);
		String metadata = vsmdu.getValueSetDefinitionMetadata(vsd_uri);
		Vector u = StringUtils.parseData(metadata);
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
		if (!DataUtils.isNCIT(defaultCodingScheme)) {
			exportToXMLAction();
			return;
		}
		if (defaultCodingScheme.compareTo("ncit") == 0) {
			defaultCodingScheme = "NCI_Thesaurus";
		}
		boolean withSource = true;
		if (supportedsource == null || supportedsource.compareTo("null") == 0 || supportedsource.compareTo("NCI") == 0) {
			withSource = false;
		}
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		String serviceUrl = RemoteServerUtil.getServiceUrl();
		//LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices(serviceUrl);
        //ValueSetFormatter test = new ValueSetFormatter(lbSvc, vsd_service);
        String version = new CodingSchemeDataUtils(lbSvc).getVocabularyVersionByTag(defaultCodingScheme, Constants.PRODUCTION);
        long ms = System.currentTimeMillis();
		ValueSetFormatter formatter = new ValueSetFormatter(serviceUrl, lbSvc, vsd_service);
		Vector fields = formatter.getDefaultFields(withSource);
		ValueSet vs = formatter.instantiateValueSet(vsd_uri, version, fields);
		String xml_str = formatter.object2XMLStream(vs);
		try {
			HttpServletResponse response = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();
			response.setContentType("text/xml");

			String vsd_name = DataUtils.valueSetDefinitionURI2Name(vsd_uri);
			vsd_name = vsd_name.replaceAll(" ", "_");
			vsd_name = vsd_name + ".xml";

		    response.setHeader("Content-Disposition", "attachment; filename="
					+ vsd_name);

			response.setContentLength(xml_str.length());

			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(xml_str.getBytes("UTF8"), 0, xml_str.length());
			ouputStream.flush();
			ouputStream.close();

		} catch(IOException e) {
			e.printStackTrace();
		}
		FacesContext.getCurrentInstance().responseComplete();
	}

    public String getNCISynonyms(ResolvedConceptReference ref) {
		if (ref == null) return null;
		Entity concept = ref.getReferencedEntry();
		if (concept == null) return null;

		Presentation[] properties = null;
		properties = concept.getPresentation();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < properties.length; i++) {
            Presentation p = properties[i];
            boolean inclusion = false;
			if (p.getPropertyName().compareTo("FULL_SYN") == 0) {
				inclusion = true;
			}
            if (inclusion) {
                String term_name = p.getValue().getContent();
                String term_type = "null";
                String term_source = "null";
                String term_source_code = "null";
                String term_subsource = "null";
                PropertyQualifier[] qualifiers = p.getPropertyQualifier();

                if (qualifiers != null) {
                    for (int j = 0; j < qualifiers.length; j++) {
                        PropertyQualifier q = qualifiers[j];
                        String qualifier_name = q.getPropertyQualifierName();
                        String qualifier_value = q.getValue().getContent();
                        if (qualifier_name.compareTo("source-code") == 0) {
                            term_source_code = qualifier_value;
                        }
                        if (qualifier_name.compareTo("subsource-name") == 0) {
                            term_subsource = qualifier_value;
                        }
                    }
                }
                term_type = p.getRepresentationalForm();
                Source[] sources = p.getSource();
                if (sources != null && sources.length > 0) {
                    Source src = sources[0];
                    term_source = src.getContent();
                }
                if (term_type.compareTo("SY") == 0 && term_source.compareTo("NCI") == 0) {
					sb.append(term_name).append("$");
				}
            }

        }
        String t = sb.toString();
        if (t.length() > 0) {
        	t = t.substring(0, t.length()-1);
		}
		return t;
	}
}
