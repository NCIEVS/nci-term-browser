<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=windows-1252"%>

<%@ page import="java.util.Vector"%>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.common.Constants" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>

<%
  String basePath = request.getContextPath();
  String ICON_LEAF = HierarchyUtils.getLeafIcon(basePath);
  String ICON_EXPAND = HierarchyUtils.getExpandIcon(basePath);
  String ICON_COLLAPSE = HierarchyUtils.getCollapseIcon(basePath);
  
  TreeItem root = HierarchyUtils.getSampleTree();
  StringBuffer buffer = new StringBuffer();
  HierarchyUtils.getHtml(basePath, buffer, root, "", "dictionary", "version");
  String tree = buffer.toString();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <head>
  <title>Vocabulary Hierarchy</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/fonts.css" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/grids.css" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/code.css" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/tree.css" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>

  <script language="JavaScript">
      function toggle(node)
      {
        // Unfold the branch if it isn't visible
        if (node.nextSibling.style.display == 'none')
        {
          // Change the image (if there is an image)
          if (node.children.length > 0)
          {
            if (node.children.item(0).tagName == "IMG")
            {
              node.children.item(0).src = "<%=ICON_COLLAPSE%>";
            }
          }

          node.nextSibling.style.display = '';
        }

        // Collapse the branch if it IS visible
        else
        {
          // Change the image (if there is an image)
          if (node.children.length > 0)
          {
            if (node.children.item(0).tagName == "IMG")
            {
              node.children.item(0).src = "<%=ICON_EXPAND%>";
            }
          }

          node.nextSibling.style.display = 'none';
        }
      }
    </script>
  </head>
  
  <f:view>
    <!-- Begin Skip Top Navigation -->
      <a href="#evs-content" class="hideLink" accesskey="1" title="Skip repetitive navigation links">skip navigation links</A>
    <!-- End Skip Top Navigation --> 
    <div id="popupContainer">
      <!-- nci popup banner -->
      <div class="ncipopupbanner">
        <a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute"><img src="<%=basePath%>/images/nci-banner-1.gif" width="440" height="39" border="0" alt="National Cancer Institute" /></a>
        <a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute"><img src="<%=basePath%>/images/spacer.gif" width="48" height="39" border="0" alt="National Cancer Institute" class="print-header" /></a>
      </div>
      <!-- end nci popup banner -->
      <div id="popupMainArea">
        <a name="evs-content" id="evs-content"></a>
        <table class="evsLogoBg" cellspacing="0" cellpadding="0" border="0">
        <tr>
          <td valign="top">
            <a href="http://evs.nci.nih.gov/" target="_blank" alt="Enterprise Vocabulary Services">
              <img src="<%=basePath%>/images/evs-popup-logo.gif" width="213" height="26" alt="EVS: Enterprise Vocabulary Services" title="EVS: Enterprise Vocabulary Services" border="0" />
            </a>
          </td>
          <td valign="top"><div id="closeWindow"><a href="javascript:window.close();"><img src="<%=basePath%>/images/thesaurus_close_icon.gif" width="10" height="10" border="0" alt="Close Window" />&nbsp;CLOSE WINDOW</a></div></td>
        </tr>
        </table>

<%
JSPUtils.JSPHeaderInfoMore info = new JSPUtils.JSPHeaderInfoMore(request);
String hierarchy_dictionary = info.dictionary;
String hierarchy_version = info.version;

//System.out.println("hierarchy.jsp hierarchy_version: " + hierarchy_version);

String hierarchy_schema = HTTPUtils.cleanXSS((String) request.getParameter("schema"));
if (hierarchy_dictionary != null && hierarchy_schema == null) hierarchy_schema = hierarchy_dictionary;
hierarchy_schema = DataUtils.getFormalName(hierarchy_schema);

String localName = DataUtils.getLocalName(hierarchy_schema);
String formalName = DataUtils.getFormalName(localName);
String term_browser_version = DataUtils.getMetadataValue(formalName, hierarchy_version, "term_browser_version");
String display_name = DataUtils.getMetadataValue(formalName, hierarchy_version, "display_name");


