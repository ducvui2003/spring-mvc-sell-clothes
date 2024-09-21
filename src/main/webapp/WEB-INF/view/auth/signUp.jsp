<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="/commonLink"/>
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
            <form:form action="/signUp" method="post" class="form form--signUp" modelAttribute="user">
                <div class="row">
                    <div class="col">
                        <div class="form__block">
                            <label for="username" class="form__label">Tên đăng nhập</label>
                            <form:input path="username" id="username" name="username" type="text" class="form__input"/>
                            <p class="form__error">
                                <c:if test="${not empty errors['username']!= null}">${errors['username']}</c:if>
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
                                <c:if test="${not empty errors['fullName'] != null}">${errors['fullName']}</c:if>
                            </p>
                        </div>
                    </div>
                    <div class="col">
                        <div class="form__block mb-3">
                            <label for="gender" class="form__label">Giới tính</label>
                            <form:select path="gender" id="gender" class="d-block w-100 p-2 rounded">
                                <form:option name="gender" value="1">Nam</form:option>
                                <form:option name="gender" value="2">Nữ</form:option>
                                <form:option name="gender" value="3">Khác</form:option>
                            </form:select>
                            <p class="form__error">
                                <c:if test="${not empty errors['gender'] != null}">${errors['gender']}</c:if>
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
                                <c:if test="${not empty errors['phone'] != null}">${errors['phone']}</c:if>
                            </p>
                        </div>
                    </div>
                    <div class="col">
                        <div class="form__block mb-3">
                            <label for="birthday" class="form__label">Ngày sinh</label>
                            <form:input path="dob" id="birthday" name="dob" type="date" class="form__input"/>
                            <p class="form__error">
                                <c:if test="${not empty errors['dob'] != null}">${errors['dob']}</c:if>
                            </p>
                        </div>
                    </div>
                </div>

                <div class="form__block">
                    <label for="password" class="form__label">Mật khẩu</label>
                    <form:input path="password" id="password" name="password" type="password" class="form__input"/>
                    <p class="form__error">
                        <c:if test="${not empty errors['password'] != null}">${errors['password']}</c:if>
                    </p>
                </div>
                <div class="form__password-error rounded">
                    <h3>Mật khẩu cần phải có:</h3>
                    <div class="error__list">
                        <p id="minLength" class="error__item ">
                            <i class="error__icon fa-solid fa-check"></i>
                            <span>Có ít nhất 8 kí tự.</span>
                        </p>
                        <p id="atLeast1Digit" class="error__item">
                            <i class="error__icon fa-solid fa-check"></i>
                            <span>Có ít nhất 1 chữ số như 1, 2, 3,...</span>
                        </p>
                        <p id="atLeast1WordUpper" class="error__item">
                            <i class="error__icon fa-solid fa-check"></i>
                            <span>Có ít nhất 1 chữ hoa như: A, B, C,...</span>
                        </p>
                        <p id="atLeast1WordLower" class="error__item">
                            <i class="error__icon fa-solid fa-check"></i>
                            <span>Có ít nhất 1 chữ thường như a, b, c,...</span>
                        </p>
                        <p id="noSpace" class="error__item">
                            <i class="error__icon fa-solid fa-check"></i>
                            <span>Mật khẩu không được có khoảng trắng.</span>
                        </p>
                        <p id="atLeast1SpecialCharacter" class="error__item">
                            <i class="error__icon fa-solid fa-check"></i> <span>Có ít nhất 1 kí tự đặc biệt như "@", ".", ":",....</span>
                        </p>
                    </div>
                </div>
                <div class="form__block">
                    <label for="confirm-password" class="form__label">Xác nhận lại mật khẩu</label>
                    <form:input path="confirmPassword" id="confirm-password" name="confirm-password" type="password" class="form__input"/>
                    <p class="form__error">
                        <c:if test="${not empty errors['confirmPassword'] != null}">${errors['confirmPassword']}</c:if>
                    </p>
                </div>
                <input type="hidden" id="recaptchaToken" name="g-recaptcha-response">
                <%--                <c:set var="errorReCaptcha" value="${requestScope.errorReCaptcha}"/>--%>
                <%--                <c:if test="${param.errorReCaptcha eq 'true'}">${errorReCaptcha}--%>
                <%--                    <p class="alert alert-danger" role="alert">Mã recaptcha không hợp lệ, vui lòng thử đăng nhập--%>
                <%--                        lại </p>--%>
                <%--                </c:if>--%>
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

    <c:if test="${requestScope.sendMail != null}">
        <!--Modal -->
        <input type="checkbox" id="modal__hide" hidden="hidden">
        <div class="modal">
            <div class="modal__notify">
                <i class="model__checked-icon fa-regular fa-circle-check"></i>
                <p class="modal__text">Đăng kí thành công, vui lòng kiểm tra email bạn đã đăng kí. </p>
                <a href="<c:url value="/public/auth/signIn.jsp"/>" class="button modal__button
                                    button--hover">Đăng nhập
                </a>
                <label for="modal__hide" class="modal__close"><i class="fa-solid fa-xmark"></i></label>
            </div>
            <label for="modal__hide" class="modal__blur"></label>
        </div>
    </c:if>
</main>
<!--JS validate-->
<script src="<c:url value="/js/validateForm.js"/>"></script>
<script>
    // Check nhập không phù hợp
    $("input.form__input").on({
        keydown: function (e) {
            console.log();
            if (e.which === 32 && this.id !== 'fullName') {
                return false;
            }
        },
        change: function () {
            if (this.id !== 'fullname') {
                this.value = this.value.replace(/\s/g, "");
            }
        }
    });
    grecaptcha.ready(function () {
        grecaptcha.execute('6LdSQu4pAAAAAJ0LbzMt-kVSfKqzVZYQtmPo3mHD', {action: 'submit'}).then(function (token) {
            document.querySelector("#recaptchaToken").value = token;
            var validation = new Validation({
                formSelector: ".form",
                formBlockClass: "form__block",
                errorSelector: ".form__error",
                rules: [
                    Validation.isRequired("#username"),
                    Validation.isExistsUsername("#username"),
                    Validation.isRequired("#email"),
                    Validation.isEmail("#email"),
                    Validation.isExistsEmail("#email"),
                    Validation.isRequired("#password"),
                    Validation.isUnique("#password"),
                    Validation.isRequired("#fullName"),
                    Validation.isRequired("#phone"),
                    // Validation.isRequired("#birthday"),
                    Validation.isRequired("#confirm-password"),
                    Validation.isConfirm("#confirm-password", function () {
                        return document.querySelector("#password").value;
                    })
                ],
                submitSelector: "#form__submit",
            })
        });
    });
</script>
</body>

</html>