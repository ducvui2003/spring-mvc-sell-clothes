package com.spring.websellspringmvc.services.cart;

import com.spring.websellspringmvc.controller.exception.AppException;
import com.spring.websellspringmvc.controller.exception.ErrorCode;
import com.spring.websellspringmvc.dao.CartDAO;
import com.spring.websellspringmvc.dto.request.AddCartRequest;
import com.spring.websellspringmvc.dto.response.CartResponse;
import com.spring.websellspringmvc.models.CartItem;
import com.spring.websellspringmvc.services.image.CloudinaryUploadServices;
import com.spring.websellspringmvc.session.SessionManager;
import com.spring.websellspringmvc.utils.constraint.ImagePath;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {
    CartDAO cartDAO;
    SessionManager userManager;
    CloudinaryUploadServices cloudinaryUploadServices;
    SessionManager sessionManager;

    @Override
    public List<CartResponse> getCart() {
        int userId = userManager.getUser().getId();
        List<CartResponse> carts = cartDAO.getCart(userId).stream().peek(cartResponse -> cartResponse.setThumbnail(cloudinaryUploadServices.getImage(ImagePath.PRODUCT.getPath(), cartResponse.getThumbnail()))).toList();
        return carts;
    }

    @Override
    public void create(AddCartRequest request) {
        Integer cartId = cartDAO.getCartId(userManager.getUser().getId());
        cartDAO.add(CartItem.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .sizeId(request.getSizeId())
                .colorId(request.getColorId())
                .cartId(cartId).build());
    }

    @Override
    public void delete(Integer productId) {
        Integer userId = cartDAO.getCartId(userManager.getUser().getId());
        cartDAO.delete(productId, userId);
    }

    @Override
    public void increase(Integer cartItemId, int quantity) {
        Integer cartId = cartDAO.getCartId(userManager.getUser().getId());
        int rowEffect = cartDAO.increase(cartItemId, quantity, cartId);
        if (rowEffect == 0) {
            throw new AppException(ErrorCode.QUANTITY_ERROR);
        }
    }

    @Override
    public void decrease(Integer cartItemId, int quantity) {
        Integer cartId = cartDAO.getCartId(userManager.getUser().getId());
        cartDAO.decrease(cartItemId, quantity, cartId);
    }
}
