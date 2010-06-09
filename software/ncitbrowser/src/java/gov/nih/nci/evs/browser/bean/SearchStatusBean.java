package gov.nih.nci.evs.browser.bean;

import gov.nih.nci.evs.browser.utils.MailUtils;
import gov.nih.nci.evs.browser.utils.SearchUtils;
import gov.nih.nci.evs.browser.utils.Utils;

import gov.nih.nci.evs.browser.properties.NCItBrowserProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.LexGrid.concepts.Concept;

import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.browser.common.Constants;
import gov.nih.nci.evs.searchlog.SearchLog;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

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

/*
 * <FORM NAME="searchOptions" METHOD="POST" CLASS="search-form">
 * <h:selectOneMenu id="selectSearchOption"
 * value="#{searchStatusBean.selectedSearchOption}" > <f:selectItems
 * value="#{searchStatusBean.searchOptionList}" /> </h:selectOneMenu> </form>
 */

public class SearchStatusBean extends Object {
    private static Logger _logger = Logger.getLogger(SearchStatusBean.class);
    private String codingSchemeName = null;

    public SearchStatusBean() {

    }

    public SearchStatusBean(String codingSchemeName) {
        this.codingSchemeName = codingSchemeName;
    }

    private static Logger logger = Logger.getLogger(SearchStatusBean.class);

    public String setSessionAttribute(String attributeName, String value) {
        HttpServletRequest request = HTTPUtils.getRequest();
        // Note: Reuse previous value if null.
        if (value == null)
            value = (String) request.getSession().getAttribute(attributeName);
        request.getSession().setAttribute(attributeName, value);
        return value;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////

    private String selectedSearchOption = "Property";
    private String matchText = null;
    private List searchOptionList = null;

    public void setSelectedSearchOption(String selectedSearchOption) {
        // this.selectedSearchOption = setSessionAttribute(
        // "advancedSearchOption", selectedSearchOption);

        this.selectedSearchOption = selectedSearchOption;
    }

    public String getSelectedSearchOption() {
        return this.selectedSearchOption;
    }

    public void searchOptionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("searchOptionChanged to " + newValue);
        setSelectedSearchOption(newValue);
        HttpServletRequest request = HTTPUtils.getRequest();

        request.setAttribute("searchOptionChangedTo", newValue);

        Object bean_obj =
            FacesContext.getCurrentInstance().getExternalContext()
                .getRequestMap().get("searchStatusBean");
        SearchStatusBean bean = null;
        if (bean_obj == null) {
            bean = new SearchStatusBean();
            FacesContext.getCurrentInstance().getExternalContext()
                .getRequestMap().put("searchStatusBean", bean);

        } else {
            bean = (SearchStatusBean) bean_obj;
            bean.setAlgorithm(this.getAlgorithm());
        }
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
            .put("searchStatusBean", bean);

        // SearchStatusBean bean = (SearchStatusBean)
        // FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("searchStatusBean");

        // HttpServletRequest request =
        // (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

    }

    public List getSearchOptionList() {
        searchOptionList = new ArrayList();
        searchOptionList.add(new SelectItem("Property", "Property"));
        searchOptionList.add(new SelectItem("Relationship", "Relationship"));
        return searchOptionList;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////

    private String selectedProperty = "ALL";
    private List propertyList = null;

    public void setSelectedProperty(String selectedProperty) {
        // this.selectedProperty = setSessionAttribute(
        // "advancedPropertyOption", selectedProperty);

        this.selectedProperty = selectedProperty;
    }

    public String getSelectedProperty() {
        return this.selectedProperty;
    }

    public void selectedPropertyChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("selectedPropertyChanged to " + newValue);
        setSelectedProperty(newValue);
    }

    public List getPropertyList() {
        if (propertyList == null) {
            propertyList = OntologyBean.getPropertyNameList(codingSchemeName);
            if (propertyList != null && propertyList.size() > 0) {
                this.selectedProperty =
                    ((SelectItem) propertyList.get(0)).getLabel();
            }
        }
        return propertyList;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    private String selectedSource = null;
    private List sourceList = null;

    public void setSelectedSource(String selectedSource) {
        this.selectedSource = selectedSource;
    }

    public String getSelectedSource() {
        return this.selectedSource;
    }

    public void selectedSourceChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("selectedSourceChanged to " + newValue);
        setSelectedSource(newValue);
    }

