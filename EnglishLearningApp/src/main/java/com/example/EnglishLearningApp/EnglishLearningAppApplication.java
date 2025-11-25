package com.example.EnglishLearningApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EnglishLearningAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnglishLearningAppApplication.class, args);
	}

}
