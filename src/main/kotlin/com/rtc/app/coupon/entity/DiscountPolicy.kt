package com.rtc.app.coupon.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
data class DiscountPolicy(
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 10, updatable = false)
    val discountType: DiscountType,

    @Column(name = "discount_value", nullable = false)
    val discountValue: Int,

    @Column(name = "min_apply_amount", nullable = false)
    val minApplyAmount: Int,

    @Column(name = "max_apply_amount", nullable = false)
    val maxApplyAmount: Int
) {

}