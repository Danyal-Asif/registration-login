package com.example.registrationlogin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.registrationlogin.dto.UpdateValidation;
import com.example.registrationlogin.dto.UserDto;
import com.example.registrationlogin.entity.User;
import com.example.registrationlogin.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {
	
	
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager; 
	
	public AuthController(UserService userService) {
		this.userService=userService;
	}
	
	@GetMapping("/index")
	public String home() {
		return "index";
	}
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		UserDto user=new UserDto();
		model.addAttribute("user",user);
		return "register";
	}
	
	@PostMapping("/register/save")
	
	public String registration(@Valid @ModelAttribute("user") UserDto userDto,
			BindingResult result, Model model)
	{
		User existingUser=userService.findUserByEmail(userDto.getEmail());
		
		if(existingUser !=null && existingUser.getEmail() !=null && !existingUser.getEmail().isEmpty()) {
			result.rejectValue("email", null,"There is already an account "
					+ "registered with the same email");
		}
		if(result.hasErrors()) {
			model.addAttribute("user", userDto);
			return "/register";
		}
		userService.saveUser(userDto);
		return "redirect:/register?success";
	}
	
	@GetMapping("/users")
	public String users(Model model) {
		List<UserDto> users=userService.findAllUsers();
		model.addAttribute("users",users);
		return "users";
	}
	
	
	@GetMapping("/deleteUser")
	public String deleteUser(@RequestParam("email") String email,Model model) {
		model.addAttribute("email",email);
		return "deleteUser";
	}
	
	@GetMapping("/updateUser")
	public String updateUser(@RequestParam("email") String email,Model model) {
		User userData=userService.findUserByEmail(email);
		UserDto user=new UserDto();
		user.setFirstName(userData.getName().split(" ")[0]);
		user.setLastName(userData.getName().split(" ")[1]);
		user.setEmail(userData.getEmail());
		model.addAttribute("user",user);
		return "updateUser";
	}
	
	@PostMapping("/updateUser/update")
	public String update(@Validated(UpdateValidation.class) 
	@Valid @ModelAttribute("user") UserDto userDto,
			@RequestParam("newPassword") String newPassword,
			BindingResult result, Model model,@RequestParam("hiddenEmail") String email)
	{
		System.out.println("line 102"+email);
		User userName=userService.findUserByEmail(email);
		System.out.println("hidden email"+email);
		try {
		Authentication auth=authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userName.getEmail(), userDto.getPassword()));
		if(auth.isAuthenticated()) {
			String firstName=userDto.getFirstName();
			String lastName=userDto.getLastName();
//			String email=userDto.getEmail();
			String password=newPassword;
			userService.updateUser(email,firstName, lastName, password);
			return "redirect:/index?success";
		}
		}
		catch(AuthenticationException e){
			result.rejectValue("password", null,"Incorrect password entered ");
			String error="Incorrect password entered ";
			userDto.setEmail(email);
			model.addAttribute("user", userDto);
			return "/updateUser";
		}
		return "redirect:/updateUser?error&user="+userDto;
	}
	
	@PostMapping("/deleteUser/delete")
	public String delete(@RequestParam("hiddenEmail") String email, @RequestParam("hiddenPassword") String password) 
	{
		try {
			Authentication auth=authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(email, password));
			if(auth.isAuthenticated()) {
				
				String userEmail=auth.getName();
				User user=userService.findUserByEmail(userEmail);
				userService.deleteUser(user);
				SecurityContextHolder.clearContext();
				return "redirect:/index?success";
			}
		}
		catch(AuthenticationException e) {
			return "redirect:/deleteUser?error&email="+email;
		}
		return "redirect:/deleteUser?error";

	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
}
