<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.DisplayItem" %>
<%@ page import="gov.nih.nci.evs.browser.common.*" %>
<%@ page import="gov.nih.nci.evs.browser.common.Constants" %>
<%@ page import="gov.nih.nci.evs.browser.properties.*" %>
<%@ page import="gov.nih.nci.evs.browser.properties.NCItBrowserProperties" %>
<%@ page import="gov.nih.nci.evs.browser.properties.PropertyFileParser" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.ConceptDetails" %>
<%@ page import="gov.nih.nci.evs.browser.utils.HTTPUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.RemoteServerUtil" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*" %>
<%@ page import="org.LexGrid.commonTypes.EntityDescription" %>
<%@ page import="org.LexGrid.commonTypes.Property" %>
<%@ page import="org.LexGrid.commonTypes.PropertyQualifier" %>
<%@ page import="org.LexGrid.commonTypes.Source" %>
<%@ page import="org.LexGrid.concepts.*" %>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="org.LexGrid.concepts.Presentation" %>
<%@ page import="org.LexGrid.LexBIG.DataModel.Core.*" %>
<%@ page import="org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods" %>
<%@ page import="org.LexGrid.LexBIG.Extensions.Generic.MappingExtension" %>
<%@ page import="org.LexGrid.LexBIG.LexBIGService.LexBIGService" %>
<%@ page import="org.LexGrid.LexBIG.Utility.Iterators.*" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html lang="en" lang="en" xmlns:c="http://java.sun.com/jsp/jstl/core">
  <head>
    <script
        src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js">
    </script>
    <%

long ms = System.currentTimeMillis();

 
    
    LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
    ConceptDetails cd = new ConceptDetails(lbSvc);
    HistoryUtils historyUtils = new HistoryUtils(lbSvc);
    MappingSearchUtils mappingSearchUtils = new MappingSearchUtils(lbSvc);
    RelationshipUtils relationshipUtils = new RelationshipUtils(lbSvc);
    CodingSchemeDataUtils csdu = new CodingSchemeDataUtils(lbSvc);
    MetathesaurusUtils metathesaurusUtils = new MetathesaurusUtils(lbSvc);
    MappingTab mappingTab = new MappingTab(lbSvc);
    SessionMonitor sessionMonitor = new SessionMonitor(lbSvc);
    MetadataUtils metadataUtils = new MetadataUtils(lbSvc);
    PropertyData propertyData = null;
    
String code = null;
String ns = null;
String type = null;
Entity c = null;    
         
         
    JSPUtils.JSPHeaderInfo prop_info = new JSPUtils.JSPHeaderInfo(request);
    String dictionary = prop_info.dictionary;
    String formalName = mappingTab.getFormalName(dictionary);
    if (dictionary != null) {
      dictionary = StringUtils.replaceAll(dictionary, "&#40;", "(");
      dictionary = StringUtils.replaceAll(dictionary, "&#41;", ")");
    }

    String version = prop_info.version;

    // appscan fix: 09082015
    boolean retval = HTTPUtils.validateRequestParameters(request);
    if (!retval) {
      try {
        String error_msg = "WARNING: Invalid parameter(s) encountered.";
        request.getSession().setAttribute("error_msg", error_msg);
        String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
        response.sendRedirect(redirectURL);

      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    String cs_name = mappingTab.getCSName(dictionary);
    
       
    
    List namespace_list = null;
    response.setContentType("text/html;charset=utf-8");
    String short_name = cs_name;

    if (dictionary != null) {
      dictionary = mappingTab.getCSName(dictionary);

      Boolean cs_available = csdu.isCodingSchemeAvailable(dictionary);
      if (cs_available == null || !cs_available.equals(Boolean.TRUE)) {
        String error_msg = "WARNING: " + Constants.CODING_SCHEME_NOT_AVAILABLE;
        request.getSession().setAttribute("error_msg", error_msg);
        String redirectURL = request.getContextPath() + "/pages/coding_scheme_unavailable.jsf";
        response.sendRedirect(redirectURL);
      }
    }
    String deprecatedVersion = prop_info.version_deprecated;
    //AppScan KLO 051512
    if (version == null) {
      version = cd.getVocabularyVersionByTag(dictionary, "PRODUCTION");
    }

    request.setAttribute("version", version);
    // AppScan
    if (DataUtils.isNCIT(dictionary)) {
      %>
      <title>NCI Thesaurus</title>
    <% } else { %>
      <title><%= dictionary %></title>
    <% } %>

    <%
    boolean view_graph_link = false;
    String ncbo_id = null;
    String is_virtual = "true";
    String ncbo_widget_info = NCItBrowserProperties.getNCBO_WIDGET_INFO();
    boolean view_graph = DataUtils.visualizationWidgetSupported(dictionary);
    %>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/styleSheet.css" />
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/script.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/search.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/dropdown.js"></script>
  </head>
  <body>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/wz_tooltip.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/tip_centerwindow.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/tip_followscroll.js"></script>

    <script type="text/javascript">
      var newwindow;
      function popup_window(url)
      {
        newwindow=window.open(
        url, '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');
        if (window.focus) {
          newwindow.focus();
        }
      }
    </script>

    <f:view>

      <!-- Begin Skip Top Navigation -->
      <a href="#evs-content" class="skip-main" accesskey="1" title="Skip repetitive navigation links">
        skip navigation links</A>
      <!-- End Skip Top Navigation -->

      <%@ include file="/pages/templates/header.jsp" %>
      <div class="center-page_960">
        <%@ include file="/pages/templates/sub-header.jsp" %>
        <!-- Main box -->
        <div id="main-area_960">
          <%

          String singleton = (String) request.getAttribute("singleton");

          boolean code_from_cart_action = false;
          code = (String) request.getAttribute("code_from_cart_action");
          if (code == null) {
               code = HTTPUtils.cleanXSS((String) request.getParameter("code"));
	       if (code == null) {
		   code = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("code"));
	       }             
          } else {
               request.removeAttribute("code_from_cart_action");
               code_from_cart_action = true;
               code = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("code"));
          }

          if (StringUtils.isNullOrBlank(code)) {
              code = (String) request.getSession().getAttribute("code");
          } 
 
           if (code == null) {
             Entity con = (Entity) request.getSession().getAttribute("concept");
             if (con != null) {
               code = con.getEntityCode();
             } 
           }


ns = HTTPUtils.cleanXSS((String) request.getParameter("ns"));
if (ns == null) {
    ns = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("ns"));
}
if (ns == null || ns.compareTo("null") == 0) {
    ns = cd.getNamespaceByCode(dictionary, version, code);
}

sessionMonitor.execute(request, dictionary, version, code, ns);
 
