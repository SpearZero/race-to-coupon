package com.rtc.app.coupon.service;

import com.rtc.app.auth.entity.User;
import com.rtc.app.auth.service.query.CouponUserReadService;
import com.rtc.app.coupon.dto.request.create.command.CreateCouponCommand;
import com.rtc.app.coupon.entity.AvailablePeriod;
import com.rtc.app.coupon.entity.CouponInfo;
import com.rtc.app.coupon.entity.DiscountPolicy;
import com.rtc.app.coupon.exception.CouponCodeDuplicateException;
import com.rtc.app.coupon.exception.CouponNotFoundException;
import com.rtc.app.coupon.repository.CouponInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminCouponService {
    private final CouponUserReadService userReadService;
    private final CouponInfoRepository repository;
    private final CouponCodeGenerator couponCodeGenerator;

    @Retryable(
            retryFor = {DataIntegrityViolationException.class, CouponCodeDuplicateException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 100)
    )
    public Long createCoupon(Long userId, CreateCouponCommand command) {
        User user = userReadService.findById(userId);

        String couponCode = couponCodeGenerator.generateCouponCode();

        CouponInfo couponInfo = CouponInfo.create(couponCode, command.getTitle(), command.getDescription(), command.getType(), command.getSubType(),
                new DiscountPolicy(command.getDiscountType(), command.getDiscountValue(), command.getMinApplyAmount(), command.getMaxApplyAmount()),
                new AvailablePeriod(command.getDownloadStart(), command.getDownloadEnd(), command.getValidateDays()),
                command.getTotalQuantity(), user);

        CouponInfo savedCoupon = repository.save(couponInfo);

        return savedCoupon.getId();
    }

    @Recover
    public Long recover() {
        throw new CouponCodeDuplicateException("쿠폰 생성 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
    }

    public void deleteCoupon(Long userId, Long couponId) {
        User user = userReadService.findById(userId);
        CouponInfo coupon = repository.findById(couponId).orElseThrow(() -> new CouponNotFoundException("쿠폰 정보를 찾을 수 없습니다."));

        coupon.deleteCoupon(user);
    }
}
