package com.xun.wang.vlog.blog.console.controller;

import com.xun.wang.vlog.blog.console.domain.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Slf4j
public class AdminController {

    private RestTemplate restTemplate = new RestTemplate();


    @GetMapping("/oauth/callback")
    public void callback (@RequestParam String code, String state, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("state is "+state);

        String oauthServiceUrl = "http://localhost:8383/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("console", "123456");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", "http://localhost:8089/oauth/callback");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<TokenInfo> token = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);
		request.getSession().setAttribute("token", token.getBody().init());
        response.sendRedirect("/");
    }

    @GetMapping("/me")
    public TokenInfo me(HttpServletRequest request) {
        TokenInfo info = (TokenInfo)request.getSession().getAttribute("token");
        return info;
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
    }


}