code = HTTPUtils.cleanXSS(code);    
          
          request.getSession().setAttribute("code", code);
          request.getSession().setAttribute("ns", ns);
          
          ///////////////////////////////////////////////////////////////////////////////////
          String active_code = (String) request.getSession().getAttribute("active_code");
          ////////////////////////////////////////////////////////////////////////////////////

          Boolean new_search = null;
          Object new_search_obj = request.getSession().getAttribute("new_search");

          if (new_search_obj != null) {
            new_search = (Boolean) new_search_obj;
            if (new_search.equals(Boolean.TRUE)) {
              type = "properties";
              request.getSession().setAttribute("new_search",Boolean.FALSE);
              String codeFromParameter = code;
              code = (String) request.getSession().getAttribute("code");
              if (code == null) {
                code = codeFromParameter;
              }
            }
          }

          if (type == null) {
            type = HTTPUtils.cleanXSS((String) request.getParameter("type"));
            if (type == null) type = (String) request.getAttribute("type");
            if (type == null) {
              type = "properties";
              } else if (type.compareTo("properties") != 0
              && type.compareTo("relationship") != 0
              && type.compareTo("synonym") != 0
              && type.compareTo("mapping") != 0
              && type.compareTo("all") != 0) {
                type = "properties";
              }
            }

            String cd_dictionary = mappingTab.getFormalName(dictionary);
            String term_suggestion_application_url = mappingTab.getMetadataValue(cd_dictionary, null, "term_suggestion_application_url");
            String name = "";
            String ltag = null;

            if (JSPUtils.isNull(dictionary)) {
              name = "Error: Invalid dictionary - " + dictionary + ".";
            } else if (JSPUtils.isNull(version)) {
              name = "Error: Invalid version - " + version + ".";
            } else {
              namespace_list = cd.getDistinctNamespacesOfCode(
              dictionary, version, code);

              if (StringUtils.isNullOrBlank(ns) || namespace_list.size() == 1) {
                c = cd.getConceptByCode(dictionary, version, code);
              } else {
                c = cd.getConceptByCode(dictionary, version, code, ns, true);
              }

              if (c != null) {
              
                request.getSession().setAttribute("concept", c);
               
                
                request.getSession().setAttribute("code", code);
                request.getSession().setAttribute("ns", ns);
                name = "";
                if (c.getEntityDescription() != null) {
                  name = c.getEntityDescription().getContent();
                }
              } else {
                //name = "The server encountered an internal error that prevented it from fulfilling this request.";
                name = "ERROR: Invalid code - " + code + ".";
              }
            }


            if (DataUtils.isNCIT(dictionary)) {
              %>
              <%@ include file="/pages/templates/content-header-other.jsp"%>
              <%
            } else {
              request.getSession().setAttribute("dictionary", dictionary);
              %>
              <%@ include file="/pages/templates/content-header-other.jsp"%>
              <%
            }

            String tg_dictionary_0 = dictionary;
            String tg_dictionary = StringUtils.replaceAll(dictionary, " ", "%20");
            if (c != null) {
              request.getSession().setAttribute("type", type);
              request.getSession().setAttribute("singleton", "false");
              %>
              <!-- Page content -->
              <div class="pagecontentLittlePadding">

              <h:form style="margin:0px 0px 0px 0px;" acceptcharset="UTF-8">

              <table border="0" width="920px" style="margin:0px 0px 0px 0px;" role='presentation'>
              <tr class="global-nav">
              <td width="25%"></td>
              <td align="right" width="75%">
              <%
              Boolean[] isPipeDisplayed = new Boolean[] { Boolean.FALSE };
              boolean tree_access2 = !DataUtils.get_vocabulariesWithoutTreeAccessHashSet().contains(dictionary);
              boolean typeLink_isMapping2 = mappingTab.isMapping(dictionary, null);
              if (tree_access2 && !typeLink_isMapping2) {
                %>

                <%
                if (DataUtils.isNullOrBlank(ns)) {
                    ns = cd.getNamespaceByCode(dictionary, version, code);
                }
                %>
      
 <%
 if (DataUtils.isNullOrBlank(ns)) {
 %>
       
                            <a href="#" onClick="javascript:window.open('<%=request.getContextPath()%>/ajax?action=search_hierarchy&ontology_node_id=<%=code%>&ontology_display_name=<%=short_name%>&version=<%=version%>', '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">

 
 <%
 } else {
 %>
       
                            <a href="#" onClick="javascript:window.open('<%=request.getContextPath()%>/ajax?action=search_hierarchy&ontology_node_id=<%=code%>&ontology_node_ns=<%=ns%>&ontology_display_name=<%=short_name%>&version=<%=version%>', '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">

 
 <%
 }
 %>
                  
                  
                  
                  View in Hierarchy</a>
                <%= JSPUtils.getPipeSeparator(isPipeDisplayed) %>
                <%
              }

              boolean historyAccess = historyUtils.isHistoryServiceAvailable(dictionary);
              if (historyAccess) {
                %>
                <%= JSPUtils.getPipeSeparator(isPipeDisplayed) %>
                <a
                    href="#"
                    onClick="javascript:window.open('<%=request.getContextPath()%>/pages/concept_history.jsf?dictionary=<%=dictionary%>&version=<%=version%>&code=<%=code%>', '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">

                  View History</a>
              <% } %>

              <% if (view_graph) { %><%= JSPUtils.getPipeSeparator(isPipeDisplayed) %>
                <a
                    href="#"
                    onclick="javascript:popup_window('<%=request.getContextPath()%>/ajax?action=view_graph&scheme=<%=dictionary%>&version=<%=version%>&ns=<%=ns%>&code=<%=code%>&type=ALL', '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');"
                    tabindex="0"
                    title="This link displays a graph that recapitulates some information in the Relationships tab in a visual format.">

                  View Graph</a>
              <% } %>

              <%= JSPUtils.getPipeSeparator(isPipeDisplayed) %>

              <h:commandLink action="#{CartActionBean.addToCart}" value="Add to Cart">
                <f:setPropertyActionListener target="#{CartActionBean.entity}" value="concept" />
                <f:setPropertyActionListener target="#{CartActionBean.codingScheme}" value="dictionary" />
                <f:setPropertyActionListener target="#{CartActionBean.version}" value="version" />
              </h:commandLink>

              <c:choose>
                <c:when test="${sessionScope.CartActionBean.count>
                  0}"> (
                  <h:outputText value="#{CartActionBean.count}" />)
                </c:when>
              </c:choose>
              <%
              if (term_suggestion_application_url != null && term_suggestion_application_url
              .compareTo("") != 0) {
                %>
                <%= JSPUtils.getPipeSeparator(isPipeDisplayed) %>
                <a
                    href="<%=term_suggestion_application_url%>?dictionary=<%=HTTPUtils.cleanXSS(cd_dictionary)%>&code=<%=HTTPUtils.cleanXSS(code)%>"
                    target="_blank"
                    alt="Term Suggestion">

                  Suggest Changes</a>
              <% } %></td></tr></table>

        <input type="hidden" id="cart_dictionary" name="cart_dictionary" value="<%=HTTPUtils.cleanXSS(dictionary)%>" />
        <input type="hidden" id="cart_version" name="cart_version" value="<%=HTTPUtils.cleanXSS(version)%>" />
        <input type="hidden" id="cart_code" name="cart_code" value="<%=HTTPUtils.cleanXSS(code)%>" />

        <%
        String b = HTTPUtils.cleanXSS((String) request.getParameter("b"));
        String n = HTTPUtils.cleanXSS((String) request.getParameter("n"));
        String m = HTTPUtils.cleanXSS((String) request.getParameter("m"));
        String vse = HTTPUtils.cleanXSS((String) request.getParameter("vse"));

        // Floating Point Value Denial of Service threats fix:
        if (!StringUtils.isNull(b) && b.compareTo("0") != 0) {
          b = "1";
        }

        if (!StringUtils.isNull(n) && !StringUtils.isInteger(n)) {
          n = "1";
        }

        if (!StringUtils.isNull(m) && m.compareTo("0") != 0) {
          m = "1";
        }

        String key = HTTPUtils.cleanXSS((String) request.getParameter("key"));

        if (!StringUtils.isNull(vse)) {
          %>
          <input type="hidden" id="vse" name="vse" value="<%=vse%>" />
          <%
        }

        if (!StringUtils.isNull(b)) {
          if (StringUtils.isNull(n)) {
            n = "1";
          }

          request.getSession().setAttribute("b", b);
          request.getSession().setAttribute("n", n);
          request.getSession().setAttribute("key", key);
          %>
          <input type="hidden" id="b" name="b" value="<%=b%>" />
          <input type="hidden" id="n" name="n" value="<%=n%>" />
          <input type="hidden" id="key" name="key" value="<%=key%>" />

          <%
          if (!StringUtils.isNull(m)) {
            request.getSession().setAttribute("m", m);
            %>
            <input type="hidden" id="m" name="m" value="<%=m%>" />
            <%
          }
        }
        %>
      </h:form>

      <a name="evs-content" id="evs-content" tabindex="0"></a>
      <table border="0" cellpadding="0" cellspacing="0" width="100%" role='presentation'>
        <tr>
          <% if (namespace_list != null && namespace_list.size() > 1) { %>
            <td class="texttitle-blue">
              <%= HTTPUtils.cleanXSS(name) %> (Code <%= HTTPUtils.cleanXSS(code) %>; &nbsp;Namespace<%= ns %>)
            </td>
          <% } else { %>
            <td class="texttitle-blue"><%= HTTPUtils.cleanXSS(name) %> (Code <%= HTTPUtils.cleanXSS(code) %>)</td>
          <% } %>

          <td class="textbodyred">
            <%
            if (namespace_list != null && namespace_list.size() > 1) {
              String count_str = Integer.valueOf(namespace_list.size()).toString();
              count_str = "(Note: Code " + code + " is found in " + count_str + " different namespaces.)";
              %>
              <%= count_str %>            <% } %>
          </td>
        </tr>
        <%
        VisitedConceptUtils.add(request, tg_dictionary_0, version, ns, code, name);
        if (deprecatedVersion != null) {
          %>
          <tr>
            <td class="textbodysmall" colspan="2">
              <% if (deprecatedVersion.compareTo(version) == 0) { %>
                <font color="#A90101">Warning:</font>
                Requested version is not accessible. Displaying version<%= version %>
                of this concept instead.
              <% } else { %>

                <font color="#A90101">Warning:</font>
                Version<%= deprecatedVersion %> of this vocabulary is not accessible. Displaying version<%= version %>
                of this concept instead.
              <% } %>

            </td>
          </tr>
        <% } %>
      </table>
      <hr>
      <%
 /*     
      
      request.getSession().setAttribute("concept", c);
      request.getSession().setAttribute("code", code);
      request.getSession().setAttribute("ns", ns);
      request.setAttribute("version", version);
      
*/   



      %>
      <%@ include file="/pages/templates/typeLinks.jsp" %>
      <div class="tabTableContentContainer">
        <%
        if (type != null && type.compareTo("all") == 0) {
          boolean isMappingCD = mappingTab.isMapping(dictionary,version);
          %>
          <h1 class="textsubtitle-blue">Table of Contents</h1>
          <ul>
            <li>
            <a href="#properties">Terms &amp; Properties</a>
          </li>
          <li>
          <a href="#synonyms">Synonym Details</a>
        </li>
        <li>
        <a href="#relationships">Relationships</a>
      </li>      <% if (!isMappingCD) { %>
        <li>
        <a href="#mappings">Mapping Details</a>
        </li>      <% } %></ul>
      <br>
    <% } %>    
    
    <!--
    //////////////////SYNONYMS////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    -->

