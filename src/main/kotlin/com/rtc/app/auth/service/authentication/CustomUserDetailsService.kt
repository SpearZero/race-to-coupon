package com.rtc.app.auth.service.authentication

import com.rtc.app.auth.dto.internal.CustomUserDetails
import com.rtc.app.auth.service.UserReadService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userReadService: UserReadService,
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userReadService.findByEmail(email)
            ?: throw IllegalArgumentException("회원이 존재하지 않습니다.")

        return CustomUserDetails.from(user)
    }
}
