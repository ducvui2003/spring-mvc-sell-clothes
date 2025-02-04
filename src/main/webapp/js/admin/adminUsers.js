import {addParam, http} from "../base.js";

$(document).ready(() => {
    $.fn.select2.defaults.set("width", "resolve");
    // Custom regex kiểm tra mật khẩu mạnh
    $.validator.addMethod("strongPassword", function (value, element) {
        return this.optional(element) || /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+[\]{}|;':"\\<>?,./-]).{8,}$/.test(value);
    }, "Mật khẩu phải chứa ít nhất 1 chữ cái viết thường, 1 chữ cái viết hoa, 1 số và 1 ký tự đặc biệt, tối thiểu 8 ký tự");

    const configDatatable = {
        paging: true,
        processing: true,
        serverSide: true,
        page: 1,
        pageLength: 8,
        scrollCollapse: true,
        ajax: {
            url: "/api/admin/user/datatable",
            type: "POST",
            contentType: "application/json",
            data: function (d) {
                return JSON.stringify(d);
            },
        }, columns: [
            {
                data: "id"
            }, {
                data: "username",
            }, {
                data: "email",
            }, {
                data: "fullName",
                defaultContent: "Chưa cập nhật",
            }, {
                data: "gender", render: (data, type, row, meta) => {
                    if (data === 'FEMALE')
                        return `<span class="p-1 text-center"  ><i class="fa-solid fa-venus" style="color:deeppink;"></i></span>`
                    else
                        return `<span class="p-1 text-center" ><i class="fa-solid fa-mars" style="color:  #0d6efd;"></i></span>`

                }
            },
            {
                data: "phone",
                defaultContent: "Chưa cập nhật",
            },
        ],
        columnDefs: [
            {
                targets: 4,
                orderable: false
            },
        ],
        createdRow: function (row, data, dataIndex) {
            if (data.availableTurns == 0) {
                $(row).addClass('table-warning');
                return;
            }
            switch (data.state) {
                case "1":
                    $(row).addClass('table-success');
                    break;
                case "2":
                    $(row).addClass('table-danger');
                    break;
            }

        },
        language: {
            url: 'https://cdn.datatables.net/plug-ins/1.11.5/i18n/vi.json'
        },
        select: true,
        initComplete: function (settings, json) {
            handleEventDatatable();
        }
    }

    function handleEventDatatable() {
        // Xử lý sự kiện khi click vào 1 dòng trong bảng
        datatable.on('select', function (e, dt, type, indexes) {
            row = {
                rowDataSelected: datatable.row(indexes).data(),
                rowIndexSelected: datatable.row(indexes).index()
            }
            button.text("Cập nhật khách hàng");
        }).on('deselect', function (e, dt, type, indexes) {
            row = {
                rowDataSelected: undefined,
                rowIndexSelected: undefined,
            }
            button.text("Thêm khách hàng")
        });
        table.on('mouseenter', 'tr', function () {
            $(this).addClass('hovered').css('cursor', 'pointer');
        }).on('mouseleave', 'tr', function () {
            $(this).removeClass('hovered').css('cursor', 'default');
        });
    }

    const configValidator = {
        rules: {
            email: {
                required: true,
            },
            username: {
                required: true,
                minlength: 10,
            },
            password: {
                required: true,
                strongPassword: true
            },
            fullName: {
                required: true,
                minlength: 5
            },
            availableTurns: {
                required: true,
                number: true,
            },
            phone: {
                required: true,
                minlength: 10
            },
            gender: {
                required: true,
            },
            birthDay: {
                required: true
            },
            role: {
                required: true
            }
        }, messages: {
            email: {
                required: "Vui lòng nhập email",
            },
            username: {
                required: "Vui lòng nhập tên đăng nhập",
                minlength: "Tên đăng nhập phải chứa ít nhất 10 ký tự",
            },
            password: {
                required: "Vui lòng nhập mật khẩu",
            },
            fullName: {
                required: "Vui lòng nhập họ tên",
                minlength: "Họ tên phải chứa ít nhất 5 ký tự",
            },
            phone: {
                required: "Vui lòng nhập số điện thoại",
                minlength: "Số điện thoại phải chứa ít nhất 11 ký tự",
            },
            gender: {
                required: "Vui lòng chọn giới tính",
            },
            birthDay: {
                required: "Vui lòng chọn ngày sinh",
            },
            role: {
                required: "Vui lòng chọn vai trò",
            }
        },
        onblur: function (element) {
            $(element).valid();
        },
        validClass: 'is-valid',
        errorClass: 'is-invalid',
        errorPlacement: function (error, element) {
            $(element).parent().find(".valid-feedback , .invalid-feedback").text(error.text());
        },
        success: function (label) {
            label.remove(); // Remove the error message when input is valid
        },
        highlight: function (element, errorClass, validClass) {
            $(element).addClass(errorClass).removeClass(validClass).attr('required', 'required');
            $(element).parent().find(".valid-feedback").addClass("invalid-feedback");
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).removeClass(errorClass).addClass(validClass).removeAttr('required');
            $(element).find(".valid-feedback").text("");
        },
        submitHandler: function (form) {
            let formData = $(form).serialize();
            // Nếu không có dòng nào được chọn thì thực hiện thêm mới
            if (!row) {
                Swal.fire({
                    title: `Bạn có muốn thêm người dùng này không?`,
                    showCancelButton: true,
                    confirmButtonText: "Có",
                    denyButtonText: `Không`
                }).then((result) => {
                    if (result.isConfirmed) {
                        handleSave(formData, (response) => {
                            if (response.success) {
                                form.reset();
                                Swal.fire({
                                    icon: 'success',
                                    title: 'Thêm thành công',
                                })
                                modal.modal("hide");
                            } else {
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Thêm thất bại',
                                })
                            }
                        })
                    }
                });
            } else {
                Swal.fire({
                    title: `Bạn có muốn cập nhập người dùng này không?`,
                    showDenyButton: true,
                    showCancelButton: true,
                    confirmButtonText: "Có",
                    denyButtonText: `Không`
                }).then((result) => {
                    // Nếu có dòng nào được chọn thì thực hiện cập nhập
                    formData = addParam(form, {
                        key: "id",
                        value: row.rowDataSelected.id
                    })
                    if (result.isConfirmed) {
                        handleUpdate(formData, (response) => {
                            if (response.success) {
                                form.reset();
                                Swal.fire({
                                    icon: 'success',
                                    title: 'Cập nhập thành công',
                                })
                                modal.modal("hide");
                            } else {
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Cập nhập thất bại',
                                })
                            }
                        })
                    }
                });
            }
            return false;
        }
    };


    function configModal() {
        // Bắt sự kiên khi modal đóng
        modal.on("hide.bs.modal", function () {
            form.find("input, textarea, select").val("")
            formValidate.resetForm();
        })
        // Bắt sự kiện khi modal mở
        modal.on("show.bs.modal", function () {
            // Tắt các input không cho phép chỉnh sửa
            disableInputs();
            // Khi ngừoi dùng đẫ chọn dòng để update
            if (row.rowDataSelected) {
                $("#staticBackdropLabel").text("Cập nhập thông tin khách hàng");
                const id = row.rowDataSelected.id;
                http({
                    url: `/api/admin/user/:id`,
                    method: "GET",
                    pathVariables: {
                        id: id
                    }
                }).then((response) => {
                    fieldDataVoucher(response.data);
                })

            } else {
                $("#staticBackdropLabel").text("Thêm khách hàng");
                // Khi người dùng thêm mới -> Bật các input cho phép chỉnh sửa
                enableInputs();
            }
        });
    }

    function handleSave(formData, callback) {
        $.ajax({
            url: "/api/admin/user/create",
            type: "POST",
            data: formData,
            success: function (response) {
                callback(response);
            }
        })
    }

    function handleUpdate(formData, callback) {
        $.ajax({
            url: "/api/admin/user/update",
            type: "POST",
            data: formData,
            success: function (response) {
                callback(response);
            }
        })
    }

    const tableKey = $("#table-key tbody");

    function fieldDataVoucher(data) {
        const user = data;
        form.find("#email").val(user.email);
        form.find("#username").val(user.username);
        form.find("#fullName").val(user.fullName);
        form.find("#password").val("")
        form.find("#phone").val(user.phone);
        form.find("#gender").val(user.gender);
        form.find("#birthday").val(user.birthDay);
        form.find("#role").val(user.role);
        console.log(user)
        console.log(user.keys)
        fieldTableKey(user.keys);
    }

    function fieldTableKey(keys) {
        tableKey.empty();
        const html = keys.map((key) => {
            return `<tr class="table-active ${key.inUse ? 'table-success' : 'table-danger'}">
                        <th >
                        <p data-bs-toggle="tooltip" 
                            data-bs-placement="top"
                            data-bs-title="${key.id}" class="text-truncate" style="width: 100px">${key.id}</p>
                        </th>
                        <th >
                            <p data-bs-toggle="tooltip" 
                            data-bs-placement="top"
                            data-bs-title="${key.publicKey}" data-public-key="${key.publicKey}" class="text-truncate icon-link icon-link-hover" style="width: 100px">${key.publicKey}</p></th>
                        <th>${key.createdAt}</th>
                        <th>${key.inUse ? "Đang sử dụng" : "Đã vô hiệu"}</th>
                    </tr>`
        });
        tableKey.append(html);
        showPublicKey();
    }

    function showPublicKey() {
        $.each($('[data-public-key]'), function (index, element) {
            const publicKey = $(element).data("public-key");
            $(element).on("click", function () {
                Swal.fire({
                    title: "Thông tin khóa công khai",
                    text: publicKey,
                    icon: "info"
                });
            })
        })
    }

    function disableInputs() {
        form.find("#password").attr("disabled", "disabled");
        form.find("#email").attr("disabled", "disabled");
        form.find("#username").attr("disabled", "disabled");
    }

    function enableInputs() {
        form.find("#password").removeAttr("disabled");
        form.find("#email").removeAttr("disabled");
        form.find("#username").removeAttr("disabled");
    }

    const table = $("#table");
    const button = $("#button");
    let row = {
        rowDataSelected: undefined,//Lưu giữ đối tượng mà ngừoi dùng đã chọn để thực hiện cập lấy thông tin trong chức năng cập nhập
        rowIndexSelected: undefined //Lưu giữ vị trí dòng mà ngừoi dùng đã chọn để thực hiện cập lấy thông tin trong chức năng cập nhập
    };
    const datatable = table.DataTable(configDatatable);
    const form = $("#form")
    const formValidate = form.validate(configValidator);
    const modal = $("#modal");
    configModal();
    $('[data-bs-toggle="tooltip"]').tooltip();
});