package com.example.registrationlogin.service;

import java.util.List;

import com.example.registrationlogin.dto.UserDto;
import com.example.registrationlogin.entity.User;

public interface UserService {
	void saveUser(UserDto userDto);
	
	User findUserByEmail(String email);
	
	List<UserDto> findAllUsers();
	
	void deleteUser(User user);
}
