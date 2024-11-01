<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav id="sidebar" class="ps ps--active-y">
    <div class="sidebar_blog_2">
        <h4 class="mt-2 display-6 text-center font-weight-bold">Chức năng</h4>
        <ul class="list-unstyled components">
            <li>
                <a role="button">
                    <i class="fa-solid fa-shirt"></i>
                    <span>Sản phẩm</span></a>
                <ul id="collapse_product">
                    <li class="sidebar_active">
                        <a href="/admin/product" data-link="/admin/product"
                           class="mb-2 pb-3 sidebar_item">
                            <i class="fa-solid fa-list-check"></i>
                            <span>Quản lý</span></a>
                    </li>

                    <li>
                        <a href="/admin/review" data-link="/admin/review"
                           class="mb-2 pb-3 sidebar_item">
                            <i class="fa-solid fa-square-poll-vertical"></i>
                            <span>Nhận xét</span></a>
                    </li>

                    <li>
                        <a href="/admin/category" data-link="/admin/category"
                           class="pb-3 sidebar_item">
                            <i class="fa-solid fa-table-list"></i>
                            <span>Loại sản phẩm</span></a>
                    </li>
                </ul>
            </li>

            <li>
                <a href="/admin/order" data-link="/admin/order"
                   class="sidebar_item">
                    <i class="fa-solid fa-cart-shopping"></i>
                    <span>Đơn hàng</span></a>
            </li>

            <li>
                <a href="/admin/user" data-link="/admin/user" class="sidebar_item">
                    <i class="fa-solid fa-user"></i>
                    <span>Người dùng</span></a>
            </li>

            <li>
                <a href="/admin/dashboard" data-link="/admin/dashboard" class="sidebar_item">
                    <i class="fa-solid fa-chart-simple"></i>
                    <span>Thống kê</span></a>
            </li>
            <li>
                <a href="<c:url value="/public/admin/adminVoucher.jsp"/>" data-link="adminVoucher.jsp"
                   class="sidebar_item">
                    <i class="fa-solid fa-ticket-simple"></i>
                    <span>Mã giảm giá</span></a>
            </li>
            <li>
                <a href="<c:url value="/public/admin/adminLogs.jsp"/>" data-link="adminLogs.jsp" class="sidebar_item">
                    <i class="fa-solid fa-book"></i>
                    <span>Nhật ký hệ thống</span></a>
            </li>
        </ul>
    </div>
    <%--    <div class="ps__rail-x" style="left: 0px; bottom: 0px;">--%>
    <%--        <div class="ps__thumb-x" tabindex="0" style="left: 0px; width: 0px;"></div>--%>
    <%--    </div>--%>
    <%--    <div class="ps__rail-y" style="top: 0px; height: 652px; right: 0px;">--%>
    <%--        <div class="ps__thumb-y" tabindex="0" style="top: 0px; height: 562px;"></div>--%>
    <%--    </div>--%>
    <%--    <div class="ps__rail-x" style="left: 0px; bottom: 0px;">--%>
    <%--        <div class="ps__thumb-x" tabindex="0" style="left: 0px; width: 0px;"></div>--%>
    <%--    </div>--%>
    <%--    <div class="ps__rail-y" style="top: 0px; height: 652px; right: 0px;">--%>
    <%--        <div class="ps__thumb-y" tabindex="0" style="top: 0px; height: 562px;"></div>--%>
    <%--    </div>--%>
</nav>

<script>
    $(document).ready(() => {
        const url = window.location.href;
        setNavActive(url);
    });

    function setNavActive(url) {
        const path = url.substring(url.lastIndexOf("/") + 1, url.length);
        $('li > .sidebar_item').each(function () {
            if (path.endsWith(this.dataset.link)) {
                $(this).parent().addClass('sidebar_active');
            } else {
                $(this).parent().removeClass('sidebar_active');
            }
        });
    }
</script>