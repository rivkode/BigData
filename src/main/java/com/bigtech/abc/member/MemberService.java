package com.bigtech.abc.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final JPAMemberRepository jpaMemberRepository;

    public void save(String name, String email, String birth) {
        Member member = new Member();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime birthDate = LocalDateTime.parse(birth, formatter);

        String input = "2022-01-01 11:22:33";
        LocalDateTime createdDate = LocalDateTime.parse(input, formatter);

        member.setName(name);
        member.setEmail(email);
        member.setBirth(birthDate);
        member.setCreatedDate(createdDate);
        member.setUpdateDate(LocalDateTime.now());
        this.jpaMemberRepository.save(member);
    }

    public Member getMember(String name) {
        Optional<Member> member = this.jpaMemberRepository.findByname(name);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new RuntimeException("Data not found");
        }
    }

    public Member createMember(String name, String email, String password, LocalDateTime birth, LocalDateTime createDate, LocalDateTime updateDate) {
        Member member = new Member();
        member.setName(name);
        member.setEmail(email);
        member.setPassword(password);
        member.setBirth(birth);
        member.setCreatedDate(createDate);
        member.setUpdateDate(updateDate);
        return member;
    }


}
