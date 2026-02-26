package com.rtc.app.coupon.dto.response.list

import com.rtc.app.coupon.type.CouponStatus
import java.time.LocalDateTime

data class CouponSummary(
    val id: Long,
    val name: String,
    val status: CouponStatus,
    val downloadStartAt: LocalDateTime?,
    val downloadEndAt: LocalDateTime?,
)