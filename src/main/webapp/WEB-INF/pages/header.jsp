<%--
  Created by IntelliJ IDEA.
  User: zjh
  Date: 14-11-28
  Time: 上午11:08
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="/darfoobackend/resources/js/vendor/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="/darfoobackend/resources/js/flat-ui.min.js"></script>

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
                <li class="<%if(resource.equals("video")){out.write("active");}else{out.write("");}%>"><a href="/darfoobackend/rest/resources/video/new/">舞蹈欣赏</a></li>
                <li class="<%if(resource.equals("tutorial")){out.write("active");}else{out.write("");}%>"><a href="/darfoobackend/rest/resources/tutorial/new/">舞蹈教程</a></li>
                <li class="<%if(resource.equals("music")){out.write("active");}else{out.write("");}%>"><a href="/darfoobackend/rest/resources/music/new/">舞蹈伴奏</a></li>
                <li class="<%if(resource.equals("author")){out.write("active");}else{out.write("");}%>"><a href="/darfoobackend/rest/resources/author/new/">明星舞队</a></li>
                <!--<li class="<%if(resource.equals("team")){out.write("active");}else{out.write("");}%>"><a href="/darfoobackend/rest/resources/team/new/">创建舞队</a></li>-->
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">下载资源表格 <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="/darfoobackend/rest/downloadvideos">下载视频资源表格</a></li>
                        <li><a href="/darfoobackend/rest/downloadtutorials">下载教程资源表格</a></li>
                        <li><a href="/darfoobackend/rest/downloadmusics">下载伴奏资源表格</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">查看和修改 <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="/darfoobackend/rest/admin/video/all">查看修改舞蹈欣赏</a></li>
                        <li><a href="/darfoobackend/rest/admin/tutorial/all">查看修改舞蹈教程</a></li>
                        <li><a href="/darfoobackend/rest/admin/music/all">查看修改舞蹈伴奏</a></li>
                        <li><a href="/darfoobackend/rest/admin/author/all">查看修改明星舞队</a></li>
                        <li><a href="/darfoobackend/rest/admin/connectmusic/all">关联舞蹈伴奏和舞蹈视频</a></li>
                        <li><a href="/darfoobackend/rest/admin/recommend/video">首页推荐舞蹈欣赏</a></li>
                        <li><a href="/darfoobackend/rest/admin/recommend/tutorial">首页推荐舞蹈教程</a></li>
                        <!--<li><a href="/darfoobackend/rest/admin/team/all">查看修改舞队</a></li>-->
                    </ul>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <% if (session != null && session.getAttribute("loginUser") != null){ %>
                    <li class="active"><a href="/darfoobackend/rest/login/out">注销</a></li>
                <% } else { %>
                    <li class="active">未登录</li>
                <% } %>
            </ul>
        </div>
    </div>
</div>






