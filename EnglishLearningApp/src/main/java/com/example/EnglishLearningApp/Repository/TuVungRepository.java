package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.TuVung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TuVungRepository extends JpaRepository<TuVung, Integer> {
}
