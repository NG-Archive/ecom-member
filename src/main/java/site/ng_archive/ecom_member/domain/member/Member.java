package site.ng_archive.ecom_member.domain.member;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import site.ng_archive.ecom_common.auth.Role;

@Table
public record Member(
        @Id
        Long id,

        String name,

        String password,

        Role role,

        MemberStatus status
){
    public static Member of(String name, String encryptedPassword, Role role) {
        return new Member(null, name, encryptedPassword, role, MemberStatus.NORMAL);
    }
}
