package com.example.EnglishLearningApp.Mapper;

import com.example.EnglishLearningApp.Model.TuVung;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TuvungMapper {
    TuVung toTuVung(TuVung tuVungrequest);
}
