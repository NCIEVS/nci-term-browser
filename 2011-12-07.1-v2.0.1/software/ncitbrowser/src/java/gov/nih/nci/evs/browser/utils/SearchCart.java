package gov.nih.nci.evs.browser.utils;

import java.util.ArrayList;
import java.util.Vector;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.SupportedHierarchy;

import org.apache.log4j.Logger;

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
 * Search utility for the cart
 *
 * @author garciawa2
 */
public class SearchCart {

    // Local variables
    private static Logger _logger = Logger.getLogger(SearchUtils.class);
    private static LexBIGService lbSvc = null;
    private static LexBIGServiceConvenienceMethods lbscm = null;

    // Local constants
    private static final int RESOLVEASSOCIATIONDEPTH = 1;
    private static final int MAXTORETURN = 1000;
    private static final String LB_EXTENSION = "LexBIGServiceConvenienceMethods";
    private static enum Direction { 
    	FORWARD, REVERSE;    	
    	public boolean test() {
    		if (ordinal() == FORWARD.ordinal()) return true;
    		return false;
    	}
    }
   
    /**
     * Constructor
     * @throws LBException 
     */
	public SearchCart() throws LBException {
		// Setup lexevs service
		if (lbSvc == null) {
			lbSvc = RemoteServerUtil.createLexBIGService();
		}
		// Setup lexevs generic extension
		lbscm = (LexBIGServiceConvenienceMethods) lbSvc
				.getGenericExtension(LB_EXTENSION);
		lbscm.setLexBIGService(lbSvc);
	}

    /**
     * Get concept Entity by code
     * @param codingScheme
     * @param code
     * @return
     */
    public ResolvedConceptReference getConceptByCode(String codingScheme, String version,
    		String code) {
        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;

        try {
            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            if (version != null) csvt.setVersion(version);
        	
            cns = lbSvc.getCodingSchemeConcepts(codingScheme, csvt);
            ConceptReferenceList crefs =
                createConceptReferenceList(new String[] { code }, codingScheme);
            cns.restrictToCodes(crefs);
            iterator = cns.resolve(null, null, null);
            if (iterator.numberRemaining() > 0) {
                ResolvedConceptReference ref = (ResolvedConceptReference) iterator.next();
                return ref;
            }
        } catch (LBException e) {
            _logger.info("Error: " + e.getMessage());
        }

        return null;
    }

    /**
     * Return list of Presentations
     * @param ref
     * @return
     */
    public Property[] getPresentationValues(ResolvedConceptReference ref) {
        return returnProperties(ref.getReferencedEntry().getPresentation());
    }

    /**
     * Return list of Definitions
     * @param ref
     * @return
     */
    public Property[] getDefinitionValues(ResolvedConceptReference ref) {
        return returnProperties(ref.getReferencedEntry().getDefinition());
    }

    /**
     * Return list of Properties
     * @param ref
     * @return
     */
    public Property[] getPropertyValues(ResolvedConceptReference ref) {
        return returnProperties(ref.getReferencedEntry().getProperty());
    }

    /**
     * Returns list of Parent Concepts
     * @param ref
     * @return
     * @throws LBException 
     */
    public Vector<Entity> getParentConcepts(ResolvedConceptReference ref) throws Exception {    	
        String scheme = ref.getCodingSchemeName();
        String version = ref.getCodingSchemeVersion();
        String code = ref.getCode();        
        Direction dir = getCodingSchemeDirection(ref);        
        Vector<String> assoNames = getAssociationNames(scheme, version);
        Vector<Entity> superconcepts = getAssociatedConcepts(scheme, version,
                code, assoNames, dir.test());       
        return superconcepts;
    }

    /**
     * Returns list of Child Concepts
     * @param ref
     * @return
     */
    public Vector<Entity> getChildConcepts(ResolvedConceptReference ref) throws Exception {
        String scheme = ref.getCodingSchemeName();
        String version = ref.getCodingSchemeVersion();
        String code = ref.getCode();
        Direction dir = getCodingSchemeDirection(ref);
        Vector<String> assoNames = getAssociationNames(scheme, version);
        Vector<Entity> supconcepts = getAssociatedConcepts(scheme, version,
                code, assoNames, !dir.test());
        return supconcepts;
    }

