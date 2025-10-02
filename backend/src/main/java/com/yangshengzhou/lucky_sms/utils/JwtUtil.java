package com.yangshengzhou.lucky_sms.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类，用于生成、解析 Token
 */
@Component // 注册为 Spring 组件，便于注入配置
public class JwtUtil {

    // 从配置文件读取密钥（避免硬编码）
    @Value("${jwt.secret}")
    private String secret;

    // 从配置文件读取过期时间（单位：毫秒，如 86400000 表示 24 小时）
    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * 生成 JWT Token（携带用户 ID）
     * @param userId 用户 ID
     * @return Token 字符串
     */
    public String generateToken(int userId) {
        // 构建自定义声明（Payload）
        Map<String, Object> claims = new HashMap<>(1);
        claims.put("userId", userId);

        // 生成 Token
        return Jwts.builder()
                .setClaims(claims) // 设置自定义信息
                .setIssuedAt(new Date()) // 签发时间（可选）
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 过期时间
                .signWith(getSecretKey(), SignatureAlgorithm.HS256) // 签名（使用密钥和算法）
                .compact();
    }

    /**
     * 解析 Token，获取用户 ID
     * @param token Token 字符串
     * @return 用户 ID
     * @throws JwtException 如果 Token 无效、过期等
     */
    public int getUserIdFromToken(String token) throws JwtException {
        Claims claims = parseTokenClaims(token);
        return claims.get("userId", int.class);
    }

    /**
     * 解析 Token 获取所有声明（内部使用）
     */
    private Claims parseTokenClaims(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey()) // 验证签名的密钥
                .build()
                .parseClaimsJws(token) // 解析 Token（会自动验证签名和过期时间）
                .getBody();
    }

    /**
     * 生成密钥（基于配置的 secret 字符串）
     * 注意：secret 长度需 >= 32 位（HS256 算法要求）
     */
    private SecretKey getSecretKey() {
        // 如果 secret 长度不足，Keys.hmacShaKeyFor 会自动处理，但建议提前确保足够长
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 验证 Token 是否有效（可单独调用，如需要提前校验）
     * @param token Token 字符串
     * @return true=有效，false=无效
     */
    public boolean validateToken(String token) {
        try {
            parseTokenClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 捕获所有 JWT 相关异常（过期、签名错误等）
            return false;
        }
    }
}