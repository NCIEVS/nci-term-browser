<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.util.Vector" %>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.common.Constants" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>

<% String basePath = request.getContextPath(); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html lang="en" xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <script
        src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js">
    </script>
<!-- Google tag (gtag.js) -->
<script async src="https://www.googletagmanager.com/gtag/js?id=G-21QRTJ0WQS"></script>
<script>
	window.dataLayer = window.dataLayer || [];
	function gtag(){dataLayer.push(arguments);}
	gtag('js', new Date());
	gtag('config', 'G-21QRTJ0WQS');
</script>
    <script src="https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js"></script>
    <title>Vocabulary Hierarchy</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>


<style>
ul {text-align: left;}
li {text-align: left;}
div {text-align: left;}
</style>


    <script type="text/javascript">

        var currOpener = opener;
	function load(url,target) {
		if (target != '')
			target.window.location.href = url;
		else
			window.location.href = url;
	}
      
	function onClickTreeNode(ontology_node_id) {
		var ontology_display_name = document.forms["pg_form"].ontology_display_name.value;
		var ontology_version = document.forms["pg_form"].ontology_version.value;
		var ontology_node_ns = document.forms["pg_form"].ontology_node_ns.value;

		load('/ncitbrowser/pages/concept_details.jsf?dictionary='+ ontology_display_name
		+ '&version='+ ontology_version
		+ '&code=' + ontology_node_id
		+ '&ns=' + ontology_node_ns, currOpener);
	}  
	
	function show_hide_div(div_id) {
		var img_id = "IMG_" + div_id.substring(4, div_id.length);
		var img_obj = document.getElementById(img_id);
		if (img_obj.getAttribute("src").indexOf("minus") != -1) {
			document.getElementById(div_id).style.display = "none";
		} else if (img_obj.getAttribute("src").indexOf("plus") != -1) {
			document.getElementById(div_id).style.display = "block";
		}
		changeImage(img_id);
	}
	
	function show_hide(div_id) {
		var curr_node = document.getElementById(div_id);
		var code = curr_node.getAttribute("code");
		var img_id = "IMG_" + div_id.substring(4, div_id.length);
		var img_obj = document.getElementById(img_id);
		
		if (img_obj.getAttribute("src").indexOf("plus") != -1) {
		        expand_node(div_id, code);
		        changeImage(img_id); 
		} else if (img_obj.getAttribute("src").indexOf("minus") != -1) {
		     var i = 1;
		     var next_div_id = div_id + "_" + i;
		     var next_img_id = img_id + "_" + i;
		     var e = document.getElementById(next_div_id);
		     while (e != null) {
		             document.getElementById(next_img_id).remove();
		             document.getElementById(next_div_id).remove();
			     i = i+1;
			     next_div_id = div_id + "_" + i;
			     next_img_id = img_id + "_" + i;
			     e = document.getElementById(next_div_id);
		     }
		     changeImage(img_id); 
		}
	}

	function changeImage(img_id) {
		var img_obj = document.getElementById(img_id);
		if (img_obj.getAttribute("src").indexOf("minus") != -1) {
			var s = img_obj.getAttribute("src");
			s = s.replace("minus", "plus");
			img_obj.setAttribute("src", s);
		} else if (img_obj.getAttribute("src").indexOf("plus") != -1) {
			var s = img_obj.getAttribute("src");
			s = s.replace("plus", "minus");
			img_obj.setAttribute("src", s);
		}
	} 

	function search(ontology_node_id, ontology_node_ns, ontology_display_name) {
		var ajax = new XMLHttpRequest();
		ajax.open("GET", '/ncitbrowser/ajax?action=search_concept_in_tree&ontology_display_name=' + ontology_display_name + '&ontology_node_ns=' + ontology_node_ns + '&ontology_node_id=' + ontology_node_id, true);
		ajax.send(); 
		ajax.onreadystatechange = function() {
			if (ajax.readyState == 4 && ajax.status == 200) {
				document.getElementById("tree").innerHTML = this.responseText;
			}
		}
	}

	function initTree() {
		var ontology_node_id = document.forms["pg_form"].ontology_node_id.value;
		var ontology_display_name = document.forms["pg_form"].ontology_display_name.value;
		var ontology_node_ns = document.forms["pg_form"].ontology_node_ns.value;
		if (ontology_node_id == null || ontology_node_id == "null")
		{
                   var content = "<center><br></br><img src='/ncitbrowser/images/loading.gif' alt='Loading'/>" + 
                        "<p>Loading hierarchy. Please wait...</p><center>";
		        document.getElementById("tree").innerHTML = content;
			init(ontology_node_id, ontology_display_name);
			
		}
		else
		{
                   var content = "<center><br></br><img src='/ncitbrowser/images/loading.gif' alt='Loading'/>" + 
                        "<p>Searching concept in hierarchy. Please wait...</p><center>";
		        document.getElementById("tree").innerHTML = content;
			search(ontology_node_id, ontology_node_ns, ontology_display_name);
		}
	}


    
	function init(ontology_node_id, ontology_display_name) {
		var ajax = new XMLHttpRequest();
		ajax.open("GET", '/ncitbrowser/ajax?action=build_hierarchy&ontology_node_id=' +ontology_node_id+'&ontology_display_name='+ontology_display_name, true);
		ajax.send();
		ajax.onreadystatechange = function() {
			if (ajax.readyState == 4 && ajax.status == 200) {
				document.getElementById("tree").innerHTML = this.responseText;
			}
		}
	}

	function expand_node(div_id, code) {
                var ontology_display_name = document.forms["pg_form"].ontology_display_name.value;
		var ajax = new XMLHttpRequest();
		ajax.open("GET", '/ncitbrowser/ajax?action=expand_hierarchy&ontology_node_id=' +code +'&ontology_display_name='+ontology_display_name+'&id=' + div_id, true);
		ajax.send();
		ajax.onreadystatechange = function() {
			if (ajax.readyState == 4 && ajax.status == 200) {
			     var content = document.getElementById(div_id).innerHTML;
			     document.getElementById(div_id).innerHTML = content + this.responseText;
			}
		}
	}


	</script>
          </head>
          <body onload="javascript:initTree()">
  <header class="flex-grow-0">
	<include-html src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html"></include-html>
  </header>
            <f:view>
              <!-- Begin Skip Top Navigation -->
              <a href="#evs-content" class="skip-main" accesskey="1" title="Skip repetitive navigation links">
                skip navigation links</A>
              <!-- End Skip Top Navigation -->
              <div id="popupContainer">
                <!-- nci popup banner -->
                <div class="ncipopupbanner">
                  <a href="https://www.cancer.gov" target="_blank" rel="noopener" alt="National Cancer Institute">
                    <img
                        src="<%=basePath%>/images/banner-red.png"
                        width="680"
                        height="39"
                        border="0"
                        alt="National Cancer Institute"
                    />
                  </a>
                  <a href="https://www.cancer.gov" target="_blank" rel="noopener" alt="National Cancer Institute">
                    <img
                        src="<%=basePath%>/images/spacer.gif"
                        width="60"
                        height="39"
                        border="0"
                        alt="National Cancer Institute"
                        class="print-header"
                    />
                  </a>
                </div>
                <!-- end nci popup banner -->
                <div id="popupMainArea">
                  <a name="evs-content" id="evs-content" tabindex="0"></a>
                  <table class="evsLogoBg" cellspacing="0" cellpadding="0" border="0" role='presentation'>
                    <tr>
                      <td valign="top">
                        <a href="https://evs.nci.nih.gov/" target="_blank" rel="noopener" alt="Enterprise Vocabulary Services">
                          <img
                              src="<%=basePath%>/images/evs-popup-logo.gif"
                              width="213"
                              height="26"
                              alt="EVS: Enterprise Vocabulary Services"
                              title="EVS: Enterprise Vocabulary Services"
                              border="0"
                          />
                        </a>
                      </td>
                      <td valign="top">
                        <div id="closeWindow">
                          <a href="javascript:window.close();">
                            <img
                                src="<%=basePath%>/images/thesaurus_close_icon.gif"
                                width="10"
                                height="10"
                                border="0"
                                alt="Close Window"
                            />
                            &nbsp;CLOSE WINDOW</a>
                        </div>
                      </td>
                    </tr>
                  </table>

                  <%
                  JSPUtils.JSPHeaderInfoMore info = new JSPUtils.JSPHeaderInfoMore(request);
                  
                      
	      String ontology_display_name = null;
	      String ontology_node_id = null;
	      String ontology_node_ns= null;
	      String ontology_version = null;

	      String vih = HTTPUtils.cleanXSS((String) request.getParameter("vih"));
	      if (vih != null) {
		      ontology_display_name = HTTPUtils.cleanXSS((String) request.getParameter("ontology_display_name"));
		      ontology_node_id = HTTPUtils.cleanXSS((String) request.getParameter("code"));
		      ontology_node_ns = HTTPUtils.cleanXSS((String) request.getParameter("ns"));
		      ontology_version = HTTPUtils.cleanXSS((String) request.getParameter("version"));
	      } else {

		      ontology_display_name = info.dictionary;
		      ontology_node_id = HTTPUtils.cleanXSS((String) request.getParameter("code"));
		      ontology_node_ns = HTTPUtils.cleanXSS((String) request.getParameter("ns"));
		      ontology_version = info.version;
	      }

	      if (ontology_node_ns == null) {
		   ontology_node_ns = HTTPUtils.cleanXSS((String) request.getParameter("ontology_node_ns"));
	      }

	      String schema = ontology_display_name;                  
                  
                  
                  String hierarchy_dictionary = ontology_display_name;
                  String hierarchy_version = ontology_version;

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

                  if (DataUtils.isNCIT(hierarchy_schema)) {
                    %>
                    <div>
                      <img
                          src="<%=basePath%>/images/thesaurus_popup_banner.gif"
                          width="612"
                          height="56"
                          alt="NCI Thesaurus"
                          title=""
                          border="0"
                      />

                      <% if (display_release_date) { %>
                        <span class="texttitle-blue-rightjust-2">
                          <%= HTTPUtils.cleanXSS(term_browser_version) %> (Release date:<%= release_date %>)
                        </span>
                      <% } else { %>
                        <span class="texttitle-blue-rightjust-2"><%= HTTPUtils.cleanXSS(term_browser_version) %></span>
                      <% } %>

                    </div>
                    <%
                  } else {
                    String hierarchy_shortName = DataUtils.getLocalName(hierarchy_schema);
                    %>
                    <div>
                      <img
                          src="<%=basePath%>/images/other_popup_banner.gif"
                          width="612"
                          height="56"
                          alt="NCI Thesaurus"
                          title=""
                          border="0"
                      />
                      <div class="vocabularynamepopupshort">
                        <%= HTTPUtils.cleanXSS(display_name) %>

                        <% if (display_release_date) { %>
                          <span class="texttitle-blue-rightjust">
                            <%= HTTPUtils.cleanXSS(term_browser_version) %> (Release date:<%= release_date %>)
                          </span>
                        <% } else { %>
                          <span class="texttitle-blue-rightjust"><%= HTTPUtils.cleanXSS(term_browser_version) %></span>
                        <% } %>

                      </div>
                    </div>
                  <% } %>

                  <div id="popupContentArea">
                    <table width="580px" cellpadding="3" cellspacing="0" border="0" role='presentation'>
                      <tr class="textbody">
                        <td class="pageTitle" align="left"><%= HTTPUtils.cleanXSS(display_name) %>&nbsp;Hierarchy</td>
                        <td class="pageTitle" align="right">
                          <font size="1" color="red" align="right">
                            <a href="javascript:printPage()">
                              <img
                                  src="<%= request.getContextPath() %>/images/printer.bmp"
                                  border="0"
                                  alt="Send to Printer">

                              <i>Send to Printer</i>
                            </a>
                          </font>
                        </td>
                      </tr>
                    </table>
                    <% if (! ServerMonitorThread.getInstance().isLexEVSRunning()) { %>
                      <div class="textbodyredsmall"><%= ServerMonitorThread.getInstance().getMessage() %></div>
                    <% } else { %>
                      <!-- Tree content -->
                      <div id="rootDesc">
                        <div id="bd"></div>
                        <div id="ft"></div>
                      </div>
                      <div id="treeStatus">
                        <div id="bd"></div>
                      </div>
                      <div id="emptyRoot">
                        <div id="bd"></div>
                      </div>
                      <div id="treecontainer"></div>
                    <% } %>

                    <form id="pg_form" enctype="application/x-www-form-urlencoded;charset=UTF-8">
                      <%


                      //11202013, KLO
                      //ontology_display_name = DataUtils.uri2CodingSchemeName(ontology_display_name);
                      ontology_display_name = DataUtils.getCSName(ontology_display_name);
                      
                      if (DataUtils.isNull(ontology_display_name)) {
                          ontology_display_name = Constants.NCIT_CS_NAME;
                      }
                      %>
                      <input
                          type="hidden"
                          id="ontology_node_id"
                          name="ontology_node_id"
                          value="<%=HTTPUtils.cleanXSS(ontology_node_id)%>"
                      />
                      <input
                          type="hidden"
                          id="ontology_node_ns"
                          name="ontology_node_ns"
                          value="<%=HTTPUtils.cleanXSS(ontology_node_ns)%>"
                      />
                      <input
                          type="hidden"
                          id="ontology_display_name"
                          name="ontology_display_name"
                          value="<%=HTTPUtils.cleanXSS(ontology_display_name)%>"
                      />
                      <input type="hidden" id="schema" name="schema" value="<%=HTTPUtils.cleanXSS(schema)%>" />
                      <input
                          type="hidden"
                          id="ontology_version"
                          name="ontology_version"
                          value="<%=HTTPUtils.cleanXSS(ontology_version)%>"
                      />

                      <input
                            type="hidden"
                            id="ontology_node_ns"
                            name="ontology_node_ns"
                            value="<%=HTTPUtils.cleanXSS(ontology_node_ns)%>"
                      />

                    </form>
                    <!-- End of Tree control content -->
                  </div>
                </div>
              </div>
            </f:view>
                <div id="status"><div>
		<div id="tree"><div>            
            
            <script type="text/javascript">_satellite.pageBottom();</script>
          </body>
        </html>
