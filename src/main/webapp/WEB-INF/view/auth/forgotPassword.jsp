<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <jsp:include page="/WEB-INF/view/common/commonLink.jsp"/>
    <link rel="stylesheet" href="<c:url value="/assets/css/logIn.css" />">
    <link rel="stylesheet" href="<c:url value="/assets/css/forgetPassword.css" />">
    <title>Quên mật khẩu</title>
</head>

<body>
<main class="main">
    <div class="frame">
        <article>
            <span class="text-center mb-3 d-flex justify-content-center hvr-bob">
                <a href="/" class="logo"></a>
            </span>
            <form class="form form--forget-password mb-5" method="post">
                <h1 class="heading">Vui lòng nhập email của bạn để lấy lại mật khẩu</h1>
                <div class="form__block">
                    <label for="email" class="form__label">Email</label>
                    <input id="email" name="email" type="email" class="form__input p-2"/>
                    <p class="form__error">
                    </p>
                </div>
                <button id="form__submit" type="submit" class="form__submit button button--hover">
                    Lấy lại mật khẩu
                </button>
            </form>
            <a href="/signIn" id="form__link--signIn" class="form__link hvr-float-shadow">Đăng nhập</a>
        </article>
    </div>
</main>
<!--JS validate-->
<script type="module" src="/js/forgetPassword.js"></script>
</body>

</html>