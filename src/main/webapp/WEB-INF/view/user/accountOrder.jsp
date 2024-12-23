<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>

    <jsp:include page="/WEB-INF/view/common/commonLink.jsp"/>
    <link rel="stylesheet" href="<c:url value="/assets/css/admin/admin.css"/>">
    <link rel="stylesheet" href="<c:url value="/assets/css/user/account.css"/>">
    <link rel="stylesheet" href="<c:url value="/assets/css/user/accountInfo.css"/>">
    <link rel="stylesheet" href="<c:url value="/assets/css/user/accountOrder.css"/>">
    <title>Lịch sử mua hàng</title>
</head>
<body>
<jsp:include page="/WEB-INF/view/common/header.jsp"/>
<div id="main" class="d-flex">
    <%@include file="accountNavigator.jsp" %>
    <div class="w-100 px-4 mt-4">
        <div class="row">
            <div class="col-3">
                <div class="list-group">
                    <div class=" py-3 list-group-item list-group-item-action active" aria-current="true"
                         data-status="VERIFYING">
                        <i class="fa-solid fa-signature me-4"></i>Chờ xác thực
                    </div>
                    <div class=" py-3 list-group-item list-group-item-action" aria-current="true"
                         data-status="CHANGED">
                         <i class="fa-solid fa-bell me-4"></i>
                        Đơn hàng đã thay đổi chờ xác thực lại
                    </div>
                    <div class=" py-3 list-group-item list-group-item-action " aria-current="true"
                         data-status="PENDING">
                        <i class="fa-solid fa-hourglass-half me-4"></i>Chờ xác nhận
                    </div>
                    <div class="py-3 list-group-item list-group-item-action" data-status="PACKAGE">
                        <i class="fa-solid fa-spinner me-3"></i> Chờ đóng gói
                    </div>
                    <div class="py-3 list-group-item list-group-item-action" data-status="DELIVERY">
                        <i class="fa-solid fa-truck me-3"></i>Đang vận chuyển
                    </div>
                    <div class="py-3 list-group-item list-group-item-action" data-status="COMPLETED">
                        <i class="fa-regular fa-circle-check me-3"></i> Hoàn thành
                    </div>
                    <div class="py-3 list-group-item list-group-item-action" data-status="CANCELLED">
                        <i class="fa-regular fa-circle-xmark me-3"></i> Hủy
                    </div>
                </div>
            </div>
            <div class="col-9">
                <table id="orderList" class="table table-bordered table-hover table-striped text-center">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Ngày đặt</th>
                        <th>Xem chi tiết</th>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<c:import url="/footer"/>
