package com.spring.service;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.spring.dao.UserRepository;
import com.spring.entity.User;
import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImp implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	public BCryptPasswordEncoder passwordEncoder;

	public List<User> getAllUsersDetails() {
		return userRepo.findAll();

	}

	public User getUserById(int id) {
		Optional<User> user = userRepo.findById(id);
		if (user.isPresent()) {
			return user.get();
		}
		return null;
	}

	public void deleteUser(Integer id) {
		userRepo.deleteById(id);

	}

	@Override
	public User saveUser(User user, String url) {

		String password = passwordEncoder.encode(user.getPassword());
		user.setPassword(password);
		user.setRole("ROLE_USER");

		user.setEnable(true);

		User newuser = userRepo.save(user);

		if (newuser != null) {
			sendEmail(newuser, url);
		}

		return newuser;
	}

	@Override
	public void removeSessionMessage() {

		HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
				.getSession();

		session.removeAttribute("msg");

	}

	@Override
	public void sendEmail(User user, String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean verifyAccount(String verificationCode) {
		// TODO Auto-generated method stub
		return false;
	}

}
