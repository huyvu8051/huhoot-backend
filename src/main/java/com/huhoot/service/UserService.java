package com.huhoot.service;


import com.huhoot.admin.host.HostResponse;

public interface UserService {
	HostResponse registerNewHostAccount(HostResponse adminDto);
}
