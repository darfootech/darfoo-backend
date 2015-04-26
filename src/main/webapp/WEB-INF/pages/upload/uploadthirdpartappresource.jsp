<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../header.jsp" %>

<div class="container">
    <h1>上传第三方软件资源</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" action="/darfoobackend/rest/resources/thirdpartapp/app/create" method="post"
                  enctype="multipart/form-data">
                <div class="form-group">
                    <label for="resource">选择要上传的第三方软件文件(请确保是APK格式的文件)</label>
                    <input type="file" id="resource" name="resource">
                </div>

                <button type="submit" class="btn btn-default">上传第三方软件资源</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>