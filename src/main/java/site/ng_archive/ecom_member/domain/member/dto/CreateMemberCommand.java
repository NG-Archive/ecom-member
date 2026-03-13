package site.ng_archive.ecom_member.domain.member.dto;

import site.ng_archive.ecom_member.domain.member.Member;

public record CreateMemberCommand(String name, String password) {
    public Member toMember(String encryptPassword) {
        return new Member(null, name, encryptPassword);
    }
}
