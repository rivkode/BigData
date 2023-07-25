package com.bigtech.abc.post;

import com.bigtech.abc.generic.Result;
import com.bigtech.abc.member.Member;
import com.bigtech.abc.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
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

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Post post = this.postService.getPost(id);
        model.addAttribute("post", post);

        return "post_detail";
    }

    @GetMapping("memberList/{id}")
    public String memberList(Model model, @PathVariable("id") Long id) {
        List<Member> members = postService.getMemberList(id);
        List<String> nameList = postService.getMemberName(members);

        model.addAttribute("nameList", nameList);

        return "post_memberList";
    }

    @GetMapping("/timeline/{id}")
    public String timeLine(Model model, @PathVariable("id") Long id) {
        List<Post> postList = postService.getPostTimelineList(id);
        model.addAttribute(postList);

        return "timeline_list";
    }
}
