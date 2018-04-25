<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.common.Constants" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.IteratorBean" %>
<%@ page import="org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="org.apache.log4j.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="org.lexgrid.valuesets.LexEVSValueSetDefinitionServices" %>
<%@ page import="org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator" %>
<%@ page import="org.LexGrid.valueSets.ValueSetDefinition" %>

<%@ page import="org.LexGrid.LexBIG.LexBIGService.*" %>

<% String vsBasePath = request.getContextPath(); %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns:c="http://java.sun.com/jsp/jstl/core">
  <head>
    <title>NCI Term Browser - test LexEVSAPI</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/fonts.css" />
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/grids.css" />
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/code.css" />
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/tree.css" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>

  </head>
  <body onLoad="document.forms.valueSetSearchForm.matchText.focus();">
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/wz_tooltip.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_centerwindow.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_followscroll.js"></script>

    <%! private static Logger _logger = Utils.getJspLogger("value_set_entity_search_results.jsp"); %>

<%
	LexBIGService lbSvc = RemoteServerUtil.createLexBIGService(); 
	long ms = System.currentTimeMillis();
	AssertedVSearchUtils avssu = new AssertedVSearchUtils(lbSvc);
	String serviceUrl = RemoteServerUtil.getServiceUrl(); 
	AssertedValueSetUtils avsu = new AssertedValueSetUtils(serviceUrl, lbSvc);
	//Red (Code C48326)
        Vector schemes = new Vector();
        Vector versions = new Vector();
        schemes.add("http://evs.nci.nih.gov/valueset/FDA/C54453");
        versions.add(null);
        schemes.add("http://evs.nci.nih.gov/valueset/FDA/C102833");
        versions.add(null);   
        
        String rvs_uri = "http://evs.nci.nih.gov/valueset/FDA/C54453";
        String matchText = "red"; 
        int searchOption = 2;
        String algorithm = "contains";
        ResolvedConceptReferencesIterator iterator = null;
        ResolvedConceptReferencesIterator iterator2 = null;
        iterator2 = avsu.getValueSetIteratorForURI(rvs_uri);
        
	try {
		 iterator = avssu.search(schemes, versions, matchText, searchOption, algorithm);
		 try {
			 if (iterator == null) {
				 System.out.println("Iterator is null???");
				 return;
			 }
			 int numRemaining = iterator.numberRemaining();
			 System.out.println("\tNumber of matches: " + numRemaining);
		 } catch (Exception ex) {
			 ex.printStackTrace();
		 }
	} catch (Exception ex) {
		 ex.printStackTrace();
	}

	System.out.println("RVSUtils initialization run time (ms): " + (System.currentTimeMillis() - ms));
%>
      <f:view>
        <!-- Begin Skip Top Navigation -->
        <a href="#evs-content" class="skip-main" accesskey="1" title="Skip repetitive navigation links">
          skip navigation links</A>
        <!-- End Skip Top Navigation -->
        <%@ include file="/pages/templates/header.jsp" %>
        
       <div>
 
       <table
	   class="datatable_960"
	   summary=""
	   cellpadding="3"
	   cellspacing="0"
	   border="0"
	   width="100%">

	 <th class="dataTableHeader" scope="col" align="left">Value Set URI</th>
	 <th class="dataTableHeader" scope="col" align="left">Vocabulary</th>
	 <th class="dataTableHeader" scope="col" align="left">Version</th>
	 <th class="dataTableHeader" scope="col" align="left">Name</th>
	 <th class="dataTableHeader" scope="col" align="left">Code</th>
	 <th class="dataTableHeader" scope="col" align="left">namespace</th>

	 <%
	 while (iterator.hasNext()) {
	 	ResolvedConceptReference ref = (ResolvedConceptReference) iterator.next();
	 %>	
	 	<tr valign="top" align="left">
	 	<td><%=ref.getCodingSchemeURI()%></td>
	 	<td><%=ref.getCodingSchemeName()%></td>
	 	<td><%=ref.getCodingSchemeVersion()%></td>
	 	<td><%=ref.getEntityDescription().getContent()%></td>
	 	<td><%=ref.getConceptCode()%></td>
	 	<td><%=ref.getCodeNamespace()%></td>
	 	</tr>
	 <%	
	 }
	 %>
     </table>
     
     <br></br>
     
       <table
	   class="datatable_960"
	   summary=""
	   cellpadding="3"
	   cellspacing="0"
	   border="0"
	   width="100%">

	 <th class="dataTableHeader" scope="col" align="left">Value Set URI</th>
	 <th class="dataTableHeader" scope="col" align="left">Vocabulary</th>
	 <th class="dataTableHeader" scope="col" align="left">Version</th>
	 <th class="dataTableHeader" scope="col" align="left">Name</th>
	 <th class="dataTableHeader" scope="col" align="left">Code</th>
	 <th class="dataTableHeader" scope="col" align="left">namespace</th>

	 <%
	 while (iterator2.hasNext()) {
	 	ResolvedConceptReference ref = (ResolvedConceptReference) iterator2.next();
	 %>	
	 	<tr valign="top" align="left">
	 	<td><%=ref.getCodingSchemeURI()%></td>
	 	<td><%=ref.getCodingSchemeName()%></td>
	 	<td><%=ref.getCodingSchemeVersion()%></td>
	 	<td><%=ref.getEntityDescription().getContent()%></td>
	 	<td><%=ref.getConceptCode()%></td>
	 	<td><%=ref.getCodeNamespace()%></td>
	 	</tr>
	 <%	
	 }
	 %>
     </table>     
     
     </div>

            <div class="mainbox-bottom">
              <img src="<%=basePath%>/images/mainbox-bottom.gif" width="945" height="5" alt="Mainbox Bottom" />
            </div>
        </f:view>
        <script type="text/javascript">_satellite.pageBottom();</script>
      </body>
    </html>