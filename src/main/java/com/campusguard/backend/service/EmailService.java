/*package com.campusguard.backend.service;

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
*/

package com.campusguard.backend.service;

import com.campusguard.backend.entity.Email;
import com.campusguard.backend.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String ML_API_URL =
            "https://campusguard-ml.onrender.com/predict";

    public Email analyzeEmail(Email email) {

        Map<String, String> requestBody = Map.of(
                "subject", email.getSubject() == null ? "" : email.getSubject(),
                "body", email.getBody() == null ? "" : email.getBody()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request =
                new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                ML_API_URL, request, Map.class
        );

        Map<String, Object> result = response.getBody();

        if (result == null || result.get("riskScore") == null
                || result.get("status") == null) {
            throw new RuntimeException("Invalid response from ML service");
        }

        int riskScore = ((Number) result.get("riskScore")).intValue();
        String status = result.get("status").toString();

        email.setRiskScore(riskScore);
        email.setStatus(status);

        return emailRepository.save(email);
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
