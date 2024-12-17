package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.dto.mvc.response.ProductCardResponse;
import com.spring.websellspringmvc.models.Category;
import com.spring.websellspringmvc.models.Parameter;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.models.ProductFilter;
import com.spring.websellspringmvc.utils.MoneyRange;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
@RegisterBeanMapper(Product.class)
public interface ProductCardDAO {
    @SqlQuery("SELECT id FROM products WHERE categoryId IN (<listIdCategory>)")
    List<Product> getIdProductByCategoryId(@BindList("listIdCategory") List<String> listIdCategory);

    @SqlQuery("SELECT products.id FROM products JOIN colors ON products.id = colors.productId WHERE colors.codeColor IN (<listCodeColor>)")
    List<Product> getIdProductByColor(@BindList("listCodeColor") List<String> listCodeColor);

    @SqlQuery("SELECT products.id FROM products JOIN sizes ON products.id = sizes.productId WHERE sizes.nameSize IN (<listSize>)")
    List<Product> getIdProductBySize(@Bind("listSize") List<String> listSize);

    @SqlQuery("SELECT id FROM products WHERE originalPrice BETWEEN :from AND :to")
    List<Product> getIdProductByMoneyRange(@BindBean List<MoneyRange> moneyRangeList);


    @SqlQuery("""
            SELECT parameters.name, parameters.minValue, parameters.maxValue, parameters.unit, parameters.guideImg 
            FROM products JOIN (parameters JOIN categories ON parameters.categoryId = categories.id) ON products.categoryId = categories.id 
            WHERE products.id = :id
            """)
    List<Parameter> getParametersByProductId(@Bind("id") int id);

    @SqlQuery("SELECT name FROM products WHERE id = :id")
    List<Product> getNameProductById(@Bind("id") int id);

    @SqlQuery("SELECT id, `name`, categoryId, originalPrice, salePrice, visibility FROM products")
    List<Product> getProduct();

    //HardCode
    @SqlQuery(
            """
                     SELECT p.id                           AS id,
                           p.name                         AS name,
                           p.description,
                           p.salePrice,
                           p.originalPrice,
                           (SELECT i.nameImage
                            FROM images i
                            WHERE i.productId = p.id
                            ORDER BY i.id ASC
                            LIMIT 1)                      AS thumbnail,
                           COALESCE(COUNT(r.id), 0)       AS reviewCount,
                           COALESCE(AVG(r.ratingStar), 0) AS rating
                    FROM products p
                             LEFT JOIN order_details od ON p.id = od.productId
                             LEFT JOIN reviews r ON od.id = r.orderDetailId
                    WHERE p.visibility = 1
                      AND p.createAt >= DATE_SUB('2023-12-01', INTERVAL 1 MONTH)
                    GROUP BY p.id
                    LIMIT :limit OFFSET :offset;
                    
                    """
    )
    @RegisterBeanMapper(ProductCardResponse.class)
    public List<ProductCardResponse> getListNewProducts(@Bind("limit") int limit, @Bind("offset") long offset);

    @SqlQuery("""
            SELECT p.id,
                   p.`name`,
                   p.`description`,
                   p.salePrice,
                   p.originalPrice,
                   (SELECT i.nameImage
                    FROM images i
                    WHERE i.productId = p.id
                    ORDER BY i.id ASC
                    LIMIT 1)                      AS thumbnail,
                   COALESCE(COUNT(r.id), 0)       AS reviewCount,
                   COALESCE(AVG(r.ratingStar), 0) AS rating
            FROM products p
                     INNER JOIN order_details od ON p.id = od.productId
                     LEFT JOIN reviews r ON od.id = r.orderDetailId
            WHERE p.visibility = 1
            GROUP BY p.id, p.`name`, p.salePrice, p.originalPrice
            HAVING SUM(od.quantityRequired) >= 10
            ORDER BY SUM(od.quantityRequired) DESC
            LIMIT :limit OFFSET :offset
            """)
    @RegisterBeanMapper(ProductCardResponse.class)
    public List<ProductCardResponse> getListTrendProducts(@Bind("limit") int limit, @Bind("offset") long offset);

    @SqlQuery("""
            SELECT DISTINCT\s
                p.id,
                p.name,
                p.description,
                p.originalPrice,
                p.salePrice,
                p.visibility,
                (SELECT i.nameImage
                 FROM images i
                 WHERE i.productId = p.id
                 ORDER BY i.id ASC
                 LIMIT 1)                      AS thumbnail,
                COALESCE(COUNT(r.id), 0)       AS reviewCount,
                COALESCE(AVG(r.ratingStar), 0) AS rating
            FROM categories c
                     JOIN products p ON c.id = p.categoryId
                     JOIN colors ON p.id = colors.productId
                     JOIN sizes ON p.id = sizes.productId
                     JOIN order_details od ON p.id = od.productId
                     LEFT JOIN reviews r ON od.id = r.orderDetailId
            WHERE (:categoryId IS NULL OR c.id IN (:categoryId))
              AND (:codeColors IS NULL OR colors.codeColor IN (:codeColors))
              AND (:sizeNames IS NULL OR sizes.nameSize IN (:sizeNames))
              AND p.visibility = 1
            GROUP BY p.id
            LIMIT :limit OFFSET :offset;
            """)
    @RegisterBeanMapper(ProductCardResponse.class)
    List<ProductCardResponse> filter(@BindBean ProductFilter productFilter);
}
