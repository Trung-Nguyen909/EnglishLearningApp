package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.NguoiDung_BinhLuan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NguoiDungBinhLuanRepository extends JpaRepository<NguoiDung_BinhLuan, Integer> {

}
