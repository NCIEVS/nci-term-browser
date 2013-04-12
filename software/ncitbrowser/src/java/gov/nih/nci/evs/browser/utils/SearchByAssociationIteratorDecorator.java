/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-term-browser/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.utils;

import java.util.*;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;

/**
 * 
 */

/**
 * The Class SearchByAssociationIteratorDecorator. Decorates a
 * ResolvedConceptReferencesIterator to provide paging support for Associated
 * Concept-type searches. As the iterator advances, subconcepts are queried from
 * the decorated iterator on demand, rather than all at once. This elminates the
 * need to resolve large CodedNodeGraphs.
 */
public class SearchByAssociationIteratorDecorator implements
        ResolvedConceptReferencesIterator {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4126716487618136771L;

    /** The lbs. */
    private static LexBIGService _lbs = RemoteServerUtil.createLexBIGService();

    /** The quick iterator. */
    private ResolvedConceptReferencesIterator _quickIterator;

    /** The resolve forward. */
    private boolean _resolveForward;

    /** The resolve backward. */
    private boolean _resolveBackward;

    /** The resolve association depth. */
    private int _resolveAssociationDepth;

    /** The max to return. */
    private int _maxToReturn;

    /** The position. */
    private int _position = 0;

    /** The current children. */
    private List<ResolvedConceptReference> _currentChildren =
        new ArrayList<ResolvedConceptReference>();

    private NameAndValueList _associationNameAndValueList;
    private NameAndValueList _associationQualifierNameAndValueList;

    private HashSet _hset = null;

    /**
     * Instantiates a new search by association iterator decorator.
     * 
     * @param quickIterator the quick iterator
     * @param resolveForward the resolve forward
     * @param resolveBackward the resolve backward
     * @param resolveAssociationDepth the resolve association depth
     * @param maxToReturn the max to return
     */
    public SearchByAssociationIteratorDecorator(
        ResolvedConceptReferencesIterator quickIterator,
        boolean resolveForward, boolean resolveBackward,
        int resolveAssociationDepth, int maxToReturn) {
        _quickIterator = quickIterator;
        _resolveForward = resolveForward;
        _resolveBackward = resolveBackward;
        _resolveAssociationDepth = resolveAssociationDepth;
        _maxToReturn = maxToReturn;
        _associationNameAndValueList = null;
        _associationQualifierNameAndValueList = null;

        _hset = new HashSet();

        // _logger.debug("Type 1 SearchByAssociationIteratorDecorator ");

    }

    public SearchByAssociationIteratorDecorator(
        ResolvedConceptReferencesIterator quickIterator,
        boolean resolveForward, boolean resolveBackward,
        NameAndValueList associationNameAndValueList,
        NameAndValueList associationQualifierNameAndValueList,
        int resolveAssociationDepth, int maxToReturn) {

        _quickIterator = quickIterator;
        _resolveForward = resolveForward;
        _resolveBackward = resolveBackward;
        _resolveAssociationDepth = resolveAssociationDepth;
        _maxToReturn = maxToReturn;
        _associationNameAndValueList = associationNameAndValueList;
        _associationQualifierNameAndValueList =
            associationQualifierNameAndValueList;

        _hset = new HashSet();

        // _logger.debug("Type 2 SearchByAssociationIteratorDecorator ");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator
     * #get(int, int)
     */
    public ResolvedConceptReferenceList get(int arg0, int arg1)
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

        try {
            return doGetNext();
        } catch (Exception e) {
            throw new LBResourceUnavailableException(e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator
     * #next(int)
     */
    public ResolvedConceptReferenceList next(int page)
            throws LBResourceUnavailableException, LBInvocationException {
        // long startTime = System.currentTimeMillis();
        ResolvedConceptReferenceList returnList =
            new ResolvedConceptReferenceList();

        // _logger.debug("next method: getResolvedConceptReferenceCount() " +
        // returnList.getResolvedConceptReferenceCount());
        // _logger.debug("next method: page " + page);

        while (returnList.getResolvedConceptReferenceCount() < page
            && hasNext()) {
            try {
                returnList.addResolvedConceptReference(doGetNext());
            } catch (Exception e) {
                throw new RuntimeException(e);
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
    public ResolvedConceptReferencesIterator scroll(int arg0)
            throws LBResourceUnavailableException, LBInvocationException {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.Utility.Iterators.EntityListIterator#hasNext()
     */
    public boolean hasNext() throws LBResourceUnavailableException {
        try {
            pageIfNecessary();
        } catch (Exception e) {
            throw new LBResourceUnavailableException(e.getMessage());
        }
        return _currentChildren.size() > 0;
    }

    /**
     * Gets the number remaining in this Iterator.
     * 
     * NOTE: This is not an exact number. The Iterator is guarenteed to have AT
     * LEAST this amount remaining -- it may actually have more.
     */
    public int numberRemaining() throws LBResourceUnavailableException {
        // _logger.debug("SearchByAssociationIteratorDecorator: calling numberRemaining()	");
        try {
            pageIfNecessary();
        } catch (Exception e) {
            throw new LBResourceUnavailableException(e.getMessage());
        }

        // _logger.debug("SearchByAssociationIteratorDecorator: quickIterator.numberRemaining(): "
        // + quickIterator.numberRemaining());
        // _logger.debug("SearchByAssociationIteratorDecorator: currentChildren.size(): "
        // + currentChildren.size());

        // int total = quickIterator.numberRemaining() +
        // currentChildren.size();
        int total = _currentChildren.size();
        // _logger.debug("SearchByAssociationIteratorDecorator: total: " +
        // total);

        // return quickIterator.numberRemaining() +
        // currentChildren.size();
        return _currentChildren.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.Utility.Iterators.EntityListIterator#release()
     */
    public void release() throws LBResourceUnavailableException {
        _quickIterator.release();
    }

    /**
     * Do get next.
     * 
     * @return the resolved concept reference
     * 
     * @throws Exception the exception
     */
    protected ResolvedConceptReference doGetNext() throws Exception {
        pageIfNecessary();
        ResolvedConceptReference returnRef =
            _currentChildren.get(_position);

        // _logger.debug("doGetNext() position: " + position);
        displayRef(returnRef);

        _position++;
        return returnRef;
    }

    /**
     * Page if necessary.
     * 
     * @throws Exception the exception
     */
    protected void pageIfNecessary() throws Exception {

        // _logger.debug("pageIfNecessary ...");

        LexBIGService lbs = RemoteServerUtil.createLexBIGService();

        // _logger.debug("position: " + position +
        // " ----------- currentChildren.size: " + currentChildren.size());

        if (_position == _currentChildren.size()) {
            _currentChildren.clear();
            _position = 0;

            // [#26965] Contains Relationship search returns invalid result
            // if (quickIterator.hasNext()) {

            while (_quickIterator.hasNext()
                && _currentChildren.size() == 0) {
                // while (quickIterator.hasNext()) {
                ResolvedConceptReference ref = _quickIterator.next();
                if (ref != null) {
                    // KLO
                    String formalName = ref.getCodingSchemeName();
                    CodedNodeGraph cng =
                        lbs.getNodeGraph(formalName, null, null);

                    if (_associationNameAndValueList != null) {
                        cng =
                            cng.restrictToAssociations(
                                _associationNameAndValueList,
                                _associationQualifierNameAndValueList);
                    }

                    ResolvedConceptReferenceList list =
                        cng.resolveAsList(Constructors.createConceptReference(
                            ref.getCode(), ref.getCodingSchemeName()),
                            _resolveForward, _resolveBackward, 0,
                            _resolveAssociationDepth, null, null, null,
                            _maxToReturn);

                    // _logger.debug("Calling populateCurrentChildren ...");
                    // populateCurrentChildren(list.getResolvedConceptReference(),
                    // false);
                    populateCurrentChildren(list.getResolvedConceptReference(),
                        false);

                }
            }
        }
        // _logger.debug("Exiting pageIfNecessary(): currentChildren.size() "
        // + currentChildren.size());

    }

    protected void displayRef(ResolvedConceptReference ref) {
        // _logger.debug(ref.getConceptCode() + ":" +
        // ref.getEntityDescription().getContent());
    }

    protected void displayRef(String msg, ResolvedConceptReference ref) {
        // _logger.debug(msg + " " + ref.getConceptCode() + ":" +
        // ref.getEntityDescription().getContent());
    }

    /**
     * Populate current children.
     * 
     * @param list the list
     */
    // [#26965] Contains Relationship search returns invalid result
    public void populateCurrentChildren(ResolvedConceptReference[] list,
        boolean addRoot) {
        if (list == null)
            return;

        for (ResolvedConceptReference ref : list) {

            displayRef("Root: ", ref);

            if (addRoot) {
                if (!_hset.contains(ref.getConceptCode())) {
                    _hset.add(ref.getConceptCode());
                    // _logger.debug("\tbefore addRoot currentChildren.size() "
                    // + currentChildren.size());
                    displayRef(ref);
                    _currentChildren.add(ref);
                    // _logger.debug("\tafter addRoot currentChildren.size() "
                    // + currentChildren.size());
                }
            } else {
                // _logger.debug("\tDO NOT add: ");
                displayRef("discarded ", ref);
            }

            if (ref.getSourceOf() != null) {
                if (ref.getSourceOf().getAssociation() != null) {
                    for (Association assoc : ref.getSourceOf().getAssociation()) {
                        populateCurrentChildren(assoc.getAssociatedConcepts()
                            .getAssociatedConcept(), true);
                    }
                }
            } else {
                // _logger.debug("\tref.getSourceOf() == null -- nothing done.");
            }

            if (ref.getTargetOf() != null) {
                if (ref.getTargetOf().getAssociation() != null) {
                    for (Association assoc : ref.getTargetOf().getAssociation()) {
                        populateCurrentChildren(assoc.getAssociatedConcepts()
                            .getAssociatedConcept(), true);
                    }
                }
            } else {
                // _logger.debug("\tref.getTargetOf() == null -- nothing done.");
            }
        }

        // _logger.debug("\tExiting populateCurrentChildren");
    }

}
