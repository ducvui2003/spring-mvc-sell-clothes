package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.*;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RegisterBeanMapper(Product.class)
public interface ProductDAO {
    @SqlQuery("SELECT id, nameImage, productId FROM images WHERE productId = :id")
    public List<Image> getListImagesByProductId(@Bind("id") int productId);

    @SqlQuery("SELECT id, codeColor, productId FROM colors WHERE productId = :productId")
    public List<Color> getListColorsByProductId(@Bind("productId") int productId);

    @SqlQuery("SELECT id, nameSize, productId, sizePrice FROM sizes WHERE productId = :productId")
    public List<Size> getListSizesByProductId(@Bind("productId") int productId);

    @SqlQuery("SELECT sizePrice FROM sizes WHERE nameSize = :nameSize AND productId = :productId")
    public double getPriceSizeByName(@Bind("nameSize") String nameSize, @Bind("productId") int productId);

    @SqlQuery("SELECT id, name, categoryId, description, salePrice, originalPrice FROM products WHERE id = :productId")
    public Product getProductByProductId(@Bind("productId") int productId);

    @SqlQuery("SELECT id, sizePrice, nameSize FROM sizes WHERE nameSize = :nameSize AND productId = :productId")
    public Size getSizeByNameSizeWithProductId(@Bind("nameSize") String nameSize, @Bind("productId") int productId);

    @SqlQuery("SELECT id, nameImage, productId FROM images WHERE nameImage = :nameImage AND productId = :productId")
    public Image getImageByNameImageWithProductId(@Bind("nameImage") String nameImage, @Bind("productId") int productId);

    @SqlQuery("SELECT id, codeColor FROM colors WHERE codeColor = :codeColor AND productId = :productId")
    public Color getColorByCodeColorWithProductId(@Bind("codeColor") String codeColor, @Bind("productId") int productId);

    @SqlQuery("SELECT id, name FROM products WHERE name = :name")
    public List<Product> getIdProductByName(@Bind(":name") String name);

    @SqlUpdate("""
            INSERT INTO products (name, categoryId, description, originalPrice, salePrice, visibility, createAt) 
            VALUES (:name, :categoryId, :description, :originalPrice, :salePrice, :visibility, :createAt)
            """)
    @GetGeneratedKeys
    public int addProduct(@BindBean Product product);

    @SqlUpdate("""
            UPDATE products (name, categoryId, description, originalPrice, salePrice, visibility, createAt) 
            VALUES (:name, :categoryId, :description, :originalPrice, :salePrice, :visibility, :createAt) 
            WHERE id = :id
            """)
    public void updateProduct(Product product);


    @SqlQuery("""
            SELECT DISTINCT products.id, products.name, products.description, products.originalPrice, products.salePrice, products.visibility, products.createAt
            FROM categories 
            JOIN products ON categories.id = products.categoryId 
            JOIN colors ON products.id = colors.productId 
            JOIN sizes ON products.id = sizes.productId
            WHERE (:categoryId IS NULL OR categories.id IN (:categoryId))
            AND (:colorId IS NULL OR colors.id = (:colorId))
            AND (:sizeId IS NULL OR sizes.id = (:sizeId))
            LIMIT :limit OFFSET :offset
            """)
    List<Product> filter(@BindBean ProductFilter productFilter);


    @SqlQuery("""
            SELECT COUNT( DISTINCT products.id) 
            FROM categories 
            JOIN products ON categories.id = products.categoryId 
            JOIN colors ON products.id = colors.productId 
            JOIN sizes ON products.id = sizes.productId
            WHERE (:categoryId IS NULL OR categories.id IN (:categoryId))
            AND (:colorId IS NULL OR colors.id = (:colorId))
            AND (:sizeId IS NULL OR sizes.id = (:sizeId))
            LIMIT :limit OFFSET :offset
            """)
    long countFilter(@BindBean ProductFilter productFilter);
}
