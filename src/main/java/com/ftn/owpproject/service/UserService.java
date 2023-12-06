package com.ftn.owpproject.service;

import java.util.List;
import java.util.Map;

import com.ftn.owpproject.model.User;

public interface UserService {
    User findOneById(int id);
    User findOne(String emailAddress); 
    User findOne(String emailAddress, String password);
    List<User> findAll(); 
    void save(User user); 
    User update(User user); 
    void delete(Long id);
	User findOneById(Long id); 
	Map<Long, User> findAllAsMap();
}
