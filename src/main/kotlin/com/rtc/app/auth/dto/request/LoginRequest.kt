package com.rtc.app.auth.dto.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class LoginRequest (
    val email: String,
    val password: String,
)