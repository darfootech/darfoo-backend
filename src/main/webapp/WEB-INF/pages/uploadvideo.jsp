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
                alert(data);
                alert("上传视频成功");
                location.reload();
            },
            error : function(){
                alert("上传视频失败");
            }
        })
    }
</script>

<form id="createvideoform" name="createvideoform">
    <input type="text" name="message" id="message"/>
    <input type="button" value="上传舞蹈视频" onclick="start()"/>
</form>

<%@include file="footer.jsp"%>