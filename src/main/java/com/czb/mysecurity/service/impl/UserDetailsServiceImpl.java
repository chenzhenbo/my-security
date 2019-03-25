package com.czb.mysecurity.service.impl;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		// 获取用户 TODO
		System.out.println("进入UserDetailsService=====");
		return new org.springframework.security.core.userdetails.User(userName, "", new ArrayList<>());
	}
	
	

}
