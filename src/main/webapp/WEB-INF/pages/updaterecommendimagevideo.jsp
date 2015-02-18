<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="header.jsp" %>

<div class="container">
    <h1>查看与修改推荐舞蹈视频封面图片</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" action="/darfoobackend/rest/admin/recommend/video/updateimage" method="post"
                  id="createvideoform" name="createvideoform" enctype="multipart/form-data">
                <!--<div style="display: none">
                    <input type="text" name="id" value="${video.id}">
                </div>-->

                <div class="form-group">
                    <label for="title">舞蹈视频标题</label>
                    <input type="text" id="title" class="form-control" value="${video.title}" disabled="disabled">
                </div>

                <div class="form-group">
                    <label for="authorname">关联的明星舞队</label>
                    <input type="text" id="authorname" class="form-control" value="${video.author.name}"
                           disabled="disabled">
                </div>

                <div class="form-group">
                    <img src="${imageurl}" width="600" height="600">
                </div>

                <div class="form-group">
                    <label for="imageresource">选择要上传的封面图片(请确保是PNG格式的图片文件)</label>
                    <input type="file" id="imageresource" name="imageresource">
                </div>

                <button type="submit" class="btn btn-default">更新推荐舞蹈视频封面图片</button>
            </form>
        </div>
    </div>
</div>


<%@include file="footer.jsp" %>
