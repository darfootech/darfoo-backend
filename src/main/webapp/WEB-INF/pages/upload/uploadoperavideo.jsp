<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script src="/darfoobackend/resources/js/searchbelongto.js?t=1430060969"></script>
<script src="/darfoobackend/resources/js/uploadresource.js?t=1430060969"></script>

<div id="type" style="display: none">${type}</div>

<div class="container">
    <h1>填写越剧视频信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createresourceform">
                <div class="form-group">
                    <label for="title">越剧视频标题(也就是上传视频文件的文件名,不需要后缀)</label>
                    <input type="text" class="form-control" name="title" id="title" placeholder="请输入越剧视频标题">
                </div>

                <c:choose>
                    <c:when test="${innertype == 'SERIES'}">
                        <div class="form-group">
                            <label for="seriesname">越剧视频关联的越剧连续剧</label>
                            <select data-toggle="select" name="seriesname" id="seriesname"
                                    class="form-control select select-success mrs mbm">
                                <c:forEach var="series" items="${serieses}">
                                    <option value="${series.title}">${series.title}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose>

                <div class="form-group">
                    <label for="videotype">越剧视频文件格式</label>
                    <select data-toggle="select" name="videotype" id="videotype"
                            class="form-control select select-success mrs mbm">
                        <option value="mp4">mp4</option>
                        <option value="flv">flv</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="imagetype">越剧视频图片文件格式</label>
                    <select data-toggle="select" name="imagetype" id="imagetype"
                            class="form-control select select-success mrs mbm">
                        <option value="jpg">jpg</option>
                        <option value="png">png</option>
                    </select>
                </div>

                <div style="display: none">
                    <input name="innertype" type="text" value="${innertype}"/>
                </div>

                <button type="button" class="btn btn-default" id="start">提交越剧视频信息</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>