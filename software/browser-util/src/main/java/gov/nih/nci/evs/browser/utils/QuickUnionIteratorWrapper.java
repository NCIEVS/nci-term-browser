package gov.nih.nci.evs.browser.utils;


import java.io.Serializable;
import java.util.*;
import org.apache.logging.log4j.*;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;


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


public class QuickUnionIteratorWrapper implements ResolvedConceptReferencesIterator {
	private static Logger _logger = LogManager.getLogger(QuickUnionIteratorWrapper.class);

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6285970594380754741L;

    /** The current iterator. */
    private int _currentIterator = 0;

    /** The iterators. */
    private List<ResolvedConceptReferencesIteratorWrapper> _iterators =
        new ArrayList<ResolvedConceptReferencesIteratorWrapper>();

    //private String _currentCodingSchemeName = null;
    //private Vector<String> _codingSchemeNames = null;

    /**
     * Instantiates a new Iterator. Be sure that any desired restrictions have
     * already been placed on the CodedNodeSets before passing into this
     * constructor
     *
     * @param codedNodeSets the coded node sets
     * @param sortOptions the sort options
     * @param filterOptions the filter options
     * @param restrictToProperties the restrict to properties
     * @param restrictToPropertyTypes the restrict to property types
     * @param resolve the resolve
     *
     * @throws LBException the LB exception
     */

 /*
    public QuickUnionIteratorWrapper(Vector<CodedNodeSet> codedNodeSets,
        SortOptionList sortOptions, LocalNameList filterOptions,
        LocalNameList restrictToProperties,
        PropertyType[] restrictToPropertyTypes, boolean resolve)
        throws LBException {

        for (CodedNodeSet cns : codedNodeSets) {
            // KLO 012310
            if (cns != null) {
                try {
                    ResolvedConceptReferencesIterator iterator =
                        cns.resolve(sortOptions, filterOptions,
                            restrictToProperties, restrictToPropertyTypes,
                            resolve);
                    if (iterator != null) {
                        _iterators.add(iterator);
                    }
                } catch (Exception ex) {
                    _logger
                        .error("QuickUnionIteratorWrapper constructor - cns.resolve throws exception???");
                    ex.printStackTrace();
                }
            }
        }

        Collections.sort(_iterators, new IteratorSizeComparator());
    }
*/

    public QuickUnionIteratorWrapper(Vector<String> codingSchemeNames,
        Vector<CodedNodeSet> codedNodeSets,
        SortOptionList sortOptions, LocalNameList filterOptions,
        LocalNameList restrictToProperties,
        PropertyType[] restrictToPropertyTypes, boolean resolve)
        throws LBException {

		for (int lcv=0; lcv<codedNodeSets.size(); lcv++) {
           CodedNodeSet cns = (CodedNodeSet) codedNodeSets.elementAt(lcv);
           if (cns != null) {
                try {
                    ResolvedConceptReferencesIterator iterator =
                        cns.resolve(sortOptions, filterOptions,
                            restrictToProperties, restrictToPropertyTypes,
                            resolve);

                    if (iterator != null) {
						ResolvedConceptReferencesIteratorWrapper wrapper = new ResolvedConceptReferencesIteratorWrapper(iterator);
						String codingSchemeName = (String) codingSchemeNames.elementAt(lcv);

						wrapper.setCodingSchemeName(codingSchemeName);
                        _iterators.add(wrapper);

                    }
                } catch (Exception ex) {
                    _logger
                        .error("QuickUnionIteratorWrapper constructor - cns.resolve throws exception???");
                    //ex.printStackTrace();
                    //System.out.println("WARNING: QuickUnionIteratorWrapper constructor - cns.resolve throws exception???");

                }
            }
        }
        //Collections.sort(_iterators, new IteratorSizeComparator());
        Collections.sort(_iterators, new IteratorWrapperSizeComparator());

    }