<!--Modal-->
<div class="modal fade text-black" id="modal" tabindex="-1" aria-labelledby="staticBackdropLabel">
    <div class="modal-dialog modal-dialog-scrollable" style="max-width: 80%">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="staticBackdropLabel">Thông tin đơn hàng</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-12">
                        <h1 class="h2">Mã đơn hàng: <span id="order__id">...</span></h1>
                    </div>
                    <div class="col-12 mt-3">
                        <div class="d-flex">
                            <p class="">Ngày đặt hàng: <span id="order__date" class="fw-bold">...</span></p>
                            <p class="vr mx-2 fs-6">
                            <p class="text-success">
                                <i class="fa-solid fa-truck"></i> Ngày giao hàng dự kiến:
                                <span id="order__lead-date">...</span>
                            <p class="ms-auto text-success fw-bold">
                                Trạng thái đơn hàng: <span id="order__status">...</span>
                            </p>

                        </div>
                    </div>
                </div>
                <hr class="border border-success border-1 opacity-75">
                <div class="row pt-1">
                    <div class="col-12">
                        <div class="row" id="order__list">
                            <div class="order__item align-items-center row">
                                <div class="item__thumbnail">
                                    <img src="/assets/img/product.png"
                                         class="rounded border img-thumbnail object-fit-cover"
                                         style="width: 100px; height: 100px" alt="...">
                                </div>
                                <div class="item__detail ms-3">
                                    <h3 class="h4 item__name pb-3 fw-4">Ao khoac</h3>
                                    <div class="d-flex pt-2 fw-6">
                                        <span class="item__color"></span> <span class="vr mx-2"></span>
                                        <span class="item__size">XL</span>
                                    </div>
                                </div>
                                <div class="ms-auto">
                                    <div class="item__price pb-2 fs-5 fw-bold">Giá: <span>100.000đ</span></div>
                                    <div class="item__quantity pt-2 fs-6">Số lượng: <span>x1</span></div>
                                </div>
                            </div>
                        </div>
                        <div class="my-4"></div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-12">
                        <hr class="border border-1 opacity-75 my-4">
                        <div class="row">
                            <div class="col-6 border-end border-1">
                                <p class="fs-5 text-bold">Thanh toán</p>
                            </div>
                            <div class="col-6">
                                <p class="fs-5 text-bold">Người nhận</p>
                                <div>
                                    <div class="row mt-3">
                                        <div class="col-6">Họ và tên</div>
                                        <div class="col-6 text-end" id="order__name"></div>
                                    </div>
                                    <div class="row mt-3">
                                        <div class="col-6">Số điện thoại</div>
                                        <div class="text-end col-6" id="order__phone"></div>
                                    </div>
                                    <div class="row mt-3">
                                        <div class="col-6">Email</div>
                                        <div class="text-end col-6" id="order__email"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <hr class="border border-1 opacity-75 my-4">
                        <div class="row">
                            <div class="col-6 border-end border-1">
                                <div class="row mt-3">
                                    <div class="col-6">
                                        <p class="fs-5 text-bold">Địa chỉ</p>
                                    </div>
                                    <div class="col-6 text-end">
                                        <div id="btn-change-address" data-bs-target="#modal-change-order"
                                             data-bs-toggle="modal" class="btn btn-primary">Thay đổi địa chỉ
                                        </div>
                                    </div>
                                </div>

                                <div class="row mt-3">
                                    <div class="col-6">Tỉnh/Thành phố</div>
                                    <div class="col-6 text-end" id="order__province"></div>
                                </div>
                                <div class="row mt-3">
                                    <div class="col-6">Quận/Huyện</div>
                                    <div class="text-end col-6" id="order__district"></div>
                                </div>
                                <div class="row mt-3">
                                    <div class="col-6">Xã/Phường</div>
                                    <div class="text-end col-6" id="order__ward"></div>
                                </div>
                                <div class="row mt-3">
                                    <div class="col-6">Đường, thị trấn, số nhà,...</div>
                                    <div class="text-end col-6" id="order__detail"></div>
                                </div>
                            </div>
                            <div class="col-6">
                                <p class="fs-5 text-bold">Tổng tiền</p>
                                <div class="row mt-3">
                                    <div class="col-6">Tạm tính</div>
                                    <div class="col-6 text-end" id="order__temporary"></div>
                                </div>
                                <div class="row mt-3">
                                    <div class="col-6">Giảm giá</div>
                                    <div class="text-end col-6" id="order__voucher"></div>
                                </div>
                                <div class="row mt-3">
                                    <div class="col-6">Vận chuyển</div>
                                    <div class="text-end col-6" id="order__shipping-fee"></div>
                                </div>
                            </div>
                        </div>
                        <div class="row align-items-center" id="verify-block">
                            <hr class="border border-1 opacity-75 my-4">
                            <div class="col-6 border-end flex-column  justify-content-center pe-auto">
                                <div class=" d-flex justify-content-center">
                                    <div class="text-center m-2">
                                        <div class="rounded-circle bg-primary p-3" onclick="handleDownloadFile()">
                                            <label class="text-light"
                                                   style="cursor: pointer; font-size: 32px;">
                                                <i class="fas fa-download"></i>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div class="text-center">
                                    <p>Vui lòng tải thông tin đơn hàng thực hiện xác thực</p>
                                </div>
                            </div>
                            <!-- Vertical Divider -->
                            <div class="col-6 border-end flex-column  justify-content-center pe-auto">
                                <div class=" d-flex justify-content-center">
                                    <div class="text-center m-2">
                                          <div class="rounded-circle bg-primary p-3">
                                            <label for="upload-sign-info" class="text-light"
                                                   style="cursor: pointer; font-size: 32px;">
                                                <i class="fas fa-upload"></i>
                                            </label>
                                            <input id="upload-sign-info" type="file" class="d-none"
                                                 />
                                        </div>
                                    </div>
                                </div>
                                <div class="text-center">
                                    <p>Vui lòng tải thông tin đơn hàng đã xác thực</p>
                                </div>
                            </div>
                        </div>

                        <hr class="border border-1 opacity-75 my-4">
                        <div class="row">
                            <p class="fs-5 text-bold col-6">Tổng cộng</p>
                            <div class="text-end text-bold col-6" id="order__total">$123</div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>


