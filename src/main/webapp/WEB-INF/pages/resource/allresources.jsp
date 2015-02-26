<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<div class="container">
    <h6>总共有${resources.size()}个资源</h6>
    <div class="row">
        <div class="col-md-12">
            <c:if test="${not empty resources}">
                <ul>
                    <c:forEach var="resource" items="${resources}">
                        <a style="color: #FFF" href="/darfoobackend/rest/admin/${type}/${resource.id}">
                            <button type="button" id="${resource.id}" class="btn btn-primary btn-lg btn-block">
                                <c:choose>
                                    <c:when test="${type == 'author'}">
                                        ${resource.name}
                                    </c:when>

                                    <c:otherwise>
                                        ${resource.title}
                                    </c:otherwise>
                                </c:choose>
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
