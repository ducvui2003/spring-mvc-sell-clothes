import {http} from "./base.js";

$(document).ready(() => {
    const SIZE = 9;
    const formFilter = $("#form__filter");
    const category = formFilter.find("#category");
    const money = formFilter.find("#money");
    const size = formFilter.find("#size");
    const color = formFilter.find("#color");

    const productList = $("#product__list")

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
                <input name="sizeId" value="${size.nameSize}" type="checkbox"
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
                <input name="colorId" type="checkbox" value="${color.codeColor}"
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
    function callAjaxToPage(page) {
        $.ajax({
            url: `/api/product/filter?page=${page}&size=${SIZE}`,
            type: "GET",
            success: function (data) {
                handlePage(parseInt(page), data["quantity"])
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
            productList.html("")
            callAjaxToPage(aTag.innerText)
            // handleFilterProduct(aTag.innerText)
        }
    }

// khi lấy được data của product dưới dạng json thì show ra giao diện
    function showProduct(products) {
        productList.html("")
        if (products.length === 0) {
            productList.html(`<p class="product__list--empty">Không có sản phẩm nào</p>`)
            return;
        }
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
        productList.html(html)
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

    function handlePage(currentPage, totalPage) {
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
                url: '/api/product/filter?page=' + pageNumber,
                type: 'GET',
                data: formData,
                success: function (response) {
                    handlePage(response.page, response.totalPage)
                    showProduct(response.content)
                },
                error: function (err) {
                    console.log(err)
                }
            });
        })
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
                            a.setAttribute("href", `/product/${response[i].id}`)
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
    const productPages = document.querySelector('.paging')
})