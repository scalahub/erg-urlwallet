<%
    if (session.getAttribute("isHuman") == null) throw new Exception("timeout");  //Session ended.
	String csrfChallenge = (String) session.getAttribute("csrf");
	String csrfResponse = request.getParameter("csrf");
	if (! csrfChallenge.equals(csrfResponse)) throw new Exception("csrf"); // possible CSRF attack
%>
