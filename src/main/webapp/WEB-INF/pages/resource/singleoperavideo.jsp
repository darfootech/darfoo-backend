<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>
<%@include file="../update/updatecota.jsp" %>

<script src="/darfoobackend/resources/js/searchbelongto.js?t=1430060969"></script>
<script src="/darfoobackend/resources/js/modifyresource.js?t=1430060969"></script>

<div class="container">
    <h1>查看与修改越剧视频信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="updateresourceform">
                <div style="display: none">
                    <input type="text" name="id" value="${video.id}">
                </div>

                <div style="display: none">
                    <input type="text" name="type" value="${video.type}">
                </div>

                <div class="form-group">
                    <label for="title">越剧视频标题(也就是上传视频文件的文件名,不需要后缀)</label>
                    <input type="text" class="form-control" name="title" id="title" value="${video.title}">
                </div>

                <c:choose>
                    <c:when test="${innertype == 'SERIES'}">
                        <div class="form-group">
                            <label for="seriesname">关联的越剧连续剧</label>
                            <select data-toggle="select" name="seriesname" id="seriesname"
                                    class="form-control select select-success mrs mbm">
                                <c:forEach var="series" items="${serieses}">
                                    <c:choose>
                                        <c:when test="${series.title == video.series.title}">
                                            <option value="${series.title}" selected="selected">${series.title}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${series.title}">${series.title}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                        </div>
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose>

                <div class="form-group">
                    <img src="${imageurl}" width="600" height="600">
                </div>

                <div class="form-group">
                    <a href="${videourl}" target="_blank">点击此处预览对应的视频资源</a>
                </div>

                <div class="btn-toolbar">
                    <div class="btn-group btn-group-lg">
                        <button type="button" class="btn btn-default" id="update">更新越剧视频信息</button>
                        <c:choose>
                            <c:when test="${role == 'cleantha'}">
                                <button type="button" class="btn btn-default" id="kickout">删除越剧视频</button>
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                        <button type="button" class="btn btn-default updateresource" id="updateimage">更新越剧视频封面图片</button>
                        <button type="button" class="btn btn-default updateresource" id="updatevideo">更新越剧视频视频资源</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
