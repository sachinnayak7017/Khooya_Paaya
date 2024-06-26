package com.spring.controller;



import java.security.Principal;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.dao.UserRepository;
import com.spring.entity.User;
import com.spring.service.UserServiceImp;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserServiceImp userService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			User user = userRepo.findByEmail(email);
			m.addAttribute("user", user);
		}

	}

	@GetMapping("/home")
	public String home() {
		return "admin_dashboard";
	}

	@GetMapping("/fileupoad")
	public String formate() {
		return "admin/fileupoad";
	}

	@GetMapping("/admin_profile")
	public String adminProfile() {
		return "admin/admin_profile";
	}

	/*
	 * @GetMapping("/admin_dashboard") public String userDashboard(Model m) {
	 * m.addAttribute("title", "Home"); m.addAttribute("stpinfo", new
	 * Students_Details());
	 * 
	 * return "admin/admin_dashboard"; }
	 */
	@GetMapping("/view_users")
	public String viewUser(Model m) {
		List<User> admin = userService.getAllUsersDetails();

		m.addAttribute("title", "View Users");
		m.addAttribute("admin", admin);
		return "admin/view_users";
	}

////show image in html table
	@GetMapping("/view_users/{id}")
	public ResponseEntity<byte[]> getEmployeeImage(@PathVariable int id) {
		User user = userService.getUserById(id);

		if (user != null && user.getImageData() != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG); // Adjust content type as needed

			return new ResponseEntity<>(user.getImageData(), headers, HttpStatus.OK);
		}

		// Return a placeholder image or an error image if needed
		return ResponseEntity.notFound().build();
	}

	@RequestMapping("/show_user/{id}")
	public String openShowUser(@PathVariable("id") Integer id, Model m) {
		m.addAttribute("title", "Show User");
		m.addAttribute("user", new User());

		Optional<User> userOpl = this.userRepo.findById(id);
		User user = userOpl.get();

		m.addAttribute("user", user);

		return "admin/show_user";
	}

	@GetMapping("/delete_user/{id}")
	public String deleteUser(@PathVariable("id") Integer id, HttpSession session) {
		userService.deleteUser(id);
		session.setAttribute("msg", "One User deleted successsfully !");
		return "admin/admin_dashboard";
	}

	
	@GetMapping("/change_admin_password")
	public String adminPassword(Model m, HttpSession session) {
		m.addAttribute("title", "Change Admin Password");

		return "admin/change_admin_password";
	}

	@PostMapping("/change-user-password")
	public String changeUserPassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, Principal p, HttpSession session) {

		session.setAttribute("msg", "Your password have been changed successfully !");

		System.out.println("OLD PASSWORD " + oldPassword);
		System.out.println("NEW PASSWORD " + newPassword);

		String userName = p.getName();
		User currentUser = this.userRepo.findByEmail(userName);
		System.out.println(currentUser.getPassword());

		if (this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepo.save(currentUser);

		} else {

		}

		return "admin/admin_dashboard";
	}
}
