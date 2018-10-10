<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=windows-1252" %>
<%@ page import="java.util.Vector" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.common.*" %>

<%
String basePath = request.getContextPath();
String ncbo_id = HTTPUtils.cleanXSS((String) request.getParameter("ncbo_id"));
String code = HTTPUtils.cleanXSS((String) request.getParameter("code"));
%>

<html lang="en" xmlns:c="http://java.sun.com/jsp/jstl/core"> 
  <head></head>
  <script
      src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js">
  </script>

  <body>

    <script language="JavaScript" type="text/javascript">
    <!--
      // Globals
      // Major version of Flash required
      var requiredMajorVersion = 9;
      // Minor version of Flash required
      var requiredMinorVersion = 0;
      // Minor version of Flash required
      var requiredRevision = 28;
    // -->
    </script>
    <script src="AC_OETags.js" language="javascript" type="text/javascript"></script>

    <script language="javascript" type="text/javascript">
    <!--

      function focusApp() {
        var app = document.getElementById("BasicFlexoViz");
        if (app) {
          app.focus();
        }
      }

      // Version check for the Flash Player that has the ability to start Player Product Install (6.0r65)
      var hasProductInstall = DetectFlashVer(6, 0, 65);

      // Version check based upon the values defined in globals
      var hasRequestedVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);

      if ( hasProductInstall && hasRequestedVersion ) {
        // if we've detected an acceptable version embed the Flash Content SWF when all tests are passed
        AC_FL_RunContent(
        "src", "BasicFlexoViz?v=2.3.4.1",
        "width", "100%",
        "height", "100%",
        "align", "middle",
        "id", "BasicFlexoViz",
        "quality", "high",
        "bgcolor", "#ffffff",
        "name", "BasicFlexoViz",
        "allowScriptAccess","always",
        "type", "application/x-shockwave-flash",
        "flashVars", "server=http://rest.bioontology.org/bioportal&redirecturl=&ontology=<%=ncbo_id%>&virtual=true&nodeid=<%=code%>",
        "pluginspage", "http://www.adobe.com/go/getflashplayer"
        );
      } else {
        // flash is too old or we can't detect the plugin
        var alternateContent = '<br/>This website requires an updated version of Adobe Flash Player.  '
        + 'Please download and install the Flash plug-in from http://www.adobe.com/go/getflash/ and try again.  '
        + '<br/>';
        document.write(alternateContent);
      }
    // -->
    </script>

    <script type="text/javascript">_satellite.pageBottom();</script>
  </body>
</html>

