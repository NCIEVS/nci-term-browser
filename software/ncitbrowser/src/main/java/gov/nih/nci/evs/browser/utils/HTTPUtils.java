package gov.nih.nci.evs.browser.utils;

import java.util.*;

import javax.faces.context.*;
import javax.servlet.http.*;

import java.io.*;
import java.net.*;
import java.util.regex.*;

import org.apache.logging.log4j.*;

import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.properties.*;


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
 * HTTP Utility methods
 *
 * @author garciawa2
 *
 */
public class HTTPUtils {
	private static Logger _logger = LogManager.getLogger(HTTPUtils.class);
    private static final String REFERER = "referer";
    private static final int MAX_FONT_SIZE = 29;
    private static final int MIN_FONT_SIZE = 22;
    private static final int MAX_STR_LEN = 18;

    public static final int ABS_MAX_STR_LEN = 200;

    /**
     * Remove potentially bad XSS syntax
     *
     * @param value
     * @return
     */

/*
    public static String cleanXSS(String value) {

        if (value == null || value.length() < 1)
            return value;

        // Remove XSS attacks
        value = replaceAll(value, "<\\s*script\\s*>.*</\\s*script\\s*>", "");
        value = replaceAll(value, ".*<\\s*iframe.*>", "");
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value =
            replaceAll(value, "[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']",
                "\"\"");
        value = value.replaceAll("\"", "&quot;");
        return value;

    }
*/

    public HTTPUtils() {

	}

    public static boolean isPositiveEven(int num) {
		return ((num % 2) == 0 && num > 0);
	}

	public static int getCount(String s, char c) {
		int num = 0;
		if (s == null) return num;
		for (int i=0; i<s.length(); i++) {
			char ch = s.charAt(i);
			if (ch == c) num++;
		}
		return num;
	}

	public static boolean checkPotentialMaliciousContent(String s) {
		if (s == null) return false;
		//[NCITERM-806] Fixing AppScan Issue from Security Team.
		if (s.indexOf(");") != -1) return true;
		// appscan response:
		//if (StringUtils.isSmallIrrationalNumber(s)) return true;
		char c1 = '<';
		char c2 = '>';
		char c3 = '/';
		int k1 = getCount(s, c1);
		int k2 = getCount(s, c2);
		int k3 = getCount(s, c3);
		if (isPositiveEven(k1) && isPositiveEven(k2) && k3 > 0) {
            return maybeMalicious(s, c3, c2);
		}
		return false;
	}


	public static boolean maybeMalicious(String s, char c1, char c2) {
		//</script>
		if (s == null) return false;
		String s1 = Character.toString(c1);
		String s2 = Character.toString(c2);

		int n1 = s.lastIndexOf(s1);
		int n2 = s.lastIndexOf(s2);

		if (n1 == -1 || n2 == -1) return false;
		if (n1 > n2) return false;
		return true;
	}


//ns=ncit21681%27);confirm(1234)//301
    public static String cleanMatchTextXSS(String value) {
		if (value == null) return null;
		value = value.trim();

		if (value.compareTo(">") == 0) return cleanXSS(value);
		if (value.compareTo("<") == 0) return cleanXSS(value);

		boolean retval = checkPotentialMaliciousContent(value);
		if (retval) {
			value = cleanXSS(value);
		}
		return value;
	}

/*
    public static String cleanXSS(String value) {
        if (value == null) return null;
        value = value.trim();

        try {
			//if (value.length() == 0) return value;
			if (value.length() <= 1) return value;
			//[NCITERM-806] Fixing AppScan Issue from Security Team.
			value = value.replaceAll(");", "");

			// Remove XSS attacks
			value = replaceAll(value, "<\\s*script\\s*>.*</\\s*script\\s*>", "");
			value = replaceAll(value, ".*<\\s*iframe.*>", "");
			value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
			//value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");

			//[NCITERM-679] Terms with apostrophes return no results.
			//value = value.replaceAll("'", "&#39;");
			value = value.replaceAll("eval\\((.*)\\)", "");
			value =
				replaceAll(value, "[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']",
					"\"\"");
			value = value.replaceAll("\"", "&quot;");
		} catch (Exception e) {
			e.printStackTrace();
		}

        return value;

    }
*/

