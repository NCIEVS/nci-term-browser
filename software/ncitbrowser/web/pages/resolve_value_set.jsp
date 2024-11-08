<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.properties.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference" %>
<%@ page import="org.apache.logging.log4j.*" %>

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
    <title>NCI Term Browser - Value Set</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
  </head>
  <body>
  <header class="flex-grow-0">
	<div style='text-align: left'>
	<include-html src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html"></include-html>
	</div>
  </header>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/wz_tooltip.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/tip_centerwindow.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/tip_followscroll.js"></script>

    <%! private static Logger _logger = LogManager.getLogger("resolve_value_set.jsp"); %>
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
          <%@ include file="/pages/templates/content-header-resolvedvalueset.jsp" %>

          <%
          String x = HTTPUtils.getRefererParmEncode(request);
          String valueSetSearch_requestContextPath = request.getContextPath();
          String message = (String) request.getSession().getAttribute("message");
          request.getSession().removeAttribute("message");

          String vsd_uri = (String) request.getSession().getAttribute("vsd_uri");
          if (vsd_uri == null) {
            vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
          }

          request.getSession().setAttribute("vsd_uri", vsd_uri);

          Vector coding_scheme_ref_vec = DataUtils.getCodingSchemeReferencesInValueSetDefinition(vsd_uri);

          String checked = "";

          String prev_cs_urn = "";
          %>
          <div class="pagecontent">
            <a name="evs-content" id="evs-content" tabindex="0"></a>
            <%-- 0 <%@ include file="/pages/templates/navigationTabs.jsp"%> --%>
            <div class="tabTableContentContainer">

              <table role='presentation'>
                <tr>
                  <td class="texttitle-blue">Resolve Value Set:&nbsp;<%= vsd_uri %></td>
                </tr>

                <%
                if (message != null)  {
                  request.getSession().removeAttribute("message");
                  %>

                  <tr class="textbodyred">
                    <td>
                      <p class="textbodyred">&nbsp;<%= message %></p>
                    </td>
                  </tr>
                <% } %>

                <tr class="textbody">
                  <td>

                    <h:form id="resolveValueSetForm" styleClass="search-form" acceptcharset="UTF-8">

                      <table class="dataTable_960" summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
                        <th class="dataTableHeader" scope="col" align="left">&nbsp;</th>
                        <th class="dataTableHeader" scope="col" align="left">Coding Scheme</th>
                        <th class="dataTableHeader" scope="col" align="left">Version</th>
                        <th class="dataTableHeader" scope="col" align="left">Tag</th>
                        <%
                        if (coding_scheme_ref_vec != null) {
                          int k = -1;

                          for (int i=0; i<coding_scheme_ref_vec.size(); i++) {

                            String coding_scheme_ref_str = (String) coding_scheme_ref_vec.elementAt(i);
                            int lcv = i+1;

                            String coding_scheme_name_version = coding_scheme_ref_str;

                            Vector u = StringUtils.parseData(coding_scheme_ref_str);
                            String cs_name = (String) u.elementAt(0);
                            String displayed_cs_name = DataUtils.uri2CodingSchemeName(cs_name);

                            //cs_name = DataUtils.uri2CodingSchemeName(cs_name);

                            String cs_version = (String) u.elementAt(1);
                            String label = cs_name + "(" + cs_version + ")";

                            String cs_tag = DataUtils.getVocabularyVersionTag(cs_name, cs_version);
                            if (cs_tag == null) cs_tag = "";

                            if (cs_name.compareTo(prev_cs_urn) != 0) {
                              k++;
                              prev_cs_urn = cs_name;
                            }

                            if (coding_scheme_ref_vec.size() == 1) {
                              checked = "checked";
                            } else if (cs_tag.compareToIgnoreCase("PRODUCTION") == 0) {
                              checked = "checked";
                            }

                            if (k % 2 == 0) {
                              %>
                              <tr class="dataRowDark">
                              <% } else { %>
                                <tr class="dataRowLight">
                                <% } %>
                                <td scope="row">
                                  <input aria-label="<%=label%>" 
                                      type="radio"
                                      id="<%=label%>"
                                      name="<%=cs_name%>"
                                      value="<%=cs_version%>"
                                      <%=checked%>
                                      tabinex="1"
                                  />
                                  <label for="<%=cs_name%>"><%=cs_name%></label>
                                </td>
                                <td class="dataCellText"><%= displayed_cs_name %></td>
                                <td class="dataCellText"><%= cs_version %></td>
                                <td class="dataCellText"><%= cs_tag %></td>
                              </tr>

                              <%
                            }
                          } else {
                            %>
                            <tr>
                              <td>
                                <p class="textbodyred">
                                  &nbsp;WARNING: Unable to retrieve coding scheme reference data from the server.
                                </p>
                              </td>
                            </tr>
                          <% } %>
                      </table>
                      <tr>
                        <td>
                          <h:commandButton
                              id="continue_resolve"
                              value="continue_resolve"
                              action="#{valueSetBean.continueResolveValueSetAction}"
                              onclick="javascript:cursor_wait();"
                              image="#{valueSetSearch_requestContextPath}/images/continue.gif"
                              alt="Resolve"
                              tabindex="0">

                          </h:commandButton>
                        </td>
                      </tr>

                      <input type="hidden" name="vsd_uri" id="vsd_uri" value="<%=vsd_uri%>">
                      <input type="hidden" name="version_selection" id="version_selection" value="true">
                      <input
                          type="hidden"
                          name="referer"
                          id="referer"
                          value="<%=x%>">

                    </h:form>

                  </td>
                </tr>
              </table>
            </div>
            <!-- end tabTableContentContainer -->
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
    <script type="text/javascript">_satellite.pageBottom();</script>
  </body>
</html>
