<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>

<%
String basePath = request.getContextPath();
String ncit_build_info = DataUtils.getNCITBuildInfo();
String application_version = DataUtils.getApplicationVersion();
String anthill_build_tag_built = DataUtils.getNCITAnthillBuildTagBuilt();
String evs_service_url = DataUtils.getEVSServiceURL();
String content_title = HTTPUtils.cleanXSS((String) request.getParameter("content_title"));
String content_page = HTTPUtils.cleanXSS((String) request.getParameter("content_page"));
String display_app_logo = HTTPUtils.cleanXSS((String) request.getParameter("display_app_logo"));
boolean is_display_app_logo = display_app_logo != null
&& display_app_logo.equalsIgnoreCase("true");

JSPUtils.JSPHeaderInfoMore hierarchy_info = new JSPUtils.JSPHeaderInfoMore(request);
String hierarchy_dictionary = hierarchy_info.dictionary;
String hierarchy_version = hierarchy_info.version;
String hierarchy_schema = HTTPUtils.cleanXSS((String) request.getParameter("schema"));
if (hierarchy_dictionary != null && hierarchy_schema == null)
hierarchy_schema = hierarchy_dictionary;
String display_name = hierarchy_info.display_name;
%>
<!--
  uild info: <%=ncit_build_info%> Version info: <%=application_version%> Tag: <%=anthill_build_tag_built%> LexEVS URL:
  <%=evs_service_url%>
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html lang="en" xmlns:c="http://java.sun.com/jsp/jstl/core">
  <head>
    <script
        src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js">
    </script>
    <script src="https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js"></script>
    <title><%= content_title %></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
<script>(function(i,s,o,g,r,a,m){i["GoogleAnalyticsObject"]=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,"script","//www.google-analytics.com/analytics.js","ga");ga("create", "UA-150112876-1", {"cookieDomain":"auto"});ga("send", "pageview");</script>
  </head>
  <body>
  <header class="flex-grow-0">
	<div style='text-align: left'>
	<include-html src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html"></include-html>
	</div>
  </header>

    <!-- Begin Skip Top Navigation -->
    <a href="#evs-content" class="skip-main" accesskey="1" title="Skip repetitive navigation links">
      skip navigation links</A>

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
      <div id="popupMainArea_Elastic">
        <table class="evsLogoBg" cellspacing="0" cellpadding="0" border="0" role='presentation'>
          <tr>
            <td valign="top">
              <a href="https://evs.nci.nih.gov/" target="_blank" rel="noopener">
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
      </div>

      <% if (is_display_app_logo) { %>
        <div>
          <table
              background="<%=basePath% role='presentation'>/images/termbrowser_popup_banner-middle.gif"
              cellspacing="0"
              cellpadding="0"
              border="0">

            <tr>
              <td width="1">
                <% if (hierarchy_schema == null) { %>
                  <img
                      src="<%=basePath%>/images/termbrowser_popup_banner-left.gif"
                      alt="NCI Term Browser Banner"
                      title=""
                      border="0"
                  />
                <% } else if (DataUtils.isNCIT(hierarchy_schema)) { %>
                  <img
                      src="<%=basePath%>/images/thesaurus_popup_banner-left.gif"
                      alt="NCI Thesaurus Banner"
                      title=""
                      border="0"
                  />
                <% } else { %>
                  <img src="<%=basePath%>/images/other_popup_banner-left.gif" alt="Other Banner" title="" border="0" />
                  <div class="vocabularynamepopupshort"><%= HTTPUtils.cleanXSS(display_name) %></div>
                <% } %>
              </td>
              <td width="100%"><%-- intentionally left blank --%></td>
              <td width="1">
                <img
                    src="<%=basePath%>/images/termbrowser_popup_banner-right.gif"
                    alt="NCI Thesaurus"
                    title=""
                    border="0"
                />
              </td>
            </tr>
          </table>
        </div>
      <% } %>

      <a name="evs-content" id="evs-content" tabindex="0"></a>

      <jsp:include page="<%=content_page%>" />
    </div>
    <script type="text/javascript">_satellite.pageBottom();</script>
  </body>
</html>
