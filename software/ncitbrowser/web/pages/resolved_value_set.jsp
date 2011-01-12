<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*"%>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.properties.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference" %>
<%@ page import="org.apache.log4j.*" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>
  <title>NCI Thesaurus</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
</head>
<body>
  <script type="text/javascript"
    src="<%=request.getContextPath()%>/js/wz_tooltip.js"></script>
  <script type="text/javascript"
    src="<%=request.getContextPath()%>/js/tip_centerwindow.js"></script>
  <script type="text/javascript"
    src="<%=request.getContextPath()%>/js/tip_followscroll.js"></script>


  <%!
    private static Logger _logger = Utils.getJspLogger("value_set_search_results.jsp");
  %>
  <f:view>
    <!-- Begin Skip Top Navigation -->
      <a href="#evs-content" class="hideLink" accesskey="1" title="Skip repetitive navigation links">skip navigation links</A>
    <!-- End Skip Top Navigation -->  
    <%@ include file="/pages/templates/header.jsp" %>
    <div class="center-page">
      <%@ include file="/pages/templates/sub-header.jsp" %>
      <!-- Main box -->
      <div id="main-area">
        <%@ include file="/pages/templates/content-header-termbrowser.jsp" %>
        
<%

String valueSetSearch_requestContextPath = request.getContextPath();

System.out.println("valueSetSearch_requestContextPath: " + valueSetSearch_requestContextPath);


String message = (String) request.getSession().getAttribute("message");          
String vsd_uri = (String) request.getSession().getAttribute("selectedvalueset");
String metadata = DataUtils.getValueSetDefinitionMetadata(DataUtils.findValueSetDefinitionByURI(vsd_uri));
Vector u = DataUtils.parseData(metadata);
String description = (String) u.elementAt(1);
String concept_domain = (String) u.elementAt(2);
String sources = (String) u.elementAt(3);

%>
        <div class="pagecontent">
          <a name="evs-content" id="evs-content"></a>
          <table>
            <tr>
            <!--
                <td class="texttitle-blue">Value Set:&nbsp;<%=vsd_uri%></td>
             -->
                <td>
                     <table>
                         <tr>
                             <td class="texttitle-blue">Value Set:&nbsp;<%=vsd_uri%></td>
                             
			     <td align="right">
				<h:commandLink value="Export XML" action="#{valueSetBean.exportToXMLAction}" styleClass="texttitle-blue-small" title="Export VSD in LexGrid XML format"/> |
				<h:commandLink value="Export CSV" action="#{valueSetBean.exportToCSVAction}" styleClass="texttitle-blue-small" title="Export VSD in CSV format"/>				
			     </td>                             
                         </tr>
                     </table>
                </td>
            </tr>
            
            <% if (message != null) { %>
        <tr class="textbodyred"><td>
      <p class="textbodyred">&nbsp;<%=message%></p>
        </td></tr>
            <% } %>
            <tr class="textbody"><td><b>Description</b>: <%=description%></td>
            <tr class="textbody"><td><b>Concept Domain</b>: <%=concept_domain%></td>
            <tr class="textbody"><td><b>Sources</b>: <%=sources%></td>
            
            <tr class="textbody"><td>&nbsp;</td>
            <tr class="textbody"><td><b>Concepts</b>:</td>

            <tr class="textbody"><td>

 <h:form id="valueSetSearchResultsForm" styleClass="search-form">            
               
              <table class="dataTable" summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
             
                <th class="dataTableHeader" scope="col" align="left">Code</th>
                <th class="dataTableHeader" scope="col" align="left">Name</th>
                <th class="dataTableHeader" scope="col" align="left">Coding Scheme</th>
                <th class="dataTableHeader" scope="col" align="left">Namespace</th>

<%
Vector concept_vec = new Vector();
concept_vec.add("Code #1|Name #1|Coding Scheme #1|Namespace #1");
concept_vec.add("Code #2|Name #2|Coding Scheme #1|Namespace #1");
concept_vec.add("Code #3|Name #3|Coding Scheme #1|Namespace #1");
concept_vec.add("Code #4|Name #4|Coding Scheme #2|Namespace #2");


            for (int i=0; i<concept_vec.size(); i++) {
            
		    String concept_str = (String) concept_vec.elementAt(i);
		    u = DataUtils.parseData(concept_str);
		    String code = (String) u.elementAt(0);
		    String name = (String) u.elementAt(1);
		    String coding_scheme = (String) u.elementAt(2);
		    String namespace = (String) u.elementAt(3);

		    if (i % 2 == 0) {
		    %>
		      <tr class="dataRowDark">
		    <%
			} else {
		    %>
		      <tr class="dataRowLight">
		    <%
			}
		    %>    
		
		      <td class="dataCellText">
			 <%=code%>
		      </td>
		      <td class="dataCellText">
			 <%=name%>
		      </td>
		      <td class="dataCellText">
			 <%=coding_scheme%>
		      </td>
		      <td class="dataCellText">
			 <%=namespace%>
		      </td>  

		      </tr>
              
              
             <%
                }
             %>                 
                  
              </table>

                  <tr><td>
                    <h:commandButton id="export" value="export" action="#{valueSetBean.exportValueSetAction}"
                      onclick="javascript:cursor_wait();"
                      image="#{valueSetSearch_requestContextPath}/images/export.gif"
                      alt="Export"
                      tabindex="2">
                    </h:commandButton>
                  </td></tr>
              
              <input type="hidden" name="referer" id="referer" value="<%=HTTPUtils.getRefererParmEncode(request)%>">
</h:form>
            
          </td></tr>
        </table>
        <%@ include file="/pages/templates/nciFooter.jsp" %>
      </div>
      <!-- end Page content -->
    </div>
    <div class="mainbox-bottom"><img src="<%=basePath%>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
    <!-- end Main box -->
  </div>
</f:view>
</body>
</html>
