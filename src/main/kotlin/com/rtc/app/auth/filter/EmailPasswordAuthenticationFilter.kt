package com.rtc.app.auth.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtc.app.auth.dto.request.LoginRequest
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import java.io.IOException

/**
 * JSON 이메일, 패스워드 로그인을 위한 필터
 */
class EmailPasswordAuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val objectMapper: ObjectMapper,
) : AbstractAuthenticationProcessingFilter(DEFAULT_REQUEST_MATCHER, authenticationManager) {

    @Throws(IOException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        if (request.method != "POST") {
            throw AuthenticationServiceException("Authentication method not supported: ${request.method}")
        }

        val loginRequest = objectMapper.readValue(request.inputStream, LoginRequest::class.java)
        val email = loginRequest.email
        val password = loginRequest.password

        val authRequest = UsernamePasswordAuthenticationToken.unauthenticated(email, password)
        authRequest.details = this.authenticationDetailsSource.buildDetails(request)
        return this.authenticationManager.authenticate(authRequest)
    }

    companion object {
        private val DEFAULT_REQUEST_MATCHER: RequestMatcher = PathPatternRequestMatcher.withDefaults()
            .matcher(HttpMethod.POST, "/auth/login")
    }
}
