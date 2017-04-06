<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:bootstrapbase highlight="start" title="Administration: ${im.config.title}" hidebreadcrumbs="true" >
    <jsp:body>
        <div class="row ">
            <div class="col-lg-12">
                <dl class="dl-horizontal">
                    <dt>Title:</dt><dd>${im.config.title}</dd>
                    <dt>Mail Sender:</dt><dd>${im.config.mailFrom}</dd>
                    <dt>Email Title:</dt><dd>${im.config.mailSubject}</dd>
                    <dt>Sample Email:</dt><dd><pre>${im.testMail}</pre></dd>
                </dl>
            </div>
        </div>
        <form method="POST">
            <div class="row"><table class="table">
                    <thead>
                        <tr>
                            <th></th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Contacted</th>
                            <th>Project</th>
                            <th>Site</th>
                            <th>Status</th>
                            <c:forEach items="${im.config.additionalFields}" var="field"><th>${field.key}</th></c:forEach>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" varStatus="idx" items="${im.approvals}">
                            <tr>
                                <td><input type="checkbox" value="${idx.index}" name="code"></td>
                                <td>${item.name}</td>
                                <td>${item.email} </td>
                                <td><c:choose><c:when test="${item.emailSent == null}">Not Sent</c:when><c:otherwise>${item.emailSent}</c:otherwise></c:choose></td>
                                <td>${item.project}</td>
                                <td>${item.site}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${item.hasResponded}"><c:choose><c:when test="${item.hasConsented == true}">Approval Given</c:when><c:otherwise>Approval Rejected</c:otherwise></c:choose>
                                            - ${item.respondedAt}
                                        </c:when>
                                        <c:otherwise>Not Responded</c:otherwise>
                                    </c:choose>
                                </td>
                                <c:forEach items="${im.config.additionalFields}" var="field"><td>${item.additionalFields[field.key]}</td></c:forEach>

                            </tr>
                        </c:forEach>
                    </tbody>
                </table></div>
                        <div class="row"><button type="submit" class="btn btn-success">Send Emails</button><a href="${im.instanceKey}/results.csv">Download Results</a></div>
        </form>
    </jsp:body>

</t:bootstrapbase>
