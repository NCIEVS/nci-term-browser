<%@ page import="gov.nih.nci.evs.browser.properties.*"%>
<%@ page import="gov.nih.nci.evs.browser.utils.*"%>
<%@ page import="gov.nih.nci.evs.browser.bean.*"%>
<%@ page import="org.apache.log4j.*"%>
<script type="text/javascript">
	function cursor_wait() {
		document.body.style.cursor = 'wait';
	}
	function disableAnchor() {
		var obj1 = document.getElementById("a_tpTab");
		if (obj1 != null)
			obj1.removeAttribute('href');

		var obj2 = document.getElementById("a_relTab");
		if (obj2 != null)
			obj2.removeAttribute('href');

		var obj3 = document.getElementById("a_synTab");
		if (obj3 != null)
			obj3.removeAttribute('href');

		var obj4 = document.getElementById("a_srcTab");
		if (obj4 != null)
			obj4.removeAttribute('href');

		var obj5 = document.getElementById("a_allTab");
		if (obj5 != null)
			obj5.removeAttribute('href');

		var obj6 = document.getElementById("a_hierBut");
		if (obj6 != null)
			obj6.removeAttribute('href');
	}
	
	
	
	
	
    function onCodeButtonPressed(formname) {
	  var algorithmObj = document.forms[formname].algorithm;
	  for (var j=0; j<algorithmObj.length; j++) {
		  algorithm = algorithmObj[j].value;
		  if (algorithm == "exactMatch") {
			 algorithmObj[j].checked = true;
		  }
	  }
    }

    function getSearchTarget(formname) {
          var searchTargetObj = document.forms[formname].searchTarget;
	  for (var j=0; j<searchTargetObj.length; j++) {
	      if (searchTargetObj[j].checked == true) {
	         return searchTargetObj[j].value;
	      }
	  }
    }

    function onAlgorithmChanged(formname) {
      var curr_target = getSearchTarget(formname);
      if (curr_target != "codes") return;

          var searchTargetObj = document.forms[formname].searchTarget;
	  for (var j=0; j<searchTargetObj.length; j++) {
		  target = searchTargetObj[j].value;
		  if (target == "codes") {
			  searchTargetObj[0].checked = true;
			  return;
		  }
	  }
    }	
	
	
	
	
