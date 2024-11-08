package gov.nih.nci.evs.browser.bean;

import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.properties.*;
import gov.nih.nci.evs.browser.utils.*;
//import gov.nih.nci.evs.security.*;
//import gov.nih.nci.system.client.*;
import java.awt.*;
import java.awt.event.* ;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;
import javax.swing.* ;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;
import javax.swing.tree.*;
import org.apache.logging.log4j.*;
import org.LexGrid.custom.relations.*;
//import org.LexGrid.LexBIG.caCore.interfaces.*;
//import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.*;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;



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


public class MappingIteratorBean extends Object {
	private static Logger _logger = LogManager.getLogger(MappingIteratorBean.class);
    private static int DEFAULT_MAX_RETURN = 100;
    private static int INITIAL_ITERATOR_RESOLUTION = 500;

    private ResolvedConceptReferencesIterator _iterator = null;
    private int _size = 0;
    private java.util.List _list = null;

    private int _pageNumber;
    private int _pageSize;
    private int _startIndex;
    private int _endIndex;
    private int _numberOfPages;

    private int _lastResolved;
    private int _maxReturn = 100;
    private String _message = null;

    private String _matchText = null;

    private String _key = null;
    private boolean _timeout = false;
    private java.util.List<TerminologyMapBean> _tmb_list = null;

	public MappingIteratorBean() {
	}


    public MappingIteratorBean(ResolvedConceptReferencesIterator iterator) {
        _iterator = iterator;
        _maxReturn = DEFAULT_MAX_RETURN;
        initialize();
    }

    public MappingIteratorBean(ResolvedConceptReferencesIterator iterator,
        int maxReturn) {
        _iterator = iterator;
        _maxReturn = maxReturn;
        initialize();
    }


	public MappingIteratorBean(
		ResolvedConceptReferencesIterator iterator,
		int size,
		int startIndex,
		int endIndex,
		int pageNumber,
		int numberOfPages) {

		this._iterator = iterator;
		this._size = size;
		this._startIndex = startIndex;
		this._endIndex = endIndex;
		this._pageNumber = pageNumber;
		this._numberOfPages = numberOfPages;
		this._pageSize = 50;
		this._list = new ArrayList();
	}

	public MappingIteratorBean(
		ResolvedConceptReferencesIterator iterator,
		int size,
		int startIndex,
		int endIndex,
		int pageNumber,
		int numberOfPages,
		java.util.List list) {

		this._iterator = iterator;
		this._size = size;
		this._startIndex = startIndex;
		this._endIndex = endIndex;
		this._pageNumber = pageNumber;
		this._numberOfPages = numberOfPages;
		this._pageSize = 50;
		this._list = list;
	}

	public MappingIteratorBean(java.util.List<MappingData> md_list) {
		this._list = md_list;
		initialize_bean();
	}

	public MappingIteratorBean(String mappingCodingScheme, String mappingCodingSchemeVersion) {
	    String metadata = getMappingMetadata(mappingCodingScheme, mappingCodingSchemeVersion);
	    System.out.println(metadata);
		Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(metadata);
		String sourceCodingScheme = (String) u.elementAt(0);
		String sourceCodingSchemeVersion = (String) u.elementAt(1);
		String targetCodingScheme = (String) u.elementAt(2);
		String targetCodingSchemeVersion = (String) u.elementAt(3);
        String associationName = (String) u.elementAt(4);

        java.util.List<TerminologyMapBean> tmb_list = new MappingExtensionImpl().resolveBulkMapping(mappingCodingScheme, mappingCodingSchemeVersion);
	    java.util.List<MappingData> md_list = terminologyMapBean2MappingData(tmb_list,
			  sourceCodingScheme,
			  sourceCodingSchemeVersion,
			  associationName,
			  targetCodingScheme,
			  targetCodingSchemeVersion);
		this._list = md_list;
		initialize_bean();
	}

    // for testing use:
	public MappingIteratorBean(String serviceUrl, String mappingCodingScheme, String mappingCodingSchemeVersion) {
	    String metadata = getMappingMetadata(serviceUrl, mappingCodingScheme, mappingCodingSchemeVersion);
	    System.out.println(metadata);
		Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(metadata);
		String sourceCodingScheme = (String) u.elementAt(0);
		String sourceCodingSchemeVersion = (String) u.elementAt(1);
		String targetCodingScheme = (String) u.elementAt(2);
		String targetCodingSchemeVersion = (String) u.elementAt(3);
        String associationName = (String) u.elementAt(4);

        java.util.List<TerminologyMapBean> tmb_list = new MappingExtensionImpl().resolveBulkMapping(mappingCodingScheme, mappingCodingSchemeVersion);
	    java.util.List<MappingData> md_list = terminologyMapBean2MappingData(tmb_list,
			  sourceCodingScheme,
			  sourceCodingSchemeVersion,
			  associationName,
			  targetCodingScheme,
			  targetCodingSchemeVersion);
		this._list = md_list;
		initialize_bean();
	}


