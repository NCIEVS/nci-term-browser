/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-term-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.bean;

import java.util.*;

import javax.faces.context.*;
import javax.faces.event.*;
import javax.faces.model.*;
import javax.servlet.http.*;

import org.apache.log4j.*;

import gov.nih.nci.evs.browser.utils.*;

/**
 * 
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
    private String _codingSchemeName = null;
    private String _version = null;

    private String _direction = null;

    public SearchStatusBean() {
    }

    public SearchStatusBean(String codingSchemeName) {
        _codingSchemeName = codingSchemeName;
    }

    public SearchStatusBean(String codingSchemeName, String version) {
        _codingSchemeName = codingSchemeName;
        _version = version;
    }

    public String setSessionAttribute(String attributeName, String value) {
        HttpServletRequest request = HTTPUtils.getRequest();
        // Note: Reuse previous value if null.
        if (value == null)
            value = (String) request.getSession().getAttribute(attributeName);
        request.getSession().setAttribute(attributeName, value);
        return value;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////

    private String _selectedSearchOption = "Property";
    private String _matchText = null;
    private List _searchOptionList = null;

    public void setSelectedSearchOption(String selectedSearchOption) {
        // selectedSearchOption = setSessionAttribute(
        // "advancedSearchOption", selectedSearchOption);

        _selectedSearchOption = selectedSearchOption;
    }

    public String getSelectedSearchOption() {
        return _selectedSearchOption;
    }

    public void setDirection(String direction) {
        _direction = direction;
    }

    public String getDirection() {
        return _direction;
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
            bean.setAlgorithm(getAlgorithm());
        }
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
            .put("searchStatusBean", bean);

        // SearchStatusBean bean = (SearchStatusBean)
        // FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("searchStatusBean");

        // HttpServletRequest request =
        // (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

    }

    public List getSearchOptionList() {
        _searchOptionList = new ArrayList();
        _searchOptionList.add(new SelectItem("Property", "Property"));
        _searchOptionList.add(new SelectItem("Relationship", "Relationship"));
        return _searchOptionList;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////

    private String _selectedProperty = "ALL";
    private List _propertyList = null;

    public void setSelectedProperty(String selectedProperty) {
        // selectedProperty = setSessionAttribute(
        // "advancedPropertyOption", selectedProperty);

        _selectedProperty = selectedProperty;
    }

    public String getSelectedProperty() {
        return _selectedProperty;
    }

    public void selectedPropertyChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("selectedPropertyChanged to " + newValue);
        setSelectedProperty(newValue);
    }

    public List getPropertyList() {
        if (_propertyList == null) {
            _propertyList = OntologyBean.getPropertyNameList(_codingSchemeName, _version);
            if (_propertyList != null && _propertyList.size() > 0) {
                _selectedProperty =
                    ((SelectItem) _propertyList.get(0)).getLabel();
            }
        }
        return _propertyList;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    private String _selectedSource = null;
    private List _sourceList = null;

    public void setSelectedSource(String selectedSource) {
        _selectedSource = selectedSource;
    }

    public String getSelectedSource() {
        return _selectedSource;
    }

    public void selectedSourceChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("selectedSourceChanged to " + newValue);
        setSelectedSource(newValue);
    }

    public List getSourceList() {
        if (_sourceList == null) {
            _sourceList = OntologyBean.getSourceList(_codingSchemeName, _version);
            if (_sourceList != null && _sourceList.size() > 0) {
                _selectedSource =
                    ((SelectItem) _sourceList.get(0)).getLabel();
            }
        }
        return _sourceList;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    private String _selectedPropertyType = null;
    private List _propertyTypeList = null;

    public void setSelectedPropertyType(String selectedPropertyType) {
        _selectedPropertyType = selectedPropertyType;
    }

    public String getSelectedPropertyType() {
        return _selectedPropertyType;
    }

    public void selectedPropertyTypeChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("selectedPropertyTypeChanged to " + newValue);
        setSelectedPropertyType(newValue);
    }

    public List getPropertyTypeList() {
        if (_propertyTypeList == null) {
            _propertyTypeList =
                OntologyBean.getPropertyTypeList(_codingSchemeName, _version);
            if (_propertyTypeList != null && _propertyTypeList.size() > 0) {
                _selectedPropertyType =
                    ((SelectItem) _propertyTypeList.get(0)).getLabel();
            }
        }
        return _propertyTypeList;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    private String _selectedAssociation = null;
    private List _associationList = null;

    public void setSelectedAssociation(String selectedAssociation) {
        _selectedAssociation = selectedAssociation;
    }

    public String getSelectedAssociation() {
        return _selectedAssociation;
    }

    public void selectedAssociationChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("selectedAssociationChanged to " + newValue);
        setSelectedAssociation(newValue);
    }

    public List getAssociationList() {
        if (_associationList == null) {
            _associationList =
                OntologyBean.getAssociationNameList(_codingSchemeName, _version);
            if (_associationList != null && _associationList.size() > 0) {
                _selectedAssociation =
                    ((SelectItem) _associationList.get(0)).getLabel();
            }
        }
        return _associationList;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    private String _selectedRelationship = "ALL";
    private List _relationshipList = null;

    public void setSelectedRelationship(String selectedRelationship) {
        _selectedRelationship =
            setSessionAttribute("advancedRelationOption", selectedRelationship);
    }

    public String getSelectedRelationship() {
        return _selectedRelationship;
    }

    public void selectedRelationshipChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("selectedRelationshipChanged to " + newValue);
        setSelectedRelationship(newValue);
    }

    public List getRelationshipList() {
        if (_relationshipList == null) {
            _relationshipList =
                OntologyBean.getAssociationNameList(_codingSchemeName, _version);
            _relationshipList.add(0, new SelectItem("ALL", "ALL"));
            //if (_relationshipList != null && _relationshipList.size() > 0) {
                _selectedRelationship =
                    ((SelectItem) _relationshipList.get(0)).getLabel();
            //}
        }
        return _relationshipList;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    private String _selectedRELA = "";
    private List _RELAList = null;

    public void setSelectedRELA(String selectedRELA) {
        _selectedRELA =
            setSessionAttribute("advancedRELAOption", selectedRELA);
    }

    public String getSelectedRELA() {
        return _selectedRELA;
    }

    public void selectedRELAChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();

        _logger.debug("selectedRELAChanged to " + newValue);
        setSelectedRELA(newValue);
    }

    public List getRELAList() {
        if (_RELAList == null) {
            _RELAList = OntologyBean.getRELAList(_codingSchemeName, _version);
            if (_RELAList != null && _RELAList.size() > 0) {
                _selectedRELA = ((SelectItem) _RELAList.get(0)).getLabel();
            }
        }
        return _RELAList;
    }

    // //////////////////////////////////////////////////////////////////////////////////
    public void setMatchText(String t) {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("matchText", t);
        _matchText = t;

        _logger.debug("Set matchText to: " + t);
    }

    public HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance()
            .getExternalContext().getRequest();
    }

    public String getMatchText() {
        return _matchText;
    }

    private String _algorithm;

    public void setAlgorithm(String algorithm) {
        _algorithm = algorithm;
    }

    public String getAlgorithm() {
        return _algorithm;
    }

    private String _searchType;

    public void setSearchType(String searchType) {
        _searchType = searchType;
    }

    public String getSearchType() {
        return _searchType;
    }

}
