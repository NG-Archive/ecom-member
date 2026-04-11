package site.ng_archive.ecom_member.domain.member.dto;

import site.ng_archive.ecom_common.auth.Role;
import site.ng_archive.ecom_member.domain.member.Member;

public record CreateSellerCommand(String name, String password) {
    public Member toEntity(String encryptPassword) {
        return Member.of(name, encryptPassword, Role.SELLER);
    }
}
