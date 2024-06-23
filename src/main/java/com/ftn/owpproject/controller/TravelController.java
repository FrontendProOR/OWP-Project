package com.ftn.owpproject.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
    public ModelAndView indexPage(@RequestParam(required = false) String page, HttpServletResponse response, HttpSession session) throws IOException {
        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

//        List<Travel> travels = travelService.findAll();
        List<Travel> travels = travelService.findAll().stream()
                .filter(travel -> ChronoUnit.HOURS.between(LocalDateTime.now(), travel.getDepartureDateTime()) > 48)
                .collect(Collectors.toList());
        
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
        // Formatiranje datuma pre slanja na front
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
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

    @GetMapping("/addtravel")
    public String addTravelForm(Model model) {
        model.addAttribute("travel", new Travel());
        return "addTravel";
    }

    @PostMapping("/travels/add")
    public String addTravel(
            @RequestParam("transportationType") TransportationType transportationType,
            @RequestParam("accommodationType") TypeOfAccommodation accommodationType,
            @RequestParam("destinationName") String destinationName,
            @RequestParam("locationImage") MultipartFile locationImage,
            @RequestParam("travelCategory") String travelCategoryName,
            @RequestParam("departureDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDateTime,
            @RequestParam("returnDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime returnDateTime,
            @RequestParam("arrangmentPrice") double arrangmentPrice,
            @RequestParam("totalSeats") int totalSeats,
            @RequestParam("availableSeats") int availableSeats,
            @RequestParam(value = "discountPercentage", required = false) Double discountPercentage,
            @RequestParam(value = "discountEndDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime discountEndDate) {

        // Čuvanje slike
        String imageFileName = locationImage.getOriginalFilename();
        try {
            Path path = Paths.get(UPLOAD_DIRECTORY + imageFileName);
            Files.createDirectories(path.getParent());
            Files.write(path, locationImage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/error?message=Image upload failed";
        }

        // Pronalazak kategorije putovanja
        Long travelCategoryId = travelCategoryService.getIdByName(travelCategoryName);
        if (travelCategoryId == null) {
            return "redirect:/error?message=Travel category not found";
        }

        // Kreiranje Travel objekta i čuvanje u bazi
        Travel travel = new Travel(null, transportationType, accommodationType, destinationName, imageFileName,
                travelCategoryService.findOne(travelCategoryId), departureDateTime, returnDateTime, discountEndDate,
                (int) ChronoUnit.DAYS.between(departureDateTime, returnDateTime), arrangmentPrice, totalSeats,
                availableSeats, discountPercentage != null ? discountPercentage : 0.0, null, null, null);

        travelService.save(travel);

        return "redirect:/travels";
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
        modelAndView.addObject("travel", travel);
        modelAndView.addObject("categories", TravelCategoryEnum.values());

        return modelAndView;
    }

    
//    @PostMapping(value = "/travels/edit")
//    public void edit(
//            @RequestParam Long id,
//            @RequestParam String transportationType,
//            @RequestParam String accommodationType,
//            @RequestParam String destinationName,
//            @RequestParam String locationImage,
//            @RequestParam String travelCategory,
//            @RequestParam String departureDateTime,
//            @RequestParam String returnDateTime,
//            @RequestParam double arrangmentPrice,
//            @RequestParam int totalSeats,
//            @RequestParam int availableSeats,
//            @RequestParam(value = "discountPercentage", required = false) String discountPercentageStr,
//            @RequestParam(value = "discountEndDate", required = false) String discountEndDateStr,
//            HttpServletResponse response,
//            HttpSession session) throws IOException {
//
//        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
//
//        if (loggedUser == null || loggedUser.getRole() != UserRole.MANAGER) {
//            response.sendRedirect(bURL);
//            return;
//        }
//
//        Travel travel = travelService.findOne(id);
//
//        if (travel == null) {
//            response.sendRedirect(bURL);
//            return;
//        }
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
//
//        travel.setTransportationType(TransportationType.valueOf(transportationType.toUpperCase()));
//        travel.setAccommodationType(TypeOfAccommodation.valueOf(accommodationType.toUpperCase()));
//        travel.setDestinationName(destinationName);
//        travel.setLocationImage(locationImage);
//
//        TravelCategoryEnum categoryEnum = TravelCategoryEnum.valueOf(travelCategory.toUpperCase());
//        TravelCategory travelCategoryMain = travelCategoryService.findByCategoryName(categoryEnum);
//
//        travel.setTravelCategory(travelCategoryMain);
//        travel.setDepartureDateTime(LocalDateTime.parse(departureDateTime, formatter));
//        travel.setReturnDateTime(LocalDateTime.parse(returnDateTime, formatter));
//        int numberOfNights = (int) ChronoUnit.DAYS.between(travel.getDepartureDateTime(), travel.getReturnDateTime());
//        travel.setNumberOfNights(numberOfNights);
//        travel.setArrangmentPrice(arrangmentPrice);
//        travel.setTotalSeats(totalSeats);
//        travel.setAvailableSeats(availableSeats);
//
//        // Provera i postavljanje vrednosti za discountPercentage
//        if (discountPercentageStr != null && !discountPercentageStr.isEmpty()) {
//            travel.setDiscountPercentage(Double.parseDouble(discountPercentageStr));
//        } else {
//            travel.setDiscountPercentage(0.0);
//        }
//
//        // Provera i postavljanje vrednosti za discountEndDate
//        if (discountEndDateStr != null && !discountEndDateStr.isEmpty()) {
//            travel.setDiscountEndDate(LocalDateTime.parse(discountEndDateStr, formatter));
//        } else {
//            travel.setDiscountEndDate(null);
//        }
//
//        travelService.update(travel);
//
//        response.sendRedirect(bURL + "travels");
//    }

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
            @RequestParam double arrangmentPrice,
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

        Travel travel = travelService.findOne(id);

        if (travel == null) {
            response.sendRedirect(bURL);
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");

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
        travel.setArrangmentPrice(arrangmentPrice);
        travel.setTotalSeats(totalSeats);
        travel.setAvailableSeats(availableSeats);

        // Provera i postavljanje vrednosti za discountPercentage
        if (discountPercentageStr != null && !discountPercentageStr.isEmpty()) {
            travel.setDiscountPercentage(Double.parseDouble(discountPercentageStr));
        } else {
            travel.setDiscountPercentage(0.0);
        }

        // Provera i postavljanje vrednosti za discountEndDate
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

        modelAndView.setViewName("addTravel");
        modelAndView.addObject("categories", TravelCategoryEnum.values());

        return modelAndView;
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
}
