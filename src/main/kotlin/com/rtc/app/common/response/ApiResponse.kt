package com.rtc.app.common.response

import com.rtc.app.common.exception.dto.ErrorResponse

class ApiResponse<T> private constructor(
    val code: Int,
    val message: String,
    val data: T?,
) {
    companion object {
        @JvmStatic
        fun <T> of(code: ResponseCode, data: T?): ApiResponse<T> {
            return ApiResponse(code.code, code.message, data)
        }

        @JvmStatic
        fun error(code: ResponseCode, message: String?): ApiResponse<ErrorResponse> {
            return ApiResponse(code.code, code.message, ErrorResponse(message))
        }

        // 파라미터 검증 에러를 위한 메서드
        @JvmStatic
        fun error(code: ResponseCode, parameters: Map<String, String?>): ApiResponse<Map<String, String?>> {
            return ApiResponse(code.code, code.message, parameters)
        }
    }
}
