<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<div class="container">
    <h6>总共有${typenames.size()}个类别</h6>

    <div class="row">
        <div class="col-md-12">
            <c:if test="${not empty typenames}">
                <ul>
                    <c:forEach var="typename" items="${typenames}">
                        <c:choose>
                            <c:when test="${operation == 'upload'}">
                                <a style="color: #FFF" href="/darfoobackend/rest/resources/new/${type}/${typename.key}">
                                    <button type="button" class="btn btn-primary btn-lg btn-block">
                                            ${typename.value}
                                    </button>
                                </a>
                            </c:when>

                            <c:when test="${operation == 'manage'}">
                                <a style="color: #FFF"
                                   href="/darfoobackend/rest/admin/gallery/all/${type}/${typename.key}">
                                    <button type="button" class="btn btn-primary btn-lg btn-block">
                                            ${typename.value}
                                    </button>
                                </a>
                            </c:when>

                            <c:when test="${operation == 'changetype'}">
                                <a style="color: #FFF" href="/darfoobackend/rest/admin/${type}/${resource.id}">
                                    <button type="button" class="btn btn-primary btn-lg btn-block">
                                            ${typename.value}
                                    </button>
                                </a>
                            </c:when>

                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                        <br/>
                    </c:forEach>
                </ul>
            </c:if>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>