</script>
<%
        boolean back_to_search_results_link = false;
        boolean value_set_entity_search = false;
        String multiple_search_flag = HTTPUtils.cleanXSS((String) request.getParameter("m"));
        String search_key = HTTPUtils.cleanXSS((String) request.getParameter("key"));
        
        String t = HTTPUtils.cleanXSS((String) request.getParameter("b"));
        if (!DataUtils.isNull(t) && t.compareTo("0") != 0) {
            t = "1";
        }        
        
        
        String t2 = HTTPUtils.cleanXSS((String) request.getParameter("vse")); 
        String vse = null;
        
        if (!DataUtils.isNull(t2)) {
            value_set_entity_search = true;
            vse = t2;            
        }
        
        String page_number = HTTPUtils.cleanXSS((String) request.getParameter("n"));
        if (!DataUtils.isNull(page_number) && !DataUtils.isInteger(page_number)) {
            page_number = "1";
        }
        
        if (DataUtils.isNull(t)) {
		t = (String) request.getSession().getAttribute("b"); 
		page_number = (String) request.getSession().getAttribute("n"); 
		search_key = (String) request.getSession().getAttribute("key"); 
	}        
        if (!DataUtils.isNull(t) || !DataUtils.isNull(t2)) {
            back_to_search_results_link = true;
        }

	Logger logger = Utils.getJspLogger("searchForm.jsp");
        if (!DataUtils.isNull(multiple_search_flag) && multiple_search_flag.compareTo("0") != 0) {
            multiple_search_flag = "1";
        }	
	
	
	if (DataUtils.isNull(multiple_search_flag)) {
	    multiple_search_flag = (String) request.getSession().getAttribute("m"); 
	}
	
	String form_requestContextPath = request.getContextPath();
	form_requestContextPath = form_requestContextPath.replace(
			"//ncitbrowser//ncitbrowser", "//ncitbrowser");
	String userAgent = request.getHeader("user-agent");
	boolean isIE = userAgent != null
			&& userAgent.toLowerCase().contains("msie");
	String match_text = (String) request.getSession().getAttribute(
			"matchText");
	if (match_text == null || match_text.compareTo("null") == 0)
		match_text = "";
	JSPUtils.JSPHeaderInfo vocab_info = new JSPUtils.JSPHeaderInfo(
			request);
	String vocab_name = vocab_info.dictionary;
	vocab_name = DataUtils.getCodingSchemeName(vocab_name);
	String srchform_version = vocab_info.version;

	boolean is_a_mapping = DataUtils.isMapping(vocab_name,
			srchform_version);

	logger.debug(Utils.SEPARATOR);
	logger.debug("searchForm.jsp vocab_name: " + vocab_name);
	logger.debug("searchForm.jsp version: " + srchform_version);

	String displayed_match_text = HTTPUtils
			.convertJSPString(match_text);
	String algorithm = gov.nih.nci.evs.browser.utils.HTTPUtils
			.cleanXSS((String) request.getSession().getAttribute(
					"algorithm"));


	String check_e = "", check_b = "", check_s = "", check_c = "";
	/*
	if (algorithm == null || algorithm.compareTo("exactMatch") == 0)
		check_e = "checked";
	else if (algorithm.compareTo("startsWith") == 0)
		check_s = "checked";
	else if (algorithm.compareTo("DoubleMetaphoneLuceneQuery") == 0)
		check_b = "checked";
	else
		check_c = "checked";
        */

	if (algorithm == null || algorithm.compareTo("contains") == 0)
		check_c = "checked";
	else if (algorithm.compareTo("startsWith") == 0)
		check_s = "checked";
	else if (algorithm.compareTo("DoubleMetaphoneLuceneQuery") == 0)
		check_b = "checked";
	else if (algorithm.compareTo("exactMatch") == 0)
		check_e = "checked";
		

	String searchTarget = (String) request.getSession().getAttribute(
			"searchTarget");
	String check_n = "", check_cd = "", check_p = "", check_r = "";
	if (searchTarget == null || searchTarget.compareTo("names") == 0)
		check_n = "checked";
	else if (searchTarget == null || searchTarget.compareTo("codes") == 0)
		check_cd = "checked";		
	else if (searchTarget.compareTo("properties") == 0)
		check_p = "checked";
	else
		check_r = "checked";
