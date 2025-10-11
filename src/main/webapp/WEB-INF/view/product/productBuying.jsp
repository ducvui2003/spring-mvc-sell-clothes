<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="/commonLink"/>
    <link rel="stylesheet" href=<c:url value="/assets/css/productBuying.css"/>>
    <title>Gian hàng</title></head>
<body>
<c:import url="/header"/>
<main class="main">
    <div class="modal_hidden_search__box"></div>
    <section class="products">
        <div class="container-xl">
            <div class="row ">
                <div class="col-3 animate__animated animate__slideInUp tab_right">
                    <form class="form__filter" id="form__filter">
                        <div class="filter__group"><span class="filter__title">Phân loại sản phẩm</span>
                            <div id="category" class="filter__radio-list">

                            </div>
                        </div>
                        <span class="filter__separate"></span>
                        <div class="filter__group"><span class="filter__title">Mức giá</span>
                            <div id="money" class="filter__radio-list">

                            </div>
                        </div>
                        <span class="filter__separate"></span>
                        <div class="filter__group"><span class="filter__title">Kích cỡ</span>
                            <div id="size" class="filter__radio-list">

                            </div>
                        </div>
                        <span class="filter__separate"></span>
                        <div class="filter__group"><span class="filter__title">Màu sắc</span>
                            <div id="color" class="filter__color-list">

                            </div>
                        </div>
                        <button class="filter__submit button--hover button p-2" type="submit">Lọc</button>
                    </form>
                </div>
                <div class="col-9 animate__animated animate__fadeInRight tab_left">
                    <div class="product__notification"></div>
                    <div id="product__list" class="product__list">
                        <%--  sản phẩm được hiển thị ở đây--%>
                    </div>
                </div>
            </div>
        </div>

        <ul class="paging"></ul>
    </section>
</main>
<c:import url="/footer"/>
</body>

<!--tippy tooltip-->
<script src="https://unpkg.com/popper.js@1"></script>
<script src="https://unpkg.com/tippy.js@5/dist/tippy-bundle.iife.js"></script>

<script type="module" src="<c:url value="/js/productBuying.js"/>"></script>

</html>