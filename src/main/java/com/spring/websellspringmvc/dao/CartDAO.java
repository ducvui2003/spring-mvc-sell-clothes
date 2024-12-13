package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.dto.response.CartItemResponse;
import com.spring.websellspringmvc.models.CartItem;
import com.spring.websellspringmvc.models.OrderDetail;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlCall;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RegisterBeanMapper(CartItem.class)
public interface CartDAO {
    @SqlQuery("""
            SELECT 
                   cart_items.id  
                   as id,
                   products.id as productId,
                   products.name          as name,
                   images.nameImage       AS thumbnail,
                   products.originalPrice as price,
                   products.salePrice     as salePrice,
                   colors.codeColor       AS color,
                   sizes.nameSize         AS size,
                   cart_items.quantity    AS quantity
            
            FROM cart
                     JOIN cart_items ON cart.id = cart_items.cart_id
                     JOIN products ON cart_items.product_id = products.id
                     JOIN colors ON cart_items.color_id = colors.id
                     JOIN sizes ON cart_items.size_id = sizes.id
                     JOIN (SELECT images.productId,
                                  images.nameImage,
                                  ROW_NUMBER() OVER (PARTITION BY images.productId ORDER BY images.id ASC) AS row_num
                           FROM images) AS images ON products.id = images.productId
            WHERE cart.user_id = :userId
              AND images.row_num = 1;
            """)
    @RegisterBeanMapper(CartItemResponse.class)
    public List<CartItemResponse> getCart(@Bind("userId") int userId);

    @SqlQuery("""
            SELECT 
                   cart_items.id  
                   as id,
                   products.id as productId,
                   products.name          as name,
                   images.nameImage       AS thumbnail,
                   products.originalPrice as price,
                   products.salePrice     as salePrice,
                   colors.codeColor       AS color,
                   sizes.nameSize         AS size,
                   cart_items.quantity    AS quantity
            
            FROM cart
                     JOIN cart_items ON cart.id = cart_items.cart_id
                     JOIN products ON cart_items.product_id = products.id
                     JOIN colors ON cart_items.color_id = colors.id
                     JOIN sizes ON cart_items.size_id = sizes.id
                     JOIN (SELECT images.productId,
                                  images.nameImage,
                                  ROW_NUMBER() OVER (PARTITION BY images.productId ORDER BY images.id ASC) AS row_num
                           FROM images) AS images ON products.id = images.productId
            WHERE cart.user_id = :userId
              AND cart_items.id IN (<cartItemIds>)
              AND images.row_num = 1;
            """)
    @RegisterBeanMapper(CartItemResponse.class)
    public List<CartItemResponse> getCart(@BindList("cartItemIds") List<Integer> listCartItemId, @Bind("userId") int userId);

    @SqlCall("CALL add_cart (:cartId, :productId, :sizeId, :colorId, :quantity)")
    public void add(@BindBean CartItem cartItem);

    @SqlUpdate("""
            UPDATE cart_items 
            JOIN cart ON cart.id = cart_items.cart_id
            SET quantity = quantity + :quantity
            WHERE cart_items.id = :cartItemId AND cart.id = :cartId
            """)
    public int increase(@Bind("cartItemId") int cartItemId, @Bind("quantity") int quantity, @Bind("cartId") int cartId);

    @SqlUpdate("""
            UPDATE cart_items
            JOIN cart ON cart.id = cart_items.cart_id
            SET quantity = quantity - :quantity
            WHERE cart_items.id = :cartItemId AND cart.id = :cartId AND quantity > 1
            """)
    public void decrease(@Bind("cartItemId") int cartItemId, @Bind("quantity") int quantity, @Bind("cartId") int cartId);

    @SqlUpdate("""
            DELETE 
            FROM cart_items 
            WHERE cart_items.id = :cartItemId AND cart_items.cart_id = :cartId
            """)
    public void delete(@Bind("cartItemId") int cartItemId, @Bind("cartId") int cartId);

    @SqlQuery("""
            SELECT id
            FROM cart
            WHERE user_id = :userId
            """)
    public Integer getCartId(@Bind("userId") int userId);

    @SqlQuery("""
            SELECT COUNT(*)
            FROM cart_items JOIN cart ON cart.id = cart_items.cart_id
            WHERE cart.user_id = :userId
            """)
    public int getQuantityCart(@Bind("userId") int userId);

    @SqlQuery("""
            SELECT *
            FROM cart
            WHERE user_id = :userId
            AND cart.id IN (<cartItems>)
            """)
    public CartItem getCartIdIn(@BindList("cartItems") List<Integer> cartItems, @Bind("userId") int userId);


    @SqlQuery("""
            SELECT 
                   c.product_id AS productId,
                   p.name AS productName,
                   s.nameSize AS sizeRequired,
                   co.codeColor AS colorRequired,
                   c.quantity AS quantityRequired,
                   (s.sizePrice + IF(p.saleDisable = TRUE, p.salePrice, p.originalPrice)) AS price
            FROM cart_items c JOIN cart ON cart.id = c.cart_id
                     JOIN products p ON c.product_id = p.id
                     JOIN sizes s ON c.size_id = s.id
                     JOIN colors co ON c.color_id = co.id
            WHERE c.id IN (<cartItems>)
              AND cart.user_id = :userId
            """)
    @RegisterBeanMapper(OrderDetail.class)
    public List<OrderDetail> getOrderDetailPreparedAdded(@BindList("cartItems") List<Integer> cartItems, @Bind("userId") int userId);
}