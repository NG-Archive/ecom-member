package site.ng_archive.ecom_member.domain;

import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import site.ng_archive.ecom_member.config.AcceptedTest;
import site.ng_archive.ecom_member.domain.member.Member;
import site.ng_archive.ecom_member.domain.member.MemberRepository;
import site.ng_archive.ecom_member.domain.member.dto.CreateMemberRequest;
import site.ng_archive.ecom_member.domain.member.dto.CreateMemberResponse;
import site.ng_archive.ecom_member.domain.member.dto.LoginRequest;
import site.ng_archive.ecom_member.domain.member.dto.LoginResponse;
import site.ng_archive.ecom_member.domain.member.dto.ReadMemberResponse;
import site.ng_archive.ecom_member.global.error.ErrorResponse;
import site.ng_archive.ecom_member.global.token.TokenUtil;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static io.restassured.module.webtestclient.RestAssuredWebTestClient.given;

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
                .pathParam("id", existId)
                .consumeWith(document(
                    info()
                        .tag("Member")
                        .summary("회원 상세 조회")
                        .description("회원 ID를 사용하여 상세 정보를 조회합니다.")
                        .pathParameters(
                            parameterWithName("id").description("회원 아이디")
                        )
                        .responseFields(
                            field(ReadMemberResponse.class, "id", "회원 ID"),
                            field(ReadMemberResponse.class, "name", "회원 이름")
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
                    info()
                        .tag("Member")
                        .summary("회원 상세 조회")
                        .description("회원 ID를 사용하여 상세 정보를 조회합니다.")
                        .pathParameters(
                            parameterWithName("id").description("회원 아이디")
                        )
                        .responseFields(
                            field(ErrorResponse.class, "errorCode", "오류 코드"),
                            field(ErrorResponse.class, "message", "오류 메시지")
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
                    info()
                        .tag("Member")
                        .summary("회원 가입")
                        .description("아이디와 패스워드로 회원가입을 합니다.")
                        .requestFields(
                            field(CreateMemberRequest.class, "name", "회원 이름"),
                            field(CreateMemberRequest.class, "password", "패스워드")
                        )
                        .responseFields(
                            field(CreateMemberResponse.class, "id", "회원 ID")
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
                    info()
                        .tag("Member")
                        .summary("회원 가입")
                        .description("아이디와 패스워드로 회원가입을 합니다.")
                        .requestFields(
                            field(CreateMemberRequest.class, "name", "회원 이름"),
                            field(CreateMemberRequest.class, "password", "패스워드")
                        )
                        .responseFields(
                            field(ErrorResponse.class, "errorCode", "오류 코드"),
                            field(ErrorResponse.class, "message", "오류 메시지")
                        )
                ))
                .post("/member")
                .then()
                .log().all()
                .status(HttpStatus.BAD_REQUEST)
                .extract().body().as(ErrorResponse.class);

        Assertions.assertThat(response.errorCode()).isEqualTo("member.password.size");
        Assertions.assertThat(response.message()).isEqualTo("비밀번호는 4자 이상 20자 이하여야 합니다.");

    }

    @Test
    void 로그인() {
        Member exist = memberTestTemplate.createMember("name", "password");
        LoginRequest request = new LoginRequest(exist.id(), exist.password());
        LoginResponse response =
            given()
                .contentType(ContentType.JSON)
                .body(request)
                .consumeWith(document(
                    info()
                        .tag("Member")
                        .summary("로그인")
                        .description("아이디와 패스워드로 로그인을 합니다. 응답값은 토큰입니다.")
                        .requestFields(
                            field(LoginRequest.class, "id", "회원 ID"),
                            field(LoginRequest.class, "password", "패스워드")
                        )
                        .responseFields(
                            field(LoginResponse.class, "token", "JWT 토큰(유효기간 30분)")
                        )
                ))
                .post("/login")
                .then()
                .log().all()
                .status(HttpStatus.OK)
                .extract().body().as(LoginResponse.class);

        Long verified = TokenUtil.verify(response.token());
        Assertions.assertThat(exist.id()).isEqualTo(verified);
    }

    @Test
    void 로그인_실패() {
        Member exist = memberTestTemplate.createMember("name", "password");
        LoginRequest request = new LoginRequest(exist.id(), exist.password() + "fail");
        ErrorResponse response =
            given()
                .contentType(ContentType.JSON)
                .body(request)
                .consumeWith(document(
                    info()
                        .tag("Member")
                        .summary("로그인")
                        .description("아이디와 패스워드로 로그인을 시도하며, 인증 실패 시 오류 응답을 반환합니다.")
                        .requestFields(
                            field(LoginRequest.class, "id", "회원 ID"),
                            field(LoginRequest.class, "password", "패스워드")
                        )
                        .responseFields(
                            field(ErrorResponse.class, "errorCode", "오류 코드"),
                            field(ErrorResponse.class, "message", "오류 메시지")
                        )
                ))
                .post("/login")
                .then()
                .log().all()
                .status(HttpStatus.UNAUTHORIZED)
                .extract().body().as(ErrorResponse.class);

        Assertions.assertThat(response.errorCode()).isEqualTo("member.login.fail");
        Assertions.assertThat(response.message()).isEqualTo("아이디 혹은 비밀번호가 일치하지 않습니다.");
    }

}