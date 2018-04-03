
package gov.nih.nci.evs.browser.utils;

import java.net.*;
import java.io.*;
import java.util.*;

import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;

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

    public static void dumpIterator(ResolvedConceptReferencesIterator itr, int batchSize, boolean showIndex) {
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

    public static void dumpIterator(ResolvedConceptReferencesIterator itr, int batchSize) {
		dumpIterator(itr, batchSize, true);
	}

    public static void dumpIterator(ResolvedConceptReferencesIterator itr) {
		dumpIterator(itr, -1);
	}
}

