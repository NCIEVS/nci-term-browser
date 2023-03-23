<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Vector" %>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.HTTPUtils" %>

<%
String ncit_build_info = DataUtils.getNCITBuildInfo();
String application_version = DataUtils.getApplicationVersion();
String anthill_build_tag_built = DataUtils.getNCITAnthillBuildTagBuilt();
String evs_service_url = DataUtils.getEVSServiceURL();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html lang="en" xmlns:c="http://java.sun.com/jsp/jstl/core">
  <head>
    <script
        src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js">
    </script>
<!-- Google tag (gtag.js) -->
<script async src="https://www.googletagmanager.com/gtag/js?id=G-21QRTJ0WQS"></script>
<script>
	window.dataLayer = window.dataLayer || [];
	function gtag(){dataLayer.push(arguments);}
	gtag('js', new Date());
	gtag('config', 'G-21QRTJ0WQS');
</script>
    <title>NCI Thesaurus</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
  </head>
  <body onLoad="document.forms.searchTerm.matchText.focus();">
    <!--
      uild info: <%=ncit_build_info%> Version info: <%=application_version%> Tag: <%=anthill_build_tag_built%> LexEVS
      URL: <%=evs_service_url%>
    -->
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/wz_tooltip.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_centerwindow.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_followscroll.js"></script>

<%
  String queryString = request.getQueryString();
%>  
<%     
  if (queryString != null && (queryString.indexOf("style") != -1 || queryString.indexOf("alert") != -1 || queryString.indexOf("netsparker") != -1)) {
%>       
<h2>
<center>Server Error</center>
</h2>
      	<center><b>The server encountered an unexpected condition that prevented it from fulfilling the request.</b></center>
<%      
  } else {
%> 

    <%
    Boolean ncit_available = DataUtils.isNCITAvailable();

    if (ncit_available == null || !ncit_available.equals(Boolean.TRUE)) {
      String error_msg = "WARNING: " + Constants.NCIT_NOT_AVAILABLE;
      request.getSession().setAttribute("error_msg", error_msg);
      String redirectURL = request.getContextPath() + "/pages/coding_scheme_unavailable.jsf";
      redirectURL = request.getContextPath() + "/start.jsf";
      response.sendRedirect(redirectURL);
    }

    request.getSession().setAttribute("dictionary", "NCI Thesaurus");
    String vocabulary_version = HTTPUtils.cleanXSS((String) request.getParameter("version"));
    if (vocabulary_version != null) {
      request.setAttribute("version", vocabulary_version);
    }
    
    %>
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
        
          <%@ include file="/pages/templates/content-header-other.jsp" %>
          <!-- Page content -->
          <div class="pagecontent">
            <a name="evs-content" id="evs-content" tabindex="0"></a>
            
            <%@ include file="/pages/templates/welcome.jsp" %>
            <%@ include file="/pages/templates/nciFooter.jsp" %>
          </div>
          <!-- end Page content -->
        </div>
        <div class="mainbox-bottom">
          <img src="<%=basePath%>/images/mainbox-bottom.gif" width="945" height="5" alt="Mainbox Bottom" />
        </div>
        <!-- end Main box -->
      </div>
    </f:view>
    
    
<% } %>       
    
    <script type="text/javascript">_satellite.pageBottom();</script>
  </body>
</html>
