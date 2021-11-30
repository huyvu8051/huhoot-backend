package com.huhoot.service.impl;

import com.huhoot.repository.AdminRepository;
import com.huhoot.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserDetails user = null;

		if (username != null && username.startsWith("admin")) {
			user = adminRepository.findOneByUsername(username);
		} else {
			user = studentRepository.findOneByUsername(username);
		}

		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		
		return user;
	}

}
