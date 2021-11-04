package com.huhoot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huhoot.model.Admin;

public interface IAdminRepository extends JpaRepository<Admin, Integer>{
	Admin findOneByUsernameAndPassword(String username, String password);
	Admin findOneByUsername(String username);
}
