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
import site.ng_archive.ecom_member.global.auth.UserContext;
import site.ng_archive.ecom_member.global.auth.aspect.LoginUser;
import site.ng_archive.ecom_member.global.auth.aspect.RequireRoles;
import site.ng_archive.ecom_member.global.auth.exception.ForbiddenException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeliveryInfoController {

    private final DeliveryInfoService deliveryInfoService;

    @RequireRoles
    @GetMapping("/{memberId}/delivery-info/{id}")
    public Mono<ReadDeliveryInfoResponse> readDeliveryInfoDetail(
        @LoginUser UserContext user,
        @PathVariable Long memberId,
        @PathVariable Long id) {
        return Mono.just(user)
            .filter(u -> u.id().equals(memberId))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new ForbiddenException("auth.forbidden"))))
            .flatMap(u -> deliveryInfoService.readDeliveryInfoDetail(u.id(), id))
            .map(ReadDeliveryInfoResponse::from);
    }

    @RequireRoles
    @GetMapping("/{memberId}/delivery-info")
    public Flux<ReadDeliveryInfoResponse> readDeliveryInfo(
        @LoginUser UserContext user,
        @PathVariable Long memberId) {
        return Mono.just(user)
            .filter(u -> u.id().equals(memberId))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new ForbiddenException("auth.forbidden"))))
            .flatMapMany(u -> deliveryInfoService.readDeliveryInfo(u.id()))
            .map(ReadDeliveryInfoResponse::from);
    }

    @RequireRoles
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/delivery-info")
    public Mono<CreateDeliveryInfoResponse> createDeliveryInfo(
        @LoginUser UserContext user,
        @Valid @RequestBody CreateDeliveryInfoRequest request) {
        return Mono.just(user)
            .filter(u -> u.id().equals(request.memberId()))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new ForbiddenException("auth.forbidden"))))
            .flatMap(u -> deliveryInfoService.createDeliveryInfo(request.toCommand()))
            .map(CreateDeliveryInfoResponse::from);
    }
}
