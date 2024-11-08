<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.List" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.common.*" %>
<%@ page import="org.LexGrid.LexBIG.LexBIGService.LexBIGService" %>
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
    <title>Sources</title>
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
          <div>
            <img
                src="<%=basePath%>/images/termbrowser_popup_banner.gif"
                width="612"
                height="56"
                alt="NCI Thesaurus"
                title=""
                border="0"
            />
          </div>
          <div id="popupContentArea">
            <a name="evs-content" id="evs-content" tabindex="0"></a>
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
            <table width="580px" cellpadding="3" cellspacing="0" border="0" role='presentation'>
              <tr class="textbody">
                <td align="left">

                  <p>
                    The NCI Term Browser provides access to the terminology sources listed below, some of which are
                    proprietary and included, by permission, for non-commercial use only.
                  </p>
                  <ul>
                    <li>
                    <b>ICD-10</b>
                    &
                    <b>ICD-O-3</b>:
                    The World Health Organization allows use of ICD-10 and ICD-O-3 in NCI Enterprise Vocabulary
                    Services, but requires licensing for other purposes (see
                    <a href="http://www.who.int/classifications/icd/en/" target="_blank" rel="noopener" alt="ICD10 License">
                      http://www.who.int/classifications/icd/en/</a>).
                  </li>
                  <li>
                  <b>MedDRA</b>:
                  The Medical Dictionary for Regulatory Activities (MedDRA) terminology is licensed for NCI work and may
                  be viewed on NCI browsers. All other uses are prohibited, unless covered by separate subscription to
                  MedDRA from the MedDRA MSSO (see
                  <a href="http://www.meddramsso.com" target="_blank" rel="noopener" alt="MedDRA License">http://www.meddramsso.com</a>)
                  or contact at http://mssohelp@ngc.com , 877.258.8280, or 12011 Sunset Hills Road Reston Virginia,
                  20190-3285.
                </li>
                <li>
                <b>NCIm</b>
                is built using the National Library of Medicine's UMLS Metathesaurus, use of which is restricted under
                the UMLS license agreement (see
                <a
                    href="https://www.nlm.nih.gov/research/umls/license.html"
                    target="_blank" rel="noopener"
                    alt="UMLS Metathesaurus License">

                  https://www.nlm.nih.gov/research/umls/license.html</a>).
              </li>
              <li>
              <b>SNOMED CT</b>:
              The International Health Terminology Standards Development Organisation (IHTSDO) allows use of SNOMED CT
              in NCI Enterprise Vocabulary Services, but requires licensing for other purposes. Terms of use for SNOMED
              CT are contained in Appendix 2,
              <a
                  href="https://www.nlm.nih.gov/research/umls/knowledge_sources/metathesaurus/release/license_agreement_snomed.html"
                  target="_blank" rel="noopener"
                  alt="SNOMED CT License">

                SNOMED CT Affiliate Licence Agreement</a>,
              of the License for Use of the UMLS Metathesaurus.</li>
            <li>
            <b>UMLS Semantic Network</b>:
            The National Library of Medicine makes the UMLS Semantic Network freely available without charge on request,
            subject to the conditions specified at
            <a
                href="https://semanticnetwork.nlm.nih.gov/TermsAndConditions/"
                target="_blank" rel="noopener"
                alt="UMLS Metathesaurus Terms and Conditions">

              https://semanticnetwork.nlm.nih.gov/TermsAndConditions/</a>.
          </li></ul>

      </td></tr></table>
  <br />
  <table width="580px" cellpadding="3" cellspacing="0" border="0" role='presentation'>
    <tr class="dataRowDark">
      <th scope="col" align="left">Source</th>
      <th scope="col" align="left">&nbsp;</th>
      <th scope="col" align="left">Description</th>
    </tr>
    <%
    String propertyName = "html_compatable_description";
    Vector from_vec = new Vector();
    Vector to_vec = new Vector();
    from_vec.add("ncim_url"); to_vec.add(DataUtils.getNCImURL());

    Vector abbr_vec = (Vector) request.getSession().getAttribute("source_descriptions");
    if (abbr_vec == null) {
      //LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
      //ist ontology_list = DataUtils.getOntologyList();
      abbr_vec = DataUtils.getSupportedVocabularyMetadataValues(propertyName);
      request.getSession().setAttribute("source_descriptions", abbr_vec);
    }

    int lcv = 0;
    for (int n=0; n<abbr_vec.size(); n++) {
      String t = (String) abbr_vec.elementAt(n);
      Vector w = StringUtils.parseData(t, "|");
      int size = w.size();

      String abbr = (String) w.elementAt(size-2);
      // Send redirect:
      if (abbr == null) {
        try {
          String error_msg = "WARNING: The server encountered an unexpected error (file: source_help_info-termbrowser.jsp, code: 1, var: abbr).";
          request.getSession().setAttribute("error_msg", error_msg);
          String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
          response.sendRedirect(redirectURL);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
      if (!(abbr.startsWith("Terminology Value Set") && abbr.startsWith("Terminology_Value_Set"))) {
        lcv++;
        String def = (String) w.elementAt(size-1);
        def = JSPUtils.replaceContextPath(def, basePath);
        def = JSPUtils.replaceInnerEvalExpressions(def, from_vec, to_vec);

        String rowColor = (lcv%2 == 0) ? "dataRowDark" : "dataRowLight";
        %>
        <tr class="<%=rowColor%>">
          <td scope="row"><%= abbr %></td>
          <td>&nbsp;</td>
          <td><%= def %></td>
        </tr>
        <%
      }
    }
    %>
  </table>
  <br />

</div>
<!-- End of Term Type content -->
</div></div></f:view>
<script type="text/javascript">_satellite.pageBottom();</script>
</body></html>
