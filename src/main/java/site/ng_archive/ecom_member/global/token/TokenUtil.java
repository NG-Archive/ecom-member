package site.ng_archive.ecom_member.global.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import site.ng_archive.ecom_member.global.exception.TokenInvalidException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public final class TokenUtil {

    private static final String JWT_NAME = "jwt";
    private static final String CLAIM_MEMBER_ID = "memberId";
    private static final Long DEFAULT_EXPIRED_MINUTE = 30L;

    public static String getSign(PrincipalDetails principalDetails) {
        return JWT.create()
                .withSubject(JWT_NAME)
                .withExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(DEFAULT_EXPIRED_MINUTE)))
                .withClaim(CLAIM_MEMBER_ID, principalDetails.id())
                .sign(Algorithm.HMAC512(TokenSecretKey.getJwtSecret()));
    }

    public static Long verify(String token) {
        Long memberId = JWT.require(Algorithm.HMAC512(TokenSecretKey.getJwtSecret()))
            .withSubject(JWT_NAME)
            .build()
            .verify(token)
            .getClaim(CLAIM_MEMBER_ID).asLong();

        Objects.requireNonNull(memberId, () -> {
            throw new TokenInvalidException("token.invalid");
        });

        return memberId;
    }
}
