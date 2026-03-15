package site.ng_archive.ecom_member.global.auth;

import site.ng_archive.ecom_member.domain.member.Member;
import site.ng_archive.ecom_member.domain.member.MemberRole;

public record UserContext(Long id, MemberRole role) {
    public static UserContext from(Member entity) {
        return new UserContext(entity.id(), entity.role());
    }
    public static UserContext of(Long id, MemberRole role) {
        return new UserContext(id, role);
    }
}
