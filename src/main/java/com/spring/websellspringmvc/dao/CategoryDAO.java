package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Category;
import com.spring.websellspringmvc.models.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryDAO {
    GeneralDAO generalDAO;

    public List<Category> getAllCategory() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, nameType, sizeTableImage ")
                .append("FROM categories");
        return generalDAO.executeQueryWithSingleTable(sql.toString(), Category.class);
    }

    public int add(Category category) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO categories (nameType, sizeTableImage) VALUES (?, ?) ");
        return generalDAO.executeInsert(sql.toString(), category.getNameType(), category.getSizeTableImage());
    }

    public List<Category> getCategoryByNameType(String nameType) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id FROM categories WHERE nameType = ?");
        return generalDAO.executeQueryWithSingleTable(sql.toString(), Category.class, nameType);
    }

    public void addParameter(Parameter parameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO parameters (name, minValue, `maxValue`, unit, categoryId, guideImg) VALUES (?, ?, ?, ?, ?, ?) ");
        generalDAO.executeAllTypeUpdate(sql.toString(), parameter.getName(), parameter.getMinValue(), parameter.getMaxValue(), parameter.getUnit(), parameter.getCategoryId(), parameter.getGuideImg());
    }

    public List<Category> getListCategoryById(int id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, nameType, sizeTableImage ")
                .append("FROM categories ")
                .append("WHERE id = ?");
        return generalDAO.executeQueryWithSingleTable(sql.toString(), Category.class, id);
    }

    public void updateCategory(Category category) {
        StringBuilder sql = new StringBuilder();
        if (category.getSizeTableImage() == null) {
            sql.append("UPDATE categories SET nameType = ? WHERE id = ?");
            generalDAO.executeAllTypeUpdate(sql.toString(), category.getNameType(), category.getId());
        } else {
            sql.append("UPDATE categories SET nameType = ?, sizeTableImage = ? WHERE id = ?");
            generalDAO.executeAllTypeUpdate(sql.toString(), category.getNameType(), category.getSizeTableImage(), category.getId());
        }
    }

    public Category getCategoryById(int id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, nameType, sizeTableImage ")
                .append("FROM categories ")
                .append("WHERE id = ?");
        return generalDAO.executeSelectOne(sql.toString(), Category.class, id);
    }
}
