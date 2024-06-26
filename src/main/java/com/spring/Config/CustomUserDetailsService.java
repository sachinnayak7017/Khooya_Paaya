package com.spring.Config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.spring.dao.UserRepository;
import com.spring.entity.User;

@Component
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		
		User user = userRepo.findByEmail(email);
		
		if(user==null)
		{
			throw new UsernameNotFoundException("user name not found");
		}
		else {
			return new CustomUser(user);	
		}
	}

}
