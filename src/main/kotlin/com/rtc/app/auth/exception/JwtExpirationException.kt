package com.rtc.app.auth.exception

import com.rtc.app.auth.type.ApiAuthResponseCode
import com.rtc.app.common.response.ResponseCode
import org.springframework.security.core.AuthenticationException

class JwtExpirationException(message: String) : AuthenticationException(message) {
    val code: ResponseCode = ApiAuthResponseCode.JWT_EXPIRED
}
