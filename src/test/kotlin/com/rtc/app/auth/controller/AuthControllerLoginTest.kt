package com.rtc.app.auth.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtc.app.auth.dto.request.LoginRequest
import com.rtc.app.auth.entity.User
import com.rtc.app.auth.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext

import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

// application-test.yml 대신 로컬에서 DB 연결 한 후 테스트
@DisplayName("인증 컨트롤러 로그인 REST 테스트")
@SpringBootTest
@Transactional
@ExtendWith(RestDocumentationExtension::class)
class AuthControllerLoginTest @Autowired constructor(
    private val context: WebApplicationContext,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    private lateinit var mockMvc: MockMvc

    private val email = "login@test.com"
    private val name = "닉네임"
    private val password = "test1234!"

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        userRepository.save(User.create(email, name, passwordEncoder.encode(password)))
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentation))
            .build()
    }

    @DisplayName("POST /auth/login")
    @Test
    fun signup() {
        val request = LoginRequest(email, password)

        this.mockMvc.perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(request)),
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andDo(
                document(
                    "auth-login",
                    requestFields(
                        fieldWithPath("email").description("로그인할 사용자 이메일"),
                        fieldWithPath("password").description("비밀번호"),
                    ),
                    responseFields(
                        fieldWithPath("code").description("응답 코드"),
                        fieldWithPath("message").description("응답 메시지"),
                        fieldWithPath("data.access_token").description("발급된 AccessToken"),
                        fieldWithPath("data.refresh_token").description("발급된 RefreshToken"),
                    ),
                ),
            )
    }
}
