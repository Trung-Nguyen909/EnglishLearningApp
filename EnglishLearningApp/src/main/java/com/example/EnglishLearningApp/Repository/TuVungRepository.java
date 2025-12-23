package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.TuVung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TuVungRepository extends JpaRepository<TuVung, Integer> {
    List<TuVung> findByIdBaiHoc(int idbaihoc);
}
