<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="/WEB-INF/view/common/adminLink.jsp"/>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" href="<c:url value="/assets/css/admin/admin.css"/>">
    <link rel="stylesheet" href="<c:url value="/assets/css/admin/adminOrders.css"/>">
    <title>Quản lý đơn hàng</title>
</head>
<body>
<!--Header-->
<jsp:include page="/WEB-INF/view/common/header.jsp"/>
<main id="main">
    <!--Navigate-->
    <jsp:include page="/WEB-INF/view/common/adminNavigator.jsp"/>
    <section class="content">
        <div class="container-xl">
            <div class="row">
                <h1 class="badge bg-primary fs-3 d-inline m-2" style="width: fit-content">
                    <i class="fa-solid fa-list"></i>
                    Danh sách đơn hàng</h1>
                <div class="d-flex justify-content-between">
                    <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                            data-bs-target="#modal-filter">
                        <i class="fa-solid fa-filter"></i> Bộ lọc
                    </button>
                </div>
                <div class="col-12">
                    <div class="table__wrapper">
                        <table id="table" class="table">
                            <thead>
                            <tr class="table__row">
                                <th class="table__head">#</th>
                                <th class="table__head">Ngày đặt hàng</th>
                                <th class="table__head">Khách hàng</th>
                                <th class="table__head">Phương thức thanh toán</th>
                                <th class="table__head">Tình trạng thanh toán</th>
                                <th class="table__head">Tình trạng đơn hàng</th>
                                <th class="table__head">Xem</th>
                                <th class="table__head">Cập nhật</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </section>
</main>

<!--Modal filter-->
<div class="modal fade" id="modal-filter" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg ">
        <form id="form-search" class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel">Lọc và tìm kiếm</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-8">
                            <div class="input-group mb-3">
                                <span class="input-group-text" id="basic-addon1">
                                    <i class="fa-solid fa-magnifying-glass"></i>
                                </span>
                                <input type="text" class="form-control" id="contentSearch" name="contentSearch"
                                       placeholder="Nhập nội dung tìm kiếm"
                                       aria-label="Username" aria-describedby="basic-addon1">
                            </div>
                        </div>
                        <div class="col-4">
                            <select class="form-select mb-3" id="searchSelect" name="searchSelect"
                                    aria-label="Tìm kiếm theo">
                                <option value="ORDER_ID" selected>Mã đơn hàng
                                </option>
                                <option value="CUSTOMER_NAME">Tên khách hàng
                                </option>
                            </select>
                        </div>
                        <div class="col-4">
                            <h2 class="filler__heading">Thời gian đặt hàng</h2>
                            <div class="interval__filler">
                                <input type="text" name="createdAt" class="form-control"
                                       id="createAt">
                            </div>
                        </div>
                        <div class="col-4">
                            <h2 class="filler__heading">Phương thức thanh toán</h2>
                            <div class="filter__content">
                                <c:forEach
                                        items="${requestScope.paymentMethod}"
                                        var="paymentMethod">
                                    <label class="filter__label check">
                                        <input type="checkbox" name="paymentMethod" hidden="hidden"
                                               value="${paymentMethod}">
                                        <span>${paymentMethod}</span>
                                    </label>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="col-4">
                            <h2 class="filler__heading">Tình trạng đơn hàng</h2>
                            <div class="filter__content">
                                <c:forEach items="${requestScope.orderStatus}"
                                           var="orderStatus">
                                    <label class="filter__label check">
                                        <input type="checkbox" name="orderStatus"
                                               value="${orderStatus.id}" class="filter__input"
                                               hidden="hidden">
                                        <span>${orderStatus.typeStatus}</span>
                                    </label>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                <button type="submit" class="btn btn-primary">Tìm kiếm</button>
            </div>
        </form>
    </div>
</div>

