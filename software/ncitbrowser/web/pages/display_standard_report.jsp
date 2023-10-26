<html lang="en" xmlns:c="http://java.sun.com/jsp/jstl/core"> 
  <body>
  <header class="flex-grow-0">
	<include-html src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown-test.html"></include-html>
  </header>
    <jsp:forward page="/pages/templates/templatePopup.jsp">
      <jsp:param name="content_title" value="NCI Thesaurus: Display Report" />
      <jsp:param name="display_app_logo" value="false" />
      <jsp:param name="content_page" value="/pages/contents/_display_standard_report.jsp" />
    </jsp:forward>
    <script type="text/javascript">_satellite.pageBottom();</script>
  </body>
</html>
