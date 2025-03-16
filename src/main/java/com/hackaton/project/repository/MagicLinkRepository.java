package com.hackaton.project.repository;

import com.hackaton.project.model.MagicLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MagicLinkRepository extends JpaRepository<MagicLink, Long> {
    Optional<MagicLink> findByToken(String token);
    Optional<MagicLink> findByEmailAndUsedFalseAndExpirationTimeAfter(String email, LocalDateTime now);
    void deleteByExpirationTimeBefore(LocalDateTime time);
}