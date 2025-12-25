package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.TuVungYeuThich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TuVungYeuThichRepository extends JpaRepository<TuVungYeuThich, Integer> {
}
