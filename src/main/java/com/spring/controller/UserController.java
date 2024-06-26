

package com.spring.controller;

import java.io.IOException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
import org.springframework.web.multipart.MultipartFile;

import com.spring.dao.PostRepo;
import com.spring.dao.UserRepository;
import com.spring.entity.PostClass;
import com.spring.entity.User;
import com.spring.service.PostImpl;
import com.spring.service.UserServiceImp;


import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepo;


	@Autowired
	private UserServiceImp userService;

	@Autowired
	private PostImpl postservice;
	
	@Autowired
	private PostRepo postRepo;
	
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
	
	@GetMapping("/view_users/{id}")
	public ResponseEntity<byte[]> getEmployeeImage(@PathVariable int id) {
		  PostClass user = postservice.getById(id);
		if (user != null && user.getImageData() != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG); // Adjust content type as needed
			return new ResponseEntity<>(user.getImageData(), headers, HttpStatus.OK);
		}
		return ResponseEntity.notFound().build();
	}
	@GetMapping("/show_users/{id}")
	public ResponseEntity<byte[]> getuserImage(@PathVariable int id) {
		  User user = userService.getUserById(id);
		if (user != null && user.getImageData() != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG); // Adjust content type as needed
			return new ResponseEntity<>(user.getImageData(), headers, HttpStatus.OK);
		}
		return ResponseEntity.notFound().build();
	}
		

////show image in html table
	@GetMapping("/view_user/{id}")
	public ResponseEntity<byte[]> getUserProfileImage(@PathVariable int id) {
		  PostClass user = postservice.getById(id);

		if (user != null && user.getUserData() != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG); // Adjust content type as needed
			return new ResponseEntity<>(user.getUserData(), headers, HttpStatus.OK);
		}
		return ResponseEntity.notFound().build();
	}

	
	
	
	@GetMapping("/home")
	public String home() {
		return "user_dashboard";
	}



	@GetMapping("/user_dashboard")
	public String userDashboard(Model m) {
     List<PostClass> posts = postservice.getAllEmp();
		m.addAttribute("posts", posts);

		return "user/user_dashboard";
	}
	
	@GetMapping("/delete_post/{id}")
	public String deleteProduct(@PathVariable int id, HttpSession session) {
		Boolean deleteProduct = postservice.deleteposts(id);
		if (deleteProduct) {
			session.setAttribute("succMsg", "Product delete success" + deleteProduct);
			System.out.println(deleteProduct);
		} else {
			session.setAttribute("errorMsg", "Something wrong on server");
		}
		return "redirect:/";
	}
	 @GetMapping("/profile/{id}")
	    public String findById(@PathVariable("id") int id, Model m) {
	        List<PostClass> posts = postservice.getByUserId(id);
	        m.addAttribute("userposts", posts);
	        return "user/profile";
	    }
	
	
	@GetMapping("/post/{id}")
	public String homeIndex(@PathVariable int id, Model m) {
	  
		User postClass= userService.getUserById(id);

		
	        LocalDateTime now = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        String formattedNow = now.format(formatter);
	        
	        // Add the user and current date and time to the model
	        m.addAttribute("user", postClass);
	        m.addAttribute("currentDateTime", formattedNow);
	   
	    
	    return "user/post";
	}
	

	
	
	// for image
//	ALTER TABLE EMP_SYSTEM
//	MODIFY COLUMN image_data LONGBLOB; --

		@GetMapping("/change_user_password")
	public String adminPassword(Model m, HttpSession session) {
		m.addAttribute("title", "Change User Password");

		return "user/change_user_password";
	}

	@PostMapping("/change-user-password")
	public String changeUserPassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, Principal p, HttpSession session) {

		session.setAttribute("msg", "Your password have been changed successfully !");

		String userName = p.getName();
		User currentUser = this.userRepo.findByEmail(userName);
		System.out.println(currentUser.getPassword());

		if (this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepo.save(currentUser);

		} else {

		}

		return "user/user_dashboard";
	}
	
	@PostMapping("/savePost")
	public String savePost(@ModelAttribute PostClass postClass,@RequestParam("user_id") int userId, HttpSession session,  @RequestParam("file") MultipartFile file) {
	    try {
	      
	        User user = userService.getUserById(userId);
	     
	        if (user != null) {
	            postClass.setUserData(user.getImageData());
	        }
	        if (!file.isEmpty()) {
	            long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB
	            if (file.getSize() <= MAX_FILE_SIZE) {
	                byte[] imageData = file.getBytes();
	                postClass.setImageData(imageData);
	            }
	        }
	        PostClass savedPost = postservice.saveUser(postClass);
	        if (savedPost != null) {
	            session.setAttribute("msg", "Post uploaded successfully!");
	        } else {
	            session.setAttribute("msg", "Something went wrong on the server!");
	        }
	    } catch (IOException e) {
	        session.setAttribute("msg", "Failed to upload post!");
	        e.printStackTrace(); // Log the exception for debugging
	    }

	    return "redirect:/";
	}
	

	
	/* Search coding */

	/*
	 * @GetMapping("/filterper") public String FilterStudentDetails(Model m, String
	 * keyword) { List<Students_Details> stinfo = stservice.getAllStudentsDetails();
	 * 
	 * if (keyword != null) { m.addAttribute("stinfo",
	 * stservice.findByKeyword(keyword)); } else { m.addAttribute("stinfo", stinfo);
	 * }
	 * 
	 * return "user/view_students_details"; }
	 */
	

	
	
	

	

	

}
