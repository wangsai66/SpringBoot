package com.auth.service;

import java.util.List;
import java.util.Map;

import com.auth.entity.AuthUser;

public interface UserService {

	List<AuthUser> findUserByPhone(String userNumber);

	void updateByName(AuthUser auth);

	AuthUser findByLoginName(String currentLoginName);

	
	
	
	
}
