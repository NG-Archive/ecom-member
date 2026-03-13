package site.ng_archive.ecom_member.domain.deliveryinfo.dto;

import jakarta.validation.constraints.NotNull;
import site.ng_archive.ecom_member.domain.deliveryinfo.DeliveryInfo;

public record CreateDeliveryInfoResponse(@NotNull Long id) {
    public static CreateDeliveryInfoResponse from(DeliveryInfo entity) {
        return new CreateDeliveryInfoResponse(entity.id());
    }
}
