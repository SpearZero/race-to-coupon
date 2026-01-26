package com.rtc.app.auth.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtc.app.auth.type.ApiAuthResponseCode
import com.rtc.app.common.exception.dto.ErrorResponse
import com.rtc.app.common.response.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import java.nio.charset.StandardCharsets

class AuthenticationExceptionHandler(
    private val objectMapper: ObjectMapper,
) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val errorResponse: ApiResponse<ErrorResponse> =
            ApiResponse.error(ApiAuthResponseCode.AUTHENTICATION_FAILED, "인증에 실패하였습니다.")

        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}
