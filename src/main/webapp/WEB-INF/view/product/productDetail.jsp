<%@ page import="java.util.List" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="en">

<head>
    <link href="https://cdn.jsdelivr.net/npm/@splidejs/splide@4.1.4/dist/css/splide.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value="/assets/css/splide/index.css" />">
    <jsp:include page="/commonLink"/>
    <link rel="stylesheet" href="<c:url value="/assets/css/productDetail.css"/>">
    <title>${product.name}</title>
</head>

<body>
<c:import url="/header"/>
<main class="main">
    <section class="product__detail">
        <div class="container-xl">
            <div class="row">
                <div class="col-6 ">
                    <div id="main-slider" class="splide custom-splide mb-3">
                        <div class="splide__track">
                            <ul class="splide__list">
                                <c:forEach var="image" items="${product.images}">
                                    <li class="splide__slide">
                                        <img src="${image}" loading="lazy" alt="${product.name}">
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                    <div id="thumbnail-slider" class="splide custom-thumbnail-slider">
                        <div class="splide__track">
                            <ul class="splide__list">
                                <c:forEach var="image" items="${product.images}">
                                    <li class="splide__slide">
                                        <img src="${image}" loading="lazy" alt="">
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="offset-1 col-5">
                    <div class="product__info">
                        <form action="${pageContext.request.contextPath}/api/cart/add" method="post" id="form__product"
                              class="product__form">
                            <h1 class="product__name" id="product__name">${product.name}</h1>
                            <input type="text" hidden="hidden" name="productId" value="${image.id}">

                            <c:forEach var="star" begin="1" end="5">
                                <c:choose>
                                    <c:when test="${product.rating < 5 }">
                                        <i class="fa-solid fa-star product__star"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fa-regular fa-star product__star"></i>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>

                            <div class="product__price-wrapper">
                                <fmt:formatNumber value="${product.originalPrice}" type="currency"
                                                  currencyCode="VND" var="originalPrice"/>
                                <fmt:formatNumber value="${product.salePrice}" type="currency"
                                                  currencyCode="VND" var="salePrice"/>
                                <c:choose>
                                    <c:when test="${product.salePrice == 0}">
                                        <p class="product__price product__price--sale hvr-grow">
                                                ${originalPrice}</p>
                                    </c:when>

                                    <c:otherwise>
                                        <p class="product__price product__price--sale hvr-grow">
                                                ${originalPrice}</p>
                                        <p class="product__price product__price--base hvr-bubble-left">
                                                ${salePrice}</p>
                                    </c:otherwise>
                                </c:choose>

                            </div>

                            <div class="separate"></div>

                            <div class="form__block">
                                <p class="form__title">Màu sắc</p>
                                <div class="form__choose-color">
                                    <c:forEach var="color" items="${product.colors}">
                                        <label class="form__color-check shadow rounded"
                                               style="background-color: ${color.code}">
                                            <input type="radio" name="color" hidden="hidden"
                                                   value="${color.code}">
                                        </label>
                                    </c:forEach>
                                </div>
                                <p class="form__error"></p>
                            </div>

                            <div class="separate"></div>

                            <p class="form__title">Kích thước</p>
                            <div class="form__block">
                                <div class="form__size-list">
                                    <c:forEach var="size" items="${product.sizes}">
                                        <div class="form__size-item hvr-skew-forward">
                                            <label> <input type="radio" name="size" class="form__radio"
                                                           hidden="hidden" value="${size.name}"
                                                           size-price="${size.price}"
                                                           onclick="addSizePrice(this)"/> ${size.name}
                                            </label>
                                        </div>
                                    </c:forEach>
                                </div>

                                <span class="size__price"></span> <span class="form__error"></span>
                            </div>

                            <div class="separate"></div>

                            <p class="form__title ">Số lượng</p>
                            <div class="form__block">
                                <div class="form__quantity">
                                    <div class="form__quantity-inner">
                                        <div class=" form___quantity-btn form___quantity--decrease hvr-bounce-out">
                                        </div>

                                        <input id="quantity" type="text" name="quantity" value="1" readonly>

                                        <div class=" form___quantity-btn form___quantity--increase hvr-bounce-in">
                                        </div>
                                    </div>

                                    <p class="form__error"></p>
                                </div>
                            </div>
                            <%--                            <a href="<c:url value="/showProductOrder?id="/><%=product.getId()%>"--%>
                            <%--                               type="submit"--%>
                            <%--                               class="form__submit form__submit--order button text-secondary"--%>
                            <%--                               data="Đặt may theo số đo">--%>
                            <%--                            </a>--%>

                            <button type="submit" class="form__submit form__submit--add button "
                                    data="Thêm vào giỏ hàng">
                            </button>
                        </form>
                    </div>
                </div>
                <div class="col-12 mb-5">
                    <hr/>
                    <div class="product__desc-review">
                        <div class="product__page product__page--clicked hvr-float-shadow">Mô tả</div>
                        <div class="product__page hvr-float-shadow">Đánh giá</div>
                    </div>

                    <!--Desc product-->
                    <div class="product__desc">
                        <h3 class="desc__title">Thông tin sản phẩm</h3>
                        <p class="desc__text">${product.description}
                        </p>
                    </div>

                    <!--Reviews-->
                    <div class="product__review">
                        <%--                        <c:choose> <c:when test="${not empty requestScope.listReview}">--%>
                        <%--                            <div class="review__list">--%>
                        <%--                                <%for (Review review : (List<Review>) request.getAttribute("listReview")) {%><%--%>
                        <%--                                User user = userFatory.getUserByIdProductDetail(review.getOrderDetailId());%>--%>
                        <%--                                <article class="review">--%>
                        <%--                                    <div class="review__avatar">--%>
                        <%--                                        <img src="<%=CloudinaryUploadServices.getINSTANCE().getImage("user", user.getAvatar())%>"--%>
                        <%--                                             alt="<%=user.getAvatar()%>"--%>
                        <%--                                             loading="lazy">--%>
                        <%--                                    </div>--%>
                        <%--                                    <div class="review__account">--%>
                        <%--                                        <h4 class="review__name"><%=user.getFullName()%>--%>
                        <%--                                        </h4>--%>
                        <%--                                        <ul class="review__stars">--%>
                        <%--                                            <%for (int starA = 1; starA <= review.getRatingStar(); starA++) {%>--%>
                        <%--                                            <li class="review__star review__start--archive">--%>
                        <%--                                                        <%}%>--%>

                        <%--                                                <c:if test="<%=review.getRatingStar() < 5%>"> <%for(int starB  = 1; starB <= 5- review.getRatingStar();starB++){%>--%>
                        <%--                                            <li class="review__star "></li>--%>
                        <%--                                            <%}%>--%>
                        <%--                                            </c:if>--%>
                        <%--                                            <fmt:formatDate var="reviewDate" value="<%=review.getReviewDate()%>"--%>
                        <%--                                                            type="date"--%>
                        <%--                                                            pattern="dd/MM/yyyy"/>--%>

                        <%--                                            <span class="review__date"><%=review.getReviewDate()%></span>--%>
                        <%--                                        </ul>--%>
                        <%--                                        <p class="review__para line-clamp"><%=review.getFeedback()%>--%>
                        <%--                                        </p>--%>
                        <%--                                    </div>--%>
                        <%--                                </article>--%>
                        <%--                                <%}%>--%>
                        <%--                            </div>--%>

                        <%--                            <ul class="paging">--%>
                        <%--                            </ul>--%>
                        <%--                        </c:when>--%>

                        <%--                            <c:otherwise>--%>
                        <%--                                <p class="review__empty">--%>
                        <%--                                    Hãy trở thành người đầu tiên đánh giá sản phẩm.--%>
                        <%--                                </p>--%>
                        <%--                            </c:otherwise> </c:choose>--%>

                    </div>
                </div>

                <div class="col-12">
                    <div class="product__related">
                        <h3 id="suggest__title">Các sản phẩm liên quan</h3>
                        <a href="/product"
                           class="product__more hvr-forward">Xem thêm
                            <i class="product__more-icon fa-solid fa-arrow-right"></i></a>
                    </div>

                    <div id="product-related" class="product__list">
                    </div>
                </div>
            </div>
        </div>
    </section>
</main>
<!--Footer-->
<c:import url="/footer"/>
<script src="https://cdnjs.cloudflare.com/ajax/libs/typeit/5.0.2/typeit.min.js"
        integrity="sha512-izh01C0sD66AuIVp4kRaEsvCSEC5bgs3n8Bm8Db/GhqJWei47La76LGf8Lbm8UHdIOsn+I7SxbeVLKb1k2ExMA=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.9.0/slick.min.js"
        integrity="sha512-HGOnQO9+SP1V92SrtZfjqxxtLmVzqZpjFFekvzZVWoiASSQgSr4cw9Kqd2+l8Llp4Gm0G8GIFJ4ddwZilcdb8A=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<article class="dialog__size-guide"></article>
<script src="<c:url value="/js/validateForm.js"/>"></script>
<script src="<c:url value="/js/productDetail.js"/>"></script>
<script type="module" src="<c:url value="/js/slick.js" />"></script>
</body>
</html>