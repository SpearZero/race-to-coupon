package com.rtc.app.coupon.service

import com.rtc.app.coupon.repository.CouponIssueRepository
import org.springframework.stereotype.Service

@Service
class UserCouponService(
    private val repository: CouponIssueRepository
)
