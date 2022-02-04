package com.example.oauthdemo.oauth;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class KakaoControllerTest {

    @Autowired
    MockMvc mockMvc;


    @Value("${custom.oauth2.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${custom.oauth2.kakao.redirect-url}")
    private String REDIRECT_URL;

    private String reqURL = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URL;
    private String code = "";

    @Test
    void 카카오_로그인_통합_테스트() throws Exception {
        mockMvc.perform(get("/login").param("code",code))
                .andDo(print())
                .andExpect(status().isOk());
    }

}