    /**
     * Returns Associated Concepts
     *
     * @param scheme
     * @param version
     * @param code
     * @param assocName
     * @param forward
     * @return
     */
    public Vector<Entity> getAssociatedConcepts(String scheme, String version,
            String code, Vector<String> assocNames, boolean forward) {

            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            if (version != null) csvt.setVersion(version);
            boolean resolveForward = true;
            boolean resolveBackward = false;

            // Set backward direction
            if (!forward) {
                resolveForward = false;
                resolveBackward = true;
            }

            Vector<Entity> v = new Vector<Entity>();

            try {

                CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);

                // Restrict coded node graph to the given association
                NameAndValueList nameAndValueList = createNameAndValueList(
                        assocNames, null);
                cng = cng.restrictToAssociations(nameAndValueList, null);

                ConceptReference graphFocus =
                    ConvenienceMethods.createConceptReference(code, scheme);

                ResolvedConceptReferencesIterator iterator =
                    codedNodeGraph2CodedNodeSetIterator(cng, graphFocus,
                        resolveForward, resolveBackward, RESOLVEASSOCIATIONDEPTH,
                        MAXTORETURN);
                v = resolveIterator(iterator, MAXTORETURN, code);

            } catch (Exception ex) {
                _logger.warn(ex.getMessage());
            }
            return v;
        }

    /**
     * Resolve the Iterator
     *
     * @param iterator
     * @param maxToReturn
     * @param code
     * @return
     */
    public Vector<Entity> resolveIterator(
            ResolvedConceptReferencesIterator iterator, int maxToReturn,
            String code) {

        Vector<Entity> v = new Vector<Entity>();

        if (iterator == null) {
            _logger.warn("No match.");
            return v;
        }
        try {
            int iteration = 0;
            while (iterator.hasNext()) {
                iteration++;
                iterator = iterator.scroll(maxToReturn);
                ResolvedConceptReferenceList rcrl = iterator.getNext();
                ResolvedConceptReference[] rcra = rcrl
                        .getResolvedConceptReference();
                for (int i = 0; i < rcra.length; i++) {
                    ResolvedConceptReference rcr = rcra[i];
                    Entity ce = rcr.getReferencedEntry();
                    if (code == null) {
                        v.add(ce);
                    } else {
                        if (ce.getEntityCode().compareTo(code) != 0)
                            v.add(ce);
                    }
                }
            }
        } catch (Exception e) {
            _logger.warn(e.getMessage());
        }
        return v;
    }

    /**
     * Return Iterator for codedNodeGraph
     *
     * @param cng
     * @param graphFocus
     * @param resolveForward
     * @param resolveBackward
     * @param resolveAssociationDepth
     * @param maxToReturn
     * @return
     */
    public ResolvedConceptReferencesIterator codedNodeGraph2CodedNodeSetIterator(
            CodedNodeGraph cng, ConceptReference graphFocus,
            boolean resolveForward, boolean resolveBackward,
            int resolveAssociationDepth, int maxToReturn) {
        CodedNodeSet cns = null;

        try {
            cns = cng.toNodeList(graphFocus, resolveForward, resolveBackward,
                    resolveAssociationDepth, maxToReturn);
            if (cns == null) return null;
            return cns.resolve(null, null, null);
        } catch (Exception ex) {
            _logger.warn(ex.getMessage());
        }

        return null;
    }

    /**
     * Return a list of Association names
     *
     * @param scheme
     * @param version
     * @return
     */
    public Vector<String> getAssociationNames(String scheme, String version) {
        Vector<String> association_vec = new Vector<String>();
        try {
            CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
            versionOrTag.setVersion(version);
            CodingScheme cs = lbSvc.resolveCodingScheme(scheme, versionOrTag);

            SupportedHierarchy[] hierarchies = cs.getMappings().getSupportedHierarchy();
            String[] ids = hierarchies[0].getAssociationNames();
            for (int i = 0; i < ids.length; i++) {
                if (!association_vec.contains(ids[i])) {
                    association_vec.add(ids[i]);
                    _logger.debug("AssociationName: " + ids[i]);
                }
            }
        } catch (Exception ex) {
            _logger.warn(ex.getMessage());
        }
        return association_vec;
    }

    /**
     * Return list of Comments
     * @param ref
     * @return
     */
    public Property[] getCommentValues(ResolvedConceptReference ref) {
        return returnProperties(ref.getReferencedEntry().getComment());
    }
    
    /**
     * Determine direction of Coding Scheme
     * 
     * @param ref
     * @return
     * @throws LBException
     */
    public Direction getCodingSchemeDirection(ResolvedConceptReference ref)
        throws Exception {
    	
    	Direction direction = Direction.FORWARD;
    	
    	// Create a version object
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(ref.getCodingSchemeVersion());    	
    	
        // Get Coding Scheme
    	CodingScheme cs = lbSvc.resolveCodingScheme(ref.getCodingSchemeName(), versionOrTag);
    	if (cs == null) {
    		throw new Exception("getTreeDirection(): CodingScheme is null!");
    	}

    	// Get hierarchy
        SupportedHierarchy[] hierarchies = cs.getMappings().getSupportedHierarchy();   	
        if (hierarchies == null || hierarchies.length < 1) {
        	throw new Exception("getTreeDirection(): hierarchies is null!");
        }    
        
        if (hierarchies[0].isIsForwardNavigable())
        	direction = Direction.REVERSE;
        else
        	direction = Direction.FORWARD;

        _logger.debug("getTreeDirection() = " + direction);

        return direction;
    }    
    
    /**
     * Returns the coding scheme's URI
     * @param scheme
     * @param version
     * @return
     * @throws Exception
     */
    public String getSchemeURI(String scheme, String version) throws Exception {    
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(version);
        CodingScheme cs = lbSvc.resolveCodingScheme(scheme, versionOrTag);    
    	return cs.getCodingSchemeURI();
    }    

    /**
     * Returns the coding scheme production version
     * @param scheme
     * @param version
     * @return
     * @throws Exception
     */
    public String getDefaultSchemeVersion(String scheme) throws Exception {    
        CodingScheme cs = lbSvc.resolveCodingScheme(scheme,null);    
    	return cs.getRepresentsVersion();
    }   
    
    /**
     * Returns list of all versions associated with a scheme
     * @param uri
     * @return
     * @throws Exception
     */
    public ArrayList<String> getSchemeVersions(String uri) throws Exception { 
    	ArrayList<String> list = new ArrayList<String>();

        CodingSchemeRenderingList csrl = lbSvc.getSupportedCodingSchemes();
        CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();

        for (int i = 0; i < csrs.length; i++) {
            CodingSchemeRendering csr = csrs[i];
            String status = csr.getRenderingDetail().getVersionStatus().value(); 
            CodingSchemeSummary css = csr.getCodingSchemeSummary();
            if (status.equals("active")) {      
	            if (css.getCodingSchemeURI().equals(uri)) 
	            	list.add(css.getRepresentsVersion());
            }
        }
    	
    	return list;
    }    
    
    // -----------------------------------------------------
    // Internal utility methods
    // -----------------------------------------------------

    /**
     * Return a NameAndValueList from two vectors
     * @param names
     * @param values
     * @return
     */
    private static NameAndValueList createNameAndValueList(Vector<String> names,
            Vector<String> values) {
        NameAndValueList nvList = new NameAndValueList();

        for (int i = 0; i < names.size(); i++) {
            NameAndValue nv = new NameAndValue();
            nv.setName(names.elementAt(i));
            if (values != null) {
                nv.setContent(values.elementAt(i));
            }
            nvList.addNameAndValue(nv);
        }
        return nvList;
    }    
    
    /**
     * @param properties
     * @return
     */
    private Property[] returnProperties(Property[] properties) {
        if (properties == null)
            return new Property[0]; // return empty list
        return properties;
    }

    /**
     * @param codes
     * @param codingSchemeName
     * @return
     */
    private ConceptReferenceList createConceptReferenceList(String[] codes,
            String codingSchemeName) {
        if (codes == null)
            return null;
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < codes.length; i++) {
            ConceptReference cr = new ConceptReference();
            cr.setCodingSchemeName(codingSchemeName);
            cr.setConceptCode(codes[i]);
            list.addConceptReference(cr);
        }
        return list;
    }

} // End of SearchCart
