package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Category;
import com.spring.websellspringmvc.models.Parameter;
import com.spring.websellspringmvc.models.Product;
import com.spring.websellspringmvc.utils.MoneyRange;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ProductCardDAO {
    @SqlQuery("SELECT id, `name`, categoryId, originalPrice, salePrice FROM products WHERE visibility = :visibility LIMIT :limit OFFSET :offset")
    public List<Product> getProducts(int pageNumber, int limit, boolean visibility);
//    {
//        int offset = (pageNumber - 1) * limit;
//        StringBuilder sql = new StringBuilder();
//        sql.append("SELECT id, `name`, categoryId, originalPrice, salePrice ")
//                .append("FROM products ")
//                .append("WHERE visibility = ? ")
//                .append("LIMIT ")
//                .append(limit)
//                .append(" OFFSET ")
//                .append(offset);
//
//        List<Product> list = GeneralDAO.executeQueryWithSingleTable(sql.toString(), Product.class, visibility);
//        return list;
//    }

    @SqlQuery("SELECT id, `name`, categoryId, originalPrice, salePrice FROM products LIMIT :limit OFFSET :offset")
    public List<Product> getProducts(int pageNumber, int limit);
//    {
//        int offset = (pageNumber - 1) * limit;
//        StringBuilder sql = new StringBuilder();
//        sql.append("SELECT id, `name`, categoryId, originalPrice, salePrice, visibility ")
//                .append("FROM products ")
//                .append("LIMIT ")
//                .append(limit)
//                .append(" OFFSET ")
//                .append(offset);
//
//        List<Product> list = GeneralDAO.executeQueryWithSingleTable(sql.toString(), Product.class);
//        return list;
//    }

    @SqlQuery("SELECT COUNT(*) FROM products")
    public int getQuantityProduct();

    @SqlQuery("SELECT COUNT(*) FROM products WHERE visibility = :visibility")
    public int getQuantityProduct(@Bind(":visibility") boolean visibility);

    @SqlQuery("SELECT COUNT(*) FROM products WHERE visibility = :visibility AND id IN (<listId>)")
    public int getQuantityProduct(@BindList("listId") List<Integer> listId, @Bind("visibility") boolean visibility);

    @SqlQuery("SELECT COUNT(*) FROM products WHERE id IN (<listId>)")
    public int getQuantityProduct(@BindList("listId") List<Integer> listId);

    @SqlQuery("SELECT id, `name`, originalPrice, salePrice, visibility FROM products WHERE id IN (<listId>) LIMIT :limit OFFSET :offset")
    public List<Product> pagingAndFilter(@Bind("listId") List<Integer> listId, @Bind("offset") int offset, @Bind("limit") int limit);

    @SqlQuery("SELECT id, `name`, originalPrice, salePrice FROM products WHERE visibility = :visibility AND id IN (<listId>) LIMIT :limit OFFSET :offset")
    public List<Product> pagingAndFilter(@BindList("listId") List<Integer> listId, @Bind("offset") int pageNumber, @Bind("limit") int limit, @Bind("visibility") boolean visibility);

    @SqlQuery("SELECT id FROM products WHERE categoryId IN (<listIdCategory>)")
    public List<Product> getIdProductByCategoryId(@BindList("listIdCategory") List<String> listIdCategory);

    @SqlQuery("SELECT products.id FROM products JOIN colors ON products.id = colors.productId WHERE colors.codeColor IN (<listCodeColor>)")
    public List<Product> getIdProductByColor(@BindList("listCodeColor") List<String> listCodeColor);

    @SqlQuery("SELECT products.id FROM products JOIN sizes ON products.id = sizes.productId WHERE sizes.nameSize IN (<listSize>)")
    public List<Product> getIdProductBySize(@Bind("listSize") List<String> listSize);

    @SqlQuery("SELECT id FROM products WHERE originalPrice BETWEEN :from AND :to")
    public List<Product> getIdProductByMoneyRange(@BindBean List<MoneyRange> moneyRangeList);

    @SqlQuery("SELECT id, `name`, originalPrice, salePrice FROM products WHERE categoryId = :categoryId")
    public List<Product> getProductByCategoryId(@Bind("categoryId") int categoryId);

    @SqlQuery("SELECT id FROM products WHERE name LIKE %:name%")
    public List<Product> getIdProductByName(@Bind("name") String name);

    @SqlQuery("SELECT id FROM products WHERE createAt BETWEEN :dateBegin AND :dateEnd")
    public List<Product> getProductByTimeCreated(@Bind("dateBegin") Date dateBegin, @Bind("dataEnd") Date dateEnd);

    @SqlQuery("""
            SELECT categories.nameType 
            FROM products JOIN categories ON products.categoryId = categories.id 
            WHERE products.id = :id
            """)
    public List<Category> getNameCategoryById(@Bind("id") int id);

    @SqlQuery("""
            SELECT categories.nameType, categories.sizeTableImage 
            FROM categories JOIN products ON products.categoryId = categories.id 
            WHERE products.id = :id
            """)
    public List<Category> getCategoryByProductId(@Bind("id") int id);

    @SqlQuery("""
            SELECT parameters.name, parameters.minValue, parameters.maxValue, parameters.unit, parameters.guideImg 
            FROM products JOIN (parameters JOIN categories ON parameters.categoryId = categories.id) ON products.categoryId = categories.id 
            WHERE products.id = :id
            """)
    public List<Parameter> getParametersByProductId(@Bind("id") int id);

    @SqlQuery("SELECT name FROM products WHERE id = :id")
    public List<Product> getNameProductById(@Bind("id") int id);

    @SqlUpdate("UPDATE products SET visibility = :visibility WHERE id = :id")
    public void updateVisibility(@Bind("id") int productId, @Bind("visibility") String visibility);

    @SqlQuery("SELECT id, `name`, categoryId, originalPrice, salePrice, visibility FROM products")
    public List<Product> getProduct();
}
