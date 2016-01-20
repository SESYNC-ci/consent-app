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
                <p>${im.config.submissionComplete}</p>
            </div>
        </div>
    </jsp:body>

</t:bootstrapbase>
