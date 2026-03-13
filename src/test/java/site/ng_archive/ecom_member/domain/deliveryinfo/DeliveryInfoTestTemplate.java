package site.ng_archive.ecom_member.domain.deliveryinfo;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.ng_archive.ecom_member.domain.deliveryinfo.dto.CreateDeliveryInfoCommand;

@Component
public class DeliveryInfoTestTemplate {

    @Autowired
    private Faker faker;

    @Autowired
    private DeliveryInfoService deliveryInfoService;

    public String randomAddress() {
        return faker.regexify("[가-힣]{10,20}");
    }

    public DeliveryInfo createDeliveryInfo(Long memberId, String address) {
        return deliveryInfoService.createDeliveryInfo(new CreateDeliveryInfoCommand(memberId, address))
            .block();
    }
}
