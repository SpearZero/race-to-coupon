package com.rtc.app.coupon.exception

import com.rtc.app.common.response.ResponseCode
import com.rtc.app.coupon.type.ApiCouponResponseCode

class CouponCodeDuplicateException(message: String) : RuntimeException(message) {
    val code: ResponseCode = ApiCouponResponseCode.COUPON_CODE_DUPLICATE_EXCEPTION
}