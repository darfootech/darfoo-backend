<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../header.jsp" %>

<div class="container">
    <h1>上传广告资源</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" action="/darfoobackend/rest/resources/dancegroup/image/create" method="post"
                  enctype="multipart/form-data">
                <div class="form-group">
                    <label for="imageresource">选择要上传的广告图片(请确保是JPG或者PNG格式的图片文件)</label>
                    <input type="file" id="imageresource" name="imageresource">
                </div>

                <button type="submit" class="btn btn-default">上传广告资源</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>