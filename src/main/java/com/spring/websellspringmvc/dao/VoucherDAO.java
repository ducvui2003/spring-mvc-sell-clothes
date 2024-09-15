package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.*;
import com.spring.websellspringmvc.services.voucher.VoucherState;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public interface VoucherDAO {
    @SqlQuery("SELECT id, code, minimumPrice,discountPercent, expiryDate, description, state FROM vouchers WHERE state = 1 AND expiryDate >= NOW() AND availableTurns > 0")
    public List<Voucher> selectAll();

    @SqlQuery("SELECT id, `code`, description,minimumPrice,discountPercent, expiryDate, availableTurns, state FROM vouchers JOIN voucher_products ON vouchers.id = voucher_products.voucherId WHERE voucher_products.productId IN (<productIds>)")
    public List<Voucher> selectAll(@BindList("productIds") List<Integer> listIdProduct);

    @SqlQuery("SELECT id, code, minimumPrice, description, discountPercent, expiryDate, state, availableTurns FROM vouchers WHERE id = :id")
    public boolean existVoucher(@Bind("id") Integer id);

    @SqlQuery("SELECT id, code, minimumPrice, description, discountPercent, expiryDate, state, availableTurns FROM vouchers WHERE code = :code")
    public Voucher selectByCode(@Bind("code") String code);

    @SqlQuery("SELECT sizePrice FROM sizes WHERE id = :id")
    public double getPriceSize(@Bind("id") Integer sizeId);

    @SqlQuery("SELECT COUNT(*) count FROM vouchers WHERE code LIKE :search OR availableTurns LIKE :search OR state LIKE :search")
    public long getSizeWithCondition(@Bind("search") String search);

    @SqlQuery("""
            SELECT id, `code`, createAt, expiryDate, availableTurns, state 
            FROM vouchers 
            WHERE code LIKE :search OR createAt LIKE :search OR expiryDate LIKE :search OR availableTurns LIKE :search OR state LIKE :search 
            ORDER BY :orderBy :orderDir LIMIT :limit OFFSET :start 
            """)
    public List<Voucher> selectWithCondition(@Bind("start") Integer start, @Bind("limit") Integer limit, @Bind("search") String search, @Bind("orderBy") String orderBy, @Bind("orderDir") String orderDir);

    @SqlUpdate("""
            INSERT INTO vouchers (code, minimumPrice, description, discountPercent, expiryDate, state, availableTurns) 
            VALUES (:code, :minimumPrice, :description, :discountPercent, :expiryDate, :state, :availableTurns)
            """)
    @GetGeneratedKeys
    public int save(@BindBean Voucher voucher);


    @SqlBatch("INSERT INTO voucher_products (voucherId, productId) VALUES (:voucherId, :productId)")
    void save(@Bind("voucherId") Integer voucherId, @BindList("productId") List<Integer> productIds);

    @SqlQuery("""
            SELECT cart_id, product_id, color_id ,size, quantity 
            "FROM cart_items JOIN cart ON cart_items.cart_id = cart.id 
            "WHERE cart_items.id IN (<ids>) 
            AND cart_items.product_id IN 
            (SELECT productId from voucher_products WHERE voucherId = :voucherId)
            """)
    public List<CartItem> getCartItemCanApply(@BindList("ids") List<Integer> cartItemId, @Bind("voucherId") Integer voucherId);

    @SqlQuery("SELECT productId FROM voucher_products JOIN vouchers on voucher_products.voucherId = vouchers.id WHERE vouchers.code = :code")
    public List<Integer> getListProductByCode(@Bind("code") String code);

    @SqlQuery("SELECT productId FROM voucher_products JOIN vouchers on voucher_products.voucherId = vouchers.id WHERE vouchers.id = :id")
    public List<Integer> getListProductById(@Bind("id") Integer id);

    @SqlUpdate("UPDATE vouchers SET state = :state WHERE code = :code")
    public void changeState(@Bind("code") String code, @Bind("state") VoucherState type);

    @SqlUpdate("UPDATE vouchers SET code = :code, minimumPrice = :minimumPrice, description = :description, discountPercent = :discountPercent, expiryDate = :expiryDate, state = :state, availableTurns = :availableTurns WHERE code = :code")
    public void update(Voucher voucher);

    @SqlUpdate("DELETE FROM voucher_products WHERE voucherId = :code AND productId IN (<ids>)")
    public void deleteProductVoucher(@Bind("code") Integer code, @BindList("ids") List<Integer> delete);

    @SqlUpdate("INSERT INTO voucher_products (voucherId, productId) VALUES (:code, :productId)")
    public void insertProductVoucher(@Bind("code") Integer code, @BindList("productId") List<Integer> insert);

    @SqlUpdate("UPDATE voucher_products SET productId = CASE "
            + "<build_case_statements> "
            + "END WHERE voucherId = :voucherId")
    void updateProductVoucher(@Bind("voucherId") Integer voucherId, @BindList("productIds") List<Integer> productIds);

    default String buildCaseStatements(List<Integer> productIds) {
        StringBuilder caseStatement = new StringBuilder();
        for (int i = 0; i < productIds.size(); i++) {
            caseStatement.append(" WHEN productId = ").append(productIds.get(i)).append(" THEN ").append(productIds.get(i));
        }
        return caseStatement.toString();
    }
}
