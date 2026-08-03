package com.campusguard.backend.service;

import com.campusguard.backend.entity.Email;
import com.campusguard.backend.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;

    public Email analyzeEmail(Email email) {

        int riskScore = calculateRisk(email);

        email.setRiskScore(riskScore);

        if (riskScore >= 70) {
            email.setStatus("PHISHING");
        } else if (riskScore >= 40) {
            email.setStatus("SUSPICIOUS");
        } else {
            email.setStatus("SAFE");
        }

        return emailRepository.save(email);
    }

    private int calculateRisk(Email email) {

        String text = (email.getSubject() + " " + email.getBody()).toLowerCase();
 
        int score = 0;

        if (text.contains("urgent")) score += 20;
        if (text.contains("verify")) score += 20;
        if (text.contains("password")) score += 20;
        if (text.contains("bank")) score += 20;
        if (text.contains("prize")) score += 20;
        if (text.contains("cash")) score += 20;
        if (text.contains("winner")) score += 20;
        if (text.contains("won")) score += 20;
        if (text.contains("free")) score += 20;
        if (text.contains("claim")) score += 20;
        if (text.contains("click here")) score += 20;
        if (text.contains("http://")) score += 20;
        if (text.contains("https://")) score += 20;
        score = Math.min(score, 100);
        return score;

    }

    public List<Email> getAllEmails() {
        return emailRepository.findAll();
    }

    public Email getEmailById(Long id) {
        return emailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Email not found"));
    }

    public void deleteEmail(Long id) {
        emailRepository.deleteById(id);
    }
}
