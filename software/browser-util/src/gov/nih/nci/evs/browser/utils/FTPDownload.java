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


public class FTPDownload {

   public static String NCIT_FTP_SITE = "https://evs.nci.nih.gov/ftp1/";
   public static String ABOUT_PAGE = "About.html";
   public static String MAPPINGS = "Mappings";
   public static String NCI_THESAURUS = "NCI_Thesaurus";
   public static String NCIT_MAPPINGS_SITE = NCIT_FTP_SITE + NCI_THESAURUS + "/" + MAPPINGS + "/";
   public static String NCIT_ABOUT_SITE = NCIT_MAPPINGS_SITE + ABOUT_PAGE;
   public static String ICDO3_MAPPIGNS_SITE = NCIT_MAPPINGS_SITE + "ICD-O-3_Mappings/";
   public static String ICDO3_ABOUT_SITE   = ICDO3_MAPPIGNS_SITE + "About.html";

   public static String[] MAPPING_SITES = new String[] {NCIT_MAPPINGS_SITE, NCIT_ABOUT_SITE, ICDO3_MAPPIGNS_SITE, ICDO3_ABOUT_SITE};

   public static HashMap lastUpdatedHashMap = null;

   public static Vector otherMappingData = null;

   static {
	   lastUpdatedHashMap = retrieveLastUpdatedData();
       Vector otherMappings = new Vector();
       otherMappings.add("ICD-O");
       otherMappings.add("SwissProt");
	   otherMappingData = constructOtherMappingData(otherMappings);
   }

   public static Vector getOtherMappingData() {
	   return otherMappingData;
   }

   public static void dumpMappingSites() {
       for (int i=0; i<MAPPING_SITES.length; i++) {
		   String t = MAPPING_SITES[i];
		   System.out.println(t);
	   }
   }

   public static Vector get(String uri) {
      URL u = null;
      InputStream is = null;
      DataInputStream dis = null;
      String s;
      Vector v = new Vector();
      try {
         u = new URL(uri);
         if (u != null) {
			 is = u.openStream();
			 dis = new DataInputStream(new BufferedInputStream(is));
			 while ((s = dis.readLine()) != null) {
				v.add(s);
			 }
		 }

      } catch (MalformedURLException mue) {
		 System.out.println("(*) MalformedURLException: " + uri);
		 return v;
         //mue.printStackTrace();

      } catch (IOException ioe) {
		 System.out.println(uri);
         ioe.printStackTrace();

      } finally {
         try {
            if (is != null) is.close();
         } catch (IOException ioe) {
            ioe.printStackTrace();
         }
      }
      return v;
   }


   public static void downloadText(String uri) {
	  if (uri == null) return;
      URL u = null;
      InputStream is = null;
      DataInputStream dis = null;
      String s = null;
      PrintWriter pw = null;
      String outputfile = null;
      int n = uri.lastIndexOf("/");
      if (n != -1) {
		  outputfile = uri.substring(n+1, uri.length());
	  }
      //System.out.println(outputfile);
      if (outputfile == null) return;
      try {
         pw = new PrintWriter(outputfile, "UTF-8");
         if (pw != null) {
			 u = new URL(uri);
			 if (u != null) {
				 is = u.openStream();
				 dis = new DataInputStream(new BufferedInputStream(is));
				 while ((s = dis.readLine()) != null) {
					pw.println(s);
				 }
			 }
		 }

      } catch (MalformedURLException mue) {
         mue.printStackTrace();

      } catch (IOException ioe) {
         ioe.printStackTrace();

      } finally {
         try {
            if (is != null) is.close();
            if (pw != null) pw.close();
            System.out.println("Output file " + outputfile + " generated.");
         } catch (IOException ioe) {
            ioe.printStackTrace();
         }
      }
   }


   public static void downloadExcel(String uri) {
      URL u;
      InputStream is = null;
      int n = uri.lastIndexOf("/");
      String outputfile = null;
      if (n != -1) {
		  outputfile = uri.substring(n+1, uri.length());
	  }
      System.out.println(outputfile);
      if (outputfile == null) return;
      try {
          u = new URL(uri);
          is = u.openStream();
		  byte[] buffer = new byte[8 * 1024];
		  try {
			  OutputStream output = new FileOutputStream(outputfile);
			  try {
				  int bytesRead;
				  while ((bytesRead = is.read(buffer)) != -1) {
				      output.write(buffer, 0, bytesRead);
				  }
			  } finally {
				  output.close();
			  }
		  } finally {
			  is.close();
		  }
	  } catch (Exception ex) {
		  ex.printStackTrace();
	  } finally {
		  System.out.println("Output file " + outputfile + " generated.");
	  }
   }


