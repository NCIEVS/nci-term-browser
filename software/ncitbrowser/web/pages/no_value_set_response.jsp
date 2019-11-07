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
<%@ page import="org.apache.log4j.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html lang="en" xmlns:c="http://java.sun.com/jsp/jstl/core">
  <head>
    <script
        src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js">
    </script>
    <title>NCI Term Browser</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="/ncitbrowser/css/styleSheet.css" />
    <link rel="shortcut icon" href="/ncitbrowser/favicon.ico" type="image/x-icon" />
    <script type="text/javascript" src="/ncitbrowser/js/script.js"></script>
    <script type="text/javascript" src="/ncitbrowser/js/search.js"></script>
    <script type="text/javascript" src="/ncitbrowser/js/dropdown.js"></script>
<script>
(function(i, s, o, g, r, a, m){
  i['GoogleAnalyticsObject'] = r;
  i[r] = i[r] || function(){
    (i[r].q = i[r].q || []).push(arguments)
  },
  i[r].l =1 * new Date();
  a = s.createElement(o),
  m = s.getElementsByTagName(o)[0];
  a.async = 1;
  a.src = g;
  m.parentNode.insertBefore(a, m)
})(window, document, 'script', 'https://www.google-analytics.com/analytics.js', 'ga');
   ga('create, 'UA-150112876-1', 'auto');
   ga('send', 'pageview');
</script>
  </head>
  <body onLoad="document.forms.searchTerm.matchText.focus();">
    <script type="text/javascript" src="/ncitbrowser/js/wz_tooltip.js"></script>
    <script type="text/javascript" src="/ncitbrowser/js/tip_centerwindow.js"></script>
    <script type="text/javascript" src="/ncitbrowser/js/tip_followscroll.js"></script>

    <a href="#evs-content" class="skip-main" accesskey="1" title="Skip repetitive navigation links">
      skip navigation links</A>

    <div style='clear:both;margin-top:-5px;padding:8px;height:32px;color:white;background-color:#C31F40'>
      <a href="http://www.cancer.gov" target="_blank">
        <img
            src="<%=request.getContextPath()%>/images/banner-red.png"
            width="955"
            height="39"
            border="0"
            alt="National Cancer Institute"
        />
      </a>

      <!--
        iv class="ncibanner"> <a href="http://www.cancer.gov" target="_blank"> <img
        src="/ncitbrowser/images/logotype.gif" width="556" height="39" border="0" alt="National Cancer Institute"/> </a>
        <a href="http://www.cancer.gov" target="_blank"> <img src="/ncitbrowser/images/spacer.gif" width="60"
        height="39" border="0" alt="National Cancer Institute" class="print-header"/> </a> <a href="http://www.nih.gov"
        target="_blank"> <img src="/ncitbrowser/images/tagline_nologo.gif" width="219" height="39" border="0" alt="U.S.
        National Institutes of Health"/> </a> <a href="http://www.cancer.gov" target="_blank"> <img
        src="/ncitbrowser/images/cancer-gov.gif" width="125" height="39" border="0" alt="www.cancer.gov"/> </a>
      -->

    </div>

    <div class="center-page_960">

      <div>
        <img
            src="/ncitbrowser/images/evs-logo-swapped.gif"
            alt="EVS Logo"
            width="945"
            height="26"
            border="0"
            usemap="#external-evs"
        />
        <map id="external-evs" name="external-evs">
          <area shape="rect" coords="0,0,140,26" href="/ncitbrowser/start.jsf" target="_self" alt="NCI Term Browser" />
          <area
              shape="rect"
              coords="520,0,941,26"
              href="http://evs.nci.nih.gov/"
              target="_blank"
              alt="Enterprise Vocabulary Services"
          />
        </map>
      </div>

      <table cellspacing="0" cellpadding="0" role='presentation'>
        <tr>

          <td width="5"></td>
          <td>
            <a href="/ncitbrowser/pages/multiple_search.jsf?nav_type=terminologies">
              <img
                  name="tab_terms"
                  src="/ncitbrowser/images/tab_terms_clicked.gif"
                  border="0"
                  alt="Terminologies"
                  title="Terminologies"
              />
            </a>
          </td>
          <td>
            <a href="/ncitbrowser/ajax?action=create_src_vs_tree&nav_type=valuesets">
              <img
                  name="tab_valuesets"
                  src="/ncitbrowser/images/tab_valuesets.gif"
                  border="0"
                  alt="Value Sets"
                  title="ValueSets"
              />
            </a>
          </td>
          <td>
            <a href="/ncitbrowser/pages/mapping_search.jsf?nav_type=mappings&b=0&m=0">
              <img name="tab_map" src="/ncitbrowser/images/tab_map.gif" border="0" alt="Mappings" title="Mappings" />
            </a>
          </td>
        </tr>
      </table>

      <div class="mainbox-top">
        <img src="/ncitbrowser/images/mainbox-top.gif" width="945" height="5" alt="" />
      </div>

      <div id="main-area_960">
        <div class="bannerarea_960">
          <a href="/ncitbrowser/start.jsf" style="text-decoration: none;">
            <div class="vocabularynamebanner_tb">
              <span class="vocabularynamelong_tb"><%= JSPUtils.getApplicationVersionDisplay() %></span>
            </div>
          </a>
          <div class="search-globalnav_960">
            <img src="/ncitbrowser/images/shim.gif" width="1" height="80" alt="Shim" border="0" />

            <table
                class="global-nav"
                border="0"
                width="100%"
                height="33px"
                cellpadding="0"
                cellspacing="0"
                role='presentation'>

              <tr>
                <td align="left" valign="bottom">
                  <a
                      href="#"
                      onclick="javascript:window.open('/ncitbrowser/pages/source_help_info-termbrowser.jsf', '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');"
                      tabindex="0">

                    Sources</a>

                </td>
                <td align="right" valign="bottom">
                  <a href="/ncitbrowser/pages/help.jsf" tabindex="0">Help</a>
                </td>
                <td width="7"></td>
              </tr>
            </table>

          </div>
        </div>

        <div class="bluebar">
          <table border="0" cellspacing="0" cellpadding="0" role='presentation'>
            <tr>
              <td>
                <div class="quicklink-status">
                  &nbsp;
                </div>
              </td>
              <td>

                <div id="quicklinksholder">
                  <ul
                      id="quicklinks"
                      onmouseover="document.quicklinksimg.src='/ncitbrowser/images/quicklinks-active.gif';"
                      onmouseout="document.quicklinksimg.src='/ncitbrowser/images/quicklinks-inactive.gif';">

                    <li>
                    <a href="#" tabindex="0">
                      <img
                          src="/ncitbrowser/images/quicklinks-inactive.gif"
                          width="162"
                          height="18"
                          border="0"
                          name="quicklinksimg"
                          alt="Quick Links"
                      />
                    </a>
                    <ul>
                      <li>
                      <a
                          href="http://evs.nci.nih.gov/"
                          tabindex="0"
                          target="_blank"
                          alt="Enterprise Vocabulary Services">

                        EVS Home</a>
                    </li>
                    <li>
                    <a href="http://ncim.nci.nih.gov/ncimbrowser" tabindex="0" target="_blank" alt="NCI Metathesaurus">
                      NCI Metathesaurus Browser</a>
                  </li>

                  <li>
                  <a href="/ncitbrowser/index.jsp" tabindex="0" alt="NCI Thesaurus Browser">NCI Thesaurus Browser</a>
                </li>

                <li>
                <a href="/ncitbrowser/termbrowser.jsf" tabindex="0" alt="NCI Term Browser">NCI Term Browser</a>
              </li>

              <li>
              <a
                  href="http://www.cancer.gov/cancertopics/terminologyresources"
                  tabindex="0"
                  target="_blank"
                  alt="NCI Terminology Resources">

                NCI Terminology Resources</a>
            </li>

          </ul></li></ul>
    </div>

  </td></tr></table></div>

<%
String error_msg = (String) request.getSession().getAttribute("no_value_set_loaded_msg");
request.getSession().removeAttribute("error_msg");
%>
<p class="textbodyred">&nbsp;<%= error_msg %></p>

<div class="footer" style="width:940px">
  <ul class="textbody">
    <li>
    <a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute">NCI Home</a>
    |</li>
  <li>
  <a href="/ncitbrowser/pages/contact_us.jsf?nav_type=terminologies">Contact Us</a>
  |</li>
<li>
<a href="http://www.cancer.gov/policies" target="_blank" alt="National Cancer Institute Policies">Policies</a>
|</li>
<li>
<a href="http://www.cancer.gov/policies/page3" target="_blank" alt="National Cancer Institute Accessibility">
  Accessibility</a>
|</li>
<li>
<a href="http://www.cancer.gov/policies/page6" target="_blank" alt="National Cancer Institute FOIA">FOIA</a>
</li></ul>
<p class="textbody">
  A Service of the National Cancer Institute
  <br />
  <img
      src="/ncitbrowser/images/external-footer-logos.gif"
      alt="External Footer Logos"
      width="238"
      height="34"
      border="0"
      usemap="#external-footer"
  />
</p>
<map id="external-footer" name="external-footer">
  <area shape="rect" coords="0,0,46,34" href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute" />
  <area
      shape="rect"
      coords="55,1,99,32"
      href="http://www.hhs.gov/"
      target="_blank"
      alt="U.S. Health &amp; Human Services"
  />
  <area
      shape="rect"
      coords="103,1,147,31"
      href="http://www.nih.gov/"
      target="_blank"
      alt="National Institutes of Health"
  />
  <area shape="rect" coords="148,1,235,33" href="http://www.usa.gov/" target="_blank" alt="USA.gov" />
</map>
</div>

</div></div>
<div class="mainbox-bottom">
  <img src="/ncitbrowser/images/mainbox-bottom.gif" width="945" height="5" alt="Mainbox Bottom" />
</div>

</div>
<script type="text/javascript">_satellite.pageBottom();</script>
</body></html>
