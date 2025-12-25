package com.example.EnglishLearningApp.Mapper;

import com.example.EnglishLearningApp.Model.CauHoi;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CauhoiMapper {
    CauHoi toCauHoi(CauHoi cauHoi);
}
