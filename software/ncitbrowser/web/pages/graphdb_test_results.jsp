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
<!-- Google tag (gtag.js) -->
<script async src="https://www.googletagmanager.com/gtag/js?id=G-21QRTJ0WQS"></script>
<script>
	window.dataLayer = window.dataLayer || [];
	function gtag(){dataLayer.push(arguments);}
	gtag('js', new Date());
	gtag('config', 'G-21QRTJ0WQS');
</script>
  <title>NCI Term Browser</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="stylesheet" type="text/css" href="/ncitbrowser/css/styleSheet.css" />
  <link rel="shortcut icon" href="/ncitbrowser/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="/ncitbrowser/js/script.js"></script>
  <script type="text/javascript" src="/ncitbrowser/js/search.js"></script>
  <script type="text/javascript" src="/ncitbrowser/js/dropdown.js"></script>


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

  <a href="https://www.cancer.gov" target="_blank" rel="noopener">
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
        href="https://evs.nci.nih.gov/"
        target="_blank" rel="noopener"
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
            <a href="https://evs.nci.nih.gov/" tabindex="0" target="_blank" rel="noopener" alt="Enterprise Vocabulary Services">
              EVS Home</a>
          </li>
          <li>
          <a href="https://ncim.nci.nih.gov/ncimbrowser" tabindex="0" target="_blank" rel="noopener" alt="NCI Metathesaurus">NCI Metathesaurus Browser</a>
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
        href="https://www.cancer.gov/cancertopics/terminologyresources"
        tabindex="0"
        target="_blank" rel="noopener"
        alt="NCI Terminology Resources">

      NCI Terminology Resources</a>
  </li>
    <li>
    <a
        href="https://ncitermform.nci.nih.gov/ncitermform/?dictionary=NCI%20Thesaurus"
        tabindex="0"
        target="_blank" rel="noopener"
        alt="Term Suggestion">

      Term Suggestion</a>
  </li>

</ul></li></ul></div>

</td></tr></table></div>


            <div class="pagecontent">
              <a name="evs-content" id="evs-content" tabindex="0"></a>


<%
String scheme = (String) request.getSession().getAttribute("cs");
String matchAlgorithm = (String) request.getSession().getAttribute("matchAlgorithm");
String matchText = (String) request.getSession().getAttribute("matchText");

System.out.println("graphdb_test_results cs: " + scheme);
System.out.println("graphdb_test_results algorithm: " + matchAlgorithm);
System.out.println("graphdb_test_results matchText: " + matchText);

Vector v = StringUtils.parseData(matchText, '\n');
System.out.println("v.size: " + v.size());

String graphdb_uri = null;
try {
   graphdb_uri = NCItBrowserProperties.getInstance().getGraphDBURL();
} catch (Exception ex) {
   ex.printStackTrace();
}
if (graphdb_uri == null) {
   graphdb_uri = "https://graphresolve-dev.nci.nih.gov";
}
		
long ms = System.currentTimeMillis();
LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();

//String matchText = (String) request.getParameter("matchText");
List rcr_list = null;
SearchUtilsExt searchUtilsExt = new SearchUtilsExt(lbSvc, graphdb_uri);
boolean getInbound = true;
int depth = 1;
String assocName = null;
Vector schemes = new Vector();
Vector versions = new Vector();
schemes.add(scheme);
versions.add(null);
String source = null;

String error_msg = (String) request.getSession().getAttribute("message");

%>

 <form name="testForm" method="post" action="/ncitbrowser/test?action=graphdb"><br>

          <table class="datatable_960" summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
          
<%
if (error_msg != null) {
%>
<p class="textbodyred">&nbsp;<%= error_msg %></p>
<%
}
%>          
          
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

<%
Vector graphdb_results = (Vector) request.getSession().getAttribute("graphdb_results");
if (graphdb_results != null) {
for (int i=0; i<graphdb_results.size(); i++) {
    String t = (String) graphdb_results.elementAt(i);
    Vector u = StringUtils.parseData(t, '|');
    scheme = (String) u.elementAt(0);
    String match_text = (String) u.elementAt(1);
    matchAlgorithm = (String) u.elementAt(2);
    String num_matches = (String) u.elementAt(3);
    String memory_use = (String) u.elementAt(4);
    String time_elapsed = (String) u.elementAt(5);
    String color = "dataRowDark";
    if (i % 2 == 0) {
        color = "dataRowLight";
    }
%>
              <tr class="<=color%>">
                  <td><%=scheme%></td>
                  <td><%=match_text%></td>
                  <td><%=matchAlgorithm%></td>
                  <td><%=num_matches%></td>
                  <td><%=memory_use%></td>
                  <td><%=time_elapsed%></td>
   	      </tr>     
<%
    }
}
%>

          </table>
        </form>

          <br/>


<div class="footer" style="width:920px">
  <ul class="textbody">
    <li>
    <a href="https://www.cancer.gov" target="_blank" rel="noopener" alt="National Cancer Institute">NCI Home</a>
    |</li>
  <li>
  <a href="/ncitbrowser/pages/contact_us.jsf?nav_type=terminologies">Contact Us</a>
  |</li>
<li>
<a href="https://www.cancer.gov/policies" target="_blank" rel="noopener" alt="National Cancer Institute Policies">Policies</a>
|</li>
<li>
<a href="https://www.cancer.gov/policies/page3" target="_blank" rel="noopener" alt="National Cancer Institute Accessibility">
  Accessibility</a>
|</li>
<li>
<a href="https://www.cancer.gov/policies/page6" target="_blank" rel="noopener" alt="National Cancer Institute FOIA">FOIA</a>
</li></ul>
<center>
  <a href="https://www.hhs.gov/" alt="U.S. Department of Health and Human Services">
    U.S. Department of Health and Human Services</a>&nbsp;|&nbsp;
  <a href="https://www.nih.gov/about-nih" alt="National Institutes of Health">National Institutes of Health</a>&nbsp;|&nbsp;
  <a href="https://www.cancer.gov/" alt="National Cancer Institute">National Cancer Institute</a>&nbsp;|&nbsp;
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
