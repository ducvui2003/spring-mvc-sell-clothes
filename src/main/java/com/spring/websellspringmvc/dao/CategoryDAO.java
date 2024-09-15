package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Category;
import com.spring.websellspringmvc.models.Parameter;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RegisterBeanMapper(Category.class)
public interface CategoryDAO {

    @SqlQuery("SELECT id, nameType, sizeTableImage FROM categories")
    public List<Category> getAllCategory();

    @SqlUpdate("INSERT INTO categories (:nameType, :sizeTableImage) VALUES (?, ?) ")
    public int add(@BindBean Category category);

    @SqlUpdate("SELECT id FROM categories WHERE nameType = :nameType")
    public List<Category> getCategoryByNameType(@Bind("nameType") String nameType);

    @SqlUpdate("""
            INSERT INTO parameters (name, minValue, maxValue, unit, categoryId, guideImg) 
            VALUES (:name, :minValue, :maxValue, :unit, :categoryId, :guideImg)
            """)
    public void addParameter(@BindBean Parameter parameter);

    @SqlQuery("SELECT id, nameType, sizeTableImage FROM categories WHERE id = :id")
    public List<Category> getListCategoryById(@Bind("id") int id);

    @SqlUpdate("UPDATE categories SET nameType = :nameType, sizeTableImage = :sizeTableImage WHERE id = :id")
    public void updateCategory(@BindBean Category category);

    @SqlQuery("SELECT id, nameType, sizeTableImage FROM categories WHERE id = :id")
    public Category getCategoryById(@Bind(":id") int id);
}
