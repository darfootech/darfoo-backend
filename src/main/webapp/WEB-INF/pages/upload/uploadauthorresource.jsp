<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../header.jsp" %>

<div class="container">
    <h1>上传作者(明星舞队)资源</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" action="/darfoobackend/rest/resources/authorresource/create" method="post"
                  enctype="multipart/form-data">
                <div class="form-group">
                    <label for="imageresource">选择要上传的作者图片(请确保是JPG或者PNG格式的图片文件)</label>
                    <input type="file" id="imageresource" name="imageresource">
                </div>

                <button type="submit" class="btn btn-default">上传作者资源</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>