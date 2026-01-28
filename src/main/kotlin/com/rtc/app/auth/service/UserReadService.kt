package com.rtc.app.auth.service

import com.rtc.app.auth.entity.User
import com.rtc.app.auth.repository.UserRepository
import org.springframework.stereotype.Service

/**
 * 인증 패키지 내부에서 사용
 */
@Service
class UserReadService(
    private val userRepository: UserRepository,
) {
    fun findByEmail(email: String): User? =
        userRepository.findByEmail(email)
}
