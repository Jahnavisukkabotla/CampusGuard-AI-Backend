package com.campusguard.backend.repository;

import com.campusguard.backend.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
