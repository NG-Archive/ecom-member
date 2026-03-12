package site.ng_archive.ecom_member.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import site.ng_archive.ecom_member.global.PrincipalDetails;

@Table
public record Member(
        @Id
        Long id,

        String name,

        String password
){
    public PrincipalDetails toPrincipalDetails() {
        return new PrincipalDetails(id, name);
    }
}
