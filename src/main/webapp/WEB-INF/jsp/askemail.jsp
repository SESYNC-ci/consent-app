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
                <p>Please enter your email address to receive a link to the admin page</p>
                <form class="form-inline" method="POST">
                    
                    <div class="form-group">
                        <label>Email</label>
                        <input type="text" name="email" class="form-control">
                        
                    </div>
                      <button type="submit" class="btn btn-default">Request Link</button>
                </form>
            </div>
        </div>
    </jsp:body>

</t:bootstrapbase>
