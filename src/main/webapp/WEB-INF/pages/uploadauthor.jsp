<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="header.jsp" %>

<script>
    function start() {
        $.ajax({
            type: "POST",
            url: "/darfoobackend/rest/resources/author/create",
            data: $("#createauthorform").serialize(),
            success: function (data) {
                if (data == "200") {
                    alert("创建作者成功");
                    window.location.href = "/darfoobackend/rest/resources/authorresource/new"
                } else if (data == "501") {
                    alert("相同名字的作者已经存在");
                } else if (data == "508") {
                    alert("请填写并上传作者相关的图片");
                } else {
                    alert("创建作者失败")
                }
            },
            error: function () {
                alert("创建作者失败");
            }
        })
    }
</script>

<div class="container">
    <h1>创建作者-明星舞队(视频,教程,伴奏)</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createauthorform" name="createauthorform">
                <div class="form-group">
                    <label for="name">作者名字</label>
                    <input type="text" class="form-control" name="name" id="name" placeholder="请输入作者名字">
                </div>
                <div class="form-group">
                    <label for="description">作者简介</label>
                    <input type="text" class="form-control" name="description" id="description" placeholder="请输入作者简介">
                </div>
                <div class="form-group">
                    <label for="imagekey">作者图片标题(也就是上传图片文件的文件名,需要加上后缀)</label>
                    <input type="text" class="form-control" name="imagekey" id="imagekey" placeholder="请输入作者图片名称">
                </div>
                <button type="button" class="btn btn-default" onclick="start()">创建作者</button>
            </form>
        </div>
    </div>
</div>

<%@include file="footer.jsp" %>