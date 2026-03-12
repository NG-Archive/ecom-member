package site.ng_archive.ecom_member.global;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public final class TokenUtil {

    private static final String JWT_NAME = "jwt";
    private static final String CLAIM_MEMBER_ID = "memberId";
    private static final String CLAIM_MEMBER_NAME = "name";
    private static final Long DEFAULT_EXPIRED_MINUTE = 30L;

    public static String getSign(PrincipalDetails principalDetails) {
        return JWT.create()
                .withSubject(JWT_NAME)
                .withExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(DEFAULT_EXPIRED_MINUTE)))
                .withClaim(CLAIM_MEMBER_ID, principalDetails.id())
                .withClaim(CLAIM_MEMBER_NAME, principalDetails.name())
                .sign(Algorithm.HMAC512(TokenSecretKey.getJwtSecret()));
    }

    public static Long verify(String token) {
        return JWT.require(Algorithm.HMAC512(TokenSecretKey.getJwtSecret())).build()
            .verify(token)
            .getClaim(CLAIM_MEMBER_ID).asLong();
    }
}
