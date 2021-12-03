package com.huhoot.service;

import com.huhoot.dto.ChangePasswordRequest;
import com.huhoot.exception.AccountException;
import com.huhoot.model.Admin;

public interface HostAccountService {

    void changePassword(ChangePasswordRequest request, Admin userDetails) throws AccountException;
}
