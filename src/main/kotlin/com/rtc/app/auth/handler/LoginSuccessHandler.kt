package com.rtc.app.auth.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtc.app.auth.dto.internal.CustomUserDetails
import com.rtc.app.auth.dto.response.LoginResponse
import com.rtc.app.auth.service.authentication.AuthTokenService
import com.rtc.app.auth.type.ApiAuthResponseCode
import com.rtc.app.common.response.ApiResponse
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import java.nio.charset.StandardCharsets

class LoginSuccessHandler(
    private val objectMapper: ObjectMapper,
    private val authTokenService: AuthTokenService,
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authentication: Authentication,
    ) {
        super<AuthenticationSuccessHandler>.onAuthenticationSuccess(
            request,
            response,
            chain,
            authentication,
        )
    }

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        response.status = HttpServletResponse.SC_OK
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val userDetail = authentication.principal as CustomUserDetails
        // 각 회원은 하나의 역할만 존재
        val role = userDetail.authorities.stream()
            .findFirst()
            .map(GrantedAuthority::getAuthority)
            .orElse(null)

        val loginResponse: LoginResponse =
            authTokenService.issueTokens(userDetail.id, userDetail.email, role)

        val jsonLoginResponse =
            objectMapper.writeValueAsString(
                ApiResponse.of(ApiAuthResponseCode.LOGIN_SUCCESS, loginResponse)
            )
        response.writer.write(jsonLoginResponse)
    }
}
