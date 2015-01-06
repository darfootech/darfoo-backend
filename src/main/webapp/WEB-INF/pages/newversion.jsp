<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp"%>

<div class="container">
    <h1>上传新版launcher文件</h1>
    <div class="row">
        <div class="col-md-12">
            <form role="form" action="/darfoobackend/rest/admin/version/create" method="post" id="createvideoform" name="createvideoform" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="version">之前的版本为${version}</label>
                    <input type="text" class="form-control" name="newversion" id="version" placeholder="请输入launcher的最新版本号">
                </div>

                <div class="form-group">
                    <label for="versionresource">选择要上传的新版launcher文件(请确保是APK格式的文件)</label>
                    <input type="file" id="versionresource" name="versionresource">
                </div>

                <button type="submit" class="btn btn-default">上传新版launcher</button>
            </form>
        </div>
    </div>
</div>

<%@include file="footer.jsp"%>