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

public class MapsToReportProcessor {
    String maps_to_file = null;
    Vector data_vec = null;
    Vector terminologies = null;
    String version = null;

    public MapsToReportProcessor(String maps_to_file) {
        this.maps_to_file = maps_to_file;
        this.data_vec = Utils.readFile(maps_to_file);
        this.terminologies = findTargetTerminologies();
    }

    public MapsToReportProcessor(Vector data_vec) {
        this.data_vec = data_vec;
         this.terminologies = findTargetTerminologies();
    }

    public void setVersion(String version) {
		this.version = version;
	}

    //C17998|Unknown|Has Synonym|RG|unknown|PT|GDC|N/A
    public Vector findTargetTerminologies() {
		Vector terminologies = new Vector();
		for (int i=0; i<data_vec.size(); i++) {
			String t = (String) data_vec.elementAt(i);
			Vector u = StringUtils.parseData(t, '|');
			String terminology = (String) u.elementAt(6);
			if (!terminologies.contains(terminology)) {
				terminologies.add(terminology);
			}
		}
		return new SortUtils().quickSort(terminologies);
	}

	public Vector getTargetTerminologies() {
		return this.terminologies;
	}

	//NCIt Maps_To Target_Terminology Name

	public static String getMapsToMappingyDisplayName(String terminology, String version) {
		String t = "NCIt Maps_To " + terminology;
		t = t.replaceAll(" ", "_");
		t = t + " (" + version + ")";
		return t;
	}

	public static String getMapsToMappingyName(String terminology) {
		String t = "NCIt Maps_To " + terminology;
		t = t.replaceAll(" ", "_");
		return t;
	}

/*
    public static void main(String[] args) {
		long ms = System.currentTimeMillis();
		String maps_to_file = "Maps_To.txt";
        MapsToReportProcessor test = new MapsToReportProcessor(maps_to_file);
        test.setVersion("19.09e");
        Vector w = test.getTargetTerminologies();
        Utils.dumpVector("w", w);

        for (int i=0; i<w.size(); i++) {
		    String t = (String) w.elementAt(i);
		    //System.out.println(t);
		    System.out.println(test.getMappingTerminologyLabel(t));
		}

        System.out.println("Total run time (ms): " + (System.currentTimeMillis() - ms));
	}
*/
}

//  Maps_To_Mapping_Data: NCI Thesaurus Maps To Target Terminologies (19.09e)
