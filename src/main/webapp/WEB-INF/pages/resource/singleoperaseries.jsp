<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>
<%@include file="../update/updatecota.jsp" %>

<script src="/darfoobackend/resources/js/modifyresource.js"></script>

<div class="container">
    <h1>查看与修改越剧连续剧信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="updateresourceform">
                <div style="display: none">
                    <input type="text" name="id" value="${series.id}">
                </div>

                <div class="form-group">
                    <label for="title">越剧连续剧名字</label>
                    <input type="text" class="form-control" name="title" id="title" value="${series.title}"/>
                </div>

                <div class="form-group">
                    <img src="${imageurl}" width="600" height="600">
                </div>

                <div class="btn-toolbar">
                    <div class="btn-group btn-group-lg">
                        <button type="button" class="btn btn-default" id="update">更新越剧连续剧信息</button>
                        <c:choose>
                            <c:when test="${role == 'cleantha'}">
                                <button type="button" class="btn btn-default" id="kickout">删除越剧连续剧</button>
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                        <button type="button" class="btn btn-default" id="updateimage">更新越剧连续剧封面图片</button>
                    </div>
                </div>
                <br/>
                <div class="btn-toolbar">
                    <div class="btn-group btn-group-lg">
                        <a href="/darfoobackend/rest/admin/download/operaseriesvideos/${series.id}">
                            <button type="button" class="btn btn-default">
                                下载越剧连续剧视频资源表格
                            </button>
                        </a>
                        <a href="/darfoobackend/rest/admin/operaseries/videos/${series.id}">
                            <button type="button" class="btn btn-default">
                                查看和越剧连续剧关联的视频资源
                            </button>
                        </a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
