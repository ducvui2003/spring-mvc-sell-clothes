package com.spring.websellspringmvc.services.voucher;

import com.spring.websellspringmvc.dao.VoucherDAO;
import com.spring.websellspringmvc.dto.VoucherDetailRequest;
import com.spring.websellspringmvc.models.CartItem;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.models.Voucher;
import com.spring.websellspringmvc.utils.Comparison;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherServices {
    VoucherDAO voucherDAO;

    public List<Voucher> getAll() {
        List<Voucher> listVoucher = voucherDAO.selectAll();
        return listVoucher.isEmpty() ? null : listVoucher;
    }

    public List<Voucher> getAll(List<Integer> listIdProduct) {
        List<Voucher> listVoucher = voucherDAO.selectAll(listIdProduct);
        return listVoucher;
    }

    public VoucherDetailRequest canApply(User user, String code, List<Integer> listIdProduct) {
        VoucherDetailRequest voucherDetailRequest = new VoucherDetailRequest();
        if (listIdProduct == null || listIdProduct.isEmpty()) {
            voucherDetailRequest.setState(VoucherApplyState.NOT_FOUND.getValue());
            return voucherDetailRequest;
        }
//        Kiểm tra danh sách sản phẩm gửi lên có nằm trong giỏ hàng của user không ?
//        List<CartItem> listCartItem = shoppingCartDao.getCartProductByProductIdAndUserId(listIdProduct, user.getId());
        List<CartItem> listCartItem = null;
        if (listCartItem == null || listCartItem.isEmpty()) {
            voucherDetailRequest.setState(VoucherApplyState.NOT_FOUND.getValue());
            return voucherDetailRequest;
        }
//        Kiểm tra xem mã giảm giá có tồn tại không?
        Voucher voucher = voucherDAO.selectByCode(code);
        if (voucher == null) {
            voucherDetailRequest.setState(VoucherApplyState.NOT_FOUND.getValue());
            return voucherDetailRequest;
        }
//       Kiểm tra xem voucher còn lượt sử dụng không?
        if (voucher.getAvailableTurns() == 0) {
            voucherDetailRequest.setState(VoucherApplyState.EMPTY_AVAILABLE_TURN.getValue());
            return voucherDetailRequest;
        }

        LocalDate current = LocalDate.now();
        LocalDate givenDate = voucher.getExpiryDate().toLocalDate().plusDays(1);
        if (current.isAfter(givenDate)) {
            voucherDetailRequest.setState(VoucherApplyState.EXPIRED.getValue());
            return voucherDetailRequest;
        }

        VoucherProductStrategy strategy = new VoucherProductStrategy(listCartItem, voucher);
        if (!strategy.apply()) {
            voucherDetailRequest.setState(VoucherApplyState.CAN_NOT_APPLY.getValue());
            return voucherDetailRequest;
        }
        voucherDetailRequest.setVoucher(voucher);
        voucherDetailRequest.setState(VoucherApplyState.CAN_APPLY.getValue());
        voucherDetailRequest.setListIdProduct(listIdProduct);
        return voucherDetailRequest;
    }

    public List<Voucher> getVoucher(Integer start, Integer length, String searchValue, String orderBy, String orderDir) {
        return voucherDAO.selectWithCondition(start, length, searchValue, orderBy, orderDir);
    }

    public long getTotalWithCondition(String searchValue) {
        return voucherDAO.getSizeWithCondition(searchValue);
    }

    public boolean saveVoucher(Voucher voucher, List<Integer> listProductId) {
        boolean isValid = voucherValid(voucher);
        if (!isValid) return false;
        voucher.setCreateAt(new Date(System.currentTimeMillis()));
        int voucherIdCreated = voucherDAO.save(voucher);
        voucherDAO.save(voucherIdCreated, listProductId);
        return true;
    }

    public VoucherDetailRequest getDetail(String code) {
        Voucher voucher = voucherDAO.selectByCode(code);
        if (voucher == null) return null;
        VoucherDetailRequest voucherDetailRequest = new VoucherDetailRequest();
        voucherDetailRequest.setVoucher(voucher);
        voucherDetailRequest.setListIdProduct(voucherDAO.getListProductByCode(code));
        return voucherDetailRequest;
    }

    public void changeState(String code, VoucherState type) {
        voucherDAO.changeState(code, type);
    }

    public boolean updateVoucher(Voucher voucher, List<Integer> listProductId) {
        boolean exist = voucherDAO.existVoucher(voucher.getId());
        if (!exist) return false;
        voucherDAO.update(voucher);
        this.updateProductVoucher(voucher.getId(), listProductId);
        return true;
    }

    private boolean voucherValid(Voucher voucher) {
//        Kiểm tra ngày hết hạn voucher có nhỏ hơn ngày hiện tại không
        Date currentDate = new Date(System.currentTimeMillis());
        if (voucher.getExpiryDate().compareTo(currentDate) < 0) return false;
//        Kiểm tra số lần sử dụng voucher có nhỏ hơn 0 không
        if (voucher.getAvailableTurns() < 0) return false;
//        Kiểm tra phần trăm giảm giá có nhỏ hơn 0 không
        if (voucher.getDiscountPercent() < 0) return false;
//        Kiểm tra giá trị đơn hàng tối thiểu có nhỏ hơn 0 không
        if (voucher.getMinimumPrice() < 0) return false;
//        Kiểm tra mã voucher đã tồn tại chưa
        boolean isExist = voucherDAO.selectByCode(voucher.getCode()) != null;
        if (isExist) return false;
        return true;
    }

    private void updateProductVoucher(Integer voucherId, List<Integer> listProductId) {
        List<Integer> listProductIdInDb = voucherDAO.getListProductById(voucherId);
        Map<String, List<Integer>> mapQueryType = Comparison.filterTypeQuery(listProductId, listProductIdInDb);
        if (!mapQueryType.get("delete").isEmpty()) {
            voucherDAO.deleteProductVoucher(voucherId, mapQueryType.get("delete"));
        }
        if (!mapQueryType.get("insert").isEmpty()) {
            voucherDAO.insertProductVoucher(voucherId, mapQueryType.get("insert"));
        }
        if (!mapQueryType.get("update").isEmpty()) {
            voucherDAO.updateProductVoucher(voucherId, mapQueryType.get("update"));
        }
    }
}