	public void initialize_bean() {
		//this._list = md_list;
		this._iterator = null;
		this._size = _list.size();
		this._startIndex = 0;
		this._endIndex = _size;
		this._pageNumber = 1;
		this._pageSize = 50;
		this._numberOfPages = _size / _pageSize;

		_pageNumber = 1;
		_pageSize = Constants.DEFAULT_PAGE_SIZE;
		_numberOfPages = _size / _pageSize;
		if (_pageSize * _numberOfPages < _size) {
			_numberOfPages = _numberOfPages + 1;
		}
		_lastResolved = -1;
	}

    public int getNumberOfPages() {
        return _numberOfPages;
    }

    public void setIterator(ResolvedConceptReferencesIterator iterator) {
        _iterator = iterator;
        _maxReturn = DEFAULT_MAX_RETURN;
        initialize();
    }

    public ResolvedConceptReferencesIterator getIterator() {
        return _iterator;
    }

    public boolean getTimeout() {
        return _timeout;
    }

    public void initialize() {
		MappingData mappingData = null;
		String sourceCode = null;
		String sourceName = null;
		String sourceCodingScheme = null;
		String sourceCodingSchemeVesion = null;
		String sourceCodeNamespace = null;
		String associationName = null;
		String rel = null;
		int score = 0;
		String targetCode = null;
		String targetName = null;
		String targetCodingScheme = null;
		String targetCodingSchemeVesion = null;
		String targetCodeNamespace = null;

        try {
            if (_iterator == null) {
                _size = 0;
            } else {
				_list = new ArrayList();
                _size = _iterator.numberRemaining();

                //KLO, work-around #2
                int knt = 0;
                while (_iterator.hasNext()) {
					knt++;
					if (knt > INITIAL_ITERATOR_RESOLUTION) break;

					ResolvedConceptReference ref = _iterator.next();

					_lastResolved++;

					//upper_bound = _lastResolved;

					String description;

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

								mappingData = new MappingData(
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

								_list.add(mappingData);

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

								mappingData = new MappingData(
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
								_list.add(mappingData);
							}
						}
					}

				}
                if (knt > _size) _size = knt;
            }


            _pageNumber = 1;
            //_list = new ArrayList();

            _pageSize = Constants.DEFAULT_PAGE_SIZE;
            _numberOfPages = _size / _pageSize;
            if (_pageSize * _numberOfPages < _size) {
                _numberOfPages = _numberOfPages + 1;
            }
            _lastResolved = -1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public int getMumberOfPages() {
        return _numberOfPages;
    }

    public int getSize() {
        return _size;
    }

    public void setPageSize(int pageSize) {
        _pageSize = pageSize;
    }

    public int getPageSize() {
        return _pageSize;
    }

    public int getLastResolved() {
        return _lastResolved;
    }

    public int getStartIndex(int pageNumber) {
        _startIndex = (pageNumber - 1) * _pageSize;
        if (_startIndex < 0)
            _startIndex = 0;
        return _startIndex;
    }

    public int getEndIndex(int pageNumber) {
        _endIndex = pageNumber * _pageSize - 1;
        if (_endIndex > (_size - 1))
            _endIndex = _size - 1;
        return _endIndex;
    }


     public java.util.List copyData(int idx1, int idx2) {
		java.util.List arrayList = new ArrayList();

        if (_list.size() == 0) return arrayList;

        // bound checking for idx1
        if (idx1 > _list.size()-1) {
            idx1 = _list.size()-1;
        }

		if (idx2 > _list.size()-1) {
			idx2 = _list.size()-1;
		}

		if (idx2 < idx1) idx2 = idx1;

		for (int i=idx1; i<=idx2; i++) {
			MappingData ref = (MappingData) _list.get(i);
			arrayList.add(ref);
			if (i > _list.size()) break;
		}
		return arrayList;
	}


    public java.util.List getData(int pageNumber) {
        int idx1 = getStartIndex(pageNumber);
        int idx2 = getEndIndex(pageNumber);
        return getData(idx1, idx2);
    }

    public java.util.List getData(int idx1, int idx2) {
        if (idx2 <= _list.size()) return copyData(idx1, idx2);
		MappingData mappingData = null;
		String sourceCode = null;
		String sourceName = null;
		String sourceCodingScheme = null;
		String sourceCodingSchemeVesion = null;
		String sourceCodeNamespace = null;
		String associationName = null;
		String rel = null;
		int score = 0;
		String targetCode = null;
		String targetName = null;
		String targetCodingScheme = null;
		String targetCodingSchemeVesion = null;
		String targetCodeNamespace = null;


        //_logger.debug("Retrieving data (from: " + idx1 + " to: " + idx2 + ")");
        //long ms = System.currentTimeMillis();
        //long dt = 0;
        //long total_delay = 0;
        int upper_bound = idx2;
        _timeout = false;

//KLO, work-around #2
		if (idx2 <= _list.size()) {
			return copyData(idx1, idx2);
		}

        try {
			while (_iterator != null && _iterator.hasNext()) {
				//KLO 03/05/11
				if (idx2 <= _list.size()) {
					return copyData(idx1, idx2);
				}

				ResolvedConceptReference[] refs =
					_iterator.next(_maxReturn).getResolvedConceptReference();

				if (refs != null) {
					for (ResolvedConceptReference ref : refs) {
						 //displayRef(ref);
						_lastResolved++;

						upper_bound = _lastResolved;

						String description;

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

									mappingData = new MappingData(
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
									_list.add(mappingData);

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

									mappingData = new MappingData(
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
									_list.add(mappingData);

								}
							}
						}
					}
				}
			}


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (_list.size() > _size) {
			_size = _list.size();
		}
		return copyData(idx1, idx2);
    }



    protected void displayRef(ResolvedConceptReference ref) {
        _logger.debug(ref.getConceptCode() + ":"
            + ref.getEntityDescription().getContent());
    }

    protected void displayRef(int k, ResolvedConceptReference ref) {
        _logger.debug("(" + k + ") " + ref.getCodingSchemeName() + " "
            + ref.getConceptCode() + ":"
            + ref.getEntityDescription().getContent());
    }

    protected void displayRef(OutputStreamWriter osWriter, int k,
        ResolvedConceptReference ref) {
        try {
            osWriter.write("(" + k + ") " + ref.getConceptCode() + ":"
                + ref.getEntityDescription().getContent() + "\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dumpData(java.util.List list) {
        if (list == null) {
            _logger.warn("WARNING: dumpData list = null???");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            ResolvedConceptReference rcr =
                (ResolvedConceptReference) list.get(i);
            int j = i + 1;
            displayRef(j, rcr);
        }
    }

    public void dumpData(OutputStreamWriter osWriter, java.util.List list) {
        if (list == null) {
            _logger.warn("WARNING: dumpData list = null???");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            ResolvedConceptReference rcr =
                (ResolvedConceptReference) list.get(i);
            int j = i + 1;
            displayRef(osWriter, j, rcr);
        }
    }


    public void setKey(String key) {
        _key = key;
    }

    public String getKey() {
        return _key;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public String getMessage() {
        return _message;
    }

    public void setMatchText(String matchText) {
        _matchText = matchText;
    }

    public String getMatchText() {
        return _matchText;
    }



    public java.util.List<TerminologyMapBean> resolveBulkMapping(String mappingName, String mappingVersion) {
		java.util.List<TerminologyMapBean> tmb_list = new MappingExtensionImpl().resolveBulkMapping(mappingName, mappingVersion);
		return tmb_list;
	}

    public static LexBIGService createLexBIGService(String serviceUrl) {
//        try {
//            if (serviceUrl == null || serviceUrl.compareTo("") == 0 || serviceUrl.compareToIgnoreCase("null") == 0) {
                LexBIGService lbSvc = LexBIGServiceImpl.defaultInstance();
                return lbSvc;
//            }
//            LexEVSApplicationService lexevsService =
//                (LexEVSApplicationService) ApplicationServiceProvider
//                    .getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");
//            return (LexBIGService) lexevsService;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    public static String getMappingMetadata(String mappingCodingScheme, String mappingCodingSchemeVersion) {
		return getMappingMetadata(null, mappingCodingScheme, mappingCodingSchemeVersion);
	}

    public static String getMappingMetadata(String serviceUrl, String mappingCodingScheme, String mappingCodingSchemeVersion) {
		LexBIGService lbSvc = createLexBIGService(serviceUrl);
		CodingSchemeDataUtils csdu = new CodingSchemeDataUtils(lbSvc);
		return csdu.getMappingMetadata(mappingCodingScheme, mappingCodingSchemeVersion);
	}

	public static java.util.List<MappingData> terminologyMapBean2MappingData(java.util.List<TerminologyMapBean> tmb_list,
			  String sourceCodingScheme,
			  String sourceCodingSchemeVersion,
			  String associationName,
			  String targetCodingScheme,
			  String targetCodingSchemeVersion) {

        java.util.List<MappingData> mb_list = new ArrayList();
        for (int i=0; i<tmb_list.size(); i++) {
			TerminologyMapBean tmb = (TerminologyMapBean) tmb_list.get(i);
			MappingData md = new MappingData(
		       tmb.getSourceCode(),
		       tmb.getSourceName(),
		       sourceCodingScheme,
		       sourceCodingSchemeVersion,
		       tmb.getSource(),
		       associationName,
		       tmb.getRel(),
		       Integer.parseInt(tmb.getMapRank()),
		       tmb.getTargetCode(),
		       tmb.getTargetName(),
		       targetCodingScheme,
		       targetCodingSchemeVersion,
		       tmb.getTarget());
		    mb_list.add(md);
		}
		return mb_list;
	}
}

