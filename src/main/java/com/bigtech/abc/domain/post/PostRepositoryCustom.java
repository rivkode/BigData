package com.bigtech.abc.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> findBySubject(String name, Pageable pageable);
}
