package com.huhoot.controller.student;

import com.huhoot.dto.ChangePasswordRequest;
import com.huhoot.exception.AccountException;
import com.huhoot.model.Student;
import com.huhoot.service.StudentAccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/student")
@AllArgsConstructor
public class StudentAccountController {
    private final StudentAccountService studentAccountService;

    @PutMapping("/account")
    public void changePassword(@Valid @RequestBody ChangePasswordRequest request) throws AccountException {
        Student userDetails = (Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        studentAccountService
                .changePassword(request, userDetails);
    }

}
