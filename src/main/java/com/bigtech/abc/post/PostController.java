package com.bigtech.abc.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/question")
@Controller
public class PostController {
    private final PostService postService;

    private final PostRepository postRepository;

    @GetMapping("/list")
    public String list(Model model) throws SQLException {
        List<Post> postList = this.postRepository.findAll(); // 전체 검색
        model.addAttribute("postList", postList);

        return "question_list";
    }

    @GetMapping("/list/first")
    public String listFirst(Model model) throws SQLException {
        List<Post> postList = this.postRepository.getPostByPage(1, 20);
        model.addAttribute("postList", postList);

        return "question_list";
    }
    @GetMapping("/list/last/{idx}") // 200만개 데이터의 마지막
    public String listLast(Model model, @PathVariable Integer idx) throws SQLException {
        List<Post> postList = this.postRepository.getPostByPage(idx/20, 20);
        model.addAttribute("postList", postList);

        return "question_list";
    }



}
