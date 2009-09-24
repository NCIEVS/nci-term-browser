<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.properties.PropertyFileParser" %>
<%@ page import="gov.nih.nci.evs.browser.properties.NCItBrowserProperties" %>
<%@ page import="gov.nih.nci.evs.browser.bean.DisplayItem" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="org.LexGrid.concepts.Concept" %>
<%@ page import="org.LexGrid.concepts.Presentation" %>
<%@ page import="org.LexGrid.commonTypes.Source" %>
<%@ page import="org.LexGrid.commonTypes.EntityDescription" %>
<%@ page import="org.LexGrid.commonTypes.Property" %>
<%@ page import="org.LexGrid.commonTypes.PropertyQualifier" %>
<%@ page import="org.LexGrid.concepts.Presentation" %>
<%@ page import="org.LexGrid.commonTypes.Source" %>
<%@ page import="org.LexGrid.commonTypes.EntityDescription" %>
<%@ page import="org.LexGrid.commonTypes.Property" %>
<%@ page import="org.LexGrid.commonTypes.PropertyQualifier" %>
<%@ page import="gov.nih.nci.evs.browser.common.Constants" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <title>NCI Thesaurus</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/wz_tooltip.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_centerwindow.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_followscroll.js"></script>
  <f:view>
    <%@ include file="/pages/templates/header.xhtml" %>
    <div class="center-page">
      <%@ include file="/pages/templates/sub-header.xhtml" %>
      <!-- Main box -->
      <div id="main-area">
       <%
          String dictionary = (String) request.getAttribute("dictionary");
          if (dictionary == null) {
              //dictionary = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
              dictionary = (String) request.getParameter("dictionary");
              dictionary = DataUtils.getCodingSchemeName(dictionary);

          }
          String shortName = "Vocabulary";
          if (dictionary != null) {
            shortName = new DataUtils().getLocalName(dictionary);
          }
          String term_suggestion_application_url = new DataUtils().getTermSuggestionURL();
          if (dictionary.compareTo("NCI Thesaurus") != 0) {
              term_suggestion_application_url = DataUtils.getTermSuggestionURL(dictionary, null);
          }

        %>
          <!-- Thesaurus, banner search area -->
          <div class="bannerarea">

       <%
          if (dictionary != null && dictionary.compareTo("NCI Thesaurus") == 0) {
        %>
                  <div><img src="<%=basePath%>/images/thesaurus_popup_banner.gif" width="612" height="56" alt="NCI Thesaurus" title="" border="0" /></div>
       <%
          } else {
        %>
              <div class="vocabularynamebanner">
                <div class="vocabularyName"><%=dictionary.substring(0,20)%></div>
              </div>
       <%
          }
        %>
          <div class="search-globalnav">
              <!-- Search box -->
              <div class="searchbox-top"><img src="<%=basePath%>/images/searchbox-top.gif" width="352" height="2" alt="SearchBox Top" /></div>
              <div class="searchbox"><%@ include file="/pages/templates/searchForm.xhtml" %></div>
              <div class="searchbox-bottom"><img src="<%=basePath%>/images/searchbox-bottom.gif" width="352" height="2" alt="SearchBox Bottom" /></div>
              <!-- end Search box -->
              <!-- Global Navigation -->

              <%@ include file="/pages/templates/menuBar.xhtml" %>

              <!-- end Global Navigation -->
          </div>
      </div>
      <!-- end Thesaurus, banner search area -->
      <!-- Quick links bar -->
      <%@ include file="/pages/templates/quickLink.xhtml" %>
      <!-- end Quick links bar -->
        <!-- Page content -->
        <div class="pagecontent">
          <%
            //String dictionary = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
            String code = null;
            String type = null;

            String singleton = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("singleton"));
            if (singleton != null && singleton.compareTo("true") == 0) {
              //dictionary = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("dictionary"));
              //dictionary = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("dictionary"));

              code = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("code"));
            } else {
              //dictionary = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
              code = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("code"));
              type = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("type"));
            }
            if (dictionary == null) {
                dictionary = Constants.CODING_SCHEME_NAME;
            }
            if (type == null) {
                type = "properties";
            }
            else if (type.compareTo("properties") != 0 &&
                     type.compareTo("relationship") != 0 &&
                     type.compareTo("synonym") != 0 &&
                     type.compareTo("all") != 0) {
                type = "properties";
            }

            String name = "";
            Concept c = null;

            String vers = null;
            String ltag = null;
            c = DataUtils.getConceptByCode(dictionary, vers, ltag, code);

            if (c != null) {
               request.getSession().setAttribute("concept", c);
               request.getSession().setAttribute("code", code);
               name = c.getEntityDescription().getContent();

               System.out.println(name);


            } else {
               name = "ERROR: Invalid code - " + code + ".";
            }

            String tg_dictionary = DataUtils.replaceAll(dictionary, " ", "%20");
            if (c != null) {
            request.getSession().setAttribute("dictionary", dictionary);
            request.getSession().setAttribute("type", type);
            request.getSession().setAttribute("singleton", "false");

          %>
          <table border="0" width="700px">
            <tr>
              <td class="texttitle-blue"><%=name%> (Code <%=code%>)</td>
              <%
              if (term_suggestion_application_url != null && term_suggestion_application_url.compareTo("") != 0) {
              %>
              <td align="right" valign="bottom" class="texttitle-blue-rightJust" nowrap>
                 <a href="<%=term_suggestion_application_url%>?dictionary=<%=tg_dictionary%>&code=<%=code%>" target="_blank" alt="Term Suggestion">Suggest changes to this concept</a>
              </td>
              <%
              }
              %>

            </tr>
          </table>
          <hr>
          <%@ include file="/pages/templates/typeLinks.xhtml" %>
          <div class="tabTableContentContainer">
              <%@ include file="/pages/templates/property.xhtml" %>
              <%@ include file="/pages/templates/relationship.xhtml" %>
              <%@ include file="/pages/templates/synonym.xhtml" %>
          </div>
              <%
              } else {
              %>
          <div class="textbody">
              <%=name%>
          </div>
           <%
              }
            %>
           <%@ include file="/pages/templates/nciFooter.html" %>
          </div>
        </div>
        <!-- end Page content -->
      </div>
      <div class="mainbox-bottom"><img src="<%=basePath%>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
      <!-- end Main box -->
    </div>
  </f:view>
</body>
</html>