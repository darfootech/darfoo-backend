<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>
<%@include file="../update/updatecota.jsp"%>

<script src="/darfoobackend/resources/js/modifyresource.js"></script>

<div class="container">
    <h1>查看与修改舞蹈伴奏信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="updateresourceform">
                <div style="display: none">
                    <input type="text" name="id" value="${music.id}">
                </div>

                <div style="display: none">
                    <input type="text" name="title" value="${music.title}">
                </div>

                <div class="form-group">
                    <label for="title">舞蹈伴奏标题(也就是上传伴奏文件的文件名,不需要后缀)</label>
                    <input type="text" class="form-control" id="title" placeholder="${music.title}" disabled="disabled">
                </div>

                <div style="display: none">
                    <input type="text" name="authorname" value="${music.authorname}">
                </div>

                <div class="form-group">
                    <label for="authorname">舞蹈伴奏舞队名字</label>
                    <input type="text" class="form-control" id="authorname"
                           placeholder="${music.authorname}" disabled="disabled">
                </div>

                <div class="form-group">
                    <label for="musicletter">伴奏首字母(大小写均可)</label>
                    <input type="text" class="form-control" name="musicletter" id="musicletter" value="${letter}">
                </div>

                <div class="form-group">
                    <a href="${musicurl}" target="_blank">点击此处预览对应的音频资源</a>
                </div>

                <button type="button" class="btn btn-default" style="margin-right: 100px" id="update">更新舞蹈伴奏信息</button>
                <button type="button" class="btn btn-default" id="kickout">删除舞蹈伴奏</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
