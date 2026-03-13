package site.ng_archive.ecom_member.domain.deliveryinfo.dto;

import site.ng_archive.ecom_member.domain.deliveryinfo.DeliveryInfo;

public record CreateDeliveryInfoCommand(Long memberId, String address) {
    public DeliveryInfo toEntity() {
        return new DeliveryInfo(null, memberId, address);
    }
}
