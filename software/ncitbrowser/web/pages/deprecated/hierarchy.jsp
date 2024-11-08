<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.util.Vector" %>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.common.Constants" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>

<!--
  Note: This tree is used in Hierarchy (ex. NCIt home page) and View in Hierarchy (ex. Concept Detail page) features.
-->

<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/yahoo-min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/event-min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/dom-min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/animation-min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/container-min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/connection-min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/autocomplete-min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/yui/treeview-min.js"></script>
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
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/fonts.css" />
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/grids.css" />
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/code.css" />
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/yui/tree.css" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>

    <script language="JavaScript">

      var tree;
      var nodeIndex;
      var rootDescDiv;
      var emptyRootDiv;
      var treeStatusDiv;
      var nodes = [];
      var currOpener;

      function load(url,target) {
        if (target != '')
        target.window.location.href = url;
        else
        window.location.href = url;
      }

      function init() {

        rootDescDiv = new YAHOO.widget.Module("rootDesc", {visible:false} );
        resetRootDesc();

        emptyRootDiv = new YAHOO.widget.Module("emptyRoot", {visible:true} );
        resetEmptyRoot();

        treeStatusDiv = new YAHOO.widget.Module("treeStatus", {visible:true} );
        resetTreeStatus();

        currOpener = opener;
        initTree();
      }

      function addTreeNode(rootNode, nodeInfo) {

        var newNodeDetails = "javascript:onClickTreeNode('"
        + nodeInfo.ontology_node_id
        + "','"
        + nodeInfo.ontology_node_ns
        + "');";

        var newNodeData = { label:nodeInfo.ontology_node_name, id:nodeInfo.ontology_node_id, ns:nodeInfo.ontology_node_ns, href:newNodeDetails };

        var newNode = new YAHOO.widget.TextNode(newNodeData, rootNode, false);
        if (nodeInfo.ontology_node_child_count > 0) {
          newNode.setDynamicLoad(loadNodeData);
        }
      }

      function buildTree(ontology_node_id, ontology_display_name) {
        var handleBuildTreeSuccess = function(o) {
          var respTxt = o.responseText;
          var respObj = eval('(' + respTxt + ')');
          if ( typeof(respObj) != "undefined") {
            if ( typeof(respObj.root_nodes) != "undefined") {
              var root = tree.getRoot();
              if (respObj.root_nodes.length == 0) {
                showEmptyRoot();
              }
              else {
                for (var i=0; i < respObj.root_nodes.length; i++) {
                  var nodeInfo = respObj.root_nodes[i];
                  var expand = false;
                  addTreeNode(root, nodeInfo, expand);
                }
              }

              tree.draw();
            }
          }
          resetTreeStatus();
        }

        var handleBuildTreeFailure = function(o) {
          resetTreeStatus();
          resetEmptyRoot();
          alert('responseFailure: ' + o.statusText);
        }

        var buildTreeCallback =
        {
          success:handleBuildTreeSuccess,
          failure:handleBuildTreeFailure
          };

          if (ontology_display_name!='') {
            resetEmptyRoot();

            showTreeLoadingStatus();
            var ontology_source = null;
            var ontology_version = document.forms["pg_form"].ontology_version.value;
            var request = YAHOO.util.Connect.asyncRequest('GET','<%= request.getContextPath() %>/ajax?action=build_tree&ontology_node_id=' +ontology_node_id+'&ontology_display_name='+ontology_display_name+'&version='+ontology_version+'&ontology_source='+ontology_source,buildTreeCallback);
          }
        }

        function resetTree(ontology_node_id, ontology_display_name) {

          var handleResetTreeSuccess = function(o) {
            var respTxt = o.responseText;
            var respObj = eval('(' + respTxt + ')');
            if ( typeof(respObj) != "undefined") {
              if ( typeof(respObj.root_node) != "undefined") {
                var root = tree.getRoot();

                var nodeDetails = "javascript:onClickTreeNode('"
                + respObj.root_node.ontology_node_id
                + "','"
                + respObj.root_node.ontology_node_ns
                + "');";
                var rootNodeData = { label:respObj.root_node.ontology_node_name,
                id:respObj.root_node.ontology_node_id,
                ns:respObj.root_node.ontology_node_ns, href:nodeDetails };

                var expand = false;
                if (respObj.root_node.ontology_node_child_count > 0) {
                  expand = true;
                }
                var ontRoot = new YAHOO.widget.TextNode(rootNodeData, root, expand);

                if ( typeof(respObj.child_nodes) != "undefined") {
                  for (var i=0; i < respObj.child_nodes.length; i++) {
                    var nodeInfo = respObj.child_nodes[i];
                    addTreeNode(ontRoot, nodeInfo);
                  }
                }
                tree.draw();
                setRootDesc(respObj.root_node.ontology_node_name, ontology_display_name);
              }
            }
            resetTreeStatus();
          }

          var handleResetTreeFailure = function(o) {
            resetTreeStatus();
            alert('responseFailure: ' + o.statusText);
          }

          var resetTreeCallback =
          {
            success:handleResetTreeSuccess,
            failure:handleResetTreeFailure
            };
            if (ontology_node_id!= '') {
              showTreeLoadingStatus();
              var ontology_source = null;
              var ontology_version = document.forms["pg_form"].ontology_version.value;
              var request = YAHOO.util.Connect.asyncRequest('GET','<%= request.getContextPath() %>/ajax?action=reset_tree&ontology_node_id=' +ontology_node_id+'&ontology_display_name='+ontology_display_name + '&version='+ ontology_version +'&ontology_source='+ontology_source,resetTreeCallback);
            }
          }

          function onClickTreeNode(ontology_node_id, ontology_node_ns) {
            var ontology_display_name = document.forms["pg_form"].ontology_display_name.value;
            var ontology_version = document.forms["pg_form"].ontology_version.value;
            load('<%= request.getContextPath() %>/ConceptReport.jsp?dictionary='+ ontology_display_name
            + '&version='+ ontology_version
            + '&code=' + ontology_node_id
            + '&ns=' + ontology_node_ns, currOpener);
          }

          function onClickViewEntireOntology(ontology_display_name) {
            var ontology_display_name = document.pg_form.ontology_display_name.value;
            tree = new YAHOO.widget.TreeView("treecontainer");
            tree.draw();
            resetRootDesc();
            buildTree('', ontology_display_name);
          }

          function initTree() {

            tree = new YAHOO.widget.TreeView("treecontainer");
            var ontology_node_id = document.forms["pg_form"].ontology_node_id.value;
            var ontology_display_name = document.forms["pg_form"].ontology_display_name.value;

            if (ontology_node_id == null || ontology_node_id == "null")
            {
              buildTree(ontology_node_id, ontology_display_name);
            }
            else
            {
              searchTree(ontology_node_id, ontology_display_name);
            }
          }

          function initRootDesc() {
            rootDescDiv.setBody('');
            initRootDesc.show();
            rootDescDiv.render();
          }

          function resetRootDesc() {
            rootDescDiv.hide();
            rootDescDiv.setBody('');
            rootDescDiv.render();
          }

          function resetEmptyRoot() {
            emptyRootDiv.hide();
            emptyRootDiv.setBody('');
            emptyRootDiv.render();
          }

          function resetTreeStatus() {
            treeStatusDiv.hide();
            treeStatusDiv.setBody('');
            treeStatusDiv.render();
          }

          function showEmptyRoot() {
            emptyRootDiv.setBody("<span class='instruction_text'>No root nodes available.</span>");
            emptyRootDiv.show();
            emptyRootDiv.render();
          }

          function showNodeNotFound(node_id) {
            //emptyRootDiv.setBody("<span class='instruction_text'>Concept with code " + node_id + " not found in the hierarchy.</span>");
            emptyRootDiv.setBody("<span class='instruction_text'>Concept not part of the parent-child hierarchy in this source; check other relationships.</span>");
            emptyRootDiv.show();
            emptyRootDiv.render();
          }

          function showPartialHierarchy(msg) {
            rootDescDiv.setBody("<span class='instruction_text'>(Note: This tree only shows partial hierarchy.)" + msg + "</span>");
            rootDescDiv.show();
            rootDescDiv.render();
          }

          function showTreeLoadingStatus() {
            treeStatusDiv.setBody("<img src='<%=basePath%>/images/loading.gif'/> <span class='instruction_text'>Building tree ...</span>");
            treeStatusDiv.show();
            treeStatusDiv.render();
          }

          function showTreeDrawingStatus() {
            treeStatusDiv.setBody("<img src='<%=basePath%>/images/loading.gif'/> <span class='instruction_text'>Drawing tree ...</span>");
            treeStatusDiv.show();
            treeStatusDiv.render();
          }

          function showSearchingTreeStatus() {
            treeStatusDiv.setBody("<img src='<%=basePath%>/images/loading.gif'/> <span class='instruction_text'>Searching tree... Please wait.</span>");
            treeStatusDiv.show();
            treeStatusDiv.render();
          }

          function showConstructingTreeStatus() {
            treeStatusDiv.setBody("<img src='<%=basePath%>/images/loading.gif'/> <span class='instruction_text'>Constructing tree... Please wait.</span>");
            treeStatusDiv.show();
            treeStatusDiv.render();
          }

          function loadNodeData(node, fnLoadComplete) {
            var id = node.data.id;
            var ns = node.data.ns;

            var responseSuccess = function(o)
            {
              var path;
              var dirs;
              var files;
              var respTxt = o.responseText;

              var respObj = eval('(' + respTxt + ')');
              var fileNum = 0;
              var categoryNum = 0;
              if ( typeof(respObj.nodes) != "undefined") {
                for (var i=0; i < respObj.nodes.length; i++) {
                  var name = respObj.nodes[i].ontology_node_name;
                  var nodeDetails = "javascript:onClickTreeNode('"
                  + respObj.nodes[i].ontology_node_id
                  + "','"
                  + respObj.nodes[i].ontology_node_ns
                  + "');";
                  var newNodeData = { label:name, id:respObj.nodes[i].ontology_node_id, ns:respObj.nodes[i].ontology_node_ns, href:nodeDetails };
                  var newNode = new YAHOO.widget.TextNode(newNodeData, node, false);
                  if (respObj.nodes[i].ontology_node_child_count > 0) {
                    newNode.setDynamicLoad(loadNodeData);
                  }
                }
              }
              tree.draw();
              fnLoadComplete();
            }

            var responseFailure = function(o){
              alert('responseFailure: ' + o.statusText);
            }

            var callback =
            {
              success:responseSuccess,
              failure:responseFailure
              };

              var ontology_display_name = document.forms["pg_form"].ontology_display_name.value;
              var ontology_version = document.forms["pg_form"].ontology_version.value;
              var cObj = YAHOO.util.Connect.asyncRequest('GET','<%= request.getContextPath() %>/ajax?action=expand_tree&ontology_node_id=' +id + '&ontology_node_ns=' +ns+'&ontology_display_name='+ontology_display_name+'&version='+ontology_version,callback);
            }

            function setRootDesc(rootNodeName, ontology_display_name) {
              var newDesc = "<span class='instruction_text'>Root set to <b>" + rootNodeName + "</b></span>";
              rootDescDiv.setBody(newDesc);
              var footer = "<a onClick='javascript:onClickViewEntireOntology();' href='#' class='link_text'>view full ontology}</a>";
              rootDescDiv.setFooter(footer);
              rootDescDiv.show();
              rootDescDiv.render();
            }

            function getTimeStamp() {
              return Math.round((new Date()).getTime() / 1000);
            }

            function searchTree(ontology_node_id, ontology_display_name) {
              var handleBuildTreeSuccess = function(o) {
                var tsTotalStart = getTimeStamp();

                var respTxt = o.responseText;
                var tsEvalStart = getTimeStamp();
                var respObj = eval('(' + respTxt + ')');
                var tsEvalTotal = getTimeStamp() - tsEvalStart;
                if ( typeof(respObj) != "undefined") {

                  if ( typeof(respObj.dummy_root_nodes) != "undefined") {
                    showNodeNotFound(ontology_node_id);
                  }

                  else if ( typeof(respObj.root_nodes) != "undefined") {
                    var root = tree.getRoot();
                    if (respObj.root_nodes.length == 0) {
                      showEmptyRoot();
                    }
                    else {
                      //showPartialHierarchy("");
                      showConstructingTreeStatus();

                      for (var i=0; i < respObj.root_nodes.length; i++) {
                        var nodeInfo = respObj.root_nodes[i];
                        //var expand = false;
                        addTreeBranch(ontology_node_id, root, nodeInfo);
                      }
                    }
                  }
                }
                resetTreeStatus();
                var tsTotal = getTimeStamp() - tsTotalStart;
                // showPartialHierarchy("<br/>* EvalTotal: " + tsEvalTotal + " sec"
                //    + ", <br/>* tsTotal: " + tsTotal + " sec");
              }

              var handleBuildTreeFailure = function(o) {
                resetTreeStatus();
                resetEmptyRoot();
                alert('responseFailure: ' + o.statusText);
              }

              var buildTreeCallback =
              {
                success:handleBuildTreeSuccess,
                failure:handleBuildTreeFailure
                };

                if (ontology_display_name!='') {
                  resetEmptyRoot();

                  showSearchingTreeStatus();
                  var ontology_source = null;//document.pg_form.ontology_source.value;
                  var ontology_version = document.forms["pg_form"].ontology_version.value;
                  var request = YAHOO.util.Connect.asyncRequest('GET','<%= request.getContextPath() %>/ajax?action=search_tree&ontology_node_id=' +ontology_node_id+'&ontology_display_name='+ontology_display_name+'&version='+ontology_version+'&ontology_source='+ontology_source,buildTreeCallback);

                }
              }

              function addTreeBranch(ontology_node_id, rootNode, nodeInfo) {
                var newNodeDetails = "javascript:onClickTreeNode('"
                + nodeInfo.ontology_node_id
                + "','"
                + nodeInfo.ontology_node_ns
                + "');";

                var newNodeData = { label:nodeInfo.ontology_node_name, id:nodeInfo.ontology_node_id, ns:nodeInfo.ontology_node_ns, href:newNodeDetails };

                var expand = false;
                var childNodes = nodeInfo.children_nodes;

                if (childNodes.length > 0) {
                  expand = true;
                }
                var newNode = new YAHOO.widget.TextNode(newNodeData, rootNode, expand);
                if (nodeInfo.ontology_node_id == ontology_node_id) {
                  newNode.labelStyle = "ygtvlabel_highlight";
                }

                if (nodeInfo.ontology_node_id == ontology_node_id) {
                  newNode.isLeaf = true;
                  if (nodeInfo.ontology_node_child_count > 0) {
                    newNode.isLeaf = false;
                    newNode.setDynamicLoad(loadNodeData);
                  } else {
                    tree.draw();
                  }

                } else {
                  if (nodeInfo.ontology_node_id != ontology_node_id) {
                    if (nodeInfo.ontology_node_child_count == 0 && nodeInfo.ontology_node_id != ontology_node_id) {
                      newNode.isLeaf = true;
                    } else if (childNodes.length == 0) {
                      newNode.setDynamicLoad(loadNodeData);
                    }
                  }
                }

                tree.draw();
                //for (var i=0; i < childNodes.length; i++) {
                  //   var childnodeInfo = childNodes[i];
                  //   addTreeBranch(ontology_node_id, newNode, childnodeInfo);
                //}
              }
              YAHOO.util.Event.addListener(window, "load", init);

            </script>
          </head>
          <body>
  <header class="flex-grow-0">
	<div style='text-align: left'>
	<include-html src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html"></include-html>
	</div>
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
                  //response.setContentType("text/html;charset=utf-8");

                  JSPUtils.JSPHeaderInfoMore info = new JSPUtils.JSPHeaderInfoMore(request);
                  String hierarchy_dictionary = info.dictionary;
                  String hierarchy_version = info.version;

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
                      String ontology_node_id = HTTPUtils.cleanXSS((String) request.getParameter("code"));
                      String ontology_node_ns = HTTPUtils.cleanXSS((String) request.getParameter("ns"));

                      String ontology_display_name = info.dictionary;
                      String ontology_version = info.version;
                      String schema = ontology_display_name;

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

                      <% if (ontology_node_ns != null && ontology_node_ns.compareTo("na") != 0) { %>
                        <input
                            type="hidden"
                            id="ontology_node_ns"
                            name="ontology_node_ns"
                            value="<%=ontology_node_ns%>"
                        />
                      <% } %>

                    </form>
                    <!-- End of Tree control content -->
                  </div>
                </div>
              </div>
            </f:view>
            <script type="text/javascript">_satellite.pageBottom();</script>
          </body>
        </html>
