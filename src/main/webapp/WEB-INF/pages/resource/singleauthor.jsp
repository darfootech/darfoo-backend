<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>
<%@include file="../update/updatecota.jsp"%>

<script src="/darfoobackend/resources/js/modifyresource.js"></script>

<div class="container">
    <h1>查看与修改作者(明星舞队)信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="updateresourceform">
                <div style="display: none">
                    <input type="text" name="id" value="${author.id}">
                </div>

                <div class="form-group">
                    <label for="name">作者名字</label>
                    <input type="text" class="form-control" name="name" id="name" placeholder="${author.name}"/>
                </div>

                <div class="form-group">
                    <label for="description">作者简介</label>
                    <input type="text" class="form-control" name="description" id="description"
                           placeholder="${author.description}"/>
                </div>

                <div style="display: none">
                    <input type="text" name="imagekey" value="${author.image.image_key}">
                </div>

                <div class="form-group">
                    <label for="imagekey">作者图片标题(也就是上传图片文件的文件名,需要加上后缀)</label>
                    <input type="text" class="form-control" id="imagekey" placeholder="${author.image.image_key}"
                           disabled="disabled">
                </div>

                <c:if test="${empty author.image || updateauthorimage == 1}">
                    <h3>该作者还没有上传图片,请上传</h3>

                    <div class="form-group">
                        <label for="newimagekey">作者图片标题(也就是上传图片文件的文件名,需要加上后缀)</label>
                        <input type="text" class="form-control" name="newimagekey" id="newimagekey"
                               placeholder="请输入作者图片名称">
                    </div>
                </c:if>

                <div class="form-group">
                    <img src="${imageurl}" width="600" height="600">
                </div>

                <button type="button" class="btn btn-default" id="update">更新作者信息</button>
                <button type="button" class="btn btn-default" id="kickout">删除作者</button>
                <button type="button" class="btn btn-default" id="updateimage">更新明星舞队封面图片</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
