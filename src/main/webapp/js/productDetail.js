import {BASE_URL, http} from "./base.js";

$(document).ready(() => {
    // Animation for head title
    const idTitle = '#product__name'
    new TypeIt(idTitle, {
        speed: 50,
        waitUntilVisible: true,
    }).go

// Animation for suggest title
    const idSuggest = '#suggest__title'
    new TypeIt(idSuggest, {
        speed: 50,
        waitUntilVisible: true,
    }).go


// Form quantity
    var quantityCurrent = 1;
    var quantityInput = document.querySelector("#quantity");
    var quantityDecrease = document.querySelector(".form___quantity--decrease");
    var quantityIncrease = document.querySelector(".form___quantity--increase");
    quantityDecrease.onclick = function () {
        if (quantityCurrent > 1) {
            quantityCurrent -= 1;
        }
        quantityInput.value = quantityCurrent;
    }
    quantityIncrease.onclick = function () {
        quantityCurrent += 1;
        quantityInput.value = quantityCurrent;
    }

// show/hide description and review product
    function showHideDescAndReview() {
        var productDesc = document.querySelector(".product__desc");
        var productReview = document.querySelector(".product__review");
        var productPage = document.querySelectorAll(".product__page");
        var productPageDesc = productPage[0];
        var productPageReview = productPage[1];

        productPageDesc.onclick = function () {
            this.classList.add("product__page--clicked");
            productPageReview.classList.remove("product__page--clicked");
            productReview.style.display = "none";
            productDesc.style.display = "block";
        }
        productPageReview.onclick = function () {
            this.classList.add("product__page--clicked");
            productPageDesc.classList.remove("product__page--clicked");
            productDesc.style.display = "none";
            productReview.style.display = "block";
        }
        var reviewParam = document.querySelectorAll(".review__para");
        reviewParam.forEach(function (element) {
            element.onclick = function () {
                this.classList.toggle("line-clamp");
            }
        });
    }

    function handleSlider() {
        let imgCurrent;
        const productImg = document.querySelector(".product__img");
        const productImgItems = document.querySelectorAll(".product__img-item");
        productImgItems.forEach(function (productItem, index) {
            productItem.onclick = function () {
                if (!this.classList.contains("product__img-item--clicked")) {
                    this.classList.add("product__img-item--clicked");
                    imgCurrent = productItem.querySelector("img").src;
                    productImgItems.forEach(function (productItemOther, indexOther) {
                        if (indexOther !== index) {
                            productItemOther.classList.remove("product__img-item--clicked");
                        }
                    });

                    productImg.src = imgCurrent;
                }
            }
        })
    }

    const sizePriceShow = document.querySelector(".size__price");
    const vndFormat = Intl.NumberFormat("vi-VI", {
        style: "currency",
        currency: "VND",
    });

    function addSizePrice(input) {
        sizePriceShow.innerText = "+ " + vndFormat.format(input.getAttribute("size-price"));
    }

    var formObj = new Validation({
        formSelector: "#form__product",
        formBlockClass: "form__block",
        errorSelector: ".form__error",
        rules: [
            Validation.isRequiredRadio(`input[name="color"]`),
            Validation.isRequiredRadio(`input[name="size"]`),
        ],
        submitSelector: ".form__submit--add",
        onSubmit: addToCartAjax
    })

    // Giỏ hàng
    function addToCartAjax() {
        console.log("call method ajax")
        const form = $('#form__product');
        let productId = $(form).find('input[name=productId]').val();
        let sizeId = $(form).find('input[name=size]:checked').val();
        let colorId = $(form).find('input[name=color]:checked').val();
        let quantity = $(form).find('input[name=quantity]').val();
        http({
            url: `${BASE_URL}/api/cart/add`,
            type: "POST",
            data: {
                productId: productId,
                sizeId: sizeId,
                colorId: colorId,
                quantity: quantity
            },
        }).then(() => {
            console.log("run ajax success")
            let addToCartSuccessHTML = `<div class="notification__cart">
                                                                <div class="status__success">
                                                                    <span><i class="fa-solid fa-circle-check icon__success"></i>Đã thêm vào giỏ hàng thành công</span>
                                                                    <span onclick="handleCloseNotificationCart()"><i class="fa-solid fa-xmark close__notification"></i></span>
                                                                </div>
                                                                <a class="view__cart" href="/cart">Xem giỏ hàng và thanh toán</a>
                                                            </div>`;
            $('.cart__wrapper').append(addToCartSuccessHTML)
            const quantityItemCartElement = $(".qlt__value")
            const quantityItemCart = parseInt(quantityItemCartElement.text()) || 0;
            quantityItemCartElement.text(quantityItemCart + 1);
        })
    }

    const moreProducts = $("#product-related");

    const getMoreProducts = () => {
        $.ajax({
            url: '/api/product/filter?size=4',
            type: 'GET',
            success: function (response) {
                showProduct(response.content)
            }
        })
    }

    const showProduct = (products) => {
        const html = products.map(function (item) {
            const star = item.rating
            const noStar = []
            const hasStar = []

            for (let starA = 0; starA < star; starA++) {
                hasStar[starA] = (`<i class="fa-solid fa-star"></i>`)
            }
            for (let starB = 0; starB < 5 - star; starB++) {
                noStar[starB] = (`<i class="fa-regular fa-star"></i>`)
            }

            const noStarHtml = noStar.map((value, index) => value)
            const noStarResult = noStarHtml.join("")

            const hasStarHtml = hasStar.map((value, index) => value)
            const hasStarResult = hasStarHtml.join("")

            const reviewCounts = item.reviewCount
            const id = item.id
            const name = item.name
            const originalPrice = item.originalPrice
            const salePrice = item.salePrice

            const thumbnail = item.thumbnail

            const vndFormat = Intl.NumberFormat("vi-VI", {
                style: "currency",
                currency: "VND",
            });
            return `
                    <div class="product__item hvr-grow-shadow">
                        <a href="/product/${id}">
                            <img src="${thumbnail}" class="product__img" alt="" loading="lazy"/>
                        </a>
                        
                        <div class="product__info">
                            <a class="product__name" target="_blank" href="/product/${id}">${name}</a>
                        
                            <div class="product__review">
                                <div class="product__review-stars">
                                    ${hasStarResult}
                                    ${noStarResult}
                                </div>
                                
                                   <a class="product__review-num" target="_blank" href="/product/${id}">
                                       ${reviewCounts} nhận xét
                                   </a>
                            </div> 
                        
                            <span class="product__price">
                                <strong class="product__price--original">${vndFormat.format(originalPrice)}</strong>
                                <strong class="product__price--sale">${vndFormat.format(salePrice)}</strong>
                            </span>
                        </div>
                    </div>`
        }).join("")
        moreProducts.html(html)
    }

    function init() {
        showHideDescAndReview();
        handleSlider();
        getMoreProducts();
    }

    init();

})