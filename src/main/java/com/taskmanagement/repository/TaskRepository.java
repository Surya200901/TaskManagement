package com.taskmanagement.repository;

import com.taskmanagement.model.Task;
import com.taskmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // Custom query to find tasks by user
    List<Task> findByUser(User user);
}
