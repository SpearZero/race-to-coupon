package com.rtc.app.auth.dto.internal

import java.util.Date

data class RefreshTokenWithExpire(
    val refreshToken: String,
    var expireAt: Date,
)