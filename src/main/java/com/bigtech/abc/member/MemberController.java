package com.bigtech.abc.member;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@RequestMapping("/member")
@Controller
public class MemberController {
    private final MemberService memberService;

    private final JPAMemberRepository jpaMemberRepository;

    @GetMapping("/save")
    public String getMemberSave() {
        return "member_form";
    }

    @PostMapping("/save")
    public String MemberSave(@RequestBody MemberFormDto memberFormDto) {
        this.memberService.save(memberFormDto.getName(), memberFormDto.getEmail(), memberFormDto.getBirth());

        return "redirect:/post/listUp";
    }

    @GetMapping("/signup")
    public String signup(MemberCreateForm memberCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(MemberCreateForm memberCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        memberService.save(memberCreateForm.getName(), memberCreateForm.getEmail(), memberCreateForm.getBirth());

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }
}
