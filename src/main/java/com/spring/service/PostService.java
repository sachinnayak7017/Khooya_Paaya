package com.spring.service;


import com.spring.entity.PostClass;


public interface PostService {
	
	public void saveUser(PostClass postClass);

	public Boolean deletepost(int id);

}
