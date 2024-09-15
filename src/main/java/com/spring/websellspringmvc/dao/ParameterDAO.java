package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Parameter;
import org.hibernate.annotations.SQLDelete;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParameterDAO {

    @SqlQuery("SELECT id, name, minValue, `maxValue`, unit, guideImg FROM parameters WHERE categoryId = :id")
    public List<Parameter> getParameterByCategoryId(@Bind("id") int id);

    //    Check
    @SqlQuery("SELECT id, name, minValue, `maxValue`, unit, guideImg FROM parameters WHERE categoryId = :id ORDER BY id ASC")
    public List<Parameter> getParameterByCategoryId(@Bind("id") int id, boolean orderById);

    //    Check guideImg
    @SqlQuery("UPDATE parameters SET name = :name, minValue = :minValue, `maxValue` = :maxValue, unit = :unit, guideImg = :guideImg WHERE id = :id")
    public void updateParameter(@BindBean Parameter parameter);

    @SqlQuery("INSERT INTO parameters (name, minValue, `maxValue`, unit, categoryId, guideImg) VALUES (:name, :minValue, :maxValue, :unit, :categoryId, :guideImg)")
    public void addParameter(@BindBean Parameter parameter);

    @SqlUpdate("DELETE FROM parameters WHERE id = :id")
    public void deleteParameter(@Bind("id") int id);
}
