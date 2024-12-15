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
    const currentKeyId = $("#currentKeyId");
    const inputNewKey = $("#inputNewKey");
    const formAddKey = $("#form-add-key");
    const addKeyModal = $("#addKeyModal");

    // Form validation for Update Key
    formAddKey.validate({
        rules: {
            // inputNewKey: {
            //     required: true,
            // },
            inputUploadKey: {
                required: true,
                singleFile: true,
                extension: "key"
            }
        },
        messages: {
            // inputNewKey: {
            //     required: "Vui lòng nhập khóa."
            // },
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
            var formData = new FormData(form);
            formData.append("currentKeyId", currentKeyId.val())
            $.ajax({
                url: "/api/key/add-key",
                type: "POST",
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    Swal.fire({
                        title: "Chúc mừng!",
                        text: "Khóa đã được cập nhập",
                        icon: "success"
                    });
                    if(response && response.data ) {
                        updateCurrentKey(response.data);
                    }
                    form.reset();
                    addKeyModal.modal('hide');
                    return true;
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

    // Hàm cập nhật Khóa hiện tại
    function updateCurrentKey(newKey) {
        if (newKey) {
            $("#alertWarning").hide();
            $("#currentKey").val(newKey); // Cập nhật textarea Khóa hiện tại
            $("#reportKeyBtn").show()
        } else {
            $("#alertWarning").show();
            $("#currentKey").val(""); // Xóa nếu không có khóa
            $("#reportKeyBtn").hide()
        }
    }

    document.getElementById('currentKey').addEventListener('dblclick', function() {
        // Select the content of the textarea
        this.select();
        // Copy the selected content to the clipboard
        document.execCommand('copy');
        // Optionally, you can show an alert or notification to the user
        alert('Content copied to clipboard!');
    });
    // Khởi tạo tất cả tooltip
    $('[data-bs-toggle="tooltip"]').tooltip();
});