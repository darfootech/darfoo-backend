<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<script>
    function upload() {
        var version = $("#version").val();
        var oldversion = $("#oldversion").text();

        if (isNaN(version)) {
            alert("版本号必须为阿拉伯数字");
            return;
        } else {
            if (!isNaN(oldversion)) {
                if (parseInt(oldversion) > parseInt(version)) {
                    alert("新的版本号必须比之前的版本号要大");
                    return;
                } else {
                    $("#uploadlauncher").submit();
                }
            } else {
                $("#uploadlauncher").submit();
            }
        }
    }
</script>

<div id="oldversion" style="display: none">${latestversion}</div>

<div class="container">
    <h1>上传新版launcher文件</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" action="/darfoobackend/rest/admin/version/${type}/create" method="post"
                  id="uploadlauncher" name="uploadlauncher" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="version">之前的版本为${latestversion}</label>
                    <input type="text" class="form-control" name="newversion" id="version"
                           placeholder="请输入launcher的最新版本号,版本号必须为阿拉伯数字并且比之前的版本号大">
                </div>

                <div class="form-group">
                    <label for="versionresource">选择要上传的新版launcher文件(请确保是APK格式的文件)</label>
                    <input type="file" id="versionresource" name="versionresource">
                </div>

                <button type="button" class="btn btn-default" onclick="upload()">上传新版launcher</button>
            </form>
        </div>
    </div>
</div>

<%@include file="footer.jsp" %>