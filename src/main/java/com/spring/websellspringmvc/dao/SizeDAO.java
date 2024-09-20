package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.Size;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RegisterBeanMapper(Size.class)
public interface SizeDAO {
    @SqlQuery("SELECT DISTINCT nameSize FROM sizes")
    public List<Size> getAllSize();

    @SqlQuery("SELECT productId FROM sizes WHERE nameSize = :sizeName")
    public List<Product> getIdProduct(@Bind("sizeName") String sizeName);

    @SqlBatch("INSERT INTO sizes (nameSize, productId, sizePrice) VALUES (:nameSize, :productId, :sizePrice)")
    public void addSizes(Size[] sizes);

    @SqlQuery("SELECT id FROM sizes WHERE productId = :productId")
    public List<Size> getIdSizeByProductId(@Bind("productId") int productId);

    @SqlUpdate("UPDATE sizes SET nameSize = :nameSize, sizePrice = :sizePrice WHERE id = :id")
    public void updateSize(Size size, int id);

    @SqlUpdate("DELETE FROM sizes WHERE id = :id")
    public void deleteSizeList(List<Integer> listId);
}
