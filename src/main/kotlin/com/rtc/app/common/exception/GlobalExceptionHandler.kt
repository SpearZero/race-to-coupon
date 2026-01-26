package com.rtc.app.common.exception

import com.rtc.app.auth.exception.DuplicateException
import com.rtc.app.auth.exception.UserNotFoundException
import com.rtc.app.common.exception.dto.ErrorResponse
import com.rtc.app.common.response.ApiCommonResponseCode
import com.rtc.app.common.response.ApiResponse
import com.rtc.app.coupon.exception.CouponCodeDuplicateException
import com.rtc.app.coupon.exception.CouponNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.HashMap

@RestControllerAdvice
class GlobalExceptionHandler {

    // 파라미터 검증 오류
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        exception: MethodArgumentNotValidException,
    ): ResponseEntity<ApiResponse<Map<String, String?>>> {
        val errors = HashMap<String, String?>()

        for (fieldError in exception.bindingResult.fieldErrors) {
            errors[fieldError.field] = fieldError.defaultMessage
        }

        return ResponseEntity(
            ApiResponse.error(ApiCommonResponseCode.INVALID_PARAMETERS, errors),
            HttpStatus.BAD_REQUEST,
        )
    }

    // 값 중복 에러(이메일, 닉네임 등)
    @ExceptionHandler(DuplicateException::class)
    fun handleDuplicationException(
        exception: DuplicateException,
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ResponseEntity(
            ApiResponse.error(exception.code, exception.message),
            HttpStatus.BAD_REQUEST,
        )
    }

    // 유저를 찾을 수 없음
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(
        exception: UserNotFoundException,
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ResponseEntity(
            ApiResponse.error(exception.code, exception.message),
            HttpStatus.BAD_REQUEST,
        )
    }

    // 쿠폰을 찾을 수 없음
    @ExceptionHandler(CouponNotFoundException::class)
    fun handleCouponNotFoundException(
        exception: CouponNotFoundException,
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ResponseEntity(
            ApiResponse.error(exception.code, exception.message),
            HttpStatus.BAD_REQUEST,
        )
    }

    // 쿠폰 코드 생성 실패
    @ExceptionHandler(CouponCodeDuplicateException::class)
    fun handleUserNotFoundException(
        exception: CouponCodeDuplicateException,
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ResponseEntity(
            ApiResponse.error(exception.code, exception.message),
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }

    // 파싱 오류
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(
        exception: HttpMessageNotReadableException,
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        val response = ApiCommonResponseCode.NOT_READABLE_REQUEST
        return ResponseEntity(
            ApiResponse.error(response, response.message),
            HttpStatus.BAD_REQUEST,
        )
    }

    // 전체 에러
    @ExceptionHandler(Exception::class)
    fun handleException(
        exception: Exception,
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        val response = ApiCommonResponseCode.INTERNAL_ERROR
        return ResponseEntity(
            ApiResponse.error(response, response.message),
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }
}