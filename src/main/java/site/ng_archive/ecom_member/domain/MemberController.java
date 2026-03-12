package site.ng_archive.ecom_member.domain;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import site.ng_archive.ecom_member.global.PasswordManager;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/{id}")
    public Mono<ReadMemberResponse> readMember(@PathVariable Long id) {
        return memberService.readMember(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/member")
    public Mono<CreateMemberResponse> createMember(@Valid @RequestBody CreateMemberRequest request) {
        return memberService.createMember(request.toCommand());
    }

    @PostMapping("/login")
    public Mono<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return memberService.login(request.toCommand());
    }
}
