package com.bigtech.abc.like;

import com.bigtech.abc.member.Member;
import com.bigtech.abc.member.MemberService;
import com.bigtech.abc.post.Post;
import com.bigtech.abc.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;

    private final PostService postService;

    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String insert(Principal principal, @PathVariable("id") Long id) throws Exception {
        Post post = postService.getPost(id);
        Member member = memberService.getMember(principal.getName());

        LikeRequestDto likeRequestDto = new LikeRequestDto();
        likeRequestDto.setPostId(post.getId());
        likeRequestDto.setMemberId(member.getId());

        likeService.insert(likeRequestDto);
        Long memberId = likeRequestDto.getMemberId();
        return String.format("좋아요 성공 %d", memberId);
    }

    @DeleteMapping()
    public ResponseEntity<String> delete(@Valid LikeRequestDto likeRequestDto) throws Exception{
        likeService.cancelLike(likeRequestDto);
        return ResponseEntity.ok().body("좋아요 실패");
    }
}
