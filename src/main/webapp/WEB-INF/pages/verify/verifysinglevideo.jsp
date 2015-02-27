<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script>
    $(function () {
        var type = $("#type").text();
        var videoid = $("#videoid").text();

        $("#kickout").click(function(){
            $.ajax({
                type: "POST",
                url: "/darfoobackend/rest/admin/" + type + "/delete",
                data: {"id": videoid},
                success: function (data) {
                    if (data == "200") {
                        alert("删除视频信息成功");
                        window.location.href = "/darfoobackend/rest/admin/gallery/" + type + "/all"
                    } else if (data == "505") {
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

<div id="videoid" style="display: none">${videoid}</div>
<div id="type" style="display: none">${type}</div>

<div class="container">
    <h1>填写舞蹈视频信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form">
                <div class="form-group">
                    <label for="title">舞蹈视频标题</label>
                    <input type="text" class="form-control" name="title" id="title" value="${title}">
                </div>

                <div style="display: none">
                    <input type="text" name="videokey" value="${videokey}">
                </div>

                <div style="display: none">
                    <input type="text" name="imagekey" value="${imagekey}">
                </div>

                <div class="form-group">
                    <label for="imagekey">舞蹈视频封面图片预览</label>
                    <img src="${imageurl}" id="imagekey" width="600" height="600" style="display:block">
                </div>

                <div class="form-group">
                    <a href="${videourl}" target="_blank">点击此处预览舞蹈视频</a>
                </div>

                <div class="form-group">
                    <label for="videotype">舞蹈视频文件格式</label>
                    <input type="text" class="form-control" name="videotype" id="videotype" value="${videotype}"
                           disabled="disabled">
                </div>

                <button type="button" class="btn btn-default" id="kickout">删除舞蹈视频</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>