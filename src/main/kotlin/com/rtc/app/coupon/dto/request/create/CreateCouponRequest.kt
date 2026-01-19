package com.rtc.app.coupon.dto.request.create

import com.rtc.app.coupon.type.CouponSubType
import com.rtc.app.coupon.type.CouponType
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

data class CreateCouponRequest(
    @field:NotNull val title: String?,
    @field:NotNull val description: String?,
    @field:NotNull val type: CouponType?,
    @field:NotNull val subType: CouponSubType?,
    @field:NotNull val totalQuantity: Int?,
    @field:NotNull @field:Valid val discountInfo: DiscountInfo?,
    @field:NotNull @field:Valid val validPeriod: ValidPeriodInfo?
)
