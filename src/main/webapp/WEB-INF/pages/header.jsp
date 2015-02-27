<%--
  Created by IntelliJ IDEA.
  User: zjh
  Date: 14-11-28
  Time: 上午11:08
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String resource = (String) session.getAttribute("resource");
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
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">新建资源 <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="/darfoobackend/rest/resources/video/new">舞蹈欣赏</a></li>
                        <li><a href="/darfoobackend/rest/resources/tutorial/new">舞蹈教程</a></li>
                        <li><a href="/darfoobackend/rest/resources/music/new">舞蹈伴奏</a></li>
                        <li><a href="/darfoobackend/rest/resources/author/new">明星舞队</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">下载表格 <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="/darfoobackend/rest/downloadvideos">下载视频资源表格</a></li>
                        <li><a href="/darfoobackend/rest/downloadtutorials">下载教程资源表格</a></li>
                        <li><a href="/darfoobackend/rest/downloadmusics">下载伴奏资源表格</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">查看和修改资源 <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="/darfoobackend/rest/admin/gallery/video/all">查看修改舞蹈欣赏</a></li>
                        <li><a href="/darfoobackend/rest/admin/gallery/tutorial/all">查看修改舞蹈教程</a></li>
                        <li><a href="/darfoobackend/rest/admin/gallery/music/all">查看修改舞蹈伴奏</a></li>
                        <li><a href="/darfoobackend/rest/admin/gallery/author/all">查看修改明星舞队</a></li>
                        <li><a href="/darfoobackend/rest/admin/connectmusic/video/all">关联舞蹈伴奏和舞蹈视频</a></li>
                        <li><a href="/darfoobackend/rest/admin/connectmusic/tutorial/all">关联舞蹈伴奏和舞蹈教程</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">资源推荐 <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="/darfoobackend/rest/admin/recommend/video">首页推荐舞蹈欣赏</a></li>
                        <li><a href="/darfoobackend/rest/admin/recommend/tutorial">首页推荐舞蹈教程</a></li>
                        <li><a href="/darfoobackend/rest/admin/recommend/updateimage/all">查看修改推荐舞蹈视频图片</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">资源审核 <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="/darfoobackend/rest/admin/gallery/uploadnoauthvideo/all">审核非注册用户上传视频资源</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">资源同步 <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="/darfoobackend/rest/admin/runscript/upload">同步视频音频资源</a></li>
                        <li><a href="/darfoobackend/rest/admin/runscript/resourcevolumn">查看未同步视频音频容量</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">版本发布 <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="/darfoobackend/rest/admin/version/release/new">上传新发布版本launcher</a></li>
                        <li><a href="/darfoobackend/rest/admin/version/debug/new">上传新调试版本launcher</a></li>
                    </ul>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <% if (session != null && session.getAttribute("loginUser") != null) { %>
                <li class="active"><a href="/darfoobackend/rest/login/out">注销</a></li>
                <% } else { %>
                <li class="active">未登录</li>
                <% } %>
            </ul>
        </div>
    </div>
</div>






