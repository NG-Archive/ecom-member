package site.ng_archive.ecom_member.domain.deliveryinfo;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public record DeliveryInfo(

    @Id
    Long id,

    Long memberId,

    String address

) {
}
