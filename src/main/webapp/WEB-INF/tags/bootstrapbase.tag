<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@attribute name="highlight" required="true" %>
<%@attribute name="title" required="true" %>
<%@attribute name="scripting" required="false" %>
<%@attribute name="fluid" required="false" %>
<%@attribute name="footer" required="false" %>
<%@attribute name="hidebreadcrumbs" required="false" type="java.lang.Boolean" %>
<%@attribute name="breadcrumbs" required="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:if test="${empty clean}" >
    <c:set var="clean" value="false" />
</c:if>
<c:choose>
    <c:when test="${fluid}" ><c:set var="container" value="container-fluid" /></c:when>
    <c:otherwise><c:set var="container" value="container" /></c:otherwise>
</c:choose>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">
        <!-- <link rel="shortcut icon" href="../../assets/ico/favicon.png">-->

        <title>${title}</title>
        <link href="${pageContext.request.contextPath}/static/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/static/css/app.css" rel="stylesheet">

    </head>

    <body data-spy="scroll" data-target=".se-sidebar-div">
        <div id="wrap">

            <c:if test="${!clean}">
                <div class="navbar navbar-inverse navbar-fixed-top" id="top-navbar">
                    <div class="container">
                        <div class="navbar-header">
                            <a class="pull-left" href="http://www.sesync.org">
                                <img style="display: inline-block;max-height: 50px;"src="http://www.sesync.org/sites/all/themes/sesync/logo.png" alt="SESYNC">
                            </a>
                            <p style="padding-left: 50px;" class="navbar-brand">${title}</p>
                        </div>
                       
                    </div>
                </div>
            </c:if>

            <div class="${container} app-main-content">
                <c:if test="${!hidebreadcrumbs}">
                    <ol class="breadcrumb">
                        <li><a href="${pageContext.request.contextPath}/platform">Home</a></li>
                            ${breadcrumbs}
                    </ol>
                </c:if>
                <jsp:doBody/>

            </div><!-- /.container -->
        </div>
                ${footer}
        

        <!-- Bootstrap core JavaScript
        ================================================== -->
        <!-- Placed at the end of the document so the pages load faster -->
        <!--<script src="https://code.jquery.com/jquery-1.10.0.min.js"></script>-->
        <script src="https://code.jquery.com/jquery-1.10.0.js"></script>

        <script src="${pageContext.request.contextPath}/static/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/respond.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/knockout/3.3.0/knockout-min.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/knockout.mapping-latest.js"></script>
        ${scripting}
    </body>
</html>
