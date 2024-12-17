import {toByteArray, fromByteArray} from "../base64/base64.js";

$(document).ready(function () {
    console.log("ready!")
    console.log($("#loginForm"))
    $("#loginForm").submit(function (event) {
        const formData = {
            email: $("#email").val()
        };
        startLogin(formData);
        event.preventDefault();
    });
});

function startLogin(formData) {
    logTitle("Step 0: Start the login process with the server")
    logRequest(formData)
    $.ajax({
        type: "POST",
        url: "/webauthn/login/start",
        data: JSON.stringify(formData),
        dataType: "json",
        contentType: "application/json",
        beforeSend: function (xhr) {
        },
        success: function (data, textStatus, jqXHR) {
            logResponse(data)
            createCredential(data)
        }
    });
}

/**
 * This functions calls out the WebAuthentication browser API to have the authenticator
 * create a public private key pair.
 * @param settings
 */

function createCredential(settings) {
    logTitle("Step 1: Use options from server to create credential in authenticator")
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

    console.log(publicKey)
    console.log(JSON.stringify(publicKey, 2))
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
            signature: fromByteArray(getAssertionResponse.response.signature)
            // userHandle: fromByteArray(getAssertionResponse.response.userHandle)
        },
        clientExtensionResults: {}
    }

    console.log(finishRequest)
    logJson("Login Finish Request", finishRequest
    )

    logJson("form post to spring login endpoint", finishRequest)
    $.ajax({
        type: "POST",
        url: "/webauthn/login/finish",
        data: JSON.stringify(finishRequest),
        contentType: "application/json",
        dataType: "json",
        beforeSend: function (xhr) {
            // spring security has CSRF protection turned on, if we don't
            // set the correct CSRF header and value spring security will
            // block the request from getting to the Spring MVC controller
            // that will process this request.
        },
        success: function (data, textStatus, jqXHR) {
            logResponse(data)

        }
    });
}

function logTitle(title) {
    const h2 = $('<h2></h2>').text(title)
    $("body").append(h2)
}

function logJson(title, data) {
    const response = JSON.stringify(data, null, 2);
    const p = $('<p>' + title + '</p>')
    const pre = $('<pre></pre>').text(response)
    $("body").append(p, pre)
}

function logRequest(data) {
    const response = JSON.stringify(data, null, 2);
    const p = $('<p>Request:</p>')
    const pre = $('<pre></pre>').text(response)
    $("body").append(p, pre)
}

function logResponse(data) {
    const response = JSON.stringify(data, null, 2);
    const p = $('<p>Response:</p>')
    const pre = $('<pre></pre>').text(response)
    $("body").append(p, pre)
}