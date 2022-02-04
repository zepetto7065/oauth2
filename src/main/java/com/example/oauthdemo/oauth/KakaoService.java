package com.example.oauthdemo.oauth;

import java.util.Map;

public interface KakaoService {
    String getAccessToken(String code);

    Map<String, Object> getUserInfo(String access_token);
}
