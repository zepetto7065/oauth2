package com.example.oauthdemo.oauth;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/login")
    public String login(String code, Model model) {
        log.info("Access Code : {}", code);

        //토큰 얻기
        String access_token = kakaoService.getAccessToken(code); //토큰 받기

        //사용자 정보 얻기
        Map<String, Object> userInfo = kakaoService.getUserInfo(access_token);
        userInfo.forEach((s, o) -> System.out.println(o.toString()));

        model.addAttribute("kakaoInfo", userInfo);

        return "main";
    }


}
