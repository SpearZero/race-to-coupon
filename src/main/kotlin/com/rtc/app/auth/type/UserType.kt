package com.rtc.app.auth.type

enum class UserType(
    val description: String,
    val securityLevel: String,
) {
    USER("유저", "ROLE_USER"),
    ADMIN("운영자", "ROLE_ADMIN"),
    PARTNER("파트너", "ROLE_PARTNER"),
}