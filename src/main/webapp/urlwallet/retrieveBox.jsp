<%@ page language="java" contentType="application/x-www-form-urlencoded; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/WEB-INF/jsp/common/init.jsp" %>
<%
String msg="";
String reload="reload";
try {
%>
<%@ include file="/WEB-INF/jsp/common/checkSession.jsp" %>
<%
     String boxId = request.getParameter("boxId");
	 org.UrlWallet.InputBox box = org.UrlWallet.Wallet.coinReader().getBoxById(boxId);
	 if (box.isSpent()) {
	    msg = "Error: Box is already spent";
	 } else {
        // msg = box.getAddressShortened()+"<br>"+org.UrlWallet.Wallet.formatUtil().formatStringNumber(box.amount().toString(10))+" "+org.UrlWallet.Wallet.coinUtil().symbol()+" "+box.getTokenStr("<br>");
        msg = box.getAddressShortened()+";"+org.UrlWallet.Wallet.formatUtil().formatStringNumber(box.amount().toString(10))+";"+box.getTokenStr(":", " ");
	 }
   } catch(Exception e) {
     // e.printStackTrace();
	 msg = "Error: Invalid boxId";
   }
msg = "read:"+msg;
%>
<%=msg%>

