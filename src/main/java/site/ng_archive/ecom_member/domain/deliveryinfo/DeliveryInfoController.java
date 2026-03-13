package site.ng_archive.ecom_member.domain.deliveryinfo;

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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.ng_archive.ecom_member.domain.deliveryinfo.dto.CreateDeliveryInfoRequest;
import site.ng_archive.ecom_member.domain.deliveryinfo.dto.CreateDeliveryInfoResponse;
import site.ng_archive.ecom_member.domain.deliveryinfo.dto.ReadDeliveryInfoResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeliveryInfoController {

    private final DeliveryInfoService deliveryInfoService;

    @GetMapping("/{memberId}/delivery-info/{id}")
    public Mono<ReadDeliveryInfoResponse> readDeliveryInfoDetail(@PathVariable Long memberId, @PathVariable Long id) {
        return deliveryInfoService.readDeliveryInfoDetail(memberId, id)
            .map(ReadDeliveryInfoResponse::from);
    }

    @GetMapping("/{memberId}/delivery-info")
    public Flux<ReadDeliveryInfoResponse> readDeliveryInfo(@PathVariable Long memberId) {
        return deliveryInfoService.readDeliveryInfo(memberId)
            .map(ReadDeliveryInfoResponse::from);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/delivery-info")
    public Mono<CreateDeliveryInfoResponse> createDeliveryInfo(@Valid @RequestBody CreateDeliveryInfoRequest request) {
        return deliveryInfoService.createDeliveryInfo(request.toCommand())
            .map(CreateDeliveryInfoResponse::from);
    }
}
