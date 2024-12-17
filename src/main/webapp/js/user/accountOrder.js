import {getFeeAndLeadTime} from "../shipping.js";
import {http, ORDER_STATUS} from "../base.js";

$(document).ready(function () {
    // Lấy ra trạng thái đơn hàng chưa xác nhận khi mới vào trang
    let status = ORDER_STATUS["VERIFYING"];
    const modal = $('#modal');

    getOrders(status);

    $('.list-group-item-action').click(function () {
        $('.list-group-item-action').removeClass('active');
        $(this).addClass('active');
        status = $(this).data('status');
        getOrders(status);
    });

    function getOrders(status) {
        http({
            url: '/api/user/order/:statusId',
            type: 'GET',
            pathVariables: {
                statusId: status,
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
    }


    function loadDataModal(order, orderId) {
        function loadAddress(address) {
            modal.find("#order__province").text(address.province);
            modal.find("#order__district").text(address.district);
            modal.find("#order__ward").text(address.ward);
            modal.find("#order__detail").text(address.detail);
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
        $('.btn__order-detail').click(function () {
            const orderId = $(this).data('id');
            http({
                url: "/api/user/order/detail/:orderId",
                pathVariables: {
                    orderId: orderId
                },
                type: 'GET',
            }).then((response) => {
                const success = response.code === 200;
                if (success) {
                    const data = response.data;
                    loadDataModal(data, orderId);
                } else {

                }
            })
        })
    }

    function formatCurrency(value) {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND',
        }).format(value);
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

    $("#btn-verify").on("click", handleVerifyButton);

});