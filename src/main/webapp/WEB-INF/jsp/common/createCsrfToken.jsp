<%
        if (session.getAttribute("csrf") == null) {
            session.setAttribute("csrf", org.UrlWallet.Random.randString());
        }
%>        
        