package com.huhoot.admin.manage.host;

import com.huhoot.exception.UsernameExistedException;
import com.huhoot.model.Admin;
import com.huhoot.repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UpdateHostControllerTest {
    @Autowired
    private ManageHostService service;
    @Autowired
    private AdminRepository repo;

    @Test
    void updateSuccess() throws UsernameExistedException {
        HostResponse hostResponse = service.findAllHostAccount(Pageable.unpaged()).getList().get(0);
        updateHost(hostResponse.getId(), hostResponse.getUsername() + "rand", !hostResponse.getIsNonLocked());
        Admin admin = repo.findOneById(hostResponse.getId()).get();
        assertEquals(hostResponse.getUsername() + "rand", admin.getUsername());
        assertEquals(!hostResponse.getIsNonLocked(), admin.isNonLocked());
    }

    @Test
    void id_host_notFound() {
        assertThrows(UsernameNotFoundException.class, () -> {
            updateHost(159, "admin99", false);
        });
    }

    @Test
    void username_existed() {
        HostResponse hostResponse = service.findAllHostAccount(Pageable.unpaged()).getList().get(0);
        assertThrows(UsernameExistedException.class, () -> {
            updateHost(hostResponse.getId(), "admin", false);
        });

    }


    void updateHost(int id, String username, Boolean isNonLocked) throws UsernameExistedException {
        service.updateHostAccount(HostUpdateRequest.builder()
                .id(id)
                .username(username)
                .isNonLocked(isNonLocked)
                .build());

    }


}