<%--
  Created by IntelliJ IDEA.
  User: zjh
  Date: 14-11-28
  Time: 上午11:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String resource = (String)session.getAttribute("resource");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>darfoo-backend</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Loading Bootstrap -->
    <link href="/darfoobackend/resources/css/vendor/bootstrap.min.css" rel="stylesheet">

    <!-- Loading Flat UI -->
    <link href="/darfoobackend/resources/css/flat-ui.css" rel="stylesheet">

    <link rel="shortcut icon" href="/darfoobackend/resources/img/favicon.ico?t=1402117937">

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements. All other JS at the end of file. -->
    <!--[if lt IE 9]>
    <script src="/darfoobackend/resources/js/vendor/html5shiv.js"></script>
    <script src="/darfoobackend/resources/js/vendor/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<style>
    body {
        min-height: 2000px;
        padding-top: 70px;
    }
</style>

<!-- Static navbar -->
<div class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
            </button>
            <a class="navbar-brand" href="/darfoobackend/rest/resources/video/new/">大福资源上传后台</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li class="<%if(resource.equals("video")){out.write("active");}else{out.write("");}%>"><a href="/darfoobackend/rest/resources/video/new/">舞蹈视频</a></li>
                <li class="<%if(resource.equals("tutorial")){out.write("active");}else{out.write("");}%>"><a href="/darfoobackend/rest/resources/tutorial/new/">舞蹈教程</a></li>
                <li class="<%if(resource.equals("music")){out.write("active");}else{out.write("");}%>"><a href="/darfoobackend/rest/resources/music/new/">舞蹈伴奏</a></li>
                <li class="<%if(resource.equals("author")){out.write("active");}else{out.write("");}%>"><a href="/darfoobackend/rest/resources/author/new/">创建作者</a></li>
                <li class="<%if(resource.equals("team")){out.write("active");}else{out.write("");}%>"><a href="/darfoobackend/rest/resources/team/new/">创建舞队</a></li>
            </ul>
        </div>
    </div>
</div>






