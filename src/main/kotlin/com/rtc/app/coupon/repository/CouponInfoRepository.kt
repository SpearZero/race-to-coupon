package com.rtc.app.coupon.repository

import com.rtc.app.coupon.entity.CouponInfo
import org.springframework.data.jpa.repository.JpaRepository

interface CouponInfoRepository : JpaRepository<CouponInfo, Long> {
    fun existsByCode(couponCode: String): Boolean
}
