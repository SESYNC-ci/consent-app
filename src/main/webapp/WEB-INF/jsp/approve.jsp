<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:bootstrapbase highlight="start" title="${im.config.title}" hidebreadcrumbs="true" >
    <jsp:body>
        <div class="row ">
            <div class="col-lg-12">
                <h1>${im.config.title}</h1>
            </div>
        </div>
        <div class="row ">
            <div class="col-lg-12">
                <p>${im.config.projectDescription}</p>
                <p>Please select the projects you wish to allow to be used in this study.</p>
            </div>
        </div>
        <form method="POST">
            <ul>
                <c:forEach var="item" items="${projects}">
                    <li><input type="checkbox" value="${item.project}" name="project"> - ${item.project}, <a href="${item.site}">${item.site}</a></li>
                </c:forEach>
            </ul>
            <button type="submit" class="btn btn-success">Submit</button></div>
    </form>
</jsp:body>

</t:bootstrapbase>
