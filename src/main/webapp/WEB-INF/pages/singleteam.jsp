<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="header.jsp"%>

<script>
    function update(){
        var createteamUrl = "/darfoobackend/rest/admin/team/update";
        $.ajax({
            type : "POST",
            url : createteamUrl,
            data : $("#createteamform").serialize(),
            success : function(data){
                if(data == "200"){
                    alert("更新伴奏信息成功");
                    window.location.href = "/darfoobackend/rest/admin/team/all"
                }else if(data == "505"){
                    alert("请确保舞队首字母填写的是一个不区分大小写的英文字母");
                }else{
                    alert("更新伴奏信息失败");
                }
            },
            error : function(){
                alert("更新伴奏信息失败");
            }
        })
    }

    function kickout(){
        var teamid = $("#teamid").text();

        var targeturl = "/darfoobackend/rest/admin/team/delete";

        $.ajax({
            type : "POST",
            url : targeturl,
            data : {"id":teamid},
            success : function(data){
                if(data == "200"){
                    alert("删除伴奏信息成功");
                    window.location.href = "/darfoobackend/rest/admin/team/all"
                }else if(data == "505"){
                    alert("删除伴奏信息失败");
                }else{
                    alert("删除伴奏信息失败");
                }
            },
            error : function(){
                alert("删除伴奏信息失败");
            }
        })
    }
</script>

<div id="teamid" style="display: none">${team.id}</div>

<div class="container">
    <h1>查看与修改舞队信息</h1>
    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createteamform" name="createteamform">
                <div style="display: none">
                    <input type="text" name="id" value="${team.id}">
                </div>

                <div class="form-group">
                    <label for="name">舞队名字</label>
                    <input type="text" class="form-control" name="name" id="name" placeholder="${team.name}"/>
                </div>

                <div class="form-group">
                    <label for="description">舞队简介</label>
                    <input type="text" class="form-control" name="description" id="description" placeholder="${team.description}"/>
                </div>

                <div style="display: none">
                    <input type="text" name="imagekey" value="${team.image.image_key}">
                </div>

                <div class="form-group">
                    <label for="imagekey">舞队封面图片标题(也就是上传图片文件的文件名,需要加上后缀)</label>
                    <input type="text" class="form-control" id="imagekey" placeholder="${team.image.image_key}" disabled="disabled">
                </div>

                <button type="button" class="btn btn-default" onclick="update()">更新舞队信息</button>
                <button type="button" class="btn btn-default" onclick="kickout()">删除舞队</button>
            </form>
        </div>
    </div>
</div>


<%@include file="footer.jsp"%>
