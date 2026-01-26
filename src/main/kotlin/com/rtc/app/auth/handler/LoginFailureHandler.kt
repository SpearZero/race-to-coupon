package com.rtc.app.auth.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtc.app.auth.type.ApiAuthResponseCode
import com.rtc.app.common.response.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import java.nio.charset.StandardCharsets

class LoginFailureHandler(
    private val objectMapper: ObjectMapper,
) : AuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException,
    ) {
        response.status = HttpServletResponse.SC_BAD_REQUEST
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        // TODO: 실패 로그 나중에 찍어야 함
        val jsonLoginResponse =
            objectMapper.writeValueAsString(
                ApiResponse.error(ApiAuthResponseCode.LOGIN_FAIL, "로그인에 실패하였습니다.")
            )
        response.writer.write(jsonLoginResponse)
    }
}
