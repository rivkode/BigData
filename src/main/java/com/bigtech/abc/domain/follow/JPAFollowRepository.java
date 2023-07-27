package com.bigtech.abc.domain.follow;

import com.bigtech.abc.domain.follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JPAFollowRepository extends JpaRepository<Follow, Long> {
    @Query(value = "select following_id from follow where follower_id = :id", nativeQuery = true)
    List<Long> getFollowList(@Param("id") Long id);


//    @Lock(LockModeType.PESSIMISTIC_WRITE) // 굳이 LOCK 할 필요 없음 for update 가 그 역할을 수행함
    @Query(value = "SELECT * FROM follow WHERE follower_id = :followerId AND following_id = :followingId for update", nativeQuery = true)
    Optional<Follow> findByFollowerIdAndFollowingId(@Param("followerId") Long followerId, @Param("followingId") Long followingId);


}
