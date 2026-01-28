package com.rtc.app.auth.service;

import com.rtc.app.auth.entity.User;
import com.rtc.app.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 인증 패키지 내부에서 사용
 */
@Service
@RequiredArgsConstructor
public class UserReadService {

    private final UserRepository userRepository;

    // TODO: Optional.ofNullable 추후 제거
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }
}
