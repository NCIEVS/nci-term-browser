<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Vector" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.common.*" %>

<% String basePath = request.getContextPath(); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html lang="en" xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
  <head>
    <script
        src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js">
    </script>
    <title>Sources</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
<script>
(function(i, s, o, g, r, a, m) {
i['GoogleAnalyticsObject'] = r;
i[r] = i[r] || function() {
(i[r].q = i[r].q || []).push(arguments)
}, i[r].l = 1 * new Date();
a = s.createElement(o),
m = s.getElementsByTagName(o)[0];
a.async = 1;
a.src = g;
m.parentNode.insertBefore(a, m)
})(window, document, 'script', 'https://www.google-analytics.com/analytics.js', 'ga');
ga('create, 'UA-150112876-1', 'auto');
ga('send', 'pageview');
ga('set', 'anonymizeIP', true);
</script>
  </head>
  <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
    <f:view>
      <!-- Begin Skip Top Navigation -->
      <a href="#evs-content" class="skip-main" accesskey="1" title="Skip repetitive navigation links">
        skip navigation links</A>
      <!-- End Skip Top Navigation -->

      <div id="popupContainer">
        <!-- nci popup banner -->
        <div class="ncipopupbanner">
          <a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute">
            <img
                src="<%=basePath%>/images/banner-red.png"
                width="680"
                height="39"
                border="0"
                alt="National Cancer Institute"
            />
          </a>
          <a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute">
            <img
                src="<%=basePath%>/images/spacer.gif"
                width="60"
                height="39"
                border="0"
                alt="National Cancer Institute"
                class="print-header"
            />
          </a>
        </div>
        <!-- end nci popup banner -->
        <div id="popupMainArea">
          <table class="evsLogoBg" cellspacing="3" cellpadding="0" border="0" width="570px" role='presentation'>
            <tr>
              <td valign="top">
                <a href="http://evs.nci.nih.gov/" target="_blank" alt="Enterprise Vocabulary Services">
                  <img
                      src="<%=basePath%>/images/evs-popup-logo.gif"
                      width="213"
                      height="26"
                      alt="EVS: Enterprise Vocabulary Services"
                      title="EVS: Enterprise Vocabulary Services"
                      border="0"
                  />
                </a>
              </td>
              <td valign="top">
                <div id="closeWindow">
                  <a href="javascript:window.close();">
                    <img
                        src="<%=basePath%>/images/thesaurus_close_icon.gif"
                        width="10"
                        height="10"
                        border="0"
                        alt="Close Window"
                    />
                    &nbsp;CLOSE WINDOW</a>
                </div>
              </td>
            </tr>
          </table>

          <%
          String dictionary = HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
          String schema = HTTPUtils.cleanXSS((String) request.getParameter("schema"));
          if (dictionary != null && schema == null)
          schema = dictionary;

          String display_name = DataUtils.getMetadataValue(schema, "display_name");
          if (display_name == null || display_name.compareTo("null") == 0) {
            display_name = DataUtils.getLocalName(schema);
          }

          if (schema.compareTo("NCI Thesaurus") == 0) {
            %>
            <div>
              <img
                  src="<%=basePath%>/images/thesaurus_popup_banner.gif"
                  width="612"
                  height="56"
                  alt="NCI Thesaurus"
                  title=""
                  border="0"
              />
            </div>
          <% } else { %>
            <div>
              <img
                  src="<%=basePath%>/images/other_popup_banner.gif"
                  width="612"
                  height="56"
                  alt="NCI Thesaurus"
                  title=""
                  border="0"
              />
              <div class="vocabularynamepopupshort"><%= DataUtils.encodeTerm(display_name) %></div>
            </div>
          <% } %>

          <div id="popupContentArea">
            <a name="evs-content" id="evs-content" tabindex="0"></a>
            <%
            String codingScheme = dictionary;
            String header = DataUtils.getMetadataValue(
            codingScheme, "source_header");
            String footer = DataUtils.getMetadataValue(
            codingScheme, "source_footer");
            %>

            <!-- Term Type content -->
            <table width="580px" cellpadding="3" cellspacing="0" border="0" role='presentation'>
              <tr class="pageTitle">
                <td align="left">
                  <b>Sources</b>
                </td>
                <td align="right">
                  <font size="1" color="red" align="right">
                    <a href="javascript:printPage()">
                      <img src="<%= request.getContextPath() %>/images/printer.bmp" border="0" alt="Send to Printer">
                      <i>Send to Printer</i>
                    </a>
                  </font>
                </td>
              </tr>
            </table>
            <hr />

            <% if (header != null) { %><%= header %>            <% } %>
            <br />
            <table width="580px" cellpadding="3" cellspacing="0" border="0" role='presentation'>
              <tr class="dataRowDark">
                <th scope="col" align="left">Source</th>
                <th scope="col" align="left">Description</th>
              </tr>
              <%
              Vector names = DataUtils.getMetadataValues(
              codingScheme, "source_name");
              Vector descriptions = DataUtils.getMetadataValues(
              codingScheme, "source_description");
              if (names != null && descriptions != null) {
                for (int n=0; n<names.size(); n++) {
                  String name = (String) names.elementAt(n);
                  String description = (String) descriptions.elementAt(n);
                  String rowColor = (n%2 == 1) ? "dataRowDark" : "dataRowLight";
                  %>
                  <tr class="<%=rowColor%>">
                    <td scope="row"><%= DataUtils.encodeTerm(name) %></td>
                    <td><%= description %></td>
                  </tr>
                  <%
                }
              }
              %>
            </table>
            <br />
            <% if (footer != null) { %><%= footer %>            <% } %>

          </div>
          <!-- End of Term Type content -->
        </div>
      </div>
    </f:view>
    <script type="text/javascript">_satellite.pageBottom();</script>
  </body>
</html>
