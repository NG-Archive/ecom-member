package site.ng_archive.ecom_member.domain.member.dto;

public record LoginRequest(Long id, String password) {
    public LoginCommand toCommand() {
        return new LoginCommand(id, password);
    }
}
