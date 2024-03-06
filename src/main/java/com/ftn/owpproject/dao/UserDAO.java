package com.ftn.owpproject.dao;

import java.util.List;

import com.ftn.owpproject.model.User;

public interface UserDAO {
	
	public User findOne(Long id);
	
	public User findOne(String email); 
	
	public User findOne(String email, String password);

	public List<User> findAll();

	public int save(User user);

	public int update(User user);

	public int delete(Long id);
	
}
