<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*"%>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.bean.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.properties.*" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference" %>
<%@ page import="org.apache.log4j.*" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>
  <title>NCI Thesaurus</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
</head>
<body>
  <script type="text/javascript"
    src="<%=request.getContextPath()%>/js/wz_tooltip.js"></script>
  <script type="text/javascript"
    src="<%=request.getContextPath()%>/js/tip_centerwindow.js"></script>
  <script type="text/javascript"
    src="<%=request.getContextPath()%>/js/tip_followscroll.js"></script>


  <%!
    private static Logger _logger = Utils.getJspLogger("resolved_value_set_search_results.jsp");
  %>
  <f:view>
    <!-- Begin Skip Top Navigation -->
      <a href="#evs-content" class="hideLink" accesskey="1" title="Skip repetitive navigation links">skip navigation links</A>
    <!-- End Skip Top Navigation -->  
    <%@ include file="/pages/templates/header.jsp" %>
    <div class="center-page">
      <%@ include file="/pages/templates/sub-header.jsp" %>
      <!-- Main box -->
      <div id="main-area">
        <%@ include file="/pages/templates/content-header-resolvedvalueset.jsp" %>
        
<%

String valueSetSearch_requestContextPath = request.getContextPath();

System.out.println("valueSetSearch_requestContextPath: " + valueSetSearch_requestContextPath);

String message = (String) request.getSession().getAttribute("message");  


String vsd_uri = (String) request.getSession().getAttribute("selectedvalueset");

Vector coding_scheme_ref_vec = DataUtils.getCodingSchemesInValueSetDefinition(vsd_uri);
String checked = "";

%>
        <div class="pagecontent">
          <a name="evs-content" id="evs-content"></a>
          
          
          <%@ include file="/pages/templates/navigationTabs.jsp"%>
          
          
      <%

