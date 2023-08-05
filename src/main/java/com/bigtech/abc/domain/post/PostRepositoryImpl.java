package com.bigtech.abc.domain.post;

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
}
