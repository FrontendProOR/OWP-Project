package com.ftn.owpproject.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
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
	
	@SuppressWarnings("unused")
	@Autowired
	private TravelCategoryService travelCategoryService;
	
	@Autowired
	private TravelService travelService;
	
	@Value("${image.upload.dir}")
    private String imageUploadDir;
	
	@PostConstruct
	public void init() {	
		bURL = servletContext.getContextPath() + "/";
	}
	
	@Autowired
    public TravelController(ServletContext servletContext, TravelCategoryService travelCategoryService, TravelService travelService) {
        this.servletContext = servletContext;
        this.travelCategoryService = travelCategoryService;
        this.travelService = travelService;
        bURL = servletContext.getContextPath() + "/";
    }
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	} 
	
//	@GetMapping(value = {"/", "/index", "/travels"}) 
//	public ModelAndView index(@RequestParam(required = false) String page, HttpServletResponse response, HttpSession session) throws IOException {
//	    
//		User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
//	    
////	    if (loggedUser.getRole() == UserRole.MANAGER ) { 
////	    	response.sendRedirect(bURL + "travels/addTravel");
////	    	return null;
////	    }else if(loggedUser.getRole() == UserRole.BUYER){
////	    	response.sendRedirect(bURL + "travels/travelOptions");
////	    	return null;
////	    }
//	    
//	    List<Travel> travels = travelService.findAll();
//	    ModelAndView result = null;
//
//	    if ("travels".equals(page)) {
//	        result = new ModelAndView("travels"); 
//	        result.addObject("travels", travels); 
//	    } else {
//	        result = new ModelAndView("index"); 
//	        result.addObject("travelOptions", travels);
//	    }
//	    result.addObject("travelCategory", TravelCategoryEnum.values());
//	    result.addObject("loggedUser", loggedUser);
//
//	    return result;
//	}
	
	@GetMapping(value = {"/", "/index"})
    public ModelAndView indexPage(@RequestParam(required = false) String page, HttpServletResponse response, HttpSession session) throws IOException {
        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        List<Travel> travels = travelService.findAll();
        ModelAndView result = new ModelAndView("index");
        result.addObject("travelOptions", travels);
        result.addObject("travelCategory", TravelCategoryEnum.values());
        result.addObject("loggedUser", loggedUser);

        return result;
    }

    @GetMapping("/travels")
    public ModelAndView travelsPage(@RequestParam(required = false) String page, HttpServletResponse response, HttpSession session) throws IOException {
        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        List<Travel> travels = travelService.findAll();
        ModelAndView result = new ModelAndView("travels");
        result.addObject("travels", travels);
        result.addObject("travelCategory", TravelCategoryEnum.values());
        result.addObject("loggedUser", loggedUser);

        return result;
    }
	
	@GetMapping(value="/travels/add")
	public ModelAndView create(HttpSession session, HttpServletResponse response) {    
	    ModelAndView result = new ModelAndView("addTravel");
	    
	    User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
	    if (loggedUser == null || loggedUser.getRole() == UserRole.BUYER) {
	        try {
	            response.sendRedirect(bURL);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null; 
	    } else if (loggedUser.getRole() != UserRole.MANAGER) {
	        try {
	            response.sendRedirect(bURL + "travels/addTravel");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null; 
	    }
	    
	    result.addObject("categories", TravelCategoryEnum.values());
	    return result;
	}
	
//	@PostMapping(value="/travels/add")
//	public void create(@RequestParam TransportationType transportationType,
//	                   @RequestParam TypeOfAccommodation accommodationType,
//	                   @RequestParam String destinationName,
//	                   @RequestParam String locationImage,
//	                   @RequestParam TravelCategory travelCategory,
//	                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime departureDateTime,
//	                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime returnDateTime,
//	                   @RequestParam double arrangmentPrice,
//	                   @RequestParam int totalSeats,
//	                   @RequestParam int availableSeats,
//	                   HttpServletResponse response,
//	                   HttpSession session) {
//	    User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
//	    if (loggedUser == null || loggedUser.getRole() == UserRole.BUYER) {
//	        try {
//	            response.sendRedirect(bURL);
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	        return;
//	    }
//	    
//	    Travel travel = new Travel(transportationType, accommodationType, destinationName, locationImage, travelCategory, departureDateTime, returnDateTime, arrangmentPrice, totalSeats, availableSeats);
//	    travelService.save(travel);
//	    
//	    try {
//	        response.sendRedirect(bURL);
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    }
//	}
	
	@PostMapping(value="/travels/add")
    public void create(@RequestParam String transportationType,
                       @RequestParam String accommodationType,
                       @RequestParam String destinationName,
                       @RequestParam MultipartFile locationImageFile,
                       @RequestParam String travelCategory,
                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime departureDateTime,
                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime returnDateTime,
                       @RequestParam double arrangmentPrice,
                       @RequestParam int totalSeats,
                       @RequestParam int availableSeats,
                       HttpServletResponse response,
                       HttpSession session) {
        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null || loggedUser.getRole() == UserRole.BUYER) {
            try {
                response.sendRedirect(bURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        TransportationType transportationTypeMain = TransportationType.valueOf(transportationType.toUpperCase());
        TypeOfAccommodation accommodationTypeMain = TypeOfAccommodation.valueOf(accommodationType.toUpperCase());
        
        TravelCategoryEnum travelCategoryEnum = TravelCategoryEnum.valueOf(travelCategory.toUpperCase());
        TravelCategory travelCategoryMain = new TravelCategory();
        travelCategoryMain.setCategoryName(travelCategoryEnum);

        String locationImageUrl = null;
        if (locationImageFile != null && !locationImageFile.isEmpty()) {
            try {
                String fileName = locationImageFile.getOriginalFilename();
                
                String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
                
                Path uploadPath = Paths.get(imageUploadDir);
                
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                Path filePath = uploadPath.resolve(uniqueFileName);
                Files.copy(locationImageFile.getInputStream(), filePath);
                
                locationImageUrl = "/images/" + uniqueFileName;
            } catch (IOException e) {
                e.printStackTrace();
                
            }
        }

        Travel travel = new Travel(transportationTypeMain, accommodationTypeMain, destinationName, locationImageUrl, travelCategoryMain, departureDateTime, returnDateTime, arrangmentPrice, totalSeats, availableSeats);
        travelService.save(travel);

        try {
            response.sendRedirect(bURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@PostMapping(value = "/travels/edit")
	public void edit(
	        @RequestParam Long id, 
	        @RequestParam TransportationType transportationType, 
	        @RequestParam TypeOfAccommodation accommodationType, 
	        @RequestParam String destinationName, 
	        @RequestParam String locationImage, 
	        @RequestParam String travelCategory, 
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime departureDateTime, 
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime returnDateTime, 
	        @RequestParam double arrangmentPrice, 
	        @RequestParam int totalSeats, 
	        @RequestParam int availableSeats, 
	        HttpServletResponse response, 
	        HttpSession session) throws IOException {

	    User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
	    
	    if (loggedUser == null || loggedUser.getRole() != UserRole.MANAGER) {
	        response.sendRedirect(bURL);
	        return;
	    }
	    
	    Travel travel = travelService.findOne(id);
	    
	    if (travel == null) {
	        response.sendRedirect(bURL);
	        return;
	    }
	    
	    travel.setTransportationType(transportationType);
	    travel.setAccommodationType(accommodationType);
	    travel.setDestinationName(destinationName);
	    travel.setLocationImage(locationImage);

//	    TravelCategory travelCategoryMain = travelCategoryService.findOne(travelCategoryService.getIdByName(travelCategory));
	    TravelCategoryEnum categoryEnum = TravelCategoryEnum.valueOf(travelCategory);
	    TravelCategory travelCategoryMain = new TravelCategory();
	    travelCategoryMain.setCategoryName(categoryEnum);
	    
	    travel.setTravelCategory(travelCategoryMain);
	    travel.setDepartureDateTime(departureDateTime);
	    travel.setReturnDateTime(returnDateTime);
	    int numberOfNights = (int) ChronoUnit.DAYS.between(departureDateTime, returnDateTime);
	    travel.setNumberOfNights(numberOfNights);
	    travel.setArrangmentPrice(arrangmentPrice);
	    travel.setTotalSeats(totalSeats);
	    travel.setAvailableSeats(availableSeats);
	    
	    travelService.update(travel);
	   
	    response.sendRedirect(bURL + "travels");
	}

	
	@PostMapping(value = "/travels/showEditForm")
	public String showEditForm(@RequestParam Long id, Model model) {
	    Travel travel = travelService.findOne(id);
	    model.addAttribute("travel", travel);
	    return "editTravel";
	}

	@GetMapping(value = "/travels/addTravel")
	public ModelAndView showAddTravelPage(HttpSession session, HttpServletResponse response) throws IOException {
	    ModelAndView modelAndView = new ModelAndView();

	    User loggedUser = (User) session.getAttribute(USER_KEY);
	    if (loggedUser == null || loggedUser.getRole() != UserRole.MANAGER) {
	        response.sendRedirect(bURL); 
	        return null;
	    }

	    modelAndView.setViewName("addTravel");
	    modelAndView.addObject("categories", TravelCategoryEnum.values());

	    return modelAndView;
	}

	
	@GetMapping(value="travels/details")
	public ModelAndView details(@RequestParam Long id,HttpServletResponse response, HttpSession session) {
		Travel travel = travelService.findOne(id);
		ModelAndView result = new ModelAndView("travel");
		result.addObject("travel", travel);
		
		return result;
	}
	
	@PostMapping(value="travels/delete")
    public void delete(@RequestParam Long id, HttpServletResponse response) throws IOException {
        travelService.delete(id);
        response.sendRedirect(bURL + "travels");
    }
}
