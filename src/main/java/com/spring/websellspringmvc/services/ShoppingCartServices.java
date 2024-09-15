package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dao.ShoppingCartDAO;
import com.spring.websellspringmvc.models.User;
import com.spring.websellspringmvc.models.Voucher;
import com.spring.websellspringmvc.models.shoppingCart.AbstractCartProduct;
import com.spring.websellspringmvc.models.shoppingCart.ShoppingCart;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShoppingCartServices {
//
    private ShoppingCartDAO shoppingCartDao;
    private User user;

    private static ShoppingCartServices INSTANCE;

    public ShoppingCartServices() {
        shoppingCartDao = new ShoppingCartDAO();
    }

    public static ShoppingCartServices getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new ShoppingCartServices();
        return INSTANCE;
    }
    public List<Voucher> getListVouchers(){
//        return shoppingCartDao.getListVouchers();
        return null;
    }

//    public Voucher getDiscountPercentByCode(double temporaryPrice, String code){
//        return shoppingCartDao.getDiscountPercentByCode(temporaryPrice, code);
//    }

    public Voucher getValidVoucherApply(String code){
//        return shoppingCartDao.getValidVoucherApply(code);
        return null;
    }


    public List<String> getListCodeOfVouchers(){
//        return shoppingCartDao.getListCodeOfVouchers();
        return null;
    }

//    public double getMinPriceApplyVoucherByCode(String code){
//        return shoppingCartDao.getMinPriceApplyVoucherByCode(code);
//    }

    public void insertCart(int cartId, int userId, Map<Integer, List<AbstractCartProduct>> products) {
//        shoppingCartDao.insertCart(cartId, userId, products);
    }

    public int findCartByUserId(int id) {
//        return shoppingCartDao.findCartByUserId(id);
        return 0;
    }

    public ShoppingCart findCartByCartId(int cartId) {
//        return shoppingCartDao.findById(cartId);
        return null;
    }

    public void deleteByCartIdAndIdProduct(int cartId, Integer[] productIds) {
//        shoppingCartDao.deleteByCartIdAndIdProduct(cartId, productIds);
    }

    public void update(Map<Integer, List<AbstractCartProduct>> change) {
//        shoppingCartDao.update(change);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
