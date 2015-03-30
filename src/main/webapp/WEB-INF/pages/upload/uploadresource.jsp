<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<div class="container">
    <h6>总共有${typenames.size()}个类别</h6>

    <div class="row">
        <div class="col-md-12">
            <c:if test="${not empty typenames}">
                <ul>
                    <c:forEach var="typename" items="${typenames}">
                        <a style="color: #FFF" href="/darfoobackend/rest/resources/new/${type}/${typename.key}">
                            <button type="button" class="btn btn-primary btn-lg btn-block">
                                    ${typename.value}
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