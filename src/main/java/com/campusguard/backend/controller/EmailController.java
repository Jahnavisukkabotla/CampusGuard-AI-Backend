package com.campusguard.backend.controller;

import com.campusguard.backend.entity.Email;
import com.campusguard.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/analyze")
    public Email analyzeEmail(@RequestBody Email email) {
        return emailService.analyzeEmail(email);
    }

    @GetMapping
    public List<Email> getAllEmails() {
        return emailService.getAllEmails();
    }

    @GetMapping("/{id}")
    public Email getEmailById(@PathVariable Long id) {
        return emailService.getEmailById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteEmail(@PathVariable Long id) {
        emailService.deleteEmail(id);
        return "Email deleted successfully";
    }
}
