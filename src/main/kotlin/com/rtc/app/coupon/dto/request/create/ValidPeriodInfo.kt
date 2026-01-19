package com.rtc.app.coupon.dto.request.create

import com.rtc.app.coupon.dto.request.create.validator.ValidPeriod
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@ValidPeriod
data class ValidPeriodInfo(
    @field:NotNull val validateDays: Int?,
    @field:NotNull val downloadStart: LocalDateTime?,
    @field:NotNull val downloadEnd: LocalDateTime?
)
