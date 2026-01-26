package com.rtc.app.auth.exception

import com.rtc.app.auth.type.ApiAuthResponseCode
import com.rtc.app.common.response.ResponseCode
import org.springframework.security.core.AuthenticationException

class JwtAuthenticationException(message: String) : AuthenticationException(message) {
    val code: ResponseCode = ApiAuthResponseCode.AUTHENTICATION_FAILED
}
