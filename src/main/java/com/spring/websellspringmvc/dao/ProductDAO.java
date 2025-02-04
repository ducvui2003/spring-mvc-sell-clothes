package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.dto.mvc.response.ProductCardResponse;
import com.spring.websellspringmvc.dto.request.datatable.DatatableRequest;
import com.spring.websellspringmvc.dto.response.datatable.ProductDatatable;
import com.spring.websellspringmvc.models.*;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.AllowUnusedBindings;
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
    @SqlQuery("SELECT id, name, categoryId, description, salePrice, originalPrice FROM products WHERE id = :productId")
    public Product getProductByProductId(@Bind("productId") int productId);


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

    @SqlUpdate("UPDATE products SET visibility = :visibility WHERE id = :id")
    public void updateVisibility(@Bind("id") int productId, @Bind("visibility") boolean visibility);




    @SqlQuery("""
            SELECT COUNT( DISTINCT products.id) 
            FROM categories 
            JOIN products ON categories.id = products.categoryId 
            JOIN colors ON products.id = colors.productId 
            JOIN sizes ON products.id = sizes.productId
            WHERE (:categoryId IS NULL OR categories.id IN (:categoryId))
            AND (:codeColors IS NULL OR colors.codeColor IN (:codeColors))
            AND (:sizeNames IS NULL OR sizes.nameSize IN (:sizeNames))
            """)
    long countFilter(@BindBean ProductFilter productFilter);

    @SqlQuery("""
            SELECT products.id AS id, products.name AS name, categories.nameType AS category, products.originalPrice AS originalPrice, products.salePrice AS salePrice, products.visibility AS visibility 
            FROM products JOIN categories ON products.categoryId = categories.id
          
            LIMIT :length OFFSET :start
            """)
    @RegisterBeanMapper(ProductDatatable.class)
    List<ProductDatatable> datatable(@BindBean DatatableRequest datatableRequest);
//    ORDER BY
//    CASE WHEN :orderColumn = 0 THEN p.id END :orderDir,
//    CASE WHEN :orderColumn = 1 THEN p.name END :orderDir,
//    CASE WHEN :orderColumn = 2 THEN p.category END :orderDir

    @SqlQuery("""
            SELECT COUNT(*) 
            FROM products JOIN categories ON products.categoryId = categories.id
            """)
    @AllowUnusedBindings(true)
    long datatableCount( DatatableRequest datatableRequest);

}
