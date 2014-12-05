<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="header.jsp"%>

<script>
    function update(){
        var createauthorUrl = "/darfoobackend/rest/admin/author/update";

        $.ajax({
            type : "POST",
            url : createauthorUrl,
            data : $("#createauthorform").serialize(),
            success : function(data){
                if(data == "200"){
                    alert("更新作者信息成功");
                    window.location.href = "/darfoobackend/rest/admin/author/all"
                }else if(data == "505"){
                    alert("请确保作者首字母填写的是一个不区分大小写的英文字母");
                }else{
                    alert("更新作者信息失败");
                }
            },
            error : function(){
                alert("更新作者信息失败");
            }
        })
    }
</script>

<div class="container">
    <h1>查看与修改作者信息</h1>
    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createauthorform" name="createauthorform">
                <div style="display: none">
                    <input type="text" name="id" value="${author.id}">
                </div>

                <div class="form-group">
                    <label for="name">作者名字</label>
                    <input type="text" class="form-control" name="name" id="name" placeholder="${author.name}"/>
                </div>

                <div class="form-group">
                    <label for="description">作者简介</label>
                    <input type="text" class="form-control" name="description" id="description" placeholder="${author.description}"/>
                </div>

                <button type="button" class="btn btn-default" onclick="update()">更新作者信息</button>
            </form>
        </div>
    </div>
</div>


<%@include file="footer.jsp"%>
