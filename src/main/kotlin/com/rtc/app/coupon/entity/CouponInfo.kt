package com.rtc.app.coupon.entity

import com.rtc.app.auth.entity.User
import com.rtc.app.common.entity.BaseTimeEntity
import com.rtc.app.coupon.type.CouponStatus
import com.rtc.app.coupon.type.CouponSubType
import com.rtc.app.coupon.type.CouponType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
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

@Entity
@Table(name = "coupon_info")
class CouponInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_info_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_user_id", updatable = false, nullable = false)
    val createUser: User,

    @Column(name = "code", unique = true, length = 10)
    val code: String?,

    @Column(name = "title", nullable = false, length = 30)
    val title: String,

    @Column(name = "description", length = 333)
    val description: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    var status: CouponStatus = CouponStatus.ACTIVATED,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    val type: CouponType,

    @Enumerated(EnumType.STRING)
    @Column(name = "sub_type", nullable = false, length = 20)
    val subType: CouponSubType,

    @Column(name = "total_quantity", nullable = false)
    val totalQuantity: Int,

    @Column(name = "issued_quantity", nullable = false)
    var issuedQuantity: Int = 0,

    @Embedded
    val discountPolicy: DiscountPolicy,

    @Embedded
    val availablePeriod: AvailablePeriod,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modify_user_id")
    var modifyUser: User
) : BaseTimeEntity() {

    companion object {
        /**
         * 쿠폰 생성 정적 팩토리 메서드
         */
        @JvmStatic
        fun create(
            code: String?,
            title: String,
            description: String?,
            type: CouponType,
            subType: CouponSubType,
            discountPolicy: DiscountPolicy,
            availablePeriod: AvailablePeriod,
            totalQuantity: Int,
            user: User
        ): CouponInfo {
            return CouponInfo(
                code = code,
                title = title,
                description = description,
                type = type,
                subType = subType,
                discountPolicy = discountPolicy,
                availablePeriod = availablePeriod,
                totalQuantity = totalQuantity,
                createUser = user,
                modifyUser = user
            )
        }
    }

    /**
     * 쿠폰 삭제 로직
     */
    fun deleteCoupon(user: User) {
        this.status = CouponStatus.DELETED
        this.modifyUser = user
    }
}