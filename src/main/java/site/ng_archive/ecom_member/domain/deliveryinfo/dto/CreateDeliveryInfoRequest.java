package site.ng_archive.ecom_member.domain.deliveryinfo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDeliveryInfoRequest(

    Long memberId,

    @NotBlank(message="delivery-info.address.blank")
    @Size(max = 255, message = "delivery-info.address.size")
    String address

) {
    public CreateDeliveryInfoCommand toCommand() {
        return new CreateDeliveryInfoCommand(memberId, address);
    }
}
