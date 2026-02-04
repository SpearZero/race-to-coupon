package com.rtc.app.auth.service

import com.rtc.app.auth.service.authentication.JwtService
import com.rtc.app.auth.type.UserType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.NullSource

@DisplayName("JwtService 테스트")
class JwtServiceTest {

    private val secret = "secretsecretsecretsecretsecretse"
    private val expirationMs = 3_600_000L
    private val refreshExpirationMs = 7_200_000L
    private val jwtService = JwtService(secret, expirationMs, refreshExpirationMs)

    @ParameterizedTest(name = "{index} - input id = {0}")
    @NullSource
    @DisplayName("액세스 토큰 생성시 id가 null이면 IllegalArgumentException 던짐")
    fun shouldThrowIllegalArgumentExceptionWhenGenerateAccessTokenIdIsNullOrBlank(id: Long?) {
        // given
        val email = "email@email.com"
        val type = UserType.USER.name

        // when, then
        val exception = assertThrows(IllegalArgumentException::class.java) {
            jwtService.generateAccessToken(id, email, type)
        }

        assertEquals("ID는 필수 값입니다.", exception.message)
        assertEquals(IllegalArgumentException::class.java, exception.javaClass)
    }

    @ParameterizedTest(name = "{index} - input id = {0}")
    @NullAndEmptySource
    @DisplayName("액세스 토큰 생성시 email이 null 또는 공백이면 IllegalArgumentException 던짐")
    fun shouldThrowIllgalArgumentExceptionWhenGenerateAccessTokenEmailIsNullOrBlank(email: String?) {
        // given
        val id = 1L
        val type = UserType.USER.name

        // when, then
        val exception = assertThrows(IllegalArgumentException::class.java) {
            jwtService.generateAccessToken(id, email, type)
        }

        assertEquals("이메일은 필수 값입니다.", exception.message)
        assertEquals(IllegalArgumentException::class.java, exception.javaClass)
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("액세스 토큰 생성시 type(유저)이 null 또는 공백이면 IllegalArgumentException 던짐")
    fun shouldThrowIllgalArgumentExceptionWhenGenerateAccessTokenTypeIsNull(type: String?) {
        // given
        val id = 1L
        val email = "email@email.com"

        // when, then
        val exception = assertThrows(IllegalArgumentException::class.java) {
            jwtService.generateAccessToken(id, email, type)
        }

        assertEquals("유저 타입은 필수 값입니다.", exception.message)
        assertEquals(IllegalArgumentException::class.java, exception.javaClass)
    }
}
