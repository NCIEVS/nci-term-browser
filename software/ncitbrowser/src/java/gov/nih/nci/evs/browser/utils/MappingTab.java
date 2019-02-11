package gov.nih.nci.evs.browser.utils;

import gov.nih.nci.evs.browser.bean.*;

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

import org.apache.commons.codec.language.*;
import org.apache.log4j.*;
import org.LexGrid.relations.Relations;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;

import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping.SearchContext;

import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.Extensions.Generic.SupplementExtension;

import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Direction;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOption;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOptionName;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.QualifierSortOption;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;

import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.relations.AssociationPredicate;


/**
 * @author EVS Team
 * @version 1.0
 *
 *      Modification history Initial implementation kim.ong@ngc.com
 *
 */


/**
 * The Class MappingTab.
 */


public class MappingTab {
    private static Logger _logger = Logger.getLogger(MappingTab.class);

	private String serviceUrl = null;
    private LexBIGService lbSvc = null;

    MappingSearchUtils mappingSearchUtils = null;
    MetathesaurusUtils metathesaurusUtils = null;
    MetadataUtils metadataUtils = null;
    CodingSchemeDataUtils csdu = null;

    public MappingTab(LexBIGService lbSvc) {
        this.lbSvc = lbSvc;
		this. mappingSearchUtils = new MappingSearchUtils(lbSvc);
		this. metathesaurusUtils = new MetathesaurusUtils(lbSvc);
		this. metadataUtils = new MetadataUtils(lbSvc);
		this.csdu = new CodingSchemeDataUtils(lbSvc);
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

    public boolean isMapping(String scheme, String version) {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null) {
            csvt.setVersion(version);
		}

		try {
			LexBIGService distributed = RemoteServerUtil.createLexBIGService();
			MappingExtension mappingExtension = (MappingExtension)
				distributed.getGenericExtension("MappingExtension");

            boolean isMappingCS = mappingExtension.isMappingCodingScheme(scheme, csvt);
			return isMappingCS;

		} catch (Exception ex) {
            return false;
        }
    }


    public Vector getMatchedMetathesaurusCUIs(String mappings_scheme_curr, String mappings_version_curr, String ns, String mappings_code_curr) {
        return metathesaurusUtils.getMatchedMetathesaurusCUIs(mappings_scheme_curr, mappings_version_curr, ns, mappings_code_curr);
    }

    public Vector getMatchedMetathesaurusCUIs(Entity con) {
        return metathesaurusUtils.getMatchedMetathesaurusCUIs(con);
    }


    public String uri2CodingSchemeName(String mapping_cs_uri) {
		CodingScheme cs = csdu.resolveCodingScheme(mapping_cs_uri);
		if (cs == null) return null;
		return cs.getCodingSchemeName();
	}

    public Vector getMetadataProperties(String scheme) {
		CodingScheme cs = csdu.resolveCodingScheme(scheme);
		if (cs == null) return null;
		NameAndValue[] nvList = metadataUtils.getMetadataProperties(cs);
		Vector metadataProperties = new Vector();
		for (int k = 0; k < nvList.length; k++) {
			NameAndValue nv = (NameAndValue) nvList[k];
			metadataProperties.add(nv.getName() + "|" + nv.getContent());
		}
		return metadataProperties;
	}

    public Vector getMetadataValues(String scheme, String propertyName) {
		Vector metadata = getMetadataProperties(scheme);
        return metadataUtils.getMetadataValues(metadata, propertyName);
    }

    public Vector getMetadataValues(String scheme, String version, String propertyName) {
        return getMetadataValues(scheme, propertyName);
    }

    public String getMetadataValue(String scheme, String version, String propertyName) {
        Vector v;
        if (version != null && ! version.equalsIgnoreCase("null")) {
            v = getMetadataValues(scheme, version, propertyName);
        } else {
			v = getMetadataValues(scheme, propertyName);
		}
        if (v == null) return null;
        if (v == null || v.size() == 0)
            return null;
        return (String) v.elementAt(0);
    }


    public ResolvedConceptReferencesIterator getMappingRelationship(String scheme, String version, String code, int direction) {
		SearchContext searchContext = SearchContext.SOURCE_OR_TARGET_CODES;
		if (direction == 1) searchContext = SearchContext.SOURCE_CODES;
        else if (direction == -1) searchContext = SearchContext.TARGET_CODES;

        ResolvedConceptReferencesIterator iterator = mappingSearchUtils.searchByCode(
            scheme, version, code, "exactMatch", searchContext, -1);

        if (iterator == null) {
			System.out.println("iterator == null.");
			return null;
		}
		return iterator;
    }


