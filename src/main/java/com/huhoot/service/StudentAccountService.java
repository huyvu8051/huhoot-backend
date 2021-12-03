package com.huhoot.service;

import com.huhoot.dto.ChangePasswordRequest;
import com.huhoot.exception.AccountException;
import com.huhoot.model.Student;
import org.springframework.security.core.userdetails.UserDetails;

public interface StudentAccountService {

    void changePassword(ChangePasswordRequest request, Student user) throws AccountException;
}
