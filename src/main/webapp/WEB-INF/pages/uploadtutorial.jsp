<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp"%>

<script>
    function start(){
        var createTutorialUrl = "/darfoobackend/rest/resources/tutorial/create";
        $.ajax({
            type : "POST",
            url : createTutorialUrl,
            data : $("#createtutorialform").serialize(),
            success : function(data){
                if(data ==  "cleantha"){
                    alert("提交教程信息成功");
                    window.location.href = "/darfoobackend/rest/resources/tutorialresource/new"
                }else{
                    alert("提交教程信息失败")
                }
            },
            error : function(){
                alert("提交教程信息失败");
            }
        })
    }
</script>

<div class="container">
    <h1>填写教学视频信息</h1>
    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createtutorialform" name="createtutorialform">
                <div class="form-group">
                    <label for="title">教学视频标题(也就是上传视频文件的文件名,不需要后缀)</label>
                    <input type="text" class="form-control" name="title" id="title" placeholder="请输入教学视频标题">
                </div>
                <div class="form-group">
                    <label for="authorname">教学视频作者名字</label>
                    <input type="text" class="form-control" name="authorname" id="authorname" placeholder="请输入教学视频作者名字">
                </div>
                <div class="form-group">
                    <label for="imagekey">教学视频封面图片标题(也就是上传图片文件的文件名,需要加上后缀)</label>
                    <input type="text" class="form-control" name="imagekey" id="imagekey" placeholder="请输入教学视频封面图片名称">
                </div>

                <div class="form-group">
                    <label for="videospeed">教学视频速度</label>
                    <select data-toggle="select" name="videospeed" id="videospeed" class="form-control select select-success mrs mbm">
                        <option value="1">快</option>
                        <option value="2">中</option>
                        <option value="3">慢</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="videodifficult">教学视频难度</label>
                    <select data-toggle="select" name="videodifficult" id="videodifficult" class="form-control select select-success mrs mbm">
                        <option value="1">简单</option>
                        <option value="2">适中</option>
                        <option value="3">稍难</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="videostyle">教学视频类型</label>
                    <select data-toggle="select" name="videostyle" id="videostyle" class="form-control select select-success mrs mbm">
                        <option value="1">队形表演</option>
                        <option value="2">背⾯教学</option>
                        <option value="3">分解教学</option>
                    </select>
                </div>

                <button type="button" class="btn btn-default" onclick="start()">提交教学视频信息</button>
            </form>
        </div>
    </div>
</div>

<%@include file="footer.jsp"%>