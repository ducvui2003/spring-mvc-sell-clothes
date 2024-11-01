package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Color;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RegisterBeanMapper(Color.class)
public interface ColorDAO {
    @SqlQuery("SELECT DISTINCT codeColor FROM colors")
    @RegisterBeanMapper(Color.class)
    public List<Color> getAllColor();

    @SqlQuery("SELECT * FROM colors WHERE productId = :productId")
    public List<Color> getListColorByProductId(@Bind("productId") int productId);

    @SqlQuery("SELECT * FROM colors WHERE id = :id")
    @RegisterBeanMapper(Color.class)
    public Color findById(@Bind("id") int id);

    @SqlBatch("INSERT INTO colors (codeColor, productId) VALUES (:codeColor, :productId)")
    public void addColors(@BindBean Color[] colors);

    @SqlQuery("SELECT id FROM colors WHERE productId = :productId")
    public List<Color> getIdColorByProductId(@Bind("productId") int productId);

    @SqlUpdate("UPDATE colors SET codeColor = :color.codeColor WHERE id = :id")
    public void updateColor(@BindBean("color") Color color, @Bind("id") int id);

    public void deleteColorList(List<Integer> listIdDelete);

}
