package com.huhoot.service.impl;

import com.huhoot.dto.ChangePasswordRequest;
import com.huhoot.exception.AccountException;
import com.huhoot.model.Student;
import com.huhoot.repository.StudentRepository;
import com.huhoot.service.StudentAccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudentAccountServiceImpl implements StudentAccountService {
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;

    @Override
    public void changePassword(ChangePasswordRequest request, Student user) throws AccountException {
        boolean matches = passwordEncoder.matches(request.getOldPassword(),user.getPassword());

        if(!matches)throw new AccountException("Old password incorrect!");

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        studentRepository.save(user);
    }
}
