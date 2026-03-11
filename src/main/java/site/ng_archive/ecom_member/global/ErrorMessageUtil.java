package site.ng_archive.ecom_member.global;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ErrorMessageUtil {

    private final MessageSource ms;
    private static final String EXCEPTION_ERROR_CODE = "error";

    public String getErrorCode(Exception e) {
        String errorCode = e.getMessage();
        try {
            ms.getMessage(errorCode, null, Locale.KOREA);
        } catch (Exception ex) {
            return EXCEPTION_ERROR_CODE;
        }
        return errorCode;
    }

    public String getErrorMessage(String errorCode) {
        try {
            return ms.getMessage(errorCode, null, Locale.KOREA);
        } catch (Exception ex) {
            return ms.getMessage(EXCEPTION_ERROR_CODE, null, Locale.KOREA);
        }
    }

    public String getErrorMessage(String errorCode, Object[] args) {
        Object[] rawArgs = args;
        Object[] copiedArgs = new Object[0];

        if (rawArgs != null && rawArgs.length > 1) {
            copiedArgs = Arrays.copyOfRange(rawArgs, 1, rawArgs.length);
            List<Object> list = Arrays.asList(args);
            Collections.reverse(list);
            copiedArgs = list.toArray();
        }
        try {
            return ms.getMessage(errorCode, copiedArgs, Locale.KOREA);
        } catch (Exception ex) {
            return ms.getMessage(EXCEPTION_ERROR_CODE, null, Locale.KOREA);
        }
    }

    public ErrorResponse getErrorResult(String errorCode, String message) {
        return new ErrorResponse(errorCode, message);
    }

}
