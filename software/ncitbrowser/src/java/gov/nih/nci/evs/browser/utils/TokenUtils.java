package gov.nih.nci.evs.browser.utils;

import java.io.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.*;
import java.nio.charset.Charset;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.*;
import java.security.SecureRandom;

import java.util.Calendar;


public class TokenUtils {
	private static final SecureRandom secureRandom = new SecureRandom();
	private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public static String CSRF_TOKEN = "CSRF_TOKEN";

	public static String generateCSRFToken() {
		byte[] randomBytes = new byte[36];
		secureRandom.nextBytes(randomBytes);
		return base64Encoder.encodeToString(randomBytes);
	}

    public static void main(String[] args) {
		System.out.println(generateCSRFToken());
	}
}
