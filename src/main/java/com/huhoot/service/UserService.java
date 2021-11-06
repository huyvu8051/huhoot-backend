package com.huhoot.service;


import com.huhoot.dto.HostResponse;

public interface UserService {
	HostResponse registerNewHostAccount(HostResponse adminDto);
}
