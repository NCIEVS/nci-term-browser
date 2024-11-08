package gov.nih.nci.evs.browser.properties;


import gov.nih.nci.evs.browser.bean.ValueSetConfig;
import gov.nih.nci.evs.browser.utils.*;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.Map;
import org.apache.logging.log4j.*;


/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction
 * with the National Cancer Institute, and so to the extent government
 * employees are co-authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the disclaimer of Article 3,
 *      below. Redistributions in binary form must reproduce the above
 *      copyright notice, this list of conditions and the following
 *      disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution,
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIT and the National
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIT" must
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not
 *      authorize the recipient to use any trademarks owned by either NCI
 *      or NGIT
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT,
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author EVS Team
 * @version 1.0
 *
 *          Modification history Initial implementation kim.ong@ngc.com
 *
 */


public class ValueSetDefinitionConfig {
    protected static HashMap valueSetConfigHashMap = null;
    private static String vsd_config_file_dir = null;
    private static String vsd_config_file = null;
	private static Logger _logger = LogManager.getLogger(ValueSetDefinitionConfig.class);

/*
    static {
		long ms = System.currentTimeMillis();
		vsd_config_file = System.getProperty(VALUE_SET_REPORT_CONFIG);
		_logger.info("VALUE_SET_REPORT_CONFIG File Location= " + vsd_config_file);
		if (vsd_config_file == null) {
			System.out.println("ERROR: Unable to find system property " + VALUE_SET_REPORT_CONFIG);
		} else {
			vsd_config_file_dir = new File(vsd_config_file).getParent();
			valueSetConfigHashMap = readValueSetDefinitionConfigFile(vsd_config_file);
			System.out.println("ValueSetDefinitionConfig initialization run time (ms): " + (System.currentTimeMillis() - ms));
		}
	}
*/
    public static final String VALUE_SET_REPORT_CONFIG = "gov.nih.nci.evs.browser.ValueSetReportConfig";
    public static final String VALUE_SET_URI_PREFIX = "http://evs.nci.nih.gov/valueset/";
    public static final String VALUE_SET_URI_PREFIX_OLD = "http://ncit";
	public static final String NO_VALUE_SET_REPORT_AVAILABLE = "No value set report is available.";

    public ValueSetDefinitionConfig() {

	}

	public static String getValueSetDownloadDir() {
		return vsd_config_file_dir;
	}


    public static boolean fileExists(String filename) {
		File f = new File(filename);
		if (f == null) return false;
		if(f.exists() && !f.isDirectory()) {
			return true;
		}
		return false;
	}

	public static String getValueSetDownloadFilename(ValueSetConfig vsc) {
		//ftp://ftp1.nci.nih.gov/pub/cacore/EVS/CDISC/SDTM/CDASH%20Terminology.xls
		if (vsc == null) return null;
		String filename = vsc.getReportURI();
		int n = filename.lastIndexOf("/");
		if (n != -1) {
			filename = filename.substring(n+1, filename.length());
		}
		filename = filename.replaceAll("%20", "_");
		return vsd_config_file_dir + File.separator + filename;
	}


    public static Vector<String> parseData(String line) {
		if (line == null) return null;
        String tab = "|";
        return parseData(line, tab);
    }

    public static Vector<String> parseData(String line, String tab) {
		if (line == null) return null;
        Vector data_vec = new Vector();
        StringTokenizer st = new StringTokenizer(line, tab);
        if (st == null) return null;
        while (st.hasMoreTokens()) {
            String value = st.nextToken();
            if (value == null) return null;
            if (value.compareTo("null") == 0)
                value = "";
            data_vec.add(value);
        }
        return data_vec;
    }


    public static HashMap readValueSetDefinitionConfigFile(String file) {
		HashMap hmap = new HashMap();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			if (in == null) return null;
			while (in.ready()) {
			    String s = in.readLine();
			    Vector v = parseData(s);
			    String name = (String) v.elementAt(0);
			    String uri = (String) v.elementAt(1);
			    String reportURI = (String) v.elementAt(2);
			    String extractionRule = (String) v.elementAt(3);

			    if (reportURI != null) {
					reportURI = reportURI.replaceAll("%20", " ");
				}

				ValueSetConfig vsc = new ValueSetConfig(
					name,
					uri,
					reportURI,
					extractionRule);
				hmap.put(uri, vsc);
				//String uri_lower = uri.toLowerCase();
				//hmap.put(uri_lower, vsc);
			}
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return hmap;
	}

