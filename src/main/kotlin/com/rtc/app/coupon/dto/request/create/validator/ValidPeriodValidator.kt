package com.rtc.app.coupon.dto.request.create.validator

import com.rtc.app.coupon.dto.request.create.ValidPeriodInfo
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ValidPeriodValidator : ConstraintValidator<ValidPeriod, ValidPeriodInfo> {

    override fun isValid(value: ValidPeriodInfo?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return true

        val start = value.downloadStart
        val end = value.downloadEnd

        if (start == null || end == null) return true

        return start.isBefore(end)
    }
}
