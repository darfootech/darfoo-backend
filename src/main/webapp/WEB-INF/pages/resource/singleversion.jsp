<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>
<%@include file="../update/updatecota.jsp" %>

<script src="/darfoobackend/resources/js/modifyresource.js"></script>

<div class="container">
    <h1>查看与修改版本发布信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="updateresourceform">
                <div style="display: none">
                    <input type="text" name="id" value="${version.id}">
                </div>

                <div class="btn-toolbar">
                    <div class="btn-group btn-group-lg">
                        <c:choose>
                            <c:when test="${role == 'cleantha'}">
                                <button type="button" class="btn btn-default" id="kickout">删除版本发布</button>
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
