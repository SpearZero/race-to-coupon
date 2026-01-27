package com.rtc.app.auth.dto.request.command

import com.rtc.app.auth.dto.request.SignUpRequest

class SignUpCommand private constructor(
    val email: String,
    val name: String,
    val password: String,
) {

    companion object {
        @JvmStatic
        fun from(request: SignUpRequest): SignUpCommand {
            return SignUpCommand(
                email = requireNotNull(request.email) { "이메일은 필수입니다." },
                name = requireNotNull(request.name) { "이름은 필수입니다." },
                password = requireNotNull(request.password) { "비밀번호는 필수입니다." }
            )
        }
    }
}