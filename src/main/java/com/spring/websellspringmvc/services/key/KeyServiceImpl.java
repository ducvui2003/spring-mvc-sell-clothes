package com.spring.websellspringmvc.services.key;

import com.spring.websellspringmvc.dao.KeyDAO;
import com.spring.websellspringmvc.models.Key;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class KeyServiceImpl implements KeyServices{
    KeyDAO keyDAO;
    @Override
    public List<Key> getKeys(int userId) {
        return keyDAO.getKeys(userId);
    }

    public void insertKey(String currentKeyId,String publicKey, int userId){
        //TODO get current key
        Key keyToInsert = new Key();
        keyToInsert.setId(UUID.randomUUID().toString());
        keyToInsert.setPublicKey(publicKey);
        keyToInsert.setUserId(userId);
        if (currentKeyId!=null) keyToInsert.setKeyId(currentKeyId);
        keyDAO.insert(keyToInsert);
    }

}
