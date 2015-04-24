<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script>
    var add_recommendevents = [];
    var delete_recommendevents = [];

    $(function () {
        var type = $("#type").text();

        $(".addrecommendevent").click(function () {
            if (parseInt($(this).attr("picked")) == 0) {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 1);
                $(this).parents("li").css("background-color", "green");
                if ($.inArray(parseInt(eid), add_recommendevents) == -1) {
                    add_recommendevents.push(parseInt(eid));
                }
                console.log(add_recommendevents);
            } else {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 0);
                $(this).parents("li").css("background-color", "white");
                if ($.inArray(parseInt(eid), add_recommendevents) != -1) {
                    add_recommendevents = $.grep(add_recommendevents, function (value) {
                        return value != parseInt(eid);
                    });
                }
                console.log(add_recommendevents);
            }
        });

        $(".deleterecommendevent").click(function () {
            if (parseInt($(this).attr("picked")) == 0) {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 1);
                $(this).parents("li").css("background-color", "red");
                if ($.inArray(parseInt(eid), delete_recommendevents) == -1) {
                    delete_recommendevents.push(parseInt(eid));
                }
                console.log(delete_recommendevents);
            } else {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 0);
                $(this).parents("li").css("background-color", "white");
                if ($.inArray(parseInt(eid), delete_recommendevents) != -1) {
                    delete_recommendevents = $.grep(delete_recommendevents, function (value) {
                        return value != parseInt(eid);
                    });
                }
                console.log(delete_recommendevents);
            }
        });

        $("#addrecommendevent").click(function () {
            if (add_recommendevents.length == 0) {
                alert("还没有选择要推荐的舞蹈视频");
            } else {
                $.post("/darfoobackend/rest/admin/recommend/add/" + type, {
                    'ids': add_recommendevents.join(',')
                }, function (data) {
                    if (data == 200) {
                        window.location.reload();
                    } else {
                        alert("操作失败");
                    }
                }, "json");
            }
        });

        $("#deleterecommendevent").click(function () {
            if (delete_recommendevents.length == 0) {
                alert("还没有选择要取消推荐的舞蹈视频");
            } else {
                $.post("/darfoobackend/rest/admin/recommend/del/" + type, {
                    'ids': delete_recommendevents.join(',')
                }, function (data) {
                    if (data == 200) {
                        window.location.reload();
                    } else {
                        alert("操作失败");
                    }
                }, "json");
            }
        });
    });
</script>

<div id="type" style="display: none">${type}</div>

<div class="container">
    <div style="margin-top:33px;"></div>
    <div class="row marketing">
        <div class="col-lg-6">
            <p><a id="addrecommendevent" class="btn btn-lg btn-success" href="#" role="button">选中要推荐的舞蹈视频然后点这里</a></p>
            <c:if test="${not empty unrecommendresources}">
                <ul class="list-group">
                    <c:forEach var="resource" items="${unrecommendresources}">
                        <li class="list-group-item" style="cursor:pointer;background:white;">
                            <div class="content addrecommendevent" picked="0" eid="${resource.id}">${resource.title}
                                (${resource.author.title})
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>

        <div class="col-lg-6">
            <p><a id="deleterecommendevent" class="btn btn-lg btn-success" href="#" role="button">选中要取消推荐的舞蹈视频然后点这里</a>
            </p>
            <c:if test="${not empty recommendresources}">
                <ul class="list-group">
                    <c:forEach var="resource" items="${recommendresources}">
                        <li class="list-group-item" style="cursor:pointer;background:white;">
                            <div class="content deleterecommendevent" picked="0" eid="${resource.id}">${resource.title}
                                (${resource.author.title})
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
