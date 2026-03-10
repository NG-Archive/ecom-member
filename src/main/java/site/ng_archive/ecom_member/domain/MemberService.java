package site.ng_archive.ecom_member.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Mono<CreateMemberResponse> createMember(Member member) {
        return memberRepository.save(member)
                .map(CreateMemberResponse::from);
    }
}
