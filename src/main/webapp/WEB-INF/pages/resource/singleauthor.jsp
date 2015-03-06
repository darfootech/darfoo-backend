<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>
<%@include file="../update/updatecota.jsp"%>

<script src="/darfoobackend/resources/js/modifyresource.js"></script>

<div class="container">
    <h1>查看与修改明星舞队信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="updateresourceform">
                <div style="display: none">
                    <input type="text" name="id" value="${author.id}">
                </div>

                <div class="form-group">
                    <label for="name">明星舞队名字</label>
                    <input type="text" class="form-control" name="name" id="name" placeholder="${author.name}"/>
                </div>

                <div class="form-group">
                    <label for="description">明星舞队简介</label>
                    <input type="text" class="form-control" name="description" id="description"
                           placeholder="${author.description}"/>
                </div>

                <c:choose>
                    <c:when test="${empty author.image || updateauthorimage == 1}">
                        <h3>该明星舞队还没有上传图片,请上传</h3>

                        <div class="form-group">
                            <label for="newimagekey">明星舞队图片标题(也就是上传图片文件的文件名,需要加上后缀)</label>
                            <input type="text" class="form-control" name="imagekey" id="newimagekey"
                                   placeholder="请输入明星舞队图片名称">
                        </div>
                    </c:when>

                    <c:otherwise>
                        <div style="display: none">
                            <input type="text" name="imagekey" value="${author.image.image_key}">
                        </div>

                        <div class="form-group">
                            <label for="imagekey">明星舞队图片标题(也就是上传图片文件的文件名,需要加上后缀)</label>
                            <input type="text" class="form-control" id="imagekey" placeholder="${author.image.image_key}"
                                   disabled="disabled">
                        </div>
                    </c:otherwise>
                </c:choose>

                <div class="form-group">
                    <img src="${imageurl}" width="600" height="600">
                </div>

                <button type="button" class="btn btn-default" style="margin-right: 100px" id="update">更新明星舞队信息</button>
                <button type="button" class="btn btn-default" style="margin-right: 100px" id="kickout">删除明星舞队</button>
                <button type="button" class="btn btn-default" id="updateimage">更新明星舞队封面图片</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
