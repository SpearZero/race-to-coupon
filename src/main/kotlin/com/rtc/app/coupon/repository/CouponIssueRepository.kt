package com.rtc.app.coupon.repository

import com.rtc.app.coupon.entity.CouponIssue
import org.springframework.data.jpa.repository.JpaRepository

interface CouponIssueRepository : JpaRepository<CouponIssue, Long>
