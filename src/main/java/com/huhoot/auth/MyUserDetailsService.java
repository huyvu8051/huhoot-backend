package com.huhoot.auth;

import com.huhoot.admin.manage.student.StudentRepository;
import com.huhoot.model.Admin;
import com.huhoot.model.Student;
import com.huhoot.repository.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails userDetails = null;

        if (username != null && username.startsWith("admin")) {
            Optional<Admin> adminOptional = adminRepository.findOneByUsername(username);
            userDetails = adminOptional.orElseThrow(() -> new UsernameNotFoundException("Account not found"));
        } else {
            Optional<Student> optionalStudent = studentRepository.findOneByUsername(username);
            userDetails = optionalStudent.orElseThrow(() -> new UsernameNotFoundException("Account not found!"));
        }


        return userDetails;
    }

}
