<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>
<%@include file="../update/updatecota.jsp"%>

<script src="/darfoobackend/resources/js/modifyresource.js"></script>

<div class="container">
    <h1>查看与修改舞队信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="updateresourceform">
                <div style="display: none">
                    <input type="text" name="id" value="${author.id}">
                </div>

                <div class="form-group">
                    <label for="name">舞队名字</label>
                    <input type="text" class="form-control" name="name" id="name" placeholder="${author.name}"/>
                </div>

                <div class="form-group">
                    <label for="description">舞队简介</label>
                    <textarea type="text" class="form-control" name="description" id="description"
                           placeholder="${author.description}"></textarea>
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

                        <div class="form-group">
                            <label for="imagekey">舞队图片标题(也就是上传图片文件的文件名,需要加上后缀)</label>
                            <input type="text" class="form-control" id="imagekey" placeholder="${author.image.image_key}"
                                   disabled="disabled">
                        </div>
                    </c:otherwise>
                </c:choose>

                <div class="form-group">
                    <img src="${imageurl}" width="600" height="600">
                </div>

                <button type="button" class="btn btn-default" style="margin-right: 100px" id="update">更新舞队信息</button>
                <button type="button" class="btn btn-default" style="margin-right: 100px" id="kickout">删除舞队</button>
                <button type="button" class="btn btn-default" style="margin-right: 100px" id="updateimage">更新舞队封面图片</button>
                <br/>
                <br/>
                <br/>
                <a href="/darfoobackend/rest/admin/download/authorvideos/${author.id}" style="margin-right: 100px">
                    <button type="button" class="btn btn-default">
                        下载舞队视频资源表格
                    </button>
                </a>
                <a href="/darfoobackend/rest/admin/author/videos/${author.id}">
                    <button type="button" class="btn btn-default">
                        查看和舞队关联的视频资源
                    </button>
                </a>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
