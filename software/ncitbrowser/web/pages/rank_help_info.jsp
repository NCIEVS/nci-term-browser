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
                width="600"
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
                  <b>Map Rank</b>
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
                    Certain generated map sets will make use of a map rank to provide additional information regarding
                    the quality of individual maps within the set. It can be thought of as a score, where the a value of
                    1 indicates a highest quality map within the set and higher numbers indicate increasingly lower
                    quality maps. The intention is to provide applications with as much information as possible (e.g.
                    every known connection between two vocabularies) while also providing them the ability to choose a
                    ?quality threshold? below which lower quality maps will not be considered. The threshold must be
                    tuned for each use case.
                  </p>

                  <p>
                    Map rank values themselves are specific to a particular set of mappings and are heavily dependent
                    upon the algorithms used to generate or choose the values. Lower values always indicate better maps
                    (e.g. ?1? is a better map than ?2?) ? but there is no defined proportionality between the values
                    (e.g. ?2? is not twice as bad as ?1?) and some sets will use values that other sets do not. Not all
                    map sets make use of ?map rank?. In cases where map rank is not used, all maps within the set are
                    considered to be of equivalent quality.
                  </p>

                </td>
              </tr>
            </table>
            <br />

          </div>
          <!-- End of Term Type content -->
        </div>
      </div>
    </f:view>
    <script type="text/javascript">_satellite.pageBottom();</script>
  </body>
</html>
