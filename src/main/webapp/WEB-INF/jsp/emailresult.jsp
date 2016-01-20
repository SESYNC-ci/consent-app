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
                <p>Please check your email for a link to access the admin site.</p>
            </div>
        </div>
    </jsp:body>

</t:bootstrapbase>
