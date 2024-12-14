import {alert} from "../notify.js";
import {addSpinner, cancelSpinner} from "../spinner.js";
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
    const inputNewKey = $("#inputNewKey");
    const formAddKey = $("#form-add-key");

    // Form validation for Update Key
    formAddKey.validate({
        rules: {
            inputNewKey: {
                required: true,
            },
            inputUploadKey: {
                required: true,
                singleFile: true,
                extension: "key"
            }
        },
        messages: {
            inputNewKey: {
                required: "Vui lòng nhập khóa."
            },
            inputUploadKey: {
                required: "Vui lòng chọn file khóa.",
                singleFile: "Vui lòng chọn 1 file duy nhất.",
                extension: "File phải có định dạng .key."
            },
        },
        validClass: 'is-valid',
        errorClass: 'is-invalid',
        errorPlacement: function (error, element) {
            $(element).next().text(error.text());
        },
        highlight: function (element, errorClass, validClass) {
            $(element).addClass(errorClass).removeClass(validClass).attr('required', 'required');
            $(element).next().addClass("invalid-feedback");
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).removeClass(errorClass).addClass(validClass).removeAttr('required');
            $(element).next().text("");
        },
        submitHandler: function (form) {
            const formData = formDataToJson(form);
            http({
                url: "/api/user/add-key",
                type: "POST",
                data: formData,
                success: function (response) {
                    Swal.fire({
                        title: "Chúc mừng!",
                        text: "Khóa đã được cập nhập",
                        icon: "success"
                    });
                    // Cập nhật UI nếu cần
                    $("#hasKey").text("Hello");
                    $("#currentKey").text($("#hasKey").text());

                },
                error: function (xhr, status, error) {
                    console.log("Hien thi input:    ",formData.get("newKey"));
                    Swal.fire({
                        title: "Lỗi!",
                        text: "Khóa không cập nhập thành công " + status,
                        icon: "error"
                    });
                },
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
                inputNewKey.text(lines[0]);
            }
            reader.readAsText(file);
        }
    });


});