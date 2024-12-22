import {configSweetAlert2, formatCurrency, formatDate, http} from "./base.js";

$(document).ready(function () {
    preFormatCurrency();
    var voucherApply = {
        state: undefined,
        voucher: undefined,
        listIdProduct: undefined,
    };

    // Các trạng thái của voucher
    const voucherState = [
        {
            state: 1,
            className: "success",
            message: "Áp dụng mã giảm giá thành công",
        }, {
            state: 2,
            className: "warning",
            message: "Mã giảm giá không tìm thấy",
        }, {
            state: 3,
            className: "info",
            message: "Hết lượt sử dụng mã giảm giá",
        }, {
            state: 4,
            className: "info",
            message: "Mã giảm giá đã hết hạn",
        }, {
            state: 5,
            className: "warning",
            message: "Mã giảm giá không áp dụng cho đơn hàng này",
        },
        {
            state: 6,
            className: "warning",
            message: "Số tiền đơn hàng không đủ để áp dụng mã giảm giá",
        },
    ];

    init();

    function init() {
        handleCheckBox();
        increaseQuantityCartProduct();
        decreaseQuantityCartProduct();
        deleteCartProduct();
        applyCodeVoucher();

        // Chuyển tiếp đến trang thanh toán
        handlePay();
    }

    function handlePay() {
        const form = $('#form__checkout');
        form.validate({
            submitHandler: function (form) {
                const checks = $('.check__pay')
                let checked = false
                checks.each(function () {
                    const check = $(this)
                    const parent = $(this).parents("tr")
                    if (check.prop('checked')) {
                        if (!checked)
                            checked = true
                        let obj = {
                            id: check.val(),
                            ind: parent.data("cartProductIndex"),
                            name: parent.find("a.product__name").text().trim(),
                            color: parent.find("p.order__color").text().trim(),
                            size: parent.find("p.order__size--specification").text().trim(),
                            count: parent.find("input.quality__required").val(),
                            price: parent.find("td.subtotal__item").text().replace('₫', '').replace('.', '').trim(),
                            voucher: voucherApply.voucher
                        }
                    }
                })
                if (checked) {
                    return true
                } else {
                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: "Vui lòng lựa chọn món hàng muốn thanh toán",
                    });
                    return false;
                }
            }
        })
        // form.on('submit', function (event) {
        //     event.preventDefault()
        //
        //     let checked = false
        //     let data = []
        //     const checks = $('.check__pay')
        //     checks.each(function () {
        //         const check = $(this)
        //         const parent = $(this).parents("tr")
        //         if (check.prop('checked')) {
        //             if (!checked)
        //                 checked = true
        //             let obj = {
        //                 id: check.val(),
        //                 ind: parent.data("cartProductIndex"),
        //                 name: parent.find("a.product__name").text().trim(),
        //                 color: parent.find("p.order__color").text().trim(),
        //                 size: parent.find("p.order__size--specification").text().trim(),
        //                 count: parent.find("input.quality__required").val(),
        //                 price: parent.find("td.subtotal__item").text().replace('₫', '').replace('.', '').trim(),
        //                 voucher: voucherApply.voucher
        //             }
        //             data.push({
        //                 name: check.attr('name'),
        //                 value: JSON.stringify(obj),
        //             });
        //         }
        //     })
        //
        //     if (checked) {
        //         // Đã lựa chọn hàng
        //         let formData = $.param(data);
        //         let url = this.href
        //         form.submit()
        //         // $.ajax({
        //         //     url: url,
        //         //     data: formData,
        //         //     processData: false,
        //         //     contentType: false,
        //         //     success: (data) => {
        //         //         // Ghi lại toàn bộ nội dung của document
        //         //         document.open();
        //         //         document.write(data);
        //         //         history.pushState(null, null, url);
        //         //         document.close();
        //         //     },
        //         //     error: (err) => {
        //         //         console.log(err)
        //         //     }
        //         // })
        //     } else {
        //         Swal.fire({
        //             icon: "error",
        //             title: "Oops...",
        //             text: "Vui lòng lựa chọn món hàng muốn thanh toán",
        //         });
        //     }
        //
        // })
    }

    function handleCheckBox() {
        // Chọn tất cả
        $("#check__pay-all").on("click", function () {
            $(".check__pay").prop("checked", true);
            updateTotalPrice();
        });

        // Bỏ tất cả
        $("#remove__pay-all").on("click", function () {
            $(".check__pay").prop("checked", false);
            updateTotalPrice();
        })

        // Checkbox từng sản phẩm
        $('.container__check__pay').on('click', function () {
            const checkbox = $(this).find('.check__pay');
            let isClick = checkbox.prop('checked');
            checkbox.prop('checked', !isClick);
            updateTotalPrice();
        });
        $('.check__pay').on('click', function () {
            let isClick = $(this).prop('checked');
            $(this).prop('checked', !isClick);
            updateTotalPrice();
        })

    }

    // Thực thi tăng số lương
    function increaseQuantityCartProduct() {
        $('.plus__quality').on('click', function (event) {
            event.preventDefault();
            let cartItem = $(this).closest('.cart__item');
            let cartItemId = cartItem.data("card-item-id");
            let quantityElement = $(cartItem).find('.quality__swapper .quality__required');
            let quantity = parseInt(quantityElement.val());
            http({
                url: `/api/cart/increase/${cartItemId}`,
                type: 'POST',
            }).then((response => {
                quantityElement.val(quantity + 1);
                updatePriceItem(cartItem);
                updateTotalPrice()
            }));
        })
    }

    // Thực thi giảm số lương
    function decreaseQuantityCartProduct() {
        $('.minus__quality').on('click', function (event) {
            event.preventDefault();
            let cartItem = $(this).closest('.cart__item');
            let cartItemId = cartItem.data("card-item-id");
            let quantityElement = $(cartItem).find('.quality__swapper .quality__required');
            let quantity = parseInt(quantityElement.val());
            if (quantity === 1) {
                Swal.fire({
                    icon: "warning",
                    title: "Cảnh bảo",
                    text: "Số lượng sản phẩm tối thiểu là 1",
                });
                return;
            }

            http({
                url: `/api/cart/decrease/${cartItemId}`,
                type: 'POST'
            }).then(response => {
                quantityElement.val(quantity - 1);
                updatePriceItem(cartItem);
                updateTotalPrice();
            });
        })
    }

    // Thực thi xóa sản phẩm
    function deleteCartProduct() {
        $('.remove__item').on('click', function (event) {
            event.preventDefault();
            Swal.fire({
                ...configSweetAlert2,
                icon: "info",
                title: "Bạn có muốn xóa sản phẩm này ra khỏi giỏ hàng không?",
                showDenyButton: true,
                confirmButtonText: "Có",
                denyButtonText: `Không`
            }).then(result => {
                if (result.isConfirmed) {
                    let cartItem = $(this).closest('.cart__item');
                    let cartItemId = cartItem.data("card-item-id");
                    http({
                        url: `/api/cart/delete/${cartItemId}`,
                        type: 'DELETE',
                    }).then(() => {
                        cartItem.remove();
                        updateTotalPrice();
                    }).catch(err => {
                        console.log(err)
                    })
                }
            })
        })
    }


    function applyCodeVoucher() {
        const promotionForm = $("#promotion__form");
        const submitPromotion = promotionForm.find("#promotion_submit")
        submitPromotion.on('click', function (event) {
            event.preventDefault();
            // Không có sản phẩm chọn để áp dụng mã giảm giá
            const totalItem = $(".cart__item:has(input.check__pay:checked)").length;
            if (totalItem == 0) {
                Swal.fire({
                    icon: "info",
                    title: "Oops...",
                    text: "Vui lòng chọn sản phẩm để áp dụng",
                });
                return;
            }
            const promotionCode = promotionForm.find('#promotion__code')
            if (promotionCode.val().trim() === "") {
                Swal.fire({
                    icon: "info",
                    title: "Oops...",
                    text: "Vui lòng nhập mã giảm giá",
                });
                return;
            }
            http({
                url: promotionForm.attr('action'),
                type: promotionForm.attr('method'),
                data: {
                    code: promotionCode.val(),
                    id: getProductListId(),
                },
                dataType: 'json',
            }).then((response => {
                if (response.code != 200) {
                    Swal.fire({
                        icon: "error",
                        title: "Có lỗi khi áp dụng mã giảm giá",
                        text: "Vui lòng thử lại sau"
                    })
                    return;
                }
                const state = getVoucherState(response.result.state);
                voucherApply = {
                    state: state,
                    voucher: response.result.voucher,
                    listIdProduct: response.result.listIdProduct,// Lưu lại danh sách id sản phẩm có thể áp dụng voucher
                }
                Swal.fire({
                    icon: state.className,
                    title: "Thông báo",
                    text: state.message,
                });

                updateTotalPrice();
            }));
        })
    }


    // Cập nhập giá khi tăng, giảm, xóa sản phẩm, khi áp dụng voucher
    function updateTotalPrice() {
        const cartItemElements = document.querySelectorAll(".cart__item:has(input.check__pay:checked)");
        const totalItem = cartItemElements.length;
        //Tổng tiền
        const totalPrice = [...cartItemElements].map((item) => {
            const quantityProduct = $(item).find(".quality__required").val();
            const priceUnit = $(item).find(".unit__price").data("price");
            return quantityProduct * priceUnit;
        }).reduce((acc, cur) => acc + cur, 0);
        const priceVoucher = voucherApply.voucher?.discountPercent ? voucherApply.voucher.discountPercent * totalPrice : 0;
        const finalPrice = totalPrice - priceVoucher;
        $("#total__items").text(totalItem);
        $("#price__total").text(formatCurrency(totalPrice));
        $("#price__voucher").text(priceVoucher ? formatCurrency(priceVoucher) : "");
        $("#price__final").text(formatCurrency(finalPrice));
    }


    function getProductListId() {
        const selectorCartItems = "[data-product-id]:has(input.check__pay:checked)";
        return Array.from(document.querySelectorAll(selectorCartItems)).map(productItem => productItem.getAttribute("data-product-id"));
    }


    function preFormatCurrency() {
        const cartItemElements = $(".cart__item");
        $.each(cartItemElements, function (index, cartItem) {
            updatePriceItem(cartItem);
        });
    }

    function updatePriceItem(cartItem) {
        const quantityProduct = $(cartItem).find(".quality__required").val();
        const priceUnit = $(cartItem).find(".unit__price").data("price");
        $(cartItem).find(".unit__price").text(formatCurrency(priceUnit));
        $(cartItem).find(".subtotal__item").text(formatCurrency(quantityProduct * priceUnit));
    }
})