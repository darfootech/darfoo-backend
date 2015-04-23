<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>
<%@include file="../update/updatecota.jsp" %>

<script src="/darfoobackend/resources/js/modifyresource.js"></script>

<div class="container">
    <h1>查看与修改广告信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="updateresourceform">
                <div style="display: none">
                    <input type="text" name="id" value="${advertise.id}">
                </div>

                <div class="form-group">
                    <label for="title">广告名字</label>
                    <input type="text" class="form-control" name="title" id="title" value="${advertise.title}"/>
                </div>

                <div class="form-group">
                    <img src="${imageurl}" width="600" height="600">
                </div>

                <div class="btn-toolbar">
                    <div class="btn-group btn-group-lg">
                        <button type="button" class="btn btn-default" id="update">更新广告信息</button>
                        <c:choose>
                            <c:when test="${role == 'cleantha'}">
                                <button type="button" class="btn btn-default" id="kickout">删除广告</button>
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                        <button type="button" class="btn btn-default" id="updateimage">更新广告封面图片</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
