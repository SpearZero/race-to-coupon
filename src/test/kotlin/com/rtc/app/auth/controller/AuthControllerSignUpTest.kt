package com.rtc.app.auth.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtc.app.auth.dto.request.SignUpRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

// application-test.yml 대신 로컬에서 DB 연결 한 후 테스트
@DisplayName("인증 컨트롤러 회원가입 REST 테스트")
@SpringBootTest
@Transactional
@ExtendWith(RestDocumentationExtension::class)
class AuthControllerSignUpTest @Autowired constructor(
    private val context: WebApplicationContext,
) {

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentation))
            .build()
    }

    @DisplayName("POST /auth/signup")
    @Test
    fun signup() {
        val request = SignUpRequest("test@example.com", "닉네임이요", "test1234!")

        this.mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(request)),
        )
            .andExpect(status().isCreated)
            .andDo(
                document(
                    "auth-signup",
                    requestFields(
                        fieldWithPath("email").description("이메일"),
                        fieldWithPath("name").description("닉네임"),
                        fieldWithPath("password").description("비밀번호"),
                    ),
                    responseFields(
                        fieldWithPath("code").description("응답 코드 (20010003 = 회원가입 성공)"),
                        fieldWithPath("message").description("응답 메시지 (SIGNUP_SUCCESS)"),
                        fieldWithPath("data.access_token").description("발급된 Access Token (JWT)"),
                        fieldWithPath("data.refresh_token").description("발급된 Refresh Token (JWT)"),
                    ),
                ),
            )
    }
}