    public List getSourceList() {
        if (sourceList == null) {
            sourceList = OntologyBean.getSourceList(codingSchemeName);
            if (sourceList != null && sourceList.size() > 0) {
                this.selectedSource =
                    ((SelectItem) sourceList.get(0)).getLabel();
            }
        }
        return sourceList;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    private String selectedPropertyType = null;
    private List propertyTypeList = null;

    public void setSelectedPropertyType(String selectedPropertyType) {
        this.selectedPropertyType = selectedPropertyType;
    }

    public String getSelectedPropertyType() {
        return this.selectedPropertyType;
    }

    public void selectedPropertyTypeChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("selectedPropertyTypeChanged to " + newValue);
        setSelectedPropertyType(newValue);
    }

    public List getPropertyTypeList() {
        if (propertyTypeList == null) {
            propertyTypeList = OntologyBean.getPropertyTypeList(codingSchemeName);
            if (propertyTypeList != null && propertyTypeList.size() > 0) {
                this.selectedPropertyType =
                    ((SelectItem) propertyTypeList.get(0)).getLabel();
            }
        }
        return propertyTypeList;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    private String selectedAssociation = null;
    private List associationList = null;

    public void setSelectedAssociation(String selectedAssociation) {
        this.selectedAssociation = selectedAssociation;
    }

    public String getSelectedAssociation() {
        return this.selectedAssociation;
    }

    public void selectedAssociationChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("selectedAssociationChanged to " + newValue);
        setSelectedAssociation(newValue);
    }

    public List getAssociationList() {
        if (associationList == null) {
            associationList = OntologyBean.getAssociationNameList(codingSchemeName);
            if (associationList != null && associationList.size() > 0) {
                this.selectedAssociation =
                    ((SelectItem) associationList.get(0)).getLabel();
            }
        }
        return associationList;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    private String selectedRelationship = "ALL";
    private List relationshipList = null;

    public void setSelectedRelationship(String selectedRelationship) {
        this.selectedRelationship =
            setSessionAttribute("advancedRelationOption", selectedRelationship);
    }

    public String getSelectedRelationship() {
        return this.selectedRelationship;
    }

    public void selectedRelationshipChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("selectedRelationshipChanged to " + newValue);
        setSelectedRelationship(newValue);
    }

    public List getRelationshipList() {
        if (relationshipList == null) {
            relationshipList = OntologyBean.getAssociationNameList(codingSchemeName);
            relationshipList.add(0, new SelectItem("ALL", "ALL"));
            if (relationshipList != null && relationshipList.size() > 0) {
                this.selectedRelationship =
                    ((SelectItem) relationshipList.get(0)).getLabel();
            }
        }
        return relationshipList;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    private String selectedRELA = "";
    private List RELAList = null;

    public void setSelectedRELA(String selectedRELA) {
        this.selectedRELA =
            setSessionAttribute("advancedRELAOption", selectedRELA);
    }

    public String getSelectedRELA() {
        return this.selectedRELA;
    }

    public void selectedRELAChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("selectedRELAChanged to " + newValue);
        setSelectedRELA(newValue);
    }

    public List getRELAList() {
        if (RELAList == null) {
            RELAList = OntologyBean.getRELAList(codingSchemeName);
            if (RELAList != null && RELAList.size() > 0) {
                this.selectedRELA = ((SelectItem) RELAList.get(0)).getLabel();
            }
        }
        return RELAList;
    }

    // //////////////////////////////////////////////////////////////////////////////////
    public void setMatchText(String t) {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("matchText", t);
        this.matchText = t;

        _logger.debug("Set matchText to: " + t);
    }

    public HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance()
            .getExternalContext().getRequest();
    }

    public String getMatchText() {
        return matchText;
    }

    private String algorithm;

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    private String _searchType;

    public void setSearchType(String searchType) {
        _searchType = searchType;
    }

    public String getSearchType() {
        return _searchType;
    }

}