   public static void downloadText(String uri, String outputfile) {
	  if (uri == null) return;
	  if (outputfile == null) return;

      URL u = null;
      InputStream is = null;
      DataInputStream dis = null;
      String s = null;
      PrintWriter pw = null;

      try {
         pw = new PrintWriter(outputfile, "UTF-8");
         if (pw != null) {
			 u = new URL(uri);
			 if (u != null) {
				 is = u.openStream();
				 dis = new DataInputStream(new BufferedInputStream(is));
				 while ((s = dis.readLine()) != null) {
					pw.println(s);
				 }
			 }
		 }

      } catch (MalformedURLException mue) {
         mue.printStackTrace();

      } catch (IOException ioe) {
         ioe.printStackTrace();

      } finally {
         try {
            if (is != null) is.close();
            if (pw != null) pw.close();
            System.out.println("Output file " + outputfile + " generated.");
         } catch (IOException ioe) {
            ioe.printStackTrace();
         }
      }
   }


   public static void downloadExcel(String uri, String outputfile) {
      URL u;
      InputStream is = null;
      if (outputfile == null) return;
      try {
          u = new URL(uri);
          is = u.openStream();
		  byte[] buffer = new byte[8 * 1024];
		  try {
			  OutputStream output = new FileOutputStream(outputfile);
			  try {
				  int bytesRead;
				  while ((bytesRead = is.read(buffer)) != -1) {
				      output.write(buffer, 0, bytesRead);
				  }
			  } finally {
				  output.close();
			  }
		  } finally {
			  is.close();
		  }
	  } catch (Exception ex) {
		  ex.printStackTrace();
	  } finally {
		  System.out.println("Output file " + outputfile + " generated.");
	  }
   }


   public static void download(String uri) {
	  String ext = FilenameUtils.getExtension(uri);
	  if (ext == null) return;
	  if (ext.compareTo("txt") == 0) {
		  downloadText(uri);
	  } else {
		  downloadExcel(uri);
	  }

   }

   public static void download(String uri, String outputfile) {
	  String ext = FilenameUtils.getExtension(uri);
	  if (ext == null) return;
	  if (ext.compareTo("txt") == 0) {
		  downloadText(uri, outputfile);
	  } else {
		  downloadExcel(uri, outputfile);
	  }

   }

