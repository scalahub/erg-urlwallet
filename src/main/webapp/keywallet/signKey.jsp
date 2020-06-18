<%@ page language="java" contentType="application/x-www-form-urlencoded; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/WEB-INF/jsp/common/init.jsp" %> 
<%
   String msg = "";    
   String reload = "reload";
   String encodingError = "encoding";
   try {
%>
<%@ include file="/WEB-INF/jsp/common/checkSession.jsp" %>
<%
		String wifKey = request.getParameter("secretData");
		String message = request.getParameter("message");
		msg=org.UrlWallet.Wallet.keyWallet().processSign(wifKey, message);
   } catch(Exception e) {
	   msg = "Error: "+e.getMessage();
   }
   msg = "sign:"+msg;
%>
<%=msg%>
