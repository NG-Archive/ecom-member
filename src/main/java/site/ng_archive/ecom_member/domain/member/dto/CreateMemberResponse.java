package site.ng_archive.ecom_member.domain.member.dto;

import site.ng_archive.ecom_member.domain.member.Member;

public record CreateMemberResponse(
        Long id
) {
    public static CreateMemberResponse from(Member member) {
        return new CreateMemberResponse(member.id());
    }
}
