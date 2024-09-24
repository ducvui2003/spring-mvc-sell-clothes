<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--Header-->
<header id="header">
    <nav class="nav">
        <div class="container-xl">
            <div class="nav__inner">
                <a href="/" class="logo"></a>
                <ul class="nav__list">
                    <li class="nav__item">
                        <a href="/" class="nav__link hvr-grow-shadow">Trang chủ</a>
                    </li>
                    <li class="nav__item">
                        <a href="/product"
                           class="nav__link hvr-grow-shadow">
                            Gian hàng
                        </a>
                    </li>
                    <li class="nav__item">
                        <a href="/contact" class="nav__link hvr-grow-shadow">
                            Liên hệ
                        </a>
                    </li>
                    <li class="nav__item">
                        <a href="/about" class="nav__link hvr-grow-shadow"> Về
                            chúng tôi
                        </a>
                    </li>
                </ul>
                <%--                <c:set var="sessionId" value="${cookie['sessionId'].value}"/>--%>
                <c:set var="auth" value="${sessionScope.user}"/>
                <c:choose>
                    <c:when test="${empty auth}"> <!--cta == call to action-->
                        <div class="nav__cta">
                            <a href="/signIn"
                               class="me-3 nav__button nav__button--signIn hvr-ripple-in">
                                Đăng nhập
                            </a>
                            <a href="/signUp"
                               class="nav__button nav__button--signUp button button button--hover hvr-round-corners
                                    hvr-radial-out">
                                Đăng ký
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise> <!--Account show (After log in success)-->
                        <div class="account__wrapper">
                            <!--Giỏ hàng-->
                            <div class="cart__wrapper">
                                <a href="<c:url value="/public/user/shoppingCart.jsp" />" class="cart">
                                        <span class="cart__content">
                                            <i class="cart__icon fa-solid fa-cart-shopping"></i>
                                            Giỏ hàng
                                        </span>
                                    <span class="qlt__swapper">
                                            <span class="qlt__value">
<%--                                                <c:set var="userIdCart" value="${String.valueOf(auth.id)}"/>--%>
<%--                                                <c:choose>--%>
<%--                                                    <c:when test="${sessionScope[userIdCart] == null}"> 0 </c:when>--%>
<%--                                                    <c:otherwise>--%>
<%--                                                        ${sessionScope[userIdCart].getTotalItems()}--%>
<%--                                                    </c:otherwise>--%>
<%--                                                </c:choose>--%>
                                            </span>
                                        </span>
                                </a>
                            </div>
                            <div class="account hvr-grow">
                                <i class="account__icon fa-regular fa-user"></i>
                                <div class="setting__list">
                                    <a href="/user/info"
                                       class="setting__item">
                                        <div class="setting__link">
                                            <div class="account__info">
                                                <i class="account__icon fa-regular fa-user"></i>
                                                <p class="account__name"> ${auth.email} </p>
                                            </div>
                                        </div>
                                    </a>
                                    <a href="/user/info"
                                       class="setting__item">
                                        <div class="setting__link">Tài khoản của tôi</div>
                                    </a>
                                    <c:if test="${auth.role == 'ADMIN'}">
                                        <a href="<c:url value="/admin/product" />"
                                           class="setting__item">
                                            <div class="setting__link">Quản lý</div>
                                        </a>
                                    </c:if>
                                    <a href="<c:url value="/signOut" />" class="setting__item">
                                        <div class="setting__link setting__logOut">Đăng xuất</div>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </nav>
</header>
