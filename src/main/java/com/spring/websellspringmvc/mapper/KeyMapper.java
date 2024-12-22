package com.spring.websellspringmvc.mapper;

import com.spring.websellspringmvc.dto.response.KeyResponse;
import com.spring.websellspringmvc.models.Key;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface KeyMapper {
    KeyMapper INSTANCE = Mappers.getMapper(KeyMapper.class);
    KeyResponse toKeyResponse(Key key);
    List<KeyResponse> toKeyResponseList(List<Key> keys);
}
