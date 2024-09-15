package com.spring.websellspringmvc.services;

import com.spring.websellspringmvc.dao.HomeDAO;
import com.spring.websellspringmvc.models.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class HomeServices {
    HomeDAO homeDao;


//    public List<Map<String, Object>> getListTrendingProducts(boolean isSeeMore){
//        return homeDao.getListTrendingProducts(isSeeMore);
//    }

//    public List<Map<String, Object>> getListNewProducts(boolean isSeeMore){
//        return homeDao.getListNewProducts(isSeeMore);
//    }

    public List<Product> getListNewProducts(boolean isSeeMore) {
        return homeDao.getListNewProducts();
    }

    public List<Slider> getListSlideShow() {
        return homeDao.getListSlideShow();
    }

    public List<Product> getListTrendProducts(boolean isSeeMore) {
        return homeDao.getListTrendProducts();
    }
}
