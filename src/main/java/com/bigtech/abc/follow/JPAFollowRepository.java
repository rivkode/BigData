package com.bigtech.abc.follow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JPAFollowRepository extends JpaRepository<Follow, Long> {
    @Query(value = "select following_id from follow where follower_id = :id", nativeQuery = true)
    List<Long> getFollowList(@Param("id") Long id);
}
