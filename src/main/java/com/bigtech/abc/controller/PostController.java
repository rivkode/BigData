package com.bigtech.abc.controller;

import com.bigtech.abc.domain.member.Member;
import com.bigtech.abc.domain.post.JPAPostRepository;
import com.bigtech.abc.domain.post.Post;
import com.bigtech.abc.service.post.PostFormDto;
import com.bigtech.abc.service.post.PostPageDto;
import com.bigtech.abc.service.post.PostService;
import com.bigtech.abc.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/post")
@Controller
public class PostController {
    private final PostService postService;

    private final JPAPostRepository jpaPostRepository;

    private final MemberService memberService;

    @GetMapping("/save")
    public String getPostSave() {
        return "post_form";
    }

    @PostMapping("/save")
    public String postSave(Principal principal, @Valid PostFormDto postFormDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "post_form";
        }


        this.postService.save(postFormDto.getSubject(), postFormDto.getContent(), memberService.getMember(principal.getName()));

        return "redirect:/post/pageList";
    }

    @GetMapping("/listUp")
    public String listModified(Model model) throws SQLException {
        List<Post> postList = this.jpaPostRepository.findByModifiedDate();
        model.addAttribute("postList", postList);
//        빠른 쿼리로 조회하기 위해 사용해야 하는 방법은 뭘까

        return "post_list";
    }

    @GetMapping("/QuicklistUp")
    public String QuickListModified(Model model) throws SQLException {
        List<Post> postList = this.jpaPostRepository.findByModifiedDate();
        int lastId = this.postService.findLastId();
        model.addAttribute("postList", postList);

        return "post_list";
    }

    @GetMapping("/pageList")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Post> paging = this.postService.getPostList(page);
        model.addAttribute("paging", paging);
        return "page_post_list";
    }

    @PostMapping("/searchBySubject")
    public String scrollList(Model model, @RequestParam(value = "subject", defaultValue = "") String subject, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Post> paging = this.postService.getQuerydslPostList(subject, page);
        model.addAttribute("paging", paging);
        return "redirect:/post/pageList";
    }


    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Post post = this.postService.getPost(id);
        model.addAttribute("post", post);

        return "post_detail";
    }

    @GetMapping("/memberList/{id}")
    public String memberList(Model model, @PathVariable("id") Long id) {
        List<Member> members = postService.getMemberList(id);
        List<String> nameList = postService.getMemberName(members);

        model.addAttribute("nameList", nameList);

        return "post_memberList";
    }

    @GetMapping("/timeline/{id}")
    public String timeLine(Model model, @PathVariable("id") Long id) {
        List<Post> postList = postService.getPostTimelineList(id); // id = member id임 팔로워의 글을 볼 수 있어야 함
        model.addAttribute(postList);

        return "timeline_list";
    }

    @PostMapping("/scrollNoOffset/{postId}")
    public String scrollNoOffset(Model model, @PathVariable("postId") Long postId) {
        List<Post> postList = postService.scrollNoOffset(postId);
        model.addAttribute(postList);
        return "post_list";
    }

    @GetMapping("/followPost/{id}")
    public String followPost(Model model, @PathVariable("id") Long id) {
        List<Post> postList = postService.followingPost(id);
        model.addAttribute(postList);

        return "timeline_list";
    }
}
