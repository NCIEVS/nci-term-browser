<%@ page import="gov.nih.nci.evs.browser.bean.IteratorBean" %>
<%@ page import="gov.nih.nci.evs.browser.bean.IteratorBeanManager" %>
<%@ page import="gov.nih.nci.evs.browser.properties.NCItBrowserProperties" %>

<FORM NAME="paginationForm" METHOD="POST" action="<%=request.getContextPath() %>/pages/mapping.jsf?nav_type=mappings">
  <table role='presentation'>
    <tr>
      <td class="textbody" align=left>
        <b>Results <%= istart_str %> - <%= iend_str %>of&nbsp;<%= match_size %></b>
      </td>
      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <!-- ---------------------------------------------------------------------- -->

      <td class="textbody" align=right>

        <% if (page_num > 1) { %> &nbsp;
          <i>
            <a
                href="<%=request.getContextPath() %>/pages/mapping.jsf?dictionary=<%=mapping_schema%>&sortBy=<%=sortByStr%>&page_number=<%=prev_page_num_str%>">

              Prev</a>

          </i>&nbsp;
          <%
        }

        if (num_pages > 1) {

          int sliding_window_start = 1;
          int sliding_window_end = num_pages;
          int sliding_window_half_width = NCItBrowserProperties.getSlidingWindowHalfWidth();

          sliding_window_start = page_num - sliding_window_half_width;
          if (sliding_window_start < 1) sliding_window_start = 1;

          sliding_window_end = sliding_window_start + sliding_window_half_width * 2 - 1;
          if (sliding_window_end > num_pages) sliding_window_end = num_pages;

          for (int idx=sliding_window_start; idx<=sliding_window_end; idx++) {
            String idx_str = Integer.toString(idx);

            if (idx == 1 && page_num == 0) {
              %>
              <%= idx_str %>&nbsp;            <% } else if (page_num != idx) { %>
              <a
                  href="<%=request.getContextPath() %>/pages/mapping.jsf?dictionary=<%=mapping_schema%>&sortBy=<%=sortByStr%>&page_number=<%=idx_str%>">

                <%= idx_str %>
              </a>
              &nbsp;
            <% } else { %><%= idx_str %>&nbsp;
              <%
            }
          }
        }

        if (num_pages > 1 && next_page_num <= num_pages) {
          %>
          &nbsp;
          <i>
            <a
                href="<%=request.getContextPath() %>/pages/mapping.jsf?dictionary=<%=mapping_schema%>&sortBy=<%=sortByStr%>&page_number=<%=next_page_num_str%>">

              Next</a>
          </i>
        <% } %>
      </td>

    </tr>
    <tr>
      <td class="textbody" align=left>
        Show
        <!--
          <h:selectOneMenu id="pageSize" value="#{userSessionBean.selectedPageSize}"
          valueChangeListener="#{userSessionBean.pageSizeChanged}" immediate="true" onchange="submit()"> <f:selectItems
          value="#{userSessionBean.pageSizeList}" /> </h:selectOneMenu>
        -->

        <select name="resultsPerPage" size=1 onChange="paginationForm.submit();">
          <%
          List resultsPerPageValues = UserSessionBean.getResultsPerPageValues();
          // Send redirect:
          if (resultsPerPageValues == null) {
            try {
              String error_msg = "WARNING: The server encountered an unexpected error (file: pagination-mapping.jsp, code: 1, var: resultsPerPageValues).";
              request.getSession().setAttribute("error_msg", error_msg);
              String redirectURL = request.getContextPath() + "/pages/appscan_response.jsf";
              response.sendRedirect(redirectURL);
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
          for (int i=0; i<resultsPerPageValues.size(); i++) {
            String resultsPerPageValue = (String) resultsPerPageValues.get(i);

            if (selectedResultsPerPage.compareTo(resultsPerPageValue) == 0) {
              %>
              <option value="<%=resultsPerPageValue%>" selected><%= resultsPerPageValue %></option>
            <% } else { %>
              <option value="<%=resultsPerPageValue%>"><%= resultsPerPageValue %></option>
              <%
            }

          }
          %>
        </select>
        &nbsp;results per page
      </td>
      <td>&nbsp;&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
  </table>
</form>
