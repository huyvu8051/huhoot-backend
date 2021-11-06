package com.huhoot.repository;

import com.huhoot.model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Admin findOneByUsername(String username);

    Page<Admin> findAllByOrderByCreatedDateDesc(Pageable pageable);

    Admin findOneById(int id);

    Page<Admin> findAllByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
