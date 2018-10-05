<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="org.lexgrid.valuesets.LexEVSValueSetDefinitionServices" %>

<script type="text/javascript">

  function refresh() {

    var selectValueSetSearchOptionObj = document.forms["valueSetSearchForm"].selectValueSetSearchOption;

    for (var i=0; i<selectValueSetSearchOptionObj.length; i++) {
      if (selectValueSetSearchOptionObj[i].checked) {
        selectValueSetSearchOption = selectValueSetSearchOptionObj[i].value;
      }
    }

    window.location.href="/ncitbrowser/pages/value_set_search.jsf?refresh=1"
    + "&opt="+ selectValueSetSearchOption;

  }
</script>

<%
String _searchform_requestContextPath = request.getContextPath();
_searchform_requestContextPath = _searchform_requestContextPath.replace("//ncitbrowser//ncitbrowser", "//ncitbrowser");

String _selected_cs = "";
String _selected_cd = null;

String _check_cs = "";
String _check_cd = "";
String _check_code = "";
String _check_name = "";
String _check_src = "";

String _valueset_search_algorithm = null;
_valueset_search_algorithm = (String) request.getSession().getAttribute("valueset_search_algorithm");
if (_valueset_search_algorithm == null) _valueset_search_algorithm = "";

String _check__e = "", _check__b = "", _check__s = "" , _check__c ="";
/*
if (_valueset_search_algorithm == null || _valueset_search_algorithm.compareTo("exactMatch") == 0)
_check__e = "checked";
else if (_valueset_search_algorithm.compareTo("startsWith") == 0)
_check__s= "checked";
else if (_valueset_search_algorithm.compareTo("DoubleMetaphoneLuceneQuery") == 0)
_check__b= "checked";
else
_check__c = "checked";
*/

if (algorithm == null || algorithm.compareTo("contains") == 0)
check_c = "checked";
else if (algorithm.compareTo("startsWith") == 0)
check_s = "checked";
else if (algorithm.compareTo("DoubleMetaphoneLuceneQuery") == 0)
check_b = "checked";
else if (algorithm.compareTo("exactMatch") == 0)
check_e = "checked";

String _selectValueSetSearchOption = null;
_selectValueSetSearchOption = HTTPUtils.cleanXSS((String) request.getParameter("opt"));

if (_selectValueSetSearchOption == null) {
  _selectValueSetSearchOption = (String) request.getSession().getAttribute("selectValueSetSearchOption");
}

if (_selectValueSetSearchOption == null || _selectValueSetSearchOption.compareTo("null") == 0) {
  _selectValueSetSearchOption = "Code";
}

if (_selectValueSetSearchOption.compareTo("CodingScheme") == 0)
_check_cs = "checked";
else if (_selectValueSetSearchOption.compareTo("Code") == 0)
_check_code = "checked";
else if (_selectValueSetSearchOption.compareTo("Name") == 0)
_check_name = "checked";
else if (_selectValueSetSearchOption.compareTo("Source") == 0)
_check_src = "checked";

