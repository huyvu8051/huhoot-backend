package com.huhoot.host;

import com.huhoot.dto.ChangePasswordRequest;
import com.huhoot.exception.AccountException;
import com.huhoot.model.Admin;
import com.huhoot.repository.AdminRepository;
import com.huhoot.host.HostAccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HostAccountServiceImpl implements HostAccountService {
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    @Override
    public void changePassword(ChangePasswordRequest request, Admin userDetails) throws AccountException {
        boolean matches = passwordEncoder.matches(request.getOldPassword(), userDetails.getPassword());

        if (!matches) throw new AccountException("Old password incorrect!");
        userDetails.setPassword(passwordEncoder.encode(request.getPassword()));
        adminRepository.save(userDetails);
    }
}
