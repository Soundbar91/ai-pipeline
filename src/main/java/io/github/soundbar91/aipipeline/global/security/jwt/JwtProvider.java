package io.github.soundbar91.aipipeline.global.security.jwt;

import io.github.soundbar91.aipipeline.global.exception.BusinessException;
import io.github.soundbar91.aipipeline.global.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private static final String SCOPE_CLAIM = "scope";
    private static final String SCOPE_SIGNUP = "SIGNUP";

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final long signupTokenExpiration;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration,
            @Value("${jwt.signup-token-expiration}") long signupTokenExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.signupTokenExpiration = signupTokenExpiration;
    }

    public String createAccessToken(Long userId) {
        return buildToken(String.valueOf(userId), accessTokenExpiration * 1000, null);
    }

    public String createRefreshToken(Long userId) {
        return buildToken(String.valueOf(userId), refreshTokenExpiration * 1000, null);
    }

    public String createSignupToken(String socialId, String provider) {
        return buildToken(socialId + ":" + provider, signupTokenExpiration * 1000, SCOPE_SIGNUP);
    }

    private String buildToken(String subject, long expirationMillis, String scope) {
        JwtBuilder builder = Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey);
        if (scope != null) {
            builder.claim(SCOPE_CLAIM, scope);
        }
        return builder.compact();
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        } catch (JwtException e) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
    }

    public Long getUserId(String token) {
        Claims claims = parseToken(token);
        requireNotSignupScope(claims);
        return Long.parseLong(claims.getSubject());
    }

    public String getSignupSubject(String token) {
        Claims claims = parseToken(token);
        requireSignupScope(claims);
        return claims.getSubject();
    }

    public boolean isSignupToken(String token) {
        try {
            Claims claims = parseToken(token);
            return SCOPE_SIGNUP.equals(claims.get(SCOPE_CLAIM, String.class));
        } catch (BusinessException e) {
            return false;
        }
    }

    private void requireSignupScope(Claims claims) {
        if (!SCOPE_SIGNUP.equals(claims.get(SCOPE_CLAIM, String.class))) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_SCOPE);
        }
    }

    private void requireNotSignupScope(Claims claims) {
        if (SCOPE_SIGNUP.equals(claims.get(SCOPE_CLAIM, String.class))) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_SCOPE);
        }
    }
}
