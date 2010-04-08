<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.LexGrid.concepts.Concept" %>
<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.common.Constants" %>
<%
  String ncit_build_info = new DataUtils().getNCITBuildInfo();
  String application_version = new DataUtils().getApplicationVersion();
  String anthill_build_tag_built = new DataUtils().getNCITAnthillBuildTagBuilt();
  String evs_service_url = new DataUtils().getEVSServiceURL();
%>
<!--
   Build info: <%=ncit_build_info%>
 Version info: <%=application_version%>
          Tag: <%=anthill_build_tag_built%>
   LexEVS URL: <%=evs_service_url%>
  -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>NCI Term Browser</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
</head>
<!--
<body onload="checkVisited();">
-->
<body>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/wz_tooltip.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_centerwindow.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_followscroll.js"></script>
  <script language="JavaScript">
     function checkVisited() {
       var test = '<%= request.getSession().getAttribute("visited") %>';
       if (test == "" || test == "null")
         checkAllButOne(document.searchTerm.ontology_list, 'Metathesaurus');
     }
  </script>
<%
    request.getSession().removeAttribute("dictionary");
    
%>
<f:view>
  <%@ include file="/pages/templates/header.jsp" %>
  <div class="center-page">
    <form name="searchTerm">
    <%@ include file="/pages/templates/sub-header.jsp" %>
    <!-- Main box -->
    <div id="main-area">
        <input type="hidden" name="initial_search" value="true" />
        <%@ include file="/pages/templates/content-header-termbrowser.jsp" %>
        <!-- Page content -->
        <div class="pagecontent">
          <div class="tabTableContentContainer">




          <table class="termstable" border="0">
                <tr>
                  <td><img
                    src="<%= request.getContextPath() %>/images/selectAll.gif"
                    name="selectAll" alt="selectAll"
                    onClick="checkAll(document.searchTerm.ontology_list)" />

                  &nbsp;&nbsp; <img
                    src="<%= request.getContextPath() %>/images/AllbutNCIm.gif"
                    name="reset" alt="selectAllButNCIm"
                    onClick="checkAllButOne(document.searchTerm.ontology_list, 'Metathesaurus')" />

                  &nbsp;&nbsp; <img
                    src="<%= request.getContextPath() %>/images/clear.gif"
                    name="reset" alt="reset"
                    onClick="uncheckAll(document.searchTerm.ontology_list)" />

                  &nbsp;&nbsp; <h:commandButton id="search" value="Search"
                    action="#{userSessionBean.multipleSearchAction}"
                    image="#{facesContext.externalContext.requestContextPath}/images/search.gif"
                    alt="Search">
                  </h:commandButton></td>
                </tr>
          </table>


             <%
             String warning_msg = (String) request.getSession().getAttribute("warning");
             if (warning_msg != null) {
             %>
                <p class="textbodyred">&nbsp;<%=warning_msg%></p>
             <%
             }
    
             String unsupported_vocabulary_message = (String) request.getSession().getAttribute("unsupported_vocabulary_message");
	     if (unsupported_vocabulary_message != null && unsupported_vocabulary_message.compareTo("null") != 0) {
	        request.getSession().removeAttribute("unsupported_vocabulary_message"); 
             %>
                <p class="textbodyred">&nbsp;<%=unsupported_vocabulary_message%></p>
             <%
	     }             
             
             request.getSession().removeAttribute("warning");
             String hide_ontology_list = (String) request.getSession().getAttribute("hide_ontology_list");
             request.getSession().removeAttribute("hide_ontology_list");
             if (hide_ontology_list == null || hide_ontology_list.compareTo("false") == 0) {
             %>
            <span class="textbody">&nbsp;Select NCI hosted terminologies to search, or click on a source name to go to its browser home page.
            <br/>
            &nbsp;(WARNING: <b>Select All</b> searches with thousands of hits may be slow; try NCI Metathesaurus separately.)
            <br/><br/>
            </span>
            <table class="termstable" border="0">

              <tr>
              <%
                List ontology_list = DataUtils.getOntologyList();
                if (ontology_list == null) System.out.println("??????????? ontology_list == null");
                int num_vocabularies = ontology_list.size();

//KLO 010610
//String ontologiesToSearchOn = (String) request.getSession().getAttribute("ontologiesToSearchOn");

String ontologiesToSearchOn = (String) request.getSession().getAttribute("defaultOntologiesToSearchOnStr");
if (ontologiesToSearchOn == null) {
  ontologiesToSearchOn = DataUtils.getDefaultOntologiesToSearchOnStr();
}
if (warning_msg != null && warning_msg.compareTo(Constants.ERROR_NO_VOCABULARY_SELECTED) == 0) {
   ontologiesToSearchOn = "|";
}

                HashMap display_name_hmap = null;
                Vector display_name_vec = null;
                display_name_hmap = (HashMap) request.getSession().getAttribute("display_name_hmap");
                display_name_vec = (Vector) request.getSession().getAttribute("display_name_vec");
                if (display_name_hmap == null || display_name_vec == null) {
                        display_name_hmap = new HashMap();
                        display_name_vec = new Vector();

                  for (int i = 0; i < ontology_list.size(); i++) {
                    SelectItem item = (SelectItem) ontology_list.get(i);
                    String value = (String) item.getValue();
                    String label = (String) item.getLabel();
                    String scheme = DataUtils.key2CodingSchemeName(value);
                    String version = DataUtils.key2CodingSchemeVersion(value);
                    String display_name = DataUtils.getMetadataValue(scheme, "display_name");
                    if (display_name == null || display_name.compareTo("null") == 0) display_name = DataUtils.getLocalName(scheme);
                    display_name_hmap.put(display_name, value);
                    display_name_vec.add(display_name);
                  }
                  display_name_vec = SortUtils.quickSort(display_name_vec);
                  request.getSession().setAttribute("display_name_hmap", display_name_hmap);
                  request.getSession().setAttribute("display_name_vec", display_name_vec);
                }
                %>
                  <td class="textbody">
                    <table border="0" cellpadding="0" cellspacing="0">
                      <%
                      for (int i = 0; i < display_name_vec.size(); i++) {
                        String display_name = (String) display_name_vec.elementAt(i);
                        String value = (String)  display_name_hmap.get(display_name);
                        String label = (String)  display_name_hmap.get(display_name);
                        String label2 = "|" + label + "|";
                        String scheme = DataUtils.key2CodingSchemeName(value);
                        String version = DataUtils.key2CodingSchemeVersion(value);
                        String http_label = null;
                        String http_scheme = null;
                        String http_version = null;

                        if (label != null)
                          http_label = label.replaceAll(" ", "%20");
                        if (scheme != null)
                          http_scheme = scheme.replaceAll(" ", "%20");
                        if (version != null)
                          http_version = version.replaceAll(" ", "%20");
                        %>
                        <tr>
                          <td width="25px"></td>
                          <td>
                        <%
                        if (scheme.compareTo("NCI Thesaurus") == 0) {
                           String nciturl = NCItBrowserProperties.getNCIT_URL();
                           if (ontologiesToSearchOn != null
                              && ontologiesToSearchOn.indexOf(label2) != -1) {
                           %>
                              <input type="checkbox" name="ontology_list" value="<%=label%>" checked />
                           <%
                           } else {
                            %>
                              <input type="checkbox" name="ontology_list" value="<%=label%>" /> <%
                           }
                           %>
                              <a href="<%=nciturl %>"><%=label%></a>
                           <%
                        } else if (scheme.compareToIgnoreCase("NCI Metathesaurus") == 0) {
                              String ncimurl = NCItBrowserProperties.getNCIM_URL();
                            if (ontologiesToSearchOn != null
                               && ontologiesToSearchOn.indexOf(label2) != -1) {
                           %>
                              <input type="checkbox" name="ontology_list"
                                      value="<%=label%>" checked />
                           <%
                            } else {
                            %>
                              <input type="checkbox" name="ontology_list" value="<%=label%>" />
                           <%
                            }
                            %>
                              <a href="<%=ncimurl%>" target="_blank"><%=label%>
                                <img src="<%= request.getContextPath() %>/images/window-icon.gif" width="10" height="11" border="0" alt="<%=label%>" />
                              </a>
                           <%
                        } else {
                           if (ontologiesToSearchOn != null
                              && ontologiesToSearchOn.indexOf(label2) != -1) {
                            %>
                              <input type="checkbox" name="ontology_list" value="<%=label%>" checked />
                           <%
                           } else {
                            %>
                              <input type="checkbox" name="ontology_list" value="<%=label%>" />
                           <%
                           }

                           String full_name = DataUtils.getMetadataValue(scheme, "full_name");
                           if (full_name == null || full_name.compareTo("null") == 0) full_name = scheme;
                           String term_browser_version = DataUtils.getMetadataValue(scheme, "term_browser_version");
                           if (term_browser_version == null || term_browser_version.compareTo("null") == 0) term_browser_version = version;

                           label = display_name + ":&nbsp;" + full_name + "&nbsp;(" + term_browser_version + ")";

                            %>
                              <a href="<%= request.getContextPath() %>/pages/vocabulary.jsf?dictionary=<%=http_scheme%>&version=<%=http_version%>">
                                <%=label%>
                              </a>
                           <%
                        }
                      %>
                        </td>
                      </tr>
                     <%
                      }
                     %>
                    </table>
                  </td>
                </tr>
                <tr><td height="20"></td></tr>
                <tr>
                  <td><img
                    src="<%= request.getContextPath() %>/images/selectAll.gif"
                    name="selectAll" alt="selectAll"
                    onClick="checkAll(document.searchTerm.ontology_list)" />

                  &nbsp;&nbsp; <img
                    src="<%= request.getContextPath() %>/images/AllbutNCIm.gif"
                    name="reset" alt="selectAllButNCIm"
                    onClick="checkAllButOne(document.searchTerm.ontology_list, 'Metathesaurus')" />

                  &nbsp;&nbsp; <img
                    src="<%= request.getContextPath() %>/images/clear.gif"
                    name="reset" alt="reset"
                    onClick="uncheckAll(document.searchTerm.ontology_list)" />

                  &nbsp;&nbsp; <h:commandButton id="search" value="Search"
                    action="#{userSessionBean.multipleSearchAction}"
                    image="#{facesContext.externalContext.requestContextPath}/images/search.gif"
                    alt="Search">
                  </h:commandButton></td>
                   <%
                   if (warning_msg != null) {
                      request.getSession().removeAttribute("ontologiesToSearchOn");
                   }
                  %>
                </tr>
            </table>
<%
}
%>
          </div> <!-- end tabTableContentContainer -->
          <%@ include file="/pages/templates/nciFooter.html"%>
        </div> <!-- end Page content -->
    </div> <!-- end main-area -->
    <div class="mainbox-bottom"><img src="<%=basePath%>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
  </form>
  </div> <!-- end center-page -->
  <br>
</f:view>
<%
    request.getSession().removeAttribute("dictionary");
    request.getSession().removeAttribute("ontologiesToSearchOn");
    //request.getSession().removeAttribute("matchText");
    request.getSession().putValue("visited","true");
%>
<br/>
</body>
</html>