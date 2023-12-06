package com.ftn.owpproject.service;

import java.util.List;

import com.ftn.owpproject.model.User;

public interface UserService {
    User findOneById(int id);
    User findOne(String emailAddress); 
    User findOne(String emailAddress, String password);
    List<User> findAll(); 
    User save(User user); 
    User update(User user); 
    User delete(Long id);
	User findOneById(Long id); 
}
