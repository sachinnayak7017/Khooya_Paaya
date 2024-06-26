package com.spring.entity;


import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name="post")
public class PostClass {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int user_id;
	private String name;
	private String contactno;
	private String time;
	private String description;
	 @Lob 
	    @Column(name = "image_data", length = 1048576)
	 private byte[] imageData;
	 @Lob 
	    @Column(name = "user_data", length = 1048576)
	 private byte[] userData;
	 
	 @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
		
	 
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContactno() {
		return contactno;
	}
	public void setContactno(String contactno) {
		this.contactno = contactno;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public byte[] getImageData() {
		return imageData;
	}
	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
	public byte[] getUserData() {
		return userData;
	}
	public void setUserData(byte[] userData) {
		this.userData = userData;
	}
	@Override
	public String toString() {
		return "PostClass [id=" + id + ", user_id=" + user_id + ", name=" + name + ", contactno=" + contactno
				+ ", time=" + time + ", description=" + description + ", imageData=" + Arrays.toString(imageData)
				+ ", userData=" + Arrays.toString(userData) + "]";
	}
	public PostClass() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	 
	
}
	 
	 