<%
        

HashMap def_map = null;
int other_src_alt_def_count = 0;
Vector nci_def_label_value = new Vector();
Vector non_nci_def_label_value = new Vector();
Vector other_label_value = new Vector();
boolean is_definition = false;

Vector ncim_metathesaurus_cui_vec = new Vector();
HashSet ncim_metathesaurus_cui_hset = new HashSet();

String ncim_cui_propName = "NCI_META_CUI";
String umls_cui_propName = "UMLS_CUI";
String ncim_cui_propName_label = null;
String ncim_cui_prop_url = null;
String ncim_cui_prop_linktext = null;
Vector ncim_cui_code_vec = new Vector();

List displayItemList = null;

Entity curr_concept = null;
String codingScheme = dictionary;
String namespace = ns;
Boolean isActive = null;
String concept_status = null;
Vector properties_to_display = null;
Vector properties_to_display_label = null;
Vector properties_to_display_url = null;
Vector properties_to_display_linktext = null;
Vector additionalproperties = null;
Vector external_source_codes = null;
Vector external_source_codes_label = null;
Vector external_source_codes_url = null;
Vector external_source_codes_linktext = null;
Vector descendantCodes = null;
HashMap propertyName2ValueHashMap = null;
HashMap propertyQualifierHashMap = null;
HashMap displayLabel2PropertyNameHashMap = null;

Vector displayed_properties = new Vector();
Vector presentation_vec = new Vector();
String concept_id = code;


try {
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
curr_concept = (Entity) request.getSession().getAttribute("concept");



  //if (curr_concept != null) {
  
  /*
    String property_data_key = (String) request.getSession().getAttribute("property_data_key");
    //if (property_data_key == null || property_data_key.compareTo(curr_concept.getEntityCode()) != 0) {
        try {
		  request.getSession().removeAttribute("propertyData");
		  propertyData = new PropertyData(lbSvc, cs_name, version);
		
		  propertyData.set_owl_role_quantifiers(NCItBrowserProperties.get_owl_role_quantifiers());
		  def_map = NCItBrowserProperties.getDefSourceMappingHashMap();
		  propertyData.setDefSourceMapping(def_map);

		  displayItemList = NCItBrowserProperties.getInstance().getDisplayItemList();
		  propertyData.setDisplayItemList(displayItemList);		
		
		propertyData.set_owl_role_quantifiers(NCItBrowserProperties.get_owl_role_quantifiers());
		propertyData.setCurr_concept(curr_concept);
		request.getSession().setAttribute("property_data_key", curr_concept.getEntityCode());
	} catch (Exception ex) {
		ex.printStackTrace();
	}
    //}
    */


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 

    propertyData = (PropertyData) request.getSession().getAttribute("propertyData");
    
    /*
    codingScheme = propertyData.getCodingScheme();
    dictionary = codingScheme;
    
    version = propertyData.getVersion();
    code = propertyData.getCode();
    namespace = propertyData.getNamespace();
    */
  
    ns = namespace;
    
    isActive = propertyData.getIsActive();
    concept_status = propertyData.getConcept_status();
    

    properties_to_display_label = propertyData.getProperties_to_display_label();
    properties_to_display_url = propertyData.getProperties_to_display_url();
    properties_to_display_linktext = propertyData.getProperties_to_display_linktext();
    additionalproperties = propertyData.getAdditionalproperties();
    external_source_codes = propertyData.getExternal_source_codes();
    external_source_codes_label = propertyData.getExternal_source_codes_label();
    external_source_codes_url = propertyData.getExternal_source_codes_url();
    external_source_codes_linktext = propertyData.getExternal_source_codes_linktext();
    descendantCodes = propertyData.getDescendantCodes();
    propertyName2ValueHashMap = propertyData.getPropertyName2ValueHashMap();
    propertyQualifierHashMap = propertyData.getPropertyQualifierHashMap();
    displayLabel2PropertyNameHashMap = propertyData.getDisplayLabel2PropertyNameHashMap();

    concept_id = propertyData.get_concept_id();
    presentation_vec = propertyData.get_presentation_vec();
    properties_to_display = propertyData.getProperties_to_display();
    
    request.getSession().setAttribute("code", curr_concept.getEntityCode());


  //}

} catch (Exception ex) {
  ex.printStackTrace();
}



