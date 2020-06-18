<%    
    /*
    // Before proceeding, first check for session (to give captcha)
    // Unfortunately this is needed because we regularly receive hundreds/thousands of requests from bots trying a DoS
    
    // However, for testing on localhost, we don't want sessions. So we ensure that first */
    if(request.getServerName().equals("localhost")){
        session.setAttribute("isHuman", true);
    }
    if (session.getAttribute("isHuman") == null) {
        // start new session by displaying captchas
%>

<title>Captcha Verification</title>
</head>
    <body>
      <div id="pagecontainer" class="pagecontainer">  
        <%@ include file="/WEB-INF/jsp/common/header.jsp" %>
        <% 
        if (session.getAttribute("sessionCaptcha")!=null && request.getParameter("answer")!=null) {
            // captcha returned. submitting captcha page
            String sessionCaptcha = session.getAttribute("sessionCaptcha").toString();
            String userAnswer = request.getParameter("answer");
            if (userAnswer.equals(sessionCaptcha)) {
                session.setAttribute("isHuman", true); 
                session.removeAttribute("sessionCaptcha");                 
                response.sendRedirect(captchaSubmitUrl(request));
            } else {
                %>
                <table style="width: 250px; margin-left:auto; margin-right:auto; vertical-align: middle; text-align:center;color: #e6e6e6;" border="0">
                    <tbody>
                    <tr><td><font face="verdana">Incorrect answer!</font><br></td> </tr>
                    </tbody>
                </table>                
                <%
            }
        }
        %>
        <form id="captchaForm" target="_self" action="<%out.print(captchaSubmitUrl(request));%>" method="get">
            <div>
            <table style="background:black; width: 250px; margin-left:auto; margin-right:auto; vertical-align: middle; text-align:center;" border="0">
                <tbody>
                <tr><td><font color="white" face="verdana">Enter the numbers below</font></td></tr>
                <tr><td><%@ include file="/WEB-INF/jsp/common/showCaptchaBox.jsp" %></td></tr>
                <tr><td><br><input value="Submit" type="submit"></td></tr> 
                </tbody>
            </table>
            </div>
        </form>
      </div>
    </body>
<%  
        captchaDone = false;
    } else {
        captchaDone = true;
    }
    
%>