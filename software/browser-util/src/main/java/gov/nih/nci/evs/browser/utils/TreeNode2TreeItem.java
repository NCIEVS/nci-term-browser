package gov.nih.nci.evs.browser.utils;

import gov.nih.nci.evs.browser.bean.*;

import java.io.*;
import java.util.*;
import java.net.*;

//import com.google.gson.*;
//import com.google.gson.GsonBuilder;
//import com.google.gson.reflect.TypeToken;
//import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
//import com.thoughtworks.xstream.io.xml.DomDriver;
//import com.thoughtworks.xstream.XStream;


public class TreeNode2TreeItem
{
// Variable declaration
    private static String ASSOCIATION_TEXT = "HAS_CHILD";

// Default constructor
	public TreeNode2TreeItem() {
	}


    public static TreeItem treeNode2TreeItem(TreeNode treeNode) {
		if (treeNode == null) return null;

        TreeItem ti = new TreeItem(treeNode.getOntology_node_id(),
                                   treeNode.getOntology_node_name(),
                                   treeNode.getOntology_node_ns(),
                                   treeNode.getOntology_node_id(),
                                   null);

		ti._expandable = false;
		if (treeNode.getOntology_node_child_count() > 0) {
			ti._expandable = true;
			List<TreeNode> children_nodes = treeNode.getChildren_nodes();
			for (int i=0; i<children_nodes.size(); i++) {
				TreeNode childNode = (TreeNode) children_nodes.get(i);
				TreeItem childItem = treeNode2TreeItem(childNode);
				ti.addChild(ASSOCIATION_TEXT, childItem);
			}
		}
		return ti;
	}

    public static TreeItem treeNodeList2TreeItem(List<TreeNode> treeNodeList) {
		if (treeNodeList == null) return null;
		TreeItem root = new TreeItem("Root", "<Root>");
		for (int i=0; i<treeNodeList.size(); i++) {
			TreeNode treeNode = (TreeNode) treeNodeList.get(i);
			TreeItem ti = treeNode2TreeItem(treeNode);
			root.addChild(ASSOCIATION_TEXT, ti);
		}
		return root;
	}
}