<!--Modal change order-->
<div class="modal fade text-black" id="modal-change-order" tabindex="-1" aria-labelledby="back-drop-modal-change-order">
    <div class="modal-dialog modal-dialog-scrollable" style="max-width: 60%">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="back-drop-modal-change-order">Thay đổi thông tin đơn hàng</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-12">
                        <h1 class="h2">Mã đơn hàng: <span id="">...</span></h1>
                    </div>
                </div>
                <hr class="border border-success border-1 opacity-75">
                <div class="row">
                    <div class="col12">
                        <table id="addressList" class="table table-bordered table-hover table-striped text-center">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Tỉnh/Thành phố</th>
                                <th>Quận/Huyện</th>
                                <th>Xã/Phường</th>
                                <th>Địa chỉ chi tiết</th>
                            </tr>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button"
                        class="btn btn-primary"
                        data-bs-target="#modal"
                        data-noreload="true"
                        data-bs-toggle="modal">Quay lại
                </button>
                <button type="button"
                        class="btn btn-warning"
                        id="btn-address-submit">Thay đổi
                </button>
            </div>
        </div>
    </div>
</div>
</body>


<!--Select 2 jquery-->
<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet"/>
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.13.2/jquery-ui.min.js"
        integrity="sha512-57oZ/vW8ANMjR/KQ6Be9v/+/h6bq9/l3f0Oc7vn6qMqyhvPd1cvKBRWWpzu0QoneImqr2SkmO4MSqU+RpHom3Q=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.20.0/jquery.validate.min.js"
        integrity="sha512-WMEKGZ7L5LWgaPeJtw9MBM4i5w5OSBlSjTjCtSnvFJGSVD26gE5+Td12qN5pvWXhuWaWcVwF++F7aqu9cvqP0A=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<!--JQuery validate-->
<script src="https://ajax.aspnetcdn.com/ajax/jquery.validate/1.15.0/additional-methods.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert2/11.10.7/sweetalert2.min.js"
        integrity="sha512-csaTzpLFmF+Zl81hRtaZMsMhaeQDHO8E3gBkN3y3sCX9B1QSut68NxqcrxXH60BXPUQ/GB3LZzzIq9ZrxPAMTg=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script type="module" src="<c:url value="/js/user/accountOrder.js"/>"></script>

<script>

    function selected(ind) {
        document.querySelectorAll('.navbar__link').forEach(tab => {
            if (tab.dataset.index == ind) {
                tab.classList.add('navbar__link--clicked');
            }
        });
    }

    function handleDownloadFile() {
        var order_id = $('span#order__id').html();
        window.open(`/api/verify-order/download?uuid=` + order_id, '_blank');
    }


    selected(3);
</script>
</html>