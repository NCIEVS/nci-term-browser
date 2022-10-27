package gov.nih.nci.evs.browser.utils;

import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping.*;

import gov.nih.nci.evs.browser.bean.*;
import java.sql.*;
import java.util.*;
import org.apache.commons.codec.language.*;
import org.apache.logging.log4j.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.commonTypes.*;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.DataModel.Collections.*;
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
import org.LexGrid.LexBIG.Impl.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.naming.*;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;
import org.LexGrid.custom.relations.*;

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


public class MappingUtils {
	private static Logger _logger = LogManager.getLogger(MappingUtils.class);

	private String serviceUrl = null;
    private LexBIGService lbSvc = null;


    public MappingUtils(LexBIGService lbSvc) {
        this.lbSvc = lbSvc;
    }

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}


	public List<MappingSortOption> createMappingSortOption(int sortBy) {
        List<MappingSortOption> list = new ArrayList<MappingSortOption>();
        MappingSortOption option = null;
        QualifierSortOption qualifierOption = null;
        switch (sortBy) {
            case 1:
				option = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);
				list.add(option);
				option = new MappingSortOption(MappingSortOptionName.SOURCE_ENTITY_DESCRIPTION, Direction.ASC);
                list.add(option);
                qualifierOption = new QualifierSortOption(Direction.ASC, "rel");
                list.add(qualifierOption);
                qualifierOption = new QualifierSortOption(Direction.DESC, "score");
                list.add(qualifierOption);
				option = new MappingSortOption(MappingSortOptionName.TARGET_CODE, Direction.ASC);
				list.add(option);
				option = new MappingSortOption(MappingSortOptionName.TARGET_ENTITY_DESCRIPTION, Direction.ASC);
                list.add(option);
                break;

            case 2:
				option = new MappingSortOption(MappingSortOptionName.SOURCE_ENTITY_DESCRIPTION, Direction.ASC);
                list.add(option);
				option = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);
				list.add(option);
                qualifierOption = new QualifierSortOption(Direction.ASC, "rel");
                list.add(qualifierOption);
                qualifierOption = new QualifierSortOption(Direction.DESC, "score");
                list.add(qualifierOption);
				option = new MappingSortOption(MappingSortOptionName.TARGET_CODE, Direction.ASC);
				list.add(option);
				option = new MappingSortOption(MappingSortOptionName.TARGET_ENTITY_DESCRIPTION, Direction.ASC);
                list.add(option);
                break;

            // to be modified
            case 3:
				option = new MappingSortOption(MappingSortOptionName.SOURCE_ENTITY_DESCRIPTION, Direction.ASC);
                list.add(option);
				option = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);
				list.add(option);
                qualifierOption = new QualifierSortOption(Direction.ASC, "rel");
                list.add(qualifierOption);
                qualifierOption = new QualifierSortOption(Direction.DESC, "score");
                list.add(qualifierOption);
				option = new MappingSortOption(MappingSortOptionName.TARGET_CODE, Direction.ASC);
				list.add(option);
				option = new MappingSortOption(MappingSortOptionName.TARGET_ENTITY_DESCRIPTION, Direction.ASC);
                list.add(option);
                break;

            case 4:
                qualifierOption = new QualifierSortOption(Direction.ASC, "rel");
                list.add(qualifierOption);
                qualifierOption = new QualifierSortOption(Direction.DESC, "score");
                list.add(qualifierOption);
				option = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);
				list.add(option);
				option = new MappingSortOption(MappingSortOptionName.SOURCE_ENTITY_DESCRIPTION, Direction.ASC);
                list.add(option);
				option = new MappingSortOption(MappingSortOptionName.TARGET_CODE, Direction.ASC);
				list.add(option);
				option = new MappingSortOption(MappingSortOptionName.TARGET_ENTITY_DESCRIPTION, Direction.ASC);
                list.add(option);
                break;

            case 5:
                qualifierOption = new QualifierSortOption(Direction.DESC, "score");
                list.add(qualifierOption);
				option = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);
				list.add(option);
				option = new MappingSortOption(MappingSortOptionName.SOURCE_ENTITY_DESCRIPTION, Direction.ASC);
                list.add(option);
                qualifierOption = new QualifierSortOption(Direction.ASC, "rel");
                list.add(qualifierOption);
				option = new MappingSortOption(MappingSortOptionName.TARGET_CODE, Direction.ASC);
				list.add(option);
				option = new MappingSortOption(MappingSortOptionName.TARGET_ENTITY_DESCRIPTION, Direction.ASC);
                list.add(option);
                break;

            case 6:
				option = new MappingSortOption(MappingSortOptionName.TARGET_CODE, Direction.ASC);
				list.add(option);
				option = new MappingSortOption(MappingSortOptionName.TARGET_ENTITY_DESCRIPTION, Direction.ASC);
                list.add(option);
				option = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);
				list.add(option);
				option = new MappingSortOption(MappingSortOptionName.SOURCE_ENTITY_DESCRIPTION, Direction.ASC);
                list.add(option);
                qualifierOption = new QualifierSortOption(Direction.ASC, "rel");
                list.add(qualifierOption);
                qualifierOption = new QualifierSortOption(Direction.DESC, "score");
                list.add(qualifierOption);
                break;

            case 7:
				option = new MappingSortOption(MappingSortOptionName.TARGET_ENTITY_DESCRIPTION, Direction.ASC);
                list.add(option);
 				option = new MappingSortOption(MappingSortOptionName.TARGET_CODE, Direction.ASC);
				list.add(option);
				option = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);
				list.add(option);
				option = new MappingSortOption(MappingSortOptionName.SOURCE_ENTITY_DESCRIPTION, Direction.ASC);
                list.add(option);
                qualifierOption = new QualifierSortOption(Direction.ASC, "rel");
                list.add(qualifierOption);
                qualifierOption = new QualifierSortOption(Direction.DESC, "score");
                list.add(qualifierOption);
               break;

            // to be modified
            case 8:
 				//option = new MappingSortOption(MappingSortOptionName.TARGET_NAMESPACE, Direction.ASC);
                //list.add(option);
				option = new MappingSortOption(MappingSortOptionName.TARGET_ENTITY_DESCRIPTION, Direction.ASC);
                list.add(option);
 				option = new MappingSortOption(MappingSortOptionName.TARGET_CODE, Direction.ASC);
				list.add(option);
				option = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);
				list.add(option);
				option = new MappingSortOption(MappingSortOptionName.SOURCE_ENTITY_DESCRIPTION, Direction.ASC);
                list.add(option);
                qualifierOption = new QualifierSortOption(Direction.ASC, "rel");
                list.add(qualifierOption);
                qualifierOption = new QualifierSortOption(Direction.DESC, "score");
                list.add(qualifierOption);
               break;

            default:
               return createMappingSortOption(1);
		}
		return list;
	}


    public ResolvedConceptReferencesIterator getMappingDataIterator(String scheme, String version) {
		return getMappingDataIterator(scheme, version, MappingData.COL_SOURCE_CODE);
	}

    public ResolvedConceptReferencesIterator getMappingDataIterator(String scheme, String version, int sortBy) {
		List<MappingSortOption> sortOptionList = createMappingSortOption(sortBy);
		return getMappingDataIterator(scheme, version, sortOptionList);
	}

    public ResolvedConceptReferencesIterator getMappingDataIterator(String scheme, String version, List<MappingSortOption> sortOptionList) {
		CodingSchemeVersionOrTag versionOrTag =
			new CodingSchemeVersionOrTag();
		if (version != null) {
			versionOrTag.setVersion(version);
		}
		String relationsContainerName = null;
        try {

			CodingScheme cs = lbSvc.resolveCodingScheme(scheme, versionOrTag);
			if (cs == null) return null;

			java.util.Enumeration<? extends Relations> relations = cs.enumerateRelations();
			if (relations == null) return null;
			while (relations.hasMoreElements()) {
				Relations relation = (Relations) relations.nextElement();
				Boolean isMapping = relation.getIsMapping();
				if (isMapping != null && isMapping.equals(Boolean.TRUE)) {
 					relationsContainerName = relation.getContainerName();
					break;
				}
			}
			if (relationsContainerName == null) {
				return null;
			}

			MappingExtension mappingExtension = (MappingExtension)
				lbSvc.getGenericExtension("MappingExtension");

			ResolvedConceptReferencesIterator itr = mappingExtension.resolveMapping(
					scheme,
					versionOrTag,
					relationsContainerName,
					sortOptionList);

			return itr;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

    public NameAndValueList getMappingAssociationNames(String scheme, String version) {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null) {
            csvt.setVersion(version);
		}
		NameAndValueList navList = new NameAndValueList();
		try {
			CodingScheme cs = lbSvc.resolveCodingScheme(scheme, csvt);
			Relations[] relations = cs.getRelations();
			if (relations == null) return null;
			for (int i = 0; i < relations.length; i++) {
				Relations relation = relations[i];
                Boolean isMapping = relation.isIsMapping();
                if (isMapping != null && isMapping.equals(Boolean.TRUE)) {
					AssociationPredicate[] associationPredicates = relation.getAssociationPredicate();
					if (associationPredicates == null) return null;
					for (int j=0; j<associationPredicates.length; j++) {
						AssociationPredicate associationPredicate = associationPredicates[j];
						String name = associationPredicate.getAssociationName();
						NameAndValue vNameAndValue = new NameAndValue();
						vNameAndValue.setName(name);
						navList.addNameAndValue(vNameAndValue);
					}
					return navList;
				} else {
					return null;
				}
			}
		} catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public Vector getMappingCodingSchemesEntityParticipatesIn(String code, String namespace) {
        Vector v = new Vector();
        try {
			MappingExtension mappingExtension =
				(MappingExtension) lbSvc.getGenericExtension("MappingExtension");

			AbsoluteCodingSchemeVersionReferenceList mappingSchemes =
				mappingExtension.getMappingCodingSchemesEntityParticipatesIn(code, namespace);

			//output is all of the mapping ontologies that this code participates in.
			for(AbsoluteCodingSchemeVersionReference ref : mappingSchemes.getAbsoluteCodingSchemeVersionReference()){
				v.add(ref.getCodingSchemeURN() + "|" + ref.getCodingSchemeVersion());
			}

		} catch (Exception ex) {
            ex.printStackTrace();
        }
		return v;
	}

    public String getMappingMetadata(String mappingCodingScheme, String mappingCodingSchemeVersion) {
		CodingSchemeDataUtils csdu = new CodingSchemeDataUtils(lbSvc);
		return csdu.getMappingMetadata(mappingCodingScheme, mappingCodingSchemeVersion);
	}

    public int mapRank2Score(String mapRank) {
		int score = 0;
		if (mapRank == null) return score;
		try {
			score = Integer.parseInt(mapRank);
		} catch (NumberFormatException e) {
			System.out.println("Invalid mapRank: " + mapRank);
		}
		return score;
	}

	public java.util.List<MappingData> terminologyMapBean2MappingData(java.util.List<TerminologyMapBean> tmb_list,
			  String sourceCodingScheme,
			  String sourceCodingSchemeVersion,
			  String associationName,
			  String targetCodingScheme,
			  String targetCodingSchemeVersion) {

        java.util.List<MappingData> mb_list = new ArrayList();
        for (int i=0; i<tmb_list.size(); i++) {
			TerminologyMapBean tmb = (TerminologyMapBean) tmb_list.get(i);
			int score = mapRank2Score(tmb.getMapRank());
			MappingData md = new MappingData(
		       tmb.getSourceCode(),
		       tmb.getSourceName(),
		       sourceCodingScheme,
		       sourceCodingSchemeVersion,
		       tmb.getSource(),
		       associationName,
		       tmb.getRel(),
		       score,
		       tmb.getTargetCode(),
		       tmb.getTargetName(),
		       targetCodingScheme,
		       targetCodingSchemeVersion,
		       tmb.getTarget());
		    mb_list.add(md);
		}
		return mb_list;
	}

    public static java.util.List<TerminologyMapBean> resolveBulkMapping(final String mappingName, String mappingVersion) {
		java.util.List<TerminologyMapBean> tmb_list = new MappingExtensionImpl().resolveBulkMapping(mappingName, mappingVersion);
		return tmb_list;
	}

    public java.util.List<MappingData> getMappingData(String mapping_schema, String mapping_version) {
		long ms = System.currentTimeMillis();
		String metadata = getMappingMetadata(mapping_schema, mapping_version);
		Vector u = gov.nih.nci.evs.browser.utils.StringUtils.parseData(metadata);
		String sourceCodingScheme = (String) u.elementAt(0);
		String sourceCodingSchemeVersion = (String) u.elementAt(1);
		String targetCodingScheme = (String) u.elementAt(2);
		String targetCodingSchemeVersion = (String) u.elementAt(3);
        String associationName = (String) u.elementAt(4);
        java.util.List<TerminologyMapBean> tmb_list = resolveBulkMapping(mapping_schema, mapping_version);
        System.out.println("Total resolveBulkMapping run time (ms): " + (System.currentTimeMillis() - ms));
        ms = System.currentTimeMillis();

	    java.util.List<MappingData> md_list = terminologyMapBean2MappingData(tmb_list,
			  sourceCodingScheme,
			  sourceCodingSchemeVersion,
			  associationName,
			  targetCodingScheme,
			  targetCodingSchemeVersion);
		System.out.println("Total export run time (ms): " + (System.currentTimeMillis() - ms));
		return md_list;
	}
}