	public static Vector tearPage(String page_url) {
		URL url;
		Vector w = new Vector();
		try {
			url = new URL(page_url);
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				w.add(inputLine + "\r\n");
			}
			br.close();
		} catch (MalformedURLException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}
        return w;
	}

    public static Vector extractMappingsFromURL(String page_url) {
		Vector v = new Vector();
		Vector w = tearPage(page_url);
		if (w == null) return null;
		for (int i=0; i<w.size(); i++) {
			String line = (String) w.elementAt(i);
			String line_lower = line.toLowerCase();

			if (line_lower.indexOf("href") != -1 && line_lower.indexOf("/</a>") != -1) {
				int n = line.lastIndexOf("</a>");
				String s1 = line.substring(0, n);
				n = s1.lastIndexOf(">");
				s1 = s1.substring(n+1, s1.length());
				if (s1.indexOf("archive") == -1) {
					//System.out.println("subfolder: " + s1);
					String sub_page_url = page_url + "/" + s1;
					Vector sub_v = extractMappingsFromURL(sub_page_url);
					v.addAll(sub_v);
				}
			} else if (line_lower.indexOf("href") != -1 && (line_lower.indexOf("mapping.txt") != -1 || line_lower.indexOf("mappings.txt") != -1)) {
				int n = line.lastIndexOf("</a>");
				String s1 = line.substring(0, n);
				n = s1.lastIndexOf(">");
				s1 = s1.substring(n+1, s1.length());
				s1 = s1.replace(".txt", "");
				n = line.lastIndexOf("</a>");
				String s2 = line.substring(n+4, line.length());
				s2 = s2.trim();
				n = s2.indexOf(" ");
				s2 = s2.substring(0, n);
				String s0 = s1;
				n = s1.lastIndexOf("-");
				if (n != -1) {
					String s4 = s1.substring(0, n);
					String s5 = s1.substring(n+1, s1.length());
					s5 = s5.replaceAll("-", " ");
					s5 = s5.replaceAll("_", " ");
					s1 = s4 + " to " + s5;
			    }
				String s3 = s1;
				s3 = s3.replaceAll(" ", "_");
				if (page_url.endsWith("/")) {
					page_url = page_url.substring(0, page_url.length()-1);
				}
				v.add(s3 + "|" + s1 + "|" + s1 + " (" + s2 + ")|" + page_url + "/" + s0 + ".txt");
			} else if (line_lower.indexOf("href") != -1 && (line_lower.indexOf("mapping.xls") != -1 || line_lower.indexOf("mappings.xls") != -1)) {
				int n = line.lastIndexOf("</a>");
				String s1 = line.substring(0, n);
				n = s1.lastIndexOf(">");
				s1 = s1.substring(n+1, s1.length());
				s1 = s1.replace(".xls", "");
				n = line.lastIndexOf("</a>");
				String s2 = line.substring(n+4, line.length());
				s2 = s2.trim();
				n = s2.indexOf(" ");
				s2 = s2.substring(0, n);
				String s0 = s1;
				n = s1.lastIndexOf("-");
				if (n != -1) {
					String s4 = s1.substring(0, n);
					String s5 = s1.substring(n+1, s1.length());
					s5 = s5.replaceAll("-", " ");
					s5 = s5.replaceAll("_", " ");
					s1 = s4 + " to " + s5;
			    }
				String s3 = s1;
				s3 = s3.replaceAll(" ", "_");
				if (page_url.endsWith("/")) {
					page_url = page_url.substring(0, page_url.length()-1);
				}
				v.add(s3 + "|" + s1 + "|" + s1 + " (" + s2 + ")|" + page_url + "/" + s0 + ".xls");
			}
		}
		return v;
	}


	public static String tear_page(String page_url) {
		URL url;
		StringBuffer buf = new StringBuffer();
		try {
			url = new URL(page_url);
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				buf.append(inputLine + "\r\n");
			}
			br.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return buf.toString();
	}

	public static Vector downloadPage(String page_url) {
		try {
			URL url = new URL(page_url);
			return downloadPage(url);
		} catch (Exception ex) {
			return null;
		}
    }

	public static Vector downloadPage(URL url) {
		Vector v = new Vector();
		try {
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				v.add(inputLine);
			}
			br.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return v;
	}

/*
	(1) https://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/ICD-O-3_Mappings/ICD-O-3.1-NCIt_Morphology_Mapping.txt|ICD-O-3.1-NCIt Morphology|ICD-O-3.1-NCIt_Morphology.txt
	(2) https://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/ICD-O-3_Mappings/ICD-O-3.1-NCIt_Topography_Mapping.txt|ICD-O-3.1-NCIt Topography|ICD-O-3.1-NCIt_Topography.txt
	(3) https://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/NCIt-SwissProt_Mapping.txt|NCIt-SwissProt_Mapping.txt|NCIt-SwissProt_Mapping.txt.txt
*/

	public static Vector addUpdatedData(Vector w) {
		HashMap hmap = lastUpdatedHashMap;
		Vector v = new Vector();
		for (int i=0; i<w.size(); i++) {
			String line = (String) w.elementAt(i);
			Vector u = StringUtils.parseData(line, '|');
			String uri = (String) u.elementAt(0);
			String mapping = (String) u.elementAt(1);
			String displayName = null;
			String filename = (String) u.elementAt(2);
			if (hmap.containsKey(filename)) {
				String lastUpdated = (String) hmap.get(filename);
				displayName = mapping + " (" + lastUpdated + ")";
			} else {
				System.out.println(filename + " not found.");
			}
			v.add(mapping + "|" + uri + "|" + displayName + "|" + filename);
		}
		return new SortUtils().quickSort(v);
	}

/*
	public static Vector searchForHrefLinks(Vector w) {
		Vector v = new Vector();
		for (int i=0; i<w.size(); i++) {
			String line = (String) w.elementAt(i);
			Vector u = new Vector();
			if (line.indexOf("\r\n") != -1) {
				u = StringUtils.parseData(line, "\r\n");
			} else {
				u.add(line);
			}
			for (int j=0; j<u.size(); j++) {
				String t = (String) u.elementAt(j);
				if (t.indexOf("href=") != -1) {
					v.add(line);
				}
			}
		}
		v = trimHrefLinks(v);


		v = hrefLinkQA(v);
		v = addUpdatedData(v);
        return v;
	}
*/

