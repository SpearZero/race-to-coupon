package com.rtc.app.auth.service.authentication

import com.rtc.app.auth.dto.internal.RefreshTokenWithExpire
import com.rtc.app.auth.dto.response.LoginResponse
import com.rtc.app.auth.repository.RefreshTokenRepository
import org.springframework.stereotype.Service
import java.time.Duration
import kotlin.math.max

@Service
class AuthTokenService(
    private val jwtService: JwtService,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    fun issueTokens(userId: Long, email: String, type: String): LoginResponse {
        val accessToken = jwtService.generateAccessToken(userId, email, type)
        val refreshToken: RefreshTokenWithExpire = jwtService.generateRefreshToken(userId)

        val ttl = Duration.ofMillis(
            max(0, refreshToken.expireAt.time - System.currentTimeMillis()),
        )
        refreshTokenRepository.save(refreshToken.refreshToken, userId, ttl)

        return LoginResponse(accessToken, refreshToken.refreshToken)
    }
}
