package com.bigtech.abc.domain.post;

import com.bigtech.abc.service.post.PostPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    Page<Post> findBySubject(String name, Pageable pageable);

//    List<PostPageDto> paginationNoOffset(Long postId, String name, int pageSize);
}
