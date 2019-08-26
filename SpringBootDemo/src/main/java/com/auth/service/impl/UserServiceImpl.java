package com.auth.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.step.item.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth.Controller.UserController;
import com.auth.entity.AuthUser;
import com.auth.mapper.UserMapper;
import com.auth.service.UserService;
import com.util.CacheDuration;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class UserServiceImpl implements UserService{
	
	@Autowired
    private  UserMapper userMapper;
	
	/*@Autowired  
	private RedisUtils redisUtil;  */
	
	
	@Override
	@Cacheable(value="user-key")
	@CacheDuration(duration=-1) //设置过期时间
	public List<AuthUser> findUserByPhone(String userNumber) {
		List<AuthUser> findUserByPhone = userMapper.findUserByPhone(userNumber);
		log.info("查询数据库");
		return findUserByPhone;
	}

	
	@Override
	@Transactional
	public void updateByName(AuthUser auth) {
	/*	auth.setUserNumber("ws");
		auth.setLoginName("ws创建的用户");
		//为防止脏数据的出现，故初步先删除缓存 ，再修改数据库
		redisUtil.remove(auth.getUserNumber());
		userMapper.updateUser(auth);*/
	}


	@Override
	public AuthUser findByLoginName(String currentLoginName) {
		return userMapper.findByLoginName(currentLoginName);
	}

}
