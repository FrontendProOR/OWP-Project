package com.ftn.owpproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.owpproject.dao.UserDAO;
import com.ftn.owpproject.model.User;
import com.ftn.owpproject.service.UserService;

import java.util.List;

@Service
public class DatabaseUserServiceImpl implements UserService {
	
	@Autowired
	private UserDAO userDAO;

	@Override
	public User findOneById(Long id) {
	    return userDAO.findOne(id);
	}

	@Override
	public User findOne(String email) {
	    return userDAO.findOne(email);
	}

	@Override
	public User findOne(String email, String password) {
	    return userDAO.findOne(email, password);
	}

	@Override
	public List<User> findAll() {
	    return userDAO.findAll();
	}

	@Override
	public void save(User user) {
	    userDAO.save(user);
//	    return user;
	}

	@Override
	public User update(User user) {
	    userDAO.update(user);
	    return user;
	}

	@Override
	public void delete(Long id) {
//	    User user = userDAO.findOne(id);
	    userDAO.delete(id);
//	    return user.getId();
	}


//	@Override
//	public Map<Long, User> findAllAsMap() {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public User findOneById(int id) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	
}
