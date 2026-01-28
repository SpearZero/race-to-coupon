package com.rtc.app.auth.service.query

import com.rtc.app.auth.entity.User
import com.rtc.app.auth.exception.UserNotFoundException
import com.rtc.app.auth.repository.UserRepository
import org.springframework.stereotype.Service

/**
 * 쿠폰 패키지에서 사용
 */
@Service
class CouponUserReadService(
    private val userRepository: UserRepository,
) {
    fun findById(id: Long): User {
        return userRepository.findById(id)
            .orElseThrow { UserNotFoundException("해당 사용자를 찾을 수 없습니다.") }
    }
}
