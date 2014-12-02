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
                if(data == "200"){
                    alert("提交视频信息成功");
                    window.location.href = "/darfoobackend/rest/resources/videoresource/new"
                }else if(data == "505"){
                    alert("请确保舞蹈视频首字母填写的是一个不区分大小写的英文字母");
                }else if(data == "503"){
                    alert("相同标题的舞蹈视频已经存在了，请修改舞蹈视频标题");
                }else if(data == "502"){
                    alert("相同名字的图片已经存在了，请修改上传图片的名字");
                }else if(data == "501"){
                    alert("舞蹈视频的作者还不存在，请先创建舞蹈视频作者");
                }else{
                    alert("提交视频信息失败");
                }
            },
            error : function(){
                alert("提交视频信息失败");
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
                        <option value="较快">较快</option>
                        <option value="适中">适中</option>
                        <option value="较慢">较慢</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="videodifficult">舞蹈难度</label>
                    <select data-toggle="select" name="videodifficult" id="videodifficult" class="form-control select select-success mrs mbm">
                        <option value="简单">简单</option>
                        <option value="中等">中等</option>
                        <option value="稍难">稍难</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="videostyle">舞蹈风格</label>
                    <select data-toggle="select" name="videostyle" id="videostyle" class="form-control select select-success mrs mbm">
                        <option value="欢快">欢快</option>
                        <option value="活泼">活泼</option>
                        <option value="优美">优美</option>
                        <option value="情歌风">情歌风</option>
                        <option value="红歌">红歌⻛</option>
                        <option value="草原风">草原风</option>
                        <option value="戏曲⻛">戏曲⻛</option>
                        <option value="印巴⻛">印巴⻛</option>
                        <option value="江南⻛">江南⻛</option>
                        <option value="民歌风">民歌风</option>
                        <option value="儿歌⻛">儿歌⻛</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="videoletter">视频首字母(大小写均可)</label>
                    <input type="text" class="form-control" name="videoletter" id="videoletter" placeholder="请输入舞蹈视频首字母,大小写均可">
                </div>

                <button type="button" class="btn btn-default" onclick="start()">提交舞蹈视频信息</button>
            </form>
        </div>
    </div>
</div>

<%@include file="footer.jsp"%>