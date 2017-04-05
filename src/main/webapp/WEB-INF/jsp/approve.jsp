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

            <p>${im.config.projectDescription}</p>
            <p>Please select the projects you wish to allow to be used in this study.</p>
        </div>

        <div class="row ">
            <form method="POST">
                <div class="col-lg-6">
                    <ul>
                        <c:forEach var="item" varStatus="status" items="${projects}">
                            <li><input type="checkbox" value="${status.index}" name="project"> - ${item.project}  <a href="${item.site}">${item.site}</a>
                                <c:forEach var="field" items="${im.config.additionalFields}">
                                    <div class="form-group">
                                        <label for="${status.index}_${field.key}">${field.key}</label>
                                        <input type="text" class="form-control" id="${status.index}_${field.key}" name="${status.index}_${field.key}" placeholder="${field.value}">
                                    </div>
                                </c:forEach>
                            </li>
                        </c:forEach>
                    </ul>
                    <button type="submit" class="btn btn-success">Submit</button>
                </div>
            </form>
        </div>
    </jsp:body>

</t:bootstrapbase>
