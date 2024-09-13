package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.CategoryMaterial;

import java.util.List;

public class CategoryMaterialDao {
    public List<CategoryMaterial> getIdByName(String name) {
        String sql = "select id from categories_material where name = ?";
        return GeneralDAO.executeQueryWithSingleTable(sql, CategoryMaterial.class, name);
    }

    public void save(String name) {
        String sql = "insert into categories_material(name) values(?)";
        GeneralDAO.executeAllTypeUpdate(sql, name);
    }

}
