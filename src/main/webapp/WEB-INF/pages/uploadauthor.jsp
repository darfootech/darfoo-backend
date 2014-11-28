<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp"%>

<script>
    function start(){
        $.ajax({
            type : "POST",
            url : "/darfoobackend/rest/resources/author/create",
            data : $("#createauthorform").serialize(),
            success : function(data){
                alert(data);
                alert("创建作者成功");
                location.reload();
            },
            error : function(){
                alert("创建作者失败");
            }
        })
    }
</script>

<div class="container">
    <h1>创建作者(视频,伴奏)</h1>
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
                <button type="button" class="btn btn-default" onclick="start()">创建作者</button>
            </form>
        </div>
    </div>
</div>

<%@include file="footer.jsp"%>