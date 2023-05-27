package com.bigtech.abc.post;

import com.bigtech.abc.member.Member;
import com.bigtech.abc.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/post")
@Controller
public class PostController {
    private final PostService postService;

    private final JPAPostRepository jpaPostRepository;

    private final MemberService memberService;

//    @GetMapping("/list")
//    public String list(Model model) throws SQLException {
//        List<Post> postList = this.postRepository.findAll(); // 전체 검색
//        model.addAttribute("postList", postList);
//
//        return "post_list";
//    }
//
//    @GetMapping("/list/first")
//    public String listFirst(Model model) throws SQLException {
//        List<Post> postList = this.postRepository.getPostByPage(1, 20);
//        model.addAttribute("postList", postList);
//
//        return "post_list";
//    }
//    @GetMapping("/list/last/{idx}") // 200만개 데이터의 마지막
//    public String listLast(Model model, @PathVariable Integer idx) throws SQLException {
//        List<Post> postList = this.postRepository.getPostByPage(idx/20, 20);
//        model.addAttribute("postList", postList);
//
//        return "post_list";
//    }

    @GetMapping("/save")
    public String getPostSave() {
        return "post_form";
    }

    @PostMapping("/save")
    public String postSave(@Valid @RequestParam String subject, @RequestParam String content) {
        this.postService.save(subject, content);

        return "redirect:/post/listUp";
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
        Page<Post> paging = this.postService.getList(page);
        model.addAttribute("paging", paging);
        return "page_post_list";
    }

    @GetMapping("/vote/{id}")
    public String postVote(Principal principal, @PathVariable("id") Long id, @RequestParam String name) {
        Post post = this.postService.getPost(id);
//        Member member = this.memberService.getMember(name);
        Member member = this.memberService.getMember(principal.getName());
        this.postService.vote(post, member);
        return String.format("redirect:/post/listUp");
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Post post = this.postService.getPost(id);
        model.addAttribute("post", post);

        return "post_detail";
    }


}
