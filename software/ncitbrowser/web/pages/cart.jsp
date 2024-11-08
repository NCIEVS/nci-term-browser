<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.bean.CartActionBean" %>
<%@ page import="gov.nih.nci.evs.browser.bean.CartActionBean.Concept" %>
<%@ page import="javax.faces.context.*" %>

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
  <script src="https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js"></script>
  <title>NCI Term Browser</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>


</head>
<body onLoad="javascript:popupMessage();document.forms.searchTerm.matchText.focus();">
  <header class="flex-grow-0">
	<div style='text-align: left'>
	<include-html src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html"></include-html>
	</div>
  </header>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/wz_tooltip.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_centerwindow.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_followscroll.js"></script>

<script type="text/javascript">
	function submitform(btn)
	{
	    document.getElementById('btn').value=btn;
	    document.cartFormId.submit();
	}
</script>


<f:view>
    <%
      JSPUtils.JSPHeaderInfoMore info = new JSPUtils.JSPHeaderInfoMore(request);
      String scheme = info.dictionary;
      String dictionary = null;
      if (scheme == null) {
          scheme = (String) request.getSession().getAttribute("scheme");
      } 
      if (scheme == null) {
          scheme = (String) request.getSession().getAttribute("search_results_dictionary");
      }       
      String version = info.version;
        
      String default_cs = "NCI_Thesaurus";
      String contactUsUrl = request.getContextPath() + "/pages/contact_us.jsf";
      String subsetsUrl = request.getContextPath() + "/pages/subset.jsf";
      gov.nih.nci.evs.browser.bean.CartActionBean.Concept item = null;
      CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");  
      if (cartActionBean == null) {
          cartActionBean = new CartActionBean();
          cartActionBean._init();
      }
      Collection<gov.nih.nci.evs.browser.bean.CartActionBean.Concept> items = cartActionBean.getConcepts(); 
      int count = items.size();//cartActionBean.getCount();
    %>
        <%@ include file="/pages/templates/header.jsp" %>
        <div class="center-page_960">
          <%@ include file="/pages/templates/sub-header.jsp" %>
          <!-- Main box -->
          <div id="main-area_960">
            <%@ include file="/pages/templates/content-header-no-searchbox.jsp" %>
            <!-- Page content -->
            <div class="pagecontent">
              <a name="evs-content" id="evs-content" tabindex="0"></a>
  
 <form name="cartFormId" method="post" action="<%=request.getContextPath() %>/ajax?action=cart"><br>
 
 <input type="hidden" id="btn" name="btn" value="not_selected">
 <input type="hidden" id="scheme" name="scheme" value="<%=scheme%>">
 <input type="hidden" id="version" name="version" value="<%=version%>">
            
            <table border="0" class="datatable_960">
              <tr>
                <td width="200px">
                <table border="0" width="100%" role='presentation'>
                  <tr>
                  
                    <td class="texttitle-blue" width="40">Cart</td>
                    <td class="texttitle-gray"> (<%=count%>)</td>
                    <td class="texttitle-gray">
