import {formatDatetime, formDataToJson, http} from "../base.js";

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
            getKeyList();
            loadDataToTable();
            updateCurrentKey();
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
    $.validator.addMethod("extension", function (value, element, param) {
        var fileExtension = value.split('.').pop().toLowerCase();
        return this.optional(element) || fileExtension === param;  // Check if the file extension is valid
    }, "File phải có định dạng .key.");
    $.validator.addMethod(
        "singleFile",
        function (value, element) {
            return element.files.length === 1;
        },
        "Vui lòng chọn 1 file duy nhất."
    );

// Validate update key form
    const formAddKey = $("#form-add-key");
    const addKeyModal = $("#addKeyModal");

// Form validation for Update Key
        formAddKey.validate({
            rules: {
                inputUploadKey: {
                    required: true,
                    singleFile: true,
                    extension: "key",
                }
            },
            messages: {
                inputUploadKey: {
                    required: "Vui lòng chọn file khóa.",
                    singleFile: "Vui lòng chọn 1 file duy nhất.",
                    extension: "File phải có định dạng .key.",
                    invalidPrivateKey: "File khóa không hợp lệ hoặc bị hỏng, vui lòng thử lại."
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
                            getKeyList();
                            loadDataToTable();
                            updateCurrentKey();
                        }
                        form.reset();
                        addKeyModal.modal('hide');
                        return true;
                    },
                    error: function (xhr, status, error) {
                        let errorMessage = xhr.responseJSON?.message || "Khóa không cập nhập thành công!";
                        if (errorMessage === "Private key file is invalid or corrupted!") {
                            errorMessage = "File khóa không hợp lệ hoặc bị hỏng, vui lòng thử lại.";
                        }
                        Swal.fire({
                            title: "Lỗi!",
                            text: errorMessage,
                            icon: "error"
                        });
                    },
                });
                return false;
            }
        });

    // Hàm cập nhật Khóa hiện tại
    function updateCurrentKey() {
        if (keyListCustomer.length > 0) {
            if(keyListCustomer[0].isDelete){
                $("#alertWarning").show();
                $("#currentKey").val("");
                $("#createdDate").val("");
                $("#reportKeyBtn").hide();
                return;
            }
            $("#alertWarning").hide();
            $("#hasKey").val(true)
            $("#reportKeyBtn").show();
            $("#currentKey").val(keyListCustomer[0].publicKey); // Cập nhật textarea Khóa hiện tại
            $("#createdDate").val(keyListCustomer[0].createAt);
        } else {
            $("#alertWarning").show();
            $("#reportKeyBtn").hide();
            $("#currentKey").val(""); // Xóa nếu không có khóa
        }
    }

    // Lấy danh sách key
    let keyListCustomer = []
    function getKeyList() {
        http({
            url: "/api/key",
            type: 'GET',
        }).then((response) => {
            const keyList = response.data;
            if (keyList && keyList.length > 0) {
                keyListCustomer = keyList
                loadDataToTable();
            }
        });
    }

    getKeyList();
    // Load danh sách key vào UI
    function loadDataToTable() {
        // Cập nhật currentKeyId
        const table = $('#keyList tbody');
        updateCurrentKey();
        table.empty();
        const htmls = keyListCustomer.map(function (key) {
            if (key) {
                return `<tr>
                        <td>${key.id}</td>
                        <td>${key.publicKey}</td>
                        <td>${key.createAt}</td>
                        <td>${key.isDelete ? "Đã hết hạn" : "Đang hoạt động"}</td>
                        <td>
                            <button class="btn btn-primary btn__key_detail" data-id="${key.id}" data-bs-toggle="modal" data-bs-target="#detailKeyModal">
                                 <i class="fa-solid fa-eye"></i>
                            </button>
                        </td>
                    </tr>`
            }
            return '';
        })
        table.html(htmls.join(''));
    }

    $(document).on('click', '.btn__key_detail', function () {
        // Lấy ID của khóa từ thuộc tính data-id
        const keyId = $(this).data('id');

        // Tìm thông tin khóa trong danh sách keyListCustomer
        const keyDetail = keyListCustomer.find(key => key.id === keyId);
        if (keyDetail) {
            // Load thông tin vào modal
            const modalBody = $('#detailKeyModal .modal-body');
            modalBody.html(`
            <div class="container my-4">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h5>Thông tin chi tiết khóa</h5>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-4 font-weight-bold">Mã khóa (ID):</div>
                            <div class="col-md-8">${keyDetail.id}</div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-4 font-weight-bold">Public Key:</div>
                            <div class="col-md-8">
                                <textarea class="form-control" rows="5" readonly>${keyDetail.publicKey}</textarea>
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-4 font-weight-bold">Mã khóa trước (ID khóa cũ):</div>
                            <div class="col-md-8">${keyDetail.previousId || 'N/A'}</div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-4 font-weight-bold">Ngày tạo:</div>
                            <div class="col-md-8">${keyDetail.createAt}</div>
                        </div>
                        ${keyDetail.isDelete ? `
                        <div class="row mb-3">
                            <div class="col-md-4 font-weight-bold">Ngày hết hạn:</div>
                            <div class="col-md-8">${keyDetail.updateAt || 'N/A'}</div>
                        </div>
                        ` : ''}
                        <div class="row mb-3">
                            <div class="col-md-4 font-weight-bold">Trạng thái:</div>
                            <div class="col-md-8">
                                <span class="badge ${keyDetail.isDelete ? 'bg-danger' : 'bg-success'}">
                                    ${keyDetail.isDelete ? 'Đã hết hạn' : 'Đang hoạt động'}
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `);
        } else {
            console.error('Không tìm thấy thông tin khóa!');
        }
    });

    $('#detailKeyModal').on('hidden.bs.modal', function () {
        const modalBody = $(this).find('.modal-body');
        modalBody.html('<p>Loading...</p>'); // Reset nội dung modal
    });


    console.log($("#createdDate").val())
    $("#createdDate").val(formatDatetime($("#createdDate").val()));


// Khởi tạo tất cả tooltip
    $('[data-bs-toggle="tooltip"]').tooltip();
})
;






