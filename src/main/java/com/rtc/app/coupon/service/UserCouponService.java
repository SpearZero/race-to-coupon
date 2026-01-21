package com.rtc.app.coupon.service;

import com.rtc.app.coupon.repository.CouponIssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserCouponService {
    private final CouponIssueRepository repository;
}
