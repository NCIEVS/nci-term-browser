package gov.nih.nci.evs.browser.utils;

import java.io.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.*;
import java.util.*;
import org.apache.commons.io.FilenameUtils;


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


public class FTPCrawlerRunner {

    public static Vector run() {
         Vector otherMappingData = FTPCrawler.getOtherMappingData();

         Utils.dumpVector("FTPCrawler.getOtherMappingData()", otherMappingData);
         int knt = 0;
         boolean noReleasedData = true;
         for (int i=0; i<otherMappingData.size(); i++) {
             String line = (String) otherMappingData.elementAt(i);
             Vector u = StringUtils.parseData(line, '|');
             String s1 = (String) u.elementAt(1);
             String s2 = (String) u.elementAt(2);
             if (s2 != null && s2.compareTo("null") == 0) {
				 knt++;
			 } else if (s1.compareTo(s2) == 0) {
				 knt++;
			 }
			 /*
             if (s1.compareTo(s2) != 0) {
                 noReleasedData = false;
                 break;
             }
             */
         }
         if (knt == otherMappingData.size()) {
             otherMappingData = FTP_Crawler.getOtherMappingData();
             Utils.dumpVector("FTP_Crawler.getOtherMappingData()", otherMappingData);
         }
         Utils.dumpVector("final getOtherMappingData()", otherMappingData);
         return otherMappingData;
    }

    public static void main(String[] args) {
         Vector v = run();
         Utils.saveToFile("final_otherMappingData.txt", v);
    }

}