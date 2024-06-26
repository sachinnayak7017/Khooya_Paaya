package com.spring.dao;

import java.io.Serializable;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Serializable>{
	 public User findByEmail(String email);

}
