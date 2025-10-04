package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.TienTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TienTrinhRepository extends JpaRepository<TienTrinh, Integer> {
}
