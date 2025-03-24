package com.logistic.client.company.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "허브를 찾을 수 없습니다."),
    ALREADY_DELETED(HttpStatus.CONFLICT, "이미 삭제되었습니다."),
    USER_UNAUTHORIZED(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다."),

    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "업체를 찾을 수 없습니다."),
    COMPANY_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 업체입니다."),
    COMPANY_UPDATE_ERROR(HttpStatus.BAD_REQUEST, "업체 수정 중 문제가 생겼습니다."),
    COMPANY_INVALID(HttpStatus.BAD_REQUEST, "우편번호 혹은 기본주소는 비어 있을 수 없습니다."),

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    PRODUCT_NOT_ENOUGH(HttpStatus.UNPROCESSABLE_ENTITY, "재고보다 많은 수를 입력하셨습니다."),
    PRODUCT_CONFLICT(HttpStatus.CONFLICT, "상품 관리 중 문제가 생겼습니다."),
    PRODUCT_INVALID(HttpStatus.BAD_REQUEST, "개수가 비어있거나 0 이하일 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
