package gov.nih.nci.evs.service;

import gov.nih.nci.evs.browser.utils.*;

import java.util.*;

//import org.LexGrid.LexBIG.caCore.interfaces.*;
//import org.LexGrid.LexBIG.LexBIGService.*;
//import org.LexGrid.LexBIG.Impl.*;

import gov.nih.nci.evs.browser.properties.*;
//import gov.nih.nci.system.client.*;
//import gov.nih.nci.evs.security.*;
//import gov.nih.nci.evs.browser.bean.*;
import org.apache.logging.log4j.*;

//import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;


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

public class RemoteServerUtil {
	private static Logger _logger = LogManager.getLogger(RemoteServerUtil.class);
    private static boolean _debug = false;
    private static String _serviceInfo = "EvsServiceInfo";
    private static String _serviceURL = null;

    public RemoteServerUtil() {
        // Do nothing
    }

//    public static LexBIGService createLexBIGService(String serviceUrl) {
//        try {
//            if (serviceUrl == null || serviceUrl.compareTo("") == 0 || serviceUrl.compareToIgnoreCase("null") == 0) {
//                LexBIGService lbSvc = LexBIGServiceImpl.defaultInstance();
//                return lbSvc;
//            }
//            LexEVSApplicationService lexevsService =
//                (LexEVSApplicationService) ApplicationServiceProvider
//                    .getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");
//            return (LexBIGService) lexevsService;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    public static LexEVSApplicationService registerSecurityToken(
//        LexEVSApplicationService lexevsService, String codingScheme,
//        String token) {
//        SecurityToken securityToken = new SecurityToken();
//        securityToken.setAccessToken(token);
//        Boolean retval = null;
//        try {
//            retval =
//                lexevsService
//                    .registerSecurityToken(codingScheme, securityToken);
//            if (retval != null && retval.equals(Boolean.TRUE)) {
//                // _logger.debug("Registration of SecurityToken was successful.");
//            } else {
//                _logger.error("WARNING: Registration of SecurityToken failed.");
//            }
//        } catch (Exception e) {
//            _logger.error("WARNING: Registration of SecurityToken failed.");
//        }
//        return lexevsService;
//    }

    // KLO 100709
//    public static LexBIGService createLexBIGService(String serviceUrl,
//        String codingScheme, String token) {
//        SecurityToken securityToken = new SecurityToken();
//        securityToken.setAccessToken(token);
//        return createLexBIGService(serviceUrl, codingScheme, securityToken);
//    }

    // KLO 100709
//    public static LexBIGService createLexBIGService(String serviceUrl,
//        String codingScheme, SecurityToken securityToken) {
//        try {
//
//            LexBIGService lbSvc = LexBIGServiceImpl.defaultInstance();
//            return lbSvc;
////            if (serviceUrl == null || serviceUrl.compareTo("") == 0 || serviceUrl.compareToIgnoreCase("null") == 0) {
////                LexBIGService lbSvc = LexBIGServiceImpl.defaultInstance();
////                return lbSvc;
////            }
////
////            LexEVSApplicationService lexevsService =
////                (LexEVSApplicationService) ApplicationServiceProvider
////                    .getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");
////
////            Boolean retval = false;
////            retval =
////                lexevsService
////                    .registerSecurityToken(codingScheme, securityToken);
////
////            if (retval.equals(Boolean.TRUE)) {
////                // _logger.debug("Registration of SecurityToken was successful.");
////            } else {
////                _logger.error("WARNING: Registration of SecurityToken failed.");
////            }
////
////            _logger.debug("Connected to " + serviceUrl);
////            //ServerMonitorThread.getInstance().monitor(lexevsService, "createLexBIGService:3");
////            return (LexBIGService) lexevsService;
//        } catch (Exception e) {
//            _logger.error("Unable to connected to " + serviceUrl);
//            e.printStackTrace();
//        }
//        //ServerMonitorThread.getInstance().monitor(null, "createLexBIGService:3");
//        return null;
//    }

//    public static LexBIGService createLexBIGService(String serviceUrl,
//        boolean registerSecurityTokens) {
//        try {
//            LexEVSApplicationService lexevsService =
//                (LexEVSApplicationService) ApplicationServiceProvider
//                    .getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");
//
//			if (registerSecurityTokens) {
//                lexevsService = registerSecurityToken(lexevsService, "MedDRA", "10382");
//			}
//
//            return (LexBIGService) lexevsService;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //ServerMonitorThread.getInstance().monitor(null, "createLexBIGService:5");
//        return null;
//    }

//    public static LexEVSDistributed getLexEVSDistributed(String serviceUrl) {
//		try {
//			LexEVSDistributed distributed =
//				(LexEVSDistributed)
//				ApplicationServiceProvider.getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");
//	        //ServerMonitorThread.getInstance().monitor(distributed, "getLexEVSDistributed:2");
//			return distributed;
//		} catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//	}

//    public static LexEVSValueSetDefinitionServices getLexEVSValueSetDefinitionServices(String serviceUrl) {
//		if (serviceUrl == null || serviceUrl.compareTo("") == 0 || serviceUrl.compareToIgnoreCase("null") == 0) {
//			return LexEVSValueSetDefinitionServicesImpl.defaultInstance();
//		}
//		try {
//			LexEVSDistributed distributed =
//				(LexEVSDistributed)
//				ApplicationServiceProvider.getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");
//
//			LexEVSValueSetDefinitionServices vds = distributed.getLexEVSValueSetDefinitionServices();
//			return vds;
//		} catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//	}
}
