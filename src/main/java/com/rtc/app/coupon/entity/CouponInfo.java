package com.rtc.app.coupon.entity;

import com.rtc.app.common.entity.BaseTimeEntity;
import com.rtc.app.coupon.type.CouponStatus;
import com.rtc.app.coupon.type.CouponSubType;
import com.rtc.app.coupon.type.CouponType;
import com.rtc.app.auth.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "coupon_info")
public class CouponInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_info_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_user_id", updatable = false, nullable = false)
    private User createUser;

    /**
     * 쿠폰 입력 코드
     */
    @Column(name = "code", unique = true, length = 10)
    private String code;

    @Column(name = "title", nullable = false, length = 30)
    private String title;

    @Column(name = "description", length = 333)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CouponStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private CouponType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "sub_type", nullable = false, length = 20)
    private CouponSubType subType;


    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;

    @Column(name = "issued_quantity", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer issuedQuantity;

    @Embedded
    private DiscountPolicy discountPolicy;

    @Embedded
    private AvailablePeriod availablePeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modify_user_id")
    private User modifyUser;

    @Builder
    private CouponInfo(String code, String title, String description, CouponType type, CouponSubType subType,
                       DiscountPolicy discountPolicy, AvailablePeriod availablePeriod,
                       Integer totalQuantity, User createUser, User modifyUser) {

        this.code = code;
        this.title = title;
        this.description = description;
        this.status = CouponStatus.ACTIVATED;
        this.type = type;
        this.subType = subType;
        this.discountPolicy = discountPolicy;
        this.availablePeriod = availablePeriod;
        this.totalQuantity = totalQuantity;
        this.issuedQuantity = 0;
        this.createUser = createUser;
        this.modifyUser = modifyUser;
    }

    // 쿠폰 생성
    public static CouponInfo create(String code, String title, String description, CouponType type, CouponSubType subType,
                                    DiscountPolicy discountPolicy, AvailablePeriod availablePeriod,
                                    Integer totalQuantity, User user) {
        return CouponInfo.builder()
                .code(code)
                .title(title)
                .description(description)
                .type(type)
                .subType(subType)
                .discountPolicy(discountPolicy)
                .availablePeriod(availablePeriod)
                .totalQuantity(totalQuantity)
                .createUser(user)
                .modifyUser(user)
                .build();
    }

    // 쿠폰 삭제
    public void deleteCoupon(User user) {
        this.status = CouponStatus.DELETED;
        this.modifyUser = user;
    }
}