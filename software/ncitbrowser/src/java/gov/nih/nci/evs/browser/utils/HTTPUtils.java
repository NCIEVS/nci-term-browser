package gov.nih.nci.evs.browser.utils;

import java.util.*;
import java.util.regex.*;

import javax.faces.context.*;
import javax.servlet.http.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * HTTP Utility methods
 * @author garciawa2
 *
 */
public class HTTPUtils {

  private static String REFERER = "referer";


    /**
	 * Remove potentially bad XSS syntax
	 * @param value
	 * @return
	 */

	public static String cleanXSS(String value) {

		if (value == null || value.length() < 1)
			return value;

		// Remove XSS attacks
		value = replaceAll(value,"<\\s*script\\s*>.*</\\s*script\\s*>", "");
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		value = value.replaceAll("'", "&#39;");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = replaceAll(value,"[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		value = value.replaceAll("\"", "&quot;");
		return value;

	}

	/**
	 * @param string
	 * @param regex
	 * @param replaceWith
	 * @return
	 */
	public static String replaceAll(String string, String regex, String replaceWith) {

		Pattern myPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		string = myPattern.matcher(string).replaceAll(replaceWith);
		return string;

	}

    public static void printRequestSessionAttributes() {
        System.out.println(" ");
        System.out.println(Utils.SEPARATOR);
        System.out.println("Request Session Attribute(s):");

        try {
            HttpServletRequest request = (HttpServletRequest)FacesContext.
                getCurrentInstance().getExternalContext().getRequest();

            HttpSession session = request.getSession();
            Enumeration<?> enumeration = SortUtils.sort(session.getAttributeNames());
            int i=0;
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                Object value = session.getAttribute(name);
                System.out.println("  " + i + ") " + name + ": " + value);
                ++i;
            }
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + ": " +
                e.getMessage());
        }
    }

    public static void printRequestAttributes() {
        System.out.println(" ");
        System.out.println(Utils.SEPARATOR);
        System.out.println("Request Attribute(s):");

        try {
            HttpServletRequest request = (HttpServletRequest)FacesContext.
                getCurrentInstance().getExternalContext().getRequest();

            Enumeration<?> enumeration = SortUtils.sort(request.getAttributeNames());
            int i=0;
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                Object value = request.getAttribute(name);
                System.out.println("  " + i + ") " + name + ": " + value);
                ++i;
            }
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + ": " +
                e.getMessage());
        }
	}

    public static void printRequestParameters() {
        System.out.println(" ");
        System.out.println(Utils.SEPARATOR);
        System.out.println("Request Parameter(s):");

        try {
            HttpServletRequest request = (HttpServletRequest)FacesContext.
                getCurrentInstance().getExternalContext().getRequest();

            Enumeration<?> enumeration = SortUtils.sort(request.getParameterNames());
            int i=0;
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                Object value = request.getParameter(name);
                System.out.println("  " + i + ") " + name + ": " + value);
                ++i;
            }
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + ": " +
                e.getMessage());
        }
    }

	public static void printAttributes() {
	    printRequestSessionAttributes();
	    printRequestAttributes();
        printRequestParameters();
        System.out.println(" ");
	}

	public static String convertJSPString(String t) {
	// Convert problem characters to JavaScript Escaped values
	  if (t == null) {
		return "";
	  }

	  if (t.compareTo("") == 0) {
		return "";
	  }

	  String sigleQuoteChar = "'";
	  String doubleQuoteChar = "\"";

	  String dq = "&quot;";

	  t = t.replaceAll(sigleQuoteChar,"\\" + sigleQuoteChar);
	  t = t.replaceAll(doubleQuoteChar,"\\" + dq);
	  t = t.replaceAll("\r","\\r");  // replace CR with \r;
	  t = t.replaceAll("\n","\\n");  // replace LF with \n;

	  return cleanXSS(t);
	}

  /**
   * @param request
   * @return
   */
  public static String getRefererParmEncode(HttpServletRequest request) {
    String iref = request.getHeader(REFERER);
    String referer = "N/A";
    if (iref != null)
      try {
        referer = URLEncoder.encode(iref,"UTF-8");
      } catch (UnsupportedEncodingException e) {
        // return N/A if encoding is not supported.
      }
    return cleanXSS(referer);
  }

  /**
   * @param request
   * @return
   */
  public static String getRefererParmDecode(HttpServletRequest request) {
    String refurl = "N/A";
    try {
      String iref = request.getParameter(REFERER);
      if (iref != null)
        refurl = URLDecoder.decode(request.getParameter(REFERER),
            "UTF-8");
    } catch (UnsupportedEncodingException e) {
      // return N/A if encoding is not supported.
    }
    return cleanXSS(refurl);
  }

  /**
   * @param request
   */
  public static void clearRefererParm(HttpServletRequest request) {
    request.setAttribute(REFERER, null);
  }

  /**
   * @return
   */
  public static HttpServletRequest getRequest() {
    return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
  }
}
