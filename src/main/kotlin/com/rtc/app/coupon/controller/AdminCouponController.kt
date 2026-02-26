package com.rtc.app.coupon.controller

import com.rtc.app.auth.dto.internal.CustomUserDetails
import com.rtc.app.common.response.ApiCommonResponseCode
import com.rtc.app.common.response.ApiResponse
import com.rtc.app.common.response.PagedResponse
import com.rtc.app.coupon.dto.request.create.CreateCouponRequest
import com.rtc.app.coupon.dto.request.create.command.CreateCouponCommand
import com.rtc.app.coupon.dto.response.create.CreateCouponResponse
import com.rtc.app.coupon.dto.response.list.CouponSummary
import com.rtc.app.coupon.service.AdminCouponService
import com.rtc.app.coupon.type.ApiCouponResponseCode
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/coupons")
class AdminCouponController(
    private val adminCouponService: AdminCouponService
) {
    @PostMapping("")
    fun create(
        @AuthenticationPrincipal admin: CustomUserDetails,
        @Valid @RequestBody request: CreateCouponRequest
    ): ResponseEntity<ApiResponse<CreateCouponResponse>> {
        val command = CreateCouponCommand.from(request)

        // 쿠폰 생성
        val couponId = adminCouponService.createCoupon(admin.id, command)

        val response = CreateCouponResponse(couponId)

        return ResponseEntity(ApiResponse.of(ApiCouponResponseCode.COUPON_CREATED_SUCCESS, response), HttpStatus.CREATED)
    }

    @DeleteMapping("/{couponId}")
    fun delete(
        @AuthenticationPrincipal admin: CustomUserDetails,
        @PathVariable couponId: Long
    ): ResponseEntity<ApiResponse<Void>> {
        adminCouponService.deleteCoupon(admin.id, couponId)
        return ResponseEntity(ApiResponse.of(ApiCommonResponseCode.SUCCESS, null), HttpStatus.OK)
    }

    @GetMapping("")
    fun list(
        @AuthenticationPrincipal admin: CustomUserDetails,
        @PageableDefault(page = 0, size = 20, sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<ApiResponse<PagedResponse<CouponSummary>>> {
        val body = adminCouponService.listCoupons(pageable)
        return ResponseEntity(ApiResponse.of(ApiCouponResponseCode.COUPON_OK, body), HttpStatus.OK)
    }
}
