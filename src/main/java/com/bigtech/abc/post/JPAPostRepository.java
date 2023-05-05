package com.bigtech.abc.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JPAPostRepository extends JpaRepository<Post, Long> {

    @Query(
            value = "SELECT * FROM post ORDER BY modifiedDate DESC LIMIT 100",
    nativeQuery = true)
    List<Post> findByModifiedDate();
}
