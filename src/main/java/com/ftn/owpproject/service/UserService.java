package com.ftn.owpproject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ftn.owpproject.model.User;
@Service
public interface UserService {
    User findOne(String emailAddress); 
    User findOne(String emailAddress, String password);
    List<User> findAll(); 
    void save(User user); 
    User update(User user); 
    void delete(Long id);
	User findOneById(Long id); 
//	Map<Long, User> findAllAsMap();
	 User findByJmbg(Long jmbg);
}
