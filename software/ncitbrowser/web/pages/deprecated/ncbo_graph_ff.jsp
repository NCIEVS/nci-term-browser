<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="gov.nih.nci.evs.browser.utils.*" %>

<%@ page contentType="text/html;charset=windows-1252" %>
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
    <script src="https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js"></script>
    <title>NCI Term Browser</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
  </head>
  <body>
  <header class="flex-grow-0">
	<include-html src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html"></include-html>
  </header>
    <f:view>

      <%
      String code = HTTPUtils.cleanXSS((String) request.getParameter("code"));
      String id = HTTPUtils.cleanXSS((String) request.getParameter("id"));
      %>

      <object
          classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
          id="FlexoViz"
          width="100%"
          height="100%"
          codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">

        <param name="movie" value="http://bioportal.bioontology.org/flex/BasicFlexoViz.swf" />
        <param name="quality" value="high" />
        <param name="bgcolor" value="#ffffff" />
        <param name="allowScriptAccess" value="always" />
        <embed
            src="http://bioportal.bioontology.org/flex/BasicFlexoViz.swf"
            bgcolor="#ffffff"
            width="100%"
            height="100%"
            name="FlexoViz"
            align="middle"
            play="true"
            loop="false"
            quality="high"
            allowScriptAccess="always"
            type="application/x-shockwave-flash"
            flashVars="widget=true&ontology=<%=id%>&nodeid=<%=code%>&server=http://rest.bioontology.org/bioportal"
            pluginspage="http://www.adobe.com/go/getflashplayer">

        </embed>
      </object>

    </f:view>
    <script type="text/javascript">_satellite.pageBottom();</script>
  </body>
</html>
