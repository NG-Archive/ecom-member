package site.ng_archive.ecom_member.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import site.ng_archive.ecom_member.domain.member.dto.CreateMemberCommand;
import site.ng_archive.ecom_member.domain.member.dto.CreateMemberResponse;
import site.ng_archive.ecom_member.domain.member.dto.LoginCommand;
import site.ng_archive.ecom_member.domain.member.dto.LoginResponse;
import site.ng_archive.ecom_member.domain.member.dto.ReadMemberResponse;
import site.ng_archive.ecom_member.global.exception.EntityNotFoundException;
import site.ng_archive.ecom_member.global.exception.LoginFailException;
import site.ng_archive.ecom_member.global.token.PasswordManager;
import site.ng_archive.ecom_member.global.token.TokenUtil;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Mono<CreateMemberResponse> createMember(CreateMemberCommand command) {
        String encryptedPw = PasswordManager.encrypt(command.password());
        return memberRepository.save(command.toMember(encryptedPw))
                .map(CreateMemberResponse::from);
    }

    public Mono<ReadMemberResponse> readMember(Long id) {
        return memberRepository.findById(id)
                .map(ReadMemberResponse::from)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new EntityNotFoundException("member.notfound"))));

    }

    public Mono<LoginResponse> login(LoginCommand command) {
        return memberRepository.findById(command.id())
                .filter(member -> PasswordManager.check(command.password(), member.password()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new LoginFailException("member.login.fail"))))
                .flatMap(member -> {
                    String token = TokenUtil.getSign(member.toPrincipalDetails());
                    return Mono.just(new LoginResponse(token));
                });
    }
}
