<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.util.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.properties.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference" %>
<%@ page import="org.LexGrid.LexBIG.LexBIGService.LexBIGService" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html lang="en" xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>
<script src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js"></script>
  <title>NCI Metathesaurus</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="stylesheet" type="text/css" href="/ncitbrowser/css/styleSheet.css" />
  <link rel="shortcut icon" href="/ncitbrowser/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="/ncitbrowser/js/script.js"></script>
  <script type="text/javascript" src="/ncitbrowser/js/search.js"></script>
  <script type="text/javascript" src="/ncitbrowser/js/dropdown.js"></script>


<script>(function(i,s,o,g,r,a,m){i["GoogleAnalyticsObject"]=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,"script","//www.google-analytics.com/analytics.js","ga");ga("create", "UA-150112876-1", {"cookieDomain":"auto"});ga("send", "pageview");</script>
</head>
<body onLoad="javascript:popupMessage();document.forms.searchTerm.matchText.focus();">
<script type="text/javascript" src="/ncitbrowser/js/wz_tooltip.js"></script>
<script type="text/javascript" src="/ncitbrowser/js/tip_centerwindow.js"></script>
<script type="text/javascript" src="/ncitbrowser/js/tip_followscroll.js"></script>

	<script type="text/javascript">
	function submitform()
	{
	  document.cartFormId.submit();
	}
	</script>



<div style='clear:both;margin-top:-5px;padding:8px;height:32px;color:white;background-color:#C31F40'>

  <a href="http://www.cancer.gov" target="_blank">
    <img
        src="/ncitbrowser/images/banner-red.png"
        width="955"
        height="39"
        border="0"
        alt="National Cancer Institute"
    />
  </a>



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
    <area
        shape="rect"
        coords="0,0,140,26"
        href="/ncitbrowser/start.jsf"
        target="_self"
        alt="NCI Term Browser"
    />
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
        <img name="tab_terms" src="/ncitbrowser/images/tab_terms_clicked.gif" border="0" alt="Terminologies" title="Terminologies" />
      </a>
    </td>



      <td>
        <a href="/ncitbrowser/ajax?action=create_src_vs_tree&nav_type=valuesets&mode=0">
          <img name="tab_valuesets" src="/ncitbrowser/images/tab_valuesets.gif" border="0" alt="Value Sets" title="ValueSets" />
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
      <span class="vocabularynamelong_tb">Version 2.16 (using LexEVS 6.5.3)</span>
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




      | <A href="#" onmouseover="Tip('<ul><li><a href=\'/ncitbrowser/ConceptReport.jsp?dictionary=NCI_Thesaurus&version=20.01d&ns=ncit&code=C16394\'>Cell&#32;Aging &#40;NCI Thesaurus 20.01d&#41;</a><br></li></ul>',WIDTH, 300, TITLE, 'Visited Concepts', SHADOW, true, FADEIN, 300, FADEOUT, 300, STICKY, 1, CLOSEBTN, true, CLICKCLOSE, true)" onmouseout=UnTip() >Visited Concepts</A>
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
            <a href="http://evs.nci.nih.gov/" tabindex="0" target="_blank" alt="Enterprise Vocabulary Services">
              EVS Home</a>
          </li>
          <li>
          <a href="https://ncim.nci.nih.gov/ncimbrowser" tabindex="0" target="_blank" alt="NCI Metathesaurus">NCI Metathesaurus Browser</a>
        </li>



          <li>
          <a href="/ncitbrowser/index.jsp" tabindex="0" alt="NCI Thesaurus Browser">
            NCI Thesaurus Browser</a>
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
    <li>
    <a
        href="http://ncitermform.nci.nih.gov/ncitermform/?dictionary=NCI%20Thesaurus"
        tabindex="0"
        target="_blank"
        alt="Term Suggestion">

      Term Suggestion</a>
  </li>

</ul></li></ul></div>

</td></tr></table></div>


            <div class="pagecontent">
              <a name="evs-content" id="evs-content" tabindex="0"></a>

 <form name="cartFormId" method="post" action="/ncitbrowser/ajax?action=cart"><br>



          <table class="datatable_960" summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
          <tr>
          <td align="left"><b><h1>Relationship Search Test Results (using Graph DB API)</h1></b></td>
          </tr>
          </table>

       <hr/>

          <table class="datatable_960" summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
              <tr>
                <th class="dataTableHeader" scope="col" align="left" width="20%">Coding Scheme</th>
                <th class="dataTableHeader" scope="col" align="left" width="30%">Search String</th>
                <th class="dataTableHeader" scope="col" align="left" width="10%">Algorithm</th>
                <th class="dataTableHeader" scope="col" align="left" width="10%">Match Count</th>
                <th class="dataTableHeader" scope="col" align="left" width="15%">Memory Use</th>
                <th class="dataTableHeader" scope="col" align="left" width="15%">Response Time</th>
              </tr>

              <tr class="dataRowDark">
                  <td>NCI Thesaurus</td>
                  <td>Blood</td>
                  <td>contains</td>
                  <td>1520</td>
                  <td>3.23 MB</td>
                  <td>120 milli sec</td>
   		      </tr>

              <tr class="dataRowLight">
                  <td>NCI Thesaurus</td>
                  <td>Cell</td>
                  <td>contains</td>
                  <td>1230</td>
                  <td>4.29 MB</td>
                  <td>325 milli sec</td>
   		      </tr>

          </table>
        </form>

          <br/>


<div class="footer" style="width:920px">
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
<center>
  <a href="http://www.hhs.gov/" alt="U.S. Department of Health and Human Services">
    U.S. Department of Health and Human Services</a>&nbsp;|&nbsp;
  <a href="https://www.nih.gov/about-nih" alt="National Institutes of Health">National Institutes of Health</a>&nbsp;|&nbsp;
  <a href="http://www.cancer.gov/" alt="National Cancer Institute">National Cancer Institute</a>&nbsp;|&nbsp;
  <a href="https://www.usa.gov/" alt="USA.gov">USA.gov</a>
</center>
</div>


        </div>
      </div>

<div class="mainbox-bottom">
  <img src="/ncitbrowser/images/mainbox-bottom.gif" width="945" height="5" alt="Mainbox Bottom" />
</div>


    </div>

<script type="text/javascript">_satellite.pageBottom();</script>
</body>
</html>
