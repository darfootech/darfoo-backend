<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="header.jsp" %>

<div class="container">
    <h6>总共有${alltutorials.size()}个舞蹈教学</h6>

    <div class="row">
        <div class="col-md-12">
            <c:if test="${not empty alltutorials}">

                <ul>
                    <c:forEach var="item" items="${alltutorials}">
                        <a style="color: #FFF" href="/darfoobackend/rest/admin/tutorial/${item.id}">
                            <button type="button" id="${item.id}" class="btn btn-primary btn-lg btn-block">
                                    ${item.title}
                            </button>
                        </a>
                        <br/>
                    </c:forEach>
                </ul>

            </c:if>
        </div>
    </div>
</div>

<%@include file="footer.jsp" %>
