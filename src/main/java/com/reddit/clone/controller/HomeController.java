package com.reddit.clone.controller;

import com.reddit.clone.service.CommunityService;
import com.reddit.clone.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class HomeController {

    private final PostService postService;
    private final CommunityService communityService;

    @GetMapping("/")
    public String home(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "hot") String sort,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        var posts = sort.equals("new") ?
                postService.getNewPosts(pageable) :
                postService.getHotPosts(pageable);

        var communities = communityService.getAllCommunities(PageRequest.of(0, 5));

        model.addAttribute("posts", posts);
        model.addAttribute("communities", communities);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("sort", sort);

        return "index";
    }
}

