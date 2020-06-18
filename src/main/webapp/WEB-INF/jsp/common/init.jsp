<%@ page import="java.io.*,java.util.*, javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="org.UrlWallet.*" %>
<%!
    public boolean isLocalHost(HttpServletRequest req) {
        boolean isLocal = (req.getServerName().equals("localhost"))? true : false; 
        return isLocal;
    }
    public String captchaSubmitUrl(HttpServletRequest req) {
        return (req.getRequestURL().toString());
    }
%>
