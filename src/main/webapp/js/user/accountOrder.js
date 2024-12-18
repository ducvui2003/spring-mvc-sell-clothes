import {getFeeAndLeadTime} from "../shipping.js";
import {formatCurrency, http, ORDER_STATUS} from "../base.js";
// Lấy ra trạng thái đơn hàng chưa xác nhận khi mới vào trang
let status = "VERIFYING";
const modal = $('#modal');
const modalChangeOrder = $('#modal-change-order');
const btnVerify = $('#btn-verify');
const btnAddress = $('#btn-change-address');
const verifyBlock = $('#verify-block');
const btnAddressSubmit = $('#btn-address-submit');
const tableAddress = $('#addressList tbody');

$(document).ready(function () {

    getOrders(status);

    $('.list-group-item-action').click(function () {
        $('.list-group-item-action').removeClass('active');
        $(this).addClass('active');
        status = $(this).data('status');
        getOrders(status);
    });


    btnVerify.on("click", handleVerifyButton);

    handleLoadAddress();

    handleSubmitModelChangeOrder();

});

function handleVerifyButton(event) {
    var order_id = $('span#order__id').html();
    var file = $('input#upload-sign-info').val();
    var formData = new FormData();
    formData.append('signed', file);
    formData.append('uuid', order_id);
    $.ajax({
        url: '/api/verify-order/upload',
        type: 'POST',
        data: formData,
        contentType: false,
        processData: false,
        success: function (data) {
            console.log("success")
            if (data.data) {
                Swal.fire({
                    icon: 'success',
                    title: 'Xác thực thành công',
                    showConfirmButton: false,
                    target: document.querySelector("#modal"),
                    timer: 1500
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Thông tin đơn hàng không đúng vui lòng kiểm tra lại',
                    showConfirmButton: false,
                    target: document.querySelector("#modal"),
                    timer: 1500
                });
            }
        }
    });
}

function getOrders(status) {
    http({
        url: '/api/user/order/:status',
        type: 'GET',
        pathVariables: {
            status: status,
        },
    }).then(function (response) {
        loadDataToTable(response.data)
    });
}

function loadDataToTable(data) {
    const table = $('#orderList tbody');
    table.empty();
    if (!data) {
        return;
    }
    const htmls = data.map(function (order) {
        return `<tr>
                        <td>${order.id}</td>
                        <td>${order.dateOrder}</td>
                        <td>${order.quantity}</td>
                        <td><button class="btn btn-primary btn__order-detail" data-id="${order.id}" data-bs-toggle="modal" data-bs-target="#modal">Xem chi tiet</button></td>
                    </tr>`
    })
    table.html(htmls.join(''));
    addEventViewDetail();
    displayBtnAddress(status);
    displayVerifyBlock(status);
}

function loadDataModal(order, orderId) {
    function loadAddress(address) {
        modal.find("#order__province").text(address.province);
        modal.find("#order__district").text(address.district);
        modal.find("#order__ward").text(address.ward);
        modal.find("#order__detail").text(address.detail);
        modal.find("#btn-change-address").attr('data-address', address);
    }

    function loadPrice(order) {
        const temporary = order.items.reduce(function (total, item) {
            return total + item.price * item.quantity;
        }, 0);
        modal.find("#order__temporary").text(formatCurrency(temporary));

        getFeeAndLeadTime(address).then(data => {
            modal.find("#order__shipping-fee").text(formatCurrency(data.feeShipping));
            modal.find("#order__lead-date").text(convertUnixToDate(data.leadDate));
            modal.find("#order__total").text(formatCurrency(temporary + data.feeShipping));
        })
    }

    function loadContact(order) {
        modal.find("#order__name").text(order.fullName ?? "");
        modal.find("#order__phone").text(order.phone ?? "");
        modal.find("#order__email").text(order.email ?? "");
    }

    const htmls = order.items.map(function (item) {
        return `<div class="order__item align-items-center mt-2 row">
                        <div class="item__thumbnail col-2">
                            <img src="${item.thumbnail}" class="rounded border img-thumbnail object-fit-cover" style="width: 100px; height: 100px" alt="...">
                        </div>
                        <div class="item__detail col-8">
                            <h3 class="h4 item__name pb-1s fw-4">${item.name}</h3>
                            <div class="d-flex pt-2 fw-6 align-items-center">
                                <span class="item__color d-block border border-black border-3 rounded" style="background: ${item.color}; width:20px; height: 20px"></span> 
                                <span class="vr mx-2"></span>
                                <span class="item__size text-uppercase">${item.size}</span>
                            </div>
                        </div>
                        <div class="col-2">
                            <p class="item__price pb-2 fs-5 fw-bold">Giá: <span>${formatCurrency(item.price)}</span></p>
                            <p class="item__quantity pt-2 fs-6">Số lượng: <span>${item.quantity}</span> </p>
                        </div>
                    </div>`;
    });

    modal.find("#order__id").text(orderId);
    modal.find("#order__date").text(order.dateOrder);
    modal.find("#order__status").text(order.status ?? 'Chưa xác nhận');
    modal.find("#order__list").html(htmls.join(''));

    const address = {
        province: order.province,
        district: order.district,
        ward: order.ward,
        detail: order.detail,
    }

    loadAddress(address)
    loadContact(order);
    loadPrice(order);
}

