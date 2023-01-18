package gov.nih.nci.evs.browser.utils;


import gov.nih.nci.evs.browser.common.*;
//import gov.nih.nci.evs.security.SecurityToken;
//import gov.nih.nci.system.client.ApplicationServiceProvider;
import java.io.*;
import java.text.*;
import java.util.*;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
//import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeTagList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.RenderingDetail;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods.*;
import org.LexGrid.LexBIG.Extensions.Generic.SupplementExtension;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.naming.*;
import org.LexGrid.relations.Relations;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;


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


public class CodingSchemeUtils {
	static int MAX_VERSION_LENGTH = 30;
	LexBIGService lbSvc = null;
	HashMap _localName2CodingSchemeNameHashMap = null;

	public CodingSchemeUtils(LexBIGService lbSvc) {
		this.lbSvc = lbSvc;
	}

	public void setLexBIGService(LexBIGService lbSvc) {
		this.lbSvc = lbSvc;
	}

    public HashMap getLocalName2CodingSchemeNameHashMap() {
		if (_localName2CodingSchemeNameHashMap != null) {
			return _localName2CodingSchemeNameHashMap;
		}
        _localName2CodingSchemeNameHashMap = new HashMap();
        try {
            CodingSchemeRenderingList csrl = null;
            try {
                csrl = lbSvc.getSupportedCodingSchemes();
            } catch (LBInvocationException ex) {
                ex.printStackTrace();
                return null;
            }
            CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
            if (csrs == null) return null;
            for (int i = 0; i < csrs.length; i++) {
                //int j = i + 1;
                CodingSchemeRendering csr = csrs[i];
                CodingSchemeSummary css = csr.getCodingSchemeSummary();
                //Boolean isActive =
                //        csr.getRenderingDetail().getVersionStatus().equals(
                //            CodingSchemeVersionStatus.ACTIVE);

                String representsVersion = css.getRepresentsVersion();
                if (representsVersion == null) return null;
                if (representsVersion.length() <= MAX_VERSION_LENGTH) {

					CodingSchemeVersionOrTag vt =
						new CodingSchemeVersionOrTag();
					vt.setVersion(representsVersion);

					try {
						CodingScheme cs = lbSvc.resolveCodingScheme(css.getFormalName(), vt);
						String[] localnames = cs.getLocalName();
						if (localnames == null) return null;
						for (int m = 0; m < localnames.length; m++) {
							String localname = localnames[m];
							_localName2CodingSchemeNameHashMap.put(localname, cs.getCodingSchemeName());
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}
			    }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _localName2CodingSchemeNameHashMap;
    }

    public void dumpHashMap(PrintWriter pw, String label, HashMap hmap) {
		pw.println(label);
		Vector key_vec = new Vector();
		Iterator it = hmap.keySet().iterator();
		if (it == null) return;
		while (it.hasNext()) {
			String key = (String) it.next();
			key_vec.add(key);
		}
		key_vec = new SortUtils().quickSort(key_vec);
		for (int i=0; i<key_vec.size(); i++) {
			String key = (String) key_vec.elementAt(i);
			String value = (String) hmap.get(key);
			pw.println("\t" + key + " --> " + value);
		}

	}

    public static void main(String args[]) {
		/*
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		CodingSchemeUtils codingSchemeUtils = new CodingSchemeUtils(lbSvc);
		PrintWriter pw = null;
		String outputfile = "cs.txt";
		try {
			pw = new PrintWriter(outputfile, "UTF-8");
			HashMap localName2CodingSchemeNameHashMap = codingSchemeUtils.getLocalName2CodingSchemeNameHashMap();
            codingSchemeUtils.dumpHashMap(pw, "LocalName2CodingSchemeName", localName2CodingSchemeNameHashMap);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				pw.close();
				System.out.println("Output file " + outputfile + " generated.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		*/
	}
}
