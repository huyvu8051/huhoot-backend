package com.huhoot.admin.manage.host;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AddHostControllerTest {
    @Autowired
    private ManageHostService service;
    @Test
    void addSuccess() {
        List<HostAddErrorResponse> list = addTest("admin6", true);
        assertTrue(list.size() == 0);
    }
    @Test
    void addFalse_duplicate_username(){
        List<HostAddErrorResponse> list = addTest("admin", true);
        assertFalse(list.size() == 0);
    }
    @Test
    void addFalse_null_or_empty_username(){
        List<HostAddErrorResponse> list = addTest("", true);
        assertFalse(list.size() == 0);
    }
    List<HostAddErrorResponse> addTest(String username, Boolean isNonLocked){
        List<HostAddRequest> hostDTOs = new ArrayList<>();
        hostDTOs.add(new HostAddRequest().builder()
                        .username(username)
                        .isNonLocked(isNonLocked)
                .build());
        return service.addManyHostAccount(hostDTOs);
    }
}