export const addParam = (form, {key, value}) => {
    let formDataArray = $(form).serializeArray();
    formDataArray.push({name: key, value: value}); // Add your custom parameter
    return $.param(formDataArray);
}
export const removeParamFromQueryString = (queryString, paramToRemove) => {
    // Parse the query string into an object
    const params = $.deparam(queryString);

    // Remove the parameter from the parsed query string object
    delete params[paramToRemove];

    // Return the updated query string
    return $.param(params);
};

export const addKeyFormData = (formData, key, value) => {
    formData.append(key, value);
}

export const deleteKeyFormData = (formData, keyRemove) => {
    const keysToRemove = [];
    for (let [key, value] of formData.entries()) {
        if (key === keyRemove) {
            keysToRemove.push(key);
        }
    }
    // Remove the keys from the FormData object
    keysToRemove.forEach(key => formData.delete(key));
}

export const getValuesOfKeyFormData = (formData, keyToRetrieve) => {
    const values = [];

    // Iterate over the entries of the FormData object
    for (let [key, value] of formData.entries()) {
        if (key === keyToRetrieve) {
            values.push(value);
        }
    }
    return values;
}

export const objectToQueryString = (obj) => {
    const keyValuePairs = [];

    // Helper function to handle value encoding
    function encodeValue(key, value) {
        return `${encodeURIComponent(key)}=${encodeURIComponent(value)}`;
    }

    // Iterate over each key in the object
    for (let key in obj) {
        if (obj.hasOwnProperty(key)) {
            let value = obj[key];

            // Check if the value is an array
            if (Array.isArray(value)) {
                // If it's an array, encode each element without index
                value.forEach(element => {
                    keyValuePairs.push(encodeValue(key, element));
                });
            } else {
                // If it's not an array, encode the key and value normally
                keyValuePairs.push(encodeValue(key, value));
            }
        }
    }

    // Join all key-value pairs with '&' to form the final query string
    return keyValuePairs.join('&');
}

export const convertFormDataToObject = (form) => {
    const formDataArray = $(form).serializeArray();

    const formDataJson = {};
    $.each(formDataArray, function (_, field) {
        if (formDataJson[field.name])
            formDataJson[field.name].push(field.value);
        else
            formDataJson[field.name] = [field.value];
    });
    const result = {};
    for (const key in formDataJson) {
        if (Array.isArray(formDataJson[key]) && formDataJson[key].length === 1) {
            result[key] = formDataJson[key][0];
        } else {
            result[key] = formDataJson[key];
        }
    }
    return result;
}

export const startLoading = () => {
    $.LoadingOverlay("show", {
        image: "",
        fontawesome: "fa fa-spinner fa-spin",
        background: "rgba(0, 0, 0, 0.5)"
    });
}

export const endLoading = () => {
    $.LoadingOverlay("hide");
}

