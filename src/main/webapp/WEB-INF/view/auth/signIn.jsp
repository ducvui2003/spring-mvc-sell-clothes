<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <jsp:include page="/WEB-INF/view/common/commonLink.jsp"/>
    <link rel="stylesheet" href="/assets/css/logIn.css"/>
    <script src="https://www.google.com/recaptcha/api.js?render=6LdSQu4pAAAAAJ0LbzMt-kVSfKqzVZYQtmPo3mHD"></script>
    <title>Đăng nhập</title>
</head>

<body>
<main class="main">
    <div class="frame">
        <div class="frame__media"></div>
        <article>
            <span class="text-center mb-3 d-flex justify-content-center hvr-bob">
                <a aria-hidden="true" href="/home" class="logo"></a>
            </span>
            <form:form action="/signIn" class="form form--signUp" method="post" modelAttribute="user">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <div class="form__block">
                    <label for="username" class="form__label">Tên đăng nhập</label>
                    <form:input path="username" id="username" name="username" type="text" class="form__input p-2"/>
                    <p class="form__error">
                        <c:if test="${not empty errors['username']}">${errors['username']}</c:if>
                    </p>
                </div>
                <div class="form__block">
                    <label for="password" class="form__label">Mật khẩu</label>
                    <form:input path="password" id="password" name="password" type="password" class="form__input p-2"/>
                    <p class="form__error">
                        <c:if test="${not empty errors['password'] != null}">${errors['password']}</c:if>
                        <c:if test="${not empty errors['signInFailed'] != null}">${errors['signInFailed']}</c:if>
                    </p>
                </div>
                <div class="form__block hvr-forward">
                    <a href="/forgetPassword " id="form__forget-password"
                       class="form__link">
                        Quên mật khẩu
                    </a>
                </div>
                <button id="form__submit" type="submit" class="form__submit button button--hover">Đăng nhập
                </button>
                <div class="d-flex justify-content-around mt-4">
                    <a class="btn btn-primary" data-btn-style="google" href="/oauth2/google/login">Đăng nhập với
                        Google</a>
                    <a class="btn btn-primary" data-btn-style="facebook" href="/oauth2/facebook/login">Đăng nhập với
                        Facebook</a>
                    <span class="btn btn-primary" id="btn-passkey" data-btn-style="passkey">Đăng nhập với
                        Passkey</span>
                </div>
            </form:form>
            <a href="<c:url value="/signUp"/>" id="form__link--signUp" class="form__link
                        hvr-float-shadow p-2">Đăng ký
            </a>

        </article>
    </div>
</main>
<!--JS validate-->
<script src="<c:url value="/js/validateForm.js"/>"></script>
<script type="module" src="<c:url value="/js/signIn.js"/>"></script>

</body>
</html>