<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page import="java.util.Vector" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.HTTPUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.HistoryUtils" %>

<%@ page import="gov.nih.nci.evs.browser.utils.RemoteServerUtil" %>
<%@ page import="org.LexGrid.LexBIG.LexBIGService.LexBIGService" %>

<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page contentType="text/html;charset=UTF-8" %>
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
    <title>NCI Thesaurus History</title>
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
    <%
    LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
    HistoryUtils historyUtils = new HistoryUtils(lbSvc);

    String code = HTTPUtils.cleanXSS((String) request.getParameter("code"));
    String dictionary = HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
    String vers = null;
    String ltag = null;
    Entity concept = (Entity) request.getSession().getAttribute("concept");
    if (concept == null) {
      concept = DataUtils.getConceptByCode(dictionary, vers, ltag, code);
    } else {
      request.getSession().setAttribute("concept", concept);
    }
    String msg = null;
    if (concept == null) {
      msg = "ERROR: Invalid code - " + code + ".";
    } else {
      msg = "ERROR: Unable to generate the requested page.";
    }
    %>
    <f:view>
      <!-- Begin Skip Top Navigation -->
      <a href="#evs-content" class="skip-main" accesskey="1" title="Skip repetitive navigation links">
        skip navigation links</A>
      <!-- End Skip Top Navigation -->
      <% if (concept == null) { %>
        <div class="textbody"><%= HTTPUtils.cleanXSS(msg) %></div>
        <%
      } else {
        Vector rows = historyUtils.getEditActions(dictionary, vers, ltag, code);
        String concept_name = "";
        if (concept.getEntityDescription() != null) {
          concept_name = concept.getEntityDescription().getContent();
        }
        Vector headers = historyUtils.getTableHeader();
        %>

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
            <a name="evs-content" id="evs-content" tabindex="0"></a>
            <table class="evsLogoBg" cellspacing="0" cellpadding="0" border="0" role='presentation'>
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
            <div id="popupContentArea">
              <!-- History content -->
              <div class="pageTitle">
                <b><%= HTTPUtils.cleanXSS(concept_name) %> (Code <%= HTTPUtils.cleanXSS(code) %>)</b>
              </div>
              <table width="580px" cellpadding="3" cellspacing="0" border="0" role='presentation'>
                <tr class="textbody">
                  <td align="left" class="texttitle-gray">History</td>
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
              <table width="580px" cellpadding="3" cellspacing="0" border="0" role='presentation'>
                <tr>
                  
                  <% for (int i=0; i<headers.size(); ++i) { %>
	               <!-- <div style='text-align: left'> -->
                   <% Object header = headers.elementAt(i); %> 
                    <th class="dataTableHeader" scope="col" align="left"><%= header %></th>
                    <!-- </div> -->
                  <% } %>
                </tr>
                <%
                for (int i=0; i<rows.size(); ++i) {
                  String rowColor = (i%2 == 0) ? "dataRowDark" : "dataRowLight";
                  %>
                  <tr class="<%=rowColor%>">
                    <%
String row = (String) rows.elementAt(i);
                Vector cols = StringUtils.parseData(row, "|");
                // Send redirect:
                if (cols == null) {
                	try {
                		String error_msg = "WARNING: The server encountered an unexpected error (file: concept_history.jsp, code: 1, var: cols).";
                		request.getSession().setAttribute("error_msg", error_msg);
                		String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
                		response.sendRedirect(redirectURL);				 
                	} catch (Exception ex) {
                		ex.printStackTrace();
                	}
                }
                for (int j=0; j<cols.size(); ++j) {
                  Object cell = cols.elementAt(j);
                  String iFormat = "", iFormatEnd = "";
                  String rowScope = j==0 ? " scope=\"row\"" : "";
                  if (j==0 || j==2)
                    { iFormat = "<i>"; iFormatEnd = "</i>"; }
                  %>
                    <td class="dataCellText"<%=rowScope%>><%=iFormat%><%=cell%><%=iFormatEnd%></td>
                  <%
                }
            %>
                </tr>
            <%
              }
            %>
            <tr><td height="10px"></td></tr>
          </table>
          <!-- End of history content -->
        </div>
      </div>
    </div>
    <%
    }
    %>
  </f:view>
<script type="text/javascript">_satellite.pageBottom();</script>
  </body>
</html>
