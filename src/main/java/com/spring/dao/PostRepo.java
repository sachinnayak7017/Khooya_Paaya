package com.spring.dao;

import java.io.Serializable;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.entity.PostClass;


@Repository
public interface PostRepo extends JpaRepository<PostClass, Serializable>{

	 @Query("SELECT p FROM PostClass p WHERE p.user_id = :userId")
	    List<PostClass> findByUserId(@Param("userId") int userId);
	 
}
