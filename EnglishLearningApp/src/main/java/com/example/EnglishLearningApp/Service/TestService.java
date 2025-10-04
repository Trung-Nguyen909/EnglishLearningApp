package com.example.EnglishLearningApp.Service;

import com.example.EnglishLearningApp.Model.Test;
import com.example.EnglishLearningApp.Repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public List<Test> getAllTests() {
        return testRepository.findAll();
    }

    public Optional<Test> getTestById(Integer id) {
        return testRepository.findById(id);
    }

    public Test createTest(Test test) {
        return testRepository.save(test);
    }

    public Test updateTest(Integer id, Test testDetails) {
        return testRepository.findById(id)
                .map(t -> {
                    t.setIdKhoaHoc(testDetails.getIdKhoaHoc());
                    t.setTenBaiTest(testDetails.getTenBaiTest());
                    t.setSoCauHoi(testDetails.getSoCauHoi());
                    t.setTgianLam(testDetails.getTgianLam());
                    return testRepository.save(t);
                })
                .orElseThrow(() -> new RuntimeException("Test not found with id " + id));
    }

    public void deleteTest(Integer id) {
        testRepository.deleteById(id);
    }
}
