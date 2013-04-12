<%--L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-term-browser/LICENSE.txt for details.
L--%>

<!-- footer -->
<div class="footer" style="width:720px">
  <ul class="textbody">
    <li><a href="http://www.cancer.gov" target="_blank" alt="National Cancer Institute">NCI Home</a> |</li>
    <li><a href="<%= request.getContextPath() %>/pages/contact_us.jsf">Contact Us</a> |</li>
    <li><a href="http://www.cancer.gov/policies" target="_blank" alt="National Cancer Institute Policies">Policies</a> |</li>
    <li><a href="http://www.cancer.gov/policies/page3" target="_blank" alt="National Cancer Institute Accessibility">Accessibility</a> |</li>
    <li><a href="http://www.cancer.gov/policies/page6" target="_blank" alt="National Cancer Institute FOIA">FOIA</a></li>
  </ul>
  <p class="textbody">
    A Service of the National Cancer Institute<br />
    <img src="<%=basePath%>/images/external-footer-logos.gif"
      alt="External Footer Logos" width="238" height="34" border="0"
      usemap="#external-footer" />
  </p>
  <map id="external-footer" name="external-footer">
    <area shape="rect" coords="0,0,46,34"
      href="http://www.cancer.gov" target="_blank"
      alt="National Cancer Institute" />
    <area shape="rect" coords="55,1,99,32"
      href="http://www.hhs.gov/" target="_blank"
      alt="U.S. Health &amp; Human Services" />
    <area shape="rect" coords="103,1,147,31"
      href="http://www.nih.gov/" target="_blank"
      alt="National Institutes of Health" />
    <area shape="rect" coords="148,1,235,33"
      href="http://www.usa.gov/" target="_blank"
      alt="USA.gov" />
  </map>
</div>
<!-- end footer -->