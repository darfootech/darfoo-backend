<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script>
    var change_to_positive = [];
    var change_to_negative = [];

    $(function () {
        $(".toggletopositive").click(function () {
            if (parseInt($(this).attr("picked")) == 0) {
                var keyword = $(this).attr("keyword");
                $(this).attr("picked", 1);
                $(this).parents("li").css("background-color", "green");
                if ($.inArray(keyword, change_to_positive) == -1) {
                    change_to_positive.push(keyword);
                }
                console.log(change_to_positive);
            } else {
                var keyword = $(this).attr("keyword");
                $(this).attr("picked", 0);
                $(this).parents("li").css("background-color", "white");
                if ($.inArray(keyword, change_to_positive) != -1) {
                    change_to_positive = $.grep(change_to_positive, function (value) {
                        return value != keyword;
                    });
                }
                console.log(change_to_positive);
            }
        });

        $(".toggletonegative").click(function () {
            if (parseInt($(this).attr("picked")) == 0) {
                var keyword = $(this).attr("keyword");
                $(this).attr("picked", 1);
                $(this).parents("li").css("background-color", "red");
                if ($.inArray(keyword, change_to_negative) == -1) {
                    change_to_negative.push(keyword);
                }
                console.log(change_to_negative);
            } else {
                var keyword = $(this).attr("keyword");
                $(this).attr("picked", 0);
                $(this).parents("li").css("background-color", "white");
                if ($.inArray(keyword, change_to_negative) != -1) {
                    change_to_negative = $.grep(change_to_negative, function (value) {
                        return value != keyword;
                    });
                }
                console.log(change_to_negative);
            }
        });

        $("#toggletopositive").click(function () {
            if (change_to_positive.length == 0) {
                alert("还没有选择要变为热门搜索关键词的非热门搜索关键词");
            } else {
                $.post("/darfoobackend/rest/statistics/admin/recommend/hotsearch/insert", {
                    'keywords': change_to_positive.join(',')
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
                alert("还没有选择要变为非热门搜索关键词的热门搜索关键词");
            } else {
                $.post("/darfoobackend/rest/statistics/admin/recommend/hotsearch/remove", {
                    'keywords': change_to_negative.join(',')
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
            <p><a id="toggletopositive" class="btn btn-lg btn-success" href="#" role="button">选中要变为热门搜索关键词</a></p>
            <c:if test="${not empty negativeresources}">
                <ul class="list-group">
                    <c:forEach var="resource" items="${negativeresources}">
                        <li class="list-group-item" style="cursor:pointer;background:white;">
                            <div class="content toggletopositive" picked="0" keyword="${resource}">
                                    ${resource}
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>

        <div class="col-lg-6">
            <p><a id="toggletonegative" class="btn btn-lg btn-success" href="#" role="button">选中要变为非热门搜索关键词</a>
            </p>
            <c:if test="${not empty positiveresources}">
                <ul class="list-group">
                    <c:forEach var="resource" items="${positiveresources}">
                        <li class="list-group-item" style="cursor:pointer;background:white;">
                            <div class="content toggletonegative" picked="0" keyword="${resource}">
                                    ${resource}
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
