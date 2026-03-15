package site.ng_archive.ecom_member.domain.member;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public record Member(
        @Id
        Long id,

        String name,

        String password,

        MemberRole role
){
    public static Member of(String name, String encryptedPassword) {
        return new Member(null, name, encryptedPassword, MemberRole.USER);
    }
}
