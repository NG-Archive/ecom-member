package site.ng_archive.ecom_member.domain;

import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;
import site.ng_archive.ecom_member.config.AcceptedTest;
import site.ng_archive.ecom_member.global.ErrorResponse;

import static io.restassured.module.webtestclient.RestAssuredWebTestClient.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;

class MemberControllerTest extends AcceptedTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberTestTemplate memberTestTemplate;

    @Test
    void 회원단건조회() {

        Long existId = memberTestTemplate.createMember();

        ReadMemberResponse response =
            given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1L)
                .consumeWith(document(
                    "Member",
                    "회원 상세 조회",
                    "회원 ID를 사용하여 상세 정보를 조회합니다.",
                    pathParameters(
                        parameterWithName("id").description("회원 아이디")
                    ),
                    responseFields(
                        fieldWithPath("id")
                            .description("회원 ID")
                            .type(JsonFieldType.NUMBER),
                        fieldWithPath("name")
                            .description("회원 이름")
                            .type(JsonFieldType.STRING)
                    )
                ))
                .get("member/{id}")
            .then()
                .status(HttpStatus.OK)
                .log().all()
                .extract().body().as(ReadMemberResponse.class);

        Assertions.assertThat(existId).isEqualTo(response.id());
    }

    @Test
    void 회원단건조회_미존재회원오류() {

        ErrorResponse response =
            given()
                .contentType(ContentType.JSON)
                .pathParam("id", -1L)
                .consumeWith(document(
                    "Member",
                    "회원 상세 조회",
                    "회원 ID를 사용하여 상세 정보를 조회합니다.",
                    pathParameters(
                        parameterWithName("id").description("회원 아이디")
                    ),
                    responseFields(
                        fieldWithPath("errorCode")
                            .description("오류 코드")
                            .type(JsonFieldType.STRING),
                        fieldWithPath("message")
                            .description("오류 메시지")
                            .type(JsonFieldType.STRING)
                    )
                ))
                .get("member/{id}")
            .then()
                .status(HttpStatus.NOT_FOUND)
                .log().all()
                .extract().body().as(ErrorResponse.class);

        Assertions.assertThat(response.errorCode()).isEqualTo("member.notfound");
        Assertions.assertThat(response.message()).isEqualTo("회원이 존재하지 않습니다.");
    }

    @Test
    void 회원가입() {
        CreateMemberRequest request = new CreateMemberRequest("member", "password");
        CreateMemberResponse response =
            given()
                .contentType(ContentType.JSON)
                .body(request)
                .consumeWith(document(
                    "Member",
                    "회원 가입",
                    "아이디와 패스워드로 회원가입을 합니다.",
                    requestFields(
                        fieldWithPath("name")
                            .description("회원 이름")
                            .type(JsonFieldType.STRING)
                            .attributes(key("constraints").value("length(min = 1, max = 20)")),
                        fieldWithPath("password")
                            .description("패스워드")
                            .type(JsonFieldType.STRING)
                            .attributes(key("constraints").value("length(min = 4, max = 20)"))
                    ),
                    responseFields(
                        fieldWithPath("id")
                            .description("회원 ID")
                            .type(JsonFieldType.NUMBER)
                    )
                ))
                .post("/member")
            .then()
                .log().all()
                .status(HttpStatus.CREATED)
                .extract().body().as(CreateMemberResponse.class);

        Member member = memberRepository.findById(response.id()).block();
        Assertions.assertThat(member.id()).isEqualTo(response.id());

    }

    @Test
    void 회원가입_패스워드오류() {
        CreateMemberRequest request = new CreateMemberRequest("member", "p");
        ErrorResponse response =
            given()
                .contentType(ContentType.JSON)
                .body(request)
                .consumeWith(document(
                    "Member",
                    "회원 가입",
                    "아이디와 패스워드로 회원가입을 합니다.",
                    requestFields(
                        fieldWithPath("name")
                            .description("회원 이름")
                            .type(JsonFieldType.STRING)
                            .attributes(key("constraints").value("length(min = 1, max = 20)")),
                        fieldWithPath("password")
                            .description("패스워드")
                            .type(JsonFieldType.STRING)
                            .attributes(key("constraints").value("length(min = 4, max = 20)"))
                    ),
                    responseFields(
                        fieldWithPath("errorCode")
                            .description("오류 코드")
                            .type(JsonFieldType.STRING),
                        fieldWithPath("message")
                            .description("오류 메시지")
                            .type(JsonFieldType.STRING)
                    )
                ))
                .post("/member")
            .then()
                .log().all()
                .status(HttpStatus.BAD_REQUEST)
                .extract().body().as(ErrorResponse.class);

        Assertions.assertThat(response.errorCode()).isEqualTo("input.error.password");
        Assertions.assertThat(response.message()).isEqualTo("비밀번호는 4자 이상 20자 이하여야 합니다.");

    }

}