/*

	public static Vector searchForHrefLinks() {
		String mapping_page_url = "https://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings";
		String ICDO3_mapping_url = "https://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/ICD-O-3_Mappings/About.html";
		String ncit_mapping_url = "https://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/About.html";
        Vector w = new Vector();
        w = downloadPage(ICDO3_mapping_url);
		Vector w2 = downloadPage(ncit_mapping_url);
		w.addAll(w2);
		return searchForHrefLinks(w);
	}

	public static Vector trimHrefLinks(Vector w) {
		HashSet hset = new HashSet();
		Vector v = new Vector();
		for (int i=0; i<w.size(); i++) {
			String line = (String) w.elementAt(i);
			int n = line.indexOf("=");
			if (n != -1) {
				line = line.substring(n+2, line.length());
			}
			if (line.indexOf("ftp") != -1 && line.indexOf("About.html") == -1 && line.indexOf("Archive") == -1) {
				int m = line.indexOf("</a>");
				if (m != -1) {
					String t = line.substring(0, m);
					t = t.replaceAll("\">", "|");
					if (!hset.contains(t)) {
					    v.add(t);
					    hset.add(t);
					}
				}
				int m2 = line.indexOf(" and ");
				if (m2 != -1) {
					int m3 = line.indexOf("href");
					if (m3 != -1) {
						String s = line.substring(m3+6, line.length());

						int m4 = line.indexOf("</a>");
						if (m4 != -1) {
							s = s.substring(0, m4);
							s = s.replaceAll("\">", "|");
							if (!hset.contains(s)) {
								v.add(s);
								hset.add(s);
							}
						}
                        if (!hset.contains(s)) {
							v.add(s);
							hset.add(s);
						}
					}
			    }
			}
		}
		v = filterHrefLinks(v);
		Vector avail_Mappings = new Vector();
		avail_Mappings.add("GO to NCIt Mapping");
		avail_Mappings.add("MA to NCIt Mapping");
		avail_Mappings.add("NCIt to ChEBI Mapping");
		avail_Mappings.add("NCIt to HGNC Mapping");
		avail_Mappings.add("PDQ to NCIt Mapping");
		avail_Mappings.add("SNOMEDCT to ICD10 Mapping");
		avail_Mappings.add("SNOMEDCT_US to ICD-10-CM Mapping");
		return removeAvailableMappings(v, avail_Mappings);

	}
*/
/*
searchForHrefLinks:
	(1) http://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/ICD-O-3_Mappings/ICD-O-3.1-NCIt_Morphology_Mapping.txt|ICD-O-3.1-NCIt Morphology
	(2) http://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/ICD-O-3_Mappings/ICD-O-3.1-NCIt_Topography_Mapping.txt|ICD-O-3.1-NCIt Topography
	(3) http://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/|http://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/
	(4) http://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/GO-NCIt_Mapping.txt|GO-NCIt_Mapping.txt
	(5) http://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/NCIt-ChEBI_Mapping.txt|NCIt-ChEBI_Mapping.txt
	(6) http://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/NCIt-HGNC_Mapping.txt|NCIt-HGNC_Mapping.txt
	(7) https://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/NCIt-SwissProt_Mapping.txt|NCIt-SwissProt_Mapping.txt
	(8) http://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/NCIt_Mapping_Version.txt|NCIt_Mapping_Version.txt
	(9) http://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/ICD-O-3_Mappings|ICD-O-3_Mappings
*/

	public static Vector filterHrefLinks(Vector w) {
		Vector v = new Vector();
		for (int i=0; i<w.size(); i++) {
			String t = (String) w.elementAt(i);
			Vector u = StringUtils.parseData(t, '|');
			String mapping_label = (String) u.elementAt(1);
			String mapping_link = (String) u.elementAt(0);
			if (mapping_label.indexOf("Mappings") == -1 && mapping_label.indexOf("_Version") == -1) {
				v.add(t);
			}
		}
		return v;
	}

	/*
 GO to NCIt Mapping: GO to NCIt Mapping (February2020)

 MA to NCIt Mapping: MA to NCIt Mapping (November2011)

 NCIt to ChEBI Mapping: NCIt to ChEBI Mapping (January2020)

 NCIt to HGNC Mapping: NCIt to HGNC Mapping (April2020)

 PDQ to NCIt Mapping: PDQ to NCIt Mapping (201607)

 SNOMEDCT to ICD10 Mapping: SNOMED CT International Edition to ICD-10 Mapping (2017_03_01)

 SNOMEDCT_US to ICD-10-CM Mapping: SNOMED CT US Edition to ICD-10-CM Mapping (2017_03_01)


	(1) http://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/ICD-O-3_Mappings/ICD-O-3.1-NCIt_Morphology_Mapping.txt|ICD-O-3.1-NCIt Morphology
	(2) http://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/ICD-O-3_Mappings/ICD-O-3.1-NCIt_Topography_Mapping.txt|ICD-O-3.1-NCIt Topography
	(3) http://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/GO-NCIt_Mapping.txt|GO-NCIt_Mapping.txt
	(4) http://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/NCIt-ChEBI_Mapping.txt|NCIt-ChEBI_Mapping.txt
	(5) http://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/NCIt-HGNC_Mapping.txt|NCIt-HGNC_Mapping.txt
	(6) https://evs.nci.nih.gov/ftp1/NCI_Thesaurus/Mappings/NCIt-SwissProt_Mapping.txt|NCIt-SwissProt_Mapping.txt


	*/
	/*
	public static Vector removeAvailableMappings(Vector w, Vector availableMappings) {

		Utils.dumpVector("removeAvailableMappings", availableMappings);

		Vector v = new Vector();
		for (int i=0; i<w.size(); i++) {
			String t = (String) w.elementAt(i);
			Vector u = StringUtils.parseData(t, '|');
			String mapping_label = (String) u.elementAt(1);
			String mapping_link = (String) u.elementAt(0);
			int n = mapping_label.lastIndexOf(".");
			if (n != -1) {
				String trimmed_mapping_label = mapping_label;

				System.out.println("(*) " + trimmed_mapping_label);

			    trimmed_mapping_label = mapping_label.replace(".txt", "");

			    System.out.println("(0) " + trimmed_mapping_label);

                String source = null;
                String target = null;
                String mapping = null;
			    int m = trimmed_mapping_label.indexOf("NCIt-");
			    if (m != -1) {
					source = trimmed_mapping_label.substring(0, m+4);
					target = trimmed_mapping_label.substring(m+5, trimmed_mapping_label.length());
				} else {
					m = trimmed_mapping_label.indexOf("-NCIt");
					if (m != -1) {
						source = trimmed_mapping_label.substring(0, m);
						target = trimmed_mapping_label.substring(m+1, trimmed_mapping_label.length());
					}
				}
				target = target.replace("_", " ");
				mapping = source + " to " + target;

				System.out.println("(source) " + source);
				System.out.println("(target) " + target);
				System.out.println("(mapping) " + mapping);


			    System.out.println("(2) " + trimmed_mapping_label);
			    if (!availableMappings.contains(mapping)) {
					v.add(t);
				}
			}
		}
		return v;
	}
	*/

	public static String replaceChar(String str, char from, char to) {
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<str.length(); i++) {
			char c = str.charAt(i);
			if (c == from) {
				c = to;
			}
			buf.append(c);
		}
		return buf.toString();
	}
