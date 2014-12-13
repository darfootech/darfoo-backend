<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="header.jsp"%>

<div class="container">
    <div style="margin-top:33px;"></div>
    <div class="row marketing">
        <div class="col-lg-6">
            <p><a id="addrecommendevent" class="btn btn-lg btn-success" href="#" role="button">选中要推荐的活动然后点这里</a></p>
            <c:if test="${not empty videos}">
                <ul class="list-group">
                    <c:forEach var="video" items="${videos}">
                        <li class="list-group-item" style="cursor:pointer;background:white;">
                            <div class="content addrecommendevent" picked="0" eid="${video.id}">${video.title} + "-" + ${video.author.name}</div>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>

        <div class="col-lg-6">
            <p><a id="deleterecommendevent" class="btn btn-lg btn-success" href="#" role="button">选中要取消推荐的活动然后点这里</a></p>
            <c:if test="${not empty videos}">
                <ul class="list-group">
                    <c:forEach var="video" items="${videos}">
                        <li class="list-group-item" style="cursor:pointer;background:white;">
                            <div class="content deleterecommendevent" picked="0" eid="${video.id}">${video.title} + "-" + ${video.author.name}</div>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>
    </div>
</div>

<%@include file="footer.jsp"%>
