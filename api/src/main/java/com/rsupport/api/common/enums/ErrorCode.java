package com.rsupport.api.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("에러가 발생하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ALREADY_REGISTERED_USER("이미 등록된 사용자입니다.", HttpStatus.CONFLICT),
    INVALID_REQUEST("", HttpStatus.BAD_REQUEST),
    NOT_FOUND_ENTITY("대상을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FILE_UPLOAD_FAILED("파일 업로드를 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DELETE_ERROR("파일 삭제 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
