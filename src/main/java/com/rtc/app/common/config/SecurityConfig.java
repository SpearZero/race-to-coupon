package com.rtc.app.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtc.app.auth.filter.EmailPasswordAuthenticationFilter;
import com.rtc.app.auth.filter.AccessTokenFilter;
import com.rtc.app.auth.handler.AuthenticationExceptionHandler;
import com.rtc.app.auth.handler.LoginFailureHandler;
import com.rtc.app.auth.handler.LoginSuccessHandler;
import com.rtc.app.auth.service.authentication.AuthTokenService;
import com.rtc.app.auth.service.authentication.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final AuthTokenService authTokenService;
    private final JwtService jwtService;

    // 개발 하면서 API 접근을 제어 해야 함, 테스트를 위해 모든 API 허용
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/signup", "/sample").permitAll()
                        .requestMatchers(HttpMethod.POST, "/coupons").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/coupons/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(config ->
                        config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager)
                .exceptionHandling(config ->
                        config.authenticationEntryPoint(new AuthenticationExceptionHandler(objectMapper)));

        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    public EmailPasswordAuthenticationFilter authenticationFilter() {
        EmailPasswordAuthenticationFilter authenticationFilter = new EmailPasswordAuthenticationFilter(authenticationManager, objectMapper);
        authenticationFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler(objectMapper, authTokenService));
        authenticationFilter.setAuthenticationFailureHandler(new LoginFailureHandler(objectMapper));

        return authenticationFilter;
    }

    public AccessTokenFilter jwtFilter() {
        return new AccessTokenFilter(objectMapper, jwtService);
    }
}

