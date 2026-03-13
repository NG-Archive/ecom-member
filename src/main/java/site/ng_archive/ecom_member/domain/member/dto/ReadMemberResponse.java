package site.ng_archive.ecom_member.domain.member.dto;

import site.ng_archive.ecom_member.domain.member.Member;

public record ReadMemberResponse(Long id, String name) {
    public static ReadMemberResponse from(Member member) {
        return new ReadMemberResponse(member.id(), member.name());
    }
}
