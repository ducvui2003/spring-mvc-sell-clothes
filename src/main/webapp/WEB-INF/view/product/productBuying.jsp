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
                                <%--                                <c:forEach items="${pageContext.servletContext.getAttribute('categoryList')}"--%>
                                <%--                                           var="category">--%>
                                <%--                                    <label class="filter__radio-item">--%>
                                <%--                                        <input name="categoryId" type="checkbox" class="filter__input filter__radio"--%>
                                <%--                                               hidden="hidden" value="${category.id}">--%>
                                <%--                                        <span class="filter-radio__icon-wrapper">--%>

                                <%--                                            <i class="fa-solid fa-check filter-radio__icon"></i>--%>
                                <%--                                        </span> ${category.nameType}--%>
                                <%--                                    </label>--%>
                                <%--                                </c:forEach>--%>
                            </div>
                        </div>
                        <span class="filter__separate"></span>
                        <div class="filter__group"><span class="filter__title">Mức giá</span>
                            <div id="money" class="filter__radio-list">
                                <%--                                <c:forEach items="${pageContext.servletContext.getAttribute('moneyRangeList')}"--%>
                                <%--                                           var="moneyRange">--%>
                                <%--                                    <fmt:formatNumber value="${moneyRange.from}" type="currency" currencyCode="VND"--%>
                                <%--                                                      var="moneyFrom"/>--%>
                                <%--                                    <fmt:formatNumber value="${moneyRange.to}" type="currency" currencyCode="VND"--%>
                                <%--                                                      var="moneyTo"/>--%>
                                <%--                                    <label class="filter__radio-item">--%>
                                <%--                                        <input name="moneyRange" type="checkbox" class="filter__input filter__radio"--%>
                                <%--                                               hidden="hidden" value="${moneyRange.getFrom()}-${moneyRange.getTo()}">--%>
                                <%--                                        <span class="filter-radio__icon-wrapper"><i--%>
                                <%--                                                class="fa-solid fa-check filter-radio__icon"></i>--%>
                                <%--                                        </span>--%>
                                <%--                                        <span class="hvr-skew-forward">--%>
                                <%--                                                ${moneyFrom} - ${moneyTo}--%>
                                <%--                                        </span>--%>
                                <%--                                    </label> </c:forEach>--%>
                            </div>
                        </div>
                        <span class="filter__separate"></span>
                        <div class="filter__group"><span class="filter__title">Kích cỡ</span>
                            <div id="size" class="filter__radio-list">
                                <%--                                <c:forEach items="${requestScope.sizeList}" var="item">--%>
                                <%--                                    <label class="filter__radio-item">--%>
                                <%--                                        <input name="size" value="${item.nameSize}" type="checkbox"--%>
                                <%--                                               class="filter__input filter__radio" hidden="hidden">--%>
                                <%--                                        <span class="filter-radio__icon-wrapper">--%>
                                <%--                                            <i class="fa-solid fa-check filter-radio__icon"></i>--%>
                                <%--                                        </span>--%>
                                <%--                                        <span class="hvr-skew-forward">--%>
                                <%--                                                ${item.nameSize}--%>
                                <%--                                        </span>--%>
                                <%--                                    </label>--%>
                                <%--                                </c:forEach>--%>
                            </div>
                        </div>
                        <span class="filter__separate"></span>
                        <div class="filter__group"><span class="filter__title">Màu sắc</span>
                            <div id="color" class="filter__color-list">
                                <%--                                <c:forEach items="${requestScope.colorList}" var="item">--%>
                                <%--                                    <label class="filter__color-item">--%>
                                <%--                                        <input name="color" type="checkbox" value="${item.codeColor}"--%>
                                <%--                                               class="filter__input filter__color" hidden="hidden">--%>
                                <%--                                        <span class="filter__color-show shadow rounded hvr-grow"--%>
                                <%--                                              style="background-color: ${item.codeColor}"></span>--%>
                                <%--                                    </label>--%>
                                <%--                                </c:forEach>--%>
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