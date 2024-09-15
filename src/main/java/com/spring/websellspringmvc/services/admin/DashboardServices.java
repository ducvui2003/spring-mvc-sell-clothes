package com.spring.websellspringmvc.services.admin;

import com.spring.websellspringmvc.dao.DashboardDAO;

import com.spring.websellspringmvc.dto.response.DashBoardDetailResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardServices {
    private DashboardDAO dashboardDAO;

    public int countUser() {
        return dashboardDAO.countUser();
    }

    public int countProduct() {
        return dashboardDAO.countProduct();
    }

    public int countOrder() {
        return dashboardDAO.countOrder();
    }

    public int countReview() {
        return dashboardDAO.countReview();
    }

    public DashBoardDetailResponse getDashBoardDetail(String month, String year) {
        DashBoardDetailResponse dashBoardDetailResponse = new DashBoardDetailResponse();
//        Các đơn hàng giao thành công trong tháng
        dashBoardDetailResponse.setOrderFailed(dashboardDAO.quantityOrderSuccess(month, year));
//        Các đơn hàng giao thất bại trong tháng
        dashBoardDetailResponse.setOrderSuccess(dashboardDAO.quantityOrderFailed(month, year));
//        Sản phẩm bán chạy nhất trong tháng
        dashBoardDetailResponse.setProductPopular(dashboardDAO.getListProductPopular(month, year));
//        Sản phẩm bán chậm nhất trong tháng
        dashBoardDetailResponse.setProductNotPopular(dashboardDAO.getListProductNotPopular(month, year));
//        Doanh thu trong tháng
        dashBoardDetailResponse.setRevenue(dashboardDAO.getRevenueByMonth(month, year));
        return dashBoardDetailResponse;
    }
}
