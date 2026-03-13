package site.ng_archive.ecom_member.domain.member;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.ng_archive.ecom_member.domain.member.dto.CreateMemberCommand;

@Component
public class MemberTestTemplate {

    @Autowired
    private MemberService memberService;

    @Autowired
    private Faker faker;

    public Member createMember() {
        String name = faker.regexify("[가-힣]{3,3}");
        String password = faker.regexify("[a-zA-Z]{4,20}");

        CreateMemberCommand command = new CreateMemberCommand(name, password);
        return memberService.createMember(command).block();
    }

    public Member createMember(String name, String password) {
        CreateMemberCommand command = new CreateMemberCommand(name, password);
        return memberService.createMember(command).block();
    }
}
