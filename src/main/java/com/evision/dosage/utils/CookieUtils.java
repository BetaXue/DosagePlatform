package com.evision.dosage.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Cookie工具类
 *
 * @author: AubreyXue
 * @date: 2020-02-25 15:47
 **/
@Slf4j
@Component
public class CookieUtils {

    @Value("${cookie.domain}")
    private String domain;

    public void clearCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user")) {
                    log.info("cookie:{}", cookie);
                    cookie.setMaxAge(36000);
                    cookie.setPath("/");
                    cookie.setDomain(domain);
                    cookie.setValue("");
                    response.addCookie(cookie);
                }
            }
        }
    }

    public void setCookie(HttpServletRequest request, HttpServletResponse response, String token) throws UnknownHostException {
        // 如果登录成功，将token设置到cookie中
        Cookie cookie = new Cookie("user", token);
        cookie.setMaxAge(36000);
        cookie.setPath("/");
        cookie.setDomain(domain);
        response.addCookie(cookie);
    }


}
