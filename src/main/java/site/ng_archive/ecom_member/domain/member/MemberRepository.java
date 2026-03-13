package site.ng_archive.ecom_member.domain.member;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface MemberRepository extends R2dbcRepository<Member, Long> {
}
