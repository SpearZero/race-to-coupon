package com.rtc.app.auth.dto.request

import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("SignUpRequest 테스트")
class SignUpRequestTest {

    companion object {
        private lateinit var validator: Validator

        @JvmStatic
        @BeforeAll
        fun setUp() {
            val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
            validator = factory.validator
        }
    }

    @ParameterizedTest(name = "{index} - input email = {0}")
    @NullAndEmptySource
    @DisplayName("이메일에 공백 또는 null이 들어올 경우 검증 실패")
    fun shouldValidationFailWhenEmailIsBlankOrNull(email: String?) {
        // given
        val name = "nickName"
        val password = "passwWord123!"
        val request = SignUpRequest(email, name, password)

        // when
        val violations: Set<ConstraintViolation<SignUpRequest>> = validator.validate(request)

        // then
        assertThat(violations).isNotEmpty()
        assertThat(violations.any { violation -> violation.propertyPath.toString() == "email" }).isTrue
    }

    @ParameterizedTest(name = "{index} - input nickname = {0}")
    @ValueSource(strings = ["invalid", "email@"])
    @DisplayName("이메일 형식이 유효하지 않을 경우 검증 실패")
    fun shouldFailValidationWhenEmailFormatIsInvalid(email: String) {
        // given
        val name = "nickName"
        val password = "passwWord123!"
        val request = SignUpRequest(email, name, password)

        // when
        val violations: Set<ConstraintViolation<SignUpRequest>> = validator.validate(request)

        // then
        assertThat(violations).isNotEmpty()
        assertThat(violations.any { violation -> violation.propertyPath.toString() == "email" }).isTrue
    }

    @ParameterizedTest(name = "{index} - input nickname = {0}")
    @ValueSource(strings = ["email@email.com", "abc.c@email.com"])
    @DisplayName("이메일 형식이 올바른 경우 유효성 검사 성공")
    fun shouldPassValidationWhenEmailFormatIsValid(email: String) {
        // given
        val name = "nickName"
        val password = "passwWord123!"
        val request = SignUpRequest(email, name, password)

        // when
        val violations: Set<ConstraintViolation<SignUpRequest>> = validator.validate(request)

        // then
        assertThat(violations).isEmpty()
    }

    @ParameterizedTest(name = "{index} - input name = {0}")
    @NullAndEmptySource
    @DisplayName("이름에 공백 또는 null이 들어올 경우 검증 실패")
    fun shouldValidationFailWhenNameIsBlankOrNull(name: String?) {
        // given
        val email = "email@email.com"
        val password = "passwWord123!"
        val request = SignUpRequest(email, name, password)

        // when
        val violations: Set<ConstraintViolation<SignUpRequest>> = validator.validate(request)

        // then
        assertThat(violations).isNotEmpty()
        assertThat(violations.any { violation -> violation.propertyPath.toString() == "name" }).isTrue
    }

    @ParameterizedTest(name = "{index} - input nickname = {0}")
    @ValueSource(strings = ["안녕!", "안녕★"])
    @DisplayName("이름 형식이 유효하지 않을 경우 검증 실패")
    fun shouldFailValidationWhenNameFormatIsInvalid(name: String) {
        // given
        val email = "email@email.com"
        val password = "passwWord123!"
        val request = SignUpRequest(email, name, password)

        // when
        val violations: Set<ConstraintViolation<SignUpRequest>> = validator.validate(request)

        // then
        assertThat(violations).isNotEmpty()
        assertThat(violations.any { violation -> violation.propertyPath.toString() == "name" }).isTrue
    }

    @ParameterizedTest(name = "{index} - input nickname = {0}")
    @ValueSource(strings = ["안녕안녕안녕안녕안녕안", "bcdefg12345"])
    @DisplayName("이름 길이가 유효하지 않을 경우 검증 실패")
    fun shouldFailValidationWhenNameLengthIsInvalid(name: String) {
        // given
        val email = "email@email.com"
        val password = "passwWord123!"
        val request = SignUpRequest(email, name, password)

        // when
        val violations: Set<ConstraintViolation<SignUpRequest>> = validator.validate(request)

        // then
        assertThat(violations).isNotEmpty()
        assertThat(violations.any { violation -> violation.propertyPath.toString() == "name" }).isTrue
    }

    @ParameterizedTest(name = "{index} - input nickname = {0}")
    @ValueSource(strings = ["안녕하세요", "반갑습니다"])
    @DisplayName("이름이 유효할 경우 검증 성공")
    fun shouldPassValidationWhenNameFormatIsValid(name: String) {
        // given
        val email = "email@email.com"
        val password = "passwWord123!"
        val request = SignUpRequest(email, name, password)

        // when
        val violations: Set<ConstraintViolation<SignUpRequest>> = validator.validate(request)

        // then
        assertThat(violations).isEmpty()
    }

    @ParameterizedTest(name = "{index} - input name = {0}")
    @NullAndEmptySource
    @DisplayName("패스워드에 공백 또는 null이 들어올 경우 검증 실패")
    fun shouldValidationFailWhenPasswordIsBlankOrNull(password: String?) {
        // given
        val email = "email@email.com"
        val name = "이름"
        val request = SignUpRequest(email, name, password)

        // when
        val violations: Set<ConstraintViolation<SignUpRequest>> = validator.validate(request)

        // then
        assertThat(violations).isNotEmpty()
        assertThat(violations.any { violation -> violation.propertyPath.toString() == "password" }).isTrue
    }

    @ParameterizedTest(name = "{index} - input nickname = {0}")
    @ValueSource(strings = ["dkssudgktpdy2!zaefda1", "dmdkr%2"])
    @DisplayName("패스워드 길이가 유효하지 않을 경우 검증 실패")
    fun shouldFailValidationWhenPasswordLengthIsInvalid(password: String) {
        // given
        val email = "email@email.com"
        val name = "이름"
        val request = SignUpRequest(email, name, password)

        // when
        val violations: Set<ConstraintViolation<SignUpRequest>> = validator.validate(request)

        // then
        assertThat(violations).isNotEmpty()
        assertThat(violations.any { violation -> violation.propertyPath.toString() == "password" }).isTrue
    }

    @ParameterizedTest(name = "{index} - input nickname = {0}")
    @ValueSource(strings = ["dkssudgktpdy"])
    @DisplayName("패스워드 형식 유효하지 않을 경우 검증 실패")
    fun shouldFailValidationWhenPasswordFormatIsInvalid(password: String) {
        // given
        val email = "email@email.com"
        val name = "이름"
        val request = SignUpRequest(email, name, password)

        // when
        val violations: Set<ConstraintViolation<SignUpRequest>> = validator.validate(request)

        // then
        assertThat(violations).isNotEmpty
        assertThat(violations.any { violation -> violation.propertyPath.toString() == "password" }).isTrue
    }

    @ParameterizedTest(name = "{index} - input nickname = {0}")
    @ValueSource(strings = ["pass1ord!", "helloHi$4"])
    @DisplayName("패스워드 형식이 올바른 경우 유효성 검사 성공")
    fun shouldPassValidationWhenPasswordFormatIsValid(password: String) {
        // given
        val email = "email@email.com"
        val name = "안녕하세요"
        val request = SignUpRequest(email, name, password)

        // when
        val violations: Set<ConstraintViolation<SignUpRequest>> = validator.validate(request)

        // then
        assertThat(violations).isEmpty()
    }
}
