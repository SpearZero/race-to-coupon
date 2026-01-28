package com.rtc.app.auth.repository

import org.redisson.api.RBucket
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RefreshTokenRepository(
    private val client: RedissonClient,
) {
    private val refreshTokenPrefix = "refreshToken:"

    fun save(refreshToken: String, userId: Long?, ttl: Duration) {
        val bucket: RBucket<String> = client.getBucket(refreshTokenPrefix + userId)
        bucket.set(refreshToken, ttl)
    }

    fun delete(userId: Long?) {
        val bucket: RBucket<String> = client.getBucket(refreshTokenPrefix + userId)
        bucket.delete()
    }
}
