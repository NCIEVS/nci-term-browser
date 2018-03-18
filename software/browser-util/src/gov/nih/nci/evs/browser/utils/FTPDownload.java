package gov.nih.nci.evs.browser.utils;

import java.io.*;
import java.util.*;
import java.net.*;
import org.apache.commons.io.FilenameUtils;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FTPDownload {

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


    public static void main (String[] args) {
		String uri = "http://evs.nci.nih.gov/ftp1/FDA/CDRH/FDA-CDRH_NCIt_Subsets.txt";
		uri = "ftp://ftp1.nci.nih.gov/pub/cacore/EVS/CDISC/SDTM/SDTM Terminology.xls";
		if (args.length == 1) {
		    uri = args[0];
		}
		String s = tear_page(uri);
		System.out.println(s);
    }
}
