<html lang="en" xmlns:c="http://java.sun.com/jsp/jstl/core"> 
  <body>
  <header class="flex-grow-0">
	<div style='text-align: left'>
	<include-html src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html"></include-html>
	</div>
  </header>
    <jsp:forward page="/pages/templates/template.jsp">
      <jsp:param name="content_title" value="NCI Thesaurus: Standard Reports" />
      <jsp:param name="content_page" value="/pages/contents/_list_standard_reports.jsp" />
    </jsp:forward>
    <script type="text/javascript">_satellite.pageBottom();</script>
  </body>
</html>
