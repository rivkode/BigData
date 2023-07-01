package com.bigtech.abc.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JPAMemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);

    @Query("SELECT count(*) FROM Member")
    Integer getMemberCount();
}
