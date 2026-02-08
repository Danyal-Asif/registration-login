package com.example.registrationlogin.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.registrationlogin.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	@Query("SELECT u FROM User u JOIN u.roles r WHERE r.name <> 'ROLE_ADMIN'")
	List<User> findAllNonAdminUsers();

}