/*
    public static Vector hrefLinkQA(Vector w) {
		Vector v = new Vector();
		for (int i=0; i<w.size(); i++) {
			String line = (String) w.elementAt(i);
			Vector u = StringUtils.parseData(line, '|');
			String uri = (String) u.elementAt(0);
			String mapping = (String) u.elementAt(1);
			if (uri.indexOf("http:") != -1 && uri.indexOf("https:") == -1) {
				uri = uri.replace("http:", "https:");
			}
			int n = uri.lastIndexOf("/");
			String filename = uri.substring(n+1, uri.length());

			int m = filename.lastIndexOf(".");
			String displayName = filename.substring(0, m);
			displayName = reLabelMapping(displayName);

			mapping = filename;
			v.add(uri + "|" + displayName + "|" + filename);
		}
		return v;
    }
*/

    public static void dumpHashMap(HashMap hmap) {
        Iterator it = hmap.keySet().iterator();
        while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) hmap.get(key);
			System.out.println(key + " --> " + value);
		}
	}

    public static String extractFirstToken(String line) {
		line = line.trim();
	    StringBuffer buf = new StringBuffer();
	    for (int i=0; i<line.length(); i++) {
			char c = line.charAt(i);
			if (c == ' ') return buf.toString();
			buf.append(c);
		}
		return buf.toString();
	}

    public static String reLabelMapping(String label) {
		String source = null;
		String target = null;
		String mapping = null;
		int m = label.indexOf("NCIt-");
		if (m != -1) {
			source = label.substring(0, m+4);
			target = label.substring(m+5, label.length());
		} else {
			m = label.indexOf("-NCIt");
			if (m != -1) {
				source = label.substring(0, m);
				target = label.substring(m+1, label.length());
			}
		}
		target = target.replace("_", " ");
		mapping = source + " to " + target;
		return mapping;
	}


    public static HashMap retrieveLastUpdatedData() {
		HashMap hmap = new HashMap();
		Vector v = new Vector();

		for (int i=0; i<MAPPING_SITES.length; i++) {
			String site = MAPPING_SITES[i];
			Vector w = get(site);
			v.addAll(w);
		}
		Vector u = new Vector();
		for (int i=0; i<v.size(); i++) {
			String line = (String) v.elementAt(i);
			if (line.indexOf("\"top\"") != -1) {
				u.add(line);
			}
		}

		String target = "</td><td align=\"right\">";
		String href_target = "href=";
		for (int i=0; i<u.size(); i++) {
			String line = (String) u.elementAt(i);
			int n = line.indexOf(target);
			if (n != -1) {
				String s = line.substring(n + target.length(), line.length());
				n = s.indexOf(target);
				if (n != -1) {
				    s = s.substring(0, n);
				    s = extractFirstToken(s);
					int m = line.indexOf(href_target);
					if (m != -1) {
						String s2 = line.substring(m+href_target.length()+1,line.length());
						int m3 = s2.indexOf("\"");
						s2 = s2.substring(0, m3);
						hmap.put(s2, s);
					}
				}
			}
		}
        return hmap;
	}


	//otherMappings: ICD-O, SwissProt
	public static Vector constructOtherMappingData(Vector otherMappings) {
		HashMap updatedData = retrieveLastUpdatedData();
		Vector w = new Vector();
		Iterator it = updatedData.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
            if (!key.endsWith("/")) {
				boolean retval = false;
				for (int i=0; i<otherMappings.size(); i++) {
					String t = (String) otherMappings.elementAt(i);
					if (key.indexOf(t) != -1) {
						retval = true;
						break;
					}
				}
				if (retval) {
					String line = filename2MappingData(otherMappings, key);
					w.add(line);
				}
			}
		}
		return w;
	}

	public static Vector findHrefLinks() {
        Vector w = new Vector();
        w = downloadPage(NCIT_MAPPINGS_SITE);
		Vector w2 = downloadPage(ICDO3_MAPPIGNS_SITE);
		w.addAll(w2);
		Vector v = new Vector();
		for (int i=0; i<w.size(); i++) {
			String line = (String) w.elementAt(i);
			Vector u = new Vector();
			if (line.indexOf("\r\n") != -1) {
				u = StringUtils.parseData(line, "\r\n");
			} else {
				u.add(line);
			}
			for (int j=0; j<u.size(); j++) {
				String t = (String) u.elementAt(j);
				if (t.indexOf("href=") != -1) {
					v.add(line);
				}
			}
		}
		//v = trimHrefLinks(v);
        return v;
	}

