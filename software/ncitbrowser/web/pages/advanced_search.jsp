<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.properties.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference" %>
<%@ page import="org.apache.logging.log4j.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html lang="en" xmlns:c="http://java.sun.com/jsp/jstl/core">
  <head>
    <script
        src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js">
    </script>

    <%
    String advSearch_requestContextPath = request.getContextPath();
    advSearch_requestContextPath = advSearch_requestContextPath.replace("//ncitbrowser//ncitbrowser", "//ncitbrowser");

    JSPUtils.JSPHeaderInfo info2 = new JSPUtils.JSPHeaderInfo(request);
    String adv_search_vocabulary = info2.dictionary;
    String adv_search_version = info2.version;

    String new_algorithm = "I feel lucky";
    %>

<!-- Google tag (gtag.js) -->
<script async src="https://www.googletagmanager.com/gtag/js?id=G-21QRTJ0WQS"></script>
<script>
	window.dataLayer = window.dataLayer || [];
	function gtag(){dataLayer.push(arguments);}
	gtag('js', new Date());
	gtag('config', 'G-21QRTJ0WQS');
</script>
    <script src="https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js"></script>
    <title><%= adv_search_vocabulary %></title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
  </head>
  <body onLoad="document.forms.advancedSearchForm.matchText.focus();">
  <header class="flex-grow-0">
	<div style='text-align: left'>
	<include-html src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html"></include-html>
	</div>
  </header>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/wz_tooltip.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/tip_centerwindow.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/tip_followscroll.js"></script>

    <script type="text/javascript">

      function onCodeButtonPressed() {
        var algorithmObj = document.forms["advancedSearchForm"].adv_search_algorithm;
        algorithmObj[0].checked = true;
        refresh();
      }

      function getSearchTarget() {
        var searchTargetObj = document.forms["advancedSearchForm"].selectSearchOption;
        // Send redirect:
        if (searchTargetObj == null) {
          try {
            String error_msg = "WARNING: The server encountered an unexpected error (file: advanced_search.jsp, code: 1, var: searchTargetObj).";
            request.getSession().setAttribute("error_msg", error_msg);
            String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
            response.sendRedirect(redirectURL);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        for (var j=0; j<searchTargetObj.length; j++) {
          if (searchTargetObj[j].checked == true) {
            return searchTargetObj[j].value;
          }
        }
      }

      function onAlgorithmChanged() {
        var curr_target = getSearchTarget();
        if (curr_target != "Code") return;

        var searchTargetObj = document.forms["advancedSearchForm"].selectSearchOption;
        // Send redirect:
        if (searchTargetObj == null) {
          try {
            String error_msg = "WARNING: The server encountered an unexpected error (file: advanced_search.jsp, code: 2, var: searchTargetObj).";
            request.getSession().setAttribute("error_msg", error_msg);
            String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
            response.sendRedirect(redirectURL);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        for (var j=0; j<searchTargetObj.length; j++) {
          target = searchTargetObj[j].value;
          if (target == "Code") {
            searchTargetObj[0].checked = true;
            break;
          }
        }
      }

      function refresh() {

        var dictionary = document.forms["advancedSearchForm"].dictionary.value;

        var text = escape(document.forms["advancedSearchForm"].matchText.value);

        var selectSearchOption = "";
        var selectSearchOptionObj = document.forms["advancedSearchForm"].selectSearchOption;
        // Send redirect:
        if (selectSearchOptionObj == null) {
          try {
            String error_msg = "WARNING: The server encountered an unexpected error (file: advanced_search.jsp, code: 3, var: selectSearchOptionObj).";
            request.getSession().setAttribute("error_msg", error_msg);
            String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
            response.sendRedirect(redirectURL);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        for (var i=0; i<selectSearchOptionObj.length; i++) {
          if (selectSearchOptionObj[i].checked) {
            selectSearchOption = selectSearchOptionObj[i].value;
            break;
          }
        }

        var algorithm = "exactMatch";
        var algorithmObj = document.forms["advancedSearchForm"].adv_search_algorithm;
        // Send redirect:
        if (algorithmObj == null) {
          try {
            String error_msg = "WARNING: The server encountered an unexpected error (file: advanced_search.jsp, code: 4, var: algorithmObj).";
            request.getSession().setAttribute("error_msg", error_msg);
            String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
            response.sendRedirect(redirectURL);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        for (var i=0; i<algorithmObj.length; i++) {
          if (algorithmObj[i].checked) {
            algorithm = algorithmObj[i].value;
            break;
          }
        }

        var adv_search_source = "";
        var sourceObj = document.forms["advancedSearchForm"].adv_search_source;
        if (!(sourceObj === undefined || sourceObj === null)) {
          adv_search_source = sourceObj.value;
        }

        var rel_search_association = document.forms["advancedSearchForm"].rel_search_association.value;
        var selectProperty = document.forms["advancedSearchForm"].selectProperty.value;
        var _version = document.forms["advancedSearchForm"].version.value;

        var direction = "";
        var directionObj = document.forms["advancedSearchForm"].direction;
        // Send redirect:
        if (directionObj == null) {
          try {
            String error_msg = "WARNING: The server encountered an unexpected error (file: advanced_search.jsp, code: 5, var: directionObj).";
            request.getSession().setAttribute("error_msg", error_msg);
            String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
            response.sendRedirect(redirectURL);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        for (var i=0; i<directionObj.length; i++) {
          if (directionObj[i].checked) {
            direction = directionObj[i].value;
          }
        }

        window.location.href="/ncitbrowser/pages/advanced_search.jsf?refresh=1"
        + "&opt="+ selectSearchOption
        + "&text="+ text
        + "&algorithm="+ algorithm
        + "&sab="+ adv_search_source
        + "&prop="+ selectProperty
        + "&rel="+ rel_search_association
        + "&dir="+ direction
        + "&dictionary="+ dictionary
        + "&version="+ _version;
      }

      function refresh_code() {

        var dictionary = document.forms["advancedSearchForm"].dictionary.value;

        var text = escape(document.forms["advancedSearchForm"].matchText.value);

        var selectSearchOption = "Code";
        var algorithm = "exactMatch";

        var adv_search_source = "";
        var sourceObj = document.forms["advancedSearchForm"].adv_search_source;
        if (!(sourceObj === undefined || sourceObj === null)) {
          adv_search_source = sourceObj.value;
        }

        var rel_search_association = document.forms["advancedSearchForm"].rel_search_association.value;
        var selectProperty = document.forms["advancedSearchForm"].selectProperty.value;
        var _version = document.forms["advancedSearchForm"].version.value;

        var direction = "";
        var directionObj = document.forms["advancedSearchForm"].direction;
        // Send redirect:
        if (directionObj == null) {
          try {
            String error_msg = "WARNING: The server encountered an unexpected error (file: advanced_search.jsp, code: 6, var: directionObj).";
            request.getSession().setAttribute("error_msg", error_msg);
            String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
            response.sendRedirect(redirectURL);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        for (var i=0; i<directionObj.length; i++) {
          if (directionObj[i].checked) {
            direction = directionObj[i].value;
          }
        }

        window.location.href="/ncitbrowser/pages/advanced_search.jsf?refresh=1"
        + "&opt="+ selectSearchOption
        + "&text="+ text
        + "&algorithm="+ algorithm
        + "&sab="+ adv_search_source
        + "&prop="+ selectProperty
        + "&rel="+ rel_search_association
        + "&dir="+ direction
        + "&dictionary="+ dictionary
        + "&version="+ _version;
      }

      function refresh_algorithm() {

        var dictionary = document.forms["advancedSearchForm"].dictionary.value;

        var text = escape(document.forms["advancedSearchForm"].matchText.value);

        var algorithm = "exactMatch";
        var algorithmObj = document.forms["advancedSearchForm"].adv_search_algorithm;
        // Send redirect:
        if (algorithmObj == null) {
          try {
            String error_msg = "WARNING: The server encountered an unexpected error (file: advanced_search.jsp, code: 7, var: algorithmObj).";
            request.getSession().setAttribute("error_msg", error_msg);
            String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
            response.sendRedirect(redirectURL);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        for (var i=0; i<algorithmObj.length; i++) {
          if (algorithmObj[i].checked) {
            algorithm = algorithmObj[i].value;
            break;
          }
        }

        var selectSearchOption = "";
        var selectSearchOptionObj = document.forms["advancedSearchForm"].selectSearchOption;
        // Send redirect:
        if (selectSearchOptionObj == null) {
          try {
            String error_msg = "WARNING: The server encountered an unexpected error (file: advanced_search.jsp, code: 8, var: selectSearchOptionObj).";
            request.getSession().setAttribute("error_msg", error_msg);
            String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
            response.sendRedirect(redirectURL);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        for (var i=0; i<selectSearchOptionObj.length; i++) {
          if (selectSearchOptionObj[i].checked) {
            selectSearchOption = selectSearchOptionObj[i].value;
            break;
          }
        }

        if (algorithm != "exactMatch" && selectSearchOption == "Code") {
          selectSearchOption = "Name";
        }

        var adv_search_source = document.forms["advancedSearchForm"].adv_search_source.value;
        var rel_search_association = document.forms["advancedSearchForm"].rel_search_association.value;
        var selectProperty = document.forms["advancedSearchForm"].selectProperty.value;
        var _version = document.forms["advancedSearchForm"].version.value;

        var direction = "";
        var directionObj = document.forms["advancedSearchForm"].direction;
        // Send redirect:
        if (directionObj == null) {
          try {
            String error_msg = "WARNING: The server encountered an unexpected error (file: advanced_search.jsp, code: 9, var: directionObj).";
            request.getSession().setAttribute("error_msg", error_msg);
            String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
            response.sendRedirect(redirectURL);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        for (var i=0; i<directionObj.length; i++) {
          if (directionObj[i].checked) {
            direction = directionObj[i].value;
          }
        }
        window.location.href="/ncitbrowser/pages/advanced_search.jsf?refresh=1"
        + "&opt="+ selectSearchOption
        + "&text="+ text
        + "&algorithm="+ algorithm
        + "&sab="+ adv_search_source
        + "&prop="+ selectProperty
        + "&rel="+ rel_search_association
        + "&dir="+ direction
        + "&dictionary="+ dictionary
        + "&version="+ _version;
      }

    </script>
    <%! private static Logger _logger = LogManager.getLogger("advanced_search.jsp"); %>
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

          <%-- Simple Search Link Version: <%@ include file="/pages/templates/content-header-alt.jsp" %> --%>

          <% request.setAttribute("hideAdvancedSearchLink", true); %>

          <%@ include file="/pages/templates/content-header-other.jsp" %>

          <%
          String x = HTTPUtils.getRefererParmEncode(request);
          String refresh = HTTPUtils.cleanXSS((String) request.getParameter("refresh"));
          boolean refresh_page = false;
          if (!DataUtils.isNull(refresh)) {
            refresh_page = true;
          }

          String adv_search_algorithm = null;
          String search_string = "";
          String selectProperty = null;
          String rel_search_association = null;
          String rel_search_rela = null;
          String adv_search_source = null;
          String adv_search_type = null;

          String t = null;
          String selectSearchOption = null;
          String direction = null;

          if (refresh_page) {

            // Note: Called when the user selects "Search By" fields.
            selectSearchOption = HTTPUtils.cleanXSS((String) request.getParameter("opt"));
            search_string = HTTPUtils.cleanXSS((String) request.getParameter("text"));
            adv_search_algorithm = HTTPUtils.cleanXSS((String) request.getParameter("algorithm"));

            adv_search_source = HTTPUtils.cleanXSS((String) request.getParameter("sab"));
            rel_search_association = HTTPUtils.cleanXSS((String) request.getParameter("rel"));
            direction = HTTPUtils.cleanXSS((String) request.getParameter("dir"));

            rel_search_rela = HTTPUtils.cleanXSS((String) request.getParameter("rela"));
            selectProperty = HTTPUtils.cleanXSS((String) request.getParameter("prop"));

            if (adv_search_algorithm.compareToIgnoreCase("lucene") == 0) {
              if (selectSearchOption.compareToIgnoreCase("Code") != 0) {
                selectSearchOption = "Name";
              }
              selectProperty = null;
              rel_search_association = null;
              adv_search_source = null;
              direction = null;
              rel_search_rela = null;
            }

            adv_search_type = selectSearchOption;

          } else {
            selectSearchOption = (String) request.getSession().getAttribute("selectSearchOption");
            adv_search_source = (String) request.getSession().getAttribute("selectedSource");
            if (adv_search_source == null) {
              adv_search_source = "ALL";
            }
            search_string = (String) request.getSession().getAttribute("matchText");
            direction = (String) request.getSession().getAttribute("direction");
          }

          if (DataUtils.isNull(selectSearchOption)) {
            //selectSearchOption = "Property";
            selectSearchOption = "Name";
          }

          if (direction == null) direction = "source";

          SearchStatusBean bean = null;
          String message = (String) request.getAttribute("message");
          if (message != null) {
            request.removeAttribute("message");
          }

          if (!refresh_page || message != null) {

            Object bean_obj = request.getSession().getAttribute("searchStatusBean");

            if (bean_obj == null) {
              bean = new SearchStatusBean(adv_search_vocabulary);
              FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("searchStatusBean", bean);

              adv_search_algorithm = bean.getAlgorithm();

            } else {

              bean = (SearchStatusBean) bean_obj;
              adv_search_algorithm = bean.getAlgorithm();
              adv_search_source = bean.getSelectedSource();
              selectProperty = bean.getSelectedProperty();
              search_string = bean.getMatchText();
              rel_search_association = bean.getSelectedAssociation();
              rel_search_rela = bean.getSelectedRELA();

              direction = bean.getDirection();

              //KLO
              adv_search_type = bean.getSearchType();
              selectSearchOption = adv_search_type;

              selectSearchOption = bean.getSelectedSearchOption();

              _logger.debug("advanced_search.jsp adv_search_algorithm: " + adv_search_algorithm);
              _logger.debug("advanced_search.jsp adv_search_source: " + adv_search_source);
              _logger.debug("advanced_search.jsp selectProperty: " + selectProperty);
              _logger.debug("advanced_search.jsp search_string: " + search_string);
              _logger.debug("advanced_search.jsp rel_search_association: " + rel_search_association);
              _logger.debug("advanced_search.jsp rel_search_rela: " + rel_search_rela);

              FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("searchStatusBean", bean);
            }
          }

          if (rel_search_association == null) rel_search_association = "ALL";
          if (rel_search_rela == null) rel_search_rela = " ";
          if (selectProperty == null) selectProperty = "ALL";
          if (adv_search_source == null) adv_search_source = "ALL";
          if (search_string == null) search_string = "";
          if (adv_search_algorithm == null) adv_search_algorithm = "contains";

          if (direction == null) {
            direction = "source";
          }

          String check__e = "", check__b = "", check__s = "" , check__c ="";

          if (adv_search_algorithm == null || adv_search_algorithm.compareTo("contains") == 0)
          check__c = "checked";
          else if (adv_search_algorithm.compareTo("startsWith") == 0)
          check__s= "checked";
          else if (adv_search_algorithm.compareToIgnoreCase("lucene") == 0)
          check__b= "checked";
          else
          check__e = "checked";

          String check_n2 = "", check_c2 = "", check_p2 = "" , check_r2 ="";

          if (selectSearchOption == null || selectSearchOption.compareToIgnoreCase("Name") == 0)
          check_n2 = "checked";
          else if (selectSearchOption.compareToIgnoreCase("Code") == 0)
          check_c2 = "checked";
          else if (selectSearchOption.compareToIgnoreCase("Property") == 0)
          check_p2 = "checked";
          else if (selectSearchOption.compareToIgnoreCase("Relationship") == 0)
          check_r2 = "checked";

          String check_source = "", check_target = "";

          if (direction == null || direction.compareTo("source") == 0)
          check_source = "checked";
          else //if (direction.compareTo("target") == 0)
          check_target= "checked";
          %>
          <div class="pagecontent">
            <a name="evs-content" id="evs-content" tabindex="0"></a>
            <table role='presentation'>
              <tr>
                <td class="texttitle-blue">Advanced Search</td>
              </tr>

              <% if (message != null) { %>
                <tr class="textbodyred">
                  <td>
                    <p class="textbodyred">&nbsp;<%= message %></p>
                  </td>
                </tr>
              <% } %>

              <tr class="textbody">
                <td>
<%
String token = (String) request.getSession().getAttribute(TokenUtils.CSRF_TOKEN);

%>
                  <h:form id="advancedSearchForm" styleClass="search-form" acceptcharset="UTF-8">

                    <table role='presentation'>
                      <tr>
                        <td>
                          <input aria-label="Match Text" 
                              CLASS="searchbox-input"
                              name="matchText"
                              value="<%=HTTPUtils.cleanXSS(search_string)%>"
                              onkeypress="return submitEnter('advancedSearchForm:adv_search',event)"
                              tabindex="0">

                          <h:commandButton
                              id="adv_search"
                              value="Search"
                              action="#{userSessionBean.advancedSearchAction}"
                              onclick="javascript:cursor_wait();"
                              image="#{advSearch_requestContextPath}/images/search.gif"
                              alt="Search"
                              tabindex="0">

                          </h:commandButton>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          <table border="0" cellspacing="0" cellpadding="0" role='presentation'>
                            <tr valign="top" align="left">
                              <td align="left" class="textbody">
                                <input
                                    type="radio"
                                    id="adv_contains"
                                    name="adv_search_algorithm"
                                    value="contains"
                                    alt="Contains"
                                    <%=check__c%>
                                    tabindex="0"
                                    onclick="refresh_algorithm()";>

                                <label for="adv_contains">Contains</label>
                                <input
                                    type="radio"
                                    id="adv_exactMatch"
                                    name="adv_search_algorithm"
                                    value="exactMatch"
                                    alt="Exact Match"
                                    <%=check__e%>
                                    tabindex="0"
                                    onclick="refresh_algorithm()";>

                                <label for="adv_exactMatch">Exact Match&nbsp;</label>
                                <input
                                    type="radio"
                                    id="adv_startsWith"
                                    name="adv_search_algorithm"
                                    value="startsWith"
                                    alt="Begins With"
                                    <%=check__s%>
                                    tabindex="0"
                                    onclick="refresh_algorithm()";>
                                <label for="adv_startsWith">Begins With&nbsp;</label>
<!--                                
                                <input
                                    type="radio"
                                    id="Lucene"
                                    name="adv_search_algorithm"
                                    value="lucene"
                                    alt="Lucene"
                                    <%=check__b%>
                                    tabindex="0"
                                    onclick="refresh_algorithm()";>

                                <label for="Lucene">Lucene</label>
-->                                
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>

                      <% if (adv_search_algorithm.compareToIgnoreCase("lucene") != 0) { %>

                        <tr>
                          <td>
                            <h:outputLabel id="rel_search_source_Label" value="Source" styleClass="textbody">
                              <select id="adv_search_source" name="adv_search_source" size="1" aria-label="Select Source" tabindex="0">
                                <%
                                Vector src_vec = OntologyBean.getSupportedSources(adv_search_vocabulary, adv_search_version);
                                t = "ALL";
                                if (adv_search_source == null) adv_search_source = "ALL";
                                if (t.compareTo(adv_search_source) == 0) {
                                  %>
                                  <option value="<%=t%>" selected><%= t %></option>
                                <% } else { %>
                                  <option value="<%=t%>"><%= t %></option>
                                  <%
                                }

                                if (src_vec != null) {
                                  for (int i=0; i<src_vec.size(); i++) {
                                    t = (String) src_vec.elementAt(i);
                                    if (t.compareTo(adv_search_source) == 0) {
                                      %>
                                      <option value="<%=t%>" selected><%= t %></option>
                                    <% } else { %>
                                      <option value="<%=t%>"><%= t %></option>
                                      <%
                                    }
                                  }
                                }
                                %>
                              </select>
                            </h:outputLabel>
                          </td>
                        </tr>

                        <tr>
                          <td>&nbsp;&nbsp;</td>
                        </tr>

                      <% } else { %>
                        <input
                            type="hidden"
                            name="adv_search_source"
                            id="adv_search_source"
                            value="<%=HTTPUtils.cleanXSS(adv_search_source)%>">

                      <% } %>

                      <tr valign="top" align="left">
                        <td align="left" class="textbody">Concepts searched for have:</td>
                      </tr>

                      <tr valign="top" align="left">
                        <td align="left" class="textbody">
                          <input
                              type="radio"
                              id="searchTarget0"
                              name="selectSearchOption"
                              value="Name"
                              alt="Name"
                              <%=check_n2%>
                              onclick="javascript:refresh()"
                              tabindex="0">

                          <label for="searchTarget0">Name&nbsp;</label>

                          <% if (adv_search_algorithm.compareToIgnoreCase("lucene") != 0) { %>
                            <input
                                type="radio"
                                id="searchTarget1"
                                name="selectSearchOption"
                                value="Code"
                                alt="Code"
                                <%=check_c2%>
                                onclick="refresh_code()"
                                tabindex="0">

                            <label for="searchTarget1">Code&nbsp;</label>
                            <input
                                type="radio"
                                id="searchTarget2"
                                name="selectSearchOption"
                                value="Property"
                                alt="Property"
                                <%=check_p2%>
                                onclick="javascript:refresh()"
                                tabindex="0">

                            <label for="searchTarget2">Property&nbsp;</label>
                            <input
                                type="radio"
                                id="searchTarget3"
                                name="selectSearchOption"
                                value="Relationship"
                                alt="Relationship"
                                <%=check_r2%>
                                onclick="javascript:refresh()"
                                tabindex="0">

                            <label for="searchTarget3">Relationship</label>
                          <% } else { %>

                          <% } %>

                        </td>
                      </tr>

                      <tr>
                        <td>
                          <table role='presentation'>
                            <% if (selectSearchOption.equals("Property")) { %>
                              <input
                                  type="hidden"
                                  name="rel_search_association"
                                  id="rel_search_association"
                                  value="<%=HTTPUtils.cleanXSS(rel_search_association)%>">

                              <input
                                  type="hidden"
                                  name="rel_search_rela"
                                  id="rel_search_rela"
                                  value="<%=HTTPUtils.cleanXSS(rel_search_rela)%>">

                              <input
                                  type="hidden"
                                  name="direction"
                                  id="direction"
                                  value="<%=HTTPUtils.cleanXSS(direction)%>">

                              <tr>
                                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                <td>
                                  <h:outputLabel id="selectPropertyLabel" value="Select one:" styleClass="textbody">
                                    <select id="selectProperty" name="selectProperty" size="1" aria-label="Select Property" tabindex="0">
                                      <%
                                      t = "ALL";
                                      if (t.compareTo(selectProperty) == 0) {
                                        %>
                                        <option value="<%=t%>" selected><%= t %></option>
                                        <%} else {%>
                                          <option value="<%=t%>"><%= t %></option>
                                          <%}%>

                                            <%
                                            Vector property_vec = OntologyBean.getSupportedPropertyNames(adv_search_vocabulary, adv_search_version);
                                            if (property_vec != null) {
                                              for (int i=0; i<property_vec.size(); i++) {
                                                t = (String) property_vec.elementAt(i);
                                                if (t.compareTo(selectProperty) == 0) {
                                                  %>
                                                  <option value="<%=t%>" selected><%= t %></option>
                                                <% } else { %>
                                                  <option value="<%=t%>"><%= t %></option>
                                                  <%
                                                }
                                              }
                                            }
                                            %>
                                          </select></h:outputLabel></td></tr>

                                  <% } else if (selectSearchOption.equals("Relationship")) { %>
                                    <input
                                        type="hidden"
                                        name="selectProperty"
                                        id="selectProperty"
                                        value="<%=HTTPUtils.cleanXSS(selectProperty)%>">

                                    <tr>
                                      <td>&nbsp;&nbsp;&nbsp;</td>
                                      <td>
                                        <h:outputLabel
                                            id="rel_search_associationLabel"
                                            value="Select one:"
                                            styleClass="textbody">

                                          <select id="rel_search_association" name="rel_search_association" aria-label="Select Association" size="1">
                                            <%
                                            t = "ALL";
                                            if (t.compareTo(rel_search_association) == 0) {
                                              %>
                                              <option value="<%=t%>" selected><%= t %></option>
                                              <%} else {%>
                                                <option value="<%=t%>"><%= t %></option>
                                                <%} %>

                                                  <%
                                                  //[NCITERM-681] The relationship combo box in some coding schemes is not populated correctly.
                                                  //Vector association_vec = OntologyBean.getSupportedAssociationNamesAndIDs(adv_search_vocabulary, adv_search_version);
                                                  Vector association_vec = DataUtils.getSupportedAssociationNames(adv_search_vocabulary, adv_search_version);
                                                  if (association_vec != null) {
                                                    for (int i=0; i<association_vec.size(); i++) {
                                                      t = (String) association_vec.elementAt(i);
                                                      //Vector name_and_id_vec = StringUtils.parseData(t);
                                                      //String association_name = (String) name_and_id_vec.elementAt(0);
                                                      //String association_id = (String) name_and_id_vec.elementAt(1);
                                                      String association_name = t;
                                                      String association_id = t;

                                                      if (association_id.compareTo(rel_search_association) == 0) {
                                                        %>
                                                        <option value="<%=association_id%>" selected>
                                                          <%= association_name %>
                                                        </option>
                                                      <% } else { %>
                                                        <option value="<%=association_id%>">
                                                          <%= association_name %>
                                                        </option>
                                                        <%
                                                      }
                                                    }
                                                  }
                                                  %>
                                                </select></h:outputLabel></td></tr>

                                          <tr>
                                            <td>&nbsp;</td>
                                            <td class="textbody">
                                              with a
                                              <input
                                                  type="radio"
                                                  id="source"
                                                  name="direction"
                                                  value="source"
                                                  alt="Source"
                                                  <%=check_source%>
                                                  tabindex="0"
                                              />
                                              <label for="source">source&nbsp;</label>
                                              <input
                                                  type="radio"
                                                  id="target"
                                                  name="direction"
                                                  value="target"
                                                  alt="Target"
                                                  <%=check_target%>
                                                  tabindex="0"
                                              />
                                              <label for="target">target&nbsp;</label>
                                              concept name matching the search criteria specified above.
                                            </td>
                                          </tr>

                                          <tr>
                                            <td>&nbsp;</td>
                                            <td class="textbody">
                                              <table role='presentation'>
                                                <tr>
                                                  <td class="textbody">
                                                    Example: [
                                                    <i>Finger</i>]-->(
                                                    <i>Anatomic_Structure_Is_Physical_Part_Of</i>)-->[
                                                    <i>Hand</i>].
                                                  </td>
                                                </tr>
                                                <tr>
                                                  <td class="textbody">
                                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                                    <i>Finger</i>
                                                    is the &quot;source&quot; concept in this relationship,
                                                    <i>Hand</i>
                                                    is the &quot;target.&quot;
                                                  </td>
                                                </tr>
                                              </table>
                                            </td>
                                          </tr>

                                        <% } else { %>
                                          <input
                                              type="hidden"
                                              name="selectProperty"
                                              id="selectProperty"
                                              value="<%=HTTPUtils.cleanXSS(selectProperty)%>">

                                          <input
                                              type="hidden"
                                              name="rel_search_association"
                                              id="rel_search_association"
                                              value="<%=HTTPUtils.cleanXSS(rel_search_association)%>">

                                          <input
                                              type="hidden"
                                              name="rel_search_rela"
                                              id="rel_search_rela"
                                              value="<%=HTTPUtils.cleanXSS(rel_search_rela)%>">

                                          <input
                                              type="hidden"
                                              name="direction"
                                              id="direction"
                                              value="<%=HTTPUtils.cleanXSS(direction)%>">

                                        <% } %>

                                      </table>
                                  </td>
                                </tr>

                                <% if (selectSearchOption.compareTo("Relationship") != 0) { %>

                                  <tr>

                                    <% if (selectSearchOption.compareTo("Property") == 0) { %>

                                      <td class="textbody">
                                        &nbsp;with its value matching the search criteria specified above.
                                      </td>
                                    <% } else { %>
                                      <td class="textbody">&nbsp;matching the search criteria specified above.</td>
                                    <% } %>

                                    <td></td>
                                  </tr>

                                <% } %>

                                <%
                                if (adv_search_algorithm.compareToIgnoreCase("lucene") == 0 && selectSearchOption.compareTo("Name") == 0) {
                                  %>

                                  <tr>
                                    <td>
                                      <p>
                                        <table role='presentation'>
                                          <tr>
                                            <td class="textbody">&nbsp;Examples:</td>
                                          </tr>
                                          <tr>
                                            <td class="textbody">
                                              &nbsp;&nbsp;
                                              <i>Wildcard (multiple characters): heart*</i>
                                            </td>
                                          </tr>
                                          <tr>
                                            <td class="textbody">
                                              &nbsp;&nbsp;
                                              <i>Wildcard (single character): he?rt</i>
                                            </td>
                                          </tr>
                                          <tr>
                                            <td class="textbody">
                                              &nbsp;&nbsp;
                                              <i>Fuzzy match: heart~</i>
                                            </td>
                                          </tr>
                                          <tr>
                                            <td class="textbody">
                                              &nbsp;&nbsp;
                                              <i>Boolean: heart AND attack</i>
                                            </td>
                                          </tr>
                                          <td class="textbody">
                                            &nbsp;&nbsp;
                                            <i>Boosting: heart^5 AND attack</i>
                                          </td>
                                        </tr>
                                        <tr>
                                          <td class="textbody">
                                            &nbsp;&nbsp;
                                            <i>Negation: heart -attack</i>
                                          </td>
                                        </tr>
                                      </table></p></td></tr>                              <% } %>

                            </table>

                            <input
                                type="hidden"
                                name="referer"
                                value="<%=x%>">

                            <input
                                type="hidden"
                                name="dictionary"
                                value="<%=HTTPUtils.cleanXSS(adv_search_vocabulary)%>">

                            <input
                                type="hidden"
                                name="version"
                                value="<%=HTTPUtils.cleanXSS(adv_search_version)%>">
                            
                            <input
                                type="hidden"
                                name="adv_search_type"
                                id="adv_search_type"
                                value="<%=HTTPUtils.cleanXSS(adv_search_type)%>"
                            />

  <input type="hidden" name="<%=TokenUtils.CSRF_TOKEN%>" id="<%=TokenUtils.CSRF_TOKEN%>" value="<%=token%>" />

                          </h:form>

                        </td></tr></table>                    <%@ include file="/pages/templates/nciFooter.jsp" %>
                  </div>
                  <!-- end Page content -->
              </div>
              <div class="mainbox-bottom">
                <img src="<%=basePath%>/images/mainbox-bottom.gif" width="945" height="5" alt="Mainbox Bottom" />
              </div>
              <!-- end Main box -->
            </div>
        </f:view>
        <script type="text/javascript">_satellite.pageBottom();</script>
      </body>
    </html>
