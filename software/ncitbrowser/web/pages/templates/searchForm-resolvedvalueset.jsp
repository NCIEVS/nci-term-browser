<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.HTTPUtils" %>
<%

          
          
  String match_text = gov.nih.nci.evs.browser.utils.HTTPUtils
    .cleanXSS((String) request.getSession().getAttribute("matchText"));

  if (match_text == null) match_text = "";

    String userAgent = request.getHeader("user-agent");
    boolean isIE = userAgent != null && userAgent.toLowerCase().contains("msie");


  String termbrowser_displayed_match_text = HTTPUtils.convertJSPString(match_text);
  String searchform_requestContextPath = request.getContextPath();
  searchform_requestContextPath = searchform_requestContextPath.replace("//ncitbrowser//ncitbrowser", "//ncitbrowser");

    String algorithm = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("algorithm"));

    String check_e = "", check_s = "" , check_c ="";
    if (algorithm == null || algorithm.compareTo("exactMatch") == 0)
      check_e = "checked";
    else if (algorithm.compareTo("startsWith") == 0)
      check_s= "checked";
    else
      check_c = "checked";
      
      
        String searchTarget = (String) request.getSession().getAttribute("searchTarget");
        String check_n = "", check_p = "" , check_r ="";
        if (searchTarget == null || searchTarget.compareTo("names") == 0)
          check_n = "checked";
        else if (searchTarget.compareTo("properties") == 0)
          check_p= "checked";
        else
          check_r = "checked";      
      
%>
  
  
<h:form id="resolvedValueSetSearchForm" styleClass="search-form">   
  <label for="matchText" />
    <input CLASS="searchbox-input" id="matchText" name="matchText" value="<%=match_text%>" onFocus="active=true"
        onBlur="active=false"  onkeypress="return submitEnter('search',event)" tabindex="1"/>
    <h:commandButton id="search" value="Search" action="#{valueSetBean.resolvedValueSetSearchAction}"
      accesskey="13"
      onclick="javascript:cursor_wait();"
      image="#{form_requestContextPath}/images/search.gif"
      alt="Search"
      styleClass="searchbox-btn"
      tabindex="2">
    </h:commandButton>
    <h:outputLink value="#{facesContext.externalContext.requestContextPath}/pages/help.jsf#searchhelp" tabindex="3">
      <h:graphicImage value="/images/search-help.gif" style="border-width:0;" styleClass="searchbox-btn"/>
    </h:outputLink>

  <table border="0" cellspacing="0" cellpadding="0" width="340px">
    <tr valign="top" align="left">
      <td align="left" class="textbody" colspan="2">
        <input type="radio" name="algorithm" id="algorithm1" value="exactMatch" alt="Exact Match" <%=check_e%> tabindex="4"/><label for="algorithm1">Exact Match&nbsp;</label>
        <input type="radio" name="algorithm" id="algorithm2" value="startsWith" alt="Begins With" <%=check_s%> tabindex="4"/><label for="algorithm2">Begins With&nbsp;</label>
        <input type="radio" name="algorithm" id="algorithm3" value="contains" alt="Contains" <%=check_c%> tabindex="4"/><label for="algorithm3">Contains</label>
      </td>
    </tr>
    <tr align="left">
      <td width="263px" height="1px" bgcolor="#2F2F5F"></td>
      <!-- The following lines are needed to make "Advanced Search" link flush right -->
      <% if (isIE) { %>
          <td width="77px"></td>
      <% } else { %>
          <td></td>
      <% } %>
    </tr>

    <tr valign="top" align="left">
      <td align="left" class="textbody" colspan="2">
        <input type="radio" name="searchTarget" id="searchTarget1" value="names" alt="Names" <%=check_n%> tabindex="5"/><label for="searchTarget1">Name/Code&nbsp;</label>
        <input type="radio" name="searchTarget" id="searchTarget2" value="properties" alt="Properties" <%=check_p%> tabindex="5"/><label for="searchTarget2">Property&nbsp;</label>
      </td>
    </tr>
    <tr><td height="5px;"></td></tr>
    <tr><td colspan="2">
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr valign="top">

    <input type="hidden" name="referer" id="referer" value="<%=HTTPUtils.getRefererParmEncode(request)%>" />
    <input type="hidden" name="vsd_uri" id="vsd_uri" value="<%=vsd_uri%>" />


</h:form>

        </tr>
      </table>
    </td></tr>
  </table>

