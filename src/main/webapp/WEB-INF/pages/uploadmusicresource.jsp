<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp"%>

<div class="container">
    <h1>上传舞蹈伴奏资源</h1>
    <div class="row">
        <div class="col-md-12">
            <!--<form role="form" action="/darfoobackend/rest/resources/musicresource/create" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="musicresource">选择要上传的音频资源(请确保是MP3格式的音频文件)</label>
                    <input type="file" id="musicresource" name="musicresource">
                </div>

                <div class="form-group">
                    <label for="imageresource">选择要上传的视频封面图片(请确保是JPG或者PNG格式的图片文件)</label>
                    <input type="file" id="imageresource" name="imageresource">
                </div>

                <button type="submit" class="btn btn-default">上传舞蹈伴奏资源</button>
            </form>-->

            <form role="form" action="/darfoobackend/rest/resources/musicresourcenopic/create" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="musicresource">选择要上传的音频资源(请确保是MP3格式的音频文件)</label>
                    <input type="file" id="musicresource" name="musicresource">
                </div>

                <button type="submit" class="btn btn-default">上传舞蹈伴奏资源</button>
            </form>
        </div>
    </div>
</div>

<%@include file="footer.jsp"%>