<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.common.Constants" %>
<%@ page import="org.apache.logging.log4j.*" %>
<%!
private static final String CABIG_APPROVED_MSG = "caBIG approved";
private static Logger _logger = LogManager.getLogger("multiple_search.jsp");

private static String getCabigIndicator(boolean display, String basePath) {
  if (! display)
  return "";

  // Added shim.gif image next to the asterisk indicator so we can be
  //   508 compliant.  This associates the alternate text from the shim
  //   to the asterisk.
  String cabig_msg = "<img src=\"" + basePath + "/images/shim.gif\""
  // + " width=\"1\" height=\"1\""
  + " alt=\"" + CABIG_APPROVED_MSG + "\"" + ">";
  return " <b>*</b> " + cabig_msg;
}
%>
<%
String nci_meta_url = DataUtils.getNCImURL();
String ncit_url = DataUtils.getNCItURL();

request.getSession().removeAttribute("dictionary");
request.getSession().removeAttribute("version");

String ncit_build_info = DataUtils.getNCITBuildInfo();
String application_version = DataUtils.getApplicationVersion();
String anthill_build_tag_built = DataUtils.getNCITAnthillBuildTagBuilt();
String evs_service_url = DataUtils.getEVSServiceURL();

String requestContextPath = request.getContextPath();
requestContextPath = requestContextPath.replace("//ncitbrowser//ncitbrowser", "//ncitbrowser");
//boolean display_cabig_approval_indicator_note = false;
// 04242014
Integer curr_sort_category = null;

request.getSession().removeAttribute("n");
request.getSession().removeAttribute("b");
request.getSession().removeAttribute("m");


%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html lang="en" xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>
<script src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js"></script>
<!-- Google tag (gtag.js) -->
<script async src="https://www.googletagmanager.com/gtag/js?id=G-21QRTJ0WQS"></script>
<script>
	window.dataLayer = window.dataLayer || [];
	function gtag(){dataLayer.push(arguments);}
	gtag('js', new Date());
	gtag('config', 'G-21QRTJ0WQS');
