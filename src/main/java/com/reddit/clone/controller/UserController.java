package com.reddit.clone.controller;

import com.reddit.clone.dto.UserDto;
import com.reddit.clone.dto.UserViewDto;
import com.reddit.clone.entity.User;
import com.reddit.clone.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "user/create";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        log.debug("User Data is {}", user);
        User newUser = userService.register(user);
        log.debug("It is in the Post Mapping {}", newUser);
//        return "redirect:/user/" + newUser.getUsername();
        return "redirect:/login";
    }

    @GetMapping("/user/{username}")
    public String viewUserProfile(@PathVariable String username, Model model) {
        UserViewDto userView = userService.getUserView(username);

        model.addAttribute("userView", userView);
        model.addAttribute("activeTab", "overview");

        return "user/view";
    }

    @GetMapping("/user/{username}/posts")
    public String viewUserPosts(@PathVariable String username, Model model) {
        UserViewDto userView = userService.getUserView(username);

        model.addAttribute("userView", userView);
        model.addAttribute("activeTab", "posts");

        return "user/view";
    }

    @GetMapping("/user/{username}/comments")
    public String viewUserComments(@PathVariable String username, Model model) {
        UserViewDto userView = userService.getUserView(username);

        model.addAttribute("userView", userView);
        model.addAttribute("activeTab", "comments");

        return "user/view";
    }
}