    //Vector mapping_uri_version_vec = DataUtils.getMappingCodingSchemesEntityParticipatesIn(mappings_code_curr, null);
    public void process_mapping_uri_version_vec(Vector mapping_uri_version_vec, String mappings_code_curr) {
		if (mapping_uri_version_vec == null || mappings_code_curr == null) return;
        for(int lcv=0; lcv<mapping_uri_version_vec.size(); lcv++) {
			String mapping_uri_version = (String) mapping_uri_version_vec.elementAt(lcv);
			Vector ret_vec = StringUtils.parseData(mapping_uri_version, "|");
			String mapping_cs_uri = (String) ret_vec.elementAt(0);
			String mapping_cs_version = (String) ret_vec.elementAt(1);
			String mapping_cs_name = uri2CodingSchemeName(mapping_cs_uri);

			//System.out.println("mapping_cs_uri: " + mapping_cs_uri);
			//System.out.println("mapping_cs_version: " + mapping_cs_version);
			//System.out.println("mapping_cs_name: " + mapping_cs_name);

			boolean show_rank_column = true;
			String map_rank_applicable = getMetadataValue(mapping_cs_name, mapping_cs_version, "map_rank_applicable");
			if (map_rank_applicable != null && map_rank_applicable.compareTo("false") == 0) {
			    show_rank_column = false;
			}
			//System.out.println("show_rank_column: " + show_rank_column);

			ResolvedConceptReferencesIterator iterator = getMappingRelationship(mapping_cs_uri, mapping_cs_version, mappings_code_curr, 1);
			int numberRemaining = 0;
			try {
				numberRemaining = iterator.numberRemaining();
				System.out.println("numberRemaining: " + numberRemaining);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			MappingIteratorBean mappingIteratorBean = new MappingIteratorBean(iterator);
			if (mappingIteratorBean == null) {
				System.out.println("mappingIteratorBean == null");
			} else {
				System.out.println("mappingIteratorBean != null");
				List list = mappingIteratorBean.getData(0, numberRemaining); // implement getAll
				System.out.println("list: " + list.size());
				if (mappingIteratorBean.getSize() != numberRemaining) {
					list = mappingIteratorBean.getData(0, mappingIteratorBean.getSize() );
				}
			}
		}
	}

    public void dumpHashMap(HashMap hmap) {
		if (hmap == null) return;
		Iterator it = hmap.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) hmap.get(key);
			System.out.println(key + " --> " + value);
		}
	}

    public HashMap getMappingHashMap(String scheme) {
		return getMappingHashMap(scheme, null);
	}

    public HashMap getMappingHashMap(String scheme, String version) {
		return metadataUtils.getMappingDisplayHashMap(scheme, version);
	}

    public String getMappingDisplayName(String scheme, String name) {
		String version = null;
		HashMap hmap = getMappingHashMap(scheme, version);
		if (hmap == null) {
			return name;
		}
		Object obj = hmap.get(name);
		if (obj == null) {
			return name;
		}
		return obj.toString();
	}


    public String encodeTerm(String s) {
		return gov.nih.nci.evs.browser.utils.StringUtils.encodeTerm(s);
		/*
		if (s == null) return null;
		if (gov.nih.nci.evs.browser.utils.StringUtils.isAlphanumeric(s)) return s;

        StringBuilder buf = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
			if (Character.isLetterOrDigit(c)) {
                buf.append(c);
            } else {
                buf.append("&#").append((int) c).append(";");
            }
        }
        return buf.toString();
        */
    }

    public String getCSName(String codingScheme) {
		CodingScheme cs = csdu.resolveCodingScheme(codingScheme);
		if (cs == null) return null;
		return cs.getCodingSchemeName();
	}

    public String getFormalName(String codingScheme) {
		CodingScheme cs = csdu.resolveCodingScheme(codingScheme);
		if (cs == null) return null;
		return cs.getFormalName();
	}

	public static void main(String[] args) {
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		MappingTab tab = new MappingTab(lbSvc);
		String scheme = "NCI_Thesaurus";
		String version = null;
		String code = "C17087";//Reproduction (Code C17087)

		code = "C13264";
		//Cytokinesis (Code C16489)
		code = "C16489";
		//Mapping Source: GO_to_NCIt_Mapping

		String namespace = null;
		long ms = System.currentTimeMillis();
		boolean isMapping = tab.isMapping(scheme, version);
		System.out.println(scheme + " isMapping? " + isMapping);

        Vector mapping_uri_version_vec = null;
		if (!isMapping) {
		    mapping_uri_version_vec = tab.getMappingCodingSchemesEntityParticipatesIn(code, namespace);
		    StringUtils.dumpVector("mapping_uri_version_vec", mapping_uri_version_vec);
	    }
	    boolean use_ns = false;

	    Vector meta_cui_vec = null;
	    Entity con = new ConceptDetails(lbSvc).getConceptByCode(scheme, version, code, namespace, use_ns);
		if (con == null) {
		    meta_cui_vec = tab.getMatchedMetathesaurusCUIs(scheme, version, namespace, code);
		} else {
		    meta_cui_vec = tab.getMatchedMetathesaurusCUIs(con);
		}
		StringUtils.dumpVector("meta_cui_vec", meta_cui_vec);

		tab.process_mapping_uri_version_vec(mapping_uri_version_vec, code);

		HashMap mappingHashmap = tab.getMappingHashMap("GO_to_NCIt_Mapping");
		tab.dumpHashMap(mappingHashmap);
	    System.out.println("Total run time (ms): " + (System.currentTimeMillis() - ms));
	}
}
