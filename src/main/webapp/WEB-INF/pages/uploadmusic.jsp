<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp"%>

<script>
    function start(){
        var createMusicUrl = "/darfoobackend/rest/resources/music/create";
        $.ajax({
            type : "POST",
            url : createMusicUrl,
            data : $("#createmusicform").serialize(),
            success : function(data){
                if(data == "cleantha"){
                    alert("提交伴奏信息成功");
                }else{
                    alert("提交伴奏信息失败")
                }
                window.location.href = "/darfoobackend/rest/resources/musicresource/new"
            },
            error : function(){
                alert("提交伴奏信息失败");
            }
        })
    }
</script>

<div class="container">
    <h1>填写舞蹈伴奏信息</h1>
    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createmusicform" name="createmusicform">
                <div class="form-group">
                    <label for="title">舞蹈伴奏标题(也就是上传音频文件的文件名,不需要后缀)</label>
                    <input type="text" class="form-control" name="title" id="title" placeholder="请输入舞蹈伴奏标题">
                </div>
                <div class="form-group">
                    <label for="authorname">舞蹈伴奏作者名字</label>
                    <input type="text" class="form-control" name="authorname" id="authorname" placeholder="请输入舞蹈伴奏作者名字">
                </div>
                <div class="form-group">
                    <label for="imagekey">舞蹈伴奏封面图片标题(也就是上传图片文件的文件名,需要加上后缀)</label>
                    <input type="text" class="form-control" name="imagekey" id="imagekey" placeholder="请输入舞蹈伴奏封面图片名称">
                </div>

                <div class="form-group">
                    <label for="musicbeat">舞蹈伴奏节拍</label>
                    <select data-toggle="select" name="musicbeat" id="musicbeat" class="form-control select select-success mrs mbm">
                        <option value="1">四拍</option>
                        <option value="2">⼋拍</option>
                        <option value="3">十六拍</option>
                        <option value="4">三⼗二拍</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="musicstyle">舞蹈伴奏风格</label>
                    <select data-toggle="select" name="musicstyle" id="musicstyle" class="form-control select select-success mrs mbm">
                        <option value="1">草原风</option>
                        <option value="2">⼉歌风</option>
                        <option value="3">情歌风</option>
                        <option value="4">红歌风</option>
                        <option value="5">戏曲⻛</option>
                        <option value="6">印巴风</option>
                        <option value="7">江南风</option>
                        <option value="8">民歌⻛</option>
                    </select>
                </div>

                <button type="button" class="btn btn-default" onclick="start()">提交舞蹈伴奏信息</button>
            </form>
        </div>
    </div>
</div>

<%@include file="footer.jsp"%>