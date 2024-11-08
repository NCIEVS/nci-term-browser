<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Vector" %>
<%@ page import="org.LexGrid.concepts.Entity" %>
<%@ page import="gov.nih.nci.evs.browser.utils.*" %>
<%@ page import="gov.nih.nci.evs.browser.common.Constants" %>

<%@ page import="gov.nih.nci.evs.browser.properties.NCItBrowserProperties" %>

<%@ page import="nl.captcha.Captcha" %>
<%@ page import="nl.captcha.audio.AudioCaptcha" %>

<%!
private static final String TELEPHONE = Constants.TELEPHONE;
private static final String MAIL_TO = Constants.MAIL_TO;
private static final String NCICB_URL = Constants.NCICB_URL;

// List of attribute name(s):
public static final String SUBJECT = "subject";
public static final String EMAIL_MSG = "message";
public static final String EMAIL_ADDRESS = "emailaddress";
public static final String WARNING_TYPE = "warning_type";

public static final String WARNINGS = "warnings";
public static final String ANSWER = "answer";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html lang="en" xmlns:c="http://java.sun.com/jsp/jstl/core">
  <head>
    <script
        src="//assets.adobedtm.com/f1bfa9f7170c81b1a9a9ecdcc6c5215ee0b03c84/satelliteLib-4b219b82c4737db0e1797b6c511cf10c802c95cb.js">
    </script>

<!-- Google tag (gtag.js) -->
<script async src="https://www.googletagmanager.com/gtag/js?id=G-21QRTJ0WQS"></script>
<script>
	window.dataLayer = window.dataLayer || [];
	function gtag(){dataLayer.push(arguments);}
	gtag('js', new Date());
	gtag('config', 'G-21QRTJ0WQS');
</script>
    <script src="https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js"></script>
    <title>NCI Thesaurus</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon" />
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/search.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/dropdown.js"></script>
    <script src='https://www.google.com/recaptcha/api.js'></script>    

    <script>
      function getContextPath() {
        return "<%=request.getContextPath()%>";
      }

      function loadAudio() {
        var path = getContextPath() + "/audio.wav?bogus=";
        document.getElementById("audioCaptcha").src = path + new Date().getTime();
        document.getElementById("audioSupport").innerHTML = document.createElement('audio').canPlayType("audio/wav");
      }
      
      
      
function verifyRecaptcha() {
    var response = grecaptcha.getResponse();
    $.ajax({
        type: "POST",
        url: 'https://www.google.com/recaptcha/api/siteverify',
        data: {"secret" : "<%=NCItBrowserProperties.getRecaptchaSiteKey()%>", "response" : response},
        contentType: 'application/x-www-form-urlencoded',
        success: function(data) { 
           //console.log(data); 
           //alert("pass");
           
            String redirectURL = request.getContextPath() + "/redirect?action='contactus";
            response.sendRedirect(redirectURL);
           
		if(data.response == '') {
		    alert("Please complete the I'm-not-a-robot widget before submitting your entry.");
		    return false;
		}
               
        }
    });
}      
      
      
      
      
    </script>

  </head>
  <%
  //String warnings = (String) request.getSession().getAttribute("warnings");

  boolean audio_captcha_background_noise_on = true;
  String audio_captcha_str = "audio.wav";
  if (!NCItBrowserProperties.isAudioCaptchaBackgroundNoiseOn()) {
    audio_captcha_background_noise_on = false;
    audio_captcha_str = "nci.audio.wav";
  }

  String captcha_option = "default";
  String alt_captcha_option = "audio";
  String opt = HTTPUtils.cleanXSS((String) request.getSession().getAttribute("captcha_option"));
  if (opt != null && opt.compareTo("audio") == 0) {
    captcha_option = "audio";
    alt_captcha_option = "default";
  }

  String test_option = "invalid_option";

  Captcha captcha = (Captcha) request.getSession().getAttribute("captcha");
  AudioCaptcha ac = null;
  boolean isUserError = false;

  String errorMsg = (String) request.getSession().getAttribute("errorMsg");
  if (errorMsg != null) {
    request.getSession().removeAttribute("errorMsg");
    isUserError = true;
  }

  String retry = (String) request.getSession().getAttribute("retry");
  if (retry != null && retry.compareTo("true") == 0) {
    request.getSession().removeAttribute("retry");
  }

  String subject = (String) request.getSession().getAttribute("subject");
  String message = (String) request.getSession().getAttribute("message");
  String emailaddress = (String) request.getSession().getAttribute("emailaddress");

  String answer  = "";
  if (subject == null) subject = "";
  if (message == null) message = "";
  if (emailaddress == null) emailaddress = "";
  if (errorMsg == null) errorMsg = "";
  boolean error = errorMsg.length() > 0;

  String color = "";
  if (error)
  color = "style=\"color:#FF0000;\"";
  %>
  <body onLoad="document.forms.searchTerm.matchText.focus();">
  <header class="flex-grow-0">
	<div style='text-align: left'>
	<include-html src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html"></include-html>
	</div>
  </header>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/wz_tooltip.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/tip_centerwindow.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/tip_followscroll.js"></script>

