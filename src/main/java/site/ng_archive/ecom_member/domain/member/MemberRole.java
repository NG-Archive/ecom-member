package site.ng_archive.ecom_member.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    USER(ROLES.USER){
        @Override
        public String toString() {
            return ROLES.USER;
        }
    },
    ADMIN(ROLES.ADMIN){
        @Override
        public String toString() {
            return ROLES.ADMIN;
        }
    };

    private final String role;

    public static class ROLES {
        public static final String USER = "USER";
        public static final String ADMIN = "ADMIN";

    }
}
