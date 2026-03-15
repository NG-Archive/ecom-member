package site.ng_archive.ecom_member.global.auth.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;
import site.ng_archive.ecom_member.global.auth.UserContext;
import site.ng_archive.ecom_member.global.auth.exception.AccessDeniedException;
import site.ng_archive.ecom_member.global.auth.exception.ForbiddenException;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class AuthAspect {

    @Around("@annotation(requireRoles)")
    public Object checkAuth(ProceedingJoinPoint joinPoint, RequireRoles requireRoles) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> returnType = signature.getReturnType();

        // 1. Mono 타입인 경우
        if (Mono.class.isAssignableFrom(returnType)) {
            return Mono.deferContextual(ctx -> {
                // 권한 체크 먼저 실행
                if (!validate(ctx, requireRoles)) {
                    return Mono.error(new ForbiddenException("auth.forbidden"));
                }
                try {
                    // 비즈니스 로직(proceed) 실행
                    return (Mono<?>) joinPoint.proceed();
                } catch (Throwable e) {
                    return Mono.error(new RuntimeException(e));
                }
            });
        }

        // 2. Flux 타입인 경우
        if (Flux.class.isAssignableFrom(returnType)) {
            return Flux.deferContextual(ctx -> {
                if (!validate(ctx, requireRoles)) {
                    return Flux.error(new ForbiddenException("auth.forbidden"));
                }
                try {
                    return (Flux<?>) joinPoint.proceed();
                } catch (Throwable e) {
                    return Flux.error(new RuntimeException(e));
                }
            });
        }

        // Mono/Flux가 아닌 일반 타입은 WebFlux 환경에서 권장되지 않으나 필요한 경우 처리
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validate(ContextView ctx, RequireRoles requireRoles) {
        // 인증 정보 존재 여부 확인
        if (!ctx.hasKey("userContext")) {
            throw new AccessDeniedException("auth.unauthorized");
        }

        UserContext user = ctx.get("userContext");
        String[] requiredRoles = requireRoles.roles();

        // 요구하는 권한이 없으면 role만 가지고 있으면 통과
        if (requiredRoles == null || requiredRoles.length == 0) {
            return Objects.nonNull(user.role());
        }

        // 유저의 권한 중 하나라도 일치하는지 확인
        return Arrays.stream(requiredRoles)
            .anyMatch(role -> user.role().name().equalsIgnoreCase(role));
    }
}
