package com.rtc.app.auth.repository

import com.rtc.app.auth.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean
    fun existsUserByName(name: String): Boolean
}
