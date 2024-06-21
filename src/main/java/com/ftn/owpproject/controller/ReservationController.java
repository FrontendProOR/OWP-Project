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

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TravelService travelService;

    @GetMapping
    public String getUserReservations(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null) {
            return "redirect:/users/login";
        }

        List<Reservation> reservations = reservationService.findByUserId(loggedUser.getId());
        model.addAttribute("reservations", reservations);
        model.addAttribute("user", loggedUser); // Ensure user is added to the model

        return "user"; // Make sure this returns to the user.html template
    }
  

    @GetMapping("/make")
    public String showMakeReservationForm(@RequestParam Long travelId, Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null) {
            return "redirect:/users/login";
        }

        if (loggedUser.getRole().toString() == "MANAGER") {
            return "redirect:/error"; // Or show an appropriate message
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

        if (loggedUser.getRole().toString() == "MANAGER") {
            return "redirect:/error"; // Or show an appropriate message
        }

        Travel travel = travelService.findOne(travelId);
        if (travel == null || travel.getAvailableSeats() < reservedSeats) {
            return "redirect:/error"; // Or some error handling logic
        }

        int newAvailableSeats = travel.getAvailableSeats() - reservedSeats;
        travelService.updateAvailableSeats(travelId, newAvailableSeats);

        Reservation reservation = new Reservation(loggedUser.getId(), travel, LocalDateTime.now(), reservedSeats);
        reservationService.save(reservation);

        return "redirect:/";
    }

    @PostMapping("/cancel")
    public String cancelReservation(@RequestParam Long reservationId, HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null) {
            return "redirect:/users/login";
        }

        try {
            Reservation reservation = reservationService.findOne(reservationId);
            if (reservation == null) {
                throw new Exception("Reservation not found");
            }

            Travel travel = reservation.getTravel();
            if (travel.getDepartureDateTime().isBefore(LocalDateTime.now().plusHours(48))) {
                throw new Exception("Cannot cancel reservation less than 48 hours before travel starts");
            }

            reservationService.cancelReservation(reservationId);
            int newAvailableSeats = travel.getAvailableSeats() + reservation.getReservedSeats();
            travelService.updateAvailableSeats(travel.getId(), newAvailableSeats);
            model.addAttribute("message", "Reservation cancelled successfully");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/reservations";
    }
}
