package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Image;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ImageDAO {
    @SqlQuery("SELECT nameImage FROM images WHERE productId = :productId LIMIT 1")
    public String getThumbnail(@Bind("productId") int productId);

    @SqlBatch("INSERT INTO images (nameImage, productId) VALUES (:nameImage, :productId)")
    public void addImages(@BindBean List<Image> images);

    @SqlQuery("SELECT nameImage FROM images WHERE productId = :productId")
    public List<Image> getNameImages(@Bind("productId") int productId);

    @SqlQuery("SELECT id FROM images WHERE productId = :productId")
    public List<Image> getIdImages(@Bind("productId") int productId);

    @SqlUpdate("DELETE FROM images WHERE id IN (<ids>)")
    public void deleteImages(@BindList("ids") List<Integer> ids);
}
