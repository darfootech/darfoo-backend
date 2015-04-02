<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script>
    var change_to_positive = [];
    var change_to_negative = [];

    $(function () {
        var type = $("#type").text();
        var field = $("#field").text();
        var negativeValue = "NOT" + field.toUpperCase();
        var positiveValue = "IS" + field.toUpperCase();

        $(".toggletopositive").click(function () {
            if (parseInt($(this).attr("picked")) == 0) {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 1);
                $(this).parents("li").css("background-color", "green");
                if ($.inArray(parseInt(eid), change_to_positive) == -1) {
                    change_to_positive.push(parseInt(eid));
                }
                console.log(change_to_positive);
            } else {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 0);
                $(this).parents("li").css("background-color", "white");
                if ($.inArray(parseInt(eid), change_to_positive) != -1) {
                    change_to_positive = $.grep(change_to_positive, function (value) {
                        return value != parseInt(eid);
                    });
                }
                console.log(change_to_positive);
            }
        });

        $(".toggletonegative").click(function () {
            if (parseInt($(this).attr("picked")) == 0) {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 1);
                $(this).parents("li").css("background-color", "red");
                if ($.inArray(parseInt(eid), change_to_negative) == -1) {
                    change_to_negative.push(parseInt(eid));
                }
                console.log(change_to_negative);
            } else {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 0);
                $(this).parents("li").css("background-color", "white");
                if ($.inArray(parseInt(eid), change_to_negative) != -1) {
                    change_to_negative = $.grep(change_to_negative, function (value) {
                        return value != parseInt(eid);
                    });
                }
                console.log(change_to_negative);
            }
        });

        $("#toggletopositive").click(function () {
            if (change_to_positive.length == 0) {
                alert("还没有选择要变为非热门舞队的热门舞队");
            } else {
                $.post("/darfoobackend/rest/admin/" + type + "/change/" + field + "/" + positiveValue, {
                    'ids': change_to_positive.join(',')
                }, function (data) {
                    if (data == 200) {
                        window.location.reload();
                    } else {
                        alert("操作失败");
                    }
                }, "json");
            }
        });

        $("#toggletonegative").click(function () {
            if (change_to_negative.length == 0) {
                alert("还没有选择要变为热门舞队的非热门舞队");
            } else {
                $.post("/darfoobackend/rest/admin/" + type + "/change/" + field + "/" + negativeValue, {
                    'ids': change_to_negative.join(',')
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
<div id="field" style="display: none">${field}</div>

<div class="container">
    <div style="margin-top:33px;"></div>
    <div class="row marketing">
        <div class="col-lg-6">
            <p><a id="toggletopositive" class="btn btn-lg btn-success" href="#" role="button">选中要变为热门舞队的非热门舞队然后点这里</a></p>
            <c:if test="${not empty negativeresources}">
                <ul class="list-group">
                    <c:forEach var="resource" items="${negativeresources}">
                        <li class="list-group-item" style="cursor:pointer;background:white;">
                            <div class="content toggletopositive" picked="0" eid="${resource.id}">
                                <c:choose>
                                    <c:when test="${type == 'dancegroup'}">
                                        ${resource.name}
                                    </c:when>

                                    <c:otherwise>
                                        ${resource.title}
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>

        <div class="col-lg-6">
            <p><a id="toggletonegative" class="btn btn-lg btn-success" href="#" role="button">选中要变为非热门舞队的热门舞队然后点这里</a>
            </p>
            <c:if test="${not empty positiveresources}">
                <ul class="list-group">
                    <c:forEach var="resource" items="${positiveresources}">
                        <li class="list-group-item" style="cursor:pointer;background:white;">
                            <div class="content toggletonegative" picked="0" eid="${resource.id}">
                                <c:choose>
                                    <c:when test="${type == 'dancegroup'}">
                                        ${resource.name}
                                    </c:when>

                                    <c:otherwise>
                                        ${resource.title}
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
