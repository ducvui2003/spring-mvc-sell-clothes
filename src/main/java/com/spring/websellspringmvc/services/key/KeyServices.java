package com.spring.websellspringmvc.services.key;

import com.spring.websellspringmvc.dto.response.KeyResponse;
import com.spring.websellspringmvc.models.Key;

import java.util.List;

public interface KeyServices {

    boolean isBlockKey(int userId, String orderId);

    List<KeyResponse> getKeys(int userId);

    void setInvalidKey(int userID, String otp);

    void insertKey(String publicKey, int userId);

    boolean setValidKey(int id, String otp);

    Key getCurrentKey(int userId);
}
