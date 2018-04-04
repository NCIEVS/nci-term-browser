package gov.nih.nci.evs.browser.bean;

import java.io.*;
import java.util.*;
import java.net.*;

import com.google.gson.*;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.XStream;

public class TreeNode implements Serializable
{

// Variable declaration
	private String ontology_node_id;
	private String ontology_node_ns;
	private String ontology_node_name;
	private Long ontology_node_child_count;
	private List<TreeNode> children_nodes;

// Default constructor
	public TreeNode() {
	}

// Constructor
	public TreeNode(
		String ontology_node_id,
		String ontology_node_ns,
		String ontology_node_name,
		Long ontology_node_child_count,
		List<TreeNode> children_nodes) {

		this.ontology_node_id = ontology_node_id;
		this.ontology_node_ns = ontology_node_ns;
		this.ontology_node_name = ontology_node_name;
		this.ontology_node_child_count = ontology_node_child_count;
		this.children_nodes = children_nodes;
	}

// Set methods
	public void setOntology_node_id(String ontology_node_id) {
		this.ontology_node_id = ontology_node_id;
	}

	public void setOntology_node_ns(String ontology_node_ns) {
		this.ontology_node_ns = ontology_node_ns;
	}

	public void setOntology_node_name(String ontology_node_name) {
		this.ontology_node_name = ontology_node_name;
	}

	public void setOntology_node_child_count(Long ontology_node_child_count) {
		this.ontology_node_child_count = ontology_node_child_count;
	}

	public void setChildren_nodes(List<TreeNode> children_nodes) {
		this.children_nodes = children_nodes;
	}


// Get methods
	public String getOntology_node_id() {
		return this.ontology_node_id;
	}

	public String getOntology_node_ns() {
		return this.ontology_node_ns;
	}

	public String getOntology_node_name() {
		return this.ontology_node_name;
	}

	public Long getOntology_node_child_count() {
		return this.ontology_node_child_count;
	}

	public List<TreeNode> getChildren_nodes() {
		return this.children_nodes;
	}

	public String toXML() {
		XStream xstream_xml = new XStream(new DomDriver());
		String xml = xstream_xml.toXML(this);
		xml = escapeDoubleQuotes(xml);
		StringBuffer buf = new StringBuffer();
		String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		buf.append(XML_DECLARATION).append("\n").append(xml);
		xml = buf.toString();
		return xml;
	}

	public String toJson() {
		JsonParser parser = new JsonParser();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
	}

	public String escapeDoubleQuotes(String inputStr) {
		char doubleQ = '"';
		StringBuffer buf = new StringBuffer();
		for (int i=0;  i<inputStr.length(); i++) {
			char c = inputStr.charAt(i);
			if (c == doubleQ) {
				buf.append(doubleQ).append(doubleQ);
			}
			buf.append(c);
		}
		return buf.toString();
	}
}
