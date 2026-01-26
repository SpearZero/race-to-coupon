package com.rtc.app.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtc.app.auth.filter.AccessTokenFilter
import com.rtc.app.auth.filter.EmailPasswordAuthenticationFilter
import com.rtc.app.auth.handler.AuthenticationExceptionHandler
import com.rtc.app.auth.handler.LoginFailureHandler
import com.rtc.app.auth.handler.LoginSuccessHandler
import com.rtc.app.auth.service.authentication.AuthTokenService
import com.rtc.app.auth.service.authentication.JwtService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authenticationManager: AuthenticationManager,
    private val objectMapper: ObjectMapper,
    private val authTokenService: AuthTokenService,
    private val jwtService: JwtService,
) {

    // 개발 하면서 API 접근을 제어 해야 함, 테스트를 위해 모든 API 허용
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/auth/login", "/auth/signup", "/sample").permitAll()
                    .requestMatchers(HttpMethod.POST, "/coupons").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/coupons/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            }
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .sessionManagement { config ->
                config.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationManager(authenticationManager)
            .exceptionHandling { config ->
                config.authenticationEntryPoint(AuthenticationExceptionHandler(objectMapper))
            }

        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    fun authenticationFilter(): EmailPasswordAuthenticationFilter {
        val authenticationFilter = EmailPasswordAuthenticationFilter(authenticationManager, objectMapper)
        authenticationFilter.setAuthenticationSuccessHandler(LoginSuccessHandler(objectMapper, authTokenService))
        authenticationFilter.setAuthenticationFailureHandler(LoginFailureHandler(objectMapper))

        return authenticationFilter
    }

    fun jwtFilter(): AccessTokenFilter {
        return AccessTokenFilter(objectMapper, jwtService)
    }
}
