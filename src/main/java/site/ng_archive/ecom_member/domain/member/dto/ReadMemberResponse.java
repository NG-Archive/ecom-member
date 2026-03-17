package site.ng_archive.ecom_member.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import site.ng_archive.ecom_member.domain.member.Member;
import site.ng_archive.ecom_member.domain.member.MemberStatus;

public record ReadMemberResponse(
    @NotNull Long id,
    @NotBlank String name,
    @NotNull MemberStatus status
) {
    public static ReadMemberResponse from(Member member) {
        return new ReadMemberResponse(member.id(), member.name(), member.status());
    }
}
