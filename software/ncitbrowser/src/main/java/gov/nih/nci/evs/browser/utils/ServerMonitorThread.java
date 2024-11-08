package gov.nih.nci.evs.browser.utils;

import gov.nih.nci.evs.browser.properties.NCItBrowserProperties;

import java.util.Date;

import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
//import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
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

public class ServerMonitorThread extends Thread {
    @SuppressWarnings("unused")
	private static Logger _logger = LogManager.getLogger(ServerMonitorThread.class);
    private static String _className = ServerMonitorThread.class.getSimpleName();
    private static ServerMonitorThread _instance = null;
	private long _interval = 1000 *
	    NCItBrowserProperties.getIntProperty(
	        NCItBrowserProperties.PING_LEXEVS_INTERVAL, 600);
	private String _message = "";
	private static boolean _enabled =
	    NCItBrowserProperties.getBooleanProperty(
	        NCItBrowserProperties.PING_LEXEVS_ENABLED, true);
    private Boolean _isThreadRunning = false;
    private boolean _isLexEVSRunning = true;
    private boolean _debug = false;  //DYEE_DEBUG (Default: false)

	static {
	    if (_enabled) //DYEE_DEBUG (Default: _enabled)
	        ServerMonitorThread.getInstance().start();
	}

	private ServerMonitorThread() {
	}

	private static void debug(String text) {
	    if (_logger == null) {
	        //System.out.println(_className + ": " + text);
	    } else {
	        _logger.debug(text);
	    }
	}

	public static ServerMonitorThread getInstance() {
	    if (_instance == null)
	        _instance = new ServerMonitorThread();
	    return _instance;
	}

	public void run() {
	    synchronized(_isThreadRunning) {
	        //Note: Ensures only one instance of this method is running.
    	    if (_isThreadRunning)
    	        return;
    	    _isThreadRunning = true;
	    }

		while (true) {
			try {
			    LexBIGService service = RemoteServerUtil.createLexBIGService();
		        monitor(service, "ServerMonitorThread");
				sleep(_interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

    public boolean isLexEVSRunning() {
        if (! _enabled) {
            // Note: Pretend it is running fine to avoid displaying warning message.
            return true;
        }

        //Quick test.
        LexBIGService service = RemoteServerUtil.createLexBIGService();
        monitor(service, "ServerMonitorThread2");
        return _isLexEVSRunning;
    }

	public void setLexEVSRunning(boolean isRunning, String msg) {
        if (! _enabled)
            return;

	    if (_debug && msg != null && msg.length() > 0)
	        debug("isRunning(" + isRunning + "): " + msg);
        boolean prevIsRunning = _isLexEVSRunning;
        if (isRunning == prevIsRunning)
            return;

        _isLexEVSRunning = isRunning;
        updateMessage(isRunning);
        debug("_isLexEVSRunning: " + _isLexEVSRunning);
	}

    public String getMessage() {
        return _message;
    }

/*
    private void updateMessage(boolean isRunning) {
        if (isRunning) {
            _message = "";
            return;
        }

        _message = "*** The server is temporarily not available, as of "
            + new Date() + ". ***";
	}
*/

    private static String updateMessage(boolean isRunning) {
		String _message = null;
        if (isRunning) {
            _message = "";
            return _message;
        }

//        _message = "*** The server is temporarily not available, as of "
//            + new Date() + ". ***";

          _message = "*** The server is temporarily not available, as of "
            + new Date() + ". ("
            + "<a href=\"https://evsexplore.semantics.cancer.gov/\">EVS Explore browser</a>"
            + " is available for your searching needs.) ***";

        return _message;
	}

    public void monitor(LexBIGService service, String msg) {
        if (! _enabled)
            return;
        if (service == null) {
            setLexEVSRunning(false, msg);
            return;
        }
        try {
            service.getLastUpdateTime();
            setLexEVSRunning(true, msg);
        } catch (Exception e) {
            error(e);
            setLexEVSRunning(false, msg);
        }
    }

//    public void monitor(LexEVSDistributed service, String msg) {
//        if (! _enabled)
//            return;
//        if (service == null) {
//            setLexEVSRunning(false, msg);
//            return;
//        }
//        try {
//            service.getLastUpdateTime();
//            setLexEVSRunning(true, msg);
//        } catch (Exception e) {
//            error(e);
//            setLexEVSRunning(false, msg);
//        }
//    }

    private void error(Exception e) {
        //Note: Trying to solve Kim's problem with this method.
        //  He is getting exceptions when log4j tries to print an error message.
        String msg = "";
        if (e != null)
            msg = e.getClass().getSimpleName() + ": " + e.getMessage();
        else {
			//msg = "Exception e == " + e;

			msg = "Exception thrown ";

		}

        try {
            debug(msg);
        } catch (Exception e1) {
            //System.out.println(_className + ": " + e1.getMessage());
            //System.out.println(_className + ": " + msg);
            e1.printStackTrace();
        }
    }
}
