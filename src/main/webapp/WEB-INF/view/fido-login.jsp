<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="https://www.thymeleaf.org">
<head>
    <title>Log In</title>
    <jsp:include page="/WEB-INF/view/common/commonLink.jsp"/>
    <script src="/js/base64/base64.js"></script>

</head>
<body>
<h1>Lets log in without a password</h1>
<form id="loginForm">
    <div>
        <input id="email" type="email" name="email" placeholder="email"/>
    </div>
    <button type="submit">
        Login
    </button>
</form>
<script type="module" src="/js/demo/fido-login.js"></script>
</body>
</html>