    public static String cleanXSS(String value) {
        if (value == null) return null;
		value = value.trim();
		//if (value.length() == 0) return value;
		if (value.length() <= 1) {
			return value;
		}
		// Remove XSS attacks
		value = replaceAll(value, "<\\s*script\\s*>.*</\\s*script\\s*>", "");
		value = replaceAll(value, ".*<\\s*iframe.*>", "");
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		//value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		//[NCITERM-679] Terms with apostrophes return no results.
		//value = value.replaceAll("'", "&#39;");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = replaceAll(value, "[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']",   "\"");
		value = value.replaceAll("\"", "&quot;");

        return value;
    }



    public static String appendNCIT(String link) {
    	String nciturl = null;
    	if (link.contains("/ncitbrowser")) return link;
    	if (link.endsWith("/"))	link = nciturl + "ncitbrowser";
    	//link = nciturl + "/ncitbrowser";
    	else link = nciturl + "/ncitbrowser";
    	return link;
    }

    /**
     * Calculate a max font size for the length of the text to be
     * 	displayed.
     * @param value
     * @param width
     * @return
     */
    public static int maxFontSize(String value) {
    	int size;
		if (value == null || value.length() == 0)
			size = MAX_FONT_SIZE;
		else if (value.length() >= MAX_STR_LEN)
			size = MIN_FONT_SIZE;
		else {
			// Calculate an intermediate font size
			/*
			size = MIN_FONT_SIZE
					+ Math.round((MAX_FONT_SIZE / MAX_STR_LEN)
							/ (MIN_FONT_SIZE / value.length()));
		    */
			size = MIN_FONT_SIZE
					+ Math.round(((float) MAX_FONT_SIZE / (float) MAX_STR_LEN)
							/ ((float) MIN_FONT_SIZE / (float) value.length()));
		}
    	return size;
    }

    /**
     * @param string
     * @param regex
     * @param replaceWith
     * @return
     */
    public static String replaceAll(String string, String regex,
        String replaceWith) {

        Pattern myPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        string = myPattern.matcher(string).replaceAll(replaceWith);
        return string;

    }

