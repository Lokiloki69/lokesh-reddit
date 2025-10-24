package com.reddit.clone.controller;

import com.reddit.clone.dto.PostDto;
import com.reddit.clone.entity.Post;
import com.reddit.clone.mapper.PostMapper;
import com.reddit.clone.service.CommunityService;
import com.reddit.clone.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class HomeController {

    private final PostService postService;
    private final CommunityService communityService;
    private final PostMapper postMapper;

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

//    @GetMapping("/search")
//    public String searchPosts(@RequestParam("q") String query, Model model) {
//        var posts = postService.searchByTitleOrContent(query); // List or Page<Post>
//        var communities = communityService.getAllCommunities(PageRequest.of(0, 5));
//        model.addAttribute("posts", posts);
//        model.addAttribute("communities", communities);
//        // Optionally set attributes like page or sort if needed
//        model.addAttribute("currentPage", 0); // or remove if not using pagination
//        model.addAttribute("totalPages", 1); // or set accordingly
//        model.addAttribute("sort", "relevance"); // or "search"
//        return "index";
//    }
//@GetMapping("/search")
//public String searchPosts(@RequestParam("query") String query,
//                          @RequestParam(defaultValue = "0") int page,
//                          @RequestParam(defaultValue = "10") int size,
//                          Model model) {
//    Pageable pageable = PageRequest.of(page, size);
//    var posts = postService.searchPosts(query, pageable);
//
//    var communities = communityService.getAllCommunities(PageRequest.of(0, 5));  // Add communities here to avoid Thymeleaf error
//    model.addAttribute("posts", posts);
//    model.addAttribute("communities", communities);  // add communities here as well
//    model.addAttribute("currentPage", page);
//    model.addAttribute("totalPages", posts.getTotalPages());
//    model.addAttribute("sort", "search");
//
//    return "index";
//}

    // PostController.java (Corrected Method)

    @GetMapping("/search")
    public String searchPosts(@RequestParam("query") String query,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {

        Pageable pageable = PageRequest.of(page, size);

        // 1. Get the raw Post entities from the database
        Page<Post> postEntities = postService.searchPosts(query, pageable);

        // 2. CRITICAL FIX: Map the Page<Post> to a Page<PostDto>
        Page<PostDto> postDtos = postEntities.map(postMapper::mapEntityToDto);

        // Get communities for the sidebar (as you have it)
        var communities = communityService.getAllCommunities(PageRequest.of(0, 5));

        // 3. Add the DTOs to the model
        model.addAttribute("posts", postDtos); // <--- Now it's Page<PostDto>
        model.addAttribute("communities", communities);
        model.addAttribute("currentPage", postDtos.getNumber());
        model.addAttribute("totalPages", postDtos.getTotalPages());
        model.addAttribute("query", query); // Keep query in model for search bar persistence

        return "index";
    }


}

