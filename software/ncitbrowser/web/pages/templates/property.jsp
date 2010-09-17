<%@ page import="gov.nih.nci.evs.browser.utils.FormatUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.DataUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.SortUtils" %>
<%@ page import="gov.nih.nci.evs.browser.utils.SearchUtils" %>
<%@ page import="org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator" %>
<%@ page import="org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference" %>

<%
  HashMap def_map = NCItBrowserProperties.getDefSourceMappingHashMap();
  
  String ncim_cui_propName = "NCI_META_CUI";
  String umls_cui_propName = "UMLS_CUI";
  String ncim_cui_propName_label = null;
  String ncim_cui_prop_url = null;
  String ncim_cui_prop_linktext = null;
  Vector ncim_cui_code_vec = new Vector();
  
  String prop_version = null;

  String prop_dictionary = (String) request.getSession().getAttribute("dictionary");
  prop_version = (String) request.getAttribute("version");
    
  System.out.println("property.jsp prop_version: " + prop_version);    

  List displayItemList = null;
  Entity curr_concept = null;
  Boolean bool_obj = null;
  //String isActive = null;

  try {
    displayItemList = NCItBrowserProperties.getInstance().getDisplayItemList();
    curr_concept = (Entity) request.getSession().getAttribute("concept");
    request.getSession().setAttribute("code", curr_concept.getEntityCode());
    
    bool_obj = curr_concept.isIsActive();

  } catch (Exception ex) {
    // Do nothing
    System.out.println("(*) property.jsp curr_concept.isIsActive() throws exception.");
  }

  if ((type.compareTo("properties") == 0 || type.compareTo("all") == 0) &&
    displayItemList != null && curr_concept != null) {

    Vector propertytypes = new Vector();
    propertytypes.add("PRESENTATION");
    propertytypes.add("DEFINITION");
    propertytypes.add("GENERIC");
    //propertytypes.add("INSTRUCTION");
    propertytypes.add("COMMENT");

    Vector additionalproperties = new Vector();
    additionalproperties.add("CONCEPT_NAME");
    additionalproperties.add("primitive");

    String concept_status = null;
    try {
        concept_status = curr_concept.getStatus();
    } catch (Exception ex) {
        
    }

    if (concept_status != null) {
       concept_status = concept_status.replaceAll("_", " ");
       if (concept_status.compareToIgnoreCase("active") == 0 || concept_status.compareToIgnoreCase("reviewed") == 0) concept_status = null;
    }

    HashSet hset = new HashSet();
    HashMap hmap = new HashMap();
    Vector propertyvalues = null;

    for (int i=0; i<propertytypes.size(); i++) {
      String propertytype = (String) propertytypes.elementAt(i);
      Vector propertynames = DataUtils.getPropertyNamesByType(
        curr_concept, propertytype);

      for (int j=0; j<propertynames.size(); j++) {
        String propertyname = (String) propertynames.elementAt(j);

        if (!hset.contains(propertyname)) {
          hset.add(propertyname);
          propertyvalues = DataUtils.getPropertyValues(
            curr_concept, propertytype, propertyname);

          if (propertyvalues != null)
            hmap.put(propertyname, propertyvalues);
        }
      }
    }

    propertyvalues = new Vector();
    String concept_id = curr_concept.getEntityCode();
    propertyvalues.add(concept_id);
    hmap.put("Code", propertyvalues);
    Vector displayed_properties = new Vector();
    Vector properties_to_display = new Vector();
    Vector properties_to_display_label = new Vector();
    Vector properties_to_display_url = new Vector();
    Vector properties_to_display_linktext = new Vector();
    
    Vector presentation_vec = DataUtils.getPresentationProperties(curr_concept);
    presentation_vec = SortUtils.quickSort(presentation_vec);
    
    //Vector preferred_name_vec = new Vector();
    for (int i=0; i<presentation_vec.size(); i++) {
        //name$value$isPreferred
        String t = (String) presentation_vec.elementAt(i);
        Vector w = DataUtils.parseData(t, "$");
        String presentaion_name = (String) w.elementAt(0);
        String presentaion_value = (String) w.elementAt(1);
        String isPreferred = (String) w.elementAt(2);
        if (isPreferred.compareTo("true") == 0) {
		properties_to_display.add(presentaion_name);
		properties_to_display_label.add(presentaion_name.replaceAll("_", " "));
		properties_to_display_url.add(null);
		properties_to_display_linktext.add(null);
        }
    }

    for (int i=0; i<displayItemList.size(); i++) {
      DisplayItem displayItem = (DisplayItem) displayItemList.get(i);
      if (!displayItem.getIsExternalCode() && !properties_to_display.contains( displayItem.getPropertyName() )) {
        properties_to_display.add(displayItem.getPropertyName());
        properties_to_display_label.add(displayItem.getItemLabel());
        properties_to_display_url.add(displayItem.getUrl());
        properties_to_display_linktext.add(displayItem.getHyperlinkText());
      }
    }

    int num_definitions = 0;
    int num_alt_definitions = 0;
    Vector external_source_codes = new Vector();
    Vector external_source_codes_label = new Vector();
    Vector external_source_codes_url = new Vector();
    Vector external_source_codes_linktext = new Vector();
    for (int i=0; i<displayItemList.size(); i++) {
    DisplayItem displayItem = (DisplayItem) displayItemList.get(i);

    if (displayItem.getIsExternalCode()) {
      external_source_codes.add(displayItem.getPropertyName());
      external_source_codes_label.add(displayItem.getItemLabel());
      external_source_codes_url.add(displayItem.getUrl());
      external_source_codes_linktext.add(displayItem.getHyperlinkText());
    }
  }
%>
<p class="textsubtitle-blue">Terms & Properties</p>
<%
if (bool_obj != null && !bool_obj.equals(Boolean.TRUE) ||
  (concept_status != null &&
    concept_status.compareToIgnoreCase("Retired Concept") == 0)) // non-active
{
%>
    <p class="textbody"><b>Concept Status:</b>&nbsp;<i class="textbodyred">Retired Concept</i>
<%

    
    Vector descendantCodes = HistoryUtils.getDescendantCodes(dictionary, null, null, curr_concept.getEntityCode());
    if (descendantCodes != null) {
            if (descendantCodes != null && descendantCodes.size() > 0) {
		    String link = "&nbsp;(See:&nbsp;"; 

	%>            
		    <%=link%>
	<%            
		    for (int i=0; i<descendantCodes.size(); i++) {
			String t = (String) descendantCodes.elementAt(i);
			Vector w = DataUtils.parseData(t);
			String descendantName = (String) w.elementAt(0);
			String descendantCode = (String) w.elementAt(1);
	%>

		      <a href="<%= request.getContextPath() %>/ConceptReport.jsp?dictionary=<%=prop_dictionary%>&code=<%=descendantCode%>">
			<%=descendantName%>
		      </a>
	<%              
		    }

		    link = ")"; 
	%>            
		    <%=link%></p>	
	<%    
            }
    }
    
%>    
    
<%
}
else if (concept_status != null && concept_status.compareToIgnoreCase("Retired Concept") != 0) {
%>
    <p class="textbody"><b>Concept Status:</b>&nbsp;<i class="textbody"><%=concept_status%></i></p>
<%
}
%>
<%


//[#26722] Support cross-linking of individual source vocabularies with NCI Metathesaurus.

  for (int i=0; i<properties_to_display.size(); i++) {
    String propName = (String) properties_to_display.elementAt(i);
   
    String propName_label = (String) properties_to_display_label.elementAt(i);
 
    if (propName_label.compareTo("NCI Thesaurus Code") == 0  && propName.compareTo("NCI_THESAURUS_CODE") != 0) {
        String formalName = DataUtils.getFormalName(dictionary);
        if (formalName == null)
        	formalName = dictionary;
	propName_label = formalName + " Code";
    }
    
    
    String propName_label2 = propName_label;
    String url = (String) properties_to_display_url.elementAt(i);
    
    String linktext = (String) properties_to_display_linktext.elementAt(i);

    if (propName.compareTo(ncim_cui_propName) == 0 || propName.compareTo(umls_cui_propName) == 0) {
        ncim_cui_propName_label = propName_label;
        ncim_cui_prop_url = url;
        ncim_cui_prop_linktext = linktext;
        
        Vector ncim_cui_code_vec_temp = DataUtils.getPropertyValues(
            curr_concept, "GENERIC", propName);
        if (ncim_cui_code_vec_temp != null) {
           for (int lcv=0; lcv<ncim_cui_code_vec_temp.size(); lcv++) {
               String t = (String) ncim_cui_code_vec_temp.elementAt(lcv);
               ncim_cui_code_vec.add(t);
           }
        } 
            
    }
    
    String qualifier = "";
    if (url != null) {
    
      displayed_properties.add(propName);
      Vector value_vec = (Vector) hmap.get(propName);
      int row3=0;
      
      if (value_vec != null && value_vec.size() > 1) {
          %>
          <b><%=propName_label%></b>:
          <table class="datatable">
          <%
      }
      
      if (value_vec != null && value_vec.size() > 0) {
          
        //[#28262] Only one "NCI Meta CUI" displays
        for (int lcv=0; lcv<value_vec.size(); lcv++) {
         
		String value = (String) value_vec.elementAt(lcv);
		String value_wo_qualifier = value;

		int n = value.indexOf("|");
		if (n != -1 && (propName_label.indexOf("Definition") != -1 || propName_label.indexOf("DEFINITION") != -1 
		         || propName_label.indexOf("definition") != -1)) {
			  value_wo_qualifier = value.substring(0, n);
			  qualifier = value.substring(n+1, value.length());

			  if (def_map != null && def_map.containsKey(qualifier)) {
			      String def_source_display_value = (String) def_map.get(qualifier);
			      value = value_wo_qualifier + " (" + qualifier + ")";
			      propName_label = def_source_display_value + " " + propName_label2;
			  } else {

				  if (qualifier.indexOf("PDQ") != -1) {
					value = FormatUtils.reformatPDQDefinition(value);
				  } else if (qualifier.compareTo("NCI") != 0) {
				      value = value_wo_qualifier;
				      propName_label = qualifier + " " + propName_label2;
				  } else
				      value = value_wo_qualifier;
			          }
			  }

			String url_str = url + value;
                        if (value_vec.size() == 1) {
			%> 
			  <p>
			  <b><%=propName_label%>:&nbsp;</b><%=value%>&nbsp;
			  <a href="javascript:redirect_site('<%= url_str %>')">(<%=linktext%>)</a>
			  </p>
			<%  
			  } else {
				  if ((row3++) % 2 == 0) {
				    %>
				      <tr class="dataRowDark">
				    <%
				  } else {
				    %>
				      <tr class="dataRowLight">
				    <%
				  }
			    %>
			          <i>
				  &nbsp;<%=value%>&nbsp;
				  <a href="javascript:redirect_site('<%= url_str %>')">(<%=linktext%>)</a>
				  </i>
			    <%
			  }
		}
      }
      
      
      if (value_vec != null && value_vec.size() > 1) {
          %>
          </table>
          <%
      }      
      
    } else if (propName_label.indexOf("Synonyms") == -1) {
      displayed_properties.add(propName);
      Vector value_vec = (Vector) hmap.get(propName);

      if (value_vec != null && value_vec.size() > 0) {
        int k = 0;  
        for (int j=0; j<value_vec.size(); j++) {
          String value = (String) value_vec.elementAt(j);
          
          if (propName.compareTo("NCI_META_CUI") == 0) {
              ncim_cui_code_vec.add(value);
          }
          
	  if(propName_label.compareTo("Definition") == 0) {
	      value = FormatUtils.reformatPDQDefinition(value);
	  }
          
          String value_wo_qualifier = value;
          int n = value.indexOf("|");

          if (n != -1 && (propName_label.indexOf("Definition") != -1 || propName_label.indexOf("DEFINITION") != -1 || propName_label.indexOf("definition") != -1)) {

              value_wo_qualifier = value.substring(0, n);
              qualifier = value.substring(n+1, value.length());
              
              if (def_map != null && def_map.containsKey(qualifier)) {
	          String def_source_display_value = (String) def_map.get(qualifier);
	          value = value_wo_qualifier + " (" + qualifier + ")";
                  propName_label = def_source_display_value + " " + propName_label2;
                  
                  
              } else {
              
		    if (qualifier.indexOf("PDQ") != -1) {
			value = FormatUtils.reformatPDQDefinition(value);
		    } else if (qualifier.compareTo("NCI") != 0) {
		      value = value_wo_qualifier;
		      propName_label = qualifier + " " + propName_label2;
		      
		    } else {
		      value = value_wo_qualifier;
		    }
              }
          }
          
          
          if (k == 0) {
%>
            <p><b><%=propName_label%>:&nbsp;</b><%=value%></p>
<%
          } else {
%>
            <p><%=value%></p>
<%
          }

      }
    }
  }
}
%>

<%
    String vocab = (String) request.getSession().getAttribute("dictionary");
    String NCIm_sab = DataUtils.getNCImSAB(vocab);
    
    if (NCIm_sab != null) {
	ResolvedConceptReferencesIterator iterator = new SearchUtils().findConceptWithSourceCodeMatching("NCI Metathesaurus", null,
	    NCIm_sab, curr_concept.getEntityCode(), 100, true);
	if (iterator != null) {
	    try {
	        int nummatches = iterator.numberRemaining();

		while(iterator.hasNext()) {
			ResolvedConceptReference[] refs = iterator.next(100).getResolvedConceptReference();
			if (refs != null) {
				for (int k=0; k<refs.length; k++) {
				    ResolvedConceptReference ref = refs[k];
				    String ref_code = ref.getCode();
				    if (!ncim_cui_code_vec.contains(ref_code)) {

					String _ncim_cui_prop_url = ncim_cui_prop_url + ref_code;
					%>
			  <p>
			  <b><%=ncim_cui_propName_label%>:&nbsp;</b><%=ref_code%>&nbsp;
			  <a href="javascript:redirect_site('<%= _ncim_cui_prop_url %>')">(<%=ncim_cui_prop_linktext%>)</a>
			  </p>
			                <%
			  
				    } 
				}
			}
		}
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	}
    }
%>


<p>
<b>Synonyms &amp; Abbreviations:</b>
<a href="<%=request.getContextPath() %>/pages/concept_details.jsf?dictionary=<%=scheme%>&code=<%=id%>&type=synonym">(see Synonym Details)</a>

<table class="datatable">
<%
    HashSet hset2 = new HashSet();
    Vector synonym_values = new Vector();
    for (int i=0; i<presentation_vec.size(); i++) {
        String t = (String) presentation_vec.elementAt(i);
        Vector w = DataUtils.parseData(t, "$");
        String presentaion_name = (String) w.elementAt(0);
        String presentaion_value = (String) w.elementAt(1);
        String isPreferred = (String) w.elementAt(2);

        displayed_properties.add(presentaion_name);
        if (!hset2.contains(presentaion_value)) {
	    synonym_values.add(presentaion_value);
	    hset2.add(presentaion_value);
        }

        synonym_values = SortUtils.quickSort(synonym_values);
    }

    int row=0;
    for (int j=0; j<synonym_values.size(); j++) {
        String value = (String) synonym_values.elementAt(j);
	if ((row++) % 2 == 0) {
%>
	      <tr class="dataRowDark">
<%
	} else {
%>
	      <tr class="dataRowLight">
<%
	}
%>
		   <td><%=value%></td>
	      </tr>
        <%
    }

%>
</table>
</p>
<p>
  <b>External Source Codes:&nbsp;</b>
  <table class="datatable">
    <%
      int n = 0;
      boolean display_UMLS_CUI = true;
      String dict_name = (String) request.getSession().getAttribute("dictionary");
      String vocab_format = DataUtils.getMetadataValue(dict_name, "format");
      if (vocab_format != null && vocab_format.compareTo("RRF") == 0) {
         display_UMLS_CUI= false;
      }

      for (int i=0; i<external_source_codes.size(); i++) {
        String propName = (String) external_source_codes.elementAt(i);
        String propName_label = (String) external_source_codes_label.elementAt(i);
        String prop_url = (String) external_source_codes_url.elementAt(i);
        String prop_linktext = (String) external_source_codes_linktext.elementAt(i);

        displayed_properties.add(propName);
        
        if (propName.compareTo("UMLS_CUI") != 0 || display_UMLS_CUI) {
        
        Vector value_vec = (Vector) hmap.get(propName);
        if (value_vec != null && value_vec.size() > 0) {
          for (int j=0; j<value_vec.size(); j++) {
            String value = (String) value_vec.elementAt(j);
             
            if (n % 2 == 0) {
              %>
                <tr class="dataRowDark">
              <%
            } else {
              %>
                <tr class="dataRowLight">
              <%
            }
            n++;
            %>
              <td><%=propName_label%></td>
              <td>
                <%=value%>
                <%
                  if (prop_url != null && prop_url.compareTo("null") != 0) {
                    String url_str = prop_url + value;
                    %>
                      <a href="javascript:redirect_site('<%= url_str %>')">(<%= prop_linktext %>)</a>
                    <%
                  }
                %>
              </td>
            </tr>
          <%
          }
          }
       }
    }
    %>
    </table>
</p>
<p>

    <%
      boolean hasOtherProperties = false;
      Vector prop_name_value_vec0 = new Vector();
      Set keyset0 = hmap.keySet();
      Iterator iterator0 = keyset0.iterator();
      while (iterator0.hasNext()) {
         String prop_name = (String) iterator0.next();
         Vector value_vec = (Vector) hmap.get(prop_name);
         for (int k=0; k<value_vec.size(); k++) {
             String value = (String) value_vec.elementAt(k);
             prop_name_value_vec0.add(prop_name + "|" + value);
         }
      }
      iterator0 = keyset0.iterator();
      n = 0;

      for (int k=0; k<prop_name_value_vec0.size(); k++) {
        String prop_name_value = (String) prop_name_value_vec0.elementAt(k);
        Vector w = DataUtils.parseData( prop_name_value );
        String prop_name = (String) w.elementAt(0);
        if (!displayed_properties.contains(prop_name) && !additionalproperties.contains(prop_name)) {
           if (w.size() > 0) {
               hasOtherProperties = true;
               break;
           }
        }
      }

if (!hasOtherProperties) {
%>
<b>Other Properties</b>:&nbsp;<i>None</i>
<%
} else {
%>



  <b>Other Properties:</b>
  <table class="datatable">
    <%
      Vector prop_name_value_vec = new Vector();
      Set keyset = hmap.keySet();
      Iterator iterator = keyset.iterator();
      while (iterator.hasNext()) {
         String prop_name = (String) iterator.next();
         Vector value_vec = (Vector) hmap.get(prop_name);
         for (int k=0; k<value_vec.size(); k++) {
             String value = (String) value_vec.elementAt(k);
             prop_name_value_vec.add(prop_name + "|" + value);
         }
      }
      prop_name_value_vec = SortUtils.quickSort(prop_name_value_vec);
      iterator = keyset.iterator();
      n = 0;

      for (int k=0; k<prop_name_value_vec.size(); k++) {
        String prop_name_value = (String) prop_name_value_vec.elementAt(k);
        Vector w = DataUtils.parseData( prop_name_value );
        String prop_name = (String) w.elementAt(0);
        if (!displayed_properties.contains(prop_name) && !additionalproperties.contains(prop_name)) {
          if (w.size() == 1) {
            if (n % 2 == 0) {
              %>
                <tr class="dataRowDark">
              <%
            } else {
              %>
                <tr class="dataRowLight">
              <%
            }
            n++;
            %>
                  <td><%=prop_name%></td>
                  <td>None</td>
                </tr>
            <%
          } else {
              String value = (String) w.elementAt(1);
              /*
              String source = "";
              if (w.size() > 2) {
                  source = (String) w.elementAt(2);
              }
              */
              if (n % 2 == 0) {
                %>
                  <tr class="dataRowDark">
                <%
              } else {
                %>
                  <tr class="dataRowLight">
                <%
              }
              n++;
              %>
                  <td><%=prop_name%></td>
                  <td><%=value%></td>
                </tr>
              <%
            }
        }
      }
    %>
  </table>
 
 <%
 }
 %>
  
</p>
<p>
    <%
      String concept_name = curr_concept.getEntityDescription().getContent();
      concept_name = concept_name.replaceAll(" ", "_");

      String concept_name_label = "Concept Name:";
      String dict = (String) request.getSession().getAttribute("dictionary");
      
      String primitive = null;
      String primitive_prop_name = "primitive";
      String primitive_label = "Defined Fully by Roles:";
      
      dict = DataUtils.getFormalName(dict);
      
      //Vector primitive_value_vec = (Vector) hmap.get(primitive_prop_name);
      String vocabulary_format = DataUtils.getMetadataValue(dict, "format");
      if (vocabulary_format != null && vocabulary_format.indexOf("OWL") != -1) {
	      Boolean isDefined = curr_concept.getIsDefined();
	      if (isDefined != null) {
		  if (isDefined.equals(Boolean.TRUE)) {
		      primitive = "No";
		  } else {
		      primitive = "Yes";
		  }
	      }
      }    
      
      String kind = "not available";
      String kind_prop_name = "Kind";
      String kind_label = "Kind:";
    %>
    
   
    <%
    if (primitive != null) {
    %>
	  <b>Additional Concept Data:</b>&nbsp;
	  <table class="datatable">
	    <tr class="dataRowLight">
	      <td><%=primitive_label%>&nbsp;<%=primitive%></td>
	      <td>&nbsp;</td>
	    </tr>
	  </table>  
    <%
    } else {
    %>
	  <b>Additional Concept Data:</b>&nbsp;<i>None</i>
    <%
    } 

    %>	  
</p>
<%
  String requestURL = request.getRequestURL().toString();
  int idx = requestURL.indexOf("pages");
  requestURL = requestURL.substring(0, idx);
  
  prop_dictionary = dictionary.replace(" ", "%20");
  
  
  String url = requestURL + "ConceptReport.jsp?dictionary=" + prop_dictionary + "&code=" + concept_id;
  String url_text = "ConceptReport.jsp?dictionary=" + prop_dictionary + "&code=" + concept_id;
  
  if (prop_version != null) {
      url = requestURL + "ConceptReport.jsp?dictionary=" + prop_dictionary + "&version=" + prop_version + "&code=" + concept_id;
      url_text = "ConceptReport.jsp?dictionary=" + prop_dictionary + "&version=" + prop_version + "&code=" + concept_id;
  }
  String bookmark_title = prop_dictionary + "%20" + concept_id;
  
  
%>
<p>
  <b>URL</b>: <%= requestURL %><%= url_text %>
<%
}
%>