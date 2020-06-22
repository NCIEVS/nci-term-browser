package gov.nih.nci.evs.browser.servlet;

import org.json.*;

import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.browser.bean.*;
import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.properties.*;

import org.LexGrid.valueSets.ValueSetDefinition;

import java.io.*;
import java.util.*;
import java.net.URI;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.*;

import gov.nih.nci.evs.browser.properties.*;
import static gov.nih.nci.evs.browser.common.Constants.*;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.valueSets.ValueSetDefinition;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.naming.*;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.*;
import org.apache.log4j.*;
import javax.faces.event.ValueChangeEvent;

import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import javax.servlet.ServletOutputStream;
import org.LexGrid.concepts.*;
import org.lexgrid.valuesets.dto.ResolvedValueSetCodedNodeSet;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.concepts.Definition;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Property;

import gov.nih.nci.evs.browser.bean.*;


import javax.faces.FactoryFinder;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;

import org.LexGrid.LexBIG.Impl.Extensions.tree.json.JsonConverter;
import org.LexGrid.LexBIG.Impl.Extensions.tree.json.JsonConverterFactory;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.*;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.*;


import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


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
 * Modification history
 *     Initial implementation kim.ong@ngc.com
 *
 */

public final class AjaxServlet extends HttpServlet {
    private static Logger _logger = Logger.getLogger(AjaxServlet.class);
    //CodingSchemeDataUtils csdu = null;
    //ValueSetDefUtils vsdu = null;
    boolean show_released_file_button = false;

    /**
     * local constants
     */
    private static final long serialVersionUID = 1L;
    //private static final int  STANDARD_VIEW = 1;
    //private static final int  TERMINOLOGY_VIEW = 2;

