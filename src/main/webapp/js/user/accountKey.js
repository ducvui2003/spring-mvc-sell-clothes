import {alert} from "../notify";
import {addSpinner, cancelSpinner} from "../spinner";
import {formDataToJson, http} from "../base.js";

$(document).ready(function () {

    $.validator.addMethod(
        "singleFile",
        function (value, element) {
            return element.files.length === 1;
        },
        "Vui lòng chọn 1 file duy nhất."
    );

    // Validate update key form
    const uploadKey = $("#uploadKey");
    const newPublicKey = $("#newPublicKey");
    const newPrivateKey = $("#newPrivateKey");
    const formUpdateKey = $("#form-update-key");

    // Form validation for Update Key
    formUpdateKey.validate({
        rules: {
            uploadKey: {
                required: true,
                singleFile: true,
                extension: "key"
            },
        },
        messages: {
            uploadKey: {
                required: "Vui lòng chọn file khóa.",
                singleFile: "Vui lòng chọn 1 file duy nhất.",
                extension: "File phải có định dạng .key."
            },
        },
        validClass: 'is-valid',
        errorClass: 'is-invalid',
        errorPlacement: function (error, element) {
            $(element).after(error); // Place error message after the element
        },
        highlight: function (element, errorClass, validClass) {
            $(element).addClass(errorClass).removeClass(validClass);
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).removeClass(errorClass).addClass(validClass);
        },
        submitHandler: function (form) {
            const formData = new FormData(form);
            formData.append("newPublicKey", newPublicKey.text());
            formData.append("newPrivateKey", newPrivateKey.text());

            $.ajax({
                url: "/api/user/update-key",
                type: "POST",
                data: formData,
                processData: false,
                contentType: false,
                beforeSend: function () {
                    addSpinner();
                },
                success: function (response) {
                    Swal.fire({
                        title: "Chúc mừng!",
                        text: "Khóa đã được cập nhập",
                        icon: "success"
                    });
                },
                error: function (xhr, status, error) {
                    Swal.fire({
                        title: "Lỗi!",
                        text: "Khóa không cập nhập thành công",
                        icon: "error"
                    });
                },
                complete: function () {
                    cancelSpinner();
                }
            });
            return false;
        }
    });

    // Change review new key
    uploadKey.change(function () {
        const file = uploadKey.prop('files')[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                const lines = e.target.result.split('\n');
                newPublicKey.text(lines[0]);
                newPrivateKey.text(lines[1]);
            }
            reader.readAsText(file);
        }
    });

});