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
import site.ng_archive.ecom_member.domain.member.dto.CreateMemberRequest;
import site.ng_archive.ecom_member.domain.member.dto.CreateMemberResponse;
import site.ng_archive.ecom_member.domain.member.dto.LoginRequest;
import site.ng_archive.ecom_member.domain.member.dto.LoginResponse;
import site.ng_archive.ecom_member.domain.member.dto.ReadMemberResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/{id}")
    public Mono<ReadMemberResponse> readMember(@PathVariable Long id) {
        return memberService.readMember(id)
            .map(ReadMemberResponse::from);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/member")
    public Mono<CreateMemberResponse> createMember(@Valid @RequestBody CreateMemberRequest request) {
        return memberService.createMember(request.toCommand())
            .map(CreateMemberResponse::from);
    }

    @PostMapping("/login")
    public Mono<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return memberService.login(request.toCommand())
            .map(LoginResponse::new);
    }
}
