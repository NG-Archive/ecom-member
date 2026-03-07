package site.ng_archive.ecom_member.domain;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @RequestMapping("/member/{id}")
    public void readMember(@PathVariable Long id) {

    }
}