%>
<h:form id="searchTerm" styleClass="search-form" onsubmit="javascript:disableAnchor();" acceptcharset="UTF-8">
   <input CLASS="searchbox-input" id="matchText" name="matchText"
      value="<%=HTTPUtils.cleanXSS(displayed_match_text)%>" onFocus="active=true"
      onBlur="active=false"
      onkeypress="return submitEnter('searchTerm:search', event)"
      tabindex="1" />
   <h:commandButton id="search" value="Search"
      action="#{userSessionBean.searchAction}" accesskey="13"
      onclick="javascript:cursor_wait();"
      image="#{form_requestContextPath}/images/search.gif" alt="Search"
      styleClass="searchbox-btn" tabindex="2">
   </h:commandButton>
   <h:outputLink
      value="#{facesContext.externalContext.requestContextPath}/pages/help.jsf#searchhelp"
      tabindex="3">
      <h:graphicImage value="/images/search-help.gif" alt="Search Help"
         style="border-width:0;" styleClass="searchbox-btn" />
   </h:outputLink>
   <table border="0" cellspacing="0" cellpadding="0" width="340px">
      <tr valign="top" align="left">
         <td align="left" class="textbody" colspan="2">
            <input type="radio" id="contains" name="algorithm" value="contains"   alt="Contains"    <%=check_c%> tabindex="4" onclick="onAlgorithmChanged('searchTerm');"><label for="contains">Contains</label>
            <input type="radio" id="exactMatch" name="algorithm" value="exactMatch" alt="Exact Match" <%=check_e%> tabindex="5"><label for="exactMatch">Exact Match&nbsp;</label>
            <input type="radio" id="startsWith" name="algorithm" value="startsWith" alt="Begins With" <%=check_s%> tabindex="6" onclick="onAlgorithmChanged('searchTerm');"><label for="startsWith">Begins With&nbsp;</label>
         </td>
      </tr>
      <tr align="left">
        <td width="263px" height="1px" bgcolor="#2F2F5F"></td>
        <td width="77px"></td>
      </tr>
      <tr valign="top" align="left">
         <td align="left" class="textbody" colspan="2">

	  <input type="radio" id="names" name="searchTarget" value="names"         alt="Name"         <%=check_n%>  tabindex="7"><label for="names">Name&nbsp;</label>
	  <input type="radio" id="codes" name="searchTarget" value="codes"         alt="Code"         <%=check_cd%> tabindex="8" onclick="onCodeButtonPressed('searchTerm');" ><label for="codes">Code&nbsp;</label>
	  <input type="radio" id="properties" name="searchTarget" value="properties"    alt="Property"     <%=check_p%>  tabindex="9"><label for="properties">Property&nbsp;</label>
	  <input type="radio" id="relationships" name="searchTarget" value="relationships" alt="Relationship" <%=check_r%>  tabindex="10"><label for="relationships">Relationship</label>
           
         </td>
      </tr>
      <tr>
         <td height="5px;"></td>
      </tr>
      <tr>
         <td colspan="2">
            <%
               Boolean hideAdvancedSearchLink = (Boolean) request.getAttribute("hideAdvancedSearchLink");
               if (!is_a_mapping && (hideAdvancedSearchLink == null || !hideAdvancedSearchLink)) {
            %>
            <table border="0" cellspacing="0" cellpadding="0" width="100%">
               <tr valign="top">
               <%
               if (DataUtils.isNull(search_key)) {
               %>
                  <td height="5px;"></td>
                  
               <%   
               } else if (back_to_search_results_link) {
              
                   if (value_set_entity_search) {
                   
                  %>
                  <td valign="middle" align="left">
                     <a class="global-nav"
                        href="<%=request.getContextPath()%>/pages/value_set_entity_search_results.jsf?key=<%=HTTPUtils.cleanXSS(search_key)%>&page_number=<%=page_number%>&vse=<%=vse%>"
                        tabindex="11" title="Back to search results">Back to search results</a>
                  </td>
                  <% 
                  } else {              
			  if (DataUtils.isNull(multiple_search_flag)) {
			  %>
			  <td valign="middle" align="left">
			     <a class="global-nav"
				href="<%=request.getContextPath()%>/pages/search_results.jsf?dictionary=<%=HTTPUtils.cleanXSS(vocab_name)%>&version=<%=HTTPUtils.cleanXSS(srchform_version)%>&key=<%=HTTPUtils.cleanXSS(search_key)%>&page_number=<%=page_number%>"
				tabindex="12" title="Back to search results">Back to search results</a>
			  </td>
			  <% 
			  } else {
			  %>
			  <td valign="middle" align="left">
			     <a class="global-nav"
				href="<%=request.getContextPath()%>/pages/multiple_search_results.jsf?key=<%=HTTPUtils.cleanXSS(search_key)%>&page_number=<%=page_number%>"
				tabindex="13" title="Back to search results">Back to search results</a>
			  </td>
			  <%  
			  }
	   	  }
               }
               %>
                  
                  <td valign="middle" align="right">
                     <a class="textbodyredsmall"
                        href="<%=request.getContextPath()%>/pages/advanced_search.jsf?dictionary=<%=HTTPUtils.cleanXSS(vocab_name)%>&version=<%=HTTPUtils.cleanXSS(srchform_version)%>"
                        tabindex="14" title="Advanced Search"> Advanced Search </a>
                  </td>
               </tr>
            </table>
            <%
               }
            %>            
         </td>
      </tr>
   </table>
   <input type="hidden" name="referer" id="referer" value="<%=HTTPUtils.getRefererParmEncode(request)%>" />   
   <%
      if (vocab_name != null) {
   %>
   <input type="hidden" id="vocabulary" name="vocabulary" value="<%=HTTPUtils.cleanXSS(vocab_name)%>" />
   <input type="hidden" id="dictionary" name="dictionary" value="<%=HTTPUtils.cleanXSS(vocab_name)%>" />
   <input type="hidden" id="scheme" name="scheme" value="<%=HTTPUtils.cleanXSS(vocab_name)%>" />
   <%
      }
      if (srchform_version != null) {
   %>
   <input type="hidden" id="version" name="version" value="<%=HTTPUtils.cleanXSS(srchform_version)%>" />
   <%
      }
   %>   
</h:form>
