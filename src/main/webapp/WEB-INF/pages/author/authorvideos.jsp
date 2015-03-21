<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<div class="container">
    <h6>总共有${videos.size()}个视频</h6>

    <div class="row">
        <div class="col-md-12">
            <c:if test="${not empty videos}">
                <ul>
                    <c:forEach var="video" items="${videos}">
                        <a style="color: #FFF" href="#">
                            <button type="button" class="btn btn-primary btn-lg btn-block">
                                    ${video.title}
                            </button>
                        </a>
                        <br/>
                    </c:forEach>
                </ul>
            </c:if>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
