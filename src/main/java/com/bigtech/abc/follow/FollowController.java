package com.bigtech.abc.follow;

import com.bigtech.abc.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/follow")
@Controller
@Slf4j
public class FollowController {
    private final FollowService followService;


    @PostMapping("/do")
    public String follow(@Valid FollowDto followDto) {
        System.out.println("followDto = " + followDto.getFollowingId());
        Long id = followDto.getFollowerId();
        followService.save(followDto.getFollowerId(), followDto.getFollowingId());
        return "redirect:/follow/followList/" + id;
    }

    @GetMapping("/apply")
    public String followForm() {
        return "follow_form";
    }

    @GetMapping("/followList/{id}")
    public String followList(Model model, @PathVariable("id") Long id) {
        System.out.println(id);
        List<Member> members = followService.getFollowerList(id);
        List<String> nameList = followService.getMemberName(members);
        model.addAttribute("nameList", nameList);

        return "follow_list";
    }

    @PostMapping("/pre")
    public String pre(@Valid FollowDto followDto) {
        Long id = followDto.getFollowerId();
        return "redirect:/post/timeline/" + id;
    }

    @GetMapping("/pre")
    public String pre() {
        return "timeline_pre";
    }
}
