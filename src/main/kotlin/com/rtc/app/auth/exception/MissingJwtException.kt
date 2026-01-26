package com.rtc.app.auth.exception

import com.rtc.app.auth.type.ApiAuthResponseCode
import com.rtc.app.common.response.ResponseCode
import org.springframework.security.core.AuthenticationException

class MissingJwtException(message: String) : AuthenticationException(message) {
    val code: ResponseCode = ApiAuthResponseCode.MISSING_JWT
}
