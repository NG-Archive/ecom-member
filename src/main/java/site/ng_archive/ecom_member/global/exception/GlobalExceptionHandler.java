package site.ng_archive.ecom_member.global.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import site.ng_archive.ecom_member.global.error.ErrorMessageUtil;
import site.ng_archive.ecom_member.global.error.ErrorResponse;
import site.ng_archive.ecom_member.global.exception.EntityNotFoundException;
import site.ng_archive.ecom_member.global.exception.LoginFailException;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorMessageUtil errorMessageUtil;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public ErrorResponse handleWebExchangeBindException(WebExchangeBindException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        if (fieldErrors.isEmpty()) {
            String errorCode = "error.input.unknown";
            return errorMessageUtil.getErrorResult(errorCode, errorMessageUtil.getErrorMessage(errorCode));
        }
        FieldError error = fieldErrors.getFirst();

        String code = error.getDefaultMessage();
        String message = errorMessageUtil.getErrorMessage(code, error.getArguments());

        return errorMessageUtil.getErrorResult(code, message);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex) {
        return errorMessageUtil.getErrorResult(ex);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LoginFailException.class)
    public ErrorResponse handleLoginFailException(LoginFailException ex) {
        return errorMessageUtil.getErrorResult(ex);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse handleRuntimeException(RuntimeException ex) {
        log.error("handleRuntimeException: ", ex);
        String errorCode = "error.runtime";
        return new ErrorResponse(errorCode, errorMessageUtil.getErrorMessage(errorCode));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGeneralException(Exception ex) {
        log.error("handleGeneralException: ", ex);
        String errorCode = "error.internal.server";
        return new ErrorResponse(errorCode, errorMessageUtil.getErrorMessage(errorCode));
    }
}
