package com.bigtech.abc.member;

import com.bigtech.abc.exception.AppException;
import com.bigtech.abc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

    private final JPAMemberRepository jpaMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> memberOptional = this.jpaMemberRepository.findByName(username);
        Member member = memberOptional.orElseThrow(() -> new AppException(ErrorCode.MEMBERNAME_NOT_FOUND, "멤버가 없습니다"));

        List<GrantedAuthority> authorityList = new ArrayList<>();
        if ("admin".equals(username)) {
            authorityList.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
        } else {
            authorityList.add(new SimpleGrantedAuthority(MemberRole.MEMBER.getValue()));
        }
        return new User(member.getName(), member.getPassword(), authorityList);
    }
}