    /*
     * (non-Javadoc)
     *
     * @see
     * org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator
     * #get(int, int)
     */
    public ResolvedConceptReferenceList get(int start, int end)
            throws LBResourceUnavailableException, LBInvocationException,
            LBParameterException {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator
     * #getNext()
     */
    public ResolvedConceptReferenceList getNext() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator
     * #next()
     */
    public ResolvedConceptReference next()
            throws LBResourceUnavailableException, LBInvocationException {
        return next(1).getResolvedConceptReference(0);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator
     * #next(int)
     */
    public ResolvedConceptReferenceList next(int maxToReturn)
            throws LBResourceUnavailableException, LBInvocationException {
        ResolvedConceptReferenceList returnList =
            new ResolvedConceptReferenceList();
        while (returnList.getResolvedConceptReferenceCount() < maxToReturn) {
            ResolvedConceptReference ref = getNextFromList();
            if (ref == null) {
                return returnList;
            } else {
                returnList.addResolvedConceptReference(ref);
            }
        }
        return returnList;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator
     * #scroll(int)
     */
    public ResolvedConceptReferencesIterator scroll(int maxToReturn)
            throws LBResourceUnavailableException, LBInvocationException {
        throw new UnsupportedOperationException();

    }

    /*
     * (non-Javadoc)
     *
     * @see org.LexGrid.LexBIG.Utility.Iterators.EntityListIterator#hasNext()
     */
    public boolean hasNext() throws LBResourceUnavailableException {
        removeEmptyIterators();
        return _iterators.size() > 0;
    }

    /**
     * Removes the empty iterators.
     */

/*
    private void removeEmptyIterators() {
        List<ResolvedConceptReferencesIterator> newList =
            new ArrayList<ResolvedConceptReferencesIterator>();
        for (ResolvedConceptReferencesIterator itr : _iterators) {
            try {
                if (itr.hasNext()) {
                    newList.add(itr);
                }
            } catch (LBResourceUnavailableException e) {
                throw new RuntimeException(e);
            }
        }
        _iterators = newList;
    }
*/

    private void removeEmptyIterators() {
        List<ResolvedConceptReferencesIteratorWrapper> newList =
            new ArrayList<ResolvedConceptReferencesIteratorWrapper>();
        for (ResolvedConceptReferencesIteratorWrapper itr : _iterators) {
            try {
                if (itr.getIterator().hasNext()) {
                    newList.add(itr);
                }
            } catch (LBResourceUnavailableException e) {
                throw new RuntimeException(e);
            }
        }
        _iterators = newList;
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * org.LexGrid.LexBIG.Utility.Iterators.EntityListIterator#numberRemaining()
     */
     /*
    public int numberRemaining() throws LBResourceUnavailableException {
        int number = 0;
        for (ResolvedConceptReferencesIterator itr : _iterators) {
            number += itr.numberRemaining();
        }
        return number;
    }
    */

    public int numberRemaining() throws LBResourceUnavailableException {
        int number = 0;
        int lcv = 0;
        for (ResolvedConceptReferencesIteratorWrapper itr : _iterators) {
			lcv++;
			int numberRemaining = itr.getIterator().numberRemaining();
            number += numberRemaining;
        }
        return number;
    }


    /*
     * (non-Javadoc)
     *
     * @see org.LexGrid.LexBIG.Utility.Iterators.EntityListIterator#release()
     */

    /*
    public void release() throws LBResourceUnavailableException {
        for (ResolvedConceptReferencesIterator itr : _iterators) {
            itr.release();
        }
    }
    */

     public void release() throws LBResourceUnavailableException {
        for (ResolvedConceptReferencesIteratorWrapper itr : _iterators) {
            itr.getIterator().release();
        }
    }


    /**
     * Gets the next from list.
     *
     * @return the next from list
     */

     /*
    private ResolvedConceptReference getNextFromList() {
        try {
            while (hasNext()) {
                int iterator = _currentIterator % _iterators.size();
                ResolvedConceptReferencesIterator itr =
                    _iterators.get(iterator);
                if (itr.hasNext()) {
                    _currentIterator++;
                    return itr.next();
                } else {
                    _currentIterator++;
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    */


    private ResolvedConceptReference getNextFromList() {
        try {
            while (hasNext()) {
                int iterator = _currentIterator % _iterators.size();

                ResolvedConceptReferencesIteratorWrapper itr_wrapper =
                    _iterators.get(iterator);

                ResolvedConceptReferencesIterator itr = itr_wrapper.getIterator();
                String codingSchemeName = itr_wrapper.getCodingSchemeName();

                if (itr.hasNext()) {
                    _currentIterator++;
                    //return itr.next();
                    //KLO
                    ResolvedConceptReference rcr = itr.next();
                    rcr.setCodingSchemeName(codingSchemeName);
                    return rcr;
                } else {
                    _currentIterator++;
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * The Class IteratorSizeComparator.
     */
    private static class IteratorSizeComparator implements java.io.Serializable,
            Comparator<ResolvedConceptReferencesIterator> {
        /*
         * (non-Javadoc)
         *
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(ResolvedConceptReferencesIterator itr1,
            ResolvedConceptReferencesIterator itr2) {
            try {
                return itr2.numberRemaining() - itr1.numberRemaining();
            } catch (LBResourceUnavailableException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class IteratorWrapperSizeComparator implements java.io.Serializable,
            Comparator<ResolvedConceptReferencesIteratorWrapper> {

        /*
         * (non-Javadoc)
         *
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(ResolvedConceptReferencesIteratorWrapper itr1,
            ResolvedConceptReferencesIteratorWrapper itr2) {
            try {
				ResolvedConceptReferencesIterator iterator1 = itr1.getIterator();
				ResolvedConceptReferencesIterator iterator2 = itr2.getIterator();

                return iterator2.numberRemaining() - iterator1.numberRemaining();
            } catch (LBResourceUnavailableException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
