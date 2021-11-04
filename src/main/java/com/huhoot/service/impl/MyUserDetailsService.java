package com.huhoot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.huhoot.repository.IAdminRepository;
import com.huhoot.repository.IStudentRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private IAdminRepository iAdminRepository;

	@Autowired
	private IStudentRepository iStudentRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserDetails user = null;

		if (username != null && username.startsWith("admin")) {
			user = iAdminRepository.findOneByUsername(username);
		} else {
			user = iStudentRepository.findOneByUsername(username);
		}

		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		
		return user;
	}

}
