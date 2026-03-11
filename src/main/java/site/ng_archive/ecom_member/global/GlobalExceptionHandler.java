package site.ng_archive.ecom_member.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public ErrorResponse handleWebExchangeBindException(WebExchangeBindException ex) {
        FieldError error = ex.getBindingResult().getFieldErrors().getFirst();
        String code = "input.error." + error.getField();
        String message = error.getDefaultMessage();

        return new ErrorResponse(code, message);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse handleRuntimeException(RuntimeException ex) {
        log.error("handleRuntimeException: ", ex);
        return new ErrorResponse("runtime.error", "실행 중 오류가 발생했습니다.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGeneralException(Exception ex) {
        log.error("handleGeneralException: ", ex);
        return new ErrorResponse(
                "internal.server.error",
                "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
        );
    }
}
