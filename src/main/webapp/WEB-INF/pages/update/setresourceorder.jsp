<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script>
    $(function () {
        var type = $("#type").text();
        $("#update").click(function () {
            $.ajax({
                type: "POST",
                url: "/darfoobackend/rest/admin/" + type + "/updateorder",
                data: $("#updateresourceform").serialize(),
                success: function (data) {
                    if (data == "200") {
                        alert("提交资源信息成功");
                        window.location.reload();
                    }
                },
                error: function () {
                    alert("提交资源信息失败");
                }
            });
        });
    });
</script>

<div id="type" style="display: none">${type}</div>

<div class="container">
    <h1>${title}</h1>
    <h6>总共有${resources.size()}个资源</h6>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="updateresourceform">
                <c:if test="${not empty resources}">
                    <ul>
                        <c:forEach var="resource" items="${resources}">
                            <a style="color: #FFF" href="/darfoobackend/rest/admin/${type}/${resource.id}">
                                <button type="button" id="${resource.id}" class="btn btn-primary btn-lg btn-block">
                                        ${resource.title}
                                </button>
                            </a>

                            <div class="form-group">
                                <label for="order-${resource.id}">选择显示的顺序,数字越小在平板上就会显示在越前面</label>
                                    ${resource.order}
                                <select data-toggle="select" name="order-${resource.id}" id="order-${resource.id}"
                                        class="form-control select select-default mrs mbm">
                                    <c:forEach var="order" items="${orders}">
                                        <c:choose>
                                            <c:when test="${order == resource.order}">
                                                <option value="${order}" selected="selected">${order}</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${order}">${order}</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </select>
                            </div>
                            <br/>
                        </c:forEach>
                    </ul>
                </c:if>
                <button type="button" class="btn btn-default" id="update">更新资源显示顺序</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
