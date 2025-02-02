package com.taskmanagement.service;

import com.taskmanagement.model.Task;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getAllTasksForUser(User user) {
        return taskRepository.findByUser(user);  // Query tasks by user
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task); // Simply save the updated task
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
    
    public void updateCompletionStatus(Long id, boolean completed) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setCompleted(completed); // Update the completed field
            taskRepository.save(task); // Save the updated task
        }
    }

    
    public Task findById(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);  // Use the repository to find the task
        if (taskOptional.isPresent()) {
            return taskOptional.get();  // Return the task if found
        } else {
            throw new RuntimeException("Task not found");  // Or handle this differently (e.g., return null)
        }
    }
}
