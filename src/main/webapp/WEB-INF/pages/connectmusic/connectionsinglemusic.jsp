<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script>
    var add_connectmusic = [];
    var del_connectmusic = [];

    $(function () {
        var type = $("#type").text();

        $(".addconnectmusic").click(function () {
            if (parseInt($(this).attr("picked")) == 0) {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 1);
                $(this).parents("li").css("background-color", "green");
                if ($.inArray(parseInt(eid), add_connectmusic) == -1) {
                    add_connectmusic.push(parseInt(eid));
                }
                console.log(add_connectmusic);
            } else {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 0);
                $(this).parents("li").css("background-color", "white");
                if ($.inArray(parseInt(eid), add_connectmusic) != -1) {
                    add_connectmusic = $.grep(add_connectmusic, function (value) {
                        return value != parseInt(eid);
                    });
                }
                console.log(add_connectmusic);
            }
        });

        $(".delconnectmusic").click(function () {
            if (parseInt($(this).attr("picked")) == 0) {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 1);
                $(this).parents("li").css("background-color", "red");
                if ($.inArray(parseInt(eid), del_connectmusic) == -1) {
                    del_connectmusic.push(parseInt(eid));
                }
                console.log(del_connectmusic);
            } else {
                var eid = $(this).attr("eid");
                $(this).attr("picked", 0);
                $(this).parents("li").css("background-color", "white");
                if ($.inArray(parseInt(eid), del_connectmusic) != -1) {
                    del_connectmusic = $.grep(del_connectmusic, function (value) {
                        return value != parseInt(eid);
                    });
                }
                console.log(del_connectmusic);
            }
        });

        $("#addconnectmusic").click(function () {
            if (add_connectmusic.length == 0) {
                alert("还没有选择要关联伴奏的舞蹈视频");
            } else {
                $.post("/darfoobackend/rest/admin/connectmusic/addconnects/" + type, {
                    'ids': add_connectmusic.join(',')
                }, function (data) {
                    if (data == 200) {
                        window.location.reload();
                    } else {
                        alert("操作失败");
                    }
                }, "json");
            }
        });

        $("#delconnectmusic").click(function () {
            if (del_connectmusic.length == 0) {
                alert("还没有选择要取消关联伴奏的舞蹈视频");
            } else {
                $.post("/darfoobackend/rest/admin/connectmusic/delconnects/" + type, {
                    'ids': del_connectmusic.join(',')
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
            <p><a id="addconnectmusic" class="btn btn-lg btn-success" href="#" role="button">选中要关联的舞蹈视频然后点这里</a></p>
            <c:if test="${not empty notconnectvideos}">
                <ul class="list-group">
                    <c:forEach var="video" items="${notconnectvideos}">
                        <li class="list-group-item" style="cursor:pointer;background:white;">
                            <div class="content addconnectmusic" picked="0" eid="${video.id}">${video.title}
                                (${video.author.name})
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>

        <div class="col-lg-6">
            <p><a id="delconnectmusic" class="btn btn-lg btn-success" href="#" role="button">选中要取消关联的舞蹈视频然后点这里</a>
            </p>
            <c:if test="${not empty connectvideos}">
                <ul class="list-group">
                    <c:forEach var="video" items="${connectvideos}">
                        <li class="list-group-item" style="cursor:pointer;background:white;">
                            <div class="content delconnectmusic" picked="0" eid="${video.id}">${video.title}
                                (${video.author.name})
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
