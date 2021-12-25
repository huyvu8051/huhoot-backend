package com.huhoot.student;

import com.huhoot.dto.ChangePasswordRequest;
import com.huhoot.exception.AccountException;
import com.huhoot.model.Student;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
