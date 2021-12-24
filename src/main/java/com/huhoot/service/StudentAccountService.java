package com.huhoot.service;

import com.huhoot.dto.ChangePasswordRequest;
import com.huhoot.exception.AccountException;
import com.huhoot.model.Student;

public interface StudentAccountService {

    void changePassword(ChangePasswordRequest request, Student user) throws AccountException;
}
