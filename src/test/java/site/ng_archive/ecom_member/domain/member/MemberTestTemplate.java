package site.ng_archive.ecom_member.domain.member;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.ng_archive.ecom_member.domain.member.dto.CreateMemberCommand;
import site.ng_archive.ecom_member.domain.member.dto.LoginCommand;

@Component
public class MemberTestTemplate {

    @Autowired
    private MemberService memberService;

    @Autowired
    private Faker faker;

    public Member createMember() {
        String name = getRandomName();
        String password = getRandomPassword();

        CreateMemberCommand command = new CreateMemberCommand(name, password);
        return memberService.createMember(command).block();
    }

    public Member createMember(String name, String password) {
        CreateMemberCommand command = new CreateMemberCommand(name, password);
        return memberService.createMember(command).block();
    }

    public String login(Long id, String password) {
        LoginCommand loginCommand = new LoginCommand(id, password);
        return memberService.login(loginCommand).block();
    }

    public String getRandomName() {
        return faker.regexify("[가-힣]{3,3}");
    }

    public String getRandomPassword() {
        return faker.regexify("[a-zA-Z]{4,20}");
    }


}
