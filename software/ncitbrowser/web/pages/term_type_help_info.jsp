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
<!-- Google tag (gtag.js) -->
<script async src="https://www.googletagmanager.com/gtag/js?id=G-21QRTJ0WQS"></script>
<script>
	window.dataLayer = window.dataLayer || [];
	function gtag(){dataLayer.push(arguments);}
	gtag('js', new Date());
	gtag('config', 'G-21QRTJ0WQS');
</script>
    <script src="https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js"></script>
    <title>Term Type Help Informaton</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  </head>
  <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
  <header class="flex-grow-0">
	<div style='text-align: left'>
	<include-html src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html"></include-html>
	</div>
  </header>
    <f:view>
      <!-- Begin Skip Top Navigation -->
      <a href="#evs-content" class="skip-main" accesskey="1" title="Skip repetitive navigation links">
        skip navigation links</A>
      <!-- End Skip Top Navigation -->

      <div id="popupContainer">
        <!-- nci popup banner -->
        <div class="ncipopupbanner">
          <a href="https://www.cancer.gov" target="_blank" rel="noopener" alt="National Cancer Institute">
            <img
                src="<%=basePath%>/images/banner-red.png"
                width="680"
                height="39"
                border="0"
                alt="National Cancer Institute"
            />
          </a>
          <a href="https://www.cancer.gov" target="_blank" rel="noopener" alt="National Cancer Institute">
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
                <a href="https://evs.nci.nih.gov/" target="_blank" rel="noopener" alt="Enterprise Vocabulary Services">
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

          if (DataUtils.isNCIT(schema)) {
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
            codingScheme, "term_type_header");
            String footer = DataUtils.getMetadataValue(
            codingScheme, "term_type_footer");
            %>

            <!-- Term Type content -->
            <table width="580px" cellpadding="3" cellspacing="0" border="0" role='presentation'>
              <tr class="pageTitle">
                <td align="left">
                  <b>Term Types</b>
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

            <% if (header != null) { %><%= header %>
              <br />
            <% } %>
            <br />
            <table width="580px" cellpadding="3" cellspacing="0" border="0" role='presentation'>
              <tr class="dataRowDark">
                <th scope="col" align="left">Name</th>
                <th scope="col" align="left">Description</th>
              </tr>
              <%
              Vector names = DataUtils.getMetadataValues(
              codingScheme, "term_type_code");
              Vector descriptions = DataUtils.getMetadataValues(
              codingScheme, "term_type_meaning");
              if (names != null && descriptions != null) {
                for (int n=0; n<names.size(); n++) {
                  String name = (String) names.elementAt(n);
                  String description = (String) descriptions.elementAt(n);
                  String rowColor = (n%2 == 1) ? "dataRowDark" : "dataRowLight";
                  %>
                  <tr class="<%=rowColor%>">
                    <td scope="row"><%= name %></td>
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