if (display_name == null || display_name.compareTo("null") == 0) {
   display_name = DataUtils.getLocalName(hierarchy_schema); 
}


String release_date = DataUtils.getVersionReleaseDate(hierarchy_schema, hierarchy_version);
boolean display_release_date = true;
if (release_date == null || release_date.compareTo("") == 0) {
    display_release_date = false;
}
 
     
     

if (hierarchy_schema.compareTo("NCI Thesaurus") == 0) {
%>
    <div>
      <img src="<%=basePath%>/images/thesaurus_popup_banner.gif" width="612" height="56" alt="NCI Thesaurus" title="" border="0" />
      
<%      
     if (display_release_date) {
%>   
             <span class="texttitle-blue-rightjust-2"><%=HTTPUtils.cleanXSS(term_browser_version)%> (Release date: <%=release_date%>)</span>
<%
     } else {
%>   
         <span class="texttitle-blue-rightjust-2"><%=HTTPUtils.cleanXSS(term_browser_version)%></span>
<%
     }      
%>      

    </div>
<%
} else {
     String hierarchy_shortName = DataUtils.getLocalName(hierarchy_schema);
%>
    <div>
      <img src="<%=basePath%>/images/other_popup_banner.gif" width="612" height="56" alt="NCI Thesaurus" title="" border="0" />
      <div class="vocabularynamepopupshort"><%=HTTPUtils.cleanXSS(display_name)%>
      
<%      
     if (display_release_date) {
%>   
             <span class="texttitle-blue-rightjust"><%=HTTPUtils.cleanXSS(term_browser_version)%> (Release date: <%=release_date%>)</span>
<%
     } else {
%>   
         <span class="texttitle-blue-rightjust"><%=HTTPUtils.cleanXSS(term_browser_version)%></span>
<%
     }      
%>         

      </div>
    </div>
<%
}
%>

        <div id="popupContentArea">
          <table width="580px" cellpadding="3" cellspacing="0" border="0">
            <tr class="textbody">
              <td class="pageTitle" align="left">
                <%=HTTPUtils.cleanXSS(display_name)%> Hierarchy
              </td>
              <td class="pageTitle" align="right">
                <font size="1" color="red" align="right">
                  <a href="javascript:printPage()"><img src="<%= request.getContextPath() %>/images/printer.bmp" border="0" alt="Send to Printer"><i>Send to Printer</i></a>
                </font>
              </td>
            </tr>
            
            <tr>
              <td>
                <%= tree %>
              </td>
            </tr>
          </table>

          <form id="pg_form">
            <%
              String ontology_node_id = HTTPUtils.cleanXSS((String) request.getParameter("code"));

              //String ontology_display_name = hierarchy_dictionary;
        //String schema = hierarchy_schema;
        //String version = hierarchy_version;

String schema = HTTPUtils.cleanXSS((String) request.getParameter("schema"));
String ontology_version = HTTPUtils.cleanXSS((String) request.getParameter("version"));

//System.out.println("hierarchy.jsp ontology_version: " + ontology_version);


String ontology_display_name = HTTPUtils.cleanXSS((String) request.getParameter("schema"));
if (ontology_display_name == null) {
    ontology_display_name = HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
}

            %>
            <input type="hidden" id="ontology_node_id" name="ontology_node_id" value="<%=HTTPUtils.cleanXSS(ontology_node_id)%>" />
            <input type="hidden" id="ontology_display_name" name="ontology_display_name" value="<%=HTTPUtils.cleanXSS(ontology_display_name)%>" />
            <input type="hidden" id="schema" name="schema" value="<%=HTTPUtils.cleanXSS(schema)%>" />
            <input type="hidden" id="ontology_version" name="ontology_version" value="<%=HTTPUtils.cleanXSS(ontology_version)%>" />

          </form>
          <!-- End of Tree control content -->
        </div>
      </div>
    </div>
  </f:view>
</body>  
</html>