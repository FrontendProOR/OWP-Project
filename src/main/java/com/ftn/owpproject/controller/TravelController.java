package com.ftn.owpproject.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.ftn.owpproject.model.Travel;
import com.ftn.owpproject.model.TravelCategory;
import com.ftn.owpproject.model.User;
import com.ftn.owpproject.model.enums.TransportationType;
import com.ftn.owpproject.model.enums.TravelCategoryEnum;
import com.ftn.owpproject.model.enums.TypeOfAccommodation;
import com.ftn.owpproject.model.enums.UserRole;
import com.ftn.owpproject.service.TravelService;

@Controller
@RequestMapping(value = "/travels")
public class TravelController implements ServletContextAware{
	public static final String USER_KEY = "loggedUser";
	public static final String TRAVELS_KEY = "travels";
	
	@Autowired
	private ServletContext servletContext;
	private  String bURL; 
	
	@Autowired
	private TravelService travelService;
	
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
//        if (loggedUser.getRole() == UserRole.MANAGER ) { //||!(loggedUser.getRole() == UserRole.MANAGER)
//            response.sendRedirect(bURL + "travels/addTravel");
//            return null;
//        }else{
//        	response.sendRedirect(bURL + "travels/makeReservation");
//		}
		
		List<Travel> travels = travelService.findAll();
		
		ModelAndView result = new ModelAndView("travels");
		result.addObject("travels", travels);
		result.addObject("travelCategory", TravelCategoryEnum.values());
		result.addObject("loggedUser", loggedUser);
		
		return result;
	}
	
	@GetMapping(value="/add")
	public ModelAndView create(HttpSession session, HttpServletResponse response) {	
		ModelAndView result = new ModelAndView("addTravel");
		
		result.addObject("categories", TravelCategoryEnum.values());
		
		return result;
	}
	
	@PostMapping(value="/add")
	public void create( @RequestParam TransportationType transportationType,@RequestParam TypeOfAccommodation accommodationType,@RequestParam String destinationName,
			@RequestParam	String locationImage,@RequestParam TravelCategory travelCategory,@RequestParam LocalDateTime departureDateTime,@RequestParam LocalDateTime returnDateTime,
			@RequestParam double arrangmentPrice,@RequestParam int totalSeats,@RequestParam int availableSeats, HttpServletResponse response,HttpSession session) throws IOException {		
	User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
    if (loggedUser.getRole() == UserRole.MANAGER ) { //||!(loggedUser.getRole() == UserRole.MANAGER)
    	response.sendRedirect(bURL + "travels/addTravel");
    }else if(loggedUser.getRole() == UserRole.BUYER){
      	response.sendRedirect("/");
	}else {
		response.sendRedirect("/");
	}
      
		int numberOfNights = (int) ChronoUnit.DAYS.between(departureDateTime, returnDateTime);		
		
		Travel travel = new Travel(transportationType,accommodationType,destinationName,locationImage,travelCategory,departureDateTime,returnDateTime,numberOfNights,arrangmentPrice,totalSeats,availableSeats);
		travelService.save(travel);
		response.sendRedirect(bURL);
	}
	
	@PostMapping(value="/edit")
	public void edit(@RequestParam Long id, @RequestParam TransportationType transportationType, @RequestParam TypeOfAccommodation accommodationType, 
	                 @RequestParam String destinationName, @RequestParam String locationImage, @RequestParam TravelCategory travelCategory, 
	                 @RequestParam LocalDateTime departureDateTime, @RequestParam LocalDateTime returnDateTime, @RequestParam double arrangmentPrice, 
	                 @RequestParam int totalSeats, @RequestParam int availableSeats, HttpServletResponse response, HttpSession session) throws IOException {
	    User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
	    
	    if (loggedUser.getRole() != UserRole.MANAGER) {
	        response.sendRedirect(bURL); // Redirect unauthorized users
	        return;
	    }
	    
	    // Get the travel by ID
	    Travel travel = travelService.findOne(id);
	    
	    if (travel == null) {
	        response.sendRedirect(bURL); // Redirect if travel is not found
	        return;
	    }
	    
	    // Update the travel object with new data
	    travel.setTransportationType(transportationType);
	    travel.setAccommodationType(accommodationType);
	    travel.setDestinationName(destinationName);
	    travel.setLocationImage(locationImage);
	    travel.setTravelCategory(travelCategory);
	    travel.setDepartureDateTime(departureDateTime);
	    travel.setReturnDateTime(returnDateTime);
	    int numberOfNights = (int) ChronoUnit.DAYS.between(departureDateTime, returnDateTime);
	    travel.setNumberOfNights(numberOfNights);
	    travel.setArrangmentPrice(arrangmentPrice);
	    travel.setTotalSeats(totalSeats);
	    travel.setAvailableSeats(availableSeats);
	    
	    // Save the updated travel object
	    travelService.save(travel);
	    
	    response.sendRedirect(bURL + "travels/details?id=" + id); // Redirect to the travel details page
	}

	
	@GetMapping(value="travels/details")
	public ModelAndView details(@RequestParam Long id) {
		Travel travel = travelService.findOne(id);
		ModelAndView result = new ModelAndView("travel");
		result.addObject("travel", travel);
		
		return result;
	}
}
