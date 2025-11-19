package com.example.EnglishLearningApp.Mapper;

import com.example.EnglishLearningApp.Model.NguoiDung_BinhLuan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NguoidungBinhLuanMapper {
    NguoiDung_BinhLuan toNguoiDungBinhLuan(NguoiDung_BinhLuan nguoiDung_binhLuan);
}
