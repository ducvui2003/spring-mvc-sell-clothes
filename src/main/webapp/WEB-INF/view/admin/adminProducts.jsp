<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="/adminLink"/>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/ion-rangeslider/2.3.1/css/ion.rangeSlider.min.css"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/ion-rangeslider/2.3.1/js/ion.rangeSlider.min.js"></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" href="<c:url value="/assets/css/admin/admin.css"/>">
    <link rel="stylesheet" href="<c:url value="/assets/css/productBuying.css"/> ">
    <link rel="stylesheet" href="<c:url value="/assets/css/admin/adminProducts.css" />">
    <!--froala-->
    <link href='https://cdn.jsdelivr.net/npm/froala-editor@4.0.10/css/froala_editor.pkgd.min.css' rel='stylesheet'
          type='text/css'/>
    <script type='text/javascript'
            src='https://cdn.jsdelivr.net/npm/froala-editor@4.0.10/js/froala_editor.pkgd.min.js'></script>
    <c:import url="/common/filePond"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/spectrum/1.8.0/spectrum.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="https://cdnjs.cloudflare.com/ajax/libs/spectrum/1.8.0/spectrum.min.css">
    <!--Cloudinary-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/cloudinary-jquery-file-upload/2.13.1/cloudinary-jquery-file-upload.min.js"
            integrity="sha512-pH53M3IB57Cd20HYZE5Cxi0mUHgHvuV5mGVWOZ7v9t7fqGf5mqRXniKoWIoQrNbCKrW+Soql4SaxO4cyPt84Zw=="
            crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <style>
        .color-display {
            width: 100%;
            height: 100%;
            text-align: center;
            line-height: 50px;
            color: white;
        }

        .select-info {
            display: none;
        }
    </style>
    <title>Quản lý sản phẩm</title>
