package gov.nih.nci.evs.browser.utils;

import gov.nih.nci.evs.browser.common.*;
import java.sql.*;
import java.util.*;
import org.apache.commons.codec.language.*;
import org.apache.log4j.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.commonTypes.*;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.DataModel.Core.types.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.Impl.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.naming.*;

public class MapsToReportGenerator {

	public static String MAPS_TO_HEADING = "CODE|PT|RELATIONSHIP_TO_TARGET|TARGET_CODE|TARGET_TERM|TARGET_TERM_TYPE|TARGET_TERMINOLOGY|TARGET_TERMINOLOGY_VERSION";
	public static String MAPS_TO = "Maps_To";
	public static int NUMER_OF_FIELDS = 8;

	public static String maps_to_name = "Maps_To_Mapping_Data";
	public static String maps_to_display_name = "NCI Thesaurus Maps To Target Terminologies";

    private LexBIGService lbSvc = null;

    public MapsToReportGenerator(LexBIGService lbSvc) {
       this.lbSvc = lbSvc;
    }

    private CodedNodeSet restrictToActiveStatus(CodedNodeSet cns,
        boolean activeOnly) {
        if (cns == null)
            return null;
        if (!activeOnly)
            return cns;
        try {
            cns = cns.restrictToStatus(CodedNodeSet.ActiveOption.ACTIVE_ONLY, null);
            return cns;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public LocalNameList vector2LocalNameList(Vector<String> v) {
        if (v == null)
            return null;
        LocalNameList list = new LocalNameList();
        for (int i = 0; i < v.size(); i++) {
            String vEntry = (String) v.elementAt(i);
            list.addEntry(vEntry);
        }
        return list;
    }

    public ResolvedConceptReferencesIterator findConceptsWithMatchingProperty(
        String scheme, String version,
        boolean searchInactive) {

        ResolvedConceptReferencesIterator matchIterator = null;
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(version);

        if (lbSvc == null) {
            return null;
        }

        LocalNameList contextList = null;
        NameAndValueList qualifierList = null;

        LocalNameList propertyLnL = null;
        Vector<String> w2 = new Vector<String>();
        String propertyName = "Maps_To";
        w2.add(propertyName);
        propertyLnL = vector2LocalNameList(w2);

        SortOptionList sortCriteria = null;// Constructors.createSortOptionList(new
                                           // String[]{"matchToQuery", "code"});
        try {
            CodedNodeSet cns = lbSvc.getCodingSchemeConcepts(scheme, null);
            if (cns == null) {
                return null;
            }
            CodedNodeSet.PropertyType[] types =
                new PropertyType[] { PropertyType.GENERIC };
            cns = cns.restrictToProperties(propertyLnL, types);

            if (cns != null) {
                boolean activeOnly = !searchInactive;
                cns = restrictToActiveStatus(cns, activeOnly);
                try {
                    matchIterator = cns.resolve(sortCriteria, propertyLnL, types);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch (Exception e) {
            return null;
        }
        return matchIterator;
    }


    public Vector getMapsToData(String scheme, String version) {
		return getMapsToData(scheme, version, false);
	}

	public String get_maps_too_heading() {
		return MAPS_TO_HEADING;
	}

    public Vector getMapsToData(String scheme, String version, boolean searchInactive) {
		Vector w = new Vector();
		//w.add(MAPS_TO_HEADING);
        ResolvedConceptReferencesIterator iterator = findConceptsWithMatchingProperty(
           scheme, version, searchInactive);

        if (iterator == null) {
			System.out.println("iterator == null???");
			System.exit(0);
		}
        try {
			int numRemaining = iterator.numberRemaining();
			System.out.println("Number of matches: " + numRemaining);
			while (iterator.hasNext()) {
				ResolvedConceptReference rcr = (ResolvedConceptReference) iterator.next();
				//System.out.println(rcr.getEntityDescription().getContent() + " (" + rcr.getCode() + ")");
                Entity e = rcr.getReferencedEntry();
                Vector values = new Vector();
                for (int i=0; i<NUMER_OF_FIELDS; i++) {
					values.add("N/A");
				}
                Property[] properties = e.getProperty();
                for (int j=0; j<properties.length; j++) {
					Property property = properties[j];
                    String propName = property.getPropertyName();
                    //System.out.println(propName);
                    String term_name = property.getValue().getContent();
                    PropertyQualifier[] qualifiers = property.getPropertyQualifier();
                    for (int k=0; k<qualifiers.length; k++) {
						PropertyQualifier qualifier = qualifiers[k];
						String qualifierName = qualifier.getPropertyQualifierName();
						String qualifierValue = qualifier.getValue().getContent();
						//System.out.println(qualifierName + " --> " + qualifierValue);
                        String field_name = qualifierName;
                        String value = qualifierValue;
						if (field_name.compareTo("Relationship_to_Target") == 0) {
							values.setElementAt(value, 2);
						} else if (field_name.compareTo("Target_Code") == 0) {
							values.setElementAt(value, 3);
						} else if (field_name.compareTo("Target_Term_Type") == 0) {
							values.setElementAt(value, 5);
						} else if (field_name.compareTo("Target_Terminology") == 0) {
							values.setElementAt(value, 6);
						} else if (field_name.compareTo("Target_Terminology_Version") == 0) {
							values.setElementAt(value, 7);
						}
					}

					values.setElementAt(rcr.getCode(), 0);
					values.setElementAt(rcr.getEntityDescription().getContent(), 1);
					values.setElementAt(term_name, 4);

					StringBuffer buf = new StringBuffer();
					for (int i=0; i<NUMER_OF_FIELDS; i++) {
						String value = (String) values.elementAt(i);
						buf.append(value);
						if (i<NUMER_OF_FIELDS-1) {
							buf.append("|");
						}
					}
					w.add(buf.toString());
                }
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return w;
	}

/*
    public static void main(String[] args) {
		long ms = System.currentTimeMillis();
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		MapsToReportGenerator test = new MapsToReportGenerator(lbSvc);
		String scheme = "NCI_Thesaurus";
		String version = "19.09e";

		Vector w = new Vector();
		w.add(MAPS_TO_HEADING);

		Vector v = test.getMapsToData(scheme, version);
		w.addAll(v);

        Utils.saveToFile("Maps_To.txt", w);
        System.out.println("Total run time (ms): " + (System.currentTimeMillis() - ms));
	}
*/
}
