package com.rtc.app.coupon.type

import com.rtc.app.common.response.ResponseCode

enum class ApiCouponResponseCode(
    override val code: Int,
    override val message: String,
) : ResponseCode {
    COUPON_CREATED_SUCCESS(20020000, "COUPON_CREATED_SUCCESS"),
    COUPON_NOT_FOUND(40020000, "COUPON_NOT_FOUND"),
    COUPON_CODE_DUPLICATE_EXCEPTION(50020000, "COUPON_CODE_DUPLICATE_EXCEPTION");
}
