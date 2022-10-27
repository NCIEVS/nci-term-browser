package gov.nih.nci.evs.browser.utils;

import java.io.*;
import java.util.*;
import java.net.URI;
import javax.servlet.*;
import javax.servlet.http.*;

import gov.nih.nci.evs.browser.properties.*;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.concepts.*;

public class SessionMonitor {
	LexBIGService lbSvc = null;
	ConceptDetails cd = null;
	RelationshipUtils relUtils = null;

	public SessionMonitor(LexBIGService lbSvc) {
		this.lbSvc = lbSvc;
		this.cd = new ConceptDetails(lbSvc);
		this.relUtils = new RelationshipUtils(lbSvc);
	}

	private PropertyData createPropertyData(String codingScheme, String version, String code) {
		PropertyData propertyData = new PropertyData(lbSvc, codingScheme, version);
		propertyData.set_owl_role_quantifiers(NCItBrowserProperties.get_owl_role_quantifiers());
		HashMap def_map = null;
		List displayItemList = null;
		try {
			def_map = NCItBrowserProperties.getDefSourceMappingHashMap();
			propertyData.setDefSourceMapping(def_map);
			displayItemList = NCItBrowserProperties.getInstance().getDisplayItemList();
			propertyData.setDisplayItemList(displayItemList);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
        return propertyData;
	}
/*
	private HashMap createRelationshipHashMap(String codingScheme, String version, String code) {
		return createRelationshipHashMap(codingScheme, version, code, null);
	}

	private HashMap createRelationshipHashMap(String codingScheme, String version, String code, String ns) {
		if (ns == null) {
			ns = cd.getNamespaceByCode(codingScheme, version, code);
		}
		boolean useNamespace = true;
	    return relUtils.getRelationshipHashMap(codingScheme, version, code, ns, useNamespace);
    }
*/
    private void refreshSessionVariables(HttpServletRequest request, String codingScheme, String version, String code, String namespace) {
		PropertyData propertyData = createPropertyData(codingScheme, version, code);
		request.getSession().setAttribute("propertyData", propertyData);
		/*
		HashMap relationshipHashMap = createRelationshipHashMap(codingScheme, version, code);
		request.getSession().setAttribute("RelationshipHashMap", relationshipHashMap);
		*/
		request.getSession().setAttribute("active_code", code);
		request.getSession().setAttribute("ns", namespace);

        Entity c = cd.getConceptByCode(codingScheme, version, code, namespace, true);
        propertyData.setCurr_concept(c);
        request.getSession().setAttribute("concept", c);
	}

	public void execute(HttpServletRequest request, String codingScheme, String version, String code) {
		String ns = cd.getNamespaceByCode(codingScheme, version, code);
		execute(request, codingScheme, version, code, ns);
	}

	public void execute(HttpServletRequest request, String codingScheme, String version, String code, String namespace) {
		String active_code = (String) request.getSession().getAttribute("active_code");
		if (active_code == null || active_code.compareTo(code) != 0) {
			refreshSessionVariables(request, codingScheme, version, code, namespace);
		}
	}

}

