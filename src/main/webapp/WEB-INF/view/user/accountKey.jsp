<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <jsp:include page="/commonLink"/>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/select2-bootstrap-5-theme@1.3.0/dist/select2-bootstrap-5-theme.min.css"/>
    <link rel="stylesheet" href="<c:url value="/assets/css/admin/admin.css"/>">
    <link rel="stylesheet" href="<c:url value="/assets/css/user/account.css"/>">
    <link rel="stylesheet" href="<c:url value="/assets/css/user/accountKey.css"/>">
    <title>Quản lý khóa</title>
</head>
<body>
<c:import url="/header"/>
<div id="main" class="d-flex">
    <%@include file="accountNavigator.jsp" %>
    <div class="px-4 mt-4 w-100">
        <div class="row">
            <div class="col-lg-9">
                <div class="card mb-4">
                    <div class="card-header">Thông tin khóa</div>
                    <div class="card-body">
                        <c:if test="${requestScope.hasKey == false}">
                            <div class="mb-3" id="alertWarning">
                                <div class="alert alert-warning" role="alert">
                                    <strong>Thông báo:</strong> Hãy cập nhật thông tin khóa của bạn để tiếp tục sử dụng
                                    dịch vụ!
                                </div>
                            </div>
                        </c:if>
                        <div class="mb-3">
                            <label class="small mb-0 d-inline-flex align-items-center" for="currentKey">Khóa hiện
                                tại <span class="ms-1 button--hover" data-bs-toggle="tooltip" data-bs-placement="bottom"
                                          data-bs-custom-class="custom-tooltip"
                                          data-bs-title="Khóa công khai."><i
                                        class="fa-regular fa-circle-question"></i></span></label>
                            <!-- Textarea hiển thị key -->
                            <textarea class="form-control overflow-auto" rows="3" name="currentKey" id="currentKey"
                                      placeholder="Hãy cập nhật khóa công khai của bạn!"
                                      disabled>${requestScope.currentKey.publicKey}</textarea>
                        </div>

                        <div class="mb-3">
                            <label class="small mb-1" for="createdDate">Ngày tạo</label>
                            <input class="form-control" name="createdDate" id="createdDate" type="text"
                                   value="${requestScope.currentKey.createAt}" disabled>
                        </div>
                        <div class="d-flex justify-content-start">
                            <button class="btn btn-primary me-3" type="button" data-bs-toggle="modal"
                                    data-bs-target="#addKeyModal">Thêm khóa mới
                            </button>
                            <c:if test="${requestScope.hasKey == true}">
                                <button class="btn btn-danger" type="button" data-bs-toggle="modal"
                                        data-bs-target="#reportKeyModal">Báo mất khóa
                                </button>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-3">
                <div class="card mb-4">
                    <div class="card-header">Công cụ ký tên</div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label class="small mb-0 d-inline-flex align-items-center" for="currentKey">Tải công cụ<span
                                    class="ms-1 button--hover" data-bs-toggle="tooltip" data-bs-placement="bottom"
                                    data-bs-custom-class="custom-tooltip"
                                    data-bs-title="Tải công cụ chữ ký điện tử phục vụ mua sắm."><i
                                    class="fa-regular fa-circle-question"></i></span></label>

                        </div>
                        <div class="d-flex justify-content-center">
                            <a href="/api/key/download-exe" class="btn btn-primary me-3" download>
                                <i class="fa-solid fa-download"></i> Tải phần mềm
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row mt-4">
            <div class="col-12">
                <div class="card mb-4">
                    <div class="card-header">Lịch sử khóa</div>
                    <div class="card-body">
                        <table id="keyList" class="table table-bordered table-hover table-striped text-center">
                            <thead>
                            <tr>
                                <th style="width: 25%">#</th>
                                <th style="width: 35%">Khóa</th>
                                <th style="width: 15%">Ngày tạo</th>
                                <th style="width: 10%">Trạng thái</th>
                                <th style="width: 10%"></th>
                            </tr>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%--Report key modal--%>
<div class="modal fade text-black" id="reportKeyModal" tabindex="-1" aria-labelledby="staticBackdropLabel"
     aria-hidden="true">
    <div id="modal" class="modal-dialog modal-dialog-scrollable modal-dialog-centered" style="max-width: 80%">
        <form id="form-report-key" class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5">Báo cáo mất khóa</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="row gx-3 mb-3 mt-2">
                    <div class="mb-3">
                        <label class="small mb-1" for="inputPassword">Mã xác thực</label>
                        <input class="form-control" name="inputPassword" id="inputPassword" type="text"
                               value="" placeholder="Nhập mã xác thực">
                    </div>
                </div>
                <div class="row gx-3 mb-3 mt-2">
                    <div class="mb-3">
                        <label class="small mb-1" for="reason">Lý do</label>
                        <textarea class="form-control" name="reason" id="reason" rows="3"
                                  placeholder="Nhập lý do báo mất khóa"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">Báo cáo</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                </div>
            </div>
        </form>
    </div>
</div>

<%--Add key modal--%>
<div class="modal fade text-black" id="addKeyModal" tabindex="-1" aria-labelledby="staticBackdropLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable modal-dialog-centered" style="max-width: 80%">
        <form id="form-add-key" class="modal-content" enctype="multipart/form-data">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="staticBackdropLabel">Thêm khóa mới</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="row gx-3 mb-3 mt-2">
                    <div class="mb-3">
                        <label class="small mb-1" for="inputUploadKey">Tải khóa <span class="ms-1 button--hover"
                                                                                      data-bs-toggle="tooltip"
                                                                                      data-bs-placement="bottom"
                                                                                      data-bs-custom-class="custom-tooltip"
                                                                                      data-bs-title="Tải khóa công khai của bạn."><i
                                class="fa-regular fa-circle-question"></i></span></label>
                        <input class="form-control" name="inputUploadKey" id="inputUploadKey" type="file">
                        <div class="valid-feedback">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">Lưu</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                </div>
            </div>
        </form>
    </div>
</div>

<%--Key detail modal--%>
<div class="modal fade" id="detailKeyModal" data-bs-keyboard="false" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
            <div class="modal-header">
<%--                <h5 class="modal-title">Thông tin chi tiết khóa</h5>--%>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<div class='d-none loader__wrapper position-fixed top-0 start-0 end-0 bottom-0'
     style="background-color: rgba(0,0,0,0.5)">
    <span class='position-absolute top-50 start-50 translate-middle loader'></span>
</div>

<c:import url="/footer"/>
<!--Select 2 jquery-->
<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet"/>
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.13.2/jquery-ui.min.js"
        integrity="sha512-57oZ/vW8ANMjR/KQ6Be9v/+/h6bq9/l3f0Oc7vn6qMqyhvPd1cvKBRWWpzu0QoneImqr2SkmO4MSqU+RpHom3Q=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<!--jQuery validator-->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.20.0/jquery.validate.min.js"
        integrity="sha512-WMEKGZ7L5LWgaPeJtw9MBM4i5w5OSBlSjTjCtSnvFJGSVD26gE5+Td12qN5pvWXhuWaWcVwF++F7aqu9cvqP0A=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://ajax.aspnetcdn.com/ajax/jquery.validate/1.15.0/additional-methods.js"></script>
<script type="module" src="<c:url value="/js/user/accountKey.js"/>">
    <script type="module" src="<c:url value="/js/base.js"/>">
</script>
<script>
    function selected(ind) {
        document.querySelectorAll('.navbar__link').forEach(tab => {
            if (tab.dataset.index == ind) {
                tab.classList.add('navbar__link--clicked');
            }
        });
    }

    selected(4);
</script>
</body>
</html>
