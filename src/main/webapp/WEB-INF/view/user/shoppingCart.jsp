<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"
           uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <jsp:include page="/commonLink"/>
    <link rel="stylesheet" href="<c:url value="/assets/css/shoppingCart.css" />">
    <title>Giỏ hàng</title>
</head>

<body>
<c:import url="/header"/>
<main id="main">
    <!-- New update template -->
    <div class="promotion__modal">
        <div class="promotion__sidebar">
            <div class="promotion__header">
                <i
                        class="fa-solid fa-arrow-left"></i>
                <span>Mã giảm giá</span>
            </div>
            <div class="promotion__content">
                <c:forEach
                        items="${requestScope.listVouchers}"
                        var="voucher">
                    <div class="promotion__item">
                        <div class="discount__percent">
                            <i class="fa-solid fa-fire"></i>
                            <span>
<%--                                <fmt:formatNumber--%>
<%--                                        type="percent"--%>
<%--                                        value="${voucher.discountPercent}"/>--%>
                            </span>
                        </div>
                        <div class="item__content">
                            <h1 class="promotion__text">
                                NHẬP MÃ: ${voucher.code}
                            </h1>
                            <p>HSD:
                                <fmt:formatDate
                                        pattern="dd-MM-yyyy"
                                        value="${voucher.expiryDate}"/>
                            </p>
                            <p class="promotion__description">
                                    ${voucher.description}
                                <fmt:setLocale
                                        value="vi_VN"/>
                                <fmt:formatNumber
                                        type="currency"
                                        value="${voucher.minimumPrice}"/>
                            </p>
                            <button
                                    class="button__copy"
                                    data-code="${voucher.code}">Sao
                                chép
                                <i class="fa-solid fa-copy"></i></button>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="promotion__footer">
                <button>Quay lại giỏ
                    hàng
                </button>
            </div>
        </div>
    </div>

    <div class="container-xl">
        <h1 class="cart__title">Giỏ hàng</h1>
        <form:form id="form__checkout" class="cart__container row" modelAttribute="checkout" method="post"
                   action="/checkout">
            <c:choose>
                <c:when test="${requestScope.carts.size() == 0}">
                    <div class="cart__container--empty">
                        <p>Không có sản phẩm nào
                            trong giỏ hàng của
                            bạn</p>
                        <a href="/product">
                            <button>Tiếp tục mua
                                sắm
                            </button>
                        </a>
                        <img src="<c:url value="/assets/img/continueShopping.svg" />">
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="cart__content col">
                        <div class="shopping__cart--form">
                            <div
                                    class="d-flex w-100 check__header">
                                <input
                                        type="radio"
                                        id="check__pay-all"
                                        name="check-high"
                                        class="check-high"/>
                                <label
                                        for="check__pay-all"
                                        class="check__title">Lựa
                                    chọn toàn
                                    bộ</label>
                                <input
                                        type="radio"
                                        id="remove__pay-all"
                                        name="check-high"
                                        class="check-high"
                                        checked/>
                                <label
                                        for="remove__pay-all"
                                        class="check__title">Hủy chọn tất cả</label>
                            </div>
                            <table
                                    id="cart__table">
                                <thead
                                        class="cart__header">
                                <tr>
                                    <th>Lựa
                                        chọn
                                    </th>
                                    <th>Sản
                                        phẩm
                                    </th>
                                    <th>Giá
                                        may
                                    </th>
                                    <th>Số
                                        lượng
                                    </th>
                                    <th>Thành
                                        tiền
                                    </th>
                                    <th>Xóa
                                    </th>
                                </tr>
                                </thead>
                                <tbody class="cart__items">
                                <!--Cart-->
                                <c:forEach var="item" items="${requestScope.carts}">
                                    <tr class="cart__item"
                                        data-card-item-id="${item.id}"
                                        data-product-id="${item.productId}">
                                        <td class="container__check__pay">
                                            <form:checkbox
                                                    path="cartItemId"
                                                    name="product"
                                                    value="${item.id}"
                                                    class="check__pay"/>
                                        </td>
                                        <td class="product__item">
                                            <div class="product__content">
                                                <a class="product__image"
                                                   href="/product/${item.productId}">
                                                    <img src="${item.thumbnail}">
                                                </a>
                                                <div class="order__product--info">
                                                    <a href="#"
                                                       class="product__name">
                                                            ${item.name}
                                                    </a>
                                                    <span>
                                                    Màu sắc:
                                                    <p class="order__color d-inline">
                                                            ${item.color}
                                                    </p>
                                                </span>
                                                    <span class="d-flex align-content-center">
                                                    Mã màu:
                                                    <p class=" d-inline-block ms-2 border border-1"
                                                       style="background:  ${item.color}; width: 30px; height: 100%">
                                                    </p>
                                                </span>
                                                    <ul class="d-block">
                                                    <span>
                                                        Kích thước:
                                                        <p class="order__size--specification d-inline">
                                                                ${item.size}
                                                        </p>
                                                    </span>
                                                    </ul>
                                                </div>
                                            </div>
                                        </td>
                                        <c:set var="price"
                                               value="${item.salePrice !=0 ? item.salePrice : item.price}"/>
                                        <td class="unit__price"
                                            data-price=" ${price}">
                                                ${price}
                                        </td>
                                        <td>
                                            <div class="quality__swapper">
                                                <button
                                                        type="submit"
                                                        class="minus__quality change__quality"
                                                        name="action"
                                                        value="decreaseQuantity">
                                                    <i class="fa-solid fa-minus"></i></button>
                                                <input
                                                        type="number"
                                                        name="quality__required"
                                                        class="quality__required"
                                                        min="1"
                                                        readonly
                                                        value="${item.quantity}">
                                                <button
                                                        type="submit"
                                                        class="plus__quality change__quality"
                                                        name="action"
                                                        value="increaseQuantity">
                                                    <i class="fa-solid fa-plus"></i></button>
                                            </div>
                                        </td>
                                        <td class="subtotal__item">
                                                ${price * item.quantity}
                                        </td>
                                        <td class="remove__action">
                                            <button
                                                    type="submit"
                                                    name="action"
                                                    value="removeCartProduct"
                                                    class="remove__item">
                                                <i class="fa-solid fa-trash-can"></i></button>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="invoice__promotion col">
                        <div class="summary__invoice">
                            <h2>Tổng đơn hàng
                            </h2>
                            <div class="invoice__detail--info">
                                <ul class="price__items">
                                    <li class="price__item">
                                        <p class="price__text">
                                            Tạm tính
                                            (<span class="total__items"
                                                   id="total__items"></span>
                                            sp)
                                        </p>
                                        <p class="price__value" id="price__total">

                                        </p>
                                    </li>
                                </ul>
                                <div class="price__total">
                                    <p class="price__text">
                                        Tổng cộng:
                                    </p>
                                    <div class="price__content">
                                        <p class="price__value--final price__final" id="price__final">
                                            0 ₫
                                        </p>
                                        <p class="price__value--noted">
                                            (Đã bao gồm VAT nếu có)
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="group__button--forward">
                            <a id="continue--directional" href="/checkout">
                                <button type="submit" id="continue--checkout">
                                    Tiến hành thanh toán
                                </button>
                            </a>
                            <a href="/product" id="continue--shopping" class="text-center">
                                Tiến tục mua sắm
                            </a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </form:form>
    </div>
</main>
<c:import url="/footer"/>
</body>
<script type="module" src="<c:url value="/js/shoppingCart.js" />"></script>
</html>