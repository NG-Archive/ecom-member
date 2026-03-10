package site.ng_archive.ecom_member.domain;

public record CreateMemberResponse(
        Long id
) {
    public static CreateMemberResponse from(Member member) {
        return new CreateMemberResponse(member.id());
    }
}
