<%@ page language="java" contentType="application/x-www-form-urlencoded; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/WEB-INF/jsp/common/init.jsp" %> 
<%
String msg="";
String reload="reload";
try {
%>
<%@ include file="/WEB-INF/jsp/common/checkSession.jsp" %>
<%
     String addr = request.getParameter("addr");
     org.UrlWallet.CoinBalance balance = org.UrlWallet.Wallet.coinReader().getBalance(addr);
	 String longString=String.valueOf(balance.value().toString(10));
     String tokens = balance.tokensString();
     String unconfirmedWarning = "";
     if (!balance.isConfirmed()) {
        unconfirmedWarning = "<a href='javascript:void(null)' onclick='window.alert(\"Unconfirmed balance. Please wait for a few minutes.\")'><img HEIGHT='13' WIDTH='13' src='/static/common/warning.png'/></a>";
     }
     msg = org.UrlWallet.Wallet.formatUtil().formatStringNumber(longString)+unconfirmedWarning+";"+tokens;
   } catch(Exception e) {
     // e.printStackTrace();
	 msg = "error";
   }
msg = "read:"+msg; 
%>
<%=msg%>

