package com.huhoot.repository;

import com.huhoot.model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Optional<Admin> findOneByUsername(String username);

    Optional<Admin> findOneById(int id);

    Page<Admin> findAllByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
