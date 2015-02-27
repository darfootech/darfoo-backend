<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>
<%@include file="../update/updatecota.jsp"%>

<script>
    $(function () {
        var type = $("#type").text();
        var id = $("#id").text();

        $("#kickout").click(function(){
            $.ajax({
                type: "POST",
                url: "/darfoobackend/rest/admin/" + type + "/delete",
                data: {"id": id},
                success: function (data) {
                    if (data == "200") {
                        alert("删除视频信息成功");
                        window.location.href = "/darfoobackend/rest/admin/gallery/" + type + "/all"
                    } else if (data == "500") {
                        alert("删除视频信息失败");
                    } else {
                        alert("删除视频信息失败");
                    }
                },
                error: function () {
                    alert("删除视频信息失败");
                }
            });
        });
    });
</script>

<div class="container">
    <h1>填写舞蹈视频信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form">
                <div class="form-group">
                    <label for="title">舞蹈视频标题</label>
                    <input type="text" class="form-control" name="title" id="title" value="${video.title}" disabled="disabled">
                </div>

                <div class="form-group">
                    <a href="${videourl}" target="_blank">点击此处预览舞蹈视频</a>
                </div>

                <button type="button" class="btn btn-default" id="kickout">删除舞蹈视频</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>