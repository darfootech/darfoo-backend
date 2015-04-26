<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script src="/darfoobackend/resources/js/uploadresource.js"></script>

<div id="type" style="display: none">${type}</div>

<div class="container">
    <h1>填写第三方软件信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createresourceform">
                <div class="form-group">
                    <label for="title">第三方应用名字</label>
                    <input type="text" class="form-control" name="title" id="title" placeholder="请输入第三方应用名字">
                </div>

                <button type="button" class="btn btn-default" id="start">创建第三方应用</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>