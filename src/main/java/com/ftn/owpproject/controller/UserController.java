package com.ftn.owpproject.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
//import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.ftn.owpproject.model.User;
import com.ftn.owpproject.model.enums.UserRole;
import com.ftn.owpproject.service.UserService;

@Controller
@RequestMapping(value = "/users")
public class UserController extends Exception implements ServletContextAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String USER_KEY = "loggedUser";

    @Autowired
    private ServletContext servletContext;
    private String bURL;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
        bURL = servletContext.getContextPath() + "/";
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    
    @GetMapping
    public ModelAndView index(HttpServletResponse response, HttpSession session) throws IOException {    
        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null ) { //||!(loggedUser.getRole() == UserRole.MANAGER)
            response.sendRedirect(bURL + "users/login");
            return null;
        }
        List<User> users = userService.findAll();

        ModelAndView result = new ModelAndView("users");
        result.addObject("users", users);
        
        // Add loggedUser to the model
        result.addObject("loggedUser", loggedUser);
        
        return result;
    }


    @GetMapping(value="/add")
    public String create(HttpServletResponse response) throws IOException {
        return "addUser";
    }

    @PostMapping(value="/add")
    public ModelAndView create(
        @RequestParam String firstName, 
        @RequestParam String lastName,  
        @RequestParam String password,
        @RequestParam String repeatPassword,
        @RequestParam String emailAddress,
//        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirth,
        @RequestParam String address, 
        @RequestParam String phoneNumber,
        @RequestParam String jmbg,
        HttpServletResponse response) throws IOException {

        User sameEmail = userService.findOne(emailAddress);

        if (sameEmail != null) {
            ModelAndView result = new ModelAndView("addUser");
            result.addObject("message", "User with the same email already exists!");
            return result;
        }
        Long jmbgLong = Long.parseLong(jmbg);
        LocalDateTime registrationDateTime = LocalDateTime.now();
        UserRole buyerRole = UserRole.BUYER;
        User user = new User(firstName, lastName, password, emailAddress, dateOfBirth, address, phoneNumber,registrationDateTime,buyerRole,jmbgLong);
        userService.save(user);
        response.sendRedirect(bURL + "users");
        return null;
    }


    @PostMapping(value="/delete")
    public void delete(@RequestParam Long id, HttpServletResponse response) throws IOException {
        userService.delete(id);
        response.sendRedirect(bURL + "users");
    }

    @GetMapping(value = "/details")
    public ModelAndView userDetails(@RequestParam("id") Long userId, HttpSession session, HttpServletResponse response) throws IOException {
        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return null;
        }
        
        if (!loggedUser.getId().equals(userId)) {
            
            response.sendRedirect(bURL + "error");
            return null;
        }
        
        User user = userService.findOneById(userId);
        boolean isCurrentUser = loggedUser.getId().equals(userId);
        
        ModelAndView result = new ModelAndView("user");
        result.addObject("user", user);

        if(loggedUser.getRole() == UserRole.BUYER && isCurrentUser) {     
            UserRole buyer = UserRole.BUYER;
            result.addObject("buyer", buyer);
        }

        if(loggedUser.getRole() == UserRole.MANAGER && isCurrentUser) {      
            UserRole manager = UserRole.MANAGER;
            result.addObject("manager", manager);
        }

        return result;
    }

    @PostMapping(value="/edit")
    public void edit(@RequestParam Long id, @RequestParam String firstName, @RequestParam String lastName,  @RequestParam String password,
               @RequestParam String emailAddress,@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirth, @RequestParam String address, @RequestParam String phoneNumber,@RequestParam String jmbg, 
               HttpSession session, HttpServletResponse response) throws IOException {       

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        
        if (loggedUser == null) {
            response.sendRedirect(bURL + "users/login");
            return;
        }

        if (!loggedUser.getId().equals(id)) {
            response.sendRedirect(bURL + "error");
            return;
        }

        User user = userService.findOneById(id);

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setEmailAddress(emailAddress);
        user.setDateOfBirth(dateOfBirth);
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        
        Long jmbgLong = Long.parseLong(jmbg);
        
        user.setJmbg(jmbgLong);
        userService.update(user);

        response.sendRedirect(bURL + "users");
    }


    @GetMapping(value="login")
    public ModelAndView getLogin(HttpSession session, HttpServletResponse response) throws IOException {
    	if(session.getAttribute(USER_KEY) != null){
    		response.sendRedirect(bURL);
    	}
    	ModelAndView result = new ModelAndView("login");
        
        return result;
    }

    @PostMapping(value="/login")
    public ModelAndView postLogin(@RequestParam String email, @RequestParam String password, 
            HttpSession session, HttpServletResponse response) throws IOException {
        
        try {
        	
            User user = userService.findOne(email, password);
            if (user == null) {
                throw new Exception("Invalid username or password!");
            }else {           
            
            session.setAttribute(UserController.USER_KEY, user);
            if (user.getRole() == UserRole.MANAGER ) {
            	response.sendRedirect(bURL + "users");
              	return null;
            }else if(user.getRole()==UserRole.BUYER){
            	response.sendRedirect(bURL + "users/details?id=" + user.getId());
            	//moze i na index stranu ako su tamo postavljene sve ponude 
//            	response.sendRedirect(bURL);
            }
            return null;
            }
        } catch (Exception ex) {
            String message = "Log in failed!";

            ModelAndView result = new ModelAndView("login");
            result.addObject("message", message);

            return result;
        }
    }


    @GetMapping(value="/logout")
    public void logout(HttpSession session, HttpServletResponse response) throws IOException {

        session.invalidate();
        
        response.sendRedirect(bURL);
    }
    
    @GetMapping(value="/error")
    public ModelAndView showErrorPage() {
        ModelAndView result = new ModelAndView("error");
        return result;
    }

}
