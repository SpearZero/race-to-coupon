package com.rtc.app.coupon.service

import com.rtc.app.coupon.exception.CouponCodeDuplicateException
import com.rtc.app.coupon.repository.CouponInfoRepository
import org.springframework.stereotype.Component
import java.util.concurrent.ThreadLocalRandom

@Component
class CouponCodeGenerator(
    private val repository: CouponInfoRepository,
) {
    fun generateCouponCode(): String {
        repeat(MAX_RETRY) {
            val code = generateRandomCode()
            if (!repository.existsByCode(code)) {
                return code
            }
        }

        throw CouponCodeDuplicateException("쿠폰 코드 생성 실패: 중복 코드 발생")
    }

    private fun generateRandomCode(): String {
        val random = ThreadLocalRandom.current()
        val sb = StringBuilder(CODE_LENGTH)
        repeat(CODE_LENGTH) {
            sb.append(CHAR_POOL[random.nextInt(CHAR_POOL.length)])
        }
        return sb.toString()
    }

    private companion object {
        private const val CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        private const val CODE_LENGTH = 10
        private const val MAX_RETRY = 3
    }
}
