<%@ page import="java.util.*" %>
<%@ page import="gov.nih.nci.evs.browser.formatter.*" %>
<%@ page import="gov.nih.nci.evs.browser.properties.*" %>

<%!
private static final String POPUP_ARGS =
"'_blank', 'top=100, left=100, height=740, width=680, status=no," +
" menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no," +
" directories=no'";
%>

<%
String page2 = request.getContextPath() + "/pages/display_standard_report.jsf";
String ftpUrl = NCItBrowserProperties.getStandardFtpReportUrl();
Vector<StandardFtpReportInfo> list =
NCItBrowserProperties.getStandardFtpReportInfoList();
%>
The following reports are found in the Terminology Subset Reports
<a href="#" onclick="javascript:window.open('<%=ftpUrl%>', <%=POPUP_ARGS%>);">download</a>page:
<ul>
  <%
  Iterator<StandardFtpReportInfo> iterator = list.iterator();
  // Send redirect:
  if (iterator == null) {
    try {
      String error_msg = "WARNING: The server encountered an unexpected error (file: _list_standard_reports.jsp, code: 1, var: iterator).";
      request.getSession().setAttribute("error_msg", error_msg);
      String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
      response.sendRedirect(redirectURL);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  while (iterator.hasNext()) {
    StandardFtpReportInfo info = iterator.next();
    %>
    <li>
    <a href="#" onclick="javascript:window.open('<%=page2%>?report=<%=info.getName()%>', <%=POPUP_ARGS%>);">
      <%= info.getName() %>
    </a>
  </li><% } %></ul>
