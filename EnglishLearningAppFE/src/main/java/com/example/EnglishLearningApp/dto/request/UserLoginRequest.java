package com.example.EnglishLearningApp.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserLoginRequest {
    String email;
    String matkhau;
}