<!-- Modal view -->
<div class="modal fade " id="modal-view" tabindex="-1" aria-labelledby="modal-label" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable modal-xl">
        <div class="modal-content ">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="modal-label">Xem đơn hàng</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body" style="max-height: 80vh">
                <div class="section__dialog">
                    <div class="section__heading">
                        <h1 class="h3">1. Thông tin giao hàng</h1>
                    </div>
                    <table class="section__content">
                        <tbody>
                        <tr class="section__info">
                            <td class="info__text">Tên khách hàng</td>
                            <td class="info__value fullname"></td>
                        </tr>
                        <tr class="section__info">
                            <td class="info__text">Email</td>
                            <td class="info__value email"></td>
                        </tr>
                        <tr class="section__info">
                            <td class="info__text">Số điện thoại</td>
                            <td class="info__value phone"></td>
                        </tr>
                        <tr class="section__info">
                            <td class="info__text">Địa chỉ</td>
                            <td class="info__value address"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="section__dialog">
                    <div class="section__heading">
                        <h1 class="h3">2. Thông tin đơn hàng</h1>
                    </div>
                    <table class="section__content">
                        <tbody>
                        <tr class="section__info">
                            <td class="info__text ">Mã đơn hàng</td>
                            <td class="info__value orderId"></td>
                        </tr>
                        <tr class="section__info">
                            <td class="info__text ">Ngày đặt hàng</td>
                            <td class="info__value createAt"></td>
                        </tr>
                        <tr class="section__info">
                            <td class="info__text ">Mã giảm giá đã áp dụng</td>
                            <td class="info__value voucherApply"></td>
                        </tr>
                        <tr class="section__info">
                            <td class="info__text">Phương thức thanh toán</td>
                            <td class="info__value paymentMethod"></td>
                        </tr>
                        <tr class="section__info order__status--section">
                            <td class="info__text ">Tình trạng đơn hàng</td>
                            <td class="info__value orderStatus"></td>
                        </tr>
                        <tr class="section__info transaction__status--section">
                            <td class="info__text ">Tình trạng giao dịch</td>
                            <td class="info__value transaction"></td>
                        </tr>
                        <tr class="section__info">
                            <td class="info__text ">Phí vận chuyển</td>
                            <td class="info__value payment-fee"></td>
                        </tr>
                        <tr class="section__info">
                            <td class="info__text ">Ngày nhận hàng</td>
                            <td class="info__value lead-date"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="section__dialog">
                    <div class="section__heading">
                        <h1 class="h3">3. Danh sách sản phẩm đã đặt trong đơn hàng</h1>
                    </div>
                    <div class="section__wrapper">
                        <table id="table-order-detail" class="products__order">
                            <thead>
                            <tr class="table__row">
                                <th class="table__head">Sản phẩm</th>
                                <th class="table__head">Màu sắc</th>
                                <th class="table__head">Kích thuớc</th>
                                <th class="table__head">Số lượng</th>
                                <th class="table__head">Gíá bán</th>
                            </tr>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<!--Modal update status-->

<div class="modal fade" id="modal-update" tabindex="-1" aria-labelledby="modal-label" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable modal-xl">
        <form id="form-update-status" class="modal-content " action="javascript:void(0);">
            <input type="text" hidden="hidden" name="id" value="">
            <div class="modal-header">
                <h1 class="modal-title fs-5">Cập nhập thông tin đơn hàng</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body" style="max-height: 80vh">
                <div class="row">
                    <div class="col-6">
                        <label for="orderStatus" class="form-label">Tình trạng đơn hàng</label>
                        <select id="orderStatus" class="orderStatus" name="orderStatus">
                            <c:forEach var="item" items="${requestScope.orderStatus}">
                                <option value="${item.alias}">${item.typeStatus}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-6">
                        <label for="transactionStatus" class="form-label">Tình trạng giao dịch</label>
                        <select id="transactionStatus" class="transactionStatus" name="transactionStatus">
                            <c:forEach var="item"
                                       items="${requestScope.transactionStatus}">
                                <option value="${item.alias}">${item.typeStatus}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-12">
                        <table id="table-update-order-item" class="table w-100  mt-3 table-striped">
                            <thead>
                            <tr>
                                <th scope="col">#</th>
                                <th scope="col">Sản phẩm</th>
                                <th scope="col">Màu sắc</th>
                                <th scope="col">Kích thuớc</th>
                                <th scope="col">Số lượng</th>
                                <th scope="col">Gíá bán</th>
                            </tr>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    <button type="submit" class="btn btn-primary">Lưu</button>
                </div>
            </div>
        </form>
    </div>
</div>
<%--<div id="form-update-status" class="d-none">--%>
<%--    <div class="container-fluid ">--%>
<%--        <div class="row">--%>
<%--            <div class="col-6">--%>
<%--                <label for="orderStatus" class="form-label">Tình trạng đơn hàng</label>--%>
<%--                <select id="orderStatus" class="orderStatus" name="orderStatus">--%>
<%--                    <c:forEach var="item" items="${requestScope.orderStatus}">--%>
<%--                        <option value="${item.alias}">${item.typeStatus}</option>--%>
<%--                    </c:forEach>--%>
<%--                </select>--%>
<%--            </div>--%>
<%--            <div class="col-6">--%>
<%--                <label for="transactionStatus" class="form-label">Tình trạng giao dịch</label>--%>
<%--                <select id="transactionStatus" class="transactionStatus" name="transactionStatus">--%>
<%--                    <c:forEach var="item"--%>
<%--                               items="${requestScope.transactionStatus}">--%>
<%--                        <option value="${item.alias}">${item.typeStatus}</option>--%>
<%--                    </c:forEach>--%>
<%--                </select>--%>
<%--            </div>--%>
<%--            <div class="col-12">--%>
<%--                <table id="table-update-order-detail" class="table w-100  mt-3 table-striped">--%>
<%--                    <thead>--%>
<%--                    <tr>--%>
<%--                        <th scope="col">#</th>--%>
<%--                        <th scope="col">Sản phẩm</th>--%>
<%--                        <th scope="col">Màu sắc</th>--%>
<%--                        <th scope="col">Kích thuớc</th>--%>
<%--                        <th scope="col">Số lượng</th>--%>
<%--                        <th scope="col">Gíá bán</th>--%>
<%--                    </tr>--%>
<%--                    </thead>--%>
<%--                    <tbody>--%>

<%--                    </tbody>--%>
<%--                </table>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>

<script type="module" src="<c:url value="/js/admin/adminOrders.js"/>"></script>
</body>
</html>