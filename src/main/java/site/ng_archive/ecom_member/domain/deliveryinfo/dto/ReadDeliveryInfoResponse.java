package site.ng_archive.ecom_member.domain.deliveryinfo.dto;

import jakarta.validation.constraints.NotNull;
import site.ng_archive.ecom_member.domain.deliveryinfo.DeliveryInfo;

public record ReadDeliveryInfoResponse(@NotNull Long id, @NotNull Long memberId, @NotNull String address) {
    public static ReadDeliveryInfoResponse from(DeliveryInfo entity) {
        return new ReadDeliveryInfoResponse(entity.id(), entity.memberId(), entity.address());
    }
}