if (type == null) {
	type = "properties";
}


    
//if ((type.compareTo("properties") == 0 || type.compareTo("all") == 0) && displayItemList != null && curr_concept != null) {
if (type.compareTo("properties") == 0 || type.compareTo("all") == 0) {


  %>
  <table border="0" width="708px" role='presentation'>
    <tr>
      <td class="textsubtitle-blue" align="left">
        <% if (type != null && type.compareTo("all") == 0) { %>
          <A name="properties">Terms & Properties</A>
        <% } else { %> Terms & Properties        <% } %>
      </td>
    </tr>
  </table>
  <%
  
  
boolean show_status = propertyData.get_show_status();
isActive = propertyData.getIsActive();


if ((isActive != null && !isActive.equals(Boolean.TRUE)  && concept_status != null) || (concept_status != null && concept_status.compareTo("null") != 0 && show_status)) {


    %>
    <p class="textbody">
      <b>Concept Status:</b>&nbsp;
      <i class="textbodyred"><%= concept_status %></i>
      <%
     
      
      if (descendantCodes != null) {
        if (descendantCodes.size() > 0) {
         
        
          String link = "&nbsp;(See:&nbsp;";
         
          
          %>
          <%= link %>
          <%
          String coding_scheme_name = ns;//mappingTab.getCSName(dictionary);
          for (int i=0; i<descendantCodes.size(); i++) {
            String t = (String) descendantCodes.elementAt(i);
            Vector w = StringUtils.parseData(t);
            String descendantName = (String) w.elementAt(0);
            String descendantCode = (String) w.elementAt(1);
            
            %>
            <a
                href="<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=coding_scheme_name%>&ns=<%=coding_scheme_name%>&code=<%=descendantCode%>">

              <%= descendantName %>
            </a>
            <%
          }
          link = ")";
          %>
          <%= link %>/p>
          <%
        }
      }
      %>
      <%
    }
    else if (concept_status != null && concept_status.compareToIgnoreCase("Retired Concept") != 0) {

      %>
      <p class="textbody">
        <b>Concept Status:</b>&nbsp;
        <i class="textbody"><%= concept_status %></i>
      </p>
    <% } %>
    <%
    //[#26722] Support cross-linking of individual source vocabularies with NCI Metathesaurus.
    HashMap<String, String> label2URL = new HashMap<String, String>();
    HashMap<String, String> label2Linktext = new HashMap<String, String>();
    for (int m=0; m<properties_to_display.size(); m++) {
      String propName = (String) properties_to_display.elementAt(m);
      String prop_nm_label = (String) properties_to_display_label.elementAt(m);
      String prop_url = (String) properties_to_display_url.elementAt(m);
      String prop_linktext = (String) properties_to_display_linktext.elementAt(m);
      if (prop_url != null) {
        label2URL.put(prop_nm_label, prop_url);
        label2Linktext.put(prop_nm_label, prop_linktext);
      }
    }

    for (int i=0; i<properties_to_display.size(); i++) {
      String propName = (String) properties_to_display.elementAt(i);
      String propName_label = (String) properties_to_display_label.elementAt(i);

      if (propName_label.compareTo("NCI Thesaurus Code") == 0  && propName.compareTo("NCI_THESAURUS_CODE") != 0) {
        //String formalName = mappingTab.getFormalName(dictionary);
        if (formalName == null) {
          formalName = dictionary;
        }
        propName_label = formalName + " Code";
      }

      String propName_label2 = propName_label;
      String url = (String) properties_to_display_url.elementAt(i);
      String linktext = (String) properties_to_display_linktext.elementAt(i);

      //KLO 102611
      //if (propName.compareTo(ncim_cui_propName) == 0 || propName.compareTo(umls_cui_propName) == 0) {
        if (propName.compareTo(ncim_cui_propName) == 0) {
          ncim_cui_propName_label = propName_label;
          ncim_cui_prop_url = url;
          ncim_cui_prop_linktext = linktext;

          Vector ncim_cui_code_vec_temp = cd.getPropertyValues(
          curr_concept, "GENERIC", propName);
          if (ncim_cui_code_vec_temp != null) {
            for (int lcv=0; lcv<ncim_cui_code_vec_temp.size(); lcv++) {
              String t = (String) ncim_cui_code_vec_temp.elementAt(lcv);
              ncim_cui_code_vec.add(t);
            }
          }
        }

        String qualifier = "";

        if (propName_label.indexOf("Synonyms") == -1) {

          displayed_properties.add(propName);
          propertyData.add_displayed_property(propName);

          Vector value_vec = (Vector) propertyName2ValueHashMap.get(propName);

          if (value_vec != null && value_vec.size() > 0) {

            int k = -1;
            for (int j=0; j<value_vec.size(); j++) {
              String value = (String) value_vec.elementAt(j);
              k++;

              if (propName.compareTo("NCI_META_CUI") == 0) {
                ncim_cui_code_vec.add(value);
              }

              if(propName_label.compareTo("Definition") == 0) {
                String value_pre = value;
                value = JSPUtils.reformatPDQDefinition(value);
                String value_post = value;
                // Send redirect:
                if (value_post == null) {
                  try {
                    String error_msg = "WARNING: The server encountered an unexpected error (file: property.jsp, code: 1, var: value_post).";
                    request.getSession().setAttribute("error_msg", error_msg);
                    String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
                    response.sendRedirect(redirectURL);
                  } catch (Exception ex) {
                    ex.printStackTrace();
                  }
                }
                if (value_pre.compareTo(value_post) != 0 && !value_post.endsWith("PDQ")) {
                    //System.out.println("WARNING -- possible definition formatting issue with " + value_pre);
                }
              }

              String value_wo_qualifier = value;
              int n = value.indexOf("|");

              is_definition = false;
              if (n != -1 && (propName_label.indexOf("Definition") != -1 || propName_label.indexOf("DEFINITION") != -1 || propName_label.indexOf("definition") != -1)) {
                is_definition = true;
                Vector def_vec = StringUtils.parseData(value);
                value_wo_qualifier = (String) def_vec.elementAt(0);
                qualifier = "";
                if (def_vec.size() > 1) {
                  qualifier = (String) def_vec.elementAt(1);
                }

                if (def_map != null && def_map.containsKey(qualifier)) {
                  String def_source_display_value = (String) def_map.get(qualifier);
                  value = value_wo_qualifier + " (" + qualifier + ")";
                  propName_label = def_source_display_value + " " + propName_label2;

                } else {
                  if (qualifier.indexOf("PDQ") != -1) {
                    //value = JSPUtils.reformatPDQDefinition(value);
                  } else if (qualifier.compareTo("NCI") != 0) {

                    value = value_wo_qualifier;
                    propName_label = qualifier + " " + propName_label2;

                  } else if (qualifier.compareTo("NCI") == 0 && propName.compareTo("ALT_DEFINITION") == 0) {
                    value = value_wo_qualifier;
                    if (other_src_alt_def_count > 0) {
                      propName_label = qualifier + " " + propName_label2;
                    } else {
                      propName_label = propName_label2;
                    }

                  } else if (qualifier.compareTo("NCI") == 0 && propName.compareTo("ALT_DEFINITION") != 0) {
                    value = value_wo_qualifier;
                    propName_label = propName_label2;

                  } else {
                    value = value_wo_qualifier;
                  }
                }
                if (qualifier.compareToIgnoreCase("NCI") == 0) {
                  nci_def_label_value.add(propName_label2 + "|" + value);
                } else {
                  non_nci_def_label_value.add(propName_label + "|" + value);
                }
              }

              if (propName_label.indexOf("textualPresentation") == -1) {
                if (!is_definition) {
                  other_label_value.add(propName_label + "|" + value);
                }
              }
            }
          }
        }
      }
      %>

      <%

      for (int i_def = 0; i_def<other_label_value.size(); i_def++) {
        String label_value = (String) other_label_value.elementAt(i_def);
        Vector u = StringUtils.parseData(label_value);
        String propName_label = (String) u.elementAt(0);

        if (propName_label.compareToIgnoreCase("Preferred Name") == 0) {
          String value = (String) u.elementAt(1);
          %>
          <p>
            <b><%= propName_label %>:&nbsp;</b>
            <%= value %>            <% if (!DataUtils.isNCIT(dictionary)) { %>
              <%= PropertyData.getDisplayLink(label2URL, label2Linktext, propName_label, value) %>
              <%
            }
            String qualifier_str = propertyData.getPropertyQualifierString(propName_label, value);
            
            if (qualifier_str != null) {
              %>
              <%= qualifier_str %>            <% } %>
          </p>
          <%
        }
      }
      %>

      <%
      
      for (int i_def = 0; i_def<nci_def_label_value.size(); i_def++) {
        String label_value = (String) nci_def_label_value.elementAt(i_def);
        Vector u = StringUtils.parseData(label_value);
        String propName_label = (String) u.elementAt(0);
        String value = (String) u.elementAt(1);
        %>
        <p>
          <b><%= propName_label %>:&nbsp;</b>
          <%= value %>          <% if (!DataUtils.isNCIT(dictionary)) { %>
            <%= PropertyData.getDisplayLink(label2URL, label2Linktext, propName_label, value) %>
            <%
          }
          String qualifier_str = propertyData.getPropertyQualifierString(propName_label, value);
          
          if (qualifier_str != null) {
            %>
            <%= qualifier_str %>          <% } %>
        </p>
      <% } %>

      <%


      for (int i_def = 0; i_def<non_nci_def_label_value.size(); i_def++) {
        String label_value = (String) non_nci_def_label_value.elementAt(i_def);

        Vector u = StringUtils.parseData(label_value);
        String propName_label = (String) u.elementAt(0);
        String value = (String) u.elementAt(1);
        %>
        <p>
          <b><%= propName_label %>:&nbsp;</b>
          <%= value %>          <% if (!DataUtils.isNCIT(dictionary)) { %>
            <%= PropertyData.getDisplayLink(label2URL, label2Linktext, propName_label, value) %>
            <%
          }
          String qualifier_str = propertyData.getPropertyQualifierString(propName_label, value);
          
          
          if (qualifier_str != null) {
            %>
            <%= qualifier_str %>          <% } %>
        </p>
      <% } %>

      <%
      
      for (int i_def = 0; i_def<other_label_value.size(); i_def++) {
        String label_value = (String) other_label_value.elementAt(i_def);
        Vector u = StringUtils.parseData(label_value);
        String propName_label = (String) u.elementAt(0);

        if (propName_label.compareToIgnoreCase("Preferred Name") != 0) {
          String value = (String) u.elementAt(1);
          %>
          <p>
            <b><%= propName_label %>:&nbsp;</b>
            <%= value %>            <% if (!DataUtils.isNCIT(dictionary)) { %>
              <%= PropertyData.getDisplayLink(label2URL, label2Linktext, propName_label, value) %>

              <%
              String qualifier_str = propertyData.getPropertyQualifierString(propName_label, value);
             
              
              if (qualifier_str != null) {
                %>
                <%= qualifier_str %>              <% } %>

            <% } else if (propName_label.equalsIgnoreCase("NCI Thesaurus Code")) { %>

              <%= PropertyData.getDisplayLink(label2URL, label2Linktext, "caDSR metadata", value) %>
              <%
              if (ValueSetDefinitionConfig.isValueSetHeaderConcept(curr_concept.getEntityCode())) {
                String vs_uri = ValueSetDefinitionConfig.getValueSetURI(curr_concept.getEntityCode());
                String url_str = request.getContextPath() + "/ajax?action=create_src_vs_tree&vsd_uri=" + vs_uri;
                url_str = url_str.replaceAll(":", "%3A");
                String linktext = "see linked value set";
                %>
                &nbsp;
                <a href="<%= url_str %>">(<%= linktext %>)</a>
              <% } %>
              <%
              //////////////////////////////
              String vs_uri_2 = ValueSetDefinitionConfig.getValueSetURI(curr_concept.getEntityCode());
              String url_str_2 = request.getContextPath() + "/ajax?action=search_all_value_sets&code=" + curr_concept.getEntityCode();
              url_str_2 = url_str_2.replaceAll(":", "%3A");
              String linktext_2 = "search value sets";
              %>
              &nbsp;
              <a href="<%= url_str_2 %>">(<%= linktext_2 %>)</a>

            <% } %>
          </p>
          <%
        }
      }
      %>

      <%
     
      ncim_metathesaurus_cui_vec = cd.getNCImCodes(curr_concept);
      
      //String ncimURL = new ConceptDetails().getNCImURL();
      String ncimURL = DataUtils.getNCImURL();
      if (ncim_metathesaurus_cui_vec.size() > 0) {
        if (ncim_metathesaurus_cui_vec.size() == 1) {
          String t = (String) ncim_metathesaurus_cui_vec.elementAt(0);
          String t0 = "NCI Metathesaurus Link";
          String t1 = t;
          String t2 = ncimURL + "ncimbrowser/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=" + t;
          if (ncimURL.endsWith("ncimbrowser")) {
            //t2 = new ConceptDetails().getNCImURL() + "/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=" + t;
            t2 = DataUtils.getNCImURL() + "/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=" + t;
          }
          String t3 = "see NCI Metathesaurus info";
          %>
          <p>
            <b><%= t0 %>:&nbsp;</b>
            <%= t1 %>&nbsp;
            <a href="javascript:redirect_site('<%= t2 %>')">(<%= t3 %>)</a>
          </p>
        <% } else { %>
          <table class="datatable_960">

            <b>NCI Metathesaurus CUI:</b>
            <%
            for (int k=0; k<ncim_metathesaurus_cui_vec.size(); k++) {
              int lcv = k;

              if ((lcv++) % 2 == 0) {
                %>
                <tr class="dataRowLight">
                <% } else { %>
                  <tr class="dataRowLight">
                    <%
                  }

                  String t = (String) ncim_metathesaurus_cui_vec.elementAt(k);
                  String t0 = "NCI Metathesaurus Link";
                  String t1 = t;
                  String t2 = ncimURL + "ncimbrowser/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=" + t;
                  if (ncimURL.endsWith("ncimbrowser")) {
                    //t2 = new ConceptDetails().getNCImURL() + "/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=" + t;
                    t2 = DataUtils.getNCImURL() + "/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=" + t;
                  }
                  String t3 = "see NCI Metathesaurus info";
                  %>
                  <td>
                    <%= t1 %>&nbsp;
                    <a href="javascript:redirect_site('<%= t2 %>')">(<%= t3 %>)</a>
                  </td>

                </tr>
              <% } %>
          </table>
          <%
        }
      }
      %>
      <p>
        <b>Synonyms &amp; Abbreviations:</b>
        <a
            href="<%=request.getContextPath() %>/pages/concept_details.jsf?dictionary=<%=scheme%>&code=<%=id%>&type=synonym">

          (see Synonym Details)</a>

        <table class="datatable_960">
          <%
          HashSet hset2 = new HashSet();
          Vector synonym_values = new Vector();

          for (int i=0; i<presentation_vec.size(); i++) {
            String t = (String) presentation_vec.elementAt(i);
           
            
            Vector w = StringUtils.parseData(t, "$");
            String presentation_name = (String) w.elementAt(0);
            String presentation_value = (String) w.elementAt(1);
            
            String isPreferred = (String) w.elementAt(2);

            displayed_properties.add(presentation_name);
            propertyData.add_displayed_property(presentation_name);

            if (!hset2.contains(presentation_value)) {
              synonym_values.add(presentation_value);
              hset2.add(presentation_value);
            }
            synonym_values = new SortUtils().quickSort(synonym_values);
          }


          int row=0;
          for (int j=0; j<synonym_values.size(); j++) {
            String value = (String) synonym_values.elementAt(j);
            if ((row++) % 2 == 0) {
              %>
              <tr class="dataRowDark">
              <% } else { %>
                <tr class="dataRowLight">
                <% } %>
                <td><%= cd.encodeTerm(value) %></td>
              </tr>
            <% } %>
        </table>
      </p>

      <p>

        <%
        int n = 0;
        boolean hasExternalSourceCodes = false;
        boolean display_UMLS_CUI = true;
        String dict_name = (String) request.getSession().getAttribute("dictionary");
        
        
        String vocab_format = mappingTab.getMetadataValue(dict_name, null, "format");
        
        
        if (vocab_format != null && vocab_format.compareTo("RRF") == 0) {
          display_UMLS_CUI = false;
        }


        if (external_source_codes != null && external_source_codes.size() != 0) {
        
        
          for (int i=0; i<external_source_codes.size(); i++) {
            String propName = (String) external_source_codes.elementAt(i);
            String propName_label = (String) external_source_codes_label.elementAt(i);
            String prop_url = (String) external_source_codes_url.elementAt(i);
            String prop_linktext = (String) external_source_codes_linktext.elementAt(i);

            if (propName.compareTo("UMLS_CUI") != 0 || display_UMLS_CUI) {
              Vector value_vec = (Vector) propertyName2ValueHashMap.get(propName);
              if (value_vec != null && value_vec.size() > 0) {
                hasExternalSourceCodes = true;
                break;
              }
            }
          }
        }

        if (!hasExternalSourceCodes) {
          %>
          <b>External Source Codes</b>:&nbsp;
          <i>None</i>
        <% } else { %>
          <b>External Source Codes:&nbsp;</b>
          <table class="datatable_960">

            <col width="20%">
              <col width="80%">

                <%
                n = 0;
                
                
                if (external_source_codes != null) {
                
                for (int i=0; i<external_source_codes.size(); i++) {
                  String propName = (String) external_source_codes.elementAt(i);
                  String propName_label = (String) external_source_codes_label.elementAt(i);
                  String prop_url = (String) external_source_codes_url.elementAt(i);
                  String prop_linktext = (String) external_source_codes_linktext.elementAt(i);

                  displayed_properties.add(propName);
                  propertyData.add_displayed_property(propName);

                  if (propName.compareTo("UMLS_CUI") != 0 || display_UMLS_CUI) {

                    Vector value_vec = (Vector) propertyName2ValueHashMap.get(propName);
                    if (value_vec != null && value_vec.size() > 0) {
                      for (int j=0; j<value_vec.size(); j++) {
                        String value = (String) value_vec.elementAt(j);

                        if (n % 2 == 0) {
                          %>
                          <tr class="dataRowDark">
                          <% } else { %>
                            <tr class="dataRowLight">
                              <%
                            }
                            n++;
                            %>
                            <td><%= propName_label %></td>
                            <td>
                              <%= cd.encodeTerm(value) %>
                              <%
                              if (propName.compareTo("UMLS_CUI") != 0 && prop_url != null && prop_url.compareTo("null") != 0) {
                                String url_str = prop_url + value;
                                %>
                                <a href="javascript:redirect_site('<%= url_str %>')">(<%= prop_linktext %>)</a>
                              <% } %>
                            </td>
                          </tr>
                          <%
                        }
                      }
                    }
                  }
                  %>
                </table>

              <% } %>
            <% } %>
            </p>
            <p>
              <%
              boolean hasOtherProperties = false;
              Vector other_prop_names = propertyData.findOtherPropertyNames();
            
              if (other_prop_names != null && other_prop_names.size() > 0) {
                hasOtherProperties = true;
              }
              if (!hasOtherProperties) {
                %>
                <b>Other Properties</b>:&nbsp;
                <i>None</i>
                <%
              } else {
              
                String other_properties_str = propertyData.generatePropertyTable(curr_concept,
                other_prop_names, "<b>Other Properties:</b>");
              
                
                %>
                <p><%= other_properties_str %></p>
              <% } %>
            </p>
            <p>
              <%
              String concept_name = "";
              if (curr_concept.getEntityDescription() != null) {
                concept_name = curr_concept.getEntityDescription().getContent();
                concept_name = concept_name.replaceAll(" ", "_");
              }

              String concept_name_label = "Concept Name:";
              String dict = (String) request.getSession().getAttribute("dictionary");

              String primitive = null;
              String primitive_prop_name = "primitive";
              String primitive_label = "Defined Fully by Roles:";
              String defined_label = "Defined Fully by Roles:";

              dict = mappingTab.getFormalName(dict);

              String vocabulary_format = mappingTab.getMetadataValue(dict, null, "format");
              Boolean isDefined = null;
              String is_defined = "No";
              if (vocabulary_format != null && vocabulary_format.indexOf("OWL") != -1) {
                isDefined = curr_concept.getIsDefined();
              }
              String kind = "not available";
              String kind_prop_name = "Kind";
              String kind_label = "Kind:";
              %>

              <%
              if (isDefined != null) {
                if (isDefined.equals(Boolean.TRUE)) is_defined = "Yes";
                %>
                <b>Additional Concept Data:</b>&nbsp;
                <table class="datatable_960">
                  <tr class="dataRowLight">
                    <td><%= defined_label %>&nbsp;<%= is_defined %></td>
                    <td>&nbsp;</td>
                  </tr>
                </table>
              <% } else { %>
                <b>Additional Concept Data:</b>&nbsp;
                <i>None</i>
              <% } %>
            </p>
            <%
            
            
            
            //String url = JSPUtils.getBookmarkUrl(request, dictionary, version, concept_id, namespace);
            String url = JSPUtils.getBookmarkUrl(lbSvc, request, dictionary, version, namespace, concept_id);
 

            String bookmark_title = dictionary + "%20" + concept_id;
            %>
            <p>
              <table class="datatable_960" border="0" cellpadding="0" cellspacing="0" width="700px">
                <tr>
                  <td class="dataRowLight">URL:<%= url %></td>
                </tr>
              </table>
            </p>
          <% } %>

    <!--
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    -->
 
 <%
 String source_header = metadataUtils.getMetadataValue(dictionary, null, null, "source_header");
 
 String term_type_header = metadataUtils.getMetadataValue(
 dictionary, null, null, "term_type_header");
 
  Vector localname_vec = DataUtils.getLocalNames(formalName);
 boolean has_subsource = false;
 boolean display_subsource = true; // turned on or off
 
 if (type.compareTo("synonym") == 0 || type.compareTo("all") == 0)
 {
   %>
   <table border="0" width="100%" role='presentation'>
     <tr>
       <td class="textsubtitle-blue" align="left">
 
         <% if (type != null && type.compareTo("all") == 0) { %>
           <A name="synonyms">Synonym Details</A>
         <% } else { %> Synonym Details
           <%
         }
 
         Vector synonyms = cd.getSynonyms(dictionary, curr_concept);
 
         // check if subsource exists
 
         for (int lcv=0; lcv<synonyms.size(); lcv++)
         {
           String s = (String) synonyms.elementAt(lcv);
           Vector synonym_data = StringUtils.parseData(s, "|");
           // Send redirect:
           if (synonym_data == null) {
             try {
               String error_msg = "WARNING: The server encountered an unexpected error (file: synonym.jsp, code: 1, var: synonym_data).";
               request.getSession().setAttribute("error_msg", error_msg);
               String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
               response.sendRedirect(redirectURL);
             } catch (Exception ex) {
               ex.printStackTrace();
             }
           }
           if (synonym_data.size() > 4) {
             String subsource_nm = (String) synonym_data.elementAt(4);
             if (!StringUtils.isNullOrBlank(subsource_nm)) {
               has_subsource = true;
               break;
             }
           }
         }
         %>
       </td>
     </tr>
   </table>
   <div>
     <table class="datatable_960" border="0" width="100%">
       <tr>
         <th class="dataTableHeader" scope="col" align="left">Term</th>
         <th class="dataTableHeader" scope="col" align="left">
           Source          <% if (source_header != null && source_header.length() > 0) { %>
             <a
                 href="#"
                 onclick="javascript:window.open('<%=request.getContextPath()%>/pages/source_help_info.jsf?dictionary=<%=dictionary%>', '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
 
               <img src="<%= request.getContextPath() %>/images/help.gif" alt="Source Definitions" border="0">
             </a>
           <% } %>
         </th>
         <th class="dataTableHeader" scope="col" align="left">
           Type          <% if (term_type_header != null && term_type_header.length() > 0) { %>
             <a
                 href="#"
                 onclick="javascript:window.open('<%=request.getContextPath()%>/pages/term_type_help_info.jsf?dictionary=<%=dictionary%>', '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
 
               <img src="<%= request.getContextPath() %>/images/help.gif" alt="Term Type Definitions" border="0">
             </a>
           <% } %>
         </th>
         <th class="dataTableHeader" scope="col" align="left">Code</th>
 
         <% if (display_subsource && has_subsource) { %>
           <th class="dataTableHeader" scope="col" align="left">Subsource Name</th>
         <% } %>
       </tr>
 
       <%
       HashSet hset = new HashSet();
       int n = -1;
       for (int lcv=0; lcv<synonyms.size(); lcv++)
       {
         String s = (String) synonyms.elementAt(lcv);
         if (!hset.contains(s)) {
           hset.add(s);
           n++;
           Vector synonym_data = StringUtils.parseData(s, "|");
           String term_name = (String) synonym_data.elementAt(0);
           //term_name = term_name.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
           String term_type = (String) synonym_data.elementAt(1);
           String term_source = (String) synonym_data.elementAt(2);
           String term_source_formal_name = DataUtils.getFormalNameByDisplayName(term_source);
 
           //Test case: NCIt, ADCS-ADL MCI - Balance Checkbook (Code C106898)
           if (term_source_formal_name == null)
           term_source_formal_name = mappingTab.getFormalName(term_source);
 
           if (term_source.equalsIgnoreCase("nci"))
           term_source_formal_name = "NCI Thesaurus";
 
           //GF#33194 Empty string source codes break Synonym Details and View All
           String term_source_code = null;
           if (synonym_data.size() > 3) {
             term_source_code = (String) synonym_data.elementAt(3);
           }
 
           String term_subsource_name = null;
           if (synonym_data.size() > 4) {
             term_subsource_name = (String) synonym_data.elementAt(4);
           }
 
           String rowColor = (n%2 == 0) ? "dataRowDark" : "dataRowLight";
           %>
           <tr class="<%=rowColor%>">
             <td class="dataCellText" scope="row"><%= DataUtils.encodeTerm(term_name) %></td>
             <td class="dataCellText"><%= term_source %></td>
             <td class="dataCellText"><%= term_type %></td>
             <%
             //String formalname_term_source = mappingTab.getFormalName(term_source);
             boolean hyperlink = false;
             if (term_source != null && !localname_vec.contains(term_source)) {
               hyperlink = true;
             }
 
             if (!DataUtils.isNull(term_source_formal_name) && !DataUtils.isNull(term_source_code) && hyperlink) {
               String term_source_nm = DataUtils.getCSName(term_source_formal_name);
               String url_str = request.getContextPath() +
               "/pages/concept_details.jsf?dictionary=" +
               term_source_nm + "&code=" + term_source_code;
               %>
               <td>
                 <a href="<%= url_str %>"><%= term_source_code %></a>
               </td>
               <%} else if (!StringUtils.isNullOrBlank(term_source_code)) {%>
                 <td class="dataCellText"><%= term_source_code %></td>
                 <%} else { %>
                   <td class="dataCellText">&nbsp;</td>
                   <%} %>
 
                     <%
                     if (display_subsource && has_subsource) {
                       if (!StringUtils.isNullOrBlank(term_subsource_name)) {
                         %>
                         <td class="dataCellText"><%= term_subsource_name %></td>
                       <% } else { %>
                         <td class="dataCellText">&nbsp;</td>
                         <%
                       }
                     }
                     %>
 
                   </tr>
                   <%
                 }
               }
               %>
             </table></div>        <% } %>

 
    <!--
    /////////////////RELATIONSHIPS/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    -->

 
 <%
  
 if (type.compareTo("relationship") == 0 || type.compareTo("all") == 0)
 {

 
   JSPUtils.JSPHeaderInfo relationship_info = new JSPUtils.JSPHeaderInfo(request);
   Entity concept_curr = (Entity) request.getSession().getAttribute("concept");
   /*
   String scheme_curr = relationship_info.dictionary;
   String version_curr = relationship_info.version;
   String version_parameter = "";
   String cNamespace = concept_curr.getEntityCodeNamespace();
   */
 
String scheme_curr = dictionary;
String version_curr = version;
String version_parameter = "";
String cNamespace = ns;
String code_curr = code;//(String) request.getSession().getAttribute("code");

 
   boolean owl2_display = false;
    if (version_curr != null && ! version_curr.equalsIgnoreCase("null")) {
     version_parameter = "&version=" + version_curr;
   }
 
   boolean isMapping = mappingTab.isMapping(scheme_curr, version_curr);
   
/*   
 
   if (code_curr == null) {
     code_curr = HTTPUtils.cleanXSS((String) request.getParameter("code"));
     //ns_curr = HTTPUtils.cleanXSS((String) request.getParameter("ns"));
   }
 
   String ns_curr = cd.getNamespaceByCode(scheme_curr, version_curr, code_curr);//(String) request.getSession().getAttribute("ns");
*/ 
   String ns_curr = ns;
 

   String rel_display_name = mappingTab.getMetadataValue(scheme_curr, version_curr, "display_name");
   
   if (rel_display_name == null) rel_display_name = DataUtils.getLocalName(scheme_curr);
 
   
   // Relationship tab failed to render correctly.
   if (ns_curr == null || ns_curr.compareTo("null") == 0 || ns_curr.compareTo("undefined") == 0) {
     ns_curr = cd.getNamespaceByCode(scheme_curr, version_curr, code_curr);
   }
     
   
   String key = scheme_curr + "$" + version_curr + "$" + code_curr;
   if (!DataUtils.isNullOrBlank(ns_curr)) {
     key = key + "$" + ns_curr;
   }
 
     HashMap hmap = propertyData.getRelationshipHashMap();
 
     ArrayList superconcepts = (ArrayList) hmap.get(Constants.TYPE_SUPERCONCEPT);
     ArrayList subconcepts = (ArrayList) hmap.get(Constants.TYPE_SUBCONCEPT);
     ArrayList roles = (ArrayList) hmap.get(Constants.TYPE_ROLE);
     ArrayList associations = (ArrayList) hmap.get(Constants.TYPE_ASSOCIATION);
     ArrayList inverse_roles = (ArrayList) hmap.get(Constants.TYPE_INVERSE_ROLE);
     ArrayList inverse_associations = (ArrayList) hmap.get(Constants.TYPE_INVERSE_ASSOCIATION);
 
     ArrayList concepts = null;
     String label = "";
     String rel = "";
     String score = "";
     String scheme_curr_0 = scheme_curr;
     String scheme_curr_nm = mappingTab.getCSName(scheme_curr);

     String associationName = "subClassOf";
     boolean direction = true;
     ArrayList arrayList = null;
     String parent_table_str = null;
     String child_table_str = null;
     %>
     <table class="datatable_960" border="0" width="100%">
       <tr>
         <td class="textsubtitle-blue" align="left">
 
           <% if (type != null && type.compareTo("all") == 0) { %>
             <A name="relationships">Relationships with other&nbsp;<%= DataUtils.encodeTerm(rel_display_name)%>&nbsp;Concepts</A>
           <% } else { %> Relationships with other&nbsp;<%=rel_display_name%>&nbsp;Concepts          <% } %>
         </td>
       </tr>
     </table>
     <%
     if (!isMapping) {
 
       propertyData.setRelationshipHashMap(hmap);
       if (owl2_display) {
         arrayList = relationshipUtils.getRelationshipData(scheme_curr, version_curr, ns_curr, code_curr, associationName, direction);
       }
       if (owl2_display && arrayList != null) {
         parent_table_str = propertyData.generateRelationshipTable(scheme_curr, version_curr, code_curr, ns_curr, Constants.TYPE_SUPERCONCEPT, true, arrayList);
         %>
         <p><%= parent_table_str %></p>
       <% } else { %>
         <p>
           <%
           parent_table_str = new RelationshipTabFormatter(lbSvc).formatSingleColumnTable(scheme_curr, Constants.TYPE_SUPERCONCEPT, superconcepts);
           %>
           <p><%= parent_table_str %></p>
         <% } %>
       </p>
       <%
       propertyData.setRelationshipHashMap(hmap);
       direction = false;
       if (owl2_display) {
         arrayList = relationshipUtils.getRelationshipData(scheme_curr, version_curr, ns_curr, code_curr, associationName, direction);
       }
       if (owl2_display && arrayList != null) {
         child_table_str = propertyData.generateRelationshipTable(scheme_curr, version_curr, code_curr, ns_curr, Constants.TYPE_SUBCONCEPT, true, arrayList);
         %>
         <p><%= child_table_str %></p>
       <% } else { %>
         <p>
           <%
           child_table_str = new RelationshipTabFormatter(lbSvc).formatSingleColumnTable(scheme_curr, Constants.TYPE_SUBCONCEPT, subconcepts);
           %>
           <p><%= child_table_str %></p>
         <% } %>
       </p>
       <%
       //propertyData.setRelationshipHashMap(hmap);
       
       //String role_table_str = propertyData.generateRelationshipTable(scheme_curr, version_curr, code, cNamespace, Constants.TYPE_ROLE, true);
       String role_table_str = propertyData.generateRelationshipTable(scheme_curr, version_curr, code, ns, Constants.TYPE_ROLE, true);
       
       %>
       <p><%= role_table_str %></p>
 
       <%
       //propertyData.setRelationshipHashMap(hmap);
       //String assoc_table_str = propertyData.generateRelationshipTable(scheme_curr, version_curr, code, cNamespace, Constants.TYPE_ASSOCIATION, true);

       String assoc_table_str = propertyData.generateRelationshipTable(scheme_curr, version_curr, code, ns, Constants.TYPE_ASSOCIATION, true);

       %>
       <p><%= assoc_table_str %></p>
       <p>
         <%
         String display_inverse_relationships_metadata_value = DataUtils.getMetadataValue(scheme_curr_0, version_curr, "display_inverse_relationships");
         boolean display_inverse_relationships = true;
         if (display_inverse_relationships_metadata_value != null && display_inverse_relationships_metadata_value.compareToIgnoreCase("false") == 0) {
           display_inverse_relationships = false;
         }
 
         if (!isMapping) {
           if (display_inverse_relationships) {
             propertyData.setRelationshipHashMap(hmap);
             //String inv_role_table_str = propertyData.generateRelationshipTable(scheme_curr, version_curr, code, cNamespace, Constants.TYPE_INVERSE_ROLE, true);
             String inv_role_table_str = propertyData.generateRelationshipTable(scheme_curr, version_curr, code, ns, Constants.TYPE_INVERSE_ROLE, true);

             %>
             <p><%= inv_role_table_str %></p>
             <%
           }
         }
         propertyData.setRelationshipHashMap(hmap);
         //String inv_asso_table_str = propertyData.generateRelationshipTable(scheme_curr, version_curr, code, cNamespace, Constants.TYPE_INVERSE_ASSOCIATION, true);

         String inv_asso_table_str = propertyData.generateRelationshipTable(scheme_curr, version_curr, code, ns, Constants.TYPE_INVERSE_ASSOCIATION, true);

         %>
         <p><%= inv_asso_table_str %></p>
         <%
       }
       if (!isMapping) {
         %>
         <p>
           <b>Mapping relationships:</b>
           <br />
           <table class="dataTable" role='presentation'>
             <tr>
               <td>
 
                 <a
                     href="<%=request.getContextPath() %>/pages/concept_details.jsf?dictionary=<%=scheme_curr%>&version=<%=version_curr%>&code=<%=code_curr%>&type=mapping">
 
                   see Mappings</a>
 
               </td>
             </tr>
 
           </table>
           <%
           } //ismapping
         }
         %>
 
   
    <!--
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    -->
         
    
    <%
    request.getSession().setAttribute("active_code", code);
    
    if (type.compareTo("mapping") == 0 || type.compareTo("all") == 0) {
      HashMap display_name_hmap = new HashMap();
    
      //Entity concept_curr = (Entity) request.getSession().getAttribute("concept");
      JSPUtils.JSPHeaderInfo mapping_info = new JSPUtils.JSPHeaderInfo(request);
      String mappings_scheme_curr = mapping_info.dictionary;
      String mappings_version_curr = mapping_info.version;
      String mappings_code_curr = (String) request.getSession().getAttribute("code");
    
      boolean isMappingCS = mappingTab.isMapping(mappings_scheme_curr, mappings_version_curr);
    
      if(!isMappingCS) {
    
        Vector mapping_uri_version_vec = mappingTab.getMappingCodingSchemesEntityParticipatesIn(mappings_code_curr, null);
    
        String source_scheme = null;//"NCI_Thesaurus";
        String source_version = null;// "10.06e";
        String source_namespace = null;
        String target_scheme = null;// "ICD_9_CM";
        String target_version = null;// "2010";
    
        String source_code = null;
        String source_name = null;
        String rel = null;
        String score = null;
        String target_code = null;
        String target_name = null;
        String target_namespace = null;
        MappingData mappingData = null;
        %>
        <table border="0" width="708px" role='presentation'>
    
          <%
          Vector meta_cui_vec = null;
          Entity con = (Entity) request.getSession().getAttribute("concept");
          if (con == null) {
            meta_cui_vec = metathesaurusUtils.getMatchedMetathesaurusCUIs(mappings_scheme_curr, mappings_version_curr, null, mappings_code_curr);
          } else {
            meta_cui_vec = metathesaurusUtils.getMatchedMetathesaurusCUIs(con);
          }
          %>
    
          <tr>
            <td class="textsubtitle-blue" align="left">
    
              <% if (type != null && type.compareTo("all") == 0) { %>
                <A name="mappings">Mapping Details</A>
    
              <% } else { %> Mapping Details          <% } %>
    
            </td>
          </tr>
    
        </table>
        <p></p>
        <%
        if ((mapping_uri_version_vec == null || mapping_uri_version_vec.size() == 0) && (meta_cui_vec == null || meta_cui_vec.size() == 0)) {
          %>
          <b>Mapping Relationships:</b>
          <i>(none)</i>
          <%
        } else {
   
          if (meta_cui_vec != null && meta_cui_vec.size() > 0)
          {
            String ncim_url = NCItBrowserProperties.getNCIM_URL();
            %>
            <b>Mapping through NCI Metathesaurus:</b>
            <table role='presentation'>
              <tr>
                <td>
                  <table role='presentation'>
                    <%
                    for(int lcv=0; lcv<meta_cui_vec.size(); lcv++) {
                      String meta_cui = (String) meta_cui_vec.elementAt(lcv);
                      String ncim_cs_name = "NCI Metathesaurus";
                      %>
                      <tr>
                        <td class="textbody">
                          <a
                              href="<%= ncim_url %>/ConceptReport.jsp?dictionary=<%=ncim_cs_name%>&type=synonym&code=<%=meta_cui%>"
                              target="_blank">
    
                            <i class="textbody"><%= meta_cui %></i>
                            <img
                                src="<%= request.getContextPath() %>/images/window-icon.gif"
                                width="10"
                                height="11"
                                border="0"
                                alt="<%=ncim_cs_name%>"
                            />
                          </a>
    
                        </td>
                      </tr>
                    <% } %>
    
                  </table>
                </td>
              </tr>
            </table>
            <hr></hr>
    
          <% } %>
    
          <b>Maps To:</b>
          <%
          for(int lcv=0; lcv<mapping_uri_version_vec.size(); lcv++) {
    
            String mapping_uri_version = (String) mapping_uri_version_vec.elementAt(lcv);
            Vector ret_vec = StringUtils.parseData(mapping_uri_version, "|");
            String mapping_cs_uri = (String) ret_vec.elementAt(0);
            String mapping_cs_version = (String) ret_vec.elementAt(1);
            String mapping_cs_name = mappingTab.uri2CodingSchemeName(mapping_cs_uri);
    
            boolean show_rank_column = true;
            String map_rank_applicable = mappingTab.getMetadataValue(mapping_cs_name, mapping_cs_version, "map_rank_applicable");
            if (map_rank_applicable != null && map_rank_applicable.compareTo("false") == 0) {
              show_rank_column = false;
            }
    
            List list = mappingSearchUtils.getMappingRelationship(
            mapping_cs_uri, mapping_cs_version, mappings_code_curr, 1);
    
            if (list != null && list.size() > 0) {
              %>
              <p></p>Mapping
              Source:<%= mappingTab.encodeTerm(mapping_cs_name) %>
              <table class="datatable_960">
    
                <th class="dataTableHeader" width="100px" scope="col" align="left">Source</th>
    
                <th class="dataTableHeader" width="100px" scope="col" align="left">Source Code</th>
    
                <th class="dataTableHeader" scope="col" align="left">Source Name</th>
    
                <th class="dataTableHeader" width="30px" scope="col" align="left">REL</th>
    
                <% if (show_rank_column) { %>
                  <th class="dataTableHeader" width="35px" scope="col" align="left">Map Rank</th>
                <% } %>
    
                <th class="dataTableHeader" width="100px" scope="col" align="left">Target</th>
    
                <th class="dataTableHeader" width="100px" scope="col" align="left">Target Code</th>
    
                <th class="dataTableHeader" scope="col" align="left">Target Name</th>
                <%
                int n2 = 0;
    
                for (int k=0; k<list.size(); k++) {
                  mappingData = (MappingData) list.get(k);
                  source_code = mappingData.getSourceCode();
                  source_name = mappingData.getSourceName();
                  source_namespace = mappingData.getSourceCodeNamespace();
    
                  if (display_name_hmap.containsKey(source_namespace)) {
                    source_namespace = (String) display_name_hmap.get(source_namespace);
                  } else {
                    String mappings_short_name = mappingTab.getMappingDisplayName(mapping_cs_name, source_namespace);
                    display_name_hmap.put(source_namespace, mappings_short_name);
                    source_namespace = mappings_short_name;
                  }
    
                  rel = mappingData.getRel();
                  score = Integer.valueOf(mappingData.getScore()).toString();
                  target_code = mappingData.getTargetCode();
                  target_name = mappingData.getTargetName();
                  target_namespace = mappingData.getTargetCodeNamespace();
    
                  if (display_name_hmap.containsKey(target_namespace)) {
                    target_namespace = (String) display_name_hmap.get(target_namespace);
                  } else {
                    String mappings_short_name = mappingTab.getMappingDisplayName(mapping_cs_name, target_namespace);
                    display_name_hmap.put(target_namespace, mappings_short_name);
                    target_namespace = mappings_short_name;
                  }
    
                  source_scheme = mappingTab.getFormalName(mappingData.getSourceCodingScheme());
                  source_version = mappingData.getSourceCodingSchemeVersion();
                  target_scheme = mappingTab.getFormalName(mappingData.getTargetCodingScheme());
                  target_version = mappingData.getTargetCodingSchemeVersion();
                  String source_scheme_nm = mappingTab.getCSName(source_scheme);
                  String target_scheme_nm = mappingTab.getCSName(target_scheme);
                  %>
    
                  <tr>
    
                    <td class="datacoldark" scope="row"><%= source_namespace %></td>
                    <td class="datacoldark">
                      <a
                          href="#"
                          onclick="javascript:window.location='<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=source_scheme_nm%>&version=<%=source_version%>&code=<%=source_code%>'">
    
                        <%= source_code %>
                      </a>
    
                    </td>
                    <td class="datacoldark"><%= mappingTab.encodeTerm(source_name) %></td>
    
                    <td class="textbody"><%= rel %></td>
    
                    <% if (show_rank_column) { %>
                      <td class="textbody"><%= score %></td>
                    <% } %>
    
                    <td class="datacoldark"><%= target_namespace %></td>
                    <td class="datacoldark">
    
                      <a
                          href="#"
                          onclick="javascript:window.location='<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=target_scheme_nm%>&version=<%=target_version%>&code=<%=target_code%>'">
    
                        <%= target_code %>
                      </a>
    
                    </td>
                    <td class="datacoldark"><%= mappingTab.encodeTerm(target_name) %></td>
    
                  </tr>
                <% } %>
              </table>
              <%
            }
          }
          %>
    
          <hr></hr>
          <b>Maps From:</b>
    
          <%
          for(int lcv=0; lcv<mapping_uri_version_vec.size(); lcv++) {
            String mapping_uri_version = (String) mapping_uri_version_vec.elementAt(lcv);
            Vector ret_vec = StringUtils.parseData(mapping_uri_version, "|");
            String mapping_cs_uri = (String) ret_vec.elementAt(0);
            String mapping_cs_version = (String) ret_vec.elementAt(1);
            String mapping_cs_name = mappingTab.uri2CodingSchemeName(mapping_cs_uri);
    
            boolean show_rank_column = true;
            String map_rank_applicable = mappingTab.getMetadataValue(mapping_cs_name, mapping_cs_version, "map_rank_applicable");
            if (map_rank_applicable != null && map_rank_applicable.compareTo("false") == 0) {
              show_rank_column = false;
            }
    
            List list = mappingSearchUtils.getMappingRelationship(
            mapping_cs_uri, mapping_cs_version, mappings_code_curr, -1);
    
            if (list != null && list.size() > 0) {
              %>
    
              <p></p>Mapping
              Source:<%= mappingTab.encodeTerm(mapping_cs_name) %>
              <table class="datatable_960">
    
                <th class="dataTableHeader" width="100px" scope="col" align="left">Source</th>
    
                <th class="dataTableHeader" scope="col" align="left">Source Code</th>
    
                <th class="dataTableHeader" scope="col" align="left">Source Name</th>
    
                <th class="dataTableHeader" width="30px" scope="col" align="left">REL</th>
    
                <% if (show_rank_column) { %>
                  <th class="dataTableHeader" width="35px" scope="col" align="left">Map Rank</th>
                <% } %>
    
                <th class="dataTableHeader" width="100px" scope="col" align="left">Target</th>
    
                <th class="dataTableHeader" scope="col" align="left">Target Code</th>
    
                <th class="dataTableHeader" scope="col" align="left">Target Name</th>
    
                <%
                int n2 = 0;
                for (int k=0; k<list.size(); k++) {
                  mappingData = (MappingData) list.get(k);
                  source_code = mappingData.getSourceCode();
                  source_name = mappingData.getSourceName();
                  source_namespace = mappingData.getSourceCodeNamespace();
    
                  if (display_name_hmap.containsKey(source_namespace)) {
                    source_namespace = (String) display_name_hmap.get(source_namespace);
                  } else {
                    String mappings_short_name = mappingTab.getMappingDisplayName(mapping_cs_name, source_namespace);
                    display_name_hmap.put(source_namespace, mappings_short_name);
                    source_namespace = mappings_short_name;
                  }
                  rel = mappingData.getRel();
                  score = Integer.valueOf(mappingData.getScore()).toString();
                  target_code = mappingData.getTargetCode();
                  target_name = mappingData.getTargetName();
                  target_namespace = mappingData.getTargetCodeNamespace();
    
                  if (display_name_hmap.containsKey(target_namespace)) {
                    target_namespace = (String) display_name_hmap.get(target_namespace);
                  } else {
                    String mappings_short_name = mappingTab.getMappingDisplayName(mapping_cs_name, target_namespace);
                    display_name_hmap.put(target_namespace, mappings_short_name);
                    target_namespace = mappings_short_name;
                  }
    
                  source_scheme = mappingTab.getFormalName(mappingData.getSourceCodingScheme());
                  source_version = mappingData.getSourceCodingSchemeVersion();
                  target_scheme = mappingTab.getFormalName(mappingData.getTargetCodingScheme());
                  target_version = mappingData.getTargetCodingSchemeVersion();
                  String source_scheme_nm = mappingTab.getCSName(source_scheme);
                  String target_scheme_nm = mappingTab.getCSName(target_scheme);
                  %>
    
                  <tr>
                    <td class="datacoldark" scope="row"><%= source_namespace %></td>
                    <td class="datacoldark">
                      <a
                          href="#"
                          onclick="javascript:window.location='<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=source_scheme_nm%>&version=<%=source_version%>&code=<%=source_code%>'">
    
                        <%= source_code %>
                      </a>
    
                    </td>
                    <td class="datacoldark"><%= mappingTab.encodeTerm(source_name) %></td>
    
                    <td class="textbody"><%= rel %></td>
    
                    <% if (show_rank_column) { %>
                      <td class="textbody"><%= score %></td>
                    <% } %>
    
                    <td class="datacoldark"><%= target_namespace %></td>
                    <td class="datacoldark">
    
                      <a
                          href="#"
                          onclick="javascript:window.location='<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=target_scheme_nm%>&version=<%=target_version%>&code=<%=target_code%>'">
    
                        <%= target_code %>
                      </a>
    
                    </td>
                    <td class="datacoldark"><%= mappingTab.encodeTerm(target_name) %></td>
    
                  </tr>
                <% } %>
              </table>
    
              <%
            }
          }
        }
      }
    }
    %>

    
    </div>  <% } else { %>
    <div class="textbody"><%= HTTPUtils.cleanXSS(name) %></div>
  <% } %>  <%@ include file="/pages/templates/nciFooter.jsp" %>

  </div>
  <!-- End pagecontentLittlePadding -->
  </div>
  <!-- End main-area_960 -->
  <div class="mainbox-bottom">
    <img src="<%=basePath%>/images/mainbox-bottom.gif" width="945" height="5" alt="Mainbox Bottom" />
  </div>
  </div>
  <!-- End center-page_960 -->

  </f:view>
  <script type="text/javascript">_satellite.pageBottom();</script>
  </body></html>
