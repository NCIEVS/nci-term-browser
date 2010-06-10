package gov.nih.nci.evs.browser.utils;

import java.util.*;
import java.sql.*;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Impl.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.LexBIG.DataModel.Core.types.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.naming.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.commonTypes.*;

import gov.nih.nci.evs.browser.properties.*;
import gov.nih.nci.evs.browser.common.*;

import org.apache.commons.codec.language.*;
import org.apache.log4j.*;

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

public class SearchUtils {
    private static Logger _logger = Logger.getLogger(SearchUtils.class);

    private DoubleMetaphone doubleMetaphone = null;

    private int max_str_length = 1000;
    private int penalty_multiplier_1 = 1;
    private int penalty_multiplier_2 = 2;

    private boolean apply_sort_score = false;
    private boolean sort_by_pt_only = true;

    Connection con;
    Statement stmt;
    ResultSet rs;

    private static org.LexGrid.LexBIG.LexBIGService.LexBIGService lbSvc = null;
    public org.LexGrid.LexBIG.Utility.ConvenienceMethods lbConvMethods = null;
    public CodingSchemeRenderingList csrl = null;
    private static HashMap codingSchemeMap = null;
    private static HashMap csnv2codingSchemeNameMap = null;
    private static HashMap csnv2VersionMap = null;
    private static String url = null;

    public static int ALL = 0;
    public static int PREFERRED_ONLY = 1;
    public static int NON_PREFERRED_ONLY = 2;

    static int RESOLVE_SOURCE = 1;
    static int RESOLVE_TARGET = -1;
    static int RESTRICT_SOURCE = -1;
    static int RESTRICT_TARGET = 1;

    public static final int SEARCH_NAME_CODE = 1;
    public static final int SEARCH_DEFINITION = 2;

    public static final int SEARCH_PROPERTY_VALUE = 3;
    public static final int SEARCH_ROLE_VALUE = 6;
    public static final int SEARCH_ASSOCIATION_VALUE = 7;

    static final List<String> STOP_WORDS =
        Arrays.asList(new String[] { "a", "an", "and", "by", "for", "of", "on",
            "in", "nos", "the", "to", "with" });

    static HashMap propertyLocalNameListHashMap = null;

    public enum NameSearchType {
        All, Name, Code
    };

    public SearchUtils() {
        initializeSortParameters();
    }

    public SearchUtils(String url) {
        SearchUtils.url = url;
        initializeSortParameters();
    }

    private void initializeSortParameters() {
        propertyLocalNameListHashMap = new HashMap();
        try {
            NCItBrowserProperties properties =
                NCItBrowserProperties.getInstance();
            String sort_str =
                properties.getProperty(NCItBrowserProperties.SORT_BY_SCORE);
            sort_by_pt_only = true;
            apply_sort_score = true;
            if (sort_str != null) {
                if (sort_str.compareTo("false") == 0) {
                    apply_sort_score = false;
                } else if (sort_str.compareToIgnoreCase("all") == 0) {
                    sort_by_pt_only = false;
                }
            }

        } catch (Exception ex) {

        }
    }

