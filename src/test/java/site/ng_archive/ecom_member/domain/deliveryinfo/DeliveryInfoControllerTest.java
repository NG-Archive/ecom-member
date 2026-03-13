package site.ng_archive.ecom_member.domain.deliveryinfo;

import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import site.ng_archive.ecom_member.config.AcceptedTest;
import site.ng_archive.ecom_member.domain.deliveryinfo.dto.CreateDeliveryInfoRequest;
import site.ng_archive.ecom_member.domain.deliveryinfo.dto.CreateDeliveryInfoResponse;
import site.ng_archive.ecom_member.domain.deliveryinfo.dto.ReadDeliveryInfoResponse;
import site.ng_archive.ecom_member.domain.member.MemberTestTemplate;

import java.util.List;

import static io.restassured.module.webtestclient.RestAssuredWebTestClient.given;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;

class DeliveryInfoControllerTest extends AcceptedTest {

    @Autowired
    private MemberTestTemplate memberTestTemplate;

    @Autowired
    private DeliveryInfoTestTemplate deliveryInfoTestTemplate;

    @Autowired
    private DeliveryInfoRepository deliveryInfoRepository;

    @Test
    void 배송정보생성() {

        Long memberId = memberTestTemplate.createMember().id();
        String address = deliveryInfoTestTemplate.randomAddress();
        CreateDeliveryInfoRequest request = new CreateDeliveryInfoRequest(memberId, address);

        CreateDeliveryInfoResponse response =
            given()
                .contentType(ContentType.JSON)
                .body(request)
                .consumeWith(document(
                    info()
                        .tag("DeliveryInfo")
                        .summary("배송정보 생성")
                        .description("회원 ID와 주소로 배송 정보를 저장 합니다.")
                        .requestFields(
                            field(CreateDeliveryInfoRequest.class, "memberId", "회원 아이디"),
                            field(CreateDeliveryInfoRequest.class, "address", "주소")
                        )
                        .requestSchema(Schema.schema("request"))
                        .responseFields(
                            field(CreateDeliveryInfoResponse.class, "id", "배송정보 ID")
                        )
                        .responseSchema(Schema.schema("response"))
                ))
                .post("/delivery-info")
                .then()
                .status(HttpStatus.CREATED)
                .log().all()
                .extract().body().as(CreateDeliveryInfoResponse.class);

        DeliveryInfo saved = deliveryInfoRepository.findById(response.id()).block();
        Assertions.assertThat(saved.memberId()).isEqualTo(request.memberId());
        Assertions.assertThat(saved.address()).isEqualTo(request.address());
    }

    @Test
    void 배송정보단건조회() {
        Long memberId = memberTestTemplate.createMember().id();
        String address = deliveryInfoTestTemplate.randomAddress();
        DeliveryInfo exists = deliveryInfoTestTemplate.createDeliveryInfo(memberId, address);

        ReadDeliveryInfoResponse response =
        given()
            .contentType(ContentType.JSON)
            .pathParam("memberId", memberId)
            .pathParam("id", exists.id())
            .consumeWith(document(
                info()
                    .tag("DeliveryInfo")
                    .summary("배송정보 생성")
                    .description("회원 ID와 주소로 배송 정보를 저장 합니다.")
                    .pathParameters(
                        parameterWithName("memberId").description("회원 ID").type(SimpleType.NUMBER),
                        parameterWithName("id").description("배송정보 ID").type(SimpleType.NUMBER)
                    )
                    .responseFields(
                        field(ReadDeliveryInfoResponse.class, "id", "배송정보 ID"),
                        field(ReadDeliveryInfoResponse.class, "memberId", "회원 ID"),
                        field(ReadDeliveryInfoResponse.class, "address", "배송지 주소")
                    )
                    .responseSchema(Schema.schema("response"))
            ))
            .get("/{memberId}/delivery-info/{id}")
            .then()
            .status(HttpStatus.OK)
            .log().all()
            .extract().body().as(ReadDeliveryInfoResponse.class);

        Assertions.assertThat(response.id()).isEqualTo(exists.id());
        Assertions.assertThat(response.memberId()).isEqualTo(exists.memberId());
        Assertions.assertThat(response.address()).isEqualTo(exists.address());
    }

    @Test
    void 배송정보목록조회() {
        Long memberId = memberTestTemplate.createMember().id();
        deliveryInfoTestTemplate.createDeliveryInfo(memberId, deliveryInfoTestTemplate.randomAddress());
        deliveryInfoTestTemplate.createDeliveryInfo(memberId, deliveryInfoTestTemplate.randomAddress());
        deliveryInfoTestTemplate.createDeliveryInfo(memberId, deliveryInfoTestTemplate.randomAddress());

        List<ReadDeliveryInfoResponse> responses =
            given()
                .contentType(ContentType.JSON)
                .pathParam("memberId", memberId)
                .consumeWith(document(
                    info()
                        .tag("DeliveryInfo")
                        .summary("배송정보 생성")
                        .description("회원 ID와 주소로 배송 정보를 저장 합니다.")
                        .pathParameters(
                            parameterWithName("memberId").description("회원 ID").type(SimpleType.NUMBER)
                        )
                        .responseFields(
                            field(List.class, "[]", "배송정보 배열"),
                            field(ReadDeliveryInfoResponse.class, "[].id", "배송정보 ID"),
                            field(ReadDeliveryInfoResponse.class, "[].memberId", "회원 ID"),
                            field(ReadDeliveryInfoResponse.class, "[].address", "배송지 주소")
                        )
                        .responseSchema(Schema.schema("response"))
                ))
                .get("/{memberId}/delivery-info")
                .then()
                .status(HttpStatus.OK)
                .log().all()
                .extract().body().as(new TypeRef<List<ReadDeliveryInfoResponse>>() {});

        ReadDeliveryInfoResponse first = responses.getFirst();
        Assertions.assertThat(responses.size()).isEqualTo(3);
        Assertions.assertThat(first.memberId()).isEqualTo(memberId);
    }

}