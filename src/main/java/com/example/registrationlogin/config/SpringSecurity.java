package com.example.registrationlogin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private CustomLoginSuccessHandler successHandler;

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.build();
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/register/**").permitAll()
						.requestMatchers("/welcome").hasRole("USER")
						.requestMatchers("/index").permitAll()
						.requestMatchers("/deleteUser").hasRole("ADMIN")
						.requestMatchers("/deleteUser/delete").hasRole("ADMIN")
						.requestMatchers("/updateUser").hasRole("ADMIN")
						.requestMatchers("/updateUser/update").hasRole("ADMIN")
						.requestMatchers("/users").hasRole("ADMIN"))
				.formLogin(

						form -> form
								.loginPage("/login")
								.loginProcessingUrl("/login")
								.successHandler(successHandler)
								.permitAll())
				.logout(
						logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
								.permitAll());
		return http.build();

	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder());
	}
}
