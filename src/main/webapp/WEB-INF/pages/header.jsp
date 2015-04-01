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
                        <li><a href="/darfoobackend/rest/resources/new/dancevideo">舞蹈视频</a></li>
                        <li><a href="/darfoobackend/rest/resources/new/dancemusic">舞蹈伴奏</a></li>
                        <li><a href="/darfoobackend/rest/resources/new/dancegroup">舞蹈舞队</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">查看和修改资源 <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="/darfoobackend/rest/admin/gallery/dancevideo/all">查看修改舞蹈视频</a></li>
                        <li><a href="/darfoobackend/rest/admin/gallery/dancemusic/all">查看修改舞蹈伴奏</a></li>
                        <li><a href="/darfoobackend/rest/admin/gallery/dancegroup/all">查看修改舞队</a></li>
                        <li><a href="/darfoobackend/rest/admin/dancegroup/changetype">切换舞队类型</a></li>
                        <li><a href="/darfoobackend/rest/admin/connectmusic/dancevideo/all">关联舞蹈伴奏和舞蹈视频</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">下载表格 <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="/darfoobackend/rest/admin/download/dancevideo">下载视舞蹈频资源表格</a></li>
                        <li><a href="/darfoobackend/rest/admin/download/dancemusic">下载伴舞蹈奏资源表格</a></li>
                        <li><a href="/darfoobackend/rest/admin/download/feedback">下载用户反馈表格</a></li>
                        <li><a href="/darfoobackend/rest/admin/download/stat/resourceclickcount">下载资源点击热度表格</a></li>
                        <li><a href="/darfoobackend/rest/admin/download/stat/menuclickcount">下载菜单点击热度表格</a></li>
                        <li><a href="/darfoobackend/rest/admin/download/stat/tabclickcount">下载底部菜单点击热度表格</a></li>
                        <li><a href="/darfoobackend/rest/admin/download/stat/resourceclicktime">下载资源点击时间表格</a></li>
                        <li><a href="/darfoobackend/rest/admin/download/stat/menuclicktime">下载菜单点击时间表格</a></li>
                        <li><a href="/darfoobackend/rest/admin/download/stat/tabclicktime">下载底部菜单点击时间表格</a></li>
                        <li><a href="/darfoobackend/rest/admin/download/stat/searchhistory">下载用户搜索记录表格</a></li>
                        <li><a href="/darfoobackend/rest/admin/download/stat/crashlog">下载崩溃日志表格</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">资源推荐 <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="/darfoobackend/rest/admin/recommend/dancevideo">首页推荐舞蹈欣赏</a></li>
                        <li><a href="/darfoobackend/rest/admin/recommend/updateimage/all">查看修改推荐舞蹈视频图片</a></li>
                        <li><a href="/darfoobackend/rest/admin/dancegroup/changehot">推荐热门舞队</a></li>
                        <li><a href="/darfoobackend/rest/admin/dancemusic/changehot">推荐热门舞蹈伴奏</a></li>
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






