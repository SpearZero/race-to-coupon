package com.rtc.app.auth.service.authentication

import com.rtc.app.auth.dto.internal.RefreshTokenWithExpire
import com.rtc.app.auth.dto.internal.UserInfo
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtService(
    @Value("\${jwt.secret}") secret: String,
    @param:Value("\${jwt.expirationMs}") private val expirationMs: Long,
    @param:Value("\${jwt.refreshExpirationMs}") private val refreshExpirationMs: Long,
) {
    private val key: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

    fun generateAccessToken(id: Long?, email: String?, type: String?): String {
        if (id == null) {
            throw IllegalArgumentException("ID는 필수 값입니다.")
        }

        if (email.isNullOrBlank()) {
            throw IllegalArgumentException("이메일은 필수 값입니다.")
        }

        if (type.isNullOrBlank()) {
            throw IllegalArgumentException("유저 타입은 필수 값입니다.")
        }

        val now = Date()
        val exp = Date(now.time + expirationMs)

        return Jwts.builder()
            .signWith(key)
            .header().add("typ", "JWT").and()
            .subject(id.toString())
            .issuedAt(now)
            .expiration(exp)
            .claim("email", email)
            .claim("type", type)
            .compact()
    }

    fun generateRefreshToken(id: Long?): RefreshTokenWithExpire {
        if (id == null) {
            throw IllegalArgumentException("ID는 필수 값입니다.")
        }

        val now = Date()
        val exp = Date(now.time + refreshExpirationMs)

        return RefreshTokenWithExpire(
            Jwts.builder()
                .signWith(key)
                .header().add("typ", "JWT").and()
                .subject(id.toString())
                .issuedAt(now)
                .expiration(exp)
                .compact(),
            exp,
        )
    }

    fun getUserInfoFromAccessToken(token: String): UserInfo {
        val payload: Claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
        val id = payload.subject
        val email = payload.get("email", String::class.java)
        val type = payload.get("type", String::class.java)

        return UserInfo(id.toLong(), email, type)
    }
}