    public static void printRequestSessionAttributes() {
        _logger.debug(" ");
        _logger.debug(Utils.SEPARATOR);
        _logger.debug("Request Session Attribute(s):");

        try {
            HttpServletRequest request =
                (HttpServletRequest) FacesContext.getCurrentInstance()
                    .getExternalContext().getRequest();

            HttpSession session = request.getSession();
            Enumeration<?> enumeration =
                new SortUtils().sort(session.getAttributeNames());
            int i = 0;
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                Object value = session.getAttribute(name);
                _logger.debug("  " + i + ") " + name + ": " + value);
                ++i;
            }
        } catch (Exception e) {
            _logger.error(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    public static void printRequestAttributes() {
        _logger.debug(" ");
        _logger.debug(Utils.SEPARATOR);
        _logger.debug("Request Attribute(s):");

        try {
            HttpServletRequest request =
                (HttpServletRequest) FacesContext.getCurrentInstance()
                    .getExternalContext().getRequest();

            Enumeration<?> enumeration =
                new SortUtils().sort(request.getAttributeNames());
            int i = 0;
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                Object value = request.getAttribute(name);
                _logger.debug("  " + i + ") " + name + ": " + value);
                ++i;
            }
        } catch (Exception e) {
            _logger.error(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    public static void printRequestParameters() {
        _logger.debug(" ");
        _logger.debug(Utils.SEPARATOR);
        _logger.debug("Request Parameter(s):");

        try {
            HttpServletRequest request =
                (HttpServletRequest) FacesContext.getCurrentInstance()
                    .getExternalContext().getRequest();

            Enumeration<?> enumeration =
                new SortUtils().sort(request.getParameterNames());
            int i = 0;
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();

                //Object value = cleanXSS((String) request.getParameter(name));
                String value = (String) request.getParameter(name);
                //_logger.debug("  " + i + ") " + name + ": " + value);
                System.out.println("name: " + name + " value: " + value.toString());
                ++i;
            }
        } catch (Exception e) {
			e.printStackTrace();
            //_logger.error(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    public static void printRequestParameters(HttpServletRequest request) {
        _logger.debug(" ");
        _logger.debug(Utils.SEPARATOR);
        _logger.debug("Request Parameter(s):");

        try {
            Enumeration<?> enumeration =
                new SortUtils().sort(request.getParameterNames());
            int i = 0;
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();

                //Object value = cleanXSS((String) request.getParameter(name));
                String value = (String) request.getParameter(name);
                //_logger.debug("  " + i + ") " + name + ": " + value);
                System.out.println("name: " + name + " value: " + value.toString());
                ++i;
            }
        } catch (Exception e) {
			e.printStackTrace();
            //_logger.error(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    public static void printAttributes() {
        printRequestSessionAttributes();
        printRequestAttributes();
        printRequestParameters();
        _logger.debug(" ");
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

        t = t.replaceAll(sigleQuoteChar, "\\" + sigleQuoteChar);
        t = t.replaceAll(doubleQuoteChar, "\\" + dq);
        t = t.replaceAll("\r", "\\r"); // replace CR with \r;
        t = t.replaceAll("\n", "\\n"); // replace LF with \n;

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
                referer = URLEncoder.encode(iref, "UTF-8");
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
            String iref = cleanXSS((String) request.getParameter(REFERER));
            if (iref != null)
                refurl =
                    URLDecoder.decode(request.getParameter(REFERER), "UTF-8");
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
        return (HttpServletRequest) FacesContext.getCurrentInstance()
            .getExternalContext().getRequest();
    }


	public static String encode(String in) {
		String retVal = "";
		try {
		    retVal = URLEncoder.encode(in, "UTF8");
		} catch (UnsupportedEncodingException ex) {
		    ex.printStackTrace();
		}
		return retVal;
	}

    public static String decode(String t) {
		String retVal = "";
		try {
        	retVal = URLDecoder.decode(t, "UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return retVal;
	}

	public static boolean isValueSetURI(String key) {
		if (key == null) return false;
		if (ValueSetDefinitionConfig.getValueSetConfig(key) != null) return true;
		return false;
	}

	// type: name=1; value=2
	public static String createErrorMessage(int type, String name) {
		if (type == 1) {
			System.out.println("WARNING: Unknown parameter name encountered - '" + cleanXSS(name) + "'.");
			return "WARNING: Invalid parameter name encountered -- please check your URL and try again. ";
		} else {
			System.out.println("WARNING: Invalid parameter value encountered - " + " (name: " + cleanXSS(name) + ").");
			return "WARNING: Invalid parameter value encountered - " + " (name: " + cleanXSS(name) + ").";
		}
	}

	public static String createErrorMessage(String name, String value) {
		return createErrorMessage(name, value, 0);

		//System.out.println("WARNING: Invalid parameter value encountered - " + " (name: " + cleanXSS(name) + " value: " + value + ").");
		//return "WARNING: Invalid parameter name and/or value encountered -- please check your URL and try again. ";
	}

	public static String createErrorMessage(String name, String value, int source) {
		if (source != 0) {
			System.out.println("createErrorMessage source: " + source);
		}
		System.out.println("WARNING: Invalid parameter value encountered - " + " (name: " + cleanXSS(name) + " value: " + value + ").");
		return "WARNING: Invalid parameter name and/or value encountered -- please check your URL and try again. ";
	}

	public static boolean validateRequestParameters(HttpServletRequest request) {
		List list = HTTPParameterConstants.HTTP_REQUEST_PARAMETER_NAME_LIST;
		String value = null;
        try {
            Enumeration<?> enumeration =
                new SortUtils().sort(request.getParameterNames());

            while (enumeration.hasMoreElements()) {
				String name = (String) enumeration.nextElement();
				if (!name.startsWith("code_")) {

					Boolean checkedVocabularies = isCheckedVocabulariesParameter(name);
					if (checkedVocabularies != null && checkedVocabularies.equals(Boolean.TRUE)) {
						value = (String) request.getParameter(name);
						//KLO, 12082023
						if (value.indexOf("eval(") != -1) {
							String error_msg = createErrorMessage(2, name);
							request.getSession().setAttribute("error_msg", error_msg);
							System.out.println("WARNING: Invalid value of the parameter " + name + ": " + value);
							return false;
						}

						if (!DataUtils.isNullOrBlank(value)) {
							Vector selected_vocabularies_vec = gov.nih.nci.evs.browser.utils.StringUtils.parseData(name, ",");
							String nm = name.toLowerCase(Locale.ENGLISH);
							if (nm == null) return false;
							if (nm.compareTo("selected_vocabularies") != 0) {
								for (int k=0; k<selected_vocabularies_vec.size(); k++) {
									String vocabularyNm = (String) selected_vocabularies_vec.elementAt(k);
									String formal_name = DataUtils.getFormalName(vocabularyNm);

									if (formal_name == null && vocabularyNm.indexOf("evs.nci.nih.gov/valueset") == 0) {
										String error_msg = createErrorMessage(2, name);
										request.getSession().setAttribute("error_msg", error_msg);
										System.out.println("WARNING: Unknown vocabulary: " + value);
										return false;
									}
								}
							}
						}
					}

					if (name.compareTo("nav_type") == 0) {
						value = (String) request.getParameter(name);
						String[] types = Constants.NAV_TYPE_VALUES;
						if (!Arrays.asList(types).contains(value)) {
							String error_msg = createErrorMessage(2, name);
							request.getSession().setAttribute("error_msg", error_msg);
							System.out.println("WARNING: Unknown nav_type: " + value);
							return false;
						}
					}

					if (name.compareTo("value_set_home") == 0) {
						value = (String) request.getParameter(name);
						String[] types = Constants.TRUE_OR_FALSE;
						if (!Arrays.asList(types).contains(value)) {
							String error_msg = createErrorMessage(2, name);
							request.getSession().setAttribute("error_msg", error_msg);
							System.out.println("WARNING: Unknown nav_type: " + value);
							return false;
						}
					}

					if (name.compareTo("view") == 0) {
						value = (String) request.getParameter(name);
						if (value != null) {
							boolean isInteger = gov.nih.nci.evs.browser.utils.StringUtils.isInteger(value);
							if (!isInteger) {
								System.out.println("Integer value violation???");
								String error_msg = createErrorMessage(name, value, 1);
								request.getSession().setAttribute("error_msg", error_msg);
								return false;
							}
						}
					}
	/*
	temporarily commented out
					if (name.compareTo("vsd_uri") == 0) {
						value = (String) request.getParameter(name);
						if (!DataUtils.isNull(value)) {
							//https://tracker.nci.nih.gov/browse/NCITERM-715
							//WARNING: Invalid parameter value encountered -  (name: vsd_uri value: TVS_CDISC)
							if (!value.startsWith("TVS_")) {
								String vsd_md = DataUtils.getValueSetDefinitionMetadata(value);
								if (vsd_md == null) {
									String error_msg = createErrorMessage(name, value, 2);
									request.getSession().setAttribute("error_msg", error_msg);
									return false;
								}
							}
						}
					}
	*/
					Boolean issearchFormParameter = isSearchFormParameter(name);
					// 09182015
					if (issearchFormParameter != null && issearchFormParameter.equals(Boolean.TRUE)) {
						value = (String) request.getParameter(name);
						if (value != null) {
							boolean isInteger = gov.nih.nci.evs.browser.utils.StringUtils.isInteger(value);
							if (!isInteger) {
								System.out.println("Integer value violation???" + value);
								String error_msg = createErrorMessage(name, value, 3);
								request.getSession().setAttribute("error_msg", error_msg);
								return false;
							}
						}
					}

					if (issearchFormParameter != null && issearchFormParameter.equals(Boolean.FALSE)) {
						Boolean isDynamic = isDynamicId(name);

						if (isDynamic != null && isDynamic.equals(Boolean.FALSE)) {
							if (name.endsWith("value=")) return true;
							String formal_name = DataUtils.getFormalName(name);
							if (formal_name == null && !name.startsWith("TVS_") && !name.startsWith("http:") && !list.contains(name)) {
								System.out.println("WARNING: parameter name: " + name + " is not in the list.");
								String error_msg = createErrorMessage(1, name);
								request.getSession().setAttribute("error_msg", error_msg);
								return false;
							}
							value = (String) request.getParameter(name);
							Boolean bool_obj = containsHazardCharacters(value);
							// Cross-Site Scripting:
							if (bool_obj != null && bool_obj.equals(Boolean.TRUE)) {
								String error_msg = createErrorMessage(2, name);
								request.getSession().setAttribute("error_msg", error_msg);
								System.out.println("WARNING: Hazardous -- " + name + ": " + value);
								return false;
							}
							bool_obj = validateRadioButtonNameAndValue(name, value);
							if (bool_obj != null && bool_obj.equals(Boolean.FALSE)) {
								String error_msg = createErrorMessage(name, value, 4);
								request.getSession().setAttribute("error_msg", error_msg);
								return false;
							}
							bool_obj = containsPercentSign(name, value);
							if (bool_obj != null && bool_obj.equals(Boolean.FALSE)) {
								String error_msg = createErrorMessage(name, value, 5);
								request.getSession().setAttribute("error_msg", error_msg);
								return false;
							}
							bool_obj = validateValueSetCheckBox(name, value);
							if (bool_obj != null && bool_obj.equals(Boolean.FALSE)) {
								String error_msg = createErrorMessage(name, value, 6);
								request.getSession().setAttribute("error_msg", error_msg);
								return false;
							}
							//09182015
							bool_obj = checkLimitedLengthCondition(name, value);
							if (bool_obj != null && bool_obj.equals(Boolean.FALSE)) {
								String error_msg = createErrorMessage(name, value, 7);
								request.getSession().setAttribute("error_msg", error_msg);
								return false;
							}
						}
					}
			    }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
	}



	public static Boolean validateRadioButtonNameAndValue(String name, String value) {
		if (name == null || value == null || value.length() == 0) return null;


		if (name.compareTo("adv_search_algorithm") == 0) {
			if (HTTPParameterConstants.adv_search_algorithm_value_list.contains(value)) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		} else if (name.compareTo("algorithm") == 0) {
			if (HTTPParameterConstants.algorithm_value_list.contains(value)) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		} else if (name.compareTo("direction") == 0) {
			if (HTTPParameterConstants.direction_value_list.contains(value)) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}

		} else if (name.compareTo("searchTarget") == 0) {
			if (HTTPParameterConstants.searchTarget_value_list.contains(value)) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		} else if (name.compareTo("selectSearchOption") == 0) {
			if (HTTPParameterConstants.selectSearchOption_value_list.contains(value)) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		} else if (name.compareTo("selectValueSetSearchOption") == 0) {
			if (HTTPParameterConstants.selectValueSetSearchOption_value_list.contains(value)) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}

		} else if (name.compareTo("valueset_search_algorithm") == 0) {
			if (HTTPParameterConstants.valueset_search_algorithm_value_list.contains(value)) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}

		}
		return null;
	}

    public static Boolean containsPercentSign(String name, String value) {
		if (name == null || value == null) return null;
		if (!name.endsWith(".x")
		    && !name.endsWith(".y")
		    && name.compareTo("javax.faces.ViewState") != 0) {
		    return null;
		}
		if (value.indexOf("%") == -1) return Boolean.TRUE;
		return Boolean.FALSE;
	}

    public static Boolean validateValueSetCheckBox(String name, String value) {
		if (name == null || value == null) return null;
		if (DataUtils.getFormalName(name) != null) {
			if (value.compareTo("on") != 0 && value.compareTo("off") != 0) {
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}

		if (!name.startsWith("TVS_") && !name.startsWith("http:")) {
		    return null;
		}

		if (isValueSetURI(name)) {
			if (value.compareTo("on") != 0 && value.compareTo("off") != 0) {
				return Boolean.FALSE;
			}
		}

		if (value.compareTo("on") == 0 || value.compareTo("off") == 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

    public static Boolean containsHazardCharacters(String value) {
		if (value == null) return Boolean.FALSE;
		String s = decode(value).toUpperCase(Locale.ENGLISH);
		s = s.trim();

		//SELECT FROM WHERE
		if (s.indexOf("SELECT") != -1 && s.indexOf("FROM") != -1 && s.indexOf("WHERE") != -1) {
			return Boolean.TRUE;
		}

		for (int i=0; i<Constants.HAZARD_CHARS.length; i++) {
			String t = Constants.HAZARD_CHARS[i];
			if (s.indexOf(t) != -1) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	public static Boolean isDynamicId(String id) {
		if (id == null) return null;
		if (id.startsWith("j_id_jsp_")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public static Boolean isSearchFormParameter(String name) {
		if (name == null) return null;
		String nm = name.toLowerCase(Locale.ENGLISH);
		if (nm == null) return null;
		if (nm.endsWith("search.x") || nm.endsWith("search.y")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}


	public static Boolean isCheckedVocabulariesParameter(String name) {
		if (name == null) return null;
		String nm = name.toLowerCase(Locale.ENGLISH);
		if (nm == null) return null;
		if (nm.compareTo("selected_vocabularies") == 0) {
			return Boolean.TRUE;
		}

		if (nm.endsWith("checked_vocabularies")) {
			return Boolean.TRUE;
		}

        //KLO, 03092016, [NCITERM-721]
		if (nm.endsWith("ontology_list")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}


	public static String createErrorMsg(String name, String value) {
		String error_msg = "WARNING: Invalid parameter value encountered - " + value +
		   " (name: " + name + ").";
		System.out.println(error_msg);
		return error_msg;
	}


	public static Boolean checkLimitedLengthCondition(String name, String value) {
		if (name == null) return null;
		if (value == null) return Boolean.TRUE;
		Boolean bool_val = isCheckedVocabulariesParameter(name);
		// exceptions:
		if ((bool_val != null && bool_val.equals(Boolean.FALSE))
		    && name.compareTo("matchText") != 0
		    && name.compareTo("message") != 0
		    && name.compareTo("referer") != 0
		    && name.compareTo("acceptedLicenses") != 0
		    && name.compareTo("ontology_list_str") != 0) {
			if (value.length() > ABS_MAX_STR_LEN) {
				return Boolean.FALSE;
			}
		}
        return Boolean.TRUE;
	}

	public static void preserveHTTPRequestParameterValues(HttpServletRequest request) {
		Object attr_1 = request.getSession().getAttribute("adv_search_algorithm");
		if (attr_1 != null && attr_1 instanceof String) {
			request.getSession().setAttribute("adv_search_algorithm", (String) attr_1);
		}

		Object attr_2 = request.getSession().getAttribute("algorithm");
		if (attr_2 != null && attr_2 instanceof String) {
			request.getSession().setAttribute("algorithm", (String) attr_2);
		}

		Object attr_3 = request.getSession().getAttribute("direction");
		if (attr_3 != null && attr_3 instanceof String) {
			request.getSession().setAttribute("direction", (String) attr_3);
		}

		Object attr_4 = request.getSession().getAttribute("matchText");
		if (attr_4 != null && attr_4 instanceof String) {
			request.getSession().setAttribute("matchText", (String) attr_4);
		}

		Object attr_5 = request.getSession().getAttribute("ontology_list");
		if (attr_5 != null && attr_5 instanceof String) {
			request.getSession().setAttribute("ontology_list", (String) attr_5);
		}

		Object attr_6 = request.getSession().getAttribute("scheme_and_version");
		if (attr_6 != null && attr_6 instanceof String) {
			request.getSession().setAttribute("scheme_and_version", (String) attr_6);
		}

		Object attr_7 = request.getSession().getAttribute("searchTarget");
		if (attr_7 != null && attr_7 instanceof String) {
			request.getSession().setAttribute("searchTarget", (String) attr_7);
		}

		Object attr_8 = request.getSession().getAttribute("selectSearchOption");
		if (attr_8 != null && attr_8 instanceof String) {
			request.getSession().setAttribute("selectSearchOption", (String) attr_8);
		}

		Object attr_9 = request.getSession().getAttribute("selectValueSetSearchOption");
		if (attr_9 != null && attr_9 instanceof String) {
			request.getSession().setAttribute("selectValueSetSearchOption", (String) attr_9);
		}

		Object attr_10 = request.getSession().getAttribute("subject");
		if (attr_10 != null && attr_10 instanceof String) {
			request.getSession().setAttribute("subject", (String) attr_10);
		}

		Object attr_11 = request.getSession().getAttribute("valueset");
		if (attr_11 != null && attr_11 instanceof String) {
			request.getSession().setAttribute("valueset", (String) attr_11);
		}

		Object attr_12 = request.getSession().getAttribute("valueset_search_algorithm");
		if (attr_12 != null && attr_12 instanceof String) {
			request.getSession().setAttribute("valueset_search_algorithm", (String) attr_12);
		}
	}


	public static void preserveSearchFormParameters(HttpServletRequest request) {
		Object param_obj_0 = request.getParameter("adv_search_algorithm");
		if (param_obj_0 != null) {
			String param_0 = HTTPUtils.cleanXSS((String) param_obj_0);
			request.getSession().setAttribute("adv_search_algorithm", param_0);
		}
		Object param_obj_1 = request.getParameter("algorithm");
		if (param_obj_1 != null) {
			String param_1 = HTTPUtils.cleanXSS((String) param_obj_1);
			request.getSession().setAttribute("algorithm", param_1);
		}
		Object param_obj_2 = request.getParameter("captcha_option");
		if (param_obj_2 != null) {
			String param_2 = HTTPUtils.cleanXSS((String) param_obj_2);
			request.getSession().setAttribute("captcha_option", param_2);
		}
		Object param_obj_3 = request.getParameter("matchText");
		if (param_obj_3 != null) {
			String param_3 = HTTPUtils.cleanXSS((String) param_obj_3);
			request.getSession().setAttribute("matchText", param_3);
		}
		Object param_obj_4 = request.getParameter("matchText_RVS");
		if (param_obj_4 != null) {
			String param_4 = HTTPUtils.cleanXSS((String) param_obj_4);
			request.getSession().setAttribute("matchText_RVS", param_4);
		}
		Object param_obj_5 = request.getParameter("matchText_VSD");
		if (param_obj_5 != null) {
			String param_5 = HTTPUtils.cleanXSS((String) param_obj_5);
			request.getSession().setAttribute("matchText_VSD", param_5);
		}
		Object param_obj_6 = request.getParameter("opt");
		if (param_obj_6 != null) {
			String param_6 = HTTPUtils.cleanXSS((String) param_obj_6);
			request.getSession().setAttribute("opt", param_6);
		}
		Object param_obj_7 = request.getParameter("searchTarget");
		if (param_obj_7 != null) {
			String param_7 = HTTPUtils.cleanXSS((String) param_obj_7);
			request.getSession().setAttribute("searchTarget", param_7);
		}
		Object param_obj_8 = request.getParameter("selectSearchOption");
		if (param_obj_8 != null) {
			String param_8 = HTTPUtils.cleanXSS((String) param_obj_8);
			request.getSession().setAttribute("selectSearchOption", param_8);
		}
		Object param_obj_9 = request.getParameter("selectValueSetSearchOption");
		if (param_obj_9 != null) {
			String param_9 = HTTPUtils.cleanXSS((String) param_obj_9);
			request.getSession().setAttribute("selectValueSetSearchOption", param_9);
		}
		Object param_obj_10 = request.getParameter("text");
		if (param_obj_10 != null) {
			String param_10 = HTTPUtils.cleanXSS((String) param_obj_10);
			request.getSession().setAttribute("text", param_10);
		}
		Object param_obj_11 = request.getParameter("valueset_search_algorithm");
		if (param_obj_11 != null) {
			String param_11 = HTTPUtils.cleanXSS((String) param_obj_11);
			request.getSession().setAttribute("valueset_search_algorithm", param_11);
		}
	}

}
