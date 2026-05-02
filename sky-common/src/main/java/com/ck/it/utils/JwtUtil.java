package com.ck.it.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class JwtUtil {
    /**
     * 生成jwt
     * 使用HS256算法，私钥使用固定秘钥
     *
     * @param secretKey jwt秘钥
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    设置的信息
     * @return
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiration = new Date(nowMillis + ttlMillis);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(buildKey(secretKey), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 解析jwt
     *
     * @param secretKey jwt秘钥
     * @param token     jwt令牌
     * @return
     */
    public static Claims parseJWT(String secretKey, String token) {
        return Jwts.parser()
                .verifyWith(buildKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private static SecretKey buildKey(String secretKey) {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalArgumentException("JWT secretKey不能为空");
        }
        try {
            byte[] keyBytes = MessageDigest.getInstance("SHA-256")
                    .digest(secretKey.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(keyBytes, "HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("当前环境不支持SHA-256算法", e);
        }
    }
}
