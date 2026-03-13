package site.ng_archive.ecom_member.domain.member.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginResponse(@NotBlank String token) {
}
