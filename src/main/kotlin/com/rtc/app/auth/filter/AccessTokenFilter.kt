package com.rtc.app.auth.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtc.app.auth.dto.internal.CustomUserDetails
import com.rtc.app.auth.dto.internal.UserInfo
import com.rtc.app.auth.exception.JwtAuthenticationException
import com.rtc.app.auth.exception.JwtExpirationException
import com.rtc.app.auth.exception.MissingJwtException
import com.rtc.app.auth.service.authentication.JwtService
import com.rtc.app.auth.type.ApiAuthResponseCode
import com.rtc.app.common.exception.dto.ErrorResponse
import com.rtc.app.common.response.ApiResponse
import com.rtc.app.common.response.ResponseCode
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class AccessTokenFilter(
    private val objectMapper: ObjectMapper,
    private val jwtService: JwtService,
) : OncePerRequestFilter() {

    private val whitelist = listOf("/auth/login", "/auth/signup", "/sample")

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val accessToken = getAccessTokenFromRequest(request)
            if (accessToken.isNullOrBlank()) {
                throw MissingJwtException("액세스 토큰이 존재하지 않습니다.")
            }

            val userInfo: UserInfo = jwtService.getUserInfoFromAccessToken(accessToken)

            val authorities: List<GrantedAuthority> =
                listOf(SimpleGrantedAuthority(userInfo.type))
            val userDetails: UserDetails =
                CustomUserDetails(userInfo.id, userInfo.email, null, authorities.toMutableList())

            val authentication =
                UsernamePasswordAuthenticationToken(userDetails, null, authorities)

            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: ExpiredJwtException) {
            handleException(response, JwtExpirationException("토큰이 만료 되었습니다."))
            return
        } catch (e: MissingJwtException) {
            handleException(response, e)
            return
        } catch (e: Exception) {
            handleException(response, JwtAuthenticationException("인증이 실패하였습니다."))
            return
        }

        filterChain.doFilter(request, response)
    }

    private fun getAccessTokenFromRequest(request: HttpServletRequest): String? {
        val headerAuth = request.getHeader("Authorization")

        if (!headerAuth.isNullOrBlank() && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7)
        }

        return null
    }

    private fun handleException(response: HttpServletResponse, e: RuntimeException) {
        SecurityContextHolder.clearContext()
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = "application/json;charset=UTF-8"

        val code = extractResponseCode(e)

        val apiResponse: ApiResponse<ErrorResponse> = ApiResponse.error(code, e.message)
        response.writer.write(objectMapper.writeValueAsString(apiResponse))
    }

    private fun extractResponseCode(e: RuntimeException): ResponseCode {
        return when (e) {
            is JwtExpirationException -> e.code
            is MissingJwtException -> e.code
            else -> ApiAuthResponseCode.AUTHENTICATION_FAILED
        }
    }

    // 화이트리스트들은(ex, 로그인) 해당 필터를 수행하면 안된다.
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        return whitelist.any { path.startsWith(it) }
    }
}
