package com.rtc.app.common.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import java.io.IOException

@Configuration
class RedissonConfig(
    @Value("\${redisson.config}")
    private val configFile: Resource
) {

    @Bean(destroyMethod = "shutdown")
    fun redissonClient(): RedissonClient {
        val config = Config.fromYAML(configFile.inputStream)
        return Redisson.create(config)
    }
}
