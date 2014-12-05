<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="header.jsp"%>

<div class="container">
    <h6>总共有${allmusics.size()}个舞蹈伴奏</h6>
    <div class="row">
        <div class="col-md-12">
            <c:if test="${not empty allmusics}">

                <ul>
                    <c:forEach var="music" items="${allmusics}">
                        <a style="color: #FFF" href="/darfoobackend/rest/admin/music/${music.id}">
                            <button type="button" id="${music.id}" class="btn btn-primary btn-lg btn-block">
                                ${music.title}
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
