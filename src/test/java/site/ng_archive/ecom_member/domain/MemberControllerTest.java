package site.ng_archive.ecom_member.domain;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import site.ng_archive.ecom_member.AcceptedTest;

import static io.restassured.module.webtestclient.RestAssuredWebTestClient.given;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

class MemberControllerTest extends AcceptedTest {

    @Test
    void readMember() {
        given()
            .contentType(ContentType.JSON)
            .consumeWith(document(
                pathParameters(
                    parameterWithName("id").description("회원 아이디")
                )
            ))
            .get("member/{id}", "1")
        .then()
            .statusCode(200)
        ;
    }

}