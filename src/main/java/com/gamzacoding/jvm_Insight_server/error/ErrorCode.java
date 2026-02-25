package com.gamzacoding.jvm_Insight_server.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    NOT_FIND_TARGET(HttpStatus.NOT_FOUND, "타겟을 찾을 수 없습니다."),
    PIP_NOT_NEGATIVE(HttpStatus.BAD_REQUEST, "PIP는 양수만 가능합니다."),
    ;

    private final HttpStatus status;
    private final String errorMessage;

    ErrorCode(HttpStatus status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
