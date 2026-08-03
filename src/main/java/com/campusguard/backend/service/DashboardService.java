package com.campusguard.backend.service;

import com.campusguard.backend.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private EmailRepository emailRepository;

    public Map<String, Long> getStatistics() {

        Map<String, Long> stats = new HashMap<>();

        stats.put("totalEmails", emailRepository.count());

        stats.put("safeEmails",
                emailRepository.findAll().stream()
                        .filter(e -> "SAFE".equals(e.getStatus()))
                        .count());

        stats.put("suspiciousEmails",
                emailRepository.findAll().stream()
                        .filter(e -> "SUSPICIOUS".equals(e.getStatus()))
                        .count());

        stats.put("phishingEmails",
                emailRepository.findAll().stream()
                        .filter(e -> "PHISHING".equals(e.getStatus()))
                        .count());

        return stats;
    }
}
