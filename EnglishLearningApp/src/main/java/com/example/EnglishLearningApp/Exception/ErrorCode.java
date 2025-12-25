package com.example.EnglishLearningApp.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    Not_Found(1001, "can't find data", HttpStatus.NOT_FOUND),
    Unauthorized(1002, "Người dùng chưa xác thực (token bị lỗi??)", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(1002, "bạn không có quyền thực hiện thao tác này, chỉ có admin mới làm được", HttpStatus.FORBIDDEN),
    Server_Error(1003, "error from server", HttpStatus.INTERNAL_SERVER_ERROR),
    Bad_Request(1004, "data is incorrect", HttpStatus.BAD_REQUEST),
    Duplicate(1005, "data is duplicate", HttpStatus.CONFLICT),
    Validation_Error(1006, "Validation Error", HttpStatus.BAD_REQUEST),
    User_Exitsted(1005, "User existed", HttpStatus.CONFLICT),
    Email_Duplicate(1005, "Email cannot be Duplicate!!!!", HttpStatus.CONFLICT),
    Sdt_Duplicate(1005, "SDT cannot be Duplicate!!!!", HttpStatus.CONFLICT),
    Id_Not_Found(1001, " Id not found ", HttpStatus.NOT_FOUND),
    Wrong_Password(1004, "Password is wronged", HttpStatus.BAD_REQUEST),
    GOOGLE_LOGIN_FAILED(1001, "wrong", HttpStatus.UNAUTHORIZED),
    Seat_Holder(1005, "your seat has been resereved", HttpStatus.CONFLICT);

    private int code;
    private String message;
    private HttpStatusCode httpStatuscode;
}
