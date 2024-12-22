<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="/WEB-INF/view/common/commonLink.jsp"/>
    <link rel="stylesheet" href="/assets/css/logIn.css"/>
    <script src="https://www.google.com/recaptcha/api.js?render=6LdSQu4pAAAAAJ0LbzMt-kVSfKqzVZYQtmPo3mHD"></script>
    <title>Đăng ký</title>
</head>

<body>
<main class="main">
    <div class="frame" style="min-height: 100vh">
        <article class="container pb-5">
                    <span class="text-center mb-3 d-flex justify-content-center hvr-bob">
                        <a href="/" class="logo"></a>
                    </span>
            <form:form method="post" class="form form--signUp" modelAttribute="user">
                <div class="row">
                    <div class="col">
                        <div class="form__block">
                            <label for="username" class="form__label">Tên đăng nhập</label>
                            <form:input path="username" id="username" name="username" type="text" class="form__input"/>
                            <p class="form__error">
                            </p>
                        </div>
                    </div>
                    <div class="col">
                        <div class="form__block">
                            <label for="email" class="form__label">Email</label>
                            <form:input path="email" id="email" name="email" type="email" class="form__input"/>
                            <p class="form__error">
                                <c:if test="${not empty errors['email'] != null}">${errors['email']}</c:if>
                            </p>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <div class="form__block mb-3">
                            <label for="fullName" class="form__label">Họ và tên</label>
                            <form:input path="fullName" id="fullName" name="fullName" type="text" class="form__input"/>
                            <p class="form__error">
                            </p>
                        </div>
                    </div>
                    <div class="col">
                        <div class="form__block mb-3">
                            <label for="gender" class="form__label">Giới tính</label>
                            <form:select path="gender" id="gender" class="d-block w-100 p-2 rounded">
                                <form:option name="gender" value="MALE">Nam</form:option>
                                <form:option name="gender" value="FEMALE">Nữ</form:option>
                                <form:option name="gender" value="OTHER">Khác</form:option>
                            </form:select>
                            <p class="form__error">
                            </p>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <div class="form__block mb-3">
                            <label for="phone" class="form__label">Số điện thoại</label>
                            <form:input path="phone" id="phone" name="phone" type="text" class="form__input"/>
                            <p class="form__error">
                            </p>
                        </div>
                    </div>
                    <div class="col">
                        <div class="form__block mb-3">
                            <label for="dob" class="form__label">Ngày sinh</label>
                            <form:input path="dob" id="dob" name="dob" type="date" class="form__input"/>
                            <p class="form__error">
                            </p>
                        </div>
                    </div>
                </div>

                <div class="form__block">
                    <label for="password" class="form__label">Mật khẩu</label>
                    <form:input path="password" id="password" name="password" type="password" class="form__input"/>
                    <p class="form__error">
                    </p>
                </div>

                <div class="form__block">
                    <label for="confirmPassword" class="form__label">Xác nhận lại mật khẩu</label>
                    <input id="confirmPassword" name="confirmPassword" type="password"
                           class="form__input"/>
                    <p class="form__error">
                    </p>
                </div>
                <input type="hidden" id="recaptchaToken" name="g-recaptcha-response">
                <div class="row">
                    <div class="col">
                        <button type="submit" id="form__submit" class="form__submit button button--hover">Đăng ký
                        </button>
                    </div>
                    <div class="col">
                        <a href="/signIn" id="form__link--signIn"
                           class="form__link hvr-float-shadow">
                            Đăng nhập
                        </a>
                    </div>
                </div>
            </form:form>
        </article>
    </div>
</main>
<script type="module" src="/js/signUp.js"></script>
</body>

</html>