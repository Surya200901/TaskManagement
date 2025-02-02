package com.taskmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.taskmanagement.service.UserService;

@Component
public class PasswordMigration {

    @Autowired
    private UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        userService.encodeExistingPasswords(); // Run encoding logic after app startup
    }
}
