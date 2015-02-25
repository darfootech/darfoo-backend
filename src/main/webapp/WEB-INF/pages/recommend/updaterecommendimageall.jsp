<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<div class="container">
    <h6>推荐舞蹈视频</h6>

    <div class="row">
        <div class="col-md-12">
            <c:if test="${not empty videos}">

                <ul>
                    <c:forEach var="video" items="${videos}">
                        <a style="color: #FFF" href="/darfoobackend/rest/admin/recommend/updateimage/video/${video.id}">
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

    <h6>推荐舞蹈教程</h6>

    <div class="row">
        <div class="col-md-12">
            <c:if test="${not empty tutorials}">

                <ul>
                    <c:forEach var="tutorial" items="${tutorials}">
                        <a style="color: #FFF"
                           href="/darfoobackend/rest/admin/recommend/updateimage/tutorial/${tutorial.id}">
                            <button type="button" id="${tutorial.id}" class="btn btn-primary btn-lg btn-block">
                                    ${tutorial.title}
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