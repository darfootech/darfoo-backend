<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script src="/darfoobackend/resources/js/uploadresource.js"></script>

<div id="type" style="display: none">${type}</div>

<div class="container">
    <h1>填写广告信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createresourceform">
                <div class="form-group">
                    <label for="title">广告名字</label>
                    <input type="text" class="form-control" name="title" id="title" placeholder="请输入广告名字">
                </div>

                <div class="form-group">
                    <label for="imagetype">广告图片文件格式</label>
                    <select data-toggle="select" name="imagetype" id="imagetype"
                            class="form-control select select-success mrs mbm">
                        <option value="jpg">jpg</option>
                        <option value="png">png</option>
                    </select>
                </div>

                <button type="button" class="btn btn-default" id="start">创建广告</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>