    private static void setCodingSchemeMap() {
        codingSchemeMap = new HashMap();
        csnv2codingSchemeNameMap = new HashMap();
        csnv2VersionMap = new HashMap();

        try {
            // RemoteServerUtil rsu = new RemoteServerUtil();
            // EVSApplicationService lbSvc = rsu.createLexBIGService();
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodingSchemeRenderingList csrl = lbSvc.getSupportedCodingSchemes();
            if (csrl == null)
                _logger.warn("csrl is NULL");

            CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
            for (int i = 0; i < csrs.length; i++) {
                CodingSchemeRendering csr = csrs[i];
                Boolean isActive =
                    csr.getRenderingDetail().getVersionStatus().equals(
                        CodingSchemeVersionStatus.ACTIVE);
                // if (isActive != null && isActive.equals(Boolean.TRUE))
                {
                    CodingSchemeSummary css = csr.getCodingSchemeSummary();
                    String formalname = css.getFormalName();
                    String representsVersion = css.getRepresentsVersion();
                    CodingSchemeVersionOrTag vt =
                        new CodingSchemeVersionOrTag();
                    vt.setVersion(representsVersion);

                    CodingScheme scheme = null;
                    try {
                        scheme = lbSvc.resolveCodingScheme(formalname, vt);
                        if (scheme != null) {
                            codingSchemeMap.put((Object) formalname,
                                (Object) scheme);

                            String value =
                                formalname + " (version: " + representsVersion
                                    + ")";
                            // _logger.debug(value);

                            csnv2codingSchemeNameMap.put(value, formalname);
                            csnv2VersionMap.put(value, representsVersion);

                        }

                    } catch (Exception e) {
                        String urn = css.getCodingSchemeURI();
                        try {
                            scheme = lbSvc.resolveCodingScheme(urn, vt);
                            if (scheme != null) {
                                codingSchemeMap.put((Object) formalname,
                                    (Object) scheme);

                                String value =
                                    formalname + " (version: "
                                        + representsVersion + ")";
                                // _logger.debug(value);

                                csnv2codingSchemeNameMap.put(value, formalname);
                                csnv2VersionMap.put(value, representsVersion);

                            }

                        } catch (Exception ex) {

                            String localname = css.getLocalName();
                            try {
                                scheme =
                                    lbSvc.resolveCodingScheme(localname, vt);
                                if (scheme != null) {
                                    codingSchemeMap.put((Object) formalname,
                                        (Object) scheme);

                                    String value =
                                        formalname + " (version: "
                                            + representsVersion + ")";
                                    // _logger.debug(value);

                                    csnv2codingSchemeNameMap.put(value,
                                        formalname);
                                    csnv2VersionMap.put(value,
                                        representsVersion);

                                }
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vector getSuperconceptCodes(String scheme, String version,
        String code) { // throws LBException{
        long ms = System.currentTimeMillis();
        Vector v = new Vector();
        try {
            // EVSApplicationService lbSvc = new
            // RemoteServerUtil().createLexBIGService(this.url);
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            LexBIGServiceConvenienceMethods lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);
            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(version);
            String desc = null;
            try {
                desc =
                    lbscm
                        .createCodeNodeSet(new String[] { code }, scheme, csvt)
                        .resolveToList(null, null, null, 1)
                        .getResolvedConceptReference(0).getEntityDescription()
                        .getContent();
            } catch (Exception e) {
                desc = "<not found>";
            }

            // Iterate through all hierarchies and levels ...
            String[] hierarchyIDs = lbscm.getHierarchyIDs(scheme, csvt);
            for (int k = 0; k < hierarchyIDs.length; k++) {
                String hierarchyID = hierarchyIDs[k];
                AssociationList associations =
                    lbscm.getHierarchyLevelPrev(scheme, csvt, hierarchyID,
                        code, false, null);
                for (int i = 0; i < associations.getAssociationCount(); i++) {
                    Association assoc = associations.getAssociation(i);
                    AssociatedConceptList concepts =
                        assoc.getAssociatedConcepts();
                    for (int j = 0; j < concepts.getAssociatedConceptCount(); j++) {
                        AssociatedConcept concept =
                            concepts.getAssociatedConcept(j);
                        String nextCode = concept.getConceptCode();
                        v.add(nextCode);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            _logger
                .debug("Run time (ms): " + (System.currentTimeMillis() - ms));
        }
        return v;
    }

    public Vector getSuperconceptCodes_Local(String scheme, String version,
        String code) { // throws LBException{
        long ms = System.currentTimeMillis();
        Vector v = new Vector();
        try {
            LexBIGService lbSvc = new LexBIGServiceImpl();
            LexBIGServiceConvenienceMethods lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);
            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(version);
            String desc = null;
            try {
                desc =
                    lbscm
                        .createCodeNodeSet(new String[] { code }, scheme, csvt)
                        .resolveToList(null, null, null, 1)
                        .getResolvedConceptReference(0).getEntityDescription()
                        .getContent();
            } catch (Exception e) {
                desc = "<not found>";
            }

            // Iterate through all hierarchies and levels ...
            String[] hierarchyIDs = lbscm.getHierarchyIDs(scheme, csvt);
            for (int k = 0; k < hierarchyIDs.length; k++) {
                String hierarchyID = hierarchyIDs[k];
                AssociationList associations =
                    lbscm.getHierarchyLevelPrev(scheme, csvt, hierarchyID,
                        code, false, null);
                for (int i = 0; i < associations.getAssociationCount(); i++) {
                    Association assoc = associations.getAssociation(i);
                    AssociatedConceptList concepts =
                        assoc.getAssociatedConcepts();
                    for (int j = 0; j < concepts.getAssociatedConceptCount(); j++) {
                        AssociatedConcept concept =
                            concepts.getAssociatedConcept(j);
                        String nextCode = concept.getConceptCode();
                        v.add(nextCode);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            _logger
                .debug("Run time (ms): " + (System.currentTimeMillis() - ms));
        }
        return v;
    }

    public Vector getHierarchyAssociationId(String scheme, String version) {

        Vector association_vec = new Vector();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();

            // Will handle secured ontologies later.
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            versionOrTag.setVersion(version);
            CodingScheme cs = lbSvc.resolveCodingScheme(scheme, versionOrTag);
            Mappings mappings = cs.getMappings();
            SupportedHierarchy[] hierarchies = mappings.getSupportedHierarchy();
            java.lang.String[] ids = hierarchies[0].getAssociationNames();

            for (int i = 0; i < ids.length; i++) {
                if (!association_vec.contains(ids[i])) {
                    association_vec.add(ids[i]);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return association_vec;
    }

    /*
     * public static String getVocabularyVersionByTag(String codingSchemeName,
     * String ltag) { if (codingSchemeName == null) return null; try {
     * //EVSApplicationService lbSvc = new
     * RemoteServerUtil().createLexBIGService(); LexBIGService lbSvc =
     * RemoteServerUtil.createLexBIGService(); CodingSchemeRenderingList lcsrl =
     * lbSvc.getSupportedCodingSchemes(); CodingSchemeRendering[] csra =
     * lcsrl.getCodingSchemeRendering(); for (int i = 0; i < csra.length; i++) {
     * CodingSchemeRendering csr = csra[i]; CodingSchemeSummary css =
     * csr.getCodingSchemeSummary(); if
     * (css.getFormalName().compareTo(codingSchemeName) == 0 ||
     * css.getLocalName().compareTo(codingSchemeName) == 0) { if (ltag == null)
     * return css.getRepresentsVersion(); RenderingDetail rd =
     * csr.getRenderingDetail(); CodingSchemeTagList cstl = rd.getVersionTags();
     * java.lang.String[] tags = cstl.getTag(); for (int j = 0; j < tags.length;
     * j++) { String version_tag = (String) tags[j]; if
     * (version_tag.compareToIgnoreCase(ltag) == 0) { return
     * css.getRepresentsVersion(); } } } } } catch (Exception e) {
     * //e.printStackTrace(); _logger.error("Version corresponding to tag " +
     * ltag + " is not found " + " in " + codingSchemeName); } return null; }
     */

    public static String getVocabularyVersionByTag(String codingSchemeName,
        String ltag) {
        if (codingSchemeName == null)
            return null;
        String version = null;
        int knt = 0;
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodingSchemeRenderingList lcsrl = lbSvc.getSupportedCodingSchemes();
            CodingSchemeRendering[] csra = lcsrl.getCodingSchemeRendering();
            for (int i = 0; i < csra.length; i++) {
                CodingSchemeRendering csr = csra[i];
                CodingSchemeSummary css = csr.getCodingSchemeSummary();
                if (css.getFormalName().compareTo(codingSchemeName) == 0
                    || css.getLocalName().compareTo(codingSchemeName) == 0) {
                    version = css.getRepresentsVersion();
                    knt++;

                    if (ltag == null)
                        return css.getRepresentsVersion();
                    RenderingDetail rd = csr.getRenderingDetail();
                    CodingSchemeTagList cstl = rd.getVersionTags();
                    java.lang.String[] tags = cstl.getTag();

                    for (int j = 0; j < tags.length; j++) {
                        String version_tag = (String) tags[j];

                        if (version_tag.compareToIgnoreCase(ltag) == 0) {
                            return css.getRepresentsVersion();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ltag == null
            || (ltag.compareToIgnoreCase("PRODUCTION") == 0 & knt == 1)) {
            _logger.warn("\tUse " + version + " as default version.");
            return version;
        }
        return null;
    }

    public static Vector<String> getVersionListData(String codingSchemeName) {

        Vector<String> v = new Vector();
        try {
            RemoteServerUtil rsu = new RemoteServerUtil();
            // EVSApplicationService lbSvc = rsu.createLexBIGService();
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodingSchemeRenderingList csrl = lbSvc.getSupportedCodingSchemes();
            if (csrl == null)
                _logger.warn("csrl is NULL");

            CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
            for (int i = 0; i < csrs.length; i++) {
                CodingSchemeRendering csr = csrs[i];
                Boolean isActive =
                    csr.getRenderingDetail().getVersionStatus().equals(
                        CodingSchemeVersionStatus.ACTIVE);
                // if (isActive != null && isActive.equals(Boolean.TRUE))
                {
                    CodingSchemeSummary css = csr.getCodingSchemeSummary();
                    String formalname = css.getFormalName();
                    if (formalname.compareTo(codingSchemeName) == 0) {
                        String representsVersion = css.getRepresentsVersion();
                        v.add(representsVersion);
                    }
                }
            }
        } catch (Exception ex) {

        }
        return v;
    }

    protected static Association processForAnonomousNodes(Association assoc) {
        // clone Association except associatedConcepts
        Association temp = new Association();
        temp.setAssociatedData(assoc.getAssociatedData());
        temp.setAssociationName(assoc.getAssociationName());
        temp.setAssociationReference(assoc.getAssociationReference());
        temp.setDirectionalName(assoc.getDirectionalName());
        temp.setAssociatedConcepts(new AssociatedConceptList());

        for (int i = 0; i < assoc.getAssociatedConcepts()
            .getAssociatedConceptCount(); i++) {
            // Conditionals to deal with anonymous nodes and UMLS top nodes
            // "V-X"
            // The first three allow UMLS traversal to top node.
            // The last two are specific to owl anonymous nodes which can act
            // like false
            // top nodes.
            if (assoc.getAssociatedConcepts().getAssociatedConcept(i)
                .getReferencedEntry() != null
                && assoc.getAssociatedConcepts().getAssociatedConcept(i)
                    .getReferencedEntry().getIsAnonymous() != null
                && assoc.getAssociatedConcepts().getAssociatedConcept(i)
                    .getReferencedEntry().getIsAnonymous() != false
                && !assoc.getAssociatedConcepts().getAssociatedConcept(i)
                    .getConceptCode().equals("@")
                && !assoc.getAssociatedConcepts().getAssociatedConcept(i)
                    .getConceptCode().equals("@@")) {
                // do nothing
            } else {
                temp.getAssociatedConcepts().addAssociatedConcept(
                    assoc.getAssociatedConcepts().getAssociatedConcept(i));
            }
        }
        return temp;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static NameAndValueList createNameAndValueList(String[] names,
        String[] values) {
        NameAndValueList nvList = new NameAndValueList();
        for (int i = 0; i < names.length; i++) {
            NameAndValue nv = new NameAndValue();
            nv.setName(names[i]);
            if (values != null) {
                nv.setContent(values[i]);
            }
            nvList.addNameAndValue(nv);
        }
        return nvList;
    }

    public static LocalNameList vector2LocalNameList(Vector<String> v) {
        if (v == null)
            return null;
        LocalNameList list = new LocalNameList();
        for (int i = 0; i < v.size(); i++) {
            String vEntry = (String) v.elementAt(i);
            list.addEntry(vEntry);
        }
        return list;
    }

    protected static NameAndValueList createNameAndValueList(Vector names,
        Vector values) {
        if (names == null)
            return null;
        NameAndValueList nvList = new NameAndValueList();
        for (int i = 0; i < names.size(); i++) {
            String name = (String) names.elementAt(i);
            String value = (String) values.elementAt(i);
            NameAndValue nv = new NameAndValue();
            nv.setName(name);
            if (value != null) {
                nv.setContent(value);
            }
            nvList.addNameAndValue(nv);
        }
        return nvList;
    }

    protected ResolvedConceptReferencesIterator restrictToMatchingProperty(
        String codingSchemeName, String version, Vector property_vec,
        Vector source_vec, Vector qualifier_name_vec,
        Vector qualifier_value_vec, java.lang.String matchText,
        java.lang.String matchAlgorithm, java.lang.String language) {

        LocalNameList propertyList = vector2LocalNameList(property_vec);
        CodedNodeSet.PropertyType[] propertyTypes = null;
        LocalNameList sourceList = vector2LocalNameList(source_vec);

        NameAndValueList qualifierList =
            createNameAndValueList(qualifier_name_vec, qualifier_value_vec);

        return restrictToMatchingProperty(codingSchemeName, version,
            propertyList, propertyTypes, sourceList, qualifierList, matchText,
            matchAlgorithm, language);

    }

    protected ResolvedConceptReferencesIterator restrictToMatchingProperty(
        String codingSchemeName, String version,

        LocalNameList propertyList, CodedNodeSet.PropertyType[] propertyTypes,
        LocalNameList sourceList, NameAndValueList qualifierList,

        java.lang.String matchText, java.lang.String matchAlgorithm,
        java.lang.String language) {

        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;
        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            if (version != null)
                versionOrTag.setVersion(version);

            if (lbSvc == null) {
                _logger.warn("lbSvc = null");
                return null;
            }

            cns = lbSvc.getCodingSchemeConcepts(codingSchemeName, versionOrTag);
            if (cns == null) {
                _logger.debug("cns = null");
                return null;
            }

            LocalNameList contextList = null;
            try {
                cns =
                    cns.restrictToMatchingProperties(propertyList,
                        propertyTypes, sourceList, contextList, qualifierList,
                        matchText, matchAlgorithm, language);
            } catch (Exception ex) {

                return null;
            }

            LocalNameList restrictToProperties = new LocalNameList();
            // KLO, 030509
            SortOptionList sortCriteria = null;
            // Constructors.createSortOptionList(new String[]{"matchToQuery"});

            try {
                // resolve nothing unless sort_by_pt_only is set to false
                boolean resolveConcepts = false;
                if (apply_sort_score && !sort_by_pt_only)
                    resolveConcepts = true;
                try {

                    iterator =
                        cns.resolve(sortCriteria, null, restrictToProperties,
                            null, resolveConcepts);
                } catch (Exception e) {
                    _logger
                        .error("Method: SearchUtil.restrictToMatchingProperty");
                    _logger.error("* ERROR: cns.resolve throws exceptions.");
                    _logger.error("* " + e.getClass().getSimpleName() + ": "
                        + e.getMessage());
                }

            } catch (Exception ex) {
                // ex.printStackTrace();
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return iterator;
    }

    private boolean isNull(String s) {
        if (s == null)
            return true;
        s = s.trim();
        if (s.length() == 0)
            return true;
        if (s.compareTo("") == 0)
            return true;
        if (s.compareToIgnoreCase("null") == 0)
            return true;
        return false;
    }

    public static Concept getConceptByCode(String codingSchemeName,
        String vers, String ltag, String code) {
        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
            if (lbSvc == null) {
                _logger.warn("lbSvc == null???");
                return null;
            }

            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            versionOrTag.setVersion(vers);

            ConceptReferenceList crefs =
                createConceptReferenceList(new String[] { code },
                    codingSchemeName);

            CodedNodeSet cns = null;

            try {
                cns =
                    lbSvc.getCodingSchemeConcepts(codingSchemeName,
                        versionOrTag);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            cns = cns.restrictToCodes(crefs);
            ResolvedConceptReferenceList matches =
                cns.resolveToList(null, null, null, null, false, 1);

            if (matches == null) {
                _logger.warn("Concept not found.");
                return null;
            }

            // Analyze the result ...
            if (matches.getResolvedConceptReferenceCount() > 0) {
                ResolvedConceptReference ref =
                    (ResolvedConceptReference) matches
                        .enumerateResolvedConceptReference().nextElement();

                org.LexGrid.concepts.Concept entry =
                    new org.LexGrid.concepts.Concept();
                entry.setEntityCode(ref.getConceptCode());
                entry.setEntityDescription(ref.getEntityDescription());

                // Concept entry = ref.getReferencedEntry();
                return entry;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static ConceptReferenceList createConceptReferenceList(
        String[] codes, String codingSchemeName) {
        if (codes == null) {
            return null;
        }
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < codes.length; i++) {
            ConceptReference cr = new ConceptReference();
            cr.setCodingSchemeName(codingSchemeName);
            cr.setConceptCode(codes[i]);
            list.addConceptReference(cr);
        }
        return list;
    }

    public Vector getParentCodes(String scheme, String version, String code) {
        // SearchUtils util = new SearchUtils();
        Vector hierarchicalAssoName_vec =
            getHierarchyAssociationId(scheme, version);
        if (hierarchicalAssoName_vec == null
            || hierarchicalAssoName_vec.size() == 0) {
            return null;
        }
        String hierarchicalAssoName =
            (String) hierarchicalAssoName_vec.elementAt(0);
        Vector superconcept_vec =
            getAssociationSources(scheme, version, code, hierarchicalAssoName);
        if (superconcept_vec == null)
            return null;
        return superconcept_vec;

    }

    public ResolvedConceptReferenceList getNext(
        ResolvedConceptReferencesIterator iterator) {
        return iterator.getNext();
    }

    /**
     * Dump_matches to output, for debug purposes
     * 
     * @param iterator the iterator
     * @param maxToReturn the max to return
     */
    public static Vector resolveIterator(
        ResolvedConceptReferencesIterator iterator, int maxToReturn) {
        return resolveIterator(iterator, maxToReturn, null);
    }

    public static Vector resolveIterator(
        ResolvedConceptReferencesIterator iterator, int maxToReturn, String code) {
        Vector v = new Vector();
        if (iterator == null) {
            _logger.debug("No match.");
            return v;
        }
        try {
            int iteration = 0;

            while (iterator.hasNext()) {
                iteration++;
                iterator = iterator.scroll(maxToReturn);
                ResolvedConceptReferenceList rcrl = iterator.getNext();
                ResolvedConceptReference[] rcra =
                    rcrl.getResolvedConceptReference();
                for (int i = 0; i < rcra.length; i++) {
                    ResolvedConceptReference rcr = rcra[i];
                    // org.LexGrid.concepts.Concept ce =
                    // rcr.getReferencedEntry();
                    org.LexGrid.concepts.Concept ce =
                        new org.LexGrid.concepts.Concept();
                    ce.setEntityCode(rcr.getConceptCode());
                    ce.setEntityDescription(rcr.getEntityDescription());

                    if (code == null) {
                        v.add(ce);
                    } else {
                        if (ce.getEntityCode().compareTo(code) != 0)
                            v.add(ce);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    public ResolvedConceptReferencesIterator codedNodeGraph2CodedNodeSetIterator(
        CodedNodeGraph cng, ConceptReference graphFocus,
        boolean resolveForward, boolean resolveBackward,
        int resolveAssociationDepth, int maxToReturn) {
        CodedNodeSet cns = null;
        try {
            cns =
                cng.toNodeList(graphFocus, resolveForward, resolveBackward,
                    resolveAssociationDepth, maxToReturn);

            if (cns == null) {
                _logger.warn("cng.toNodeList returns null???");
                return null;
            }

            SortOptionList sortCriteria = null;
            // Constructors.createSortOptionList(new String[]{"matchToQuery",
            // "code"});

            LocalNameList propertyNames = null;
            CodedNodeSet.PropertyType[] propertyTypes = null;
            ResolvedConceptReferencesIterator iterator = null;
            try {
                iterator =
                    cns.resolve(sortCriteria, propertyNames, propertyTypes);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (iterator == null) {
                _logger.warn("cns.resolve returns null???");
            }
            return iterator;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Vector getSuperconcepts(String scheme, String version, String code) {
        // String assocName = "hasSubtype";
        String hierarchicalAssoName = "hasSubtype";
        Vector hierarchicalAssoName_vec =
            getHierarchyAssociationId(scheme, version);
        if (hierarchicalAssoName_vec != null
            && hierarchicalAssoName_vec.size() > 0) {
            hierarchicalAssoName =
                (String) hierarchicalAssoName_vec.elementAt(0);
        }
        return getAssociationSources(scheme, version, code,
            hierarchicalAssoName);
    }

    public Vector getAssociationSources(String scheme, String version,
        String code, String assocName) {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);
        ResolvedConceptReferenceList matches = null;
        Vector v = new Vector();
        try {
            // EVSApplicationService lbSvc = new
            // RemoteServerUtil().createLexBIGService();
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
            NameAndValueList nameAndValueList =
                createNameAndValueList(new String[] { assocName }, null);

            NameAndValueList nameAndValueList_qualifier = null;
            cng =
                cng.restrictToAssociations(nameAndValueList,
                    nameAndValueList_qualifier);
            ConceptReference graphFocus =
                ConvenienceMethods.createConceptReference(code, scheme);

            boolean resolveForward = false;
            boolean resolveBackward = true;

            int resolveAssociationDepth = 1;
            int maxToReturn = 1000;

            ResolvedConceptReferencesIterator iterator =
                codedNodeGraph2CodedNodeSetIterator(cng, graphFocus,
                    resolveForward, resolveBackward, resolveAssociationDepth,
                    maxToReturn);

            v = resolveIterator(iterator, maxToReturn, code);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return v;
    }

    public Vector getSubconcepts(String scheme, String version, String code) {
        // String assocName = "hasSubtype";
        String hierarchicalAssoName = "hasSubtype";
        Vector hierarchicalAssoName_vec =
            getHierarchyAssociationId(scheme, version);
        if (hierarchicalAssoName_vec != null
            && hierarchicalAssoName_vec.size() > 0) {
            hierarchicalAssoName =
                (String) hierarchicalAssoName_vec.elementAt(0);
        }
        return getAssociationTargets(scheme, version, code,
            hierarchicalAssoName);
    }

    public Vector getAssociationTargets(String scheme, String version,
        String code, String assocName) {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);
        ResolvedConceptReferenceList matches = null;
        Vector v = new Vector();
        try {
            // EVSApplicationService lbSvc = new
            // RemoteServerUtil().createLexBIGService();
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
            NameAndValueList nameAndValueList =
                createNameAndValueList(new String[] { assocName }, null);

            NameAndValueList nameAndValueList_qualifier = null;
            cng =
                cng.restrictToAssociations(nameAndValueList,
                    nameAndValueList_qualifier);
            ConceptReference graphFocus =
                ConvenienceMethods.createConceptReference(code, scheme);

            boolean resolveForward = true;
            boolean resolveBackward = false;

            int resolveAssociationDepth = 1;
            int maxToReturn = 1000;

            ResolvedConceptReferencesIterator iterator =
                codedNodeGraph2CodedNodeSetIterator(cng, graphFocus,
                    resolveForward, resolveBackward, resolveAssociationDepth,
                    maxToReturn);

            v = resolveIterator(iterator, maxToReturn, code);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return v;
    }

    // search for "lycogen"
    public static String preprocessContains(String s) {
        if (s == null)
            return null;
        if (s.length() == 0)
            return null;
        s = s.trim();
        List<String> words = toWords(s, "\\s", false, false);
        String delim = ".*";
        StringBuffer searchPhrase = new StringBuffer();

        int k = -1;
        searchPhrase.append(delim);

        for (int i = 0; i < words.size(); i++) {
            String wd = (String) words.get(i);
            wd = wd.trim();
            if (wd.compareTo("") != 0) {
                searchPhrase.append(wd);
                if (words.size() > 1 && i < words.size() - 1) {
                    searchPhrase.append("\\s");
                }
            }
        }
        searchPhrase.append(delim);
        String t = searchPhrase.toString();
        return t;
    }

    private boolean isNumber(String s) {
        if (s.length() == 0)
            return false;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i)))
                return false;
        }
        return true;
    }

    // /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 5.1 implementation
    // /////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ResolvedConceptReferencesIteratorWrapper searchByName(String scheme,
        String version, String matchText, String matchAlgorithm,
        boolean ranking, int maxToReturn) {
        return searchByName(scheme, version, matchText, null, matchAlgorithm,
            ranking, maxToReturn);
    }

    public ResolvedConceptReferencesIteratorWrapper searchByName(String scheme,
        String version, String matchText, String source, String matchAlgorithm,
        boolean ranking, int maxToReturn) {
        String matchText0 = matchText;
        String matchAlgorithm0 = matchAlgorithm;
        matchText0 = matchText0.trim();

        // boolean preprocess = true;
        if (matchText == null || matchText.length() == 0) {
            return null;
        }

        matchText = matchText.trim();
        if (matchAlgorithm.compareToIgnoreCase("contains") == 0) // p11.1-q11.1
                                                                 // /100{WBC}
        {
            // matchAlgorithm = Constants.CONTAIN_SEARCH_ALGORITHM; // to be
            // replace by literalSubString
            matchAlgorithm = findBestContainsAlgorithm(matchText);
        }

        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;
        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();

            if (lbSvc == null) {
                _logger.warn("lbSvc = null");
                return null;
            }

            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            if (version != null)
                versionOrTag.setVersion(version);
            cns = lbSvc.getNodeSet(scheme, versionOrTag, null);

            if (cns == null) {
                _logger.warn("cns = null");
                return null;
            }

            // LocalNameList contextList = null;
            try {
                cns =
                    cns.restrictToMatchingDesignations(matchText,
                        SearchDesignationOption.ALL, matchAlgorithm, null);
                cns = restrictToSource(cns, source);
            } catch (Exception ex) {
                return null;
            }

            LocalNameList restrictToProperties = new LocalNameList();
            // boolean resolveConcepts = true;
            // if (!ranking) resolveConcepts = false;
            boolean resolveConcepts = false;

            SortOptionList sortCriteria = null;

            if (ranking) {
                sortCriteria =
                    Constructors
                        .createSortOptionList(new String[] { "matchToQuery" });

            } else {
                sortCriteria =
                    Constructors
                        .createSortOptionList(new String[] { "entityDescription" }); // code
                _logger.debug("*** Sort alphabetically...");
                resolveConcepts = false;
            }
            try {
                try {
                    long ms = System.currentTimeMillis(), delay = 0;
                    iterator =
                        cns.resolve(sortCriteria, null, restrictToProperties,
                            null, resolveConcepts);
                    // Debug.println("cns.resolve delay ---- Run time (ms): " +
                    // (delay = System.currentTimeMillis() - ms) +
                    // " -- matchAlgorithm " + matchAlgorithm);
                    // DBG.debugDetails(delay, "cns.resolve",
                    // "searchByName, CodedNodeSet.resolve");

                } catch (Exception e) {
                    _logger.error("Method: SearchUtil.searchByName");
                    _logger.error("* ERROR: cns.resolve throws exceptions.");
                    _logger.error("* " + e.getClass().getSimpleName() + ": "
                        + e.getMessage());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (iterator == null) {
            _logger.warn("*** WARNING: iterator == null.." + scheme);
            iterator =
                matchConceptCode(scheme, version, matchText0, source,
                    "LuceneQuery");
        } else {
            try {
                int size = iterator.numberRemaining();
                if (size == 0) {
                    iterator =
                        matchConceptCode(scheme, version, matchText0, source,
                            "LuceneQuery");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ResolvedConceptReferencesIteratorWrapper(iterator);
    }

    public ResolvedConceptReferencesIteratorWrapper searchByName(
        Vector schemes, Vector versions, String matchText,
        String matchAlgorithm, boolean ranking, int maxToReturn) {
        return searchByName(schemes, versions, matchText, null, matchAlgorithm,
            ranking, maxToReturn);
    }

    public ResolvedConceptReferencesIteratorWrapper searchByName(
        Vector schemes, Vector versions, String matchText, String source,
        String matchAlgorithm, boolean ranking, int maxToReturn) {
        String matchText0 = matchText;
        String matchAlgorithm0 = matchAlgorithm;
        matchText0 = matchText0.trim();

        _logger.debug("searchByName ..." + matchText);

        long ms = System.currentTimeMillis(), delay = 0;
        long tnow = System.currentTimeMillis();
        long total_delay = 0;
        boolean debug_flag = false;

        // if (debug_flag)
        // _logger.debug("Entering SearchUtils searchByName ...");

        boolean preprocess = true;
        if (matchText == null || matchText.length() == 0) {
            return null;
        }

        matchText = matchText.trim();
        if (matchAlgorithm.compareToIgnoreCase("contains") == 0) // p11.1-q11.1
                                                                 // /100{WBC}
        {
            // matchAlgorithm = Constants.CONTAIN_SEARCH_ALGORITHM;
            matchAlgorithm = findBestContainsAlgorithm(matchText);
        }

        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;

        String scheme = null;
        String version = null;

        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();

            if (lbSvc == null) {
                _logger.warn("lbSvc = null");
                return null;
            }

            Vector cns_vec = new Vector();
            for (int i = 0; i < schemes.size(); i++) {

                cns = null;
                iterator = null;
                scheme = (String) schemes.elementAt(i);

                ms = System.currentTimeMillis();

                CodingSchemeVersionOrTag versionOrTag =
                    new CodingSchemeVersionOrTag();
                version = (String) versions.elementAt(i);
                if (version != null)
                    versionOrTag.setVersion(version);
                try {
                    if (lbSvc == null) {
                        _logger.warn("lbSvc = null");
                        return null;
                    }
                    cns = lbSvc.getNodeSet(scheme, versionOrTag, null);
                    if (cns != null) {
                        try {
                            cns =
                                cns.restrictToMatchingDesignations(matchText,
                                    null, matchAlgorithm, null);
                            cns = restrictToSource(cns, source);
                        } catch (Exception ex) {
                            return null;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // return null;
                }
                if (cns != null) {
                    cns_vec.add(cns);
                }

                delay = System.currentTimeMillis() - ms;
                if (debug_flag)
                    _logger.debug("Restricting CNS on " + scheme
                        + " delay (millisec.): " + delay);
                if (debug_flag)
                    System.out.flush();

            }

            iterator = null;
            /*
             * ms = System.currentTimeMillis(); cns = union(cns_vec); delay =
             * System.currentTimeMillis() - ms; if (debug_flag)
             * _logger.debug("CNS union " + "delay (millisec.): " + delay);
             * 
             * 
             * delay = System.currentTimeMillis() - ms; if (debug_flag)
             * _logger.debug("Restricting CNS on " + scheme +
             * " delay (millisec.): " + delay);
             * 
             * 
             * if (cns == null) return null;
             */

            LocalNameList restrictToProperties = new LocalNameList();
            // boolean resolveConcepts = true;
            // if (!ranking) resolveConcepts = false;
            boolean resolveConcepts = false;

            SortOptionList sortCriteria = null;

            if (ranking) {
                sortCriteria = null;// Constructors.createSortOptionList(new
                                    // String[]{"matchToQuery"});

            } else {
                sortCriteria =
                    Constructors
                        .createSortOptionList(new String[] { "entityDescription" }); // code
                _logger.debug("*** Sort alphabetically...");
                resolveConcepts = false;
            }
            try {
                try {
                    ms = System.currentTimeMillis();
                    if (debug_flag)
                        _logger
                            .debug("Calling  cns.resolve to resolve the union CNS ... ");
                    // iterator = cns.resolve(sortCriteria, null,
                    // restrictToProperties, null, resolveConcepts);
                    iterator =
                        new QuickUnionIterator(cns_vec, sortCriteria, null,
                            restrictToProperties, null, resolveConcepts);

                    delay = System.currentTimeMillis() - ms;
                    if (debug_flag)
                        _logger.debug("Resolve CNS union "
                            + "delay (millisec.): " + delay);

                    // Debug.println("cns.resolve delay ---- Run time (ms): " +
                    // (delay = System.currentTimeMillis() - ms) +
                    // " -- matchAlgorithm " + matchAlgorithm);
                    // DBG.debugDetails(delay, "cns.resolve",
                    // "searchByName, CodedNodeSet.resolve");

                } catch (Exception e) {
                    _logger.error("Method: SearchUtils.searchByName 2");
                    _logger.error("* ERROR: cns.resolve throws exceptions.");
                    _logger.error("* " + e.getClass().getSimpleName() + ": "
                        + e.getMessage());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        int lcv = 0;
        int iterator_size = 0;
        if (iterator != null) {
            try {
                iterator_size = iterator.numberRemaining();
                _logger.debug("Number of matches: " + iterator_size);
            } catch (Exception ex) {

            }
        }

        ms = System.currentTimeMillis();
        while (iterator_size == 0 && lcv < schemes.size()) {
            scheme = (String) schemes.elementAt(lcv);

            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            version = (String) versions.elementAt(lcv);
            if (version != null)
                versionOrTag.setVersion(version);
            iterator =
                matchConceptCode(scheme, version, matchText0, source,
                    "LuceneQuery");
            if (iterator != null) {
                try {
                    iterator_size = iterator.numberRemaining();
                    _logger.debug("Number of matches: " + iterator_size);
                } catch (Exception ex) {

                }
            }
            lcv++;
        }

        delay = System.currentTimeMillis() - ms;
        if (debug_flag)
            _logger
                .debug("Match concept code " + "delay (millisec.): " + delay);

        total_delay = System.currentTimeMillis() - tnow;
        _logger.debug("Total search delay: (millisec.): " + total_delay);

        // return iterator;
        return new ResolvedConceptReferencesIteratorWrapper(iterator);

    }

    public static ResolvedConceptReferencesIterator filterIterator(
        ResolvedConceptReferencesIterator iterator, String scheme,
        String version, String code) {
        if (iterator == null)
            return null;
        try {
            int num = iterator.numberRemaining();
            if (num <= 1)
                return iterator;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            while (iterator.hasNext()) {
                try {
                    ResolvedConceptReference[] refs =
                        iterator.next(100).getResolvedConceptReference();
                    for (ResolvedConceptReference ref : refs) {
                        if (ref.getConceptCode().compareTo(code) == 0) {
                            long ms0 = System.currentTimeMillis(), delay0 = 0;
                            LexBIGService lbSvc =
                                new RemoteServerUtil().createLexBIGService();
                            if (lbSvc == null) {
                                _logger.warn("lbSvc = null");
                                return null;
                            }
                            CodingSchemeVersionOrTag versionOrTag =
                                new CodingSchemeVersionOrTag();
                            if (version != null)
                                versionOrTag.setVersion(version);

                            ConceptReferenceList crefs =
                                createConceptReferenceList(
                                    new String[] { code }, scheme);

                            CodedNodeSet cns = null;
                            try {
                                try {
                                    cns =
                                        lbSvc.getNodeSet(scheme, versionOrTag,
                                            null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return null;
                                }

                                if (cns == null) {
                                    _logger
                                        .warn("getConceptByCode getCodingSchemeConcepts returns null??? "
                                            + scheme);
                                    return null;
                                }

                                cns = cns.restrictToCodes(crefs);
                                return cns.resolve(null, null, null);
                            } catch (Exception ex) {

                            }
                        }
                    }
                } catch (Exception ex) {
                    break;
                }
            }
        } catch (Exception ex) {

        }
        return null;
    }

    public static ResolvedConceptReferencesIterator matchConceptCode(
        String scheme, String version, String matchText, String source,
        String matchAlgorithm) {
        LexBIGService lbs = RemoteServerUtil.createLexBIGService();
        Vector v = new Vector();
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        if (version != null)
            versionOrTag.setVersion(version);
        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;
        try {
            cns = lbs.getNodeSet(scheme, versionOrTag, null);
            if (source != null)
                cns = restrictToSource(cns, source);
            CodedNodeSet.PropertyType[] propertyTypes = null;
            LocalNameList sourceList = null;
            LocalNameList contextList = null;
            NameAndValueList qualifierList = null;
            cns =
                cns.restrictToMatchingProperties(ConvenienceMethods
                    .createLocalNameList(new String[] { "conceptCode" }),
                    propertyTypes, sourceList, contextList, qualifierList,
                    matchText, matchAlgorithm, null);

            LocalNameList restrictToProperties = new LocalNameList();
            SortOptionList sortCriteria = null;
            try {
                boolean resolveConcepts = true;
                try {
                    long ms = System.currentTimeMillis(), delay = 0;
                    iterator =
                        cns.resolve(sortCriteria, null, restrictToProperties,
                            null, resolveConcepts);
                    if (iterator == null) {
                        return matchConceptCode(scheme, version, matchText);
                    }

                    int size = iterator.numberRemaining();
                    // test
                    if (size == 0) {
                        return matchConceptCode(scheme, version, matchText);
                    }

                } catch (Exception e) {
                    _logger.error("Method: SearchUtil.matchConceptCode");
                    _logger.error("* ERROR: cns.resolve throws exceptions.");
                    _logger.error("* " + e.getClass().getSimpleName() + ": "
                        + e.getMessage());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        } catch (Exception ex) {
            // _logger.error("WARNING: searchByCode throws exception.");
        }

        // return iterator;
        if (source == null || source.compareToIgnoreCase("ALL") == 0) {
            return filterIterator(iterator, scheme, version, matchText);
        }
        return iterator;
    }

    public static ResolvedConceptReferencesIterator matchConceptCode(
        String scheme, String version, String code) {
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        Vector v = new Vector();
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        if (version != null)
            versionOrTag.setVersion(version);
        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;
        try {
            cns = lbSvc.getNodeSet(scheme, versionOrTag, null);
            CodedNodeSet.PropertyType[] propertyTypes = null;
            // LocalNameList sourceList = null;
            // LocalNameList contextList = null;
            // NameAndValueList qualifierList = null;
            ConceptReferenceList crefs =
                createConceptReferenceList(new String[] { code }, scheme);
            try {
                cns = lbSvc.getCodingSchemeConcepts(scheme, versionOrTag);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            if (cns == null) {
                _logger
                    .warn("getConceptByCode getCodingSchemeConcepts returns null??? "
                        + scheme);
                return null;
            }

            cns = cns.restrictToCodes(crefs);
            LocalNameList restrictToProperties = new LocalNameList();
            SortOptionList sortCriteria = null;
            try {
                boolean resolveConcepts = true;
                try {
                    long ms = System.currentTimeMillis(), delay = 0;
                    iterator =
                        cns.resolve(sortCriteria, null, restrictToProperties,
                            null, resolveConcepts);

                    int size = iterator.numberRemaining();
                } catch (Exception e) {
                    _logger.error("Method: SearchUtil.matchConceptCode");
                    _logger.error("* ERROR: cns.resolve throws exceptions.");
                    _logger.error("* " + e.getClass().getSimpleName() + ": "
                        + e.getMessage());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        } catch (Exception ex) {
            _logger.error("WARNING: searchByCode throws exception.");
        }
        // [#26386] Need app to be able to distinguish/prioritize/display
        // matching code vrs. matching name results

        return filterIterator(iterator, scheme, version, code);
        // return iterator;
    }

    public static Concept matchConceptByCode(String scheme, String version,
        String matchText, String source, String matchAlgorithm) {
        ResolvedConceptReferencesIterator iterator =
            matchConceptCode(scheme, version, matchText, source, matchAlgorithm);
        if (iterator == null)
            return null;
        try {
            while (iterator.hasNext()) {
                /*
                 * ResolvedConceptReference[] refs =
                 * iterator.next(1).getResolvedConceptReference();
                 * for(ResolvedConceptReference ref : refs){ return
                 * ref.getReferencedEntry(); }
                 */
                ResolvedConceptReference[] refs =
                    iterator.next(100).getResolvedConceptReference();
                for (ResolvedConceptReference ref : refs) {
                    if (ref.getReferencedEntry().getEntityCode().equals(
                        matchText)) {
                        return ref.getReferencedEntry();
                    }
                }
            }
        } catch (Exception e) {
            _logger.error("Method: SearchUtil.matchConceptByCode");
            _logger.error("* ERROR: cns.resolve throws exceptions.");
            _logger.error("* " + e.getClass().getSimpleName() + ": "
                + e.getMessage());
        }
        return null;
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected static List<String> toWords(String s, String delimitRegex,
        boolean removeStopWords, boolean removeDuplicates) {
        String[] words = s.split(delimitRegex);
        List<String> adjusted = new ArrayList<String>();
        for (int i = 0; i < words.length; i++) {
            String temp = words[i].toLowerCase();
            if (removeDuplicates && adjusted.contains(temp))
                continue;
            if (!removeStopWords || !STOP_WORDS.contains(temp))
                adjusted.add(temp);
        }
        return adjusted;
    }

    protected static String[] toWords(String s, boolean removeStopWords) {
        String[] words = s.split("\\s");
        List<String> adjusted = new ArrayList<String>();
        for (int i = 0; i < words.length; i++) {
            if (!removeStopWords || !STOP_WORDS.contains(words[i]))
                adjusted.add(words[i].toLowerCase());
        }
        return adjusted.toArray(new String[adjusted.size()]);
    }

    protected static List<String> toWords2(String s, String delimitRegex,
        boolean removeStopWords, boolean removeDuplicates) {
        s = s.trim();
        s = replaceSpecialCharsWithBlankChar(s);
        List<String> adjusted = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(s);
        while (st.hasMoreTokens()) {
            String temp = st.nextToken().toLowerCase();
            if (removeDuplicates && adjusted.contains(temp))
                continue;
            if (!removeStopWords || !STOP_WORDS.contains(temp)) {
                adjusted.add(temp);
            }
        }
        return adjusted;
    }

    public static String preprocessSearchString(String s) {
        if (s == null)
            return null;
        if (s.length() == 0)
            return null;
        StringBuffer searchPhrase = new StringBuffer();
        List<String> words = toWords(s, "\\s", true, false);
        int k = -1;
        for (int i = 0; i < words.size(); i++) {
            String wd = (String) words.get(i);
            wd = wd.trim();
            if (wd.compareTo("") != 0) {
                k++;
                if (k > 0) {
                    searchPhrase.append(" ");
                }
                searchPhrase.append(wd);
            }
        }
        String t = searchPhrase.toString();
        return t;
    }

    public static boolean nonAlphabetic(String s) {
        if (s == null)
            return false;
        if (s.length() == 0)
            return true;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (Character.isLetter(ch))
                return false;
        }
        return true;
    }

    /*
     * private static String replaceSpecialChars(String s){ //String
     * escapedChars = "|!(){}[]^\"~*?:;-"; String escapedChars =
     * "|!(){}[]^\"~*?;-_"; for (int i=0; i<escapedChars.length(); i++) { char c
     * = escapedChars.charAt(i); s = s.replace(c, ' '); } return s; }
     */

    private static String escapeSpecialChars(String s, String specChars) {
        String escapeChar = "\\";
        StringBuffer regex = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (specChars.indexOf(c) != -1) {
                regex.append(escapeChar);

            }
            regex.append(c);
        }
        return regex.toString();
    }

    private static String replaceSpecialChars(String s) {
        String escapedChars = "/";
        for (int i = 0; i < escapedChars.length(); i++) {
            char c = escapedChars.charAt(i);
            s = s.replace(c, '.');
        }
        return s;
    }

    public static String preprocessRegExp(String s) {
        s = replaceSpecialChars(s);
        // s = escapeSpecialChars(s, "()");
        s = escapeSpecialChars(s, "(){}\\,-[]");
        String prefix = s.toLowerCase();
        String[] words = toWords(prefix, false); // include stop words
        StringBuffer regex = new StringBuffer();
        regex.append('^');
        for (int i = 0; i < words.length; i++) {
            boolean lastWord = i == words.length - 1;
            String word = words[i];
            int word_length = word.length();
            if (word_length > 0) {
                regex.append('(');
                if (word.charAt(word.length() - 1) == '.') {
                    regex.append(word.substring(0, word.length()));
                    regex.append("\\w*");
                } else {
                    regex.append(word);
                }
                regex.append("\\s").append(lastWord ? '*' : '+');
                regex.append(')');
            }
        }
        return regex.toString();
    }

    /************************
     * Custom sort processing
     ************************/
    /**
     * Sorts the given concept references based on a scoring algorithm designed
     * to provide a more natural ordering. Scores are determined by comparing
     * each reference against a provided search term.
     * 
     * @param searchTerm The term used for comparison; single or multi-word.
     * @param toSort The iterator containing references to sort.
     * @param maxToReturn Sets upper limit for number of top-scored items
     *        returned.
     * @return Iterator over sorted references.
     * @throws LBException
     */

    protected ResolvedConceptReferencesIterator sortByScore(String searchTerm,
        ResolvedConceptReferencesIterator toSort, int maxToReturn)
            throws LBException {
        // Determine the set of individual words to compare against.
        List<String> compareWords = toScoreWords(searchTerm);

        // Create a bucket to store results.
        Map<String, ScoredTerm> scoredResult =
            new TreeMap<String, ScoredTerm>();

        // Score all items ...
        while (toSort.hasNext()) {
            // Working in chunks of 100.
            ResolvedConceptReferenceList refs = toSort.next(100);
            for (int i = 0; i < refs.getResolvedConceptReferenceCount(); i++) {
                ResolvedConceptReference ref =
                    refs.getResolvedConceptReference(i);

                String code = ref.getConceptCode();
                Concept ce = ref.getReferencedEntry();
                ce.setEntityCodeNamespace(ref.getCodingSchemeName());

                // Note: Preferred descriptions carry more weight,
                // but we process all terms to allow the score to improve
                // based on any contained presentation.
                Presentation[] allTermsForConcept = ce.getPresentation();
                for (Presentation p : allTermsForConcept) {
                    // float score = score(p.getValue().getContent(),
                    // compareWords, p.isIsPreferred(), i);
                    float score =
                        score(p.getValue().getContent(), compareWords, p
                            .isIsPreferred(), i);

                    // Check for a previous match on this code for a different
                    // presentation.
                    // If already present, keep the highest score.
                    if (scoredResult.containsKey(code)) {
                        ScoredTerm scoredTerm =
                            (ScoredTerm) scoredResult.get(code);
                        if (scoredTerm.score > score)
                            continue;
                    }
                    scoredResult.put(code, new ScoredTerm(ref, score));
                }
            }
        }
        // Return an iterator that will sort the scored result.
        return new ScoredIterator(scoredResult.values(), maxToReturn);
    }

    protected ResolvedConceptReferencesIterator sortByScore(String searchTerm,
        ResolvedConceptReferencesIterator toSort, int maxToReturn,
        boolean descriptionOnly) throws LBException {

        if (!descriptionOnly)
            return sortByScore(searchTerm, toSort, maxToReturn);
        // Determine the set of individual words to compare against.
        List<String> compareWords = toScoreWords(searchTerm);

        // Create a bucket to store results.
        Map<String, ScoredTerm> scoredResult =
            new TreeMap<String, ScoredTerm>();

        // Score all items ...
        while (toSort.hasNext()) {
            // Working in chunks of 100.
            ResolvedConceptReferenceList refs = toSort.next(100);
            for (int i = 0; i < refs.getResolvedConceptReferenceCount(); i++) {
                ResolvedConceptReference ref =
                    refs.getResolvedConceptReference(i);
                String code = ref.getConceptCode();
                String name = ref.getEntityDescription().getContent();
                float score = score(name, compareWords, true, i);
                scoredResult.put(code, new ScoredTerm(ref, score));
            }
        }
        // Return an iterator that will sort the scored result.
        return new ScoredIterator(scoredResult.values(), maxToReturn);
    }

    protected float score_contains(String text, String target) {
        String text_lower = text.toLowerCase();
        String target_lower = target.toLowerCase();
        int n = text_lower.indexOf(target_lower);
        if (n == -1)
            return -1 * max_str_length;
        int m1 = n;
        int m2 = text.length() - (n + target.length());
        int score =
            max_str_length - penalty_multiplier_2 * m2 - penalty_multiplier_1
                * m1;
        return Math.max(0, score);
    }

    protected ResolvedConceptReferencesIterator sortByScore(String searchTerm,
        ResolvedConceptReferencesIterator toSort, int maxToReturn,
        String algorithm) throws LBException {
        // Determine the set of individual words to compare against.
        List<String> compareWords = toScoreWords(searchTerm);
        List<String> compareCodes = new ArrayList<String>();
        for (int k = 0; k < compareWords.size(); k++) {
            String word = (String) compareWords.get(k);
            compareCodes.add(word);
        }
        String target = "";
        if (algorithm.compareTo("DoubleMetaphoneLuceneQuery") == 0) {
            compareCodes = new ArrayList<String>();
            for (int k = 0; k < compareWords.size(); k++) {
                String word = (String) compareWords.get(k);
                String doubleMetaphonecode = doubleMetaphoneEncode(word);
                compareCodes.add(doubleMetaphonecode);
                // _logger.debug("*** DoubleMetaphoneLuceneQuery word " + word +
                // " code: " + doubleMetaphone.encode(word));
                target = target + doubleMetaphonecode;
                if (k < compareWords.size() - 1)
                    target = target + " ";
            }
        }
        // Create a bucket to store results.
        Map<String, ScoredTerm> scoredResult =
            new TreeMap<String, ScoredTerm>();
        // Score all items ...

        int knt = 0, nloops = 0;
        long msTotal = System.currentTimeMillis(), msTotal_toSortNext = 0, msTotal_sorted =
            0;
        while (toSort.hasNext()) {
            ++nloops;
            // Working in chunks of 100.
            long ms = System.currentTimeMillis(), delay = 0;
            ResolvedConceptReferenceList refs = toSort.next(500); // slow why???
            // //Debug.println("Run time (ms): toSort.next() method call took "
            // + (delay = System.currentTimeMillis() - ms) + " millisec.");
            // DBG.debugDetails("" + nloops + ") toSort.next(500): " +
            // Utils.timeToString(delay) +
            // " [ResolvedConceptReferencesIterator.next]");
            msTotal_toSortNext += delay;

            ms = System.currentTimeMillis();

            for (int i = 0; i < refs.getResolvedConceptReferenceCount(); i++) {
                ResolvedConceptReference ref =
                    refs.getResolvedConceptReference(i);

                String code = ref.getConceptCode();
                Concept ce = ref.getReferencedEntry();
                ce.setEntityCodeNamespace(ref.getCodingSchemeName());

                // Note: Preferred descriptions carry more weight,
                // but we process all terms to allow the score to improve
                // based on any contained presentation.
                Presentation[] allTermsForConcept = ce.getPresentation();
                for (Presentation p : allTermsForConcept) {
                    float score = (float) 0.0;
                    /*
                     * if (algorithm.compareTo("DoubleMetaphoneLuceneQuery") !=
                     * 0) { score = score(p.getValue().getContent(),
                     * compareWords); } else { score =
                     * score(p.getValue().getContent(), compareWords,
                     * compareCodes, target, true); }
                     */
                    if (algorithm.compareTo("contains") == 0
                        || algorithm.compareTo("RegExp") == 0) {
                        score =
                            score_contains(p.getValue().getContent(),
                                searchTerm);
                        // score = score(p.getValue().getContent(),
                        // compareWords);
                    } else if (algorithm
                        .compareTo("DoubleMetaphoneLuceneQuery") == 0) {
                        score =
                            score(p.getValue().getContent(), compareWords,
                                compareCodes, target, true);
                    } else {
                        score = score(p.getValue().getContent(), compareWords);
                    }

                    if (score > 0.) {
                        // Check for a previous match on this code for a
                        // different presentation.
                        // If already present, keep the highest score.
                        if (scoredResult.containsKey(code)) {
                            ScoredTerm scoredTerm =
                                (ScoredTerm) scoredResult.get(code);
                            if (scoredTerm.score > score)
                                continue;
                        }
                        scoredResult.put(code, new ScoredTerm(ref, score));
                    }
                }
            }
            int num_concepts = refs.getResolvedConceptReferenceCount();

            knt = knt + num_concepts;
            // if (knt > 1000) break;
            // //Debug.println("" + knt +
            // " completed.  Run time (ms): Assigning scores to " + num_concepts
            // + " concepts took " + (delay = System.currentTimeMillis() - ms) +
            // " millisec.");
            // DBG.debugDetails("" + nloops + ") Sorted [" + knt +
            // " concepts]: " + Utils.timeToString(delay));
            msTotal_sorted += delay;
        }
        // if (//DBG.isPerformanceTesting()) {
        // long duration = System.currentTimeMillis() - msTotal;
        // long avgDuration = duration/nloops;
        // DBG.debugDetails("* Summary of toSort/Sorted calls:");
        // DBG.debugDetails(duration, "Run Time", "sortByScore");
        // DBG.debugDetails("Iterations", nloops);
        // DBG.debugDetails(avgDuration, "Average", "sortByScore");
        // DBG.debugDetails(msTotal_toSortNext, "Total toSort.next time",
        // "sortByScore");
        // DBG.debugDetails(msTotal_sorted, "Total sorted time", "sortByScore");
        // }
        // Return an iterator that will sort the scored result.
        return new ScoredIterator(scoredResult.values(), maxToReturn);
    }

    protected ResolvedConceptReferencesIterator sortByScore(String searchTerm,
        ResolvedConceptReferencesIterator toSort, int maxToReturn,
        boolean descriptionOnly, String algorithm) throws LBException {

        if (!descriptionOnly) {
            return sortByScore(searchTerm, toSort, maxToReturn);
        }
        // Determine the set of individual words to compare against.

        List<String> compareWords = toScoreWords(searchTerm);
        List<String> compareCodes = new ArrayList<String>(compareWords.size());
        String target = "";
        if (algorithm.compareTo("DoubleMetaphoneLuceneQuery") == 0) {
            for (int k = 0; k < compareWords.size(); k++) {
                String word = (String) compareWords.get(k);
                String doubleMetaphonecode = doubleMetaphoneEncode(word);
                compareCodes.set(k, doubleMetaphonecode);
                target = target + doubleMetaphonecode;
                if (k < compareWords.size() - 1)
                    target = target + " ";
            }
        }

        // Create a bucket to store results.
        Map<String, ScoredTerm> scoredResult =
            new TreeMap<String, ScoredTerm>();

        // Score all items ...
        while (toSort.hasNext()) {
            // Working in chunks of 100.
            long ms = System.currentTimeMillis();

            ResolvedConceptReferenceList refs = toSort.next(500); // slow why???
            // _logger.debug("Run time (ms): toSort.next() took " +
            // (System.currentTimeMillis() - ms) + " millisec.");

            ms = System.currentTimeMillis();
            for (int i = 0; i < refs.getResolvedConceptReferenceCount(); i++) {
                ResolvedConceptReference ref =
                    refs.getResolvedConceptReference(i);
                String code = ref.getConceptCode();
                String name = ref.getEntityDescription().getContent();
                float score = (float) 0.0;// score(name, compareWords, true, i);
                /*
                 * if (algorithm.compareTo("DoubleMetaphoneLuceneQuery") == 0) {
                 * score = score(name, compareWords, compareCodes, target,
                 * true); } else { score = score(name, compareWords); }
                 */

                if (algorithm.compareTo("DoubleMetaphoneLuceneQuery") == 0) {
                    score =
                        score(name, compareWords, compareCodes, target, true);
                } else if (algorithm.compareTo("contains") == 0
                    || algorithm.compareTo("RegExp") == 0) {
                    score = score_contains(name, searchTerm);
                } else {
                    score = score(name, compareWords);
                }

                if (score > 0.) {
                    scoredResult.put(code, new ScoredTerm(ref, score));
                }
            }
            // _logger.debug("Run time (ms): assigning scores took " +
            // (System.currentTimeMillis() - ms) + " millisec.");
        }
        // Return an iterator that will sort the scored result.
        return new ScoredIterator(scoredResult.values(), maxToReturn);
    }

    /**
     * Returns a score providing a relative comparison of the given text against
     * a set of keywords.
     * <p>
     * Currently the score is evaluated as a simple percentage based on number
     * of words in the first set that are also in the second, with minor
     * adjustment for order (matching later words given slightly higher weight,
     * anticipating often the subject of search will follow descriptive text).
     * Weight is also increased for shorter phrases (measured in # words) If the
     * text is indicated to be preferred, the score is doubled to promote
     * 'bubbling to the top'.
     * <p>
     * Ranking from the original search is available but not currently used in
     * the heuristic (tends to throw-off desired alphabetic groupings later).
     * 
     * @param text
     * @param keywords
     * @param isPreferred
     * @param searchRank
     * @return The score; a higher value indicates a stronger match.
     */

    protected float score(String text, List<String> keywords,
        boolean isPreferred, float searchRank) {
        List<String> wordsToCompare = toScoreWords(text);
        float totalWords = wordsToCompare.size();
        float matchScore = 0;
        float position = 0;
        for (Iterator<String> words = wordsToCompare.listIterator(); words
            .hasNext(); position++) {
            String word = words.next();
            if (keywords.contains(word))
                matchScore += ((position / 10) + 1);
        }
        return Math.max(0, 100 + (matchScore / totalWords * 100)
            - (totalWords * 2));
        // Math.max(0, 100 + (matchScore / totalWords * 100) - (totalWords * 2))
        // * (isPreferred ? 2 : 1);
    }

    /* scoring method for DoubleMetaphoneLuceneQuery */
    protected float score(String text, List<String> keywords,
        List<String> keyword_codes, String target, boolean fuzzy_match) {
        List<String> wordsToCompare = toScoreWords(text);
        float totalWords = wordsToCompare.size();
        float matchScore = 0;
        float position = 0;
        String s = "";
        int k = 0;
        for (Iterator<String> words = wordsToCompare.listIterator(); words
            .hasNext(); position++) {
            String word = words.next();
            if (keywords.contains(word)) {
                // matchScore += ((position / 10) + 1);
                // matchScore = matchScore * 2;
                matchScore = matchScore + (float) 10. * ((position / 10) + 1);
                word = doubleMetaphoneEncode(word);

            } else if (fuzzy_match) {
                word = doubleMetaphoneEncode(word);
                if (keyword_codes.contains(word)) {
                    matchScore += ((position / 10) + 1);
                }
            }
            s = s + word;
            if (k < wordsToCompare.size() - 1)
                s = s + " ";
            k++;
        }

        if (s.indexOf(target) == -1) {
            return (float) 0.0;
        }
        float score =
            Math.max(0, 100 + (matchScore / totalWords * 100)
                - (totalWords * 2));
        return score;
        // Math.max(0, 100 + (matchScore / totalWords * 100) - (totalWords * 2))
        // * (isPreferred ? 2 : 1);
    }

    /**
     * Return words from the given string to be used in scoring algorithms, in
     * order of occurrence and ignoring duplicates, stop words, whitespace and
     * common separators.
     * 
     * @param s
     * @return List
     */

    protected List<String> toScoreWords(String s) {
        // return toWords(s, "[\\s,:+-;]", true, true);
        return toWords2(s, "[\\s,:+-;]", true, true);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean containsSpecialChars(String s) {
        // String escapedChars = "/.|!(){}[]^\"~*?;-_";
        String escapedChars = "/.|!(){}[]^\"~?;-_";
        for (int i = 0; i < escapedChars.length(); i++) {
            char c = escapedChars.charAt(i);
            if (s.indexOf(c) != -1)
                return true;
        }
        return false;
    }

    private static String replaceSpecialCharsWithBlankChar(String s) {
        String escapedChars = "/.|!(){}[]^\"~*?;-_";
        for (int i = 0; i < escapedChars.length(); i++) {
            char c = escapedChars.charAt(i);
            s = s.replace(c, ' ');
        }
        s = s.trim();
        return s;
    }

    public static Vector resolveIterator(
        ResolvedConceptReferencesIterator iterator, int maxToReturn,
        String code, boolean sortLight) {
        Vector v = new Vector();
        if (iterator == null) {
            _logger.warn("No match.");
            return v;
        }

        if (maxToReturn <= 0)
            maxToReturn = 100;
        try {
            int iteration = 0;
            while (iterator.hasNext()) {
                iteration++;
                iterator = iterator.scroll(maxToReturn);
                ResolvedConceptReferenceList rcrl = iterator.getNext();
                if (rcrl != null) {
                    ResolvedConceptReference[] rcra =
                        rcrl.getResolvedConceptReference();
                    if (rcra != null && rcra.length > 0) {
                        for (int i = 0; i < rcra.length; i++) {
                            ResolvedConceptReference rcr = rcra[i];
                            if (rcr != null) {
                                if (sortLight) {
                                    org.LexGrid.concepts.Concept ce =
                                        new org.LexGrid.concepts.Concept();
                                    ce.setEntityCode(rcr.getConceptCode());
                                    ce.setEntityDescription(rcr
                                        .getEntityDescription());
                                    if (code == null) {
                                        v.add(ce);
                                    } else {
                                        if (ce.getEntityCode().compareTo(code) != 0)
                                            v.add(ce);
                                    }
                                } else {
                                    Concept ce = rcr.getReferencedEntry();
                                    if (ce == null) {
                                        _logger
                                            .debug("rcr.getReferencedEntry() returns null???");
                                    } else {
                                        if (code == null)
                                            v.add(ce);
                                        else if (ce.getEntityCode().compareTo(
                                            code) != 0)
                                            v.add(ce);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    // /////////////////////////////////////////////////////////////////
    public HashMap getNamespaceId2CodingSchemeFormalNameMapping() {
        HashMap hmap = new HashMap();
        LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
        if (lbSvc == null) {
            _logger.warn("setCodingSchemeMap..??????????????");
            return null;
        }
        try {
            CodingSchemeRenderingList csrl = null;
            try {
                csrl = lbSvc.getSupportedCodingSchemes();
            } catch (LBInvocationException ex) {
                ex.printStackTrace();
                _logger.error("lbSvc.getSupportedCodingSchemes() FAILED..."
                    + ex.getCause());
                return null;
            }

            CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
            for (int i = 0; i < csrs.length; i++) {
                CodingSchemeRendering csr = csrs[i];
                Boolean isActive =
                    csr.getRenderingDetail().getVersionStatus().equals(
                        CodingSchemeVersionStatus.ACTIVE);
                // _logger.debug("\nActive? " + isActive);

                // if (isActive != null && isActive.equals(Boolean.TRUE))
                {
                    CodingSchemeSummary css = csr.getCodingSchemeSummary();
                    String formalname = css.getFormalName();

                    _logger.debug("\tformalname: " + formalname);
                    EntityDescription ed = css.getCodingSchemeDescription();
                    _logger.debug("\tcodingSchemeDescription: "
                        + ed.getContent());
                    java.lang.String uri = css.getCodingSchemeURI();
                    _logger.debug("\tURI: " + uri);
                    java.lang.String localName = css.getLocalName();
                    _logger.debug("\tlocalName: " + localName);
                    java.lang.String version = css.getRepresentsVersion();
                    _logger.debug("\tversion: " + version + "\n");
                    CodingSchemeVersionOrTag versionOrTag =
                        new CodingSchemeVersionOrTag();
                    CodingScheme cs =
                        lbSvc.resolveCodingScheme(formalname, versionOrTag);
                    SupportedNamespace[] namespaces =
                        cs.getMappings().getSupportedNamespace();
                    for (int j = 0; j < namespaces.length; j++) {
                        SupportedNamespace ns = namespaces[j];
                        java.lang.String ns_name =
                            ns.getEquivalentCodingScheme();
                        java.lang.String ns_id = ns.getContent();
                        _logger.debug("\tns_name: " + ns_name + " ns_id:"
                            + ns_id);
                        if (ns_id != null && ns_id.compareTo("") != 0) {
                            hmap.put(ns_id, formalname);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return hmap;
    }

    public Vector searchByCode(String scheme, String version, String matchText,
        String source, String matchAlgorithm) {
        LexBIGService lbs = RemoteServerUtil.createLexBIGService();
        Vector v = new Vector();
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        if (version != null)
            versionOrTag.setVersion(version);
        CodedNodeSet cns = null;
        try {
            cns = lbs.getNodeSet(scheme, versionOrTag, null);
            if (source != null)
                cns = restrictToSource(cns, source);
            CodedNodeSet.PropertyType[] propertyTypes = null;
            LocalNameList sourceList = null;
            LocalNameList contextList = null;
            NameAndValueList qualifierList = null;
            cns =
                cns.restrictToMatchingProperties(ConvenienceMethods
                    .createLocalNameList(new String[] { "conceptCode" }),
                    propertyTypes, sourceList, contextList, qualifierList,
                    matchText, matchAlgorithm, null);

            ResolvedConceptReference[] list = null;
            try {
                list =
                    cns.resolveToList(null, null, null, 500)
                        .getResolvedConceptReference();

                for (ResolvedConceptReference ref : list) {
                    v.add(ref.getReferencedEntry());
                }
            } catch (Exception ex) {
                _logger.error("WARNING: searchByCode throws exception.");
            }
        } catch (Exception ex) {
            _logger.error("WARNING: searchByCode throws exception.");
        }
        return v;
    }

    private CodedNodeSet union(Vector<CodedNodeSet> cns_vec) {
        if (cns_vec == null)
            return null;
        if (cns_vec.size() == 0)
            return null;
        CodedNodeSet cns = cns_vec.elementAt(0);

        if (cns_vec.size() == 1)
            return cns;
        for (int i = 1; i < cns_vec.size(); i++) {
            CodedNodeSet next_cns = cns_vec.elementAt(i);
            try {
                cns = cns.union(next_cns);
            } catch (Exception ex) {
                _logger.error("WARNING: cns.union throws exception.");
            }
        }
        return cns;
    }

    /*
     * public Vector findConceptWithSourceCodeMatching(String scheme, String
     * version, String sourceAbbr, String code, int maxToReturn, boolean
     * searchInactive) { if (sourceAbbr == null || code == null) return new
     * Vector();
     * 
     * LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
     * CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
     * versionOrTag.setVersion(version);
     * 
     * if (lbSvc == null) { _logger.warn("lbSvc = null"); return null; }
     * 
     * LocalNameList contextList = null; NameAndValueList qualifierList = null;
     * 
     * Vector<String> v = null;
     * 
     * if (code != null && code.compareTo("") != 0) { qualifierList = new
     * NameAndValueList(); NameAndValue nv = new NameAndValue();
     * nv.setName("source-code"); nv.setContent(code);
     * qualifierList.addNameAndValue(nv); }
     * 
     * LocalNameList propertyLnL = null; // sourceLnL Vector<String> w2 = new
     * Vector<String>(); w2.add(sourceAbbr); LocalNameList sourceLnL =
     * vector2LocalNameList(w2); if (sourceAbbr.compareTo("*") == 0 ||
     * sourceAbbr.compareToIgnoreCase("ALL") == 0) { sourceLnL = null; }
     * 
     * ResolvedConceptReferencesIterator matchIterator = null; SortOptionList
     * sortCriteria = null;//Constructors.createSortOptionList(new
     * String[]{"matchToQuery", "code"}); try { CodedNodeSet cns =
     * lbSvc.getCodingSchemeConcepts(scheme, null); if (cns == null) {
     * _logger.warn("lbSvc.getCodingSchemeConceptsd returns null"); return null;
     * } CodedNodeSet.PropertyType[] types = new CodedNodeSet.PropertyType[]
     * {CodedNodeSet.PropertyType.PRESENTATION}; cns =
     * cns.restrictToProperties(propertyLnL, types, sourceLnL, contextList,
     * qualifierList);
     * 
     * if (cns != null) { boolean activeOnly = !searchInactive; cns =
     * restrictToActiveStatus(cns, activeOnly);
     * 
     * try { matchIterator = cns.resolve(sortCriteria,
     * null,null);//ConvenienceMethods
     * .createLocalNameList(getPropertyForCodingScheme(cs)),null); } catch
     * (Exception ex) {
     * 
     * } if (matchIterator != null) { v = resolveIterator( matchIterator,
     * maxToReturn); return v; } }
     * 
     * } catch (Exception e) {
     * //getLogger().error("ERROR: Exception in findConceptWithSourceCodeMatching."
     * ); return null; } return null; }
     */
    private CodedNodeSet restrictToActiveStatus(CodedNodeSet cns,
        boolean activeOnly) {
        if (cns == null)
            return null;
        if (!activeOnly)
            return cns; // no restriction, do nothing
        try {
            cns =
                cns.restrictToStatus(CodedNodeSet.ActiveOption.ACTIVE_ONLY,
                    null);
            return cns;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static CodedNodeSet restrictToSource(CodedNodeSet cns, String source) {
        if (cns == null)
            return cns;
        if (source == null || source.compareTo("*") == 0
            || source.compareTo("") == 0 || source.compareTo("ALL") == 0)
            return cns;

        LocalNameList contextList = null;
        LocalNameList sourceLnL = null;
        NameAndValueList qualifierList = null;

        Vector<String> w2 = new Vector<String>();
        w2.add(source);
        sourceLnL = vector2LocalNameList(w2);
        LocalNameList propertyLnL = null;
        CodedNodeSet.PropertyType[] types =
            new CodedNodeSet.PropertyType[] { CodedNodeSet.PropertyType.PRESENTATION };
        try {
            cns =
                cns.restrictToProperties(propertyLnL, types, sourceLnL,
                    contextList, qualifierList);
        } catch (Exception ex) {
            _logger.error("restrictToSource throws exceptions.");
            return null;
        }
        return cns;
    }

    private static final boolean isInteger(final String s) {
        for (int x = 0; x < s.length(); x++) {
            final char c = s.charAt(x);
            if (x == 0 && (c == '-'))
                continue; // negative
            if ((c >= '0') && (c <= '9'))
                continue; // 0 - 9
            return false; // invalid
        }
        return true; // valid
    }

    private static final boolean containsDigit(final String s) {
        for (int x = 0; x < s.length(); x++) {
            final char c = s.charAt(x);
            if (x == 0 && (c == '-'))
                continue; // negative
            if ((c >= '0') && (c <= '9'))
                return true;
        }
        return false; // does not contain digit
    }

    protected String doubleMetaphoneEncode(String word) {
        if (word == null || word.length() == 0)
            return word;
        // if (isInteger(word)) return word;
        if (containsDigit(word))
            return word;

        return doubleMetaphone.encode(word);
    }

    protected float score(String text, List<String> keywords) {
        List<String> wordsToCompare = toScoreWords(text);
        float totalWords = wordsToCompare.size();
        float matchScore = 0;
        float position = 0;
        for (Iterator<String> words = wordsToCompare.listIterator(); words
            .hasNext(); position++) {
            String word = words.next();
            if (keywords.contains(word))
                matchScore += ((position / 10) + 1);
        }
        return Math.max(0, 100 + (matchScore / totalWords * 100)
            - (totalWords * 2));
        // Math.max(0, 100 + (matchScore / totalWords * 100) - (totalWords * 2))
        // * (isPreferred ? 2 : 1);
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Search by matching ALL properties
    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected void displayRef(ResolvedConceptReference ref) {
        _logger.debug(ref.getConceptCode() + ":"
            + ref.getEntityDescription().getContent());
    }

    public void dumpIterator(ResolvedConceptReferencesIterator itr) {
        try {
            while (itr.hasNext()) {
                try {
                    ResolvedConceptReference[] refs =
                        itr.next(100).getResolvedConceptReference();
                    for (ResolvedConceptReference ref : refs) {
                        displayRef(ref);
                    }
                } catch (Exception ex) {
                    break;
                }
            }
        } catch (Exception ex) {

        }
    }

    /*
     * new SearchUtils().searchByProperties(scheme, version, matchText,
     * property_types, property_names, source, matchAlgorithm,
     * excludeDesignation, ranking, maxToReturn);
     */

    public ResolvedConceptReferencesIteratorWrapper searchByProperties(
        Vector schemes, Vector versions, String matchText, String source,
        String matchAlgorithm, boolean excludeDesignation, boolean ranking,
        int maxToReturn) {
        String matchText0 = matchText;
        String matchAlgorithm0 = matchAlgorithm;
        matchText0 = matchText0.trim();

        _logger.debug("searchByProperties..." + matchText);

        if (matchText == null || matchText.length() == 0) {
            return null;
        }

        matchText = matchText.trim();
        if (matchAlgorithm.compareToIgnoreCase("contains") == 0) {
            matchAlgorithm = findBestContainsAlgorithm(matchText);
        }

        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;

        String scheme = null;
        String version = null;

        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();

            if (lbSvc == null) {
                _logger.warn("lbSvc = null");
                return null;
            }

            Vector cns_vec = new Vector();
            for (int i = 0; i < schemes.size(); i++) {
                cns = null;
                iterator = null;
                scheme = (String) schemes.elementAt(i);
                CodingSchemeVersionOrTag versionOrTag =
                    new CodingSchemeVersionOrTag();
                version = (String) versions.elementAt(i);
                if (version != null)
                    versionOrTag.setVersion(version);

                try {
                    if (lbSvc == null) {
                        _logger.warn("lbSvc = null");
                        return null;
                    }

                    cns = lbSvc.getNodeSet(scheme, versionOrTag, null);

                    if (cns != null) {
                        try {
                            LocalNameList propertyNames = new LocalNameList();
                            CodedNodeSet.PropertyType[] propertyTypes =
                                getAllPropertyTypes();

                            if (excludeDesignation) {
                                propertyTypes =
                                    getAllNonPresentationPropertyTypes();
                            }

                            String language = null;
                            try {
                                cns =
                                    cns.restrictToMatchingProperties(
                                        propertyNames, propertyTypes,
                                        matchText, matchAlgorithm, language);
                            } catch (Exception e) {
                                _logger
                                    .error("\t(*) restrictToMatchingProperties throws exceptions???: "
                                        + matchText
                                        + " matchAlgorithm: "
                                        + matchAlgorithm);
                                // e.printStackTrace();
                            }
                            try {
                                cns = restrictToSource(cns, source);
                            } catch (Exception e) {
                                _logger
                                    .error("\t(*) restrictToSource throws exceptions???: "
                                        + matchText
                                        + " matchAlgorithm: "
                                        + matchAlgorithm);
                                // e.printStackTrace();
                            }

                        } catch (Exception ex) {
                            _logger
                                .error("\t(*) searchByProperties2 throws exceptions.");
                            // ex.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // return null;
                }
                if (cns != null) {
                    cns_vec.add(cns);
                }
            }

            iterator = null;
            /*
             * cns = union(cns_vec); if (cns == null) return null;
             */

            LocalNameList restrictToProperties = null;// new LocalNameList();
            boolean resolveConcepts = false;
            SortOptionList sortCriteria = null;
            if (ranking) {
                sortCriteria =
                    Constructors
                        .createSortOptionList(new String[] { "matchToQuery" });

            } else {
                sortCriteria =
                    Constructors
                        .createSortOptionList(new String[] { "entityDescription" }); // code
                _logger.debug("*** Sort alphabetically...");
                resolveConcepts = false;
            }
            try {
                try {
                    long ms = System.currentTimeMillis(), delay = 0;
                    // iterator = cns.resolve(sortCriteria, null,
                    // restrictToProperties, null, resolveConcepts);
                    iterator =
                        new QuickUnionIterator(cns_vec, sortCriteria, null,
                            restrictToProperties, null, resolveConcepts);

                } catch (Exception e) {
                    _logger.error("Method: SearchUtil.searchByProperties");
                    _logger.error("* ERROR: cns.resolve throws exceptions.");
                    _logger.error("* " + e.getClass().getSimpleName() + ": "
                        + e.getMessage());
                    e.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (!excludeDesignation) {
            int lcv = 0;
            int iterator_size = 0;
            if (iterator != null) {
                try {
                    iterator_size = iterator.numberRemaining();
                } catch (Exception ex) {

                }
            }

            while (iterator_size == 0 && lcv < schemes.size()) {
                scheme = (String) schemes.elementAt(lcv);
                CodingSchemeVersionOrTag versionOrTag =
                    new CodingSchemeVersionOrTag();
                version = (String) versions.elementAt(lcv);
                if (version != null)
                    versionOrTag.setVersion(version);

                iterator =
                    matchConceptCode(scheme, version, matchText0, source,
                        "LuceneQuery");
                if (iterator != null) {
                    try {
                        iterator_size = iterator.numberRemaining();
                    } catch (Exception ex) {

                    }
                }
                lcv++;
            }
        }
        // return iterator;
        return new ResolvedConceptReferencesIteratorWrapper(iterator);

    }

    private CodedNodeSet.PropertyType[] getAllPropertyTypes() {
        CodedNodeSet.PropertyType[] propertyTypes =
            new CodedNodeSet.PropertyType[4];
        propertyTypes[0] = PropertyType.COMMENT;
        propertyTypes[1] = PropertyType.DEFINITION;
        propertyTypes[2] = PropertyType.GENERIC;
        propertyTypes[3] = PropertyType.PRESENTATION;
        return propertyTypes;
    }

    private CodedNodeSet.PropertyType[] getAllNonPresentationPropertyTypes() {
        CodedNodeSet.PropertyType[] propertyTypes =
            new CodedNodeSet.PropertyType[3];
        propertyTypes[0] = PropertyType.COMMENT;
        propertyTypes[1] = PropertyType.DEFINITION;
        propertyTypes[2] = PropertyType.GENERIC;
        return propertyTypes;
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Search by matching ALL relationships
    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public CodedNodeGraph getRestrictedCodedNodeGraph(LexBIGService lbSvc,
        String scheme, String version, String associationName,
        CodedNodeSet cns, int direction) {
        CodedNodeGraph cng = null;
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(version);

        try {
            cng = lbSvc.getNodeGraph(scheme, versionOrTag, null);
            if (associationName != null) {
                NameAndValueList asso_list =
                    createNameAndValueList(new String[] { associationName },
                        null);
                cng = cng.restrictToAssociations(asso_list, null);
            }
            if (cns != null) {
                if (direction == -1) {
                    cng = cng.restrictToSourceCodes(cns);
                } else if (direction == 1) {
                    cng = cng.restrictToTargetCodes(cns);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cng;
    }

    public ResolvedConceptReferencesIteratorWrapper searchByAssociations(
        Vector schemes, Vector versions, String matchText, String source,
        String matchAlgorithm, boolean designationOnly, boolean ranking,
        int maxToReturn) {
        String matchText0 = matchText;
        String matchAlgorithm0 = matchAlgorithm;
        matchText0 = matchText0.trim();

        _logger.debug("searchByAssociations..." + matchText);
        long ms = System.currentTimeMillis();
        long dt = 0;
        long total_delay = 0;
        boolean timeout = false;

        boolean preprocess = true;
        if (matchText == null || matchText.length() == 0) {
            return null;
        }

        matchText = matchText.trim();
        if (matchAlgorithm.compareToIgnoreCase("contains") == 0) {
            // matchAlgorithm = Constants.CONTAIN_SEARCH_ALGORITHM; // to be
            // replaced by literalSubString
            matchAlgorithm = findBestContainsAlgorithm(matchText);
        }

        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;

        String scheme = null;
        String version = null;

        String message = null;

        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();

            if (lbSvc == null) {
                _logger.warn("lbSvc = null");
                return null;
            }

            Vector cns_vec = new Vector();

            for (int i = 0; i < schemes.size(); i++) {
                cns = null;
                iterator = null;
                scheme = (String) schemes.elementAt(i);
                _logger.debug("\tsearching " + scheme);

                ms = System.currentTimeMillis();
                CodingSchemeVersionOrTag versionOrTag =
                    new CodingSchemeVersionOrTag();
                version = (String) versions.elementAt(i);
                if (version != null)
                    versionOrTag.setVersion(version);

                // i++;

                try {
                    if (lbSvc == null) {
                        _logger.warn("lbSvc = null");
                        return null;
                    }

                    // KLO, 022410 change failed
                    cns = lbSvc.getNodeSet(scheme, versionOrTag, null);
                    // cns = getNodeSetByEntityType(scheme, versionOrTag,
                    // "concept");

                    if (cns != null) {
                        try {
                            // find cns
                            if (designationOnly) {
                                try {
                                    cns =
                                        cns.restrictToMatchingDesignations(
                                            matchText, null, matchAlgorithm,
                                            null);
                                } catch (Exception ex) {
                                    return null;
                                }
                            }
                            cns = restrictToSource(cns, source);
                            String associationName = null;
                            int direction = RESTRICT_TARGET;
                            // CodedNodeGraph cng =
                            // getRestrictedCodedNodeGraph(lbSvc, scheme,
                            // version, associationName, cns, direction);
                            // toNode
                            boolean resolveForward = false;
                            boolean resolveBackward = true;

                            int resolveAssociationDepth = 1;
                            // int maxToReturn = -1;
                            ConceptReference graphFocus = null;
                            // CodedNodeSet cns2 = cng.toNodeList(graphFocus,
                            // resolveForward, resolveBackward,
                            // resolveAssociationDepth, maxToReturn);
                            // CodedNodeSet difference(CodedNodeSet
                            // codesToRemove)
                            // cns = cns2.difference(cns);
                            if (cns != null) {
                                cns =
                                    filterOutAnonymousClasses(lbSvc, scheme,
                                        cns);
                                if (cns != null) {
                                    cns_vec.add(cns);
                                }
                            }
                        } catch (Exception ex) {
                            // return null;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // return null;
                }

                dt = System.currentTimeMillis() - ms;
                ms = System.currentTimeMillis();
                total_delay = total_delay + dt;

                if (total_delay > NCItBrowserProperties.getPaginationTimeOut() * 60 * 1000) {
                    message =
                        "WARNING: Search is incomplete -- please enter more specific search criteria.";
                    // cont_flag = false;
                    break;
                }
            }

            iterator = null;
            if (cns_vec.size() == 0)
                return null;
            /*
             * cns = union(cns_vec); if (cns == null) return null;
             */

            LocalNameList restrictToProperties = null;// new LocalNameList();
            // boolean resolveConcepts = true;
            // if (!ranking) resolveConcepts = false;
            boolean resolveConcepts = false;

            SortOptionList sortCriteria = null;

            if (ranking) {
                sortCriteria =
                    Constructors
                        .createSortOptionList(new String[] { "matchToQuery" });

            } else {
                sortCriteria =
                    Constructors
                        .createSortOptionList(new String[] { "entityDescription" }); // code
                _logger.debug("*** Sort alphabetically...");
                // resolveConcepts = false;
            }
            // Need to set to true to retrieve concept name
            resolveConcepts = true;
            try {
                try {
                    boolean resolveForward = false;
                    boolean resolveBackward = true;

                    int resolveAssociationDepth = 1;
                    // iterator = cns.resolve(sortCriteria, null,
                    // restrictToProperties, null, resolveConcepts);
                    ResolvedConceptReferencesIterator quickUnionIterator =
                        new QuickUnionIterator(cns_vec, sortCriteria, null,
                            restrictToProperties, null, resolveConcepts);
                    iterator =
                        new SearchByAssociationIteratorDecorator(
                            quickUnionIterator, resolveForward,
                            resolveBackward, resolveAssociationDepth,
                            maxToReturn);
                    // testing, KLO (work-around) failed
                    // iterator = filterOutAnonymousClasses(cns, iterator);

                } catch (Exception e) {
                    _logger.error("Method: SearchUtil.searchByAssociations");
                    _logger.error("* ERROR: cns.resolve throws exceptions.");
                    _logger.error("* " + e.getClass().getSimpleName() + ": "
                        + e.getMessage());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // Pending LexEVS fix:
        // if (iterator != null) iterator.setMessage(message);
        // return iterator;
        return new ResolvedConceptReferencesIteratorWrapper(iterator, message);
    }

    private CodedNodeSet.PropertyType[] getAllPropertypes() {
        CodedNodeSet.PropertyType[] propertyTypes =
            new CodedNodeSet.PropertyType[4];
        propertyTypes[0] = PropertyType.COMMENT;
        propertyTypes[1] = PropertyType.DEFINITION;
        propertyTypes[2] = PropertyType.GENERIC;
        // propertyTypes[3] = PropertyType.INSTRUCTION;
        propertyTypes[3] = PropertyType.PRESENTATION;
        return propertyTypes;
    }

    private ResolvedConceptReferencesIterator filterOutAnonymousClasses(
        LexBIGService lbSvc, String scheme, CodedNodeSet cns,
        ResolvedConceptReferencesIterator iterator) {

        int maxReturn = 100;
        ConceptReferenceList codeList = new ConceptReferenceList();
        int knt = 0;
        int knt_concept = 0;

        try {
            if (iterator == null || iterator.numberRemaining() == 0)
                return iterator;

            // _logger.debug("iterator.numberRemaining(): " +
            // iterator.numberRemaining());

            while (iterator.hasNext()) {
                ResolvedConceptReference[] refs =
                    iterator.next(maxReturn).getResolvedConceptReference();
                for (ResolvedConceptReference ref : refs) {
                    String code = ref.getConceptCode();
                    knt++;
                    _logger.debug("(" + knt + ") code: " + code);
                    if (code.indexOf("@") == -1) {
                        codeList.addConceptReference(ref);
                        knt_concept++;
                    } else {
                        // _logger.debug("name: " +
                        // ref.getEntityDescription().getContent());
                    }
                }
            }

            // _logger.debug("(**) Number of concepts: " + knt_concept);
            // KLO 022410 changed failed.
            cns = lbSvc.getNodeSet(scheme, null, null);
            // cns = getNodeSetByEntityType(scheme, null, "concept");

            cns = cns.restrictToCodes(codeList);
            SortOptionList sortCriteria = null;
            LocalNameList restrictToProperties = new LocalNameList();
            boolean resolveConcepts = false;
            iterator =
                cns.resolve(sortCriteria, null, restrictToProperties, null,
                    resolveConcepts);

            // _logger.debug("New iterator.numberRemaining(): " +
            // iterator.numberRemaining());

            return iterator;
        } catch (Exception ex) {

        }
        return null;
    }

    private CodedNodeSet filterOutAnonymousClasses(LexBIGService lbSvc,
        String scheme, CodedNodeSet cns) {
        if (cns == null)
            return cns;

        SortOptionList sortCriteria = null;
        LocalNameList restrictToProperties = new LocalNameList();
        boolean resolveConcepts = false;

        int maxReturn = 100;
        ConceptReferenceList codeList = new ConceptReferenceList();
        int knt = 0;
        int knt_concept = 0;

        try {
            ResolvedConceptReferencesIterator iterator =
                cns.resolve(sortCriteria, null, restrictToProperties, null,
                    resolveConcepts);
            // _logger.debug("iterator.numberRemaining(): " +
            // iterator.numberRemaining());

            while (iterator.hasNext()) {
                ResolvedConceptReference[] refs =
                    iterator.next(maxReturn).getResolvedConceptReference();
                for (ResolvedConceptReference ref : refs) {
                    String code = ref.getConceptCode();
                    knt++;
                    // _logger.debug("(" + knt + ") code: " + code);
                    if (code.indexOf("@") == -1) {
                        codeList.addConceptReference(ref);
                        knt_concept++;
                    } else {
                        // _logger.debug("name: " +
                        // ref.getEntityDescription().getContent());
                    }
                }
            }
            if (knt_concept == 0)
                return null;

            // _logger.debug("(**) Number of concepts: " + knt_concept);

            cns = lbSvc.getNodeSet(scheme, null, null);
            cns = cns.restrictToCodes(codeList);

            return cns;
        } catch (Exception ex) {

        }
        return null;
    }

    private String findBestContainsAlgorithm(String matchText) {
        if (matchText == null)
            return "nonLeadingWildcardLiteralSubString";
        matchText = matchText.trim();
        if (matchText.length() == 0)
            return "nonLeadingWildcardLiteralSubString"; // or null
        if (matchText.length() > 1)
            return "nonLeadingWildcardLiteralSubString";
        char ch = matchText.charAt(0);
        if (Character.isDigit(ch))
            return "literal";
        else if (Character.isLetter(ch))
            return "LuceneQuery";
        else
            return "literalContains";
    }

    public static LocalNameList getPropertyLocalNameList(String codingSchemeName) {
        if (propertyLocalNameListHashMap == null) {
            propertyLocalNameListHashMap = new HashMap();
        }
        if (!propertyLocalNameListHashMap.containsKey(codingSchemeName)) {
            try {
                LocalNameList lnl = getAllPropertyNames(codingSchemeName);
                propertyLocalNameListHashMap.put(codingSchemeName, lnl);
                return lnl;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            return (LocalNameList) propertyLocalNameListHashMap
                .get(codingSchemeName);
        }
        return null;
    }

    public static LocalNameList getAllPropertyNames(String codingSchemeName)
            throws Exception {
        LocalNameList propertyNames = new LocalNameList();
        LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
        CodingScheme cs = lbSvc.resolveCodingScheme(codingSchemeName, null);
        for (SupportedProperty prop : cs.getMappings().getSupportedProperty()) {
            propertyNames.addEntry(prop.getLocalId());
        }
        return propertyNames;
    }

    public static CodedNodeSet getNodeSetByEntityType(String scheme,
        CodingSchemeVersionOrTag versionOrTag, String vEntry) {

        LocalNameList list = new LocalNameList();
        if (vEntry != null)
            list.addEntry(vEntry);

        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();

            if (lbSvc == null) {
                _logger.warn("lbSvc = null");
                return null;
            }

            return lbSvc.getNodeSet(scheme, versionOrTag, list);
        } catch (Exception ex) {

        }
        return null;
    }

    public ResolvedConceptReferencesIteratorWrapper searchByProperties(
        String scheme, String version, String matchText,
        String[] property_types, String[] property_names, String source,
        String matchAlgorithm, boolean excludeDesignation, boolean ranking,
        int maxToReturn) {
        boolean restrictToProperties = false;
        CodedNodeSet.PropertyType[] propertyTypes = null;
        LocalNameList propertyNames = null;
        LocalNameList sourceList = null;
        if (property_types != null || property_names != null) {
            _logger
                .debug("Case #1 -- property_types != null || property_names != null");

            restrictToProperties = true;
            if (property_types != null)
                propertyTypes = createPropertyTypes(property_types);
            if (property_names != null)
                propertyNames =
                    ConvenienceMethods.createLocalNameList(property_names);
        } else {
            _logger.debug("Case #2 ");
            propertyNames = new LocalNameList();
            propertyTypes = getAllPropertyTypes();
            if (excludeDesignation) {
                _logger.debug("Case #3 ");
                propertyTypes = getAllNonPresentationPropertyTypes();
            }
        }
        _logger.debug("searchByProperties scheme: " + scheme);
        _logger.debug("searchByProperties matchAlgorithm: " + matchAlgorithm);
        _logger.debug("searchByProperties source: " + source);
        _logger.debug("searchByProperties excludeDesignation: "
            + excludeDesignation);

        if (source != null)
            sourceList =
                ConvenienceMethods.createLocalNameList(new String[] { source });
        NameAndValueList qualifierList = null;

        String matchText0 = matchText;
        String matchAlgorithm0 = matchAlgorithm;
        matchText0 = matchText0.trim();
        if (matchText == null || matchText.length() == 0) {
            return null;
        }
        matchText = matchText.trim();
        if (matchAlgorithm.compareToIgnoreCase("contains") == 0) {
            matchAlgorithm = findBestContainsAlgorithm(matchText);
        }

        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;
        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();

            if (lbSvc == null) {
                _logger.warn("lbSvc = null");
                return null;
            }

            cns = null;
            iterator = null;
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            if (version != null)
                versionOrTag.setVersion(version);

            try {
                if (lbSvc == null) {
                    _logger.warn("lbSvc = null");
                    return null;
                }

                cns = lbSvc.getNodeSet(scheme, versionOrTag, null);

                if (cns != null) {
                    try {
                        String language = null;
                        try {
                            _logger
                                .debug("*** restrictToMatchingProperties ...matchText "
                                    + matchText
                                    + " matchAlgorithm "
                                    + matchAlgorithm);
                            cns =
                                cns.restrictToMatchingProperties(propertyNames,
                                    propertyTypes, matchText, matchAlgorithm,
                                    language);

                        } catch (Exception e) {
                            _logger
                                .debug("\t(*) restrictToMatchingProperties throws exceptions???: "
                                    + matchText
                                    + " matchAlgorithm: "
                                    + matchAlgorithm);
                            // e.printStackTrace();
                        }
                        try {
                            _logger.debug("restrictToSource ...source "
                                + source);
                            cns = restrictToSource(cns, source);
                        } catch (Exception e) {
                            _logger
                                .error("\t(*) restrictToSource throws exceptions???: "
                                    + matchText
                                    + " matchAlgorithm: "
                                    + matchAlgorithm);
                            // e.printStackTrace();
                        }

                    } catch (Exception ex) {
                        // ex.printStackTrace();
                        _logger
                            .error("\t(*) searchByProperties2 throws exceptions.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // return null;
            }
            iterator = null;
            if (cns == null) {
                return null;
            }

            // LocalNameList restrictToProperties = null;//new LocalNameList();
            boolean resolveConcepts = false;
            SortOptionList sortCriteria = null;
            if (ranking) {
                sortCriteria =
                    Constructors
                        .createSortOptionList(new String[] { "matchToQuery" });

            } else {
                sortCriteria =
                    Constructors
                        .createSortOptionList(new String[] { "entityDescription" }); // code
                _logger.debug("*** Sort alphabetically...");
                resolveConcepts = false;
            }
            try {
                try {
                    long ms = System.currentTimeMillis(), delay = 0;
                    cns = restrictToSource(cns, source);
                    _logger.debug("cns.resolve ...");
                    iterator =
                        cns.resolve(sortCriteria, null, null, null,
                            resolveConcepts);

                } catch (Exception e) {
                    _logger.error("Method: SearchUtil.searchByProperties");
                    _logger.error("* ERROR: cns.resolve throws exceptions.");
                    _logger.error("* " + e.getClass().getSimpleName() + ": "
                        + e.getMessage());
                    e.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (!excludeDesignation) {
            int lcv = 0;
            int iterator_size = 0;
            if (iterator != null) {
                try {
                    iterator_size = iterator.numberRemaining();

                    _logger.debug("Number of matches: " + iterator_size);
                } catch (Exception ex) {

                }
            }

            if (iterator_size == 0) {
                iterator =
                    matchConceptCode(scheme, version, matchText0, source,
                        "LuceneQuery");
                if (iterator != null) {
                    try {
                        iterator_size = iterator.numberRemaining();
                    } catch (Exception ex) {

                    }
                }
            }
        }
        return new ResolvedConceptReferencesIteratorWrapper(iterator);
    }

    public ResolvedConceptReferencesIteratorWrapper searchByProperties(
        String scheme, String version, String matchText, String source,
        String matchAlgorithm, boolean excludeDesignation, boolean ranking,
        int maxToReturn) {
        return searchByProperties(scheme, version, matchText, null, null,
            source, matchAlgorithm, excludeDesignation, ranking, maxToReturn);

    }

    public static CodedNodeSet.PropertyType[] createPropertyTypes(String[] types) {
        if (types == null || types.length == 0)
            return null;
        CodedNodeSet.PropertyType[] propertyTypes =
            new CodedNodeSet.PropertyType[types.length];
        int knt = -1;
        for (int i = 0; i < types.length; i++) {
            String type = (String) types[i];
            if (type.compareToIgnoreCase("PRESENTATION") == 0) {
                knt++;
                propertyTypes[knt] = PropertyType.PRESENTATION;
            } else if (type.compareToIgnoreCase("DEFINITION") == 0) {
                knt++;
                propertyTypes[knt] = PropertyType.DEFINITION;
            } else if (type.compareToIgnoreCase("GENERIC") == 0) {
                knt++;
                propertyTypes[knt] = PropertyType.GENERIC;
            } else if (type.compareToIgnoreCase("COMMENT") == 0) {
                knt++;
                propertyTypes[knt] = PropertyType.COMMENT;
            }
        }
        return propertyTypes;
    }

    /*
     * public ResolvedConceptReferencesIteratorWrapper searchByName(String
     * scheme, String version, String matchText, String source, String
     * matchAlgorithm, boolean ranking, int maxToReturn) { return
     * searchByName(scheme, version, matchText, source, matchAlgorithm, ranking,
     * maxToReturn, NameSearchType.All); }
     */

    public ResolvedConceptReferencesIteratorWrapper searchByName(String scheme,
        String version, String matchText, String source, String matchAlgorithm,
        boolean ranking, int maxToReturn, NameSearchType nameSearchType) {
        if (matchText == null || matchText.length() == 0)
            return null;

        matchText = matchText.trim();
        if (matchAlgorithm.compareToIgnoreCase("startsWith") == 0) {
            // No literalStartsWith support
        } else if (matchAlgorithm.compareToIgnoreCase("contains") == 0) { // p11.1-q11.1
                                                                          // /100{WBC}
            // matchAlgorithm = CONTAIN_SEARCH_ALGORITHM;
            matchAlgorithm = findBestContainsAlgorithm(matchText);
            // _logger.debug("algorithm: " + matchAlgorithm);
        }

        ResolvedConceptReferencesIterator iterator = null;
        if (nameSearchType == NameSearchType.All
            || nameSearchType == NameSearchType.Name) {
            CodedNodeSet cns = null;
            CodedNodeSet.PropertyType[] propertyTypes =
                new CodedNodeSet.PropertyType[1];
            propertyTypes[0] = PropertyType.PRESENTATION;

            try {
                LexBIGService lbSvc =
                    new RemoteServerUtil().createLexBIGService();
                if (lbSvc == null) {
                    _logger.warn("lbSvc = null");
                    return null;
                }
                CodingSchemeVersionOrTag versionOrTag =
                    new CodingSchemeVersionOrTag();
                if (version != null)
                    versionOrTag.setVersion(version);

                cns = lbSvc.getNodeSet(scheme, versionOrTag, null);
                if (cns == null) {
                    _logger.warn("cns = null");
                    return null;
                }

                // LocalNameList contextList = null;
                try {
                    LocalNameList sourceList = null;
                    if (source != null
                        && source.compareToIgnoreCase("ALL") != 0) {
                        sourceList = new LocalNameList();
                        sourceList.addEntry(source);
                    }

                    cns =
                        cns.restrictToMatchingDesignations(matchText,
                            SearchDesignationOption.ALL, matchAlgorithm, null);
                    cns =
                        cns.restrictToProperties(null, propertyTypes,
                            sourceList, null, null);

                } catch (Exception ex) {
                    return null;
                }

                LocalNameList restrictToProperties = null;// new
                                                          // LocalNameList();
                LocalNameList propertyNames =
                    Constructors.createLocalNameList("Semantic_Type");
                // boolean resolveConcepts = true; // Semantic_Type is no longer
                // required.

                SortOptionList sortCriteria = null;
                boolean resolveConcepts = true;
                LocalNameList filterOptions = null;
                propertyTypes = null;
                try {
                    try {
                        long ms = System.currentTimeMillis(), delay = 0;
                        cns = restrictToSource(cns, source);
                        // iterator = cns.resolve(sortCriteria, propertyNames,
                        // restrictToProperties, null, resolveConcepts);
                        iterator =
                            cns.resolve(sortCriteria, filterOptions,
                                propertyNames, propertyTypes, resolveConcepts);
                        /*
                         * Debug.println("cns.resolve delay ---- Run time (ms): "
                         * + (delay = System.currentTimeMillis() - ms) +
                         * " -- matchAlgorithm " + matchAlgorithm);
                         */
                        // DBG.debugDetails(delay, "cns.resolve",
                        // "searchByName, CodedNodeSet.resolve");
                    } catch (Exception e) {
                        _logger.error("ERROR: cns.resolve throws exceptions.");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        if (nameSearchType == NameSearchType.All
            || nameSearchType == NameSearchType.Code) {
            try {
                int size = iterator != null ? iterator.numberRemaining() : 0;
                if (size == 0) {
                    iterator =
                        matchConceptCode(scheme, version, matchText, source,
                            "LuceneQuery");
                }
                size = iterator.numberRemaining();
                if (size == 0) {
                    iterator =
                        findConceptWithSourceCodeMatching(scheme, version,
                            source, matchText, maxToReturn, true);
                }
                // Find ICD9CM concepts by code
                size = iterator.numberRemaining();
                if (size < 20) {
                    // heuristic rule (if the number of matches is large,
                    // then it's less likely that the matchText is a code)
                    Vector w = new Vector();
                    w.add(iterator);
                    ResolvedConceptReferencesIterator itr1 =
                        matchConceptCode(scheme, version, matchText, source,
                            "LuceneQuery");
                    if (itr1 != null)
                        w.add(itr1);
                    ResolvedConceptReferencesIterator itr2 =
                        findConceptWithSourceCodeMatching(scheme, version,
                            source, matchText, maxToReturn, true);
                    if (itr2 != null)
                        w.add(itr2);
                    iterator =
                        getResolvedConceptReferencesIteratorUnion(scheme,
                            version, w);
                }
            } catch (Exception e) {
            }
        }
        return new ResolvedConceptReferencesIteratorWrapper(iterator);
    }

    ResolvedConceptReferencesIterator getResolvedConceptReferencesIteratorUnion(
        String scheme, String version,
        Vector<ResolvedConceptReferencesIterator> v) {
        if (v == null)
            return null;
        int maxReturn = 100;
        Vector w = new Vector();
        HashSet hset = new HashSet();
        ResolvedConceptReferencesIterator matchIterator = null;

        for (int i = 0; i < v.size(); i++) {
            ResolvedConceptReferencesIterator iterator =
                (ResolvedConceptReferencesIterator) v.elementAt(i);
            try {
                while (iterator != null && iterator.hasNext()) {
                    ResolvedConceptReferenceList rcrl =
                        iterator.next(maxReturn);
                    if (rcrl != null) {
                        ResolvedConceptReference[] refs =
                            rcrl.getResolvedConceptReference();

                        if (refs != null) {
                            for (ResolvedConceptReference ref : refs) {
                                if (ref != null) {
                                    if (!hset.contains(ref.getConceptCode())) {
                                        w.add(ref.getConceptCode());
                                        hset.add(ref.getConceptCode());
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (w.size() > 0) {
            String[] codes = new String[w.size()];
            for (int i = 0; i < w.size(); i++) {
                String code = (String) w.elementAt(i);
                codes[i] = code;
            }

            try {
                LexBIGService lbSvc =
                    new RemoteServerUtil().createLexBIGService();
                if (lbSvc == null) {
                    _logger.warn("lbSvc == null???");
                    return null;
                }

                CodingSchemeVersionOrTag versionOrTag =
                    new CodingSchemeVersionOrTag();
                if (version != null)
                    versionOrTag.setVersion(version);

                ConceptReferenceList crefs =
                    createConceptReferenceList(codes, scheme);
                CodedNodeSet cns = lbSvc.getCodingSchemeConcepts(scheme, null);
                cns = cns.restrictToCodes(crefs);
                CodedNodeSet.PropertyType[] types =
                    new PropertyType[] { PropertyType.PRESENTATION };
                cns = cns.restrictToProperties(null, types, null, null, null);

                try {
                    matchIterator = cns.resolve(null, null, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            hset.clear();
        }
        return matchIterator;

    }

    public ResolvedConceptReferencesIterator findConceptWithSourceCodeMatching(
        String scheme, String version, String sourceAbbr, String code,
        int maxToReturn, boolean searchInactive) {
        if (sourceAbbr == null || code == null)
            return null;
        ResolvedConceptReferencesIterator matchIterator = null;

        LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(version);

        if (lbSvc == null) {
            _logger.warn("lbSvc = null");
            return null;
        }

        LocalNameList contextList = null;
        NameAndValueList qualifierList = null;

        Vector<String> v = null;

        if (code != null && code.compareTo("") != 0) {
            qualifierList = new NameAndValueList();
            NameAndValue nv = new NameAndValue();
            nv.setName("source-code");
            nv.setContent(code);
            qualifierList.addNameAndValue(nv);
        }

        LocalNameList propertyLnL = null;
        // sourceLnL
        Vector<String> w2 = new Vector<String>();
        w2.add(sourceAbbr);
        LocalNameList sourceLnL = vector2LocalNameList(w2);
        if (sourceAbbr.compareTo("*") == 0
            || sourceAbbr.compareToIgnoreCase("ALL") == 0) {
            sourceLnL = null;
        }

        SortOptionList sortCriteria = null;// Constructors.createSortOptionList(new
                                           // String[]{"matchToQuery", "code"});
        try {
            CodedNodeSet cns = lbSvc.getCodingSchemeConcepts(scheme, null);
            if (cns == null) {
                _logger.warn("lbSvc.getCodingSchemeConceptsd returns null");
                return null;
            }
            CodedNodeSet.PropertyType[] types =
                new PropertyType[] { PropertyType.PRESENTATION };
            cns =
                cns.restrictToProperties(propertyLnL, types, sourceLnL,
                    contextList, qualifierList);

            if (cns != null) {
                boolean activeOnly = !searchInactive;
                cns = restrictToActiveStatus(cns, activeOnly);

                try {
                    matchIterator = cns.resolve(sortCriteria, null, null);// ConvenienceMethods.createLocalNameList(getPropertyForCodingScheme(cs)),null);
                } catch (Exception ex) {

                }
            }

        } catch (Exception e) {
            return null;
        }
        return matchIterator;
    }

    public ResolvedConceptReferencesIteratorWrapper searchByCode(String scheme,
        String version, String matchText, String source, String matchAlgorithm,
        boolean ranking, int maxToReturn) {

        ResolvedConceptReferencesIterator iterator = null;

        iterator =
            matchConceptCode(scheme, version, matchText, source, "LuceneQuery");
        try {
            int size = iterator.numberRemaining();
            if (size == 0) {
                iterator =
                    findConceptWithSourceCodeMatching(scheme, version, source,
                        matchText, maxToReturn, true);
            }
            return new ResolvedConceptReferencesIteratorWrapper(iterator);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }

    public ResolvedConceptReferencesIteratorWrapper searchByAssociations(
        String scheme, String version, String matchText, String source,
        String matchAlgorithm, boolean designationOnly, boolean ranking,
        int maxToReturn) {
        return searchByAssociations(scheme, version, matchText, null, null,
            null, Constants.SEARCH_SOURCE, source, matchAlgorithm,
            designationOnly, ranking, maxToReturn);
    }

    /*
     * String[] associationsToNavigate, String[] association_qualifier_names,
     * String[] association_qualifier_values
     */
    public ResolvedConceptReferencesIteratorWrapper searchByAssociations(
        String scheme, String version, String matchText,
        String[] associationsToNavigate, String[] association_qualifier_names,
        String[] association_qualifier_values, int search_direction,
        String source, String matchAlgorithm, boolean designationOnly,
        boolean ranking, int maxToReturn) {
        /*
         * _logger.debug("searchByAssociations scheme: " + scheme);
         * _logger.debug("searchByAssociations matchText: " + matchText);
         * _logger.debug("searchByAssociations resolve_direction: " +
         * search_direction); _logger.debug("searchByAssociations source: " +
         * source); _logger.debug("searchByAssociations search_direction: " +
         * search_direction);
         * _logger.debug("searchByAssociations matchAlgorithm: " +
         * matchAlgorithm);
         * 
         * if (associationsToNavigate != null) { for (int lcv=0;
         * lcv<associationsToNavigate.length; lcv++) { String str = (String)
         * associationsToNavigate[lcv];
         * _logger.debug("searchByAssociations associationsToNavigate: " + str);
         * } }
         * 
         * if (association_qualifier_names != null) { for (int lcv=0;
         * lcv<association_qualifier_names.length; lcv++) { String str =
         * (String) association_qualifier_names[lcv];
         * _logger.debug("searchByAssociations association_qualifier_names: " +
         * str); } }
         * 
         * if (association_qualifier_values != null) { for (int lcv=0;
         * lcv<association_qualifier_values.length; lcv++) { String str =
         * (String) association_qualifier_values[lcv];
         * _logger.debug("searchByAssociations association_qualifier_values: " +
         * str); } }
         */
        NameAndValueList associationList = null;
        if (associationsToNavigate != null) {
            associationList =
                createNameAndValueList(associationsToNavigate, null);
        }
        NameAndValueList qualifiers = null;
        if (association_qualifier_names != null) {
            qualifiers =
                createNameAndValueList(association_qualifier_names,
                    association_qualifier_values);
        }

        String matchText0 = matchText;
        String matchAlgorithm0 = matchAlgorithm;
        matchText0 = matchText0.trim();
        String message = null;

        long ms = System.currentTimeMillis();
        long dt = 0;
        long total_delay = 0;
        boolean timeout = false;

        boolean preprocess = true;
        if (matchText == null || matchText.length() == 0) {
            return null;
        }

        matchText = matchText.trim();
        if (matchAlgorithm.compareToIgnoreCase("contains") == 0) {
            matchAlgorithm = findBestContainsAlgorithm(matchText);
        }

        CodedNodeSet cns = null;
        ResolvedConceptReferencesIterator iterator = null;

        try {
            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();

            if (lbSvc == null) {
                _logger.warn("lbSvc = null");
                return null;
            }

            Vector cns_vec = new Vector();

            cns = null;
            iterator = null;
            _logger.debug("\tsearching " + scheme);

            ms = System.currentTimeMillis();
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            if (version != null)
                versionOrTag.setVersion(version);

            try {
                if (lbSvc == null) {
                    _logger.warn("lbSvc = null");
                    return null;
                }

                // KLO, 022410 change failed
                cns = lbSvc.getNodeSet(scheme, versionOrTag, null);
                // cns = getNodeSetByEntityType(scheme, versionOrTag,
                // "concept");

                if (cns != null) {
                    try {
                        // find cns
                        if (designationOnly) {
                            cns =
                                cns.restrictToMatchingDesignations(matchText,
                                    null, matchAlgorithm, null);
                        }
                        cns = restrictToSource(cns, source);
                        int resolveAssociationDepth = 1;
                        // int maxToReturn = -1;
                        ConceptReference graphFocus = null;
                        // CodedNodeSet cns2 = cng.toNodeList(graphFocus,
                        // resolveForward, resolveBackward,
                        // resolveAssociationDepth, maxToReturn);
                        // CodedNodeSet difference(CodedNodeSet codesToRemove)
                        // cns = cns2.difference(cns);
                        // if (cns != null) {
                        // cns = filterOutAnonymousClasses(lbSvc, scheme, cns);
                        if (cns != null) {
                            cns_vec.add(cns);
                        }
                        // }
                    } catch (Exception ex) {
                        // return null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // return null;
            }

            dt = System.currentTimeMillis() - ms;
            ms = System.currentTimeMillis();
            total_delay = total_delay + dt;

            if (total_delay > NCItBrowserProperties.getPaginationTimeOut() * 60 * 1000) {
                message =
                    "WARNING: Search is incomplete -- please enter more specific search criteria.";
            }

            iterator = null;
            if (cns_vec.size() == 0)
                return null;

            LocalNameList restrictToProperties = null;// new LocalNameList();
            // boolean resolveConcepts = true;
            // if (!ranking) resolveConcepts = false;
            boolean resolveConcepts = false;

            SortOptionList sortCriteria = null;

            if (ranking) {
                sortCriteria =
                    Constructors
                        .createSortOptionList(new String[] { "matchToQuery" });

            } else {
                sortCriteria =
                    Constructors
                        .createSortOptionList(new String[] { "entityDescription" }); // code
                // resolveConcepts = false;
            }
            resolveConcepts = false;
            try {
                try {
                    boolean resolveForward = true;
                    boolean resolveBackward = true;

                    if (search_direction == Constants.SEARCH_SOURCE) {
                        resolveForward = false;
                        resolveBackward = true;
                    } else if (search_direction == Constants.SEARCH_TARGET) {
                        resolveForward = true;
                        resolveBackward = false;
                    }

                    int resolveAssociationDepth = 1;
                    // iterator = cns.resolve(sortCriteria, null,
                    // restrictToProperties, null, resolveConcepts);
                    ResolvedConceptReferencesIterator quickUnionIterator =
                        new QuickUnionIterator(cns_vec, sortCriteria, null,
                            restrictToProperties, null, resolveConcepts);
                    if (associationsToNavigate == null && qualifiers == null) {
                        iterator =
                            new SearchByAssociationIteratorDecorator(
                                quickUnionIterator, resolveForward,
                                resolveBackward, resolveAssociationDepth,
                                maxToReturn);
                    } else {
                        iterator =
                            new SearchByAssociationIteratorDecorator(
                                quickUnionIterator, resolveForward,
                                resolveBackward, associationList, qualifiers,
                                resolveAssociationDepth, maxToReturn);
                    }
                } catch (Exception e) {
                    _logger.error("Method: SearchUtil.searchByAssociations");
                    _logger.error("* ERROR: cns.resolve throws exceptions.");
                    _logger.error("* " + e.getClass().getSimpleName() + ": "
                        + e.getMessage());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            _logger
                .debug("Run time (ms): " + (System.currentTimeMillis() - ms));
        }
        // Pending LexEVS fix:
        // if (iterator != null) iterator.setMessage(message);

        if (iterator == null) {
            _logger
                .warn("=================== searchByAssociations returns iterator == null???");
        }

        return new ResolvedConceptReferencesIteratorWrapper(iterator, message);
    }

}
