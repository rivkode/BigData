package com.bigtech.abc.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JPAMemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByname(String name);
}
