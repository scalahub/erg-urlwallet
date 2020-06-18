<%@ page language="java" contentType="application/x-www-form-urlencoded; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/WEB-INF/jsp/common/init.jsp" %> 
<%
   String msg = "";    
   String reload = "reload";
   try {
%>
<%@ include file="/WEB-INF/jsp/common/checkSession.jsp" %>
<%@ include file="/WEB-INF/jsp/common/sendInit.jsp" %>
<%
	String wifKey = request.getParameter("secretData");
	String fee = request.getParameter("customFeeAmt");

	String amtToSend = request.getParameter("amt");
    String tokenToSend = request.getParameter("token");
    String tokenIdToSend = request.getParameter("tokenid");
    String addressToSend = request.getParameter("address");

    String amt2ToSend = request.getParameter("amt2");
	String token2ToSend = request.getParameter("token2");
    String tokenId2ToSend = request.getParameter("tokenid2");
	String address2ToSend = request.getParameter("address2");

	String inputBoxId = request.getParameter("inputBoxId");
	String copyTo = request.getParameter("copyTo");

	boolean allowTokenBurn = Boolean.parseBoolean(request.getParameter("allowTokenBurn"));

	String [] addressesToSend;
	String [] amtsToSend;
	long [] tokensToSend;
	String [] tokenIdsToSend;

	if (address2ToSend == null) {
		addressesToSend = new String[]{addressToSend};
		amtsToSend = new String[]{amtToSend};
		tokensToSend = new long[]{Long.parseLong(tokenToSend)};
		tokenIdsToSend = new String[]{tokenIdToSend};
	} else {
		addressesToSend = new String[]{addressToSend, address2ToSend};
		amtsToSend = new String[]{amtToSend, amt2ToSend};
		tokensToSend = new long[]{Long.parseLong(tokenToSend), Long.parseLong(token2ToSend)};
		tokenIdsToSend = new String[]{tokenIdToSend, tokenId2ToSend};
	}

    msg=org.UrlWallet.Wallet.keyWallet().processSend(wifKey, addressesToSend, amtsToSend, tokensToSend, tokenIdsToSend, fee, inputBoxId, copyTo, allowTokenBurn);
   } catch(Exception e) {
       e.printStackTrace();
	   msg = "Error: "+e.getMessage();
   }
   msg="send:"+msg;
%>
<%=msg%>
