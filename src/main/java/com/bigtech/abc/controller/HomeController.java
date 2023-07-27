package com.bigtech.abc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class HomeController {

    @GetMapping("/home") //홈 화면 페이지 추가
    public String home() {
        return "home";
    }

}
