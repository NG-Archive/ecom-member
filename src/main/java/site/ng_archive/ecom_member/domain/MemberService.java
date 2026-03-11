package site.ng_archive.ecom_member.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import site.ng_archive.ecom_member.global.PasswordManager;

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
}
