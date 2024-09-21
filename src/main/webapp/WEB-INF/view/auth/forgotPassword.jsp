<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <jsp:include page="/commonLink.jsp"/>
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
            <form:form action="/forgetPassword" class="form form--forget-password mb-5" method="post"
                       modelAttribute="form">
                <h1 class="heading">Vui lòng nhập email của bạn để lấy lại mật khẩu</h1>
                <div class="form__block">
                    <label for="email" class="form__label">Email</label>
                    <form:input path="email" id="email" name="email" type="email" class="form__input p-2"/>
                    <c:set value="${errors['email']}" var="emailError"/>
                    <p class="form__error">
                        <c:if test="${emailError != null}">${emailError}</c:if>
                    </p>
                    <c:if test="${sendMail != null}">
                        <p class="form__error">${sendMail}</p>
                    </c:if>
                </div>
                <button id="form__submit" type="submit" class="form__submit button button--hover">
                    Lấy lại mật khẩu
                </button>
            </form:form>
            <a href="/signIn" id="form__link--signIn" class="form__link hvr-float-shadow">Đăng nhập</a>
        </article>
        <input type="checkbox" id="modal__hide" class="modal__hide" hidden="hidden" checked>
        <div class="modal">
            <div class="modal__notify modal__content">
                <i class="model__checked-icon fa-regular fa-circle-check"></i>
                <p class="modal__text">Vui lòng kiểm tra email bạn đã đăng kí.</p>
                <a href="/signIn" class="button modal__button button--hover">Đăng nhập</a>
                <p class="modal__resend ">Nếu bạn chưa nhận được email xác nhận, hãy
                    <span>nhấn vào đây.</span>
                </p>
            </div>
            <label for="modal__hide" class="modal__blur"></label>
        </div>
    </div>
</main>
<!--JS validate-->
<script src="<c:url value="/js/validateForm.js"/>"></script>
<script>
    $(document).ready(() => {
        var validation = new Validation({
            formSelector: ".form",
            formBlockClass: "form__block",
            errorSelector: ".form__error",
            rules: [
                Validation.isRequired("#email"),
                Validation.isEmail("#email")
            ],
            submitSelector: "#form__submit",
        })
    })
</script>
</body>

</html>