<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp"%>

<script>
    function start(){
        var createVideoUrl = "/darfoobackend/rest/resources/video/create";
        $.ajax({
            type : "POST",
            url : createVideoUrl,
            data : $("#createvideoform").serialize(),
            success : function(data){
                if(data == "cleantha"){
                    alert("写入视频信息成功");
                }else{
                    alert("写入视频信息失败");
                }
                window.location.href = "/darfoobackend/rest/resources/videoresource/new"
            },
            error : function(){
                alert("写入视频信息失败");
            }
        })
    }
</script>

<div class="container">
    <h1>填写舞蹈视频信息</h1>
    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createvideoform" name="createvideoform">
                <div class="form-group">
                    <label for="title">舞蹈视频标题(也就是上传视频文件的文件名,不需要后缀)</label>
                    <input type="text" class="form-control" name="title" id="title" placeholder="请输入舞蹈视频标题">
                </div>

                <div class="form-group">
                    <label for="authorname">舞蹈视频作者名字</label>
                    <input type="text" class="form-control" name="authorname" id="authorname" placeholder="请输入舞蹈视频作者名字">
                </div>

                <div class="form-group">
                    <label for="imagekey">舞蹈视频封面图片标题(也就是上传图片文件的文件名,需要加上后缀)</label>
                    <input type="text" class="form-control" name="imagekey" id="imagekey" placeholder="请输入舞蹈视频封面图片名称">
                </div>

                <div class="form-group">
                    <label for="videospeed">舞蹈速度</label>
                    <select data-toggle="select" name="videospeed" id="videospeed" class="form-control select select-success mrs mbm">
                        <option value="1">较快</option>
                        <option value="2">较中</option>
                        <option value="3">较慢</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="videodifficult">舞蹈难度</label>
                    <select data-toggle="select" name="videodifficult" id="videodifficult" class="form-control select select-success mrs mbm">
                        <option value="1">简单</option>
                        <option value="2">适中</option>
                        <option value="3">稍难</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="videostyle">舞蹈风格</label>
                    <select data-toggle="select" name="videostyle" id="videostyle" class="form-control select select-success mrs mbm">
                        <option value="1">欢快</option>
                        <option value="2">活泼</option>
                        <option value="3">优美</option>
                        <option value="4">情歌风</option>
                        <option value="5">红歌⻛</option>
                        <option value="6">草原风</option>
                        <option value="7">戏曲⻛</option>
                        <option value="8">印巴⻛</option>
                        <option value="9">江南⻛</option>
                        <option value="10">民歌风</option>
                        <option value="11">儿歌⻛</option>
                    </select>
                </div>

                <button type="button" class="btn btn-default" onclick="start()">提交舞蹈视频信息</button>
            </form>
        </div>
    </div>
</div>

<%@include file="footer.jsp"%>