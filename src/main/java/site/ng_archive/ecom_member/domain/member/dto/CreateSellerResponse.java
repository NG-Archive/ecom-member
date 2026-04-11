package site.ng_archive.ecom_member.domain.member.dto;

import site.ng_archive.ecom_member.domain.member.Member;

public record CreateSellerResponse(
        Long id
) {
    public static CreateSellerResponse from(Member member) {
        return new CreateSellerResponse(member.id());
    }
}
