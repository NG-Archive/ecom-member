package site.ng_archive.ecom_member.global.exception;

import lombok.Getter;

public class LoginFailException extends RuntimeException {
    @Getter
    private String code;

    public LoginFailException(String code) {
        super(code);
        this.code = code;
    }
}
