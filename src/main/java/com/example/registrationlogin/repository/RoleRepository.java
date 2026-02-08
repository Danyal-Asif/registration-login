package com.example.registrationlogin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.registrationlogin.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);
}
