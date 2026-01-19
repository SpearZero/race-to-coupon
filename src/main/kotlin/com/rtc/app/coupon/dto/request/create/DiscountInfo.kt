package com.rtc.app.coupon.dto.request.create

import com.rtc.app.coupon.dto.request.create.validator.ValidDiscountAmount
import com.rtc.app.coupon.type.DiscountType
import jakarta.validation.constraints.NotNull

@ValidDiscountAmount
data class DiscountInfo(
    @field:NotNull val discountType: DiscountType?,
    @field:NotNull val discountValue: Int?,
    @field:NotNull val minApplyAmount: Int?,
    @field:NotNull val maxApplyAmount: Int?
)