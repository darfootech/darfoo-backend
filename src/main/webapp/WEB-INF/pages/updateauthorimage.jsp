<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<div class="container">
    <h1>更新明星舞队图片</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" action="/darfoobackend/rest/admin/author/updateimageresource" method="post"
                  enctype="multipart/form-data">
                <div style="display: none">
                    <input type="text" name="id" value="${authorid}">
                </div>

                <div class="form-group">
                    <label for="imageresource">选择要上传的视频封面图片(请确保是JPG或者PNG格式的图片文件)</label>
                    <input type="file" id="imageresource" name="imageresource">
                </div>

                <button type="submit" class="btn btn-default">更新明星舞队图片</button>
            </form>
        </div>
    </div>
</div>

<%@include file="footer.jsp" %>