<!--
<% 
if (count > 0) {  
%>
       <a href="javascript:submitform('exit_cart')">
<%
} else {
%>
       <a href="javascript:go_to_browser_home()">
<%
} 
%>
-->
       <a href="javascript:submitform('exit_cart')"><font size="2">Exit Cart</font></a>  
                    </td>
                  </tr>
                </table>
                </td>
                
  <%               
  if (count > 0) {              
  %>                
              <td align="right" valign="bottom" nowrap>

<!--              
<input type=image src="<%=request.getContextPath() %>/images/select_all.gif"  id="cartAction" name="cartAction1" value="selectall" alt="Select All" title="Select all concepts" onclick="this.form.submit();">
&nbsp;
<input type=image src="<%=request.getContextPath() %>/images/clearselections.gif"  id="cartAction" name="cartAction2" value="unselectall" alt="Unselect" title="Unselect all concepts"  onclick="this.form.submit();">
&nbsp;
<input type=image src="<%=request.getContextPath() %>/images/remove.gif"  id="cartAction" name="cartAction3" value="removefromcart" alt="Remove" title="Remove concepts from the cart" onclick="this.form.submit();">
&nbsp; 
<input type=image src="<%=request.getContextPath() %>/images/exportxml.gif"  id="cartAction" name="cartAction4" value="exportxml" alt="Export XML" title="Export cart contents in LexGrid XML format" onclick="this.form.submit();">
&nbsp; 
<input type=image src="<%=request.getContextPath() %>/images/exportcsv.gif"  id="cartAction" name="cartAction5" value="exportcsv" alt="Export CSV" title="Generate a list of cart concepts in CSV format readable from Excel" onclick="this.form.submit();">
-->

<input type=image src="<%=request.getContextPath() %>/images/select_all.gif"  id="cartAction" name="cartAction" value="selectall" alt="Select All" title="Select all concepts" onclick="submitform('selectall');">
&nbsp;
<input type=image src="<%=request.getContextPath() %>/images/clearselections.gif"  id="cartAction" name="cartAction" value="unselectall" alt="Unselect" title="Unselect all concepts" onclick="submitform('unselectall');">
&nbsp;
<input type=image src="<%=request.getContextPath() %>/images/remove.gif"  id="cartAction" name="cartAction" value="removefromcart" alt="Remove" title="Remove concepts from the cart" onclick="submitform('removefromcart');">
&nbsp; 
<input type=image src="<%=request.getContextPath() %>/images/exportxml.gif"  id="cartAction" name="cartAction" value="exportxml" alt="Export XML" title="Export cart contents in LexGrid XML format" onclick="submitform('exportxml');">
&nbsp; 
<input type=image src="<%=request.getContextPath() %>/images/exportcsv.gif"  id="cartAction" name="cartAction" value="exportcsv" alt="Export CSV" title="Generate a list of cart concepts in CSV format readable from Excel" onclick="submitform('exportcsv');">


              </td>              
            </tr>
         </table>
        <hr/>
  <%
  String message = (String) request.getSession().getAttribute("message");
  request.getSession().removeAttribute("message");
  if (message != null) {
  %>
  <p class="textbodyred">&nbsp;<%=message%></p>
  <%
  } 
  %>
          <table class="datatable_960" summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
              <tr>
                <th class="dataTableHeader" scope="col" align="left" width="20px">&nbsp;</th>
                <th class="dataTableHeader" scope="col" align="left">Concept</th>
                <th class="dataTableHeader" scope="col" align="left">Semantic Type</th>
              </tr>
            
              
  <%
  if (cartActionBean == null) {
  %>
          <tr><td>cartActionBean is null???</td></tr>
  <%        
  } else {
  	items = cartActionBean.getConcepts();
  	if (items != null) {
 	   try { 
  		Iterator it = items.iterator();
  		int i=0;
  		while (it.hasNext()) {
                      item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it.next(); 
                      String checked = "";
                      String checkbox_name = "code_" + item.getCode();
                      if (item.getSelected()) {
                          checked = "checked";
                      }
                          if (i%2 == 0) {
  %>                         
                               <tr class="dataRowDark">
  <%                             
                           } else {
  %>                             
                               <tr class="dataRowLight">
  <%
                           }
                           i++;
  %>
                         <td>
<input type="checkbox" id=<%=checkbox_name%> name=<%=checkbox_name%> value=<%=item.getCode()%> <%=checked%> />
                         </td>
                         <td>
  			     <label for=<%=item.getCode()%>>
<a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=item.getCodingScheme()%>&code=<%=item.getCode()%>"><%=DataUtils.encodeTerm(item.getName())%></a>
  			     </label>
  			 </td>
  
  			 
  			 <td><%=item.getDisplayCodingSchemeName()%></td>
  			 </tr>
  	<%		  
  		}
  		
  	    } catch (Exception ex) {
  	        ex.printStackTrace();
  	    }
  		
  	}
  }
  
  }
  %>               
          </table>
        </form>
 
          <br/>
          <%@ include file="/pages/templates/nciFooter.jsp" %>
        </div> <!-- end pagecontent -->
      </div> <!-- end main-area -->
      
<div class="mainbox-bottom">
  <img src="/ncitbrowser/images/mainbox-bottom.gif" width="945" height="5" alt="Mainbox Bottom" />
</div>

      <!-- end Main box -->
    </div> <!-- end center-page -->
    
</f:view>

<script type="text/javascript">_satellite.pageBottom();</script>
</body>
</html>
