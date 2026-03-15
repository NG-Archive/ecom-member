package site.ng_archive.ecom_member.domain.deliveryinfo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.ng_archive.ecom_member.domain.deliveryinfo.dto.CreateDeliveryInfoCommand;
import site.ng_archive.ecom_member.domain.member.MemberRepository;
import site.ng_archive.ecom_member.global.exception.EntityNotFoundException;

@RequiredArgsConstructor
@Service
public class DeliveryInfoService {

    private final DeliveryInfoRepository deliveryInfoRepository;
    private final MemberRepository memberRepository;

    public Mono<DeliveryInfo> createDeliveryInfo(CreateDeliveryInfoCommand command) {
        return memberRepository.existsById(command.memberId())
            .filter(exists -> exists)
            .switchIfEmpty(Mono.defer(() -> Mono.error(new EntityNotFoundException("member.notfound"))))
            .then(deliveryInfoRepository.save(command.toEntity()));
    }

    public Mono<DeliveryInfo> readDeliveryInfoDetail(Long memberId, Long id) {
        return memberRepository.existsById(memberId)
            .filter(exists -> exists)
            .switchIfEmpty(Mono.defer(() -> Mono.error(new EntityNotFoundException("member.notfound"))))
            .then(deliveryInfoRepository.findByIdAndMemberId(id, memberId))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new EntityNotFoundException("delivery-info.notfound"))));
    }

    public Flux<DeliveryInfo> readDeliveryInfo(Long memberId) {
        return memberRepository.existsById(memberId)
            .filter(exists -> exists)
            .switchIfEmpty(Mono.defer(() -> Mono.error(new EntityNotFoundException("member.notfound"))))
            .thenMany(deliveryInfoRepository.findAllByMemberId(memberId));
    }
}
