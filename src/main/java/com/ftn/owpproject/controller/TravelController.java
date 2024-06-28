package com.ftn.owpproject.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
public class TravelController implements ServletContextAware {
    public static final String USER_KEY = "loggedUser";
    public static final String TRAVELS_KEY = "travels";
    private static final int NUM_TRAVELS_TO_DISPLAY = 3;
    
    @Autowired
    private ServletContext servletContext;
    private String bURL;

    @Autowired
    private TravelCategoryService travelCategoryService;

    @Autowired
    private TravelService travelService;

    @Value("${image.upload.dir}")
    private String imageUploadDir;

    private static final String UPLOAD_DIRECTORY = "src/main/resources/static/images/";

    @PostConstruct
    public void init() {
        bURL = servletContext.getContextPath() + "/";
        travelService.updateAllTravelPrices();  
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

    @GetMapping(value = {"/", "/index"})
    public ModelAndView indexPage(
            HttpServletResponse response, HttpSession session,
            @RequestParam(required = false) String transportation,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minNights,
            @RequestParam(required = false) Integer maxNights,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "6") int limit) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        String userRole = null;
        if (loggedUser != null) {
            userRole = loggedUser.getRole().toString();            
        }

        travelService.updateAllTravelPrices();

        List<Travel> travels = travelService.findAll();

        if (userRole == "BUYER" || loggedUser == null) {
            travels = travels.stream()
                .filter(travel -> travel.getAvailableSeats() > 0)
                .filter(travel -> ChronoUnit.HOURS.between(LocalDateTime.now(), travel.getDepartureDateTime()) > 48)
                .collect(Collectors.toList());
        } else if (userRole == "MANAGER") {
            travels = travels.stream()
                .filter(travel -> ChronoUnit.HOURS.between(LocalDateTime.now(), travel.getDepartureDateTime()) > 48)
                .collect(Collectors.toList());
        }

        travels = searchAndFilterTravels(travels, transportation, destination, category, minPrice, maxPrice, minNights, maxNights, sort);

        List<Travel> promotionalTravels = getPromotionalTravels(travels);
        List<Travel> seasonalTravels = getSeasonalTravels(travels);
        List<Travel> allTravels = travels.stream()
            .filter(travel -> !promotionalTravels.contains(travel) && !seasonalTravels.contains(travel))
            .limit(limit)
            .collect(Collectors.toList());

        ModelAndView result = new ModelAndView("index");
        result.addObject("promotionalTravels", promotionalTravels);
        result.addObject("seasonalTravels", seasonalTravels);
        result.addObject("allTravels", allTravels);
        result.addObject("travelCategory", TravelCategoryEnum.values());
        result.addObject("loggedUser", loggedUser);
        result.addObject("transportation", transportation);
        result.addObject("destination", destination);
        result.addObject("category", category);
        result.addObject("minPrice", minPrice);
        result.addObject("maxPrice", maxPrice);
        result.addObject("minNights", minNights);
        result.addObject("maxNights", maxNights);
        result.addObject("sort", sort);

