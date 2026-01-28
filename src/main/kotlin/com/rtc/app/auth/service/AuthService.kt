package com.rtc.app.auth.service

import com.rtc.app.auth.dto.internal.RefreshTokenWithExpire
import com.rtc.app.auth.dto.request.command.SignUpCommand
import com.rtc.app.auth.dto.response.SignUpResponse
import com.rtc.app.auth.entity.User
import com.rtc.app.auth.exception.DuplicateException
import com.rtc.app.auth.repository.RefreshTokenRepository
import com.rtc.app.auth.repository.UserRepository
import com.rtc.app.auth.service.authentication.JwtService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import kotlin.math.max

@Transactional
@Service
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
) {
    fun logout(userId: Long) {
        // 시간이 유효한 액세스 토큰을 블랙리스트에 넣으면, 모든 API 요청 마다 해당 토큰이 유효한지 체크 해야 함
        refreshTokenRepository.delete(userId)
    }

    fun signUp(command: SignUpCommand): SignUpResponse {
        if (userRepository.existsByEmail(command.email)) {
            throw DuplicateException("이미 존재하는 이메일입니다.")
        }

        if (userRepository.existsUserByName(command.name)) {
            throw DuplicateException("이미 존재하는 이름입니다.")
        }

        val user = User.create(command.email, command.name, passwordEncoder.encode(command.password))
        val savedUser = userRepository.save(user)

        // 액세스 토큰 및 리프레시 토큰 생성
        val accessToken = jwtService.generateAccessToken(
            savedUser.id,
            savedUser.email,
            savedUser.type.securityLevel,
        )
        val refreshToken: RefreshTokenWithExpire = jwtService.generateRefreshToken(savedUser.id)

        // TTL 설정 후, Redis 저장
        val ttl = Duration.ofMillis(
            max(0, refreshToken.expireAt.time - System.currentTimeMillis()),
        )
        refreshTokenRepository.save(refreshToken.refreshToken, savedUser.id, ttl)

        return SignUpResponse(accessToken, refreshToken.refreshToken)
    }
}
