<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script src="/darfoobackend/resources/js/uploadresource.js"></script>

<div id="type" style="display: none">${type}</div>

<div class="container">
    <h1>填写广告信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createresourceform">
                <button type="button" class="btn btn-default" id="start">创建广告</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>