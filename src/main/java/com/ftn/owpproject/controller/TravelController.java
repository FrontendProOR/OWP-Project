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
import org.springframework.format.annotation.DateTimeFormat;
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
import com.ftn.owpproject.service.TravelCategoryService;
import com.ftn.owpproject.service.TravelService;

@Controller
@RequestMapping(value = "/")
public class TravelController implements ServletContextAware{
	public static final String USER_KEY = "loggedUser";
	public static final String TRAVELS_KEY = "travels";
	
	@Autowired
	private ServletContext servletContext;
	private  String bURL; 
	
	@Autowired
	private TravelCategoryService travelCategoryService;
	
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
	
	@GetMapping(value = {"/", "/index", "/travels"}) // Define the URL mapping for the index page
	public ModelAndView index(@RequestParam(required = false) String page, HttpServletResponse response, HttpSession session) throws IOException {
	    
		User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
	    
	    if (loggedUser.getRole() == UserRole.MANAGER ) { //||!(loggedUser.getRole() == UserRole.MANAGER)
	    	response.sendRedirect(bURL + "travels/addTravel");
	    	return null;
	    }else if(loggedUser.getRole() == UserRole.BUYER){
	    	response.sendRedirect(bURL + "travels/travelOptions");
	    	return null;
	    }
	    
	    List<Travel> travels = travelService.findAll();
	    ModelAndView result = null;

	    if ("travels".equals(page)) {
	        result = new ModelAndView("travels"); // Assuming your HTML file for travels is named "travels.html"
	        result.addObject("travels", travels); // Add the travel options to the model
	    } else {
	        result = new ModelAndView("index"); // Assuming your HTML file for index is named "index.html"
	        result.addObject("travelOptions", travels); // Add the travel options to the model
	    }
	    result.addObject("travelCategory", TravelCategoryEnum.values());        
	    // Add any additional data you need for both pages here
	    result.addObject("loggedUser", loggedUser);

	    return result;
	}

	
//	@GetMapping(value="travels/add")
//	public ModelAndView create(HttpSession session, HttpServletResponse response) {	
//		ModelAndView result = new ModelAndView("addTravel");
//		
//		result.addObject("categories", TravelCategoryEnum.values());
//		
//		return result;
//	}
//	
//	@PostMapping(value="travels/add")
//	public void create( @RequestParam TransportationType transportationType,@RequestParam TypeOfAccommodation accommodationType,@RequestParam String destinationName,
//			@RequestParam	String locationImage,@RequestParam TravelCategory travelCategory,@RequestParam LocalDateTime departureDateTime,@RequestParam LocalDateTime returnDateTime,
//			@RequestParam double arrangmentPrice,@RequestParam int totalSeats,@RequestParam int availableSeats, HttpServletResponse response,HttpSession session) throws IOException {		
//	User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
//    if (loggedUser.getRole() == UserRole.MANAGER ) { //||!(loggedUser.getRole() == UserRole.MANAGER)
//    	response.sendRedirect(bURL + "travels/addTravel");
//    }else if(loggedUser.getRole() == UserRole.BUYER){
//      	response.sendRedirect("/");
//	}else {
//		response.sendRedirect("/");
//	}
//      
//		int numberOfNights = (int) ChronoUnit.DAYS.between(departureDateTime, returnDateTime);		
//		
//		Travel travel = new Travel(transportationType,accommodationType,destinationName,locationImage,travelCategory,departureDateTime,returnDateTime,numberOfNights,arrangmentPrice,totalSeats,availableSeats);
//		travelService.save(travel);
//		response.sendRedirect(bURL);
//	}
	
	@GetMapping(value="/travels/add")
	public ModelAndView create(HttpSession session, HttpServletResponse response) {    
	    ModelAndView result = new ModelAndView("addTravel");
	    
	    User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
	    if (loggedUser == null || loggedUser.getRole() == UserRole.BUYER) {
	        try {
	            response.sendRedirect(bURL);
	        } catch (IOException e) {
	            // Handle IOException
	            e.printStackTrace();
	        }
	        return null; // Returning null to end the method execution
	    } else if (loggedUser.getRole() != UserRole.MANAGER) {
	        try {
	            response.sendRedirect(bURL + "travels/addTravel");
	        } catch (IOException e) {
	            // Handle IOException
	            e.printStackTrace();
	        }
	        return null; // Returning null to end the method execution
	    }
	    
	    result.addObject("categories", TravelCategoryEnum.values());
	    return result;
	}
	//OVA DVA NISU GOTOVI a za travelcategory nije db gotov za njega proveriti da li treba dodavati stranice ili samo vezu sa bazom
	@PostMapping(value="/travels/add")
	public void create(@RequestParam TransportationType transportationType,
	                   @RequestParam TypeOfAccommodation accommodationType,
	                   @RequestParam String destinationName,
	                   @RequestParam String locationImage,
	                   @RequestParam String travelCategoryName,
	                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime departureDateTime,
	                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime returnDateTime,
	                   @RequestParam double arrangementPrice,
	                   @RequestParam int totalSeats,
	                   @RequestParam int availableSeats,
	                   HttpServletResponse response,
	                   HttpSession session) {
	    User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
	    if (loggedUser == null || loggedUser.getRole() == UserRole.BUYER) {
	        try {
	            response.sendRedirect(bURL);
	        } catch (IOException e) {
	            // Handle IOException
	            e.printStackTrace();
	        }
	        return; // Returning to end the method execution
	    }
//	    TravelCategoryEnum travelCategoryEnum = TravelCategoryEnum.valueOf(travelCategoryName);
	    
//	    List<TravelCategory> travelCategories = travelCategoryService.findAll();
//	    TravelCategory travelCategory = new TravelCategory(travelCategoryEnum,travelCategoryService.findByNameOfTravelCategory());
//	    Travel travel = new Travel(transportationType, accommodationType, destinationName, locationImage, travelCategory, departureDateTime, returnDateTime, arrangementPrice, totalSeats, availableSeats);
//	    travelService.save(travel);
	    
	    try {
	        response.sendRedirect(bURL);
	    } catch (IOException e) {
	        // Handle IOException
	        e.printStackTrace();
	    }
	}

	
	@PostMapping(value="travels/edit")
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
	    
	    travelService.save(travel);
	    
	    response.sendRedirect(bURL + "travels/details?id=" + id); // Redirect to the travel details page
	}

	
	@GetMapping(value="travels/details")
	public ModelAndView details(@RequestParam Long id,HttpServletResponse response, HttpSession session) {
		Travel travel = travelService.findOne(id);
		ModelAndView result = new ModelAndView("travel");
		result.addObject("travel", travel);
		
		return result;
	}
}
