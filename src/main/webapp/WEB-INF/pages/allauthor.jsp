<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="header.jsp"%>

<div class="container">
    <h6>总共有${allauthors.size()}个作者</h6>
    <div class="row">
        <div class="col-md-12">
            <c:if test="${not empty allauthors}">

                <ul>
                    <c:forEach var="author" items="${allauthors}">
                        <a style="color: #FFF" href="/darfoobackend/rest/admin/author/${author.id}">
                            <button type="button" id="${author.id}" class="btn btn-primary btn-lg btn-block">
                                ${author.name}
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
