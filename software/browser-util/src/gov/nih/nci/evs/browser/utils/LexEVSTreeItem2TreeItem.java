package gov.nih.nci.evs.browser.utils;

import gov.nih.nci.evs.browser.properties.*;

import org.lexevs.dao.database.service.valuesets.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
			for (int i=0; i<children.size(); i++) {
				LexEVSTreeItem childItem = (LexEVSTreeItem) children.get(i);
				TreeItem child_ti = toTreeItem(childItem);
				ti.addChild(association, child_ti);
				ti._expandable = true;
			}
		}
		//ti = replaceCodeByURI(ti);
		return ti;
	}

/*
	public static TreeItem replaceCodeByURI(TreeItem ti_0) {
		if (ti_0 == null) return null;
		String code = ti_0._code;
		String uri = ValueSetDefinitionConfig.getValueSetURI(code);
		TreeItem ti = new TreeItem(uri, ti_0._text, null, ti_0._id, ti_0._auis);

		ti._expandable = false;
		for (String association : ti_0._assocToChildMap.keySet()) {
			List<TreeItem> children = ti_0._assocToChildMap.get(association);
			new SortUtils().quickSort(children);
			for (int i=0; i<children.size(); i++) {
				TreeItem childItem = (TreeItem) children.get(i);
				TreeItem child_ti = replaceCodeByURI(childItem);
				ti.addChild(association, child_ti);
				ti._expandable = true;
			}
		}
		return ti;
	}
*/
	public static TreeItem placeNCItAsFirstNode(TreeItem ti_0) {
		if (ti_0 == null) return null;
		TreeItem ti = new TreeItem(ti_0._code, ti_0._text, ti_0._ns, ti_0._id, ti_0._auis);
		ti._expandable = false;
		for (String association : ti_0._assocToChildMap.keySet()) {
			List<TreeItem> children = ti_0._assocToChildMap.get(association);
			List list = new ArrayList();
			for (int i=0; i<children.size(); i++) {
				TreeItem childItem = (TreeItem) children.get(i);
				if (childItem._text.compareTo("NCI Thesaurus") == 0) {
					ti.addChild(association, childItem);
					ti._expandable = true;
				} else {
					list.add(childItem);
				}
			}
			//new SortUtils().quickSort(list);
			for (int k=0; k<list.size(); k++) {
				TreeItem childItem3 = (TreeItem) list.get(k);
				//ti.addChild(association, sort(childItem3));
				ti.addChild(association, childItem3);
			}
		}
		return ti;
	}
}
