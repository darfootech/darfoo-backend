<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>
<%@include file="../update/updatecota.jsp" %>

<script src="/darfoobackend/resources/js/modifyresource.js"></script>

<div class="container">
    <h1>查看与修改第三方软件信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="updateresourceform">
                <div style="display: none">
                    <input type="text" name="id" value="${advertise.id}">
                </div>

                <div class="form-group">
                    <label for="title">第三方软件名字</label>
                    <input type="text" class="form-control" name="title" id="title" value="${advertise.title}"/>
                </div>

                <div class="form-group">
                    <a href="${appurl}" target="_blank">点击此处检查上传的第三方软件APK文件</a>
                </div>

                <div class="btn-toolbar">
                    <div class="btn-group btn-group-lg">
                        <button type="button" class="btn btn-default" id="update">更新第三方软件信息</button>
                        <c:choose>
                            <c:when test="${role == 'cleantha'}">
                                <button type="button" class="btn btn-default" id="kickout">删除第三方软件</button>
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                        <button type="button" class="btn btn-default" id="updateimage">更新第三方软件封面图片</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
