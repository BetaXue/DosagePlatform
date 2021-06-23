package com.evision.dosage.utils;

import com.evision.dosage.exception.DosageException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * @author DingZhanYang
 * @date 2020/2/17 17:05
 */
@Slf4j
public class TokenUtils {
    /**
     * 签名秘钥(唯一秘钥，可以用密码做为秘钥)
     */
    public static final String SECRET = "admin";

    /**
     * 生成token
     *
     * @param userId 用户ID
     * @return token
     */
    public static String createJwtToken(String userId) {
        long ttlMillis = 36000000;//10个小时后过期
        return createJwtToken(userId, ttlMillis);
    }

    /**
     * 生成token
     *
     * @param userId    用户ID
     * @param ttlMillis 签发时间（有效时间，过期会报错）
     * @return token string
     */
    public static String createJwtToken(String userId, long ttlMillis) {
        //签名算法，将token进行签名
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成签发时间
        long nowMills = System.currentTimeMillis();
        Date now = new Date(nowMills);
        //通过秘钥签名JWT
        //创建token
        JwtBuilder builder = Jwts.builder().setId(userId)
                .setIssuedAt(now)
                .signWith(signatureAlgorithm, getKey());
        //添加过期时间
        if (ttlMillis >= 0) {
            long expMillis = nowMills + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        //列入计算时间
        String token = builder.compact();
        log.info("1sign=" + token);
        return token;
    }

    //验证和读取JWT的示例方法
    public static Claims parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                .parseClaimsJws(jwt).getBody();
    }

    public static Integer getUserId(HttpServletRequest request) {
        String token = getToken(request);
        Claims cl = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                .parseClaimsJws(token).getBody();
        return Integer.valueOf(cl.getId());
    }

    //刷新令牌
    public static String RefreshToken(String token) throws JsonProcessingException {
        //获取token的刷新时间
        String sign = token.split("\\.")[2];
        String refreshTime = "60000000";

        String new_token = null;
        Claims claims = parseJWT(token);
        if (String.valueOf(System.currentTimeMillis()).compareTo(refreshTime) < 0) {
            System.out.println("重新生成token");
            new_token = createJwtToken(claims.getId());
        } else {
            System.out.println("自动刷新时间已经过期");
        }
        return new_token;
    }

    //生成秘钥
    private static Key getKey() {
        //HS256加密
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //秘钥
        byte[] apiKeySecretByte = DatatypeConverter.parseBase64Binary(SECRET);
        return new SecretKeySpec(apiKeySecretByte, signatureAlgorithm.getJcaName());
    }

    public static String getToken(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        String cookieToken = "";
        if (cookies == null || cookies.length == 0) {
            throw new DosageException("请先登录");
        }
        for (Cookie cookie : cookies) {
            //遍历所有Cookie
            if (cookie.getName().equals("user")) {
                //找到对应的cookie
                //Cookie并不能根本意义上删除，只需要这样设置为0即可
                cookie.setMaxAge(0);
                cookieToken = cookie.getValue();
            }
        }
        return cookieToken;
    }
}
