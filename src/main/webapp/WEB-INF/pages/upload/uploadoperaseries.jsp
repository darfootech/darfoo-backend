<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script src="/darfoobackend/resources/js/uploadresource.js"></script>

<div id="type" style="display: none">${type}</div>

<div class="container">
    <h1>填写越剧连续剧信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createresourceform">
                <div class="form-group">
                    <label for="title">越剧连续剧名字</label>
                    <input type="text" class="form-control" name="title" id="title" placeholder="请输入越剧连续剧名字">
                </div>

                <div class="form-group">
                    <label for="imagetype">舞队图片文件格式</label>
                    <select data-toggle="select" name="imagetype" id="imagetype"
                            class="form-control select select-success mrs mbm">
                        <option value="jpg">jpg</option>
                        <option value="png">png</option>
                    </select>
                </div>

                <button type="button" class="btn btn-default" id="start">创建越剧连续剧</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>