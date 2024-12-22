import {formDataToJson, http} from "../base.js";

$(document).ready(function () {
    const formLostKey = $("#form-report-key");
    formLostKey.on('submit', function (event) {
        event.preventDefault();
        const form = formDataToJson(this);
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
            "url": "/api/key/lost-key",
            method: "PUT",
        }).then(response => {
            Swal.fire({
                title: 'Thông báo',
                text: 'Vui lòng kiểm tra email để nhận mã OTP',
                icon: 'info',
                confirmButtonText: 'Đồng ý'
            })
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
                    if (response && response.data) {
                        updateCurrentKey(response.data);
                        loadDataToTable();
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
    // Lấy danh sách key
    let keyListCustomer = []
    http({
        url: "/api/key",
        type: 'GET',
    }).then((response) => {
        const keyList = response.data;
        console.log(keyList);
        if(keyList && keyList.length > 0){
            keyListCustomer = keyList
            loadDataToTable();
        }
    });

    // Load danh sách key vào UI
    function loadDataToTable() {
        console.log("Hello");
        // Cập nhật currentKeyId
        currentKeyId.val(keyListCustomer[0].id);
        const table = $('#keyList tbody');
        table.empty();
        const htmls = keyListCustomer.map(function (key) {
            if (key) {
                return `<tr>
                        <td>${key.id}</td>
                        <td>${key.publicKey}</td>
                        <td>${key.createAt}</td>
                        <td>${key.updateAt}</td>
                        <td>${key.isDelete ? "Đã ngừng hoạt động" : "Đang hoạt động"}</td>
                        <td>
                            <button class="btn btn-primary btn__address-update" data-id="${key.id}" data-bs-toggle="modal" data-bs-target="#modal">
                                 <i class="fa-solid fa-pen-to-square"></i>
                            </button>
                           <button class="btn btn-danger btn__address-delete" data-id="${key.id}" >
                                <i class="fa-solid fa-trash"></i>
                            </button>
                        </td>
                    </tr>`
            }
            return '';
        })
        table.html(htmls.join(''));
        viewModal();
    }
// Khởi tạo tất cả tooltip
    $('[data-bs-toggle="tooltip"]').tooltip();
})
;






