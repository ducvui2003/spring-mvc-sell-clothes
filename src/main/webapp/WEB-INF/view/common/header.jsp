<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.spring.websellspringmvc.utils.constraint.Role" %>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

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
                <sec:authorize access="!isAuthenticated()"> <!--cta == call to action-->
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
                </sec:authorize>
                <sec:authorize access="isAuthenticated()"> <!--Account show (After log in success)-->
                    <div class="account__wrapper">
                        <!--Giỏ hàng-->
                        <div class="cart__wrapper">
                            <a href="/cart" class="cart">
                                    <span class="cart__content">
                                        <i class="cart__icon fa-solid fa-cart-shopping"></i>
                                        Giỏ hàng
                                    </span>
                                <span class="qlt__swapper">
                                        <span class="qlt__value">
                                                ${empty sessionScope.quantity ? 0: sessionScope.quantity }
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
                                            <span class="account__name">
                                                <sec:authentication property="name"/>
                                             </p>
                                        </div>
                                    </div>
                                </a>
                                <a href="/user/info"
                                   class="setting__item">
                                    <div class="setting__link">Tài khoản của tôi</div>
                                </a>
                                <sec:authorize access="hasRole('ADMIN')">
                                    <a href="<c:url value="/admin/product" />"
                                       class="setting__item">
                                        <div class="setting__link">Quản lý</div>
                                    </a>
                                </sec:authorize>
                                <form id="logoutForm" action="<c:url value='/signOut' />" method="post" style="display:none;">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                </form>
                                <a href="<c:url value="/signOut" />" class="setting__item"
                                   onclick="handleSignOut(event,this)">
                                    <div class="setting__link setting__logOut">Đăng xuất</div>
                                </a>
                            </div>
                        </div>
                    </div>
                </sec:authorize>
            </div>
        </div>
    </nav>
</header>
<script>
    function handleSignOut(e, element) {
        e.preventDefault();
        Swal.fire({
            title: 'Bạn có chắc chắn muốn đăng xuất?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            cancelButtonText: 'Không',
            confirmButtonText: 'Đăng xuất'
        }).then((result) => {
            if (result.isConfirmed) {
                localStorage.clear();
                sessionStorage.clear();
                document.getElementById('logoutForm').submit();
            }
        })
        return false;
    }
</script>
