package com.example.oauthdemo.oauth;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KakaoServiceImpl implements KakaoService {

    @Value("${custom.oauth2.kakao.client-id}")
    private String CLIENT_ID;

    public String getAccessToken(String code) {
        String accessToken = "";
        String refeshToken = "";
        String reqUrl = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id="+CLIENT_ID);
            sb.append("&redirect_uri=http://localhost:8080/login");
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            log.info("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body : " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refeshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            log.info("access token : {}", accessToken);
            log.info("refresh token : {}", refeshToken);

            br.close();
            bw.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return accessToken;
    }

    @Override
    public Map<String, Object> getUserInfo(String access_token) {

        //요청하는 클라이언트마다 가진 정보가 다를 수 있으므로
        Map<String, Object> userInfo = new HashMap<>();
        String reqUrl = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + access_token);

            int responseCode = conn.getResponseCode();
            log.info("responseCode : {}",responseCode);

            BufferedReader br= new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            while((line = br.readLine()) != null){
                result += line;
            }
            log.info("response body : {}",result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
//            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();

            userInfo.put("nickname", nickname);
//            userInfo.put("email", email);

        } catch (Exception e) {
            log.error("e -> {}",e.getMessage());
            throw new RuntimeException();
        }

        return userInfo;
    }
}
