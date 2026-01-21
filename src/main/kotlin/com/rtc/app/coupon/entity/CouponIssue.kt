package com.rtc.app.coupon.entity

import com.rtc.app.auth.entity.User
import com.rtc.app.common.entity.BaseTimeEntity
import com.rtc.app.coupon.type.CouponIssueStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "coupon_issue")
class CouponIssue(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_issue_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_info_id", nullable = false)
    val couponInfo: CouponInfo,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_user_id", nullable = false)
    val user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    var status: CouponIssueStatus = CouponIssueStatus.ISSUED,

    /**
     * 쿠폰 사용 가능 시작 시간
     */
    @Column(name = "usable_start", nullable = false)
    val usableStart: LocalDateTime,

    /**
     * 쿠폰 사용 가능 종료 시간
     */
    @Column(name = "usable_end", nullable = false)
    val usableEnd: LocalDateTime
) : BaseTimeEntity() {

}