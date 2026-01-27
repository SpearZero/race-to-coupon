package com.rtc.app.auth.type

import com.rtc.app.common.response.ResponseCode

enum class ApiAuthResponseCode(
    override val code: Int,
    override val message: String,
) : ResponseCode {
    LOGIN_FAIL(4001000, "LOGIN FAIL"),
    JWT_EXPIRED(40010002, "JWT_EXPIRED"),
    AUTHENTICATION_FAILED(40010003, "AUTHENTICATION_FAILED"),
    MISSING_JWT(40010004, "MISSING_JWT"),
    USER_NOT_FOUND(40010005, "USER_NOT_FOUND"),
    LOGIN_SUCCESS(20010001, "LOGIN_SUCCESS"),
    LOGOUT_SUCCESS(20010002, "LOGOUT_SUCCESS"),
    SIGNUP_SUCCESS(20010003, "SIGNUP_SUCCESS"),
    DUPLICATE_FILED(20010004, "DUPLICATE_FIELD");
}
