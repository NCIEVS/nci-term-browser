package gov.nih.nci.evs.browser.utils;


import gov.nih.nci.evs.browser.bean.MappingData;
import gov.nih.nci.evs.browser.common.Constants;
import gov.nih.nci.evs.browser.properties.*;
import java.io.*;
import java.net.URI;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Map;
import javax.faces.model.*;
import org.apache.commons.lang.*;
import org.apache.log4j.*;
import org.lexevs.property.PropertyExtension;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.commonTypes.*;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSApplicationService;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.DataModel.Core.types.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Direction;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping.SearchContext;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOption;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOptionName;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.QualifierSortOption;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Extensions.Generic.SupplementExtension;
import org.LexGrid.LexBIG.History.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.naming.*;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.*;
import static gov.nih.nci.evs.browser.common.Constants.*;


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


public class PartonomyUtils {
    private static Logger _logger = Logger.getLogger(PartonomyUtils.class);
    private LexBIGService lbSvc = null;
    private LexBIGServiceConvenienceMethods lbscm = null;

	private static String[] PART_OF = new String[] {
		                                    "part_of",
		                                    "componentOf",
		                                    "Anatomic_Structure_Is_Physical_Part_Of",
		                                    "Gene_Product_Is_Physical_Part_Of",
		                                    "Part_Of"};


	private static String[] HAS_PART = new String[] {
		                                    "has_part",
		                                    "has_organism_part",
		                                    "has_part_modified",
											"has_component_part",
											"Has_Part"};


	private static List<String> PART_OF_LIST = Arrays.asList(PART_OF);
	private static List<String> HAS_PART_LIST = Arrays.asList(HAS_PART);

	RelationshipUtils relationshipUtils = null;

	public PartonomyUtils(LexBIGService lbSvc) {
        this.lbSvc = lbSvc;
        this.relationshipUtils = new RelationshipUtils(lbSvc);
        try {
            this.lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public List getPartOfData(HashMap relMap) {
		if (relMap == null) return null;
		List list_all = new ArrayList();
	    List list = relationshipUtils.getRelationshipData(relMap, "type_role", PART_OF_LIST);
	    if (list != null && list.size() > 0) {
			list_all.addAll(list);
		}
	    list = relationshipUtils.getRelationshipData(relMap, "type_association", PART_OF_LIST);
	    if (list != null && list.size() > 0) {
			list_all.addAll(list);
		}

	    list = relationshipUtils.getRelationshipData(relMap, "inverse_type_role", HAS_PART_LIST);
	    if (list != null && list.size() > 0) {
			list_all.addAll(list);
		}
	    list = relationshipUtils.getRelationshipData(relMap, "inverse_type_association", HAS_PART_LIST);
	    if (list != null && list.size() > 0) {
			list_all.addAll(list);
		}
		return list_all;
	}


	public List getHasPartData(HashMap relMap) {
		if (relMap == null) return null;
		List list_all = new ArrayList();
	    List list = relationshipUtils.getRelationshipData(relMap, "type_inverse_role", PART_OF_LIST);
	    if (list != null && list.size() > 0) {
			list_all.addAll(list);
		}
	    list = relationshipUtils.getRelationshipData(relMap, "type_inverse_association", PART_OF_LIST);
	    if (list != null && list.size() > 0) {
			list_all.addAll(list);
		}
	    list = relationshipUtils.getRelationshipData(relMap, "type_role", HAS_PART_LIST);
	    if (list != null && list.size() > 0) {
			list_all.addAll(list);
		}
	    list = relationshipUtils.getRelationshipData(relMap, "type_association", HAS_PART_LIST);
	    if (list != null && list.size() > 0) {
			list_all.addAll(list);
		}

		return list_all;
	}

	public boolean hasPartOfRelationships(HashMap relMap) {
		if (relMap == null) return false;
		List list = getHasPartData(relMap);
		if (list != null && list.size()>0) return true;
		list = getPartOfData(relMap);
		if (list != null && list.size()>0) return true;
		return false;
	}

	public String encode(String t) {
		if (t == null) return null;
		t = t.replaceAll("'", "\'");
		return t;
	}

	public TreeItem getChildNode(String codingSchemeURN, String codingSchemeVersion, String code, String namespace, String rel) {
		//HashMap hmap = new HashMap();
		boolean useNamespace = true;
		if (gov.nih.nci.evs.browser.utils.StringUtils.isNullOrBlank(namespace)) {
			useNamespace = false;
		}

		Entity concept = new ConceptDetails(lbSvc).getConceptByCode(codingSchemeURN, codingSchemeVersion, code, namespace, useNamespace);
		String name = "<NO DESCRIPTION>";
		if (concept.getEntityDescription() != null) {
			name = concept.getEntityDescription().getContent();
		}
		name = encode(name);
		TreeItem root = new TreeItem(code, name);
		RelationshipUtils relUtils = new RelationshipUtils(lbSvc);
		HashMap relMap = relUtils.getRelationshipHashMap(codingSchemeURN, codingSchemeVersion, code, namespace, useNamespace);

        List part_of_list = null;
        if (rel.compareTo("part_of") == 0) {
		    part_of_list = getPartOfData(relMap);
	    } else {
			part_of_list = getHasPartData(relMap);
		}

		for (int i=0; i<part_of_list.size(); i++) {
			String t = (String) part_of_list.get(i);
			//String child_name = gov.nih.nci.evs.browser.utils.StringUtils.getFieldValue(t, 1);
			String child_code = gov.nih.nci.evs.browser.utils.StringUtils.getFieldValue(t, 2);
			String rel_label = gov.nih.nci.evs.browser.utils.StringUtils.getFieldValue(t, 0);
			TreeItem child_node = getChildNode(codingSchemeURN, codingSchemeVersion, child_code, namespace, rel);
			root.addChild(rel_label, child_node);
		}
		return root;
	}



	public HashMap getPathsToRoots(String codingSchemeURN, String codingSchemeVersion, String code, String namespace, String rel) {
		HashMap hmap = new HashMap();
		TreeItem root = getChildNode(codingSchemeURN, codingSchemeVersion, code, namespace, rel);
        hmap.put("<ROOT>", root);
        return hmap;
	}

}
