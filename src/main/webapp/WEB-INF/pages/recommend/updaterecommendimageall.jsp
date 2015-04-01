<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<div class="container">
    <h6>推荐舞蹈视频</h6>

    <div class="row">
        <div class="col-md-12">
            <c:if test="${not empty videos}">

                <ul>
                    <c:forEach var="video" items="${videos}">
                        <a style="color: #FFF" href="/darfoobackend/rest/admin/recommend/updateimage/dancevideo/${video.id}">
                            <button type="button" id="${video.id}" class="btn btn-primary btn-lg btn-block">
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
