package com.spring.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	public CustomAuthSuccessHandler successHandler;
	
	
	@Bean
	@Primary
	public  BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
	
	@Bean
	public UserDetailsService getDetailsService() {
		
		return new CustomUserDetailsService();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(getDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		/*
		 * http.csrf().disable() .authorizeHttpRequests()
		 * .requestMatchers("/","/register","/signin","/saveUser").permitAll()
		 * .requestMatchers("/user/**") .authenticated()
		 * .and().formLogin().loginPage("/signin") .loginProcessingUrl("/userlogin")
		 * .defaultSuccessUrl("/user/profile") .permitAll();
		 */
		
	//  Roll based mapping
		
		http.csrf().disable()
		.authorizeHttpRequests()
     	.requestMatchers("/user/**").hasRole("USER")
		.requestMatchers("/admin/**").hasRole("ADMIN")
		.requestMatchers("/**").permitAll().and()
		.formLogin().loginPage("/").loginProcessingUrl("/userlogin")
		.successHandler(successHandler)
		.permitAll();
			
		return http.build();
		
	}

}
