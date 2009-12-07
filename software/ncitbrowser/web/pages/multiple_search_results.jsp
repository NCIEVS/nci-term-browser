<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.LexGrid.concepts.Concept" %>
<%@ page import="gov.nih.nci.evs.browser.common.Constants" %>
<%@ page import="gov.nih.nci.evs.browser.utils.HTTPUtils" %>

<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.bean.IteratorBean" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
  <head>
    <title>NCI Term Browser</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
  </head>
  <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/wz_tooltip.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_centerwindow.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/tip_followscroll.js"></script>
  <f:view>
    <%@ include file="/pages/templates/header.jsp" %>
    <div class="center-page">
      <%@ include file="/pages/templates/sub-header.jsp" %>
      <!-- Main box -->
      <div id="main-area">
 <%
     //String match_text = gov.nih.nci.evs.browser.utils.HTTPUtils
     //  .cleanXSS((String) request.getSession().getAttribute("matchText"));
     String match_text = (String) request.getSession().getAttribute("matchText"); 
    
     if (match_text == null) match_text = "";
    
     String algorithm = (String) request.getSession().getAttribute("algorithm");
    
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
      <!-- Thesaurus, banner search area -->
      <form class="search-form-main-area"> 
      <div class="bannerarea">
        <div class="banner"><a href="<%=basePath%>/start.jsf"><img src="<%=basePath%>/images/evs_termsbrowser_logo.gif" width="383" height="97" alt="Thesaurus Browser Logo" border="0"/></a></div>
        <div class="search-globalnav">
          <!-- Search box -->
          <div class="searchbox-top"><img src="<%=basePath%>/images/searchbox-top.gif" width="352" height="2" alt="SearchBox Top" /></div>
            <!-- form name="searchTerm" method="post" class="search-form-main-area" --> 
            <div class="searchbox">
              <div class="search-form">  
              <input CLASS="searchbox-input" name="matchText" type="text" value="<%=match_text%>" />
              <h:commandButton
                id="search"
                value="Search"
                action="#{userSessionBean.multipleSearchAction}"
                image="#{facesContext.externalContext.requestContextPath}/images/search.gif"
                alt="Search">
              </h:commandButton>
              <h:outputLink
                value="#{facesContext.externalContext.requestContextPath}/pages/help.jsf#searchhelp">
                <h:graphicImage value="/images/search-help.gif"
                style="border-width:0;" />
              </h:outputLink>
              <table border="0" cellspacing="0" cellpadding="0">
                <tr valign="top" align="left">
                  <td align="left" class="textbody">
                    <input type="radio" name="algorithm" value="exactMatch" alt="Exact Match" <%=check_e%>>Exact Match&nbsp;
                    <input type="radio" name="algorithm" value="startsWith" alt="Begins With" <%=check_s%>>Begins With&nbsp;
                    <input type="radio" name="algorithm" value="contains" alt="Containts" <%=check_c%>>Contains&nbsp;
                  </td>
                </tr>
                <tr align="left">
                  <td height="1px" bgcolor="#2F2F5F"></td>
                </tr>
                <tr valign="top" align="left">
                  <td align="left" class="textbody">
                    <input type="radio" name="searchTarget" value="names" alt="Names" <%=check_n%>>Name/Code&nbsp;
                    <input type="radio" name="searchTarget" value="properties" alt="Properties" <%=check_p%>>Property&nbsp;
                    <input type="radio" name="searchTarget" value="relationships" alt="Relationships" <%=check_r%>>Relationship&nbsp;
                  </td>
                </tr>
              </table>
              </div> <!--  end search-form -->
            </div> <!-- end searchbox -->            
          <div class="searchbox-bottom"><img src="<%=basePath%>/images/searchbox-bottom.gif" width="352" height="2" alt="SearchBox Bottom" /></div>
          <!-- end Search box -->
          <!-- Global Navigation -->
          <%@ include file="/pages/templates/menuBar-termbrowser.jsp" %>
          <!-- end Global Navigation -->
      </div> <!-- end search-globalnav -->
    </div> <!-- end bannerarea -->
    </form>
    <!-- end Thesaurus, banner search area -->
    <!-- Quick links bar -->
    <%@ include file="/pages/templates/quickLink.jsp" %>
    <!-- end Quick links bar -->
    <!-- Page content -->
    <div class="pagecontent">
      <%
        request.getSession().removeAttribute("dictionary");

        HashMap hmap = DataUtils.getNamespaceId2CodingSchemeFormalNameMapping();

        IteratorBean iteratorBean = (IteratorBean) FacesContext.getCurrentInstance().getExternalContext()
              .getSessionMap().get("iteratorBean");

        String matchText = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("matchText"));
        String match_size = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("match_size"));
        String page_string = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("page_string"));
        Boolean new_search = (Boolean) request.getSession().getAttribute("new_search");
        
        String page_number = HTTPUtils.cleanXSS((String) request.getParameter("page_number"));
        String selectedResultsPerPage = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("selectedResultsPerPage"));
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
        if (selectedResultsPerPage != null)
        {
            page_size = Integer.parseInt(selectedResultsPerPage);
        }
        int iend = page_num * page_size;
        int istart = iend - page_size;
        int size = iteratorBean.getSize();

        if (iend > size) iend = size;
        int num_pages = size / page_size;
        if (num_pages * page_size < size) num_pages++;
        String istart_str = Integer.toString(istart+1);
        String iend_str = Integer.toString(iend);
        String prev_page_num_str = Integer.toString(prev_page_num);
        String next_page_num_str = Integer.toString(next_page_num);
      %>
        <table width="700px">
          <tr>
            <td>
              <table>
                <tr>
                  <td class="texttitle-blue">Result for:</td>
                  <td class="texttitle-gray"><%=matchText%></td>
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
              <b>Results <%=istart_str%>-<%=iend_str%> of&nbsp;<%=match_size%> for: <%=matchText%></b>&nbsp;<%=contains_warning_msg%>
             <%
              } else {
              %>
              Results <%=istart_str%>-<%=iend_str%> of&nbsp;<%=match_size%> for: <%=matchText%></b>
              <%
              }
              String ontologiesToSearchOnStr = (String) request.getSession().getAttribute("ontologiesToSearchOn");
              String tooltip_str = "";
              if (ontologiesToSearchOnStr != null) {
                Vector ontologies_to_search_on = DataUtils.parseData(ontologiesToSearchOnStr);
                for (int k=0; k<ontologies_to_search_on.size(); k++) {
                  String s = (String) ontologies_to_search_on.elementAt(k);
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
                  for (int i=0; i<list.size(); i++) {
                      ResolvedConceptReference rcr = (ResolvedConceptReference) list.get(i);

                      String code = rcr.getConceptCode();
                      String name = rcr.getEntityDescription().getContent();

                      String vocabulary_name = (String) hmap.get(rcr.getCodingSchemeName());
                      String short_vocabulary_name = null;
                      //String short_vocabulary_name = DataUtils.getLocalName(vocabulary_name);
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
                      Concept con = DataUtils.getConceptByCode(vocabulary_name, null, null, code);
                      if (con != null) {
                      String con_status = con.getStatus();

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
                %>
              </table>
            </td>
          </tr>
        </table>
        <%@ include file="/pages/templates/pagination-termbrowser.jsp" %>
        <%@ include file="/pages/templates/nciFooter.html" %>
      </div> <!-- end Page content -->
    </div> <!-- end main-area -->
    <div class="mainbox-bottom"><img src="images/mainbox-bottom.gif" width="745" height="5" alt="Mainbox Bottom" /></div>
    <!-- end Main box -->
  </div> <!-- end center-page -->
</f:view>
</body>
</html>