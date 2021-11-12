package com.huhoot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminManageHostControllerTest {

    @Autowired
    AdminManageHostController controller;

    @Test
    void getAll() {
        controller.getAll(2, 8, "createdDate", "desc");
    }

    @Test
    void getDetails() {
    }

    @Test
    void search() {
    }

    @Test
    void addMany() {
    }

    @Test
    void update() {
    }

    @Test
    void lock() {
    }

    @Test
    void setAdminService() {
    }
}