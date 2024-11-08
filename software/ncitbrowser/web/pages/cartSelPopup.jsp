<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
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
  </head>
  <body>
  <header class="flex-grow-0">
	<div style='text-align: left'>
	<include-html src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html"></include-html>
	</div>
  </header>
    <f:view>
      <div class="pagecontent">
        <h:form id="popupForm" acceptcharset="UTF-8">
          <table border="0" role='presentation'>
            <tr>
              <td align="left">
                <b>Select export versions</b>
              </td>
            </tr>
            <tr>
              <td align="left">
                <h:selectManyListbox value="#{CartActionBean.selectedVersionItems}" size="10" style="width:250px;">
                  <f:selectItems value="#{CartActionBean.selectVersionItems}" />
                </h:selectManyListbox>
              </td>
            </tr>
            <tr>
              <td align="left">
                <h:outputLink value="javascript:checkIfDone();">
                  <h:graphicImage
                      value="../images/exportxml.gif"
                      alt="Export XML"
                      title="Export XML"
                      style="border: none"
                  />
                </h:outputLink>
                &#xA0;
                <h:outputLink value="javascript:window.close();">
                  <h:graphicImage
                      value="../images/close.gif"
                      alt="Close window"
                      title="Close window"
                      style="border: none"
                  />
                </h:outputLink>
                <h:commandButton
                    style="display: none; visibility: hidden;"
                    id="startExport"
                    action="#{CartActionBean.exportCartXML}">

                </h:commandButton>
              </td>
            </tr>
          </table>
        </h:form>
      </div>
      <script language="javascript" type="text/javascript">
        function checkIfDone() {
          document.getElementById('popupForm:startExport').click();
          alert('Export started. Download page should appear shortly.');
          setTimeout ('window.close()',1200);
        }
      </script>
    </f:view>
    <script type="text/javascript">_satellite.pageBottom();</script>
  </body>
</html>
