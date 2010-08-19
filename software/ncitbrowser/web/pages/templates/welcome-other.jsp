<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.HTTPUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.MetadataUtils" %>

<%
  String nci_meta_url = new DataUtils().getNCImURL();
  String vocablary_version_value = version;
  if (vocablary_version_value == null) vocablary_version_value = "";
  
  boolean isMapping = DataUtils.isMapping(scheme, null);
  System.out.println("*** welcome-other.jsp isMapping: " + isMapping);


%>
<div id="message" class="textbody">
  <table border="0" width="700px"><tr>
    <td><div class="texttitle-blue">Welcome</div></td>
    
    <!-- <td><div class="texttitle-blue-rightJust">Version: <%=HTTPUtils.cleanXSS(vocablary_version_value) %></div></td> -->
<%   
    if (isMapping) {
%>
      <td>
      <a href="#"
      onclick="javascript:window.open('<%=request.getContextPath() %>/pages/mapping.jsf?dictionary=<%=HTTPUtils.cleanXSS(scheme)%>&version=<%=vocablary_version_value%>', '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');">
      <img src="<%=basePath%>/images/ViewMapping.gif" alt="View Mapping" /> 
      </a> 
      </td>
      
<%    
    }
%>    
    
    
  </tr></table>
  <hr/>

<%


String _version = request.getParameter("version");
System.out.println("(*****welcome-other.jsp) vocabulary_version: " + _version);
if (vocabulary_version != null) {
	request.setAttribute("version", _version);
}



String html_compatable_description_value = DataUtils.getMetadataValue(scheme, "html_compatable_description");
String version_value = DataUtils.getMetadataValue(scheme, "term_browser_version");
if (version_value == null) version_value = DataUtils.getMetadataValue(scheme, "version");
String source_url_value = DataUtils.getMetadataValue(scheme, "source_url");
String download_url_value = DataUtils.getMetadataValue(scheme, "download_url");
String copyright_statement_value = DataUtils.getMetadataValue(scheme, "copyright");
String cabig_vkc_index_url_value = DataUtils.getMetadataValue(scheme, "cabig_vkc_index_url");

String license_statement_value = null;
String license_display_value = DataUtils.getMetadataValue(scheme, "license_display");
if (license_display_value != null && (license_display_value.compareTo("show") == 0 || license_display_value.compareTo("accept") == 0)) {
    license_statement_value = DataUtils.getMetadataValue(scheme, "license_statement");
}


%>
  <table border="0">
    <tr>
      <td class="textbody" width="388px" valign="top" align="left">
      <%
        if (html_compatable_description_value == null) {
      %>
            <%=HTTPUtils.cleanXSS(scheme)%>
        <%
        } else {
        %>
            <%=html_compatable_description_value%>
        <%
        }
        %>

        <%
        if (source_url_value != null) {
        %>
            <p>
            Source Home Page:
              <a href="<%=source_url_value%>" target="_blank"><%=source_url_value%></a>
            </p>
        <%
        }

        if (download_url_value != null) {
        %>
            <p>
            Download:
              <a href="<%=download_url_value%>" target="_blank"><%=download_url_value%></a>
            </p>
        <%
        }

        if (cabig_vkc_index_url_value != null) {
        %>
            <p>
            caBIG VKC Link:
              <a href="<%=cabig_vkc_index_url_value%>" target="_blank"><%=cabig_vkc_index_url_value%></a>
            </p>
        <%
        }

        if (license_statement_value != null) {
        %>
            <p>
            <%=license_statement_value%>
            </p>
        <%
        }
        %>

      </td>


      <td valign="top" align="right">
        <table border="0">
          <tr valign="top">
            <td width="10px"></td>
            <td width="77px">
              <a href="http://evs.nci.nih.gov/" target="_blank" alt="EVS">
                <img src="<%=basePath%>/images/EVSTile.gif"
                  width="77px" height="38px" alt="EVS" border="0"/>
              </a>
            </td>
            <td width="3px"></td>
            <td class="textbody" align="left" valign="top" width="210px">
              <a href="http://evs.nci.nih.gov/" target="_blank" alt="EVS">
                NCI Enterprise Vocabulary Services</a>:
              Terminology resources and services for NCI and the biomedical community.
            </td>
          </tr>
          <tr valign="top">
            <td width="10px"></td>
            <td>
              <a href="<%=nci_meta_url%>" target="_blank" alt="NCIm">
                <img src="<%=basePath%>/images/NCImTile.gif"
                  width="77" height="38px" alt="NCIm" border="0"/>
              </a>
            </td>
            <td width="3px"></td>
            <td class="textbody" valign="top">
              <a href="<%=nci_meta_url%>" target="_blank" alt="NCIm">
                NCI Metathesaurus</a>:
              Comprehensive database of 3,600,000 terms from 76 terminologies.
            </td>
          </tr>
          <tr valign="top">
            <td width="10px"></td>
            <td>
              <a href="<%= request.getContextPath() %>/start.jsf" alt="NCI Term Browser">
                <img src="<%=basePath%>/images/EVSTermsBrowserTile.gif"
                  width="77" height="38px" alt="Bio Portal" border="0"/>
              </a>
            </td>
            <td width="3px"></td>
            <td class="textbody" valign="top">
              <a href="<%= request.getContextPath() %>/start.jsf" alt="NCI Term Browser">
                NCI Term Browser</a>:
              NCI and other terminologies in an integrated environment.
            </td>
          </tr>
          <tr valign="top">
            <td width="10px"></td>
            <td>
              <a href="http://www.cancer.gov/cancertopics/terminologyresources/"
                  target="_blank" alt="NCI Terminology Resources">
                <img src="<%=basePath%>/images/Cancer_govTile.gif"
                  alt="NCI Terminology Resources" border="0"/>
              </a>
            </td>
            <td width="3px"></td>
            <td class="textbody" valign="top">
              <a href="http://www.cancer.gov/cancertopics/terminologyresources/"
                  target="_blank" alt="NCI Terminology Resources">
                NCI Terminology Resources</a>:
              More information on NCI dictionaries and resources.
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</div>