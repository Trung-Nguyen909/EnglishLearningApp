package com.example.EnglishLearningApp.Mapper;

import com.example.EnglishLearningApp.Model.NguoiDung;
import com.example.EnglishLearningApp.dto.request.UserRegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NguoiDungMapper {
     NguoiDung toNguoiDung(UserRegisterRequest nguoiDung1);
     void updateUser(@MappingTarget NguoiDung nguoiDung, NguoiDung nguoiDung1);
}
