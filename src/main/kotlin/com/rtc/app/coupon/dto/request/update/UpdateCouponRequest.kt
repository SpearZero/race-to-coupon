package com.rtc.app.coupon.dto.request.update

import com.rtc.app.coupon.type.CouponStatus
import java.time.LocalDateTime

data class UpdateCouponRequest(
    val title: String?,
    val description: String?,
    val downloadStartDate: LocalDateTime?,
    val downloadEndDate: LocalDateTime?,
    val status: CouponStatus?
)