	public static ValueSetConfig getValueSetConfig(String uri) {
		long ms = System.currentTimeMillis();
		vsd_config_file = System.getProperty(VALUE_SET_REPORT_CONFIG);

		if (vsd_config_file == null) {
			System.out.println("ERROR: Unable to find system property " + VALUE_SET_REPORT_CONFIG);
		    String user_dir = System.getProperty("user.dir");
            System.out.println("Try user.dir" + user_dir);
            vsd_config_file = user_dir + File.separator + "value_set_report_config.txt";
		}
		vsd_config_file_dir = new File(vsd_config_file).getParent();
		valueSetConfigHashMap = readValueSetDefinitionConfigFile(vsd_config_file);
		System.out.println("ValueSetDefinitionConfig initialization run time (ms): " + (System.currentTimeMillis() - ms));

		if (valueSetConfigHashMap.containsKey(uri)) {
			return (ValueSetConfig) valueSetConfigHashMap.get(uri);
		} else if (valueSetConfigHashMap.containsKey(uri.toLowerCase())) {
			return (ValueSetConfig) valueSetConfigHashMap.get(uri.toLowerCase());
		} else { // backward compatibility

			int n = uri.indexOf(VALUE_SET_URI_PREFIX_OLD);
			if (n != -1) {
				n = uri.lastIndexOf(":");
				uri = VALUE_SET_URI_PREFIX + uri.substring(n+1, uri.length());
				return (ValueSetConfig) valueSetConfigHashMap.get(uri);
			}

		}
		return null;
	}


    public static Vector interpretExtractionRule(String extractionRule) {
		if (extractionRule == null || extractionRule.compareTo("null") == 0 || extractionRule.length() == 0) {
			System.out.println("No report.");
			return null;
		} else {
			Vector v = parseData(extractionRule, ":");
			return v;
		}
	}

    public static void main(String[] args) throws IOException {
		Set<Map.Entry<String, String>> set = ValueSetDefinitionConfig.valueSetConfigHashMap.entrySet();

		Vector w = new Vector();
		for (Map.Entry entry : set) {
			String uri = (String) entry.getKey();
			ValueSetConfig vsc = (ValueSetConfig) entry.getValue();
			System.out.println("\n");
			System.out.println(vsc.getUri());
        	System.out.println(vsc.getName());
        	System.out.println(vsc.getReportURI());
        	System.out.println(vsc.getExtractionRule());
        	Vector u = interpretExtractionRule(vsc.getExtractionRule());
        	if (u != null && u.size() == 2) {
				w.add(vsc.getExtractionRule());
			}
		}

        System.out.println("\nRules with Two Parameters:");
		for (int i=0; i<w.size(); i++) {
			String t = (String) w.elementAt(i);
			System.out.println(t);
		}

		w = new Vector();
		for (Map.Entry entry : set) {
			String uri = (String) entry.getKey();
			ValueSetConfig vsc = (ValueSetConfig) entry.getValue();
			System.out.println("\n");
			System.out.println(vsc.getUri());
        	System.out.println(vsc.getName());
        	System.out.println(vsc.getReportURI());
        	System.out.println(vsc.getExtractionRule());
        	Vector u = interpretExtractionRule(vsc.getExtractionRule());

        	if (u == null || u.size() == 1) {
				w.add(vsc.getName());
			}
		}

        System.out.println("\nRoot:");
		for (int i=0; i<w.size(); i++) {
			String t = (String) w.elementAt(i);
			System.out.println(t);
		}


		w = new Vector();
		HashSet hset = new HashSet();
		for (Map.Entry entry : set) {
			String uri = (String) entry.getKey();
			ValueSetConfig vsc = (ValueSetConfig) entry.getValue();
			System.out.println("\n");
			System.out.println(vsc.getUri());
        	System.out.println(vsc.getName());
        	System.out.println(vsc.getReportURI());
        	System.out.println(vsc.getExtractionRule());
        	if (vsc.getExtractionRule() != null && vsc.getExtractionRule().length() > 0) {
				if (!hset.contains(vsc.getExtractionRule())) {
					hset.add(vsc.getExtractionRule());
					w.add(vsc.getExtractionRule());
				}
			}
		}
		w = new SortUtils().quickSort(w);
        System.out.println("\nExtractionRules:");
		for (int i=0; i<w.size(); i++) {
			String t = (String) w.elementAt(i);
			System.out.println(t);
		}
		hset.clear();

		w = new Vector();
		hset = new HashSet();
		for (Map.Entry entry : set) {
			String uri = (String) entry.getKey();
			ValueSetConfig vsc = (ValueSetConfig) entry.getValue();
			System.out.println("\n");
			System.out.println(vsc.getUri());
        	System.out.println(vsc.getName());
        	System.out.println(vsc.getReportURI());
        	System.out.println(vsc.getExtractionRule());
        	if (vsc.getReportURI() != null && vsc.getReportURI().length() > 0) {
				if (!hset.contains(vsc.getReportURI())) {
					hset.add(vsc.getReportURI());
					w.add(vsc.getReportURI());
				}
			}
		}
		w = new SortUtils().quickSort(w);
        System.out.println("\nTop node reports:");
		for (int i=0; i<w.size(); i++) {
			String t = (String) w.elementAt(i);
			System.out.println(t);
		}
		hset.clear();
    }
}

