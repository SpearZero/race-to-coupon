package com.rtc.app.coupon.dto.request.create.validator

import com.rtc.app.coupon.dto.request.create.DiscountInfo
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ValidDiscountAmountValidator : ConstraintValidator<ValidDiscountAmount, DiscountInfo> {

    override fun isValid(value: DiscountInfo?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return true

        val min = value.minApplyAmount
        val max = value.maxApplyAmount

        if (min == null || max == null) return true

        if (min == 0 && max == 0) return true

        return min < max
    }
}
