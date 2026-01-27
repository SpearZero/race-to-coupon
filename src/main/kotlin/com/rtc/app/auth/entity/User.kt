package com.rtc.app.auth.entity

import com.rtc.app.auth.type.UserType
import com.rtc.app.common.entity.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "coupon_user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_user_id", nullable = false, updatable = false)
    val id: Long? = null,

    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Column(name = "password", nullable = false, length = 60)
    var password: String,

    @Column(name = "name", nullable = false, length = 30, unique = true)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 10)
    val type: UserType = UserType.USER,
) : BaseTimeEntity() {

    companion object {
        @JvmStatic
        fun create(email: String, name: String, password: String): User {
            return User(
                email = email,
                name = name,
                password = password,
                type = UserType.USER
            )
        }
    }

    // 비밀번호 변경 같은 로직은 여기에 메서드로 추가
    fun updatePassword(newPassword: String) {
        this.password = newPassword
    }
}
