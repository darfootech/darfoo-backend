<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script src="/darfoobackend/resources/js/uploadresource.js"></script>

<div id="type" style="display: none">${type}</div>

<div class="container">
    <h1>填写明星舞队信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createresourceform">
                <div class="form-group">
                    <label for="name">明星舞队名字</label>
                    <input type="text" class="form-control" name="name" id="name" placeholder="请输入明星舞队名字">
                </div>
                <div class="form-group">
                    <label for="description">明星舞队简介</label>
                    <input type="text" class="form-control" name="description" id="description" placeholder="请输入明星舞队简介">
                </div>

                <div style="display: none">
                    <input name="innertype" type="text" value="${innertype}"/>
                </div>

                <button type="button" class="btn btn-default" id="start">创建明星舞队</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>