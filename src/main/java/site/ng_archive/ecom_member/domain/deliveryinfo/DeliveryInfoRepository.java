package site.ng_archive.ecom_member.domain.deliveryinfo;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface DeliveryInfoRepository extends R2dbcRepository<DeliveryInfo, Long> {
    Flux<DeliveryInfo> findAllByMemberId(Long memberId);
}
