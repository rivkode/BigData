package com.bigtech.abc.domain.post;

import com.bigtech.abc.service.post.PostPageDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bigtech.abc.domain.post.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final QPost qPost = post;

    @Override
    public Page<Post> findBySubject(String name, Pageable pageable) {
        BooleanExpression predicate = qPost.subject.eq(name);
        long totalCount = queryFactory.selectFrom(qPost).where(predicate).fetchCount();
        List<Post> content = queryFactory.selectFrom(qPost)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qPost.modifiedDate.desc())
                .fetch();
        return new PageImpl<>(content, pageable, totalCount);
    }

//    @Override
//    public List<PostPageDto> paginationNoOffset(Long postId, String name, int pageSize) {
//        return queryFactory
//                .select(Projections.fields(PostPageDto.class,
//                        post.id.as("postId"),
//                        post.subject,
//                        post.content,
//                        post.createdDate,
//                        post.modifiedDate,
//                        post.member))
//                .from(post)
//                .where(
//                        ltPostId(postId),
//                        post.subject.like(name + "%")
//                )
//                .orderBy(post.id.desc())
//                .limit(pageSize)
//                .fetch();
//    }

    private BooleanExpression ltPostId(Long postId) {
        if (postId == null) {
            return null;
        }
        return post.id.lt(postId);
    }
}
