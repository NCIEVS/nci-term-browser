<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.HTTPUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.JSPUtils" %>
<!-- Thesaurus, banner search area -->
<div class="bannerarea_960">
  <%
  JSPUtils.JSPHeaderInfoMore info3 = new JSPUtils.JSPHeaderInfoMore(request);
String search_results_dictionary_hc = (String) request.getSession().getAttribute("search_results_dictionary");
String display_name = null;
String info3_dictionary = info3.dictionary;
if (info3.dictionary == null && search_results_dictionary_hc != null) {
    info3.setDictionary(search_results_dictionary_hc);
    display_name = DataUtils.getMetadataValue(search_results_dictionary_hc, "display_name");
    info3_dictionary = search_results_dictionary_hc;
    info3.set_display_name(display_name);
}
  String nciturl = request.getContextPath() + "/pages/home.jsf" + "?version=" + info3.version;
  if (JSPUtils.isNull(info3_dictionary)) {
 
    %>
    <a href="<%=basePath%>/start.jsf" style="text-decoration: none;">
      <div class="vocabularynamebanner_tb">
        <span class="vocabularynamelong_tb"><%= JSPUtils.getApplicationVersionDisplay() %></span>
      </div>
    </a>
  <% } else if (DataUtils.isNCIT(info3_dictionary)) { 
  
  request.getSession().setAttribute("active_vocabulary", Constants.CODING_SCHEME_NAME);
  
  %>
 
    <a href="<%=nciturl%>" style="text-decoration: none;">
      <div class="vocabularynamebanner_ncit">
        <%
        String content_header_other_dictionary = HTTPUtils.cleanXSS(info3_dictionary);
        String content_header_other_version = HTTPUtils.cleanXSS(info3.version);

        String release_date = DataUtils.getVersionReleaseDate(content_header_other_dictionary, content_header_other_version);
        boolean display_release_date = true;
        if (release_date == null || release_date.compareTo("") == 0) {
          display_release_date = false;
        }
        if (display_release_date) {
          %>
          <span class="vocabularynamelong_ncit">
            Version:<%= HTTPUtils.cleanXSS(info3.term_browser_version) %> (Release date:<%= release_date %>)
          </span>
        <% } else { %>
          <span class="vocabularynamelong_ncit">
            Version:&nbsp;<%= HTTPUtils.cleanXSS(info3.term_browser_version) %>
          </span>
        <% } %>

      </div>
    </a>
  <% } else { 
      System.out.println("info3_dictionary: " + info3_dictionary);
   %>
  
    <a
        class="vocabularynamebanner"
        href="<%=request.getContextPath()%>/pages/vocabulary.jsf?dictionary=<%=HTTPUtils.cleanXSS(info3_dictionary)%>">

      <div class="vocabularynamebanner">
        <div
            class="vocabularynameshort"
            STYLE="font-size: <%=HTTPUtils.maxFontSize(info3.display_name)%>px; font-family : Arial">

          <%= HTTPUtils.cleanXSS(info3.display_name) %>
        </div>

        <%
        String content_header_other_dictionary = HTTPUtils.cleanXSS(info3_dictionary);
        String content_header_other_version = HTTPUtils.cleanXSS(info3.version);

        String release_date = DataUtils.getVersionReleaseDate(content_header_other_dictionary, content_header_other_version);
        boolean display_release_date = true;
        if (release_date == null || release_date.compareTo("") == 0) {
          display_release_date = false;
        }
        if (display_release_date) {
          %>
          <div class="vocabularynamelong">
            Version:<%= HTTPUtils.cleanXSS(info3.term_browser_version) %> (Release date:<%= release_date %>)
          </div>
        <% } else { %>
          <div class="vocabularynamelong">Version:&nbsp;<%= HTTPUtils.cleanXSS(info3.term_browser_version) %></div>
        <% } %>

      </div>
    </a>
  <% } %>  
  
  <% if (! JSPUtils.isNull(info3_dictionary)) { %>
    <div class="search-globalnav_960">
      <!-- Search box -->
      <div class="searchbox-top">
        <img src="<%=basePath%>/images/searchbox-top.gif" width="352" height="2" alt="SearchBox Top" />
      </div>
      <div class="searchbox"><%@ include file="searchForm.jsp" %></div>
      <div class="searchbox-bottom">
        <img src="<%=basePath%>/images/searchbox-bottom.gif" width="352" height="2" alt="SearchBox Bottom" />
      </div>
      <!-- end Search box -->
      <!-- Global Navigation -->
      <%@ include file="menuBar.jsp" %>
      <!-- end Global Navigation -->
    </div>
  <% } %>
</div>
<!-- end Thesaurus, banner search area -->
<!-- Quick links bar -->
<%@ include file="quickLink.jsp" %>
<!-- end Quick links bar -->
