package com.example.EnglishLearningApp.Mapper;

import com.example.EnglishLearningApp.Model.KhoaHoc;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KhoahocMapper {
    KhoaHoc toKhoaHoc(KhoaHoc khoaHoc);
}
