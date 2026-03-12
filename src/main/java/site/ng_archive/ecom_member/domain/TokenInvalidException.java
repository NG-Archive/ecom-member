package site.ng_archive.ecom_member.domain;

import lombok.Getter;

public class TokenInvalidException extends RuntimeException {
    @Getter
    private String code;

    public TokenInvalidException(String code) {
        super(code);
        this.code = code;
    }
}
