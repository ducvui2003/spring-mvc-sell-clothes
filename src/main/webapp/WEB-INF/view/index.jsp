<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <jsp:include page="common/commonLink.jsp"/>
    <link rel="stylesheet" href="<c:url value="/assets/css/home.css" />">
    <title>Trang chủ</title>
</head>

<body>
<!--Header-->
<c:import url="common/header.jsp" charEncoding="UTF-8"/>
<!--Main: chứa nội dung chính, các section như giới thiệu sản phầm, các cổ đông,...-->
<!--Hero-->
<main id="main" class="animate__animated animate__zoomIn">
    <div class="hero">
        <img src="<c:url value="/assets/img/hero__img.png" />" alt="" class="hero__img">
        <div class="hero__slogan">
            <h1>Change Your Styles Now</h1>
            <p>Cùng chúng tôi tạo nên thiết kế khác biệt cho quần áo của bạn</p>
            <a href="<c:url value="/product/productBuying" />"
               class="hero__button button button--hover hvr-radial-out">
                Bắt đầu đặt may
            </a>
        </div>
    </div>
    <div class="container-xl">
        <div class="mt-3 p-5 search">
            <div class="form-inline my-2 my-lg-0 d-flex">
                <input style="z-index: 2;" class="search__inp form-control mr-sm-2 p-3 me-2" type="search"
                       placeholder="Search"
                       aria-label="Search" name="keyword">
                <button class="search__btn btn btn-outline-success my-2 my-sm-0 ps-4 pe-4 hvr-rectangle-out"
                        type="submit">
                    <i class="fa-solid fa-magnifying-glass"></i>
                </button>
            </div>
            <ul class="search__box shadow"></ul>
        </div>

        <div id="slider__category--section">
            <div class="slider__container">
                <div class="slider__items">
                    <c:forEach var="item" items="${listSlider}">
                        <img class="slider__item"
                             src="${item}"
                             alt=""/>
                    </c:forEach>
                </div>
                <div class="navigation__button nav__prev hvr-bounce-in">
                    <i class="fa-solid fa-chevron-left"></i>
                </div>
                <div class="navigation__button nav__next hvr-bounce-in">
                    <i class="fa-solid fa-chevron-right"></i>
                </div>
                <div class="carousel__indicators">
                    <div class="indicator active"></div>
                    <div class="indicator"></div>
                    <div class="indicator"></div>
                    <div class="indicator"></div>
                </div>
            </div>
            <div class="category__container category__items">
                <c:forEach step="1" var="entry" items="${categorySlider}">
                    <div class="category__item">
                        <p class="item__text">${entry.key}</p>
                        <img class="item__image" src="${entry.value}" alt="${entry.key}"/>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <div class="popular__section container-xl">
        <div class="popular__title">
            <h2 class="section__title">Sản phẩm thịnh hành</h2>
            <a class="see__more hvr-forward" href="<c:url value="/trendingProducts" />">Xem
                thêm
            </a>
        </div>
        <div class="product__wrapper">
            <button class="left__button"><i
                    class="fa-solid fa-arrow-left"></i></button>
            <div class="product__items">
                <c:forEach step="1" varStatus="status" var="product" items="${list6TrendingProducts}">
                    <div class="product__item">
                        <div class="product__content">
                            <div class="image--tag">
                                <img src="${product.thumbnail}" alt=""/>
                                <span class="product__tag"
                                      data-style="${status.index % 2== 1 ? "popular" : "guaranteed"}">
                                        ${status.index % 2== 1 ? "Thịnh hành" : "Phổ biến"}
                                </span>
                                <form action="/api/cart/add"
                                      class="action__bar" method="post">
                                    <input type="hidden"
                                           name="productId"
                                           value="${product.id}">
                                    <button type="submit"
                                            class="add__cart"><i
                                            class="fa-solid fa-cart-shopping"></i>
                                    </button>
                                    <a class="see__detail"
                                       target="_blank"
                                       href="/showProductDetail?id=${product.id}">
                                        <i class="fa-solid fa-eye"></i>
                                    </a>
                                </form>
                            </div>

                            <div class="product__info">
                                <a class="product__name" target="_blank"
                                   href="/product/${product.id}">${product.name}
                                </a>
                                <div class="product__review">
                                    <div class="review__icon">
                                        <c:forEach var="star" begin="1" end="5">
                                            <c:choose>
                                                <c:when test="${product.rating < 5 }">
                                                    <i class="fa-solid fa-star icon__item"></i>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="fa-regular fa-star icon__item"></i>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </div>
                                    <a class="number__turns--ratting"
                                       href="/showProductDetail?id=${product.id}"> ${product.reviewCount}
                                        nhận xét
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <button class="right__button">
                <i class="fa-solid fa-arrow-right"></i>
            </button>
        </div>
    </div>

    <div class="new__section container-xl">
        <div class="new__title">
            <h2 class="section__title">Sản phẩm mới</h2>
            <a class="see__more hvr-forward" href="<c:url value="/newProducts" />">Xem thêm
            </a>
        </div>
        <div class="product__wrapper">
            <button class="left__button"><i
                    class="fa-solid fa-arrow-left"></i></button>
            <div class="product__items">
                <c:forEach var="product" items="${list6NewProducts}">
                    <div class="product__item">
                        <div class="product__content">
                            <div class="image--tag">
                                <img src="${product.thumbnail}" alt="${product.name}">

                                <span class="product__tag" data-style="popular">Thịnh hành</span>
                                <form action="/api/cart/add"
                                      class="action__bar" method="post">
                                    <input type="hidden"
                                           name="productId"
                                           value="${product.name}>">
                                    <button type="submit"
                                            class="add__cart"><i
                                            class="fa-solid fa-cart-shopping"></i>
                                    </button>
                                    <a class="see__detail"
                                       target="_blank"
                                       href="/showProductDetail?id=${product.id}">
                                        <i class="fa-solid fa-eye"></i>
                                    </a>
                                </form>
                            </div>

                            <div class="product__info">
                                <a class="product__name" target="_blank"
                                   href="/showProductDetail?id=${product.id}">${product.name}
                                </a>
                                <div class="product__review">
                                    <div class="review__icon">
                                        <c:forEach var="star" begin="1" end="5">
                                            <c:choose>
                                                <c:when test="${product.rating < 5 }">
                                                    <i class="fa-solid fa-star icon__item"></i>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="fa-regular fa-star icon__item"></i>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </div>
                                    <a class="number__turns--ratting"
                                       href="/showProductDetail?id=${product.id}"> ${product.reviewCount}
                                        nhận xét
                                    </a>
                                </div>

                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <button class="right__button">
                <i class="fa-solid fa-arrow-right"></i>
            </button>
        </div>
    </div>
    <div id="discovery__us--section" class="container-xl">
        <div class="discovery__container">
            <h2 class="section__title">Bạn có thể khám phá được điều gì ở chúng tôi?</h2>
            <div class="discovery__content">
                <div class="disco_thing hvr-underline-from-left">
                    <p>50+</p>
                    <p>Mẫu đồ bạn có thể đặt may</p>
                </div>
                <div class="disco_thing hvr-underline-from-left">
                    <p>1,000+</p>
                    <p>Khách hàng yêu cầu đặt may mỗi tháng</p>
                </div>
                <div class="disco_thing hvr-underline-from-left">
                    <p>50+</p>
                    <p>Mẫu đồ được gia công liên tục</p>
                </div>
            </div>
        </div>
    </div>
    <div class="step__guide--section container-xl">
        <h2 class="section__title">Cách bước để bạn có thể đặt may một mẫu đồ</h2>
        <div class="guide__content row">
            <div class="col hvr-grow-shadow">
                <div class="step__item">
                    <img src="${stepGuide['1']}"/>
                    <div class="description_step">
                        <span>Bước 1</span>
                        <p>Chọn một mẫu đồ mà bạn ưng ý</p>
                    </div>
                </div>
            </div>
            <div class="col hvr-grow-shadow">
                <div class="step__item">
                    <img src="${stepGuide['2']}"/>
                    <div class="description_step">
                        <span>Bước 2</span>
                        <p>Tùy chọn size và kiểu dáng phù hợp với nhu cầu của bạn</p>
                    </div>
                </div>
            </div>
            <div class="col hvr-grow-shadow">
                <div class="step__item">
                    <img src="${stepGuide['3']}"/>
                    <div class="description_step">
                        <span>Bước 3</span>
                        <p>Tiến hành đặt may và thanh toán</p>
                    </div>
                </div>
            </div>
            <div class="col hvr-grow-shadow">
                <div class="step__item">
                    <img src="${stepGuide['4']}"/>
                    <div class="description_step">
                        <span>Bước 4</span>
                        <p>Chờ nhận hàng sau khi bạn đã thanh toán thành công</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="modal_hidden_search__box"></div>
