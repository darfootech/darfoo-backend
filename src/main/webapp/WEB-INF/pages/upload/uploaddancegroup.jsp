<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script src="/darfoobackend/resources/js/uploadresource.js"></script>

<div id="type" style="display: none">${type}</div>

<div class="container">
    <h1>填写舞队信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createresourceform">
                <div class="form-group">
                    <label for="name">舞队名字</label>
                    <input type="text" class="form-control" name="name" id="name" placeholder="请输入舞队名字">
                </div>
                <div class="form-group">
                    <label for="description">舞队简介</label>
                    <textarea type="text" class="form-control" name="description" id="description"
                              placeholder="请输入舞队简介"></textarea>
                </div>

                <div class="form-group">
                    <label for="imagetype">舞队图片文件格式</label>
                    <select data-toggle="select" name="imagetype" id="imagetype"
                            class="form-control select select-success mrs mbm">
                        <option value="jpg">jpg</option>
                        <option value="png">png</option>
                    </select>
                </div>

                <div style="display: none">
                    <input name="innertype" type="text" value="${innertype}"/>
                </div>

                <button type="button" class="btn btn-default" id="start">创建舞队</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>