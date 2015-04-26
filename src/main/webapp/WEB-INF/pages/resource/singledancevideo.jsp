<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>
<%@include file="../update/updatecota.jsp" %>

<script src="/darfoobackend/resources/js/searchdancegroup.js?t=1430060969"></script>
<script src="/darfoobackend/resources/js/getalldancemusic.js?t=1430060969"></script>
<script src="/darfoobackend/resources/js/modifyresource.js?t=1430060969"></script>

<script>
    $(function () {
        var authorname = $("#oldauthorname").text();

        $('#authorname option[value="' + authorname + '"]').attr("selected", true);
    });
</script>

<div id="oldauthorname" style="display: none">${video.author.title}</div>

<div class="container">
    <h1>查看与修改舞蹈视频信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="updateresourceform">
                <div style="display: none">
                    <input type="text" name="id" value="${video.id}">
                </div>

                <div class="form-group">
                    <label for="title">舞蹈视频标题(也就是上传视频文件的文件名,不需要后缀)</label>
                    <input type="text" class="form-control" name="title" id="title" value="${video.title}">
                </div>

                <div class="form-group">
                    <label for="authorname">关联的明星舞队---
                        <div style="color: green; display: inline; font-size: 18pt">原本为${video.author.title}</div>
                    </label>
                    <select data-toggle="select" name="authorname" id="authorname"
                            class="form-control select select-success mrs mbm">
                        <c:forEach var="author" items="${authors}">
                            <option value="${author.title}">${author.title}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <img src="${imageurl}" width="600" height="600">
                </div>

                <div class="form-group">
                    <a href="${videourl}" target="_blank">点击此处预览对应的视频资源</a>
                </div>

                <c:choose>
                    <c:when test="${innertype == 'TUTORIAL'}">
                        <select data-toggle="select" id="categories" name="categories" multiple
                                class="form-control multiselect multiselect-default mrs mbm">
                            <c:forEach var="category" items="${bindcategories}">
                                <option value="${category.key}" selected>${category.value}</option>
                            </c:forEach>
                            <c:forEach var="category" items="${notbindcategories}">
                                <option value="${category.key}">${category.value}</option>
                            </c:forEach>
                        </select>
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose>

                <div class="form-group">
                    <label for="connectmusic">视频要关联的伴奏(没有可以暂时不填)</label>
                    <input class="form-control typeahead-only input-lg" name="connectmusic" type="text"
                           id="connectmusic" placeholder="${connectmusic}"/>
                </div>

                <div class="btn-toolbar">
                    <div class="btn-group btn-group-lg">
                        <button type="button" class="btn btn-default" id="update">更新舞蹈视频信息</button>
                        <c:choose>
                            <c:when test="${role == 'cleantha'}">
                                <button type="button" class="btn btn-default" id="kickout">删除舞蹈视频</button>
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                        <button type="button" class="btn btn-default" id="updateimage">更新舞蹈视频封面图片</button>
                        <button type="button" class="btn btn-default" id="updatevideo">更新舞蹈视频视频资源</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
