package com.rtc.app.common.response

enum class ApiCommonResponseCode(
    override val code: Int,
    override val message: String,
) : ResponseCode {
    SUCCESS(2000000, "SUCCESS"),
    INVALID_PARAMETERS(4000005, "INVALID_PARAMETERS"),
    NOT_READABLE_REQUEST(4000006, "NOT_READABLE_REQUEST"),
    INTERNAL_ERROR(5000000, "INTERNAL_ERROR"),
}
