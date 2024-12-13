<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="/WEB-INF/view/common/commonLink.jsp"/>
    <link rel="stylesheet" href="<c:url value="/assets/css/checkout.css" />">
    <title>Thanh toán</title>
</head>

<body>
<jsp:include page="/WEB-INF/view/common/header.jsp"/>
<main id="main">
    <div class="container-xl">
        <h1 class="checkout__title">Thanh toán</h1>
        <form:form class="checkout__container row" id="form"  modelAttribute="checkout">
            <div class="col-6">
                <div class="delivery__info--container">
                    <h2 class="checkout__subtitle">Thông tin giao hàng</h2>
                    <div class="row" id="address-list">
                        <c:if test="${not empty requestScope.addresses}">
                            <c:forEach items="${requestScope.addresses}" var="address" varStatus="status">
                                <div class="col-sm-12 mb-3">
                                    <div class="card">
                                        <label class="card-body focus__address <c:if test="${status.first}">selected</c:if>"
                                               onclick="selectCard(this)" style="cursor: pointer"
                                        >
                                            <span class=" card-title fs-5 fs-bold">Địa chỉ giao hàng</span>
                                            <span class="card-tex d-block mt-2">${address.detail}, ${address.wardName}, ${address.districtName}, ${address.provinceName}</span>
                                            <input type="radio" name="addressId" ${status.first ? 'checked' : ''}
                                                   value="${address.id}" hidden="hidden"
                                                   data-province="${address.provinceId}"
                                                   data-district="${address.districtId}"
                                                   data-ward="${address.wardId}"
                                            />
                                        </label>

                                    </div>
                                </div>
                            </c:forEach>
                        </c:if>
                    </div>
                    <p class="other__info">Bạn muốn giao hàng đến địa chỉ khác?
                        <span> <a class="add__delivery" href="/user/info">Thêm thông tin giao hàng mới</a></span>
                    </p>
                </div>

                <div class="delivery__method--container">
                    <h2 class="checkout__subtitle">Phương thức vận chuyển</h2>
                    <div id="delivery__method--form" class="radio__section">
                        <div class="method__content">
                            <div class="method__item section__info--selection">
                                <label class="label__selection">
                                    <span>Giao hàng nhanh</span>
                                    <span id="feeShipping">
                                       ...
                                        </span>
                                    <span id="leadDate">
                                    ...
                                        </span>
                                </label>
                            </div>
                            <p class="description__method">
                                <span>Có hàng trong </span>
                            </p>
                        </div>
                    </div>
                </div>
                <div class="payment__method--container">
                    <h2 class="checkout__subtitle">Phương thức thanh toán</h2>
                    <div id="payment__method--form" class="radio__section">
                        <c:forEach items="${applicationScope.listPaymentMethod}" var="paymentMethod">
                            <div class="method__content">
                                <div class="method__item section__info--selection">
                                    <form:radiobutton
                                            path="paymentMethodId"
                                            name="payment__method" class="radio__button"
                                            value="${paymentMethod.id}"
                                            id="payment__method${paymentMethod.id}"/>
                                    <label class="label__selection"
                                           for="payment__method${paymentMethod.id}">${paymentMethod.typePayment}</label>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
            <div class="col-6">
                <div class="row">
                    <div id="customize__info--form">
                        <div class="customize__item">
                            <label for="fullName" class="input__text">Họ và tên
                                <span class="compulsory">*</span></label>
                            <form:input path="fullName" type="text" name="fullName"
                                        class="input__content field__content"
                                        id="fullName" placeholder="Họ và tên của bạn"/>
                            <span id="fullNameError" class="error__notice"></span>
                        </div>
                        <div class="customize__item">
                            <label for="email" class="input__text">Email
                                <span class="compulsory">*</span></label>
                            <form:input path="email" type="text" name="email" class="input__content field__content"
                                        id="email"
                                        placeholder="Email của bạn"/>
                            <span id="emailError" class="error__notice"></span>
                        </div>
                        <div class="customize__item">
                            <label for="phone" class="input__text">Số điện thoại
                                <span class="compulsory">*</span></label>
                            <form:input path="phone" type="text" name="phone" class="input__content field__content"
                                        id="phone"
                                        placeholder="Số điện thoại của bạn"/>
                            <span id="phoneError" class="error__notice"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <span class="summary__cart">Sản phẩm</span>
                    <div class="order__detail--info">
                        <table class="order__table">
                            <thead>
                            <tr class="row__header">
                                <th class="thead__item">Sản phầm</th>
                                <th class="thead__item">Số lượng</th>
                                <th class="thead__item">Đơn giá</th>
                            </tr>
                            </thead>
                            <tbody class="order__list">

                            <c:forEach var="item" items="${requestScope.cartItems}">
                                <input type="checkbox" name="cartItemId" hidden="hidden" value="${item.id}" checked/>
                            </c:forEach>
                            <c:forEach var="item" items="${requestScope.cartItems}">
                                <tr class="row__content">
                                    <td class="td__item">
                                        <div class="product__item">
                                            <img src="${item.thumbnail}" alt="${item.name}"/>
                                            <div class="order__product--info">
                                                <p class="product__name">${item.name}</p>
                                                <span>
                                                Màu sắc:
                                                <p class="order__color d-inline">${item.color}</p>
                                            </span>
                                                <p class="order__size">${item.size}</p>
                                            </div>
                                        </div>
                                    </td>
                                    <td class="td__item">${item.quantity}</td>
                                    <td class="td__item">${item.salePrice !=0 ? item.salePrice : item.price}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="invoice--final">
                        <div class="invoice__content">
                            <div class="price__item--detail">
                                <div class="temporary__container">
                                    <span>Tạm tính ( <span class="count__product"></span> sản phẩm)</span>
                                        <%--                                <span>${sessionScope[userIdCart].temporaryPriceFormat()}</span>--%>
                                </div>
                                    <%--                            <c:if test="${sessionScope[userIdCart].voucherApplied != null}">--%>
                                    <%--                                <div class="discount__container">--%>
                                    <%--                                    <span>Giảm giá</span>--%>
                                    <%--                                    <span>${sessionScope[userIdCart].discountPriceFormat()}</span>--%>
                                    <%--                                </div>--%>
                                    <%--                            </c:if>--%>
                                    <%--                            <c:if test="${sessionScope[userIdCart].deliveryMethod != null}">--%>
                                    <%--                                <div class="shipping__container">--%>
                                    <%--                                    <span>Phí vận chuyển</span>--%>
                                    <%--                                    <span>--%>
                                    <%--                                        <fmt:setLocale value="vi_VN"/><fmt:formatNumber type="currency"/>--%>
                                    <%--                                    </span>--%>
                                    <%--                                </div>--%>
                                    <%--                            </c:if>--%>
                            </div>
                            <div class="total__price--final">
                                <span class="total__label">Tổng tiền</span>
                                <span class="total__value"></span>
                            </div>
                        </div>
                        <div class="ground__button--forward">
                            <button type="submit" class="place__order">Đặt hàng</button>
                            <a href="<c:url value="/cart" />" class="back--shopping__cart">
                                Quay lại giỏ hàng
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </div>
</main>
<div class="popup__deletion"></div>
<jsp:include page="/WEB-INF/view/common/footer.jsp"/>
</body>
<script type="module" src="<c:url value="/js/checkout.js"/>"></script>
<script type="text/javascript">
    function selectCard(card) {
        // Xóa lớp 'selected' khỏi tất cả các thẻ
        var cards = document.querySelectorAll('.focus__address');
        cards.forEach(function (card) {
            card.classList.remove('selected');
        });

        // Thêm lớp 'selected' vào thẻ đã được click
        card.classList.add('selected');
    }

    <%--    function updateCheckout(orders) {--%>
    <%--        const con = $('.order__list')--%>
    <%--        const conCount = $('.count__product')--%>
    <%--        const totalPrice = $('.total__value')--%>
    <%--        let totalCost = 0--%>
    <%--        conCount.text(orders.order.length + '')--%>
    <%--        con.html('')--%>
    <%--        orders.order.forEach(order => {--%>
    <%--            let content = `--%>
    <%--            <tr class="row__content">--%>
    <%--                            <td class="td__item">--%>
    <%--                                <div class="product__item">--%>
    <%--                                    <div class="order__product--info">--%>
    <%--                                        <p class="product__name">` + order.name + `</p>--%>
    <%--                                        <span>--%>
    <%--                                                Màu sắc:--%>
    <%--                                                <p class="order__color d-inline">` + order.color + `</p>--%>
    <%--                                            </span>--%>
    <%--                                        <p class="order__size">` + order.size + `</p>--%>
    <%--                                    </div>--%>
    <%--                                </div>--%>
    <%--                            </td>--%>
    <%--                            <td class="td__item">` + order.count + `</td>--%>
    <%--                            <td class="td__item">` + order.price.toLocaleString('vi-VN') + `</td>--%>
    <%--                        </tr>--%>
    <%--            `--%>
    <%--            con.append(content)--%>
    <%--            totalCost += order.price--%>
    <%--        })--%>
    <%--        totalPrice.text(totalCost.toLocaleString('vi-VN'))--%>
    <%--    }--%>

    <%--    &lt;%&ndash;var jsonOrder =&ndash;%&gt;--%>
    <%--    &lt;%&ndash;    <c:if test="${not empty requestScope.models}">&ndash;%&gt;--%>
    <%--    &lt;%&ndash;    <c:set value="${requestScope.models}" var="jsonOrder" />&ndash;%&gt;--%>
    <%--    &lt;%&ndash;    updateCheckout(${jsonOrder})&ndash;%&gt;--%>

    <%--    function handlePlaceOrder() {--%>
    <%--        $('#delivery__method--form input[class=radio__button][name=delivery__method]').change(function () {--%>
    <%--            $('#payment__method--form input[class=radio__button][name=payment__method]').prop('disabled', false);--%>
    <%--        })--%>

    <%--        $('.place__order').on('click', function () {--%>
    <%--            <c:choose>--%>
    <%--            <c:when test="${not empty requestScope.address}">--%>
    <%--            $.ajax({--%>
    <%--                type: 'POST',--%>
    <%--                url: '/public/user/placeOrder',--%>
    <%--                data: {},--%>
    <%--                dataType: 'json',--%>
    <%--                success: function (response) {--%>
    <%--                    const popupOrder = `<div class="popup__order">--%>
    <%--                                            <div class="bar__loading"></div>--%>
    <%--                                            <p class="message__process">Hệ thống đang xử lý, vui lòng quý khách chờ trong vài giây và không đóng tab này. Trong trường hợp tab bị đóng thì quá trình hiện đang được xử lý sẽ thất bại</p>--%>
    <%--                                        </div>`--%>
    <%--                    $('.place__order').parent().append(popupOrder)--%>
    <%--                    $(document).find('.ground__button--forward a').addClass('disabled-link')--%>
    <%--                    $(document).find('.place__order').css('cursor', 'not-allowed').prop('disabled', 'false')--%>
    <%--                    $(document).find('.radio__button').each(function (index) {--%>
    <%--                        $(this).css('cursor', 'not-allowed').prop('disabled', 'false')--%>
    <%--                    })--%>
    <%--                    $(document).find('.popup__order').css('display', 'block')--%>
    <%--                    setTimeout(function () {--%>
    <%--                        $(document).find('.popup__order').addClass('active');--%>
    <%--                    }, 100);--%>

    <%--                    setTimeout(function () {--%>
    <%--                        let invoiceNo = response.invoiceNo;--%>
    <%--                        let dateOrder = response.dateOrder;--%>
    <%--                        listOrder = ${jsonOrder};--%>
    <%--                        obj = {--%>
    <%--                            delivery: $('input[class=radio__button][name=delivery__method]:checked').val(),--%>
    <%--                            payment: $('input[class=radio__button][name=payment__method]:checked').val(),--%>
    <%--                            address: $('.focus__address.selected > #card-address').val(),--%>
    <%--                            orders: listOrder.order,--%>
    <%--                            invoiceNo: invoiceNo--%>
    <%--                        }--%>
    <%--                        data = [{--%>
    <%--                            name: 'order',--%>
    <%--                            value: JSON.stringify(obj)--%>
    <%--                        }]--%>
    <%--                        let formData = $.param(data);--%>
    <%--                        console.log("Order >> " + JSON.stringify(obj))--%>
    <%--                        $.ajax({--%>
    <%--                            url: "/public/user/successOrder",--%>
    <%--                            method: 'post',--%>
    <%--                            data: data,--%>
    <%--                            success: function (res) {--%>
    <%--                                $('#main').html(res)--%>
    <%--                            },--%>
    <%--                            error: function (err) {--%>
    <%--                                console.log(err)--%>
    <%--                            }--%>
    <%--                        })--%>
    <%--                    }, 3000);--%>
    <%--                }--%>
    <%--            })--%>
    <%--            </c:when>--%>
    <%--            <c:otherwise>--%>
    <%--            Swal.fire({--%>
    <%--                title: "Chúng tôi không tìm thấy địa chỉ giao hàng của bạn?",--%>
    <%--                text: "Vui lòng bổ sung thông tin ở trường bên trái!!!",--%>
    <%--                icon: "question"--%>
    <%--            });--%>
    <%--            </c:otherwise>--%>
    <%--            </c:choose>--%>
    <%--        })--%>
    <%--    }--%>

    <%--    handlePlaceOrder();--%>
    <%--    &lt;%&ndash;    </c:if>&ndash;%&gt;--%>
    <%--    $(document).ready(function () {--%>
    <%--        // Thiết lập lựa chọn mặc định--%>
    <%--        $('input[class=radio__button][name=delivery__method]')[0].checked = true--%>
    <%--        $('input[class=radio__button][name=payment__method]')[0].checked = true--%>
    <%--    })--%>

    <%--    // function handleChoiceDeliveryMethod() {--%>
    <%--    //     $(document).ready(function () {--%>
    <%--    //         $('input[name="delivery__method"]').change(function () {--%>
    <%--    //             let action = $('#delivery__method--form input[type=hidden][name="action"]').val();--%>
    <%--    //             let deliveryMethodId = $(this).val();--%>
    <%--    //             $.ajax({--%>
    <%--    //                 type: 'POST',--%>
    <%--    //                 url: '/Checkout',--%>
    <%--    //                 data: {--%>
    <%--    //                     action: action,--%>
    <%--    //                     deliveryMethodId: deliveryMethodId--%>
    <%--    //                 },--%>
    <%--    //                 dataType: 'json',--%>
    <%--    //                 success: function (response) {--%>
    <%--    //                     $(this).prop('checked', true);--%>
    <%--    //                     $('.total__price--final .total__value').text(response.newTotalPrice);--%>
    <%--    //                     $('.shipping__container span:last-child').text(response.shippingFee);--%>
    <%--    //                     $('.amount__pay .amount').text(response.newTotalPrice);--%>
    <%--    //                 }--%>
    <%--    //             })--%>
    <%--    //         })--%>
    <%--    //     })--%>
    <%--    // }--%>
    <%--    //--%>
    <%--    // handleChoiceDeliveryMethod();--%>
    <%--    //--%>
    <%--    // function handleChoicePaymentMethod() {--%>
    <%--    //     $(document).ready(function () {--%>
    <%--    //         $('input[name="payment__method"]').change(function () {--%>
    <%--    //             let action = $('#payment__method--form input[type=hidden][name="action"]').val();--%>
    <%--    //             let paymentMethodId = $(this).val();--%>
    <%--    //             $.ajax({--%>
    <%--    //                 type: 'POST',--%>
    <%--    //                 url: '/Checkout',--%>
    <%--    //                 data: {--%>
    <%--    //                     action: action,--%>
    <%--    //                     paymentMethodId: paymentMethodId--%>
    <%--    //                 },--%>
    <%--    //                 dataType: 'json',--%>
    <%--    //                 success: function (response) {--%>
    <%--    //                     $('.transaction__content .content').text(response.contentForPay);--%>
    <%--    //                 }--%>
    <%--    //             })--%>
    <%--    //         })--%>
    <%--    //     })--%>
    <%--    // }--%>
    <%--    //--%>
    <%--    // handleChoicePaymentMethod();--%>
    <%--    //--%>
    <%--    // function handleChoiceDeliveryInfo() {--%>
    <%--    //     $(document).ready(function () {--%>
    <%--    //         $('#delivery__info--form').on('click', '.button__choice', function (event) {--%>
    <%--    //             event.preventDefault();--%>
    <%--    //             let buttonChoiceClicked = $(this);--%>
    <%--    //             if (buttonChoiceClicked.text() === 'Chọn') {--%>
    <%--    //                 let deliveryInfo = buttonChoiceClicked.closest('.delivery__info');--%>
    <%--    //                 let deliveryInfoKey = deliveryInfo.find('input[type=hidden][name=deliveryInfoKey]').val();--%>
    <%--    //                 let typeEdit = buttonChoiceClicked.val();--%>
    <%--    //                 $.ajax({--%>
    <%--    //                     type: 'POST',--%>
    <%--    //                     url: '/Checkout',--%>
    <%--    //                     data: {--%>
    <%--    //                         typeEdit: typeEdit,--%>
    <%--    //                         deliveryInfoKey: deliveryInfoKey--%>
    <%--    //                     },--%>
    <%--    //                     success: function (response) {--%>
    <%--    //                         buttonChoiceClicked.text(response)--%>
    <%--    //                         $('.button__choice').not(buttonChoiceClicked).text("Chọn")--%>
    <%--    //                     }--%>
    <%--    //                 })--%>
    <%--    //             }--%>
    <%--    //         })--%>
    <%--    //     })--%>
    <%--    // }--%>
    <%--    //--%>
    <%--    // handleChoiceDeliveryInfo();--%>
    <%--    //--%>
    <%--    // function handleRemoveDeliveryInfo() {--%>
    <%--    //     $(document).ready(function () {--%>
    <%--    //         $('#delivery__info--form').on('click', '.button__remove', function (event) {--%>
    <%--    //             event.preventDefault();--%>
    <%--    //             let buttonRemoveClicked = $(this);--%>
    <%--    //             let deliveryInfo = buttonRemoveClicked.closest('.delivery__info');--%>
    <%--    //             let deliveryInfoKey = deliveryInfo.find('input[type=hidden][name=deliveryInfoKey]').val();--%>
    <%--    //             let typeEdit = buttonRemoveClicked.val();--%>
    <%--    //--%>
    <%--    //             let buttonChoice = deliveryInfo.find('.button__choice');--%>
    <%--    //             let statusChoice = buttonChoice.text();--%>
    <%--    //--%>
    <%--    //             const popupDeletion = $(document).find('.popup__deletion');--%>
    <%--    //             popupDeletion.html(`<div class="popup__container">--%>
    <%--    //--%>
    <%--    //                                     <div class="popup__content">--%>
    <%--    //                                         <div class="title__header">--%>
    <%--    //                                             <span class="title"><i class="fa-solid fa-triangle-exclamation"></i> Xóa thông tin giao hàng</span>--%>
    <%--    //                                             <span class="subtitle">Bạn có muốn xóa thông tin giao hàng đang chọn?</span>--%>
    <%--    //                                         </div>--%>
    <%--    //                                         <div class="button__control">--%>
    <%--    //                                             <button class="agree__button">Xác nhận</button>--%>
    <%--    //                                             <button class="cancel__button">Hủy</button>--%>
    <%--    //                                         </div>--%>
    <%--    //                                     </div>--%>
    <%--    //                                 </div>`);--%>
    <%--    //--%>
    <%--    //             $(popupDeletion).find('.cancel__button').on('click', function () {--%>
    <%--    //                 $(popupDeletion).find('.popup__container').remove();--%>
    <%--    //             })--%>
    <%--    //--%>
    <%--    //             $(popupDeletion).find('.agree__button').on('click', function () {--%>
    <%--    //                 $.ajax({--%>
    <%--    //                     type: 'POST',--%>
    <%--    //                     url: '/Checkout',--%>
    <%--    //                     data: {--%>
    <%--    //                         typeEdit: typeEdit,--%>
    <%--    //                         deliveryInfoKey: deliveryInfoKey,--%>
    <%--    //                         statusChoice: statusChoice--%>
    <%--    //                     },--%>
    <%--    //                     success: function (response) {--%>
    <%--    //                         $(popupDeletion).find('.popup__container').remove();--%>
    <%--    //                         deliveryInfo.remove();--%>
    <%--    //                         if (statusChoice === "Đã chọn") {--%>
    <%--    //                             $('#default__info').find('.button__choice').text("Đã chọn")--%>
    <%--    //                         }--%>
    <%--    //                     }--%>
    <%--    //                 })--%>
    <%--    //             })--%>
    <%--    //         })--%>
    <%--    //     })--%>
    <%--    // }--%>
    <%--    //--%>
    <%--    // handleRemoveDeliveryInfo();--%>
</script>
</html>