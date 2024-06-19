package com.ftn.owpproject.controller;

import com.ftn.owpproject.model.Reservation;
import com.ftn.owpproject.model.Travel;
import com.ftn.owpproject.model.User;
import com.ftn.owpproject.service.ReservationService;
import com.ftn.owpproject.service.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TravelService travelService;
    
    @GetMapping
    public String getUserReservations(HttpSession session, Model model,
                                      @RequestParam(value = "filter", required = false) String filter) {
        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null) {
            return "redirect:/users/login";
        }

        List<Reservation> reservations = reservationService.findByUserId(loggedUser.getId());

        if ("future".equals(filter)) {
            reservations = reservations.stream()
                    .filter(r -> r.getTravel().getDepartureDateTime().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
        } else if ("past".equals(filter)) {
            reservations = reservations.stream()
                    .filter(r -> r.getTravel().getDepartureDateTime().isBefore(LocalDateTime.now()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("reservations", reservations);
        return "reservations";
    }

    @GetMapping("/make")
    public String showMakeReservationForm(@RequestParam Long travelId, Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null) {
            return "redirect:/users/login";
        }

        Travel travel = travelService.findOne(travelId);
        model.addAttribute("travel", travel);
        return "makeReservation";
    }

    @PostMapping("/make")
    public String makeReservation(@RequestParam Long travelId, @RequestParam int reservedSeats, HttpSession session) {
        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null) {
            return "redirect:/users/login";
        }

        Travel travel = travelService.findOne(travelId);
        if (travel == null || travel.getAvailableSeats() < reservedSeats) {
            return "redirect:/error"; // Or some error handling logic
        }

        // Ažuriranje broja dostupnih mesta
        int newAvailableSeats = travel.getAvailableSeats() - reservedSeats;
        travelService.updateAvailableSeats(travelId, newAvailableSeats);

        Reservation reservation = new Reservation(loggedUser.getId(), travel, LocalDateTime.now(), reservedSeats);
        reservationService.save(reservation);

        return "redirect:/reservations";
    }


    
    @PostMapping("/cancel")
    public String cancelReservation(@RequestParam Long reservationId, HttpSession session) {
        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null) {
            return "redirect:/users/login";
        }

        try {
            reservationService.cancelReservation(reservationId);
        } catch (Exception e) {
            // Handle the exception (e.g., add an error message to the model)
        }

        return "redirect:/reservations";
    }
}
