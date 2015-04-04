<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script>
    var change_to_normal = [];
    var change_to_star = [];

    $(function () {
        $(".changetonormal").click(function () {
            if (parseInt($(this).attr("picked")) == 0) {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 1);
                $(this).parents("li").css("background-color", "green");
                if ($.inArray(parseInt(eid), change_to_normal) == -1) {
                    change_to_normal.push(parseInt(eid));
                }
                console.log(change_to_normal);
            } else {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 0);
                $(this).parents("li").css("background-color", "white");
                if ($.inArray(parseInt(eid), change_to_normal) != -1) {
                    change_to_normal = $.grep(change_to_normal, function (value) {
                        return value != parseInt(eid);
                    });
                }
                console.log(change_to_normal);
            }
        });

        $(".changetostar").click(function () {
            if (parseInt($(this).attr("picked")) == 0) {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 1);
                $(this).parents("li").css("background-color", "red");
                if ($.inArray(parseInt(eid), change_to_star) == -1) {
                    change_to_star.push(parseInt(eid));
                }
                console.log(change_to_star);
            } else {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 0);
                $(this).parents("li").css("background-color", "white");
                if ($.inArray(parseInt(eid), change_to_star) != -1) {
                    change_to_star = $.grep(change_to_star, function (value) {
                        return value != parseInt(eid);
                    });
                }
                console.log(change_to_star);
            }
        });

        $("#changetonormal").click(function () {
            if (change_to_normal.length == 0) {
                alert("还没有选择要变为普通舞队的明星舞队");
            } else {
                $.post("/darfoobackend/rest/admin/dancegroup/changetype/NORMAL", {
                    'ids': change_to_normal.join(',')
                }, function (data) {
                    if (data == 200) {
                        window.location.reload();
                    } else {
                        alert("操作失败");
                    }
                }, "json");
            }
        });

        $("#changetostar").click(function () {
            if (change_to_star.length == 0) {
                alert("还没有选择要变为明星舞队的普通舞队");
            } else {
                $.post("/darfoobackend/rest/admin/dancegroup/changetype/STAR", {
                    'ids': change_to_star.join(',')
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

<div class="container">
    <div style="margin-top:33px;"></div>
    <div class="row marketing">
        <div class="col-lg-6">
            <p><a id="changetonormal" class="btn btn-lg btn-success" href="#" role="button">选中要变为普通舞队的明星舞队然后点这里</a></p>
            <c:if test="${not empty stardancegroups}">
                <ul class="list-group">
                    <c:forEach var="dancegroup" items="${stardancegroups}">
                        <li class="list-group-item" style="cursor:pointer;background:white;">
                            <div class="content changetonormal" picked="0" eid="${dancegroup.id}">
                                    ${dancegroup.name}
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>

        <div class="col-lg-6">
            <p><a id="changetostar" class="btn btn-lg btn-success" href="#" role="button">选中要变为明星舞队的普通舞队然后点这里</a>
            </p>
            <c:if test="${not empty normaldancegroups}">
                <ul class="list-group">
                    <c:forEach var="dancegroup" items="${normaldancegroups}">
                        <li class="list-group-item" style="cursor:pointer;background:white;">
                            <div class="content changetostar" picked="0" eid="${dancegroup.id}">
                                    ${dancegroup.name}
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
