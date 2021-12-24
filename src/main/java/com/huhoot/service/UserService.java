package com.huhoot.service;


import com.huhoot.admin.manage.host.HostResponse;

public interface UserService {
	HostResponse registerNewHostAccount(HostResponse adminDto);
}