<f:view>
  <!-- Begin Skip Top Navigation -->
  <a href="#evs-content" class="skip-main" accesskey="1" title="Skip repetitive navigation links">
    skip navigation links</A>
  <!-- End Skip Top Navigation -->
  <%@ include file="/pages/templates/header.jsp" %>
  <div class="center-page_960">
    <%@ include file="/pages/templates/sub-header.jsp" %>
    <div id="main-area_960">
      <%@ include file="/pages/templates/content-header.jsp" %>
      <div class="pagecontent">
        <a name="evs-content" id="evs-content" tabindex="0"></a>
        <div class="texttitle-blue">Contact Us</div>
        <hr></hr>

        <% if (errorMsg != null && errorMsg.compareTo("") != 0) { %>
          <p class="textbodyred">
            &nbsp;
            <b><%= errorMsg %></b>
          </p>
        <% } %>

        <div>
          <b>
            You can request help or make suggestions by filling out the online form below, or by using any one of these
            contact points:
          </b>
        </div>
        <br />

        <table class="textbody" role='presentation'>
          <tr>
            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td>Telephone:</td>
            <td><%= TELEPHONE %></td>
          </tr>
          <tr>
            <td />
            <td>Email:</td>
            <td>
              <a href="mailto:<%=MAIL_TO%>"><%= MAIL_TO %></a>
            </td>
          </tr>
          <tr>
            <td />
            <td>Web Page:</td>
            <td>
              <a href="<%=NCICB_URL%>" target="_blank" rel="noopener"><%= NCICB_URL %></a>
            </td>
          </tr>
        </table>
        <br />

        <div>
          Telephone support is available Monday to Friday, 8 am - 8 pm Eastern Time, excluding government holidays. You
          may leave a message, send an email, or submit a support request via the Web at any time. Please include:
          <ul>
            <li></li>Your
            contact information;
            <li></li>Reference
            to the Term Suggestions Application; and
            <li></li>A
            detailed description of your problem or suggestion.
          </ul>
          For questions related to NCI's Cancer.gov Web site, see the
          <a href="https://www.cancer.gov/help" target="_blank" rel="noopener">Cancer.gov help page</a>.
          &nbsp; For help and other questions concerning NCI Enterprise Vocabulary Services (EVS), see the
          <a href="https://evs.nci.nih.gov/" target="_blank" rel="noopener">EVS Web site</a>.
        </div>

        <p>
          <b>Online Form</b>
        </p>
        <p class="textbody">To use this web form, please fill in every box below and then click on 'Submit'.</p>

        <h:form>
          <p>
            <i>Subject of your email:</i>
            <i style="color:#FF0000;">*</i>
          </p>
      
          <input aria-label="Subject" 
              class="textbody"
              size="100"
              name="subject"
              alt="Subject"
              value="<%= subject %>"
              onFocus="active = true"
              onBlur="active = false"
              onKeyPress="return ifenter(event,this.form)">
          <p>
            <i>Detailed description of your problem or suggestion (no attachments):</i>
            <i style="color:#FF0000;">*</i>
          </p>
<label for="<%= EMAIL_MSG %>"><%= EMAIL_MSG %></label>
          <TEXTAREA class="textbody" Name="<%= EMAIL_MSG %>" rows="4" cols="98"><%= message %></TEXTAREA>
          <p>
            <i>Your e-mail address:</i>
            <i style="color:#FF0000;">*</i>
          </p>
          <input aria-label="Email Address" 
              class="textbody"
              size="100"
              name="<%= EMAIL_ADDRESS %>"
              alt="<%= EMAIL_ADDRESS %>"
              value="<%= emailaddress %>"
              onFocus="active = true"
              onBlur="active = false"
              onKeyPress="return ifenter(event,this.form)">

          <p>
            <i style="color:#FF0000;">* Required</i>
          </p>

<div class="g-recaptcha" data-sitekey="<%=NCItBrowserProperties.getRecaptchaSiteKey()%>"></div>
          <br />
          <br />

          <h:commandButton
              id="clear"
              value="clear"
              image="/images/clear.gif"
              action="#{userSessionBean.clearContactUs}"
              alt="clear">

          </h:commandButton>
          <img src="<%=basePath%>/images/spacer.gif" width="1" alt="spacer" />
          <h:commandButton
              id="mail"
              value="submit"
              image="/images/submit.gif"
              action="#{userSessionBean.contactUs}"
              alt="submit">

          </h:commandButton>

          <input type="hidden" name="alt_captcha_option" id="alt_captcha_option" value="<%=alt_captcha_option%>">
          <input type="hidden" name="captcha_option" id="captcha_option" value="<%=captcha_option%>">

        </h:form>
        <a href="https://www.cancer.gov/global/web/policies/page2" target="_blank" rel="noopener" alt="Privacy Policy">
          <i>Privacy Policy on E-mail Messages Sent to the NCI Web Site</i>
        </a>
        <%@ include file="/pages/templates/nciFooter.jsp" %>
      </div>
      <!-- end Page content -->
    </div>
    <div class="mainbox-bottom">
      <img src="<%=basePath%>/images/mainbox-bottom.gif" width="945" height="5" alt="Mainbox Bottom" />
    </div>
    <!-- end Main box -->
  </div>
</f:view>
<script type="text/javascript">_satellite.pageBottom();</script>
</body></html>
