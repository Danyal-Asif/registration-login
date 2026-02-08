package com.example.registrationlogin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

public class UserDto {
	private Long id;
	
	@NotEmpty
	private String firstName;

	@Size(max = 50)
	private String lastName;
	
	@Null(message = "Email must be empty", groups = UpdateValidation.class)
	@NotEmpty(message="Email should not be empty")
	@Email
	private String email;
	
	@NotEmpty(message="Password should not be emtpy")
	private String password;
	
	public UserDto() {}
	
	public UserDto(Long id, @NotEmpty String firstName, @NotEmpty String lastName,
			@NotEmpty(message = "Email should not be empty") @Email String email,
			@NotEmpty(message = "Password should not be emtpy") String password) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	
}
