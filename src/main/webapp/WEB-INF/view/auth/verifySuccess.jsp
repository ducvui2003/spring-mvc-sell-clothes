<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="/WEB-INF/view/common/commonLink.jsp.jsp"/>
    <link rel="stylesheet" href="/assets/css/verify.css">
    <title>Xác thực hành công</title>
</head>

<body>
<main>
    <div class="verify">
        <i class="verify__icon fa-solid fa-check"></i>
    </div>
    <h1>Xác thực hành công</h1>
    <p>
        Chúc mừng tài khoản <b>${requestScope.username}</b> của bạn đã xác thực hành công! </p>
    <a href="/signIn" class="button button--hover">Tiến hành đăng nhập</a>
</main>
</body>

</html>