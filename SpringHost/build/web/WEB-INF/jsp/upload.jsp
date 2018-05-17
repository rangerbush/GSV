<%-- 
    Document   : upload
    Created on : 11/04/2018, 9:47:28 PM
    Author     : New User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reset your password.</title>
    </head>
    <body>
        <h1>Please paste the secret key in your email in the box below. </h1>
        <p> When you finished the previous step, click submit button to apply the change.</p>
        <br/>
        <form action="${pageContext.request.contextPath}/account/auth" method="POST">
        <input type="text" name="token">
        <br />
        <input type="submit" value="SUBMIT" />
        </form>
    </body>
</html>
