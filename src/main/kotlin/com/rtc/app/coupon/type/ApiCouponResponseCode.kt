package com.rtc.app.coupon.type

import com.rtc.app.common.response.ResponseCode

enum class ApiCouponResponseCode(
    private val _code: Int,
    private val _message: String,
) : ResponseCode {
    COUPON_CREATED_SUCCESS(20020000, "COUPON_CREATED_SUCCESS"),
    COUPON_NOT_FOUND(40020000, "COUPON_NOT_FOUND"),
    COUPON_CODE_DUPLICATE_EXCEPTION(50020000, "COUPON_CODE_DUPLICATE_EXCEPTION");

    override fun getCode(): Int = _code
    override fun getMessage(): String = _message
}