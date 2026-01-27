package com.rtc.app.auth.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class SignUpRequest (

    @field:Email
    @field:NotBlank("이메일은 필수입니다.")
    val email: String?,

    @field:NotBlank("이름은 필수입니다.")
    @field:Pattern(
        regexp = "^[가-힣a-zA-Z0-9]{1,10}$",
        message = "한글,영어,숫자 1~10자리를 입력해주세요",
    )
    val name: String?,

    @field:NotBlank("비밀번호는 필수입니다.")
    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*]).{8,20}$",
        message = "영문 소문자, 특수문자(!@#$%^&*), 숫자를 포함한 8~20자리를 입력해주세요",
    )
    val password: String?,
)
