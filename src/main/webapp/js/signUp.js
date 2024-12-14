import {formDataToJson, http} from "./base.js";

$(document).ready(() => {

    $.validator.addMethod("strongPassword", function (value, element) {
        return this.optional(element) || /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+[\]{}|;':"\\<>?,./-]).{8,}$/.test(value);
    }, "Mật khẩu phải chứa ít nhất 1 chữ cái viết thường, 1 chữ cái viết hoa, 1 số và 1 ký tự đặc biệt, tối thiểu 8 ký tự");

    jQuery.validator.addMethod("vietnamesePhone", function (value, element) {
        // Regular expression for Vietnamese phone numbers
        const phoneRegex = /^(0[3|5|7|8|9])\d{8}$/; // Mobile numbers
        return this.optional(element) || phoneRegex.test(value);
    }, "Please enter a valid Vietnamese phone number");

    const form = $("form")

    const formValidator = {
        rules: {
            username: {
                required: true,
            },
            email: {
                required: true,
                email: true
            },
            fullName: {
                required: true,
            },
            gender: {
                required: true,
            },
            dob: {
                required: true,
            },
            phone: {
                required: true,
                vietnamesePhone: true
            },
            password: {
                required: true,
                strongPassword: true
            },
            confirmPassword: {
                required: true, equalTo: "#password"
            }
        },
        messages: {
            username: {
                required: "Vui lòng nhập tên đăng nhập",
            },
            email: {
                required: "Vui lòng nhập email",
                email: 'Email không hợp lệ'
            },
            fullName: {
                required: "Vui lòng nhập họ tên",
            },
            gender: {
                required: 'Vui lòng chọn giới tính',
            },
            dob: {
                required: "Vui lòng chọn ngày sinh",
            },
            phone: {
                required: "Vui lòng nhập số điện thoại",
                vietnamesePhone: "Số điện thoại không hợp lệ"
            },
            password: {
                required: "Vui lòng nhập mật khẩu",
                strongPassword: "Mật khẩu phải chứa ít nhất 1 chữ cái viết thường, 1 chữ cái viết hoa, 1 số và 1 ký tự đặc biệt, tối thiểu 8 ký tự"
            },
            confirmPassword: {
                required: "Vui lòng xác nhận mật khẩu",
                equalTo: "Mật khẩu xác nhận không khớp"
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
                    url: '/api/auth/sign-up',
                    method: 'POST',
                    data: data
                }
            ).then((response) => {
                if (response.code === 200)
                    Swal.fire({
                        title: 'Đăng ký thành công',
                        text: 'Vui lòng kiểm tra email để xác thực tài khoản',
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
                    title: 'Đăng ký thất bại',
                    text: "Thông tin đăng ký không hợp lệ hoặc tài khoản với email đã tồn tại",
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