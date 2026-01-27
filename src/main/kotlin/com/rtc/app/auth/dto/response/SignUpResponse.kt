package com.rtc.app.auth.dto.response

data class SignUpResponse(
    val accessToken: String,
    val refreshToken: String,
)
