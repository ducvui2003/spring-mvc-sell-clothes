import {fromByteArray, toByteArray} from "../base64/base64.js";

/**
 * Read the values from the form on the page and then call the server, to
 * get a configuration settings to use when calling the WebAuthentication
 * API
 */
$(document).ready(function () {
    $("#registerForm").submit(function (event) {
        event.preventDefault();
        const formData = {
            fullName: $("#fullName").val(),
            email: $("#email").val()
        };
        startRegistration(formData);

    });
});

/**
 * Call the server with the form data fields converted to a JSON
 * object, the server will return a JSON object containing all
 * the settings that are needed to sucessfully call the browsers
 * WebAuthn API to generate the public / private key pair in the
 * authenticator device.
 *
 * @param formData an object containing the form values to send to the server.
 */
function startRegistration(formData) {
    logTitle("Step 0: Start the registration process with the server")
    logRequest(formData)
    $.ajax({
        type: "POST",
        url: "/webauthn/register/start",
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
function createCredential(credentialCreationOptions) {
    logTitle("Step 1: Use options from server to create credential in authenticator")
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

    console.log(finishRequest)
    logRequest(finishRequest)

    $.ajax({
        type: "POST",
        url: "/webauthn/register/finish",
        data: JSON.stringify(finishRequest),
        dataType: "json",
        contentType: "application/json",
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