package com.rtc.app.auth.dto.internal

import com.rtc.app.auth.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    val id: Long,
    val email: String,
    private val password: String?,
    private val authorities: MutableCollection<out GrantedAuthority?>?
) : UserDetails {

    companion object {

        @JvmStatic
        fun from(user: User): CustomUserDetails {
            val authorities = listOf(SimpleGrantedAuthority(user.type.securityLevel))

            return CustomUserDetails(
                id = user.id!!,
                email = user.email,
                password = user.password,
                authorities = authorities.toMutableList()
            )
        }
    }

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities as Collection<GrantedAuthority>

    override fun getPassword(): String? = password

    override fun getUsername(): String = email
}