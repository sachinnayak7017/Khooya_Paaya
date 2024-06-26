package com.spring.service;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import org.springframework.web.bind.annotation.PathVariable;

import com.spring.dao.PostRepo;
import com.spring.entity.PostClass;


import ch.qos.logback.core.model.Model;

@Service
public class PostImpl {
//implements PostService {

	@Autowired
	private PostRepo postrepo;

	
	public PostClass saveUser(PostClass user) {
          PostClass newuser = postrepo.save(user);
        return newuser;
	}


	


	public List<PostClass> getAllEmp() {
		// TODO Auto-generated method stub
		return postrepo.findAll();
	}


	public PostClass getById(int id) {
		// TODO Auto-generated method stub
		Optional<PostClass> e = postrepo.findById(id);
		if (e.isPresent()) {
			return e.get();
		}
		return null;
	}
	
	
	public Boolean deleteposts(@PathVariable("id") int id) {
		PostClass  services= postrepo.findById(id).orElse(null);
		if (!ObjectUtils.isEmpty(services)) {
			postrepo.delete(services);
			return true;
		}
		return false;
	}

	   public List<PostClass> getByUserId(int userId) {
	        return postrepo.findByUserId(userId);
	    }
	
	
}
