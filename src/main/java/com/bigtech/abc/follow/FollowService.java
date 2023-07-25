package com.bigtech.abc.follow;

import com.bigtech.abc.exception.AppException;
import com.bigtech.abc.exception.ErrorCode;
import com.bigtech.abc.member.JPAMemberRepository;
import com.bigtech.abc.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FollowService {
    private final JPAFollowRepository jpaFollowRepository;

    private final JPAMemberRepository jpaMemberRepository;

    public void save(Long followerId, Long followingId) {

        Member follower = this.findMember(followerId);
        Member following = this.findMember(followingId);

        Follow follow = Follow.create(follower, following);

        this.jpaFollowRepository.save(follow);
    }

    public Member findMember(Long id) {
        return jpaMemberRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA, "Member가 없습니다"));
    }

    public List<Member> getFollowerList(Long id) {
        List<Member> members = new ArrayList<>();
        List<Long> followIdList = jpaFollowRepository.getFollowList(id);

        for (Long i : followIdList) {
            Member member = jpaMemberRepository.findById(i)
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA, "Member가 없습니다"));
            members.add(member);
        }
        return members;
    }

    public List<String> getMemberName(List<Member> members) {
        List<String> nameList = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            nameList.add(members.get(i).getName());
        }

        return nameList;
    }
}
