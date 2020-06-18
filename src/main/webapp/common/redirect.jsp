<!DOCTYPE html>
<%
    String addr = request.getParameter("addr");
%>
<html>
    <head>
        <title>Redirecting you to <%if(addr != null) out.println(addr);%></title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <script type="text/javascript">
        function reloadNew() {
			<%if(addr != null) out.println("window.location.href = \""+addr+"\"");%>
        }
        <%if(addr != null) out.println("window.onload = reloadNew"); %>
        </script>
    </head>
    <body>

    </body>
</html>
