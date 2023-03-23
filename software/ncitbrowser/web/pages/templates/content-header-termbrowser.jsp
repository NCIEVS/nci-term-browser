<!-- Thesaurus, banner search area -->

<div class="bannerarea_960">
  <a href="<%=basePath%>/start.jsf" style="text-decoration: none;">
    <div class="vocabularynamebanner_tb">
      <span class="vocabularynamelong_tb"><%= JSPUtils.getApplicationVersionDisplay() %></span>
    </div>
  </a>
  <div class="search-globalnav_960">
    <!-- Search box -->
    <div class="searchbox-top">
      <img src="<%=basePath%>/images/searchbox-top.gif" width="352" height="2" alt="SearchBox Top" />
    </div>

    <div class="searchbox"><%@ include file="/pages/templates/searchForm-termbrowser.jsp" %></div>

    <div class="searchbox-bottom">
      <img src="<%=basePath%>/images/searchbox-bottom.gif" width="352" height="2" alt="SearchBox Bottom" />
    </div>
    <!-- end Search box -->
    <!-- Global Navigation -->
    <%@ include file="menuBar-termbrowser.jsp" %>
    <!-- end Global Navigation -->
  </div>
</div>
<!-- end Thesaurus, banner search area -->
<!-- Quick links bar -->
<%@ include file="quickLink.jsp" %>
<!-- end Quick links bar -->
