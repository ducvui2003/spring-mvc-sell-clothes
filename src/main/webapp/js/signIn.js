import {http} from "./base.js";
import {toByteArray, fromByteArray} from "./base64/base64.js";

$(document).ready(() => {

    init();

    function init() {
        console.log("ready!")
        const btnPasskey = $("#btn-passkey");
        btnPasskey.on("click", function () {
            Swal.fire({
                title: 'Đăng nhập với passkey',
                input: "email",
                inputLabel: "Email",
                inputPlaceholder: "Vui lòng nhập email của bạn",
                focusConfirm: false,
                showCancelButton: true,
                confirmButtonText: 'Submit',
                inputValidator: (value) => {
                    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Regular expression for email validation
                    if (!value) {
                        return "Email không được để trống!"; // Custom message if the field is empty
                    }
                    if (!emailRegex.test(value)) {
                        return "Email không hợp lệ. Vui lòng nhập đúng định dạng."; // Custom invalid email message
                    }
                    return null; // No errors
                }
            }).then((result) => {
                let email = result.value;
                if (result.isConfirmed) {
                    const formData = {
                        email: email
                    };
                    startLogin(formData);
                }
            });
        });
    }

    function startLogin(formData) {
        http({
            type: "POST",
            url: "/webauthn/login/start",
            data: formData,
        }).then((response) => {
            createCredential(response)
        });
    }

    /**
     * This functions calls out the WebAuthentication browser API to have the authenticator
     * create a public private key pair.
     * @param settings
     */

    function createCredential(settings) {
        let creds = [];
        settings.publicKeyCredentialRequestOptions.allowCredentials.forEach(
            cred => {
                const result = {
                    type: cred.type,
                    id: toByteArray(cred.id)
                }
                creds.push(result)
            }
        )
        const publicKey = {
            challenge: toByteArray(settings.publicKeyCredentialRequestOptions.challenge),
            allowCredentials: creds
        }

        navigator.credentials.get({'publicKey': publicKey})
            .then((getAssertionResponse) => {
                console.log('SUCCESSFULLY GOT AN ASSERTION!', getAssertionResponse)
                finishLogin(settings, getAssertionResponse)
            })
            .catch((error) => {
                alert('Open your browser console!')
                console.log('FAIL', error)
            })
    }

    function finishLogin(settings, getAssertionResponse) {
        const finishRequest = {
            id: getAssertionResponse.id,
            rawId: fromByteArray(getAssertionResponse.rawId),
            type: getAssertionResponse.type,
            response: {
                authenticatorData: fromByteArray(getAssertionResponse.response.authenticatorData),
                clientDataJSON: fromByteArray(getAssertionResponse.response.clientDataJSON),
                signature: fromByteArray(getAssertionResponse.response.signature),
                userHandle: fromByteArray(getAssertionResponse.response.userHandle)
            },
            clientExtensionResults: {}
        }
        http({
            type: "POST",
            url: "/webauthn/login/finish",
            data: finishRequest,
        }).then((response) => {
            if (response.code === 200) {
                Swal.fire({
                    icon: 'success',
                    title: 'Đăng nhập thành công!',
                    showConfirmButton: false,
                    timer: 1500
                }).then(() => {
                    window.location.href = "/home";
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Đăng nhập thất bại!',
                    showConfirmButton: false,
                    timer: 1500
                });
            }
        }).catch((error) => {
            console.log("error")
            console.log(error)
            Swal.fire({
                icon: 'error',
                title: 'Đăng nhập thất bại!',
                showConfirmButton: false,
                timer: 1500
            });
        });
    }

})