import {
    configSweetAlert2,
    endLoading,
    formatCurrency,
    formatDate,
    formDataToJson,
    http, ORDER_STATUS,
    startLoading, TRANSACTION_STATUS
} from "../base.js";
import {getFeeAndLeadTime} from "../shipping.js";
import {alert} from "../sweetAlert.js";

$(document).ready(function () {

        const
            configDatatable = {
                paging: true,
                scrollCollapse: true,
                scrollY: '300px',
                processing: true,
                serverSide: true,
                page: 1,
                pageLength: 8,
                lengthChange: false,
                searching: false,
                ordering: false,
                ajax: {
                    url: "/api/admin/order/datatable",
                    type: "POST",
                    contentType: "application/json",
                    data: function (d) {
                        return JSON.stringify({...d, ...getDataSearch()});
                    },
                }, columns: [
                    {
                        data: "id", render(data) {
                            return `<span class="text-truncate d-block" style="max-width: 50px"  data-bs-toggle="tooltip" data-bs-placement="top" data-bs-title="${data}">${data}</span>`
                        }
                    },
                    {data: "dateOrder"},
                    {data: "fullName"},
                    {
                        data: "paymentMethod",
                    },
                    {
                        data: "transactionStatus",
                        render: function
                            (data) {
                            return `${TRANSACTION_STATUS[data]}`;
                        }
                    },
                    {
                        data: "orderStatus",
                        render: function
                            (data) {
                            return `${ORDER_STATUS[data]}`;
                        }
                    },
                    {
                        data: "id", render:

                            function (data) {
                                return `<button type="button" class="btn btn-info" data-bs-toggle="modal" data-bs-target="#modal-view" data-id="${data}"><i class="fa-solid fa-eye"></i></button>`
                            }
                    }
                    ,
                    {
                        data: "id", render:
                            function (data, type, row, meta) {
                                return `<button type="button" class="btn btn-warning btn-update" 
                                        data-id="${data}" 
                                        data-order-status="${row.orderStatus}"
                                        data-bs-toggle="modal"
                                        data-bs-target="#modal-update">
                                            <i class="fa-solid fa-pen"></i>
                                        </button>`
                            }
                    }
                ],
                language: {
                    url: 'https://cdn.datatables.net/plug-ins/1.11.5/i18n/vi.json'
                }
                ,
                select: {
                    style: 'multi'
                }
                ,
                createdRow: function (row, data, dataIndex) {
                    if (data.orderStatus === "CANCELLED") {
                        $(row).addClass('table-danger');
                    }
                    if (data.orderStatus === "COMPLETED") {
                        $(row).addClass('table-success');
                    }
                    if (data.orderStatus === "VERIFYING") {
                        $(row).addClass('table-warning');
                    }
                    if (data.orderStatus === "CHANGED") {
                        $(row).addClass('table-secondary');
                    }
                }
                ,
                initComplete: function (settings, json) {
                    initEventDatatable();
                    initFormSearch();
                    handleFormSearch();
                    configModalFilter();
                    configModalView();
                    configModalUpdate();
                }
            }
        const table = $("#table");
        const datatable = table.DataTable(configDatatable);
        const formSearch = $("#form-search");
        const createdAt = $("#createAt");
        const modalFilter = $("#modal-filter");
        const modalView = $("#modal-view")
        const tableOrderDetail = $("#table-order-detail tbody");
        const modalUpdate = $("#modal-update")
        const tableUpdateOrderDetail = $("#table-update-order-item tbody");
        const selectUpdateOrderStatus = $("#orderStatus");
        const selectUpdateTransactionStatus = $("#transactionStatus");

        function initEventDatatable() {
            table.find("tbody").on('click', 'button', function (e) {
                e.stopPropagation();
            })
            datatable.on("select", function (e, dt, type, indexes) {
                console.log("selected")
            })
        }

        let startDate = null;
        let endDate = null;

        function initFormSearch() {
            createdAt.daterangepicker({
                opens: 'right',
                autoUpdateInput: false,
                showDropdowns: true,
                startDate: null,
                endDate: null,
                locale: {
                    cancelLabel: 'Hủy ',
                    applyLabel: 'Chọn',
                    daysOfWeek: ['CN', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7'],
                    monthNames: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6',
                        'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
                    firstDay: 1,
                    format: 'DD/MM/YYYY',
                }
            });

            createdAt.on('apply.daterangepicker', function (ev, picker) {
                $(this).val(picker.startDate.format('DD/MM/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));
                startDate = picker.startDate.format('YYYY-MM-DD');
                endDate = picker.endDate.format('YYYY-MM-DD');
            });

            createdAt.on('cancel.daterangepicker', function (ev, picker) {
                $(this).val('');
                startDate = null;
                endDate = null;
            });
        }

        function getDataSearch() {
            const formDataJson = formDataToJson(formSearch[0]);
            formDataJson.startDate = startDate
            formDataJson.endDate = endDate
            if (formDataJson.createdAt)
                delete formDataJson.createdAt;
            if (formDataJson.orderStatus) {
                formDataJson.orderStatus = formDataJson.orderStatus.map(Number);
            }

            return formDataJson
        }

        function handleFormSearch() {
            formSearch.on("submit", function (e) {
                e.preventDefault();

                console.log(getDataSearch())

                datatable.ajax.reload();

                modalFilter.modal("hide");

                Swal.fire({
                    ...configSweetAlert2,
                    icon: 'success',
                    title: "Áp dụng bộ lọc tìm kiếm thành công",
                    showConfirmButton: false,
                    timer: 1500
                });
            });
        }

        function configModalFilter() {
            modalFilter.on("show.bs.modal", function () {
                // if (!objFilter) return;
                // formSearch[0].reset();
                // formSearch.find("#contentSearch").val(objFilter.contentSearch);
                // formSearch.find("#searchSelect").val(objFilter.searchSelect);
                // formSearch.find("#createdAt").val(objFilter.startDate + " - " + objFilter.endDate);
                // objFilter?.paymentMethod?.forEach(function (item) {
                //     $('input[name="paymentMethod"][value="' + item + '"]').prop('checked', true);
                // });
                // objFilter?.orderStatus?.forEach(function (item) {
                //     $('input[name="orderStatus"][value="' + item + '"]').prop('checked', true);
                // });
            }).on("hidden.bs.modal", function () {
            });
        }

        function configModalView() {
            modalView.on("show.bs.modal", function (event) {
                // Button that triggered the modal
                const button = event.relatedTarget
                // Extract info from data-bs-* attributes
                const id = $(button).data("id");
                getDetail(id)
            }).on("hidden.bs.modal", function () {
                clearData();
            });
        }


        function configModalUpdate() {
            modalUpdate.on("show.bs.modal", function (event) {
                const button = event.relatedTarget
                const id = $(button).data("id");
                const orderStatus = $(button).data("order-status");
                modalUpdate.find('input[name="id"]').val(id);
                if (orderStatus === "VERIFYING") {
                    alert("Đơn hàng của người dùng chưa xác nhận chũ ký", "warning");
                    return;
                }

                getStatusOrder(id);
                handleUpdateStatus();
            }).on("hidden.bs.modal", function () {
            });
        }

        function handleUpdateStatus() {
            const validationForm = {
                submitHandler: function (form) {
                    const id = formDataToJson(form).id;
                    const items = getOrderItem();
                    const transactionStatus = selectUpdateTransactionStatus.val();
                    const orderStatus = selectUpdateOrderStatus.val();
                    console.log(id, transactionStatus, orderStatus, items)
                    updateStatus(id, transactionStatus, orderStatus, items);
                    return false;
                }
            }

            $("#form-update-status").validate(validationForm);
        }


        function getDetail(id) {
            http({
                url: '/api/admin/order/:id',
                pathVariables: {
                    id: id
                }
            }).then(function (response) {
                fieldData(response.data);
            })
        }

        function fieldData(order) {
            modalView.find(".fullname").text(order.fullName || "");
            modalView.find(".email").text(order.email || "");
            modalView.find(".phone").text(order.phone || "");
            const address = order.detail + ", " + order.ward + ", " + order.district + ", " + order.province;
            modalView.find(".address").text(address);
            modalView.find(".orderId").text(order.id);
            modalView.find(".createAt").text(order.dateOrder);
            modalView.find(".voucherApply").text(order?.voucherId || "Không sử dụng mã giảm giá");
            modalView.find(".paymentMethod").text(order.paymentMethod);
            modalView.find(".orderStatus").text(ORDER_STATUS[order.orderStatus]);
            modalView.find(".payment-fee").text(formatCurrency(order.fee));
            modalView.find(".transaction").text(TRANSACTION_STATUS[order.transactionStatus]);
            loadListOrderDetail(order.items);
            if (order.orderStatus === ORDER_STATUS[1] || order.status === ORDER_STATUS[2]) return;

            startLoading();
            getFeeAndLeadTime({
                province: order.province,
                district: order.district,
                ward: order.ward,
                detail: order.detail
            }).then(data => {
                modalView.find(".payment-fee").text(formatCurrency(data.feeShipping));
                modalView.find(".lead-date").text(formatDate(data.leadDate));
            }).finally(() => {
                endLoading();
            })
        }

        function clearData() {
            modalView.find(".fullname").text("");
            modalView.find(".email").text("");
            modalView.find(".phone").text("");
            modalView.find(".address").text("");
            modalView.find(".orderId").text("");
            modalView.find(".createAt").text("");
            modalView.find(".voucherApply").text("");
            modalView.find(".paymentMethod").text("");
            modalView.find(".orderStatus").text("");
            modalView.find(".transaction").text("");
            modalView.find(".payment-fee").text("");
            modalView.find(".lead-date").text("");
            tableOrderDetail.html("");
        }

        function loadListOrderDetail(listOrderDetail) {
            const htmls = listOrderDetail.map((item) => (`<tr class="table__row">
                                                                <td class="table__data" style="text-align: left">${item.name}</td>
                                                                <td class="table__data">${item.color}</td>
                                                                <td class="table__data">${item.size}</td>
                                                                <td class="table__data">${item.quantity}</td>
                                                                <td class="table__data">${formatCurrency(item.price)}</td>
                                                            </tr>`))
            tableOrderDetail.html(htmls.join(""));
        }

        // function handleBeforeModalUpdate(id, orderStatus) {
        //     const htmlContent = $("#form-update-status").html();
        //     if (orderStatus === "VERIFYING") {
        //         alert("Đơn hàng của người dùng chưa xác nhận chũ ký", "warning");
        //         return;
        //     }
        //
        //     Swal.fire({
        //         ...configSweetAlert2,
        //         title: 'Cập nhập tình trạng đơn hàng',
        //         icon: "warning",
        //         html: htmlContent,
        //         showCloseButton: true,
        //         showCancelButton: true,
        //         focusConfirm: true,
        //         width: 1000,
        //         confirmButtonText: 'Cập nhập!',
        //         cancelButtonText: 'Đóng',
        //         willOpen: () => {
        //
        //         },
        //         didOpen() {
        //             const modal = $(Swal.getHtmlContainer());
        //             getStatusOrder(modal, id);
        //         },
        //         preConfirm: () => {
        //             const modal = $(Swal.getHtmlContainer());
        //             const orderStatus = updateOrderStatus.val();
        //             const transactionStatus = updateTransactionStatus.val();
        //             const orderDetails = getOrderItem();
        //             return {
        //                 orderStatusId: orderStatus,
        //                 transactionStatusId: transactionStatus,
        //                 items: orderDetails
        //             };
        //         },
        //     }).then(result => {
        //         if (result.isConfirmed) {
        //             const {orderStatus, transactionStatus, items} = result.value;
        //             updateStatus(id, orderStatus, transactionStatus, items);
        //         }
        //     });
        // }

        function getStatusOrder(id) {
            http({
                url: "/api/admin/order/status-target/:orderId",
                type: "GET",
                pathVariables: {
                    orderId: id
                }
            }).then(response => {
                const orderStatusTarget = response.data.orderStatusTarget;
                const transactionStatusTarget = response.data.transactionStatusTarget;
                const orderItems = response.data.items;
                const listAllOrderStatus = [];
                const listAllTransactionStatus = [];

                selectUpdateOrderStatus.find("option").each(function () {
                    const data = {
                        id: $(this).val(),
                        text: $(this).text(),
                        disabled: !orderStatusTarget.includes($(this).val())
                    };
                    listAllOrderStatus.push(data);
                });

                selectUpdateTransactionStatus.find("option").each(function () {
                    const data = {
                        id: $(this).val(),
                        text: $(this).text(),
                        disabled: !transactionStatusTarget.includes($(this).val())
                    }
                    listAllTransactionStatus.push(data);
                });

                selectUpdateOrderStatus.empty();
                selectUpdateTransactionStatus.empty();

                selectUpdateOrderStatus.select2({
                    width: '100%',
                    closeOnSelect: true,
                    minimumResultsForSearch: -1,
                    allowClear: true,
                    placeholder: 'Chọn tình trạng đơn hàng',
                    dropdownParent: modalUpdate,
                    data: listAllOrderStatus,
                }).val(orderStatusTarget.id);

                selectUpdateTransactionStatus.select2({
                    width: '100%',
                    closeOnSelect: true,
                    allowClear: true,
                    minimumResultsForSearch: -1,
                    placeholder: 'Chọn tình trạng giao dịch',
                    dropdownParent: modalUpdate,
                    data: listAllTransactionStatus,
                }).val(transactionStatusTarget.id);

                tableUpdateOrderDetail.empty();
                if (orderItems && orderItems.length > 0) {
                    const html = orderItems.map((item, index) => (
                        `
                    <tr>
                        <th data-id="${item.id}" scope="row">${index + 1}</th>
                        <td>${item.name}</td>
                        <td>
                            <select class="select2-colors">
                                 ${item.colors.map(color => (`<option value="${color.id}" data-code="${color.codeColor}">${color.codeColor}</option>`))}
                            </select>
                        </td>
                        <td>
                              <select class="select2-sizes">
                                 ${item.sizes.map(sizes => (`<option value="${sizes.id}">${sizes.nameSize}</option>`))}
                            </select>
                        </td>
                        <td>
                            <div class="d-flex gap-2 align-items-center" class="quantity">
                                <button type="button" class="btn btn-primary btn-decrease">
                                     <i class="fa-solid fa-minus"></i>
                                </button>
                                <span class="badge bg-secondary fs-5"> ${item.quantity}</span>
                                 <button type="button" class="btn btn-primary btn-increase">
                                    <i class="fa-solid fa-plus"></i>
                                </button>
                            </div>
                        </td>
                        <td>${formatCurrency(item.price)}</td>
                    </tr>
                   `
                    ));
                    tableUpdateOrderDetail.html(html.join(""));

                    handleSelect2();

                    handleQuantity();
                }
            });
        }

        function handleQuantity() {
            tableUpdateOrderDetail.find(".btn-increase").on("click", function () {
                const quantity = $(this).closest("td").find(".badge");
                const currentQuantity = parseInt(quantity.text());
                quantity.text(currentQuantity + 1);
            });

            tableUpdateOrderDetail.find(".btn-decrease").on("click", function () {
                const quantity = $(this).closest("td").find(".badge");
                const currentQuantity = parseInt(quantity.text());
                if (currentQuantity > 1) {
                    quantity.text(currentQuantity - 1);
                }
            });
        }

        function getOrderItem() {
            const result = [];
            tableUpdateOrderDetail.find("tr").each(function (index, element) {
                const orderDetail = $(element);
                const id = orderDetail.find("th:nth-child(1)").data("id");
                const name = orderDetail.find("td:nth-child(2)").text();
                const colorId = orderDetail.find("td:nth-child(3) select").val();
                const sizeId = orderDetail.find("td:nth-child(4) select").val();
                const quantity = orderDetail.find("td:nth-child(5)").text();
                const price = orderDetail.find("td:nth-child(6)").text();
                result.push({id, name, colorId, sizeId, quantity, price});
            });
            return result;
        }

        function handleSelect2() {
            tableUpdateOrderDetail.find('.select2-sizes').select2({
                width: '100%',
                closeOnSelect: true,
                minimumResultsForSearch: -1,
                placeholder: 'Chọn size muốn thay đổi',
                dropdownParent: modalUpdate,
            });

            tableUpdateOrderDetail.find('.select2-colors').select2({
                width: '100%',
                closeOnSelect: true,
                minimumResultsForSearch: -1,
                placeholder: 'Chọn màu sắc muốn thay đổi',
                dropdownParent: modalUpdate,
                templateResult: formatOption,
                templateSelection: formatSelection,
            });

            function formatOption(option) {
                if (!option.id) {
                    return option.text;
                }

                const color = $(option.element).data("code");
                return $(
                    `<span style="background-color:${color} " ">${option.text}</span>`
                );
            }

            function formatSelection(option) {
                const color = $(option.element).data("code");
                return $(
                    `<span style="background-color:${color} " ">${option.text}</span>`
                );
            }
        }

        function updateStatus(id, transactionStatus, orderStatus, items) {
            http({
                url: "/api/admin/order/status-target/:orderId",
                type: "PUT",
                pathVariables: {
                    orderId: id
                },
                data: {
                    transactionStatus: transactionStatus,
                    orderStatus: orderStatus,
                    items: items,
                }
            }).then(response => {
                if (response.code === 200)
                    Swal.fire({
                        ...configSweetAlert2,
                        title: 'Cập nhập trạng thái thành công!',
                        icon: 'success',
                        showCloseButton: true,
                    }).then(() => {
                        datatable.ajax.reload();
                    });
                else
                    Swal.fire({
                        ...configSweetAlert2,
                        title: 'Cập nhập trạng thất bại!',
                        text: "Ràng buộc thay đổi không hợp lệ",
                        icon: 'error',
                        showCloseButton: true,
                    });
            }).catch(error => {
                Swal.fire({
                    ...configSweetAlert2,
                    title: 'Cập nhập trạng thất bại!',
                    text: "Ràng buộc thay đổi không hợp lệ",
                    icon: 'error',
                    showCloseButton: true,
                });
            });
        }

        $('[data-bs-toggle="tooltip"]').tooltip();
    }
)
;