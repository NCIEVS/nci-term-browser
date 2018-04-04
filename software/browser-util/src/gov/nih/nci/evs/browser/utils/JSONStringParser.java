package gov.nih.nci.evs.browser.utils;
import gov.nih.nci.evs.browser.bean.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.io.*;
import java.util.*;

public class JSONStringParser {

	public static Vector readFile(String filename)
	{
		Vector v = new Vector();
		try {
			BufferedReader in = new BufferedReader(
			   new InputStreamReader(
						  new FileInputStream(filename), "UTF8"));
			String str;
			while ((str = in.readLine()) != null) {
				v.add(str);
			}
            in.close();
		} catch (Exception ex) {
            ex.printStackTrace();
		}
		return v;
	}

	public static String treeNode2String(TreeNode tree_node) {
		return treeNode2String(tree_node, 1);
	}

	public static String getIndentation(int level) {
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<level; i++) {
			buf.append("\t");
		}
		return buf.toString();
	}

	public static String treeNode2String(TreeNode tree_node, int level) {
		StringBuffer buf = new StringBuffer();
		String indent = getIndentation(level);
		buf.append(indent + "ontology_node_child_count: " + tree_node.getOntology_node_child_count()).append("\n");
		buf.append(indent + "ontology_node_id: " + tree_node.getOntology_node_id()).append("\n");
		buf.append(indent + "ontology_node_ns: " + tree_node.getOntology_node_ns()).append("\n");
		buf.append(indent + "ontology_node_name: " + tree_node.getOntology_node_name()).append("\n");
		if (tree_node.getOntology_node_child_count() > 0) {
			List children_nodes = tree_node.getChildren_nodes();
			for (int i=0; i<children_nodes.size(); i++) {
				TreeNode child_node = (TreeNode) children_nodes.get(i);
				String str = treeNode2String(child_node, level+1);
				buf.append(str).append("\n");
			}
		}
        return buf.toString();
	}

    public static List<TreeNode> jsonArray2TreeNodeList(JSONArray jsonArray) {
		List<TreeNode> node_list = new ArrayList();
		try {
			Iterator<JSONObject> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				JSONObject node_obj = iterator.next();
				TreeNode node = jsonObject2TreeNode(node_obj);
				node_list.add(node);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return node_list;
	}

    public static TreeNode jsonObject2TreeNode(JSONObject jsonObject) {
		try {
			Long ontology_node_child_count = (Long) jsonObject.get("ontology_node_child_count");
			String ontology_node_id = (String) jsonObject.get("ontology_node_id");
			String ontology_node_name = (String) jsonObject.get("ontology_node_name");
			String ontology_node_ns = (String) jsonObject.get("ontology_node_ns");
			List<TreeNode> children_nodes = new ArrayList();
			JSONArray child_node_ary = (JSONArray) jsonObject.get("children_nodes");
			Iterator<JSONObject> iterator = child_node_ary.iterator();
			while (iterator.hasNext()) {
				JSONObject child_node_obj = iterator.next();
				TreeNode child_node = jsonObject2TreeNode(child_node_obj);
				children_nodes.add(child_node);
			}
			return new TreeNode(
					ontology_node_id,
					ontology_node_ns,
					ontology_node_name,
					ontology_node_child_count,
					children_nodes);

		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }



    public static String getJsonFromFile(String filename) {
		Vector u = readFile(filename);
		return (String) u.elementAt(0);
	}

    public static JSONObject string2JSONObject(String json) {
		JSONParser parser = new JSONParser();
		try {
			JSONObject json_obj = (JSONObject) parser.parse(json);
			return json_obj;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

    public static JSONArray string2JSONArray(String json) {
		JSONParser parser = new JSONParser();
		try {
			JSONArray json_array = (JSONArray) parser.parse(json);
			return json_array;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

    public static void main(String[] args) {
		String jsonfile = "test.json";
		String json = getJsonFromFile(jsonfile);
		JSONArray jsonArray = string2JSONArray(json);
		List<TreeNode> list = jsonArray2TreeNodeList(jsonArray);
		for (int i=0; i<list.size(); i++) {
			TreeNode node = (TreeNode) list.get(i);
		    String str = treeNode2String(node);
		    System.out.println(str);
		}
	}

}

