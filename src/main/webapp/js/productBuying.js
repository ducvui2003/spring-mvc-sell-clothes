import {http} from "./base.js";

$(document).ready(() => {
    const formFilter = $("#form__filter");
    const category = formFilter.find("#category");
    const money = formFilter.find("#money");
    const size = formFilter.find("#size");
    const color = formFilter.find("#color");
    // các sản phẩm khi vừa vào trang sản phẩm
    callAjaxToPage(1);
    handleFilterProduct(1)
    handleSearch()

    http({
        url: '/api/product/form-filter',
        method: 'GET'
    }).then(data => {
        const categoryHTML = data.categories.map(category => (
            `
            <label class="filter__radio-item">
                <input name="categoryId" type="checkbox" class="filter__input filter__radio"
                       hidden="hidden" value="${category.id}">
                <span class="filter-radio__icon-wrapper">
    
                    <i class="fa-solid fa-check filter-radio__icon"></i>
                </span> ${category.nameType}
            </label>
            `
        )).join('');
        category.html(categoryHTML);

        const moneyHTML = data.moneys.map(money => (
            `
            <label class="filter__radio-item">
                <input name="moneyRange" type="checkbox" class="filter__input filter__radio"
                       hidden="hidden" value="${money.from}-${money.to}">
                <span class="filter-radio__icon-wrapper"><i
                        class="fa-solid fa-check filter-radio__icon"></i>
                </span>
                <span class="hvr-skew-forward">
                        ${money.from} - ${money.to}
                </span>
            </label>
            `
        )).join("");
        money.html(moneyHTML);

        const sizeHTML = data.sizes.map(size => (
            `
            <label class="filter__radio-item">
                <input name="size" value="${size.nameSize}" type="checkbox"
                       class="filter__input filter__radio" hidden="hidden">
                <span class="filter-radio__icon-wrapper">
                    <i class="fa-solid fa-check filter-radio__icon"></i>
                </span>
                <span class="hvr-skew-forward">
                        ${size.nameSize}
                </span>
            </label>
            `
        )).join("");
        size.html(sizeHTML);

        const colorHTML = data.colors.map(color => (
            `
           <label class="filter__color-item">
                <input name="color" type="checkbox" value="${color.codeColor}"
                       class="filter__input filter__color" hidden="hidden">
                <span class="filter__color-show shadow rounded hvr-grow"
                      style="background-color: ${color.codeColor}"></span>
            </label>
            `
        )).join("");

        color.html(colorHTML);
    })


// phương thức này truyền vào số trang hiện tại
// sẽ trả về sản phẩm của trang đó
    function callAjaxToPage(pageNumber) {
        $.ajax({
            url: "/api/product/filter",
            type: "GET",
            success: function (data) {
                generationPage(parseInt(pageNumber), data["quantity"])
                showProduct(data.content)
            },
            error: function (error) {
                console.log("loi khong hien thi san pham")
            },
        });
    }

// phương thức này dùng để xử lý sự kiện click vào trang nào đó
// sẽ gọi method callAjaxToPage(currentPage)
    function changePage(aTag) {
        if (aTag) {
            productList.innerHTML = ``
            callAjaxToPage(aTag.innerText)
            // handleFilterProduct(aTag.innerText)
        }
    }

// khi lấy được data của product dưới dạng json thì show ra giao diện
    function showProduct(products) {
        products.forEach(function (item) {
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
            productList.innerHTML += `
            <div class="product__item hvr-grow-shadow">
                <a href="/showProductDetail?id=${id}">
                    <img src="${thumbnail}" class="product__img" alt="" loading="lazy"/>
                </a>
                
                <div class="product__info">
                    <a class="product__name" target="_blank" href="/showProductDetail?id=${id}">${name}</a>
                
                    <div class="product__review">
                        <div class="product__review-stars">
                            ${hasStarResult}
                            ${noStarResult}
                        </div>
                        
                           <a class="product__review-num" target="_blank" href="/showProductDetail?id=${id}">
                               ${reviewCounts} nhận xét
                           </a>
                    </div> 
                
                    <span class="product__price">
                        <strong class="product__price--original">${vndFormat.format(originalPrice)}</strong>
                        <strong class="product__price--sale">${vndFormat.format(salePrice)}</strong>
                    </span>
                </div>
            </div>`
        })
    }

    function showBeforePaging(currentPage, minPage) {
        if (currentPage > 3) {
            productPages.innerHTML += `<a class="page access_page_quickly">...</a>`
            const div = document.createElement('div')
            div.style.width = '200px'
            div.style.height = '50px'
            div.style.overflow = 'auto'

            for (let i = 1; i < minPage; i++) {
                const a = document.createElement('a')
                a.innerText = i
                a.classList.add("page")
                a.onclick = function () {
                    changePage(a)
                }
                div.appendChild(a)
            }

            tippy('.access_page_quickly', {
                content: div,
                placement: 'top-start',
                interactive: true,
                duration: [500, 250],
                arrow: true,
            });
        }
    }

    function generationPage(currentPage, totalPage) {
        var minPage = currentPage - 2
        var maxPage = currentPage + 2;

        if (maxPage > totalPage) {
            maxPage = totalPage
        }
        if (currentPage < 3) {
            minPage = 1;
            maxPage = 5
        }
        productPages.innerHTML = ''
        showBeforePaging(currentPage, minPage)
        for (let i = minPage; i <= maxPage; i++) {
            const a = document.createElement('a');
            if (i === currentPage) {
                a.classList.add("page--current");
            }
            a.classList.add('page')
            a.innerText = i;
            a.onclick = function () {
                changePage(a)
            }
            productPages.appendChild(a);
        }
    }

    function handleFilterProduct(pageNumber) {
        // Bắt sự kiện gửi đi của form lọc
        $('#form__filter').on('submit', function (event) {
            // Ngăn chặn hành vi mặc định của form (chẳng hạn chuyển hướng trang)
            event.preventDefault();
            var formData = $(this).serialize();
            $.ajax({
                url: '/filterProductBuying?page=' + pageNumber,
                type: 'GET',
                data: formData,
                success: function (response) {
                    generationPage(response['page'], response['quantity'])
                    updateProducts(response)
                },
                error: function (err) {
                    console.log(err)
                }
            });
        })
    }

    function updateProducts(response) {
        window.history.pushState('string', '', response["url"]);
        let container = $('.product__list')[0]
        let products = response["products"]
        let content = ''
        if (products.length <= 0) {
            content = '<p class="product__list--empty">Không có sản phẩm nào ứng với bộ lọc </p>'
        } else {
            const vndFormat = Intl.NumberFormat("vi-VI", {
                style: "currency",
                currency: "VND",
            });
            content = products.map(function (product) {
                const contentProduct = product["product"]
                let linkProductDetail = '/showProductDetail?id=' + contentProduct['id']
                let stars = ''
                let noStars = ''

                for (let star = 0; star < product['stars']; star++) {
                    stars += '<i class="fa-solid fa-star"></i>'
                }
                for (let star = 0; star < 5 - product['stars']; star++) {
                    noStars += '<i class="fa-regular fa-star"></i>'
                }

                const salePrice = contentProduct['salePrice']
                const originPrice = contentProduct['originalPrice']
                return `<div class = "product__item" >
                        <a class="product__name" target="_blank" href="${linkProductDetail}">
                            <img src = "${product.images[0].nameImage}" class="product__img" >
                        </a>
                        <div class = "product__info" >
                            <a class="product__name" target="_blank" href="${linkProductDetail}">${contentProduct.name}</a>
                            <div class="product__review">
                                <div class="product__review-stars">${stars} ${noStars}</div>
                                <a class="product__review-num" target="_blank" href="${linkProductDetail}">${product.reviewCounts} nhận xét</a>
                            </div>
                            
                            <span class="product__price">
                                <strong class="product__price--original">${vndFormat.format(originPrice)}</strong>
                                <strong class="product__price--sale">${vndFormat.format(salePrice)}</strong>
                            </span>
                        </div>
                    </div>
            `;
            })
        }
        container.innerHTML = content.join("")

    }

    let ulCom = $('.search__box')[0]

    function handleSearch() {
        let debounceTimer;
        $('.search__inp').keydown(function () {

            var formData = $(this).serialize();
            clearTimeout(debounceTimer);

            debounceTimer = setTimeout(() => {
                $.ajax({
                    url: '/searchProduct',
                    method: 'GET',
                    data: formData,
                    success: function (response) {
                        ulCom.innerHTML = ""
                        for (let i = 0; i < response.length; ++i) {
                            const li = document.createElement("li")
                            li.setAttribute("class", "mb-1")
                            const a = document.createElement("a")
                            a.setAttribute("class", "text-dark mb-2 search__box-item")
                            a.setAttribute("href", `/showProductDetail?id=${response[i].id}`)
                            a.setAttribute("target", "_blank");
                            a.style.cursor = "pointer";
                            a.innerText = response[i].name
                            li.appendChild(a)
                            ulCom.appendChild(li)
                        }
                    },
                    error: function (xhr, status, error) {
                        console.error(xhr.responseText);
                    }
                })
            }, 800);
        })
    }

    $('.search__inp').on('click', function () {
        $('.modal_hidden_search__box').css('display', 'block');
        $('.search__box').addClass('focused');
        // $('.products').css('z-index', '-1');
    });

// $('.products').on('click', function () {
//     $('.search__box').removeClass('focused');
//     // $('.products').css('z-index', '1');
// });

    $('.modal_hidden_search__box').on('click', function () {
        $('.search__box').removeClass('focused');
        $('.modal_hidden_search__box').css('display', 'none');
        // $('.tab_right').css('z-index', '1');
        // $('.tab_left').css('z-index', '1');
    });

    const productList = document.querySelector('.product__list')
    const productPages = document.querySelector('.paging')
})