<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script>
    $(function () {
        $("#update").click(function () {
            $.ajax({
                type: "POST",
                url: "/darfoobackend/rest/admin/operavideo/updateorder",
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

<div class="container">
    <h1>设置越剧连续剧关联越剧视频的显示顺序</h1>
    <h6>总共有${resources.size()}个越剧视频</h6>

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
                                <label for="order-${resource.id}">选择越剧视频在越剧连续剧中的顺序</label>
                                <select data-toggle="select" name="order-${resource.id}" id="order-${resource.id}"
                                        class="form-control select select-success mrs mbm">
                                    <c:forEach var="order" items="${orders}">
                                        <option value="${order}">${order}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <br/>
                        </c:forEach>
                    </ul>
                </c:if>
                <button type="button" class="btn btn-default" id="update">更新越剧连续剧中视频顺序</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