/*
Archive/ --> 2017-12-21
About.html --> 2017-12-21
NCIt-ChEBI_Mapping.txt --> 2020-01-28
ICD-O-3.1-NCIt_Morphology_Mapping.txt --> 2017-12-21
NCIt-HGNC_Mapping.txt --> 2020-04-27
ICD-O-3.1-NCIt_Axis_Mappings.xls --> 2017-12-21
ICD-O-3.1-NCIt_Topography_Mapping.txt --> 2017-12-21
GO-NCIt_Mapping.txt --> 2020-02-25
ICD-O-3_Mappings/ --> 2017-12-21
NCIt-SwissProt_Mapping.txt --> 2020-04-27
NCIt_Mapping_Version.txt --> 2020-02-25
archive/ --> 2020-04-27
*/


    public static String filename2MappingData(Vector otherMappings, String filename) {
		HashMap hmap = lastUpdatedHashMap;
		String uri = null;
		String mapping = null;
		String retstr = null;

//NCIt-SwissProt_Mapping.txt


		if (filename.indexOf("ICD-O") != -1) {
			uri = ICDO3_MAPPIGNS_SITE + filename;
		} else {
			uri = NCIT_MAPPINGS_SITE + filename;
		}
		int m = filename.lastIndexOf(".");
		mapping = filename.substring(0, m);
		mapping = reLabelMapping(mapping);
		String lastUpdated = (String) hmap.get(filename);
		String displayName = mapping + " (" + lastUpdated + ")";

		String cs_name = getCSName(mapping);


		//retstr = mapping + "|" + uri + "|" + displayName + "|" + filename;
		retstr = cs_name + "|" + mapping + "|" + displayName + "|" + uri;

		return retstr;
	}

