package com.example.EnglishLearningApp.Mapper;

import com.example.EnglishLearningApp.Model.BinhLuan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BinhluanMapper {
    BinhLuan toBinhLuan(BinhLuan binhLuan);
}
