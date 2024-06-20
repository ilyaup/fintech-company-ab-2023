package com.academy.fintech.origination.core.service.application.db.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findById(String id);

    Optional<Client> findClientByEmail(String email);

    boolean existsByEmail(String email);
}
