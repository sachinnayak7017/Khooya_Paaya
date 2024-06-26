package com.spring.controller;

import java.io.IOException;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.spring.dao.UserRepository;
import com.spring.entity.User;
import com.spring.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepo;


	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			User user = userRepo.findByEmail(email);
			m.addAttribute("user", user);
		}

	}

	@GetMapping("/")
	public String mainIndex() {

		return "index";

	}

	

	@GetMapping("/login")
	public String userSignin() {

		return "login";

	}

	@GetMapping("/register")
	public String userRegister() {

		return "register";

	}

	

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute User user, HttpSession session, HttpServletRequest request,@RequestParam("file") MultipartFile file) {

		String url = request.getRequestURI().toString();

		url = url.replace(request.getServletPath(), "");
		try {
			if (!file.isEmpty()) {

				long MAX_FILE_SIZE = 10 * 1024 * 1024;
				if (file.getSize() <= MAX_FILE_SIZE) {

					byte[] imageData = file.getBytes();
					user.setImageData(imageData);
				}
			}
		User u = userService.saveUser(user, url);

		if (u != null) {
			session.setAttribute("msg", "Register successfully!"); //
			System.out.println("save success!");
		} else {
			session.setAttribute("msg", "Something wrong on server!");
			System.out.println("error!");
		}
	} catch (IOException e1) {
		// Handle the exception
	}
		return "redirect:/register";
	}

	
	
}