    /**
     * Validates the Init and Context parameters, configures authentication URL
     *
     * @throws ServletException if the init parameters are invalid or any other
     *         problems occur during initialisation
     */
    public void init() throws ServletException {
		/*
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
		vsdu = new ValueSetDefUtils(lbSvc, vsd_service);
		csdu = new CodingSchemeDataUtils(lbSvc);
		*/
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

    private static void debugJSONString(String msg, String jsonString) {
    	boolean debug = false;
    	if (! debug)
    		return;
    	_logger.debug(Utils.SEPARATOR);
	    if (msg != null && msg.length() > 0)
	    	_logger.debug(msg);
	    _logger.debug("jsonString: " + jsonString);
	    _logger.debug("jsonString length: " + jsonString.length());
	    Utils.debugJSONString(jsonString);
    }


    public void search_tree(HttpServletResponse response, String node_id,
        String ontology_display_name, String ontology_version, String namespace) {
        try {
            String jsonString = search_tree(node_id,
                ontology_display_name, ontology_version, namespace);

            if (jsonString == null) return;

            JSONObject json = new JSONObject();
            JSONArray rootsArray = new JSONArray(jsonString);
            json.put("root_nodes", rootsArray);

            response.setContentType("text/html");
            response.setHeader("Cache-Control", "no-cache");
            response.getWriter().write(json.toString());
            response.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String get_alt_vs_tree_mode(String mode) {
		if (mode.compareTo(Constants.MODE_EXPAND) == 0) {
			return Constants.MODE_COLLAPSE;
		} else if (mode.compareTo(Constants.MODE_COLLAPSE) == 0) {
			return Constants.MODE_EXPAND;
		}
		return Constants.MODE_COLLAPSE;
	}

    public String search_tree(String node_id,
        String ontology_display_name, String ontology_version, String namespace) throws Exception {

        if (node_id == null || ontology_display_name == null)
            return null;

        Utils.StopWatch stopWatch = new Utils.StopWatch();
//        String max_tree_level_str =
//            NCItBrowserProperties.getProperty(
//                NCItBrowserProperties.MAXIMUM_TREE_LEVEL);
//        int maxLevel = Integer.parseInt(max_tree_level_str);
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        if (ontology_version != null) {
			versionOrTag.setVersion(ontology_version);
		}

/*
        String jsonString =
            CacheController.getTree(
                ontology_display_name, versionOrTag, node_id, namespace);
*/
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        String jsonString = new ViewInHierarchyUtils(lbSvc).getTree(ontology_display_name, ontology_version, node_id, namespace);

        debugJSONString("Section: search_tree", jsonString);

        _logger.debug("search_tree: " + stopWatch.getResult());
        return jsonString;
    }


     private void show_other_versions(HttpServletRequest request, boolean show) {
		 String matchText = HTTPUtils.cleanMatchTextXSS(request.getParameter("matchText"));
		 String algorithm = HTTPUtils.cleanXSS(request.getParameter("algorithm"));
		 String searchTarget = HTTPUtils.cleanXSS(request.getParameter("searchTarget"));
		 String ontologiesToSearchOnStr = HTTPUtils.cleanXSS(request.getParameter("ontology_list"));
		 String action_cs = HTTPUtils.cleanXSS(request.getParameter("csn"));
		 request.getSession().setAttribute("matchText", matchText);
		 request.getSession().setAttribute("algorithm", algorithm);
		 request.getSession().setAttribute("searchTarget", searchTarget);


Vector display_name_vec = (Vector) request.getSession().getAttribute("display_name_vec");
if (display_name_vec == null) {
     display_name_vec = DataUtils.getSortedOntologies();
}

		String new_ontologiesToSearchOnStr = "";
		for (int i = 0; i < display_name_vec.size(); i++) {
			 OntologyInfo info = (OntologyInfo) display_name_vec.elementAt(i);
			 if (info == null) return;
			 if (ontologiesToSearchOnStr.indexOf(info.getLabel()) != -1) { // visible and checked by the user
				 info.setSelected(true);
			 } else if (info.getVisible() && ontologiesToSearchOnStr.indexOf(info.getLabel()) == -1) {
				 info.setSelected(false);
			 }
			 if (info.getSelected()) {
				 new_ontologiesToSearchOnStr = new_ontologiesToSearchOnStr + "|" + info.getLabel();
			 }

			 if (action_cs != null && action_cs.compareTo(info.getCodingScheme()) == 0 && info.getHasMultipleVersions()) {
				 info.setExpanded(show);
			 } else if (action_cs != null && action_cs.compareTo(info.getCodingScheme()) == 0 && !info.isProduction()) {
				 info.setVisible(show);
			 }

		}

		ontologiesToSearchOnStr = new_ontologiesToSearchOnStr;
		String ontologiesToExpandStr = getOntologiesToExpandStr(display_name_vec);
		request.getSession().setAttribute("ontologiesToExpandStr", ontologiesToExpandStr);
        request.getSession().setAttribute("display_name_vec", display_name_vec);
        request.getSession().setAttribute("ontologiesToSearchOnStr", ontologiesToSearchOnStr);

     }

    public String getOntologiesToExpandStr(Vector display_name_vec) {
		StringBuffer buf = new StringBuffer();
		String ontologiesToExpandStr = null;
		buf.append("|");
		if (display_name_vec != null) {
			for (int i = 0; i < display_name_vec.size(); i++) {
				 OntologyInfo info = (OntologyInfo) display_name_vec.elementAt(i);
			     if (info == null) return null;
			     if (info.getExpanded()) {
					 buf.append(info.getLabel() + "|");
				 }
			}
		}
		return buf.toString();
	}

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     *
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

		//[NCITERM-644] Reduce SQL injection AppScan delays.
        request.getSession().removeAttribute("error_msg");

		boolean retval = HTTPUtils.validateRequestParameters(request);
		if (!retval) {
			 try {
				 String nextJSP = "/pages/appscan_response.jsf";
				 RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
				 dispatcher.forward(request,response);
				 return;

			 } catch (Exception ex) {
				 ex.printStackTrace();
			 }
		}

        //Appscan:
        String vocabulary_name = HTTPUtils.cleanXSS(request.getParameter("dictionary"));
        if (vocabulary_name != null) {
			String formal_name = DataUtils.getFormalName(vocabulary_name);
			if (formal_name == null) {
				 try {
					 String nextJSP = "/pages/appscan_response.jsf";
					 String errormsg = "WARNING: Unidentifiable vocabulary name.";
					 request.getSession().setAttribute("error_msg", errormsg);
					 RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
					 dispatcher.forward(request,response);
					 return;

				 } catch (Exception ex) {
					 ex.printStackTrace();
				 }
			}
		}


        // Determine request by attributes
        String action = HTTPUtils.cleanXSS(request.getParameter("action"));
        if (action.equals("export_to_excel")) {
            exportToExcelAction(request, response);
        } else if (action.equals("export_to_csv")) {
            exportToCSVAction(request, response);
        //} else if (action.equals("export_mapping")) {
		} else if (action.equals("exportMapping")) {
            exportMappingAction(request, response);
        } else if (action.equals("export_vsrc")) {
            export_vsrc(request, response);

		} else if (action.equals("export_maps_to_mapping")) {
            exportMapsToMappingAction(request, response);


        } else if (action.compareTo("show") == 0) {
            show_other_versions(request, true);

				 try {
					 String nextJSP = "/pages/multiple_search.jsf";
					 RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
					 dispatcher.forward(request,response);
					 return;

				 } catch (Exception ex) {
					 ex.printStackTrace();
				 }

		} else if (action.compareTo("hide") == 0) {
            show_other_versions(request, false);

				 try {
					 String nextJSP = "/pages/multiple_search.jsf";
					 RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
					 dispatcher.forward(request,response);
					 return;

				 } catch (Exception ex) {
					 ex.printStackTrace();
				 }
        }

		if (action == null) {
			action = "create_src_vs_tree";
		}

		if (action.compareTo("create_alt_src_vs_tree") == 0) {
			action = "create_src_vs_tree";
		}

		if (action.compareTo("create_alt_cs_vs_tree") == 0) {
			action = "create_cs_vs_tree";
		}


		if (action.compareTo("values") == 0) {
			resolveValueSetAction(request, response);
			return;
		}

		if (action.compareTo("download") == 0) {
			downloadValueSetAction(request, response);
			return;
		}

		if (action.compareTo("versions") == 0) {
			selectCSVersionAction(request, response);
			return;
		}

		if (action.compareTo("xmldefinitions") == 0) {
			exportVSDToXMLAction(request, response);
			return;
		}

        String node_id = HTTPUtils.cleanXSS(request.getParameter("ontology_node_id"));// DataConstants.ONTOLOGY_NODE_ID);
        String ns = HTTPUtils.cleanXSS(request.getParameter("ontology_node_ns"));// DataConstants.ONTOLOGY_NODE_ID);

        String ontology_display_name =
            HTTPUtils.cleanXSS(request.getParameter("ontology_display_name"));// DataConstants.ONTOLOGY_DISPLAY_NAME);

        String ontology_version = HTTPUtils.cleanXSS(request.getParameter("version"));
        if (ontology_version == null) {
			ontology_version = DataUtils.getVocabularyVersionByTag(ontology_display_name, "PRODUCTION");
		}

        long ms = System.currentTimeMillis();
        if (action.equals("expand_tree")) {
            if (node_id != null && ontology_display_name != null) {
                response.setContentType("text/html");
                response.setHeader("Cache-Control", "no-cache");
                JSONObject json = new JSONObject();
                JSONArray nodesArray = null;

                try {

					 if (node_id.indexOf("_dot_") == -1) {
						 if (ns == null || ns.compareTo("null") == 0 || ns.compareTo("undefined") == 0) {
							 LexBIGService lb_svc = RemoteServerUtil.createLexBIGService();
							 ns = new ConceptDetails(lb_svc).getNamespaceByCode(ontology_display_name, null, node_id);
						 }
					 } else {
						 int idx = node_id.indexOf("_dot_");
						 String parent_code = node_id.substring(0, idx);
						 LexBIGService lb_svc = RemoteServerUtil.createLexBIGService();
						 ns = new ConceptDetails(lb_svc).getNamespaceByCode(ontology_display_name, null, parent_code);
					 }


                    nodesArray =
                        CacheController.getInstance().getSubconcepts(
                            ontology_display_name, ontology_version, node_id, ns);

                    if (nodesArray != null) {
                        json.put("nodes", nodesArray);
                    }

                } catch (Exception e) {
					e.printStackTrace();
                }
                response.getWriter().write(json.toString());
            }
        }

        if (action.equals("export_mapping")) {
            export_mapping_data(request, response);
        } else if (action.equals("export_mapping_search")) {
            export_mapping_search(request, response);
        } else if (action.equals("search_all_value_sets")) {
            search_all_value_sets(request, response);

        } else if (action.equals("search_value_set")) {
            search_value_set(request, response);
        } else if (action.equals("search_downloaded_value_set")) {
            search_downloaded_value_set(request, response);

        } else if (action.equals("create_src_vs_tree")) {
			//KLO 031214
			request.getSession().setAttribute("nav_type", "valuesets");
            create_src_vs_tree(request, response);
        } else if (action.equals("create_cs_vs_tree")) {
			request.getSession().setAttribute("nav_type", "valuesets");
            create_cs_vs_tree(request, response);

        } else if (action.equals("search_hierarchy")) {

            search_hierarchy(request, response, node_id, ontology_display_name, ontology_version, ns);


        } else if (action.equals("search_tree")) {

            search_tree(response, node_id, ontology_display_name, ontology_version, ns);
        } else if (action.equals("build_tree")) {

            if (ontology_display_name == null)
                ontology_display_name = CODING_SCHEME_NAME;

            response.setContentType("text/html");
            response.setHeader("Cache-Control", "no-cache");

			long ms1 = System.currentTimeMillis();
			//090215
				JSONObject json = new JSONObject();
				JSONArray nodesArray = null;// new JSONArray();
				try {
					nodesArray = new JSONArray(CacheController.getInstance().getRootJSONString(ontology_display_name, ontology_version));
					if (nodesArray != null) {
						json.put("root_nodes", nodesArray);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				response.getWriter().write(json.toString());
				System.out.println("Run time (milliseconds): " + (System.currentTimeMillis() - ms1));
            return;

        } else if (action.equals("build_vs_tree")) {

            if (ontology_display_name == null)
                ontology_display_name = CODING_SCHEME_NAME;

            response.setContentType("text/html");
            response.setHeader("Cache-Control", "no-cache");
            JSONObject json = new JSONObject();
            JSONArray nodesArray = null;// new JSONArray();
            try {
				String codingSchemeVersion = null;
                nodesArray =
                    CacheController.getInstance().getRootValueSets(
                        ontology_display_name, codingSchemeVersion);
                if (nodesArray != null) {
                    json.put("root_nodes", nodesArray);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            response.getWriter().write(json.toString());
            _logger.debug("Run time (milliseconds): "
                + (System.currentTimeMillis() - ms));
            return;

        } else if (action.equals("expand_vs_tree")) {
            if (node_id != null && ontology_display_name != null) {
                response.setContentType("text/html");
                response.setHeader("Cache-Control", "no-cache");
                JSONObject json = new JSONObject();
                JSONArray nodesArray = null;

                try {

                    nodesArray =
                        CacheController.getInstance().getSubValueSets(
                            ontology_display_name, ontology_version, node_id);


                    if (nodesArray != null) {
                       json.put("nodes", nodesArray);
                    }

                } catch (Exception e) {
                }
                response.getWriter().write(json.toString());
                _logger.debug("Run time (milliseconds): "
                    + (System.currentTimeMillis() - ms));
            }


        } else if (action.equals("expand_entire_vs_tree")) {
            if (node_id != null && ontology_display_name != null) {
                response.setContentType("text/html");
                response.setHeader("Cache-Control", "no-cache");
                JSONObject json = new JSONObject();
                JSONArray nodesArray = null;

                try {
                    nodesArray =
                        CacheController.getInstance().getSourceValueSetTree(
                            ontology_display_name, ontology_version, true);
                    if (nodesArray != null) {
                        json.put("root_nodes", nodesArray);
                    }
                } catch (Exception e) {
					e.printStackTrace();
                }
                response.getWriter().write(json.toString());
                _logger.debug("Run time (milliseconds): "
                    + (System.currentTimeMillis() - ms));
            }

        } else if (action.equals("expand_entire_cs_vs_tree")) {
            //if (node_id != null && ontology_display_name != null) {
                response.setContentType("text/html");
                response.setHeader("Cache-Control", "no-cache");
                JSONObject json = new JSONObject();
                JSONArray nodesArray = null;

                try {
                    nodesArray =
                        CacheController.getInstance().getCodingSchemeValueSetTree(
                            ontology_display_name, ontology_version, true);
                    if (nodesArray != null) {
                        json.put("root_nodes", nodesArray);
                    }

                } catch (Exception e) {
                }
                response.getWriter().write(json.toString());
                _logger.debug("Run time (milliseconds): "
                    + (System.currentTimeMillis() - ms));
            //}


        } else if (action.equals("build_cs_vs_tree")) {

            response.setContentType("text/html");
            response.setHeader("Cache-Control", "no-cache");
            JSONObject json = new JSONObject();
            JSONArray nodesArray = null;// new JSONArray();
            try {
				//HashMap getRootValueSets(String codingSchemeURN)
				String codingSchemeVersion = null;
                nodesArray =
                    CacheController.getInstance().getRootValueSets(true);

                if (nodesArray != null) {
                    json.put("root_nodes", nodesArray);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            response.getWriter().write(json.toString());

            _logger.debug("Run time (milliseconds): "
                + (System.currentTimeMillis() - ms));
            return;
        } else if (action.equals("expand_cs_vs_tree")) {

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			JSONObject json = new JSONObject();
			JSONArray nodesArray = null;

			//String vsd_uri = ValueSetHierarchy.getValueSetURI(node_id);
			node_id = StringUtils.getStringComponent(node_id, "$", 0);
			String vsd_uri = StringUtils.getStringComponent(node_id, "$", 1);

            //if (node_id != null && ontology_display_name != null) {
			if (node_id != null) {
				ValueSetDefinition vsd = DataUtils.getValueSetHierarchy().findValueSetDefinitionByURI(vsd_uri);
				if (vsd == null) {
				   try {
					   //
					    nodesArray = CacheController.getInstance().getRootValueSets(node_id, null);
						//nodesArray = CacheController.getInstance().getRootValueSets(node_id, null); //find roots (by source)

						if (nodesArray != null) {
							json.put("nodes", nodesArray);
						}

					} catch (Exception e) {
					}
			    } else {
					try {
						nodesArray =
							CacheController.getInstance().getSubValueSets(
								node_id, null, vsd_uri);

						if (nodesArray != null) {
							json.put("nodes", nodesArray);
						}

					} catch (Exception e) {
					}
				}

                response.getWriter().write(json.toString());
                _logger.debug("Run time (milliseconds): "
                    + (System.currentTimeMillis() - ms));
            }


        } else if (action.equals("build_src_vs_tree")) {


            response.setContentType("text/html");
            response.setHeader("Cache-Control", "no-cache");
            JSONObject json = new JSONObject();
            JSONArray nodesArray = null;// new JSONArray();
            try {
				//HashMap getRootValueSets(String codingSchemeURN)
				String codingSchemeVersion = null;
                nodesArray =
                    //CacheController.getInstance().getRootValueSets(true, true);
                    CacheController.getInstance().build_src_vs_tree();

                if (nodesArray != null) {
                    json.put("root_nodes", nodesArray);
                } else {
					_logger.debug("*** AjaxServlet build_src_vs_tree returns null???");
				}



            } catch (Exception e) {
                e.printStackTrace();
            }

            response.getWriter().write(json.toString());
            _logger.debug("Run time (milliseconds): "
                + (System.currentTimeMillis() - ms));
            return;

        } else if (action.equals("expand_src_vs_tree")) {

            if (node_id != null && ontology_display_name != null) {
                response.setContentType("text/html");
                response.setHeader("Cache-Control", "no-cache");
                JSONObject json = new JSONObject();
                JSONArray nodesArray = null;
				nodesArray = CacheController.getInstance().expand_src_vs_tree(node_id);

                try {
                    if (nodesArray != null) {
                        json.put("nodes", nodesArray);
                    }
                } catch (Exception e) {
					e.printStackTrace();

                }
                response.getWriter().write(json.toString());
                _logger.debug("Run time (milliseconds): "
                    + (System.currentTimeMillis() - ms));
            }
        } else if (action.equals("view_graph")) {
            String scheme =  HTTPUtils.cleanXSS(request.getParameter("scheme"));
            String version =  HTTPUtils.cleanXSS(request.getParameter("version"));
            ns =  HTTPUtils.cleanXSS(request.getParameter("ns"));
            String code =  HTTPUtils.cleanXSS(request.getParameter("code"));
            String type =  HTTPUtils.cleanXSS(request.getParameter("type"));

			if (type == null) {
				type = "ALL";
			}

            view_graph(request, response, scheme, version, ns, code, type);

        } else if (action.equals("reset_graph")) {
            String id =  HTTPUtils.cleanXSS(request.getParameter("id"));
            String scheme = (String) request.getSession().getAttribute("scheme");
            String version = (String) request.getSession().getAttribute("version");
            ns = (String) request.getSession().getAttribute("ns");
            String nodes_and_edges = (String) request.getSession().getAttribute("nodes_and_edges");
            String code = findCodeInGraph(nodes_and_edges, id);
            view_graph(request, response, scheme, version, ns, code, "ALL");

        } else if (action.equals("cart")) {
			processCartActions(request, response);
        } else if (action.equals("addtocart")) {
			addToCart(request, response);
		}

	}

    public String findCodeInGraph(String nodes_and_edges, String id) {
		String target = "{id: " + id + ", label:";
		int n = nodes_and_edges.indexOf(target);
		if (n == -1) return null;
		String t = nodes_and_edges.substring(n+target.length(), nodes_and_edges.length());
		target = ")'}";
		n = t.indexOf(target);
		t = t.substring(0, n);
		n = t.lastIndexOf("(");
		t = t.substring(n+1, t.length());
		return t;
	}


    private boolean isRoot(JSONArray rootsArray, String code) {
        for (int i = 0; i < rootsArray.length(); i++) {
            String node_id = null;
            try {
                JSONObject node = rootsArray.getJSONObject(i);
                node_id = (String) node.get(CacheController.ONTOLOGY_NODE_ID);
                if (node_id.compareTo(code) == 0)
                    return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean _debug = false;
    //private static StringBuffer _debugBuffer = null;

    public void println(PrintWriter out, String text) {
		/*
        if (_debug) {
            _logger.debug("DBG: " + text);
            _debugBuffer.append(text + "\n");
        }
        */
        out.println(text);
    }


    public void search_hierarchy(HttpServletRequest request, HttpServletResponse response, String node_id,
        String ontology_display_name, String ontology_version, String namespace) {

      Enumeration parameters = request.getParameterNames();
      String param = null;
      while (parameters.hasMoreElements())
      {
         param = (String) parameters.nextElement();
         String paramValue = HTTPUtils.cleanXSS((String) request.getParameter(param));
      }
      response.setContentType("text/html");
      PrintWriter out = null;

      try {
      	  out = response.getWriter();
      } catch (Exception ex) {
		  ex.printStackTrace();
		  return;
	  }
/*
      if (_debug) {
          _debugBuffer = new StringBuffer();
      }
*/
      String localName = DataUtils.getLocalName(ontology_display_name);
      String formalName = DataUtils.getFormalName(localName);
      String term_browser_version = DataUtils.getMetadataValue(formalName, ontology_version, "term_browser_version");
      String display_name = DataUtils.getMetadataValue(formalName, ontology_version, "display_name");

      println(out, "");
      println(out, "<script type=\"text/javascript\" src=\"/ncitbrowser/js/yui/yahoo-min.js\" ></script>");
      println(out, "<script type=\"text/javascript\" src=\"/ncitbrowser/js/yui/event-min.js\" ></script>");
      println(out, "<script type=\"text/javascript\" src=\"/ncitbrowser/js/yui/dom-min.js\" ></script>");
      println(out, "<script type=\"text/javascript\" src=\"/ncitbrowser/js/yui/animation-min.js\" ></script>");
      println(out, "<script type=\"text/javascript\" src=\"/ncitbrowser/js/yui/container-min.js\" ></script>");
      println(out, "<script type=\"text/javascript\" src=\"/ncitbrowser/js/yui/connection-min.js\" ></script>");
      println(out, "<script type=\"text/javascript\" src=\"/ncitbrowser/js/yui/treeview-min.js\" ></script>");

      println(out, "");
      println(out, "");
      println(out, "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
      println(out, "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
      println(out, "  <head>");
      println(out, "  <title>Vocabulary Hierarchy</title>");
      println(out, "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
      println(out, "  <link rel=\"stylesheet\" type=\"text/css\" href=\"/ncitbrowser/css/styleSheet.css\" />");
      println(out, "  <link rel=\"shortcut icon\" href=\"/ncitbrowser/favicon.ico\" type=\"image/x-icon\" />");
      println(out, "  <link rel=\"stylesheet\" type=\"text/css\" href=\"/ncitbrowser/css/yui/fonts.css\" />");
      println(out, "  <link rel=\"stylesheet\" type=\"text/css\" href=\"/ncitbrowser/css/yui/grids.css\" />");
      println(out, "  <link rel=\"stylesheet\" type=\"text/css\" href=\"/ncitbrowser/css/yui/code.css\" />");
      println(out, "  <link rel=\"stylesheet\" type=\"text/css\" href=\"/ncitbrowser/css/yui/tree.css\" />");


      println(out, "  <script type=\"text/javascript\" src=\"/ncitbrowser/js/script.js\"></script>");
      println(out, "  <script type=\"text/javascript\" src=\"/ncitbrowser/js/search.js\"></script>");
      println(out, "  <script type=\"text/javascript\" src=\"/ncitbrowser/js/dropdown.js\"></script>");


      println(out, "");
      println(out, "  <script language=\"JavaScript\">");
      println(out, "");
      println(out, "    var tree;");
      println(out, "    var nodeIndex;");
      println(out, "    var rootDescDiv;");
      println(out, "    var emptyRootDiv;");
      println(out, "    var treeStatusDiv;");
      println(out, "    var nodes = [];");
      println(out, "    var currOpener;");
      println(out, "");
      println(out, "    function load(url,target) {");
      println(out, "      if (target != '')");
      println(out, "        target.window.location.href = url;");
      println(out, "      else");
      println(out, "        window.location.href = url;");
      println(out, "    }");
      println(out, "");
      println(out, "    function init() {");
      println(out, "");
      println(out, "      rootDescDiv = new YAHOO.widget.Module(\"rootDesc\", {visible:false} );");
      println(out, "      resetRootDesc();");
      println(out, "");
      println(out, "      emptyRootDiv = new YAHOO.widget.Module(\"emptyRoot\", {visible:true} );");
      println(out, "      resetEmptyRoot();");
      println(out, "");
      println(out, "      treeStatusDiv = new YAHOO.widget.Module(\"treeStatus\", {visible:true} );");
      println(out, "      resetTreeStatus();");
      println(out, "");
      println(out, "      currOpener = opener;");
      println(out, "      initTree();");
      println(out, "    }");
      println(out, "");
      println(out, "    function addTreeNode(rootNode, nodeInfo) {");

      out.println("      var newNodeDetails = \"javascript:onClickTreeNode('\"");
      out.println("                         + nodeInfo.ontology_node_id");
      out.println("                         + \"','\"");
      out.println("                         + nodeInfo.ontology_node_ns");
      out.println("                         + \"');\";");

      out.println("      ");
      out.println("      var newNodeData = { label:nodeInfo.ontology_node_name, id:nodeInfo.ontology_node_id, ns:nodeInfo.ontology_node_ns, href:newNodeDetails };");


      println(out, "      var newNode = new YAHOO.widget.TextNode(newNodeData, rootNode, false);");
      println(out, "      if (nodeInfo.ontology_node_child_count > 0) {");
      println(out, "        newNode.setDynamicLoad(loadNodeData);");
      println(out, "      }");
      println(out, "    }");
      println(out, "");
      println(out, "    function buildTree(ontology_node_id, ontology_display_name) {");
      println(out, "      var handleBuildTreeSuccess = function(o) {");
      println(out, "        var respTxt = o.responseText;");
      println(out, "        var respObj = eval('(' + respTxt + ')');");
      println(out, "        if ( typeof(respObj) != \"undefined\") {");
      println(out, "          if ( typeof(respObj.root_nodes) != \"undefined\") {");
      println(out, "            var root = tree.getRoot();");
      println(out, "            if (respObj.root_nodes.length == 0) {");
      println(out, "              showEmptyRoot();");
      println(out, "            }");
      println(out, "            else {");
      println(out, "              for (var i=0; i < respObj.root_nodes.length; i++) {");
      println(out, "                var nodeInfo = respObj.root_nodes[i];");
      println(out, "                var expand = false;");
      println(out, "                addTreeNode(root, nodeInfo, expand);");
      println(out, "              }");
      println(out, "            }");
      println(out, "");
      println(out, "            tree.draw();");
      println(out, "          }");
      println(out, "        }");
      println(out, "        resetTreeStatus();");
      println(out, "      }");
      println(out, "");
      println(out, "      var handleBuildTreeFailure = function(o) {");
      println(out, "        resetTreeStatus();");
      println(out, "        resetEmptyRoot();");
      println(out, "        alert('responseFailure: ' + o.statusText);");
      println(out, "      }");
      println(out, "");
      println(out, "      var buildTreeCallback =");
      println(out, "      {");
      println(out, "        success:handleBuildTreeSuccess,");
      println(out, "        failure:handleBuildTreeFailure");
      println(out, "      };");
      println(out, "");
      println(out, "      if (ontology_display_name!='') {");
      println(out, "        resetEmptyRoot();");
      println(out, "");
      println(out, "        showTreeLoadingStatus();");
      println(out, "        var ontology_source = null;");
      println(out, "        var ontology_version = document.forms[\"pg_form\"].ontology_version.value;");
      println(out, "        var request = YAHOO.util.Connect.asyncRequest('GET','/ncitbrowser/ajax?action=build_tree&ontology_node_id=' +ontology_node_id+'&ontology_display_name='+ontology_display_name+'&version='+ontology_version+'&ontology_source='+ontology_source,buildTreeCallback);");
      println(out, "      }");
      println(out, "    }");
      println(out, "");
      println(out, "    function resetTree(ontology_node_id, ontology_display_name) {");
      println(out, "");
      println(out, "      var handleResetTreeSuccess = function(o) {");
      println(out, "        var respTxt = o.responseText;");
      println(out, "        var respObj = eval('(' + respTxt + ')');");
      println(out, "        if ( typeof(respObj) != \"undefined\") {");
      println(out, "          if ( typeof(respObj.root_node) != \"undefined\") {");
      println(out, "            var root = tree.getRoot();");

      out.println("      var nodeDetails = \"javascript:onClickTreeNode('\" ");
      out.println("                         + respObj.root_node.ontology_node_id ");
      out.println("                         + \"','\"");
      out.println("                         + respObj.root_node.ontology_node_ns ");
      out.println("                         + \"');\";");
      out.println("      var rootNodeData = { label:respObj.root_node.ontology_node_name, id:respObj.root_node.ontology_node_id, ns:respObj.root_node.ontology_node_ns, href:nodeDetails };");

      println(out, "            var expand = false;");
      println(out, "            if (respObj.root_node.ontology_node_child_count > 0) {");
      println(out, "              expand = true;");
      println(out, "            }");
      println(out, "            var ontRoot = new YAHOO.widget.TextNode(rootNodeData, root, expand);");
      println(out, "");
      println(out, "            if ( typeof(respObj.child_nodes) != \"undefined\") {");
      println(out, "              for (var i=0; i < respObj.child_nodes.length; i++) {");
      println(out, "                var nodeInfo = respObj.child_nodes[i];");
      println(out, "                addTreeNode(ontRoot, nodeInfo);");
      println(out, "              }");
      println(out, "            }");
      println(out, "            tree.draw();");
      println(out, "            setRootDesc(respObj.root_node.ontology_node_name, ontology_display_name);");
      println(out, "          }");
      println(out, "        }");
      println(out, "        resetTreeStatus();");
      println(out, "      }");
      println(out, "");
      println(out, "      var handleResetTreeFailure = function(o) {");
      println(out, "        resetTreeStatus();");
      println(out, "        alert('responseFailure: ' + o.statusText);");
      println(out, "      }");
      println(out, "");
      println(out, "      var resetTreeCallback =");
      println(out, "      {");
      println(out, "        success:handleResetTreeSuccess,");
      println(out, "        failure:handleResetTreeFailure");
      println(out, "      };");
      println(out, "      if (ontology_node_id!= '') {");
      println(out, "        showTreeLoadingStatus();");
      println(out, "        var ontology_source = null;");
      println(out, "        var ontology_version = document.forms[\"pg_form\"].ontology_version.value;");
      println(out, "        var request = YAHOO.util.Connect.asyncRequest('GET','/ncitbrowser/ajax?action=reset_tree&ontology_node_id=' +ontology_node_id+'&ontology_display_name='+ontology_display_name + '&version='+ ontology_version +'&ontology_source='+ontology_source,resetTreeCallback);");
      println(out, "      }");
      println(out, "    }");
      println(out, "");

      println(out, "    function onClickTreeNode(ontology_node_id, ontology_node_ns) {");
// KLO, 082415
      println(out, "      if (ontology_node_id.indexOf(\"_dot_\") != -1) return;");
      println(out, "      var ontology_display_name = document.forms[\"pg_form\"].ontology_display_name.value;");
      println(out, "      var ontology_version = document.forms[\"pg_form\"].ontology_version.value;");
      println(out, "      load('/ncitbrowser/ConceptReport.jsp?dictionary='+ ontology_display_name + '&version='+ ontology_version + '&code=' + ontology_node_id + '&ns=' + ontology_node_ns, currOpener);");
      println(out, "    }");

      println(out, "");
      println(out, "    function onClickViewEntireOntology(ontology_display_name) {");
      println(out, "      var ontology_display_name = document.pg_form.ontology_display_name.value;");
      println(out, "      tree = new YAHOO.widget.TreeView(\"treecontainer\");");
      println(out, "      tree.draw();");
      println(out, "      resetRootDesc();");
      println(out, "      buildTree('', ontology_display_name);");
      println(out, "    }");
      println(out, "");
      println(out, "    function initTree() {");
      println(out, "");
      println(out, "      tree = new YAHOO.widget.TreeView(\"treecontainer\");");
      println(out, "      var ontology_node_id = document.forms[\"pg_form\"].ontology_node_id.value;");
      println(out, "      var ontology_display_name = document.forms[\"pg_form\"].ontology_display_name.value;");
      println(out, "");
      println(out, "      if (ontology_node_id == null || ontology_node_id == \"null\")");
      println(out, "      {");
      println(out, "          buildTree(ontology_node_id, ontology_display_name);");
      println(out, "      }");
      println(out, "      else");
      println(out, "      {");
      println(out, "          searchTree(ontology_node_id, ontology_display_name);");
      println(out, "      }");
      println(out, "    }");
      println(out, "");
      println(out, "    function initRootDesc() {");
      println(out, "      rootDescDiv.setBody('');");
      println(out, "      initRootDesc.show();");
      println(out, "      rootDescDiv.render();");
      println(out, "    }");
      println(out, "");
      println(out, "    function resetRootDesc() {");
      println(out, "      rootDescDiv.hide();");
      println(out, "      rootDescDiv.setBody('');");
      println(out, "      rootDescDiv.render();");
      println(out, "    }");
      println(out, "");
      println(out, "    function resetEmptyRoot() {");
      println(out, "      emptyRootDiv.hide();");
      println(out, "      emptyRootDiv.setBody('');");
      println(out, "      emptyRootDiv.render();");
      println(out, "    }");
      println(out, "");
      println(out, "    function resetTreeStatus() {");
      println(out, "      treeStatusDiv.hide();");
      println(out, "      treeStatusDiv.setBody('');");
      println(out, "      treeStatusDiv.render();");
      println(out, "    }");
      println(out, "");
      println(out, "    function showEmptyRoot() {");
      println(out, "      emptyRootDiv.setBody(\"<span class='instruction_text'>No root nodes available.</span>\");");
      println(out, "      emptyRootDiv.show();");
      println(out, "      emptyRootDiv.render();");
      println(out, "    }");
      println(out, "");
      println(out, "    function showNodeNotFound(node_id) {");
      println(out, "      //emptyRootDiv.setBody(\"<span class='instruction_text'>Concept with code \" + node_id + \" not found in the hierarchy.</span>\");");
      println(out, "      emptyRootDiv.setBody(\"<span class='instruction_text'>Concept not part of the parent-child hierarchy in this source; check other relationships.</span>\");");
      println(out, "      emptyRootDiv.show();");
      println(out, "      emptyRootDiv.render();");
      println(out, "    }");
      println(out, "    ");
      println(out, "    function showPartialHierarchy() {");
      println(out, "      rootDescDiv.setBody(\"<span class='instruction_text'>(Note: This tree only shows partial hierarchy.)</span>\");");
      println(out, "      rootDescDiv.show();");
      println(out, "      rootDescDiv.render();");
      println(out, "    }");
      println(out, "");
      println(out, "    function showTreeLoadingStatus() {");
      println(out, "      treeStatusDiv.setBody(\"<img src='/ncitbrowser/images/loading.gif'/> <span class='instruction_text'>Building tree ...</span>\");");
      println(out, "      treeStatusDiv.show();");
      println(out, "      treeStatusDiv.render();");
      println(out, "    }");
      println(out, "");
      println(out, "    function showTreeDrawingStatus() {");
      println(out, "      treeStatusDiv.setBody(\"<img src='/ncitbrowser/images/loading.gif'/> <span class='instruction_text'>Drawing tree ...</span>\");");
      println(out, "      treeStatusDiv.show();");
      println(out, "      treeStatusDiv.render();");
      println(out, "    }");
      println(out, "");
      println(out, "    function showSearchingTreeStatus() {");
      println(out, "      treeStatusDiv.setBody(\"<img src='/ncitbrowser/images/loading.gif'/> <span class='instruction_text'>Searching tree... Please wait.</span>\");");
      println(out, "      treeStatusDiv.show();");
      println(out, "      treeStatusDiv.render();");
      println(out, "    }");
      println(out, "");
      println(out, "    function showConstructingTreeStatus() {");
      println(out, "      treeStatusDiv.setBody(\"<img src='/ncitbrowser/images/loading.gif'/> <span class='instruction_text'>Constructing tree... Please wait.</span>\");");
      println(out, "      treeStatusDiv.show();");
      println(out, "      treeStatusDiv.render();");
      println(out, "    }");
      println(out, "");

      out.println("    function loadNodeData(node, fnLoadComplete) {");
      out.println("      var id = node.data.id;");
      out.println("      var ns = node.data.ns;");
      out.println("      var responseSuccess = function(o)");
      out.println("      {");
      out.println("        var path;");
      out.println("        var dirs;");
      out.println("        var files;");
      out.println("        var respTxt = o.responseText;");
      out.println("        var respObj = eval('(' + respTxt + ')');");
      out.println("        var fileNum = 0;");
      out.println("        var categoryNum = 0;");
      out.println("        var pos = id.indexOf(\"_dot_\");");
      out.println("        if ( typeof(respObj.nodes) != \"undefined\") {");
      out.println("			if (pos == -1) {");
      out.println("				for (var i=0; i < respObj.nodes.length; i++) {");
      out.println("				    var name = respObj.nodes[i].ontology_node_name;");
      out.println("			        var nodeDetails = \"javascript:onClickTreeNode('\"");
      out.println("								 + respObj.nodes[i].ontology_node_id");
      out.println("								 + \"','\"");
      out.println("								 + respObj.nodes[i].ontology_node_ns");
      out.println("								 + \"');\";");
      out.println("");
      out.println("					if (respObj.nodes[i].ontology_node_child_count > 0) {");
      out.println("					    var newNodeData = { label:name, id:respObj.nodes[i].ontology_node_id, ns:respObj.nodes[i].ontology_node_ns, href:nodeDetails };");
      out.println("						var newNode = new YAHOO.widget.TextNode(newNodeData, node, false);");
      out.println("						newNode.setDynamicLoad(loadNodeData);");
      out.println("						fnLoadComplete();");
      out.println("					} else {");
      out.println("			            var newNodeData = { isLeaf:true, label:name, id:respObj.nodes[i].ontology_node_id, ns:respObj.nodes[i].ontology_node_ns, href:nodeDetails };");
      out.println("					    var newNode = new YAHOO.widget.TextNode(newNodeData, node, false);");
      out.println("					    newNode.isLeaf = true;");
      out.println("					    newNode.setDynamicLoad(loadNodeData);");
      out.println("					    fnLoadComplete();");
      out.println("					}");
      out.println("");
      out.println("				}");
      out.println("");
      out.println("			} else {");
      out.println("			    var parent = node.parent;");
      out.println("				for (var i=0; i < respObj.nodes.length; i++) {");
      out.println("					  var name = respObj.nodes[i].ontology_node_name;");
      out.println("");
      out.println("				      var nodeDetails = \"javascript:onClickTreeNode('\"");
      out.println("									 + respObj.nodes[i].ontology_node_id");
      out.println("									 + \"','\"");
      out.println("									 + respObj.nodes[i].ontology_node_ns");
      out.println("									 + \"');\";");
      out.println("");
      out.println("					if (respObj.nodes[i].ontology_node_child_count > 0) {");
      out.println("					    var newNodeData = { label:name, id:respObj.nodes[i].ontology_node_id, ns:respObj.nodes[i].ontology_node_ns, href:nodeDetails };");
      out.println("						var newNode = new YAHOO.widget.TextNode(newNodeData, parent, true);");
      out.println("						newNode.setDynamicLoad(loadNodeData);");
      out.println("					} else {");
      out.println("			            var newNodeData = { isLeaf:true, label:name, id:respObj.nodes[i].ontology_node_id, ns:respObj.nodes[i].ontology_node_ns, href:nodeDetails };");
      out.println("					    var newNode = new YAHOO.widget.TextNode(newNodeData, parent, true);");
      out.println("					    newNode.setDynamicLoad(loadNodeData);");
      //out.println("					    loadNodeData(newNode, fnLoadComplete);");
      out.println("					    fnLoadComplete();");
      out.println("					}");
      out.println("");
      out.println("				}");
      out.println("				tree.removeNode(node, true);");
      out.println("			}");
      out.println("        }");
      out.println("        fnLoadComplete();");
      out.println("      }");

      println(out, "");
      println(out, "      var responseFailure = function(o){");
      println(out, "        alert('responseFailure: ' + o.statusText);");
      println(out, "      }");
      println(out, "");
      println(out, "      var callback =");
      println(out, "      {");
      println(out, "        success:responseSuccess,");
      println(out, "        failure:responseFailure");
      println(out, "      };");
      println(out, "");

      println(out, "      var ontology_display_name = document.forms[\"pg_form\"].ontology_display_name.value;");
      println(out, "      var ontology_version = document.forms[\"pg_form\"].ontology_version.value;");
      println(out, "      var cObj = YAHOO.util.Connect.asyncRequest('GET','/ncitbrowser/ajax?action=expand_tree&ontology_node_id=' +id+'&ontology_node_ns='+ns+'&ontology_display_name='+ontology_display_name+'&version='+ontology_version,callback);");

      println(out, "    }");
      println(out, "");
      println(out, "    function setRootDesc(rootNodeName, ontology_display_name) {");
      println(out, "      var newDesc = \"<span class='instruction_text'>Root set to <b>\" + rootNodeName + \"</b></span>\";");
      println(out, "      rootDescDiv.setBody(newDesc);");
      println(out, "      var footer = \"<a onClick='javascript:onClickViewEntireOntology();' href='#' class='link_text'>view full ontology}</a>\";");
      println(out, "      rootDescDiv.setFooter(footer);");
      println(out, "      rootDescDiv.show();");
      println(out, "      rootDescDiv.render();");
      println(out, "    }");
      println(out, "");
      println(out, "");
      println(out, "    function searchTree(ontology_node_id, ontology_display_name) {");
      println(out, "");

      println(out, "      var root = tree.getRoot();");
      LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
      String entity_ns = new ConceptDetails(lbSvc).getNamespaceByCode(ontology_display_name, null, node_id);
      new ViewInHierarchyUtils(lbSvc).printTree(out, ontology_display_name, ontology_version, node_id, entity_ns);
      //new ViewInHierarchyUtils(lbSvc).printTree(out, ontology_display_name, ontology_version, node_id, namespace);

      //println(out, "             showPartialHierarchy();");
      println(out, "             tree.draw();");

      println(out, "    }");
      println(out, "");
      println(out, "");
      println(out, "    function addTreeBranch(ontology_node_id, rootNode, nodeInfo) {");

      out.println("      var newNodeDetails = \"javascript:onClickTreeNode('\" ");
      out.println("                         + nodeInfo.ontology_node_id ");
      //out.println("                               out.println("                         + \"','\"");
      out.println("                         + \"','\"");

      out.println("                         + nodeInfo.ontology_node_ns ");
      out.println("                         + \"');\";");
      out.println("      ");
      out.println("      var newNodeData = { label:nodeInfo.ontology_node_name, id:nodeInfo.ontology_node_id, ns:nodeInfo.ontology_node_ns, href:newNodeDetails };");


      println(out, "");
      println(out, "      var expand = false;");
      println(out, "      var childNodes = nodeInfo.children_nodes;");
      println(out, "");
      println(out, "      if (childNodes.length > 0) {");
      println(out, "          expand = true;");
      println(out, "      }");
      println(out, "      var newNode = new YAHOO.widget.TextNode(newNodeData, rootNode, expand);");
      println(out, "      if (nodeInfo.ontology_node_id == ontology_node_id) {");
      println(out, "          newNode.labelStyle = \"ygtvlabel_highlight\";");
      println(out, "      }");
      println(out, "");
      println(out, "      if (nodeInfo.ontology_node_id == ontology_node_id) {");
      println(out, "         newNode.isLeaf = true;");
      println(out, "         if (nodeInfo.ontology_node_child_count > 0) {");
      println(out, "             newNode.isLeaf = false;");
      println(out, "             newNode.setDynamicLoad(loadNodeData);");
      println(out, "         } else {");
      println(out, "             tree.draw();");
      println(out, "         }");
      println(out, "");
      println(out, "      } else {");
      println(out, "          if (nodeInfo.ontology_node_id != ontology_node_id) {");
      println(out, "          if (nodeInfo.ontology_node_child_count == 0 && nodeInfo.ontology_node_id != ontology_node_id) {");
      println(out, "        newNode.isLeaf = true;");
      println(out, "          } else if (childNodes.length == 0) {");
      println(out, "        newNode.setDynamicLoad(loadNodeData);");
      println(out, "          }");
      println(out, "        }");
      println(out, "      }");
      println(out, "");
      println(out, "      tree.draw();");
      //println(out, "      for (var i=0; i < childNodes.length; i++) {");
      //println(out, "         var childnodeInfo = childNodes[i];");
      //println(out, "         addTreeBranch(ontology_node_id, newNode, childnodeInfo);");
      //println(out, "      }");
      println(out, "    }");
      println(out, "    YAHOO.util.Event.addListener(window, \"load\", init);");
      println(out, "");
      println(out, "  </script>");
      println(out, "<script>(function(i,s,o,g,r,a,m){i[\"GoogleAnalyticsObject\"]=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,\"script\",\"//www.google-analytics.com/analytics.js\",\"ga\");ga(\"create\", \"UA-150112876-1\", {\"cookieDomain\":\"auto\"});ga(\"send\", \"pageview\");</script>");
      println(out, "</head>");
      println(out, "<body>");

      println(out, "  ");
      println(out, "    <!-- Begin Skip Top Navigation -->");
      println(out, "      <a href=\"#evs-content\" class=\"hideLink\" accesskey=\"1\" title=\"Skip repetitive navigation links\">skip navigation links</A>");
      println(out, "    <!-- End Skip Top Navigation --> ");
      println(out, "    <div id=\"popupContainer\">");
      println(out, "      <!-- nci popup banner -->");
      println(out, "      <div class=\"ncipopupbanner\">");
      println(out, "        <a href=\"http://www.cancer.gov\" target=\"_blank\" alt=\"National Cancer Institute\"><img src=\"/ncitbrowser/images/banner-red.png\" width=\"680\" height=\"39\" border=\"0\" alt=\"National Cancer Institute\" /></a>");
      println(out, "        <a href=\"http://www.cancer.gov\" target=\"_blank\" alt=\"National Cancer Institute\"><img src=\"/ncitbrowser/images/spacer.gif\" width=\"60\" height=\"39\" border=\"0\" alt=\"National Cancer Institute\" class=\"print-header\" /></a>");
      println(out, "      </div>");
      println(out, "      <!-- end nci popup banner -->");
      println(out, "      <div id=\"popupMainArea\">");
      println(out, "        <a name=\"evs-content\" id=\"evs-content\"></a>");
      println(out, "        <table class=\"evsLogoBg\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role='presentation'>");
      println(out, "        <tr>");
      println(out, "          <td valign=\"top\">");
      println(out, "            <a href=\"http://evs.nci.nih.gov/\" target=\"_blank\" alt=\"Enterprise Vocabulary Services\">");
      println(out, "              <img src=\"/ncitbrowser/images/evs-popup-logo.gif\" width=\"213\" height=\"26\" alt=\"EVS: Enterprise Vocabulary Services\" title=\"EVS: Enterprise Vocabulary Services\" border=\"0\" />");
      println(out, "            </a>");
      println(out, "          </td>");
      println(out, "          <td valign=\"top\"><div id=\"closeWindow\"><a href=\"javascript:window.close();\"><img src=\"/ncitbrowser/images/thesaurus_close_icon.gif\" width=\"10\" height=\"10\" border=\"0\" alt=\"Close Window\" />&nbsp;CLOSE WINDOW</a></div></td>");
      println(out, "        </tr>");
      println(out, "        </table>");
      println(out, "");
      println(out, "");

      String release_date = DataUtils.getVersionReleaseDate(ontology_display_name, ontology_version);
      //if (ontology_display_name.compareTo("NCI Thesaurus") == 0 || ontology_display_name.compareTo("NCI_Thesaurus") == 0) {
	  if (DataUtils.isNCIT(ontology_display_name)) {

      println(out, "    <div>");
      println(out, "      <img src=\"/ncitbrowser/images/thesaurus_popup_banner.gif\" width=\"612\" height=\"56\" alt=\"NCI Thesaurus\" title=\"\" border=\"0\" />");
      println(out, "      ");
      println(out, "	 ");
      println(out, "             <span class=\"texttitle-blue-rightjust-2\">" + ontology_version + " (Release date: " + release_date + ")</span>");
      println(out, "      ");
      println(out, "");
      println(out, "    </div>");

      } else {

      println(out, "    <div>");
      println(out, "      <img src=\"/ncitbrowser/images/other_popup_banner.gif\" width=\"612\" height=\"56\" alt=\"" + display_name + "\" title=\"\" border=\"0\" />");
      println(out, "      <div class=\"vocabularynamepopupshort\">" + display_name );
      println(out, "      ");
      println(out, "	 ");
      println(out, "             <span class=\"texttitle-blue-rightjust\">" + ontology_version + " (Release date: " + release_date + ")</span>");
      println(out, "         ");
      println(out, " ");
      println(out, "      </div>");
      println(out, "    </div>");

      }


      println(out, "");
      println(out, "        <div id=\"popupContentArea\">");
      println(out, "          <table width=\"580px\" cellpadding=\"3\" cellspacing=\"0\" border=\"0\" role='presentation'>");
      println(out, "            <tr class=\"textbody\">");
      println(out, "              <td class=\"pageTitle\" align=\"left\">");
      println(out, "                " + display_name + " Hierarchy");
      println(out, "              </td>");
      println(out, "              <td class=\"pageTitle\" align=\"right\">");
      println(out, "                <font size=\"1\" color=\"red\" align=\"right\">");
      println(out, "                  <a href=\"javascript:printPage()\"><img src=\"/ncitbrowser/images/printer.bmp\" border=\"0\" alt=\"Send to Printer\"><i>Send to Printer</i></a>");
      println(out, "                </font>");
      println(out, "              </td>");
      println(out, "            </tr>");
      println(out, "          </table>");

      if (! ServerMonitorThread.getInstance().isLexEVSRunning()) {
          println(out, "            <div class=\"textbodyredsmall\">" + ServerMonitorThread.getInstance().getMessage() + "</div>");
      } else {
          println(out, "            <!-- Tree content -->");
          println(out, "            <div id=\"rootDesc\">");
          println(out, "              <div id=\"bd\"></div>");
          println(out, "              <div id=\"ft\"></div>");
          println(out, "            </div>");
          println(out, "            <div id=\"treeStatus\">");
          println(out, "              <div id=\"bd\"></div>");
          println(out, "            </div>");
          println(out, "            <div id=\"emptyRoot\">");
          println(out, "              <div id=\"bd\"></div>");
          println(out, "            </div>");
          println(out, "            <div id=\"treecontainer\"></div>");
      }

      println(out, "");
      println(out, "          <form id=\"pg_form\" enctype=\"application/x-www-form-urlencoded;charset=UTF-8\">");
      println(out, "            ");


	  String ontology_node_id_value = HTTPUtils.cleanXSS(node_id);
	  String ontology_display_name_value = HTTPUtils.cleanXSS(ontology_display_name);
	  String ontology_version_value = HTTPUtils.cleanXSS(ontology_version);

      println(out, "            <input type=\"hidden\" id=\"ontology_node_id\" name=\"ontology_node_id\" value=\"" + ontology_node_id_value + "\" />");
      println(out, "            <input type=\"hidden\" id=\"ontology_display_name\" name=\"ontology_display_name\" value=\"" + ontology_display_name_value + "\" />");
      println(out, "            <input type=\"hidden\" id=\"ontology_version\" name=\"ontology_version\" value=\"" + ontology_version_value + "\" />");

      println(out, "");
      println(out, "          </form>");
      println(out, "          <!-- End of Tree control content -->");
      println(out, "        </div>");
      println(out, "      </div>");
      println(out, "    </div>");
      println(out, "  ");
      println(out, "</body>");
      println(out, "</html>");
/*
      if (_debug) {
          _logger.debug(Utils.SEPARATOR);
          _logger.debug("VIH HTML:\n" + _debugBuffer);
          _debugBuffer = null;
          _logger.debug(Utils.SEPARATOR);
      }
*/
    }


    public void create_src_vs_tree(HttpServletRequest request, HttpServletResponse response) {
		create_vs_tree(request, response, Constants.STANDARD_VIEW);
	}

    public void create_cs_vs_tree(HttpServletRequest request, HttpServletResponse response) {
		String dictionary = HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
		if (!DataUtils.isNull(dictionary)) {
			String version = HTTPUtils.cleanXSS((String) request.getParameter("version"));
			create_vs_tree(request, response, Constants.TERMINOLOGY_VIEW, dictionary, version);
		} else {
		    create_vs_tree(request, response, Constants.TERMINOLOGY_VIEW);
		}
	}


    public void create_vs_tree(HttpServletRequest request, HttpServletResponse response, int view) {
		String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
		//KLO, 10182017
		if (vsd_uri != null) {
			if (vsd_uri.indexOf("http") == -1) {
				vsd_uri = ValueSetDefinitionConfig.getValueSetURI(vsd_uri);
			}
		}
		create_vs_tree(request, response, view, vsd_uri);
	}


//////////////////////////
//  create_vs_tree
//////////////////////////

    public String get_checked_nodes(HttpServletRequest request) {
		return  get_checked_vocabularies(request);
	}


    public void create_vs_tree(HttpServletRequest request, HttpServletResponse response, int view, String vsd_uri) {
        SimpleTreeUtils stu = new SimpleTreeUtils(DataUtils.getVocabularyNameSet());

String mode = HTTPUtils.cleanXSS((String) request.getParameter("mode"));
if (mode == null) {
	mode = (String) request.getSession().getAttribute("mode");
}
request.getSession().setAttribute("mode", mode);

        stu.setUrl(request.getContextPath() + "ajax?action=create_src_vs_tree&mode=" + mode);

		String nav_type = HTTPUtils.cleanXSS((String) request.getParameter("nav_type"));
		request.getSession().setAttribute("vs_nav_type", "valuesets");

	    request.getSession().removeAttribute("dictionary");
	    request.getSession().removeAttribute("version");


//[NCITERM-745] Top node of value sets deselected after search.
String checked_valuesets = get_checked_vocabularies(request);//HTTPUtils.cleanXSS((String) request.getSession().getAttribute("checked_vocabularies"));

String checked_nodes = get_checked_nodes(request);//HTTPUtils.cleanXSS((String) request.getSession().getAttribute("checked_vocabularies"));
Vector selected_valuesets = null;
Vector selected_nodes = null;
if (!DataUtils.isNullOrBlank(checked_nodes)) {
	request.getSession().setAttribute("checked_vocabularies", checked_nodes);
	selected_valuesets = DataUtils.parseData(checked_nodes, ",");
	selected_nodes = DataUtils.parseData(checked_nodes, ",");
	stu.setSelectedNodes(selected_nodes);
}

		String root_vsd_uri = vsd_uri;
		String vsd_name = null;
		boolean isValueSet = false;
		ValueSetDefinition vsd = null;

        String vsd_description =  "DESCRIPTION NOT AVAILABLE";

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if (vsd_uri != null) {
			isValueSet = true;
			/*
			vsd = DataUtils.findValueSetDefinitionByURI(vsd_uri);
			if (vsd != null) {
					vsd_name = vsd.getValueSetDefinitionName();
					isValueSet = true;

					//KLO, 10182017
					if (DataUtils.getValueSetHierarchy() != null) {
						vsd_description = DataUtils.getValueSetHierarchy().getValueSetDecription(vsd_uri);
					} else {
						System.out.println("WARNING: DataUtils.getValueSetHierarchy() = null.");
					}
					//vsd_description = vsd.getEntityDescription().getContent();

			} else {
					Entity entity = DataUtils.getConceptByCode(Constants.TERMINOLOGY_VALUE_SET_NAME, null, vsd_uri);
					if (entity != null) {
						vsd_name = entity.getEntityDescription().getContent();
					}
					vsd_description = DataUtils.getTerminologyValueSetDescription(vsd_uri);
			}
			*/
			//vsd_description = NCItBrowserProperties.getResolvedValueSetName(vsd_uri);
			vsd_name = DataUtils.findValueSetNameByURI(vsd_uri);
			vsd_description = DataUtils.findValueSetDescriptionByURI(vsd_uri);
	    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	  request.getSession().removeAttribute("b");
	  request.getSession().removeAttribute("m");

      response.setContentType("text/html");
      PrintWriter out = null;

      try {
      	  out = response.getWriter();
      } catch (Exception ex) {
		  ex.printStackTrace();
		  return;
	  }


	  //String checked_vocabularies = HTTPUtils.cleanXSS((String) request.getParameter("checked_vocabularies"));
	  String checked_vocabularies = get_checked_vocabularies(request);
	  String partial_checked_vocabularies = HTTPUtils.cleanXSS((String) request.getParameter("partial_checked_vocabularies"));


	  String message = (String) request.getSession().getAttribute("message");
      out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
      out.println("<html xmlns:c=\"http://java.sun.com/jsp/jstl/core\">");
      out.println("<head>");


if (DataUtils.isNull(vsd_uri)) {
	if (view == Constants.STANDARD_VIEW) {
		out.println("  <title>NCI Term Browser - Value Set Source View</title>");
	} else {
		out.println("  <title>NCI Term Browser - Value Set Terminology View</title>");
	}
} else {
	out.println("  <title>NCI Term Browser - Value Set " + vsd_uri + " </title>");
}

      out.println("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
      out.println("");
      out.println("<style type=\"text/css\">");
      out.println("/*margin and padding on body element");
      out.println("  can introduce errors in determining");
      out.println("  element position and are not recommended;");
      out.println("  we turn them off as a foundation for YUI");
      out.println("  CSS treatments. */");
      out.println("body {");
      out.println("	margin:0;");
      out.println("	padding:0;");
      out.println("}");
      out.println("</style>");
      out.println("");
      out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"http://yui.yahooapis.com/2.9.0/build/fonts/fonts-min.css\" />");
      out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"http://yui.yahooapis.com/2.9.0/build/treeview/assets/skins/sam/treeview.css\" />");
      out.println("");
	  out.println("<script type=\"text/javascript\" src=\"/ncitbrowser/js/event_simulate.js\"></script>");
	  out.println("<script type=\"text/javascript\" src=\"/ncitbrowser/js/value_set_tree_navigation.js\"></script>");

      out.println("<!--Additional custom style rules for this example:-->");
      out.println("<style type=\"text/css\">");
      out.println("");
      out.println("");
      out.println(".ygtvcheck0 { background: url(/ncitbrowser/images/yui/treeview/check0.gif) 0 0 no-repeat; width:16px; height:20px; float:left; cursor:pointer; }");
      out.println(".ygtvcheck1 { background: url(/ncitbrowser/images/yui/treeview/check1.gif) 0 0 no-repeat; width:16px; height:20px; float:left; cursor:pointer; }");
      out.println(".ygtvcheck2 { background: url(/ncitbrowser/images/yui/treeview/check2.gif) 0 0 no-repeat; width:16px; height:20px; float:left; cursor:pointer; }");
      out.println("");
      out.println("");
      out.println(".ygtv-edit-TaskNode  {	width: 190px;}");
      out.println(".ygtv-edit-TaskNode .ygtvcancel, .ygtv-edit-TextNode .ygtvok  {	border:none;}");
      out.println(".ygtv-edit-TaskNode .ygtv-button-container { float: right;}");
      out.println(".ygtv-edit-TaskNode .ygtv-input  input{	width: 140px;}");
      out.println(".whitebg {");
      out.println("	background-color:white;");
      out.println("}");
      out.println("</style>");
      out.println("");
      out.println("  <link rel=\"stylesheet\" type=\"text/css\" href=\"/ncitbrowser/css/styleSheet.css\" />");
      out.println("  <link rel=\"shortcut icon\" href=\"/ncitbrowser/favicon.ico\" type=\"image/x-icon\" />");
      out.println("");
      out.println("  <script type=\"text/javascript\" src=\"/ncitbrowser/js/script.js\"></script>");
      out.println("  <script type=\"text/javascript\" src=\"/ncitbrowser/js/tasknode.js\"></script>");

      println(out, "  <script type=\"text/javascript\" src=\"/ncitbrowser/js/search.js\"></script>");
      println(out, "  <script type=\"text/javascript\" src=\"/ncitbrowser/js/dropdown.js\"></script>");


      out.println("  <script type=\"text/javascript\">");
      out.println("");
      out.println("    function refresh() {");
      out.println("");
      out.println("      var selectValueSetSearchOptionObj = document.forms[\"valueSetSearchForm\"].selectValueSetSearchOption;");
      out.println("");
      out.println("      for (var i=0; i<selectValueSetSearchOptionObj.length; i++) {");
      out.println("        if (selectValueSetSearchOptionObj[i].checked) {");
      out.println("            selectValueSetSearchOption = selectValueSetSearchOptionObj[i].value;");
      out.println("        }");
      out.println("      }");
      out.println("");
      out.println("");
      out.println("      window.location.href=\"/ncitbrowser/pages/value_set_source_view.jsf?refresh=1\""); //Before(GF31982)
      out.println("          + \"&nav_type=valuesets\" + \"&opt=\"+ selectValueSetSearchOption;");
      out.println("");
      out.println("    }");
      out.println("  </script>");
      out.println("");


HashMap value_set_tree_hmap = null;
Boolean value_set_tab = Boolean.TRUE;


 String contextPath = request.getContextPath();
 String view_str = Integer.valueOf(view).toString();

//[#31914] Search option and algorithm in value set search box are not preserved in session.
//String option = (String) request.getSession().getAttribute("selectValueSetSearchOption");
//String algorithm = (String) request.getSession().getAttribute("valueset_search_algorithm");

String option = HTTPUtils.cleanXSS((String) request.getParameter("selectValueSetSearchOption"));
if (DataUtils.isNull(option)) {
	option = (String) request.getSession().getAttribute("selectValueSetSearchOption");
}

if (DataUtils.isNull(option)) {
	option = "Name";
}
request.getSession().setAttribute("selectValueSetSearchOption", option);

String algorithm = HTTPUtils.cleanXSS((String) request.getParameter("valueset_search_algorithm"));
if (DataUtils.isNull(algorithm)) {
	algorithm = (String) request.getSession().getAttribute("valueset_search_algorithm");
}

if (DataUtils.isNull(algorithm)) {
	algorithm = Constants.DEFAULT_SEARCH_ALGORITHM;//"contains";
}
request.getSession().setAttribute("valueset_search_algorithm", algorithm);


        String matchText = HTTPUtils.cleanMatchTextXSS((String) request.getParameter("matchText"));
        if (DataUtils.isNull(matchText)) {
			matchText = (String) request.getSession().getAttribute("matchText");
		}

		if (DataUtils.isNull(matchText)) {
			matchText = "";
		} else {
			matchText = matchText.trim();
		}
        request.getSession().setAttribute("matchText", matchText);
        request.getSession().setAttribute("matchText_RVS", matchText);

String option_code = "";
String option_name = "";
if (DataUtils.isNull(option)) {
	option_name = "checked";

} else {
	if (option.compareToIgnoreCase("Code") == 0 || option.compareToIgnoreCase("codes") == 0 || option.compareToIgnoreCase("code") == 0) {
		option_code = "checked";
	}
	if (option.compareToIgnoreCase("Name") == 0 || option.compareToIgnoreCase("names") == 0) {
		option_name = "checked";
	}
}


String algorithm_exactMatch = "";
String algorithm_startsWith = "";
String algorithm_contains = "";

if (DataUtils.isNull(algorithm)) {
	algorithm = "contains";
}

if (algorithm.compareToIgnoreCase("exactMatch") == 0) {
	algorithm_exactMatch = "checked";
}

if (algorithm.compareToIgnoreCase("startsWith") == 0) {
	algorithm_startsWith = "checked";
	option_name = "checked";
	option_code = "";
}

if (algorithm.compareToIgnoreCase("contains") == 0) {
	algorithm_contains = "checked";
	option_name = "checked";
	option_code = "";
}
      out.println("");


      out.println("<style>");
      out.println("html, body{ box-sizing:border-box}");
      out.println("*, *:before, *:after{ box-sizing:inherit}");
      out.println("");
      out.println(".table-scroll{");
      out.println("	position:relative;");
      out.println("	margin:auto;");
      //out.println("	padding-left:9em;");
      out.println("	width:100%;");
      out.println("	overflow:auto;");
      out.println("	height:300px;");
      out.println("	overflow-y:scroll;");
      out.println("}");
      out.println(".table-wrap{");
      out.println("	margin-left:-9em;");
      out.println("	outline:1px solid;");
      out.println("	outline-offset:-1px;");
      out.println("	overflow:auto;");
      out.println("}");
      out.println("</style>");
      out.println("<script>(function(i,s,o,g,r,a,m){i[\"GoogleAnalyticsObject\"]=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,\"script\",\"//www.google-analytics.com/analytics.js\",\"ga\");ga(\"create\", \"UA-150112876-1\", {\"cookieDomain\":\"auto\"});ga(\"send\", \"pageview\");</script>");
      out.println("</head>");
      out.println("");


      out.println("<style>");
      out.println(".outer {position:relative}");
      out.println(".inner {");
      out.println("  overflow-x:scroll;");
      out.println("  overflow-y:scroll;");
      out.println("  width:900px;");
      out.println("  margin-left:5px;");
      out.println("  height:500px;");
      out.println("}");
      out.println("</style>");
      out.println("");
      out.println("");
      out.println("<style type=\"text/css\">");
      out.println("    body, .mt {padding:0px;font-family: Tahoma, sans-serif;font-size: 0.9em;}");
      out.println("    body {margin:0px;}");
      out.println("    p {margin:30px 0px 30px 0px;}");
      out.println("    table.mt {border-width: 1px;border-spacing:0px ;border-style: solid;border-color: #cfcfcf;border-collapse: collapse;background-color: transparent;}");
      out.println("    table.mt th {border-width: 1px;padding: 1px;border-style: solid;border-color: #cfcfcf;white-space: nowrap; background-color: #afafaf;text-align:left;}");
      out.println("    table.mt td {border-width: 1px;padding: 1px;border-style: solid;border-color: #cfcfcf;text-align: left;vertical-align:top;}");
      out.println("    .frc {background: #efefef;}");
      out.println("</style>");
      out.println("");


if (mode != null && (mode.compareTo("0") == 0 || mode.compareTo("2") == 0)) {
	// to be modified: -- collapse_all method
	  out.println("<body onLoad=\"collapse_all();\">");
} else {
      out.println("<body onLoad=\"document.forms.valueSetSearchForm.matchText.focus();\">");
}

      out.println("  <script type=\"text/javascript\" src=\"/ncitbrowser/js/wz_tooltip.js\"></script>");
      out.println("  <script type=\"text/javascript\" src=\"/ncitbrowser/js/tip_centerwindow.js\"></script>");
      out.println("  <script type=\"text/javascript\" src=\"/ncitbrowser/js/tip_followscroll.js\"></script>");



      out.println("  <!-- Begin Skip Top Navigation -->");
      out.println("  <a href=\"#evs-content\" class=\"hideLink\" accesskey=\"1\" title=\"Skip repetitive navigation links\">skip navigation links</A>");
      out.println("  <!-- End Skip Top Navigation -->");
      out.println("");
      out.println("<!-- nci banner -->");
      //out.println("<div class=\"ncibanner\">");  height:32px
      out.println("<div style='clear:both;margin-top:-5px;padding:8px;height:47px;color:white;background-color:#C31F40'>");

      out.println("  <a href=\"http://www.cancer.gov\" target=\"_blank\">");
      out.println("    <img src=\"/ncitbrowser/images/banner-red.png\"");
      out.println("      width=\"955\" height=\"39\" border=\"0\"");
      out.println("      alt=\"National Cancer Institute\"/>");
      out.println("  </a>");

      out.println("</div>");
      out.println("<!-- end nci banner -->");
      out.println("");
      out.println("  <div class=\"center-page_960\">");
      out.println("    <!-- EVS Logo -->");
      out.println("<div>");
      out.println("  <img src=\"/ncitbrowser/images/evs-logo-swapped.gif\" alt=\"EVS Logo\"");
      out.println("       width=\"941\" height=\"26\" border=\"0\"");
      out.println("       usemap=\"#external-evs\" />");
      out.println("  <map id=\"external-evs\" name=\"external-evs\">");
      out.println("    <area shape=\"rect\" coords=\"0,0,140,26\"");
      out.println("      href=\"/ncitbrowser/start.jsf\" target=\"_self\"");
      out.println("      alt=\"NCI Term Browser\" />");
      out.println("    <area shape=\"rect\" coords=\"520,0,745,26\"");
      out.println("      href=\"http://evs.nci.nih.gov/\" target=\"_blank\"");
      out.println("      alt=\"Enterprise Vocabulary Services\" />");
      out.println("  </map>");
      out.println("</div>");
      out.println("");
      out.println("");
      out.println("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role='presentation'>");
      out.println("  <tr>");
      out.println("    <td width=\"5\"></td>");
      out.println("    <td><a href=\"/ncitbrowser/pages/multiple_search.jsf?nav_type=terminologies\">");
      out.println("      <img name=\"tab_terms\" src=\"/ncitbrowser/images/tab_terms.gif\"");
      out.println("        border=\"0\" alt=\"Terminologies\" title=\"Terminologies\" /></a></td>");
      out.println("    <td><a href=\"/ncitbrowser/ajax?action=create_src_vs_tree\">"); //GF31982
      out.println("      <img name=\"tab_valuesets\" src=\"/ncitbrowser/images/tab_valuesets_clicked.gif\"");
      out.println("        border=\"0\" alt=\"Value Sets\" title=\"ValueSets\" /></a></td>");
      out.println("    <td><a href=\"/ncitbrowser/pages/mapping_search.jsf?nav_type=mappings\">");
      out.println("      <img name=\"tab_map\" src=\"/ncitbrowser/images/tab_map.gif\"");
      out.println("        border=\"0\" alt=\"Mappings\" title=\"Mappings\" /></a></td>");
      out.println("  </tr>");
      out.println("</table>");
      out.println("");
      out.println("<div class=\"mainbox-top\"><img src=\"/ncitbrowser/images/mainbox-top.gif\" width=\"945\" height=\"5\" alt=\"\"/></div>");
      out.println("<!-- end EVS Logo -->");
      out.println("    <!-- Main box -->");
      out.println("    <div id=\"main-area_960\">");
      out.println("");
      out.println("      <!-- Thesaurus, banner search area -->");
      out.println("      <div class=\"bannerarea_960\">");

      if (DataUtils.isNull(vsd_uri)) {
		  out.println("        <a href=\"/ncitbrowser/start.jsf\" style=\"text-decoration: none;\">");
		  out.println("            <div class=\"vocabularynamebanner_tb\">");
		  out.println("               <span class=\"vocabularynamelong_tb\">" + JSPUtils.getApplicationVersionDisplay() + "</span>");
		  out.println("            </div>");
		  out.println("        </a>");
	  } else {
		  out.println("        <a class=\"vocabularynamebanner\" href=\"/ncitbrowser/ajax?action=create_src_vs_tree&vsd_uri=" + vsd_uri + "\">");
		  out.println("	             <div class=\"vocabularynamebanner\">");
		  out.println("                <div class=\"vocabularynameshort\" STYLE=\"font-size: 22px; font-family : Arial\">");
		  out.println(                     vsd_name);
		  out.println("                </div>");
		  out.println("              </div>");
		  out.println("	       </a>");
	  }

      out.println("        <div class=\"search-globalnav_960\">");
      out.println("          <!-- Search box -->");
      out.println("          <div class=\"searchbox-top\"><img src=\"/ncitbrowser/images/searchbox-top.gif\" width=\"352\" height=\"2\" alt=\"SearchBox Top\" /></div>");
      out.println("          <div class=\"searchbox\">");
      out.println("");
      out.println("");

///////////////
// searchbox
///////////////

      out.println("<form id=\"valueSetSearchForm\" name=\"valueSetSearchForm\" method=\"post\" action=\"" + contextPath + "/ajax?action=search_value_set\" class=\"search-form-main-area\" enctype=\"application/x-www-form-urlencoded;charset=UTF-8\">");

      out.println("<input type=\"hidden\" name=\"valueSetSearchForm\" value=\"valueSetSearchForm\" />");
      out.println("<input type=\"hidden\" name=\"view\" value=\"" + view_str + "\" />");

      out.println("            <input type=\"hidden\" id=\"checked_vocabularies\" name=\"checked_vocabularies\" value=\"\" />");
      out.println("            <input type=\"hidden\" id=\"partial_checked_vocabularies\" name=\"partial_checked_vocabularies\" value=\"\" />");
      out.println("            <input type=\"hidden\" id=\"value_set_home\" name=\"value_set_home\" value=\"true\" />");
      out.println("            <input type=\"hidden\" name=\"vsd_uri\" value=\"" + vsd_uri + "\" />");

      out.println("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin: 2px\" role='presentation'>");
      out.println("  <tr valign=\"top\" align=\"left\">");
      out.println("    <td align=\"left\" class=\"textbody\">");
      out.println("");
      out.println("                  <input CLASS=\"searchbox-input-2\"");
      out.println("                    name=\"matchText\"");
      out.println("                    value=\"" + matchText + "\"");
      out.println("                    onFocus=\"active = true\"");
      out.println("                    onBlur=\"active = false\"");
      out.println("                    onkeypress=\"return submitEnter('valueSetSearchForm:valueset_search',event)\"");
      out.println("                    tabindex=\"1\"/>");
      out.println("");
      out.println("");
      out.println("                <input id=\"valueSetSearchForm:valueset_search\" type=\"image\" src=\"/ncitbrowser/images/search.gif\" name=\"valueSetSearchForm:valueset_search\" alt=\"Search value sets containing matched concepts\" tabindex=\"2\" class=\"searchbox-btn\" /><a href=\"/ncitbrowser/pages/help.jsf#searchhelp\" tabindex=\"3\"><img src=\"/ncitbrowser/images/search-help.gif\" alt=\"Search Help\" style=\"border-width:0;\" class=\"searchbox-btn\" /></a>");
      out.println("");
      out.println("");
      out.println("    </td>");
      out.println("  </tr>");
      out.println("");
      out.println("  <tr valign=\"top\" align=\"left\">");
      out.println("    <td>");
      out.println("      <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin: 0px\" role='presentation'>");
      out.println("");
      out.println("        <tr valign=\"top\" align=\"left\">");
      out.println("        <td align=\"left\" class=\"textbody\">");
      out.println("                     <input type=\"radio\" name=\"valueset_search_algorithm\" value=\"contains\" alt=\"Contains\" "      + algorithm_contains   + " tabindex=\"3\"  onclick=\"onVSAlgorithmChanged();\">Contains");
      out.println("                     <input type=\"radio\" name=\"valueset_search_algorithm\" value=\"exactMatch\" alt=\"Exact Match\" " + algorithm_exactMatch + " tabindex=\"3\">Exact Match&nbsp;");
      out.println("                     <input type=\"radio\" name=\"valueset_search_algorithm\" value=\"startsWith\" alt=\"Begins With\" " + algorithm_startsWith + " tabindex=\"3\"  onclick=\"onVSAlgorithmChanged();\">Begins With&nbsp;");
      out.println("        </td>");
      out.println("        </tr>");
      out.println("");
      out.println("        <tr align=\"left\">");
      out.println("            <td height=\"1px\" bgcolor=\"#2F2F5F\" align=\"left\"></td>");
      out.println("        </tr>");
      out.println("        <tr valign=\"top\" align=\"left\">");
      out.println("          <td align=\"left\" class=\"textbody\">");
      out.println("                <input type=\"radio\" id=\"selectValueSetSearchOption\" name=\"selectValueSetSearchOption\" value=\"Name\" " + option_name + " alt=\"Name\" checked tabindex=\"4\"  >Name&nbsp;");
      out.println("                <input type=\"radio\" id=\"selectValueSetSearchOption\" name=\"selectValueSetSearchOption\" value=\"Code\" " + option_code + " alt=\"Code\" tabindex=\"4\" onclick=\"onVSCodeButtonPressed();\">Code&nbsp;");
      out.println("          </td>");
      out.println("        </tr>");

      out.println("      </table>");
      out.println("    </td>");
      out.println("  </tr>");
      out.println("</table>");

      out.println("                <input type=\"hidden\" id=\"nav_type\" name=\"nav_type\" value=\"valuesets\" />");
      out.println("                <input type=\"hidden\" id=\"view\" name=\"view\" value=\"source\" />");

      out.println("          </div> <!-- searchbox -->");

      out.println("");
      out.println("          <div class=\"searchbox-bottom\"><img src=\"/ncitbrowser/images/searchbox-bottom.gif\" width=\"352\" height=\"2\" alt=\"SearchBox Bottom\" /></div>");
      out.println("          <!-- end Search box -->");
      out.println("          <!-- Global Navigation -->");
      out.println("");
      out.println("<table class=\"global-nav\" border=\"0\" width=\"100%\" height=\"37px\" cellpadding=\"0\" cellspacing=\"0\" role='presentation'>");
      out.println("  <tr>");
      out.println("    <td align=\"left\" valign=\"bottom\">");
      out.println("      <a href=\"#\" onclick=\"javascript:window.open('/ncitbrowser/pages/source_help_info-termbrowser.jsf',");
      out.println("        '_blank','top=100, left=100, height=740, width=780, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');\" tabindex=\"13\">");
      out.println("        Sources</a>");
      out.println("");

 String cart_size = (String) request.getSession().getAttribute("cart_size");
 if (!DataUtils.isNull(cart_size)) {
          out.write("|");
          out.write("                        <a href=\"");
          out.print(request.getContextPath());
          out.write("/pages/cart.jsf\" tabindex=\"14\">Cart</a>\r\n");
 }


//KLO, 022612
	  out.println(" \r\n");
	  out.println("      ");
	  out.print( VisitedConceptUtils.getDisplayLink(request, true) );
	  out.println(" \r\n");
      out.println("    </td>");




      out.println("    <td align=\"right\" valign=\"bottom\">");
	  out.println("      <a href=\"");
	  out.print( request.getContextPath() );
	  out.println("/pages/help.jsf\" tabindex=\"16\">Help</a>\r\n");
	  out.println("    </td>\r\n");
	  out.println("    <td width=\"7\"></td>\r\n");

	  out.println("  </tr>\r\n");
	  out.println("</table>");

      out.println("          <!-- end Global Navigation -->");
      out.println("");
      out.println("        </div> <!-- search-globalnav -->");
      out.println("      </div> <!-- bannerarea -->");
      out.println("");
      out.println("      <!-- end Thesaurus, banner search area -->");


//addHiddenForm(out, checked_vocabularies, partial_checked_vocabularies);


      out.println("      <!-- Quick links bar -->");
      out.println("");
      out.println("<div class=\"bluebar\">");
      out.println("  <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" role='presentation'>");
      out.println("  <tr>");
      out.println("    <td><div class=\"quicklink-status\">&nbsp;</div></td>");
      out.println("    <td>");
      out.println("");


      addQuickLink(request, out);

      out.println("");
      out.println("      </td>");
      out.println("    </tr>");
      out.println("  </table>");
      out.println("");
      out.println("</div>");

      if (! ServerMonitorThread.getInstance().isLexEVSRunning()) {
      out.println("    <div class=\"redbar\">");
      out.println("      <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" role='presentation'>");
      out.println("        <tr>");
      out.println("          <td class=\"lexevs-status\">");
      out.println("            " + ServerMonitorThread.getInstance().getMessage());
      out.println("          </td>");
      out.println("        </tr>");
      out.println("      </table>");
      out.println("    </div>");
      }

      out.println("      <!-- end Quick links bar -->");
      out.println("");
      out.println("      <!-- Page content -->");
      out.println("      <div class=\"pagecontent\">");
      out.println("");

      if (!DataUtils.isNullOrBlank(message)) {
		  out.println("\r\n");
		  out.println("      <p class=\"textbodyred\">");
		  out.print(message);
		  out.println("</p>\r\n");
		  out.println("    ");
		  request.getSession().removeAttribute("message");
      }


      if (DataUtils.isNull(vsd_uri)) {
		  out.println("<p class=\"textbody\">");
		  out.println("View value sets organized by standards category or source terminology.");
		  out.println("Standards categories group the value sets supporting them; all other labels lead to the home pages of actual value sets or source terminologies.");
		  out.println("Search or browse a value set from its home page, or search all value sets at once from this page (very slow) to find which ones contain a particular code or term.");
//out.println("Many of these value sets are subsets of NCI Thesaurus that are developed jointly with EVS partners as federal and international coding standards (see the <a href=http://ncit.nci.nih.gov/ncitbrowser/pages/subset.jsf>NCI Thesaurus Subsets</a> page).");
out.println("Many of these value sets are subsets of NCI Thesaurus that are developed jointly with EVS partners as federal and international coding standards (see the ");
out.println(" <a href=\"");
out.print( request.getContextPath() );
out.print("/pages/subset.jsf\">NCI Thesaurus Subsets</a> page).");

		  out.println("</p>");
	  }


	  out.println("      <a href=\"");
	  out.print( request.getContextPath() );



      out.println("");
      out.println("        <div id=\"popupContentArea\">");
      out.println("          <a name=\"evs-content\" id=\"evs-content\"></a>");
      out.println("");

      out.println("      <table class=\"datatableValueSet_960\" summary=\"\" cellpadding=\"3\" cellspacing=\"0\" border=\"0\" width=\"100%\">");

      //out.println("                    <tr><td colspan=\"2\">");
      //out.println("&nbsp;");
      //out.println("                    </td></tr>");


      if (!DataUtils.isNull(vsd_uri)) {

		  if (isValueSet) {
			  out.println("            <tr class=\"textbody\">");

			  out.println("                      <td>");
			  out.println("                         <div class=\"texttitle-blue\">Welcome</div>");
			  out.println("                      </td>");
			  out.println("");
			  out.println("                      <td>");


			ValueSetConfig vsc = ValueSetDefinitionConfig.getValueSetConfig(vsd_uri);
			if (show_released_file_button) {
				if (vsc != null && !DataUtils.isNullOrBlank(vsc.getReportURI())) {

					  out.println("<table role='presentation'>");
					  out.println("<tr><td>");
					  out.println("<a href=\"/ncitbrowser/ajax?action=download&vsd_uri=" + vsd_uri + "\"><img src=\"/ncitbrowser/images/released_file.gif\" alt=\"Value Set Released Files (FTP Server)\" border=\"0\" tabindex=\"2\"></a>");
					  out.println("</td>");
					  out.println("</tr>");
					  out.println("</table>");


				} else {
					  out.println("&nbsp;");
				}
			} else {
				  out.println("&nbsp;");
			}



			  out.println("                      </td>");
			  out.println("");
			  out.println("                      <td>");
			  out.println("<table role='presentation'>");
			  out.println("<tr><td>");

			  out.println("<a href=\"/ncitbrowser/ajax?action=values&vsd_uri=" + vsd_uri + "\"><img src=\"/ncitbrowser/images/values.gif\" alt=\"Values\" border=\"0\" tabindex=\"2\"></a>");
			  out.println("&nbsp;");
			  out.println("<a href=\"/ncitbrowser/ajax?action=versions&vsd_uri=" + vsd_uri + "\"><img src=\"/ncitbrowser/images/versions.gif\" alt=\"Versions\" border=\"0\" tabindex=\"2\"></a>");
			  out.println("&nbsp;");
			  out.println("<a href=\"/ncitbrowser/ajax?action=xmldefinitions&vsd_uri=" + vsd_uri + "\"><img src=\"/ncitbrowser/images/xmldefinitions.gif\" alt=\"XML Definition\" border=\"0\" tabindex=\"2\"></a>");

			  out.println("</td>");
			  out.println("</tr>");
			  out.println("</table>");
			  out.println("                      </td>");


              out.println("            </tr>");
		  } else {
			  out.println("            <tr class=\"textbody\">");
			  out.println("                      <td>");
			  out.println("                         <div class=\"texttitle-blue\">Welcome</div>");
			  out.println("                      </td>");

			  out.println("                      <td class=\"dataCellText\" align=\"right\">");
			  out.println("&nbsp;");
              out.println("                       </td>");
              out.println("            </tr>");
		  }


		  out.println("                    <tr><td colspan=\"2\" align=\"left\"><b>");
		  out.println(vsd_name);
		  out.println("                    </b></td></tr>");

		  out.println("                    <tr><td colspan=\"2\" align=\"left\">");
		  out.println(vsd_description);
		  out.println("                    </td></tr>");

	  }

      //out.println("                    <tr><td colspan=\"2\">");
     //out.println("                      <hr/>");
      //out.println("                    </td></tr>");


		if (DataUtils.isNull(vsd_uri)) {

			  out.println("            <tr class=\"textbody\">");
			  out.println("              <td class=\"textbody\" align=\"left\"  width=\"700\" nowrap>");
			  out.println("");

		//v2.9 modification;
			mode = HTTPUtils.cleanXSS((String) request.getParameter("mode"));
			if (mode == null) {
				mode = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("mode"));
				//
				if (mode == null) {
					mode = "0";
				}
			}

		String tree_views = JSPUtils.getValueSetTreeViews(Integer.parseInt(mode), contextPath);
		out.println(tree_views);

			  out.println("              </td>");
			  out.println("");
			  out.println("              <td align=\"right\">");
			  out.println("               <font size=\"1\" color=\"red\" align=\"right\">");
			  out.println("                 <a href=\"javascript:printPage()\"><img src=\"/ncitbrowser/images/printer.bmp\" border=\"0\" alt=\"Send to Printer\"><i>Send to Printer</i></a>");
			  out.println("               </font>");


			  out.println("              </td>");
			  out.println("            </tr>");
		}


      out.println("          </table>");
      out.println("");
      out.println("          <hr/>");
      out.println("");
      out.println("");
      out.println("");
      out.println("<style>");
      out.println("#expandcontractdiv {border:1px solid #336600; background-color:#FFFFCC; margin:0 0 .5em 0; padding:0.2em;}");
      out.println("#treecontainer { background: #fff }");
      out.println("</style>");
      out.println("");
      out.println("");


      stu.printSelectAllOrNoneLinks(out);

      out.println("");
      out.println("");
      out.println("");
      out.println("          <!-- Tree content -->");
      out.println("");

      out.println("<div>");

stu.printFormHeader(out);

TreeItem root = null;
if (DataUtils.isNull(vsd_uri)) {
	if (view == Constants.STANDARD_VIEW) {
		if (DataUtils.isNullOrEmpty(selected_valuesets)) {
			out.println(DataUtils.getSourceValueSetTreeStringBuffer().toString());
		} else {
			value_set_tree_hmap = DataUtils.getSourceValueSetTree();
			stu.printTree(out, value_set_tree_hmap);
		}
	} else if (view == Constants.TERMINOLOGY_VIEW){
		if (DataUtils.isNullOrEmpty(selected_valuesets)) {
		    out.println(DataUtils.getCodingSchemeValueSetTreeStringBuffer().toString());
		} else {
			value_set_tree_hmap = DataUtils.getCodingSchemeValueSetTree();
			stu.printTree(out, value_set_tree_hmap);
		}
	}

} else {
	value_set_tab = Boolean.FALSE;
	if (view == Constants.STANDARD_VIEW) {
		value_set_tree_hmap = DataUtils.getSourceValueSetTree(vsd_uri);
	} else {
		value_set_tree_hmap = DataUtils.getCodingSchemeValueSetTree(vsd_uri);
	}

	if (value_set_tree_hmap != null) {
		stu.setFocusNodeId(vsd_uri);
		stu.setCheckAll(true);
		stu.printTree(out, value_set_tree_hmap);
	}
}

stu.printFormFooter(out);
out.flush();

      out.println("");
      out.println("          </div> <!-- popupContentArea -->");
      out.println("");

		 // add released file content:
		if (!show_released_file_button) {
			if (!DataUtils.isNull(vsd_uri)) {

				ValueSetConfig vsc = ValueSetDefinitionConfig.getValueSetConfig(vsd_uri);
				String filename = vsc.getReportURI();

				if (filename != null) {
					filename = filename.trim();
				}
				System.out.println("Released file: " + filename);

                if (!DataUtils.isNull(filename) && filename.length() > 0) {
					ResolvedValueSetIteratorHolder rvsi = constructResolvedValueSetIteratorHolder(vsd_uri);
					request.getSession().setAttribute("rvsi", rvsi);

					String content = rvsi.getResolvedValueSetContent();

					out.println("            <div class=\"pagecontent\">");
					out.println("               <a name=\"evs-content\" id=\"evs-content\"></a>");
					out.println("                  <div class=\"tabTableContentContainer\">");
					out.println("");
					out.println("                  <div class=\"table-scroll\">");

					writeExportForm(out, vsd_uri);
					out.println("<table border=\"0\" width=\"900\" role='presentation'>");
					out.println("	<tr><td>");
					out.println("	<div style=\"float:left;width:360px;\">");

					if (content != null) {
						out.println(content);
					} else {
						out.println("ERROR: Unable to generate value set tree.");
					}

					out.println("	</div>");
					out.println("	</td></tr>");
					out.println("</table>");

					out.println("</div></div>");
			    }
			}
		} else {
			System.out.println("No released file content.");
		}

      out.flush();
      out.println("");
      out.println("<div class=\"textbody\">");
      out.println("<!-- footer -->");
      out.println("<div class=\"footer\" style=\"width:920px\">");
      out.println("  <ul>");
      out.println("    <li><a href=\"http://www.cancer.gov\" target=\"_blank\" alt=\"National Cancer Institute\">NCI Home</a> |</li>");
      out.println("    <li><a href=\"/ncitbrowser/pages/contact_us.jsf\">Contact Us</a> |</li>");
      out.println("    <li><a href=\"http://www.cancer.gov/policies\" target=\"_blank\" alt=\"National Cancer Institute Policies\">Policies</a> |</li>");
      out.println("    <li><a href=\"http://www.cancer.gov/policies/page3\" target=\"_blank\" alt=\"National Cancer Institute Accessibility\">Accessibility</a> |</li>");
      out.println("    <li><a href=\"http://www.cancer.gov/policies/page6\" target=\"_blank\" alt=\"National Cancer Institute FOIA\">FOIA</a></li>");
      out.println("  </ul>");


      out.println("<center>");
      out.println("<a href=\"http://www.hhs.gov/\" alt=\"U.S. Department of Health and Human Services\">");
      out.println("U.S. Department of Health and Human Services");
      out.println("</a>");
      out.println("&nbsp;|&nbsp;");
      out.println("<a href=\"https://www.nih.gov/about-nih\" alt=\"National Institutes of Health\">");
      out.println("National Institutes of Health");
      out.println("</a>");
      out.println("&nbsp;|&nbsp;");
      out.println("<a href=\"http://www.cancer.gov/\" alt=\"National Cancer Institute\">");
      out.println("National Cancer Institute");
      out.println("</a>");
      out.println("&nbsp;|&nbsp;");
      out.println("<a href=\"https://www.usa.gov/\" alt=\"USA.gov\">");
      out.println("USA.gov");
      out.println("</a>");
      out.println("</center>");

      out.println("</div>");
      out.println("<!-- end footer -->");
      out.println("</div>");
      out.println("");
      out.println("");
      out.println("      </div> <!-- pagecontent -->");
      out.println("    </div> <!--  main-area -->");
      out.println("    <div class=\"mainbox-bottom\"><img src=\"/ncitbrowser/images/mainbox-bottom.gif\" width=\"945\" height=\"5\" alt=\"Mainbox Bottom\" /></div>");
      out.println("");
      out.println("  </div> <!-- center-page -->");
      out.println("");

      addHiddenForm(out, checked_vocabularies, partial_checked_vocabularies);

      out.println("");
      out.println("        <script type=\"text/javascript\">");
      out.println("            fxheaderInit('rvs_table',300,1,0);");
      out.println("            fxheader();");
      out.println("        </script>");

      out.println("</body>");
      out.println("</html>");
      out.println("");
      out.flush();
  }


      private String find_checked_value_sets(HttpServletRequest request) {
		  HashMap map = DataUtils.getResolvedValueSetHashMap();
		  if (map == null) {
			  return null;
		  }
		  Iterator it = map.keySet().iterator();
		  int lcv = 0;
		  int knt = 0;
		  StringBuffer buf = new StringBuffer();
		  while (it.hasNext()) {
			  lcv++;
			  String rvs_uri = (String) it.next();
			  String[] results = request.getParameterValues(rvs_uri);
			  if (results != null && results.length > 0) {
				  for (int i = 0; i < results.length; i++) {
					  String result = results[i];
					  if (result != null && result.compareTo("") != 0) {
						  knt++;
						  if (knt > 1) {
							  buf.append(",");
						  }
						  buf.append(rvs_uri);
					  }
				  }
		      }
		  }
		  return buf.toString();
	  }

/*
String selected_ValueSetSearchOption = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("selectValueSetSearchOption"));
String checked_vocabularies = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("checked_vocabularies"));
IteratorBean iteratorBean = (IteratorBean) request.getSession().getAttribute("value_set_entity_search_results");
String matchText = HTTPUtils.cleanMatchTextXSS((String) request.getSession().getAttribute("matchText"));
*/
      public String construct_checked_vocabularies_string() {
		  StringBuffer buf = new StringBuffer();
		  LexBIGService lbs = RemoteServerUtil.createLexBIGService();
		  List<CodingScheme> choices = new ArrayList<CodingScheme>();
		  LexEVSResolvedValueSetService lrvs = new LexEVSResolvedValueSetServiceImpl(lbs);
		  try {
			  List<CodingScheme> schemes = lrvs.listAllResolvedValueSets();
			  if (schemes == null) return null;
			  for (int i = 0; i < schemes.size(); i++) {
					CodingScheme cs = schemes.get(i);
					String uri = cs.getCodingSchemeURI();
					buf.append(uri);
					if (i < schemes.size()-1) {
						buf.append(",");
					}
			  }
		  } catch (Exception ex) {
			  ex.printStackTrace();
		  }
		  return buf.toString();
	  }

      public void search_all_value_sets(HttpServletRequest request, HttpServletResponse response) {
          String matchText = HTTPUtils.cleanMatchTextXSS((String) request.getParameter("code"));
          String searchOption = "codes";
          String algorithm = "exactMatch";
          String msg = null;

		  int simpleSearchOption = SimpleSearchUtils.BY_CODE;

			  request.getSession().setAttribute("searchTarget", searchOption);
			  request.getSession().setAttribute("algorithm", algorithm);
			  request.getSession().setAttribute("matchText", matchText);

          String checked_vocabularies = DataUtils.get_checked_all_vocabularies_string();
		  LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		  String serviceUrl = RemoteServerUtil.getServiceUrl();
          ResolvedConceptReferencesIterator iterator = new ValueSetSearchUtils(lbSvc, serviceUrl).searchResolvedValueSetCodingSchemes(checked_vocabularies,
              matchText, simpleSearchOption, algorithm);

          if (iterator == null) {
			  msg = Constants.ERROR_NO_MATCH_FOUND_CODE_IS_CASESENSITIVE;
     		  request.getSession().setAttribute("message", msg);

		  } else {

			  try {
				  int numRemaining = iterator.numberRemaining();
				  if (numRemaining == 0) {
					  msg = "No match found.";
					  if (simpleSearchOption == SimpleSearchUtils.BY_CODE) {
						  msg = Constants.ERROR_NO_MATCH_FOUND_CODE_IS_CASESENSITIVE;
					  }
					  request.getSession().setAttribute("message", msg);
				  }

			  } catch (Exception ex) {
				msg = "No match found.";
				if (simpleSearchOption == SimpleSearchUtils.BY_CODE) {
					msg = Constants.ERROR_NO_MATCH_FOUND_CODE_IS_CASESENSITIVE;
				}
				request.getSession().setAttribute("message", msg);
				//return;// "message";
			  }
			  IteratorBean iteratorBean = new IteratorBean(iterator);
              String key = IteratorBeanManager.createIteratorKey("ALL_RVSCS", matchText,
                  searchOption, algorithm);
              iteratorBean.setKey(key);

			  request.getSession().setAttribute("value_set_entity_search_results", iteratorBean);

			  //return "value_set";
		  }


		  String contextPath = request.getContextPath();
		  String destination = contextPath + "/pages/value_set_entity_search_results.jsf";
		  try {
			  response.sendRedirect(response.encodeRedirectURL(destination));
		  } catch (Exception ex) {
			  ex.printStackTrace();
		  }
	  }

      public void search_value_set(HttpServletRequest request, HttpServletResponse response) {
        String selectValueSetSearchOption = HTTPUtils.cleanXSS((String) request.getParameter("selectValueSetSearchOption"));
		request.getSession().setAttribute("selectValueSetSearchOption", selectValueSetSearchOption);

        String algorithm = HTTPUtils.cleanXSS((String) request.getParameter("valueset_search_algorithm"));
        request.getSession().setAttribute("valueset_search_algorithm", algorithm);

		// check if any checkbox is checked.
        String contextPath = request.getContextPath();
		String view_str = HTTPUtils.cleanXSS((String) request.getParameter("view"));
		String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
		String root_vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("root_vsd_uri"));

		int view = Constants.STANDARD_VIEW;
		boolean isInteger = DataUtils.isInteger(view_str);
		if (isInteger) {
			view = Integer.parseInt(view_str);
		}

		if (vsd_uri != null && root_vsd_uri == null) {
			root_vsd_uri = vsd_uri;
		}

		String msg = null;

        String checked_vocabularies = get_checked_vocabularies(request);

		if (checked_vocabularies == null || checked_vocabularies.length() <= 1) {
			checked_vocabularies = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("checked_vocabularies"));
		}

//04182018
//System.out.println(checked_vocabularies);
request.getSession().setAttribute("checked_vocabularies", checked_vocabularies);

		request.getSession().removeAttribute("partial_checked_vocabularies");
        String matchText = HTTPUtils.cleanMatchTextXSS((String) request.getParameter("matchText"));
        if (DataUtils.isNull(matchText)) {
			matchText = "";
		} else {
			matchText = matchText.trim();
		}
        request.getSession().setAttribute("matchText", matchText);
		String ontology_display_name = HTTPUtils.cleanXSS((String) request.getParameter("ontology_display_name"));
		String ontology_version = HTTPUtils.cleanXSS((String) request.getParameter("ontology_version"));

		if (matchText.compareTo("") == 0) {
			msg = "Please enter a search string.";
			request.getSession().setAttribute("message", msg);
			if (!DataUtils.isNull(ontology_display_name) && !DataUtils.isNull(ontology_version)) {
				create_vs_tree(request, response, view, ontology_display_name, ontology_version);
			} else {
			    create_vs_tree(request, response, view);
			}
			return;
		}
		if (checked_vocabularies == null || checked_vocabularies.length() <= 1) {
			msg = "No value set is selected.";
			request.getSession().setAttribute("message", msg);
			if (!DataUtils.isNull(ontology_display_name) && !DataUtils.isNull(ontology_version)) {
				create_vs_tree(request, response, view, ontology_display_name, ontology_version);
			} else {
                vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
                if (DataUtils.isNull(vsd_uri)) {
					create_vs_tree(request, response, view);
				} else {
					create_vs_tree(request, response, view, vsd_uri);
				}
			}
		} else {
			request.getSession().setAttribute("checked_vocabularies", checked_vocabularies);
			try {
				String retstr = valueSetSearchAction(request);
				//KLO, 041312
				if (retstr.compareTo("message") == 0) {
					if (!DataUtils.isNull(ontology_display_name) && !DataUtils.isNull(ontology_version)) {
						create_vs_tree(request, response, view, ontology_display_name, ontology_version);
					} else {
 					    create_vs_tree(request, response, view);
					}
					return;
				}
				String destination = contextPath + "/pages/value_set_entity_search_results.jsf";
				if (!DataUtils.isNull(vsd_uri)) {
					destination = contextPath + "/pages/value_set_entity_search_results.jsf?value_set_tab=false&root_vsd_uri=" + root_vsd_uri;
				}
request.getSession().setAttribute("checked_vocabularies", checked_vocabularies);
				response.sendRedirect(response.encodeRedirectURL(destination));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
	    }
    }

    private String get_checked_vocabularies(HttpServletRequest request) {
		StringBuffer buf = new StringBuffer();
		Enumeration<String> parameterNames = request.getParameterNames();
		if (parameterNames == null) return null;
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			if (paramName == null) return null;
			if (paramName.indexOf("http://") != -1) {
				String paramValue = HTTPUtils.cleanXSS((String) request.getParameter(paramName));
				if (paramValue == null) return null;
				if (paramValue.compareTo("on") == 0) {
					buf.append(paramName).append(",");
				}
			}
		}
		String checked_vocabularies = buf.toString();
		if (checked_vocabularies == null) return null;
		if (checked_vocabularies.length() > 0) {
			checked_vocabularies = checked_vocabularies.substring(0, checked_vocabularies.length()-1);
		}
		return checked_vocabularies;
	}

    public String valueSetSearchAction(HttpServletRequest request) {
		String checked_vocabularies = null;
		checked_vocabularies = get_checked_vocabularies(request);
		if (checked_vocabularies == null || checked_vocabularies.length() < 1) {
			checked_vocabularies = (String) request.getSession().getAttribute("checked_vocabularies");
		}

		if (checked_vocabularies != null) {
			request.getSession().setAttribute("checked_vocabularies", checked_vocabularies);
		}

		java.lang.String valueSetDefinitionRevisionId = null;
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		String msg = null;

		long ms = System.currentTimeMillis();
        String selectValueSetSearchOption = HTTPUtils.cleanXSS((String) request.getParameter("selectValueSetSearchOption"));
		String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));

        if (DataUtils.isNull(selectValueSetSearchOption)) {
			selectValueSetSearchOption = "Name";
		}
		request.getSession().setAttribute("selectValueSetSearchOption", selectValueSetSearchOption);

        String algorithm = HTTPUtils.cleanXSS((String) request.getParameter("valueset_search_algorithm"));
        if (DataUtils.isNull(algorithm)) {
			algorithm = Constants.DEFAULT_SEARCH_ALGORITHM;//"exactMatch";
		}

		request.getSession().setAttribute("valueset_search_algorithm", algorithm);

		//Vector selected_vocabularies = DataUtils.parseData(checked_vocabularies, ",");
        String VSD_view = HTTPUtils.cleanXSS((String) request.getParameter("view"));
        request.getSession().setAttribute("view", VSD_view);

        String matchText = HTTPUtils.cleanMatchTextXSS((String) request.getParameter("matchText"));

        //LexEVSValueSetDefinitionServices vsd_service = null;
        //vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();

        if (matchText != null) matchText = matchText.trim();
        int searchOption = SimpleSearchUtils.BY_CODE;
        if (selectValueSetSearchOption.compareToIgnoreCase("Name") == 0 || selectValueSetSearchOption.compareToIgnoreCase("Names") == 0) {
			searchOption = SimpleSearchUtils.BY_NAME;
		}
        String serviceUrl = RemoteServerUtil.getServiceUrl();
        ResolvedConceptReferencesIterator iterator = new ValueSetSearchUtils(lbSvc, serviceUrl).searchResolvedValueSetCodingSchemes(checked_vocabularies,
            matchText, searchOption, algorithm);

        if (iterator == null) {
			msg = "No match found.";
			if (searchOption == SimpleSearchUtils.BY_CODE) {
   			    msg = Constants.ERROR_NO_MATCH_FOUND_CODE_IS_CASESENSITIVE;
			}
			request.getSession().setAttribute("message", msg);
			return "message";
		} else {
			/*
			ResolvedConceptReferencesIterator iterator = wrapper.getIterator();
			if (iterator == null) {
				msg = "No match found.";
				if (searchOption == SimpleSearchUtils.BY_CODE) {
					msg = Constants.ERROR_NO_MATCH_FOUND_CODE_IS_CASESENSITIVE;
				}
				request.getSession().setAttribute("message", msg);
				return "message";
			}
			*/
			try {
				int numRemaining = iterator.numberRemaining();
				if (numRemaining == 0) {
					msg = "No match found.";
					if (searchOption == SimpleSearchUtils.BY_CODE) {
						msg = Constants.ERROR_NO_MATCH_FOUND_CODE_IS_CASESENSITIVE;
					}
					request.getSession().setAttribute("message", msg);
					return "message";
				}
			} catch (Exception ex) {
				msg = "No match found.";
				if (searchOption == SimpleSearchUtils.BY_CODE) {
					msg = Constants.ERROR_NO_MATCH_FOUND_CODE_IS_CASESENSITIVE;
				}
				request.getSession().setAttribute("message", msg);
				return "message";
			}


			IteratorBean iteratorBean = new IteratorBean(iterator);
            String key = IteratorBeanManager.createIteratorKey(checked_vocabularies, matchText,
                selectValueSetSearchOption, algorithm);
            iteratorBean.setKey(key);
			request.getSession().setAttribute("value_set_entity_search_results", iteratorBean);
			return "value_set";
		}
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Possible error here?
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void create_vs_tree(HttpServletRequest request, HttpServletResponse response, int view, String dictionary, String version) {
	  request.getSession().removeAttribute("b");
	  request.getSession().removeAttribute("m");

      response.setContentType("text/html");
      PrintWriter out = null;

		//String checked_vocabularies = HTTPUtils.cleanXSS((String) request.getParameter("checked_vocabularies"));
		String checked_vocabularies = get_checked_vocabularies(request);
		String partial_checked_vocabularies = HTTPUtils.cleanXSS((String) request.getParameter("partial_checked_vocabularies"));

      try {
      	  out = response.getWriter();
      } catch (Exception ex) {
		  ex.printStackTrace();
		  return;
	  }

	  String message = (String) request.getSession().getAttribute("message");

      out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
      out.println("<html xmlns:c=\"http://java.sun.com/jsp/jstl/core\">");
      out.println("<head>");

	  out.println("  <title>" + dictionary + " value set</title>");

      out.println("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
      out.println("");

      out.println("<body>");

      out.println("");
      out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"http://yui.yahooapis.com/2.9.0/build/fonts/fonts-min.css\" />");
      out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"http://yui.yahooapis.com/2.9.0/build/treeview/assets/skins/sam/treeview.css\" />");
      out.println("");
      out.println("<script type=\"text/javascript\" src=\"http://yui.yahooapis.com/2.9.0/build/yahoo-dom-event/yahoo-dom-event.js\"></script>");
      out.println("<script type=\"text/javascript\" src=\"/ncitbrowser/js/yui/treeview-min.js\" ></script>");
      out.println("");
      out.println("");
      out.println("<!-- Dependency -->");
      out.println("<script src=\"http://yui.yahooapis.com/2.9.0/build/yahoo/yahoo-min.js\"></script>");
      out.println("");
      out.println("<!-- Source file -->");
      out.println("<!--");
      out.println("	If you require only basic HTTP transaction support, use the");
      out.println("	connection_core.js file.");
      out.println("-->");
      out.println("<script src=\"http://yui.yahooapis.com/2.9.0/build/connection/connection_core-min.js\"></script>");
      out.println("");
      out.println("<!--");
      out.println("	Use the full connection.js if you require the following features:");
      out.println("	- Form serialization.");
      out.println("	- File Upload using the iframe transport.");
      out.println("	- Cross-domain(XDR) transactions.");
      out.println("-->");
      out.println("<script src=\"http://yui.yahooapis.com/2.9.0/build/connection/connection-min.js\"></script>");
      out.println("");
      out.println("");
      out.println("");
      out.println("<!--begin custom header content for this example-->");
      out.println("<!--Additional custom style rules for this example:-->");
      out.println("<style type=\"text/css\">");
      out.println("");
      out.println("");
      out.println(".ygtvcheck0 { background: url(/ncitbrowser/images/yui/treeview/check0.gif) 0 0 no-repeat; width:16px; height:20px; float:left; cursor:pointer; }");
      out.println(".ygtvcheck1 { background: url(/ncitbrowser/images/yui/treeview/check1.gif) 0 0 no-repeat; width:16px; height:20px; float:left; cursor:pointer; }");
      out.println(".ygtvcheck2 { background: url(/ncitbrowser/images/yui/treeview/check2.gif) 0 0 no-repeat; width:16px; height:20px; float:left; cursor:pointer; }");
      out.println("");
      out.println("");
      out.println(".ygtv-edit-TaskNode  {	width: 190px;}");
      out.println(".ygtv-edit-TaskNode .ygtvcancel, .ygtv-edit-TextNode .ygtvok  {	border:none;}");
      out.println(".ygtv-edit-TaskNode .ygtv-button-container { float: right;}");
      out.println(".ygtv-edit-TaskNode .ygtv-input  input{	width: 140px;}");
      out.println(".whitebg {");
      out.println("	background-color:white;");
      out.println("}");
      out.println("</style>");
      out.println("");
      out.println("  <link rel=\"stylesheet\" type=\"text/css\" href=\"/ncitbrowser/css/styleSheet.css\" />");
      out.println("  <link rel=\"shortcut icon\" href=\"/ncitbrowser/favicon.ico\" type=\"image/x-icon\" />");
      out.println("");
      out.println("  <script type=\"text/javascript\" src=\"/ncitbrowser/js/script.js\"></script>");
      out.println("  <script type=\"text/javascript\" src=\"/ncitbrowser/js/tasknode.js\"></script>");

      println(out, "  <script type=\"text/javascript\" src=\"/ncitbrowser/js/search.js\"></script>");
      println(out, "  <script type=\"text/javascript\" src=\"/ncitbrowser/js/dropdown.js\"></script>");

	  out.println("<script type=\"text/javascript\"   src=\"/ncitbrowser/js/event_simulate.js\"></script>");
	  out.println("<script type=\"text/javascript\"   src=\"/ncitbrowser/js/value_set_tree_navigation.js\"></script>");


      out.println("");
      out.println("  <script type=\"text/javascript\">");
      out.println("");
      out.println("    function refresh() {");
      out.println("");
      out.println("      var selectValueSetSearchOptionObj = document.forms[\"valueSetSearchForm\"].selectValueSetSearchOption;");
      out.println("");
      out.println("      for (var i=0; i<selectValueSetSearchOptionObj.length; i++) {");
      out.println("        if (selectValueSetSearchOptionObj[i].checked) {");
      out.println("            selectValueSetSearchOption = selectValueSetSearchOptionObj[i].value;");
      out.println("        }");
      out.println("      }");
      out.println("");
      out.println("");
      out.println("      window.location.href=\"/ncitbrowser/pages/value_set_source_view.jsf?refresh=1\"");
      out.println("          + \"&nav_type=valuesets\" + \"&opt=\"+ selectValueSetSearchOption;");
      out.println("");
      out.println("    }");
      out.println("  </script>");
      out.println("");


 String contextPath = request.getContextPath();
 //String view_str = new Integer(view).toString();
 String view_str = Integer.valueOf(view).toString();


 String option = HTTPUtils.cleanXSS((String) request.getParameter("selectValueSetSearchOption"));
 String algorithm = HTTPUtils.cleanXSS((String) request.getParameter("valueset_search_algorithm"));


String option_code = "";
String option_name = "";
if (DataUtils.isNull(option)) {
	option_name = "checked";

} else {
	if (option.compareToIgnoreCase("Code") == 0) {
		option_code = "checked";
	}
	if (option.compareToIgnoreCase("Name") == 0) {
		option_name = "checked";
	}
}


String algorithm_exactMatch = "";
String algorithm_startsWith = "";
String algorithm_contains = "";

if (DataUtils.isNull(algorithm)) {
	algorithm = "contains";
}

if (algorithm.compareToIgnoreCase("exactMatch") == 0) {
	algorithm_exactMatch = "checked";
}

if (algorithm.compareToIgnoreCase("startsWith") == 0) {
	algorithm_startsWith = "checked";
	option_name = "checked";
	option_code = "";
}

if (algorithm.compareToIgnoreCase("contains") == 0) {
	algorithm_contains = "checked";
	option_name = "checked";
	option_code = "";
}
      out.println("<script>(function(i,s,o,g,r,a,m){i[\"GoogleAnalyticsObject\"]=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,\"script\",\"//www.google-analytics.com/analytics.js\",\"ga\");ga(\"create\", \"UA-150112876-1\", {\"cookieDomain\":\"auto\"});ga(\"send\", \"pageview\");</script>");
      out.println("</head>");
      out.println("");

      out.println("<body onLoad=\"document.forms.valueSetSearchForm.matchText.focus();\">");

      out.println("  <script type=\"text/javascript\" src=\"/ncitbrowser/js/wz_tooltip.js\"></script>");
      out.println("  <script type=\"text/javascript\" src=\"/ncitbrowser/js/tip_centerwindow.js\"></script>");
      out.println("  <script type=\"text/javascript\" src=\"/ncitbrowser/js/tip_followscroll.js\"></script>");


      out.println("  <!-- Begin Skip Top Navigation -->");
      out.println("  <a href=\"#evs-content\" class=\"hideLink\" accesskey=\"1\" title=\"Skip repetitive navigation links\">skip navigation links</A>");
      out.println("  <!-- End Skip Top Navigation -->");
      out.println("");
      out.println("<!-- nci banner -->");
      //out.println("<div class=\"ncibanner\">");
      out.println("<div style='clear:both;margin-top:-5px;padding:8px;height:32px;color:white;background-color:#C31F40'>");

      out.println("  <a href=\"http://www.cancer.gov\" target=\"_blank\">");
      out.println("    <img src=\"/ncitbrowser/images/banner-red.png\"");
      out.println("      width=\"955\" height=\"39\" border=\"0\"");
      out.println("      alt=\"National Cancer Institute\"/>");
      out.println("  </a>");

      /*
      out.println("  <a href=\"http://www.cancer.gov\" target=\"_blank\">");
      out.println("    <img src=\"/ncitbrowser/images/logotype.gif\"");
      out.println("      width=\"556\" height=\"39\" border=\"0\"");
      out.println("      alt=\"National Cancer Institute\"/>");
      out.println("  </a>");
      out.println("  <a href=\"http://www.cancer.gov\" target=\"_blank\">");
      out.println("    <img src=\"/ncitbrowser/images/spacer.gif\"");
      out.println("      width=\"60\" height=\"39\" border=\"0\"");
      out.println("      alt=\"National Cancer Institute\" class=\"print-header\"/>");
      out.println("  </a>");
      out.println("  <a href=\"http://www.nih.gov\" target=\"_blank\" >");
      out.println("    <img src=\"/ncitbrowser/images/tagline_nologo.gif\"");
      out.println("      width=\"219\" height=\"39\" border=\"0\"");
      out.println("      alt=\"U.S. National Institutes of Health\"/>");
      out.println("  </a>");
      out.println("  <a href=\"http://www.cancer.gov\" target=\"_blank\">");
      out.println("    <img src=\"/ncitbrowser/images/cancer-gov.gif\"");
      out.println("      width=\"125\" height=\"39\" border=\"0\"");
      out.println("      alt=\"www.cancer.gov\"/>");
      out.println("  </a>");
      */
      out.println("</div>");
      out.println("<!-- end nci banner -->");
      out.println("");
      out.println("  <div class=\"center-page_960\">");
      out.println("    <!-- EVS Logo -->");
      out.println("<div>");


// to be modified
      out.println("  <img src=\"/ncitbrowser/images/evs-logo-swapped.gif\" alt=\"EVS Logo\"");
      out.println("       width=\"941\" height=\"26\" border=\"0\"");
      out.println("       usemap=\"#external-evs\" />");
      out.println("  <map id=\"external-evs\" name=\"external-evs\">");
      out.println("    <area shape=\"rect\" coords=\"0,0,140,26\"");
      out.println("      href=\"/ncitbrowser/start.jsf\" target=\"_self\"");
      out.println("      alt=\"NCI Term Browser\" />");
      out.println("    <area shape=\"rect\" coords=\"520,0,745,26\"");
      out.println("      href=\"http://evs.nci.nih.gov/\" target=\"_blank\"");
      out.println("      alt=\"Enterprise Vocabulary Services\" />");
      out.println("  </map>");



      out.println("</div>");
      out.println("");
      out.println("");
      out.println("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role='presentation'>");
      out.println("  <tr>");
      out.println("    <td width=\"5\"></td>");

//to be modified
      out.println("    <td><a href=\"/ncitbrowser/pages/multiple_search.jsf?nav_type=terminologies\">");
      out.println("      <img name=\"tab_terms\" src=\"/ncitbrowser/images/tab_terms_clicked.gif\"");
      out.println("        border=\"0\" alt=\"Terminologies\" title=\"Terminologies\" /></a></td>");

//KLO 031214
      //out.println("    <td><a href=\"/ncitbrowser/ajax?action=create_src_vs_tree\">");
      out.println("    <td><a href=\"/ncitbrowser/ajax?action=create_src_vs_tree&nav_type=valuesets\">");
      out.println("      <img name=\"tab_valuesets\" src=\"/ncitbrowser/images/tab_valuesets.gif\"");
      out.println("        border=\"0\" alt=\"Value Sets\" title=\"ValueSets\" /></a></td>");
      out.println("    <td><a href=\"/ncitbrowser/pages/mapping_search.jsf?nav_type=mappings\">");
      out.println("      <img name=\"tab_map\" src=\"/ncitbrowser/images/tab_map.gif\"");
      out.println("        border=\"0\" alt=\"Mappings\" title=\"Mappings\" /></a></td>");


      out.println("  </tr>");
      out.println("</table>");



      out.println("");
      out.println("<div class=\"mainbox-top\"><img src=\"/ncitbrowser/images/mainbox-top.gif\" width=\"945\" height=\"5\" alt=\"\"/></div>");
      out.println("<!-- end EVS Logo -->");
      out.println("    <!-- Main box -->");
      out.println("    <div id=\"main-area_960\">");
      out.println("");



      out.println("      <!-- Thesaurus, banner search area -->");
      out.println("      <div class=\"bannerarea_960\">");



JSPUtils.JSPHeaderInfoMore info = new JSPUtils.JSPHeaderInfoMore(request);
String scheme = info.dictionary;
String term_browser_version = info.term_browser_version;
String display_name = info.display_name;
 String basePath = request.getContextPath();


String release_date = DataUtils.getVersionReleaseDate(scheme, version);
if (dictionary != null && dictionary.compareTo("NCI Thesaurus") == 0) {



      out.println("<a href=\"/ncitbrowser/pages/home.jsf?version=" + version + "\" style=\"text-decoration: none;\">");
      out.println("	<div class=\"vocabularynamebanner_ncit\">");
      out.println("	    <span class=\"vocabularynamelong_ncit\">Version: " + version + " (Release date: " + release_date + ")</span>");
      out.println("	</div>");
      out.println("</a>");



} else {

          out.write("\r\n");
          out.write("\r\n");
          out.write("                ");
			 if (version == null) {
					  out.write("\r\n");
					  out.write("                  <a class=\"vocabularynamebanner\" href=\"");
					  out.print(request.getContextPath());
					  out.write("/pages/vocabulary.jsf?dictionary=");
					  out.print(HTTPUtils.cleanXSS(dictionary));
					  out.write("\">\r\n");
					  out.write("                ");
			 } else {
					  out.write("\r\n");
					  out.write("                  <a class=\"vocabularynamebanner\" href=\"");
					  out.print(request.getContextPath());
					  out.write("/pages/vocabulary.jsf?dictionary=");
					  out.print(HTTPUtils.cleanXSS(dictionary));
					  out.write("&version=");
					  out.print(HTTPUtils.cleanXSS(version));
					  out.write("\">\r\n");
					  out.write("                ");
			 }
          out.write("\r\n");
          out.write("                    <div class=\"vocabularynamebanner\">\r\n");
          out.write("                      <div class=\"vocabularynameshort\" STYLE=\"font-size: ");
          out.print(HTTPUtils.maxFontSize(display_name));
          out.write("px; font-family : Arial\">\r\n");
          out.write("                          ");
          out.print(HTTPUtils.cleanXSS(display_name));
          out.write("\r\n");
          out.write("                      </div>\r\n");
          out.write("                      \r\n");


boolean display_release_date = true;
if (release_date == null || release_date.compareTo("") == 0) {
    display_release_date = false;
}
if (display_release_date) {

          out.write("\r\n");
          out.write("    <div class=\"vocabularynamelong\">Version: ");
          out.print(HTTPUtils.cleanXSS(term_browser_version));
          out.write(" (Release date: ");
          out.print(release_date);
          out.write(")</div>\r\n");

} else {

          out.write("\r\n");
          out.write("    <div class=\"vocabularynamelong\">Version: ");
          out.print(HTTPUtils.cleanXSS(term_browser_version));
          out.write("</div>\r\n");

}

          out.write("                    \r\n");
          out.write("                      \r\n");
          out.write("                    </div>\r\n");
          out.write("                  </a>\r\n");

}





      out.println("        <div class=\"search-globalnav_960\">");
      out.println("          <!-- Search box -->");
      out.println("          <div class=\"searchbox-top\"><img src=\"/ncitbrowser/images/searchbox-top.gif\" width=\"352\" height=\"2\" alt=\"SearchBox Top\" /></div>");
      out.println("          <div class=\"searchbox\">");
      out.println("");
      out.println("");

      out.println("<form id=\"valueSetSearchForm\" name=\"valueSetSearchForm\" method=\"post\" action=\"" + contextPath + "/ajax?action=search_value_set\" class=\"search-form-main-area\" enctype=\"application/x-www-form-urlencoded;charset=UTF-8\">");

      out.println("<input type=\"hidden\" name=\"valueSetSearchForm\" value=\"valueSetSearchForm\" />");

      out.println("<input type=\"hidden\" name=\"view\" value=\"" + view_str + "\" />");


String matchText = (String) request.getSession().getAttribute("matchText");
if (DataUtils.isNull(matchText)) {
	matchText = "";
}




      out.println("");
      out.println("");
      out.println("");
      out.println("            <input type=\"hidden\" id=\"checked_vocabularies\" name=\"checked_vocabularies\" value=\"\" />");
      out.println("            <input type=\"hidden\" id=\"partial_checked_vocabularies\" name=\"partial_checked_vocabularies\" value=\"\" />");
      out.println("");
      out.println("");
      out.println("");
      out.println("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin: 2px\" role='presentation'>");
      out.println("  <tr valign=\"top\" align=\"left\">");
      out.println("    <td align=\"left\" class=\"textbody\">");
      out.println("");
      out.println("                  <input CLASS=\"searchbox-input-2\"");
      out.println("                    name=\"matchText\"");
      out.println("                    value=\"" + matchText + "\"");
      out.println("                    onFocus=\"active = true\"");
      out.println("                    onBlur=\"active = false\"");
      out.println("                    onkeypress=\"return submitEnter('valueSetSearchForm:valueset_search',event)\"");
      out.println("                    tabindex=\"1\"/>");
      out.println("");
      out.println("");
      out.println("                <input id=\"valueSetSearchForm:valueset_search\" type=\"image\" src=\"/ncitbrowser/images/search.gif\" name=\"valueSetSearchForm:valueset_search\" alt=\"Search value sets containing matched concepts\" tabindex=\"2\" class=\"searchbox-btn\" /><a href=\"/ncitbrowser/pages/help.jsf#searchhelp\" tabindex=\"3\"><img src=\"/ncitbrowser/images/search-help.gif\" alt=\"Search Help\" style=\"border-width:0;\" class=\"searchbox-btn\" /></a>");
      out.println("");
      out.println("");
      out.println("    </td>");
      out.println("  </tr>");
      out.println("");
      out.println("  <tr valign=\"top\" align=\"left\">");
      out.println("    <td>");
      out.println("      <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin: 0px\" role='presentation'>");

      out.println("        <tr valign=\"top\" align=\"left\">");
      out.println("        <td align=\"left\" class=\"textbody\">");
      out.println("                     <input type=\"radio\" name=\"valueset_search_algorithm\" value=\"contains\" alt=\"Contains\" "      + algorithm_contains   + " tabindex=\"3\"  onclick=\"onVSAlgorithmChanged();\">Contains");
      out.println("                     <input type=\"radio\" name=\"valueset_search_algorithm\" value=\"exactMatch\" alt=\"Exact Match\" " + algorithm_exactMatch + " tabindex=\"3\">Exact Match&nbsp;");
      out.println("                     <input type=\"radio\" name=\"valueset_search_algorithm\" value=\"startsWith\" alt=\"Begins With\" " + algorithm_startsWith + " tabindex=\"3\"  onclick=\"onVSAlgorithmChanged();\">Begins With&nbsp;");
      out.println("        </td>");
      out.println("        </tr>");
      out.println("");
      out.println("        <tr align=\"left\">");
      out.println("            <td height=\"1px\" bgcolor=\"#2F2F5F\" align=\"left\"></td>");
      out.println("        </tr>");
      out.println("        <tr valign=\"top\" align=\"left\">");
      out.println("          <td align=\"left\" class=\"textbody\">");
      out.println("                <input type=\"radio\" id=\"selectValueSetSearchOption\" name=\"selectValueSetSearchOption\" value=\"Name\" " + option_name + " alt=\"Name\" checked tabindex=\"4\"  >Name&nbsp;");
      out.println("                <input type=\"radio\" id=\"selectValueSetSearchOption\" name=\"selectValueSetSearchOption\" value=\"Code\" " + option_code + " alt=\"Code\" tabindex=\"4\" onclick=\"onVSCodeButtonPressed();\">Code&nbsp;");
      out.println("          </td>");
      out.println("        </tr>");


      out.println("      </table>");
      out.println("    </td>");
      out.println("  </tr>");
      out.println("</table>");
      out.println("                <input type=\"hidden\" name=\"referer\" id=\"referer\" value=\"<%=HTTPUtils.getRefererParmEncode(request)%>\">");
      out.println("                <input type=\"hidden\" id=\"nav_type\" name=\"nav_type\" value=\"valuesets\" />");
      out.println("                <input type=\"hidden\" id=\"view\" name=\"view\" value=\"source\" />");

      out.println("            <input type=\"hidden\" id=\"ontology_display_name\" name=\"ontology_display_name\" value=\"" + dictionary + "\" />");
      out.println("            <input type=\"hidden\" id=\"schema\" name=\"schema\" value=\"" + dictionary + "\" />");
      out.println("            <input type=\"hidden\" id=\"ontology_version\" name=\"ontology_version\" value=\"" + version + "\" />");

      out.println("");
//      out.println("<input type=\"hidden\" name=\"javax.faces.ViewState\" id=\"javax.faces.ViewState\" value=\"j_id22:j_id23\" />");
      out.println("</form>");

      //addHiddenForm(out, checked_vocabularies, partial_checked_vocabularies);


      out.println("          </div> <!-- searchbox -->");
      out.println("");
      out.println("          <div class=\"searchbox-bottom\"><img src=\"/ncitbrowser/images/searchbox-bottom.gif\" width=\"352\" height=\"2\" alt=\"SearchBox Bottom\" /></div>");
      out.println("          <!-- end Search box -->");


      out.println("          <!-- Global Navigation -->");
      out.println("");


 boolean hasValueSet = DataUtils.getValueSetHierarchy().hasValueSet(scheme);
 boolean hasMapping = DataUtils.hasMapping(scheme);

 boolean tree_access_allowed = true;
 if (DataUtils.get_vocabulariesWithoutTreeAccessHashSet().contains(scheme)) {
     tree_access_allowed = false;
 }
 boolean vocabulary_isMapping = DataUtils.isMapping(scheme, null);


          out.write("                  <table class=\"global-nav\" border=\"0\" width=\"100%\" height=\"30px\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
          out.write("                    <tr>\r\n");
          out.write("                      <td valign=\"bottom\">\r\n");
          out.write("                         ");
 Boolean[] isPipeDisplayed = new Boolean[] { Boolean.FALSE };
          out.write("\r\n");
          out.write("                         ");
 if (vocabulary_isMapping) {
          out.write("\r\n");
          out.write("                              ");
          out.print( JSPUtils.getPipeSeparator(isPipeDisplayed) );
          out.write("\r\n");
          out.write("                              <a href=\"");
          out.print(request.getContextPath() );
          out.write("/pages/mapping.jsf?dictionary=");
          out.print(HTTPUtils.cleanXSS(scheme));
          out.write("&version=");
          out.print(version);
          out.write("\">\r\n");
          out.write("                                Mapping\r\n");
          out.write("                              </a>\r\n");
          out.write("                         ");
 } else if (tree_access_allowed) {
          out.write("\r\n");
          out.write("                              ");
          out.print( JSPUtils.getPipeSeparator(isPipeDisplayed) );
          out.write("\r\n");
          out.write("                              <a href=\"#\" onclick=\"javascript:window.open('");
          out.print(request.getContextPath());
          out.write("/pages/hierarchy.jsf?dictionary=");
          out.print(HTTPUtils.cleanXSS(scheme));
          out.write("&version=");
          out.print(HTTPUtils.cleanXSS(version));
          out.write("', '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');\" tabindex=\"12\">\r\n");
          out.write("                                Hierarchy </a>\r\n");
          out.write("                         ");
 }
          out.write("       \r\n");
          out.write("                                \r\n");
          out.write("                                \r\n");
          out.write("      ");
 if (hasValueSet) {
          out.write("\r\n");
          out.write("        ");
          out.print( JSPUtils.getPipeSeparator(isPipeDisplayed) );
          out.write("\r\n");
          out.write("        <!--\r\n");
          out.write("        <a href=\"");
          out.print( request.getContextPath() );
          out.write("/pages/value_set_hierarchy.jsf?dictionary=");
          out.print(HTTPUtils.cleanXSS(scheme));
          out.write("&version=");
          out.print(HTTPUtils.cleanXSS(version));
          out.write("\" tabindex=\"15\">Value Sets</a>\r\n");
          out.write("        -->\r\n");
          out.write("        <a href=\"");
          out.print( request.getContextPath() );
          out.write("/ajax?action=create_cs_vs_tree&dictionary=");

          //out.print(HTTPUtils.cleanXSS(scheme));
          String scheme_name = HTTPUtils.cleanXSS(DataUtils.getFormalName(scheme));
		  out.print(scheme_name);

          out.write("&version=");
          out.print(HTTPUtils.cleanXSS(version));
          out.write("\" tabindex=\"15\">Value Sets</a>\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("      ");
 }
          out.write("\r\n");
          out.write("      \r\n");
          out.write("      ");
 if (hasMapping) {
          out.write("\r\n");
          out.write("          ");
          out.print( JSPUtils.getPipeSeparator(isPipeDisplayed) );
          out.write("\r\n");
          out.write("          <a href=\"");
          out.print( request.getContextPath() );
          out.write("/pages/cs_mappings.jsf?dictionary=");
          out.print(HTTPUtils.cleanXSS(scheme));
          out.write("&version=");
          out.print(HTTPUtils.cleanXSS(version));
          out.write("\" tabindex=\"15\">Maps</a>      \r\n");
          out.write("      ");
 }


 String cart_size = (String) request.getSession().getAttribute("cart_size");
 if (!DataUtils.isNull(cart_size)) {
          out.write("|");
          out.write("                        <a href=\"");
          out.print(request.getContextPath());
          out.write("/pages/cart.jsf\" tabindex=\"16\">Cart</a>\r\n");
 }


          out.write("                         ");
          out.print( VisitedConceptUtils.getDisplayLink(request, isPipeDisplayed) );
          out.write("\r\n");
          out.write("                      </td>\r\n");
          out.write("                      <td align=\"right\" valign=\"bottom\">\r\n");
          out.write("                        <a href=\"");
          out.print(request.getContextPath());
          out.write("/pages/help.jsf\" tabindex=\"16\">Help</a>\r\n");
          out.write("                      </td>\r\n");


          out.write("                      <td width=\"7\" valign=\"bottom\"></td>\r\n");
          out.write("                    </tr>\r\n");
          out.write("                  </table>\r\n");




      out.println("          <!-- end Global Navigation -->");
      out.println("");
      out.println("        </div> <!-- search-globalnav -->");
      out.println("      </div> <!-- bannerarea -->");
      out.println("");
      out.println("      <!-- end Thesaurus, banner search area -->");
      out.println("      <!-- Quick links bar -->");
      out.println("");
      out.println("<div class=\"bluebar\">");
      out.println("  <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" role='presentation'>");
      out.println("  <tr>");
      out.println("    <td><div class=\"quicklink-status\">&nbsp;</div></td>");
      out.println("    <td>");
      out.println("");


      addQuickLink(request, out);


      out.println("");
      out.println("      </td>");
      out.println("    </tr>");
      out.println("  </table>");
      out.println("");
      out.println("</div>");

      if (! ServerMonitorThread.getInstance().isLexEVSRunning()) {
		  out.println("    <div class=\"redbar\">");
		  out.println("      <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" role='presentation'>");
		  out.println("        <tr>");
		  out.println("          <td class=\"lexevs-status\">");
		  out.println("            " + ServerMonitorThread.getInstance().getMessage());
		  out.println("          </td>");
		  out.println("        </tr>");
		  out.println("      </table>");
		  out.println("    </div>");
      }

      out.println("      <!-- end Quick links bar -->");
      out.println("");
      out.println("      <!-- Page content -->");
      out.println("      <div class=\"pagecontent\">");
      out.println("");

      if (!DataUtils.isNullOrBlank(message)) {
		  out.println("\r\n");
		  out.println("      <p class=\"textbodyred\">");
		  out.print(message);
		  out.println("</p>\r\n");
		  out.println("    ");
		  request.getSession().removeAttribute("message");
      }

      out.println("");
      out.println("        <div id=\"popupContentArea\">");
      out.println("          <a name=\"evs-content\" id=\"evs-content\"></a>");
      out.println("");
      out.println("          <table width=\"580px\" cellpadding=\"3\" cellspacing=\"0\" border=\"0\" role='presentation'>");
      out.println("");
      out.println("");
      out.println("");
      out.println("");
      out.println("            <tr class=\"textbody\">");
      out.println("              <td class=\"textbody\" align=\"left\">");
      out.println("");


      out.println("              </td>");
      out.println("");
      out.println("              <td align=\"right\">");
      out.println("               <font size=\"1\" color=\"red\" align=\"right\">");
      out.println("                 <a href=\"javascript:printPage()\"><img src=\"/ncitbrowser/images/printer.bmp\" border=\"0\" alt=\"Send to Printer\"><i>Send to Printer</i></a>");
      out.println("               </font>");


      out.println("              </td>");
      out.println("            </tr>");
      out.println("          </table>");
      out.println("");
      out.println("          <hr/>");
      out.println("");
      out.println("");
      out.println("");
      out.println("<style>");
      out.println("#expandcontractdiv {border:1px solid #336600; background-color:#FFFFCC; margin:0 0 .5em 0; padding:0.2em;}");
      out.println("#treecontainer { background: #fff }");
      out.println("</style>");
      out.println("");
      out.println("");

      SimpleTreeUtils stu = new SimpleTreeUtils(DataUtils.getVocabularyNameSet());

      stu.printSelectAllOrNoneLinks(out);


stu.printFormHeader(out);
TreeItem root = null;

//HashMap value_set_tree_hmap = DataUtils.getCodingSchemeValueSetTree(scheme, version);
HashMap value_set_tree_hmap = DataUtils.getCodingSchemeValueSetSubTree(scheme);

stu.printTree(out, value_set_tree_hmap);
stu.printFormFooter(out);
out.flush();


      out.println("");
      out.println("");
      out.println("");
      out.println("          <!-- Tree content -->");
      out.println("");
      out.println("          <div id=\"treecontainer\" class=\"ygtv-checkbox\"></div>");
      out.println("");
      out.println("          <form id=\"pg_form\" enctype=\"application/x-www-form-urlencoded;charset=UTF-8\">");
      out.println("");
      out.println("            <input type=\"hidden\" id=\"ontology_node_id\" name=\"ontology_node_id\" value=\"null\" />");
      out.println("            <input type=\"hidden\" id=\"ontology_display_name\" name=\"ontology_display_name\" value=\"" + dictionary + "\" />");
      out.println("            <input type=\"hidden\" id=\"schema\" name=\"schema\" value=\"" + dictionary + "\" />");
      out.println("            <input type=\"hidden\" id=\"ontology_version\" name=\"ontology_version\" value=\"" + version + "\" />");
      out.println("            <input type=\"hidden\" id=\"view\" name=\"view\" value=\"source\" />");
      out.println("          </form>");
      out.println("");

      out.println("");
      out.println("        </div> <!-- popupContentArea -->");

      out.println("");
      out.println("");
      out.println("<div class=\"textbody\">");
      out.println("<!-- footer -->");
      out.println("<div class=\"footer\" style=\"width:920px\">");
      out.println("  <ul>");
      out.println("    <li><a href=\"http://www.cancer.gov\" target=\"_blank\" alt=\"National Cancer Institute\">NCI Home</a> |</li>");
      out.println("    <li><a href=\"/ncitbrowser/pages/contact_us.jsf\">Contact Us</a> |</li>");
      out.println("    <li><a href=\"http://www.cancer.gov/policies\" target=\"_blank\" alt=\"National Cancer Institute Policies\">Policies</a> |</li>");
      out.println("    <li><a href=\"http://www.cancer.gov/policies/page3\" target=\"_blank\" alt=\"National Cancer Institute Accessibility\">Accessibility</a> |</li>");
      out.println("    <li><a href=\"http://www.cancer.gov/policies/page6\" target=\"_blank\" alt=\"National Cancer Institute FOIA\">FOIA</a></li>");
      out.println("  </ul>");

      out.println("<center>");
      out.println("<a href=\"http://www.hhs.gov/\" alt=\"U.S. Department of Health and Human Services\">");
      out.println("U.S. Department of Health and Human Services");
      out.println("</a>");
      out.println("&nbsp;|&nbsp;");
      out.println("<a href=\"https://www.nih.gov/about-nih\" alt=\"National Institutes of Health\">");
      out.println("National Institutes of Health");
      out.println("</a>");
      out.println("&nbsp;|&nbsp;");
      out.println("<a href=\"http://www.cancer.gov/\" alt=\"National Cancer Institute\">");
      out.println("National Cancer Institute");
      out.println("</a>");
      out.println("&nbsp;|&nbsp;");
      out.println("<a href=\"https://www.usa.gov/\" alt=\"USA.gov\">");
      out.println("USA.gov");
      out.println("</a>");
      out.println("</center>");

      out.println("</div>");
      out.println("<!-- end footer -->");
      out.println("</div>");
      out.println("");
      out.println("");
      out.println("      </div> <!-- pagecontent -->");
      out.println("    </div> <!--  main-area -->");
      out.println("    <div class=\"mainbox-bottom\"><img src=\"/ncitbrowser/images/mainbox-bottom.gif\" width=\"945\" height=\"5\" alt=\"Mainbox Bottom\" /></div>");
      out.println("");
      out.println("  </div> <!-- center-page -->");
      out.println("");

      addHiddenForm(out, checked_vocabularies, partial_checked_vocabularies);

      out.println("</body>");
      out.println("</html>");
      out.println("");
  }


   public void addHiddenForm(PrintWriter out, String checkedNodes, String partialCheckedNodes) {
      out.println("   <form id=\"hidden_form\" enctype=\"application/x-www-form-urlencoded;charset=UTF-8\">");
      out.println("      <input type=\"hidden\" id=\"checkedNodes\" name=\"checkedNodes\" value=\"" + checkedNodes + "\" />");
      out.println("      <input type=\"hidden\" id=\"partialCheckedNodes\" name=\"partialCheckedNodes\" value=\"" + partialCheckedNodes + "\" />");
      out.println("   </form>");
   }

   public void writeInitialize(PrintWriter out) {
      out.println("   function initialize() {");
      out.println("	     tree = new YAHOO.widget.TreeView(\"treecontainer\");");
      out.println("	     initializeNodeCheckState();");
      out.println("	     tree.expandAll();");
      out.println("	     tree.draw();");
      out.println("   }");
   }

/*
      * checkState
      * 0=unchecked, 1=some children checked, 2=all children checked
      * @type int
*/

   public void initializeNodeCheckState(PrintWriter out) {
	   initializeNodeCheckState(out, Boolean.TRUE);
   }

   public void initializeNodeCheckState(PrintWriter out, Boolean value_set_tab) {
      out.println("   function initializeNodeCheckState(nodes) {");
      out.println("       nodes = nodes || tree.getRoot().children;");
      out.println("       var checkedNodes = document.forms[\"hidden_form\"].checkedNodes.value;");
      out.println("       var partialCheckedNodes = document.forms[\"hidden_form\"].partialCheckedNodes.value;");
      out.println("       for(var i=0, l=nodes.length; i<l; i=i+1) {");
      out.println("            var n = nodes[i];");
      out.println("");

      if (value_set_tab.equals(Boolean.TRUE)) {
		  out.println("            if (checkedNodes.indexOf(n.label) != -1) {");
		  out.println("                n.setCheckState(2);");
		  out.println("            } else if (partialCheckedNodes.indexOf(n.label) != -1) {");
		  out.println("                n.setCheckState(1);");
		  out.println("            }");
	  } else {
          out.println("                n.setCheckState(2);");
	  }


      out.println("");
      out.println("            if (n.hasChildren()) {");
      out.println("		        initializeNodeCheckState(n.children);");
      out.println("            }");
      out.println("        }");
      out.println("   }");
   }


    public void addQuickLink(HttpServletRequest request, PrintWriter out) {

		String basePath = request.getContextPath();
		//String ncim_url = new DataUtils().getNCImURL();
		//String ncim_url = new ConceptDetails().getNCImURL();
		String ncim_url = DataUtils.getNCImURL();
		String quicklink_dictionary = (String) request.getSession().getAttribute("dictionary");
		quicklink_dictionary = DataUtils.getFormalName(quicklink_dictionary);
		String term_suggestion_application_url2 = "";
		String dictionary_encoded2 = "";
		if (quicklink_dictionary != null) {
			term_suggestion_application_url2 = DataUtils.getMetadataValue(quicklink_dictionary, "term_suggestion_application_url");
			dictionary_encoded2 = DataUtils.replaceAll(quicklink_dictionary, " ", "%20");
		}


              out.write("  <div id=\"quicklinksholder\">\r\n");
              out.write("      <ul id=\"quicklinks\"\r\n");
              out.write("        onmouseover=\"document.quicklinksimg.src='");
              out.print(basePath);
              out.write("/images/quicklinks-active.gif';\"\r\n");
              out.write("        onmouseout=\"document.quicklinksimg.src='");
              out.print(basePath);
              out.write("/images/quicklinks-inactive.gif';\">\r\n");
              out.write("        <li>\r\n");
              out.write("          <a href=\"#\" tabindex=\"-1\"><img src=\"");
              out.print(basePath);
              out.write("/images/quicklinks-inactive.gif\" width=\"162\"\r\n");
              out.write("            height=\"18\" border=\"0\" name=\"quicklinksimg\" alt=\"Quick Links\" />\r\n");
              out.write("          </a>\r\n");
              out.write("          <ul>\r\n");
              out.write("            <li><a href=\"http://evs.nci.nih.gov/\" tabindex=\"-1\" target=\"_blank\"\r\n");
              out.write("              alt=\"Enterprise Vocabulary Services\">EVS Home</a></li>\r\n");
              out.write("            <li><a href=\"");
              out.print(ncim_url);
              out.write("\" tabindex=\"-1\" target=\"_blank\"\r\n");
              out.write("              alt=\"NCI Metathesaurus\">NCI Metathesaurus Browser</a></li>\r\n");
              out.write("\r\n");
              out.write("            ");

            if (quicklink_dictionary == null || quicklink_dictionary.compareTo("NCI Thesaurus") != 0) {

              out.write("\r\n");
              out.write("\r\n");
              out.write("            <li><a href=\"");
              out.print( request.getContextPath() );
              out.write("/index.jsp\" tabindex=\"-1\"\r\n");
              out.write("              alt=\"NCI Thesaurus Browser\">NCI Thesaurus Browser</a></li>\r\n");
              out.write("\r\n");
              out.write("            ");

            }

              out.write("\r\n");
              out.write("\r\n");
              out.write("            <li>\r\n");
              out.write("              <a href=\"");
              out.print( request.getContextPath() );
              out.write("/termbrowser.jsf\" tabindex=\"-1\" alt=\"NCI Term Browser\">NCI Term Browser</a>\r\n");
              out.write("            </li>\r\n");
              out.write("              \r\n");
              out.write("            <li><a href=\"http://www.cancer.gov/cancertopics/terminologyresources\" tabindex=\"-1\" target=\"_blank\"\r\n");
              out.write("              alt=\"NCI Terminology Resources\">NCI Terminology Resources</a></li>\r\n");
              out.write("            ");
 if (term_suggestion_application_url2 != null && term_suggestion_application_url2.length() > 0) {
              out.write("\r\n");
              out.write("              <li><a href=\"");
              out.print(term_suggestion_application_url2);
              out.write("?dictionary=");
              out.print(dictionary_encoded2);
              out.write("\" tabindex=\"-1\" target=\"_blank\" alt=\"Term Suggestion\">Term Suggestion</a></li>\r\n");
              out.write("            ");
 }
              out.write("\r\n");
              out.write("\r\n");
              out.write("          </ul>\r\n");
              out.write("        </li>\r\n");
              out.write("      </ul>\r\n");
              out.write("  </div>\r\n");

	  }




    public void selectCSVersionAction(HttpServletRequest request, HttpServletResponse response) {
        String selectedvalueset = null;
        String uri = null;
        String multiplematches = HTTPUtils.cleanXSS((String) request.getParameter("multiplematches"));
        if (multiplematches != null) {
			selectedvalueset = HTTPUtils.cleanXSS((String) request.getParameter("valueset"));
			uri = selectedvalueset;
		} else {
			uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
			if (uri.indexOf("|") != -1) {
				Vector u = DataUtils.parseData(uri);
				uri = (String) u.elementAt(1);
			}
		}
		request.getSession().setAttribute("vsd_uri", uri);

        try {
			String nextJSP = "/pages/resolve_value_set.jsf";
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request,response);
			return;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


    //[NCITERM-768] Migrate to lexevsapi65-6.5.1.
    public void resolveValueSetAction(HttpServletRequest request, HttpServletResponse response) {
        String selectedvalueset = null;
        String multiplematches = HTTPUtils.cleanXSS((String) request.getParameter("multiplematches"));
        if (multiplematches != null) {
			selectedvalueset = HTTPUtils.cleanXSS((String) request.getParameter("valueset"));
		} else {
			selectedvalueset = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
			if (selectedvalueset != null && selectedvalueset.indexOf("|") != -1) {
				Vector u = StringUtils.parseData(selectedvalueset);
				selectedvalueset = (String) u.elementAt(1);
			}
		}

		String valueset_search_algorithm = HTTPUtils.cleanXSS((String) request.getParameter("valueset_search_algorithm"));
		String selectValueSetSearchOption = HTTPUtils.cleanXSS((String) request.getParameter("selectValueSetSearchOption"));
		request.getSession().setAttribute("valueset_search_algorithm", valueset_search_algorithm);
		request.getSession().setAttribute("searchTarget", selectValueSetSearchOption);

        String vsd_uri = selectedvalueset;
		request.getSession().setAttribute("selectedvalueset", selectedvalueset);
        request.getSession().setAttribute("vsd_uri", vsd_uri);
        String[] coding_scheme_ref = null;
        String key = vsd_uri;

        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        String serviceUrl = RemoteServerUtil.getServiceUrl();

		key = vsd_uri;
		coding_scheme_ref = new String[1];
		coding_scheme_ref[0] = "NCI_Thesaurus";
        request.getSession().setAttribute("coding_scheme_ref", coding_scheme_ref);

		try {
			AssertedValueSetUtils avsu = new AssertedValueSetUtils(serviceUrl, lbSvc);
			//ResolvedConceptReferencesIterator itr = csdu.resolveCodingScheme(vsd_uri, null, false);
			ResolvedConceptReferencesIterator itr = avsu.getValueSetIteratorForURI(vsd_uri);

			IteratorBeanManager iteratorBeanManager = null;

			if (FacesContext.getCurrentInstance() != null &&
				FacesContext.getCurrentInstance().getExternalContext() != null &&
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap() != null) {
				 iteratorBeanManager = (IteratorBeanManager) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("iteratorBeanManager");
			}

			if (iteratorBeanManager == null) {
				iteratorBeanManager = new IteratorBeanManager();
				request.getSession().setAttribute("iteratorBeanManager", iteratorBeanManager);
			}

			request.getSession().setAttribute("ResolvedConceptReferencesIterator", itr);
			IteratorBean iteratorBean = iteratorBeanManager.getIteratorBean(key);
			if (iteratorBean == null) {
				iteratorBean = new IteratorBean(itr);
				iteratorBean.initialize();
				iteratorBean.setKey(key);
				iteratorBeanManager.addIteratorBean(iteratorBean);
			}

			request.getSession().setAttribute("coding_scheme_ref", coding_scheme_ref);
			request.getSession().setAttribute("ResolvedConceptReferencesIterator", itr);
			request.getSession().setAttribute("resolved_vs_key", key);

			try {
				String nextJSP = "/pages/resolved_value_set.jsf";
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
				dispatcher.forward(request,response);
				return;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return;

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String msg = "Unable to resolve the value set " + vsd_uri;
		request.getSession().setAttribute("message", msg);
        try {
			String nextJSP = "/pages/resolved_value_set.jsf?vsd_uri="+vsd_uri;
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request,response);
			return;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

    public String convertValueSetURI(String uri) {
		if (!uri.startsWith(Constants.VALUE_SET_URI_PREFIX)) {
			int n = uri.lastIndexOf(":");
			if (n != -1) {
				String code = uri.substring(n+1, uri.length());
				return Constants.VALUE_SET_URI_PREFIX + code;
			}
		}
		return uri;
	}


//KLO 071814
    public void downloadValueSetAction(HttpServletRequest request, HttpServletResponse response) {
        String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
        String refresh = HTTPUtils.cleanXSS((String) request.getParameter("refresh"));
        String resultsPerPage = HTTPUtils.cleanXSS((String) request.getParameter("resultsPerPage"));

        String valueset_search_algorithm = HTTPUtils.cleanXSS((String) request.getParameter("valueset_search_algorithm"));
        if (valueset_search_algorithm != null) {
            request.getSession().setAttribute("valueset_search_algorithm", valueset_search_algorithm);
		}

        String selectValueSetSearchOption = HTTPUtils.cleanXSS((String) request.getParameter("selectValueSetSearchOption"));
        if (selectValueSetSearchOption != null) {
            request.getSession().setAttribute("selectValueSetSearchOption", selectValueSetSearchOption);
		}

        if (DataUtils.isNull(refresh)) {
			ValueSetConfig vsc = ValueSetDefinitionConfig.getValueSetConfig(vsd_uri);
			String table_content = null;
			StringBuffer table_content_buf = new StringBuffer();

			String filename = vsc.getReportURI();
			String excelfile = ValueSetDefinitionConfig.getValueSetDownloadFilename(vsc);

            //[NCITERM-680] Released File views get out of sync with ftp publications
			//if (!ValueSetDefinitionConfig.fileExists(excelfile)) {
				 FTPDownload.download(vsc.getReportURI(), excelfile);
			//}

			Vector u = ValueSetDefinitionConfig.interpretExtractionRule(vsc.getExtractionRule());
			int col = -1;
			int sheet = -1;
			String code = null;

			if (u == null || (u.size() != 2 && u.size() != 3)) {
				System.out.println("Data not found.");
			} else {
				sheet = Integer.parseInt((String) u.elementAt(0)) - 1;
				if (u.size() == 2) {
					code = (String) u.elementAt(1);

				} else if (u.size() == 3) {
					col = Integer.parseInt((String) u.elementAt(1)) - 1;
					code = (String) u.elementAt(2);
				}

				int startIndex = ExcelUtil.getExcelStartRow(excelfile, sheet, col, code);

				boolean cdisc = false;
				if (vsc.getExtractionRule() != null && !vsc.getExtractionRule().endsWith(":all")) {
					String header = ExcelUtil.getExcelHeader(excelfile, sheet);
					if (header != null && header.indexOf(Constants.CDISC_SUBMISSION_VALUE) != -1) {
						startIndex = startIndex - 1;
						cdisc = true;
					}
				}

				request.getSession().removeAttribute("rvsi");
				//if (startIndex != -1 && endIndex != -1) {
				if (startIndex != -1) {
					try {
						//String url = "/ncitbrowser/ConceptReport.jsp?dictionary=NCI%20Thesaurus";
						String url = "/ncitbrowser/ConceptReport.jsp?dictionary=NCI%20Thesaurus";
						String ncit_production_version = DataUtils.getProductionVersion(Constants.NCI_THESAURUS);
						if (ncit_production_version != null) {
							url = url + "&version=" + ncit_production_version;
						}
						//[NCITERM-731] Concepts selected from a Value Sets' Released File: View Graph not viewable due to ns=null in the URL
						//09082016
						//url = url + "&ns=NCI%20Thesaurus";
						//url = url + "&ns=NCI_Thesaurus";
						LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
						String ns = new ConceptDetails(lbSvc).getNamespaceByCode(Constants.NCIT_CS_NAME, ncit_production_version, code);
						url = url + "&ns=" + ns;

						ResolvedValueSetIteratorHolder rvsi = new ResolvedValueSetIteratorHolder(excelfile, sheet, startIndex, col, code, url, cdisc);
						request.getSession().setAttribute("rvsi", rvsi);
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					try {
						String nextJSP = "/pages/download_value_set.jsf?vsd_uri=" + vsd_uri;
						RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
						dispatcher.forward(request,response);
						return;

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

			//String msg = "Unable to resolve the value set " + vsd_uri;
			String msg = Constants.NO_VALUE_SET_REPORT_AVAILABLE;
			request.getSession().setAttribute("message", msg);
	    }

        try {
			String nextJSP = "/pages/download_value_set.jsf?vsd_uri=" + vsd_uri;
			if (resultsPerPage != null) {
				nextJSP = nextJSP + "&resultsPerPage=" + resultsPerPage;
			}

			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request,response);
			return;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

    public String valueSetDefinition2XMLString(String uri) {

        LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
        String s = null;
        String valueSetDefinitionRevisionId = null;
        try {
			URI valueSetDefinitionURI = new URI(uri);
			StringBuffer buf = vsd_service.exportValueSetDefinition(valueSetDefinitionURI, valueSetDefinitionRevisionId);
            s = buf.toString();
        } catch (Exception ex) {
           ex.printStackTrace();
        }
		return s;
	}


    public void exportVSDToXMLAction(HttpServletRequest request, HttpServletResponse response) {
       String selectedvalueset = null;
       String multiplematches = HTTPUtils.cleanXSS((String) request.getParameter("multiplematches"));
        if (multiplematches != null) {
			selectedvalueset = HTTPUtils.cleanXSS((String) request.getParameter("valueset"));
		} else {
			selectedvalueset = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
			if (selectedvalueset != null && selectedvalueset.indexOf("|") != -1) {
				Vector u = DataUtils.parseData(selectedvalueset);
				selectedvalueset = (String) u.elementAt(1);
			}
	    }
        String uri = selectedvalueset;
		request.getSession().setAttribute("selectedvalueset", uri);

        String xml_str = valueSetDefinition2XMLString(uri);

		try {
			response.setContentType("text/xml");

			String vsd_name = DataUtils.valueSetDefinitionURI2Name(uri);
			vsd_name = vsd_name.replaceAll(" ", "_");
			vsd_name = vsd_name + ".xml";

		    response.setHeader("Content-Disposition", "attachment; filename="
					+ vsd_name);

			response.setContentLength(xml_str.length());

			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(xml_str.getBytes("UTF8"), 0, xml_str.length());
			ouputStream.flush();
			ouputStream.close();

		} catch(IOException e) {
			e.printStackTrace();
		}

		FacesContext.getCurrentInstance().responseComplete();
	}

    public void export_mapping_search(HttpServletRequest request, HttpServletResponse response) {
        String mapping_schema = HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
        String mapping_version = HTTPUtils.cleanXSS((String) request.getParameter("version"));
        MappingIteratorBean iteratorBean = (MappingIteratorBean) request.getSession().getAttribute("mapping_search_results");
        int size = iteratorBean.getSize();
        StringBuffer sb = new StringBuffer();

        try {
			sb.append("Source Code,");
			sb.append("Source Name,");
			sb.append("Source Coding Scheme,");
			sb.append("Source Coding Scheme Version,");
			sb.append("Source Coding Scheme Namespace,");

			sb.append("Association Name,");
			sb.append("REL,");
			sb.append("Map Rank,");

			sb.append("Target Code,");
			sb.append("Target Name,");
			sb.append("Target Coding Scheme,");
			sb.append("Target Coding Scheme Version,");
			sb.append("Target Coding Scheme Namespace");
			sb.append("\r\n");

            List list = iteratorBean.getData(0, size-1);
            if (list == null) return;
            for (int k=0; k<list.size(); k++) {
				MappingData mappingData = (MappingData) list.get(k);
				sb.append("\"" + mappingData.getSourceCode() + "\",");
				sb.append("\"" + mappingData.getSourceName() + "\",");
				sb.append("\"" + mappingData.getSourceCodingScheme() + "\",");
				sb.append("\"" + mappingData.getSourceCodingSchemeVersion() + "\",");
				sb.append("\"" + mappingData.getSourceCodeNamespace() + "\",");

				sb.append("\"" + mappingData.getAssociationName() + "\",");
				sb.append("\"" + mappingData.getRel() + "\",");
				sb.append("\"" + mappingData.getScore() + "\",");

				sb.append("\"" + mappingData.getTargetCode() + "\",");
				sb.append("\"" + mappingData.getTargetName() + "\",");
				sb.append("\"" + mappingData.getTargetCodingScheme() + "\",");
				sb.append("\"" + mappingData.getTargetCodingSchemeVersion() + "\",");
				sb.append("\"" + mappingData.getTargetCodeNamespace() + "\"");
				sb.append("\r\n");
			}
		} catch (Exception ex)	{
			sb.append("WARNING: Export to CVS action failed.");
			ex.printStackTrace();
		}

		String filename = mapping_schema + "_" + mapping_version + "_search_results";
		filename = filename.replaceAll(" ", "_");
		filename = filename + ".csv";

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ filename);

		response.setContentLength(sb.length());

		try {
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(sb.toString().getBytes("UTF-8"), 0, sb.length());
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			sb.append("WARNING: Export to CVS action failed.");
		}
		FacesContext.getCurrentInstance().responseComplete();
		return;

	}

	public static String escapeCommaCharacters(String s) {
		if (s == null) return null;
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			if (c != ',') {
				buf.append(c);
			} else {
				buf.append(c).append(c);
			}
		}
		return buf.toString();
	}

    public void export_mapping(HttpServletRequest request, HttpServletResponse response) {
        String mapping_schema = HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
        String mapping_version = HTTPUtils.cleanXSS((String) request.getParameter("version"));
		String sourceCode = null;
		String sourceName = null;
		String sourceCodingScheme = null;
		String sourceCodingSchemeVesion = null;
		String sourceCodeNamespace = null;
		String rel = null;
		int score = 0;
		String targetCode = null;
		String targetName = null;
		String targetCodingScheme = null;
		String targetCodingSchemeVesion = null;
		String targetCodeNamespace = null;
		String description = null;
		String associationName = null;

        ResolvedConceptReferencesIterator _iterator = DataUtils.getMappingDataIterator(mapping_schema, mapping_version);
		int numRemaining = 0;
		if (_iterator != null) {
			try {
				numRemaining = _iterator.numberRemaining();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		ServletOutputStream ouputStream = null;

		String filename = mapping_schema + "_" + mapping_version;
		filename = filename.replaceAll(" ", "_");
		filename = filename + ".csv";

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ filename);


        StringBuffer sb = new StringBuffer();
        try {
			ouputStream = response.getOutputStream();

			sb.append("Source Code,");
			sb.append("Source Name,");
			sb.append("Source Coding Scheme,");
			sb.append("Source Coding Scheme Version,");
			sb.append("Source Coding Scheme Namespace,");

			sb.append("Association Name,");
			sb.append("REL,");
			sb.append("Map Rank,");

			sb.append("Target Code,");
			sb.append("Target Name,");
			sb.append("Target Coding Scheme,");
			sb.append("Target Coding Scheme Version,");
			sb.append("Target Coding Scheme Namespace");
			sb.append("\r\n");
			ouputStream.write(sb.toString().getBytes("UTF-8"), 0, sb.length());
			//ouputStream.flush();

			while (_iterator.hasNext()) {
				sb = new StringBuffer();
				ResolvedConceptReference ref = _iterator.next();
				description = null;
				if(ref.getEntityDescription() == null) {
					description = "NOT AVAILABLE";
				} else {
					description = ref.getEntityDescription().getContent();
				}
				sourceCode = ref.getCode();
				sourceName = description;
				sourceCodingScheme = ref.getCodingSchemeName();
				sourceCodingSchemeVesion = ref.getCodingSchemeVersion();
				sourceCodeNamespace = ref.getCodeNamespace();
				rel = null;
				score = 0;

				AssociationList assocs = ref.getSourceOf();
				if(assocs != null){
					for(Association assoc : assocs.getAssociation()){
						associationName = assoc.getAssociationName();
						int lcv = 0;
						for(AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept()){
							lcv++;
							if(ac.getEntityDescription() == null) {
								description = "NOT AVAILABLE";
							} else {
								description = ac.getEntityDescription().getContent();
							}
							targetCode = ac.getCode();
							targetName = description;
							targetCodingScheme = ac.getCodingSchemeName();
							targetCodingSchemeVesion = ac.getCodingSchemeVersion();
							targetCodeNamespace = ac.getCodeNamespace();

							if (ac.getAssociationQualifiers() != null && ac.getAssociationQualifiers().getNameAndValue() != null) {
								for (NameAndValue qual : ac.getAssociationQualifiers().getNameAndValue()) {
									String qualifier_name = qual.getName();
									String qualifier_value = qual.getContent();
									if (qualifier_name.compareTo("rel") == 0) {
										rel = qualifier_value;
									} else if (qualifier_name.compareTo("score") == 0) {
										score = Integer.parseInt(qualifier_value);
									}
								}
							}

							MappingData mappingData = new MappingData(
								sourceCode,
								sourceName,
								sourceCodingScheme,
								sourceCodingSchemeVesion,
								sourceCodeNamespace,
								associationName,
								rel,
								score,
								targetCode,
								targetName,
								targetCodingScheme,
								targetCodingSchemeVesion,
								targetCodeNamespace);

							sb = new StringBuffer();
							sb.append("\"" + mappingData.getSourceCode() + "\",");
							sb.append("\"" + escapeCommaCharacters(mappingData.getSourceName()) + "\",");
							sb.append("\"" + mappingData.getSourceCodingScheme() + "\",");
							sb.append("\"" + mappingData.getSourceCodingSchemeVersion() + "\",");
							sb.append("\"" + mappingData.getSourceCodeNamespace() + "\",");

							sb.append("\"" + mappingData.getAssociationName() + "\",");
							sb.append("\"" + mappingData.getRel() + "\",");
							sb.append("\"" + mappingData.getScore() + "\",");

							sb.append("\"" + mappingData.getTargetCode() + "\",");
							sb.append("\"" + escapeCommaCharacters(mappingData.getTargetName()) + "\",");
							sb.append("\"" + mappingData.getTargetCodingScheme() + "\",");
							sb.append("\"" + mappingData.getTargetCodingSchemeVersion() + "\",");
							sb.append("\"" + mappingData.getTargetCodeNamespace() + "\"");
							sb.append("\r\n");
							ouputStream.write(sb.toString().getBytes("UTF-8"), 0, sb.length());
							ouputStream.flush();
						}
					}
				}

				assocs = ref.getTargetOf();
				if(assocs != null){
					for(Association assoc : assocs.getAssociation()){
						associationName = assoc.getAssociationName();

						int lcv = 0;
						for(AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept()){
							lcv++;
							if(ac.getEntityDescription() == null) {
								description = "NOT AVAILABLE";
							} else {
								description = ac.getEntityDescription().getContent();
							}
							targetCode = ac.getCode();
							targetName = description;
							targetCodingScheme = ac.getCodingSchemeName();
							targetCodingSchemeVesion = ac.getCodingSchemeVersion();
							targetCodeNamespace = ac.getCodeNamespace();

							if (ac.getAssociationQualifiers() != null && ac.getAssociationQualifiers().getNameAndValue() != null) {
								for (NameAndValue qual : ac.getAssociationQualifiers().getNameAndValue()) {
									String qualifier_name = qual.getName();
									String qualifier_value = qual.getContent();
									if (qualifier_name.compareTo("rel") == 0) {
										rel = qualifier_value;
									} else if (qualifier_name.compareTo("score") == 0) {
										score = Integer.parseInt(qualifier_value);
									}
								}
							}

							MappingData mappingData = new MappingData(
								sourceCode,
								sourceName,
								sourceCodingScheme,
								sourceCodingSchemeVesion,
								sourceCodeNamespace,
								associationName,
								rel,
								score,
								targetCode,
								targetName,
								targetCodingScheme,
								targetCodingSchemeVesion,
								targetCodeNamespace);


							sb = new StringBuffer();
							sb.append("\"" + mappingData.getSourceCode() + "\",");
							sb.append("\"" + escapeCommaCharacters(mappingData.getSourceName()) + "\",");
							sb.append("\"" + mappingData.getSourceCodingScheme() + "\",");
							sb.append("\"" + mappingData.getSourceCodingSchemeVersion() + "\",");
							sb.append("\"" + mappingData.getSourceCodeNamespace() + "\",");

							sb.append("\"" + mappingData.getAssociationName() + "\",");
							sb.append("\"" + mappingData.getRel() + "\",");
							sb.append("\"" + mappingData.getScore() + "\",");

							sb.append("\"" + mappingData.getTargetCode() + "\",");
							sb.append("\"" + escapeCommaCharacters(mappingData.getTargetName()) + "\",");
							sb.append("\"" + mappingData.getTargetCodingScheme() + "\",");
							sb.append("\"" + mappingData.getTargetCodingSchemeVersion() + "\",");
							sb.append("\"" + mappingData.getTargetCodeNamespace() + "\"");
							sb.append("\r\n");
							ouputStream.write(sb.toString().getBytes("UTF-8"), 0, sb.length());
							ouputStream.flush();
						}
					}
				}
			}
		} catch (Exception ex)	{
			sb.append("WARNING: Export to CVS action failed.");
			ex.printStackTrace();
		}

		//response.setContentLength(sb.length());
		try {
			//ouputStream.write(sb.toString().getBytes("UTF-8"), 0, sb.length());
			//ouputStream.flush();
			ouputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			//sb.append("WARNING: Export to CVS action failed.");
		}
		FacesContext.getCurrentInstance().responseComplete();
		return;
	}

	public int countEdges(HashMap relMap, String[] types) {
		if (relMap == null || types == null) return 0;
		int knt = 0;
		List typeList = Arrays.asList(types);
		for (int k=0; k<Constants.ALL_RELATIONSHIP_TYPES.length; k++) {
			String rel_type = (String) Constants.ALL_RELATIONSHIP_TYPES[k];
			if (typeList.contains(rel_type)) {
				List list = (ArrayList) relMap.get(rel_type);
				if (list != null) {
					knt = knt + list.size();
				}
			}
		}
        return knt;
	}


    public void view_graph(HttpServletRequest request, HttpServletResponse response,
        String scheme, String version, String namespace, String code, String type) {
	  LexBIGService lb_svc = RemoteServerUtil.createLexBIGService();

	  if (StringUtils.isNullOrBlank(namespace)) {
		    LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		    namespace = new ConceptDetails(lbSvc).getNamespaceByCode(scheme, version, code);
	  }

	  HashMap hmap = null;
	  String focused_concept = scheme + "|" + version + "|" + namespace + "|" +code;
	  String prev_focused_concept = (String) request.getSession().getAttribute("prev_focused_concept");
	  if (prev_focused_concept == null || prev_focused_concept.compareTo(focused_concept) != 0) {
		  RelationshipUtils relationshipUtils = new RelationshipUtils(lb_svc);
		  hmap = relationshipUtils.getRelationshipHashMap(scheme, version, code, namespace, true);
		  request.getSession().setAttribute("RelationshipHashMap", hmap);
		  request.getSession().setAttribute("prev_focused_concept", focused_concept);
	  } else {
		  hmap = (HashMap) request.getSession().getAttribute("RelationshipHashMap");
	  }

	  // compute nodes and edges using hmap
	  VisUtils visUtils = new VisUtils(lb_svc);
	  String[] types = null;
	  if (type == null || type.compareTo("ALL") == 0) {
		  types = Constants.ALL_RELATIONSHIP_TYPES;
	  } else {
		  types = new String[1];
		  types[0] = type;
	  }
	  int edge_count = countEdges(hmap, types);
	  //String nodes_and_edges =  visUtils.generateGraphScript(scheme, version, namespace, code, types, VisUtils.NODES_AND_EDGES, hmap);
      //boolean graph_reduced = false;
	  Vector v = visUtils.generateGraphScriptVector(scheme, version, namespace, code, types, VisUtils.NODES_AND_EDGES, hmap);
      String nodes_and_edges = null;
      String group_node_data = "";
      String group_node_id = null;
      String group_node_data_2 = "";
      String group_node_id_2 = null;
      boolean direction = true;
      HashMap group_node_id2dataMap = new HashMap();


      GraphReductionUtils graphReductionUtils = new GraphReductionUtils();
      int graph_size = graphReductionUtils.getNodeCount(v);

      //KLO, 02122016
      graphReductionUtils.initialize_group_node_id(graph_size);
      if (graph_size > graphReductionUtils.MINIMUM_REDUCED_GRAPH_SIZE) {

		  group_node_id = graphReductionUtils.getGroupNodeId(v);
		  int group_node_id_int = Integer.parseInt(group_node_id);
		  group_node_id_2 = new Integer(group_node_id_int+1).toString();
		  Vector w = graphReductionUtils.reduce_graph(v, direction);

		  boolean graph_reduced = graphReductionUtils.graph_reduced(v, w);
		  if (graph_reduced) {
			  group_node_data = graphReductionUtils.get_removed_node_str(v, direction);
			  Vector group_node_ids = graphReductionUtils.get_group_node_ids(w);
			  if (group_node_ids == null) return;
			  for (int k=0; k<group_node_ids.size(); k++) {
				  String node_id = (String) group_node_ids.elementAt(k);
				  if (!group_node_id2dataMap.containsKey(node_id)) {
					  group_node_id2dataMap.put(node_id, group_node_data);
					  break;
				  }
			  }

			  nodes_and_edges =  GraphUtils.generateGraphScript(w);
			  v = (Vector) w.clone();
		  }


		  direction = false;
		  w = graphReductionUtils.reduce_graph(v, direction);
		  graph_reduced = graphReductionUtils.graph_reduced(v, w);
		  if (graph_reduced) {
			  group_node_data_2 = graphReductionUtils.get_removed_node_str(v, direction);
			  Vector group_node_ids = graphReductionUtils.get_group_node_ids(w);
			  if (group_node_ids == null) return;
			  for (int k=0; k<group_node_ids.size(); k++) {
				  String node_id = (String) group_node_ids.elementAt(k);
				  if (!group_node_id2dataMap.containsKey(node_id)) {
					  group_node_id2dataMap.put(node_id, group_node_data_2);
					  break;
				  }
			  }
			  nodes_and_edges =  GraphUtils.generateGraphScript(w);
			  v = (Vector) w.clone();
		  }
      }

      if (group_node_id2dataMap.keySet().size() == 0) {
		  nodes_and_edges =  visUtils.generateGraphScript(scheme, version, namespace, code, types, VisUtils.NODES_AND_EDGES, hmap);
	  }

	  Vector group_node_ids = graphReductionUtils.get_group_node_ids(v);
	  boolean graph_available = true;
	  if (nodes_and_edges.compareTo(GraphUtils.NO_DATA_AVAILABLE) == 0) {
		  graph_available = false;
	  }
      response.setContentType("text/html");
      PrintWriter out = null;

      try {
      	  out = response.getWriter();
      } catch (Exception ex) {
		  ex.printStackTrace();
		  return;
	  }

      out.println("<!doctype html>");
      out.println("<html>");
      out.println("<head>");
      out.println("  <title>View Graph</title>");
      out.println("");
      out.println("  <style type=\"text/css\">");
      out.println("    body {");
      out.println("      font: 10pt sans;");
      out.println("    }");
      out.println("    #conceptnetwork {");
      out.println("      width: 1200px;");
      if (edge_count > 50) {
      	  out.println("      height: 800px;");
	  } else {
		  out.println("      height: 600px;");
	  }
      out.println("      border: 1px solid lightgray;");
      out.println("    }");
      out.println("    table.legend_table {");
      out.println("      border-collapse: collapse;");
      out.println("    }");
      out.println("    table.legend_table td,");
      out.println("    table.legend_table th {");
      out.println("      border: 1px solid #d3d3d3;");
      out.println("      padding: 10px;");
      out.println("    }");
      out.println("");
      out.println("    table.legend_table td {");
      out.println("      text-align: center;");
      out.println("      width:110px;");
      out.println("    }");
      out.println("  </style>");
      out.println("");
      out.println("  <script type=\"text/javascript\" src=\"/ncitbrowser/css/vis/vis.js\"></script>");
      out.println("  <link rel=\"stylesheet\" type=\"text/css\" href=\"/ncitbrowser/css/vis/vis.css\" />");
      out.println("  <link rel=\"stylesheet\" type=\"text/css\" href=\"/ncitbrowser/css/styleSheet.css\" />");

      out.println("");
      out.println("  <script type=\"text/javascript\">");
      out.println("    var nodes = null;");
      out.println("    var edges = null;");
      out.println("    var network = null;");
      out.println("");

      out.println("    function reset_graph(id) {");
      out.println("        window.location.href=\"/ncitbrowser/ajax?action=reset_graph&id=\" + id;");
      out.println("    }");


      out.println("    function destroy() {");
      out.println("      if (network !== null) {");
      out.println("        network.destroy();");
      out.println("        network = null;");
      out.println("      }");
      out.println("    }");
      out.println("");

      out.println("    function draw() {");

	  if (graph_available) {
		  out.println(nodes_and_edges);
	  }

      out.println("      // create a network");
      out.println("      var container = document.getElementById('conceptnetwork');");
      out.println("      var data = {");
      out.println("        nodes: nodes,");
      out.println("        edges: edges");
      out.println("      };");

      if (type.endsWith("path")) {
		  out.println("            var directionInput = document.getElementById(\"direction\").value;");
		  out.println("            var options = {");
		  out.println("                layout: {");
		  out.println("                    hierarchical: {");
		  out.println("                        direction: directionInput");
		  out.println("                    }");
		  out.println("                }");
		  out.println("            };");


	  } else {

		  out.println("      var options = {");
		  out.println("        interaction: {");
		  out.println("          navigationButtons: true,");
		  out.println("          keyboard: true");
		  out.println("        }");
		  out.println("      };");

      }

      out.println("      network = new vis.Network(container, data, options);");
      out.println("");
      out.println("      // add event listeners");


      out.println("      network.on('select', function(params) {");

      Iterator it = group_node_id2dataMap.keySet().iterator();
      if (it == null) return;
      while (it.hasNext()) {
		  String node_id = (String) it.next();
		  String node_data = (String) group_node_id2dataMap.get(node_id);
		  out.println("      if (params.nodes == '" + node_id + "') {");
		  out.println("         document.getElementById('selection').innerHTML = '" + node_data + "';");
		  out.println("      }");
	  }

      out.println("      });");
      out.println("			network.on(\"doubleClick\", function (params) {");

      String node_id_1 = null;
      String node_id_2 = null;
      it = group_node_id2dataMap.keySet().iterator();
      int lcv = 0;
      while (it.hasNext()) {
		  String node_id = (String) it.next();
		  if (lcv == 0) {
			  node_id_1 = node_id;
		  } else if (lcv == 1) {
			  node_id_2 = node_id;
		  }
		  lcv++;
  	  }

  	  if (node_id_1 != null && node_id_2 != null) {
		  out.println("      if (params.nodes != '" + node_id_1 + "' && params.nodes != '" + node_id_2 + "') {");
		  out.println("				params.event = \"[original event]\";");
		  out.println("				var json = JSON.stringify(params, null, 4);");
		  out.println("				reset_graph(params.nodes);");
		  out.println("      }");
  	  } else if (node_id_1 != null && node_id_2 == null) {
		  out.println("      if (params.nodes != '" + node_id_1 + "') {");
		  out.println("				params.event = \"[original event]\";");
		  out.println("				var json = JSON.stringify(params, null, 4);");
		  out.println("				reset_graph(params.nodes);");
		  out.println("      }");
  	  } else if (node_id_2 != null && node_id_1 == null) {
		  out.println("      if (params.nodes != '" + node_id_2 + "') {");
		  out.println("				params.event = \"[original event]\";");
		  out.println("				var json = JSON.stringify(params, null, 4);");
		  out.println("				reset_graph(params.nodes);");
		  out.println("      }");
  	  } else if (node_id_2 == null && node_id_1 == null) {
		  out.println("				params.event = \"[original event]\";");
		  out.println("				var json = JSON.stringify(params, null, 4);");
		  out.println("				reset_graph(params.nodes);");
	  }

      out.println("		    });");

      out.println("    }");
      out.println("  </script>");
      out.println("<script>(function(i,s,o,g,r,a,m){i[\"GoogleAnalyticsObject\"]=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,\"script\",\"//www.google-analytics.com/analytics.js\",\"ga\");ga(\"create\", \"UA-150112876-1\", {\"cookieDomain\":\"auto\"});ga(\"send\", \"pageview\");</script>");

      out.println("</head>");
      out.println("");
      out.println("<body onload=\"draw();\">");

      //out.println("<div class=\"ncibanner\">");
      out.println("<div style='clear:both;margin-top:-5px;padding:8px;height:32px;color:white;background-color:#C31F40'>");

      out.println("  <a href=\"http://www.cancer.gov\" target=\"_blank\">");
      out.println("    <img src=\"/ncitbrowser/images/banner-red.png\"");
      out.println("      width=\"955\" height=\"39\" border=\"0\"");
      out.println("      alt=\"National Cancer Institute\"/>");
      out.println("  </a>");
      /*
      out.println("  <a href=\"http://www.cancer.gov\" target=\"_blank\">     ");
      out.println("    <img src=\"/ncitbrowser/images/logotype.gif\"");
      out.println("      width=\"556\" height=\"39\" border=\"0\"");
      out.println("      alt=\"National Cancer Institute\"/>");
      out.println("  </a>");
      out.println("  <a href=\"http://www.cancer.gov\" target=\"_blank\">     ");
      out.println("    <img src=\"/ncitbrowser/images/spacer.gif\"");
      out.println("      width=\"60\" height=\"39\" border=\"0\" ");
      out.println("      alt=\"National Cancer Institute\" class=\"print-header\"/>");
      out.println("  </a>");
      out.println("  <a href=\"http://www.nih.gov\" target=\"_blank\" >      ");
      out.println("    <img src=\"/ncitbrowser/images/tagline_nologo.gif\"");
      out.println("      width=\"219\" height=\"39\" border=\"0\"");
      out.println("      alt=\"U.S. National Institutes of Health\"/>");
      out.println("  </a>");
      out.println("  <a href=\"http://www.cancer.gov\" target=\"_blank\">      ");
      out.println("    <img src=\"/ncitbrowser/images/cancer-gov.gif\"");
      out.println("      width=\"125\" height=\"39\" border=\"0\"");
      out.println("      alt=\"www.cancer.gov\"/>");
      out.println("  </a>");
      */

      out.println("</div>");
      out.println("<p></p>");

	  if (!graph_available) {
		  out.println("<p class=\"textbodyred\">&nbsp;No graph data is available.</p>");
	  }

      out.println("<form id=\"data\" method=\"post\" action=\"/ncitbrowser/ajax?action=view_graph\">");

      out.println("Relationships");
      out.println("<select name=\"type\" >");
      if (type == null || type.compareTo("ALL") == 0) {
     	  out.println("  <option value=\"ALL\" selected>ALL</option>");
      } else {
		  out.println("  <option value=\"ALL\">ALL</option>");
	  }
	  String rel_type = null;
	  String option_label = null;

	  for (int k=0; k<Constants.ALL_RELATIONSHIP_TYPES.length; k++) {
          rel_type = (String) Constants.ALL_RELATIONSHIP_TYPES[k];
          List list = (List) hmap.get(rel_type);
          if (list != null && list.size() > 0) {
			  option_label = VisUtils.getRelatinshipLabel(rel_type);
			  if (type.compareTo(rel_type) == 0) {
				  out.println("  <option value=\"" + rel_type + "\" selected>" + option_label + "</option>");
			  } else {
				  out.println("  <option value=\"" + rel_type + "\">" + option_label + "</option>");
			  }
	      }
	  }

	  boolean hasPartOf = new PartonomyUtils(lb_svc).hasPartOfRelationships(hmap);
	  if (hasPartOf) {
		  rel_type = "type_part_of";
		  option_label = VisUtils.getRelatinshipLabel(rel_type);
		  if (type.compareTo(rel_type) == 0) {
			  out.println("  <option value=\"" + rel_type + "\" selected>" + option_label + "</option>");
		  } else {
			  out.println("  <option value=\"" + rel_type + "\">" + option_label + "</option>");
		  }

		  rel_type = "type_part_of_path";
		  option_label = VisUtils.getRelatinshipLabel(rel_type);
		  if (type.compareTo(rel_type) == 0) {
			  out.println("  <option value=\"" + rel_type + "\" selected>" + option_label + "</option>");
		  } else {
			  out.println("  <option value=\"" + rel_type + "\">" + option_label + "</option>");
		  }
	  }

      out.println("</select>");
      out.println("<input type=\"hidden\" id=\"scheme\" name=\"scheme\" value=\"" + scheme + "\" />");
      out.println("<input type=\"hidden\" id=\"version\" name=\"version\" value=\"" + version + "\" />");
      out.println("<input type=\"hidden\" id=\"ns\" name=\"ns\" value=\"" + namespace + "\" />");
      out.println("<input type=\"hidden\" id=\"code\" name=\"code\" value=\"" + code + "\" />");


      request.getSession().setAttribute("scheme", scheme);
      request.getSession().setAttribute("version", version);
      request.getSession().setAttribute("ns", namespace);
      request.getSession().setAttribute("code", code);
      request.getSession().setAttribute("nodes_and_edges", nodes_and_edges);


      out.println("");
      out.println("&nbsp;&nbsp;");
      out.println("<input type=\"submit\" value=\"Refresh\"></input>");
      out.println("</form>");
      out.println("");

      if (type.endsWith("path")) {

      out.println("<p>");
      out.println("    <input type=\"button\" id=\"btn-UD\" value=\"Up-Down\">");
      out.println("    <input type=\"button\" id=\"btn-DU\" value=\"Down-Up\">");
      out.println("    <input type=\"button\" id=\"btn-LR\" value=\"Left-Right\">");
      out.println("    <input type=\"button\" id=\"btn-RL\" value=\"Right-Left\">");
      out.println("    <input type=\"hidden\" id='direction' value=\"UD\">");
      out.println("</p>");
      out.println("<script language=\"javascript\">");
      out.println("    var directionInput = document.getElementById(\"direction\");");
      out.println("    var btnUD = document.getElementById(\"btn-UD\");");
      out.println("    btnUD.onclick = function () {");
      out.println("        directionInput.value = \"UD\";");
      out.println("        draw();");
      out.println("    }");
      out.println("    var btnDU = document.getElementById(\"btn-DU\");");
      out.println("    btnDU.onclick = function () {");
      out.println("        directionInput.value = \"DU\";");
      out.println("        draw();");
      out.println("    };");
      out.println("    var btnLR = document.getElementById(\"btn-LR\");");
      out.println("    btnLR.onclick = function () {");
      out.println("        directionInput.value = \"LR\";");
      out.println("        draw();");
      out.println("    };");
      out.println("    var btnRL = document.getElementById(\"btn-RL\");");
      out.println("    btnRL.onclick = function () {");
      out.println("        directionInput.value = \"RL\";");
      out.println("        draw();");
      out.println("    };");
      out.println("</script>");
      }

      out.println("<div style=\"width: 800px; font-size:14px; text-align: justify;\">");
      out.println("</div>");
      out.println("");
      out.println("<div id=\"conceptnetwork\"></div>");
      out.println("");
      out.println("<p id=\"selection\"></p>");
      out.println("</body>");
      out.println("</html>");

      out.flush();

		try {
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}


   }

    public void search_downloaded_value_set(HttpServletRequest request, HttpServletResponse response) {
		java.lang.String valueSetDefinitionRevisionId = null;

		String msg = null;
		long ms = System.currentTimeMillis();

        String selectValueSetSearchOption = HTTPUtils.cleanXSS((String) request.getParameter("selectValueSetSearchOption"));
		String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));

        if (DataUtils.isNull(selectValueSetSearchOption)) {
			selectValueSetSearchOption = "Name";
		}
		request.getSession().setAttribute("selectValueSetSearchOption", selectValueSetSearchOption);

        String algorithm = HTTPUtils.cleanXSS((String) request.getParameter("valueset_search_algorithm"));
        if (DataUtils.isNull(algorithm)) {
			algorithm = Constants.DEFAULT_SEARCH_ALGORITHM;//"exactMatch";
		}

		//String checked_vocabularies = HTTPUtils.cleanXSS((String) request.getParameter("checked_vocabularies"));
		String checked_vocabularies = get_checked_vocabularies(request);

		if (checked_vocabularies != null) {
			checked_vocabularies = checked_vocabularies.trim();
		}
		if (DataUtils.isNullOrBlank(checked_vocabularies)) {
			checked_vocabularies = find_checked_value_sets(request);
		}

		if (DataUtils.isNullOrBlank(checked_vocabularies)) {
			checked_vocabularies = vsd_uri;
		}
		if (checked_vocabularies != null) {
			request.getSession().setAttribute("checked_vocabularies", checked_vocabularies);
		}
		if (checked_vocabularies == null || checked_vocabularies.compareTo("") <= 1) {
			msg = "No value set is selected.";
			request.getSession().setAttribute("message", msg);
		}

		//Vector selected_vocabularies = DataUtils.parseData(checked_vocabularies, ",");
        String VSD_view = HTTPUtils.cleanXSS((String) request.getParameter("view"));
        request.getSession().setAttribute("view", VSD_view);

        String matchText = HTTPUtils.cleanMatchTextXSS((String) request.getParameter("matchText"));

        //LexEVSValueSetDefinitionServices vsd_service = null;
        //vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();

        if (matchText != null) matchText = matchText.trim();

        int searchOption = SimpleSearchUtils.BY_CODE;
        if (selectValueSetSearchOption.compareTo("Name") == 0) {
			searchOption = SimpleSearchUtils.BY_NAME;
		}
        request.getSession().setAttribute("valueset_search_algorithm", algorithm);
        request.getSession().setAttribute("searchTarget", selectValueSetSearchOption);
        request.getSession().setAttribute("matchText_RVS", matchText);

        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        String serviceUrl = RemoteServerUtil.getServiceUrl();
        ResolvedConceptReferencesIterator iterator = new ValueSetSearchUtils(lbSvc, serviceUrl).searchResolvedValueSetCodingSchemes(checked_vocabularies,
            matchText, searchOption, algorithm);

        if (iterator == null) {
			msg = "No match found.";
			if (searchOption == SimpleSearchUtils.BY_CODE) {
   			    msg = Constants.ERROR_NO_MATCH_FOUND_CODE_IS_CASESENSITIVE;
			}
			request.getSession().setAttribute("message", msg);
			//return "message";
		} else {
			Vector matched_concept_codes = new Vector();

				try {
					while (iterator.hasNext()) {
					        ResolvedConceptReference rcr = (ResolvedConceptReference) iterator.next();
							//String name = rcr.getEntityDescription().getContent();
							String concept_code = rcr.getConceptCode();
							matched_concept_codes.add(concept_code);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				request.getSession().setAttribute("matched_concept_codes", matched_concept_codes);
			//}

			try {
				int numRemaining = matched_concept_codes.size();

				if (numRemaining == 0) {
					msg = "No match found.";
					if (searchOption == SimpleSearchUtils.BY_CODE) {
						msg = Constants.ERROR_NO_MATCH_FOUND_CODE_IS_CASESENSITIVE;
					}
					request.getSession().setAttribute("message", msg);
				}

			} catch (Exception ex) {
				msg = "No match found.";
				if (searchOption == SimpleSearchUtils.BY_CODE) {
					msg = Constants.ERROR_NO_MATCH_FOUND_CODE_IS_CASESENSITIVE;
				}
				request.getSession().setAttribute("message", msg);
			}
		}

        try {
			request.getSession().setAttribute("display_matched_concepts_only", "true");
			String nextJSP = "/pages/download_value_set.jsf";
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request,response);
			return;

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void export_vsrc(HttpServletRequest request, HttpServletResponse response) {
		new VSRCExportUtils().exportValueSetDefinitionConfigToCSV(request, response);
	}

	public void dumpVector(Vector v) {
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			System.out.println(t);
		}
	}

    public String getReleasedFileContent(ResolvedValueSetIteratorHolder rvsi) {
		if (rvsi == null) return null;
		//String content = getResolvedValueSetContent(rvsi);
		String content = rvsi.getResolvedValueSetContent();
        return content;
	}

    public ResolvedValueSetIteratorHolder constructResolvedValueSetIteratorHolder(String vsd_uri) {
		ValueSetConfig vsc = ValueSetDefinitionConfig.getValueSetConfig(vsd_uri);
		return constructResolvedValueSetIteratorHolder(vsc);
	}

    public ResolvedValueSetIteratorHolder constructResolvedValueSetIteratorHolder(ValueSetConfig vsc) {
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        CodingSchemeDataUtils csdu = new CodingSchemeDataUtils(lbSvc);

		String table_content = null;
		StringBuffer table_content_buf = new StringBuffer();

		ResolvedValueSetIteratorHolder rvsi = null;

		String filename = vsc.getReportURI();
		String excelfile = ValueSetDefinitionConfig.getValueSetDownloadFilename(vsc);
		FTPDownload.download(vsc.getReportURI(), excelfile);

		Vector u = ValueSetDefinitionConfig.interpretExtractionRule(vsc.getExtractionRule());
		int col = -1;
		int sheet = -1;
		String code = null;

		if (u == null || (u.size() != 2 && u.size() != 3)) {
			System.out.println("Data not found.");
			return null;
		} else {
			sheet = Integer.parseInt((String) u.elementAt(0)) - 1;
			if (u.size() == 2) {
				code = (String) u.elementAt(1);
			} else if (u.size() == 3) {
				col = Integer.parseInt((String) u.elementAt(1)) - 1;
				code = (String) u.elementAt(2);
			}
			int startIndex = ExcelUtil.getExcelStartRow(excelfile, sheet, col, code);
			boolean cdisc = false;
			if (vsc.getExtractionRule() != null && !vsc.getExtractionRule().endsWith(":all")) {
				String header = ExcelUtil.getExcelHeader(excelfile, sheet);
				if (header != null && header.indexOf(Constants.CDISC_SUBMISSION_VALUE) != -1) {
					//KLO
					//startIndex = startIndex - 1;
					cdisc = true;
				}
			}
			if (startIndex != -1) {
				try {
					String url = "/ncitbrowser/ConceptReport.jsp?dictionary=NCI%20Thesaurus";
					String ncit_production_version = csdu.getVocabularyVersionByTag(Constants.NCIT_CS_NAME, "PRODUCTION");
					if (ncit_production_version != null) {
						url = url + "&version=" + ncit_production_version;
					}
					String ns = new ConceptDetails(lbSvc).getNamespaceByCode(Constants.NCIT_CS_NAME, ncit_production_version, code);
					url = url + "&ns=" + ns;
					rvsi = new ResolvedValueSetIteratorHolder(excelfile, sheet, startIndex, col, code, url, cdisc);
    				return rvsi;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return null;
	}

/*
    public String getReleasedFileContent(ValueSetConfig vsc) {
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		CodingSchemeDataUtils csdu = new CodingSchemeDataUtils(lbSvc);

		String table_content = null;
		StringBuffer table_content_buf = new StringBuffer();
		String filename = vsc.getReportURI();
		String excelfile = ValueSetDefinitionConfig.getValueSetDownloadFilename(vsc);
		FTPDownload.download(vsc.getReportURI(), excelfile);
		Vector u = ValueSetDefinitionConfig.interpretExtractionRule(vsc.getExtractionRule());
		int col = -1;
		int sheet = -1;
		String code = null;

		if (u == null || (u.size() != 2 && u.size() != 3)) {
			System.out.println("Data not found.");
			return "Data not found.";
		} else {
			sheet = Integer.parseInt((String) u.elementAt(0)) - 1;
			if (u.size() == 2) {
				code = (String) u.elementAt(1);
			} else if (u.size() == 3) {
				col = Integer.parseInt((String) u.elementAt(1)) - 1;
				code = (String) u.elementAt(2);
			}
			int startIndex = ExcelUtil.getHSSFStartRow(excelfile, sheet, col, code);
			boolean cdisc = false;
			if (vsc.getExtractionRule() != null && !vsc.getExtractionRule().endsWith(":all")) {
				String header = ExcelUtil.getHSSFHeader(excelfile, sheet);
				if (header != null && header.indexOf(Constants.CDISC_SUBMISSION_VALUE) != -1) {
					startIndex = startIndex - 1;
					cdisc = true;
				}
			}
			if (startIndex != -1) {
				try {
					//String url = "/ncitbrowser/ConceptReport.jsp?dictionary=NCI%20Thesaurus";
					String url = "/ncitbrowser/ConceptReport.jsp?dictionary=NCI_Thesaurus";
					String ncit_production_version = csdu.getVocabularyVersionByTag(Constants.NCIT_CS_NAME, "PRODUCTION");
					if (ncit_production_version != null) {
						url = url + "&version=" + ncit_production_version;
					}
					//LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
					String ns = new ConceptDetails(lbSvc).getNamespaceByCode(Constants.NCIT_CS_NAME, ncit_production_version, code);
					url = url + "&ns=" + ns;
					ResolvedValueSetIteratorHolder rvsi = new ResolvedValueSetIteratorHolder(excelfile, sheet, startIndex, col, code, url, cdisc);
    				//String content = getResolvedValueSetContent(rvsi);
    				String content = rvsi.getResolvedValueSetContent();
                    return content;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		String msg = Constants.NO_VALUE_SET_REPORT_AVAILABLE;
		return(msg);
	}
*/

    public String getReleasedFileContent(ValueSetConfig vsc) {
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		CodingSchemeDataUtils csdu = new CodingSchemeDataUtils(lbSvc);

		String table_content = null;
		StringBuffer table_content_buf = new StringBuffer();
		String filename = vsc.getReportURI();
		String excelfile = ValueSetDefinitionConfig.getValueSetDownloadFilename(vsc);
		FTPDownload.download(vsc.getReportURI(), excelfile);
		Vector u = ValueSetDefinitionConfig.interpretExtractionRule(vsc.getExtractionRule());
		int col = -1;
		int sheet = -1;
		String code = null;

		if (u == null || (u.size() != 2 && u.size() != 3)) {
			System.out.println("Data not found.");
			return "Data not found.";
		} else {
			sheet = Integer.parseInt((String) u.elementAt(0)) - 1;
			if (u.size() == 2) {
				code = (String) u.elementAt(1);
			} else if (u.size() == 3) {
				col = Integer.parseInt((String) u.elementAt(1)) - 1;
				code = (String) u.elementAt(2);
			}
			int startIndex = ExcelUtil.getExcelStartRow(excelfile, sheet, col, code);
			boolean cdisc = false;
			if (vsc.getExtractionRule() != null && !vsc.getExtractionRule().endsWith(":all")) {
				String header = ExcelUtil.getExcelHeader(excelfile, sheet);
				if (header != null && header.indexOf(Constants.CDISC_SUBMISSION_VALUE) != -1) {
					startIndex = startIndex - 1;
					cdisc = true;
				}
			}
			if (startIndex != -1) {
				try {
					//String url = "/ncitbrowser/ConceptReport.jsp?dictionary=NCI%20Thesaurus";
					String url = "/ncitbrowser/ConceptReport.jsp?dictionary=NCI_Thesaurus";
					String ncit_production_version = csdu.getVocabularyVersionByTag(Constants.NCIT_CS_NAME, "PRODUCTION");
					if (ncit_production_version != null) {
						url = url + "&version=" + ncit_production_version;
					}
					//LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
					String ns = new ConceptDetails(lbSvc).getNamespaceByCode(Constants.NCIT_CS_NAME, ncit_production_version, code);
					url = url + "&ns=" + ns;
					ResolvedValueSetIteratorHolder rvsi = new ResolvedValueSetIteratorHolder(excelfile, sheet, startIndex, col, code, url, cdisc);
    				//String content = getResolvedValueSetContent(rvsi);
    				String content = rvsi.getResolvedValueSetContent();
                    return content;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		String msg = Constants.NO_VALUE_SET_REPORT_AVAILABLE;
		return(msg);
	}


	public void writeExportForm(PrintWriter out, String vsd_uri) {
		out.println("                     <table border=\"0\" role='presentation'>");
		out.println("                        <tr>");
		out.println("                           <td>");
		out.println("                              <table border=\"0\" width=\"870\" role='presentation'>");
		out.println("                                 <tr>");
		out.println("                                    <td align=\"right\">");

        out.println("<a href=\"/ncitbrowser/ajax?action=export_to_excel&from_download=true&vsd_uri=" + vsd_uri + "\"><font face=\"verdana\" size=\"1\">Export Excel</font></a>");
		out.println("<a title=\"Download Plugin Microsoft Excel Viewer\" href=\"https://products.office.com/en-US/excel?legRedir=true&CorrelationId=1229dc2e-5ff3-4e3b-adc8-2b6f59e21be4\" target=\"_blank\"><img");
		out.println("     src=\"/ncitbrowser/images/link_xls.gif\" width=\"16\"");
		out.println("     height=\"16\" border=\"0\"");
		out.println("alt=\"Download Plugin Microsoft Excel Viewer\" /></a>");
        out.println("<a href=\"/ncitbrowser/ajax?action=export_to_csv&from_download=true&vsd_uri=" + vsd_uri + "\"><font face=\"verdana\" size=\"1\">Export CSV</font></a>");

		out.println("                                    </td>");
		out.println("                                 </tr>");
		out.println("                              </table>");
		out.println("                           </td>");
		out.println("                        </tr>");
		out.println("                     </table>");
	}

    public void exportToCSVAction(HttpServletRequest request, HttpServletResponse response) {
        StringBuffer sb = new StringBuffer();
		String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
		ResolvedValueSetIteratorHolder rvsi = (ResolvedValueSetIteratorHolder) request.getSession().getAttribute("rvsi");
		if (rvsi != null) {
			Vector w = rvsi.extractRawDataFromTableContent();
			if (w != null) {
				try {
					w = rvsi.tableContent2CSV(w);
					for (int k=0; k<w.size(); k++) {
						String t = (String) w.elementAt(k);
						sb.append(t);
						sb.append("\n");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
			    }
			}
		}
		String vsd_name = DataUtils.valueSetDefinitionURI2Name(vsd_uri);
		vsd_name = vsd_name.replaceAll(" ", "_");
		vsd_name = vsd_name + ".txt";
		//System.out.println("vsd_name: " + vsd_name);

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ vsd_name);

		response.setContentLength(sb.length());

		try {
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(sb.toString().getBytes("UTF8"), 0, sb.length());
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			//sb.append("WARNING: Export to CVS action failed.");
		}
		FacesContext.getCurrentInstance().responseComplete();
	}



/*
    public void exportToExcelAction(HttpServletRequest request, HttpServletResponse response) {
		try {
			String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
    		response.setContentType("application/vnd.ms-excel");
    		//response.setContentType("application/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

			String vsd_name = DataUtils.valueSetDefinitionURI2Name(vsd_uri);
			vsd_name = vsd_name.replaceAll(" ", "_");
			vsd_name = vsd_name + ".xls";
			//System.out.println("vsd_name: " + vsd_name);

		    response.setHeader("Content-Disposition", "attachment; filename="
					+ vsd_name);

            StringBuffer sb = new StringBuffer();

			ResolvedValueSetIteratorHolder rvsi = (ResolvedValueSetIteratorHolder) request.getSession().getAttribute("rvsi");
			List list = null;
			if (rvsi != null) {
				list = rvsi.getResolvedValueSetList();
			    sb.append(rvsi.getOpenTableTag("rvs_table"));
				String first_line = (String) list.get(0);
			    first_line = first_line.replaceAll("td", "th");
			    sb.append(first_line);

				for (int k=1; k<list.size(); k++) {
					String line = (String) list.get(k);
					line = ResolvedValueSetIteratorHolder.removeHyperlinks(line);
					sb.append(line);
				}
				sb.append(rvsi.getCloseTableTag());
			}

			String outputstr = sb.toString();
			response.setContentLength(outputstr.length());

			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(outputstr.getBytes("UTF8"), 0, outputstr.length());
			ouputStream.flush();
			ouputStream.close();

		} catch(IOException e) {
			e.printStackTrace();
		}
		FacesContext.getCurrentInstance().responseComplete();
	}
*/

   public void exportToExcelAction(HttpServletRequest request, HttpServletResponse response) {
		try {
			String vsd_uri = HTTPUtils.cleanXSS((String) request.getParameter("vsd_uri"));
    		response.setContentType("application/vnd.ms-excel");
			String vsd_name = DataUtils.valueSetDefinitionURI2Name(vsd_uri);
			vsd_name = vsd_name.replaceAll(" ", "_");
			String sheet_name = vsd_name;
			vsd_name = vsd_name + ".xls";
		    response.setHeader("Content-Disposition", "attachment; filename="
					+ vsd_name);
			ResolvedValueSetIteratorHolder rvsi = (ResolvedValueSetIteratorHolder) request.getSession().getAttribute("rvsi");
			HSSFWorkbook hssfWorkbook = rvsi.createHSSFWorkbook(sheet_name);
			ServletOutputStream ouputStream = response.getOutputStream();
            hssfWorkbook.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();

		} catch(IOException e) {
			e.printStackTrace();
		}
		FacesContext.getCurrentInstance().responseComplete();
	}

    public void exportMappingAction(HttpServletRequest request, HttpServletResponse response) {
		try {
			String uri = HTTPUtils.cleanXSS((String) request.getParameter("uri"));
			if (uri == null) return;
			//System.out.println("uri: " + uri);
			String format = DataUtils.getMappingFileFormat(uri);
			//System.out.println("format: " + format);
			ServletOutputStream ouputStream = null;
            if (format.compareTo("txt") == 0) {
				response.setContentType("text/html");
				int n = uri.lastIndexOf("/");
				String mapping_name = uri.substring(n+1, uri.length());
				//System.out.println("mapping_name: " + mapping_name);
				response.setHeader("Content-Disposition", "attachment; filename="
						+ mapping_name);

				String outputstr = FTPDownload.tear_page(uri);
				if (outputstr != null) {
					response.setContentLength(outputstr.length());
					ouputStream = response.getOutputStream();
					try {
						ouputStream.write(outputstr.getBytes("UTF8"), 0, outputstr.length());
						ouputStream.flush();
					} catch (Exception ex) {
						ex.printStackTrace();
					} finally {
						try {
							ouputStream.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			} else if (format.compareTo("xls") == 0) {
                String outputfile = DataUtils.getMappingFileName(uri);
                if (outputfile == null) return;
                File file = new File(outputfile);
                if (file != null) {
					FTPDownload.downloadExcel(uri, outputfile);
					response.setContentType("application/xls");

					int n = uri.lastIndexOf("/");
					String mapping_name = uri.substring(n+1, uri.length());
					//System.out.println("mapping_name: " + mapping_name);
					response.setHeader("Content-Disposition", "attachment; filename="
							+ mapping_name);

					response.setContentLength((int) file.length());
					FileInputStream fileInputStream = null;
					OutputStream responseOutputStream = null;
					try {
						fileInputStream = new FileInputStream(file);
						responseOutputStream = response.getOutputStream();
						int bytes;
						while ((bytes = fileInputStream.read()) != -1) {
							responseOutputStream.write(bytes);
						}
						//fileInputStream.close();
						//responseOutputStream.close();

					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} finally {
						try {
							fileInputStream.close();
							responseOutputStream.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		FacesContext.getCurrentInstance().responseComplete();
	}


    public void export_mapping_data(HttpServletRequest request, HttpServletResponse response) {
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        String mapping_schema = HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
        String mapping_version = HTTPUtils.cleanXSS((String) request.getParameter("version"));
		String sourceCode = null;
		String sourceName = null;
		String sourceCodingScheme = null;
		String sourceCodingSchemeVesion = null;
		String sourceCodeNamespace = null;
		String rel = null;
		int score = 0;
		String targetCode = null;
		String targetName = null;
		String targetCodingScheme = null;
		String targetCodingSchemeVesion = null;
		String targetCodeNamespace = null;
		String description = null;
		String associationName = null;

        java.util.List<MappingData> list = new MappingUtils(lbSvc).getMappingData(mapping_schema, mapping_version);
		ServletOutputStream ouputStream = null;

		String filename = mapping_schema + "_" + mapping_version;
		filename = filename.replaceAll(" ", "_");
		filename = filename + ".csv";

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ filename);

        StringBuffer sb = new StringBuffer();
        try {
			ouputStream = response.getOutputStream();

			sb.append("Source Code,");
			sb.append("Source Name,");
			sb.append("Source Coding Scheme,");
			sb.append("Source Coding Scheme Version,");
			sb.append("Source Coding Scheme Namespace,");

			sb.append("Association Name,");
			sb.append("REL,");
			sb.append("Map Rank,");

			sb.append("Target Code,");
			sb.append("Target Name,");
			sb.append("Target Coding Scheme,");
			sb.append("Target Coding Scheme Version,");
			sb.append("Target Coding Scheme Namespace");
			sb.append("\r\n");
			ouputStream.write(sb.toString().getBytes("UTF-8"), 0, sb.length());

			for (int i=0; i<list.size(); i++) {
				sb = new StringBuffer();
				MappingData mappingData = (MappingData) list.get(i);
				sb = new StringBuffer();
				sb.append("\"" + mappingData.getSourceCode() + "\",");
				sb.append("\"" + escapeCommaCharacters(mappingData.getSourceName()) + "\",");
				sb.append("\"" + mappingData.getSourceCodingScheme() + "\",");
				sb.append("\"" + mappingData.getSourceCodingSchemeVersion() + "\",");
				sb.append("\"" + mappingData.getSourceCodeNamespace() + "\",");

				sb.append("\"" + mappingData.getAssociationName() + "\",");
				sb.append("\"" + mappingData.getRel() + "\",");
				sb.append("\"" + mappingData.getScore() + "\",");

				sb.append("\"" + mappingData.getTargetCode() + "\",");
				sb.append("\"" + escapeCommaCharacters(mappingData.getTargetName()) + "\",");
				sb.append("\"" + mappingData.getTargetCodingScheme() + "\",");
				sb.append("\"" + mappingData.getTargetCodingSchemeVersion() + "\",");
				sb.append("\"" + mappingData.getTargetCodeNamespace() + "\"");
				sb.append("\r\n");
				ouputStream.write(sb.toString().getBytes("UTF-8"), 0, sb.length());
				ouputStream.flush();
			}
		} catch (Exception ex)	{
			sb.append("WARNING: Export to CVS action failed.");
			ex.printStackTrace();
		}

		try {
			ouputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		FacesContext.getCurrentInstance().responseComplete();
		return;
	}


    public void exportMapsToMappingAction(HttpServletRequest request, HttpServletResponse response) {
		Vector maps_to_vec = DataUtils.get_maps_to_vec();
		if (maps_to_vec == null) {
			System.out.println("exportMapsToMappingAction ...maps_to_vec == null??? ");
			return;
		}
		String target = HTTPUtils.cleanXSS((String) request.getParameter("target"));
		String maps_to_string = DataUtils.getTargetTerminologyResponseString(target);
		int len = maps_to_string.length();
		String filename = MapsToReportProcessor.getMapsToMappingyName(target) + ".csv";
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ filename);
		response.setContentLength(len);
		try {
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(maps_to_string.getBytes("UTF8"), 0, len);
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		FacesContext.getCurrentInstance().responseComplete();
	}





    public String selectAllInCart(HttpServletRequest request, HttpServletResponse response) {
		try {
			CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");
			if (cartActionBean.getCount() < 1) {
				cartActionBean._messageflag = true;
				cartActionBean._message = CartActionBean.NO_CONCEPTS;
			} else {
				HashMap hmap = new HashMap();
				for (Iterator<gov.nih.nci.evs.browser.bean.CartActionBean.Concept> i = cartActionBean.getConcepts().iterator(); i.hasNext();) {
					gov.nih.nci.evs.browser.bean.CartActionBean.Concept item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept)i.next();
					item.setSelected(true);
					hmap.put(item.getCode(), item);
				}
				cartActionBean.setCart(hmap);
				request.getSession().setAttribute("cartActionBean", cartActionBean);

				String nextJSP = "/pages/cart.jsf";
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
				dispatcher.forward(request,response);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        return "refresh_cart";
    }

    public String unselectAllInCart(HttpServletRequest request, HttpServletResponse response) {
		try {
			CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");
			if (cartActionBean.getCount() < 1) {
				cartActionBean._messageflag = true;
				cartActionBean._message = CartActionBean.NO_CONCEPTS;
			} else {
				HashMap hmap = new HashMap();
				for (Iterator<gov.nih.nci.evs.browser.bean.CartActionBean.Concept> i = cartActionBean.getConcepts().iterator(); i.hasNext();) {
					gov.nih.nci.evs.browser.bean.CartActionBean.Concept item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept)i.next();
					item.setSelected(false);
					hmap.put(item.getCode(), item);
				}
				cartActionBean.setCart(hmap);
				request.getSession().setAttribute("cartActionBean", cartActionBean);

				String nextJSP = "/pages/cart.jsf";
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
				dispatcher.forward(request,response);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        return "refresh_cart";
	}


    public String exportCart2XML(HttpServletRequest request, HttpServletResponse response) {
		try {
			CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");
			if (cartActionBean == null) {
				System.out.println("WARNING: exportCart2XML detects a null cartActionBean???");
			}
			cartActionBean.exportCartXML(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "refresh_cart";
	}

    public String exportCart2CSV(HttpServletRequest request, HttpServletResponse response) {
		try {
			CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");
			cartActionBean.exportCartCSV(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "refresh_cart";
	}

    public String addToCart(HttpServletRequest request, HttpServletResponse response) {
		CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");
		String retval = null;
		try {
			retval = cartActionBean.addToCart(request, response);
			String codingScheme = HTTPUtils.cleanXSS((String) request.getParameter("dictionary"));
			String version = HTTPUtils.cleanXSS((String) request.getParameter("version"));
			String ns = HTTPUtils.cleanXSS((String) request.getParameter("ns"));
			String code = HTTPUtils.cleanXSS((String) request.getParameter("code"));

			String nextJSP = "/pages/concept_details.jsf?"
			    + "scheme=" + codingScheme
			    + "&version=" + version
			    + "&ns=" + ns
			    + "&code=" + code;

			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request,response);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return retval;
	}

    public String removeFromCart(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("message");
		try {
			Set<String> paramNames = request.getParameterMap().keySet();
			CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");
			gov.nih.nci.evs.browser.bean.CartActionBean.Concept item = null;
			HashMap cart_hmap = new HashMap();
			Collection<gov.nih.nci.evs.browser.bean.CartActionBean.Concept> items = cartActionBean.getConcepts();
			Iterator it = items.iterator();
			while (it.hasNext()) {
				item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it.next();
				cart_hmap.put(item.getCode(), item);
			}
			Vector removed_codes = new Vector();
			for (String name : paramNames) {
				String value = request.getParameter(name);
				Iterator it2 = items.iterator();
				while (it2.hasNext()) {
					item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it2.next();
					if (item.getCode().compareTo(value) == 0) {
						removed_codes.add(value);
					}
				}
			}
			String ans = (String) request.getParameter("ans");
			if (ans == null) {
				/*
				String message = "Are you sure you want to permanently remove the following selected concepts from the cart? " +
				"&nbsp;<input type=\"radio\" name=\"ans\" checked=\"checked\" value=\"yes\" >Yes</input>&nbsp;<input type=\"radio\" value=\"no\" name=\"ans\">No</input>" +
				". &nbsp;Click <a href=\"javascript:submitform()\">here</a> to confirm.";
				*/
				String message = "Are you sure you want to permanently remove the following selected concepts from the cart? " +
				"&nbsp;<input type=\"radio\" name=\"ans\" checked=\"checked\" value=\"yes\" >Yes</input>&nbsp;<input type=\"radio\" value=\"no\" name=\"ans\">No</input>";

				request.getSession().setAttribute("message", message);
				request.getSession().setAttribute("confirmation", "true");
				String nextJSP = "/pages/cart.jsf";
				try {
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
					dispatcher.forward(request,response);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return null;
			} else {
			    if (ans.compareToIgnoreCase("yes") == 0) {
					for (int i=0; i<removed_codes.size(); i++) {
						String code = (String) removed_codes.elementAt(i);
						cart_hmap.remove(code);
					}
					cartActionBean.setCart(cart_hmap);
				}
			}
			request.getSession().setAttribute("cartActionBean", cartActionBean);
			//items = cartActionBean.getConcepts();
			//int count = items.size();
		    String nextJSP = "/pages/cart.jsf";
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			dispatcher.forward(request,response);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "refresh_cart";
	}

    public String processCartActions(HttpServletRequest request, HttpServletResponse response) {
		Set<String> paramNames = request.getParameterMap().keySet();
		CartActionBean cartActionBean = (CartActionBean) request.getSession().getAttribute("cartActionBean");
		gov.nih.nci.evs.browser.bean.CartActionBean.Concept item = null;
		Collection<gov.nih.nci.evs.browser.bean.CartActionBean.Concept> items = cartActionBean.getConcepts();

		Iterator it0 = items.iterator();
		while (it0.hasNext()) {
			item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it0.next();
			item.setSelected(false);
		}

		for (String name : paramNames) {
			String value = request.getParameter(name);
			Iterator it2 = items.iterator();
			while (it2.hasNext()) {
				item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it2.next();
				if (item.getCode().compareTo(value) == 0) {
					item.setSelected(true);
				}
			}
		}

		HashMap cart_hmap = new HashMap();
		Iterator it = items.iterator();
		while (it.hasNext()) {
			item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it.next();
			cart_hmap.put(item.getCode(), item);
		}
        cartActionBean.setCart(cart_hmap);
        request.getSession().setAttribute("cartActionBean", cartActionBean);

		String ans = (String) request.getSession().getAttribute("ans");
		request.getSession().removeAttribute("message");
		int count = items.size();
		String btn = (String) request.getParameter("btn");
		if (count == 0 && btn.compareTo("exit_cart") != 0) {
			String message = "INFO: The cart is empty.";
			request.getSession().setAttribute("message", message);
			String nextJSP = "/pages/cart.jsf";
			if (cartActionBean.getCount() == 0) {
				nextJSP = "/pages/home.jsf";
			}
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			try {
				dispatcher.forward(request,response);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		Iterator it3 = items.iterator();
		int selected_count = 0;
		while (it3.hasNext()) {
			item = (gov.nih.nci.evs.browser.bean.CartActionBean.Concept) it3.next();
			if (item.getSelected()) {
				selected_count++;
			}
		}
		if ((btn.compareTo("removefromcart") == 0
		    || btn.compareTo("exportxml") == 0
		    || btn.compareTo("exportcsv") == 0)
		    && selected_count == 0) {
			String message = "WARNING: No concept is selected.";
			request.getSession().setAttribute("message", message);

			String nextJSP = "/pages/cart.jsf";
			if (cartActionBean.getCount() == 0) {
				nextJSP = "/pages/home.jsf";
			}

			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			try {
				dispatcher.forward(request,response);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
		if (btn.compareTo("exit_cart") == 0) {
			String scheme = (String) request.getParameter("scheme");
			String version = (String) request.getParameter("version");
			String nextJSP = "/pages/multiple_search.jsf?nav_type=terminologies";

			if (count > 0) {
				if (scheme == null || scheme.compareTo("null") == 0 || scheme.compareTo("NCI Thesaurus") == 0) {
					 nextJSP = "/pages/home.jsf";
				} else if (scheme != null && scheme.compareTo("null") != 0 && version != null && version.compareTo("null") != 0) {
					 nextJSP = "/pages/vocabulary.jsf?dictionary=" + scheme + "&version=" + version;
				}
		    }

			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
			try {
				dispatcher.forward(request,response);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
        switch (btn) {
            case "selectall":  selectAllInCart(request, response);
                     break;
            case "unselectall":  unselectAllInCart(request, response);
                     break;
            case "removefromcart":  removeFromCart(request, response);
                     break;
            case "exportxml":  exportCart2XML(request, response);
                     break;
            case "exportcsv":  exportCart2CSV(request, response);
                     break;
		}
        return "refresh_cart";
	}
}

