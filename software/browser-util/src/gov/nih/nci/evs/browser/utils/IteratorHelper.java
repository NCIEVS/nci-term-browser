package gov.nih.nci.evs.browser.utils;

import java.net.*;
import java.io.*;
import java.util.*;

import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;

public class IteratorHelper {

	public static String getResolvedConceptReferenceExpression(ResolvedConceptReference ref) {
		StringBuffer buf = new StringBuffer();
		buf.append(ref.getEntityDescription().getContent() + " (" + ref.getConceptCode() + ")").append("\n");
		buf.append("\turi: " + ref.getCodingSchemeURI()).append("\n");
		buf.append("\tcoding scheme: " + ref.getCodingSchemeName()).append("\n");
		buf.append("\tversion: " + ref.getCodingSchemeVersion()).append("\n");
		buf.append("\tnamespace: " + ref.getCodeNamespace());
		return buf.toString();
	}

    protected static void displayRef(ResolvedConceptReference ref) {
        System.out.println(getResolvedConceptReferenceExpression(ref));
    }

    protected static void displayRef(int index, ResolvedConceptReference ref) {
        System.out.println("(" + index + ") " + getResolvedConceptReferenceExpression(ref));
    }

    public static void dumpResolvedConceptReferenceList(ResolvedConceptReferenceList list) {
		dumpResolvedConceptReferenceList(list, true);
	}

    public static void dumpResolvedConceptReferenceList(ResolvedConceptReferenceList list, boolean showIndex) {
		int knt = 0;
		for (int i=0; i<list.getResolvedConceptReferenceCount(); i++) {
			ResolvedConceptReference ref = list.getResolvedConceptReference(i);
			knt++;
			if (showIndex) {
				displayRef(knt, ref);
			} else {
				displayRef(ref);
			}
		}
	}

    public static void dumpAbsoluteCodingSchemeVersionReferenceList(List<AbsoluteCodingSchemeVersionReference> list) {
		int knt = 0;
		//for (int i=0; i<list.getAbsoluteCodingSchemeVersionReferenceCount(); i++) {
		for (int i=0; i<list.size(); i++) {
			AbsoluteCodingSchemeVersionReference ref = list.get(i);
			StringBuffer buf = new StringBuffer();
			//buf.append(ref.getEntityDescription().getContent() + " (" + ref.getConceptCode() + ")").append("\n");
			buf.append("\turi: " + ref.getCodingSchemeURN()).append("\n");
			/*
			buf.append("\tcoding scheme: " + ref.getCodingSchemeName()).append("\n");
			buf.append("\tversion: " + ref.getCodingSchemeVersion()).append("\n");
			buf.append("\tnamespace: " + ref.getCodeNamespace());
			*/
			System.out.println(buf.toString());
		}
	}

    public static void dumpIterator(ResolvedConceptReferencesIterator itr, int batchSize, boolean showIndex) {
		if (itr == null) return;
		if (batchSize != -1) {
			try {
				int knt = 0;
				while (itr.hasNext()) {
					ResolvedConceptReference[] refs =
						itr.next(batchSize).getResolvedConceptReference();
					for (ResolvedConceptReference ref : refs) {
						knt++;
						if (showIndex) {
							displayRef(knt, ref);
						} else {
							displayRef(ref);
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			try {
				int knt = 0;
				while (itr.hasNext()) {
					knt++;
					ResolvedConceptReference ref = (ResolvedConceptReference) itr.next();
					if (showIndex) {
						displayRef(knt, ref);
					} else {
						displayRef(ref);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
    }



    public static void dumpIterator(ResolvedConceptReferencesIterator itr, int batchSize, boolean showIndex, int maxReturn) {
		if (itr == null) return;
		int knt = 0;
		if (batchSize != -1) {
			try {
				while (itr.hasNext()) {
					ResolvedConceptReference[] refs =
						itr.next(batchSize).getResolvedConceptReference();
					for (ResolvedConceptReference ref : refs) {
						knt++;
						if (maxReturn != -1 && knt >= maxReturn) break;
						if (showIndex) {
							displayRef(knt, ref);
						} else {
							displayRef(ref);
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			try {
				while (itr.hasNext()) {
					knt++;
					if (maxReturn != -1 && knt >= maxReturn) break;
					ResolvedConceptReference ref = (ResolvedConceptReference) itr.next();
					if (showIndex) {
						displayRef(knt, ref);
					} else {
						displayRef(ref);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
    }




    public static void dumpIterator(ResolvedConceptReferencesIterator itr, int batchSize) {
		dumpIterator(itr, batchSize, true);
	}

    public static void dumpIterator(ResolvedConceptReferencesIterator itr) {
		dumpIterator(itr, -1);
	}
}

