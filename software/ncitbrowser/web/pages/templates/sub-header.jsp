<!-- EVS Logo -->
<div>
  <img
      src="<%=basePath%>/images/evs-logo-swapped.gif"
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
        href="<%= request.getContextPath() %>/start.jsf"
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

 
<%@ include file="/pages/templates/navigationTabs.jsp" %>
<div class="mainbox-top">
  <img src="<%=basePath%>/images/mainbox-top.gif" width="945" height="5" alt="" />
</div>
<!-- end EVS Logo -->
