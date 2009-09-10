<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils"%>
<%@ page import="gov.nih.nci.evs.browser.properties.PropertyFileParser"%>
<%@ page
  import="gov.nih.nci.evs.browser.properties.NCItBrowserProperties"%>
<%@ page import="gov.nih.nci.evs.browser.bean.DisplayItem"%>
<%@ page import="gov.nih.nci.evs.browser.bean.*"%>
<%@ page import="gov.nih.nci.evs.browser.utils.*"%>
<%@ page import="org.LexGrid.concepts.Concept"%>
<%@ page import="org.LexGrid.concepts.Presentation"%>
<%@ page import="org.LexGrid.commonTypes.Source"%>
<%@ page import="org.LexGrid.commonTypes.EntityDescription"%>
<%@ page import="org.LexGrid.commonTypes.Property"%>
<%@ page import="org.LexGrid.commonTypes.PropertyQualifier"%>
<%@ page import="gov.nih.nci.evs.browser.common.Constants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <title>NCI Terms Browser</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <link rel="stylesheet" type="text/css"
    href="<%= request.getContextPath() %>/css/styleSheet.css"></link>
  <script type="text/javascript"
    src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript"
    src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript"
    src="<%= request.getContextPath() %>/js/dropdown.js"></script>
</head>
<body>
<f:view>
  <form name="searchTerm" method="post">
  <%@ include file="/pages/templates/header.xhtml"%>
  <div class="center-page"><%@ include file="/pages/templates/sub-header.xhtml"%>
    <div class="bannerarea">
        <div class="banner"><a href="<%=basePath%>"><img
          src="<%=basePath%>/images/evs_termsbrowser_logo.gif" width="383"
          height="82" alt="Thesaurus Browser Logo" border="0" /></a></div>
        <div class="search-globalnav">
          <div class="searchbox-top"><img
            src="<%=basePath%>/images/searchbox-top.gif" width="352"
            height="2" alt="SearchBox Top" /></div>
            <%
              String match_text = gov.nih.nci.evs.browser.utils.HTTPUtils
              .cleanXSS((String) request.getSession().getAttribute(
              "matchText"));
              if (match_text == null)
                match_text = "";
             %>
          <div class="searchbox">
            <div class="search-form">
              <input CLASS="searchbox-input" name="matchText"
                value="<%=match_text%>" onFocus="active = true"
                onBlur="active = false"
                onkeypress="return submitEnter('search',event)" />
              <h:commandButton
                id="search" value="Search"
                action="#{userSessionBean.multipleSearchAction}"
                image="#{facesContext.externalContext.requestContextPath}/images/search.gif"
                alt="Search">
              </h:commandButton>
              <h:outputLink
                value="#{facesContext.externalContext.requestContextPath}/pages/help.jsf#searchhelp">
                <h:graphicImage value="/images/search-help.gif"
                  style="border-width:0;" />
                </h:outputLink>
                <%
                  String algorithm = (String) request.getSession().getAttribute(
                    "selectedAlgorithm");
                  String check_e = "", check_s = "", check_c = "";
                  if (algorithm == null || algorithm.compareTo("exactMatch") == 0)
                    check_e = "checked";
                  else if (algorithm.compareTo("startsWith") == 0)
                    check_s = "checked";
                  else
                    check_c = "checked";
                 %>
                <input type="radio" name="algorithm"
                  value="exactMatch" alt="Exact Match" <%=check_e%>>Exact
                  Match&nbsp;
                <input type="radio" name="algorithm"
                  value="startsWith" alt="Begins With" <%=check_s%>>Begins
                  With&nbsp;
                <input type="radio" name="algorithm" value="contains"
                  alt="Containts" <%=check_c%>>Contains&nbsp;
              </div> <!-- end search-form -->
            </div> <!-- end searchbox -->
            <div class="searchbox-bottom"><img
              src="<%=basePath%>/images/searchbox-bottom.gif" width="352"
              height="2" alt="SearchBox Bottom" /></div>
            <%@ include file="/pages/templates/menuBar2.xhtml"%>
          </div> <!-- end search-globalnav -->
        </div> <!-- end bannerarea -->
        <%@ include file="/pages/templates/quickLink.xhtml"%>
        <!-- Page content -->
        <div class="pagecontent">
          <div class="tabTableContentContainer">
            <p class="textbody">&nbsp;Select NCI hosted terminologies to
            search, or click on a source name to go to its browser home page.</p>
            <table class="termstable">
              <tr>
                <%
                  List ontology_list = DataUtils.getOntologyList();
                  if (ontology_list == null)
                    System.out.println("??????????? ontology_list == null");
                  int num_vocabularies = ontology_list.size();

                  String ontologiesToSearchOn = (String) request.getSession()
                      .getAttribute("ontologiesToSearchOn");
                %>
                  <td class="textbody">
                  <ol>
                    <%
                        for (int i = 0; i < ontology_list.size(); i++) {
                          SelectItem item = (SelectItem) ontology_list.get(i);
                          String value = (String) item.getValue();
                          String label = (String) item.getLabel();
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

                          if (scheme.compareTo("NCI Thesaurus") == 0) {
                     %>
                            <li>
                               <%
                                   if (ontologiesToSearchOn != null
                                      && ontologiesToSearchOn.indexOf(label2) != -1) {
                                %>
                                      <input type="checkbox" name="ontology_list" value="<%=label%>" checked />
                                <%
                                   } else {
                                %>
                                      <input type="checkbox" name="ontology_list" value="<%=label%>" /> <%
                                   } %>
                                <a href="<%= request.getContextPath() %>"><%=label%></a></li>
                           <%
                           } else if (scheme.compareTo("NCI MetaThesaurus") == 0) {
                                String ncimurl = NCItBrowserProperties.getNCIM_URL();
                            %>
                            <li>
                                 <%
                                    if (ontologiesToSearchOn != null
                                      && ontologiesToSearchOn.indexOf(label2) != -1) {
                                  %> <input type="checkbox" name="ontology_list"
                                        value="<%=label%>" checked />
                                 <%
                                     } else {
                                  %>
                                       <input type="checkbox" name="ontology_list" value="<%=label%>" />
                                 <%
                                     }
                                  %>
                                <a href="http://ncim.nci.nih.gov"><%=label%></a></li>
                            <%
                            } else {
                             %>
                            <li>
                            <%
                                if (ontologiesToSearchOn != null
                                  && ontologiesToSearchOn.indexOf(label2) != -1) {
                             %>
                                       <input type="checkbox" name="ontology_list" value="<%=label%>" checked /
                            <%
                                } else {
                             %>
                                       <input type="checkbox" name="ontology_list" value="<%=label%>" />
                            <%
                                }
                             %>
                         <a href="<%= request.getContextPath() %>/pages/vocabulary.jsf?dictionary=<%=http_label%>&scheme=<%=http_scheme%>&version=<%=http_version%>">
                            <%=label%>
                         </a>
                       </li>
                  <%
                        }
                    }
                  %>
                  </ol>
                  </td>
                </tr>
                <tr>
                  <td><img
                    src="<%= request.getContextPath() %>/images/selectAll.gif"
                    name="selectAll" alt="selectAll"
                    onClick="checkAll(document.searchTerm.ontology_list)" />
                  &nbsp;&nbsp; <img
                    src="<%= request.getContextPath() %>/images/reset.gif"
                    name="reset" alt="reset"
                    onClick="uncheckAll(document.searchTerm.ontology_list)" />
                  &nbsp;&nbsp; <h:commandButton id="search" value="Search"
                    action="#{userSessionBean.multipleSearchAction}"
                    image="#{facesContext.externalContext.requestContextPath}/images/search.gif"
                    alt="Search">
                  </h:commandButton></td>
                </tr>
            </table>
        </div> <!-- end tabTableContentContainer -->
        <%@ include file="/pages/templates/nciFooter.html"%>
      </div> <!-- end pagecontent -->
    </div> <!-- end center-page -->
    <div class="mainbox-bottom"><img
      src="<%=basePath%>/images/mainbox-bottom.gif" width="745" height="5"
      alt="Mainbox Bottom" />
    </div> <!-- end Main box -->
  </f:view>
  </form>
</body>
</html>