package site.ng_archive.ecom_member.domain.member.dto;

public record LoginRequest(String name, String password) {
    public LoginCommand toCommand() {
        return new LoginCommand(name, password);
    }
}