String _valueset_match_text = null;
_valueset_match_text = (String) request.getSession().getAttribute("matchText_VSD");
if (_valueset_match_text == null) _valueset_match_text = "";
%>
<h:form id="valueSetSearchForm" styleClass="search-form" acceptcharset="UTF-8">

  <input type="hidden" id="nav_type" name="nav_type" value="valuesets" />

  <table role='presentation'>
    <tr>
      <td>

        <% if (_selectValueSetSearchOption.compareTo("CodingScheme") == 0) { %>
          <input
              CLASS="searchbox-input-2"
              name="matchText"
              value=""
              onkeypress="return submitEnter('valueSetSearchForm:valueset_search',event)"
              tabindex="1"
          />
        <% } else { %>

          <input aria-labelledby="Match Text" 
              CLASS="searchbox-input-2"
              name="matchText"
              value="<%=_valueset_match_text%>"
              onFocus="active = true"
              onBlur="active = false"
              onkeypress="return submitEnter('valueSetSearchForm:valueset_search',event)"
              tabindex="2"
          />
        <% } %>

        <h:commandButton
            id="valueset_search"
            value="Search"
            action="#{valueSetBean.valueSetSearchAction}"
            onclick="javascript:cursor_wait();"
            image="#{valueSetSearch_requestContextPath}/images/search.gif"
            styleClass="searchbox-btn"
            alt="Search concepts in value set"
            tabindex="3">

        </h:commandButton>

        <h:outputLink value="#{facesContext.externalContext.requestContextPath}/pages/help.jsf#searchhelp" tabindex="4">
          <h:graphicImage
              value="/images/search-help.gif"
              styleClass="searchbox-btn"
              alt="Search Help"
              style="border-width:0;"
          />
        </h:outputLink>
      </td>
    </tr>

    <tr>
      <td>
        <table border="0" cellspacing="0" cellpadding="0" role='presentation'>

          <tr valign="top" align="left">
            <td align="left" class="textbody">
              <input
                  type="radio"
                  id="contains"
                  name="valueset_search_algorithm"
                  value="contains"
                  alt="Contains"
                  <%=_check__c%>
                  tabindex="5">

              <label for="contains">Contains</label>
              <input
                  type="radio"
                  id="exactMatch"
                  name="valueset_search_algorithm"
                  value="exactMatch"
                  alt="Exact Match"
                  <%=_check__e%>
                  tabindex="6">

              <label for="exactMatch">Exact Match&nbsp;</label>
              <input
                  type="radio"
                  id="startsWith"
                  name="valueset_search_algorithm"
                  value="startsWith"
                  alt="Begins With"
                  <%=_check__s%>
                  tabindex="7">

              <label for="startsWith">Begins With&nbsp;</label>
            </td>
          </tr>

          <tr align="left">
            <td height="1px" bgcolor="#2F2F5F"></td>
          </tr>

          <tr valign="top" align="left">
            <td align="left" class="textbody">
              <input 
                  type="radio"
                  id="selectValueSetSearchOption"
                  name="selectValueSetSearchOption"
                  value="Code"
                  <%=_check_code%>
                  alt="Code"
                  tabindex="8"
                  onclick="javascript:refresh()">
              <label for="codes">Code&nbsp;</label>
              <input
                  type="radio"
                  id="selectValueSetSearchOption"
                  name="selectValueSetSearchOption"
                  value="Name"
                  <%=_check_name%>
                  alt="Name"
                  tabindex="9"
                  onclick="javascript:refresh()">

              <label for="names">Name&nbsp;</label>
              <input
                  type="radio"
                  id="selectValueSetSearchOption"
                  name="selectValueSetSearchOption"
                  value="Source"
                  <%=_check_src%>
                  alt="Source"
                  tabindex="10"
                  onclick="javascript:refresh()">
              <label for="source">Source&nbsp;</label>
              <input
                  type="radio"
                  id="selectValueSetSearchOption"
                  name="selectValueSetSearchOption"
                  value="CodingScheme"
                  <%=_check_cs%>
                  alt="Coding Scheme"
                  tabindex="11"
                  onclick="javascript:refresh()">
              <label for="CodingScheme">Coding Scheme&nbsp;</label>
            </td>
          </tr>
        </table>
      </td>
    </tr>

    <% if (_selectValueSetSearchOption.compareToIgnoreCase("CodingScheme") == 0) { %>
      <tr>

        <td class="dataCellText">
          <h:outputLabel id="codingschemelabel" value="Terminology:" styleClass="textbody">
            <h:selectOneMenu
                id="selectedOntology"
                value="#{valueSetBean.selectedOntology}"
                immediate
                =
                "true"
                valueChangeListener="#{valueSetBean.ontologyChangedEvent}">

              <f:selectItems value="#{valueSetBean.ontologyList}" />
            </h:selectOneMenu>
          </h:outputLabel>
        </td>
      </tr>
    <% } %>

  </table>

  <input type="hidden" name="referer" id="referer" value="<%=HTTPUtils.getRefererParmEncode(request)%>">

</h:form>
