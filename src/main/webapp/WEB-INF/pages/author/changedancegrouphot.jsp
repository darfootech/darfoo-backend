<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script>
    var change_to_hot = [];
    var change_to_nothot = [];

    $(function () {
        $(".changetohot").click(function () {
            if (parseInt($(this).attr("picked")) == 0) {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 1);
                $(this).parents("li").css("background-color", "green");
                if ($.inArray(parseInt(eid), change_to_hot) == -1) {
                    change_to_hot.push(parseInt(eid));
                }
                console.log(change_to_hot);
            } else {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 0);
                $(this).parents("li").css("background-color", "white");
                if ($.inArray(parseInt(eid), change_to_hot) != -1) {
                    change_to_hot = $.grep(change_to_hot, function (value) {
                        return value != parseInt(eid);
                    });
                }
                console.log(change_to_hot);
            }
        });

        $(".changetonothot").click(function () {
            if (parseInt($(this).attr("picked")) == 0) {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 1);
                $(this).parents("li").css("background-color", "red");
                if ($.inArray(parseInt(eid), change_to_nothot) == -1) {
                    change_to_nothot.push(parseInt(eid));
                }
                console.log(change_to_nothot);
            } else {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 0);
                $(this).parents("li").css("background-color", "white");
                if ($.inArray(parseInt(eid), change_to_nothot) != -1) {
                    change_to_nothot = $.grep(change_to_nothot, function (value) {
                        return value != parseInt(eid);
                    });
                }
                console.log(change_to_nothot);
            }
        });

        $("#changetohot").click(function () {
            if (change_to_hot.length == 0) {
                alert("还没有选择要变为非热门舞队的热门舞队");
            } else {
                $.post("/darfoobackend/rest/admin/dancegroup/changehot/ISHOT", {
                    'ids': change_to_hot.join(',')
                }, function (data) {
                    if (data == 200) {
                        window.location.reload();
                    } else {
                        alert("操作失败");
                    }
                }, "json");
            }
        });

        $("#changetonothot").click(function () {
            if (change_to_nothot.length == 0) {
                alert("还没有选择要变为热门舞队的非热门舞队");
            } else {
                $.post("/darfoobackend/rest/admin/dancegroup/changehot/NOTHOT", {
                    'ids': change_to_nothot.join(',')
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
            <p><a id="changetohot" class="btn btn-lg btn-success" href="#" role="button">选中要变为热门舞队的非热门舞队然后点这里</a></p>
            <c:if test="${not empty nothotdancegroups}">
                <ul class="list-group">
                    <c:forEach var="dancegroup" items="${nothotdancegroups}">
                        <li class="list-group-item" style="cursor:pointer;background:white;">
                            <div class="content changetohot" picked="0" eid="${dancegroup.id}">
                                ${dancegroup.name}
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>

        <div class="col-lg-6">
            <p><a id="changetonothot" class="btn btn-lg btn-success" href="#" role="button">选中要变为非热门舞队的热门舞队然后点这里</a>
            </p>
            <c:if test="${not empty hotdancegroups}">
                <ul class="list-group">
                    <c:forEach var="dancegroup" items="${hotdancegroups}">
                        <li class="list-group-item" style="cursor:pointer;background:white;">
                            <div class="content changetonothot" picked="0" eid="${dancegroup.id}">
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
