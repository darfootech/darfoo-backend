<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="header.jsp"%>

<div class="container">
    <h6>总共有${allvideos.size()}个未审核视频</h6>
    <div class="row">
        <div class="col-md-12">
            <c:if test="${not empty allvideos}">

                <ul>
                    <c:forEach var="video" items="${allvideos}">
                        <a style="color: #FFF" href="/darfoobackend/rest/admin/verifyvideo/${video.id}">
                            <button type="button" id="${video.id}" class="btn btn-primary btn-lg btn-block">
                                ${video.videotitle}
                            </button>
                        </a>
                        <br/>
                    </c:forEach>
                </ul>

            </c:if>
        </div>
    </div>
</div>

<%@include file="footer.jsp"%>
