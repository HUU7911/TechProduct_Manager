package com.techstore.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    AUTHENTICATION_ERROR(1001, "Sai thông tin đăng nhập"),
    VALIDATION_ERROR(1002, "Dữ liệu không hợp lệ"),
    DATABASE_ERROR(1003, "Lỗi cơ sở dữ liệu"),
    PRODUCT_NOT_FOUND(1004, "Không tìm thấy sản phẩm"),
    SYSTEM_ERROR(9999, "Lỗi hệ thống");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