//String mapping = "NCIt to SwissProt Mapping";
    public static String getCSName(String mapping) {
		String source = null;
		String target = null;
		int m = mapping.indexOf("NCIt to");
		//NCIt-SwissProt_Mapping.txt
		if (m != -1) {
			String res = mapping.substring(m+7, mapping.length());
			source = mapping.substring(m, 4);
			target = extractFirstToken(res);
		} else {
			m = mapping.indexOf("to NCIt");
			if (m != -1) {
				source = extractFirstToken(mapping);
				target = "NCIt";
			}
		}
		if (source.compareTo("NCIt") == 0) return target;
		if (target.compareTo("NCIt") == 0) return source;
        return null;
	}


    public static void main (String[] args) {
		/*
		String uri = "http://evs.nci.nih.gov/ftp1/FDA/CDRH/FDA-CDRH_NCIt_Subsets.txt";
		uri = "ftp://ftp1.nci.nih.gov/pub/cacore/EVS/CDISC/SDTM/SDTM Terminology.xls";
		if (args.length == 1) {
		    uri = args[0];
		}
		String s = tear_page(uri);
		System.out.println(s);

		Vector v = extractMappingsFromURL(page_url);
		Utils.dumpVector(page_url, v);

		*/
/*
		Vector w = searchForHrefLinks();
		Utils.dumpVector("searchForHrefLinks", w);

		String line = filename2MappingData("ICD-O-3.1-NCIt_Axis_Mappings.xls");
		System.out.println(line);
*/
/*
        Vector w = findHrefLinks();
        Utils.dumpVector("HREF", w);
*/
        Vector otherMappings = new Vector();
        otherMappings.add("ICD-O");
        otherMappings.add("SwissProt");
	    Vector w = constructOtherMappingData(otherMappings);
	    Utils.dumpVector("constructOtherMappingData", w);

/*
	    String mapping = "NCIt to SwissProt Mapping";
	    mapping = "ICD-O-3.1 to NCIt_Axis_Mappings";
	    String cs = getCSName(mapping);
	    System.out.println(cs);
*/

/*
		dumpMappingSites();
        HashMap hmap = retrieveLastUpdatedData();
        dumpHashMap(hmap);
*/
        //Utils.dumpHashMap("retrieveLastUpdatedData", hmap);
    }
}


