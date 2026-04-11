package site.ng_archive.ecom_member.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @Size(min = 1, max = 20, message = "member.username.size")
        @NotBlank(message = "member.username.blank")
        String name,

        @Size(min = 4, max = 20, message = "member.password.size")
        @NotBlank(message = "member.password.blank")
        String password
) {
    public CreateUserCommand toCommand() {
        return new CreateUserCommand(name, password);
    }
}
