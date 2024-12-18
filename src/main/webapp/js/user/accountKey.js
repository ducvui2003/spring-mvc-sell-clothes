import {alert} from "../notify.js";
import {addSpinner, cancelSpinner} from "../spinner.js";
import {formDataToJson, http} from "../base.js";

$(document).ready(function () {
    const formLostKey = $("#form-report-key");
    formLostKey.on('submit', function (event) {
        event.preventDefault();
        const form= formDataToJson(this);
        http({
            url: "/api/key/verify-otp",
            method: "PUT",
            data: form,
        }).then(response => {

        }).catch(error => {

        })

    });


    const lostKeyButton = $('#reportKeyModal');
    lostKeyButton.on('show.bs.modal', function (event) {
        http({
            "url": "/api/user/key/lost-key",
            method: "PUT",
        }).then(response => {
        //TODO:show dialog notify
        })
    }).on('hide.bs.modal', function (event) {
        $('#reportKeyForm').trigger('reset');
    });

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
    const addKeyModal = $("#addKeyModal");

// Form validation for Update Key
    formAddKey.validate({
        rules: {
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
            $.ajax({
                url: "/api/user/key/add-key",
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
                    if (response && response.data) {
                        updateCurrentKey(response.data);
                    }
                    form.reset();
                    addKeyModal.modal('hide');
                    return true;
                },
                error: function (xhr, status, error) {
                    console.log("Hien thi input:    ", formData.get("newKey"));
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
    $("#inputUploadKey").change(function () {
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

    $('#currentKey').on('input', function () {
        const minRows = 3;
        const maxRows = 6;
        const lineHeight = parseFloat($(this).css('line-height')); // Line height
        this.style.height = 'auto'; // Reset height
        const newHeight = this.scrollHeight;

        // Calculate height limits
        if (newHeight > lineHeight * maxRows) {
            this.style.height = lineHeight * maxRows + 'px'; // Max rows
            this.style.overflowY = 'auto'; // Show scrollbar
        } else {
            this.style.height = newHeight + 'px';
            this.style.overflowY = 'hidden'; // Hide scrollbar
        }
    }).trigger('input'); // Initialize on page load


    document.getElementById('currentKey').addEventListener('dblclick', function () {
        // Select the content of the textarea
        this.select();
        // Copy the selected content to the clipboard
        document.execCommand('copy');
        // Optionally, you can show an alert or notification to the user
        alert('Content copied to clipboard!');
    });
// Khởi tạo tất cả tooltip
    $('[data-bs-toggle="tooltip"]').tooltip();
})
;

// Hàm cập nhật Khóa hiện tại
function updateCurrentKey(newKey) {
    if (newKey) {
        $("#alertWarning").hide();
        $("#hasKey").val(newKey)
        $("#currentKey").val(newKey); // Cập nhật textarea Khóa hiện tại
    } else {
        $("#alertWarning").show();
        $("#currentKey").val(""); // Xóa nếu không có khóa
    }
}
