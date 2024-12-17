package com.spring.websellspringmvc.services.key;

import com.spring.websellspringmvc.models.Key;

import java.util.List;

public interface KeyServices {

    boolean isBlockKey(int userId);

    List<Key> getKeys(int userId);

    void setInvalidKey(int userID, String otp);

    void insertKey(String publicKey, int userId);

}