</head>
<body>
<!--Header-->
<c:import url="/header"/>
<main id="main">
    <!--Navigate-->
    <c:import url="/common/adminNavigator"/>
    <section class="content">
        <div class="container-xl">
            <div class="row">
                <h1 class="h3">Danh sách sản phẩm</h1>
                <div class="d-flex justify-content-between">
                    <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                            data-bs-target="#modal-filter">
                        <i class="fa-solid fa-filter"></i> Bộ lọc
                    </button>
                    <div class="d-flex gap-2">
                        <a href="<c:url value="/public/admin/adminImportProducts.jsp" />"
                           class="btn btn-primary d-flex align-items-center gap-1">
                            <i class="fa-solid fa-file-excel"></i>
                            <span>Thêm sản phẩm qua excel</span>
                        </a>
                        <form action="<c:url value="/exportExcelProduct"/>" method="POST" class="">
                            <button class="btn_export" type="submit">
                                <i class="fa-solid fa-file-export"></i>
                                Xuất file excel
                            </button>
                        </form>
                        <button class="button button__add" id="button-modal" type="button" data-bs-toggle="modal"
                                data-bs-target="#modal-create">
                            <i class="fa-solid fa-plus"></i>
                            Thêm sản phẩm
                        </button>
                    </div>
                </div>
                <div class="col-12 mt-2">
                    <div>
                        <table id="table" class="table">
                            <thead>
                            <tr class="table__row">
                                <th class="table__head">#</th>
                                <th class="table__head">Tên sản phẩm</th>
                                <th class="table__head">
                                    Phân loại sản phẩm
                                </th>
                                <th class="table__head">Giá gốc</th>
                                <th class="table__head">Giá giảm</th>
                                <th>Hiển thị</th>
                            </tr>
                            </thead>
                            <tbody class="product__list-admin">
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
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <form id="form-filter" action="<c:url value="/filterProductAdmin"/>" class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel">Lọc và tìm kiếm</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-6">
                            <div class="filter__group">
                                <label for="keyword" class="filter__title d-inline-block" data-bs-toggle="tooltip"
                                       data-bs-placement="top"
                                       data-bs-title="Tìm kiếm theo tên sản phẩm">Tên sản phẩm</label>
                                <div class="input-group mb-3">
                                    <span class="input-group-text">
                                        <i class="fa-solid fa-magnifying-glass"></i>
                                    </span>
                                    <input type="text" class="form-control" placeholder="Nhập tên sản phẩm "
                                           id="keyword" name="keyword">
                                </div>
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="filter__group">
                                <label for="createdAt" class="filter__title d-inline-block" data-bs-toggle="tooltip"
                                       data-bs-placement="top"
                                       data-bs-title="Tìm kiếm theo thời gian cập nhập gần nhât">Thời gian cập
                                    nhập</label>
                                <div class="input-group mb-3">
                                    <span class="input-group-text" id="basic-addon1">  <i
                                            class="fa-solid fa-calendar-days"></i></span>
                                    <input type="text" readonly class="form-control"
                                           aria-label="Username" name="text" id="createdAt">
                                </div>
                            </div>
                        </div>
                    </div>
                    <hr>
                    <div class="col-12">
                        <div class="filter__group">
                            <label for="category" class="filter__title d-inline-block" data-bs-toggle="tooltip"
                                   data-bs-placement="top"
                                   data-bs-title="Tìm kiếm theo phân loại, nếu không có sẽ tìm tất cả">Phân loại sản
                                phẩm</label>
                            <div class="filter__radio-list">
                                <select id="category" name="categoryId" multiple>
                                    <c:forEach items="${categoryList}"
                                               var="category">
                                        <option class="filter__input filter__radio"
                                                value="${category.id}"> ${category.nameType}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <hr>
                    <div class="col-12">
                        <div class="filter__group">
                            <label for="moneyRange" class="filter__title d-inline-block" data-bs-toggle="tooltip"
                                   data-bs-placement="top"
                                   data-bs-title="Tìm kiếm mức giá của sản phẩm">Mức giá</label>
                            <input type="text" id="moneyRange" name="moneyRange" value=""/>
                        </div>
                    </div>
                    <hr>
                    <div class="col-12">
                        <div class="filter__group">
                            <label for="size" class="filter__title d-inline-block" data-bs-toggle="tooltip"
                                   data-bs-placement="top"
                                   data-bs-title="Tìm kiếm kích cỡ sản phẩm, nếu không chọn sẽ tìm tất cả">Kích
                                cỡ</label>
                            <select id="size" name="size" multiple>
                                <c:forEach items="${sizeList}" var="item">
                                    <option name="size" value="${item.nameSize}"
                                            class="filter__input filter__radio"> ${item.nameSize}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <hr>
                    <div class="col-12">
                        <div class="filter__group">
                            <label for="color" class="filter__title d-inline-block" data-bs-toggle="tooltip"
                                   data-bs-placement="top"
                                   data-bs-title="Tìm kiếm theo màu sắc, nếu không có sẽ tìm tất cả">Màu sắc</label>
                            <select id="color" name="color" multiple>
                                <c:forEach items="${colorList}" var="item">
                                    <option name="color" value="${item.codeColor}">${item.codeColor}</option>
                                </c:forEach>
                            </select>
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
<!-- Modal -->
<div class="modal fade " id="modal-create" tabindex="-1" aria-labelledby="modal-label" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable modal-xl">
        <form id="form__add" class="modal-content needs-validation ">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="modal-label">Thêm sản phẩm</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body" style="max-height: 80vh">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-6">
                            <label for="code" class="form-label" data-bs-toggle="tooltip" data-bs-placement="top"
                                   data-bs-title="Tên sản phẩm"> Tên sản phẩm</label>
                            <input type="text" class="form-control" name="name" id="code" value="">
                            <div class="valid-feedback">

                            </div>
                        </div>
                        <div class="col-6">
                            <label for="idCategory" class="form-label text-nowrap">Phân loại sản phẩm</label>
                            <select id="idCategory" name="idCategory" class="form-select" aria-label="Chọn">
                                <c:forEach items="${categoryList}"
                                           var="category">
                                    <option value="${category.id}"> ${category.nameType}</option>
                                </c:forEach>
                            </select>
                            <div class="valid-feedback">

                            </div>
                        </div>
                        <div class="col-6 mt-2">
                            <label for="originalPrice" class="form-label">Giá bán</label>
                            <input type="text" class="form-control" name="originalPrice" id="originalPrice" value="">
                            <div class="valid-feedback">

                            </div>
                        </div>
                        <div class="col-6 mt-2">
                            <label for="salePrice" class="form-label">Giá giảm</label>
                            <input type="text" class="form-control" name="salePrice" id="salePrice" value="">
                            <div class="valid-feedback">

                            </div>
                        </div>
                        <div class="col-12 mt-2">
                            <label for="description" class="form-label">Mô tả</label>
                            <div id="editor"></div>
                            <textarea hidden="hidden" name="description" id="description" cols="30"
                                      rows="10"></textarea>
                            <div class="valid-feedback">

                            </div>
                        </div>
                        <div class="col-6 mt-4">
                            <!--Size-->
                            <div class=" form__block border border-1 rounded p-2">
                                <div class="d-flex justify-content-between align-content-center">
                                    <h2 class="h4" data-bs-toggle="tooltip" data-bs-placement="top" data-bs-title="Mỗi sản phẩm có ít nhất một kích thước, mỗi kích thước gồm tên kích thước và giá của kích
                                           thước đó. Giá kích thước sẽ được cộng cùng với giá sản phẩm ở trên">Kích
                                        thước</h2>
                                    <div id="form__add-size" class="btn btn-primary ">Thêm size</div>
                                </div>
                                <div id="form__size" class="container-fluid">
                                    <div class="row mt-2 py-2">
                                        <div class="col-4">Tên kích thước</div>
                                        <div class="col-4">Giá</div>
                                        <div class="col-4">
                                        </div>
                                    </div>
                                    <div class="row align-items-center mt-2" data-size-id="">
                                        <div class="col-4 form__label">
                                            <input type="text" name="nameSize[]" class="form-control">
                                            <div class="valid-feedback">

                                            </div>
                                        </div>
                                        <div class="col-4 form__label">
                                            <input type="text" name="sizePrice[]" class="form-control">
                                            <div class="valid-feedback">

                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-6 mt-4">
                            <!--Color -->
                            <div class="form__label form__block border border-1 rounded p-2">
                                <div class="d-flex justify-content-between align-content-center ">
                                    <h2 class="h4" data-bs-toggle="tooltip" data-bs-placement="top" data-bs-title="Màu sắc của sản phẩm, mỗi sản phẩm phải có ít nhất 1 màu, mã màu được lưu dưới dạng mã
                                       HEX">Màu sắc có sẵn</h2>
                                    <div id="form__add-color" class="btn btn-primary">Thêm màu sắc</div>
                                </div>
                                <div id="form__color" class="d-flex flex-wrap mt-2">
                                    <div data-color-id
                                         class="d-inline-flex align-items-center gap-1 p-1 border border-1 round mb-1 me-2">
                                        <input id="color-input" name="color" hidden="hidden" type="text">
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-12 mt-4">
                            <h2 class="mb-2 d-inline-block" data-bs-toggle="tooltip" data-bs-placement="top"
                                data-bs-title="Hình ảnh của sản phẩm, ảnh đầu tiên sẽ là ảnh nền của sản phẩm ">Hình
                                ảnh</h2>
                            <input type="file" id="image" name="image"/>
                            <div class="valid-feedback"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                <button type="submit" class="btn btn-primary">Lưu</button>
            </div>
        </form>
    </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/froala-editor/4.0.7/js/languages/vi.js"></script>
<script type="module" src="/js/admin/adminProducts.js"></script>
</body>
</html>