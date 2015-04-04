<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script src="/darfoobackend/resources/js/searchdancegroup.js"></script>
<script src="/darfoobackend/resources/js/getalldancemusic.js"></script>
<script src="/darfoobackend/resources/js/uploadresource.js"></script>

<div id="type" style="display: none">${type}</div>

<div class="container">
    <h1>填写舞蹈视频信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createresourceform">
                <div class="form-group">
                    <label for="title">舞蹈视频标题(也就是上传视频文件的文件名,不需要后缀)</label>
                    <input type="text" class="form-control" name="title" id="title" placeholder="请输入舞蹈视频标题">
                </div>

                <div class="form-group">
                    <label for="authorname">舞蹈视频关联的明星舞队</label>
                    <select data-toggle="select" name="authorname" id="authorname"
                            class="form-control select select-success mrs mbm">
                        <c:forEach var="author" items="${authors}">
                            <option value="${author.name}">${author.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="videotype">舞蹈视频文件格式</label>
                    <select data-toggle="select" name="videotype" id="videotype"
                            class="form-control select select-success mrs mbm">
                        <option value="mp4">mp4</option>
                        <option value="flv">flv</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="imagetype">舞蹈视频图片文件格式</label>
                    <select data-toggle="select" name="imagetype" id="imagetype"
                            class="form-control select select-success mrs mbm">
                        <option value="jpg">jpg</option>
                        <option value="png">png</option>
                    </select>
                </div>

                <c:choose>
                    <c:when test="${innertype == 'TUTORIAL'}">
                        <select data-toggle="select" id="categories" name="categories" multiple
                                class="form-control multiselect multiselect-default mrs mbm">
                            <option value="0">正面教学</option>
                            <option value="1">口令分解</option>
                            <option value="2">背面教学</option>
                            <option value="3">队形教学</option>
                        </select>
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose>

                <div class="form-group">
                    <label for="connectmusic">视频要关联的伴奏(没有可以暂时不填)</label>
                    <input class="form-control typeahead-only input-lg" name="connectmusic" type="text"
                           id="connectmusic" placeholder="请输入要关联的伴奏并选择"/>
                </div>

                <div style="display: none">
                    <input name="innertype" type="text" value="${innertype}"/>
                </div>

                <button type="button" class="btn btn-default" id="start">提交舞蹈视频信息</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>