export const http = ({
                         beforeSend,
                         complete,
                         data = null,
                         contentType = 'application/json',
                         dataType = 'json',
                         queryParams = {},
                         pathVariables = {},
                         ...rest
                     }, automaticLoading = true) => {
    return new Promise((resolve, reject) => {
        let finalUrl = rest.url;
        if (pathVariables && typeof pathVariables === 'object') {
            Object.entries(pathVariables).forEach(([key, value]) => {
                finalUrl = finalUrl.replace(`:${key}`, encodeURIComponent(value));
            });
        }
        // Build the query string if queryParams is provided
        const queryString = new URLSearchParams(queryParams).toString();
        // Append query string to the URL if it exists
        if (queryString) {
            finalUrl += `${finalUrl.includes('?') ? '&' : '?'}${queryString}`;
        }

        $.ajax({
            ...rest,
            url: finalUrl, // Use the updated URL with query string
            beforeSend: function (xhr, settings) {
                if (automaticLoading) startLoading();
                if (typeof beforeSend === 'function') {
                    beforeSend.call(this, xhr, settings);
                }
            },
            data: data && contentType === 'application/json' ? JSON.stringify(data) : data,
            dataType,
            contentType,
            success: function (response) {
                // Check if response is null or empty, resolve with a default value
                if (response === null || response === undefined) {
                    resolve(null); // Return null or any default you prefer
                } else {
                    resolve(response); // Return the valid response
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                const statusCode = jqXHR.status;
                if (statusCode === 401) {
                    Swal.fire({
                        icon: "error",
                        title: "Hết phiên làm việc",
                        text: "Vui lòng đăng nhập lại",
                        timer: 5000, // 5 seconds timer
                        timerProgressBar: true,
                        showConfirmButton: true, // Shows the button
                        confirmButtonText: 'Đăng nhập', // Text for the button
                    }).then((result) => {
                        if (result.dismiss === Swal.DismissReason.timer) {
                            window.location.href = "/login";
                        } else {
                            window.location.href = "/login";
                        }
                    });
                    return;
                }
                reject(new Error(`Error: ${textStatus}, ${errorThrown}`));
            },
            complete: function (xhr, status) {
                if (automaticLoading) endLoading();
                if (typeof complete === 'function') {
                    complete.call(xhr, status);
                }
            }
        });
    })
        ;
};


export const formatDate = (dateString) => {
    const d = new Date(dateString);
    const day = String(d.getDate()).padStart(2, '0');
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const year = d.getFullYear();
    return `${day} / ${month} / ${year}`;
}

export const formatDatetime = (dateString) => {
    const date = new Date(dateString);

    // Extract the components
    const hours = String(date.getHours()).padStart(2, "0");
    const minutes = String(date.getMinutes()).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0"); // Months are zero-indexed
    const year = date.getFullYear();

    // Format the date
    const formattedDate = `${hours}:${minutes} ${day}-${month}-${year}`;
    return formattedDate;
}


export const formatCurrency = (value) => {
    return new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(value);
}

export const configSweetAlert2 = {
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#d33",
}

export const formDataToJson = function (formElement, additionalFields = {}) {
    const formData = new FormData(formElement);
    const json = {};

    for (const [key, value] of formData.entries()) {
        const inputElements = formElement.elements[key];

        if (inputElements) {
            if (inputElements.length && inputElements[0].type === 'checkbox') {
                // Handle multiple checkboxes with the same name
                json[key] = Array.from(inputElements)
                    .filter(checkbox => checkbox.checked)
                    .map(checkbox => checkbox.value);
            } else if (inputElements.nodeName === 'SELECT') {
                // Handle <select multiple>
                const selectElement = inputElements;
                if (selectElement.multiple) {
                    json[key] = Array.from(selectElement.selectedOptions).map(opt => opt.value);
                } else {
                    json[key] = value;
                }
            } else if (inputElements.type === 'checkbox' || inputElements.type === 'radio') {
                // Handle single checkbox or radio button
                if (inputElements.type === 'checkbox' && inputElements.checked) {
                    if (!json[key]) json[key] = [];
                    json[key].push(value);
                } else if (inputElements.type === 'radio' && inputElements.checked) {
                    json[key] = value;
                }
            } else {
                // Handle other input types
                if (json[key] === undefined) {
                    json[key] = value; // Single value
                } else if (Array.isArray(json[key])) {
                    json[key].push(value); // Append to array if already an array
                } else {
                    json[key] = [json[key], value]; // Convert to array if duplicate keys
                }
            }
        }
    }

    // Remove empty arrays for unchecked checkboxes
    for (const key in json) {
        if (Array.isArray(json[key]) && json[key].length === 0) {
            delete json[key];
        }
    }

    return {...json, ...additionalFields};
};

export const ORDER_STATUS = {
    PENDING: "Chờ xác nhận",
    PACKAGE: "Chờ đóng gói",
    DELIVERY: "Đang vận chuyển",
    COMPLETED: "Giao hàng thành công ",
    CANCELLED: "Giao hàng thất bại ",
    VERIFYING: "Đang xác nhận",
    CHANGED: "Đơn hàng thay đổi đang chờ xác nhận"
}

export const TRANSACTION_STATUS = {
    UN_PAID: "Chưa thanh toán",
    PROCESSING: "Đang xử lý",
    PAID: "Đã thanh toán",
    CANCELLED: "Đã hủy",
}