package com.rtc.app.coupon.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.time.LocalDateTime

@Embeddable
data class AvailablePeriod(
    @Column(name = "download_start", nullable = false)
    val downloadStart: LocalDateTime,

    @Column(name = "download_end", nullable = false)
    val downloadEnd: LocalDateTime,

    @Column(name = "validate_days_after_download", nullable = false, updatable = false)
    val validateDaysAfterDownload: Int
) {

}