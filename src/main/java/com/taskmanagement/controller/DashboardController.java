package com.taskmanagement.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Correct import
import org.springframework.web.bind.annotation.GetMapping;

import com.taskmanagement.model.Task;
import com.taskmanagement.model.User;
import com.taskmanagement.service.TaskService;
import com.taskmanagement.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService; // Inject UserService

    @Autowired
    private TaskService taskService; // Inject TaskService

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOptional = userService.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();
        List<Task> tasks = taskService.getAllTasksForUser(user);

        model.addAttribute("user", user); // Add this line to fix the issue
        model.addAttribute("tasks", tasks);

        return "dashboard";
    }

}
