<%@ include file="/WEB-INF/jsp/common/init.jsp" %> 
<%@ include file="/WEB-INF/jsp/keywallet/getkeyurl.jsp" %> 
<%
String logoSrc="/static/urlwallet/urlwallet.png";
String logoAlt="UrlWallet";
%>
<%@ include file="/WEB-INF/jsp/common/symbols.jsp" %> 
<%@ include file="/WEB-INF/jsp/common/htmlstart.jsp" %> 
<% 
try { 
%>
<%@ include file="/WEB-INF/jsp/common/captcha.jsp" %> 
<%
    if (captchaDone) {          // human session exists. Now proceed to displaying actual wallet page.
        String randomPattern = org.UrlWallet.Wallet.keyWallet().getRandomPrivateKey().toString();  // a random pattern may be needed later on. Preemptive computing, for shortening code
%>
<%@ include file="/WEB-INF/jsp/common/createCsrfToken.jsp" %>        
<%@ include file="/WEB-INF/jsp/urlwallet/pageParams.jsp" %>        
<%
        // get rid of all non-ascii characters. Main purpose is to prevent XSS attacks that use < and other characters

        String wifKey = urlPattern.replaceAll("[^\\p{L}\\p{Nd}]", "");

        // now we have a url pattern. 
        String address= org.UrlWallet.Wallet.keyWallet().getAddress(wifKey).toString();
        String prvKey = wifKey;
        
%>
        <% String sendMethod = "/sendkey";%>                                
        <% String signMethod = "/signkey";%>
        <% String addressMessage = "Address";%>
        <% String addressHelpMessage = "This is your "+currencyName+" address to receive funds.";%>         
        <% String balanceMessage = "Balance";%>                
        <% String balanceMethod = "/balanceurl";%>
        <% String retrieveMethod = "/retrievebox";%>
        <% String secretData = wifKey;%>
        <% boolean showPrivateKey = false;%>
        <% boolean showSendBox = true;%>        
        <% boolean showSignBox = false;%>
        <% boolean showQRBox = false;%>

        <% String postheader="/WEB-INF/jsp/urlwallet/postheader.jsp";%>
        <% boolean showPostHeader = true;%>        
        <% boolean useCsrf = true;%>
        
        <% boolean showPreFooter = false;%>
        <% boolean showFooter = true;%>
        <% String prefooter = "/WEB-INF/jsp/urlwallet/prefooter.jsp"; %>
        <% String thisUrl = fullUrl(request, urlPattern); %>
        <% session.setAttribute("thisUrl", thisUrl); %>

        <%@ include file="/WEB-INF/jsp/common/pagestart.jsp" %>
</head>
        <%@ include file="/WEB-INF/jsp/common/body.jsp" %>
<% 
    }
} catch (Exception e) { 
%>
<%@ include file="/WEB-INF/jsp/common/exception.jsp" %> 
<%
} 
%> 
</html>