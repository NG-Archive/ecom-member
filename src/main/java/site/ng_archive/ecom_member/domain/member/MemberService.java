package site.ng_archive.ecom_member.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import site.ng_archive.ecom_common.auth.PasswordManager;
import site.ng_archive.ecom_common.auth.UserContext;
import site.ng_archive.ecom_common.auth.exception.LoginFailException;
import site.ng_archive.ecom_common.auth.token.TokenUtil;
import site.ng_archive.ecom_common.handler.EntityNotFoundException;
import site.ng_archive.ecom_member.domain.member.dto.CreateMemberCommand;
import site.ng_archive.ecom_member.domain.member.dto.LoginCommand;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Mono<Member> createMember(CreateMemberCommand command) {
        String encryptedPw = PasswordManager.encrypt(command.password());
        return memberRepository.save(command.toEntity(encryptedPw));
    }

    public Mono<Member> readMember(Long id) {
        return memberRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new EntityNotFoundException("member.notfound"))));

    }

    public Mono<String> login(LoginCommand command) {
        return memberRepository.findById(command.id())
                .filter(member -> PasswordManager.check(command.password(), member.password()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new LoginFailException("member.login.fail"))))
                .flatMap(member -> {
                    String token = TokenUtil.getSign(UserContext.of(member.id(), member.role().name()));
                    return Mono.just(token);
                });
    }
}
