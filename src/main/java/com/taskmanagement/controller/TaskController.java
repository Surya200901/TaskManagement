package com.taskmanagement.controller;

import com.taskmanagement.model.Task;
import com.taskmanagement.model.User;
import com.taskmanagement.service.TaskService;
import com.taskmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import java.util.Map;

@Controller
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    // Show the task page with all tasks
    @GetMapping("/task")
    public String showTaskPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOptional = userService.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();
        List<Task> tasks = taskService.getAllTasksForUser(user);
        model.addAttribute("tasks", tasks);
        return "task";
    }

    // Create a new task
    @PostMapping("/create")
    public String createTask(@ModelAttribute Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOptional = userService.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();
        task.setUser(user);

        taskService.createTask(task);
        return "redirect:/task/task";
    }

    // Update a task
    @PostMapping("/update/{id}")
    @ResponseBody
    public String updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        Task existingTask = taskService.findById(id);

        // Update task fields
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setDeadline(updatedTask.getDeadline());
        existingTask.setCategory(updatedTask.getCategory());
        existingTask.setPriority(updatedTask.getPriority());

        taskService.updateTask(existingTask); // Save the updated task
        return "Task updated successfully";
    }
    
    // Update completion status of a task
    @PostMapping("/update-completed/{id}")
    public ResponseEntity<Void> updateCompletionStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> status) {
        boolean completed = status.get("completed");
        System.out.println("Received request to update completion status for task ID: " + id + " to: " + completed);
        taskService.updateCompletionStatus(id, completed);
        return ResponseEntity.ok().build();
    }
    
    // Controller method to delete a task
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/task/task";
    }
}
