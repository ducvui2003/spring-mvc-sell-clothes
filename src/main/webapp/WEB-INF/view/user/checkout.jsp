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
        <form:form class="checkout__container row" id="form" modelAttribute="checkout">
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
                        <div class="method__content">
                            <div class="method__item section__info--selection">
                                <input type="radio"
                                       name="paymentMethod" class="radio__button"
                                       value="COD"
                                       id="payment__method-cod"/>
                                <label class="label__selection"
                                       for="payment__method-cod">COD</label>
                            </div>
                        </div>
                        <div class="method__content">
                            <div class="method__item section__info--selection">

                                <input
                                        type="radio"
                                        name="paymentMethod" class="radio__button"
                                        value="VNPAY"
                                        id="payment__method-vnpay"/>
                                <label class="label__selection"
                                       for="payment__method-vnpay">VNPAY</label>
                            </div>
                        </div>
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

</script>
</html>