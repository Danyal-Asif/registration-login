package com.example.registrationlogin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.registrationlogin.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
}
