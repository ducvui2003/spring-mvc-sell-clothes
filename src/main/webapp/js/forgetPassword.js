import {formDataToJson, http} from "./base.js";

$(document).ready(() => {
    const form = $("form")

    const formValidator = {
        rules: {
            email: {
                required: true,
                email: true
            }
        },
        messages: {
            email: {
                required: "Vui lòng nhập email",
                email: 'Email không hợp lệ'
            }
        },
        onkeyup: function (element) {
            $(element).valid();
        },
        onfocusout: function (element) {
            $(element).valid();
        },
        onblur: function (element) {
            $(element).valid();
        },
        validClass: 'text-success',
        errorClass: 'invalid',
        errorPlacement: function (error, element) {
            $(element).parents(".form__block").children(".form__error").text(error.text());
        },
        highlight: function (element, errorClass, validClass) {
            $(element).parents(".form__block").addClass(errorClass);
            $(element).parents(".form__block").children(".form__error").removeClass(validClass)
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).parents(".form__block").removeClass(errorClass);
            $(element).parents(".form__block").children(".form__error").text("Hợp lệ").addClass(validClass)
        },
        submitHandler: function (form) {
            const data = formDataToJson(form);
            http(
                {
                    url: '/api/auth/forgot-password',
                    method: 'POST',
                    data: data
                }
            ).then((response) => {
                if (response.code === 200)
                    Swal.fire({
                        title: 'Quên mật khẩu thành công',
                        text: 'Vui lòng kiểm tra email để lấy lại tài khoản',
                        icon: 'success',
                        confirmButtonText: 'Đăng nhập',
                        confirmButtonColor: "#28a745",
                        html: `
                        <div>
                            <p class="fs-3">Vui lòng kiểm tra email bạn đã đăng kí. </p>
                        </div>
                    `
                    }).then((response) => {
                        if (response.isConfirmed) {
                            window.location.href = "/home";
                        }
                    });
            }).catch((error) => {
                Swal.fire({
                    title: 'Quên mật khẩu không thành công',
                    text: "Thông tin không hợp lệ hoặc tài khoản với email đã không tồn tại",
                    icon: 'error',
                    confirmButtonText: 'Đóng',
                    confirmButtonColor: "#dc3545",
                });
            });
            return false;
        }
    }

    form.validate(formValidator);
});