        return result;
    }

    private List<Travel> searchAndFilterTravels(
            List<Travel> travels,
            String transportation,
            String destination,
            String category,
            Double minPrice,
            Double maxPrice,
            Integer minNights,
            Integer maxNights,
            String sort) {

        
        if (transportation != null && !transportation.isEmpty()) {
            travels = travels.stream()
                    .filter(travel -> travel.getTransportationType().toString().equalsIgnoreCase(transportation))
                    .collect(Collectors.toList());
        }
        if (destination != null && !destination.isEmpty()) {
            travels = travels.stream()
                    .filter(travel -> travel.getDestinationName().toLowerCase().startsWith(destination.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (category != null && !category.isEmpty()) {
            travels = travels.stream()
                    .filter(travel -> travel.getTravelCategory().getCategoryName().toString().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }
        if (minPrice != null) {
            travels = travels.stream()
                    .filter(travel -> travel.getArrangmentPrice() >= minPrice)
                    .collect(Collectors.toList());
        }
        if (maxPrice != null) {
            travels = travels.stream()
                    .filter(travel -> travel.getArrangmentPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }
        if (minNights != null) {
            travels = travels.stream()
                    .filter(travel -> travel.getNumberOfNights() >= minNights)
                    .collect(Collectors.toList());
        }
        if (maxNights != null) {
            travels = travels.stream()
                    .filter(travel -> travel.getNumberOfNights() <= maxNights)
                    .collect(Collectors.toList());
        }

        
        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "destination":
                    travels = travels.stream()
                            .sorted(Comparator.comparing(Travel::getDestinationName))
                            .collect(Collectors.toList());
                    break;
                case "price":
                    travels = travels.stream()
                            .sorted(Comparator.comparing(Travel::getArrangmentPrice))
                            .collect(Collectors.toList());
                    break;
                case "nights":
                    travels = travels.stream()
                            .sorted(Comparator.comparing(Travel::getNumberOfNights))
                            .collect(Collectors.toList());
                    break;
                default:
                    break;
            }
        }

        return travels;
    }


    private List<Travel> getPromotionalTravels(List<Travel> travels) {
        return travels.stream()
            .filter(travel -> travel.getDiscountEndDate() != null && travel.getDiscountEndDate().isAfter(LocalDateTime.now()))
            .limit(NUM_TRAVELS_TO_DISPLAY)
            .collect(Collectors.toList());
    }

    private List<Travel> getSeasonalTravels(List<Travel> travels) {
        LocalDateTime now = LocalDateTime.now();
        TravelCategoryEnum currentSeason = getCurrentSeason(now);
        return travels.stream()
            .filter(travel -> travel.getTravelCategory().getCategoryName() == currentSeason)
            .filter(travel -> travel.getDiscountEndDate() != null && travel.getDiscountEndDate().isAfter(now))
            .limit(NUM_TRAVELS_TO_DISPLAY)
            .collect(Collectors.toList());
    }

    private TravelCategoryEnum getCurrentSeason(LocalDateTime date) {
        if (isSummer(date)) {
            return TravelCategoryEnum.SUMMER_VACATION;
        } else if (isWinter(date)) {
            return TravelCategoryEnum.SKIING;
        } else {
            return null; 
        }
    }

    private boolean isSummer(LocalDateTime date) {
        return date.getMonthValue() >= 6 && date.getMonthValue() <= 8;
    }

    private boolean isWinter(LocalDateTime date) {
        return date.getMonthValue() == 12 || date.getMonthValue() == 1 || date.getMonthValue() == 2;
    }




    @GetMapping("/travels")
    public ModelAndView travelsPage(@RequestParam(required = false) String page, HttpServletResponse response, HttpSession session) throws IOException {
        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null || loggedUser.getRole() != UserRole.MANAGER) {
            response.sendRedirect(bURL);
        }
        
        travelService.updateAllTravelPrices(); 

        List<Travel> travels = travelService.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        travels.forEach(travel -> {
            travel.setFormattedDepartureDateTime(travel.getDepartureDateTime().format(formatter));
            travel.setFormattedReturnDateTime(travel.getReturnDateTime().format(formatter));
        });

        ModelAndView result = new ModelAndView("travels");
        result.addObject("travels", travels);
        result.addObject("travelCategory", TravelCategoryEnum.values());
        result.addObject("loggedUser", loggedUser);

        return result;
    }

    @GetMapping(value = "/travels/editTravel")
    public ModelAndView showEditTravelPage(@RequestParam Long id, HttpSession session, HttpServletResponse response) throws IOException {
        User loggedUser = (User) session.getAttribute(USER_KEY);
        if (loggedUser == null || loggedUser.getRole() != UserRole.MANAGER) {
            response.sendRedirect(bURL);
            return null;
        }

        Travel travel = travelService.findOne(id);
        if (travel == null) {
            response.sendRedirect(bURL + "error");
            return null;
        }

        ModelAndView modelAndView = new ModelAndView("editTravel");
        modelAndView.addObject("loggedUser",loggedUser);
        modelAndView.addObject("travel", travel);
        modelAndView.addObject("categories", TravelCategoryEnum.values());

        return modelAndView;
    }

    @PostMapping(value = "/travels/edit")
    public void edit(
            @RequestParam Long id,
            @RequestParam String transportationType,
            @RequestParam String accommodationType,
            @RequestParam String destinationName,
            @RequestParam(required = false) MultipartFile locationImage,
            @RequestParam String travelCategory,
            @RequestParam String departureDateTime,
            @RequestParam String returnDateTime,
            @RequestParam double originalPrice,
            @RequestParam int totalSeats,
            @RequestParam int availableSeats,
            @RequestParam(value = "discountPercentage", required = false, defaultValue = "0.0") String discountPercentageStr,
            @RequestParam(value = "discountEndDate", required = false) String discountEndDateStr,
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        travel.setTransportationType(TransportationType.valueOf(transportationType.toUpperCase()));
        travel.setAccommodationType(TypeOfAccommodation.valueOf(accommodationType.toUpperCase()));
        travel.setDestinationName(destinationName);

        if (locationImage != null && !locationImage.isEmpty()) {
            String imageFileName = locationImage.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIRECTORY + imageFileName);
            Files.createDirectories(path.getParent());
            Files.write(path, locationImage.getBytes());
            travel.setLocationImage(imageFileName);
        }

        TravelCategoryEnum categoryEnum = TravelCategoryEnum.valueOf(travelCategory.toUpperCase());
        TravelCategory travelCategoryMain = travelCategoryService.findByCategoryName(categoryEnum);

        travel.setTravelCategory(travelCategoryMain);
        travel.setDepartureDateTime(LocalDateTime.parse(departureDateTime, formatter));
        travel.setReturnDateTime(LocalDateTime.parse(returnDateTime, formatter));
        int numberOfNights = (int) ChronoUnit.DAYS.between(travel.getDepartureDateTime(), travel.getReturnDateTime());
        travel.setNumberOfNights(numberOfNights);

        
        double arrangmentPrice = originalPrice;
        if (discountPercentageStr != null && !discountPercentageStr.isEmpty()) {
            double discountPercentage = Double.parseDouble(discountPercentageStr);
            if (discountEndDateStr != null && !discountEndDateStr.isEmpty()) {
                LocalDateTime discountEndDate = LocalDateTime.parse(discountEndDateStr, formatter);
                if (discountPercentage > 0 && discountEndDate.isAfter(LocalDateTime.now())) {
                    arrangmentPrice = originalPrice - (originalPrice * discountPercentage / 100);
                }
            }
        }

        travel.setOriginalPrice(originalPrice); 
        travel.setArrangmentPrice(arrangmentPrice); 
        travel.setTotalSeats(totalSeats);
        travel.setAvailableSeats(availableSeats);

        if (discountPercentageStr != null && !discountPercentageStr.isEmpty()) {
            travel.setDiscountPercentage(Double.parseDouble(discountPercentageStr));
        } else {
            travel.setDiscountPercentage(0.0);
        }

        if (discountEndDateStr != null && !discountEndDateStr.isEmpty()) {
            travel.setDiscountEndDate(LocalDateTime.parse(discountEndDateStr, formatter));
        } else {
            travel.setDiscountEndDate(null);
        }

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
        modelAndView.addObject("loggedUser",loggedUser);
        modelAndView.setViewName("addTravel");
        modelAndView.addObject("categories", TravelCategoryEnum.values());

        return modelAndView;
    }

    @PostMapping(value = "/travels/add")
    public void addTravel(
            @RequestParam String transportationType,
            @RequestParam String accommodationType,
            @RequestParam String destinationName,
            @RequestParam MultipartFile locationImage,
            @RequestParam String travelCategory,
            @RequestParam String departureDateTime,
            @RequestParam String returnDateTime,
            @RequestParam double originalPrice,
            @RequestParam int totalSeats,
            @RequestParam int availableSeats,
            @RequestParam(value = "discountPercentage", required = false) String discountPercentageStr,
            @RequestParam(value = "discountEndDate", required = false) String discountEndDateStr,
            HttpServletResponse response,
            HttpSession session) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null || loggedUser.getRole() != UserRole.MANAGER) {
            response.sendRedirect(bURL);
            return;
        }

        Travel travel = new Travel();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        travel.setTransportationType(TransportationType.valueOf(transportationType.toUpperCase()));
        travel.setAccommodationType(TypeOfAccommodation.valueOf(accommodationType.toUpperCase()));
        travel.setDestinationName(destinationName);

        if (locationImage != null && !locationImage.isEmpty()) {
            String imageFileName = locationImage.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIRECTORY + imageFileName);
            Files.createDirectories(path.getParent());
            Files.write(path, locationImage.getBytes());
            travel.setLocationImage(imageFileName);
        }

        TravelCategoryEnum categoryEnum = TravelCategoryEnum.valueOf(travelCategory.toUpperCase());
        TravelCategory travelCategoryMain = travelCategoryService.findByCategoryName(categoryEnum);

        travel.setTravelCategory(travelCategoryMain);
        travel.setDepartureDateTime(LocalDateTime.parse(departureDateTime, formatter));
        travel.setReturnDateTime(LocalDateTime.parse(returnDateTime, formatter));
        int numberOfNights = (int) ChronoUnit.DAYS.between(travel.getDepartureDateTime(), travel.getReturnDateTime());
        travel.setNumberOfNights(numberOfNights);

        
        double arrangmentPrice = originalPrice;
        if (discountPercentageStr != null && !discountPercentageStr.isEmpty()) {
            double discountPercentage = Double.parseDouble(discountPercentageStr);
            LocalDateTime discountEndDate = LocalDateTime.parse(discountEndDateStr, formatter);
            if (discountPercentage > 0 && discountEndDate.isAfter(LocalDateTime.now())) {
                arrangmentPrice = originalPrice - (originalPrice * discountPercentage / 100);
            }
        }

        travel.setOriginalPrice(originalPrice);
        travel.setArrangmentPrice(arrangmentPrice); 
        travel.setTotalSeats(totalSeats);
        travel.setAvailableSeats(availableSeats);

        if (discountPercentageStr != null && !discountPercentageStr.isEmpty()) {
            travel.setDiscountPercentage(Double.parseDouble(discountPercentageStr));
        } else {
            travel.setDiscountPercentage(0.0);
        }

        if (discountEndDateStr != null && !discountEndDateStr.isEmpty()) {
            travel.setDiscountEndDate(LocalDateTime.parse(discountEndDateStr, formatter));
        } else {
            travel.setDiscountEndDate(null);
        }

        travelService.save(travel);

        response.sendRedirect(bURL + "travels");
    }

    
    @GetMapping("/travels/details")
    public ModelAndView viewTravelDetails(@RequestParam Long travelId, HttpServletResponse response, HttpSession session) throws IOException {
        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null) {
            response.sendRedirect(bURL + "users/login");
            return null;
        }

        Travel travel = travelService.findOne(travelId);
        if (travel == null) {
            response.sendRedirect(bURL + "error");
            return null;
        }

        checkAndUpdatePrice(travel);

        ModelAndView result = new ModelAndView("travel");
        result.addObject("travel", travel);
        result.addObject("loggedUser", loggedUser);

        return result;
    }

    @PostMapping(value = "/travels/delete")
    public void delete(@RequestParam Long id, HttpServletResponse response, HttpSession session) throws IOException {
        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null || loggedUser.getRole() != UserRole.MANAGER) {
            response.sendRedirect(bURL);
            return;
        }

        boolean hasReservations = travelService.hasReservations(id);
        if (hasReservations) {
            response.sendRedirect(bURL + "travels?error=Travel%20has%20reservations%20and%20cannot%20be%20deleted");
        } else {
            travelService.delete(id);
            response.sendRedirect(bURL + "travels");
        }
    }

    private void checkAndUpdatePrice(Travel travel) {
        if (travel.getDiscountEndDate() != null && travel.getDiscountEndDate().isAfter(LocalDateTime.now())) {
            double discount = travel.getOriginalPrice() * (travel.getDiscountPercentage() / 100);
            double newPrice = travel.getOriginalPrice() - discount;
            if (travel.getArrangmentPrice() != newPrice) {
                travel.setArrangmentPrice(newPrice);
                travelService.updatePrice(travel.getId(), newPrice);
            }
        } else if (travel.getDiscountEndDate() != null && travel.getDiscountEndDate().isBefore(LocalDateTime.now())) {
            
            travel.setDiscountPercentage(0.0);
            travel.setDiscountEndDate(null);
            travel.setArrangmentPrice(travel.getOriginalPrice());
            travelService.update(travel); 
        } else if (travel.getArrangmentPrice() != travel.getOriginalPrice()) {
            travel.setArrangmentPrice(travel.getOriginalPrice());
            travelService.updatePrice(travel.getId(), travel.getOriginalPrice());
        }
    }
}
