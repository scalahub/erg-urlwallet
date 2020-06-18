
<body id="main_body">
    <%@ include file="/WEB-INF/jsp/common/jsutil.jsp" %>
    <div id="pagecontainer" class="clearfix">
        <%@ include file="/WEB-INF/jsp/common/header.jsp" %>
        <% if(showPostHeader) { %>
            <jsp:include page="<%=postheader%>" />
        <% } %>
        <% if(useCsrf) {%>
            <%@ include file="/WEB-INF/jsp/common/putCsrfToken.jsp" %>
        <% } %>
        <form id="frm1" method="post">
            <input name="addr" type="hidden" value="<%out.print(address);%>">
            <input name="secretData" type="hidden" value="<%out.print(secretData);%>">
            <div>
                <table style="text-align: left; width: 100%; height: 165px;" border="0" bgcolor="#F6F6F6" cellpadding="2" cellspacing="2">
                    <tbody>
                    <tr>
                        <% if(showQRBox) { %>
                            <%@ include file="/WEB-INF/jsp/common/showQR.jsp" %>
                        <% } %>
                        <td style="width: 826px; height: 161px;">
                        <div id="mainstuff" class="mainstuff">
                            <div id="walletbox" class="walletbox">
                                <table border="0" style="box-shadow: 5px 5px 5px #888888;border-color:#0000ff;border-radius: 3px;border-style:solid;border-width:1px;">
                                    <tbody>
                                    <%@ include file="/WEB-INF/jsp/common/showAddress.jsp" %>
                                    <% if(showPrivateKey) { %>
                                        <%@ include file="/WEB-INF/jsp/common/showPrvKey.jsp" %>
                                    <% } %>
                                    <%@ include file="/WEB-INF/jsp/common/showBalance.jsp" %>
                                    </tbody>
                                </table>
                            </div>
                            <% if(showSendBox) { %>
                                <%@ include file="/WEB-INF/jsp/common/showSendBox.jsp" %>
                            <% } %>
                            <% if(showSignBox) { %>
                                <%@ include file="/WEB-INF/jsp/common/showSignBox.jsp" %>
                            <% } %>
                        </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </form>
        <% if(showPreFooter) { %>
            <jsp:include page="<%=prefooter%>"/>
        <% } %>
        <% if(showFooter) { %>
            <%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
        <% } %>
    </div>
</body>