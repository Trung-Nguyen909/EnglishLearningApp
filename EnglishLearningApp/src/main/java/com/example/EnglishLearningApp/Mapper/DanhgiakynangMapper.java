package com.example.EnglishLearningApp.Mapper;

import com.example.EnglishLearningApp.Model.DanhGiaKyNang;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DanhgiakynangMapper {
    DanhGiaKyNang toDanhGiaKyNang(DanhGiaKyNang danhGiaKyNang);
}
