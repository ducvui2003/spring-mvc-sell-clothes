<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en">
<head>
    <jsp:include page="/WEB-INF/view/common/adminLink.jsp"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/assets/css/admin/adminUsers.css" />">
    <title>Quản lí người dùng</title>
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
                <div class="col-12">
                    <div class="d-flex justify-content-between">
                        <h1>Danh sách người dùng</h1>
                        <div>
                            <button id="button" class="button button__delete" data-bs-toggle="modal"
                                    data-bs-target="#modal">
                                <i class="fa-solid fa-add"></i>Thêm người dùng
                            </button>
                        </div>
                    </div>
                    <div class="table__wrapper">
                        <table class="table " id="table">
                            <thead>
                            <tr class="">
                                <th class=" table__id">#</th>
                                <th class=" table__username">Tên người dùng</th>
                                <th class=" table__email">Email</th>
                                <th class=" table__fullname">Họ tên</th>
                                <th class=" table__gender">Giới tính</th>
                                <th class=" table__phone">Số điện thoại</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <div class="pagination">
                    </div>
                </div>
            </div>
        </div>
    </section>
</main>

<!-- Modal -->
<div class="modal fade" id="modal" data-bs-keyboard="false" tabindex="-1"
     aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog   modal-dialog-scrollable modal-xl">
        <form id="form" class="modal-content needs-validation">
            <div class="modal-header">
                <h2 class="modal-title" id="staticBackdropLabel">Cập nhập thông tin ngừoi dùng</h2>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-4">
                        <label for="email" class="form-label">Email</label>
                        <input type="text" class="form-control" id="email" name="email"
                               placeholder="email@example.com">
                        <div class="valid-feedback">

                        </div>
                    </div>
                    <div class="col-4">
                        <label for="username" class="form-label">Tên đăng nhập</label>
                        <input type="text" class="form-control" id="username" name="username"
                               placeholder="email@example.com">
                        <div class="valid-feedback">

                        </div>
                    </div>
                    <div class="col-4">
                        <label for="password" class="form-label">Mật khẩu</label>
                        <input type="password" name="password" class="form-control" id="password">
                        <div class="valid-feedback">

                        </div>
                    </div>
                    <div class="col-6 mt-2">
                        <label for="fullName" class="form-label">Họ và tên</label>
                        <input type="text" class="form-control" id="fullName" name="fullName"
                               placeholder="email@example.com">
                        <div class="valid-feedback">

                        </div>
                    </div>
                    <div class="col-6 mt-2">
                        <label for="phone" class="form-label"> Số điện thoại </label>
                        <input type="text" class="form-control" id="phone" name="phone"
                               placeholder="email@example.com">
                        <div class="valid-feedback">

                        </div>
                    </div>
                    <div class="col-4 mt-2">
                        <label for="gender" class="form-label">Giới tính</label>
                        <select class="form-select" id="gender" name="gender" disabled>
                            <option name="gender" value="MALE">Nam</option>
                            <option name="gender" value="FEMALE">Nữ</option>
                        </select>
                        <div class="valid-feedback">

                        </div>
                    </div>
                    <div class="col-4 mt-2">
                        <label for="birthDay" class="form-label text-nowrap"> Ngày sinh</label>
                        <input type="date" class="form-control" id="birthDay" name="birthDay"
                               placeholder="email@example.com">
                        <div class="valid-feedback">

                        </div>
                    </div>
                    <div class="col-4 mt-2">
                        <label for="role" class="form-label text-nowrap">Vai trò</label>
                        <select class="form-select" aria-label="" name="role" id="role" disabled>
                            <option value="ADMIN">Quản trị</option>
                            <option value="USER">Người dùng</option>
                            <option value="BLOCK">Khóa tài khoản</option>
                        </select>
                        <div class="valid-feedback">

                        </div>
                    </div>
                </div>
                <hr>
                <p class="h3 mt-3">Khóa xác thực</p>
                <div class="row">
                    <table id="table-key" class="table table-hover">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Khóa công khai</th>
                            <th scope="col">Ngày tạo</th>
                            <th scope="col">Tình trạng</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr class="table-active">
                            <th>#</th>
                            <th>Khóa công khai</th>
                            <th>Ngày tạo</th>
                            <th>Tình trạng</th>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                <button type="submit" class="btn btn-primary">Thêm</button>
            </div>
        </form>
    </div>
</div>
<script type="module" src="<c:url value="/js/admin/adminUsers.js" />"></script>
</body>
</html>