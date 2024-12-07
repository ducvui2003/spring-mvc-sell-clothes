package com.spring.websellspringmvc.services.voucher;

import com.spring.websellspringmvc.dao.ProductDAO;
import com.spring.websellspringmvc.dao.VoucherDAO;
import com.spring.websellspringmvc.models.CartItem;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.Size;
import com.spring.websellspringmvc.models.Voucher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VoucherProductStrategy implements IVoucherStrategy {
    List<CartItem> cartItems;
    Voucher voucher;
    VoucherDAO voucherDAO;
    ProductDAO productDao;
    List<Integer> listProductIdCanApply;

    public VoucherProductStrategy(List<CartItem> cartItems, Voucher voucher) {
        this.cartItems = cartItems;
        this.voucher = voucher;
        this.listProductIdCanApply = new ArrayList<>();
    }


    @Override
    public boolean apply() {
        if (!checkMinimumPrice()) {
            return false;
        }
        return true;
    }

    private boolean checkMinimumPrice() {
        this.cartItems = getCartItemCanApply(cartItems, voucher.getId());
        double totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            Product product = productDao.getProductByProductId(cartItem.getProductId());
            double price = product.getSalePrice() == 0.0 ? product.getOriginalPrice() : product.getSalePrice();
            double priceSize = (isSizeCustom(cartItem)) ? 0.0 : getSizePrice(cartItem.getSizeId());
            totalPrice += (price + priceSize) * cartItem.getQuantity();
            if (totalPrice >= voucher.getMinimumPrice()) {
                return true;
            }
        }
        return false;
    }


    public List<CartItem> getCartItemCanApply(List<CartItem> cartItems, Integer voucherId) {
        List<Integer> listCartItemId = cartItems.stream().map(CartItem::getId).collect(Collectors.toList());
        return voucherDAO.getCartItemCanApply(listCartItemId, voucher.getId());
    }

    private boolean isSizeCustom(Object size) {
        try {
            // Nó là của sản phẩm có sẵn
            Size sizeWrapper = (Size) size;
            return false;
        } catch (Exception e) {
            // Nó là của sản phẩm custom
            return true;
        }
    }

    private double getSizePrice(Object size) {
        Size sizeWrapper = (Size) size;
        return voucherDAO.getPriceSize(sizeWrapper.getId());
    }
}
