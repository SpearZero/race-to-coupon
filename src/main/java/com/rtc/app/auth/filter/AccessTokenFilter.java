package com.rtc.app.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtc.app.auth.dto.internal.CustomUserDetails;
import com.rtc.app.auth.dto.internal.UserInfo;
import com.rtc.app.auth.exception.JwtAuthenticationException;
import com.rtc.app.auth.exception.JwtExpirationException;
import com.rtc.app.auth.exception.MissingJwtException;
import com.rtc.app.auth.service.authentication.JwtService;
import com.rtc.app.auth.type.ApiAuthResponseCode;
import com.rtc.app.common.exception.dto.ErrorResponse;
import com.rtc.app.common.response.ApiResponse;
import com.rtc.app.common.response.ResponseCode;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class AccessTokenFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final List<String> WHITELIST = List.of("/auth/login", "/sample");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = getAccessTokenFromRequest(request);
            if (accessToken == null || accessToken.isBlank()) {
                throw new MissingJwtException("액세스 토큰이 존재하지 않습니다.");
            }

            UserInfo userInfo = jwtService.getUserInfoFromAccessToken(accessToken);

            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userInfo.getType()));
            UserDetails userDetails = new CustomUserDetails(userInfo.getId(), userInfo.getEmail(), null, authorities);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (ExpiredJwtException e) {
            handleException(response, new JwtExpirationException("토큰이 만료 되었습니다."));
            // 예외 반환 후 즉시 리턴
            return;
        } catch (MissingJwtException e) {
            handleException(response, e);
            return;
        } catch (Exception e) {
            handleException(response, new JwtAuthenticationException("인증이 실패하였습니다."));
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessTokenFromRequest(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && !headerAuth.isBlank() && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }

    private void handleException(HttpServletResponse response, RuntimeException e) throws IOException {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        ResponseCode code = extractResponseCode(e);

        ApiResponse<ErrorResponse> apiResponse = ApiResponse.error(code, e.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    private ResponseCode extractResponseCode(RuntimeException e) {
        return switch (e) {
            case JwtExpirationException ex -> ex.getCode();
            case MissingJwtException ex -> ex.getCode();
            default -> ApiAuthResponseCode.AUTHENTICATION_FAILED;
        };
    }

    // 화이트리스트들은(ex, 로그인) 해당 필터를 수행하면 안된다.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return WHITELIST.stream().anyMatch(path::startsWith);
    }
}