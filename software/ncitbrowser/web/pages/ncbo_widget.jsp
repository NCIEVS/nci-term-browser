<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Vector" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.common.*" %>
<%@ page import="gov.nih.nci.evs.browser.properties.*" %>

<%
String basePath = request.getContextPath();
String dictionary = HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
String code = HTTPUtils.cleanXSS((String) request.getParameter("code"));
String url = DataUtils.getVisualizationWidgetURL(dictionary, code);
%>

<html lang="en" xmlns:c="http://java.sun.com/jsp/jstl/core"> 
  <head></head>
  <script
      src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js">
  </script>
  <body>
  <header class="flex-grow-0">
	<div style='text-align: left'>
	<include-html src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html"></include-html>
	</div>
  </header>

    <% if (url == null) { %>
      <p class="textbodyred">Visualization not supported.</p>
      <%
    } else {

      //System.out.println(url);
      %>
      <iframe title="NCBO visualization widget" src="<%=url%>" sandbox width="550" height="550" frameborder="0"></iframe>
    <% } %>
    <script type="text/javascript">_satellite.pageBottom();</script>
  </body>
</html>