function addEventViewDetail() {
    modal.on('show.bs.modal', function (event) {
        const btn = $(event.relatedTarget);
        const orderId = btn.data('id');
        http({
            url: "/api/user/order/detail/:orderId",
            pathVariables: {
                orderId: orderId
            },
            type: 'GET',
        }).then((response) => {
            if (response.code === 200) {
                const data = response.data;
                loadDataModal(data, orderId);
                modalChangeOrder.find("[data-bs-target='#modal']").attr('data-id', orderId);
            } else {
                modal.modal('hide');
                Swal.fire({
                    icon: 'error',
                    title: 'Đã có lỗi xảy ra',
                    showConfirmButton: false,
                    timer: 1500
                })
            }
        })
    });
}

function convertUnixToDate(timestamp) {
    const milliseconds = timestamp * 1000;
    const date = new Date();
    date.setTime(milliseconds);

    const day = date.getDate();
    const month = date.getMonth() + 1;
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
}

function displayBtnAddress(status) {
    if (status === "VERIFYING" || status === "PENDING" || status === "PACKAGE") {
        btnAddress.show();
    } else {
        btnAddress.hide();
    }
}

function displayVerifyBlock(status) {
    if (status === "VERIFYING") {
        verifyBlock.show();
    } else {
        verifyBlock.hide();
    }
}

function handleLoadAddress() {
    modalChangeOrder.on('show.bs.modal', function (event) {
        // Lấy danh sách địa chỉ
        http({
            url: "/api/user/address",
            type: 'GET',
        }).then((response) => {
            const addressList = response.data;
            if (addressList && addressList.length > 0) {
                loadDataToModalChangeOrder(addressList);
            }
        });
    });
}

// Load danh sách địa chỉ vào UI
function loadDataToModalChangeOrder(addressList) {
    tableAddress.empty();
    const htmls = addressList.map(function (address) {
        if (address) {
            return `<tr data-id="${address.id}">
                        <td>${address.id}</td>
                        <td>${address.provinceName}</td>
                        <td>${address.districtName}</td>
                        <td>${address.wardName}</td>
                        <td>${address.detail}</td>
                    </tr>`
        }
        return '';
    })
    tableAddress.html(htmls.join(''));
    tableAddress.find('tr').on('click', function () {

        tableAddress.find('tr').removeClass('table-active table-primary');

        $(this).addClass('table-primary');
    });
}

function handleSubmitModelChangeOrder() {
    btnAddressSubmit.on('click', function () {
        const addressId = tableAddress.find('tr.table-primary').data('id');
        const orderId = modalChangeOrder.find("[data-bs-target='#modal']").data('id');
        if (addressId) {
            http({
                url: "/api/user/order/address/:orderId",
                type: 'PUT',
                pathVariables: {
                    orderId: orderId
                },
                data: {
                    addressId: addressId
                }
            }).then((response) => {
                if (response.code === 200) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Đã thay đổi địa chỉ cho đơn hàng thành công',
                        showConfirmButton: false,
                        timer: 1500
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Thay đổi địa chỉ cho đơn hàng không thành công',
                        showConfirmButton: false,
                        timer: 1500
                    });
                }
            });
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Vui lòng chọn địa chỉ',
                showConfirmButton: false,
                timer: 1500
            });
        }
    });
}