String resultsPerPage = request.getParameter("resultsPerPage");
if (resultsPerPage == null) {
    resultsPerPage = "50";
}

		String selectedResultsPerPage = resultsPerPage;
        request.getSession().removeAttribute("dictionary");
        HashMap hmap = DataUtils.getNamespaceId2CodingSchemeFormalNameMapping();

        IteratorBean iteratorBean = (IteratorBean) FacesContext.getCurrentInstance().getExternalContext()
              .getSessionMap().get("iteratorBean");

        String matchText = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("matchText"));
        //String match_size = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("match_size"));
        String page_string = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("page_string"));
        Boolean new_search = (Boolean) request.getSession().getAttribute("new_search");

        String page_number = HTTPUtils.cleanXSS((String) request.getParameter("page_number"));
        //String selectedResultsPerPage = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("selectedResultsPerPage"));
        String contains_warning_msg = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("contains_warning_msg"));

        if (page_number != null && new_search == Boolean.FALSE)
        {
            page_string = page_number;
        }
        request.getSession().setAttribute("new_search", Boolean.FALSE);

        int page_num = Integer.parseInt(page_string);
        int next_page_num = page_num + 1;
        int prev_page_num = page_num - 1;
        int page_size = 50;
        if (selectedResultsPerPage != null && selectedResultsPerPage.compareTo("") != 0)
        {
            page_size = Integer.parseInt(selectedResultsPerPage);
        }

          int iend = page_num * page_size;
          int istart = iend - page_size;
          iend = iend-1;
          int size = iteratorBean.getSize();
          String match_size = new Integer(size).toString();

          if (iend > size-1) iend = size-1;
          int num_pages = size / page_size;
          if (num_pages * page_size < size) num_pages++;
          String istart_str = Integer.toString(istart+1);
          String iend_str = Integer.toString(iend+1);
          String prev_page_num_str = Integer.toString(prev_page_num);
          String next_page_num_str = Integer.toString(next_page_num);

      %>
        <table width="700px">
          <tr>
            <td>
              <table>
                <tr>
                  <td class="texttitle-blue">Result for:</td>
                  <td class="texttitle-gray"><%=match_text%></td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td><hr></td>
          </tr>
          <tr>
            <td class="dataTableHeader">
            <%
              if (contains_warning_msg != null) {
             %>
              <b>Results <%=istart_str%>-<%=iend_str%> of&nbsp;<%=match_size%> for: <%=match_text%></b>&nbsp;<%=contains_warning_msg%>
             <%
              } else {
              %>
              Results <%=istart_str%>-<%=iend_str%> of&nbsp;<%=match_size%> for: <%=match_text%></b>
              <%
              }
              String ontologiesToSearchOnStr = (String) request.getSession().getAttribute("ontologiesToSearchOn");
              String tooltip_str = "";

              HashMap display_name_hmap = null;
              Vector display_name_vec = null;
              display_name_hmap = (HashMap) request.getSession().getAttribute("display_name_hmap");
              display_name_vec = (Vector) request.getSession().getAttribute("display_name_vec");

              if (ontologiesToSearchOnStr != null) {

                Vector ontologies_to_search_on = DataUtils.parseData(ontologiesToSearchOnStr);
                for (int k=0; k<ontologies_to_search_on.size(); k++) {
                  String s = (String) ontologies_to_search_on.elementAt(k);

                  String t1 = DataUtils.key2CodingSchemeName(s);
                  String term_browser_version = DataUtils.getMetadataValue(t1, "term_browser_version");

                  if (term_browser_version == null)
                     term_browser_version = DataUtils.key2CodingSchemeVersion(s);
                  for (int i=0; i<display_name_vec.size(); i++) {
                      OntologyInfo info = (OntologyInfo) display_name_vec.elementAt(i);
                      String nm = info.getDisplayName();
                      String val = (String) display_name_hmap.get(nm);
                      if (val.compareTo(s) == 0) {
                          s = nm;
                          break;
                      }
                  }
                  s = s + " (" + term_browser_version + ")";
                  tooltip_str = tooltip_str + s + "<br/>";
                }
              }
              HashMap name_hmap = new HashMap();
              %>
              from <a href="#" onmouseover="Tip('<%=tooltip_str%>')" onmouseout="UnTip()">selected vocabularies</a>.
            </td>
          </tr>

          <tr>
            <td class="textbody">
              <table class="dataTable" summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
                <th class="dataTableHeader" scope="col" align="left">Concept</th>
                <th class="dataTableHeader" scope="col" align="left">Vocabulary</th>
                <%
                  List list = iteratorBean.getData(istart, iend);

    boolean timeout = iteratorBean.getTimeout();
    String message = iteratorBean.getMessage();

    if (message != null) {
      %>
      <p class="textbodyred"><%=message%></p>
      <%
    message = null;
    iteratorBean.setMessage(message);

    } else if (timeout) {
      %>
      <p class="textbodyred">WARNING: System times out. Please advance fewer pages at one time.</p>
      <%
    } else {

    for (int i=0; i<list.size(); i++) {
        ResolvedConceptReference rcr = (ResolvedConceptReference) list.get(i);
              if (rcr != null && rcr.getConceptCode() != null && rcr.getEntityDescription() != null) {
        String code = rcr.getConceptCode();
        String name = rcr.getEntityDescription().getContent();



        String vocabulary_name = (String) DataUtils.getFormalName(rcr.getCodingSchemeName());
        if (vocabulary_name == null) {
      vocabulary_name = (String) hmap.get(rcr.getCodingSchemeName());
        }

        String short_vocabulary_name = null;
        if (name_hmap.containsKey(vocabulary_name)) {
      short_vocabulary_name = (String) name_hmap.get(vocabulary_name);
        } else {
      short_vocabulary_name = DataUtils.getMetadataValue(vocabulary_name, "display_name");
      if (short_vocabulary_name == null || short_vocabulary_name.compareTo("null") == 0) {
          short_vocabulary_name = DataUtils.getLocalName(vocabulary_name);
      }
      name_hmap.put(vocabulary_name, short_vocabulary_name);
        }

            if (code == null || code.indexOf("@") != -1) {
            if (i % 2 == 0) {
            %>
              <tr class="dataRowDark">
            <%
                } else {
            %>
              <tr class="dataRowLight">
            <%
                }
                %>
              <td class="dataCellText">
                 <%=name%>
              </td>
              <td class="dataCellText">
                 <%=short_vocabulary_name%>
              </td>
            </tr>
            <%
            } else {

            String con_status = DataUtils.getConceptStatus(vocabulary_name, null, null, code);

            if (con_status != null) {
          con_status = con_status.replaceAll("_", " ");
            }

            String vocabulary_name_encoded = null;
            if (vocabulary_name != null) vocabulary_name_encoded = vocabulary_name.replace(" ", "%20");

            if (i % 2 == 0) {
        %>
          <tr class="dataRowDark">
        <%
            } else {
        %>
          <tr class="dataRowLight">
        <%
            }
            %>
          <%
          if (con_status == null) {
          %>

          <td class="dataCellText">
          <%
          if (vocabulary_name.compareToIgnoreCase("NCI Thesaurus") == 0) {
          %>
               <a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=vocabulary_name_encoded%>&code=<%=code%>" ><%=name%></a>
          <%
          } else if (vocabulary_name.compareToIgnoreCase("NCI MetaThesaurus") == 0) {
               String meta_url = "http://ncim.nci.nih.gov/ncimbrowser/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=" + code;
          %>
               <a href="javascript:openQuickLinkSite('<%=meta_url%>')"><%=name%></a>
          <%
          } else {
          %>
               <a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=vocabulary_name_encoded%>&code=<%=code%>" ><%=name%></a>
          <%
          }
          %>
          </td>
          <td class="dataCellText">
            <%=short_vocabulary_name%>
          </td>


          <%
          } else {
          %>

          <td class="dataCellText">
          <%
          if (vocabulary_name.compareToIgnoreCase("NCI Thesaurus") == 0) {
          %>
               <a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=vocabulary_name_encoded%>&code=<%=code%>" ><%=name%></a>&nbsp;(<%=con_status%>)
          <%
          } else if (vocabulary_name.compareToIgnoreCase("NCI MetaThesaurus") == 0) {
               String meta_url = "http://ncim.nci.nih.gov/ncimbrowser/ConceptReport.jsp?dictionary=NCI%20MetaThesaurus&code=" + code;
          %>
               <a href="javascript:openQuickLinkSite('<%=meta_url%>')"><%=name%></a>&nbsp;(<%=con_status%>)
          <%
          } else {
          %>
               <a href="<%=request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=vocabulary_name_encoded%>&code=<%=code%>" ><%=name%></a>&nbsp;(<%=con_status%>)
          <%
          }
          %>
          </td>
          <td class="dataCellText">
            <%=short_vocabulary_name%>
          </td>

          <%
          }
          %>


        </tr>
            <%
          }
        }
                      }
                  }
                %>
              </table>
            </td>
          </tr>
        </table>
        <%@ include file="/pages/templates/pagination-termbrowser.jsp" %>
        
        
        
        <%@ include file="/pages/templates/nciFooter.jsp" %>
      </div>
      <!-- end Page content -->
    </div>
    
    
    
    <div class="mainbox-bottom"><img src="<%=basePath%>/images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
    <!-- end Main box -->
  </div>
</f:view>
</body>
</html>