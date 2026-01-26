package com.rtc.app.auth.exception

import com.rtc.app.auth.type.ApiAuthResponseCode
import com.rtc.app.common.response.ResponseCode

class UserNotFoundException(message: String) : RuntimeException(message) {
    val code: ResponseCode = ApiAuthResponseCode.USER_NOT_FOUND
}
