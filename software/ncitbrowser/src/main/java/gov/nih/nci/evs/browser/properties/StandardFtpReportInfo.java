package gov.nih.nci.evs.browser.properties;

import gov.nih.nci.evs.browser.utils.*;

import java.util.*;

import org.apache.logging.log4j.*;

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
 */

public class StandardFtpReportInfo {
	private static Logger _logger = LogManager.getLogger(StandardFtpReportInfo.class);
    private String _name = "";
    private String _url = "";
    private Vector<Integer> _ncitColumns = new Vector<Integer>();

    public StandardFtpReportInfo(String name, String url,
        Vector<Integer> ncitColumns) {
        _name = name;
        _url = url;
        _ncitColumns = ncitColumns;
    }

    public String getName() {
        return _name;
    }

    public String getUrl() {
        return _url;
    }

    public Vector<Integer> getNcitColumns() {
        return _ncitColumns;
    }

    public String toString() {
        return "name=" + _name + ", url=" + _url + ", ncitColumns="
            + _ncitColumns.toString();
    }

    public static Vector<StandardFtpReportInfo> parse(String propertyName,
        int max) throws Exception {
        Vector<StandardFtpReportInfo> list =
            new Vector<StandardFtpReportInfo>();
        for (int i = 0; i < max; ++i) {
            String propertyValue =
                NCItBrowserProperties.getInstance().getProperty(
                    propertyName + "_" + i);
            StandardFtpReportInfo info = parse(propertyName, propertyValue);
            if (info == null)
                continue;
            list.add(info);
        }
        return list;
    }

    public static StandardFtpReportInfo parse(String propertyName,
        String propertyValue) {
        if (propertyValue == null)
            return null;
        String[] values = Utils.toStrings(propertyValue, ";", false);
        if (values == null) return null;
        if (values.length == 1 && values[0].trim().startsWith("$"))
            return null;
        if (values.length < 3) {
            _logger.error("Error parsing property: " + propertyName);
            _logger
                .error("  * Missing some values.  Format: Name ; URL ; NCIt Columns");
            _logger.error("  * Current value: " + propertyValue);
            return null;
        }

        String name = values[0];
        String url = values[1];

        Vector<Integer> ncitColumns = new Vector<Integer>();
        String[] values2 = Utils.toStrings(values[2], " ", false);
        if (values2 == null) return null;
        for (int i = 0; i < values2.length; ++i) {
            try {
                int col = Integer.parseInt(values2[i]);
                ncitColumns.add(col);
            } catch (Exception e) {
                _logger.error(e.getClass().getSimpleName() + ": "
                    + e.getMessage());
                _logger
                    .error("  * Could not parse NCIt columns from property value: "
                        + propertyValue);
            }
        }

        StandardFtpReportInfo report =
            new StandardFtpReportInfo(name, url, ncitColumns);
        return report;
    }

    public static StandardFtpReportInfo getByName(
        Vector<StandardFtpReportInfo> list, String name) {
        Iterator<StandardFtpReportInfo> iterator = list.iterator();
        if (iterator == null) return null;
        while (iterator.hasNext()) {
            StandardFtpReportInfo info = iterator.next();
            if (info == null) return null;
            if (info.getName().equals(name))
                return info;
        }
        return null;
    }
}
