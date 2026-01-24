package com.rtc.app.coupon.dto.request.create.command

import com.rtc.app.coupon.dto.request.create.CreateCouponRequest
import com.rtc.app.coupon.type.CouponSubType
import com.rtc.app.coupon.type.CouponType
import com.rtc.app.coupon.entity.DiscountType
import java.time.LocalDateTime

class CreateCouponCommand private constructor(
    val title: String,
    val description: String,
    val type: CouponType,
    val subType: CouponSubType,
    val totalQuantity: Int,
    val discountType: DiscountType,
    val discountValue: Int,
    val minApplyAmount: Int,
    val maxApplyAmount: Int,
    val validateDays: Int,
    val downloadStart: LocalDateTime,
    val downloadEnd: LocalDateTime
) {
    companion object {

        @JvmStatic
        fun from(request: CreateCouponRequest): CreateCouponCommand {
            val discount = request.discountInfo!!
            val period = request.validPeriod!!

            return CreateCouponCommand(
                request.title!!,
                request.description!!,
                request.type!!,
                request.subType!!,
                request.totalQuantity!!,
                discount.discountType!!,
                discount.discountValue!!,
                discount.minApplyAmount!!,
                discount.maxApplyAmount!!,
                period.validateDays!!,
                period.downloadStart!!,
                period.downloadEnd!!
            )
        }
    }
}
