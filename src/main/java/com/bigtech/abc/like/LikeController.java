package com.bigtech.abc.like;

import com.bigtech.abc.member.Member;
import com.bigtech.abc.member.MemberService;
import com.bigtech.abc.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;

    private final PostService postService;

    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}") // id는 post id임, 기준 좋아요 증가
    public ResponseEntity<String> insert(Principal principal, @PathVariable("id") Long id) throws Exception {
        Member member = memberService.getMember(principal.getName());

        LikeRequestDto likeRequestDto = new LikeRequestDto(member.getId(), id);

        likeService.insert(likeRequestDto);
        Long memberId = likeRequestDto.getMemberId();
        String strMemberId = String.valueOf(memberId);
        String strId = String.valueOf(id);
        return ResponseEntity.ok().body("좋아요 수행 member id = " + strMemberId + "post id = " + strId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}") // id는 post id임, cancelLike() 에서 dto 기준으로 찾음
    public ResponseEntity<String> delete(Principal principal, @PathVariable("id") Long id) throws Exception{
        Member member = memberService.getMember(principal.getName());
        System.out.println(principal.getName());
        LikeRequestDto likeRequestDto = new LikeRequestDto(member.getId(), id);
        System.out.println("likeRequestDto 수행");

        likeService.cancelLike(likeRequestDto);
        return ResponseEntity.ok().body("좋아요 취소");
    }
}
