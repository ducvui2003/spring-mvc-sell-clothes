<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="/WEB-INF/view/common/commonLink.jsp"/>
    <meta charset="UTF-8">
    <title>Create a new Account</title>

    <!-- CSRF header name and value to be used by froms on this page to talk to the server -->
</head>
<body>

<H1>Create a new account</H1>

<form id="registerForm">
    <div>
        <input id="fullName" type="text" name="fullName" placeholder="Full name"/>
    </div>
    <div>
        <input id="email" type="email" name="email" placeholder="email"/>
    </div>
    <button type="submit">
        Submit
    </button>
</form>
<script type="module" src="/js/demo/fido-register.js"></script>
</body>
</html>
