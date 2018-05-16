package gov.nih.nci.evs.browser.utils;


import gov.nih.nci.evs.browser.properties.*;
import java.io.Serializable;
import java.util.*;
import org.lexevs.dao.database.service.valuesets.*;


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


public class LexEVSTreeItem2TreeItem {

	public static TreeItem toTreeItem(LexEVSTreeItem lexevs_ti) {
		if (lexevs_ti == null) return null;
		TreeItem ti = new TreeItem(lexevs_ti.get_code(), lexevs_ti.get_text(), lexevs_ti.get_ns(), lexevs_ti.get_id(), lexevs_ti.get_auis());

		ti._expandable = false;
		for (String association : lexevs_ti._assocToChildMap.keySet()) {
			List<LexEVSTreeItem> children = lexevs_ti._assocToChildMap.get(association);
			if (children == null) return null;
			for (int i=0; i<children.size(); i++) {
				LexEVSTreeItem childItem = (LexEVSTreeItem) children.get(i);
				TreeItem child_ti = toTreeItem(childItem);
				ti.addChild(association, child_ti);
				ti._expandable = true;
			}
		}
		return ti;
	}


    public static int get_children_node_count(TreeItem ti) {
		int knt = 0;
		for (String association : ti._assocToChildMap.keySet()) {
			List<TreeItem> children = ti._assocToChildMap.get(association);
			knt = knt + children.size();
		}
		return knt;
	}


    public static void write_children_nodes(TreeItem ti) {
		int knt = 0;
		if (!ti._expandable) return;
		for (String association : ti._assocToChildMap.keySet()) {
			List<TreeItem> children = ti._assocToChildMap.get(association);
			for (int i=0; i<children.size(); i++) {
				TreeItem childItem = (TreeItem) children.get(i);
			    System.out.println(ti._text + "  child node: " + childItem._text);
			    write_children_nodes(childItem);
		    }
		}
	}

	public static TreeItem sortChildNodes(TreeItem ti) {
		if (ti == null) return null;
		if (!ti._expandable) return ti;
		TreeItem ti_clone = new TreeItem(ti._code, ti._text, ti._ns, ti._id, ti._auis);
		HashMap hmap = new HashMap();
		Vector keys = new Vector();
		String associationName = null;
		ti_clone._expandable = false;

		for (String association : ti._assocToChildMap.keySet()) {
			associationName = association;
			List<TreeItem> children = ti._assocToChildMap.get(association);
			for (int i=0; i<children.size(); i++) {
				TreeItem childItem = (TreeItem) children.get(i);
				hmap.put(childItem._text, childItem);
				keys.add(childItem._text);
				ti_clone._expandable = true;
			}
		}

		Vector v = new Vector();
		Vector w = new Vector();
		for (int i=0; i<keys.size(); i++) {
			String key = (String) keys.elementAt(i);
			if (key.compareTo("NCI Thesaurus") == 0 || key.compareTo("National Cancer Institute Terminology") == 0) {
				v.add(key);
			} else {
				w.add(key);
			}
		}
		if (v.size() == 0) {
			return ti;
		}
		v.addAll(w);
		for (int i=0; i<v.size(); i++) {
			String key = (String) v.elementAt(i);
			TreeItem childItem = (TreeItem) hmap.get(key);
			ti_clone.addChild(associationName, sortChildNodes(childItem));
		}
		return ti_clone;
	}

	public static TreeItem placeNCItAsFirstNode(TreeItem ti_0) {
		if (ti_0 == null) return null;
		TreeItem ti = new TreeItem(ti_0._code, ti_0._text, ti_0._ns, ti_0._id, ti_0._auis);
		ti._expandable = false;
		for (String association : ti_0._assocToChildMap.keySet()) {
			List<TreeItem> children = ti_0._assocToChildMap.get(association);
			List list = new ArrayList();
			for (int i=0; i<children.size(); i++) {
				TreeItem childItem = (TreeItem) children.get(i);
				childItem = sortChildNodes(childItem);
				ti.addChild(association, childItem);
				ti._expandable = true;
			}
		}
		return ti;
	}
}
