package com.example.EnglishLearningApp.Mapper;

import com.example.EnglishLearningApp.Model.NguoiDung;
import com.example.EnglishLearningApp.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    UserResponse toUserResponse(NguoiDung nguoiDung);
}
