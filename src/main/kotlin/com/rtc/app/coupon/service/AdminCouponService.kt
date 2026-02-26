package com.rtc.app.coupon.service

import com.rtc.app.auth.entity.User
import com.rtc.app.auth.service.query.CouponUserReadService
import com.rtc.app.coupon.dto.request.create.command.CreateCouponCommand
import com.rtc.app.coupon.dto.response.list.CouponSummary
import com.rtc.app.coupon.entity.AvailablePeriod
import com.rtc.app.coupon.entity.CouponInfo
import com.rtc.app.coupon.entity.DiscountPolicy
import com.rtc.app.coupon.exception.CouponCodeDuplicateException
import com.rtc.app.coupon.exception.CouponNotFoundException
import com.rtc.app.coupon.repository.CouponInfoRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import com.rtc.app.common.response.PagedResponse
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service

@Transactional
@Service
class AdminCouponService(
    private val userReadService: CouponUserReadService,
    private val repository: CouponInfoRepository,
    private val couponCodeGenerator: CouponCodeGenerator
) {
    @Retryable(
        retryFor = [DataIntegrityViolationException::class, CouponCodeDuplicateException::class],
        maxAttempts = 5,
        backoff = Backoff(delay = 100)
    )
    fun createCoupon(userId: Long, command: CreateCouponCommand): Long {
        val user: User = userReadService.findById(userId)

        val couponCode = couponCodeGenerator.generateCouponCode()

        val couponInfo = CouponInfo.create(
            couponCode,
            command.title,
            command.description,
            command.type,
            command.subType,
            DiscountPolicy(
                command.discountType,
                command.discountValue,
                command.minApplyAmount,
                command.maxApplyAmount
            ),
            AvailablePeriod(
                command.downloadStart,
                command.downloadEnd,
                command.validateDays
            ),
            command.totalQuantity,
            user
        )

        val savedCoupon = repository.save(couponInfo)

        return checkNotNull(savedCoupon.id) { "쿠폰 저장 후 ID가 NULL 입니다." }
    }

    @Recover
    fun recover(): Long {
        throw CouponCodeDuplicateException("쿠폰 생성 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
    }

    fun deleteCoupon(userId: Long, couponId: Long) {
        val user: User = userReadService.findById(userId)
        val coupon = repository.findById(couponId)
            .orElseThrow { CouponNotFoundException("쿠폰 정보를 찾을 수 없습니다.") }

        coupon.deleteCoupon(user)
    }

    fun listCoupons(pageable: Pageable): PagedResponse<CouponSummary> {
        val page = repository.findAll(pageable)
        val items = page.content.map { info ->
            CouponSummary(
                id = checkNotNull(info.id),
                name = info.title,
                status = info.status,
                downloadStartAt = info.availablePeriod.downloadStart,
                downloadEndAt = info.availablePeriod.downloadEnd,
            )
        }

        return PagedResponse(
            items = items,
            page = page.number,
            size = page.size,
            totalElements = page.totalElements,
            totalPages = page.totalPages,
            hasNext = page.hasNext()
        )
    }
}
