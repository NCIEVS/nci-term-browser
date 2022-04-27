<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%
String match_text = gov.nih.nci.evs.browser.utils.HTTPUtils
.cleanXSS((String) request.getSession().getAttribute("matchText"));

if (match_text == null) match_text = "";

String termbrowser_displayed_match_text = HTTPUtils.convertJSPString(match_text);
String searchform_requestContextPath = request.getContextPath();
searchform_requestContextPath = searchform_requestContextPath.replace("//ncitbrowser//ncitbrowser", "//ncitbrowser");
%>
<div class="search-form">
  <input aria-label="Match Text" 
      CLASS="searchbox-input"
      name="matchText"
      value="<%=termbrowser_displayed_match_text%>"
      onFocus="active = true"
      onBlur="active = false"
      onkeypress="return submitEnter('searchTerm:multiple_search',event)"
      tabindex="0"
  />
  <h:commandButton
      id="multiple_search"
      value="Search"
      action="#{userSessionBean.multipleSearchAction}"
      image="#{searchform_requestContextPath}/images/search.gif"
      alt="Search"
      styleClass="searchbox-btn"
      tabindex="0">

  </h:commandButton>
  <h:outputLink value="#{facesContext.externalContext.requestContextPath}/pages/help.jsf#searchhelp" tabindex="0">
    <h:graphicImage
        value="/images/search-help.gif"
        styleClass="searchbox-btn"
        alt="Search Help"
        style="border-width:0;"
    />
  </h:outputLink>
  <%
  //String algorithm = (String) request.getSession().getAttribute("algorithm");
  String algorithm = gov.nih.nci.evs.browser.utils.HTTPUtils.cleanXSS((String) request.getSession().getAttribute("algorithm"));

  String check_e = "", check_s = "" , check_c ="";
  if (algorithm == null || algorithm.compareTo("contains") == 0)
  check_c = "checked";
  else if (algorithm.compareTo("startsWith") == 0)
  check_s= "checked";
  else
  check_e = "checked";
  

  
  
  %>
  <table border="0" cellspacing="0" cellpadding="0" role='presentation'>
    <tr valign="top" align="left">
      <td align="left" class="textbody">
        <input
            type="radio"
            id="contains"
            name="algorithm"
            value="contains"
            alt="Contains"
            <%=check_c%>
            tabindex="0"
            onclick="onAlgorithmChanged('searchTerm');">
        <label for="contains">Contains&nbsp;</label>    
        <input
            type="radio"
            id="exactMatch"
            name="algorithm"
            value="exactMatch"
            alt="Exact Match"
            <%=check_e%>
            tabindex="0">
        <label for="exactMatch">Exact Match&nbsp;</label>
        <input
            type="radio"
            id="startsWith"
            name="algorithm"
            value="startsWith"
            alt="Begins With"
            <%=check_s%>
            tabindex="0"
            onclick="onAlgorithmChanged('searchTerm');">
        <label for="startsWith">Begins With&nbsp;</label>
        <%
        String searchTarget = (String) request.getSession().getAttribute("searchTarget");
        String check_n = "", check_cd = "", check_p = "" , check_r ="";
        if (searchTarget == null || searchTarget.compareTo("names") == 0)
        check_n = "checked";
        else if (searchTarget == null || searchTarget.compareTo("codes") == 0)
        check_cd = "checked";
        else if (searchTarget.compareTo("properties") == 0)
        check_p= "checked";
        else
        check_r = "checked";
        %>
      </td>
    </tr>
    <tr align="left">
      <td height="1px" bgcolor="#2F2F5F"></td>
    </tr>
    <tr valign="top" align="left">
      <td align="left" class="textbody">
        <input type="radio" id="names" name="searchTarget" value="names" alt="Names" <%=check_n%> tabindex="0">
        <label for="names">Name&nbsp;</label>
        <input
            type="radio"
            id="codes"
            name="searchTarget"
            value="codes"
            alt="Codes"
            <%=check_cd%>
            tabindex="0"
            onclick="onCodeButtonPressed('searchTerm');">

        <label for="codes">Code&nbsp;</label>
        <input
            type="radio"
            id="properties"
            name="searchTarget"
            value="properties"
            alt="Properties"
            <%=check_p%>
            tabindex="0">

        <label for="properties">Property&nbsp;</label>
        <input
            type="radio"
            id="relationships"
            name="searchTarget"
            value="relationships"
            alt="Relationships"
            <%=check_r%>
            tabindex="0">

        <label for="relationships">Relationship&nbsp;</label>
      </td>
    </tr>
  </table>
</div>
