package gov.nih.nci.evs.browser.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Concept
{

// Variable declaration
	private String code;
	private String label;
	private String codingScheme;
	private String version;
	private String nameSpace;
	private String uri;
	private boolean selected;

// Default constructor
	public Concept() {
	}

// Constructor
	public Concept(
		String code,
		String label,
		String codingScheme,
		String version,
		String nameSpace,
		String uri) {

		this.code = code;
		this.label = label;
		this.codingScheme = codingScheme;
		this.version = version;
		this.nameSpace = nameSpace;
		this.uri = uri;
		this.selected = false;
	}

// Set methods
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setCodingScheme(String codingScheme) {
		this.codingScheme = codingScheme;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}


// Get methods
	public String getCode() {
		return this.code;
	}

	public String getLabel() {
		return this.label;
	}

	public String getCodingScheme() {
		return this.codingScheme;
	}

	public String getVersion() {
		return this.version;
	}

	public String getNameSpace() {
		return this.nameSpace;
	}

	public String getUri() {
		return this.uri;
	}

	public boolean getSelected() {
		return this.selected;
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
