package gov.nih.nci.evs.browser.utils;


import gov.nih.nci.system.client.ApplicationServiceProvider;
import java.io.*;
import java.net.URI;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Map;
import org.apache.log4j.*;
import org.json.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.commonTypes.*;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSApplicationService;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
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
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.naming.*;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;
import org.LexGrid.util.PrintUtility;
import org.LexGrid.valueSets.DefinitionEntry;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;
import org.LexGrid.valueSets.EntityReference;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.LexGrid.valueSets.PropertyMatchValue;
import org.LexGrid.valueSets.PropertyReference;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.*;


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


public class ValueSetResolver {
	LexBIGService lbSvc = null;
	LexEVSValueSetDefinitionServices vsd_service = null;
	ValueSetDefUtils valueSetDefUtils = null;
	HashMap vsdUri2NameMap = null;
	CodingSchemeDataUtils csdu = null;

	public ValueSetResolver() {

	}

	public ValueSetResolver(LexBIGService lbSvc, LexEVSValueSetDefinitionServices vsd_service) {
		this.lbSvc = lbSvc;
		valueSetDefUtils = new ValueSetDefUtils(lbSvc, vsd_service);
		vsdUri2NameMap = valueSetDefUtils.getVsdUri2NameMap();
        csdu = new CodingSchemeDataUtils(lbSvc);
	}

	public void run(String outputfile) {
		if (outputfile == null) return;

		long ms = System.currentTimeMillis();
		PrintWriter pw = null;
		String version = null;
		boolean resolveObjects = false;
		Vector result_vec = new Vector();
		int i = 0;
		try {
			pw = new PrintWriter(outputfile, "UTF-8");
			if (pw != null) {
				Iterator it = vsdUri2NameMap.keySet().iterator();
				if (it == null) return;
				while (it.hasNext()) {
					String cs_uri = (String) it.next();
					String name = (String) vsdUri2NameMap.get(cs_uri);
					i++;
					System.out.println("(" + i + ") " + name + " (" + cs_uri + ")");
					ResolvedConceptReferencesIterator rcri = null;
					try {
						rcri = csdu.resolveCodingScheme(cs_uri, version, resolveObjects);
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					if (rcri == null) {
						//pw.println("\t" + "Unable to resolve " + name + " (" + cs_uri + ")");
						result_vec.add(name + "|" + cs_uri + "|Exception thrown at csdu.resolveCodingScheme.");
					} else {
						try {
							int numberRemaining = rcri.numberRemaining();
							result_vec.add(name + "|" + cs_uri + "|" + numberRemaining);
						} catch (Exception ex) {
							ex.printStackTrace();
							result_vec.add(name + "|" + cs_uri + "|Exception thrown at rcri.numberRemaining()");
						}
					}
				}
				result_vec = new SortUtils().quickSort(result_vec);
				int j = 0;
				pw.println("\n\n");
				for (i=0; i<result_vec.size(); i++) {
					String t = (String) result_vec.elementAt(i);
					j++;
					pw.println("(" + j + ") " + t);
				}
			}


		} catch (Exception ex) {

		} finally {
			try {
				if (pw != null) pw.close();
				System.out.println("Output file " + outputfile + " generated.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("Total run time (ms): " + (System.currentTimeMillis() - ms));
	}
/*
    public static void main(String[] args) {
		String outputfile = args[0];
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		LexEVSValueSetDefinitionServices vsd_service = RemoteServerUtil.getLexEVSValueSetDefinitionServices();
        ValueSetResolver resolver = new ValueSetResolver(lbSvc, vsd_service);
		long ms = System.currentTimeMillis();
		resolver.run(outputfile);
		System.out.println("ValueSetResolver total run time (ms): " + (System.currentTimeMillis() - ms));
    }
*/
}
