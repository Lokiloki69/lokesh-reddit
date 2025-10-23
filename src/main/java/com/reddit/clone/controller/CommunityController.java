package com.reddit.clone.controller;

import com.reddit.clone.dto.CommunityDto;
import com.reddit.clone.service.CommunityService;
import com.reddit.clone.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/communities")
@AllArgsConstructor
@Slf4j
public class CommunityController {

    private final CommunityService communityService;
    private final PostService postService;

    @GetMapping
    public String listCommunities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            Model model) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CommunityDto> communities;
        if (search != null && !search.trim().isEmpty()) {
            communities = communityService.searchCommunities(search, pageable);
            model.addAttribute("search", search);
        } else {
            communities = communityService.getAllCommunities(pageable);
        }

        model.addAttribute("communities", communities);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", communities.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        return "community/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("communityDto", new CommunityDto());
        return "community/create";
    }

    @PostMapping("/create")
    public String createCommunity(
            @Valid @ModelAttribute CommunityDto communityDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "community/create";
        }

        try {
            CommunityDto savedCommunity = communityService.save(communityDto);
            redirectAttributes.addFlashAttribute("success",
                    "Community '" + savedCommunity.getName() + "' created successfully!");
            return "redirect:/communities/" + savedCommunity.getId();
        } catch (Exception e) {
            result.rejectValue("name", "error.communityDto", e.getMessage());
            return "community/create";
        }
    }

    @GetMapping("/{id}")
    public String viewCommunity(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "hot") String sort,
            Model model) {

        try {
            CommunityDto community = communityService.getCommunityById(id);

            Pageable pageable = PageRequest.of(page, size);
            var posts = postService.getPostsByCommunity(id, pageable);

            model.addAttribute("community", community);
            model.addAttribute("posts", posts);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", posts.getTotalPages());
            model.addAttribute("sort", sort);

            return "community/view";
        } catch (Exception e) {
            log.error("Community not found: {}", id, e);
            return "redirect:/communities?error=Community not found";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            CommunityDto community = communityService.getCommunityById(id);
            model.addAttribute("communityDto", community);
            return "community/edit";
        } catch (Exception e) {
            return "redirect:/communities?error=Community not found";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateCommunity(
            @PathVariable Long id,
            @Valid @ModelAttribute CommunityDto communityDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "community/edit";
        }

        try {
            communityService.updateCommunity(id, communityDto);
            redirectAttributes.addFlashAttribute("success", "Community updated successfully!");
            return "redirect:/communities/" + id;
        } catch (Exception e) {
            result.rejectValue("description", "error.communityDto", e.getMessage());
            return "community/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteCommunity(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            communityService.deleteCommunity(id);
            redirectAttributes.addFlashAttribute("success", "Community deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete community: " + e.getMessage());
        }
        return "redirect:/communities";
    }
}