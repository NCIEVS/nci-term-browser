package gov.nih.nci.evs.browser.servlet;

import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.properties.*;

import java.io.*;
import java.util.*;
import java.text.*;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;

import gov.nih.nci.evs.security.SecurityToken;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.naming.*;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;

import org.lexevs.tree.json.JsonConverter;
import org.lexevs.tree.json.JsonConverterFactory;
import org.lexevs.tree.model.LexEvsTree;
import org.lexevs.tree.model.LexEvsTreeNode;
import org.lexevs.tree.model.LexEvsTreeNode.ExpandableStatus;
import org.lexevs.tree.service.TreeService;
import org.lexevs.tree.service.TreeServiceFactory;

import org.lexevs.tree.dao.iterator.ChildTreeNodeIterator;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeTagList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.RenderingDetail;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

import org.LexGrid.concepts.Entity;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.PropertyQualifier;

import org.LexGrid.concepts.Definition;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONValue;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.*;

/**
 * @author EVS Team
 * @version 1.0
 *
 *      Modification history Initial implementation kim.ong@ngc.com
 *
 */


/**
 * The Class DataServlet.
 */

public final class TestServlet extends HttpServlet {
    private static Logger _logger = Logger.getLogger(TestServlet.class);

    /**
     * local constants
     */
    private static final long serialVersionUID = 4L;

    /**
     * Validates the Init and Context parameters, configures authentication URL
     *
     * @throws ServletException if the init parameters are invalid or any other
     *         problems occur during initialisation
     */
    public void init() throws ServletException {

    }

    /**
     * Route the user to the execute method
     *
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        execute(request, response);
    }

    /**
     * Route the user to the execute method
     *
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a Servlet exception occurs
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        execute(request, response);
    }

    public long getSizeOf(List<ResolvedConceptReference> rcr_list) {
		long size = (long) 0;
		for (int i=0; i<rcr_list.size(); i++) {
			ResolvedConceptReference rcr = rcr_list.get(i);
			size = size + SerializationUtil.sizeOf(rcr);
		}
		return size;
	}

    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String action = HTTPUtils.cleanXSS(request.getParameter("action"));
        if (action.compareTo("graphdb") == 0) {
			try {
				String graphdb_uri = null;
				try {
				   graphdb_uri = NCItBrowserProperties.getInstance().getGraphDBURL();
				} catch (Exception ex) {
				   ex.printStackTrace();
				}
				if (graphdb_uri == null || graphdb_uri.indexOf("graph.db.url") != -1) {
				   System.out.println("ERROR: graphdb_uri might have not been configured correctly.");
				   graphdb_uri = "https://graphresolve-dev.nci.nih.gov";
				}

				String scheme = HTTPUtils.cleanXSS((String) request.getParameter("codingscheme"));
                String matchText = HTTPUtils.cleanMatchTextXSS((String) request.getParameter("matchText"));
                String matchAlgorithm = HTTPUtils.cleanMatchTextXSS((String) request.getParameter("matchAlgorithm"));
                System.out.println("TestServlet cs: " + scheme);
                System.out.println("TestServlet algorithm: " + matchAlgorithm);

				request.getSession().setAttribute("cs", scheme);
				request.getSession().setAttribute("matchAlgorithm", matchAlgorithm);
				request.getSession().setAttribute("matchText", matchText);

				LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
				List<ResolvedConceptReference> rcr_list = null;
				boolean getInbound = true;
				int depth = 1;
				String assocName = null;
				Vector schemes = new Vector();
				Vector versions = new Vector();
				schemes.add(scheme);
				versions.add(null);
				String source = null;
				SearchUtilsExt searchUtilsExt = null;
				searchUtilsExt = new SearchUtilsExt(lbSvc, graphdb_uri);

				int lcv = 0;
				Vector v = StringUtils.parseData(matchText, '\n');
				Vector graphdb_results = new Vector();
				for (int i=0; i<v.size(); i++) {
					String match_text = (String) v.elementAt(i);
					lcv++;
					try {
						long ms = System.currentTimeMillis();
						rcr_list = searchUtilsExt.getAssociatedConcepts(schemes, versions, match_text, matchAlgorithm, source, getInbound, depth, assocName);
                        long time_elapsed = System.currentTimeMillis() - ms;
                        long memory_use = getSizeOf(rcr_list);
                        graphdb_results.add(scheme + "|" + match_text
                                                   + "|" + matchAlgorithm
                                                   + "|" + rcr_list.size()
                                                   + "|" + memory_use
                                                   + "|" + time_elapsed);

					} catch (Exception ex) {
						ex.printStackTrace();
						String err_msg = "ERROR: searchUtilsExt.getAssociatedConcepts failed.";
						request.getSession().setAttribute("message", err_msg);
					}
			    }
			    request.getSession().setAttribute("graphdb_results", graphdb_results);

			} catch (Exception ex) {
				ex.printStackTrace();
				String err_msg = "ERROR: Failed to connect to graph db server.";
				request.getSession().setAttribute("message", err_msg);
			}
			String nextJSP = "/pages/graphdb_test_results.jsf";
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request,response);
			return;
		}
	}
}


