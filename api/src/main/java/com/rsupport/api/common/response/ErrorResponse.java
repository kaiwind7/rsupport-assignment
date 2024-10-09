package com.rsupport.api.common.response;

import com.rsupport.api.common.enums.ErrorCode;
import com.rsupport.api.common.exception.ServiceException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@Schema(description = "에러 응답")
public class ErrorResponse {
    @Schema(description = "에러 메시지")
    private final String message;
    @Schema(description = "에러 코드", implementation = ErrorCode.class)
    private final String errorCode;

    public ErrorResponse(ServiceException e) {
        this.message = e.getMessage();
        this.errorCode = e.getErrorCode().name();
    }
}
