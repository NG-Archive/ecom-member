package site.ng_archive.ecom_member.domain.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import site.ng_archive.ecom_common.auth.UserContext;
import site.ng_archive.ecom_common.auth.aspect.LoginUser;
import site.ng_archive.ecom_common.auth.aspect.RequireRoles;
import site.ng_archive.ecom_common.auth.exception.ForbiddenException;
import site.ng_archive.ecom_member.domain.member.dto.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @RequireRoles
    @GetMapping("/member/{id}")
    public Mono<ReadMemberResponse> readMember(@LoginUser UserContext user, @PathVariable Long id) {
        return Mono.just(user)
            .filter(u -> u.id().equals(id))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new ForbiddenException("auth.forbidden"))))
            .flatMap(u -> memberService.readMember(u.id()))
            .map(ReadMemberResponse::from);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/member/user")
    public Mono<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return memberService.createUser(request.toCommand())
            .map(CreateUserResponse::from);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/member/seller")
    public Mono<CreateSellerResponse> createSeller(@Valid @RequestBody CreateSellerRequest request) {
        return memberService.createSeller(request.toCommand())
            .map(CreateSellerResponse::from);
    }

    @PostMapping("/login")
    public Mono<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return memberService.login(request.toCommand())
            .map(LoginResponse::new);
    }
}
