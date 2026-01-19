package com.rtc.app.coupon.dto.request.update.command

import com.rtc.app.coupon.dto.request.update.UpdateCouponRequest
import com.rtc.app.coupon.type.CouponStatus
import java.time.LocalDateTime

class UpdateCouponCommand private constructor(
    val title: String?,
    val description: String?,
    val downloadStartDate: LocalDateTime?,
    val downloadEndDate: LocalDateTime?,
    val status: CouponStatus?
) {
    companion object {
        
        @JvmStatic
        fun from(request: UpdateCouponRequest): UpdateCouponCommand {
            return UpdateCouponCommand(
                request.title,
                request.description,
                request.downloadStartDate,
                request.downloadEndDate,
                request.status
            )
        }
    }
}
