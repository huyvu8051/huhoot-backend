package com.huhoot.repository;

import com.huhoot.admin.manage.host.HostResponse;
import com.huhoot.model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Optional<Admin> findOneByUsername(String username);

    Optional<Admin> findOneById(int id);

    Page<Admin> findAllByUsernameContainingIgnoreCase(String username, Pageable pageable);

    @Query("SELECT new com.huhoot.admin.manage.host.HostResponse(a.id, a.username, a.isNonLocked, a.createdDate, a.createdBy, a.modifiedDate, a.modifiedBy) " +
            "FROM Admin a " +
            "WHERE a.username = :username")
    Optional<HostResponse> findOneHostResponseByUsername(@Param("username") String username);
}
