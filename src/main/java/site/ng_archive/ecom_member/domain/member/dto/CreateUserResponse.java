package site.ng_archive.ecom_member.domain.member.dto;

import site.ng_archive.ecom_member.domain.member.Member;

public record CreateUserResponse(
        Long id
) {
    public static CreateUserResponse from(Member member) {
        return new CreateUserResponse(member.id());
    }
}
