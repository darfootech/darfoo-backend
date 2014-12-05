<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="header.jsp"%>

<div class="container">
    <h6>总共有${allteams.size()}个舞队</h6>
    <div class="row">
        <div class="col-md-12">
            <c:if test="${not empty allteams}">

                <ul>
                    <c:forEach var="team" items="${allteams}">
                        <a style="color: #FFF" href="/darfoobackend/rest/admin/team/${team.id}">
                            <button type="button" id="${team.id}" class="btn btn-primary btn-lg btn-block">
                                ${team.name}
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
