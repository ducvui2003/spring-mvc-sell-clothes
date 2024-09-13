package com.spring.websellspringmvc.dao;

import com.spring.websellspringmvc.models.Image;

import java.util.ArrayList;
import java.util.List;

public class ImageDAO {
    public List<Image> getThumbnail(int productId) {
        String sql = "SELECT nameImage FROM images WHERE productId = ? AND isThumbnail = 1";
        return GeneralDAO.executeQueryWithSingleTable(sql, Image.class, productId);
    }

    public void addImages(List<Image> images) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        sql.append("INSERT INTO images (nameImage, productId) ")
                .append("VALUES ");
        for (int i = 0; i < images.size(); i++) {
            if (i != 0)
                sql.append(" , ");
            String nameImage = images.get(i).getNameImage();
            nameImage = nameImage.substring(nameImage.indexOf("product_img") + 12);
            sql.append(" ( ? , ? ) ");
            params.add(nameImage);
            params.add(images.get(i).getProductId());
        }
        GeneralDAO.executeAllTypeUpdate(sql.toString(), params.toArray());
    }

    public List<Image> getNameImages(int productId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT nameImage FROM images WHERE productId = ?");
        return GeneralDAO.executeQueryWithSingleTable(sql.toString(), Image.class, productId);
    }

    public List<Image> getIdImages(int productId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id FROM images WHERE productId = ?");
        return GeneralDAO.executeQueryWithSingleTable(sql.toString(), Image.class, productId);
    }

    public void deleteImages(List<Integer> nameImages) {
        StringBuilder idRange = new StringBuilder();
        if (nameImages.size() == 1) idRange.append(nameImages.get(0));
        else
            for (int i = 0; i < nameImages.size(); i++) {
                idRange.append(nameImages.get(i));
                if (i != nameImages.size() - 1) {
                    idRange.append(" , ");
                }
            }
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM images ").append("WHERE id IN (").append(idRange).append(")");
        GeneralDAO.executeAllTypeUpdate(sql.toString());
    }
}