</main>
<!--Footer-->
<c:import url="common/footer.jsp"/>
<script src="<c:url value="/js/home.js" />"></script>
<script type="text/javascript">
    // function addToCartAjax() {
    //     $(document).ready(function () {
    //         $('.action__bar').each(function (index, actionBar) {
    //             $(actionBar).on('submit', function (event) {
    //                 event.preventDefault();
    //                 const form = $(actionBar);
    //                 let productId = form.find('input[name="productId"]').val();
    //                 $.ajax({
    //                     type: form.attr('method'),
    //                     url: form.attr('action'),
    //                     data: {productId: productId},
    //                     success: function (response) {
    //                         let addToCartSuccessHTML = `<div class="notification__cart">
    //                                                     <div class="status__success">
    //                                                         <span>
    //                                                         <i class="fa-solid fa-circle-check icon__success"></i>
    //                                                         Đã thêm vào giỏ hàng thành công
    //                                                         </span>
    //                                                         <span onclick="handleCloseNotificationCart()">
    //                                                         <i class="fa-solid fa-xmark close__notification"></i>
    //                                                         </span>
    //                                                     </div>
    //                                                     <a class="view__cart" href="/public/user/shoppingCart.jsp">Xem giỏ hàng và thanh toán</a>
    //                                                 </div>`;
    //                         $('.cart__wrapper').append(addToCartSuccessHTML)
    //                         $('.qlt__value').text(response);
    //                     },
    //                     error: function (error) {
    //                         console.error('Lỗi khi thêm sản phẩm vào giỏ hàng', error);
    //                     }
    //                 })
    //             })
    //         })
    //     })
    // }
    //
    // let ulCom = $('.search__box')[0]
    //
    // function handelSearch() {
    //     let debounceTimer;
    //     $('.search__inp').keydown(function () {
    //
    //         var formData = $(this).serialize();
    //
    //         clearTimeout(debounceTimer);
    //
    //         debounceTimer = setTimeout(() => {
    //             $.ajax({
    //                 url: '/searchProduct',
    //                 method: 'GET',
    //                 data: formData,
    //                 success: function (response) {
    //                     console.log(response)
    //                     ulCom.innerHTML = ""
    //                     for (let i = 0; i < response.length; ++i) {
    //                         const li = document.createElement("li")
    //                         li.setAttribute("class", "mb-1")
    //                         const a = document.createElement("a")
    //                         a.setAttribute("class", "text-dark mb-2 search__box-item")
    //                         a.setAttribute("href", "/")
    //                         a.innerText = response[i].name
    //                         li.appendChild(a)
    //                         ulCom.appendChild(li)
    //                     }
    //                 },
    //                 error: function (xhr, status, error) {
    //                     console.error(xhr.responseText);
    //                 }
    //             })
    //         }, 800);
    //     })
    // }
    //
    // $('.search__inp').on('focus', function () {
    //     $('.search__box').addClass('focused');
    // });
    //
    // $('.search__inp').on('blur', function () {
    //     $('.search__box').removeClass('focused');
    // });
    //
    // $('#header').addClass("animate__animated animate__backInDown")
    // handelSearch()
    // addToCartAjax()
</script>
</body>

</html>