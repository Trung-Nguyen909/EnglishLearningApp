package com.example.EnglishLearningApp.Mapper;

import com.example.EnglishLearningApp.Model.BaiHoc;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BaihocMapper {
    BaiHoc toBaiHoc(BaiHoc baiHoc);
    void updateBaiHoc(BaiHoc baiHoc, @MappingTarget BaiHoc baiHoc1);
}