</script>
<script src="https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js"></script>
<title>NCI Term Browser</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()
%>
/css/styleSheet.css" />
<link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
<script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
</head>
<!-- y onload="checkVisited();"> -->
<body onLoad="document.forms.searchTerm.matchText.focus();">
  <header class="flex-grow-0">
	<div style='text-align: left'>
	<include-html src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html"></include-html>
	</div>
  </header>
  <!--
    uild info: <%=ncit_build_info%> Version info: <%=application_version%> Tag: <%=anthill_build_tag_built%> LexEVS URL:
    <%=evs_service_url%>
  -->
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/wz_tooltip.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_centerwindow.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_followscroll.js"></script>
  <script language="JavaScript">

    function trim(x) {
      return x.replace(/^\s+|\s+$/gm,'');
    }

    function show_others(csn) {
      var checkedStr = "";
      var target = "";
      var alg = "";

      var matchText = document.searchTerm.matchText.value;

      var checkedObj = document.forms["searchTerm"].ontology_list;
      for (var i=0; i<checkedObj.length; i++) {
        if (checkedObj[i].checked) {
          checkedStr = checkedStr + checkedObj[i].value + "|";
        }
      }

      checkedStr = trim(checkedStr);
      if (checkedStr.endsWith("|")) {
        checkedStr = checkedStr.substring(0, checkedStr.length-1);
      }

      var searchTargetObj = document.forms["searchTerm"].searchTarget;
      for (var j=0; j<searchTargetObj.length; j++) {
        if (searchTargetObj[j].checked == true) {
          target = searchTargetObj[j].value;
          break;
        }
      }

      var algorithmObj = document.forms["searchTerm"].algorithm;
      for (var j=0; j<algorithmObj.length; j++) {
        if (algorithmObj[j].checked == true) {
          alg = algorithmObj[j].value;
          break;
        }
      }

      window.location.href = "/ncitbrowser/ajax?action=show&csn="+ csn +"&matchText=" + matchText +"&algorithm=" + alg + "&searchTarget=" + target + "&ontology_list=" + checkedStr + "";

    }

    function hide_others(csn) {
      var matchText = document.searchTerm.matchText.value;

      var checkedStr = "";
      var target = "";
      var alg = "";
      var checkedObj = document.forms["searchTerm"].ontology_list;
      for (var i=0; i<checkedObj.length; i++) {
        if (checkedObj[i].checked) {
          checkedStr = checkedStr + checkedObj[i].value + "|";
        }
      }

      checkedStr = trim(checkedStr);
      if (checkedStr.endsWith("|")) {
        checkedStr = checkedStr.substring(0, checkedStr.length-1);
      }

      var searchTargetObj = document.forms["searchTerm"].searchTarget;
      for (var j=0; j<searchTargetObj.length; j++) {
        if (searchTargetObj[j].checked == true) {
          target = searchTargetObj[j].value;
          break;
        }
      }

      var algorithmObj = document.forms["searchTerm"].algorithm;
      for (var j=0; j<algorithmObj.length; j++) {
        if (algorithmObj[j].checked == true) {
          alg = algorithmObj[j].value;
          break;
        }
      }

      window.location.href = "/ncitbrowser/ajax?action=hide&csn="+ csn +"&matchText=" + matchText +"&algorithm=" + alg + "&searchTarget=" + target + "&ontology_list=" + checkedStr + "";

    }

    function checkVisited() {
      var test = '<%= request.getSession().getAttribute("visited") %>';
      if (test == "" || test == "null")
      checkAllButOne(document.searchTerm.ontology_list, 'Metathesaurus');
    }

    function onCodeButtonPressed(formname) {
      var algorithmObj = document.forms["searchTerm"].algorithm;
      for (var j=0; j<algorithmObj.length; j++) {
        algorithm = algorithmObj[j].value;
        if (algorithm == "exactMatch") {
          algorithmObj[j].checked = true;
          break;
        }
      }
    }

    function getSearchTarget(formname) {
      var searchTargetObj = document.forms[formname].searchTarget;
      for (var j=0; j<searchTargetObj.length; j++) {
        if (searchTargetObj[j].checked == true) {
          return searchTargetObj[j].value;
        }
      }
    }

    function onAlgorithmChanged(formname) {
      var target = getSearchTarget(formname);
      if (target != "codes") return;
      var targetObj = document.forms["searchTerm"].searchTarget;
      targetObj[0].checked = true;
    }

  </script>
  <%
  request.getSession().removeAttribute("dictionary");
  request.getSession().setAttribute("active_scheme", "none");
  
  
  
  	String token = (String) request.getSession().getAttribute(TokenUtils.CSRF_TOKEN);
  	if (token == null) {
  		token = TokenUtils.generateCSRFToken();
  		request.getSession().setAttribute(TokenUtils.CSRF_TOKEN, token);
  	}  

  //System.out.println("multiple_search.jsp token: " + token);  	

  
  Vector display_name_vec = (Vector) request.getSession().getAttribute("display_name_vec");
  if (display_name_vec == null) {
    display_name_vec = DataUtils.getSortedOntologies();
  }
  // Send redirect:
  if (display_name_vec == null) {
    try {
      String error_msg = "WARNING: The server encountered an unexpected error (file: multiple_search.jsp, var: display_name_vec).";
      request.getSession().setAttribute("error_msg", error_msg);
      String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
      response.sendRedirect(redirectURL);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  String browserType = request.getHeader("User-Agent");

  //   Modifications:

  String ontologiesToSearchOnStr = (String) request.getSession().getAttribute("ontologiesToSearchOnStr");
  if (ontologiesToSearchOnStr == null) {
    ontologiesToSearchOnStr = DataUtils.getDefaultOntologiesToSearchOnStr();
    request.getSession().setAttribute("ontologiesToSearchOnStr", ontologiesToSearchOnStr);
  }

  String action = HTTPUtils.cleanXSS((String) request.getParameter("action"));
  if (action != null) {
    String action_cs = HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
    if (action.compareTo("show") == 0) {
      for (int i = 0; i < display_name_vec.size(); i++) {
        OntologyInfo info = (OntologyInfo) display_name_vec.elementAt(i);
        //KLO 030915
        if (info.getVisible()) {
          info.setSelected(false);
          if (ontologiesToSearchOnStr.indexOf(info.getLabel()) != -1) {
            info.setSelected(true);
          }
        }

        if (action_cs != null && action_cs.compareTo(info.getCodingScheme()) == 0 && info.getHasMultipleVersions()) {
          info.setExpanded(true);
        } else if (action_cs != null && action_cs.compareTo(info.getCodingScheme()) == 0 && !info.isProduction()) {
          info.setVisible(true);
        }
      }
    } else if(action.compareTo("hide") == 0) {
      for (int i = 0; i < display_name_vec.size(); i++) {
        OntologyInfo info = (OntologyInfo) display_name_vec.elementAt(i);
        // Send redirect:
        if (info == null) {
          try {
            String error_msg = "WARNING: The server encountered an unexpected error (file: multiple_search.jsp, code: 9, var: info).";
            request.getSession().setAttribute("error_msg", error_msg);
            String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
            response.sendRedirect(redirectURL);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        if (info.getVisible()) {
          info.setSelected(false);
          if (ontologiesToSearchOnStr.indexOf(info.getLabel()) != -1) {
            info.setSelected(true);
          }
        }
        if (action_cs != null && action_cs.compareTo(info.getCodingScheme()) == 0 && info.getHasMultipleVersions()) {
          info.setExpanded(false);
        } else if (action_cs != null && action_cs.compareTo(info.getCodingScheme()) == 0 && !info.isProduction()) {
          info.setVisible(false);
        }
      }

    }
  }

  request.getSession().removeAttribute("error_msg");
  request.getSession().setAttribute("display_name_vec", display_name_vec);
  String warning_msg = (String) request.getSession().getAttribute("warning");

  if (warning_msg != null && warning_msg.compareTo(Constants.ERROR_NO_VOCABULARY_SELECTED) == 0) {
    ontologiesToSearchOnStr = "|";
  }
  String unsupported_vocabulary_message = (String) request.getSession().getAttribute("unsupported_vocabulary_message");
  %>
  

  
  
  <f:view>
    <!-- Begin Skip Top Navigation -->
    <a href="#evs-content" class="skip-main" accesskey="1" title="Skip repetitive navigation links">
      skip navigation links</A>
    <!-- End Skip Top Navigation -->
    <%@ include file="/pages/templates/header.jsp" %>
    <div class="center-page_960">
      <h:form id="searchTerm" acceptcharset="UTF-8">
        <%@ include file="/pages/templates/sub-header.jsp" %>
        <!-- Main box -->
        <div id="main-area_960">
          <input type="hidden" name="initial_search" value="true" />
          <%@ include file="/pages/templates/content-header-termbrowserhome.jsp" %>

          <!-- Page content -->
          <div class="pagecontent">
            <a name="evs-content" id="evs-content" tabindex="0"></a>

            <%-- 1 <%@ include file="/pages/templates/navigationTabs.jsp"%> --%>

            <div class="tabTableContentContainer">

              <!--
                modification
                starts----------------------------------------------------------------------------------------------
              -->
              <table border="0" role='presentation'>
                <tr>
                  <td width="650px">
                    <!-- <table class="termstable_960" border="0" role='presentation'> -->
                    <table border="0" role='presentation'>
                      <tr>
                        <td>
                          <img
                              src="<%= request.getContextPath() %>/images/AllbutNCIm.gif"
                              name="selectAllButNCIm"
                              alt="selectAllButNCIm"
                              tabindex="0"
                              onClick="checkAllButOne(document.searchTerm.ontology_list, 'Metathesaurus')"
                          />
                          &nbsp;&nbsp;
                          <img
                              src="<%= request.getContextPath() %>/images/selectall.gif"
                              name="selectAll"
                              alt="selectAll"
                              tabindex="0"
                              onClick="checkAll(document.searchTerm.ontology_list)"
                          />
                          &nbsp;&nbsp;
                          <h:commandButton
                              id="clear"
                              value="clearall"
                              action="#{userSessionBean.clearAll}"
                              image="#{requestContextPath}/images/clear.gif"
                              alt="reset"
                              tabindex="0">

                          </h:commandButton>
                          &nbsp;&nbsp;
                          <h:commandButton
                              id="multi_search"
                              value="Search"
                              action="#{userSessionBean.multipleSearchAction}"
                              image="#{requestContextPath}/images/search.gif"
                              alt="Search"
                              tabindex="0">

                          </h:commandButton>
                        </td>
                      </tr>
                    </table>

                    <% if (warning_msg != null) { %>
                      <p class="textbodyred">&nbsp;<%= warning_msg %></p>
                      <%
                    }
                    if (unsupported_vocabulary_message != null && unsupported_vocabulary_message.compareTo("null") != 0) {
                      request.getSession().removeAttribute("unsupported_vocabulary_message");
                      %>
                      <p class="textbodyred">&nbsp;<%= unsupported_vocabulary_message %></p>
                      <%
                    }

                    request.getSession().removeAttribute("warning");
                    String hide_ontology_list = (String) request.getSession().getAttribute("hide_ontology_list");
                    request.getSession().removeAttribute("hide_ontology_list");

                    if (hide_ontology_list == null || hide_ontology_list.compareTo("false") == 0) {
                      %>

                      <span class="textbody">
                        &nbsp;Select NCI hosted terminologies to search, or click on a source name to go to its browser
                        home page.
                        <br />
                        &nbsp;(WARNING:
                        <b>Select All</b>
                        searches with thousands of hits may be slow; try NCI Metathesaurus separately.)
                        <br />
                        <br />
                      </span>

                      <!-- <table class="termstable_960" border="0" role='presentation'> -->
                      <table border="0" role='presentation'>
                        <tr>
                          <%
                          List ontology_list = DataUtils.getOntologyList();
                          int num_vocabularies = ontology_list.size();
                          //if (display_name_vec == null) {

                            display_name_vec = DataUtils.getSortedOntologies();

                            // [NCITERM-641] Tomcat session is mixed up.
                            String ontologiesToExpandStr = (String) request.getSession().getAttribute("ontologiesToExpandStr");
                            if (ontologiesToExpandStr == null) {
                              ontologiesToExpandStr = "|";
                            }

                            for (int k = 0; k < display_name_vec.size(); k++) {
                              OntologyInfo info = (OntologyInfo) display_name_vec.elementAt(k);
                              // [NCITERM-641] Tomcat session is mixed up.
                              info.setSelected(false);
                              if (ontologiesToSearchOnStr.indexOf(info.getLabel()) != -1) {
                                info.setSelected(true);
                              }
                            }

                            for (int k = 0; k < display_name_vec.size(); k++) {
                              OntologyInfo info = (OntologyInfo) display_name_vec.elementAt(k);
                              // Send redirect:
                              if (info == null) {
                                try {
                                  String error_msg = "WARNING: The server encountered an unexpected error (file: multiple_search.jsp, code: 10, var: info).";
                                  request.getSession().setAttribute("error_msg", error_msg);
                                  String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
                                  response.sendRedirect(redirectURL);
                                } catch (Exception ex) {
                                  ex.printStackTrace();
                                }
                              }
                              if (!info.isProduction()) {
                                //info.setSelected(false);
                              }

                              info.setExpanded(false);
                              if (ontologiesToExpandStr.indexOf(info.getLabel()) != -1) {
                                info.setExpanded(true);
                              }
                            }

                            Collections.sort(display_name_vec, new OntologyInfo.ComparatorImpl());
                          //}

                          request.getSession().setAttribute("display_name_vec", display_name_vec);

                          display_name_vec = DataUtils.sortOntologyInfo(display_name_vec);

                          boolean blank_line_added = false;
                          %>
                          <td class="textbody">
                            <table border="0" cellpadding="0" cellspacing="0" role='presentation'>
                              <%
                              int hide_counter = 0;
                              int show_counter = 0;

                              OntologyInfo info_0 = (OntologyInfo) display_name_vec.elementAt(0);
                              if (info_0 == null) {
                                curr_sort_category = 0;
                              } else {
                                curr_sort_category = Integer.valueOf(info_0.getSortCategory());
                              }

                              //curr_sort_category = Integer.valueOf(info_0.getSortCategory());

                              for (int i = 0; i < display_name_vec.size(); i++) {
                                OntologyInfo info = (OntologyInfo) display_name_vec.elementAt(i);
                                int sort_category = info.getSortCategory();

                                String display_name = info.getDisplayName();
                                String label = info.getLabel();
                                String label2 = "|" + label + "|";

                                String scheme = info.getCodingScheme();
                                String version = info.getVersion();

                                boolean isMapping = DataUtils.isMapping(scheme, version);
                                if (!isMapping) {

                                  String indent = "&nbsp;&nbsp;&nbsp;&nbsp;";
                                  if (info.isProduction()) {
                                    indent = "";
                                  }

                                  String http_scheme = null;
                                  String http_version = null;

                                  if (scheme != null)
                                  http_scheme = scheme.replaceAll(" ", "%20");
                                  if (version != null)
                                  http_version = version.replaceAll(" ", "%20");
                                  %>

                                  <%
                                  //if (sort_category != curr_sort_category.intValue()) {
                                    if (indent.length() == 0 && !blank_line_added && !DataUtils.isNCIT_OR_NCIM(display_name)) {
                                      %>
                                      <tr>
                                        <td width="25px">&nbsp;</td>
                                        <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                      </tr>
                                      <%
                                      blank_line_added = true;
                                    }
                                    curr_sort_category = Integer.valueOf(sort_category);
                                    %>

                                    <tr>
                                      <td width="25px"></td>
                                      <td>
                                        <%
                                        //boolean checked = info.getSelected();
                                        boolean checked = false;
                                        if (ontologiesToSearchOnStr.indexOf(info.getLabel()) != -1) {
                                          checked = true;
                                        }

                                        String checkedStr = checked ? "checked" : "";
                                        %>

                                        <%= indent %>
                                        <input aria-label="<%=label%>" 
                                            type="checkbox"
                                            id="<%=label%>"
                                            name="ontology_list"
                                            value="<%=label%>"
                                            <%=checkedStr%>
                                        />
                                        
                                        <%
                                        String full_name = DataUtils.getMetadataValue(DataUtils.getFormalName(scheme), version, "full_name");
                                        if (full_name == null || full_name.compareTo("null") == 0) {
                                          full_name = scheme;
                                        }

                                        String term_browser_version = DataUtils.getMetadataValue(scheme, version, "term_browser_version");

                                        if (term_browser_version == null || term_browser_version.compareTo("null") == 0) {
                                          term_browser_version = version;
                                        }
                                        String display_label = display_name + ":&nbsp;" + full_name + "&nbsp;(" + term_browser_version + ")";

                                        if (DataUtils.isNCIT(scheme)) {
                                          String nciturl = request.getContextPath() + "/pages/home.jsf" + "?version=" + version;
                                          %>
                                          <a href="<%=nciturl%>"><%= display_label %></a>
                                          <%
                                        } else if (scheme.compareToIgnoreCase("NCI Metathesaurus") == 0) {
                                          String ncimurl = NCItBrowserProperties.getNCIM_URL();
                                          %>
                                          <a href="<%=ncimurl%>" target="_blank" rel="noopener">
                                            <%= display_label %>
                                            <img
                                                src="<%= request.getContextPath() %>/images/window-icon.gif"
                                                width="10"
                                                height="11"
                                                border="0"
                                                alt="<%=display_label%>"
                                            />
                                          </a>
                                        <% } else { %>
                                          <a
                                              href="<%= request.getContextPath() %>/pages/vocabulary.jsf?dictionary=<%=http_scheme%>&version=<%=http_version%>">

                                            <%= display_label %>
                                          </a>
                                        <% } %>

                                        <%
                                        String cs_nm = info.getCodingScheme();
                                        if (info.isProduction() && info.getHasMultipleVersions() && !info.getExpanded()) {
                                          %>
                                          &nbsp&nbsp;
                                          <a href="#" onclick="javascript:show_others('<%=cs_nm%>')";>
                                            <i>
                                              <font color="red">[show other versions]</font>
                                            </i>
                                          </a>

                                        </td>

                                      </td>

                                    </font>

                                    <%
                                  } else if (info.isProduction() && info.getHasMultipleVersions() && info.getExpanded()) {
                                    %>
                                    &nbsp&nbsp;
                                    <a href="#" onclick="javascript:hide_others('<%=cs_nm%>')";>
                                      <i>
                                        <font color="red">[hide other versions]</font>
                                      </i>
                                    </a>

                                  </td>

                                </td></font>

                            <% } %>

                          <% } %>
                        </td>
                      </tr>
                    <% } %>

                  </table>
                </td></tr>

              <tr>
                <td height="20"></td>
              </tr>

              <tr>
                <td>
                  <img
                      src="<%= request.getContextPath() %>/images/AllbutNCIm.gif"
                      name="selectAllExceptNCIm"
                      alt="selectAllButNCIm"
                      tabindex="0"
                      onClick="checkAllButOne(document.searchTerm.ontology_list, 'Metathesaurus')"
                  />
                  &nbsp;&nbsp;
                  <img
                      src="<%= request.getContextPath() %>/images/selectall.gif"
                      name="selectAll"
                      alt="selectAll"
                      tabindex="0"
                      onClick="checkAll(document.searchTerm.ontology_list)"
                  />
                  &nbsp;&nbsp;
                  <h:commandButton
                      id="clearall"
                      value="clearall"
                      action="#{userSessionBean.clearAll}"
                      image="#{requestContextPath}/images/clear.gif"
                      alt="reset"
                      tabindex="0">

                  </h:commandButton>
                  &nbsp;&nbsp;
                  <h:commandButton
                      id="multipleSearch"
                      value="Search"
                      action="#{userSessionBean.multipleSearchAction}"
                      image="#{requestContextPath}/images/search.gif"
                      alt="Search"
                      tabindex="0">

                  </h:commandButton>

                </td>

                <%
                if (warning_msg != null) {
                  request.getSession().removeAttribute("ontologiesToSearchOn");
                }
                %>
              </tr>

            </table>

          <% } %></td>

        <td valign="top" width="300px" align="right">
          <table border="0"  role='presentation'>
            <tr valign="top">
              <td width="10px"></td>
              <td>
                <a href="https://evs.nci.nih.gov/" target="_blank" rel="noopener" alt="EVS">
                  <img
                      src="<%= request.getContextPath() %>/images/EVSTile.gif"
                      width="77"
                      height="38px"
                      alt="EVS"
                      border="0"
                  />
                </a>
              </td>
              <td width="3px"></td>
              <td class="textbody" valign="top">
                <a href="https://evs.nci.nih.gov/" target="_blank" rel="noopener" alt="EVS">NCI Enterprise Vocabulary Services</a>:
                Terminology resources and services for NCI and the biomedical community.
              </td>
            </tr>
            <tr valign="top">
              <td width="10px"></td>
              <td>
                <a href="<%=nci_meta_url%>" target="_blank" rel="noopener" alt="NCIm">
                  <img
                      src="<%= request.getContextPath() %>/images/NCImTile.gif"
                      width="77"
                      height="38px"
                      alt="NCIm"
                      border="0"
                  />
                </a>
              </td>
              <td width="3px"></td>
              <td class="textbody" valign="top">
                <a href="<%=nci_meta_url%>" target="_blank" rel="noopener" alt="NCIm">NCI Metathesaurus</a>:
                Comprehensive database of 6,700,000 terms from over 85 terminologies.
              </td>
            </tr>
            <tr valign="top">
              <td width="10px"></td>
              <td>
                <a href="<%=ncit_url%>" target="_blank" rel="noopener" alt="NCI Thesaurus">
                  <img src="<%=basePath%>/images/NCItTile.jpg" width="77" height="38px" alt="NCIt" border="0" />
                </a>
              </td>
              <td width="3px"></td>
              <td class="textbody" valign="top">
                <a href="<%=ncit_url%>" target="_blank" rel="noopener" alt="NCI Thesaurus">NCI Thesaurus</a>:
                Reference terminology for NCI, NCI Metathesaurus and NCI informatics infrastructure.
              </td>
            </tr>
            <tr valign="top">
              <td width="10px"></td>
              <td>
                <a
                    href="https://www.cancer.gov/cancertopics/terminologyresources/"
                    target="_blank" rel="noopener"
                    alt="NCI Terminology Resources">

                  <img
                      src="<%= request.getContextPath() %>/images/Cancer_govTile.gif"
                      alt="NCI Terminology Resources"
                      border="0"
                  />
                </a>
              </td>
              <td width="3px"></td>
              <td class="textbody" valign="top">
                <a
                    href="https://www.cancer.gov/cancertopics/terminologyresources/"
                    target="_blank" rel="noopener"
                    alt="NCI Terminology Resources">

                  NCI Terminology Resources</a>:
                More information on NCI dictionaries and resources.
              </td>
            </tr>

          </table>
        </td>

        <tr>
        </table>
        <!--
          modification
          ends----------------------------------------------------------------------------------------------
        -->

      </div>
      <!-- end tabTableContentContainer -->
      <%@ include file="/pages/templates/nciFooter.jsp" %>
    </div>
    <!-- end Page content -->
  </div>
  <!-- end main-area_960 -->
  <div class="mainbox-bottom">
    <img src="<%=basePath%>/images/mainbox-bottom.gif" width="945" height="5" alt="Mainbox Bottom" />
  </div>

  <input type="hidden" name="<%=TokenUtils.CSRF_TOKEN%>" id="<%=TokenUtils.CSRF_TOKEN%>" value="<%=token%>" />

  </h:form>

  </div>
  <!-- end center-page_960 -->
  <br>
  </f:view>
  <%
  request.getSession().removeAttribute("dictionary");
  request.getSession().removeAttribute("message");
  request.getSession().removeAttribute("warning");
  request.getSession().removeAttribute("ontologiesToSearchOn");
  request.getSession().putValue("visited","true");
  %>
  <br />
  <script type="text/javascript">_satellite.pageBottom();</script>
  </body></html>
