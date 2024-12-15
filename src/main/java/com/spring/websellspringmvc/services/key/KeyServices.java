package com.spring.websellspringmvc.services.key;

import com.spring.websellspringmvc.models.Key;

import java.util.List;

public interface KeyServices {
    List<Key> getKeys(int userId);

}
