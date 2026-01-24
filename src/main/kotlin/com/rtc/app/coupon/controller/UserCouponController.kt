package com.rtc.app.coupon.controller

import com.rtc.app.coupon.service.UserCouponService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/coupons")
class UserCouponController(
    private val userCouponService: UserCouponService
)
