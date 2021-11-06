package com.huhoot.repository;

import com.huhoot.model.Admin;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Admin findOneByUsername(String username);

    List<Admin> findAllByOrderByCreatedDateDesc(Pageable pageable);

    Admin findOneById(int id);
}
