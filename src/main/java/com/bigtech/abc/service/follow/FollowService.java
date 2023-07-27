package com.bigtech.abc.service.follow;

import com.bigtech.abc.common.exception.AppException;
import com.bigtech.abc.common.exception.ErrorCode;
import com.bigtech.abc.domain.follow.Follow;
import com.bigtech.abc.domain.follow.JPAFollowRepository;
import com.bigtech.abc.domain.member.JPAMemberRepository;
import com.bigtech.abc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FollowService {
    private final JPAFollowRepository jpaFollowRepository;

    private final JPAMemberRepository jpaMemberRepository;

    @Transactional
    public void save(Long followerId, Long followingId) {

        /**
         * 동일한 팔로우가 존재하는지 검증하는 로직
         * followerId 기준 이전 동일한 followingId가 있다면 바로 return
         * 아무동작하지 않도록 함
         */
        List<Long> followIdList = jpaFollowRepository.getFollowList(followerId);
        if (followIdList.contains(followingId)) {
            return;
        }
        Member follower = this.findMember(followerId);
        Member following = this.findMember(followingId);

        // 동기화 처리
        followSave(follower, following);
    }

    @Transactional
    public synchronized void followSave(Member follower, Member following) { // synchronized 사용하여 동기화 처리
        var f = jpaFollowRepository.findByFollowerIdAndFollowingId(follower.getId(), following.getId());
        if (f.isPresent()) {
            // follow가 이미 존재하므로 아무런 동작 없음
            return;
        } else {
            Follow follow = Follow.create(follower, following);
            this.jpaFollowRepository.save(follow);
        }
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
