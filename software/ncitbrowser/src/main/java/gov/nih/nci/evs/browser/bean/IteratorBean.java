package gov.nih.nci.evs.browser.bean;

import java.io.*;
import java.util.*;

import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.apache.logging.log4j.*;

import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.properties.*;

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

//public class IteratorBean extends Object implements Serializable {
public class IteratorBean extends Object implements Serializable {
	static final long serialVersionUID = 13L;
	private static Logger _logger = LogManager.getLogger(IteratorBean.class);
    private static int DEFAULT_MAX_RETURN = 100;
    private ResolvedConceptReferencesIterator _iterator = null;
    private int _size = 0;
    private List _list = null;

    private int _pageNumber;
    private int _pageSize;
    private int _startIndex;
    private int _endIndex;
    private int _numberOfPages;

    private int _lastResolved;
    private int _maxReturn = 100;
    private int _stopping_rule = 1000;

    private String _message = null;

    private String _matchText = null;

    private String _key = null;
    private boolean _timeout = false;

    public IteratorBean(ResolvedConceptReferencesIterator iterator) {
        _iterator = iterator;
        _maxReturn = DEFAULT_MAX_RETURN;
        initialize();
    }

    public IteratorBean(ResolvedConceptReferencesIterator iterator,
        int maxReturn) {
        _iterator = iterator;
        _maxReturn = maxReturn;
        initialize();
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
        try {
            if (_iterator == null) {
                _size = 0;
            } else {
                _size = _iterator.numberRemaining();

            }
            _pageNumber = 1;

/*
            _list = new ArrayList(_size);
            for (int i = 0; i < _size; i++) {
                _list.add(null);
            }
*/

            _list = new ArrayList<ResolvedConceptReference>();

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

    public List getData(int pageNumber) {
        int idx1 = getStartIndex(pageNumber);
        int idx2 = getEndIndex(pageNumber);
        return getData(idx1, idx2);
    }

    public boolean hasNext() {
		try {
			return _iterator.hasNext();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

    public ResolvedConceptReference getFirstResolvedConceptReference(ResolvedConceptReferencesIterator iterator) {
		if (iterator == null) {
			return null;
		}
		try {
			int numberRemaining = iterator.numberRemaining();
			while (iterator.hasNext()) {
				//ResolvedConceptReference[] refs = iterator.next(1).getResolvedConceptReference();
				//return refs[0];
				ResolvedConceptReference ref = (ResolvedConceptReference) iterator.next();
				if (ref == null) {
					//System.out.println("(*) UserSessionBean.broken iterator getFirstResolvedConceptReference returns null???");
				}
				return ref;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
    }

    public List getData(int idx1, int idx2) {
        if (_list.size() > 0 && idx2 <= _list.size()) {
			return copyData(idx1, idx2);
		}

        _logger.debug("Retrieving data (from: " + idx1 + " to: " + idx2 + ")");
        //long ms = System.currentTimeMillis();
        long dt = 0;
        long total_delay = 0;
        int upper_bound = idx2;
        _timeout = false;

        try {
            int count = (idx2 - idx1) + 1;
            int lcv = 0;
			while (_iterator != null && _iterator.hasNext()) {

				ResolvedConceptReference[] refs =
					_iterator.next(_maxReturn).getResolvedConceptReference();

				if (refs != null) {
					for (ResolvedConceptReference ref : refs) {

						_lastResolved++;
						upper_bound = _lastResolved;
						_list.add(ref);

						lcv++;

						//displayRef(ref);

					}
/*
					if (_list.size() > idx2 && knt != _maxReturn) {
						break;
					}
*/

					if (_list.size() > idx2 && lcv > _stopping_rule) {
						break;
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

		if (ref == null) {
			return;
		}


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

    public void dumpData(List list) {
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

    public void dumpData(OutputStreamWriter osWriter, List list) {
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

     public List copyData(int idx1, int idx2) {
		List arrayList = new ArrayList();

        if (_list.size() == 0) {
			return arrayList;
		}

        // bound checking for idx1
		if (idx1 > _list.size()-1) {
			idx1 = _list.size()-1;
		}

		if (idx2 > _list.size()-1) {
			idx2 = _list.size()-1;
		}

		if (idx2 < idx1) idx2 = idx1;

		for (int i=idx1; i<=idx2; i++) {
			ResolvedConceptReference ref = (ResolvedConceptReference) _list.get(i);
			arrayList.add(ref);
			if (i > _list.size()) {
				break;
			}
		}

		return arrayList;
	}



}
