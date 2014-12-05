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

    <script>
        function start(){
            $.ajax({
                type : "POST",
                url : "/darfoobackend/rest/login/auth",
                data : $("#loginform").serialize(),
                success : function(data){
                    if(data == "200"){
                        alert("登陆成功");
                        window.location.href = "/darfoobackend/rest/resources/video/new"
                    }else if(data == "501") {
                        alert("登陆失败，用户名或者密码错误");
                    }
                },
                error : function(){
                    alert("登陆失败，用户名或者密码错误");
                }
            })
        }
    </script>

    <!-- Static navbar -->
    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                </button>
                <a class="navbar-brand" href="/darfoobackend/rest/login">大福资源后台登陆</a>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="col-md-4 col-sm-offset-4">
                <h4>后台登陆</h4>
                <form role="form" id="loginform" name="loginform">
                    <div class="form-group">
                        <label for="username">用户名</label>
                        <input type="email" class="form-control" name="username" id="username" placeholder="请输入管理员用户名">
                    </div>
                    <div class="form-group">
                        <label for="password">密码</label>
                        <input type="password" class="form-control" name="password" id="password" placeholder="请输入管理员密码">
                    </div>

                    <button type="button" class="btn btn-default" onclick="start()">登陆</button>
                </form>
            </div>
        </div>
    </div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="/darfoobackend/resources/js/vendor/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="/darfoobackend/resources/js/flat-ui.min.js"></script>

    </body>
</html>
