<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="en">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=8; IE=9; IE=10">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="generator" content="openElement (1.35)">
<%@ include file="/WEB-INF/jsp/common/noscript.jsp" %> 
<% 
boolean captchaDone = false;  
%>


<script>
    function reloadpic()
    {
     document.images["captcha"].src = "/static/common/CircularProcessing.gif";
     var timestamp = new Date().getTime();
     document.images["captcha"].src = "/captcha/img"+timestamp;
    }
</script>

<%
String jspPath = session.getServletContext().getRealPath("/");
java.nio.file.Path file = new java.io.File(jspPath+"/static/common/view.css").toPath();
java.nio.file.attribute.BasicFileAttributes attr = java.nio.file.Files.readAttributes(file, java.nio.file.attribute.BasicFileAttributes.class);
String version = Long.toString(attr.creationTime().toMillis());
%>

<link rel="stylesheet" type="text/css" href="/static/common/view.css?v=<%out.print(version);%>" media="all">
