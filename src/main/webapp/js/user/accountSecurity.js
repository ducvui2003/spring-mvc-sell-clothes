import {formDataToJson, http} from "../base.js";
import {fromByteArray, toByteArray} from "../base64/base64.js";

$(document).ready(function () {

    const btnAddKey = $("#btn-add-passkey");
    // Custom regex kiểm tra mật khẩu mạnh
    $.validator.addMethod("strongPassword", function (value, element) {
        return this.optional(element) || /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+[\]{}|;':"\\<>?,./-]).{8,}$/.test(value);
    }, "Mật khẩu phải chứa ít nhất 1 chữ cái viết thường, 1 chữ cái viết hoa, 1 số và 1 ký tự đặc biệt, tối thiểu 8 ký tự");

    var form = $('#form');
    // Validate from đổi mật khẩu
    form.validate({
        rules: {
            currentPassword: {
                required: true,
            }, newPassword: {
                required: true, strongPassword: true
            }, confirmPassword: {
                required: true, equalTo: "#newPassword"
            }
        }, messages: {
            currentPassword: {
                required: "Vui lòng nhập mật khẩu hiện tại",
            }, newPassword: {
                required: "Vui lòng nhập mật khẩu mới",
                strongPassword: "Mật khẩu phải chứa ít nhất 1 chữ cái viết thường, 1 chữ cái viết hoa, 1 số và 1 ký tự đặc biệt, tối thiểu 8 ký tự"
            }, confirmPassword: {
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
            // Hiển thi dialog xác nhận
            Swal.fire({
                title: "Bạn có muốn lưu thay đổi không?",
                showDenyButton: true,
                confirmButtonText: "Lưu",
                denyButtonText: "Không lưu",
            }).then((result) => {
                if (result.isConfirmed) {
                    handleChangePassword(form)
                    return;
                }
                if (result.isDenied) {
                    Swal.fire({
                        title: "Thay đổi của bạn đã sẽ không được lưu",
                        icon: "info"
                    });
                }
            });
        }
    });

    function handleChangePassword(form) {
        const data = formDataToJson(form);
        http(
            {
                url: "/api/user/change-password",
                type: 'POST',
                data: data,
            }
        ).then((response) => {
            if (response.code === 200)
                Swal.fire({
                    title: "Chúc mừng!",
                    text: "Đổi mật khẩu thành công",
                    icon: "success"
                });
            else
                Swal.fire({
                    title: "Lỗi!",
                    text: "Đổi mật khẩu ko thành công, vui lòng kiểm tra lại mật khẩu hiện tại",
                    icon: "error"
                });

            form.reset();
        }).catch((err) => {
            Swal.fire({
                title: "Lỗi!",
                text: "Mật khẩu hiện tại không chính xác",
                icon: "error"
            });
        })
    }

    getPasskey();
    btnAddKey.on("click", () => {
        handleAddPasskey();
    })

    $('[data-bs-toggle="tooltip"]').tooltip();
});
const passkeyListElement = $("#passkey-list");

function getPasskey() {
    http(
        {
            url: "/api/passkey",
            type: 'GET',
        }
    ).then((response) => {
        if (response.code === 200)
            loadPasskey(response.data)
    })
}

function loadPasskey(passkeyList) {
    passkeyListElement.empty();
    passkeyList.forEach((passkey) => addNewCredentialToView(passkey));
}

function handleAddPasskey() {
    Swal.fire({
        title: 'Thêm khóa mới',
        text: "Sử dụng khóa này để đăng nhập với passkey",
        showDenyButton: true,
        confirmButtonText: "Tiến hành",
        denyButtonText: 'Hủy',
        focusConfirm: false,
    }).then((result) => {
            if (result.isConfirmed) {
                startRegistration()
            }
        }
    )
}

function startRegistration() {
    http({
        type: "POST",
        url: "/webauthn/register/start",
    }).then((response) => {
        createCredential(response)
    });
}

function createCredential(credentialCreationOptions) {
    const publicKey = {
        challenge: toByteArray(credentialCreationOptions.challenge),
        rp: {
            name: credentialCreationOptions.rp.name,
            id: credentialCreationOptions.rp.id,
        },
        user: {
            name: credentialCreationOptions.user.name,
            displayName: credentialCreationOptions.user.displayName,
            id: toByteArray(credentialCreationOptions.user.id)
        },
        pubKeyCredParams: credentialCreationOptions.pubKeyCredParams,
        attestation: credentialCreationOptions.attestation
    }
    console.log(publicKey)
    navigator.credentials.create({'publicKey': publicKey})
        .then((newCredentialInfo) => {
            console.log('SUCCESS', newCredentialInfo)
            finishRegistration(newCredentialInfo)
        })
        .catch((error) => {
            console.log('FAIL', error)
        })
}

function finishRegistration(newCredentialInfo) {
    const finishRequest = {
        id: newCredentialInfo.id,
        rawId: fromByteArray(newCredentialInfo.rawId),
        type: newCredentialInfo.type,
        response: {
            clientDataJSON: fromByteArray(newCredentialInfo.response.clientDataJSON),
            attestationObject: fromByteArray(newCredentialInfo.response.attestationObject)
        },
        clientExtensionResults: {}
    }
    Swal.fire({
        title: "Đặt tên cho chứng chỉ này",
        input: "text",
        inputPlaceholder: "Tên chứng chỉ",
        inputValidator: (value) => {
            if (!value.trim()) {
                return "Vui lòng nhập tên chứng chỉ!";
            }
            if (value.length < 3) {
                return "Tên chứng chỉ phải dài ít nhất 3 ký tự!";
            }
            return null;
        }
    }).then((result) => {
        if (result.isConfirmed) {
            const data = {
                name: result.value,
                request: finishRequest
            }
            http({
                type: "POST",
                data: data,
                url: "/webauthn/register/finish",
            }).then((response) => {
                if (response.code === 200) {
                    Swal.fire({
                        title: "Thành công!",
                        text: "Đã thêm khóa mới",
                        icon: "success"
                    });
                    addNewCredentialToView({
                        id: response.data.id,
                        publicKey: response.data.publicKey,
                        name: response.data.name,
                        createdAt: response.data.createdAt
                    });
                } else
                    Swal.fire({
                        title: "Lỗi!",
                        text: "Không thêm được khóa mới",
                        icon: "error"
                    });
            });
        }
    });
}

function addNewCredentialToView({id, publicKey, name, createdAt}) {
    const html = `
                 <div data-id="${id}" class="d-flex justify-content-between border border-1 p-2 bg-info bg-opacity-10 border border-info border-start-0 rounded mt-2">
                    <div class="">
                        <div class="d-flex gap-2 align-items-center py-2 ">
                            <img src="/assets/img/icon_passkey.png" width="35px" height="35px">
                            <span  data-bs-toggle="tooltip" 
                                    data-bs-placement="top"
                                    data-bs-title="${publicKey}" >${name}</span>
                        </div>
                        <span class="text-secondary fs-6">Khóa được thêm vào lúc ${createdAt}</span>
                    </div>
                    <button class="btn btn-danger" data-id="${id}">Xóa</button>
                </div>
    `;
    const appendElement = $(html).appendTo(passkeyListElement);
    appendElement.find(".btn.btn-danger").on("click", handleDeletePasskey);
}


function handleDeletePasskey() {
    const credentialId = $(this).data("id");
    const ref = this
    Swal.fire({
        title: 'Xóa khóa',
        text: "Bạn có chắc chắn muốn xóa khóa này?",
        showDenyButton: true,
        confirmButtonText: "Xóa",
        denyButtonText: 'Hủy',
        focusConfirm: false,
    }).then((result) => {
            if (result.isConfirmed) {
                http({
                    url: "/api/passkey/:id",
                    type: "DELETE",
                    pathVariables: {
                        id: credentialId
                    }
                }).then((response) => {
                    if (response.code === 200) {
                        {
                            Swal.fire({
                                title: "Thành công!",
                                text: "Đã xóa khóa",
                                icon: "success"
                            });
                            deleteCredentialToView(ref);
                        }
                    } else
                        Swal.fire({
                            title: "Lỗi!",
                            text: "Không xóa được khóa",
                            icon: "error"
                        });
                })
            }
        }
    )
}

function deleteCredentialToView(element) {
    const credentialId = $(element).data("id");
    const credential = $(`[data-id="${credentialId}"]`);
    credential.remove();
}