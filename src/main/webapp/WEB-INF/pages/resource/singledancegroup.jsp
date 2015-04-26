<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>
<%@include file="../update/updatecota.jsp" %>

<script src="/darfoobackend/resources/js/modifyresource.js?t=1430060969"></script>

<div class="container">
    <h1>查看与修改舞队信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="updateresourceform">
                <div style="display: none">
                    <input type="text" name="id" value="${author.id}">
                </div>

                <div class="form-group">
                    <label for="title">舞队名字</label>
                    <input type="text" class="form-control" name="title" id="title" value="${author.title}"/>
                </div>

                <div class="form-group">
                    <label for="description">舞队简介</label>
                    <textarea type="text" class="form-control" name="description" id="description"
                              >${author.description}</textarea>
                </div>

                <c:choose>
                    <c:when test="${empty author.image || updateauthorimage == 1}">
                        <h3>该舞队还没有上传图片,请上传</h3>

                        <div class="form-group">
                            <label for="newimagekey">舞队图片标题(也就是上传图片文件的文件名,需要加上后缀)</label>
                            <input type="text" class="form-control" name="imagekey" id="newimagekey"
                                   placeholder="请输入舞队图片名称">
                        </div>
                    </c:when>

                    <c:otherwise>
                        <div style="display: none">
                            <input type="text" name="imagekey" value="${author.image.image_key}">
                        </div>
                    </c:otherwise>
                </c:choose>

                <div class="form-group">
                    <img src="${imageurl}" width="600" height="600">
                </div>

                <div class="btn-toolbar">
                    <div class="btn-group btn-group-lg">
                        <button type="button" class="btn btn-default" id="update">更新舞队信息</button>
                        <c:choose>
                            <c:when test="${role == 'cleantha'}">
                                <button type="button" class="btn btn-default" id="kickout">删除舞队</button>
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                        <button type="button" class="btn btn-default updateresource" id="updateimage">更新舞队封面图片</button>
                    </div>
                </div>
                <br/>
                <div class="btn-toolbar">
                    <div class="btn-group btn-group-lg">
                        <a href="/darfoobackend/rest/admin/download/dancegroup/videos/${author.id}">
                            <button type="button" class="btn btn-default">
                                下载舞队视频资源表格
                            </button>
                        </a>
                        <a href="/darfoobackend/rest/admin/dancegroup/videos/${author.id}">
                            <button type="button" class="btn btn-default">
                                查看和舞队关联的视频资源
                            </button>
                        </a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
