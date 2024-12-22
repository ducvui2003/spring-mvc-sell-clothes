import {formatCurrency, formatDate, formDataToJson, http} from "./base.js";
import {getFeeAndLeadTimeById} from "./shipping.js";

$(document).ready(() => {
    const form = $("#form")
    // form.on("submit", (e) => {
    //     e.preventDefault()
    // })
    const formValidationConfig = {
        rules: {
            addressId: {
                required: true,
            },
            paymentMethodId: {
                required: true,
            },
            fullName: {
                required: true,
            },
            email: {
                required: true,
            },
            phone: {
                required: true,
            },
        },
        messages: {
            addressId: {
                required: "Vui lòng chọn địa chỉ giao hàng",
            },
            paymentMethodId: {
                required: "Vui lòng chọn phương thức thanh toán",
            },
            fullName: {
                required: "Vui lòng nhập họ tên",
            },
            email: {
                required: "Vui lòng nhập email",
            },
            phone: {
                required: "Vui lòng nhập số điện thoại",
            },
        },
        invalidHandler: function (event, validator) {
            // Hiện notify form ko hợp lệ
            console.log(validator)
        },
        submitHandler: function (form, {}) {
            const data = formDataToJson(form)
            data.addressId = Number(data.addressId)
            data.cartItemId = data.cartItemId.map(Number)
            Swal.fire({
                title: "Bạn có đã chắc chắn với các thông tin đơn hàng cung cấp?",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                denyButtonColor: "#d33",
                showDenyButton: true,
                denyButtonText: "Kiểm tra lại thông tin",
                confirmButtonText: "Tiến hành đặt hàng"
            }).then((result) => {
                if (result.isConfirmed) {
                    if (data.paymentMethod === "COD")
                        http({
                            url: "/api/checkout",
                            method: "POST",
                            data: data,
                        }).then((response) => {
                            Swal.fire({
                                icon: 'success',
                                title: 'Đặt hàng thành công!',
                                text: "Chúng tôi sẽ liên hệ với bạn trong thời gian sớm nhất, vui lòng kiểm tra đơn hàng có mã " + response.data,
                                timer: 1500,
                            }).then(() => {
                                window.location.href = "/"
                            })
                        }).catch((error) => {
                            console.log(error)
                            Swal.fire({
                                icon: 'error',
                                title: 'Đặt hàng thất bại!',
                            })
                        });
                    else if (data.paymentMethod === "VNPAY")
                        http({
                            url: "/api/checkout/vn-pay",
                            method: "POST",
                            data: data,
                        }).then((response) => {
                            if (response.code === 200) {
                                window.location.href = response.data
                            }
                        }).catch((error) => {
                            console.log(error)
                            Swal.fire({
                                icon: 'error',
                                title: 'Đặt hàng thất bại!',
                            })
                        });

                }
            });

            // Ngăn việc reload page khi submit form
            return false;
            // http({}).then((response) => {
            //     Swal.fire({
            //         title: "Bạn có muốn sử dụng khóa hiện tại của bạn không? ",
            //         text: "Một khi đã chọn khóa, bạn không thể thay đổi.",
            //         icon: "warning",
            //         showCancelButton: true,
            //         confirmButtonColor: "#3085d6",
            //         cancelButtonColor: "#d33",
            //         denyButtonColor: "#d33",
            //         showDenyButton: true,
            //         denyButtonText: "Sử dụng khóa mới",
            //         confirmButtonText: "Sử dụng khóa hiện tại"
            //     }).then((result) => {
            //         if (result.isConfirmed) {
            //             Swal.fire({
            //                 title: "Deleted!",
            //                 text: "Your file has been deleted.",
            //                 icon: "success"
            //             });
            //
            //
            //             // http({
            //             //     url:"/api/checkout",
            //             //     method: "POST",
            //             //     data: jsonData,
            //             // }).then();
            //         }
            //     });
            // });


        }
    }
    const feeShipping = $("#feeShipping")
    const leadDate = $("#leadDate")

    init();

    function init() {
        form.validate(formValidationConfig)
        handleAddressChange();
    }

    function handleAddressChange() {

        handleAddress($("input[name='addressId']:checked"))

        $("input[name='addressId']").each((index, element) => {
            $(element).on("click", () => handleAddress(element)
            )
        })
    }

    function handleAddress(addressElement) {
        const provinceId = $(addressElement).data("province")
        const districtId = $(addressElement).data("district")
        const wardCode = $(addressElement).data("ward")
        getFeeAndLeadTimeById({
            provinceId, districtId, wardCode
        }).then((response) => {
            feeShipping.text(formatCurrency(response.feeShipping))
            leadDate.text(formatDate(response.leadDate))
        })
    }
});