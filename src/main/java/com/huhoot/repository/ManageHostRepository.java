package com.huhoot.repository;

import com.huhoot.admin.manage.host.HostResponse;
import com.huhoot.model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ManageHostRepository extends JpaRepository<Admin, Integer> {

    @Query("SELECT new com.huhoot.admin.manage.host.HostResponse(n.id, n.username, n.isNonLocked, n.createdDate, n.createdBy, n.modifiedDate, n.modifiedBy) " +
            "FROM Admin n " +
            "WHERE n.role = com.huhoot.enums.Role.HOST")
    Page<HostResponse> findAllHost(Pageable pageable);
}
