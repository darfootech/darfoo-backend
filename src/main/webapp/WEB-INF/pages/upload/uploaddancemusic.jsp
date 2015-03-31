<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script src="/darfoobackend/resources/js/uploadresource.js"></script>

<div id="type" style="display: none">${type}</div>

<div class="container">
    <h1>填写舞蹈伴奏信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createresourceform">
                <div class="form-group">
                    <label for="title">舞蹈伴奏标题(也就是上传音频文件的文件名,不需要后缀)</label>
                    <input type="text" class="form-control" name="title" id="title" placeholder="请输入舞蹈伴奏标题">
                </div>

                <div class="form-group">
                    <label for="authorname">舞蹈伴奏舞队名字</label>
                    <input type="text" class="form-control" name="authorname" id="authorname" placeholder="请输入舞蹈伴奏舞队名字">
                </div>

                <div class="form-group">
                    <label for="musicletter">伴奏首字母(大小写均可)</label>
                    <input type="text" class="form-control" name="musicletter" id="musicletter"
                           placeholder="请输入舞蹈伴奏首字母,大小写均可">
                </div>

                <button type="button" class="btn btn-default" id="start">提交舞蹈伴奏信息</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>