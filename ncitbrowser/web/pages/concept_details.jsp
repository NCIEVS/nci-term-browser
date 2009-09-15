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
  <f:view>
    <%@ include file="/pages/templates/header.xhtml" %>
    <div class="center-page">
      <%@ include file="/pages/templates/sub-header.xhtml" %>
      <!-- Main box -->
      <div id="main-area">
      
          <%
            String dictionary = null;
            String code = null;
            String type = null;

            String singleton = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getAttribute("singleton"));


dictionary = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
            
            if (dictionary != null) {

dictionary = DataUtils.replaceAll(dictionary, "&#40;", "(");
dictionary = DataUtils.replaceAll(dictionary, "&#41;", ")");
dictionary = DataUtils.getCodingSchemeName( dictionary ); 
                
            } else {
                dictionary = Constants.CODING_SCHEME_NAME;
            }    

            if (singleton != null && singleton.compareTo("true") == 0) {

 		if (dictionary != null && dictionary.compareTo(Constants.CODING_SCHEME_NAME) != 0) {
			dictionary = DataUtils.getCodingSchemeName(dictionary);
		}
             
            } 

code = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("code"));
if (code == null) {
    code = (String) request.getSession().getAttribute("code");
}  

Boolean new_search = null;
Object new_search_obj = request.getSession().getAttribute("new_search");
if (new_search_obj != null) {
    new_search = (Boolean) new_search_obj;
    if (new_search.equals(Boolean.TRUE)) {
        type = "properties";
        request.getSession().setAttribute("new_search", Boolean.FALSE);
        
        code = (String) request.getSession().getAttribute("code");
    }
}

if (type == null) {
type = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getParameter("type"));
            
            if (type == null) {
                type = "properties";
            } else if (type.compareTo("properties") != 0 &&
                     type.compareTo("relationship") != 0 &&
                     type.compareTo("synonym") != 0 &&
                     type.compareTo("all") != 0) {
                type = "properties";
            }
}


            String term_suggestion_application_url = new DataUtils().getTermSuggestionURL();
            if (dictionary.compareTo("NCI Thesaurus") != 0) {
                term_suggestion_application_url = DataUtils.getTermSuggestionURL(dictionary, null);
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

		} else {
		   //name = "The server encountered an internal error that prevented it from fulfilling this request.";
		   name = "ERROR: Invalid code - " + code + ".";
		}

       
        if (dictionary.compareTo("NCI Thesaurus") == 0) {
        %>
        	<%@ include file="/pages/templates/content-header.xhtml" %>
        <%	
       	} else {
       	        request.getSession().setAttribute("dictionary", dictionary);
       	%>
       	        <%@ include file="/pages/templates/content-header1.xhtml" %>
       	<%        
       	}

        String tg_dictionary = DataUtils.replaceAll(dictionary, " ", "%20");
        if (c != null) {
        request.getSession().setAttribute("type", type);
        request.getSession().setAttribute("singleton", "false");

          %>      
        
        <!-- Page content -->
        <div class="pagecontent">
        


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