/*
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html>
 <head>
  <title>Index of /ftp1/NCI_Thesaurus/Mappings/ICD-O-3_Mappings</title>
 </head>
 <body>
<h1>Index of /ftp1/NCI_Thesaurus/Mappings/ICD-O-3_Mappings</h1>
  <table>
   <tr><th valign="top"><img src="/icons/blank.gif" alt="[ICO]"></th><th><a href="?C=N;O=D">Name</a></th><th><a href="?C=M;O=A">Last modified</a></th><th><a href="?C=S;O=A">Size</a></th><th><a href="?C=D;O=A">Description</a></th></tr>
   <tr><th colspan="5"><hr></th></tr>
<tr><td valign="top"><img src="/icons/back.gif" alt="[PARENTDIR]"></td><td><a href="/ftp1/NCI_Thesaurus/Mappings/">Parent Directory</a>       </td><td>&nbsp;</td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/text.gif" alt="[TXT]"></td><td><a href="About.html">About.html</a>             </td><td align="right">2017-12-21 09:49  </td><td align="right">3.5K</td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="Archive/">Archive/</a>               </td><td align="right">2017-12-21 09:26  </td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/unknown.gif" alt="[   ]"></td><td><a href="ICD-O-3.1-NCIt_Axis_Mappings.xls">ICD-O-3.1-NCIt_Axis_..&gt;</a></td><td align="right">2017-12-21 09:49  </td><td align="right">748K</td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/text.gif" alt="[TXT]"></td><td><a href="ICD-O-3.1-NCIt_Morphology_Mapping.txt">ICD-O-3.1-NCIt_Morph..&gt;</a></td><td align="right">2017-12-21 09:25  </td><td align="right">243K</td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/text.gif" alt="[TXT]"></td><td><a href="ICD-O-3.1-NCIt_Topography_Mapping.txt">ICD-O-3.1-NCIt_Topog..&gt;</a></td><td align="right">2017-12-21 09:25  </td><td align="right"> 97K</td><td>&nbsp;</td></tr>
   <tr><th colspan="5"><hr></th></tr>
</table>
</body></html>


   <tr><th valign="top"><img src="/icons/blank.gif" alt="[ICO]"></th><th><a href="?C=N;O=D">Name</a></th><th><a href="?C=M;O=A">Last modified</a></th><th><a href="?C=S;O=A">Size</a></th><th><a href="?C=D;O=A">Description</a></th></tr>
<tr><td valign="top"><img src="/icons/back.gif" alt="[PARENTDIR]"></td><td><a href="/ftp1/NCI_Thesaurus/">Parent Directory</a>       </td><td>&nbsp;</td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/text.gif" alt="[TXT]"></td><td><a href="About.html">About.html</a>             </td><td align="right">2020-02-25 12:27  </td><td align="right">4.8K</td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/text.gif" alt="[TXT]"></td><td><a href="GO-NCIt_Mapping.txt">GO-NCIt_Mapping.txt</a>    </td><td align="right">2020-02-25 12:19  </td><td align="right">5.7K</td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="ICD-O-3_Mappings/">ICD-O-3_Mappings/</a>      </td><td align="right">2017-12-21 09:49  </td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/text.gif" alt="[TXT]"></td><td><a href="NCIt-ChEBI_Mapping.txt">NCIt-ChEBI_Mapping.txt</a> </td><td align="right">2020-01-28 09:59  </td><td align="right"> 69K</td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/text.gif" alt="[TXT]"></td><td><a href="NCIt-HGNC_Mapping.txt">NCIt-HGNC_Mapping.txt</a>  </td><td align="right">2020-04-27 18:07  </td><td align="right"> 94K</td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/text.gif" alt="[TXT]"></td><td><a href="NCIt-SwissProt_Mapping.txt">NCIt-SwissProt_Mappi..&gt;</a></td><td align="right">2020-04-27 18:07  </td><td align="right">233K</td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/text.gif" alt="[TXT]"></td><td><a href="NCIt_Mapping_Version.txt">NCIt_Mapping_Version..&gt;</a></td><td align="right">2020-02-25 12:27  </td><td align="right">  6 </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="archive/">archive/</a>               </td><td align="right">2020-04-27 18:08  </td><td align="right">  - </td><td>&nbsp;</td></tr>
   <tr><th valign="top"><img src="/icons/blank.gif" alt="[ICO]"></th><th><a href="?C=N;O=D">Name</a></th><th><a href="?C=M;O=A">Last modified</a></th><th><a href="?C=S;O=A">Size</a></th><th><a href="?C=D;O=A">Description</a></th></tr>
<tr><td valign="top"><img src="/icons/back.gif" alt="[PARENTDIR]"></td><td><a href="/ftp1/NCI_Thesaurus/Mappings/">Parent Directory</a>       </td><td>&nbsp;</td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/text.gif" alt="[TXT]"></td><td><a href="About.html">About.html</a>             </td><td align="right">2017-12-21 09:49  </td><td align="right">3.5K</td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="Archive/">Archive/</a>               </td><td align="right">2017-12-21 09:26  </td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/unknown.gif" alt="[   ]"></td><td><a href="ICD-O-3.1-NCIt_Axis_Mappings.xls">ICD-O-3.1-NCIt_Axis_..&gt;</a></td><td align="right">2017-12-21 09:49  </td><td align="right">748K</td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/text.gif" alt="[TXT]"></td><td><a href="ICD-O-3.1-NCIt_Morphology_Mapping.txt">ICD-O-3.1-NCIt_Morph..&gt;</a></td><td align="right">2017-12-21 09:25  </td><td align="right">243K</td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/text.gif" alt="[TXT]"></td><td><a href="ICD-O-3.1-NCIt_Topography_Mapping.txt">ICD-O-3.1-NCIt_Topog..&gt;</a></td><td align="right">2017-12-21 09:25  </td><td align="right"> 97K</td><td>&nbsp;</td></tr>


*/
